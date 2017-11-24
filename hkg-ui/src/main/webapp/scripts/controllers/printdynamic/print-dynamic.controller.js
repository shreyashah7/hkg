define(['hkg', 'customFieldService', 'printService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive','printBarcodeValue', 'dynamicForm', 'accordionCollapse'], function(hkg) {
    hkg.register.controller('PrintDynamicController', ["$rootScope", "$scope", "DynamicFormService", "PrintService", "CustomFieldService", function($rootScope, $scope, DynamicFormService, PrintService, CustomFieldService) {
            $rootScope.maskLoading();
            $rootScope.activateMenu();
            $scope.printdataflag = false;
            $scope.printList = [];
            $scope.featureMap = {};
            $scope.initializeData = function() {
//                $scope.generalSearchTemplate = [];
                $scope.flag = {};
                $scope.flag.showprintPage = false;
                $scope.printList = [];
                $scope.listFilled = false;
                $scope.nodeDetailsInfo = [];
                $scope.printLabelListForUiGrid = [];
                $scope.gridOptions = {};
                $scope.gridOptions.enableFiltering = true;
                $scope.gridOptions.multiSelect = false;
                $scope.gridOptions.columnDefs = [];
                $scope.gridOptions.data = [];
                $scope.gridOptions.onRegisterApi = function(gridApi) {
                    //set gridApi on scope
                    $scope.gridApi = gridApi;
                    gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                        if ($scope.gridApi.selection.getSelectedRows().length > 0) {
                            $scope.toPrint = $scope.gridApi.selection.getSelectedRows()[0];
                            if (!!$scope.toPrint["~@status"]) {
                                $scope.flag.isPacket = true;
                            } else {
                                $scope.flag.isPacket = false;
                            }
                        }
                    });
                };
                $scope.searchedPrintList = [];

                $scope.printLabelListForUiGrid = [];
                var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("dynamicPrint");
                templateData.then(function(section) {
                    $scope.generalSearchTemplate = section['genralSection'];

                    if ($scope.generalSearchTemplate !== null && $scope.generalSearchTemplate !== undefined) {
                        for (var i = 0; i < $scope.generalSearchTemplate.length; i++) {
                            var item = $scope.generalSearchTemplate [i];
                            $scope.featureMap[item.model] = item.featureName;
                            if (item.fromModel) {
                                $scope.printLabelListForUiGrid.push({name: item.fromModel, displayName: item.label, minWidth: 200});
                            } else if (item.toModel) {
                                $scope.printLabelListForUiGrid.push({name: item.toModel, displayName: item.label, minWidth: 200});
                            } else if (item.model) {
                                $scope.printLabelListForUiGrid.push({name: item.model, displayName: item.label, minWidth: 200});
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

                    $rootScope.maskLoading();
                    PrintService.retrieveSearchedLotsAndPackets(finalMap, function(result) {
                        $rootScope.unMaskLoading();
                        $scope.nodeDetailsInfo = [];
                        $scope.searchedPrintList = angular.copy(result.printList);
                        $scope.dataRetrieved = true;
                        if ($scope.generalSearchTemplate === null || $scope.generalSearchTemplate === undefined) {
                            $scope.flag.configSearchFlag = true;
                        } else {
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
//                            $scope.printLabelListForUiGrid = result['tabelHeaders'];
                            var success = function(result)
                            {
//                            $scope.searchedPrintList=angular.copy(result);
                                angular.forEach($scope.searchedPrintList, function(itr) {
                                    angular.forEach($scope.printLabelListForUiGrid, function(list) {
                                        if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                            itr.categoryCustom[list.name] = "NA";
                                        }
                                        else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                            if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                itr.categoryCustom[list.name] = "NA";
                                            }
                                        }
                                        if (itr.hasOwnProperty("value")) {
                                            itr.categoryCustom["~@value"] = itr.value;
                                        }
                                        if (itr.hasOwnProperty("label")) {
                                            itr.categoryCustom["~@label"] = itr.label;
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
                                    $scope.printList.push(itr.categoryCustom);
                                });
//                                $scope.gridOptions.data = $scope.printList;
//                                $scope.gridOptions.columnDefs = $scope.printLabelListForUiGrid;
//                                $scope.listFilled = true;
                                $scope.updateStockAccordingToNode($scope.currentActivityNode);
                                $scope.flag.configSearchFlag = false;
                            };
                            $scope.flag.configSearchFlag = false;
                            DynamicFormService.convertorForCustomField($scope.searchedPrintList, success);
                        }
                    }, function() {
                        $rootScope.unMaskLoading();
                    });
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
                    console.log("asdasd:::"+JSON.stringify($scope.nodeIdAndWorkAllocationIdsMap));
                    if (angular.isDefined($scope.nodeIdAndWorkAllocationIdsMap)) {
                        var result = [];
                        var workAllocationIds = $scope.nodeIdAndWorkAllocationIdsMap[nodeId];
                        console.log(workAllocationIds.length > 0);
                        angular.forEach($scope.printList, function(item, index) {
                            if (workAllocationIds.length > 0 && workAllocationIds.indexOf(item["~@value"]) !== -1) {
                                item["~@index"] = index + 1;
                                result.push(item);
                            }
                        });
                        $scope.currentNodeStocks = angular.copy(result);
                        if (angular.isDefined($scope.gridApi)) {
                            $scope.gridApi.selection.clearSelectedRows();
                        }
                        $scope.gridOptions.data = result;
                        $scope.gridOptions.columnDefs = $scope.printLabelListForUiGrid;
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
                }
            };
            $scope.setToPrintData = function(data) {
                if (!!data) {
                    $scope.toPrint = data;
                }
            };

            $scope.onBack = function() {
                $scope.printdataflag = false;
                $scope.initializeData();
                delete $scope.toPrint;
            };

            $scope.onCanelOfSearch = function() {
                delete $scope.toPrint;
                $scope.gridApi.selection.clearSelectedRows();
            };
            $scope.printDataTemp = function() {
                var mapToSent = {};
                var invoiceDbFieldName = [];
                var parcelDbFieldName = [];
                var lotDbFieldName = [];
                var packetDbFieldName = [];
                $scope.printdataflag=false;
                if (!!$scope.toPrint) {
                    var allotmentID = $scope.toPrint["~@value"];

                    CustomFieldService.retrieveDesignationBasedFields("dynamicPrint", function(response) {
//                        $scope.invoiceCustom = DynamicFormService.resetSection($scope.generaInvoiceTemplate);
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
//                            $scope.invoiceCustom = result1;
                            angular.forEach($scope.generaInvoiceTemplate, function(itr) {
                                if (itr.model) {
                                    invoiceDbFieldName.push(itr.model);
                                }
                            });
                            if (invoiceDbFieldName.length > 0) {
                                mapToSent['invoiceDbFieldName'] = invoiceDbFieldName;
                            }
                        }, function(reason) {

                        }, function(update) {

                        });
//                        $scope.parcelCustom = DynamicFormService.resetSection($scope.generaParcelTemplate);
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
//                            $scope.parcelCustom = result1;
                            angular.forEach($scope.generaParcelTemplate, function(itr) {
                                if (itr.model) {
                                    parcelDbFieldName.push(itr.model);
                                }
                            });
                            if (parcelDbFieldName.length > 0) {
                                mapToSent['parcelDbFieldName'] = parcelDbFieldName;
                            }
                        }, function(reason) {

                        }, function(update) {

                        });
//                        $scope.categoryCustom = DynamicFormService.resetSection($scope.generaLotTemplate);
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
//                            $scope.categoryCustom = result1;
                            angular.forEach($scope.generalLotTemplate, function(itr) {
                                if (itr.model) {
                                    lotDbFieldName.push(itr.model);
                                }
                            });
                            if (lotDbFieldName.length > 0) {
                                mapToSent['lotDbFieldName'] = lotDbFieldName;
                            }

                        }, function(reason) {

                        }, function(update) {

                        });
//                        $scope.packetCustom = DynamicFormService.resetSection($scope.generaPacketTemplate);
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
//                            $scope.packetCustom = result1;
                            angular.forEach($scope.generalPacketTemplate, function(itr) {
                                if (itr.model) {
                                    packetDbFieldName.push(itr.model);
                                }
                            });
                            if (packetDbFieldName.length > 0) {
                                mapToSent['packetDbFieldName'] = packetDbFieldName;
                            }
                        }, function(reason) {

                        }, function(update) {

                        });

                        $scope.finalPayload = {};

//                        $scope.printLotCustom = DynamicFormService.resetSection($scope.generalPrintLotTemplate);
                        var printLotData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                        $scope.printLotDbType = {};
                        printLotData.then(function(section) {
                            $scope.generalPrintLotTemplate=[];
                            $scope.generalPrintLotTemplate = section['genralSection'];
                            var printLotField = [];
                            var temp = Object.keys(response).map(function(key, value) {
                                angular.forEach(this[key], function(itr) {
                                    if (key === 'Lot') {
                                        printLotField.push({Lot: itr});
                                    }
                                });
                            }, response);
                            $scope.generalPrintLotTemplate = DynamicFormService.retrieveCustomData($scope.generalPrintLotTemplate, printLotField);
//                            $scope.printLotCustom = result1;
                            angular.forEach($scope.generalPrintLotTemplate, function(itr) {
                                if (itr.model) {
                                    lotDbFieldName.push(itr.model);
                                }
                            });
                            if (lotDbFieldName.length > 0) {
                                mapToSent['lotDbFieldName'] = (mapToSent['lotDbFieldName'] || []).concat(lotDbFieldName);
//                                mapToSent['lotDbFieldName'] = lotDbFieldName;
                            }
                        }, function(reason) {

                        }, function(update) {

                        });
//                        $scope.printPacketCustom = DynamicFormService.resetSection($scope.generalPrintPacketTemplate);
                        var printPacketData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                        $scope.printPacketDbType = {};
                        printPacketData.then(function(section) {
                            $scope.generalPrintPacketTemplate=[];
                            $scope.generalPrintPacketTemplate = section['genralSection'];
                            var printPacketField = [];
                            var temp = Object.keys(response).map(function(key, value) {
                                angular.forEach(this[key], function(itr) {
                                    if (key === 'Packet') {
                                        printPacketField.push({Packet: itr});
                                    }
                                });
                            }, response);
                            $scope.generalPrintPacketTemplate = DynamicFormService.retrieveCustomData($scope.generalPrintPacketTemplate, printPacketField);
//                            $scope.printPacketCustom = result1;
                            angular.forEach($scope.generalPrintPacketTemplate, function(itr) {
                                if (itr.model) {
                                    packetDbFieldName.push(itr.model);
                                }
                            });
                            if (packetDbFieldName.length > 0) {
//                                mapToSent['packetDbFieldName'] = packetDbFieldName;
                                mapToSent['packetDbFieldName'] = (mapToSent['packetDbFieldName'] || []).concat(packetDbFieldName);
                            }
                            var payload = {"payload": allotmentID, "fields": mapToSent};
                            PrintService.retrieveInformationByWorkallotmentPrint(payload, function(result1) {
                                console.log("result1:::::" + JSON.stringify(result1));

                                $scope.invoiceCustom = result1.custom4;
                                $scope.parcelCustom = angular.copy(result1.custom3);

                                $scope.categoryCustom = angular.copy(result1.custom1);
                                $scope.printLotCustom = angular.copy(result1.custom1);

                                $scope.printPacketCustom = angular.copy(result1.custom5);

                                $scope.printdataflag = true;
                            });

                        }, function(reason) {

                        }, function(update) {

                        });
                    }, function() {
                        $rootScope.unMaskLoading();
                        var msg = "Failed to retrieve data";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });


                }
            };

            $scope.generatePDFFromData = function() {
                var payload = {};
                var obj = [];
                obj.push({categoryCustom: $scope.printLotCustom});
                DynamicFormService.convertorForCustomField(obj, function(dt) {
                    angular.forEach($scope.generalPrintLotTemplate, function(item) {
                        var value = dt[0].categoryCustom[item.model];
                        if (!!value) {
                            payload[item.label] = value;
                        } else {
                            payload[item.label] = "N/A";
                        }
                    });

                    var payload1 = {};
                    var obj1 = [];
                    obj1.push({categoryCustom: $scope.printPacketCustom});
                    DynamicFormService.convertorForCustomField(obj1, function(dt) {
                        angular.forEach($scope.generalPrintPacketTemplate, function(item) {
                            var value = null;
                            if (!!$scope.printPacketCustom) {
                                value = dt[0].categoryCustom[item.model];
                            }
                            if (!!value) {
                                payload1[item.label] = value;
                            } else {
                                payload1[item.label] = "N/A";
                            }
                        });

                        var idToPrint = [];

                        var finalPayload = {};
                        finalPayload["payload"] = payload;
                        finalPayload["payload1"] = payload1;
                        if (!!$scope.toPrint) {
                            var data = $scope.toPrint;
                            if (!!data["~@status"]) {
                                finalPayload["idToPrint"] = {"id": data["packetID$AG$String"], "allotmentID": $scope.toPrint["~@value"]};
                                delete finalPayload["payload"];
                            } else if (!!data["~@id"]) {
                                finalPayload["idToPrint"] = {"id": data["lotID$AG$String"], "allotmentID": $scope.toPrint["~@value"]};
                                delete finalPayload["payload1"];
                            }
                        }
                        $rootScope.maskLoading();
                        PrintService.generatePrintData(finalPayload, function(filename) {
                            $rootScope.unMaskLoading();
                            if (filename !== undefined && filename !== null) {
                                $scope.fileName = filename["filename"];
                                window.location.href = $rootScope.appendAuthToken($rootScope.centerapipath + "print/downloadPDFReport?fileName=" + $scope.fileName);
                                var msg = "Stock printed successfully";
                                var type = $rootScope.success;
                                $rootScope.addMessage(msg, type);
                                $scope.onBack();
                            }
                        }, function() {
                            $rootScope.unMaskLoading();
                            var msg = "Error while printing stock";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                            // console.log("Failure");
                        });
                    });

                });

            };
            $rootScope.unMaskLoading();
        }]);
});