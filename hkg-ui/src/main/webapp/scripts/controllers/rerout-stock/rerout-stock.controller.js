define(['hkg', 'reRoutStockService', 'customFieldService', 'activityFlowService', 'changestausService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'printBarcodeValue', 'dynamicForm', 'accordionCollapse', 'assetService'], function (hkg, reRoutStockService) {
    hkg.register.controller('ReRoutStockController', ["$rootScope", "$scope", "ReRoutStockService", "ActivityFlowService", "DynamicFormService", "CustomFieldService", "ChangeStausService", "$filter", "AssetService", function ($rootScope, $scope, ReRoutStockService, ActivityFlowService, DynamicFormService, CustomFieldService, ChangeStausService, $filter, AssetService) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "rerouteStock";
            $rootScope.activateMenu();
            var invoiceFld = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
            invoiceFld.then(function (section) {
                $scope.invoiceCustomData = angular.copy(section['genralSection']);
            });
            var parcelFld = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
            parcelFld.then(function (section) {
                $scope.parcelCustomData = angular.copy(section['genralSection']);
            });
            var lotFld = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
            lotFld.then(function (section) {
                $scope.lotCustomData = angular.copy(section['genralSection']);
            });
            var packetFld = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
            packetFld.then(function (section) {
                $scope.packetCustomData = angular.copy(section['genralSection']);
            });
            var featureMap = {};
            $scope.initializeData = function () {
                $scope.flag = {};
                $scope.popover =
                        "<NOBR><font color='red;'>Please select Activity and Service <br/>Than use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr></table>";
                $scope.searchResetFlag = false;
                $scope.status = {};
                $scope.activityServiceChanged = false;
                $scope.lotIds = [];
                $scope.packetIds = [];
                $scope.proposedStatus = {};
                $scope.proposedstatusList = [];
                $scope.changedReRoutStockDataBean = {};
                $scope.reRoutStockDataBean = {};
                $scope.tempReRoutStockDataBean = {};
                $scope.submitted = false;
                $scope.flag.statusChangeflag = false;
                $scope.flag.rowSelectedflag = false;
                $scope.flag.multipleLotflag = false;
                $scope.searchedData = [];
                $scope.searchedDataFromDb = [];
                $scope.listFilled = false;
                $scope.lotListTodisplay = [];
                $scope.searchCustom = {};
                var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("stockreroute");
                $scope.dbType = {};
                $scope.stockLabelListForUiGrid = [];
                $scope.gridOptions = {};
                $scope.gridOptions.enableFiltering = true;
                $scope.gridOptions.multiSelect = true;
                $scope.gridOptions.enableRowSelection = true;
                $scope.gridOptions.enableSelectAll = true;
                $scope.gridOptions.columnDefs = [];
                $scope.gridOptions.data = [];
                $scope.gridOptions.selectedItems = [];
                $scope.sellStockList = [];
                $scope.stockList = [];
                $scope.selectedRows = [];
                $scope.flag.displayEditPacketflag = false;
                $scope.flag.displayEditLotflag = false;
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
                templateData.then(function (section) {
                    $scope.searchInvoiceTemplate = [];
                    $scope.searchParcelTemplate = [];
                    $scope.searchLotTemplate = [];
                    $scope.searchPacketTemplate = [];
                    $scope.generalSearchTemplate = section['genralSection'];
                    if ($scope.generalSearchTemplate != null && $scope.generalSearchTemplate.length > 0) {
                        for (var i = 0; i < $scope.generalSearchTemplate.length; i++) {
                            var item = $scope.generalSearchTemplate [i];
                            if (item.featureName.toLowerCase() === 'invoice') {
                                $scope.searchInvoiceTemplate.push(angular.copy(item));
                            } else if (item.featureName.toLowerCase() === 'parcel') {
                                $scope.searchParcelTemplate.push(angular.copy(item));
                            }
                            else if (item.featureName.toLowerCase() === 'lot') {
                                $scope.searchLotTemplate.push(angular.copy(item));
                            }
                            else if (item.featureName.toLowerCase() === 'packet') {
                                $scope.searchPacketTemplate.push(angular.copy(item));
                            }
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
                    $scope.searchResetFlag = true;
                    $scope.stockLabelListForUiGrid.push({name: "activity", displayName: "Activity", minWidth: 200});
                    $scope.stockLabelListForUiGrid.push({name: "service", displayName: "Service", minWidth: 200});

                }, function (reason) {

                }, function (update) {

                });
                ActivityFlowService.retrievePrerequisite(function (res) {
                    if (!!res) {
                        var versions = res['activityflowbycompany'];
                        if (angular.isDefined(versions[0]) && versions[0] !== null) {
                            $scope.versionList = versions[0]["custom2"];
                        }
                    }
                });
            };

            $scope.onVersionChange = function (version) {
                $scope.mapOfActivitySerice = {};
                $scope.tempReRoutStockDataBean.activity = {};
                ActivityFlowService.retrieveServices(function (res1) {
                    $scope.serviceList = res1;
                    $rootScope.unMaskLoading();
                    ActivityFlowService.retrieveDesignations(function (res2) {
                        $scope.designationList = res2.data;
                        ActivityFlowService.retrieveActivityFlowVersion(version.value, function (res3) {
                            angular.forEach(res3.data['activityFlowGroups'], function (flow) {
                                $scope.mapOfActivitySerice[flow.flowGroupName] = [];
                                angular.forEach(flow.nodeDataBeanList, function (nodeItem) {
                                    var serviceEnt = $filter('filter')($scope.serviceList, function (service) {
                                        return service.id === nodeItem.associatedService;
                                    })[0];
                                    var designation = $filter('filter')($scope.designationList, function (desg) {
                                        return desg.value === nodeItem.designationId;
                                    })[0];
                                    serviceEnt.designation = designation.value;
                                    serviceEnt.groupId = nodeItem.groupId;
                                    serviceEnt.nodeId = nodeItem.nodeId;
                                    serviceEnt.activityName = flow.flowGroupName;
                                    $scope.mapOfActivitySerice[flow.flowGroupName].push(angular.copy(serviceEnt));
                                });
                            });
                        });
                    });

                });
            };
            $scope.retrieveSearchedData = function (reRoutStockForm) {
                $scope.submitted = true;
                if (!!$scope.tempReRoutStockDataBean.service && reRoutStockForm.$valid) {
                    $rootScope.maskLoading();
                    $scope.stockList = [];
                    $scope.gridOptions.columnDefs = [];
                    $scope.gridOptions.data = [];


                    $scope.reRoutStockDataBean.featureCustomMapValue = {};
                    $scope.reRoutStockDataBean.activityId = $scope.tempReRoutStockDataBean.service.groupId;
                    $scope.reRoutStockDataBean.serviceId = $scope.tempReRoutStockDataBean.service.nodeId;
                    $scope.map = {};
                    var finalMap = {};
                    var searchResult = DynamicFormService.convertSearchData($scope.invoiceCustomData, $scope.parcelCustomData, $scope.lotCustomData, $scope.packetCustomData, angular.copy($scope.searchCustom));
                    angular.forEach(featureMap, function (val, label) {

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
                    $scope.reRoutStockDataBean.featureCustomMapValue = finalMap;
                    $scope.reRoutStockDataBean.featureMap = featureMap;
                    ReRoutStockService.search($scope.reRoutStockDataBean, function (res) {
                        $scope.searchedDataFromDb = angular.copy(res);
                        var success = function (result)
                        {
                            angular.forEach($scope.searchedDataFromDb, function (itr) {
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
                                        itr.categoryCustom["~@lotid"] = itr.value;
                                    }
                                    if (itr.hasOwnProperty("label")) {
                                        itr.categoryCustom["~@parcelid"] = itr.label;
                                    }
                                    if (itr.hasOwnProperty("description")) {
                                        itr.categoryCustom["~@invoiceid"] = itr.description;
                                    }
                                    if (itr.hasOwnProperty("id")) {
                                        itr.categoryCustom["~@packetid"] = itr.id;
                                    }
                                    if (itr.hasOwnProperty("status")) {
                                        itr.categoryCustom["~@status"] = itr.status;
                                    }
                                });
                                itr.categoryCustom["activity"] = $scope.tempReRoutStockDataBean.service.activityName;
                                itr.categoryCustom["service"] = $scope.tempReRoutStockDataBean.service.serviceName;
                                $scope.stockList.push(itr.categoryCustom);
                            });
                            $scope.gridOptions.data = $scope.stockList;
                            $scope.gridOptions.columnDefs = $scope.stockLabelListForUiGrid;
                            $scope.listFilled = true;
                            window.setTimeout(function () {
                                $(window).resize();
                                $(window).resize();
                            }, 100);
                        }
                        DynamicFormService.convertorForCustomField($scope.searchedDataFromDb, success);
                        reRoutStockForm.$dirty = false;
                        reRoutStockForm.$setPristine();
                        $rootScope.unMaskLoading();
                    }, function () {
                        var msg = "Could not retrieve, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();

                    });
                }
            };

            $scope.onCanelOfSearch = function (reRoutStockForm) {
                if (reRoutStockForm != null) {
                    reRoutStockForm.$dirty = false;
                }
                $scope.listFilled = false;
                $scope.searchResetFlag = false;
                $scope.reset("searchCustom");
                $scope.initializeData();
                reRoutStockForm.$setPristine();
            };
            $scope.next = function (reRoutStockForm) {
                $scope.selectedRows = $scope.gridApi.selection.getSelectedRows();
                $scope.submitted = true;
                $scope.isMultipleLotsOrPackets = true;
                $scope.generalStatus = {};
                $scope.flag.generalStatusFlag = true;
                $scope.generaInvoiceTemplate = [];
                $scope.generaParcelTemplate = [];
                $scope.generalLotTemplate = [];
                $scope.noOfLots = 0;
                $scope.noOfPackets = 0;
                $scope.reRoutStockDataBean = {};
                if (reRoutStockForm.$valid) {
                    $scope.flag.reroutstockflag = true;

                    if ($scope.selectedRows.length >= 1) {
                        $scope.flag.singlePacket = false;
                        if ($scope.selectedRows.length > 1) {
                            $scope.flag.multipleLotflag = true;
                            if ($scope.selectedRows[0]["~@packetid"] != null) {
                                $scope.flag.singlePacket = true;
                            }
                        } else {
                            $scope.flag.multipleLotflag = false;
                        }

                        $scope.parentGridOptions = {};
                        $scope.parentGridOptions.columnDefs = [];
                        $scope.parentGridOptions.data = [];


                        $scope.isMultipleLotsOrPackets = false;
                        for (var row = 0; row < $scope.selectedRows.length; row++) {
                            if ($scope.selectedRows[row]["~@packetid"] != null) {
                                $scope.packetIds.push($scope.selectedRows[row]["~@packetid"]);
                            }
                            else if ($scope.selectedRows[row]["~@packetid"] === null && $scope.selectedRows[row]["~@lotid"] != null) {
                                $scope.lotIds.push($scope.selectedRows[row]["~@lotid"]);
                            }
                        }

                        $scope.payload = {};
                        $scope.payload["lotids"] = $scope.lotIds;
                        $scope.payload["packetids"] =  $scope.packetIds;

                        $rootScope.maskLoading();
                        var mapToSent = {};
                        var invoiceDbFieldName = [];
                        var parcelDbFieldName = [];
                        var lotDbFieldName = [];
                        var packetDbFieldName = [];
                        CustomFieldService.retrieveDesignationBasedFields("stockreroute", function (response) {
                            //INVOICE
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
                                angular.forEach($scope.generaInvoiceTemplate, function (itr) {
                                    if (itr.model) {
                                        invoiceDbFieldName.push(itr.model);
                                        $scope.parentGridOptions.columnDefs.push({name: itr.model, displayName: itr.label,
                                            cellTemplate: '<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                                    }
                                });
                                if (invoiceDbFieldName.length > 0) {
                                    mapToSent['invoiceDbFieldName'] = invoiceDbFieldName;
                                }

                                //PARCEL
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
                                    angular.forEach($scope.generaParcelTemplate, function (itr) {
                                        if (itr.model) {
                                            parcelDbFieldName.push(itr.model);
                                            $scope.parentGridOptions.columnDefs.push({name: itr.model, displayName: itr.label,
                                                cellTemplate: '<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                                        }
                                    });
                                    if (parcelDbFieldName.length > 0) {
                                        mapToSent['parcelDbFieldName'] = parcelDbFieldName;
                                    }

                                    //PACKET
                                    var templateDataPacket = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                                    $scope.packetDbType = {};
                                    templateDataPacket.then(function (section) {
                                        $scope.generaPacketTemplate = section['genralSection'];
                                        var packetFields = [];
                                        var result = Object.keys(response).map(function (key, value) {
                                            angular.forEach(this[key], function (itr) {
                                                if (key === 'Packet#P#') {
                                                    packetFields.push({Packet: itr});
                                                }
                                            });
                                        }, response);
                                        $scope.generaPacketTemplate = DynamicFormService.retrieveCustomData($scope.generaPacketTemplate, packetFields);
                                        angular.forEach($scope.generaPacketTemplate, function (itr) {
                                            if (itr.model) {
                                                packetDbFieldName.push(itr.model);
                                                $scope.parentGridOptions.columnDefs.push({name: itr.model, displayName: itr.label,
                                                    cellTemplate: '<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                                            }
                                        });
                                        if (packetDbFieldName.length > 0) {
                                            mapToSent['packetDbFieldName'] = packetDbFieldName;
                                        }

                                        //LOT
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
                                            angular.forEach($scope.generalLotTemplate, function (itr) {
                                                if (itr.model) {
                                                    lotDbFieldName.push(itr.model);
                                                    $scope.parentGridOptions.columnDefs.push({name: itr.model, displayName: itr.label,
                                                        cellTemplate: '<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                                                }
                                            });
                                            if (lotDbFieldName.length > 0) {
                                                mapToSent['lotDbFieldName'] = lotDbFieldName;
                                            }

                                            //SERVICE CALL
                                            var payload = {"payload": $scope.payload, "fields": mapToSent};
                                            ChangeStausService.retrieveStockByLotIdOrPacketId(payload, function (result) {
                                                $scope.stocks = result;


                                                if ($scope.flag.multipleLotflag === false) {
                                                    var currentStock = $scope.stocks[0];
                                                    $scope.invoiceCustom = currentStock.stock["custom3"];
                                                    $scope.parcelCustom = currentStock.stock["custom4"];
                                                    $scope.lotCustom = currentStock.stock["custom1"];
                                                    $scope.packetCustom = currentStock.stock["custom5"];
                                                } else if ($scope.stocks.length > 1 && $scope.flag.multipleLotflag === true) {
                                                    $scope.gridRecords = [];
                                                    //add records with values
                                                    angular.forEach($scope.stocks, function (currentStockDetails) {
                                                        var data = {};
                                                        if (currentStockDetails.stock.custom1 !== undefined && currentStockDetails.stock.custom1 !== null) {
                                                            data = angular.copy(currentStockDetails.stock.custom1);
                                                        }
                                                        if (currentStockDetails.stock.custom3 !== undefined && currentStockDetails.stock.custom3 !== null) {
                                                            angular.forEach(currentStockDetails.stock.custom3, function (val, key) {
                                                                data[key] = val;
                                                            });
                                                        }
                                                        if (currentStockDetails.stock.custom4 !== undefined && currentStockDetails.stock.custom4 !== null) {
                                                            angular.forEach(currentStockDetails.stock.custom4, function (val, key) {
                                                                data[key] = val;
                                                            });
                                                        }
                                                        if (currentStockDetails.stock.custom5 !== undefined && currentStockDetails.stock.custom5 !== null) {
                                                            angular.forEach(currentStockDetails.stock.custom5, function (val, key) {
                                                                data[key] = val;
                                                            });
                                                        }
                                                        $scope.gridRecords.push({categoryCustom: data});

                                                    });

                                                    var success = function (result) {
                                                        angular.forEach($scope.gridRecords, function (item) {
                                                            $scope.parentGridOptions.data.push(item.categoryCustom);
                                                        });
                                                    };
                                                    DynamicFormService.convertorForCustomField($scope.gridRecords, success);
                                                }



                                            });


                                        }, function (reason) {
                                        }, function (update) {
                                        });
                                    }, function (reason) {
                                    }, function (update) {
                                    });
                                }, function (reason) {
                                }, function (update) {
                                });
                            }, function (reason) {
                            }, function (update) {
                            });
                            $rootScope.unMaskLoading();
                        }, function () {
                            $rootScope.unMaskLoading();
                            var msg = "Failed to retrieve data";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                        });

                    }
                    $scope.searchResetFlag = false;
                    $scope.reset("searchCustom");
                }
            };
            $scope.onBack = function (changedReRoutStockForm) {
                if (changedReRoutStockForm != null) {
                    changedReRoutStockForm.$dirty = false;
                    changedReRoutStockForm.$setPristine();
                    $scope.flag.reroutstockflag = false;
                }
                $scope.initializeData();
                $scope.submitted = false;
            };
            $scope.stockReRout = function (changedReRoutStockForm) {
                $scope.changedsubmitted = true;

                if (changedReRoutStockForm.$valid) {
                    $rootScope.maskLoading();
                    $scope.flag.reroutstockflag = false;
                    var stockList = [];
                    var payload = {};
                    payload.activityId = $scope.changedReRoutStockDataBean.service.groupId;
                    payload.serviceId = $scope.changedReRoutStockDataBean.service.nodeId;
                    payload.allotTo = $scope.changedReRoutStockDataBean.allotTo;
                    payload.lotIds = $scope.lotIds;
                    payload.packetIds = $scope.packetIds;
                    ReRoutStockService.stockReRout(payload, function (response) {
                        var msg = "Stock Re-Routed Succesfully.";
                        var type = $rootScope.success;
                        $scope.initializeData();
                        changedReRoutStockForm.$dirty = false;
                        changedReRoutStockForm.$setPristine();
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    }, function () {
                        var msg = "Could not re-route stock, please try again.";
                        var type = $rootScope.error;
                        $scope.initializeData();
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    });
                }
            };

            $scope.userSingleselectComponent = {
                multiple: true,
                closeOnSelect: false,
                placeholder: 'Select',
                maximumSelectionSize: 1,
                initSelection: function (element, callback) {
                    var data = [];
                    callback(data);
                },
                formatResult: function (item) {
                    return item.text;
                },
                formatSelection: function (item) {
                    return item.text;
                },
                query: function (query) {
                    var selected = query.term;
                    $scope.allotTo = [];
                    var success = function (data) {
                        if (data.length !== 0) {
                            angular.forEach(data, function (item) {
                                $scope.allotTo.push({
                                    id: item.value + ":" + item.description,
                                    text: item.label

                                });
                            });
                        }
                        query.callback({
                            results: $scope.allotTo
                        });
                    };
                    var failure = function () {
                    };
                    if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e') {
                        var search = query.term.slice(2);
                        if (!!$scope.changedReRoutStockDataBean && !!$scope.changedReRoutStockDataBean.service && !!$scope.changedReRoutStockDataBean.service.nodeId) {
                            ReRoutStockService.retrieveAllotToByActivityFlowNodeId($scope.changedReRoutStockDataBean.service.nodeId, success, failure);
                        }
                    }

                }
            };
            $scope.userSingleselectComponentForInStockOf = {
                multiple: true,
                closeOnSelect: false,
                placeholder: 'Select',
                maximumSelectionSize: 1,
                initSelection: function (element, callback) {
                    var data = [];
                    callback(data);
                },
                formatResult: function (item) {
                    return item.text;
                },
                formatSelection: function (item) {
                    return item.text;
                },
                query: function (query) {
                    var selected = query.term;
                    $scope.inStockOf = [];
                    var success = function (data) {
                        if (data.length !== 0) {
                            angular.forEach(data, function (item) {
                                $scope.inStockOf.push({
                                    id: item.value + ":" + item.description,
                                    text: item.label

                                });
                            });
                        }
                        query.callback({
                            results: $scope.inStockOf
                        });
                    };
                    var failure = function () {
                    };
                    if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e') {
                        var search = query.term.slice(2);
                        if (!!$scope.changedReRoutStockDataBean && !!$scope.changedReRoutStockDataBean.service && !!$scope.changedReRoutStockDataBean.service.nodeId) {
                            AssetService.retrieveUserList(search.trim(), success, failure);
                        }
                    }

                }
            };
            $scope.removeAllotTo = function () {
                $scope.changedReRoutStockDataBean.allotTo = {};
                $scope.allotTo = [];
//                $("#allotTo").select2('val', []);
                $("#allotTo").select2('data', []);
            }
            $scope.reset = function (sectionTobeReset) {
                if (sectionTobeReset === "searchCustom") {
                    $scope.searchCustom = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("stockreroute");
                    $scope.dbType = {};
                    templateData.then(function (section) {
                        $scope.generalSearchTemplate = section['genralSection'];
                        $scope.searchResetFlag = true;
                    }, function (reason) {

                    }, function (update) {

                    });
                }
            };
            $rootScope.unMaskLoading();

        }

    ]);
});