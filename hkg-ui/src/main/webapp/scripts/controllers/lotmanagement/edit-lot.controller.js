define(['hkg', 'lotService', 'customFieldService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'printBarcodeValue', 'dynamicForm', 'accordionCollapse', 'sublotService', 'ruleExecutionService'], function (hkg, lotService) {
    hkg.register.controller('EditLotController', ["$rootScope", "$scope", "LotService", "$timeout", "$filter", "$location", "$window", "DynamicFormService", "CustomFieldService", "SublotService", "RuleExecutionService", function ($rootScope, $scope, LotService, $timeout, $filter, $location, $window, DynamicFormService, CustomFieldService, SublotService, RuleExecutionService) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "editLot";
            $scope.entity = "EDITLOT.";
            $rootScope.activateMenu();
            $scope.searchedDataFromDbForUiGrid = [];
            $scope.lotLabelListForUiGrid = [];
            $scope.selectedSubLot = [];
            var featureMap = {},
                    featureCustomMap = {},
                    associatedSubLotIds = [],
                    gridApiSubLot,
                    fieldNameSubLot = {carat: "carat$TF$Double", pieces: "no_of_pieces_of_sublot$NM$Long"},
            associatedSubLotCaratTotal,
                    associatedSubLotPiecesTotal,
                    countLotAndSubLotBothSuceed = 0,
                    parcelIdOfEditLot;
            $scope.initializeData = function (flag) {
                if (flag) {
                    $scope.lotEditShow = false;
                    featureCustomMap = {};
                    $scope.lotDataBean = {};
                    $scope.searchedData = [];
                    $scope.searchedDataFromDb = [];
                    $scope.listFilled = false;
                    $scope.lotListTodisplay = [];
                    $scope.searchCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);
                    $scope.lotDataBean.featureCustomMap = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("lotEdit");
                    $scope.flag = {};
                    $scope.flag.searchFieldNotAvailable = false;
                    $scope.dbType = {};
                    $scope.modelAndHeaderList = [];
                    $scope.modelAndHeaderListForLot = [];
                    templateData.then(function (section) {
                        $scope.generalSearchTemplate = section['genralSection'];
                        $scope.searchInvoiceTemplate = [];
                        $scope.searchParcelTemplate = [];
                        $scope.searchLotTemplate = [];
                        var invoiceDbFieldName = [];
                        var parcelDbFieldName = [];
                        var lotDbFieldName = [];
                        $scope.fieldIdNameMap = {};
                        if ($scope.generalSearchTemplate != null && $scope.generalSearchTemplate.length > 0) {
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
                                featureMap[item.model] = item.featureName;
                                $scope.modelAndHeaderList.push({model: item.model, header: item.label});
                                if (item.fromModel) {
                                    $scope.lotLabelListForUiGrid.push({name: item.fromModel, displayName: item.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.editLotScreenRule(row, \'' + item.fromModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                    $scope.fieldIdNameMap[item.fieldId] = item.fromModel;
                                } else if (item.toModel) {
                                    $scope.lotLabelListForUiGrid.push({name: item.toModel, displayName: item.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.editLotScreenRule(row, \'' + item.toModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                    $scope.fieldIdNameMap[item.fieldId] = item.toModel;
                                } else if (item.model) {
                                    $scope.lotLabelListForUiGrid.push({name: item.model, displayName: item.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.editLotScreenRule(row, \'' + item.model + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                    $scope.fieldIdNameMap[item.fieldId] = item.model;
                                }
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
                        } else {
                            $scope.flag.searchFieldNotAvailable = true;
                        }
                        $scope.searchResetFlag = true;
                        $scope.dataRetrieved = true;
                    }, function (reason) {

                    }, function (update) {

                    });
                }
            };
            $scope.editLotScreenRule = function (row, colField) {
                var color;
                if (!!row.entity.screenRuleDetailsWithDbFieldName && row.entity.screenRuleDetailsWithDbFieldName[colField] !== undefined && row.entity.screenRuleDetailsWithDbFieldName[colField] !== null) {
                    if ($scope.selectedLot.length === 0 || ($scope.selectedLot[0].$$lotId$$ !== row.entity.$$lotId$$)) {
                        color = row.entity.screenRuleDetailsWithDbFieldName[colField].colorCode;
                    }
                }
                return color;
            };
            $scope.editSublotScreenRule = function (row, colField) {
                var color;
                if (!!row.entity.screenRuleDetailsWithDbFieldName && row.entity.screenRuleDetailsWithDbFieldName[colField] !== undefined && row.entity.screenRuleDetailsWithDbFieldName[colField] !== null) {
                    if (associatedSubLotIds.length === 0 || (associatedSubLotIds.indexOf(row.entity.$$subLotId$$)) === -1) {
                        color = row.entity.screenRuleDetailsWithDbFieldName[colField].colorCode;
                    }
                }
                return color;
            };
            $scope.initializeData(true);
            $scope.initEditLotForm = function (editLotForm) {
                $scope.editLotForm = editLotForm;
//                console.log($scope.editLotForm);
            };
            $scope.retrieveSearchedData = function (editLotForm) {
                $scope.selectOneParameter = false;
                $scope.gridOptions = {};
                $scope.searchedData = [];
                $scope.searchedDataFromDbForUiGrid = [];
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
                        $scope.lotDataBean.featureCustomMap = {};
                        $scope.lotDataBean.featureCustomMapValue = {};
                        $scope.map = {};
//                        console.log('search custom : ' + JSON.stringify($scope.searchCustom));
//                        console.log('feature map :: ' + JSON.stringify(featureMap));
                        var finalMap = {};
                        var searchResult = DynamicFormService.convertSearchData($scope.searchInvoiceTemplate, $scope.searchParcelTemplate, $scope.searchLotTemplate, null, $scope.searchCustom);
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
                        $scope.lotDataBean.featureCustomMapValue = finalMap;
                        $scope.lotDataBean.hasPacket = false;
                        $scope.lotDataBean.featureDbFieldMap = featureCustomMap;
                        $scope.lotDataBean.ruleConfigMap = {
                            fieldIdNameMap: $scope.fieldIdNameMap,
                            featureName: "editLot"
                        };
                        LotService.search($scope.lotDataBean, function (res) {

                            var lotField = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                            lotField.then(function (section) {
                                $scope.lotCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);
                                $scope.lotTemplate = section['genralSection'];

                                $scope.searchedDataFromDb = angular.copy(res);

                                var success = function (result)
                                {
//                                    console.log('res : ' + JSON.stringify(result));
                                    $scope.searchedData = angular.copy(result);
                                    angular.forEach($scope.searchedData, function (itr) {
                                        ///////////////////////////////////////////UI-GRID/////////////////////////////////////////////////////
                                        angular.forEach($scope.lotLabelListForUiGrid, function (list) {
                                            if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                                itr.categoryCustom[list.name] = "NA";
                                            }
                                            else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                                if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                    itr.categoryCustom[list.name] = "NA";
                                                }
                                            }
//                                            itr.categoryCustom["value"] = itr.value;
                                        });
                                        itr.categoryCustom.$$lotId$$ = itr.value;
                                        itr.categoryCustom.screenRuleDetailsWithDbFieldName = itr.screenRuleDetailsWithDbFieldName;
                                        $scope.searchedDataFromDbForUiGrid.push(itr.categoryCustom);
                                    });
                                    $scope.gridOptions = {};
                                    $scope.gridOptions.enableFiltering = true;
                                    $scope.gridOptions.data = $scope.searchedDataFromDbForUiGrid;
                                    $scope.gridOptions.columnDefs = $scope.lotLabelListForUiGrid;
                                    $scope.gridOptions.multiSelect = false;
                                    $scope.gridOptions.enableRowSelection = true;
                                    $scope.gridOptions.enableSelectAll = false;
                                    $scope.selectedLot = [];
                                    $scope.gridOptions.onRegisterApi = function (gridApi) {
                                        $scope.gridApi = gridApi;
                                        gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                                            $scope.selectedLot = gridApi.selection.getSelectedRows();
                                        });
                                        gridApi.selection.on.rowSelectionChangedBatch($scope, function (rows) {
                                            $scope.selectedLot = gridApi.selection.getSelectedRows();
                                        });
                                    };
                                    window.setTimeout(function () {
                                        $(window).resize();
                                        $(window).resize();
                                    }, 100);
                                    $scope.listFilled = true;

                                    $rootScope.unMaskLoading();
//                                    $scope.onCanel();
                                }
                                DynamicFormService.convertorForCustomField(res, success);


                            }, function (reason) {

                            }, function (update) {

                            });
//                            $scope.searchedDataFromDb = angular.copy(res);
//                            var data = DynamicFormService.getValuesOfComponentFromId(res, $scope.generalSearchTemplate);
//                            $scope.searchedData = angular.copy(data);
//                            $scope.listFilled = true;
//                            $scope.searchCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);
//                            $rootScope.unMaskLoading();
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
                    var msg = "Please select atleast one search criteria for search";
                    var type = $rootScope.warning;
                    $rootScope.addMessage(msg, type);
                }
            };
            $scope.reset = function (sectionTobeReset) {
                if (sectionTobeReset === "searchCustom") {
                    $scope.searchCustom = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("lotEdit");
                    $scope.dbType = {};
                    templateData.then(function (section) {
                        $scope.generalSearchTemplate = section['genralSection'];
                        $scope.searchResetFlag = true;
                        $rootScope.unMaskLoading();
                        $scope.flag.customFieldGenerated = true;
                    }, function (reason) {
                    }, function (update) {
                    });
                } else if (sectionTobeReset === "lotCustom") {
                    $scope.lotCustom = {};
                    var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
//                    $scope.lotDbType = {};
                    templateData.then(function (section) {
                        var lotField = [];
                        var result = Object.keys($scope.response).map(function (key, value) {
                            angular.forEach(this[key], function (itr) {
                                if (key !== 'Lot#P#') {
                                    lotField.push({Lot: itr});
                                }
                            });
                        }, $scope.response);
                        $scope.updateLotTemplate = null;
                        $scope.updateLotTemplate = section['genralSection'];
                        $scope.updateLotTemplate = DynamicFormService.retrieveCustomData($scope.updateLotTemplate, lotField);
                        $scope.lotEditShow = true;
                        $rootScope.unMaskLoading();
                        $scope.flag.customFieldGenerated = true;
//                        $scope.lotDataBean.lotDbType = $scope.lotDbType;
                    }, function (reason) {
                    }, function (update) {
                    });
                }
            };
            $scope.onCanel = function (editLotForm) {
                if (editLotForm != null) {
                    editLotForm.$dirty = false;
                    $scope.flag.showUpdatePage = false;
                }
//                $scope.categoryCustom = DynamicFormService.resetSection($scope.generalLotTemplate);
                $rootScope.editLot = false;
                $rootScope.lotByParcel = null;
                $scope.submitted = false;
                $scope.lotEditShow = false;
                $scope.selectedLot = [];
                $scope.reset("lotCustom");
            };

            $scope.onCanelOfSearch = function (editLotForm) {
                if ($scope.editLotForm != null) {
                    $scope.editLotForm.$dirty = false;
                }
                $scope.searchedData = [];
//                $scope.parcelCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);
                $scope.listFilled = false;
                $scope.searchResetFlag = false;
                $scope.selectedLot = [];
                $scope.gridOptions = {};
                $scope.reset("searchCustom");
            };

            $scope.saveLot = function (editLotForm) {
                $scope.submitted = true;
                angular.forEach($scope.listOfModelsOfDateType, function (listOfModel)
                {
                    if ($scope.lotCustom.hasOwnProperty(listOfModel))
                    {
                        if ($scope.lotCustom[listOfModel] !== null && $scope.lotCustom[listOfModel] !== undefined)
                        {
                            $scope.lotCustom[listOfModel] = new Date($scope.lotCustom[listOfModel]);
                        } else
                        {
                            $scope.lotCustom[listOfModel] = '';
                        }
                    }
                });
                console.log("form validation :" + JSON.stringify(editLotForm.$valid));
                if (editLotForm.$valid && $scope.checkAddLotFormExtraValidation()
                        && $scope.checkAddLotFormExtraValidationWihoutSublot()) {
                    $rootScope.maskLoading();
                    var dataToSend = {
                        featureName: 'editLot',
                        entityId: $scope.lotIdForConstraint,
                        entityType: 'lot',
                        currentFieldValueMap: $scope.lotCustom,
                        dbType: $scope.lotDbType,
                        otherEntitysIdMap: {invoiceId: $scope.invoiceIdForConstraint, parcelId: $scope.parcelIdForConstraint}
                    };
                    console.log('post rule dataToSend::::' + JSON.stringify(dataToSend));
                    RuleExecutionService.executePostRule(dataToSend, function (res) {
                        if (!!res.validationMessage) {
                            var type = $rootScope.warning;
                            $rootScope.addMessage(res.validationMessage, type);
                            $rootScope.unMaskLoading();
                        } else {
                            $scope.lotDataBean.lotCustom = $scope.lotCustom;
                            $scope.lotDataBean.lotDbType = angular.copy($scope.lotDbType);
                            $scope.lotDataBean.subLots = associatedSubLotIds;
                            LotService.update($scope.lotDataBean, function (response) {
                                $scope.submitted = false;
                                $scope.editLotForm.$dirty = false;
                                $scope.initializeData(true);
                                $rootScope.editLot = false;
                                $rootScope.lotByParcel = null;
                                $scope.selectedLot = [];
                                $scope.gridOptions = {};
//                        $location.path("/editlot");
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

                }
            };
            $scope.checkAddLotFormExtraValidation = function () {
                if (associatedSubLotIds.length > 0) {
                    if (associatedSubLotCaratTotal != $scope.lotCustom['carat_of_lot$NM$Double'] ||
                            associatedSubLotPiecesTotal != $scope.lotCustom['no_of_pieces_of_lot$NM$Long']) {
                        $rootScope.addMessage("Carat and pieces value should be equal to selected sub lot total carat and pieces", $rootScope.error);
                        return false;
                    }
                    else {
                        return true;
                    }
                }
                else {
                    return true;
                }
            };
            $scope.checkAddLotFormExtraValidationWihoutSublot = function () {
                if (associatedSubLotIds.length <= 0) {
                    if ($scope.lotCustom.stockCarat < $scope.lotCustom['carat_of_lot$NM$Double'] ||
                            $scope.lotCustom.stockPieces < $scope.lotCustom['no_of_pieces_of_lot$NM$Long']) {
                        $rootScope.addMessage("Carat or pieces values should not be greater than stock carat and stock pieces", $rootScope.error);
                        return false;
                    }
                    else {
                        return true;
                    }
                }
                else {
                    return true;
                }
            };
            $scope.editLot = function (lotObj) {
                var invoiceDbFieldName = [];
                var parcelDbFieldName = [];
                var lotDbFieldName = [];
                var mapToSent = {};
                if ((lotObj === undefined || lotObj === null)) {
                    lotObj = $scope.selectedLot[0];
                }

                if (lotObj != null) {
                    $rootScope.maskLoading();
                    if ($scope.searchedDataFromDb !== undefined && $scope.searchedDataFromDb !== null) {
                        for (var i = 0; i < $scope.searchedDataFromDb.length; i++) {
                            if (lotObj.lotID$AG$String === $scope.searchedDataFromDb[i].categoryCustom.lotID$AG$String) {
                                lotObj = angular.copy($scope.searchedDataFromDb[i]);
                                break;
                            }
                        }
                    }
                    $scope.invoiceIdForConstraint = lotObj.description;
                    $scope.parcelIdForConstraint = lotObj.label;
                    $scope.lotIdForConstraint = lotObj.value;
                    $scope.flag.showUpdatePage = false;
                    $scope.preRuleSatisfied = false;
                    var dataToSend = {
                        featureName: 'editLot',
                        entityId: lotObj.value,
                        entityType: 'lot'
                    };
                    RuleExecutionService.executePreRule(dataToSend, function (res) {
                        //Prevent the record from edit if pre rule satisfies.                      
                        if (!!res.validationMessage) {
                            $scope.preRuleSatisfied = true;
                            var type = $rootScope.warning;
                            $rootScope.addMessage(res.validationMessage, type);
                            $rootScope.unMaskLoading();
                        } else {
                            $scope.flag.showUpdatePage = true;
                            CustomFieldService.retrieveDesignationBasedFieldsBySection(["lotEdit", "GEN"], function (response) {
                                if (response.Lot == null || response.Lot.length == 0) {
                                    $scope.noFieldConfiguredForLot = true;
                                }

                                // Retrieve Lot Parent
//                        console.log("response :"+JSON.stringify(response));
                                $scope.lotDataBean = {};
//                        $scope.lotParentCustom = DynamicFormService.resetSection($scope.updateLotParentTemplate);
                                var templateDataForUpdate = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
//                        $scope.lotDbType = {};
                                templateDataForUpdate.then(function (section) {
                                    $scope.updateLotParentTemplate = section['genralSection'];
                                    var lotFieldParent = [];
                                    $scope.response = angular.copy(response);
                                    var result = Object.keys(response).map(function (key, value) {
                                        angular.forEach(this[key], function (itr) {
                                            if (key === 'Lot#P#') {
                                                lotFieldParent.push({Lot: itr});
                                            }
                                        });
                                    }, response);
                                    $scope.updateLotParentTemplate = DynamicFormService.retrieveCustomData($scope.updateLotParentTemplate, lotFieldParent);
                                    angular.forEach($scope.updateLotParentTemplate, function (itr) {
                                        if (itr.model) {
                                            lotDbFieldName.push(itr.model);
                                        }
                                    });
                                    if (lotDbFieldName.length > 0) {
                                        mapToSent['lotDbFieldName'] = lotDbFieldName;
                                    }

                                }, function (reason) {

                                }, function (update) {
                                });
//                        Retrieve invoice template code
//                        $scope.invoiceCustom = DynamicFormService.resetSection($scope.updateInvoiceTemplate);
                                var templateDataForUpdate = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                                $scope.dbTypeForInvoice = {};
                                templateDataForUpdate.then(function (section) {
//                            console.log(JSON.stringify(templateDataForUpdate));
                                    $scope.updateInvoiceTemplate = section['genralSection'];
                                    var invoiceField = [];
                                    var result = Object.keys(response).map(function (key, value) {
                                        angular.forEach(this[key], function (itr) {
                                            if (key === 'Invoice#P#') {
                                                invoiceField.push({Invoice: itr});
                                            }
                                        });
                                    }, response);
                                    $scope.updateInvoiceTemplate = DynamicFormService.retrieveCustomData($scope.updateInvoiceTemplate, invoiceField);
                                    angular.forEach($scope.updateInvoiceTemplate, function (itr) {
                                        if (itr.model) {
                                            invoiceDbFieldName.push(itr.model);
                                        }
                                    });
                                    if (invoiceDbFieldName.length > 0) {
                                        mapToSent['invoiceDbFieldName'] = invoiceDbFieldName;
                                    }
                                    if (lotObj.custom3 != null) {
                                        $scope.invoiceDataBean = {};
//                                $scope.invoiceCustom = lotObj.custom3;
//                                $scope.invoiceDataBean.invoiceCustom = $scope.updateParcelData;
                                        $scope.invoiceDataBean.invoiceDbType = $scope.dbTypeForInvoice;
                                    }
                                }, function (reason) {

                                }, function (update) {
                                });

//                        $scope.parcelCustom = DynamicFormService.resetSection($scope.updateParcelTemplate);
                                var templateDataOfParcel = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
                                $scope.dbTypeForParcel = {};
                                templateDataOfParcel.then(function (section) {
//                            console.log(JSON.stringify(templateDataForUpdate));
                                    $scope.updateParcelTemplate = section['genralSection'];
                                    var parcelField = [];
                                    var result = Object.keys(response).map(function (key, value) {
                                        angular.forEach(this[key], function (itr) {
                                            if (key === 'Parcel#P#') {
                                                parcelField.push({Parcel: itr});
                                            }
                                        });
                                    }, response);
                                    $scope.updateParcelTemplate = DynamicFormService.retrieveCustomData($scope.updateParcelTemplate, parcelField);
                                    angular.forEach($scope.updateParcelTemplate, function (itr) {
                                        if (itr.model) {
                                            parcelDbFieldName.push(itr.model);
                                        }
                                    });
                                    if (parcelDbFieldName.length > 0) {
                                        mapToSent['parcelDbFieldName'] = parcelDbFieldName;
                                    }
                                    $scope.flag.customFieldGeneratedForUpdate = true;
                                    if (lotObj.custom4 != null) {
                                        $scope.parcelDataBean = {};
//                                $scope.parcelCustom = lotObj.custom4;
//                                $scope.invoiceDataBean.invoiceCustom = $scope.updateParcelData;
                                        $scope.parcelDataBean.parcelDbType = $scope.dbTypeForParcel;
                                    }
                                }, function (reason) {

                                }, function (update) {
                                });

                                $scope.lotCustom = DynamicFormService.resetSection($scope.updateLotTemplate);
                                var templateDataForUpdate = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");

                                templateDataForUpdate.then(function (section) {
                                    if (!($scope.lotDbType != null))
                                        $scope.lotDbType = {};
                                    $scope.updateLotTemplate = section['genralSection'];
                                    $scope.listOfModelsOfDateType = [];
                                    if ($scope.updateLotTemplate !== null && $scope.updateLotTemplate !== undefined)
                                    {
                                        angular.forEach($scope.updateLotTemplate, function (updateTemplate)
                                        {
                                            if (updateTemplate.type !== null && updateTemplate.type !== undefined && updateTemplate.type === 'date')
                                            {
                                                $scope.listOfModelsOfDateType.push(updateTemplate.model);
                                            }
                                            if (updateTemplate.model === 'in_stock_of_lot$UMS$String' || updateTemplate.model === 'allot_to_lot$UMS$String' || updateTemplate.model === 'lotID$AG$String') {
                                                updateTemplate.isViewFromDesignation = true;
                                            }
                                        });
                                    }
                                    var lotField = [];
                                    var result = Object.keys(response).map(function (key, value) {
                                        angular.forEach(this[key], function (itr) {
                                            if (key === 'Lot') {
                                                lotField.push({Lot: itr});
                                            }
                                        });
                                    }, response);

                                    $scope.updateLotTemplate = DynamicFormService.retrieveCustomData($scope.updateLotTemplate, lotField);
                                    $scope.fieldIdNameMap = {};
                                    angular.forEach($scope.updateLotTemplate, function (itr) {
                                        if (itr.model && lotDbFieldName.indexOf(itr.model) === -1) {
                                            lotDbFieldName.push(itr.model);
                                            $scope.fieldIdNameMap[itr.fieldId] = itr.model;
                                        }
                                    });
                                    var ids = [];
                                    ids.push(lotObj.value);
                                    mapToSent['lotObjectId'] = ids;
                                    if (lotDbFieldName.length > 0) {
                                        mapToSent['lotDbFieldName'] = lotDbFieldName;
                                    }
                                    mapToSent.ruleConfigMap = {
                                        fieldIdNameMap: $scope.fieldIdNameMap,
                                        featureName: "editLot"
                                    };
                                    LotService.retrieveLotById(mapToSent, function (response) {
                                        $scope.flag.customFieldGeneratedForUpdate = true;
                                        $scope.lotDataBean = {};
                                        $scope.lotDataBean.id = $scope.lotIdForConstraint;
                                        $scope.lotDataBean.franchise = response.custom7;
                                        if (response.custom3 == null) {
                                            $scope.invoiceCustom = {};
                                        }
                                        if (response.custom4 == null) {
                                            $scope.parcelCustom = {};
                                        }
                                        $scope.invoiceCustom = response.custom3;
                                        $scope.parcelCustom = angular.copy(response.custom4);
                                        if (response.custom1 !== null && response.custom1 !== undefined) {
//                                    if (response.custom1.hasOwnProperty("carate_range_of_lot$DRP$Long") && response.custom1["carate_range_of_lot$DRP$Long"]!==undefined && response.custom1["carate_range_of_lot$DRP$Long"]!==null)
//                                    {
//                                        response.custom1['carate_range_of_lot$DRP$Long'] = response.custom1['carate_range_of_lot$DRP$Long'].toString();
//                                    }
                                            angular.forEach($scope.listOfModelsOfDateType, function (listOfModel)
                                            {
                                                if (response.custom1.hasOwnProperty(listOfModel))
                                                {
                                                    if (response.custom1[listOfModel] !== null && response.custom1[listOfModel] !== undefined)
                                                    {
                                                        response.custom1[listOfModel] = new Date(response.custom1[listOfModel]);
                                                    } else
                                                    {
                                                        response.custom1[listOfModel] = '';
                                                    }
                                                }
                                            })
                                            response.custom1.stockPieces += angular.copy(response.custom1["no_of_pieces_of_lot$NM$Long"]);
                                            response.custom1.stockCarat += angular.copy(response.custom1["carat_of_lot$NM$Double"]);
                                            delete response.custom1['stockPieces'];
                                            delete response.custom1['stockCarat'];
                                            $scope.lotCustom = angular.copy(response.custom1);
                                            $scope.lotParentCustom = angular.copy(response.custom1);
                                            $scope.lotScreenRules = response.screenRuleDetailsWithDbFieldName;
                                        } else {
                                            $scope.lotParentCustom = {};
                                            $scope.lotCustom = {};
                                        }
                                        $scope.lotDataBean.lotDbType = angular.copy($scope.lotDbType);
                                        $scope.lotDataBean.subLots = response.otherValues;
                                        parcelIdOfEditLot = response.label;
                                        console.log('calling you.....................' + lotObj.value);
                                        retrieveAndSelectSubLot(lotObj.value);
                                        $scope.lotEditShow = true;
                                        $rootScope.unMaskLoading();
                                    }, function () {
                                        $rootScope.unMaskLoading();
                                        var msg = "Could not retrieve parcel, please try again.";
                                        var type = $rootScope.error;
                                        $rootScope.addMessage(msg, type);
                                    });
//                            $rootScope.unMaskLoading();

                                }, function (reason) {

                                }, function (update) {
                                });
                                $scope.flag.customFieldGenerated = true;

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

            var retrieveAndSelectSubLot = function (lotId) {
                //Associate sublot with Lot

                $scope.gridOptionsSubLot = {
                    enableRowSelection: true,
                    enableSelectAll: true,
                    enableFiltering: true,
                    multiSelect: true,
                    data: [],
                    columnDefs: [],
                    onRegisterApi: function (gridApi) {
                        gridApiSubLot = gridApi;
                        gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                            getSubLotIdsFromSelectedRowData(gridApi.selection.getSelectedRows());
                        });
                        gridApi.selection.on.rowSelectionChangedBatch($scope, function (rows) {
                            getSubLotIdsFromSelectedRowData(gridApi.selection.getSelectedRows());
                        });
                    }
                };

                var getSubLotIdsFromSelectedRowData = function (selectedRows) {
                    associatedSubLotIds = [];
                    associatedSubLotCaratTotal = 0;
                    associatedSubLotPiecesTotal = 0;
                    for (var rowId in selectedRows) {
                        associatedSubLotIds.push(selectedRows[rowId].id);
                        if (!!selectedRows[rowId][fieldNameSubLot.carat]) {
                            associatedSubLotCaratTotal += selectedRows[rowId][fieldNameSubLot.carat];
                        }
                        if (!!selectedRows[rowId][fieldNameSubLot.pieces]) {
                            associatedSubLotPiecesTotal += selectedRows[rowId][fieldNameSubLot.pieces];
                        }
                    }
                    $scope.lotCustom['carat_of_lot$NM$Double'] = associatedSubLotCaratTotal;
                    $scope.lotCustom['no_of_pieces_of_lot$NM$Long'] = associatedSubLotPiecesTotal;
                };

                var uiGridSubLotCustomFieldData = {};
                CustomFieldService.retrieveDesignationBasedFieldsBySection(["lotEdit", "ASL"], function (response) {
                    console.log(JSON.stringify(response));
                    if (!!response.SubLot && response.SubLot.length > 0) {
                        $scope.noFieldConfiguredForSubLot = true;
                    }
                    $scope.sublotDataBean = {};
                    var templateDataForUpdate = DynamicFormService.retrieveSectionWiseCustomFieldInfo("subLotEntity");
                    if (!($scope.dbTypeForUpdate))
                        $scope.dbTypeForUpdate = {};
                    templateDataForUpdate.then(function (section) {
                        uiGridSubLotCustomFieldData.template = null;
                        uiGridSubLotCustomFieldData.template = section['genralSection'];
                        $scope.listOfModelsOfDateType = [];
                        var sublotField = [];
                        Object.keys(response).map(function (key, value) {
                            angular.forEach(this[key], function (itr) {
                                if (key === 'SubLot') {
                                    sublotField.push({Sublot: itr});
                                }
                            });
                        }, response);
                        console.log("check 1");
                        $scope.editFieldName = [];
                        $scope.fieldIdNameMap = {};
                        uiGridSubLotCustomFieldData.template = DynamicFormService.retrieveCustomData(uiGridSubLotCustomFieldData.template, sublotField);
                        angular.forEach(uiGridSubLotCustomFieldData.template, function (itr) {
                            if (itr.fromModel) {
                                $scope.fieldIdNameMap[itr.fieldId] = itr.fromModel;
                                $scope.gridOptionsSubLot.columnDefs.push({name: itr.fromModel, displayName: itr.label, minWidth: 200,
                                    cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.editSublotScreenRule(row, \'' + itr.fromModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                });
                                if ($scope.editFieldName.indexOf(itr.fromModel) === -1)
                                    $scope.editFieldName.push(itr.fromModel);
                            } else if (itr.toModel) {
                                $scope.fieldIdNameMap[itr.fieldId] = itr.toModel;
                                $scope.gridOptionsSubLot.columnDefs.push({name: itr.toModel, displayName: itr.label, minWidth: 200,
                                    cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.editSublotScreenRule(row, \'' + itr.toModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                });
                                if ($scope.editFieldName.indexOf(itr.toModel) === -1)
                                    $scope.editFieldName.push(itr.toModel);
                            } else if (itr.model) {
                                $scope.fieldIdNameMap[itr.fieldId] = itr.model;
                                $scope.gridOptionsSubLot.columnDefs.push({name: itr.model, displayName: itr.label, minWidth: 200,
                                    cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.editSublotScreenRule(row, \'' + itr.model + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                });
                                if ($scope.editFieldName.indexOf(itr.model) === -1)
                                    $scope.editFieldName.push(itr.model);
                            }
                        });
                        $scope.fieldNotConfigured = false;
                        $scope.showConfigMsg = false;
                        var fieldsInTemplate = [];
                        if (uiGridSubLotCustomFieldData.template !== undefined && uiGridSubLotCustomFieldData.template !== null && uiGridSubLotCustomFieldData.template.length > 0) {
                            for (var i = 0; i < uiGridSubLotCustomFieldData.template.length; i++) {
                                fieldsInTemplate.push(angular.copy(uiGridSubLotCustomFieldData.template[i].model));
                            }
                        }
                        if (fieldsInTemplate.length > 0 && $scope.mandatorySublotFields && $scope.mandatorySublotFields.length > 0) {
                            for (var field = 0; field < $scope.mandatorySublotFields.length; field++) {
                                if (fieldsInTemplate.indexOf($scope.mandatorySublotFields[field]) === -1) {
                                    $scope.fieldNotConfigured = true;
                                    break;
                                }
                            }
                        } else {
                            $scope.fieldNotConfigured = false;
                        }
                        if ($scope.fieldNotConfigured) {
                            $scope.showConfigMsg = true;
                        }
                        $scope.map = {};
                        $scope.map['dbFieldNames'] = $scope.editFieldName;
                        console.log($scope.editFieldName.length > 0);
                        if ($scope.editFieldName.length > 0) {
                            var data = {
                                parcelId: parcelIdOfEditLot,
                                map: $scope.map,
                                ruleConfigMap: {
                                    fieldIdNameMap: $scope.fieldIdNameMap,
                                    featureName: "addLot"
                                },
                                excludeSubLotWithAssociatedLot: true,
                                includeSubLotWithAssociatedLot: lotId
                            };
                            SublotService.retrieveSublotsbyParcel(data, function (res) {
                                var subLots;
                                var success = function (result) {
                                    subLots = angular.copy(result);
                                    for (var sublot in subLots) {
                                        var subLotObjWithId = {};
                                        subLots[sublot].categoryCustom.$$subLotId$$ = subLots[sublot].value;
                                        subLots[sublot].categoryCustom.screenRuleDetailsWithDbFieldName = subLots[sublot].screenRuleDetailsWithDbFieldName;
                                        angular.extend(subLotObjWithId, subLots[sublot].categoryCustom, {id: subLots[sublot].value})
                                        $scope.gridOptionsSubLot.data.push(subLotObjWithId);
                                    }
                                    //Looping this again as it is in timeout so creating issue to push data of grid and also keept it selected
                                    $timeout(function () {
                                        for (var sublot in subLots) {
                                            if (!!$scope.lotDataBean.subLots && $scope.lotDataBean.subLots.indexOf(subLots[sublot].value) > -1 && gridApiSubLot) {
                                                gridApiSubLot.selection.selectRow($scope.gridOptionsSubLot.data[sublot]);
                                            }
                                        }
                                    });
                                };
                                var newVar = angular.copy(res);
                                if (newVar !== undefined) {
                                    DynamicFormService.convertorForCustomField(newVar, success);
                                }

                                $rootScope.unMaskLoading();
                            }
                            , function () {
                                var msg = "Could not save, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                                $rootScope.unMaskLoading();
                            });
                        }
                    }, function (reason) {

                    }, function (update) {
                    });
                }, function () {
                    $rootScope.unMaskLoading();
                    var msg = "Failed to retrieve data";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                });
            };

            if ($rootScope.editLot) {
                $scope.editLot($rootScope.lotByParcel);
            }
            $scope.deleteLot = function () {
                $("#deleteLotPopUp").modal("show");
            };
            $scope.deleteLotFromPopup = function () {
                if ($scope.lotIdForConstraint !== undefined && $scope.lotIdForConstraint !== null) {
                    $rootScope.maskLoading();
                    LotService.deleteLot($scope.lotIdForConstraint, function () {
                        $rootScope.unMaskLoading();
                        $scope.hideLotPopUp();
                        $scope.onCanel($scope.editLotForm);
                        $scope.searchedData = [];
                        $scope.listFilled = false;
                    }, function () {
                        $rootScope.unMaskLoading();
                    });
                }
            };
            $scope.hideLotPopUp = function () {
                $("#deleteLotPopUp").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            }
            ;
        }]);
});