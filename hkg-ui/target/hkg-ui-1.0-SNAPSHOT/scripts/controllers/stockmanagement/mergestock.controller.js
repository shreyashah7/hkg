define(['hkg', 'customFieldService', 'mergestockService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'splitstockService', 'printBarcodeValue', 'dynamicForm', 'lotService', 'accordionCollapse'], function(hkg) {
    hkg.register.controller('MergeStockController', ["$rootScope", "$scope", "DynamicFormService", "MergeStockService", "CustomFieldService", "LotService", "SplitStockService", function($rootScope, $scope, DynamicFormService, MergeStockService, CustomFieldService, LotService, SplitStockService) {
            $rootScope.maskLoading();
            $rootScope.activateMenu();
            $scope.stockdataflag = false;
//            $scope.stockList = [];
            $scope.featureMap = {};
            $scope.flag = {};
            $scope.searchCustom = {};
            $scope.initializeData = function() {
                $scope.nodeDetailsInfo = [];
//                var rootNodeRetrieved = function(res) {
//                    $scope.rootNodeDesignationIds = res.data.toString();
//                };
//                LotService.retrieveRootNodeDesignationIds(rootNodeRetrieved);
                $scope.flag = {};
//                $scope.flag.configSearchFlag = false;
                $scope.flag.showstockPage = false;
                $scope.stockList = [];
                $scope.listFilled = false;
                $scope.flag.rowSelectedflag = false;
                $scope.stockLabelListForUiGrid = [];
                $scope.gridOptions = {};
                $scope.gridOptions.enableFiltering = true;
                $scope.gridOptions.multiSelect = true;
                $scope.gridOptions.columnDefs = [];
                $scope.gridOptions.data = [];
                $scope.gridOptions.onRegisterApi = function(gridApi) {
                    //set gridApi on scope
                    $scope.gridApi = gridApi;
                    gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                        if ($scope.gridApi.selection.getSelectedRows().length > 1) {
                            $scope.flag.rowSelectedflag = true;
                        } else {
                            $scope.flag.rowSelectedflag = false;
                        }
                        if ($scope.gridApi.selection.getSelectedRows().length > 1) {
                            var flag = null;
                            for (var index = 0; index < $scope.gridApi.selection.getSelectedRows().length; index++) {
                                var item = $scope.gridApi.selection.getSelectedRows()[index];
                                if (flag === null) {
                                    flag = item["~@haveValue"];
                                } else if (flag !== item["~@haveValue"]) {
                                    $scope.flag.haveValueFlag = false;
                                    break;
                                } else {
                                    $scope.flag.haveValueFlag = true;
                                }
                            }
                        }
                    });
                    gridApi.selection.on.rowSelectionChangedBatch($scope, function(rows) {
                        if ($scope.gridApi.selection.getSelectedRows().length > 1) {
                            $scope.flag.rowSelectedflag = true;
                        } else {
                            $scope.flag.rowSelectedflag = false;
                        }
                        if ($scope.gridApi.selection.getSelectedRows().length > 1) {
                            var flag = null;
                            for (var index = 0; index < $scope.gridApi.selection.getSelectedRows().length; index++) {
                                var item = $scope.gridApi.selection.getSelectedRows()[index];
                                if (flag === null) {
                                    flag = item["~@haveValue"];
                                } else if (flag !== item["~@haveValue"]) {
                                    $scope.flag.haveValueFlag = false;
                                    break;
                                } else {
                                    $scope.flag.haveValueFlag = true;
                                }
                            }
                        }
                    });
                };
                $scope.searchedStockList = [];
                $scope.generalSearchTemplate = [];
                $scope.stockLabelListForUiGrid = [];

                var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("stockmerge");
                templateData.then(function(section) {
                    $scope.generalSearchTemplate = section['genralSection'];

                    if ($scope.generalSearchTemplate !== null && $scope.generalSearchTemplate !== undefined) {
                        for (var i = 0; i < $scope.generalSearchTemplate.length; i++) {
                            var item = $scope.generalSearchTemplate [i];
                            $scope.featureMap[item.model] = item.featureName;
                            if (item.fromModel) {
                                $scope.stockLabelListForUiGrid.push({name: item.fromModel, displayName: item.label, minWidth: 200});
                            } else if (item.toModel) {
                                $scope.stockLabelListForUiGrid.push({name: item.toModel, displayName: item.label, minWidth: 200});
                            } else if (item.model) {
                                $scope.stockLabelListForUiGrid.push({name: item.model, displayName: item.label, minWidth: 200});
                            }

                        }
                    }
                    $scope.searchResetFlag = true;

                    $scope.map = {};
                    var finalMap = {};
                    angular.forEach($scope.featureMap, function(val, label) {
                        if (!finalMap[val]) {
                            finalMap[val] = [];
                        }
                        finalMap[val].push(label);
                    });

                    MergeStockService.retrieveSearchedLotsAndPacketsNew(finalMap, function(result1) {
                        $scope.nodeDetailsInfo = [];
                        $scope.searchedStockList = angular.copy(result1.stockList);
                        if ($scope.generalSearchTemplate === null || $scope.generalSearchTemplate === undefined) {
                            $scope.flag.configSearchFlag = true;
                        } else {
                            if (result1.dynamicServiceInitBean !== null && result1.dynamicServiceInitBean !== undefined) {
                                var dynamicServiceInitBean = result1.dynamicServiceInitBean;
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
                                if ($scope.currentActivityNode !== undefined && $scope.currentActivityNode !== null) {
                                    SplitStockService.retrieveNextNodeDesignationIds($scope.currentActivityNode, function(response) {
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
                                    }, function(res) {
                                        console.log('failure----');
                                    });
                                }
                            }
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
                                        if (itr.hasOwnProperty("lotId")) {
                                            itr.categoryCustom["~@lotNumber"] = itr.lotId;
                                        }
                                        if (itr.hasOwnProperty("parcelId")) {
                                            itr.categoryCustom["~@parcelNumber"] = itr.parcelId;
                                        }
                                        if (itr.hasOwnProperty("custom1")) {
                                            itr.categoryCustom["~@haveValue"] = itr.custom1["haveValue"];
                                        }
                                    });
                                    $scope.stockList.push(itr.categoryCustom);
                                });
//                                $scope.gridOptions.data = $scope.stockList;
//                                $scope.gridOptions.columnDefs = $scope.stockLabelListForUiGrid;
//                                $scope.listFilled = true;
                                $scope.updateStockAccordingToNode($scope.currentActivityNode);
                                $scope.flag.configSearchFlag = false;
                            };
                            DynamicFormService.convertorForCustomField($scope.searchedStockList, success);
                        }
                    });

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
                    if ($scope.currentActivityNode !== undefined && $scope.currentActivityNode !== null) {
                        SplitStockService.retrieveNextNodeDesignationIds($scope.currentActivityNode, function(response) {
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
                        }, function(res) {
                            console.log('failure----');
                        });
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
            $scope.mergeStockNext = function() {
                if ($scope.flag.haveValueFlag) {
                    $scope.selectedRows = $scope.gridApi.selection.getSelectedRows();
                    $scope.noOfLots = 0;
                    $scope.noOfPackets = 0;
                    for (var row = 0; row < $scope.selectedRows.length; row++) {
                        if ($scope.selectedRows[row]["~@packetid"] !== null) {
                            $scope.noOfPackets = $scope.noOfPackets + 1;
                        }
                        else if ($scope.selectedRows[row]["~@packetid"] === null && $scope.selectedRows[row]["~@lotid"] !== null) {
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
                        $scope.flag.displayEditLotflag = true;
                    }
                    else if ($scope.noOfPackets > 0 && $scope.noOfLots === 0) {
                        $scope.flag.type = "Packet";
                        $scope.flag.displayEditPacketflag = true;
                    }
                    if (($scope.noOfLots > 0 && $scope.noOfPackets === 0) || ($scope.noOfPackets > 0 && $scope.noOfLots === 0)) {
                        $scope.parentList = [];
                        var parentIdList = [];
                        $scope.allotmentIds = [];
                        $scope.idsToMerge = [];
                        for (var row = 0; row < $scope.selectedRows.length; row++) {
                            if ($scope.noOfLots > 0) {
                                if (parentIdList.indexOf($scope.selectedRows[row]["~@parcelNumber"]) === -1) {
                                    $scope.parentList.push({id: $scope.selectedRows[row]["~@parcelid"], text: $scope.selectedRows[row]["~@parcelNumber"]});
                                    parentIdList.push($scope.selectedRows[row]["~@parcelNumber"]);
                                }
                                $scope.idsToMerge.push($scope.selectedRows[row]["~@lotid"]);
                            } else if ($scope.noOfPackets > 0) {
                                if (parentIdList.indexOf($scope.selectedRows[row]["~@lotNumber"]) === -1) {
                                    $scope.parentList.push({id: $scope.selectedRows[row]["~@lotid"], text: $scope.selectedRows[row]["~@lotNumber"]});
                                    parentIdList.push($scope.selectedRows[row]["~@lotNumber"]);
                                }
                                $scope.idsToMerge.push($scope.selectedRows[row]["~@packetid"]);
                            }
                            $scope.flag.multipleParents = true;
                            $scope.allotmentIds.push($scope.selectedRows[row]["~@workallotmentid"]);
                        }
                        var dataToSend = {};
                        dataToSend.workAllotmentId = $scope.allotmentIds;
                        if ($scope.flag.type === "Lot") {
                            dataToSend.isPacket = false;
                        } else {
                            dataToSend.isPacket = true;
                        }
                        MergeStockService.retrieveLotOrPacketByAllotmentId(dataToSend, function(response1) {
                            $scope.parentGridOptions = {};
                            $scope.parentGridOptions.columnDefs = [];
                            $scope.parentGridOptions.data = [];
                            $scope.currentStockDetailsList = angular.copy(response1.data);
                            if (!$scope.flag.multipleParents && $scope.currentStockDetailsList.custom2 !== null && $scope.currentStockDetailsList.custom2 !== undefined) {
                                $scope.currentStockDetails = angular.copy($scope.currentStockDetailsList.custom2[0]);
                            }
                            CustomFieldService.retrieveDesignationBasedFields("stockmerge", function(response) {
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
                                var templateDataLotEdit = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");

                                $scope.lotEditDbType = {};
                                templateDataLotEdit.then(function(section) {
                                    $scope.generalLotEditTemplate = section['genralSection'];
                                    var lotFields = [];
                                    var result = Object.keys(response).map(function(key, value) {
                                        angular.forEach(this[key], function(itr) {
                                            if (key === 'Lot') {
                                                lotFields.push({Lot: itr});
                                            }
                                        });
                                    }, response);
                                    $scope.generalLotEditTemplate = DynamicFormService.retrieveCustomData($scope.generalLotEditTemplate, lotFields);
                                    $scope.fieldConfiguredForLot = false;
                                    if ($scope.generalLotEditTemplate !== undefined && $scope.generalLotEditTemplate !== null) {
                                        for (var i = 0; i < $scope.generalLotEditTemplate.length; i++) {
                                            if ($scope.generalLotEditTemplate[i].model === 'in_stock_of_lot$UMS$String') {
                                                $scope.generalLotEditTemplate[i].required = true;
                                                $scope.fieldConfiguredForLot = true;
                                                break;
                                            }
                                        }
                                    }
                                    $scope.lotEditCustom = DynamicFormService.resetSection($scope.generalLotEditTemplate);
                                }, function(reason) {
                                }, function(update) {
                                });
                                var templateDataPacketEdit = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                                $scope.packetEditDbType = {};
                                templateDataPacketEdit.then(function(section) {
                                    $scope.generalPacketEditTemplate = section['genralSection'];
                                    var packetFields = [];
                                    var result = Object.keys(response).map(function(key, value) {
                                        angular.forEach(this[key], function(itr) {
                                            if (key === 'Packet') {
                                                packetFields.push({Packet: itr});
                                            }
                                        });
                                    }, response);
                                    $scope.generalPacketEditTemplate = DynamicFormService.retrieveCustomData($scope.generalPacketEditTemplate, packetFields);
                                    $scope.fieldConfiguredForPacket = false;
                                    if ($scope.generalPacketEditTemplate !== undefined && $scope.generalPacketEditTemplate !== null) {
                                        for (var i = 0; i < $scope.generalPacketEditTemplate.length; i++) {
                                            if ($scope.generalPacketEditTemplate[i].model === 'in_stock_of_packet$UMS$String') {
                                                $scope.generalPacketEditTemplate[i].required = true;
                                                $scope.fieldConfiguredForPacket = true;
                                                break;
                                            }
                                        }
                                    }
                                    $scope.packetEditCustom = DynamicFormService.resetSection($scope.generalPacketEditTemplate);
                                }, function(update) {
                                });

                                if ($scope.currentStockDetailsList.custom2 !== undefined && $scope.currentStockDetailsList.custom2 !== null && $scope.flag.multipleParents) {
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
                    }
                } else {
                    var msg = "Cannot merge stock with different visibilities.";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                }
            };

            $scope.mergeStock = function() {
                $scope.submitted = true;
                if ($scope.mergeForm.$valid) {
                    var stockdataBean = {};
                    stockdataBean.allotmentIds = $scope.allotmentIds;
                    stockdataBean.parentID = $scope.flag.parentId;
                    stockdataBean.type = $scope.flag.type;
                    stockdataBean.idsToMerge = $scope.idsToMerge;

                    if ($scope.flag.type === 'Lot') {
                        stockdataBean.stockCustom = $scope.lotEditCustom;
                        stockdataBean.stockDbType = $scope.lotEditDbType;
                    } else if ($scope.flag.type === 'Packet') {
                        stockdataBean.stockCustom = $scope.packetEditCustom;
                        stockdataBean.stockDbType = $scope.packetEditDbType;
                    }

                    if (!!stockdataBean.idsToMerge) {
                        $rootScope.maskLoading();
                        console.log("stockdataBean:::::" + JSON.stringify(stockdataBean));
                        MergeStockService.mergeStock(stockdataBean, function() {
                            console.log("Success");
                            $rootScope.unMaskLoading();
                            $scope.onBack();
//                            var msg = "Stock merged successfully";
//                            var type = $rootScope.success;
//                            $rootScope.addMessage(msg, type);
                        }, function() {
                            $rootScope.unMaskLoading();
                            $scope.onBack();
                            var msg = "Failed to merge stock";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                        });
                    }
                }
            };
            $rootScope.unMaskLoading();
        }]);
});