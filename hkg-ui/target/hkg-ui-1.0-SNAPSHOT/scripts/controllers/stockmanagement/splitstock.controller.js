define(['hkg', 'customFieldService', 'splitstockService', 'printService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'printBarcodeValue', 'dynamicForm', 'accordionCollapse', 'lotService'], function(hkg) {
    hkg.register.controller('SplitStockController', ["$rootScope", "$scope", "DynamicFormService", "SplitStockService", "CustomFieldService", "PrintService", "$timeout", "LotService", function($rootScope, $scope, DynamicFormService, SplitStockService, CustomFieldService, PrintService, $timeout, LotService) {
            $rootScope.maskLoading();
            $rootScope.activateMenu();
            $scope.stockdataflag = false;
            $scope.stockList = [];
            $scope.flag = {};
            $scope.SplitStock = this;
            $scope.featureMap = {};
            $scope.tempDbMap = {};
            $scope.initializeData = function() {
                $scope.nodeDetailsInfo = [];
                $scope.flag = {};
                $scope.flag.showstockPage = false;
                $scope.stockList = [];
                $scope.listFilled = false;
                $scope.flag.rowSelectedflag = false;
                $scope.stockLabelListForUiGrid = [];
                this.lotedtflg = false;
                this.packetedtflg = false;

                $scope.gridOptions1 = {};
                $scope.gridOptions1.enableFiltering = true;
                $scope.gridOptions1.columnDefs = [];
                $scope.gridOptions1.data = [];

                $scope.gridOptions = {};
                $scope.gridOptions.enableFiltering = true;
                $scope.gridOptions.multiSelect = false;
                $scope.gridOptions.columnDefs = [];
                $scope.gridOptions.data = [];
                $scope.gridOptions.onRegisterApi = function(gridApi) {
                    //set gridApi on scope
                    $scope.gridApi = gridApi;
                    gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                        $scope.toSplit = $scope.gridApi.selection.getSelectedRows()[0];
                    });
                };
                $scope.searchedStockList = [];
                $scope.generalSearchTemplate = [];
                $scope.stockLabelListForUiGrid = [];
                $rootScope.maskLoading();
                var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("stocksplit");
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

                    SplitStockService.retrieveSearchedLotsAndPacketsForSplit(finalMap, function(result) {
                        $rootScope.unMaskLoading();
                        $scope.nodeDetailsInfo = [];
                        $scope.dataRetrieved = true;
                        $scope.searchedStockList = angular.copy(result.stockList);
                        if ($scope.generalSearchTemplate === null || $scope.generalSearchTemplate === undefined) {
                            $scope.flag.configSearchFlag = true;
                        } else {
//                            $scope.generalSearchTemplate = result['generalSearchTemplate'];
//                            $scope.searchedStockList = result['stockList'];
//                            $scope.stockLabelListForUiGrid = result['tabelHeaders'];
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
                                if ($scope.currentActivityNode !== undefined && $scope.currentActivityNode !== null) {
                                    SplitStockService.retrieveNextNodeDesignationIds($scope.currentActivityNode, function(response) {
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
                                    });
                                    $scope.stockList.push(itr.categoryCustom);
                                });
//                                $scope.gridOptions.data = $scope.stockList;
//                                $scope.gridOptions.columnDefs = $scope.stockLabelListForUiGrid;
//                                $scope.listFilled = true;
                                $scope.updateStockAccordingToNode($scope.currentActivityNode);
                                $scope.flag.configSearchFlag = false;
                                window.setTimeout(function() {
                                    $(window).resize();
                                    $(window).resize();
                                }, 100);
                            };
                            DynamicFormService.convertorForCustomField($scope.searchedStockList, success);
                        }
                    }, function() {
                        $rootScope.unMaskLoading();
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
            $scope.splitStockNext = function() {
                console.log("$scope.toSplit::::" + JSON.stringify($scope.toSplit));
                var allotmentID = $scope.toSplit["~@workallotmentid"];
                if (!!$scope.toSplit["~@packetid"]) {
                    $scope.flag.displayEditPacketflag = true;
                    $scope.totalCaratMain = $scope.toSplit.carat_of_packet$NM$Double;
                    this.lotedtflg = true;
                } else if (!!$scope.toSplit["~@lotid"]) {
                    $scope.flag.displayEditLotflag = true;
                    $scope.totalCaratMain = $scope.toSplit.carat_of_lot$NM$Double;
                    this.packetedtflg = true;
                }
                $scope.gridOptions1 = {};
                $scope.gridOptions1.enableFiltering = true;
                $scope.gridOptions1.columnDefs = [];
                $scope.gridOptions1.data = [];

                $scope.stockListToSave = [];
                $scope.stockListTodisplay = [];
                $scope.count = 0;
                $scope.flag.showstockPage = true;
                $scope.flag.editStock = false;
                $scope.stockdataflag = true;
                PrintService.retrieveInformationByWorkallotment(allotmentID, function(result1) {
                    $scope.printdataflag = true;

                    CustomFieldService.retrieveDesignationBasedFields("stocksplit", function(response) {
                        $scope.SplitStock = this;
                        delete $scope.response;
                        $scope.response = response;
                        $scope.invoiceCustom = DynamicFormService.resetSection($scope.generaInvoiceTemplate);
                        var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                        $scope.invoiceDbType = {};
                        templateData.then(function(section) {
                            $scope.generaInvoiceTemplate = section['genralSection'];
                            var invoiceField = [];
                            var result = Object.keys(response).map(function(key, value) {
                                angular.forEach(this[key], function(itr) {
                                    if (key === 'Invoice#P#') {
                                        invoiceField.push({Invoice: itr});
                                    }
                                });
                            }, response);
                            $scope.generaInvoiceTemplate = DynamicFormService.retrieveCustomData($scope.generaInvoiceTemplate, invoiceField);
                            $scope.invoiceCustom = result1;
                        }, function(reason) {

                        }, function(update) {

                        });
                        $scope.parcelCustom = DynamicFormService.resetSection($scope.generaParcelTemplate);
                        var templateDataForParcel = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
                        $scope.parcelDbType = {};
                        templateDataForParcel.then(function(section) {
                            $scope.generaParcelTemplate = section['genralSection'];
                            var parcelField = [];
                            var result = Object.keys(response).map(function(key, value) {
                                angular.forEach(this[key], function(itr) {
                                    if (key === 'Parcel#P#') {
                                        parcelField.push({Parcel: itr});
                                    }
                                });
                            }, response);
                            $scope.generaParcelTemplate = DynamicFormService.retrieveCustomData($scope.generaParcelTemplate, parcelField);
                            $scope.parcelCustom = result1;
                        }, function(reason) {

                        }, function(update) {

                        });
                        $scope.lotCustom = DynamicFormService.resetSection($scope.generaLotTemplate);
                        var templateDataLot = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                        $scope.lotDbType = {};
                        templateDataLot.then(function(section) {
                            $scope.generalLotTemplate = section['genralSection'];
                            var lotField = [];
                            var result = Object.keys(response).map(function(key, value) {
                                angular.forEach(this[key], function(itr) {
                                    if (key === 'Lot#P#') {
                                        lotField.push({Parcel: itr});
                                    }
                                });
                            }, response);
                            $scope.generalLotTemplate = DynamicFormService.retrieveCustomData($scope.generalLotTemplate, lotField);
                            $scope.lotCustom = result1;

                        }, function(reason) {

                        }, function(update) {

                        });
                        $scope.packetCustom = DynamicFormService.resetSection($scope.generaPacketTemplate);
                        var templateDataParcelPacket = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                        $scope.packetDbType = {};
                        templateDataParcelPacket.then(function(section) {
                            $scope.generalPacketTemplate = section['genralSection'];
                            var packetField = [];
                            var result = Object.keys(response).map(function(key, value) {
                                angular.forEach(this[key], function(itr) {
                                    if (key === 'Packet#P#') {
                                        packetField.push({Packet: itr});
                                    }
                                });
                            }, response);
                            $scope.generalPacketTemplate = DynamicFormService.retrieveCustomData($scope.generalPacketTemplate, packetField);
                            $scope.packetCustom = result1;
                        }, function(reason) {

                        }, function(update) {

                        });

                        $scope.finalPayload = {};
                        $scope.modelAndHeaderListForStock = [];
                        $scope.fieldNotConfigured = false;
                        if ($scope.flag.displayEditLotflag) {
                            $scope.lotEditCustom = {};
                            $scope.reset();
                            var printLotData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                            $scope.lotEditDbType = {};
                            printLotData.then(function(section) {

                                $scope.generalLotEditTemplate = section['genralSection'];
                                var printLotField = [];
                                var temp = Object.keys(response).map(function(key, value) {
                                    angular.forEach(this[key], function(itr) {
                                        if (key === 'Lot') {
                                            printLotField.push({Lot: itr});
                                        }
                                    });
                                }, response);
//                                alert(JSON.stringify(printLotField));
                                $scope.generalLotEditTemplate = DynamicFormService.retrieveCustomData($scope.generalLotEditTemplate, printLotField);
                                angular.forEach($scope.generalLotEditTemplate, function(itr) {
                                    $scope.modelAndHeaderListForStock.push({name: itr.model, displayName: itr.label, minWidth: 200});
                                });
                                $scope.gridOptions1.columnDefs = $scope.modelAndHeaderListForStock;
                                $scope.gridOptions1.columnDefs.push({name: 'Action',
                                    cellTemplate: '<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.updateLot(row.entity)">Update</a></div>', enableFiltering: false, minWidth: 200});

                                $scope.mandatoryFields = ['carat_of_lot$NM$Double'];

                                var lotDbFieldName = [];
                                angular.forEach($scope.generalLotEditTemplate, function(itr) {
                                    lotDbFieldName.push(itr.model);
                                });
                                if (lotDbFieldName.length > 0 && $scope.mandatoryFields != null && $scope.mandatoryFields.length > 0) {
                                    for (var field = 0; field < $scope.mandatoryFields.length; field++) {
                                        if (lotDbFieldName.indexOf($scope.mandatoryFields[field]) === -1) {
                                            $scope.fieldNotConfigured = true;
                                            break;
                                        }
                                    }
                                } else {
                                    $scope.fieldNotConfigured = true;
                                }
                                console.log("fieldNotConfigured::::" + $scope.fieldNotConfigured);
                            }, function(reason) {

                            }, function(update) {

                            });
                        }
                        $scope.fieldNotConfiguredForPacket = false;
                        if ($scope.flag.displayEditPacketflag) {
                            $scope.packetEditCustom = {};
                            $scope.reset();
                            var printPacketData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                            $scope.packetEditDbType = {};
                            printPacketData.then(function(section) {
                                $scope.generalPacketEditTemplate = section['genralSection'];
                                var printPacketField = [];
                                var temp = Object.keys(response).map(function(key, value) {
                                    angular.forEach(this[key], function(itr) {
                                        if (key === 'Packet') {
                                            printPacketField.push({Packet: itr});
                                        }
                                    });
                                }, response);
                                $scope.generalPacketEditTemplate = DynamicFormService.retrieveCustomData($scope.generalPacketEditTemplate, printPacketField);

                                angular.forEach($scope.generalPacketEditTemplate, function(itr) {
                                    $scope.modelAndHeaderListForStock.push({name: itr.model, displayName: itr.label, minWidth: 200});
                                });
                                $scope.gridOptions1.columnDefs = $scope.modelAndHeaderListForStock;
                                $scope.gridOptions1.columnDefs.push({name: 'Action',
                                    cellTemplate: '<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.updateLot(row.entity)">Update</a></div>', enableFiltering: false, minWidth: 200});

                                $scope.mandatoryFieldsForPacket = ['carat_of_packet$NM$Double'];

                                var packetDbFieldName = [];
                                angular.forEach($scope.generalPacketEditTemplate, function(itr) {
                                    packetDbFieldName.push(itr.model);
                                });
                                if (packetDbFieldName.length > 0 && $scope.mandatoryFieldsForPacket != null && $scope.mandatoryFieldsForPacket.length > 0) {
                                    for (var field = 0; field < $scope.mandatoryFieldsForPacket.length; field++) {
                                        if (packetDbFieldName.indexOf($scope.mandatoryFieldsForPacket[field]) === -1) {
                                            $scope.fieldNotConfiguredForPacket = true;
                                            break;
                                        }
                                    }
                                } else {
                                    $scope.fieldNotConfiguredForPacket = true;
                                }
                            }, function(reason) {

                            }, function(update) {

                            });
                        }
                        $rootScope.unMaskLoading();
                    }, function() {
                        $rootScope.unMaskLoading();
                        var msg = "Failed to retrieve data";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });

                }, function() {
                    // console.log("Failure");
                });

            };
            $scope.createStock = function() {
                $scope.categoryCustom = {};
                if ($scope.flag.displayEditPacketflag) {
                    $scope.categoryCustom = $scope.packetEditCustom;
                    $scope.tempDbMap = angular.copy($scope.packetEditDbType);
                } else if ($scope.flag.displayEditLotflag) {
                    $scope.categoryCustom = $scope.lotEditCustom;
                    $scope.tempDbMap = angular.copy($scope.lotEditDbType);
                }
                // console.log(Object.getOwnPropertyNames($scope.categoryCustom).length > 0);
                if (Object.getOwnPropertyNames($scope.categoryCustom).length > 0) {
                    var mapHasValue = false;
                    for (var prop in $scope.categoryCustom) {
                        if (!!($scope.categoryCustom[prop])) {
                            mapHasValue = true;
                            break;
                        }
                    }
                    $scope.submitted = true;
                    // console.log($scope.stockform.$valid && mapHasValue);
                    if ($scope.stockform.$valid && mapHasValue) {

                        $scope.submitted = false;
                        var sectionData = [];
                        sectionData.push({categoryCustom: angular.copy($scope.categoryCustom)});
                        $scope.count++;
                        $scope.stockListToSave.push(angular.copy({id: $scope.count, categoryCustom: $scope.categoryCustom}));
                        if ($scope.flag.displayEditPacketflag) {
                            this.packetedtflg = false;
                        } else if ($scope.flag.displayEditLotflag) {
                            this.lotedtflg = false;
                        }
                        var success = function(result)
                        {
                            if ($scope.flag.displayEditPacketflag) {
                                $scope.packetEditDbType = $scope.tempDbMap;
                            } else if ($scope.flag.displayEditLotflag) {
                                $scope.lotEditDbType = $scope.tempDbMap;
                            }

                            angular.forEach($scope.modelAndHeaderListForStock, function(list) {
                                if (!result[0]["categoryCustom"].hasOwnProperty(list.name)) {
                                    result[0]["categoryCustom"][list.name] = "NA";
                                }
                                else if (result[0]["categoryCustom"].hasOwnProperty(list.name)) {
                                    if (result[0]["categoryCustom"][list.name] === null || result[0]["categoryCustom"][list.name] === '' || result[0]["categoryCustom"][list.name] === undefined) {
                                        result[0]["categoryCustom"][list.name] = "NA";
                                    }
                                }
                            });
                            result[0]["categoryCustom"]["~@index"] = $scope.count;
                            $scope.stockListTodisplay.push(angular.copy({id: $scope.count, category: result[0]}));
                            $scope.gridOptions1.data.push(angular.copy(result[0]["categoryCustom"]));
                            $scope.gridOptions1.columnDefs.push({name: 'Action',
                                cellTemplate: '<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.editStockLocally(row.entity)">Edit</i></a>&nbsp;<a ng-click="grid.appScope.showPopUp(row.entity)">Delete</i></a></div>', enableFiltering: false, minWidth: 200});


                        };
                        if ($scope.flag.displayEditPacketflag) {
                            DynamicFormService.convertorForCustomField(sectionData, success);
                        } else if ($scope.flag.displayEditLotflag) {
                            DynamicFormService.convertorForCustomField(sectionData, success);
                        }
                        $scope.reset();
                        $scope.listFilled = true;
                    }
                }
            };

            $scope.editStockLocally = function(rowdata) {
                if ($scope.flag.displayEditPacketflag) {
                    $scope.SplitStock = this;
                    this.packetedtflg = false;
                } else if ($scope.flag.displayEditLotflag) {
                    $scope.SplitStock = this;
                    this.lotedtflg = false;
                }
                $scope.index = rowdata["~@index"];
                $scope.oldObj = angular.copy(rowdata, $scope.oldObj);
                if (rowdata !== null) {
                    $scope.flag.editStock = true;
                    if (!!($scope.stockListToSave && $scope.stockListToSave.length > 0)) {
                        for (var i = 0; i < $scope.stockListToSave.length; i++) {
                            if (rowdata["~@index"] !== null && $scope.stockListToSave[i].id === rowdata["~@index"]) {
                                if ($scope.flag.displayEditPacketflag) {
                                    $scope.categoryCustom = angular.copy($scope.stockListToSave[i].categoryCustom);
                                    $scope.packetEditCustom = angular.copy($scope.stockListToSave[i].categoryCustom);
                                } else if ($scope.flag.displayEditLotflag) {
                                    $scope.categoryCustom = angular.copy($scope.stockListToSave[i].categoryCustom);
                                    $scope.lotEditCustom = angular.copy($scope.stockListToSave[i].categoryCustom);
                                }
                                break;
                            }
                        }
                    }
                }
                $timeout(function() {
                    if ($scope.flag.displayEditPacketflag) {
                        $scope.SplitStock = this;
                        this.packetedtflg = true;
                    } else if ($scope.flag.displayEditLotflag) {
                        $scope.SplitStock = this;
                        this.lotedtflg = true;
                    }
                }, 50);
            };

            $scope.onCancel = function() {
                $scope.flag.editStock = false;
                if ($scope.flag.displayEditPacketflag) {
                    this.packetedtflg = false;
                } else if ($scope.flag.displayEditLotflag) {
                    this.lotedtflg = false;
                }
                $scope.reset();
            };

            $scope.showPopUp = function(stockObj) {
                $scope.stockObjectToDelete = stockObj;
                $scope.index = stockObj["~@index"];
                $("#deleteDialog").modal('show');
            };

            $scope.hidePopUp = function() {
                $("#deleteDialog").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };

            $scope.deleteStock = function() {
                for (var i = 0; i < $scope.gridOptions1.data.length; i++) {
                    var item = $scope.gridOptions1.data[i];
                    if (item["~@index"] === $scope.index) {
                        $scope.gridOptions1.data.splice(i, 1);
                        break;
                    }
                }
                for (var i = 0; i < $scope.stockListToSave.length; i++) {
                    var item = $scope.stockListToSave[i];
                    if (item.id === $scope.index) {
                        $scope.stockListToSave.splice(i, 1);
                        break;
                    }
                }
                if ($scope.flag.displayEditPacketflag) {
                    this.packetedtflg = false;
                } else if ($scope.flag.displayEditLotflag) {
                    this.lotedtflg = false;
                }
                $scope.reset();
                $scope.flag.editStock = false;
                $("#deleteDialog").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };

            $scope.saveStock = function() {
                $scope.submitted = true;

                if ($scope.flag.displayEditPacketflag) {
                    $scope.categoryCustom = $scope.packetEditCustom;
                } else if ($scope.flag.displayEditLotflag) {
                    $scope.categoryCustom = $scope.lotEditCustom;
                }
                if (Object.getOwnPropertyNames($scope.categoryCustom).length > 0) {
                    var mapHasValue = false;
                    for (var prop in $scope.categoryCustom) {
                        if (!!($scope.categoryCustom[prop])) {
                            mapHasValue = true;
                            break;
                        }
                    }
                    if ((mapHasValue && $scope.stockform.$valid)) {
                        var sectionData = [];
                        sectionData.push({categoryCustom: angular.copy($scope.categoryCustom)});
                        angular.forEach($scope.stockListToSave, function(itr) {
                            if (itr.id === $scope.index) {
                                itr.categoryCustom = ($scope.categoryCustom);
                            }
                        });
                        var success = function(result)
                        {
                            angular.forEach($scope.modelAndHeaderListForStock, function(list) {
                                if (!result[0]["categoryCustom"].hasOwnProperty(list.name)) {
                                    result[0]["categoryCustom"][list.name] = "NA";
                                }
                                else if (result[0]["categoryCustom"].hasOwnProperty(list.name)) {
                                    if (result[0]["categoryCustom"][list.name] === null || result[0]["categoryCustom"][list.name] === '' || result[0]["categoryCustom"][list.name] === undefined) {
                                        result[0]["categoryCustom"][list.name] = "NA";
                                    }
                                }
                            });
                            for (var i = 0; i < $scope.gridOptions1.data.length; i++) {
                                var item = $scope.gridOptions1.data[i];
                                if (item["~@index"] === $scope.index) {
                                    $scope.gridOptions1.data[i] = angular.copy(((result[0].categoryCustom)));
                                    $scope.gridOptions1.data[i]["~@index"] = $scope.index;
                                    break;
                                }
                            }
                        };
                        if ($scope.flag.displayEditPacketflag) {
                            DynamicFormService.convertorForCustomField(sectionData, success);
                            this.packetedtflg = false;
                        } else if ($scope.flag.displayEditLotflag) {
                            DynamicFormService.convertorForCustomField(sectionData, success);
                            this.lotedtflg = false;
                        }
                        $scope.reset();

                        $scope.listFilled = true;
                        $scope.flag.editStock = false;
                    }
                }
            };

            $scope.reset = function() {
                $scope.categoryCustom = {};
                if ($scope.flag.displayEditLotflag) {
                    $scope.lotEditCustom = {};
                    var printLotData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                    $scope.lotEditDbType = {};
                    $scope.flag.displayEditLotflag = false;
                    printLotData.then(function(section) {
//                        this.lotedtflg = true;
//                        $scope.flag.displayEditLotflag=true;
                        $scope.generalLotEditTemplate = section['genralSection'];
                        var printLotField = [];
                        var temp = Object.keys($scope.response).map(function(key, value) {
                            angular.forEach(this[key], function(itr) {
                                if (key === 'Lot') {
                                    printLotField.push({Lot: itr});
                                }
                            });
                        }, $scope.response);
                        $scope.generalLotEditTemplate = DynamicFormService.retrieveCustomData($scope.generalLotEditTemplate, printLotField);
                        $timeout(function() {
                            // Controller As
                            this.lotedtflg = true;
                            $scope.flag.displayEditLotflag = true;
                        }, 100);

                    }, function(reason) {

                    }, function(update) {

                    });
                }
                if ($scope.flag.displayEditPacketflag) {
                    $scope.packetEditCustom = {};
                    var printPacketData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                    $scope.packetEditDbType = {};
                    $scope.flag.displayEditPacketflag = false;
                    printPacketData.then(function(section) {
//                        this.packetedtflg = true;
                        $scope.generalPacketEditTemplate = section['genralSection'];
                        var printPacketField = [];
                        var temp = Object.keys($scope.response).map(function(key, value) {
                            angular.forEach(this[key], function(itr) {
                                if (key === 'Packet') {
                                    printPacketField.push({Packet: itr});
                                }
                            });
                        }, $scope.response);
                        $scope.generalPacketEditTemplate = DynamicFormService.retrieveCustomData($scope.generalPacketEditTemplate, printPacketField);
                        $timeout(function() {
                            // Controller As
                            this.packetedtflg = true;
                            $scope.flag.displayEditPacketflag = true;
                        }, 100);
                    }, function(reason) {

                    }, function(update) {

                    });
                }
            };

            $scope.splitStock = function() {
                var splitData = {};
                splitData.allotmentIds = [];
                splitData.allotmentIds.push($scope.toSplit["~@workallotmentid"]);
                var sumOfCaratUI = undefined;
                for (var i = 0; i < $scope.gridOptions1.data.length; i++) {
                    var item = $scope.gridOptions1.data[i];
                    if ($scope.flag.displayEditLotflag) {
                        if (sumOfCaratUI !== undefined) {
                            sumOfCaratUI = sumOfCaratUI + parseFloat(item.carat_of_lot$NM$Double);
                        } else {
                            sumOfCaratUI = parseFloat(item.carat_of_lot$NM$Double);
                        }
                    } else if ($scope.flag.displayEditPacketflag) {
                        if (sumOfCaratUI !== undefined) {
                            sumOfCaratUI = sumOfCaratUI + parseFloat(item.carat_of_packet$NM$Double);
                        } else {
                            sumOfCaratUI = parseFloat(item.carat_of_packet$NM$Double);
                        }
                    }
                }
                if ($scope.totalCaratMain === sumOfCaratUI) {
                    if ($scope.flag.displayEditPacketflag) {
                        splitData.type = "Packet";
                        splitData.id = $scope.toSplit["~@packetid"];
                        splitData.parentID = $scope.toSplit["~@lotid"];
                        splitData.stockDbType = $scope.packetEditDbType;
                    } else if ($scope.flag.displayEditLotflag) {
                        splitData.type = "Lot";
                        splitData.id = $scope.toSplit["~@lotid"];
                        splitData.parentID = $scope.toSplit["~@parcelid"];
                        splitData.stockDbType = $scope.lotEditDbType;
                    }
                    if (!!$scope.stockListToSave) {
                        var datas = [];
                        angular.forEach($scope.stockListToSave, function(itr) {
                            datas.push(itr.categoryCustom);
                        });

                        splitData.stockDataForSplit = datas;

                        $rootScope.maskLoading();
                        SplitStockService.splitStock(splitData, function(res) {
                            $rootScope.unMaskLoading();
                            $scope.onBack();
                            delete $scope.toSplit;
//                        var msg = "Stock splitted successfully";
//                        var type = $rootScope.success;
//                        $rootScope.addMessage(msg, type);
                        }, function() {
                            $rootScope.unMaskLoading();
                            $scope.onBack();
                            delete $scope.toSplit;
                            var msg = "Failed to split stock";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                        });
                    } else {
                        // console.log("Split data is null");
                    }
                } else {
                    var msg = "Carat value does not match, please try again.";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                }
            };
            $rootScope.unMaskLoading();
        }]);
});