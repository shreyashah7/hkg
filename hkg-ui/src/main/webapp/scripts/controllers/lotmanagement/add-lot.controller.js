define(['hkg', 'lotService', 'customFieldService', 'parcelService', 'lotmanagement/edit-lot.controller', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'printBarcodeValue', 'dynamicForm', 'accordionCollapse', 'sublotService', 'ruleExecutionService'], function (hkg, lotService) {
    hkg.register.controller('AddLotController', ["$rootScope", "$scope", "LotService", "$timeout", "$filter", "$location", "$window", "CustomFieldService", "DynamicFormService", "ParcelService", "SublotService", "$q", "RuleExecutionService", function ($rootScope, $scope, LotService, $timeout, $filter, $location, $window, CustomFieldService, DynamicFormService, ParcelService, SublotService, $q, RuleExecutionService) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "addLot";
            $scope.entity = "ADDLOT.";
            $rootScope.activateMenu();
            $scope.flag = {};
            $scope.flag.searchFieldNotAvailable = false;
            $scope.remCaratMsg = "";

            var featureDbFieldMap = {}, fieldIdNameMap = {}, associatedSubLotIds = [], gridApiSubLot, gridApiParcel,
                    fieldNameSubLot = {carat: "carat$TF$Double", pieces: "no_of_pieces_of_sublot$NM$Long"}, associatedSubLotCaratTotal,
                    associatedSubLotPiecesTotal;

            $scope.currentYear = $rootScope.getCurrentServerDate().getFullYear().toString().substring(2, 4);
            $scope.fieldsToClear = ['carat_of_lot$NM$Double', 'no_of_pieces_of_lot$NM$Long'];
            $rootScope.maskLoading();
            $scope.searchedDataFromDbForUiGrid = [];
            $scope.lotDataForUiGrid = [];
            $scope.lotListForUIGrid = [];
            $scope.parcelLabelListForUiGrid = [];
            $scope.lotDataBean = {};
            $scope.parcelDataBean = {};
            $scope.lotListTodisplay = [];
            $scope.modifiedLotListToDisplay = [];
            $scope.AddLot = this;
            $scope.searchCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);
//            $scope.parcelDataBean.featureCustomMap = {};
            var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("lotAdd");
            $scope.dbType = {};
            var featureMap = {};
            $scope.modelAndHeaderList = [];
            $scope.modelAndHeaderListForLot = [];
            $scope.processingFlag = {};
//                var rootNodeRetrieved = function (res) {                   
//                    $scope.designationIdForInStock = res.data['forIssue'].toString();                    
//                };
//                LotService.retrieveRootNodeDesignationIds(rootNodeRetrieved);
            templateData.then(function (section) {
                $scope.generalSearchTemplate = angular.copy(section['genralSection']);
                if (section['genralSection'] === undefined || section['genralSection'] === null) {
                    $scope.generalSearchTemplate = [];
                }
                $scope.searchInvoiceTemplate = [];
                $scope.searchParcelTemplate = [];
                var invoiceDbFieldName = [];
                var parcelDbFieldName = [];
                fieldIdNameMap = {};
                if ($scope.generalSearchTemplate != null && $scope.generalSearchTemplate.length > 0) {
                    for (var i = 0; i < $scope.generalSearchTemplate.length; i++) {
                        var item = $scope.generalSearchTemplate [i];
                        if (item.featureName.toLowerCase() === 'invoice') {
                            $scope.searchInvoiceTemplate.push(angular.copy(item));
                            invoiceDbFieldName.push(angular.copy(item.model))
                            fieldIdNameMap[item.fieldId] = item.model;
                        } else if (item.featureName.toLowerCase() === 'parcel') {
                            $scope.searchParcelTemplate.push(angular.copy(item));
                            parcelDbFieldName.push(angular.copy(item.model))
                            fieldIdNameMap[item.fieldId] = item.model;
                        }
                        featureMap[item.model] = item.featureName;
                        $scope.modelAndHeaderList.push({model: item.model, header: item.label});
                        if (item.fromModel) {
                            $scope.parcelLabelListForUiGrid.push({name: item.fromModel, displayName: item.label, minWidth: 200,
                                cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.searchedParcelScreenRule(row, \'' + item.fromModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                            });
                        } else if (item.toModel) {
                            $scope.parcelLabelListForUiGrid.push({name: item.toModel, displayName: item.label, minWidth: 200,
                                cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.searchedParcelScreenRule(row, \'' + item.toModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                            });
                        } else if (item.model) {
                            $scope.parcelLabelListForUiGrid.push({name: item.model, displayName: item.label, minWidth: 200,
                                cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.searchedParcelScreenRule(row, \'' + item.model + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                            });
                        }
                    }
                    if (invoiceDbFieldName.length > 0) {
                        featureDbFieldMap['invoice'] = invoiceDbFieldName;
                    }
                    if (parcelDbFieldName.length > 0) {
                        featureDbFieldMap['parcel'] = parcelDbFieldName;
                    }
                } else {
                    $scope.flag.searchFieldNotAvailable = true;
                }
                $scope.searchResetFlag = true;
                $scope.dataRetrieved = true;
                $rootScope.unMaskLoading();
            }, function (reason) {

            }, function (update) {

            });

            $scope.searchedParcelScreenRule = function (row, colField) {
                var color;

                if (!!row.entity.screenRuleDetailsWithDbFieldName && row.entity.screenRuleDetailsWithDbFieldName[colField] !== undefined && row.entity.screenRuleDetailsWithDbFieldName[colField] !== null) {
                    if ($scope.selectedParcel.length === 0 || ($scope.selectedParcel[0].$$parcelId$$ !== row.entity.$$parcelId$$)) {
                        color = row.entity.screenRuleDetailsWithDbFieldName[colField].colorCode;
                    }
                }
                return color;
            };
            $scope.addSublotScreenRule = function (row, colField) {
                var color;

                if (!!row.entity.screenRuleDetailsWithDbFieldName && row.entity.screenRuleDetailsWithDbFieldName[colField] !== undefined && row.entity.screenRuleDetailsWithDbFieldName[colField] !== null) {
                    if (associatedSubLotIds.length === 0 || (associatedSubLotIds.indexOf(row.entity.$$subLotId$$) === -1)) {
                        color = row.entity.screenRuleDetailsWithDbFieldName[colField].colorCode;
                    }
                }
                return color;
            };
            $scope.addedLotScreenRule = function (row, colField) {
                var color;
                if (!!row.entity.screenRuleDetailsWithDbFieldName && row.entity.screenRuleDetailsWithDbFieldName[colField] !== undefined && row.entity.screenRuleDetailsWithDbFieldName[colField] !== null) {
//                    if ($scope.selectedParcel.length === 0 || ($scope.selectedParcel[0].$$parcelId$$ !== row.entity.$$parcelId$$)) {
                    color = row.entity.screenRuleDetailsWithDbFieldName[colField].colorCode;
//                    }
                }
                return color;
            };
            $scope.initAddLotForm = function (addLotForm) {
                $scope.addLotForm = addLotForm;
            };
            $scope.retrieveSearchedData = function (addLotForm) {
                $scope.processingFlag.retrieveSearchedDataCompleted = false;
                $scope.selectOneParameter = false;
                $scope.searchedDataFromDbForUiGrid = [];
                $scope.gridOptionsForParcel = {};
                $scope.searchedData = [];
                $scope.listFilled = false;
                if (Object.getOwnPropertyNames($scope.searchCustom).length > 0 || $rootScope.createdParcelIds) {
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
                    if (mapHasValue || $rootScope.createdParcelIds) {
                        $scope.parcelDataBean.featureCustomMapValue = {};
                        $scope.map = {};
                        var finalMap = {};
                        var searchResult = DynamicFormService.convertSearchData($scope.searchInvoiceTemplate, $scope.searchParcelTemplate, null, null, $scope.searchCustom);
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
                        $scope.parcelDataBean.featureCustomMapValue = finalMap;
                        $rootScope.maskLoading();
                        $scope.parcelDataBean.featureDbFieldMap = featureDbFieldMap;
                        $scope.parcelDataBean.ruleConfigMap = {
                            fieldIdNameMap: fieldIdNameMap,
                            featureName: "addLot"
                        };

                        delete $scope.parcelDataBean["invoiceDataBean"];
                        $scope.parcelDataBean.searchOnParameter = true;
                        if ($rootScope.createdParcelIds) {
                            $scope.parcelDataBean.searchOnParameter = false;
                            $scope.parcelDataBean.id = angular.copy($rootScope.createdParcelIds);
                            $scope.parcelDataBean.id = $scope.parcelDataBean.id.substring(1, $scope.parcelDataBean.id.length - 1);
                            console.log(JSON.stringify($scope.parcelDataBean.id));
                            $rootScope.createdParcelIds = undefined;
                        }
                        ParcelService.search($scope.parcelDataBean, function (res) {
                            $scope.searchedDataFromDb = angular.copy(res);
                            var success = function (result)
                            {
                                $scope.searchedData = angular.copy(result);
                                angular.forEach($scope.searchedData, function (itr) {
                                    ///////////////////////////////////////////UI-GRID/////////////////////////////////////////////////////
                                    angular.forEach($scope.parcelLabelListForUiGrid, function (list) {
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
                                    itr.categoryCustom.screenRuleDetailsWithDbFieldName = itr.screenRuleDetailsWithDbFieldName;
                                    itr.categoryCustom.$$parcelId$$ = itr.value;
                                    $scope.searchedDataFromDbForUiGrid.push(itr.categoryCustom);
                                });
//                                        $scope.gridOptionsForParcel = {};
                                $scope.gridOptionsForParcel.enableFiltering = true;
                                $scope.gridOptionsForParcel.multiSelect = false;
                                $scope.gridOptionsForParcel.enableRowSelection = true;
                                $scope.gridOptionsForParcel.enableSelectAll = false;
                                $scope.selectedParcel = [];
                                $scope.gridOptionsForParcel.onRegisterApi = function (gridApi) {
                                    gridApiParcel = gridApi;
                                    gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                                        $scope.selectedParcel = gridApi.selection.getSelectedRows();
                                    });
                                    gridApi.selection.on.rowSelectionChangedBatch($scope, function (rows) {
                                        $scope.selectedParcel = gridApi.selection.getSelectedRows();
                                    });
                                };
                                $scope.gridOptionsForParcel.data = $scope.searchedDataFromDbForUiGrid;
                                $scope.gridOptionsForParcel.columnDefs = $scope.parcelLabelListForUiGrid;
                                $scope.processingFlag.retrieveSearchedDataCompleted = true;
                            };
                            DynamicFormService.convertorForCustomField(res, success,false);
                            window.setTimeout(function () {
                                $(window).resize();
                                $(window).resize();
                            }, 100);
                            $scope.listFilled = true;
                            $scope.lotDataBean.lotCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);
                            $rootScope.unMaskLoading();
                        }, function () {
                            var msg = "Could not retrieve, please try again.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                            $scope.processingFlag.retrieveSearchedDataCompleted = true;
                            $rootScope.unMaskLoading();
                        });
                    } else {
                        var msg = "Please select atleast one search criteria for search";
                        var type = $rootScope.warning;
                        $rootScope.addMessage(msg, type);
                        $scope.processingFlag.retrieveSearchedDataCompleted = true;
                    }
                } else {
                    var msg = "Please select atleast one search criteria for search";
                    var type = $rootScope.warning;
                    $rootScope.addMessage(msg, type);
                    $scope.processingFlag.retrieveSearchedDataCompleted = true;
                }
            };
            if ($rootScope.createdParcelIds !== undefined) {
                $timeout(function () {
                    $scope.retrieveSearchedData();
                }, 1000);
            }
//When user clicks on add lot link from datatable
            $scope.addLot = function (parcelObj) {
                $scope.preRuleSatisfied = false;
                $scope.flag.showAddPage = false;
                var parcelObj = angular.copy($scope.selectedParcel[0]);
                var dataToSend = {
                    featureName: 'addLot',
                    entityId: parcelObj.$$parcelId$$,
                    entityType: 'parcel',
                };
                RuleExecutionService.executePreRule(dataToSend, function (res) {
                    //Prevent the record from edit if pre rule satisfies.
                    if (!!res.validationMessage) {
                        $scope.preRuleSatisfied = true;
                        var type = $rootScope.warning;
                        $rootScope.addMessage(res.validationMessage, type);
                    } else {
                        $scope.mapToSent = {};
                        fieldIdNameMap = {};
                        this.lotAddFlag = false;
                        $scope.lotDataForUiGrid = [];
                        $scope.gridOptions = {};
                        $scope.gridOptions.enableFiltering = true;
                        $scope.caratOfParcel = undefined;
                        if (parcelObj != null) {
                            $scope.caratOfParcel = parseFloat(parcelObj.carat_of_parcel$NM$Double);
                            $scope.remainingCarat = parseFloat(parcelObj.carat_of_parcel$NM$Double);
                            if ($scope.remainingCarat < 0) {
                                $scope.remCaratMsg = "Carat value limit exceeded"
                            } else {
                                $scope.remCaratMsg = "Carat remaining is " + $scope.remainingCarat;
                            }
                            $scope.piecesOfParcel = parseFloat(parcelObj.no_of_pieces_of_parcel$NM$Long);
                            if ($scope.caratOfParcel !== undefined && $scope.caratOfParcel !== null && $scope.caratOfParcel.toString() !== 'NaN') {
                                $scope.caratDoesNotMatch = false;
                            } else {
                                $scope.caratDoesNotMatch = true;
                            }
                            if ($scope.searchedDataFromDb != null && $scope.searchedDataFromDb.length > 0) {
                                for (var itr = 0; itr < $scope.searchedDataFromDb.length; itr++) {
                                    if ($scope.searchedDataFromDb[itr].categoryCustom.parcelDbObjectId === parcelObj.$$parcelId$$) {
                                        parcelObj = angular.copy($scope.searchedDataFromDb[itr]);
                                        break;
                                    }
                                }
                            }
                            $scope.lotListToSave = [];
                            $scope.count = 0;
                            $scope.invoiceId = parcelObj.description;
                            $scope.invoiceIdForConstraint = parcelObj.description;
                            $scope.parcelId = parcelObj.value;
                            $scope.parcelIdForConstraint = parcelObj.value;
                            //                    $scope.invoiceIdForConstraint = parcelObj.custom3.invoiceID;
//                    $scope.parcelIdForConstraint = parcelObj.custom1.parcelID;
                            $scope.flag.showAddPage = true;
                            $rootScope.maskLoading();
                            $scope.parcelDataBean = {invoiceDataBean: {invoiceCustom: '', invoiceDbType: ''}};
                            var invoiceDbFieldName = [];
                            LotService.retrieveFranchiseDetails(function (franchiseDetails) {
                                $scope.franchiseList = angular.copy(franchiseDetails);

                            }, function () {
                                $rootScope.unMaskLoading();
                                var msg = "Could not retrieve franchise detail, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                            });
                            CustomFieldService.retrieveDesignationBasedFieldsBySection(["lotAdd", "GEN"], function (response) {
                                if (response.Lot == null || response.Lot.length == 0) {
                                    $scope.noFieldConfiguredForLot = true;
                                }
                                // Controller As 
                                $scope.AddLot = this;
                                this.lotAddFlag = true;
                                $scope.invoiceCustomData = DynamicFormService.resetSection($scope.generaInvoiceTemplate);
                                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                                $scope.invoiceDbType = {};
                                templateData.then(function (section) {
                                    $scope.generaInvoiceTemplate = section['genralSection'];
                                    var lotField = [];
                                    $scope.response = angular.copy(response);
                                    var result = Object.keys(response).map(function (key, value) {
                                        angular.forEach(this[key], function (itr) {
                                            if (key === 'Invoice#P#') {
                                                lotField.push({Lot: itr});
                                            }
                                        });
                                    }, response);
                                    $scope.generaInvoiceTemplate = DynamicFormService.retrieveCustomData($scope.generaInvoiceTemplate, lotField);
                                    angular.forEach($scope.generaInvoiceTemplate, function (itr) {
                                        if (itr.model) {
                                            invoiceDbFieldName.push(itr.model);
                                        }
                                    });
                                }, function (reason) {

                                }, function (update) {

                                });
                                var parcelDbFieldName = [];
                                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
                                $scope.parcelDbType = {};
                                templateData.then(function (section) {
                                    $scope.generaParcelTemplate = section['genralSection'];
                                    var parcelField = [];
                                    var result = Object.keys(response).map(function (key, value) {
                                        angular.forEach(this[key], function (itr) {
                                            if (key === 'Parcel#P#') {
                                                parcelField.push({Parcel: itr});
                                            }
                                        });
                                    }, response);
                                    $scope.generaParcelTemplate = DynamicFormService.retrieveCustomData($scope.generaParcelTemplate, parcelField);
                                    angular.forEach($scope.generaParcelTemplate, function (itr) {
                                        if (itr.model) {
                                            parcelDbFieldName.push(itr.model);
                                        }
                                    });
                                    var ids = [];
                                    ids.push(parcelObj.value);
                                    $scope.mapToSent['parcelObjectId'] = ids;
                                    $scope.mapToSent['parcelDbFieldName'] = parcelDbFieldName;
                                    $scope.mapToSent['invoiceDbFieldName'] = invoiceDbFieldName;
                                }, function (reason) {
                                }, function (update) {

                                });
                                $scope.categoryCustom = DynamicFormService.resetSection($scope.generaParcelTemplate);
                                var templateDataParcel = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                                if ($scope.lotDbType != null) {
                                } else {
                                    $scope.lotDbType = {};
                                }

                                templateDataParcel.then(function (section) {
                                    var lotDbFieldName = [];
                                    $scope.generalLotTemplate = section['genralSection'];
                                    var lotField = [];
                                    var result = Object.keys(response).map(function (key, value) {
                                        angular.forEach(this[key], function (itr) {
                                            if (key === 'Lot') {
                                                lotField.push({Parcel: itr});
                                            }
                                        });
                                    }, response);
                                    $scope.generalLotTemplate = DynamicFormService.retrieveCustomData($scope.generalLotTemplate, lotField);
                                    $scope.fieldNotConfigured = false;

//                                    if ($scope.generalLotTemplate !== undefined && $scope.generalLotTemplate !== null) {
//                                        for (var i = 0; i < $scope.generalLotTemplate.length; i++) {
//                                            if ($scope.generalLotTemplate[i].model === 'allot_to_lot$UMS$String') {
//                                                $scope.fieldNotConfigured = true;
//                                                break;
//                                            }
//                                        }
//                                    }
                                    for (var i in $scope.generalLotTemplate) {
                                        var itr = $scope.generalLotTemplate[i];
                                        if (itr.model == "issued_to_franchise$UMS$String") {
                                            $scope.generalLotTemplate.splice(i, 1);
                                            continue;
                                        }
                                        if (itr.model === 'carat_of_lot$NM$Double') {
                                            itr.required = true;
                                        } else if (itr.model === 'allot_to_lot$UMS$String') {
                                            itr.required = true;
                                        }
                                        else if (itr.model === 'in_stock_of_lot$UMS$String') {
                                            itr.required = true;
                                        }
                                        else if (itr.model === 'due_date_of_lot$DT$Date') {
                                            itr.required = true;
                                        }
                                        fieldIdNameMap[itr.fieldId] = itr.model;
                                        lotDbFieldName.push(itr.model);
                                    }
                                    ;

                                    $scope.mandatoryFields = ['carat_of_lot$NM$Double', 'due_date_of_lot$DT$Date', 'no_of_pieces_of_lot$NM$Long'];
                                    $scope.mapToSent['lotDbFieldName'] = lotDbFieldName;
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
                                    angular.forEach($scope.generalLotTemplate, function (itr) {
                                        $scope.modelAndHeaderListForLot.push({model: itr.model, header: itr.label});
                                        if (itr.fromModel) {
                                            $scope.lotListForUIGrid.push({name: itr.fromModel, displayName: itr.label, minWidth: 200,
                                                cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.addedLotScreenRule(row, \'' + itr.fromModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                            });
                                        } else if (itr.toModel) {
                                            $scope.lotListForUIGrid.push({name: itr.toModel, displayName: itr.label, minWidth: 200,
                                                cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.addedLotScreenRule(row, \'' + itr.toModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                            });
                                        } else if (itr.model) {
                                            $scope.lotListForUIGrid.push({name: itr.model, displayName: itr.label, minWidth: 200,
                                                cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.addedLotScreenRule(row, \'' + itr.model + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                            });
                                        }
                                    });
                                    $scope.lotDataBean.lotDbType = $scope.lotDbType;
                                    $scope.mapToSent.ruleConfigMap = {
                                        fieldIdNameMap: fieldIdNameMap,
                                        featureName: "addLot"
                                    };
                                    LotService.retrieveLotByParcelId($scope.mapToSent, function (response) {
//                                        console.log(response[0].categoryCustom);
//                                $scope.flag.lotsAlreadyCreated = false;
                                        if (response !== undefined) {
//                                    if (response.length > 1) {
//                                        $scope.flag.lotsAlreadyCreated = true;
//                                    }
                                            if (response[0] !== undefined) {
                                                if (response[0].custom3 !== null && response[0].custom3 !== undefined) {
                                                    $scope.invoiceCustom = angular.copy(response[0].custom3);
                                                } else {
                                                    $scope.invoiceCustom = {};
                                                }
                                                if (response[0].custom1 !== null && response[0].custom1 !== undefined) {
                                                    $scope.parcelCustom = angular.copy(response[0].custom1);
                                                } else {
                                                    $scope.parcelCustom = {};
                                                }
                                            }
//                                    console.log(JSON.stringify(response));
                                            $scope.lotInDb = angular.copy(response);
                                        } else {
//                                            $rootScope.unMaskLoading();
                                        }

                                        $scope.flag.customFieldGenerated = true;

                                        var success = function (result)
                                        {
//                                    var data = result;
//                                    $scope.modifiedLotListToDisplay = angular.copy(data);
//                                    angular.forEach($scope.modifiedLotListToDisplay, function(itr) {
//                                        itr.category = {};
//                                        itr.category.categoryCustom = itr.categoryCustom;
//                                    });
                                            angular.forEach(result, function (itr) {
                                                if (itr.categoryCustom !== undefined && itr.categoryCustom !== null) {
                                                    angular.forEach($scope.lotListForUIGrid, function (list) {
                                                        if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                                            itr.categoryCustom[list.name] = "NA";
                                                        }
                                                        else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                                            if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                                itr.categoryCustom[list.name] = "NA";
                                                            }
                                                        }
                                                        itr.objectIdOfParcel = {id: itr.value};
                                                        //                                            itr.categoryCustom["value"] = itr.value;
                                                    });
                                                    itr.categoryCustom.$$lotId$$ = itr.value;
                                                    itr.categoryCustom.screenRuleDetailsWithDbFieldName = itr.screenRuleDetailsWithDbFieldName;
                                                    $scope.lotDataForUiGrid.push(itr.categoryCustom);
                                                }
                                            });
                                            $scope.gridOptions.data = $scope.lotDataForUiGrid;
                                            $scope.gridOptions.columnDefs = $scope.lotListForUIGrid;
                                            if ($scope.editLotFeature) {
                                                $scope.gridOptions.columnDefs.push({name: 'Action',
                                                    cellTemplate: '<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.updateLot(row.entity)">Update</a></div>', enableFiltering: false, minWidth: 200});
                                            }
                                            $scope.countRemainingCarat();
                                            window.setTimeout(function () {
                                                $(window).resize();
                                                $(window).resize();
                                                $rootScope.unMaskLoading();
                                            }, 100);
                                            $scope.listFilled = true
                                        }
                                        DynamicFormService.convertorForCustomField(response, success,false);
//                                if ($scope.flag.lotsAlreadyCreated) {
//                                    var msg = "Lot(s) already created for current parcel";
//                                    var type = $rootScope.warning;
//                                    $rootScope.addMessage(msg, type);
//
//                                }
                                    }, function () {
                                        $rootScope.unMaskLoading();
                                        var msg = "Could not retrieve lot, please try again.";
                                        var type = $rootScope.error;
                                        $rootScope.addMessage(msg, type);
                                    });
                                }, function (reason) {

                                }, function (update) {

                                });
                            }, function () {
                                $rootScope.unMaskLoading();
                                var msg = "Failed to retrieve data";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                            });
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
                                $scope.categoryCustom['carat_of_lot$NM$Double'] = associatedSubLotCaratTotal;
                                $scope.categoryCustom['no_of_pieces_of_lot$NM$Long'] = associatedSubLotPiecesTotal;
                            };

                            var uiGridSubLotCustomFieldData = {};
                            CustomFieldService.retrieveDesignationBasedFieldsBySection(["lotAdd", "ASL"], function (response) {
                                if (response.SubLot == null || response.SubLot.length == 0) {
                                    $scope.noFieldConfiguredForSubLot = true;
                                }
//                    $scope.response = angular.copy(response);
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
                                    $scope.editFieldName = [];
                                    $scope.fieldIdNameMap = {};
                                    uiGridSubLotCustomFieldData.template = DynamicFormService.retrieveCustomData(uiGridSubLotCustomFieldData.template, sublotField);
                                    angular.forEach(uiGridSubLotCustomFieldData.template, function (itr) {
                                        if (itr.fromModel) {
                                            $scope.fieldIdNameMap[itr.fieldId] = itr.fromModel;
                                            $scope.gridOptionsSubLot.columnDefs.push({name: itr.fromModel, displayName: itr.label, minWidth: 200,
                                                cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.addSublotScreenRule(row, \'' + itr.fromModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                            });
                                            if ($scope.editFieldName.indexOf(itr.fromModel) === -1)
                                                $scope.editFieldName.push(itr.fromModel);
                                        } else if (itr.toModel) {
                                            $scope.fieldIdNameMap[itr.fieldId] = itr.toModel;
                                            $scope.gridOptionsSubLot.columnDefs.push({name: itr.toModel, displayName: itr.label, minWidth: 200,
                                                cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.addSublotScreenRule(row, \'' + itr.toModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                            });
                                            if ($scope.editFieldName.indexOf(itr.toModel) === -1)
                                                $scope.editFieldName.push(itr.toModel);
                                        } else if (itr.model) {
                                            $scope.fieldIdNameMap[itr.fieldId] = itr.model;
                                            $scope.gridOptionsSubLot.columnDefs.push({name: itr.model, displayName: itr.label, minWidth: 200,
                                                cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.addSublotScreenRule(row, \'' + itr.model + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
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
                                    if ($scope.editFieldName.length > 0) {
                                        var data = {
                                            parcelId: $scope.parcelId,
                                            map: $scope.map,
                                            ruleConfigMap: {
                                                fieldIdNameMap: $scope.fieldIdNameMap,
                                                featureName: "addLot"
                                            },
                                            excludeSubLotWithAssociatedLot: true

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
                                            }
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
                        }
                    }
                }, function (failure) {
                    $rootScope.unMaskLoading();
                    var msg = "Failed to retrieve pre rule.";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                });

            };

            $scope.reset = function (sectionTobeReset, isCreate) {
                if (sectionTobeReset === "parcelCustom") {
                    $scope.parcelCustom = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("lotAdd");
                    $scope.dbType = {};
                    templateData.then(function (section) {
                        $scope.generalSearchTemplate = section['genralSection'];
                        $scope.searchResetFlag = true;
                        $rootScope.unMaskLoading();
                        $scope.flag.customFieldGenerated = true;
                    }, function (reason) {
                    }, function (update) {
                    });
                } else if (sectionTobeReset === "categoryCustom") {
                    $scope.franchiseCode = null;
                    $scope.franchiseCodeYear = null;
                    if (!isCreate) {
                        $scope.categoryCustom = {};
                    } else {
                        angular.forEach($scope.fieldsToClear, function (item) {
                            delete $scope.categoryCustom[item];
                        });
                    }
                    var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
//                            $scope.lotDbType = {};
                    templateData.then(function (section) {
                        var lotField = [];
                        var result = Object.keys($scope.response).map(function (key, value) {
                            angular.forEach(this[key], function (itr) {
                                if (key === 'Lot') {
                                    lotField.push({Lot: itr});
                                }
                            });
                        }, $scope.response);
                        $scope.generalLotTemplate = section['genralSection'];
                        $scope.generalLotTemplate = DynamicFormService.retrieveCustomData($scope.generalLotTemplate, lotField);
                        for (var i in $scope.generalLotTemplate) {
                            var itr = $scope.generalLotTemplate[i];
                            if (itr.model == "issued_to_franchise$UMS$String") {
                                $scope.generalLotTemplate.splice(i, 1);
                                continue;
                            }
                            if (itr.model === 'carat_of_lot$NM$Double') {
                                itr.required = true;
                            } else if (itr.model === 'no_of_pieces_of_lot$NM$Long') {
                                itr.required = true;
                            }
                            else if (itr.model === 'due_date_of_lot$DT$Date') {
                                itr.required = true;
                            }
                            else if (itr.model === 'in_stock_of_lot$UMS$String') {
                                itr.required = true;
                            }
                        }
                        ;
                        $timeout(function () {
                            // Controller As
                            $scope.AddLot = this;
                            this.lotAddFlag = true;
                        }, 100);

                        $rootScope.unMaskLoading();
                        $scope.flag.customFieldGenerated = true;
                        $scope.lotDataBean.lotDbType = $scope.lotDbType;
                    }, function (reason) {
                    }, function (update) {
                    });
                }
            };
            $scope.resetAddForm = function (isCreate) {
                $scope.submitted = false;
                if ($scope.addLotForm != null) {
                    $scope.addLotForm.$dirty = false;
                }
//                $scope.categoryCustom = DynamicFormService.resetSection($scope.generalLotTemplate);
                $scope.flag.editLot = false;
                // Controller As
                $scope.AddLot = this;
                this.lotAddFlag = false;
                $scope.reset("categoryCustom", isCreate);
            };

            $scope.onCanelOfSearch = function (addLotForm) {
                if ($scope.addLotForm != null) {
                    $scope.addLotForm.$dirty = false;
                }
                $scope.searchedData = [];
                $scope.searchCustom = {};
                $scope.listFilled = false;
                $scope.searchResetFlag = false;
                $scope.selectedParcel = [];
                $scope.gridOptionsForParcel = {};
                $scope.reset("parcelCustom");
            };


            $scope.createLot = function (addParcelForm) {
                $scope.submitted = true;
                $scope.lotDbTypeToSent = angular.copy($scope.lotDbType);
                if (addParcelForm.$valid && $scope.checkAddLotFormExtraValidationIfSubLot() &&
                        $scope.checkAddLotFormExtraValidationWihoutSublot()) {
                    var promiseSequenceExists = $scope.sequenceNumberExists($scope.categoryCustom['lotID$AG$String']);
                    promiseSequenceExists.then(function () {
                        var dataToSend = {
                            featureName: 'addLot',
                            entityId: null,
                            entityType: 'lot',
                            currentFieldValueMap: $scope.categoryCustom,
                            dbType: $scope.lotDbType,
                            otherEntitysIdMap: {invoiceId: $scope.invoiceIdForConstraint, parcelId: $scope.parcelIdForConstraint}
                        };
                        $scope.submitted = false;
                        console.log('post rule dataToSend::::' + JSON.stringify(dataToSend));
                        RuleExecutionService.executePostRule(dataToSend, function (res) {
                            console.log('post rule res::::' + JSON.stringify(res));
                            if (!!res.validationMessage) {
                                var type = $rootScope.warning;
                                $rootScope.addMessage(res.validationMessage, type);
                            } else {
                                var sectionData = [];
                                sectionData.push({categoryCustom: angular.copy($scope.categoryCustom)});
                                var lotDataBean = {};
                                lotDataBean.lotCustom = $scope.categoryCustom;
                                lotDataBean.lotDbType = angular.copy($scope.lotDbType);
                                lotDataBean.lotDbType['lotID$AG$String'] = "String";
                                lotDataBean.hasPacket = false;
                                lotDataBean.subLots = associatedSubLotIds;
                                lotDataBean.invoiceDataBean = {id: $scope.invoiceId};
                                lotDataBean.parcelDataBean = {id: $scope.parcelId};
                                lotDataBean.sequenceNumber = $scope.categoryCustom['lotID$AG$String'];
                                lotDataBean.ruleConfigMap = $scope.mapToSent.ruleConfigMap;
                                lotDataBean["issued_to_franchise$UMS$String"] = $scope.franchiseId;
                                for (var i = 0; i < $scope.franchiseList.length; i++) {
                                    if ($scope.franchiseList[i].description === $scope.franchiseCode) {
                                        lotDataBean.franchise = $scope.franchiseList[i].value;
                                        break;
                                    }
                                }
                                $scope.categoryCustom['lotID$AG$String'] = $scope.currentYear.concat($scope.franchiseCode).concat($scope.categoryCustom['lotID$AG$String']);
//                    console.log(JSON.stringify(lotDataBean));
                                LotService.create(lotDataBean, function (response) {
//                        console.log(JSON.stringify(response));
                                    if (response !== null) {
                                        $scope.listFilled = false;
                                        $scope.AddLot = this;
                                        this.lotAddFlag = false;
                                        $scope.addLotForm.$dirty = false;
                                        var success = function (result) {
                                            $scope.selectedParcel[0].stockCarat = Number($scope.selectedParcel[0].stockCarat) - Number(lotDataBean.lotCustom['carat_of_lot$NM$Double']);
                                            $scope.selectedParcel[0].stockPieces = Number($scope.selectedParcel[0].stockPieces) - Number(lotDataBean.lotCustom['no_of_pieces_of_lot$NM$Long']);
                                            for (var i in response) {
                                                if (i === 'lotId') {
                                                    result[0].categoryCustom["$$lotId$$"] = i;
                                                    break;
                                                }
                                            }
                                            angular.forEach($scope.lotListForUIGrid, function (list) {
                                                if (!result[0].categoryCustom.hasOwnProperty(list.name)) {
                                                    result[0].categoryCustom[list.name] = "NA";
                                                }
                                                else if (result[0].categoryCustom.hasOwnProperty(list.name)) {
                                                    if (result[0].categoryCustom[list.name] === null || result[0].categoryCustom[list.name] === '' || result[0].categoryCustom[list.name] === undefined || result[0].categoryCustom[list.name] === 'undefined') {
                                                        result[0].categoryCustom[list.name] = "NA";
                                                    }
                                                }
                                            });
                                            //Update with current lot id.
                                            result[0].categoryCustom['lotID$AG$String'] = response[response.lotId];
                                            //Rules related code
                                            //Preserving lot id in bot ui-grid and actual data.
                                            result[0].categoryCustom.$$lotId$$ = response.lotId;
                                            result[0].categoryCustom.screenRuleDetailsWithDbFieldName = response.screenRuleDetailsWithDbFieldName;
//                                result[0].value = response.lotId;
                                            //Screen rule details
//                                result[0].screenRuleDetailsWithDbFieldName = response.screenRuleDetailsWithDbFieldName;
                                            //Added in main collections.
//                                $scope.lotInDb.push(result[0]);
                                            $scope.gridOptions.data.push(angular.copy(result[0].categoryCustom));
                                            if ($scope.editLotFeature) {
                                                $scope.gridOptions.columnDefs.push({name: 'Action',
                                                    cellTemplate: '<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.updateLot(row.entity)">Update</i></a></div>', enableFiltering: false, minWidth: 200});
                                            }
                                            $scope.countRemainingCarat();
                                            $scope.listFilled = true;
                                        }
                                        DynamicFormService.convertorForCustomField(sectionData, success);
                                        $scope.resetAddForm(false);
                                        if (gridApiSubLot != null) {
                                            var selectedRows = gridApiSubLot.selection.getSelectedRows();
                                            for (var selectedRow in selectedRows) {
                                                $scope.gridOptionsSubLot.data.splice($scope.gridOptionsSubLot.data.indexOf(selectedRows[selectedRow]), 1);
                                            }
                                        }
                                    } else {
                                        $rootScope.unMaskLoading();
                                        var msg = "Could not create lot, please try again.";
                                        var type = $rootScope.error;
                                        $rootScope.addMessage(msg, type);
                                    }
                                }, function () {
                                    $rootScope.unMaskLoading();
                                    var msg = "Could not create lot, please try again.";
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

                    });
                }
            };
            $scope.checkAddLotFormExtraValidationIfSubLot = function () {
                if (associatedSubLotIds.length > 0) {
                    if (associatedSubLotCaratTotal != Number($scope.categoryCustom['carat_of_lot$NM$Double']) ||
                            associatedSubLotPiecesTotal != Number($scope.categoryCustom['no_of_pieces_of_lot$NM$Long'])) {
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
                    if (Number($scope.selectedParcel[0].stockCarat) < Number($scope.categoryCustom['carat_of_lot$NM$Double']) ||
                            Number($scope.selectedParcel[0].stockPieces) < Number($scope.categoryCustom['no_of_pieces_of_lot$NM$Long'])) {
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
            $scope.editLotLocally = function (lotObj) {
                $scope.index = lotObj.$$objectId;
                $scope.oldObj = angular.copy(lotObj, $scope.oldObj);
                if (lotObj != null) {
                    $scope.flag.editLot = true;
                    $scope.lotDataBean = {};
                    $scope.AddLot = this;
                    this.lotAddFlag = false;
                    if (!!($scope.lotListToSave && $scope.lotListToSave.length > 0)) {
                        for (var i = 0; i < $scope.lotListToSave.length; i++) {
                            if (lotObj.$$objectId != null && $scope.lotListToSave[i].$$objectId === lotObj.$$objectId) {
                                $scope.categoryCustom = angular.copy($scope.lotListToSave[i].categoryCustom);
                                $scope.lotDataBean.lotCustom = $scope.categoryCustom;
                                break;
                            }
                        }
                    }
                    $scope.lotDataBean.lotDbType = $scope.lotDbType;
                }

                $timeout(function () {
                    // Controller As
                    $scope.AddLot = this;
                    this.lotAddFlag = true;
                }, 100);
            };
            $scope.saveLot = function (addLotForm) {
                $scope.submitted = true;
//                        if (Object.getOwnPropertyNames($scope.categoryCustom).length > 0) {
//                            var mapHasValue = false;
//                            for (var prop in $scope.categoryCustom) {
//                                if (!!($scope.categoryCustom[prop])) {
//                                    mapHasValue = true;
//                                    break;
//                                }
//                            }
//                    if (!!(mapHasValue && addLotForm.$valid && $scope.lotListToSave && $scope.modifiedLotListToDisplay && $scope.oldObj && $scope.lotListToSave.length > 0 && $scope.modifiedLotListToDisplay.length > 0)) {
//                        $scope.submitted = false;
//                        var id = $scope.oldObj.id;
//                        angular.forEach($scope.modifiedLotListToDisplay, function(itr) {
//                            if (itr.id === id) {
//                                var j = $scope.modifiedLotListToDisplay.indexOf(itr);
//                                if (j !== -1) {
//                                    $scope.modifiedLotListToDisplay.splice(j, 1);
//                                }
//                            }
//                        });
//                        angular.forEach($scope.lotListToSave, function(itr) {
//                            if (itr.id === id) {
//                                var i = $scope.lotListToSave.indexOf(itr);
//                                if (i !== -1) {
//                                    $scope.lotListToSave.splice(i, 1);
//                                }
//                            }
//                        });
//                        var sectionData = [];
//                        sectionData.push({categoryCustom: angular.copy($scope.categoryCustom)});
//                        $scope.count++;
//                        $scope.lotListToSave.push(angular.copy({id: $scope.count, categoryCustom: $scope.categoryCustom}));
////                        var data = DynamicFormService.getValuesOfComponentFromId(sectionData, $scope.generalLotTemplate);
////                        $scope.lotListTodisplay.push(angular.copy({id: $scope.count, category: data[0]}));
//                        var success = function(result)
//                        {
//                            $scope.modifiedLotListToDisplay.push({id: $scope.count, category: result[0]});
////                            $scope.modifiedLotListToDisplay.push(angular.copy({id: $scope.count, category: result[0]}));
//                            console.log("In Success" + JSON.stringify(result));
//                        }
//                        DynamicFormService.getValuesOfComponentFromIdTemp(sectionData, $scope.generalLotTemplate, success);
//                        $scope.categoryCustom = DynamicFormService.resetSection($scope.generalLotTemplate);
//                        $scope.listFilled = true;
//                    }
//                    $scope.categoryCustom = DynamicFormService.resetSection($scope.generalLotTemplate);
//                    $scope.flag.editLot = false;
                if (addLotForm.$valid) {
                    var sectionData = [];
                    sectionData.push({categoryCustom: angular.copy($scope.categoryCustom)});
//                    $scope.count++;
//                        $scope.lotListToSave.push(angular.copy({id: $scope.count, categoryCustom: $scope.categoryCustom}));
                    angular.forEach($scope.lotListToSave, function (itr) {
                        if (itr.$$objectId === $scope.index) {
                            itr.categoryCustom = ($scope.categoryCustom);
                        }
                    });
//                    $scope.lotListToSave[$scope.index] = {parcelID: $scope.index, categoryCustom: $scope.categoryCustom};
//                        var data = DynamicFormService.getValuesOfComponentFromId(sectionData, $scope.generalParcelTemplate);
//                       
//                        $scope.lotListTodisplay.push(angular.copy({id: $scope.count, category: data[0]}));
                    var success = function (result)
                    {
                        var tempLot = angular.copy(result[0].categoryCustom);
                        if (tempLot !== undefined && tempLot !== null) {
                            angular.forEach($scope.lotListForUIGrid, function (list) {
                                if (!tempLot.hasOwnProperty(list.name)) {
                                    tempLot[list.name] = "NA";
                                }
                                else if (tempLot.hasOwnProperty(list.name)) {
                                    console.log(tempLot[list.name]);
                                    if (tempLot[list.name] === null || tempLot[list.name] === '' || tempLot[list.name] === undefined || tempLot[list.name] === 'undefined') {
                                        tempLot[list.name] = "NA";
                                    }
                                }
                            });
//                                        $scope.lotDataForUiGrid.push(tempLot);
                        }
                        for (var i = 0; i < $scope.gridOptions.data.length; i++) {
                            var item = $scope.gridOptions.data[i];
                            if (item.$$objectId === $scope.index) {
                                $scope.gridOptions.data[i] = angular.copy(tempLot);
                                $scope.gridOptions.data[i].$$objectId = $scope.index;
                                break;
                            }
                        }
//                            console.log("$scope.gridOptions :"+JSON.stringify($scope.gridOptions));
                        $scope.countRemainingCarat();
                    };
                    DynamicFormService.convertorForCustomField(sectionData, success,false);
                    $scope.categoryCustom = DynamicFormService.resetSection($scope.generalLotTemplate);
                    $scope.listFilled = true;
//                    }
                    $scope.flag.editLot = false;
                    $scope.resetAddForm();
                }
//                        }
            };
            $scope.showPopUp = function (lotObj) {
                $scope.lotObjectToDelete = lotObj;
                $scope.index = lotObj.$$objectId;
                $("#deleteDialog").modal('show');
            };
            $scope.hidePopUp = function () {
                $("#deleteDialog").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();

            };
            $scope.deleteLot = function () {
                for (var i = 0; i < $scope.gridOptions.data.length; i++) {
                    var item = $scope.gridOptions.data[i];
                    if (item.$$objectId === $scope.index) {
                        $scope.gridOptions.data.splice(i, 1);
                        break;
                    }
                }
                for (var i = 0; i < $scope.lotListToSave.length; i++) {
                    var item = $scope.lotListToSave[i];
                    if (item.$$objectId === $scope.index) {
                        $scope.lotListToSave.splice(i, 1);
                        break;
                    }
                }
                $scope.categoryCustom = DynamicFormService.resetSection($scope.generalLotTemplate);
                $scope.flag.editLot = false;
                $("#deleteDialog").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.onCancel = function (addLotForm) {
                if (addLotForm != null) {
//                            $scope.categoryCustom = DynamicFormService.resetSection($scope.generalLotTemplate);
                    $scope.flag.showAddPage = false;
                    $scope.lotListToSave = [];
                    $scope.modifiedLotListToDisplay = [];
                    $scope.submitted = false;
                    $scope.selectedParcel = [];
                    $scope.flag.lotsAlreadyCreated = false;
                    $scope.reset("categoryCustom");
                }
            };
            $scope.saveAllLot = function (addLotForm) {
                $scope.caratDoesNotMatch = false;
                var lotList = [];
                var lotDataBean = {};
                var totalCaratFromUI = undefined;
                angular.forEach($scope.lotListToSave, function (itr) {
                    if (!$scope.caratDoesNotMatch && itr.categoryCustom['carat_of_lot$NM$Double'] !== undefined && itr.categoryCustom['carat_of_lot$NM$Double'] !== null) {
                        if (totalCaratFromUI !== undefined) {
                            totalCaratFromUI = totalCaratFromUI + parseFloat(itr.categoryCustom['carat_of_lot$NM$Double']);
                        } else {
                            totalCaratFromUI = parseFloat(itr.categoryCustom['carat_of_lot$NM$Double']);
                        }
                    } else {
                        $scope.caratDoesNotMatch = true;
                    }
                    lotList.push({lotCustom: itr.categoryCustom, invoiceDataBean: {id: $scope.invoiceId}, lotDbType: $scope.lotDbTypeToSent, parcelDataBean: {id: $scope.parcelId}});
                });
                if (!$scope.caratDoesNotMatch) {
                    if ($scope.caratOfParcel !== totalCaratFromUI) {
                        $scope.caratDoesNotMatch = true;
                    }
                }
                lotDataBean.lotList = lotList;
                lotDataBean.invoiceDataBean = {id: $scope.invoiceId};
                lotDataBean.parcelDataBean = {id: $scope.parcelId};
                if (!$scope.caratDoesNotMatch) {
                    $rootScope.maskLoading();
                    LotService.create(lotDataBean, function (response) {
                        $scope.lotListToSave = [];
                        $scope.lotListTodisplay = [];
                        $scope.modifiedLotListToDisplay = [];
                        $scope.onCancel();
                        $scope.onCanelOfSearch();
                        $scope.searchedData = [];
                        $scope.searchedDataFromDb = [];
                        $scope.listFilled = false;
                        $scope.addLotForm.$dirty = false;
                        $scope.flag.showAddPage = false;
                        $rootScope.unMaskLoading();
                    }, function () {
                        $rootScope.unMaskLoading();
                        var msg = "Could not create lot, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                } else {
                    var msg = "Carat value does not match, please try again.";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                }
            };

            $scope.updateLot = function (searchDataObj) {
                console.log(JSON.stringify(searchDataObj));
                searchDataObj.value = angular.copy(searchDataObj['$$lotId$$']);
                if (!!(searchDataObj)) {
                    $rootScope.unMaskLoading();
                    $scope.flag.showUpdatePage = true;
                    $scope.addLotForm.$dirty = false;
                    $location.path('/editlot');
                    $rootScope.editLot = true;
                    $rootScope.lotByParcel = angular.copy(searchDataObj);
                }

            };

            $scope.countRemainingCarat = function () {
                if ($scope.gridOptions.data !== undefined && $scope.gridOptions.data !== null) {
                    var totalCreated = 0;
                    angular.forEach($scope.gridOptions.data, function (item) {
                        if (item["carat_of_lot$NM$Double"] !== undefined && item["carat_of_lot$NM$Double"] !== null) {
                            totalCreated += parseFloat(item["carat_of_lot$NM$Double"]);
                        }
                    });
                    $scope.remainingCarat = parseFloat($scope.caratOfParcel) - parseFloat(totalCreated);
                    if ($scope.remainingCarat < 0) {
                        $scope.remCaratMsg = "Carat value limit exceeded"
                    } else {
                        $scope.remCaratMsg = "Carat remaining is " + $scope.remainingCarat;
                    }
                    var totalPiecesCreated = 0;
                    angular.forEach($scope.gridOptions.data, function (item) {
                        if (item["no_of_pieces_of_lot$NM$Long"] !== undefined && item["no_of_pieces_of_lot$NM$Long"] !== null) {
                            totalPiecesCreated += parseFloat(item["no_of_pieces_of_lot$NM$Long"]);
                        }
                    });
//                    $scope.remainingPieces = parseFloat($scope.piecesOfParcel) - parseFloat(totalPiecesCreated);
                }
            };
            $scope.sequenceNumberExists = function (sequenceNumber, addLotForm) {
//                $scope.sequenceNumber = sequenceNumber;
//                addLotForm.sequenceNumber.$setValidity('exists', true);
                var deferred = $q.defer();
                if (sequenceNumber !== undefined && sequenceNumber !== null) {
                    for (var i = 0; i < $scope.franchiseList.length; i++) {
                        if ($scope.franchiseList[i].description === $scope.franchiseCode) {
                            $scope.franchiseId = $scope.franchiseList[i].value;
                            var franchiseSeqNumberMap = {franchise: $scope.franchiseId, seqNumber: sequenceNumber};
                            LotService.isLotSequenceNumberExist(franchiseSeqNumberMap, function (res) {
                                if (res.data == true) {
                                    var msg = "Lot already exists for the same id.";
                                    var type = $rootScope.error;
                                    $rootScope.addMessage(msg, type);
                                    deferred.reject();
                                }
                                else {
                                    deferred.resolve();
                                }
                            }, function () {
                                $rootScope.unMaskLoading();
                                var msg = "Could not retrieve sequence number, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                                deferred.reject();
                            });
                            break;
                        }
                    }
                }
                return deferred.promise;
            };
            $scope.startItemChanged = function (franchiseCode, addLotForm) {
                $scope.franchiseCode = franchiseCode;
                $scope.franchiseCodeYear = $scope.currentYear + franchiseCode;
//                addLotForm.sequenceNumber.$setValidity('exists', true);
                if (franchiseCode !== undefined && franchiseCode !== null) {
                    for (var i = 0; i < $scope.franchiseList.length; i++) {
                        if ($scope.franchiseList[i].description === $scope.franchiseCode) {
                            $scope.franchiseId = $scope.franchiseList[i].value;
                            LotService.getnextlotsequence($scope.franchiseId, function (res) {
                                $scope.categoryCustom['lotID$AG$String'] = res.sequenceNumber;
                            }, function () {
                                $rootScope.unMaskLoading();
                                var msg = "Could not retrieve sequence number, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                            });
                            break;
                        }
                    }

                }
                else {
                    $scope.categoryCustom['lotID$AG$String'] = null;
                }
            };
            $scope.hasEditLotPermission = function (flag) {
                $scope.editLotFeature = flag;
            };
        }]);
});