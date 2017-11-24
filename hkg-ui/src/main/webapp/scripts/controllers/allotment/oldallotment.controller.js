define(['hkg', 'customFieldService', 'allotService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'printBarcodeValue', 'dynamicForm', 'datepickercustom.directive', 'accordionCollapse'], function(hkg) {
    hkg.register.controller('OldAllotmentController', ["$rootScope", "$scope", "$timeout", "DynamicFormService", "AllotService", "CustomFieldService", function($rootScope, $scope, $timeout, DynamicFormService, AllotService, CustomFieldService) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "allotMenu";
            $rootScope.activateMenu();
            $scope.stockdataflag = false;
            $scope.stockList = [];
            $scope.currentNodeStocks = undefined;
            $scope.flag = {};
            $scope.forms = {};
            $scope.manualAllocation = 'MA';
            $scope.automaticAllocation = 'AA';
            $scope.typeOfAllocation = 'MA';
            var featureMap = {};
            //Mandatory static fields for lot and packet.
            $scope.stockStaticFields = [];
            $scope.lotStaticFields = [];
            $scope.packetStaticFields = [];

            //Demo data for automatic allocation.
            $scope.suggestionData = {};
            $scope.suggestionParameters = [{id: 'cut', text: 'Cut'}, {id: 'color', text: 'Color'}, {id: 'carat', text: 'Carat'}, {id: 'clarity', text: 'Clarity'}];
            $scope.suggestionData.parameter = 'cut';
            $scope.suggestionList = [];

            $scope.initializeData = function() {
                $scope.flag = {};
                $scope.flag.showstockPage = false;
                $scope.flag.staticFieldMissing = false;
                $scope.stockList = [];
                $scope.listFilled = false;
                $scope.submitted = false;
                $scope.flag.rowSelectedflag = false;
                $scope.stockLabelListForUiGrid = [];
                $scope.nodeDetailsInfo = [];
                $scope.currentNodeStocks = undefined;
                $scope.gridOptions = {};
                $scope.gridOptions.enableFiltering = true;
                $scope.gridOptions.multiSelect = true;
                $scope.gridOptions.columnDefs = [];
                $scope.gridOptions.data = [];
                $scope.gridOptions.onRegisterApi = function(gridApi) {
                    //set gridApi on scope
                    $scope.gridApi = gridApi;
                    gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                        if ($scope.typeOfAllocation === $scope.manualAllocation) {
                            if ($scope.gridApi.selection.getSelectedRows().length > 0) {
                                $scope.flag.rowSelectedflag = true;
                            } else {
                                $scope.flag.rowSelectedflag = false;
                            }
                        } else {
                            if ($scope.gridApi.selection.getSelectedRows().length === 1) {
                                $scope.flag.rowSelectedflag = true;
                            } else {
                                $scope.flag.rowSelectedflag = false;
                            }
                        }
                    });
                    gridApi.selection.on.rowSelectionChangedBatch($scope, function(rows) {
                        if ($scope.typeOfAllocation === $scope.manualAllocation) {
                            if ($scope.gridApi.selection.getSelectedRows().length > 0) {
                                $scope.flag.rowSelectedflag = true;
                            } else {
                                $scope.flag.rowSelectedflag = false;
                            }
                        } else {
                            if ($scope.gridApi.selection.getSelectedRows().length === 1) {
                                $scope.flag.rowSelectedflag = true;
                            } else {
                                $scope.flag.rowSelectedflag = false;
                            }
                        }
                    });
                };
                $scope.searchedStockList = [];
                $scope.generalSearchTemplate = [];
                $scope.stockLabelListForUiGrid = [];
                var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("allot");
                $scope.modelAndHeaderList = [];
                templateData.then(function(section) {
                    $scope.generalSearchTemplate = section['genralSection'];
                    if ($scope.generalSearchTemplate != null && $scope.generalSearchTemplate.length > 0) {
                        $scope.flag.configSearchFlag = false;
                        for (var i = 0; i < $scope.generalSearchTemplate.length; i++) {
                            var item = $scope.generalSearchTemplate [i];
                            $scope.modelAndHeaderList.push({model: item.model, header: item.label});
                            if (item.fromModel) {
                                featureMap[item.fromModel] = item.featureName;
                                $scope.stockLabelListForUiGrid.push({name: item.fromModel, displayName: item.label, minWidth: 200});
                            } else if (item.toModel) {
                                featureMap[item.toModel] = item.featureName;
                                $scope.stockLabelListForUiGrid.push({name: item.toModel, displayName: item.label, minWidth: 200});
                            } else if (item.model) {
                                featureMap[item.model] = item.featureName;
                                $scope.stockLabelListForUiGrid.push({name: item.model, displayName: item.label, minWidth: 200});
                            }
                        }
                        var finalMap = {};
                        if (featureMap != null) {
                            angular.forEach(featureMap, function(value, key) {
//                            alert(JSON.stringify(value));
                                if (!finalMap[value]) {
                                    finalMap[value] = [];
                                }
                                finalMap[value].push(key);
                            });
                        }
                        $rootScope.maskLoading();
                        AllotService.retrieveSearchedLotsAndPackets(finalMap, function(result) {
                            $scope.dataRetrieved = true;
                            $scope.nodeDetailsInfo = [];
                            $scope.flag.multipleIdInvolved = false;
                            if (result.dynamicServiceInitBean !== null && result.dynamicServiceInitBean !== undefined) {
                                var dynamicServiceInitBean = result.dynamicServiceInitBean;
                                if (dynamicServiceInitBean.nodeAndWorkAllocationIds !== null) {
                                    $scope.nodeIdAndWorkAllocationIdsMap = angular.copy(dynamicServiceInitBean.nodeAndWorkAllocationIds);
                                }
                                if (dynamicServiceInitBean.mandatoryFields !== null) {
                                    $scope.stockStaticFields = dynamicServiceInitBean.mandatoryFields;
                                }
                                if (dynamicServiceInitBean.diamondsInQueue !== null) {
                                    $scope.diamondsInQueue = dynamicServiceInitBean.diamondsInQueue;
                                }
                                if (!!dynamicServiceInitBean.dynamicServiceInitDataBeans) {
                                    var dynamicServiceInitDataBeans = dynamicServiceInitBean.dynamicServiceInitDataBeans;
                                    if (angular.isArray(dynamicServiceInitDataBeans) && dynamicServiceInitDataBeans.length > 0) {
                                        angular.forEach(dynamicServiceInitDataBeans, function(item) {
                                            var data = {};
                                            data.groupId = item.groupId;
                                            data.groupName = item.groupName;
                                            data.modifier = item.modifier;
                                            data.nodeId = item.nodeId;
                                            data.nodeName = item.nodeName;
                                            $scope.nodeDetailsInfo.push(data);
                                        });
                                    }
                                    if ($scope.nodeDetailsInfo.length > 1) {
                                        $scope.flag.multipleIdInvolved = true;
                                    } else {
                                        $scope.flag.multipleIdInvolved = false;
                                    }
                                }
                                if ($scope.nodeDetailsInfo.length > 0) {
                                    $scope.currentActivityNode = $scope.nodeDetailsInfo[0].nodeId;
                                }
                            }
//                            console.log(JSON.stringify($scope.nodeDetailsInfo));
                            if (result.stockList !== null && result.stockList !== undefined && result.stockList.length > 0) {
                                $scope.searchedStockList = result.stockList;
                                var success = function(result)
                                {
                                    angular.forEach($scope.searchedStockList, function(itr) {
                                        angular.forEach($scope.stockLabelListForUiGrid, function(list) {
                                            if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                                itr.categoryCustom[list.name] = "NA";
                                            }
                                            else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                                if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                    itr.categoryCustom[list.name] = "NA";
                                                }
                                            }
                                            if (itr.hasOwnProperty("value")) {
                                                itr.categoryCustom["~@workallotmentid"] = itr.value;
                                            }
                                            if (itr.hasOwnProperty("label")) {
                                                itr.categoryCustom["~@parcelid"] = itr.label;
                                            }
                                            if (itr.hasOwnProperty("description")) {
                                                itr.categoryCustom["~@invoiceid"] = itr.description;
                                            }
                                            if (itr.hasOwnProperty("id")) {
                                                itr.categoryCustom["~@lotid"] = itr.id;
                                            }
                                            if (itr.hasOwnProperty("status")) {
                                                itr.categoryCustom["~@packetid"] = itr.status;
                                            }
                                        });
                                        $scope.stockList.push(itr.categoryCustom);
                                    });
                                    //Filter records according to current node and display in table.
                                    $scope.updateStockAccordingToNode($scope.currentActivityNode);
                                    $rootScope.unMaskLoading();
                                };
                                DynamicFormService.convertorForCustomField($scope.searchedStockList, success);
                            } else {
                                $scope.currentNodeStocks = [];
                                $rootScope.unMaskLoading();
                            }
                        }, function() {
                            $rootScope.unMaskLoading();
                        });

                    } else {
                        $scope.flag.configSearchFlag = true;
                    }
                }, function(reason) {

                }, function(update) {

                });

            };

            $scope.updateStockAccordingToNode = function(nodeId) {
                if (nodeId !== null && nodeId !== undefined) {
                    $scope.currentActivityNode = nodeId;
                    angular.forEach($scope.nodeDetailsInfo, function(item) {
                        if (item.nodeId === nodeId) {
                            var modifier = item.modifier;
                            if (modifier !== null) {
                                if (modifier.indexOf("|") > -1) {
                                    var modifiers = modifier.split("|");
                                    for (var i = 0; i < modifiers.length; i++) {
                                        if (modifiers[i] === 'AA' || modifiers[i] === 'MA') {
                                            $scope.typeOfAllocation = modifiers[i];
                                        }
                                        if (modifiers[i] === 'Q') {
                                            $scope.isQuingRequired = true;
                                        }
                                    }
                                } else {
                                    if (modifier === 'AA' || modifier === 'MA') {
                                        $scope.typeOfAllocation = modifier;
                                    }
                                }
                            }
                        }
                    });
                    if (angular.isDefined($scope.nodeIdAndWorkAllocationIdsMap)) {
                        var result = [];
                        var workAllocationIds = $scope.nodeIdAndWorkAllocationIdsMap[nodeId];
                        angular.forEach($scope.stockList, function(item, index) {
                            if (workAllocationIds.length > 0 && workAllocationIds.indexOf(item["~@workallotmentid"]) !== -1) {
                                item["~@index"] = index + 1;
                                result.push(item);
                            }
                        });
                        $scope.currentNodeStocks = angular.copy(result);
                        if (angular.isDefined($scope.gridApi)) {
                            $scope.gridApi.selection.clearSelectedRows();
                        }
                        $scope.gridOptions.data = result;
                        $scope.gridOptions.columnDefs = $scope.stockLabelListForUiGrid;
                        $scope.listFilled = true;
                        if (!!$scope.isQuingRequired) {
                            $scope.gridOptions.enableSorting = false;
                            $scope.gridOptions.isRowSelectable = function(row) {
                                if (!!$scope.diamondsInQueue) {
                                    if (row.entity["~@index"] > $scope.diamondsInQueue) {
                                        return false;
                                    } else {
                                        return true;
                                    }
                                } else {
                                    if (row.entity["~@index"] > 0) {
                                        return true;
                                    } else {
                                        return false;
                                    }
                                }
                            };
                        }
                    } else {
                        console.log("Node map not initialized");
                    }

                    AllotService.retrieveNextNodeDesignationIds($scope.currentActivityNode, function(response) {
//                        console.log('result----' + JSON.stringify(response));
                        $scope.designationIdForAllot = undefined;
                        $scope.designationIdForInStock = undefined;
                        if (response.data !== null) {
                            if (response.data['forAllotTo'] !== undefined && response.data['forAllotTo'] !== null) {
                                $scope.designationIdForAllot = response.data['forAllotTo'].toString();
                            }
                            if (response.data['forIssue'] !== undefined && response.data['forIssue'] !== null) {
                                $scope.designationIdForInStock = response.data['forIssue'].toString();
                            }
                        }
//                        console.log('$scope.nextNodeDesignationIds----' + JSON.stringify($scope.nextNodeDesignationIds));
                    }, function(res) {
                        console.log('failure----');
                    });
                }
            };

            $scope.updateAllotTo = function(userId) {
                if (userId !== null) {
                    if ($scope.flag.type === 'Lot') {
                        $scope.flag.displayEditLotflag = false;
                        $scope.lotEditCustom['allot_to_lot$UMS$String'] = userId + ':E';
                        $timeout(function() {
                            $scope.flag.displayEditLotflag = true;
                        }, 100);
                    } else {
                        $scope.flag.displayEditPacketflag = false;
                        $scope.packetEditCustom['allot_to_packet$UMS$String'] = userId + ':E';
                        $timeout(function() {
                            $scope.flag.displayEditPacketflag = true;
                        }, 100);
                    }
                }
            };

            $scope.setToPrintData = function(data) {
                if (!!data) {
                    $scope.toPrint = data;
                }
            };

            $scope.onBack = function() {
                $scope.stockdataflag = false;
                $scope.initializeData();
                delete $scope.toPrint;
            };

            $scope.onCanelOfSearch = function() {
                delete $scope.toPrint;
                $scope.gridApi.selection.clearSelectedRows();
            };
            $scope.allotStockNext = function() {
                $scope.selectedRows = $scope.gridApi.selection.getSelectedRows();
                $scope.noOfLots = 0;
                $scope.noOfPackets = 0;
                for (var row = 0; row < $scope.selectedRows.length; row++) {
                    if ($scope.selectedRows[row]["~@packetid"] != null) {
                        $scope.noOfPackets = $scope.noOfPackets + 1;
                    }
                    else if ($scope.selectedRows[row]["~@packetid"] === null && $scope.selectedRows[row]["~@lotid"] != null) {
                        $scope.noOfLots = $scope.noOfLots + 1;
                    }
                    if ($scope.noOfLots > 0 && $scope.noOfPackets > 0) {
                        break;
                    }
                }
                if ($scope.noOfLots > 0 && $scope.noOfPackets > 0) {
                    delete $scope.selectedRows;
                    $scope.selectedRows = [];
                    $scope.gridApi.selection.clearSelectedRows();
                    var msg = "Select either lots or packets";
                    var type = $rootScope.warning;
                    $rootScope.addMessage(msg, type);
                }
                else if ($scope.noOfLots > 0 && $scope.noOfPackets === 0) {
                    $scope.flag.type = "Lot";
                }
                else if ($scope.noOfPackets > 0 && $scope.noOfLots === 0) {
                    $scope.flag.type = "Packet";
                }
                if (($scope.noOfLots > 0 && $scope.noOfPackets === 0) || ($scope.noOfPackets > 0 && $scope.noOfLots === 0)) {
                    $scope.allotmentIds = [];
                    $scope.stockIds = [];
                    for (var row = 0; row < $scope.selectedRows.length; row++) {
                        if ($scope.noOfLots > 0) {
                            $scope.stockIds.push($scope.selectedRows[row]["~@lotid"]);
                        } else if ($scope.noOfPackets > 0) {
                            $scope.stockIds.push($scope.selectedRows[row]["~@packetid"]);
                        }
                        $scope.allotmentIds.push($scope.selectedRows[row]["~@workallotmentid"]);
                    }

                    if ($scope.selectedRows.length >= 1) {
                        $scope.selectedWorkAllotmentIds = [];
                        if ($scope.noOfLots > 0) {
                            for (var j = 0; j < $scope.selectedRows.length; j++) {
                                angular.forEach($scope.searchedStockList, function(itr) {

                                    if (itr.categoryCustom["~@lotid"] === $scope.selectedRows[j]["~@lotid"]) {
                                        $scope.selectedWorkAllotmentIds.push(itr.categoryCustom["~@workallotmentid"]);
                                    }
                                });
                            }
                        } else if ($scope.noOfPackets > 0) {
                            for (var j = 0; j < $scope.selectedRows.length; j++) {
                                angular.forEach($scope.searchedStockList, function(itr) {
                                    if (itr.categoryCustom["~@packetid"] === $scope.selectedRows[j]["~@packetid"]) {
                                        $scope.selectedWorkAllotmentIds.push(itr.categoryCustom["~@workallotmentid"]);
                                    }
                                });
                            }
                        }
                        if ($scope.selectedRows.length === 1) {
                            $scope.flag.multipleParents = false;
                        } else {
                            $scope.flag.multipleParents = true;
                        }
                        $scope.flag.showParent = true;
                    } else {
                        $scope.flag.showParent = false;
                    }


                    //Retrieve allocation suggestion in case of automatic allocation.
                    if ($scope.typeOfAllocation === $scope.automaticAllocation) {
                        var data = {};
                        data.allocationPropertyName = $scope.suggestionData.parameter;
                        data.currentActivityNode = $scope.currentActivityNode;
                        AllotService.retrieveAllotmentSuggestion(data, function(res) {
                            $scope.suggestionList = angular.copy(res);
                            angular.forEach($scope.suggestionList, function(item) {
                                item.isSelected = false;
                            });
                        });
                    }

                    if ($scope.flag.showParent) {
                        var dataToSend = {};
                        dataToSend.workAllotmentId = $scope.selectedWorkAllotmentIds;
                        if ($scope.flag.type === "Lot") {
                            dataToSend.isPacket = false;
                        } else {
                            dataToSend.isPacket = true;
                        }
                        $rootScope.maskLoading();
//                        console.log('data to send---'+JSON.stringify(dataToSend));
                        AllotService.retrieveLotOrPacketByAllotmentId(dataToSend, function(response) {
                            $scope.parentGridOptions = {};
                            $scope.parentGridOptions.columnDefs = [];
                            $scope.parentGridOptions.data = [];
//                            console.log(JSON.stringify(response));
                            $scope.currentStockDetailsList = angular.copy(response.data);
                            if (!$scope.flag.multipleParents && $scope.currentStockDetailsList.custom2 !== null && $scope.currentStockDetailsList.custom2 !== undefined) {
                                $scope.currentStockDetails = angular.copy($scope.currentStockDetailsList.custom2[0]);
                            }
//                            $scope.currentStockDetails = angular.copy(response.data);
                            CustomFieldService.retrieveDesignationBasedFields("allot", function(response) {
                                var templateDataInvoice = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                                templateDataInvoice.then(function(section) {
                                    $scope.generalParentInvoiceTemplate = section['genralSection'];
                                    var parentInvoiceFields = [];
                                    var result = Object.keys(response).map(function(key, value) {
                                        angular.forEach(this[key], function(itr) {
                                            if (key === 'Invoice#P#') {
                                                parentInvoiceFields.push({Invoice: itr});
                                            }
                                        });
                                    }, response);
                                    $scope.generalParentInvoiceTemplate = DynamicFormService.retrieveCustomData($scope.generalParentInvoiceTemplate, parentInvoiceFields);
                                    $scope.parentInvoiceCustom = DynamicFormService.resetSection($scope.generalParentInvoiceTemplate);
                                    if (angular.isDefined($scope.currentStockDetails) && !$scope.flag.multipleParents) {
                                        if ($scope.currentStockDetails.custom1 !== undefined && $scope.currentStockDetails.custom1 !== null) {
                                            $scope.parentInvoiceCustom = angular.copy($scope.currentStockDetails.custom1);
                                        }
                                    }
                                    if ($scope.generalParentInvoiceTemplate && $scope.generalParentInvoiceTemplate.length > 0 && $scope.flag.multipleParents) {
                                        for (var i = 0; i < $scope.generalParentInvoiceTemplate.length; i++) {
                                            var item = $scope.generalParentInvoiceTemplate [i];
                                            if (item.fromModel) {
                                                $scope.parentGridOptions.columnDefs.push({name: item.fromModel, displayName: item.label,
                                                    cellTemplate: '<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                                            } else if (item.toModel) {
                                                $scope.parentGridOptions.columnDefs.push({name: item.toModel, displayName: item.label,
                                                    cellTemplate: '<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                                            } else if (item.model) {
                                                $scope.parentGridOptions.columnDefs.push({name: item.model, displayName: item.label,
                                                    cellTemplate: '<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                                            }
                                        }
                                    }
                                }, function(reason) {
                                }, function(update) {
                                });
                                var templateDataParcel = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
                                templateDataParcel.then(function(section) {
                                    $scope.generalParentParcelTemplate = section['genralSection'];
                                    var parentParcelFields = [];
                                    var result = Object.keys(response).map(function(key, value) {
                                        angular.forEach(this[key], function(itr) {
                                            if (key === 'Parcel#P#') {
                                                parentParcelFields.push({Parcel: itr});
                                            }
                                        });
                                    }, response);
                                    $scope.generalParentParcelTemplate = DynamicFormService.retrieveCustomData($scope.generalParentParcelTemplate, parentParcelFields);
                                    $scope.parentParcelCustom = DynamicFormService.resetSection($scope.generalParentParcelTemplate);
                                    if (angular.isDefined($scope.currentStockDetails) && !$scope.flag.multipleParents) {
                                        if ($scope.currentStockDetails.custom3 !== undefined && $scope.currentStockDetails.custom3 !== null) {
                                            $scope.parentParcelCustom = angular.copy($scope.currentStockDetails.custom3);
                                        }
                                    }
                                    if ($scope.generalParentParcelTemplate && $scope.generalParentParcelTemplate.length > 0 && $scope.flag.multipleParents) {
                                        for (var i = 0; i < $scope.generalParentParcelTemplate.length; i++) {
                                            var item = $scope.generalParentParcelTemplate [i];
                                            if (item.fromModel) {
                                                $scope.parentGridOptions.columnDefs.push({name: item.fromModel, displayName: item.label,
                                                    cellTemplate: '<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                                            } else if (item.toModel) {
                                                $scope.parentGridOptions.columnDefs.push({name: item.toModel, displayName: item.label,
                                                    cellTemplate: '<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                                            } else if (item.model) {
                                                $scope.parentGridOptions.columnDefs.push({name: item.model, displayName: item.label,
                                                    cellTemplate: '<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                                            }
                                        }
                                    }
                                }, function(reason) {
                                }, function(update) {
                                });
                                var templateDataLot = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                                templateDataLot.then(function(section) {
                                    $scope.generalParentLotTemplate = section['genralSection'];
                                    var parentLotFields = [];
                                    var result = Object.keys(response).map(function(key, value) {
                                        angular.forEach(this[key], function(itr) {
                                            if (key === 'Lot#P#') {
                                                parentLotFields.push({Lot: itr});
                                            }
                                        });
                                    }, response);
                                    $scope.generalParentLotTemplate = DynamicFormService.retrieveCustomData($scope.generalParentLotTemplate, parentLotFields);
                                    $scope.parentLotCustom = DynamicFormService.resetSection($scope.generalParentLotTemplate);
                                    if (angular.isDefined($scope.currentStockDetails) && !$scope.flag.multipleParents) {
                                        if ($scope.currentStockDetails.custom4 !== undefined && $scope.currentStockDetails.custom4 !== null) {
                                            $scope.parentLotCustom = angular.copy($scope.currentStockDetails.custom4);
                                        }
                                    }
                                    if ($scope.generalParentLotTemplate && $scope.generalParentLotTemplate.length > 0 && $scope.flag.multipleParents) {
                                        for (var i = 0; i < $scope.generalParentLotTemplate.length; i++) {
                                            var item = $scope.generalParentLotTemplate [i];
                                            if (item.fromModel) {
                                                $scope.parentGridOptions.columnDefs.push({name: item.fromModel, displayName: item.label,
                                                    cellTemplate: '<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                                            } else if (item.toModel) {
                                                $scope.parentGridOptions.columnDefs.push({name: item.toModel, displayName: item.label,
                                                    cellTemplate: '<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                                            } else if (item.model) {
                                                $scope.parentGridOptions.columnDefs.push({name: item.model, displayName: item.label,
                                                    cellTemplate: '<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                                            }
                                        }
                                    }
                                }, function(reason) {
                                }, function(update) {
                                });

                                if ($scope.flag.type === 'Packet') {
                                    var templateDataPacket = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                                    templateDataPacket.then(function(section) {
                                        $scope.generalParentPacketTemplate = section['genralSection'];
                                        var parentPacketFields = [];
                                        var result = Object.keys(response).map(function(key, value) {
                                            angular.forEach(this[key], function(itr) {
                                                if (key === 'Packet#P#') {
                                                    parentPacketFields.push({Packet: itr});
                                                }
                                            });
                                        }, response);
                                        $scope.generalParentPacketTemplate = DynamicFormService.retrieveCustomData($scope.generalParentPacketTemplate, parentPacketFields);
                                        $scope.parentPacketCustom = DynamicFormService.resetSection($scope.generalParentPacketTemplate);
                                        if (angular.isDefined($scope.currentStockDetails) && !$scope.flag.multipleParents) {
                                            if ($scope.currentStockDetails.custom5 !== undefined && $scope.currentStockDetails.custom5 !== null) {
                                                $scope.parentPacketCustom = angular.copy($scope.currentStockDetails.custom5);
                                            }
                                        }
                                        if ($scope.generalParentPacketTemplate && $scope.generalParentPacketTemplate.length > 0 && $scope.flag.multipleParents) {
                                            for (var i = 0; i < $scope.generalParentPacketTemplate.length; i++) {
                                                var item = $scope.generalParentPacketTemplate [i];
                                                if (item.fromModel) {
                                                    $scope.parentGridOptions.columnDefs.push({name: item.fromModel, displayName: item.label,
                                                        cellTemplate: '<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                                                } else if (item.toModel) {
                                                    $scope.parentGridOptions.columnDefs.push({name: item.toModel, displayName: item.label,
                                                        cellTemplate: '<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                                                } else if (item.model) {
                                                    $scope.parentGridOptions.columnDefs.push({name: item.model, displayName: item.label,
                                                        cellTemplate: '<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                                                }
                                            }
                                        }
                                    }, function(reason) {
                                    }, function(update) {
                                    });
                                }
                                if ($scope.flag.type === 'Lot') {
                                    var templateDataLotEdit = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                                    $scope.lotEditDbType = {};
                                    templateDataLotEdit.then(function(section) {
                                        $scope.generalLotEditTemplate = section['genralSection'];
                                        var lotFields = [];
                                        var readOnlyFields = [];
                                        var result = Object.keys(response).map(function(key, value) {
                                            angular.forEach(this[key], function(itr) {
                                                if (key === 'Lot') {
                                                    lotFields.push({Lot: itr});
                                                    if (!itr.isEditable) {
                                                        readOnlyFields.push(itr.fieldId);
                                                    }
                                                }
                                            });
                                        }, response);
//                                console.log(JSON.stringify(lotFields));
                                        $scope.generalLotEditTemplate = DynamicFormService.retrieveCustomData($scope.generalLotEditTemplate, lotFields);
                                        $scope.lotEditCustom = DynamicFormService.resetSection($scope.generalLotEditTemplate);
                                        if ($scope.generalLotEditTemplate) {
                                            angular.forEach($scope.stockStaticFields, function(fieldModel) {
                                                if (!$scope.flag.staticFieldMissing) {
                                                    var isFieldExists = false;
                                                    angular.forEach($scope.generalLotEditTemplate, function(field) {
                                                        if (field.model === fieldModel + '_lot' + '$UMS$String') {
                                                            isFieldExists = true;
                                                        }
                                                    });
                                                    if (!isFieldExists) {
                                                        $scope.flag.staticFieldMissing = true;
                                                    } else if (readOnlyFields.length > 0) {
                                                        angular.forEach($scope.generalLotEditTemplate, function(field) {
                                                            if (readOnlyFields.indexOf(field.fieldId) > -1) {
                                                                $scope.flag.staticFieldMissing = true;
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                        $scope.flag.displayEditLotflag = true;
                                    }, function(reason) {
                                    }, function(update) {
                                    });

                                }
                                if ($scope.flag.type === 'Packet') {
                                    var templateDataPacketEdit = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                                    $scope.packetEditDbType = {};
                                    templateDataPacketEdit.then(function(section) {
                                        $scope.generalPacketEditTemplate = section['genralSection'];
                                        var packetFields = [];
                                        var readOnlyFields = [];
                                        var result = Object.keys(response).map(function(key, value) {
                                            angular.forEach(this[key], function(itr) {
                                                if (key === 'Packet') {
                                                    packetFields.push({Packet: itr});
                                                    if (!itr.isEditable) {
                                                        readOnlyFields.push(itr.fieldId);
                                                    }
                                                }
                                            });
                                        }, response);
                                        $scope.generalPacketEditTemplate = DynamicFormService.retrieveCustomData($scope.generalPacketEditTemplate, packetFields);
                                        $scope.packetEditCustom = DynamicFormService.resetSection($scope.generalPacketEditTemplate);
                                        if ($scope.generalPacketEditTemplate) {
                                            angular.forEach($scope.stockStaticFields, function(fieldModel) {
                                                var isFieldExists = false;
                                                angular.forEach($scope.generalPacketEditTemplate, function(field) {
                                                    if (field.model === fieldModel + '_packet$UMS$String') {
                                                        isFieldExists = true;
                                                    }
                                                });
                                                if (!isFieldExists) {
                                                    $scope.flag.staticFieldMissing = true;
                                                } else if (readOnlyFields.length > 0) {
                                                    angular.forEach($scope.generalPacketEditTemplate, function(field) {
                                                        if (readOnlyFields.indexOf(field.fieldId) > -1) {
                                                            $scope.flag.staticFieldMissing = true;
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                        $scope.flag.displayEditPacketflag = true;
                                    }, function(update) {
                                    });

                                }
                                //Populate parent records to ui grid
                                if ($scope.currentStockDetailsList.custom2 !== undefined && $scope.currentStockDetailsList.custom2 !== null && $scope.flag.multipleParents) {
//                                    console.log('$scope.currentStockDetailsList.custom2---' + JSON.stringify($scope.currentStockDetailsList.custom2));
                                    $scope.gridRecords = [];
                                    //add records with values
                                    angular.forEach($scope.currentStockDetailsList.custom2, function(currentStockDetails) {
                                        var data = {};
                                        if (currentStockDetails.custom1 !== undefined && currentStockDetails.custom1 !== null) {
                                            data = angular.copy(currentStockDetails.custom1);
                                        }
                                        if (currentStockDetails.custom3 !== undefined && currentStockDetails.custom3 !== null) {
                                            angular.forEach(currentStockDetails.custom3, function(val, key) {
                                                data[key] = val;
                                            });
                                        }
                                        if (currentStockDetails.custom4 !== undefined && currentStockDetails.custom4 !== null) {
                                            angular.forEach(currentStockDetails.custom4, function(val, key) {
                                                data[key] = val;
                                            });
                                        }
                                        if (currentStockDetails.custom5 !== undefined && currentStockDetails.custom5 !== null) {
                                            angular.forEach(currentStockDetails.custom5, function(val, key) {
                                                data[key] = val;
                                            });
                                        }
                                        $scope.gridRecords.push({categoryCustom: data});

                                    });

                                    var success = function(result) {
                                        angular.forEach($scope.gridRecords, function(item) {
                                            $scope.parentGridOptions.data.push(item.categoryCustom);
                                        });
                                    }
                                    DynamicFormService.convertorForCustomField($scope.gridRecords, success);
                                }
                                $scope.flag.showstockPage = true;
                                $scope.stockdataflag = true;
                                $rootScope.unMaskLoading();
                            }, function() {
                                $rootScope.unMaskLoading();
                                var msg = "Failed to retrieve data";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                            });
                        });
                    } else {
                        $rootScope.maskLoading();
                        CustomFieldService.retrieveDesignationBasedFields("allot", function(response) {

                            if ($scope.flag.type === 'Lot') {
                                var templateDataLotEdit = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                                $scope.lotEditDbType = {};
                                templateDataLotEdit.then(function(section) {
                                    $scope.generalLotEditTemplate = section['genralSection'];
                                    var lotFields = [];
                                    var readOnlyFields = [];
                                    var result = Object.keys(response).map(function(key, value) {
                                        angular.forEach(this[key], function(itr) {
                                            if (key === 'Lot') {
                                                lotFields.push({Lot: itr});
                                                if (!itr.isEditable) {
                                                    readOnlyFields.push(itr.fieldId);
                                                }
                                            }
                                        });
                                    }, response);
                                    $scope.generalLotEditTemplate = DynamicFormService.retrieveCustomData($scope.generalLotEditTemplate, lotFields);
                                    $scope.lotEditCustom = DynamicFormService.resetSection($scope.generalLotEditTemplate);
                                    if ($scope.generalLotEditTemplate) {
                                        angular.forEach($scope.stockStaticFields, function(fieldModel) {
                                            if (!$scope.flag.staticFieldMissing) {
                                                var isFieldExists = false;
                                                angular.forEach($scope.generalLotEditTemplate, function(field) {
                                                    if (field.model === fieldModel + '_lot$UMS$String') {
                                                        isFieldExists = true;
                                                    }
                                                });
                                                if (!isFieldExists) {
                                                    $scope.flag.staticFieldMissing = true;
                                                } else if (readOnlyFields.length > 0) {
                                                    angular.forEach($scope.generalLotEditTemplate, function(field) {
                                                        if (readOnlyFields.indexOf(field.fieldId) > -1) {
                                                            $scope.flag.staticFieldMissing = true;
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                    $scope.flag.displayEditLotflag = true;
                                }, function(reason) {
                                }, function(update) {
                                });
                            }
                            if ($scope.flag.type === 'Packet') {
                                var templateDataPacketEdit = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                                $scope.packetEditDbType = {};
                                templateDataPacketEdit.then(function(section) {
                                    $scope.generalPacketEditTemplate = section['genralSection'];
                                    var packetFields = [];
                                    var readOnlyFields = [];
                                    var result = Object.keys(response).map(function(key, value) {
                                        angular.forEach(this[key], function(itr) {
                                            if (key === 'Packet') {
                                                packetFields.push({Packet: itr});
                                                if (!itr.isEditable) {
                                                    readOnlyFields.push(itr.fieldId);
                                                }
                                            }
                                        });
                                    }, response);
                                    $scope.generalPacketEditTemplate = DynamicFormService.retrieveCustomData($scope.generalPacketEditTemplate, packetFields);
                                    $scope.packetEditCustom = DynamicFormService.resetSection($scope.generalPacketEditTemplate);
                                    if ($scope.generalPacketEditTemplate) {
                                        angular.forEach($scope.stockStaticFields, function(fieldModel) {
                                            var isFieldExists = false;
                                            angular.forEach($scope.generalPacketEditTemplate, function(field) {
                                                if (field.model === fieldModel + '_packet$UMS$String') {
                                                    isFieldExists = true;
                                                }
                                            });
                                            if (!isFieldExists) {
                                                $scope.flag.staticFieldMissing = true;
                                            } else if (readOnlyFields.length > 0) {
                                                angular.forEach($scope.generalPacketEditTemplate, function(field) {
                                                    if (readOnlyFields.indexOf(field.fieldId) > -1) {
                                                        $scope.flag.staticFieldMissing = true;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                    $scope.flag.displayEditPacketflag = true;
                                }, function(update) {
                                });

                            }
                            $scope.flag.showstockPage = true;
                            $scope.stockdataflag = true;
                            $rootScope.unMaskLoading();
                        }, function() {
                            $rootScope.unMaskLoading();
                            var msg = "Failed to retrieve data";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                        });
                    }
                }
            };

            $scope.updateSuggestionList = function() {
                if ($scope.typeOfAllocation === $scope.automaticAllocation) {
                    var data = {};
                    data.allocationPropertyName = $scope.suggestionData.parameter;
                    data.currentActivityNode = $scope.currentActivityNode;
                    AllotService.retrieveAllotmentSuggestion(data, function(res) {
                        $scope.suggestionList = angular.copy(res);
                        angular.forEach($scope.suggestionList, function(item) {
                            item.isSelected = false;
                        });
                    });
                }
            };

            $scope.openAttendance = function(employee) {
                $scope.userDetails = angular.copy(employee);
                $("#attendanceModal").modal('show');
            };

            $scope.hideAttendancePopUp = function() {
                $("#attendanceModal").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };

            $scope.allotStock = function(form) {
                $scope.submitted = true;
                //                alert('Yet to be implemented');
                //               return;
                if (form.$valid) {
                    var allotDataBean = {};
                    allotDataBean.allotmentIds = $scope.allotmentIds;
                    allotDataBean.type = $scope.flag.type;
                    allotDataBean.stockIds = $scope.stockIds;
                    var isMandatoryFilled = true;
                    var missingFields = [];
                    if ($scope.flag.type === 'Lot') {
                        allotDataBean.stockCustom = $scope.lotEditCustom;
                        allotDataBean.stockDbType = $scope.lotEditDbType;
                        if ($scope.stockStaticFields.length > 0) {
                            angular.forEach($scope.stockStaticFields, function(field) {
                                if (allotDataBean.stockCustom[field + "_lot$UMS$String"] === null || allotDataBean.stockCustom[field + "_lot$UMS$String"] === undefined || allotDataBean.stockCustom[field + "_lot$UMS$String"].length === 0) {
                                    isMandatoryFilled = false;
                                    missingFields.push(field + '_lot$UMS$String');
                                }
                            });
                        }
                    } else if ($scope.flag.type === 'Packet') {
                        allotDataBean.stockCustom = $scope.packetEditCustom;
                        allotDataBean.stockDbType = $scope.packetEditDbType;
                        if ($scope.stockStaticFields.length > 0) {
                            angular.forEach($scope.stockStaticFields, function(field) {
                                if (allotDataBean.stockCustom[field + "_packet$UMS$String"] === null || allotDataBean.stockCustom[field + "_packet$UMS$String"] === undefined || allotDataBean.stockCustom[field + "_packet$UMS$String"].length === 0) {
                                    isMandatoryFilled = false;
                                    missingFields.push(field + '_packet$UMS$String');
                                }
                            });
                        }
                    }

                    if (!!allotDataBean.stockIds && isMandatoryFilled) {
                        $rootScope.maskLoading();
                        AllotService.saveAllotment(allotDataBean, function() {
                            $rootScope.unMaskLoading();
                            $scope.onBack();
                            var msg = "Stock alloted successfully";
                            var type = $rootScope.success;
                            $rootScope.addMessage(msg, type);
                        }, function() {
                            $rootScope.unMaskLoading();
                            $scope.onBack();
                            var msg = "Failed to allot stock";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                        });
                    } else {
                        if (!isMandatoryFilled) {
                            var msg = "Mandatory fields can't be left blank";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                        }
                    }
                }
            };
            $rootScope.unMaskLoading();
        }]);
});