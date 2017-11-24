define(['hkg', 'sellstockService', 'customFieldService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive','printBarcodeValue', 'dynamicForm', 'accordionCollapse'], function (hkg, sellstockService) {
    hkg.register.controller('SellStockController', ["$rootScope", "$scope", "SellStockService", "DynamicFormService", "CustomFieldService", function ($rootScope, $scope, SellStockService, DynamicFormService, CustomFieldService) {

            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "sellstock";
            $rootScope.activateMenu();
            var featureMap = {};
            $scope.sellstock = function () {
//                console.log("stockobj------" + JSON.stringify(stockObj));

//                console.log("selected row----" + JSON.stringify($scope.gridApi.selection.getSelectedRows()[0]));
                var stockObj = $scope.gridApi.selection.getSelectedRows()[0];
//                console.log("stock object----" + JSON.stringify(stockObj));
                $scope.flag.showstockSellPage = true;
                $scope.flag.stockSellFlag = false;
                $scope.invoiceId = stockObj["~@description"];
                $scope.invoiceIdForConstraint = stockObj["~@description"];
                $scope.parcelId = stockObj["~@parcelId"];
                $scope.parcelIdForConstraint = stockObj["~@parcelId"];
                if (stockObj["~@id"] !== undefined && stockObj["~@id"] !== null && stockObj["~@status"] !== undefined && stockObj["~@status"] !== null) {
                    $scope.packetId = stockObj["~@status"];
                    $scope.isPacket = true;
                } else {
                    $scope.lotId = stockObj["~@id"];
                    $scope.isPacket = false;
                }
                $scope.lotIdForConstraint = stockObj["~@id"];
                $scope.workAllocationId = stockObj["~@workAllocationId"];
                $scope.payload = {
                    "workAllotmentId": $scope.workAllocationId,
                    "isPacket": $scope.isPacket
                };
                SellStockService.retrieveStockByWorkAllotmentId($scope.payload, function (result) {
//                    console.log(JSON.stringify(result));
                    if (result["stock"] !== null) {
//                        console.log("result--secondpage---" + JSON.stringify(result));
                        $rootScope.maskLoading();
                        $scope.sellStock = result["stock"];
                        $scope.lotId = $scope.sellStock["id"];
                        $scope.invoiceId = $scope.sellStock["description"];
                        $scope.parcelId = $scope.sellStock["label"];
                        $scope.packetId = $scope.sellStock["status"];
//                        console.log("lotId-----" + $scope.lotId + "------invoiceId----" + $scope.invoiceId + "---parcelId-----" + $scope.parcelId);
//                        console.log("sellStockList------------------------------" + JSON.stringify($scope.sellStock));
                        $scope.invoiceCustomFieldData = $scope.sellStock["custom1"];
                        $scope.parcelCustomFieldData = $scope.sellStock["custom3"];
                        $scope.lotCustomFieldData = $scope.sellStock["custom4"];
                        $scope.packetCustomFieldData = $scope.sellStock["custom5"];
                        CustomFieldService.retrieveDesignationBasedFields("stocksell", function (response) {
                            $scope.flag.stockSellFlag = true;
                            var templateDataInvoice = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                            $scope.invoiceDbType = {};
                            templateDataInvoice.then(function (section) {
                                $scope.generaInvoiceTemplate = section['genralSection'];
                                var invoiceFields = [];
                                var result = Object.keys(response).map(function (key, value) {
                                    angular.forEach(this[key], function (itr) {
                                        if (key === 'Invoice#P#') {
                                            invoiceFields.push({Invoice: itr});
                                        }
                                    });
                                }, response);
                                $scope.generaInvoiceTemplate = DynamicFormService.retrieveCustomData($scope.generaInvoiceTemplate, invoiceFields);
                                $scope.invoiceCustom = $scope.invoiceCustomFieldData;
                            }, function (reason) {
                            }, function (update) {
                            });
                            var templateDataParcel = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
                            $scope.parcelDbType = {};
                            templateDataParcel.then(function (section) {
                                $scope.generaParcelTemplate = section['genralSection'];
                                var parcelFields = [];
                                var result = Object.keys(response).map(function (key, value) {
                                    angular.forEach(this[key], function (itr) {
                                        if (key === 'Parcel#P#') {
                                            parcelFields.push({Parcel: itr});
                                        }
                                    });
                                }, response);
                                $scope.generaParcelTemplate = DynamicFormService.retrieveCustomData($scope.generaParcelTemplate, parcelFields);
                                $scope.parcelCustom = $scope.parcelCustomFieldData;
                            }, function (reason) {
                            }, function (update) {
                            });
                            var templateDatalot = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                            $scope.lotDbType = {};
                            templateDatalot.then(function (section) {
                                $scope.generalLotTemplate = section['genralSection'];
                                var lotFields = [];
                                var result = Object.keys(response).map(function (key, value) {
                                    angular.forEach(this[key], function (itr) {
                                        if (key === 'Lot#P#') {
                                            lotFields.push({Lot: itr});
                                        }
                                    });
                                }, response);
                                $scope.generalLotTemplate = DynamicFormService.retrieveCustomData($scope.generalLotTemplate, lotFields);
                                $scope.lotCustom = $scope.lotCustomFieldData;
                            }, function (reason) {
                            }, function (update) {
                            });
                            var templateDatapacket = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                            $scope.packetDbType = {};
                            templateDatapacket.then(function (section) {
                                $scope.generalPacketTemplate = section['genralSection'];
                                var packetFields = [];
                                var result = Object.keys(response).map(function (key, value) {
                                    angular.forEach(this[key], function (itr) {
                                        if (key === 'Packet#P#') {
                                            packetFields.push({Lot: itr});
                                        }
                                    });
                                }, response);
                                $scope.generalPacketTemplate = DynamicFormService.retrieveCustomData($scope.generalPacketTemplate, packetFields);
                                $scope.packetCustom = $scope.packetCustomFieldData;
                            }, function (reason) {
                            }, function (update) {
                            });
                            var templateDataSellstock = DynamicFormService.retrieveSectionWiseCustomFieldInfo("sell");
                            $scope.sellstockDbType = {};
                            templateDataSellstock.then(function (section) {
                                $scope.generalSellstockTemplate = section['genralSection'];
                                var lotEditFields = [];
                                var result = Object.keys(response).map(function (key, value) {
                                    angular.forEach(this[key], function (itr) {
                                        if (key === 'Sell') {
                                            lotEditFields.push({Lot: itr});
                                        }
                                    });
                                }, response);
                                $scope.generalSellstockTemplate = DynamicFormService.retrieveCustomData($scope.generalSellstockTemplate, lotEditFields);
                                if ($scope.generalSellstockTemplate !== undefined) {
                                    angular.forEach($scope.stockStaticFields, function (fieldModel) {
                                        if (!$scope.flag.staticFieldMissing) {
                                            var isFieldExists = false;
                                            angular.forEach($scope.generalSellstockTemplate, function (field) {
                                                if (field.model === fieldModel + '$DRP$String') {
                                                    isFieldExists = true;
                                                }
                                            });
                                            if (!isFieldExists) {
                                                $scope.flag.staticFieldMissing = true;
                                            }
                                        }
                                    });
                                }
                                $scope.flag.customFieldGenerated = true;
                                $scope.editResetFlag = true;
                            }, function (reason) {
                            }, function (update) {
                            });
//                
                            $rootScope.unMaskLoading();
                        }, function () {
                            $rootScope.unMaskLoading();
                            var msg = "Failed to retrieve data";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                        });
                    }
                });
            };
            $scope.initializeData = function (flag) {
                if (flag) {
                    $scope.currentNodeStocks = [];
                    $scope.nodeDetailsInfo = [];
                    $scope.stockStaticFields = [];
                    $scope.lotStaticFields = [];
                    $scope.packetStaticFields = [];
                    $scope.dataRetrieved = false;
                    $scope.flag = {};
                    $scope.flag.showstockSellPage = false;
                    $scope.flag.stockSellFlag = false;
                    $scope.flag.staticFieldMissing = false;
                    $scope.stockList = [];
                    $scope.listFilled = false;
                    $scope.invoiceCustom = {};
                    $scope.parcelCustom = {};
                    $scope.lotCustom = {};
                    $scope.packetCustom = {};
                    $scope.sellstockCustom = {};
                    $scope.stockLabelListForUiGrid = [];
                    $scope.gridOptions = {};
                    $scope.gridOptions.multiSelect = false;
                    $scope.gridOptions.enableRowSelection = true;
                    $scope.gridOptions.enableSelectAll = false;
                    $scope.gridOptions.enableSorting = false;
                    $scope.gridOptions.enableFiltering = true;
                    $scope.gridOptions.columnDefs = [];
                    $scope.gridOptions.data = [];
                    $scope.sellStockList = [];
                    $scope.generalSearchTemplate = [];

                    $scope.stockLabelListForUiGrid = [];
                    $scope.flag.rowSelectedflag = false;
                    $scope.gridOptions.onRegisterApi = function (gridApi) {
                        //set gridApi on scope
                        $scope.gridApi = gridApi;
                        gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                            if ($scope.gridApi.selection.getSelectedRows().length > 0) {
                                $scope.flag.rowSelectedflag = true;
                            } else {
                                $scope.flag.rowSelectedflag = false;
                            }
                        });
                        gridApi.selection.on.rowSelectionChangedBatch($scope, function (rows) {
                            if ($scope.gridApi.selection.getSelectedRows().length > 0) {
                                $scope.flag.rowSelectedflag = true;
                            } else {
                                $scope.flag.rowSelectedflag = false;
                            }
                        });
                    };
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("stocksell");
                    templateData.then(function (section) {
                        $scope.dataRetrieved = true;
                        $scope.generalSearchTemplate = section['genralSection'];
                        if ($scope.generalSearchTemplate != null && $scope.generalSearchTemplate.length > 0) {
                            for (var i = 0; i < $scope.generalSearchTemplate.length; i++) {
                                var item = $scope.generalSearchTemplate [i];
                                featureMap[item.model] = item.featureName;
                                if (item.fromModel) {
                                    $scope.stockLabelListForUiGrid.push({name: item.fromModel, displayName: item.label, minWidth: 200});
                                } else if (item.toModel) {
                                    $scope.stockLabelListForUiGrid.push({name: item.toModel, displayName: item.label, minWidth: 200});
                                } else if (item.model) {
                                    $scope.stockLabelListForUiGrid.push({name: item.model, displayName: item.label, minWidth: 200});
                                }
                            }
                        }
//                        console.log(!!$scope.generalSearchTemplate);
                        if (!!$scope.generalSearchTemplate) {
                            $scope.flag.generalSearchTemplateFlag = true;
                            $rootScope.maskLoading();
                            SellStockService.retrieveSellstocks(featureMap, function (result) {
                                $rootScope.unMaskLoading();
//                                console.log("result----" + JSON.stringify(result));
                                $scope.nodeDetailsInfo = [];
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
                                            angular.forEach(dynamicServiceInitDataBeans, function (item) {
                                                var data = {};
                                                data.groupId = item.groupId;
                                                data.groupName = item.groupName;
                                                data.modifier = item.modifier;
                                                data.nodeId = item.nodeId;
                                                data.nodeName = item.nodeName;
                                                $scope.nodeDetailsInfo.push(data);
                                            });
                                        }
                                    } else {
                                        $scope.nodeDetailsInfo = [];
                                    }
                                    $scope.currentActivityNode = $scope.nodeDetailsInfo[0].nodeId;
                                }

                                if (result['stockList'] != null) {

//                                    console.log("result--first page----" + JSON.stringify(result['stockList']));

//                                    $scope.generalSearchTemplate = result['generalSearchTemplate'];
                                    $scope.sellStockList = result['stockList'];
                                    var success = function (result)
                                    {
                                        angular.forEach($scope.sellStockList, function (itr) {
                                            angular.forEach($scope.stockLabelListForUiGrid, function (list) {
                                                if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                                    itr.categoryCustom[list.name] = "NA";
                                                }
                                                else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                                    if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                        itr.categoryCustom[list.name] = "NA";
                                                    }
                                                }
                                                if (itr.hasOwnProperty("value")) {
                                                    itr.categoryCustom["~@workAllocationId"] = itr.value;
                                                }
                                                if (itr.hasOwnProperty("label")) {
                                                    itr.categoryCustom["~@parcelId"] = itr.label;
                                                }
                                                if (itr.hasOwnProperty("description")) {
                                                    itr.categoryCustom["~@description"] = itr.description;
                                                }
                                                if (itr.hasOwnProperty("id")) {
                                                    itr.categoryCustom["~@id"] = itr.id;
                                                }
                                                if (itr.hasOwnProperty("status")) {
                                                    itr.categoryCustom["~@status"] = itr.status;
                                                }
                                            });
                                            $scope.stockList.push(itr.categoryCustom);
                                        });
                                        $scope.updateStockAccordingToNode($scope.currentActivityNode);
                                    }
                                    DynamicFormService.convertorForCustomField($scope.sellStockList, success);
//                    });
//                                    }
                                }
                            }, function (reason) {
                            }, function (update) {
                            });
                        } else {
                            $scope.flag.generalSearchTemplateFlag = false;
                        }

                    }, function (reason) {

                    }, function (update) {

                    });

                }
            };
            $scope.initializeData(true);
            $scope.initSellstockForm = function (sellstockForm) {
                $scope.sellstockForm = sellstockForm;
            };
            $scope.sell = function (sellstockForm) {
                $rootScope.maskLoading();
                $scope.submitted = true;
                $scope.flag.showstockSellPage = false;
                var stockList = [];
                var stockDataBean = {};
                stockDataBean.stockCustom = $scope.sellstockCustom;
                stockDataBean.stockDbType = $scope.sellstockDbType;
                stockDataBean.invoiceDataBean = {id: $scope.invoiceId};
                stockDataBean.parcelDataBean = {id: $scope.parcelId};
                if ($scope.lotId !== undefined && $scope.lotId !== null && $scope.packetId !== undefined && $scope.packetId !== null) {
                    stockDataBean.packetDataBean = {id: $scope.packetId};
                    stockDataBean.lotDataBean = {id: $scope.lotId};
                } else {
                    stockDataBean.lotDataBean = {id: $scope.lotId};
                }
                $scope.gridOptions.columnDefs = [];
                $scope.gridOptions.data = [];
                $scope.sellstockForm.$dirty = false;
//                    console.log("stockDataBean-----" + JSON.stringify(stockDataBean));
                if (sellstockForm.$valid) {
                    SellStockService.sellStock(stockDataBean, function (response) {
                        var msg = "Stock sell successfully.";
                        var type = $rootScope.success;
                        $scope.initializeData(true);
                        $rootScope.addMessage(msg, type);
                        $scope.editResetFlag = false;
                        $scope.reset();
                        $rootScope.unMaskLoading();
                    }, function () {
                        var msg = "Could not sell stock, please try again.";
                        var type = $rootScope.error;
                        $scope.initializeData(true);
                        $rootScope.addMessage(msg, type);
                        $scope.editResetFlag = false;
                        $scope.reset();
                        $rootScope.unMaskLoading();
                    });

                }
            };
            $scope.onCanel = function (sellstockForm) {
                if (sellstockForm != null) {
                    sellstockForm.$dirty = false;
                    $scope.flag.showstockSellPage = false;
                }
                $scope.submitted = false;
                $scope.editResetFlag = false;
                $scope.reset();
            };
            $scope.reset = function () {
                var templateDataLotEdit = {};
                templateDataLotEdit = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                $scope.lotEditDbType = {};
                templateDataLotEdit.then(function (section) {
                    $scope.generalLotEditTemplate = section['genralSection'];
                    $scope.lotEditCustom = {};
                    $scope.editResetFlag = true;
                }, function (reason) {
                }, function (update) {
                });
            };

            $scope.updateStockAccordingToNode = function (nodeId) {
                if (nodeId !== null && nodeId !== undefined) {
                    $scope.currentActivityNode = nodeId;
                    angular.forEach($scope.nodeDetailsInfo, function (item) {
                        if (item.nodeId === nodeId) {
                            $scope.currentSelectedNodeInfo = angular.copy(item);
                        }
                    });
                    if (angular.isDefined($scope.nodeIdAndWorkAllocationIdsMap)) {
                        var result = [];
                        var index = 0;
                        var workAllocationIds = $scope.nodeIdAndWorkAllocationIdsMap[nodeId];
//                        console.log("workAllocationIds --" + workAllocationIds);
                        angular.forEach($scope.stockList, function (item) {
//                            console.log(JSON.stringify(item));
                            if (workAllocationIds.length > 0 && workAllocationIds.indexOf(item["~@workAllocationId"]) !== -1) {
                                item["~@index"] = ++index;
                                result.push(item);
                            }
                        });
                        $scope.currentNodeStocks = angular.copy(result);
                        if($scope.currentNodeStocks.length > 0){
                            $scope.flag.valueRetrieved = true;
                        }else{
                            $scope.flag.valueRetrieved = false;
                        }
                        if (angular.isDefined($scope.gridApi)) {
                            $scope.gridApi.selection.clearSelectedRows();
                        }
                        $scope.gridOptions.data = result;
                        $scope.gridOptions.columnDefs = $scope.stockLabelListForUiGrid;
                        $scope.listFilled = true;
                        if (!!$scope.currentSelectedNodeInfo && !!$scope.currentSelectedNodeInfo.modifier) {
                            $scope.gridOptions.isRowSelectable = function (row) {
                                if (!!$scope.diamondsInQueue) {
                                    if (row.entity["~@index"] > $scope.diamondsInQueue) {
                                        return false;
                                    } else {
                                        return true;
                                    }
                                }
                            };
                        } else {
                            $scope.gridOptions.isRowSelectable = function (row) {
                                if (row.entity["~@index"] > 0) {
                                    return true;
                                } else {
                                    return false;
                                }
                            };
                        }
                        window.setTimeout(function () {
                            $(window).resize();
                            $(window).resize();
                        }, 100);
                    } else {
                        console.log("Node map not initialized");
                    }
                }
            };
        }
    ]);
});