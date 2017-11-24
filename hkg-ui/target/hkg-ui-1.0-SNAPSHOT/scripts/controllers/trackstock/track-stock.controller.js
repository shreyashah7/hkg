define(['hkg', 'customFieldService', 'trackstockService', 'ruleService', 'activityFlowService', 'lotService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive','printBarcodeValue', 'dynamicForm', 'accordionCollapse', 'ngload!flowChart'], function(hkg) {
    hkg.register.controller('TrackStockController', ["$rootScope", "$scope", "DynamicFormService", "TrackStockService", "RuleService", "ActivityFlowService", "CustomFieldService", "LotService", "$filter", "$location", function($rootScope, $scope, DynamicFormService, TrackStockService, RuleService, ActivityFlowService, CustomFieldService, LotService, $filter, $location) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "trackstock";
            $rootScope.activateMenu();
            console.log("loaded controller");
            var featureMap = {};
            $scope.initializeData = function() {
                $scope.submitted = false;
                $scope.flag = {};
                $scope.flag.rowSelectedflag = false;
                $scope.flag.multipleLotflag = false;
                $scope.searchedDataFromDb = [];
                $scope.trackStockDataBean = {};
                $scope.searchCustom = {};
                var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("stocktrack");
                $scope.dbType = {};
                $scope.stockLabelListForUiGrid = [];
                $scope.gridOptions = {};
                $scope.gridOptions.enableFiltering = true;
                $scope.gridOptions.multiSelect = false;
                $scope.gridOptions.enableRowSelection = true;
                $scope.gridOptions.enableSelectAll = true;
                $scope.gridOptions.columnDefs = [];
                $scope.gridOptions.data = [];
                $scope.gridOptions.selectedItems = [];
                $scope.stockList = [];
                $scope.selectedRows = [];
                $scope.lotIds = [];
                $scope.packetsIds = [];
                $scope.flag.displayEditPacketflag = false;
                $scope.flag.displayEditLotflag = false;
                $scope.flag.trackStockflag = false;
                $scope.gridOptions.onRegisterApi = function(gridApi) {
                    //set gridApi on scope
                    $scope.gridApi = gridApi;
                    gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                        if ($scope.gridApi.selection.getSelectedRows().length > 0) {
                            $scope.flag.rowSelectedflag = true;
                        } else {
                            $scope.flag.rowSelectedflag = false;
                        }
                    });
                    gridApi.selection.on.rowSelectionChangedBatch($scope, function(rows) {
                        if ($scope.gridApi.selection.getSelectedRows().length > 0) {
                            $scope.flag.rowSelectedflag = true;
                        } else {
                            $scope.flag.rowSelectedflag = false;
                        }
                    });
                };
                templateData.then(function(section) {
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

                }, function(reason) {
                    console.log("reason :" + reason);
                }, function(update) {
                    console.log("update :" + update);
                });
            };

            $scope.$on('$viewContentLoaded', function() {
                if ($location.path() === '/trackstockprint') {
                    $scope.displayActivityFlow();
                } else if ($location.path() === '/totalstock') {
                    $scope.retrieveVersionList();
                }
            });

            $scope.initTrackStockForm = function(trackStockForm) {
                $scope.trackStockForm = trackStockForm;
            };

            $scope.retrieveSearchedData = function() {
                $scope.selectOneParameter = false;
                $scope.searchedData = [];
                $scope.searchedDataFromDb = [];
                $scope.listFilled = false;
                $scope.stockList = [];
                $scope.gridOptions.columnDefs = [];
                $scope.gridOptions.data = [];
                if (Object.getOwnPropertyNames($scope.searchCustom).length > 0) {
                    $rootScope.maskLoading();
                    var mapHasValue = false;
                    for (var prop in $scope.searchCustom) {
                        if (typeof $scope.searchCustom[prop] === 'object' && $scope.searchCustom[prop] !== null) {
                            var toString = angular.copy($scope.searchCustom[prop].toString());
                            if (typeof toString === 'string' && toString !== null && toString !== undefined && toString.length > 0) {
                                mapHasValue = true;
                                break;
                            }
                        }
                        if (typeof $scope.searchCustom[prop] === 'string' && $scope.searchCustom[prop] !== null && $scope.searchCustom[prop] !== undefined && $scope.searchCustom[prop].length > 0) {
                            mapHasValue = true;
                            break;
                        }
                        if (typeof $scope.searchCustom[prop] === 'number' && !!($scope.searchCustom[prop])) {
                            mapHasValue = true;
                            break;
                        }
                        if (typeof $scope.searchCustom[prop] === 'boolean') {
                            mapHasValue = true;
                            break;
                        }
                    }
                    if (mapHasValue) {
                        $scope.trackStockDataBean.featureCustomMapValue = {};
                        $scope.map = {};
                        var finalMap = {};
                        var searchResult = DynamicFormService.convertSearchData($scope.searchInvoiceTemplate, $scope.searchParcelTemplate, $scope.searchLotTemplate, $scope.searchPacketTemplate, $scope.searchCustom);
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
                        $scope.trackStockDataBean.featureCustomMapValue = finalMap;
                        $scope.trackStockDataBean.featureMap = featureMap;
                        console.log("$scope.trackStockDataBean :"+JSON.stringify($scope.trackStockDataBean));
                        TrackStockService.search($scope.trackStockDataBean, function(res) {
                            console.log("result ::::"+JSON.stringify(res));
                            $scope.searchedDataFromDb = angular.copy(res);
                            if ($scope.generalSearchTemplate === null || $scope.generalSearchTemplate === undefined) {
                                $scope.flag.configSearchFlag = true;
                            } else {
                                var success = function()
                                {
                                    angular.forEach($scope.searchedDataFromDb, function(itr) {
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
                                                itr.categoryCustom["status"] = itr.status;
                                            }
                                        });
                                        $scope.stockList.push(itr.categoryCustom);
                                    });
                                    $scope.gridOptions.data = $scope.stockList;
                                    $scope.gridOptions.columnDefs = $scope.stockLabelListForUiGrid;
                                    $scope.listFilled = true;
                                    $scope.flag.configSearchFlag = false;
                                    window.setTimeout(function() {
                                        $(window).resize();
                                        $(window).resize();
                                    }, 100);
                                };
                                DynamicFormService.convertorForCustomField($scope.searchedDataFromDb, success);
                            }
                            $rootScope.unMaskLoading();
                        }, function() {
                            var msg = "Could not retrieve, please try again.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        });
                    } else {
                        var msg = "Please select atleast one search criteria for search";
                        var type = $rootScope.warning;
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    }
                } else {
                    var msg = "Please select atleast one search criteria for search";
                    var type = $rootScope.warning;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                }
                $rootScope.unMaskLoading();
            };

            $scope.onCancelOfSearch = function() {
                if ($scope.trackStockForm !== null) {
                    $scope.trackStockForm.$dirty = false;
                }
                $scope.listFilled = false;
                $scope.searchResetFlag = false;
                $scope.reset("searchCustom");
                $scope.initializeData();
                $scope.trackStockForm.$setPristine();
            };
            $scope.reset = function(sectionTobeReset) {
                if (sectionTobeReset === "searchCustom") {
                    $scope.searchCustom = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("stocktrack");
                    $scope.dbType = {};
                    templateData.then(function(section) {
                        $scope.generalSearchTemplate = section['genralSection'];
                        $scope.searchResetFlag = true;
                    }, function(reason) {
                        console.log("reason :" + reason);
                    }, function(update) {
                        console.log("update :" + update);
                    });
                }
            };

            //Adds days in date and clears time
            $scope.getDateWithoutTimeStamp = function(date) {
                if (date !== undefined) {
                    date.setHours(0);
                    date.setMinutes(0);
                    date.setSeconds(0);
                    date.setMilliseconds(0);
                    return date.getTime();
                }
                return null;
            };

            $scope.addDaysToDate = function(daysToAdd) {
                var date = new Date($scope.getDateWithoutTimeStamp($rootScope.getCurrentServerDate()));
                date.setDate(date.getDate() + daysToAdd);
                return date.getTime();
            }

            $scope.formatDate = function(date) {

                if ($scope.getDateWithoutTimeStamp(new Date(date)) === $scope.addDaysToDate(0)) {
                    return "Today";
                }
                else if ($scope.getDateWithoutTimeStamp(new Date(date)) === $scope.addDaysToDate(-1)) {
                    return "Yesterday";
                } else if ($scope.getDateWithoutTimeStamp(new Date(date)) === $scope.addDaysToDate(1)) {
                    return "Tomorrow";
                }
                else {
                    return $filter("date")(date, $rootScope.dateFormat);
                }

            };

            $scope.trackStock = function() {
                $scope.selectedRows = $scope.gridApi.selection.getSelectedRows();
                $scope.submitted = true;
                $scope.flag.trackStockflag = true;
                if ($scope.selectedRows.length === 1) {
                    $scope.payload = {};
                    $scope.payload["lotid"] = $scope.selectedRows[0]["~@lotid"];
                    $scope.payload["packetid"] = $scope.selectedRows[0]["~@packetid"];
                    var trackStockDataBean = {stock: {lotId: null}, invoiceDataBean: {id: null}};
                    var dbFeatureMap = {};
                    if ($scope.selectedRows[0]["~@lotid"] !== undefined && $scope.selectedRows[0]["~@lotid"] !== null && ($scope.selectedRows[0]["~@packetid"] === undefined || $scope.selectedRows[0]["~@packetid"] === null)) {
                        trackStockDataBean.stock.lotId = $scope.selectedRows[0]["~@lotid"];
                        trackStockDataBean.invoiceDataBean.id = 'lot';
                    } else if ($scope.selectedRows[0]["~@packetid"] !== undefined && $scope.selectedRows[0]["~@packetid"] !== null) {
                        trackStockDataBean.stock.lotId = $scope.selectedRows[0]["~@packetid"];
                        trackStockDataBean.invoiceDataBean.id = 'packet';
                    }

                    TrackStockService.retrieveLotOrPacketActivites($scope.payload, function(res) {
                        $scope.stockTrackDetails = angular.copy(res);
                        localStorage.setItem("stockDts", JSON.stringify($scope.stockTrackDetails));
                    }, function() {
                    });

                    $rootScope.maskLoading();
//                    $scope.stock = result["stock"];
//                    $scope.invoiceCustomFieldData = $scope.stock["custom3"];
//                    $scope.parcelCustomFieldData = $scope.stock["custom4"];
//                    $scope.lotCustomFieldData = $scope.stock["custom1"];
                    CustomFieldService.retrieveDesignationBasedFields("stocktrack", function(response) {
                        var templateDataInvoice = {};
                        templateDataInvoice = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                        $scope.invoiceDbType = {};
                        templateDataInvoice.then(function(section) {
                            $scope.generaInvoiceTemplate = section['genralSection'];
                            var invoiceFields = [];
                            var result = Object.keys(response).map(function(key, value) {
                                angular.forEach(this[key], function(itr) {
                                    if (key === 'Invoice#P#') {
                                        invoiceFields.push({Invoice: itr});
                                    }
                                });
                            }, response);
                            $scope.generaInvoiceTemplate = DynamicFormService.retrieveCustomData($scope.generaInvoiceTemplate, invoiceFields);
                            angular.forEach($scope.generaInvoiceTemplate, function(itr) {
                                dbFeatureMap[itr.model] = itr.featureName;
                            });
//                            $scope.invoiceCustom = $scope.invoiceCustomFieldData;
                        }, function(reason) {
                        }, function(update) {
                        });
                        var templateDataParcel = {};
                        templateDataParcel = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
                        $scope.parcelDbType = {};
                        templateDataParcel.then(function(section) {
                            $scope.generaParcelTemplate = section['genralSection'];
                            var parcelFields = [];
                            var result = Object.keys(response).map(function(key, value) {
                                angular.forEach(this[key], function(itr) {
                                    if (key === 'Parcel#P#') {
                                        parcelFields.push({Parcel: itr});
                                    }
                                });
                            }, response);
                            $scope.generaParcelTemplate = DynamicFormService.retrieveCustomData($scope.generaParcelTemplate, parcelFields);
                            angular.forEach($scope.generaParcelTemplate, function(itr) {
                                dbFeatureMap[itr.model] = itr.featureName;
                            });
//                            $scope.parcelCustom = $scope.parcelCustomFieldData;
                        }, function(reason) {
                        }, function(update) {
                        });
                        var templateDataPacket = {};
                        templateDataPacket = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                        $scope.packetDbType = {};
                        templateDataPacket.then(function(section) {
                            $scope.generalPacketTemplate = section['genralSection'];
                            var packetFields = [];
                            var result = Object.keys(response).map(function(key, value) {
                                angular.forEach(this[key], function(itr) {
                                    if (key === 'Packet#P#') {
                                        packetFields.push({Packet: itr});
                                    }
                                });
                            }, response);
                            $scope.generalPacketTemplate = DynamicFormService.retrieveCustomData($scope.generalPacketTemplate, packetFields);
                            angular.forEach($scope.generalPacketTemplate, function(itr) {
                                dbFeatureMap[itr.model] = itr.featureName;
                            });
//                            $scope.packetCustom = $scope.packetCustomFieldData;
                        }, function(reason) {
                        }, function(update) {
                        });
                        var templateDatalot = {};
                        templateDatalot = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                        $scope.lotDbType = {};
                        templateDatalot.then(function(section) {
                            $scope.generalLotTemplate = section['genralSection'];
                            var lotFields = [];
                            var result = Object.keys(response).map(function(key, value) {
                                angular.forEach(this[key], function(itr) {
                                    if (key === 'Lot#P#') {
                                        lotFields.push({Lot: itr});
                                    }
                                });
                            }, response);
                            $scope.generalLotTemplate = DynamicFormService.retrieveCustomData($scope.generalLotTemplate, lotFields);
                            angular.forEach($scope.generalLotTemplate, function(itr) {
                                dbFeatureMap[itr.model] = itr.featureName;
                            });
                            trackStockDataBean.featureMap = dbFeatureMap;
                            TrackStockService.retrieveStockByLotIdOrPacketId(trackStockDataBean, function(result) {
                                console.log("result :"+JSON.stringify(result));
                                $scope.stock = result["stock"];
                                $scope.invoiceCustom = $scope.stock["custom3"];
                                $scope.parcelCustom = $scope.stock["custom4"];
                                $scope.lotCustom = $scope.stock["custom1"];
                                $scope.packetCustom = $scope.stock["custom5"];
                                 console.log("packetCustom :"+JSON.stringify($scope.packetCustom));
                                $rootScope.unMaskLoading();
                            }
                            , function() {
                                var msg = "Could not retrieve, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                                $rootScope.unMaskLoading();
                            });
                            $scope.lotCustom = $scope.lotCustomFieldData;
                           
                        }, function(reason) {
                        }, function(update) {
                        });
                        $scope.searchResetFlag = false;
                        $scope.reset("searchCustom");
                    }, function() {
                        $rootScope.unMaskLoading();
                        var msg = "Failed to retrieve data";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });



                }
            };

            $scope.onBack = function(trackStockForm) {
                if (trackStockForm !== null) {
                    trackStockForm.$dirty = false;
                    $scope.flag.trackStockflag = false;
                }
                $scope.listFilled = false;
                $scope.initializeData();
                $scope.submitted = false;
            };

            $scope.generateReportOfActivity = function(extension) {
                $scope.payload = {};
                $scope.payload["lotid"] = $scope.selectedRows[0]["~@lotid"];
                $scope.payload["packetid"] = $scope.selectedRows[0]["~@packetid"];
                $scope.payload["extension"] = extension;
                $scope.generateReport($scope.payload, extension);
            };
            $scope.generateReport = function(data, extension) {
                $rootScope.maskLoading();
                TrackStockService.generateReportOfActivities(data, function(result) {
                    $rootScope.unMaskLoading();
                    if (!!result) {
                        if (result[0] === 'T') {
                            window.location.href = $rootScope.appendAuthToken($rootScope.centerapipath + "trackstock/downloadpdfreportofactivity?extension=" + extension);
                        }
                    } else {
                        var msg = "Failed to download report";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    }
                }, function() {
                    $rootScope.unMaskLoading();
                    var msg = "Failed to download report";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                });
            };

            $scope.displayActivityFlow = function() {
                if (localStorage.getItem("stockDts") !== undefined) {
                    $scope.stockDts = JSON.parse(localStorage.getItem("stockDts"));
                }
                if ($scope.stockDts !== undefined && $scope.stockDts !== null && $scope.stockDts.length > 0) {
                    if ($scope.stockDts[$scope.stockDts.length - 1].activityVersion !== undefined || $scope.stockDts[$scope.stockDts.length - 1].activityVersion !== null) {
                        var versionId = $scope.stockDts[$scope.stockDts.length - 1].activityVersion;
                        $scope.nodeIds = [];
                        angular.forEach($scope.stockDts, function(item) {
                            $scope.nodeIds.push(item.nodeId);
                        });
                        $scope.send = {
                            "nodeIds": $scope.nodeIds,
                            "versionId": versionId
                        };
                        $scope.retrieveActivityFlowVersion($scope.send);
                    }

                }
            };

            $scope.retrieveActivityFlowVersion = function(data) {
                chartDataModel = {
                    "isreadonly": true,
                    "groups": [
                    ],
                    "connections": [
                    ]
                };
                if (data.versionId !== null) {
                    $rootScope.maskLoading();
                    TrackStockService.retrieveActivityFlowVersion(data, function(res) {
                        $rootScope.unMaskLoading();
                        if (res.data !== null && res.data.activityFlowGroups !== null && res.data.activityFlowGroups.length > 0) {
                            angular.forEach(res.data.activityFlowGroups, function(item) {
                                if (item.nodeDataBeanList !== null) {
                                    $scope.nodes = [];
                                    angular.forEach(item.nodeDataBeanList, function(nodeItem) {
                                        if (nodeItem.designationId) {
                                            $scope.nodeDataModel = {
                                                "name": nodeItem.associatedServiceName,
                                                "id": nodeItem.nodeId,
                                                "serviceCode": nodeItem.associatedServiceCode,
                                                "x": nodeItem.x,
                                                "y": nodeItem.y,
                                                "designation": nodeItem.designationId,
                                                "modifier": nodeItem.modifier,
                                                "nonModifierCodes": nodeItem.nonMandatoryModifier,
                                                "plans": nodeItem.plans,
                                                "noOfPlans": nodeItem.noOfPlans,
                                                "inputConnectors": [
                                                    {
                                                        "name": "X"
                                                    }
                                                ],
                                                "outputConnectors": [
                                                    {
                                                        "name": "1"
                                                    }
                                                ],
                                                "active": nodeItem.selected
                                            };
                                        } else {
                                            $scope.nodeDataModel = {
                                                "name": nodeItem.associatedServiceName,
                                                "id": nodeItem.nodeId,
                                                "serviceCode": nodeItem.associatedServiceCode,
                                                "x": nodeItem.x,
                                                "y": nodeItem.y,
                                                "designation": nodeItem.designationId,
                                                "modifier": nodeItem.modifier,
                                                "nonModifierCodes": nodeItem.nonMandatoryModifier,
                                                "plans": nodeItem.plans,
                                                "noOfPlans": nodeItem.noOfPlans,
                                                "inputConnectors": [
                                                    {
                                                        "name": "X"
                                                    }
                                                ],
                                                "outputConnectors": [
                                                    {
                                                        "name": "1"
                                                    }
                                                ],
                                                "active": nodeItem.selected
                                            };
                                        }
                                        $scope.nodes.push($scope.nodeDataModel);
                                    });
                                    var groupDataModel = {
                                        "name": item.flowGroupName,
                                        "description": item.description,
                                        "id": item.groupId,
                                        "x": item.x,
                                        "y": item.y,
                                        "nodes": [],
                                        "active": item.selected
                                    };
                                    angular.forEach($scope.nodes, function(item) {
                                        groupDataModel.nodes.push(item);
                                    })
                                    chartDataModel.groups.push(groupDataModel);
                                } else {
                                    var groupDataModel = {
                                        "name": item.flowGroupName,
                                        "description": item.description,
                                        "id": item.groupId,
                                        "x": item.x,
                                        "y": item.y,
                                        "nodes": [],
                                        "active": item.selected
                                    };
                                    chartDataModel.groups.push(groupDataModel);
                                }
                            });
                            if (res.data.activityFlowNodeRoutes !== null) {
                                angular.forEach(res.data.activityFlowNodeRoutes, function(item) {
                                    var connectionDataModel = {
                                        "id": item.nodeRouteId,
                                        "ruleset": item.nodeRouteStatus,
                                        "source": {
                                            "nodeID": item.curentNode,
                                            "connectorIndex": 0
                                        },
                                        "dest": {
                                            "nodeID": item.nextNode,
                                            "connectorIndex": 0
                                        },
                                        "active": item.selected
                                    };
                                    chartDataModel.connections.push(connectionDataModel);
                                });
                            }
                            $scope.onlyViewModel = new flowchart.ChartViewModel(chartDataModel);
                        } else {
                            $scope.chartViewModel = new flowchart.ChartViewModel(chartDataModel);
                        }
                    });
                }
            };

            $scope.retrieveVersionList = function() {
                $scope.totalStock = {};
                TrackStockService.retrieveVersionList(function(res) {
                    if (res.data !== undefined) {
                        $scope.versionList = res.data["activityflowbycompany"];
                        $scope.totalStock.version = $scope.versionList[0].value;
                        $scope.retrieveTotalStockByVersion();
                    }
                });
            };

            $scope.retrieveTotalStockByVersion = function() {
                var version = $scope.totalStock.version;
                $scope.totalStockDtls = [];
                TrackStockService.retrieveTotalStockByVersion(version, function(res) {
                    $scope.totalStockDtls = angular.copy(res.data);
                });
            };

            $scope.onVersionChange = function() {
                $scope.retrieveTotalStockByVersion();
            };

            $scope.openStockDetails = function(selectedStock) {
                if (selectedStock !== undefined) {
                    $scope.selectedNodeId = selectedStock.nodeId;
                    $scope.activityName = selectedStock.activityName;
                    $scope.serviceName = selectedStock.serviceName;
                    $scope.totalDtls = [];
                    TrackStockService.retrieveTotalStockByNode(selectedStock.nodeId, function(res) {
                        if (res !== undefined) {
                            $scope.totalDtls = angular.copy(res.data);
                            $("#totalStockDetailsPopup").modal("show");
                        }
                    });
                }
            };

            $scope.hideDetailsPopup = function() {
                $scope.totalDtls = [];
                $("#totalStockDetailsPopup").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            };

            $scope.previousPage = function() {
                $scope.totalDtls = [];
                $scope.initializeData();
                $location.path("/trackStock");
            };

            $scope.generateStockDetailsReport = function(extension) {
                $scope.payload = {};
                $scope.payload["versionId"] = $scope.totalStock.version;
                $scope.payload["extension"] = extension;
                $scope.generateReportOfStockDetails($scope.payload, extension);
            };
            $scope.generateReportOfStockDetails = function(data, extension) {
                $rootScope.maskLoading();
                TrackStockService.generateStockDetailsReport(data, function(result) {
                    $rootScope.unMaskLoading();
                    if (!!result) {
                        if (result[0] === 'T') {
                            window.location.href = $rootScope.appendAuthToken($rootScope.centerapipath + "trackstock/downloadpdfofstockdetails?extension=" + extension);
                        }
                    } else {
                        var msg = "Failed to download report";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    }
                }, function() {
                    $rootScope.unMaskLoading();
                    var msg = "Failed to download report";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                });
            };

            $scope.generateTotalStockDetailReport = function(extension) {
                console.log("insie")
                $scope.payload = {};
                $scope.payload["nodeId"] = $scope.selectedNodeId;
                $scope.payload["extension"] = extension;
                $scope.payload["activity"] = $scope.activityName;
                $scope.payload["service"] = $scope.serviceName;
                $scope.totalStockDetailReport($scope.payload, extension);
            };
            $scope.totalStockDetailReport = function(data, extension) {
                $rootScope.maskLoading();
                TrackStockService.generateTotalStockReport(data, function(result) {
                    $rootScope.unMaskLoading();
                    if (!!result) {
                        if (result[0] === 'T') {
                            window.location.href = $rootScope.appendAuthToken($rootScope.centerapipath + "trackstock/downloadpdfoftotalstockdetails?extension=" + extension);
                        }
                    } else {
                        var msg = "Failed to download report";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    }
                }, function() {
                    $rootScope.unMaskLoading();
                    var msg = "Failed to download report";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                });
            };
            $rootScope.unMaskLoading();
        }]);
});
