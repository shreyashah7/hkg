define(['hkg', 'customFieldService', 'changestausService', 'ruleService', 'activityFlowService', 'lotService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'printBarcodeValue', 'dynamicForm', 'accordionCollapse'], function (hkg) {
    hkg.register.controller('ChangeStatusController', ["$rootScope", "$scope", "DynamicFormService", "ChangeStausService", "CustomFieldService", "LotService", "$filter", function ($rootScope, $scope, DynamicFormService, ChangeStausService, CustomFieldService, LotService, $filter) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "changestatus";
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
                var rootNodeRetrieved = function (res) {
                    $scope.rootNodeDesignationIds = res.data.toString();
                };
                LotService.retrieveRootNodeDesignationIds(rootNodeRetrieved);
                $scope.flag = {};
                $scope.status = {};
                $scope.lotIds = [];
                $scope.searchResetFlag = false;
                $scope.packetIds = [];
                $scope.proposedStatus = {};
                $scope.proposedstatusList = [];
                $scope.statusChangeDataBean = {};
                $scope.submitted = false;
                $scope.submittedFlag = false;
                $scope.flag.statusChangeflag = false;
                $scope.flag.rowSelectedflag = false;
                $scope.flag.multipleLotflag = false;
                $scope.searchedData = [];
                $scope.searchedDataFromDb = [];
                $scope.listFilled = false;
                $scope.lotListTodisplay = [];
                $scope.searchCustom = {};
                var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("statuschange");
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
                $scope.mandatoryFieldsByStatusForLot = {
                    "Rejected": "reason_to_reject_lot$DRP$Long"
                };
                $scope.mandatoryFieldsByStatusForPacket = {
                    "Rejected": "reason_to_reject_packet$DRP$Long$POI"
                };
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
                    $scope.searchResetFlag = true;
                    $scope.stockLabelListForUiGrid.push({name: "status", displayName: "Status", minWidth: 200});

                }, function (reason) {

                }, function (update) {

                });
                $scope.statusMap = [];
                ChangeStausService.retrieveStatusMapAndPraposedStatusMap(function (statusMap) {
                    $scope.statusMap = statusMap;
                    angular.forEach($scope.statusMap, function (val, key) {
                        if (key != "Sold" && key != "Transferred" && key != "New/Rough" && key != "$promise" && key != "$resolved") {
                            $scope.proposedstatusList.push(key);
                        }

                    });
                });
            };
            $scope.retrieveSearchedData = function () {
//                console.log("$scope.searchCustom :" + JSON.stringify($scope.searchCustom));
                // Check If whole JSON is empty
                var isModelEmpty = false;
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
                }
                if (($scope.statusChangeDataBean.status !== undefined && $scope.statusChangeDataBean.status !== null) || ($scope.statusChangeDataBean.proposedStatus !== undefined && $scope.statusChangeDataBean.proposedStatus !== null) || mapHasValue) {
                    $rootScope.maskLoading();
                    $scope.stockList = [];
                    $scope.gridOptions.columnDefs = [];
                    $scope.gridOptions.data = [];
                    $scope.statusChangeDataBean.featureCustomMapValue = {};
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
                    $scope.statusChangeDataBean.featureCustomMapValue = finalMap;
                    $scope.statusChangeDataBean.featureMap = featureMap;
                    ChangeStausService.search($scope.statusChangeDataBean, function (res) {
                        $scope.searchedDataFromDb = angular.copy(res);
                        var success = function ()
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
                                        itr.categoryCustom["status"] = itr.status;
                                    }
                                });
                                $scope.stockList.push(itr.categoryCustom);
                            });
                            $scope.gridOptions.data = $scope.stockList;
                            $scope.gridOptions.columnDefs = $scope.stockLabelListForUiGrid;
                            $scope.listFilled = true;
                            $scope.flag.configSearchFlag = false;
                            window.setTimeout(function () {
                                $(window).resize();
                                $(window).resize();
                            }, 100);
                        };

                        DynamicFormService.convertorForCustomField($scope.searchedDataFromDb, success);
                        $rootScope.unMaskLoading();
                    }, function () {
                        var msg = "Could not retrieve, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    });
                } else {
                    $scope.flag.configSearchFlag = true;
                    var msg = "Please select atleast one search criteria for search";
                    var type = $rootScope.warning;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                }
            };
            $scope.initChangeStatusForm = function (statusChangeForm) {
                $scope.statusChangeForm = statusChangeForm;
            };
            $scope.onCanelOfSearch = function () {
                if ($scope.statusChangeForm != null) {
                    $scope.statusChangeForm.$dirty = false;
                }
                $scope.statusChangeForm.$setPristine();
                $scope.listFilled = false;
                $scope.searchResetFlag = false;
                $scope.reset("searchCustom");
                $scope.initializeData();
                $scope.statusChangeForm.$setPristine();
            };

            $scope.checkMandatoryFields = function (status) {
                if (status === "Rejected") {
                    if ($scope.noOfLots > 0 && ($scope.noOfPackets === undefined || $scope.noOfPackets === 0)) {
                        angular.forEach($scope.mandatoryFieldsByStatusForLot, function (fieldModel) {
                            if (!$scope.flag.staticFieldMissing) {
                                var isFieldExists = false;
                                angular.forEach($scope.generalLotEditTemplate, function (field) {
                                    if (field.model === fieldModel) {
                                        isFieldExists = true;
                                    }
                                });
                                if (!isFieldExists) {
                                    $scope.flag.staticFieldMissing = true;
                                }
                            }
                        });
                    } else if ($scope.noOfPackets > 0 && ($scope.noOfLots === undefined || $scope.noOfLots === 0)) {
                        angular.forEach($scope.mandatoryFieldsByStatusForPacket, function (fieldModel) {
                            if (!$scope.flag.staticFieldMissing) {
                                var isFieldExists = false;
                                angular.forEach($scope.generalPacketEditTemplate, function (field) {
                                    if (field.model === fieldModel) {
                                        isFieldExists = true;
                                    }
                                });
                                if (!isFieldExists) {
                                    $scope.flag.staticFieldMissing = true;
                                }
                            }
                        });
                    }
                } else {
                    $scope.flag.staticFieldMissing = false;
                }
            };
            $scope.changeStatus = function () {

                $scope.selectedRows = $scope.gridApi.selection.getSelectedRows();
                $scope.submitted = true;
                $scope.isMultipleLotsOrPackets = true;
                $scope.generalStatus = {};
                $scope.flag.generalStatusFlag = true;
//                $scope.invoiceCustom = {};
                $scope.generaInvoiceTemplate = [];
                $scope.generaParcelTemplate = [];
                $scope.generalLotTemplate = [];
                $scope.generalLotEditTemplate = [];
                $scope.generalPacketEditTemplate = [];
                $scope.flag.staticFieldMissing = false;
                $scope.noOfLots = 0;
                $scope.noOfPackets = 0;
//                $scope.parcelCustom = {};
//                $scope.lotCustom = {};
                $scope.lotEditCustom = {};
                $scope.packetEditCustom = {};
                $scope.statusChangeDataBean = {};
                if ($scope.statusChangeForm.$valid) {
                    $scope.generalStatus = $scope.selectedRows[0]["status"];
                    $scope.statusListToBeChange = $scope.statusMap[$scope.generalStatus];
                    for (var row = 0; row < $scope.selectedRows.length; row++) {
                        if ($scope.selectedRows[row]["~@packetid"] != null) {
                            $scope.noOfPackets = $scope.noOfPackets + 1;
                        }
                        else if ($scope.selectedRows[row]["~@packetid"] === null && $scope.selectedRows[row]["~@lotid"] != null) {
                            $scope.noOfLots = $scope.noOfLots + 1;
                        }
                        if (!!$scope.generalStatus && $scope.generalStatus != $scope.selectedRows[row]["status"]) {

                            delete $scope.selectedRows;
                            $scope.selectedRows = [];
                            $scope.gridApi.selection.clearSelectedRows();
                            $scope.flag.generalStatusFlag = false;
                            var msg = "Only select either lots or packets of same status";
                            var type = $rootScope.warning;
                            $rootScope.addMessage(msg, type);
                            $scope.flag.statusChangeflag = false;
                        }
                    }

                    if ($scope.noOfLots > 0 && $scope.noOfPackets > 0) {
                        delete $scope.selectedRows;
                        $scope.selectedRows = [];
                        $scope.gridApi.selection.clearSelectedRows();
                        var msg = "Only select either lots or packets";
                        var type = $rootScope.warning;
                        $rootScope.addMessage(msg, type);
                        $scope.flag.statusChangeflag = false;
                    }
                    else if ($scope.noOfLots > 0 && $scope.noOfPackets === 0 && $scope.flag.generalStatusFlag) {
                        $scope.flag.displayEditLotflag = true;
                        $scope.flag.statusChangeflag = true;
                    }
                    else if ($scope.noOfPackets > 0 && $scope.noOfLots === 0 && $scope.flag.generalStatusFlag) {
                        $scope.flag.displayEditPacketflag = true;
                        $scope.flag.statusChangeflag = true;
                    }
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

                        var mapToSent = {};
                        var invoiceDbFieldName = [];
                        var parcelDbFieldName = [];
                        var lotDbFieldName = [];
                        var packetDbFieldName = [];
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
                        $scope.payload["packetids"] = $scope.packetIds;

//                        console.log("rsult :::" + JSON.stringify(result));
                        $rootScope.maskLoading();

                        CustomFieldService.retrieveDesignationBasedFields("statuschange", function (response) {
                            var templateDataInvoice = {};
                            templateDataInvoice = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
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

                                var templateDataParcel = {};
                                templateDataParcel = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
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

//                                $scope.parcelCustom = $scope.parcelCustomFieldData;
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

                                    var templateDatalot = {};
                                    templateDatalot = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
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

//                                $scope.lotCustom = $scope.lotCustomFieldData;
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

                                        var templateDataPacket = {};
                                        templateDataPacket = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                                        $scope.packetDbType = {};
                                        templateDataPacket.then(function (section) {
                                            $scope.generalPacketTemplate = section['genralSection'];
                                            var packetFields = [];
                                            var result = Object.keys(response).map(function (key, value) {
                                                angular.forEach(this[key], function (itr) {
                                                    if (key === 'Packet#P#') {
                                                        packetFields.push({Packet: itr});
                                                    }
                                                });
                                            }, response);
                                            $scope.generalPacketTemplate = DynamicFormService.retrieveCustomData($scope.generalPacketTemplate, packetFields);

//                                $scope.lotCustom = $scope.lotCustomFieldData;
                                            angular.forEach($scope.generalPacketTemplate, function (itr) {
                                                if (itr.model) {
                                                    packetDbFieldName.push(itr.model);
                                                    $scope.parentGridOptions.columnDefs.push({name: itr.model, displayName: itr.label,
                                                        cellTemplate: '<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                                                }
                                            });
                                            if (packetDbFieldName.length > 0) {
                                                mapToSent['packetDbFieldName'] = packetDbFieldName;
                                            }

                                            var templateDataLotEdit = {};
                                            templateDataLotEdit = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                                            $scope.lotEditDbType = {};
                                            templateDataLotEdit.then(function (section) {
                                                $scope.generalLotEditTemplate = section['genralSection'];
                                                var editLotfieldIds = [];
                                                var result = Object.keys(response).map(function (key, value) {
                                                    angular.forEach(this[key], function (itr) {
                                                        if (key === 'Lot') {
                                                            editLotfieldIds.push({Lot: itr});
                                                        }
                                                    });
                                                }, response);
                                                $scope.generalLotEditTemplate = DynamicFormService.retrieveCustomData($scope.generalLotEditTemplate, editLotfieldIds);
                                                $scope.editResetFlag = true;
                                                if ($scope.generalLotEditTemplate.length > 0) {
                                                    $scope.flag.customFieldGenerated = true;
                                                }

                                                var templateDataPacketEdit = {};
                                                templateDataPacketEdit = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                                                $scope.packetEditDbType = {};
                                                templateDataPacketEdit.then(function (section) {
                                                    $scope.generalPacketEditTemplate = section['genralSection'];
                                                    var editPacketfieldIds = [];
                                                    var result = Object.keys(response).map(function (key, value) {
                                                        angular.forEach(this[key], function (itr) {
                                                            if (key === 'Packet') {
                                                                editPacketfieldIds.push({Packet: itr});
                                                            }
                                                        });
                                                    }, response);
                                                    $scope.generalPacketEditTemplate = DynamicFormService.retrieveCustomData($scope.generalPacketEditTemplate, editPacketfieldIds);
                                                    $scope.editResetFlag = true;
                                                    if ($scope.generalPacketEditTemplate.length > 0) {
                                                        $scope.flag.customFieldGenerated = true;
                                                    }
                                                    var payload = {"payload": $scope.payload, "fields": mapToSent};
//                                                    console.log("payload :"+JSON.stringify(payload));
                                                    ChangeStausService.retrieveStockByLotIdOrPacketId(payload, function (result) {
//                                                        console.log("result :::::"+JSON.stringify(result));
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
//                                                            console.log("$scope.stocks :"+JSON.stringify($scope.stocks));
                                                            angular.forEach($scope.stocks, function (currentStockDetails) {
                                                                console.log("currentStockDetails :" + JSON.stringify(currentStockDetails.stock));
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
                                                            console.log("$scope.gridRecords :" + JSON.stringify($scope.gridRecords));
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
                                }, function (reason) {
                                }, function (update) {
                                });
                            }, function (reason) {
                            }, function (update) {
                            });
                            $scope.searchResetFlag = false;
                            $scope.reset("searchCustom");
                            $rootScope.unMaskLoading();
                        }, function () {
                            $rootScope.unMaskLoading();
                            var msg = "Failed to retrieve data";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                        });

                    }

                }
            };
            $scope.onBack = function (statusChangeForm) {
                if (statusChangeForm != null) {
                    statusChangeForm.$dirty = false;
                    $scope.flag.statusChangeflag = false;
                }
                $scope.initializeData();
                $scope.editResetFlag = false;
                $scope.reset("changeStatusTemplate");
                $scope.submitted = false;
            };
            $scope.onSave = function (statusChangeForm) {
                $scope.submittedFlag = true;
                if ((!jQuery.isEmptyObject($scope.lotEditCustom) || !jQuery.isEmptyObject($scope.packetEditCustom)) || ($scope.statusChangeDataBean.statusToBeChange !== undefined && $scope.statusChangeDataBean.statusToBeChange !== null)) {
                    $rootScope.maskLoading();
                    $scope.submitted = true;
                    $scope.submittedFlag = true;
                    $scope.flag.statusChangeflag = false;
                    var stockList = [];
                    var payload = {};
                    payload.lotCustom = $scope.lotEditCustom;
                    payload.packetCustom = $scope.packetEditCustom;
                    payload.lotdbType = $scope.lotEditDbType;
                    payload.packetdbType = $scope.packetEditDbType;
                    payload.lotIds = $scope.lotIds;
                    payload.packetIds = $scope.packetIds;
                    payload.status = $scope.statusChangeDataBean.statusToBeChange;
                    $scope.statusChangeForm.$dirty = false;
                    if (statusChangeForm.$valid) {
                        ChangeStausService.onSave(payload, function (response) {
                            var msg = "Status changed successfully.";
                            var type = $rootScope.success;
                            $scope.initializeData();
                            $scope.editResetFlag = false;
                            $scope.reset("changeStatusTemplate");
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        }, function () {
                            var msg = "Could not changed status, please try again.";
                            var type = $rootScope.error;
                            $scope.initializeData();
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        });
                    }
                } else {
                    var msg = "Please select Status to be change.";
                    var type = $rootScope.warning;
                    $rootScope.addMessage(msg, type);
                }
            };
            $scope.reset = function (sectionTobeReset) {
                if (sectionTobeReset === "searchCustom") {
                    $scope.searchCustom = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("statuschange");
                    $scope.dbType = {};
                    templateData.then(function (section) {
                        $scope.generalSearchTemplate = section['genralSection'];
                        $scope.searchResetFlag = true;
                    }, function (reason) {

                    }, function (update) {

                    });
                } else if (sectionTobeReset === "changeStatusTemplate") {
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
                    var templateDataPacketEdit = {};
                    templateDataPacketEdit = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                    $scope.packetEditDbType = {};
                    templateDataPacketEdit.then(function (section) {
                        $scope.generalPacketEditTemplate = section['genralSection'];
                        $scope.packetEditCustom = {};
                        $scope.editResetFlag = true;
                    }, function (reason) {
                    }, function (update) {
                    });
                }
            };
            $rootScope.unMaskLoading();
        }]);
});