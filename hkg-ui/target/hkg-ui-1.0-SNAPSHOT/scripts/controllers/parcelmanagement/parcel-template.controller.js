/**
 * 
 * @param {type} hkg
 * @param {type} parcelService
 * @param {type} customFieldService
 * @description This controller will be used for parcel template, parceltemplate is responsible to allow operation like 
 * parcel selection and carat,pieces values edit at time of parcel merge and sale
 * @returns {undefined}
 */

define(['hkg', 'ngload!uiGrid', 'parcelService', 'ParcelTemplateService'], function (hkg) {
    hkg.register.controller('ParcelTemplateController', ["$scope", "$rootScope", "ParcelService", "DynamicFormService", "CustomFieldService", 'ParcelTemplateService', '$timeout',
        function ($scope, $rootScope, ParcelService, DynamicFormService, CustomFieldService, ParcelTemplateService, $timeout) {
            $scope.editableParcelRows = [];
            $scope.allParcelIds = [];
            $scope.editableFieldNames = ["Stock Carat", "Stock Pieces", "exchangeRate", "origAmountInDollar$FRM$Double", "amountInRs", "origRateInDollar$NM$Double", "parcelId$AG$String"];
            $scope.entityParcelTemplate = ParcelTemplateService.getEntityName();
            $scope.tableFooterData = {};
            $scope.searchedDataFromDbForUiGrid = [];
            $scope.gridOptions = {
                enableRowSelection: true,
                enableSelectAll: false,
                enableFiltering: true,
                multiSelect: true,
                data: []
            };
            $scope.flag = {};
            $scope.selectedInvoice = [];
            $scope.gridOptions.onRegisterApi = function (gridApi) {
                //set gridApi on scope
                $scope.gridApi = gridApi;
                gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                    if (row.isSelected) {
                        rowSelected(row.entity);
                    }
                    else {
                        rowDeSelected(row.entity);
                    }
                    $scope.allParcelIds = [];
                    angular.forEach(gridApi.selection.getSelectedRows(), function(item) {
                        $scope.allParcelIds.push(item.$$parcelId$$);
                    });
                    $scope.refreshFooterData();
                });
            };

            var prepareEditableObject = function (rowEntity) {
                var editableRowObj = {};
                var field;
                for (var fieldKey in $scope.editableFieldNames) {
                    field = $scope.editableFieldNames[fieldKey];
                    editableRowObj[field] = rowEntity[field];
                }
                editableRowObj.value = rowEntity["$$$value"];
                if (!!ParcelTemplateService.getSelectedParcels() && ParcelTemplateService.getSelectedParcels()[editableRowObj.value] != null) {
                    var selectedParcelObj = ParcelTemplateService.getSelectedParcels()[editableRowObj.value];
                    editableRowObj["Stock Carat"] = selectedParcelObj["carat"];
                    editableRowObj["Stock Pieces"] = selectedParcelObj["pieces"];
                    editableRowObj["exchangeRate"] = selectedParcelObj["exRate"];
                    editableRowObj.oldPieces = rowEntity["Stock Pieces"] + editableRowObj["Stock Pieces"];
                    editableRowObj.oldCarat = rowEntity["Stock Carat"] + editableRowObj["Stock Carat"];
                }
                else {
                    editableRowObj.oldPieces = rowEntity["Stock Pieces"];
                    editableRowObj.oldCarat = rowEntity["Stock Carat"];
                }
                return editableRowObj;
            };
            var rowSelected = function (rowEntity) {
                var editableRowObj = prepareEditableObject(rowEntity);

                $scope.calculateAmounts(editableRowObj);
                $scope.editableParcelRows.push(editableRowObj);
            };
            var rowDeSelected = function (rowEntity) {
                var editableRowObj = prepareEditableObject(rowEntity);
                $scope.editableParcelRows.splice($scope.editableParcelRows.indexOf(editableRowObj));
            };
            $scope.calculateAmounts = function (editableRowObj, form) {
                if (form != null) {
                    if (!form.carat.$error.min &&
                            !form.carat.$error.max &&
                            !form.carat.$error.required &&
                            !form.pieces.$error.min &&
                            !form.pieces.$error.max &&
                            !form.pieces.$error.required) {
                        if (validateCaratPieces(editableRowObj, form)) {
                            calculateAmountAfterValidation(editableRowObj);
                        }
                    }
                }
                else {
                    calculateAmountAfterValidation(editableRowObj);
                }
            };
            var validateCaratPieces = function (editableRowObj, form) {
                var piecesDiff = editableRowObj.oldPieces - editableRowObj["Stock Pieces"];
                var caratDiff = editableRowObj.oldCarat - editableRowObj["Stock Carat"];
                if ((piecesDiff == 0 && caratDiff != 0) || (piecesDiff != 0 && caratDiff == 0)) {
                    form.carat.$setValidity('caratpieces', false);
                    form.pieces.$setValidity('caratpieces', false);
                    return false;
                }
                else {
                    form.carat.$setValidity('caratpieces', true);
                    form.pieces.$setValidity('caratpieces', true);
                    return true;
                }
            };
            var calculateAmountAfterValidation = function (editableRowObj) {
                if (!!editableRowObj["Stock Carat"] && !!editableRowObj["origRateInDollar$NM$Double"]) {
                    editableRowObj["origAmountInDollar$FRM$Double"] = editableRowObj["Stock Carat"] * editableRowObj["origRateInDollar$NM$Double"];
                }
                if (!!editableRowObj["exchangeRate"] && !!editableRowObj["origAmountInDollar$FRM$Double"]) {
                    editableRowObj["amountInRs"] = editableRowObj["exchangeRate"] * editableRowObj["origAmountInDollar$FRM$Double"];
                }
                $scope.refreshFooterData();
            }
            $scope.$watch('editableParcelRows', function () {
                convertToRoughStockDatabean();
            }, true);

            var convertToRoughStockDatabean = function () {
                var roughstockDbObj = {};
                var selectedParcelData = {roughStock: [], footerData: {}};
                for (var i = 0; i < $scope.editableParcelRows.length; i++) {
                    roughstockDbObj = {};
                    roughstockDbObj.changedPieces = $scope.editableParcelRows[i]["Stock Pieces"];
                    roughstockDbObj.changedCarat = $scope.editableParcelRows[i]["Stock Carat"];
                    roughstockDbObj.changedRate = $scope.editableParcelRows[i]["origRateInDollar$NM$Double"];
                    roughstockDbObj.changedExchangeRate = $scope.editableParcelRows[i]["exchangeRate"];
                    roughstockDbObj.changedAmountInDollar = $scope.editableParcelRows[i]["origAmountInDollar$FRM$Double"];
                    roughstockDbObj.changedAmountInRs = $scope.editableParcelRows[i]["amountInRs"];
                    roughstockDbObj.parcel = $scope.editableParcelRows[i]["value"];
                    selectedParcelData.roughStock.push(roughstockDbObj);
                }
                selectedParcelData.footerData.changedPieces = $scope.tableFooterData["Stock Pieces"];
                selectedParcelData.footerData.changedCarat = $scope.tableFooterData["Stock Carat"];
                selectedParcelData.footerData.changedRate = $scope.tableFooterData["origRateInDollar$NM$Double"];
                selectedParcelData.footerData.changedExchangeRate = $scope.tableFooterData["exchangeRate"];
                selectedParcelData.footerData.changedAmountInDollar = $scope.tableFooterData["origAmountInDollar$FRM$Double"];
                selectedParcelData.footerData.changedAmountInRs = $scope.tableFooterData["amountInRs"];
                ParcelTemplateService.setSelectedParcelData(selectedParcelData);
            };
            $scope.sellParcelScreenRule = function (row, colField) {
                var color;

                if (!!row.entity.screenRuleDetailsWithDbFieldName && row.entity.screenRuleDetailsWithDbFieldName[colField] !== undefined && row.entity.screenRuleDetailsWithDbFieldName[colField] !== null) {
                    if ($scope.allParcelIds.length === 0 || ($scope.allParcelIds.indexOf(row.entity.$$parcelId$$) === -1)) {
                        color = row.entity.screenRuleDetailsWithDbFieldName[colField].colorCode;
                    }
                }
                return color;
            };
            $scope.parcelLabelListForUiGrid = [];
            $scope.retrieveParcels = function () {
                $rootScope.maskLoading();
                $scope.parameters = [ParcelTemplateService.getFeatureName(), "GEN"];
                CustomFieldService.retrieveDesignationBasedFieldsBySection($scope.parameters, function (response) {
                    $scope.response = angular.copy(response);
                    var templateDataForUpdate = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
                    if (!($scope.dbTypeForUpdate !== null))
                        $scope.dbTypeForUpdate = {};
                    templateDataForUpdate.then(function (section) {
                        $scope.parcelCommonTemplate = null;
                        $scope.parcelCommonTemplate = section['genralSection'];
                        $scope.listOfModelsOfDateType = [];
                        var parcelField = [];
                        var result = Object.keys(response).map(function (key, value) {
                            angular.forEach(this[key], function (itr) {
                                if (key === 'Parcel') {
                                    parcelField.push({Parcel: itr});
                                }
                            });
                        }, response);
                        $scope.editFieldName = [];
                        $scope.fieldIdNameMap = {};
                        $scope.parcelCommonTemplate = DynamicFormService.retrieveCustomData($scope.parcelCommonTemplate, parcelField);
                        angular.forEach($scope.parcelCommonTemplate, function (itr) {
                            if (itr.fromModel) {
                                $scope.fieldIdNameMap[itr.fieldId] = itr.fromModel;
                                if ($scope.editFieldName.indexOf(itr.fromModel) === -1)
                                    $scope.editFieldName.push(itr.fromModel);
                            } else if (itr.toModel) {
                                $scope.fieldIdNameMap[itr.fieldId] = itr.toModel;
                                if ($scope.editFieldName.indexOf(itr.toModel) === -1)
                                    $scope.editFieldName.push(itr.toModel);
                            } else if (itr.model) {
                                $scope.fieldIdNameMap[itr.fieldId] = itr.model;
                                if ($scope.editFieldName.indexOf(itr.model) === -1)
                                    $scope.editFieldName.push(itr.model);
                            }
                        });
                        $scope.fieldNotConfigured = false;
                        $scope.showConfigMsg = false;
                        var fieldsInTemplate = [];

                        $scope.mandatoryFields = ["origAmountInDollar$FRM$Double", "origRateInDollar$NM$Double", "parcelId$AG$String"];
                        if ($scope.parcelCommonTemplate !== undefined && $scope.parcelCommonTemplate !== null && $scope.parcelCommonTemplate.length > 0) {
                            for (var i = 0; i < $scope.parcelCommonTemplate.length; i++) {
                                fieldsInTemplate.push(angular.copy($scope.parcelCommonTemplate[i].model));
                            }
                        }
                        if (fieldsInTemplate.length > 0 && $scope.mandatoryFields != null && $scope.mandatoryFields.length > 0) {
                            for (var field = 0; field < $scope.mandatoryFields.length; field++) {
                                if (fieldsInTemplate.indexOf($scope.mandatoryFields[field]) === -1) {
                                    $scope.fieldNotConfigured = true;
                                    break;
                                }
                            }
                        } else {
                            $scope.fieldNotConfigured = true;
                        }
                        if ($scope.fieldNotConfigured) {
                            $scope.showConfigMsg = true;
                        }
                        angular.forEach($scope.parcelCommonTemplate, function (itr) {
                            if (itr.fromModel) {
                                $scope.parcelLabelListForUiGrid.push({name: itr.fromModel, displayName: itr.label, minWidth: 200,
                                    cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.sellParcelScreenRule(row, \'' + itr.fromModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                });
                            } else if (itr.toModel) {
                                $scope.parcelLabelListForUiGrid.push({name: itr.toModel, displayName: itr.label, minWidth: 200,
                                    cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.sellParcelScreenRule(row, \'' + itr.toModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                });
                            } else if (itr.model) {
                                $scope.parcelLabelListForUiGrid.push({name: itr.model, displayName: itr.label, minWidth: 200,
                                    cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.sellParcelScreenRule(row, \'' + itr.model + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                });
                            }
                        });
                        $scope.map = {};
                        $scope.map['dbFieldNames'] = $scope.editFieldName;
                        var ids = [];
                        var parcelMap = ParcelTemplateService.getSelectedParcels();
                        angular.forEach(parcelMap, function (value, key) {
                            ids.push(key);
                        })
                        $scope.map['parcelIds'] = ids;
                        $scope.parcelDataBean = {};
                        $scope.parcelDataBean.featureDbFieldMap = $scope.map;
                        $scope.parcelDataBean.ruleConfigMap = {
                            fieldIdNameMap: $scope.fieldIdNameMap,
                            featureName: "roughSale"
                        };
                        if ($scope.editFieldName.length > 0) {
                            ParcelService.retrieveAllParcels($scope.parcelDataBean, function (res) {
                                if (!(res !== undefined && res !== null && res.length > 0)) {
                                    $scope.searchedDataFromDbForUiGrid = undefined;
                                    $rootScope.unMaskLoading();
                                }
                                var success = function (result) {
                                    $scope.parcelData = angular.copy(result);
                                    $scope.parcelLabelListForUiGrid.push({name: "Stock Carat", displayName: "Stock Carat", minWidth: 200}, {name: "Stock Pieces", displayName: "Stock Pieces", minWidth: 200});
                                    $scope.searchedDataFromDbForUiGrid = [];
                                    angular.forEach($scope.parcelData, function (itr) {
                                        angular.forEach($scope.parcelLabelListForUiGrid, function (list) {
                                            if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                                itr.categoryCustom[list.name] = "NA";
                                            }
                                            else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                                if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                    itr.categoryCustom[list.name] = "NA";
                                                }
                                            }
                                            itr.categoryCustom["$$$value"] = itr.value;
                                        });
                                        itr.categoryCustom.$$parcelId$$ = itr.value;
                                        itr.categoryCustom.screenRuleDetailsWithDbFieldName = itr.screenRuleDetailsWithDbFieldName;
                                        $scope.searchedDataFromDbForUiGrid.push(itr.categoryCustom);
                                    });
                                    if ($scope.searchedDataFromDbForUiGrid.length === 0) {
                                        $scope.searchedDataFromDbForUiGrid = undefined;
                                    }
                                    $scope.gridOptions.data = $scope.searchedDataFromDbForUiGrid;
                                    $scope.gridOptions.columnDefs = $scope.parcelLabelListForUiGrid;

//TODO : remove this static code
                                    if (!!ParcelTemplateService.getSelectedParcels() && Object.keys(ParcelTemplateService.getSelectedParcels()).length > 0) {
                                        $timeout(function () {
                                            for (var parcelIdKey in ParcelTemplateService.getSelectedParcels()) {
                                                for (var j = 0; j < $scope.gridOptions.data.length; j++) {
                                                    if ($scope.gridOptions.data[j]["$$$value"] == parcelIdKey) {
                                                        $scope.gridApi.selection.selectRow($scope.gridOptions.data[j]);
                                                        $scope.gridOptions.data[j]["Stock Carat"] += ParcelTemplateService.getSelectedParcels()[parcelIdKey].carat;
                                                        $scope.gridOptions.data[j]["Stock Pieces"] += ParcelTemplateService.getSelectedParcels()[parcelIdKey].pieces;
                                                        $scope.gridOptions.data[j]["exchangeRate"] += ParcelTemplateService.getSelectedParcels()[parcelIdKey].exRate;
                                                        break;
                                                    }
                                                }
                                            }
                                        });
                                    }

                                    window.setTimeout(function () {
                                        $(window).resize();
                                        $(window).resize();
                                    }, 100);
                                    $rootScope.unMaskLoading();
                                };
                                DynamicFormService.convertorForCustomField(res, success);
                            }, function () {
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
            $scope.refreshFooterData = function () {
                var footerData = {};
                for (var j = 0; j < $scope.editableFieldNames.length; j++) {
                    footerData[$scope.editableFieldNames[j]] = 0;
                }
                for (var i = 0; i < $scope.editableParcelRows.length; i++) {
                    for (var j = 0; j < $scope.editableFieldNames.length; j++) {
                        if ($scope.editableParcelRows[i][$scope.editableFieldNames[j]] == undefined || $scope.editableParcelRows[i][$scope.editableFieldNames[j]] == null) {
                            $scope.editableParcelRows[i][$scope.editableFieldNames[j]] = 0;
                        }
                        footerData[$scope.editableFieldNames[j]] = Number(footerData[$scope.editableFieldNames[j]]);
                        if ($scope.editableFieldNames[j] != "parcelId$AG$String") {
                            $scope.editableParcelRows[i][$scope.editableFieldNames[j]] = Number($scope.editableParcelRows[i][$scope.editableFieldNames[j]]);
                        }
                        footerData[$scope.editableFieldNames[j]] += $scope.editableParcelRows[i][$scope.editableFieldNames[j]];
                    }
                }
                $scope.tableFooterData = footerData;
                if ($scope.editableParcelRows.length > 0) {
                    $scope.tableFooterData["origRateInDollar$NM$Double"] = $scope.tableFooterData["origRateInDollar$NM$Double"] / $scope.editableParcelRows.length;
                    $scope.tableFooterData["exchangeRate"] = $scope.tableFooterData["exchangeRate"] / $scope.editableParcelRows.length;
                }
            };
            $scope.retrieveParcels();
        }]);
});