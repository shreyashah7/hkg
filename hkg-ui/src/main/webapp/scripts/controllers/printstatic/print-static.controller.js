define(['hkg', 'customFieldService', 'printService', 'ruleService', 'activityFlowService', 'lotService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive','printBarcodeValue', 'dynamicForm', 'accordionCollapse'], function(hkg) {
    hkg.register.controller('PrintStaticController', ["$rootScope", "$scope", "DynamicFormService", "PrintService", "RuleService", "ActivityFlowService", "CustomFieldService", "LotService", "$filter", function($rootScope, $scope, DynamicFormService, PrintService, RuleService, ActivityFlowService, CustomFieldService, LotService, $filter) {
            $rootScope.maskLoading();
            var invoiceFld = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
            invoiceFld.then(function(section) {
                $scope.invoiceCustomData = angular.copy(section['genralSection']);
            });
            var parcelFld = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
            parcelFld.then(function(section) {
                $scope.parcelCustomData = angular.copy(section['genralSection']);
            });
            var lotFld = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
            lotFld.then(function(section) {
                $scope.lotCustomData = angular.copy(section['genralSection']);
            });
            var packetFld = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
            packetFld.then(function(section) {
                $scope.packetCustomData = angular.copy(section['genralSection']);
            });
            $scope.selectOneParameter = false;
            var featureMap = {};
            var featureCustomMap = {};
            $scope.initializeData = function() {
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
                            var tempToPrint = $scope.gridApi.selection.getSelectedRows()[0];
                            var tempIndex = tempToPrint["~@index"];
                            for (var index = 0; index < $scope.searchedData.length; index++) {
                                if ($scope.searchedData[index].categoryCustom["~@index"] === tempIndex) {
                                    var data = $scope.searchedData[index];
                                    $scope.toPrint = data;
                                    break;
                                }
                            }
                        } else {
                            delete $scope.toPrint;
                        }
                    });
                };
                $scope.printDataBean = {};
                $scope.submitted = false;
                $scope.printdataflag = false;
                $scope.searchedData = [];
                $scope.searchedDataFromDb = [];
                $scope.listFilled = false;
                $scope.lotListTodisplay = [];
                $scope.searchResetFlag = false;
                $scope.reset();
                //                $scope.searchCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);

                var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("staticPrint");
                $scope.flag = {};
                $scope.dbType = {};
                $scope.modelAndHeaderList = [];
                $scope.modelAndHeaderListForLot = [];
                templateData.then(function(section) {
                    var invoiceDbFieldName = [];
                    var parcelDbFieldName = [];
                    var lotDbFieldName = [];
                    var packetDbFieldName = [];
                    $scope.searchInvoiceTemplate = [];
                    $scope.searchParcelTemplate = [];
                    $scope.searchLotTemplate = [];
                    $scope.searchPacketTemplate = [];
                    $scope.generalSearchTemplate = section['genralSection'];
                    if ($scope.generalSearchTemplate !== undefined && $scope.generalSearchTemplate !== null && $scope.generalSearchTemplate.length > 0) {
                        for (var i = 0; i < $scope.generalSearchTemplate.length; i++) {
                            var item = $scope.generalSearchTemplate [i];

                            if (item.featureName.toLowerCase() === 'invoice') {
                                $scope.searchInvoiceTemplate.push(angular.copy(item));
                                invoiceDbFieldName.push(angular.copy(item.model))
                            } else if (item.featureName.toLowerCase() === 'parcel') {
                                $scope.searchParcelTemplate.push(angular.copy(item));
                                parcelDbFieldName.push(angular.copy(item.model));
                            }
                            else if (item.featureName.toLowerCase() === 'lot') {
                                $scope.searchLotTemplate.push(angular.copy(item));
                                lotDbFieldName.push(angular.copy(item.model));
                            }
                            else if (item.featureName.toLowerCase() === 'packet') {
                                $scope.searchPacketTemplate.push(angular.copy(item));
                                packetDbFieldName.push(angular.copy(item.model));
                            }
                            featureMap[item.model] = item.featureName;
                            $scope.modelAndHeaderList.push({name: item.model, displayName: item.label, minWidth: 200});
                        }
                        if (invoiceDbFieldName.length > 0) {
                            featureCustomMap['invoice'] = invoiceDbFieldName;
                        }
                        if (parcelDbFieldName.length > 0) {
                            featureCustomMap['parcel'] = parcelDbFieldName;
                        }
                        if (lotDbFieldName.length > 0) {
                            featureCustomMap['lot'] = lotDbFieldName;
                        }
                        if (packetDbFieldName.length > 0) {
                            featureCustomMap['packet'] = packetDbFieldName;
                        }
                    }

                    $scope.dataRetrieved = true;
                    $scope.searchResetFlag = true;
                }, function(reason) {

                }, function(update) {

                });

                RuleService.retrievePrerequisite(function(res) {
                    $scope.entityList = res.entity;
                });
                $scope.mapOfActivitySerice = {};
                ActivityFlowService.retrievePrerequisite(function(res) {
                    if (!!res) {
                        ActivityFlowService.retrieveServices(function(res1) {
                            $scope.serviceList = res1;
                            $rootScope.unMaskLoading();
                            ActivityFlowService.retrieveDesignations(function(res2) {
                                $scope.designationList = res2.data;
                                ActivityFlowService.retrieveActivityFlowVersion(res['activityflowbycompany'][0]['custom2'][0].value, function(res3) {
                                    angular.forEach(res3.data['activityFlowGroups'], function(flow) {
                                        $scope.mapOfActivitySerice[flow.flowGroupName] = [];
                                        angular.forEach(flow.nodeDataBeanList, function(nodeItem) {
                                            var serviceEnt = $filter('filter')($scope.serviceList, function(service) {
                                                return service.id === nodeItem.associatedService;
                                            })[0];
                                            var designation = $filter('filter')($scope.designationList, function(desg) {
                                                return desg.value === nodeItem.designationId;
                                            })[0];
                                            serviceEnt.designation = designation.value;
                                            $scope.mapOfActivitySerice[flow.flowGroupName].push(serviceEnt);
                                        });
                                    });
                                });
                            });

                        });

                    }
                });
            };

            $scope.retrieveSearchedData = function() {
                if (Object.getOwnPropertyNames($scope.searchCustom).length > 0) {
                    var isModelEmpty = false;
                    if (!jQuery.isEmptyObject($scope.searchCustom)) {
                        var counter = 0;
                        var totalElements = 0;
                        // This is done to check if whether all keys have null values or not.If all keys have null value then dont add
                        for (var k in $scope.searchCustom)
                        {
                            totalElements++;
                            if ($scope.searchCustom[k] === '' || $scope.searchCustom[k] === null)
                            {
                                counter++;
                            }
                        }
                        if (parseInt(counter) === parseInt(totalElements))
                        {
                            isModelEmpty = true;
                        }
                    }
                    if (!isModelEmpty) {
                        $rootScope.maskLoading();
                        $scope.printDataBean.featureCustomMapValue = {};
                        $scope.map = {};
                        var finalMap = {};
                        var searchResult = DynamicFormService.convertSearchData($scope.invoiceCustomData, $scope.parcelCustomData, $scope.lotCustomData, $scope.packetCustomData, angular.copy($scope.searchCustom));
                        angular.forEach(featureMap, function(val, label) {

                            var vlaueOfCus = searchResult[label];
                            if (vlaueOfCus !== undefined) {
                                var temp = {};
                                if (!finalMap[val]) {
                                    temp[label] = vlaueOfCus;
                                    finalMap[val] = temp;
                                } else {
                                    var tempMap = finalMap[val];
                                    tempMap[label] = vlaueOfCus;
                                    finalMap[val] = tempMap;
                                }
                            } else {
                                vlaueOfCus = searchResult['to' + label];
                                if (vlaueOfCus !== undefined) {
                                    var temp = {};
                                    if (!finalMap[val]) {
                                        temp['to' + label] = vlaueOfCus;
                                        finalMap[val] = temp;
                                    } else {
                                        var tempMap = finalMap[val];
                                        tempMap['to' + label] = vlaueOfCus;
                                        finalMap[val] = tempMap;
                                    }
                                }
                                vlaueOfCus = searchResult['from' + label];
                                if (vlaueOfCus !== undefined) {
                                    var temp = {};
                                    if (!finalMap[val]) {
                                        temp['from' + label] = vlaueOfCus;
                                        finalMap[val] = temp;
                                    } else {
                                        var tempMap = finalMap[val];
                                        tempMap['from' + label] = vlaueOfCus;
                                        finalMap[val] = tempMap;
                                    }
                                }
                            }
                        });
                        $scope.printDataBean.featureCustomMapValue = finalMap;
                        $scope.printDataBean.featureDbFieldMap = featureCustomMap;
                        PrintService.search($scope.printDataBean, function(res) {
                            $rootScope.unMaskLoading();
                            delete $scope.toPrint;
                            // console.log("Success");
                            // console.log("Data::" + JSON.stringify(res));
                            var printField = DynamicFormService.retrieveSectionWiseCustomFieldInfo("staticPrint");
                            printField.then(function(section) {
                                $scope.searchResetFlag = false;

                                $scope.printTemplate = section['genralSection'];
                                $scope.searchedDataFromDb = angular.copy(res);
                                var success = function(result)
                                {
                                    $scope.searchedData = angular.copy(result);
                                    var dataToPrint = [];
                                    var index = 0;
                                    angular.forEach($scope.searchedData, function(itr) {
                                        angular.forEach($scope.modelAndHeaderList, function(list) {
                                            if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                                itr.categoryCustom[list.name] = "NA";
                                            }
                                            else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                                if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                    itr.categoryCustom[list.name] = "NA";
                                                }
                                            }
                                        });
                                        itr.categoryCustom["~@index"] = index;
                                        index = index + 1;
                                        dataToPrint.push(itr.categoryCustom);
                                    });
                                    $scope.listFilled = true;
                                    $scope.gridOptions.data = dataToPrint;
                                    $scope.gridOptions.columnDefs = $scope.modelAndHeaderList;
                                    $rootScope.unMaskLoading();
                                    //                                    $scope.onCanel();
                                };
                                DynamicFormService.convertorForCustomField(res, success);
                                $scope.reset();
                            }, function(reason) {

                            }, function(update) {
                            });
                        }, function() {
                            var msg = "Could not retrieve, please try again.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        });
                    } else {
                        delete $scope.searchedData;
                        $scope.listFilled = false;
                        $scope.selectOneParameter = true;
                    }
                } else {
                    $scope.selectOneParameter = true;
                }
            };
            $scope.onCanelOfSearch = function() {
                if ($scope.printstaticForm !== null) {
                    $scope.printstaticForm.$dirty = false;
                }
                $scope.searchedData = [];
                $scope.listFilled = false;
                $scope.searchResetFlag = false;
                //                $scope.searchCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);
                $scope.reset();
                delete $scope.toPrint;
                delete $scope.service;
                delete $scope.activity;
                $scope.printstaticForm.$setPristine();
            };
            $scope.setToPrintData = function(data) {
                if (!!data) {
                    $scope.toPrint = data;
                }
            };
            $scope.printData = function() {
                $scope.flag.printlot = false;
                $scope.flag.printpacket = false;
                $scope.submitted = true;
                var mapToSent = {};
                var invoiceDbFieldName = [];
                var parcelDbFieldName = [];
                var lotDbFieldName = [];
                var packetDbFieldName = [];
                if ($scope.printstaticForm.$valid) {
                    $scope.printdataflag = true;

                    CustomFieldService.retrieveDesignationBasedFields("staticPrint", function(response) {
                        // console.log("response::" + JSON.stringify(response));
                        $scope.invoiceCustom = DynamicFormService.resetSection($scope.generaInvoiceTemplate);
                        var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                        $scope.invoiceDbType = {};
                        templateData.then(function(section) {
                            // console.log("section::" + JSON.stringify(section));
                            $scope.generaInvoiceTemplate = section['genralSection'];
                            var invoiceField = [];
                            var result = Object.keys(response).map(function(key, value) {
                                angular.forEach(this[key], function(itr) {
                                    if (key === 'Invoice#P#') {
                                        invoiceField.push({Invoice: itr});
                                    }
                                });
                            }, response);
                            // console.log("invoiceField::" + JSON.stringify(invoiceField));
                            $scope.generaInvoiceTemplate = DynamicFormService.retrieveCustomData($scope.generaInvoiceTemplate, invoiceField);
//                            $scope.invoiceCustom = $scope.toPrint.custom3;
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
//                            $scope.parcelCustom = $scope.toPrint.custom4;
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
                        $scope.categoryCustom = DynamicFormService.resetSection($scope.generaLotTemplate);
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
//                            $scope.categoryCustom = $scope.toPrint.custom5;
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
//                            $scope.packetCustom = $scope.toPrint.custom1;
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

                        $rootScope.unMaskLoading();
                    }, function() {
                        $rootScope.unMaskLoading();
                        var msg = "Failed to retrieve data";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                    $scope.designations = [];
                    $scope.designations.push($scope.service.designation);
                    var data = {featureName: "staticPrint", roles: $scope.designations};
                    $rootScope.maskLoading();
                    CustomFieldService.retrieveExternalDesignationBasedFields(data, function(result) {
                        $scope.finalPayload = {};
                        // console.log("Method::" + JSON.stringify(result));
                        $rootScope.unMaskLoading();

                        $scope.printLotCustom = DynamicFormService.resetSection($scope.generalPrintLotTemplate);
                        var printLotData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                        $scope.printLotDbType = {};
                        printLotData.then(function(section) {
                            $scope.generalPrintLotTemplate = section['genralSection'];
                            var printLotField = [];
                            var temp = Object.keys(result).map(function(key, value) {
                                angular.forEach(this[key], function(itr) {
                                    if (key === 'Lot') {
                                        printLotField.push({Lot: itr});
                                    }
                                });
                            }, result);
                            // console.log("invoiceField::" + JSON.stringify(printLotField));
                            $scope.generalPrintLotTemplate = DynamicFormService.retrieveCustomData($scope.generalPrintLotTemplate, printLotField);
//                            $scope.printLotCustom = $scope.toPrint.custom5;
                            angular.forEach($scope.generalPrintLotTemplate, function(itr) {
                                if (itr.model) {
                                    lotDbFieldName.push(itr.model);
                                }
                            });
                            if (lotDbFieldName.length > 0) {
//                                mapToSent['lotDbFieldName'] = lotDbFieldName;
                                mapToSent['lotDbFieldName'] = (mapToSent['lotDbFieldName'] || []).concat(lotDbFieldName);
                            }


                        }, function(reason) {

                        }, function(update) {

                        });
                        $scope.printPacketCustom = DynamicFormService.resetSection($scope.generalPrintPacketTemplate);
                        var printPacketData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                        $scope.printLotDbType = {};
                        printPacketData.then(function(section) {
                            $scope.generalPrintPacketTemplate = section['genralSection'];
                            var printPacketField = [];
                            var temp = Object.keys(result).map(function(key, value) {
                                angular.forEach(this[key], function(itr) {
                                    if (key === 'Packet') {
                                        printPacketField.push({Lot: itr});
                                    }
                                });
                            }, result);
                            // console.log("invoiceField::" + JSON.stringify(printPacketField));
                            $scope.generalPrintPacketTemplate = DynamicFormService.retrieveCustomData($scope.generalPrintPacketTemplate, printPacketField);
//                            $scope.printPacketCustom = $scope.toPrint.custom1;
                            angular.forEach($scope.generalPrintPacketTemplate, function(itr) {
                                if (itr.model) {
                                    packetDbFieldName.push(itr.model);
                                }
                            });
                            if (packetDbFieldName.length > 0) {
//                                mapToSent['packetDbFieldName'] = packetDbFieldName;
                                mapToSent['packetDbFieldName'] = (mapToSent['packetDbFieldName'] || []).concat(packetDbFieldName);
                            }
                            if ($scope.toPrint.id === null && $scope.toPrint.value !== null) {
                                var ids = [];
                                ids.push($scope.toPrint.value);
                                mapToSent["lotObjectId"] = ids;
                                LotService.retrieveLotById(mapToSent, function(response) {
                                    $scope.invoiceCustom = response.custom3;
                                    $scope.parcelCustom = angular.copy(response.custom4);
                                    $scope.categoryCustom = angular.copy(response.custom1);
                                    $scope.printLotCustom = angular.copy(response.custom1);
                                    $scope.flag.printlot = true;
                                    $rootScope.unMaskLoading();
                                }, function() {
                                    $rootScope.unMaskLoading();
                                    var msg = "Could not retrieve lot, please try again.";
                                    var type = $rootScope.error;
                                    $rootScope.addMessage(msg, type);
                                });
                            } else if ($scope.toPrint.id !== null) {
                                var ids = [];
                                ids.push($scope.toPrint.id);
                                mapToSent["packetId"] = ids;
                                PrintService.retrievePacketById(mapToSent, function(response) {
                                    $scope.invoiceCustom = response.custom1;
                                    $scope.parcelCustom = angular.copy(response.custom3);
                                    $scope.categoryCustom = angular.copy(response.custom4);
                                    $scope.printPacketCustom = angular.copy(response.custom5);
                                    $scope.flag.printpacket = true;
                                    $rootScope.unMaskLoading();
                                }, function() {
                                    $rootScope.unMaskLoading();
                                    var msg = "Could not retrieve packet, please try again.";
                                    var type = $rootScope.error;
                                    $rootScope.addMessage(msg, type);
                                });
                            }
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

            $scope.onBack = function() {
                $scope.printdataflag = false;
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
                            if (!!data.id) {
                                finalPayload["idToPrint"] = {"id": data.categoryCustom["packetID$AG$String"]};
                                delete finalPayload["payload"];
                            } else if (!!data.value) {
                                finalPayload["idToPrint"] = {"id": data.categoryCustom["lotID$AG$String"]};
                                delete finalPayload["payload1"];
                            }
                        }
                        $rootScope.maskLoading();
                        PrintService.generatePrintData(finalPayload, function(filename) {
                            $rootScope.unMaskLoading();
                            if (!!filename) {
                                $scope.fileName = filename["filename"];
                                var hiddenElement = document.createElement('a');

                                hiddenElement.href = $rootScope.appendAuthToken($rootScope.centerapipath + "print/downloadPDFReport?fileName=" + $scope.fileName);
                                hiddenElement.target = '_blank';
                                hiddenElement.download = 'myFile.pdf';

                                hiddenElement.click();
                                $scope.onBack();
                            }
                        }, function() {
                            $rootScope.unMaskLoading();
                            // console.log("Failure");
                        });
                    });
                });
            };

            $scope.reset = function() {
                $scope.searchCustom = {};
                delete $scope.generalSearchTemplate;
                var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("staticPrint");
                templateData.then(function(section) {
                    $scope.generalSearchTemplate = section['genralSection'];
                    $scope.searchResetFlag = true;

                    $scope.flag.customFieldGenerated = true;
                }, function(reason) {
                }, function(update) {
                });
            };
            $rootScope.unMaskLoading();
        }]);
});