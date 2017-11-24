define(['hkg', 'packetService', 'splitstockService', 'customFieldService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'printBarcodeValue', 'dynamicForm', 'accordionCollapse', 'ruleExecutionService'], function (hkg, packetService) {
    hkg.register.controller('SplitPacketController', ["$rootScope", "$scope", "PacketService", "$timeout", "$filter", "$location", "$window", "CustomFieldService", "DynamicFormService", "RuleExecutionService", "SplitStockService", function ($rootScope, $scope, PacketService, $timeout, $filter, $location, $window, CustomFieldService, DynamicFormService, RuleExecutionService, SplitStockService) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "splitPacket";
            $rootScope.activateMenu();
            $scope.Packet = this;
            var featureMap = {};
            var gridApiParcel;
            var featureCustomMap = {};
            $scope.initializeData = function (flag) {
                if (flag) {
                    $rootScope.maskLoading();
                    $scope.searchedData = [];
                    $scope.searchedDataFromDb = [];
                    $scope.listFilled = false;
                    $scope.lotListTodisplay = [];
                    $scope.searchCustom = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("packetsplit");
                    $scope.flag = {};
                    $scope.flag.searchFieldNotAvailable = false;
                    $scope.dbType = {};
                    templateData.then(function (section) {
//                        //console.log("sections::::" + JSON.stringify(section));
                        $scope.searchInvoiceTemplate = [];
                        $scope.searchParcelTemplate = [];
                        $scope.searchLotTemplate = [];
                        $scope.searchPacketTemplate = [];
                        $scope.fieldIdNameMap = {};
                        var invoiceDbFieldName = [];
                        var parcelDbFieldName = [];
                        var lotDbFieldName = [];
                        var packetDbFieldName = [];
                        $scope.generalSearchTemplate = section['genralSection'];
                        $scope.labelListForUiGrid = [];
                        if ($scope.generalSearchTemplate != null && $scope.generalSearchTemplate.length > 0) {
                            featureMap = {};
                            angular.forEach($scope.generalSearchTemplate, function (itr) {
                                var item = angular.copy(itr);
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
                                if (item.fromModel) {
                                    $scope.labelListForUiGrid.push({name: item.fromModel, displayName: item.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.editPacketScreenRule(row, \'' + itr.fromModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                    $scope.fieldIdNameMap[item.fieldId] = item.fromModel;
                                } else if (item.toModel) {
                                    $scope.labelListForUiGrid.push({name: item.toModel, displayName: item.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.editPacketScreenRule(row, \'' + itr.toModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                    $scope.fieldIdNameMap[item.fieldId] = item.toModel;
                                } else if (item.model) {
                                    $scope.labelListForUiGrid.push({name: item.model, displayName: item.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.editPacketScreenRule(row, \'' + itr.model + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                    $scope.fieldIdNameMap[item.fieldId] = item.model;
                                }
                            });
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
                        } else {
                            $scope.flag.searchFieldNotAvailable = true;
                            $scope.dataRetrieved = true;
                        }
                        $scope.searchResetFlag = true;
                        $scope.dataRetrieved = true;
                        $rootScope.unMaskLoading();
                    }, function (reason) {

                    }, function (update) {

                    });
                }
            };
            $scope.initializeData(true);

            $scope.editPacketScreenRule = function (row, colField) {
                var color;
                if (!!row.entity.screenRuleDetailsWithDbFieldName && row.entity.screenRuleDetailsWithDbFieldName[colField] !== undefined && row.entity.screenRuleDetailsWithDbFieldName[colField] !== null) {
                    if ($scope.selectedStock.length === 0 || ($scope.selectedStock[0].$$packetId$$ !== row.entity.$$packetId$$)) {
                        color = row.entity.screenRuleDetailsWithDbFieldName[colField].colorCode;
                    }
                }
                return color;
            };
            $scope.retrieveSearchedData = function () {
                $scope.selectOneParameter = false;
                $scope.searchedData = [];
                $scope.listFilled = false;
                if (Object.getOwnPropertyNames($scope.searchCustom).length > 0) {
                    var mapHasValue = false;
                    for (var prop in $scope.searchCustom) {
                        if (typeof $scope.searchCustom[prop] === 'object' && $scope.searchCustom[prop] != null) {
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
                        $rootScope.maskLoading();
                        $scope.packetDataBean = {};
                        $scope.packetDataBean.featureCustomMapValue = {};
                        var finalMap = {};
                        var searchResult = DynamicFormService.convertSearchData($scope.searchInvoiceTemplate, $scope.searchParcelTemplate, $scope.searchLotTemplate, $scope.searchPacketTemplate, $scope.searchCustom);
                        $scope.searchCustom = angular.copy(searchResult);
                        angular.forEach(featureMap, function (val, label) {

                            var vlaueOfCus = $scope.searchCustom[label];
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
                                vlaueOfCus = $scope.searchCustom['to' + label];
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
                                vlaueOfCus = $scope.searchCustom['from' + label];
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
                        $scope.packetDataBean.featureCustomMapValue = finalMap;
                        $scope.packetDataBean.hasPacket = false;
                        $scope.packetDataBean.featureDbFieldMap = featureCustomMap;
                        $scope.packetDataBean.ruleConfigMap = {
                            fieldIdNameMap: $scope.fieldIdNameMap,
                            featureName: "editPacket"
                        };
                        $scope.packetDataBean.isInStockOfLoggedInUser = true;
                        if ($scope.packetDataBean.featureCustomMapValue.packet !== undefined && $scope.packetDataBean.featureCustomMapValue.packet !== null) {
                            //console.log(JSON.stringify($scope.packetDataBean.featureCustomMapValue.packet));
                            delete $scope.packetDataBean.featureCustomMapValue.packet.in_stock_of_packet$UMS$String;
                        }
                        //console.log(JSON.stringify($scope.packetDataBean));
                        PacketService.search($scope.packetDataBean, function (res) {
                            $scope.searchedDataFromDb = angular.copy(res);
                            //console.log(JSON.stringify(res));
                            $scope.issuedStock = {};
                            $scope.issuedStock.enableFiltering = true;
                            $scope.issuedStock.multiSelect = false;
                            $scope.issuedStock.enableRowSelection = true;
                            $scope.selectedStock = [];
                            $scope.issuedStock.onRegisterApi = function (gridApi) {
                                $scope.gridApi = gridApi;
                                gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                                    if ($scope.selectedStock.length > 0) {
                                        $.each($scope.selectedStock, function (index, result) {
                                            if (result["$$hashKey"] === row["entity"]["$$hashKey"]) {
                                                //Remove from array
                                                $scope.flag.repeatedRow = true;
                                                $scope.selectedStock.splice(index, 1);
                                                return false;
                                            } else {
                                                $scope.flag.repeatedRow = false;
                                            }
                                        });
                                        if (!$scope.flag.repeatedRow) {
                                            $scope.selectedStock.push(row["entity"]);
                                        }
                                    } else {
                                        $scope.selectedStock.push(row["entity"]);
                                    }
                                    if ($scope.selectedStock.length > 0) {
                                        $scope.flag.rowSelectedflag = true;
                                    } else {
                                        $scope.flag.rowSelectedflag = false;
                                    }
                                });
                                gridApi.selection.on.rowSelectionChangedBatch($scope, function (rows) {
                                    if ($scope.selectedStock.length > 0) {
                                        angular.forEach(rows, function (row) {
                                            $.each($scope.selectedStock, function (index, result) {
                                                if (result["$$hashKey"] === row["entity"]["$$hashKey"]) {
                                                    //Remove from array
                                                    $scope.flag.repeatedRow = true;
                                                    $scope.selectedStock = [];
                                                    return false;
                                                } else {
                                                    $scope.flag.repeatedRow = false;
                                                }
                                            });
                                            if (!$scope.flag.repeatedRow) {
                                                $scope.selectedStock.push(row["entity"]);
                                            }
                                        });
                                    } else {
                                        angular.forEach(rows, function (row) {
                                            $scope.selectedStock.push(row["entity"]);
                                        });
                                    }
                                    if ($scope.selectedStock.length > 0) {
                                        $scope.flag.rowSelectedflag = true;
                                    } else {
                                        $scope.flag.rowSelectedflag = false;
                                    }
                                });
                            };

                            $scope.issuedStockList = [];
//                            for (var i = 0; i < res.length; i++) {
//                                if (res[i].categoryCustom != null) {
//                                    res[i].categoryCustom["$$$value"] = (res[i].value);
////                                        console.log(res[i].categoryCustom["$$$value"]);
//                                }
//                            }
                            var success = function (result)
                            {
                                $scope.searchedData = angular.copy(res);
                                angular.forEach($scope.searchedData, function (itr) {
                                    angular.forEach($scope.labelListForUiGrid, function (list) {
                                        if (itr.custom1 != null && !itr.custom1.hasOwnProperty(list.name)) {
                                            itr.custom1[list.name] = "NA";
                                        }
                                        else if (itr.custom1 != null && itr.custom1.hasOwnProperty(list.name)) {
                                            if (!(!!(itr.custom1[list.name]))) {
                                                itr.custom1[list.name] = "NA";
                                            }
                                        }
                                    });
                                    if (itr.categoryCustom != null) {
                                        itr.categoryCustom['packetIdForConstraint'] = itr.value;
                                        itr.categoryCustom.screenRuleDetailsWithDbFieldName = itr.screenRuleDetailsWithDbFieldName;
                                        $scope.issuedStockList.push(itr.categoryCustom);
                                    }
                                });
                                $rootScope.unMaskLoading();
                                $scope.issuedStock.data = $scope.issuedStockList;
                                $scope.issuedStock.columnDefs = $scope.labelListForUiGrid;
                            }
                            DynamicFormService.convertorForCustomField(res, success);
                            window.setTimeout(function () {
                                $(window).resize();
                                $(window).resize();
                            }, 200);
                            $scope.listFilled = true;
                            $scope.issueListFilled = true;
                            $scope.dataRetrieved = true;
                        }, function () {
                            var msg = "Could not retrieve, please try again.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        });
                    } else {
                        var msg = "Please select atleast one search criteria for search";
                        var type = $rootScope.warning;
                        $rootScope.addMessage(msg, type);
                    }
                } else {
                    $scope.issuedStock = {};
                    $scope.issuedStock.data = [];
                    $scope.dataRetrieved = true;
                    $scope.listFilled = true;
                    var msg = "Please select atleast one search criteria for search";
                    var type = $rootScope.warning;
                    $rootScope.addMessage(msg, type);
                }

            };
            $scope.initEditPacketForm = function (editPacketForm) {
                $scope.editPacketForm = editPacketForm;
            };

            $scope.reset = function (sectionTobeReset) {
                if (sectionTobeReset === "searchCustom") {
                    $scope.searchCustom = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("packetsplit");
                    $scope.dbType = {};
                    templateData.then(function (section) {
                        $scope.generalSearchTemplate = section['genralSection'];
                        $scope.searchResetFlag = true;
                        $rootScope.unMaskLoading();
                        $scope.flag.customFieldGenerated = true;
                    }, function (reason) {
                    }, function (update) {
                    });
                } else if (sectionTobeReset === "packetCustom") {
                    if ($scope.response !== undefined && $scope.response !== null) {
                        angular.forEach($scope.packetCustom, function (val, key) {
                            if (!key !== 'no_of_pieces_of_packet$NM$Long') {
                                delete $scope.packetCustom[key];
                            }
                        })
                        var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                        templateData.then(function (section) {
                            var packetField = [];
                            var result = Object.keys($scope.response).map(function (key, value) {
                                angular.forEach(this[key], function (itr) {
                                    if (key !== 'Packet#P#') {
                                        packetField.push({Packet: itr});
                                    }
                                });
                            }, $scope.response);
                            $scope.updatePacketTemplate = null;
                            $scope.updatePacketTemplate = section['genralSection'];
                            $scope.updatePacketTemplate = DynamicFormService.retrieveCustomData($scope.updatePacketTemplate, packetField);
                            $scope.finalUpdatePacketTemplate = [];
                            if ($scope.updatePacketTemplate !== undefined && $scope.updatePacketTemplate !== null && $scope.updatePacketTemplate.length > 0) {
                                var count = 0;
                                for (var i = 0; i < $scope.updatePacketTemplate.length; i++) {
                                    if (count <= 3) {
                                        if ($scope.updatePacketTemplate[i].model === 'no_of_pieces_of_packet$NM$Long') {
                                            $scope.updatePacketTemplate[i].required = true;
                                            $scope.finalUpdatePacketTemplate.push($scope.updatePacketTemplate[i]);
                                            count++;
                                        } else if ($scope.updatePacketTemplate[i].model === 'carat_of_packet$NM$Double') {
                                            $scope.updatePacketTemplate[i].required = true;
                                            $scope.finalUpdatePacketTemplate.push($scope.updatePacketTemplate[i]);
                                            count++;
                                        } else if ($scope.updatePacketTemplate[i].model === 'isCheck') {
                                            //TODO
                                            $scope.finalUpdatePacketTemplate.push($scope.updatePacketTemplate[i]);
                                            count++;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                            $timeout(function () {
                                // Controller As
                                $scope.Packet = this;
                                this.packetSplitFlag = true;
                            }, 100);
                            $scope.lotEditShow = true;
                            $rootScope.unMaskLoading();
                            $scope.flag.customFieldGenerated = true;
//                        $scope.packetDataBean.packetDbType = angular.copy($scope.packetDbType);
                        }, function (reason) {
                        }, function (update) {
                        });
                    }
                }
            };
            $scope.onCanel = function () {
                if ($scope.editPacketForm != null) {
                    $scope.editPacketForm.$dirty = false;
                }
                $scope.selectedStock = [];
                $scope.flag.showAddPage = false;
                $scope.flag.showUpdatePage = false;
                $scope.submitted = false;
                $scope.reset("packetCustom");
                $scope.flag.showUpdatePage = false;
                $scope.flag.rowSelectedflag = false;
            };

            $scope.onCanelOfSearch = function (addLotForm) {
                if ($scope.editPacketForm != null) {
                    $scope.editPacketForm.$dirty = false;
                }
                $scope.searchedData = [];
//                $scope.searchCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);
                $scope.searchResetFlag = false;
                $scope.listFilled = false;
                $scope.reset("searchCustom");
            };

            //edit packet starts here
            $scope.addPacket = function () {
                $scope.reset("packetCustom");
                $scope.Packet = this;
                this.packetSplitFlag = false;
                $scope.flag.showAddPage = false;
                if (($scope.selectedStock !== undefined && $scope.selectedStock !== null && $scope.selectedStock.length > 0) || ($rootScope.editPacket)) {
                    $rootScope.maskLoading();
                    if ($scope.searchedDataFromDb !== undefined && $scope.searchedDataFromDb !== null) {
                        for (var i = 0; i < $scope.searchedDataFromDb.length; i++) {
                            if ($scope.searchedDataFromDb[i].value === $scope.selectedStock[0].packetIdForConstraint) {
                                $scope.invoiceIdForConstraint = $scope.searchedDataFromDb[i].id;
                                $scope.parcelIdForConstraint = $scope.searchedDataFromDb[i].description;
                                $scope.lotIdForConstraint = $scope.searchedDataFromDb[i].label;
                                $scope.packetIdForConstraint = angular.copy($scope.searchedDataFromDb[i].value);
                                break;
                            }
                        }
                    }
                    $scope.preRuleSatisfied = false;
                    var dataToSend = {
                        featureName: 'editPacket',
                        entityId: $scope.packetIdForConstraint,
                        entityType: 'packet'
                    };
                    RuleExecutionService.executePreRule(dataToSend, function (res) {
                        //Prevent the record from edit if pre rule satisfies.                        
                        if (!!res.validationMessage) {
                            $scope.preRuleSatisfied = true;
                            var type = $rootScope.warning;
                            $rootScope.addMessage(res.validationMessage, type);
                            $rootScope.unMaskLoading();
                        } else {
                            $scope.count = 0;
                            $scope.flag.showUpdatePage = true;
                            $scope.gridOptions = {};
                            $scope.gridOptions.enableFiltering = true;
                            $scope.gridOptions.data = [];
                            $scope.packetLabelForUIGrid = [];
                            $scope.flag.showAddPage = true;
                            var featureDbfieldNameMap = {};
                            var dbFieldName = [];
                            $scope.packetDataBean = {};
                            $scope.packetListToSave = [];
                            $scope.parcelDataBean = {invoiceDataBean: {invoiceCustom: '', invoiceDbType: ''}};
                            CustomFieldService.retrieveDesignationBasedFields("packetsplit", function (response) {
                                $scope.response = angular.copy(response);
//                        $scope.invoiceCustom = DynamicFormService.resetSection($scope.updateInvoiceTemplate);
                                var templateDataInvoice = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                                $scope.invoiceDbType = {};
                                templateDataInvoice.then(function (section) {
                                    $scope.updateInvoiceTemplate = section['genralSection'];
                                    $scope.updateInvoiceTemplate = DynamicFormService.retrieveCustomData($scope.updateInvoiceTemplate, response);
                                    var dbFieldName = [];
                                    if ($scope.updateInvoiceTemplate != null && $scope.updateInvoiceTemplate.length > 0) {
                                        angular.forEach($scope.updateInvoiceTemplate, function (itr) {
                                            if (itr.model) {
                                                dbFieldName.push(itr.model);
                                            } else if (itr.fromModel) {
                                                dbFieldName.push(itr.fromModel);
                                            }
                                            else if (itr.toModel) {
                                                dbFieldName.push(itr.toModel);
                                            }
                                        });
                                        featureDbfieldNameMap["invoice"] = dbFieldName;
                                    }
                                }, function (reason) {
                                }, function (update) {
                                });
//                        $scope.parcelCustom = DynamicFormService.resetSection($scope.updateParcelTemplate);
                                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
                                $scope.parcelDbType = {};
                                templateData.then(function (section) {
                                    $scope.updateParcelTemplate = section['genralSection'];
                                    $scope.updateParcelTemplate = DynamicFormService.retrieveCustomData($scope.updateParcelTemplate, response);
                                    var dbFieldName = [];
                                    if ($scope.updateParcelTemplate != null && $scope.updateParcelTemplate.length > 0) {
                                        angular.forEach($scope.updateParcelTemplate, function (itr) {
                                            if (itr.model) {
                                                dbFieldName.push(itr.model);
                                            } else if (itr.fromModel) {
                                                dbFieldName.push(itr.fromModel);
                                            }
                                            else if (itr.toModel) {
                                                dbFieldName.push(itr.toModel);
                                            }
                                        });
                                        featureDbfieldNameMap["parcel"] = dbFieldName;
                                    }
                                }, function (reason) {
                                }, function (update) {
                                });
//                        $scope.lotCustom = DynamicFormService.resetSection($scope.updateLotTemplate);
                                var templateDataOfLot = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                                $scope.lotDbType = {};
                                templateDataOfLot.then(function (section) {
                                    $scope.updateLotTemplate = section['genralSection'];
                                    $scope.updateLotTemplate = DynamicFormService.retrieveCustomData($scope.updateLotTemplate, response);
                                    var dbFieldName = [];
                                    if ($scope.updateLotTemplate != null && $scope.updateLotTemplate.length > 0) {
                                        angular.forEach($scope.updateLotTemplate, function (itr) {
                                            if (itr.model) {
                                                dbFieldName.push(itr.model);
                                            } else if (itr.fromModel) {
                                                dbFieldName.push(itr.fromModel);
                                            }
                                            else if (itr.toModel) {
                                                dbFieldName.push(itr.toModel);
                                            }
                                        });

                                        featureDbfieldNameMap["lot"] = dbFieldName;
                                        var dbFieldName = [];
                                        dbFieldName.push($scope.lotIdForConstraint);
                                        featureDbfieldNameMap["id"] = dbFieldName;
                                    }
                                }, function (reason) {

                                }, function (update) {
                                });

//                        retrieve parent packet
                                $scope.parentDbFieldName = [];
//                        $scope.packetParentCustom = DynamicFormService.resetSection($scope.updatePacketParentTemplate);
                                var templateDataForUpdate = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                                $scope.packetDbTypeParent = {};
                                templateDataForUpdate.then(function (section) {
                                    $scope.updatePacketParentTemplate = section['genralSection'];
                                    var packetParentField = [];
                                    var result = Object.keys(response).map(function (key, value) {
                                        angular.forEach(this[key], function (itr) {
                                            if (key === 'Packet#P#') {
                                                packetParentField.push({Lot: itr});
                                            }
                                        });
                                    }, response);
                                    $scope.updatePacketParentTemplate = DynamicFormService.retrieveCustomData($scope.updatePacketParentTemplate, packetParentField);
                                    var dbFieldName = [];
                                    angular.forEach($scope.updatePacketParentTemplate, function (itr) {
                                        if (itr.model) {
                                            dbFieldName.push(itr.model);
                                        } else if (itr.fromModel) {
                                            dbFieldName.push(itr.fromModel);
                                        }
                                        else if (itr.toModel) {
                                            dbFieldName.push(itr.toModel);
                                        }
                                    });
                                    featureDbfieldNameMap["packet"] = dbFieldName;
                                    $scope.parentDbFieldName = angular.copy(dbFieldName);
                                    var dbFieldName = [];
                                    dbFieldName.push($scope.packetIdForConstraint);
                                    featureDbfieldNameMap["packetId"] = dbFieldName;
                                }, function (reason) {

                                }, function (update) {
                                });
//                        retrieve parent packet ends here


                                $scope.categoryCustom = DynamicFormService.resetSection($scope.updatePacketTemplate);
                                var templateDataPacket = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                                if (!($scope.packetDbType != null)) {
                                    $scope.packetDbType = {};
                                }
                                templateDataPacket.then(function (section) {
                                    var dbFieldName = [];
                                    $scope.fieldIdNameMap = {};
                                    $scope.updatePacketTemplate = section['genralSection'];
                                    var packetField = [];
                                    var result = Object.keys(response).map(function (key, value) {
                                        angular.forEach(this[key], function (itr) {
                                            if (key !== 'Packet#P#') {
                                                packetField.push({Packet: itr});
                                            }
                                        });
                                    }, response);
                                    $scope.updatePacketTemplate = DynamicFormService.retrieveCustomData($scope.updatePacketTemplate, packetField);
                                    $scope.listOfModelsOfDateType = [];
                                    var fields = [];
                                    if ($scope.updatePacketTemplate !== null && $scope.updatePacketTemplate !== undefined)
                                    {
                                        angular.forEach($scope.updatePacketTemplate, function (updateTemplate)
                                        {
                                            if (updateTemplate.type !== null && updateTemplate.type !== undefined && updateTemplate.type === 'date')
                                            {
                                                $scope.listOfModelsOfDateType.push(angular.copy(updateTemplate.model));
                                            }
                                            fields.push(updateTemplate.model);

                                        });
                                    }
                                    $scope.parcelLabelListForUiGrid = [];
                                    angular.forEach($scope.updatePacketTemplate, function (itr) {
                                        if (itr.fromModel) {
                                            $scope.parcelLabelListForUiGrid.push({name: itr.fromModel, displayName: itr.label, minWidth: 200,
                                                cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.searchedParcelScreenRule(row, \'' + itr.fromModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                            });
                                        } else if (itr.toModel) {
                                            $scope.parcelLabelListForUiGrid.push({name: itr.toModel, displayName: itr.label, minWidth: 200,
                                                cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.searchedParcelScreenRule(row, \'' + itr.toModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                            });
                                        } else if (itr.model) {
                                            $scope.parcelLabelListForUiGrid.push({name: itr.model, displayName: itr.label, minWidth: 200,
                                                cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.searchedParcelScreenRule(row, \'' + itr.model + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                            });
                                        }
                                        if (itr.model === 'no_of_pieces_of_packet$NM$Long') {
                                            itr.required = true;
                                        } else if (itr.model === 'carat_of_packet$NM$Double') {
                                            itr.required = true;
                                        } else {
                                            itr.isViewFromDesignation = true;
                                        }
                                        if (itr.model) {
                                            $scope.fieldIdNameMap[itr.fieldId] = itr.model;
                                            $scope.packetLabelForUIGrid.push({name: itr.model, displayName: itr.label, minWidth: 200});
//                                    if ($scope.parentDbFieldName.length > 0 && $scope.parentDbFieldName.indexOf(itr.model === -1)) {
                                            $scope.parentDbFieldName.push(itr.model);
//                                    }
                                        } else if (itr.fromModel) {
                                            $scope.fieldIdNameMap[itr.fieldId] = itr.fromModel;
                                            $scope.packetLabelForUIGrid.push({name: itr.fromModel, displayName: itr.label, minWidth: 200});
                                            if ($scope.parentDbFieldName.length > 0 && $scope.parentDbFieldName.indexOf(itr.fromModel === -1)) {
                                                $scope.parentDbFieldName.push(itr.fromModel);
                                            }
                                        }
                                        else if (itr.toModel) {
                                            $scope.fieldIdNameMap[itr.fieldId] = itr.toModel;
                                            $scope.packetLabelForUIGrid.push({name: itr.toModel, displayName: itr.label, minWidth: 200});
                                            if ($scope.parentDbFieldName.length > 0 && $scope.parentDbFieldName.indexOf(itr.toModel === -1)) {
                                                $scope.parentDbFieldName.push(itr.toModel);
                                            }
                                        }
                                    });
                                    $scope.fieldNotConfigured = false;
                                    //console.log(fields);
                                    $scope.mandatoryFields = ['carat_of_packet$NM$Double', 'no_of_pieces_of_packet$NM$Long'];
                                    if (fields.length > 0 && $scope.mandatoryFields != null && $scope.mandatoryFields.length > 0) {
                                        for (var field = 0; field < fields.length; field++) {
                                            if ($scope.mandatoryFields[field] !== undefined) {
                                                if (fields.indexOf($scope.mandatoryFields[field]) === -1) {
                                                    //console.log("==========");
                                                    //console.log($scope.mandatoryFields[field]);
                                                    $scope.fieldNotConfigured = true;
                                                    break;
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                    } else {
                                        $scope.fieldNotConfigured = true;
                                    }
                                    if (!$scope.fieldNotConfigured) {
                                        if ($scope.updatePacketTemplate !== undefined && $scope.updatePacketTemplate !== null && $scope.updatePacketTemplate.length > 0) {
                                            $scope.finalUpdatePacketTemplate = [];
                                            var count = 0;
                                            for (var i = 0; i < $scope.updatePacketTemplate.length; i++) {
                                                if (count <= 3) {
                                                    if ($scope.updatePacketTemplate[i].model === 'no_of_pieces_of_packet$NM$Long') {
                                                        $scope.finalUpdatePacketTemplate.push($scope.updatePacketTemplate[i]);
                                                        count++;
                                                    } else if ($scope.updatePacketTemplate[i].model === 'carat_of_packet$NM$Double') {
                                                        $scope.finalUpdatePacketTemplate.push($scope.updatePacketTemplate[i]);
                                                        count++;
                                                    } else if ($scope.updatePacketTemplate[i].model === 'isCheck') {
                                                        //TODO
                                                        $scope.finalUpdatePacketTemplate.push($scope.updatePacketTemplate[i]);
                                                        count++;
                                                    }
                                                } else {
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    featureDbfieldNameMap["packet"] = $scope.parentDbFieldName;
                                    var dbFieldName = [];
                                    featureDbfieldNameMap.ruleConfigMap = {
                                        fieldIdNameMap: $scope.fieldIdNameMap,
                                        featureName: "editPacket"
                                    };
                                    console.log(JSON.stringify("--------------------------"));
                                    console.log(JSON.stringify(featureDbfieldNameMap['packetId']));
                                    PacketService.retrievePacketById(featureDbfieldNameMap, function (res) {
//                                        //console.log(JSON.stringify(res[0]));                                        
//                                //console.log(JSON.stringify("response :::" + JSON.stringify(res)));
                                        $scope.stockCaratOfPacket = undefined;
                                        $scope.stockPiecesOfPacket = undefined;

                                        if (res !== undefined && res !== null && res[0] !== null) {
                                            $scope.invoiceCustom = res[0].custom1;
                                            $scope.parcelCustom = res[0].custom3;
                                            $scope.lotCustom = res[0].custom4;
                                            if (res[0].custom5 !== undefined && res[0].custom5 !== null) {
                                                $scope.stockCaratOfPacket = angular.copy(res[0].custom5['stockCarat']);
                                                $scope.stockPiecesOfPacket = angular.copy(res[0].custom5['stockPieces']);
                                                delete res[0].custom5['stockPieces'];
                                                delete res[0].custom5['stockCarat'];
                                                $scope.packetParentCustom = res[0].custom5;
                                                angular.forEach($scope.listOfModelsOfDateType, function (listOfModel)

                                                {
                                                    if (res[0].custom5.hasOwnProperty(listOfModel))
                                                    {
                                                        if (res[0].custom5[listOfModel] !== null && res[0].custom5[listOfModel] !== undefined)
                                                        {
                                                            res[0].custom5[listOfModel] = new Date(res[0].custom5[listOfModel]);
                                                        } else
                                                        {
                                                            res[0].custom5[listOfModel] = '';
                                                        }
                                                    }
                                                });
                                                $scope.packetCustom = {};
                                                $scope.packetCustom = angular.copy(res[0].custom5);
                                                $scope.packetCustom.no_of_pieces_of_packet$NM$Long = 1;
                                                $scope.packetCustom.packetID$AG$String = res[0].custom5['packetID$AG$String'];
                                                $scope.packetCustom['carat_of_packet$NM$Double'] = null;
                                                console.log($scope.packetCustom);
                                            } else {
                                                $scope.packetCustom = {};
                                                $scope.packetCustom = angular.copy(res[0].custom5);
                                                $scope.packetCustom.no_of_pieces_of_packet$NM$Long = 1;
                                                $scope.packetCustom.packetID$AG$String = res[0].custom5['packetID$AG$String'];
                                                $scope.packetCustom['carat_of_packet$NM$Double'] = null;
                                            }
                                        }
                                        $timeout(function () {
                                            // Controller As
                                            $scope.Packet = this;
                                            this.packetSplitFlag = true;
                                            $rootScope.unMaskLoading();
                                        }, 100);
//                                        $scope.searchedDataFromDb = angular.copy(res);
                                        $scope.dataToConvert = angular.copy(res);
                                        $scope.dataToConvert = $scope.dataToConvert.splice(1, 1);
                                        var success = function (result)
                                        {
                                            $scope.searchedSplitData = angular.copy(result);
                                            $scope.searchedDataFromDbForUiGrid = [];
                                            angular.forEach($scope.searchedSplitData, function (itr) {
                                                ///////////////////////////////////////////UI-GRID/////////////////////////////////////////////////////
                                                angular.forEach($scope.parcelLabelListForUiGrid, function (list) {
                                                    if (!!itr.categoryCustom && !itr.categoryCustom.hasOwnProperty(list.name)) {
                                                        itr.categoryCustom[list.name] = "NA";
                                                    }
                                                    else if (!!itr.categoryCustom && itr.categoryCustom.hasOwnProperty(list.name)) {
                                                        if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                            itr.categoryCustom[list.name] = "NA";
                                                        }
                                                    }
                                                });
//                                                itr.categoryCustom.screenRuleDetailsWithDbFieldName = itr.screenRuleDetailsWithDbFieldName;
                                                if (!!itr.categoryCustom)
                                                    $scope.searchedDataFromDbForUiGrid.push(itr.categoryCustom);
                                            });
                                            $scope.gridOptionsForParcel = {};
                                            $scope.gridOptionsForParcel.enableFiltering = true;
                                            $scope.gridOptionsForParcel.multiSelect = false;
                                            $scope.gridOptionsForParcel.enableRowSelection = true;
                                            $scope.gridOptionsForParcel.enableSelectAll = false;
                                            $scope.selectedParcel = [];
//                                            $scope.gridOptionsForParcel.onRegisterApi = function (gridApi) {
//                                                gridApiParcel = gridApi;
//
//                                            };
                                            $scope.gridOptionsForParcel.data = $scope.searchedDataFromDbForUiGrid;
                                            $scope.gridOptionsForParcel.columnDefs = $scope.parcelLabelListForUiGrid;
//                                            $scope.processingFlag.retrieveSearchedDataCompleted = true;
                                        };
                                        DynamicFormService.convertorForCustomField(res, success);
                                        window.setTimeout(function () {
                                            $(window).resize();
                                            $(window).resize();
                                        }, 100);
                                        $scope.splitlistFilled = true;
                                    }
                                    , function () {
                                        var msg = "Could not retrieve, please try again.";
                                        var type = $rootScope.error;
                                        $rootScope.addMessage(msg, type);
                                        $rootScope.unMaskLoading();
                                    });
                                    $scope.flag.customFieldGenerated = true;
                                    $scope.packetAddFlag = true;

                                }, function (reason) {

                                }, function (update) {

                                });

                            }, function () {
                                $rootScope.unMaskLoading();
                                var msg = "Failed to retrieve data";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                            });
                        }
                    }, function (failure) {
                        $rootScope.unMaskLoading();
                        var msg = "Failed to retrieve pre rule.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                }

            };

            $scope.splitStock = function (editPacketForm) {
                var splitData = {};
                var sumOfCaratUI = true;
//                TODO check condition
                if (editPacketForm.$valid) {
                    //console.log(JSON.stringify($scope.stockCaratOfPacket));
                    //console.log(JSON.stringify($scope.stockPiecesOfPacket));
                    if ($scope.stockCaratOfPacket && $scope.stockPiecesOfPacket && $scope.stockCaratOfPacket > $scope.packetCustom['carat_of_packet$NM$Double'] && $scope.stockPiecesOfPacket > 1) {
//                        console.log("here-------------------------------");
                        splitData.type = "Packet";
                        splitData.id = $scope.packetIdForConstraint;
                        splitData.parentID = $scope.lotIdForConstraint;
                        splitData.stockDbType = angular.copy($scope.packetDbType);
//                    TODO 
//                    if packets are not split then else get latest packet id of that
                        if ($scope.gridOptionsForParcel !== undefined && $scope.gridOptionsForParcel !== null && $scope.gridOptionsForParcel.data !== undefined && $scope.gridOptionsForParcel.data !== null && $scope.gridOptionsForParcel.data.length > 0) {
                            splitData.databeanOfPacket = {packetCustom: {packetID$AG$String: $scope.gridOptionsForParcel.data[$scope.gridOptionsForParcel.data.length - 1].packetID$AG$String}};
                        } else {
                            splitData.databeanOfPacket = {packetCustom: {packetID$AG$String: $scope.packetCustom.packetID$AG$String}};
                        }
                        splitData.stockDbType['packetID$AG$String'] = "String";
                        var datas = [];
                        datas.push($scope.packetCustom);
                        splitData.stockDataForSplit = datas;

                        $rootScope.maskLoading();
                        console.log(JSON.stringify(splitData.databeanOfPacket.packetCustom.packetID$AG$String));

                        SplitStockService.splitStock(splitData, function (res) {
                            console.log(JSON.stringify(res.data));
                            if (res !== undefined && res !== null && res.data !== undefined && res.data !== null) {
                                var sectionData = [];
                                res.data = res.data.toString().substring(1, res.data.toString().length - 1);
                                var dataToConvert = {};
                                dataToConvert = angular.copy($scope.packetCustom);
                                dataToConvert['packetID$AG$String'] = res.data.toString();
                                sectionData.push({categoryCustom: angular.copy(dataToConvert)});
                                $scope.stockCaratOfPacket = $scope.stockCaratOfPacket - $scope.packetCustom['carat_of_packet$NM$Double'];
                                $scope.stockPiecesOfPacket = $scope.stockPiecesOfPacket - $scope.packetCustom['no_of_pieces_of_packet$NM$Long'];
                                $scope.packetCustom['carat_of_packet$NM$Double'] = null;
                                $scope.packetCustom['no_of_pieces_of_packet$NM$Long'] = 1;
                                if ($scope.packetParentCustom !== undefined && $scope.packetParentCustom !== null) {
                                    $scope.packetParentCustom['carat_of_packet$NM$Double'] = angular.copy($scope.stockCaratOfPacket);
                                    $scope.packetParentCustom['no_of_pieces_of_packet$NM$Long'] = angular.copy($scope.stockPiecesOfPacket);

                                }
                                var success = function (result) {
                                    angular.forEach($scope.parcelLabelListForUiGrid, function (list) {
                                        if (!result[0].categoryCustom.hasOwnProperty(list.name)) {
                                            result[0].categoryCustom[list.name] = "NA";
                                        }
                                        else if (result[0].categoryCustom.hasOwnProperty(list.name)) {
                                            if (result[0].categoryCustom[list.name] === null || result[0].categoryCustom[list.name] === '' || result[0].categoryCustom[list.name] === undefined || result[0].categoryCustom[list.name] === 'undefined') {
                                                result[0].categoryCustom[list.name] = "NA";
                                            }
                                        }
                                    });
                                    $scope.gridOptionsForParcel.data.push(angular.copy(result[0].categoryCustom));

                                }
                                DynamicFormService.convertorForCustomField(sectionData, success);
                            }
                            $rootScope.unMaskLoading();
                        }, function () {
                            $rootScope.unMaskLoading();
                            var msg = "Failed to split packet";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                        });
                    } else {
                        var msg = "Carat or Pieces value does not match, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    }
                }
            };
        }]);
});