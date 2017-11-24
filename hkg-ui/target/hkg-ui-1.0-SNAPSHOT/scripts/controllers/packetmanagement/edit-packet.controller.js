define(['hkg', 'packetService', 'customFieldService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'printBarcodeValue', 'dynamicForm', 'accordionCollapse', 'ruleExecutionService'], function (hkg, packetService) {
    hkg.register.controller('EditPacketController', ["$rootScope", "$scope", "PacketService", "$timeout", "$filter", "$location", "$window", "CustomFieldService", "DynamicFormService", "RuleExecutionService", function ($rootScope, $scope, PacketService, $timeout, $filter, $location, $window, CustomFieldService, DynamicFormService, RuleExecutionService) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "editPacket";
            $rootScope.activateMenu();
            $scope.Packet = this;
            var featureMap = {};
            var featureCustomMap = {};
            $scope.initializeData = function (flag) {
                if (flag) {
                    $rootScope.maskLoading();
                    $scope.searchedData = [];
                    $scope.searchedDataFromDb = [];
                    $scope.listFilled = false;
                    $scope.lotListTodisplay = [];
                    $scope.searchCustom = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("packetEdit");
                    $scope.flag = {};
                    $scope.flag.searchFieldNotAvailable = false;
                    $scope.dbType = {};
                    templateData.then(function (section) {
//                        console.log("sections::::" + JSON.stringify(section));
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
//                            console.log(JSON.stringify($scope.packetDataBean));
                        PacketService.search($scope.packetDataBean, function (res) {
                            $scope.searchedDataFromDb = angular.copy(res);
//                                console.log(JSON.stringify(res));
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
                            for (var i = 0; i < res.length; i++) {
                                if (res[i].categoryCustom != null) {
                                    res[i].categoryCustom["$$$value"] = (res[i].value);
//                                        console.log(res[i].categoryCustom["$$$value"]);
                                }
                            }
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
                                        itr.categoryCustom.$$packetId$$ = itr.value;
                                        itr.categoryCustom.screenRuleDetailsWithDbFieldName = itr.screenRuleDetailsWithDbFieldName;
                                        console.log(itr.categoryCustom["$$$value"]);
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
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("packetEdit");
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
                        $scope.packetCustom = {};
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
                            $timeout(function () {
                                // Controller As
                                $scope.Packet = this;
                                this.packetEditFlag = true;
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
                if ($rootScope.editPacket) {
                    $scope.initializeData(true);
                }
                $scope.selectedStock = [];
                $scope.flag.showAddPage = false;
                $rootScope.editPacket = false;
                $scope.flag.showUpdatePage = false;
                $rootScope.packetId = null;
                $scope.submitted = false;
                $scope.reset("packetCustom");
                $scope.flag.showUpdatePage = false;
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

            $scope.savePacket = function (editPacketForm) {
                $scope.caratDoesNotMatch = false;
                angular.forEach($scope.listOfModelsOfDateType, function (listOfModel)

                {
                    if ($scope.packetCustom.hasOwnProperty(listOfModel))
                    {
                        if ($scope.packetCustom[listOfModel] !== null && $scope.packetCustom[listOfModel] !== undefined)
                        {
                            $scope.packetCustom[listOfModel] = new Date($scope.packetCustom[listOfModel]);
                        } else
                        {
                            $scope.packetCustom[listOfModel] = '';
                        }
                    }
                });
                $scope.submitted = true;
                $scope.packetDataBean.packetCustom = $scope.packetCustom;
                $scope.packetDataBean.packetDbType = angular.copy($scope.packetDbType);
                $scope.packetDataBean.id = $scope.packetIdForConstraint;
                $scope.packetDataBean.workallocationId = $scope.workAllocationId;
                if (editPacketForm.$valid) {
                    $scope.submitted = false;
                    if (Object.getOwnPropertyNames($scope.packetCustom).length > 0) {
                        if (($scope.packetCustom['carat_of_packet$NM$Double'] != null && $scope.packetCustom['carat_of_packet$NM$Double'] > $scope.stockCaratOfLot) || ($scope.packetCustom['no_of_pieces_of_packet$NM$Long'] != null && $scope.packetCustom['no_of_pieces_of_packet$NM$Long'] > $scope.stockPiecesOfLot)) {
                            $scope.caratDoesNotMatch = true;
                        }
                    }
                    if (!$scope.caratDoesNotMatch && $scope.stockPiecesOfLot !== undefined && $scope.stockCaratOfLot !== undefined) {
                        $rootScope.maskLoading();
                        var dataToSend = {
                            featureName: 'editPacket',
                            entityId: $scope.packetIdForConstraint,
                            entityType: 'packet',
                            currentFieldValueMap: $scope.packetCustom,
                            dbType: $scope.packetDbType,
                            otherEntitysIdMap: {invoiceId: $scope.invoiceIdForConstraint, parcelId: $scope.parcelIdForConstraint, lotId: $scope.lotIdForConstraint}
                        };
                        RuleExecutionService.executePostRule(dataToSend, function (res) {
                            if (!!res.validationMessage) {
                                $rootScope.unMaskLoading();
                                var type = $rootScope.warning;
                                $rootScope.addMessage(res.validationMessage, type);
                            } else {
                                PacketService.update($scope.packetDataBean, function (response) {
                                    $scope.editPacketForm.$dirty = false;
                                    $rootScope.editPacket = false;
                                    $rootScope.packetByParcel = null;
                                    $scope.flag.showAddPage = false;
                                    $scope.onCanel();
                                    $rootScope.unMaskLoading();
                                }, function () {
                                    $rootScope.unMaskLoading();
                                    var msg = "Could not update lot, please try again.";
                                    var type = $rootScope.error;
                                    $rootScope.addMessage(msg, type);
                                });
                            }
                        }, function (failure) {
                            $rootScope.unMaskLoading();
                            var msg = "Failed to authenticate post rule.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                        });

                    } else {
                        $rootScope.unMaskLoading();
                        var msg = "Carat value does not match, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    }
                }

            };
            //edit packet starts here
            $scope.addPacket = function () {
                $scope.reset("packetCustom");
                this.packetEditFlag = false;
                $scope.flag.showAddPage = false;
                if (($scope.selectedStock !== undefined && $scope.selectedStock !== null && $scope.selectedStock.length > 0) || ($rootScope.editPacket)) {                    
                    $rootScope.maskLoading();
                    if ($scope.searchedDataFromDb !== undefined && $scope.searchedDataFromDb !== null) {
                        for (var i = 0; i < $scope.searchedDataFromDb.length; i++) {
                            if ($scope.searchedDataFromDb[i].value === $scope.selectedStock[0].$$$value) {
                                $scope.invoiceIdForConstraint = $scope.searchedDataFromDb[i].id;
                                $scope.parcelIdForConstraint = $scope.searchedDataFromDb[i].description;
                                $scope.lotIdForConstraint = $scope.searchedDataFromDb[i].label;
                                $scope.packetIdForConstraint = angular.copy($scope.searchedDataFromDb[i].value);                                
                                break;
                            }
                        }
                    }
                    if ($rootScope.editPacket) {
                        $scope.packetIdForConstraint = $rootScope.packetId;
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
                            CustomFieldService.retrieveDesignationBasedFields("packetEdit", function (response) {
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
                                    if ($scope.updatePacketTemplate !== null && $scope.updatePacketTemplate !== undefined)
                                    {
                                        angular.forEach($scope.updatePacketTemplate, function (updateTemplate)
                                        {
                                            if (updateTemplate.type !== null && updateTemplate.type !== undefined && updateTemplate.type === 'date')
                                            {
                                                $scope.listOfModelsOfDateType.push(angular.copy(updateTemplate.model));
                                            }
                                        });
                                    }
                                    angular.forEach($scope.updatePacketTemplate, function (itr) {
                                        if (itr.model === 'packetID$AG$String') {
                                            itr.isViewFromDesignation = true;
                                        }
                                        if (itr.model === 'no_of_pieces_of_packet$NM$Long') {
                                            itr.required = true;
                                        } else if (itr.model === 'carat_of_packet$NM$Double') {
                                            itr.required = true;
                                        }
                                        else if (itr.model === 'due_date_of_packet$DT$Date') {
                                            itr.required = true;
                                        }
                                        else if (itr.model === 'in_stock_of_packet$UMS$String') {
                                            itr.required = true;
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
                                    featureDbfieldNameMap["packet"] = $scope.parentDbFieldName;
                                    var dbFieldName = [];
                                    featureDbfieldNameMap.ruleConfigMap = {
                                        fieldIdNameMap: $scope.fieldIdNameMap,
                                        featureName: "editPacket"
                                    };
                                    console.log(JSON.stringify(featureDbfieldNameMap));
                                    PacketService.retrievePacketById(featureDbfieldNameMap, function (res) {
                                        
                                        console.log(res);
//                                console.log(JSON.stringify("response :::" + JSON.stringify(res)));
                                        $scope.stockCaratOfLot = undefined;
                                        $scope.stockPiecesOfLot = undefined;
                                        if (res != null) {
                                            res = angular.copy(res[0]);
                                            $scope.invoiceCustom = res.custom1;
                                            $scope.parcelCustom = res.custom3;
                                            $scope.lotCustom = res.custom4;
                                            if (res.custom5 !== undefined && res.custom5 !== null) {
                                                $scope.stockCaratOfLot = angular.copy(res.custom5['stockCarat']);
                                                $scope.stockPiecesOfLot = angular.copy(res.custom5['stockPieces']);
                                                delete res.custom5['stockPieces'];
                                                delete res.custom5['stockCarat'];
                                                $scope.packetParentCustom = res.custom5;
                                                angular.forEach($scope.listOfModelsOfDateType, function (listOfModel)

                                                {
                                                    if (res.custom5.hasOwnProperty(listOfModel))
                                                    {
                                                        if (res.custom5[listOfModel] !== null && res.custom5[listOfModel] !== undefined)
                                                        {
                                                            res.custom5[listOfModel] = new Date(res.custom5[listOfModel]);
                                                        } else
                                                        {
                                                            res.custom5[listOfModel] = '';
                                                        }
                                                    }
                                                });
                                                $scope.packetCustom = angular.copy(res.custom5);
                                                console.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>");
                                                console.log(JSON.stringify(res.custom5));
                                                $scope.packetScreenRules = res.screenRuleDetailsWithDbFieldName;
                                            } else {
                                                $scope.packetCustom = {};
                                            }
                                        }
                                        $timeout(function () {
                                            // Controller As
                                            $scope.Packet = this;
                                            this.packetEditFlag = true;
                                            $rootScope.unMaskLoading();
                                        }, 100);
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
//edit packet ends here
            if ($rootScope.editPacket) {
                $scope.addPacket();
            }

            $scope.showPopUp = function () {
                $("#deletePacketPopUp").modal("show");
            };
            $scope.hidePacketPopUp = function () {
                $("#deletePacketPopUp").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.deletePacket = function () {
                if ($scope.packetIdForConstraint !== undefined && $scope.packetIdForConstraint !== null) {
                    $rootScope.maskLoading();
                    PacketService.deletePacket($scope.packetIdForConstraint, function (res) {
                        if (res.data) {
                            $scope.hidePacketPopUp();
                            $scope.onCanel($scope.editLotForm);
                            $scope.searchedData = [];
                            $scope.listFilled = false;
                            $rootScope.unMaskLoading();
                        } else {
                            $scope.hidePacketPopUp();
                            $rootScope.unMaskLoading();
                            var msg = "Failed to delete packet, please try again.";
                            var type = $rootScope.error;
                        }
                    }, function () {
                        $scope.hidePacketPopUp();
                        $rootScope.unMaskLoading();
                        var msg = "Failed to delete packet, please try again.";
                        var type = $rootScope.error;
                    });
                }
            };

        }]);
});