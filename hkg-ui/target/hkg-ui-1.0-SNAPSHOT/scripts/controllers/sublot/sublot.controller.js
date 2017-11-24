/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Author : sadhvaryu
 */
define(['hkg', 'sublotService', 'parcelService', 'customFieldService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'printBarcodeValue', 'dynamicFormGrid', 'editableCustomGrid', 'accordionCollapse', 'ParcelTemplateService'], function (hkg) {
    hkg.register.controller('SublotController', ["$rootScope", "$scope", "SublotService", "ParcelService", "DynamicFormService", "CustomFieldService", '$q', 'ParcelTemplateService', function ($rootScope, $scope, SublotService, ParcelService, DynamicFormService, CustomFieldService, $q, ParcelTemplateService) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "SubLot";
            $scope.entity = 'SUBLOT.';
            $scope.sublotDataBean = {};
            $scope.sublotDataBean.parcelDataBean = {};
            var parcelId = "";
            $scope.search = {};
            $scope.valueCustom = {};
            $scope.isParcelSelected = false;
            $scope.searchedDataFromDbForUiGrid = [];
            $scope.selectedParcel = [];
            ParcelTemplateService.setFeatureName("lotSub");
            $rootScope.activateMenu();
            $scope.searchCustom = DynamicFormService.resetSection($scope.generalSublotTemplate);
            var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("lotSub");
            $scope.caratValueSublotTemplate = [];
            var parcelIdMap = [];
            $scope.dbType = {};
            $scope.datePicker = {};
            $scope.showConfigMsg = true;
            $scope.showSublotConfigMsg = true;
            $scope.isParcelSelected = false;
            $scope.open = function ($event, opened) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.datePicker[opened] = true;
            };
            var staticFieldList = ['shape', 'quality_from', 'quality_to', 'color_from', 'color_to', 'cut', 'carat'];
            $scope.dateOptions = {
                'year-format': "'yy'",
                'starting-day': 1
            };
            var currentDate = new Date();
            currentDate.setSeconds(0);
            currentDate.setMilliseconds(0);
            currentDate.setHours(0);
            currentDate.setMinutes(0);
            $scope.search.toDate = new Date(currentDate);
            $scope.search.fromDate = new Date(currentDate.setMonth(currentDate.getMonth() - 1));
            $scope.format = $rootScope.dateFormat;
            $scope.initSublotForm = function (subLoteForm) {
                $scope.sublotForm = subLoteForm;
            };

            $scope.$watch("valueCustom", function () {
                //If any row is editable when value is changed then call copyValuesToObject for that row
                if ($scope.editableGridSublotOptions.getRowIndexInEditMode && $scope.editableGridSublotOptions.getRowIndexInEditMode() >= 0) {
                    copyValuesToObject($scope.editableGridSublotOptions.datarows[$scope.editableGridSublotOptions.getRowIndexInEditMode()], $scope.valueCustom);
                }
            }, true);

            var copyValuesToObject = function (obj, newValue) {
                angular.forEach(newValue, function (value, key) {
                    obj.categoryCustom[ key ] = value;
                });
            };
            $scope.createOrUpdateSubLot = function (createOrUpdateRecord, dbType, callback) {
                $scope.sublotDataBean.subLotCustom = createOrUpdateRecord.categoryCustom;
                $scope.sublotDataBean.id = createOrUpdateRecord.value;
                $scope.sublotDataBean.subLotDbType = dbType;
                $scope.sublotDataBean.isArchive = false;
                $scope.sublotDataBean.uiFieldMap = $scope.map;
                $scope.sublotDataBean.ruleConfigMap = {
                    fieldIdNameMap: $scope.fieldIdNameMap,
                    featureName: "subLot"
                };
                $scope.sublotDataBean.parcelDataBean = {};
                $scope.sublotDataBean.parcelDataBean.id = parcelId;
                $rootScope.maskLoading();

                if ($scope.sublotDataBean.id == undefined || $scope.sublotDataBean.id == null) {
                    SublotService.createSublot($scope.sublotDataBean, function (response) {
                        var deferred = $q.defer();
                        var promise = deferred.promise;
                        promise.then(function () {
                            console.log(JSON.stringify(response));
                            callback(response);
                        });
                        deferred.resolve();
                        $rootScope.unMaskLoading();
                    }, function () {
                        $rootScope.unMaskLoading();
                        var msg = "Could not create sublot, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                } else {
                    SublotService.updateSublot($scope.sublotDataBean, function (response) {
                        var deferred = $q.defer();
                        var promise = deferred.promise;
                        promise.then(function () {
                            callback(response);
                        });
                        deferred.resolve();
                        $rootScope.unMaskLoading();
                    }, function () {
                        $rootScope.unMaskLoading();
                        var msg = "Could not update sublot, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                }


            };
            $scope.deleteSublot = function (deleteRecord) {
                SublotService.deleteSublot(deleteRecord.value, function (response) {
                    $scope.retrieveSublots();
                    $rootScope.unMaskLoading();
                }, function () {
                    $rootScope.unMaskLoading();
                    var msg = "Could not delete Sublot, please try again.";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                });
            };
            var newRowAdded = function () {
                if ($scope.valueCustom) {
                    copyValuesToObject($scope.editableGridSublotOptions.datarows[$scope.editableGridSublotOptions.getRowIndexInEditMode()], $scope.valueCustom);
                }
            };
            $scope.editableGridSublotOptions = {
                datarows: [],
                template: {},
                labelrows: [],
                labelrowsFromDb: [],
                datarowsFromDb: [],
                dbType: {},
                createOrUpdateRecord: $scope.createOrUpdateSubLot,
                deleteRecord: $scope.deleteSublot,
                deleteModalId: 'sublotModalPanel',
                cancelModalId: 'sublotCancelModalPanel',
                featureName: 'Sublot',
                tableName: 'sublotTable',
                newRowAdded: newRowAdded
            };
            templateData.then(function (section) {
                $scope.generalsublotTemplate = [];
                $scope.editableGridSublotOptions.template = [];
                $scope.generalSublotTemplate = section['genralSection'];
                $scope.editableGridSublotOptions.template = angular.copy(section['genralSection']);
                $scope.searchResetFlag = true;
            }, function (reason) {

            }, function (update) {

            });
            var fillCaratValueSublotTemplate = function (template) {
                template.forEach(function (val) {
                    if (staticFieldList.indexOf(val.modelWithoutSeperators) !== -1) {
                        $scope.caratValueSublotTemplate.push(val);
                    }
                });
            };

            $scope.resetCommonConfig = function () {
                $scope.valueCustom = {};
            };

            var retrieveTemplateData = function () {
                CustomFieldService.retrieveDesignationBasedFields("lotSub", function (response) {
                    $scope.response = angular.copy(response);
                    $scope.sublotDataBean = {};
                    var templateDataForUpdate = DynamicFormService.retrieveSectionWiseCustomFieldInfo("subLotEntity");
                    if (!($scope.dbTypeForUpdate))
                        $scope.dbTypeForUpdate = {};
                    templateDataForUpdate.then(function (section) {
                        $scope.editableGridSublotOptions.template = null;
                        $scope.editableGridSublotOptions.template = section['genralSection'];
//                                $scope.editableGridSublotOptions.template));
                        fillCaratValueSublotTemplate($scope.editableGridSublotOptions.template);
                    });
                }, function () {
                    $rootScope.unMaskLoading();
                    var msg = "Failed to retrieve template data";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                });
            };

            $scope.retrieveSublots = function () {
                CustomFieldService.retrieveDesignationBasedFields("lotSub", function (response) {
                    $scope.response = angular.copy(response);
                    $scope.sublotDataBean = {};
                    var templateDataForUpdate = DynamicFormService.retrieveSectionWiseCustomFieldInfo("subLotEntity");
                    if (!($scope.dbTypeForUpdate))
                        $scope.dbTypeForUpdate = {};
                    templateDataForUpdate.then(function (section) {
                        $scope.editableGridSublotOptions.template = null;
                        $scope.editableGridSublotOptions.template = section['genralSection'];
//                                $scope.editableGridSublotOptions.template));
//                        fillCaratValueSublotTemplate($scope.editableGridSublotOptions.template);
//                        $scope.caratValueSublotTemplate = angular.copy($scope.editableGridSublotOptions.template);

                        $scope.listOfModelsOfDateType = [];
                        var sublotField = [];
                        var result = Object.keys(response).map(function (key, value) {
                            angular.forEach(this[key], function (itr) {
                                if (key === 'SubLot') {
                                    sublotField.push({Sublot: itr});
                                }
                            });
                        }, response);
                        $scope.editFieldName = [];
                        $scope.fieldIdNameMap = {};
                        $scope.editableGridSublotOptions.template = DynamicFormService.retrieveCustomData($scope.editableGridSublotOptions.template, sublotField);
                        angular.forEach($scope.editableGridSublotOptions.template, function (itr) {
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
                        $scope.showSublotConfigMsg = false;
//                        var fieldsInTemplate = [];
//                        if ($scope.editableGridSublotOptions.template !== undefined && $scope.editableGridSublotOptions.template !== null && $scope.editableGridSublotOptions.template.length > 0) {
//                            for (var i = 0; i < $scope.editableGridSublotOptions.template.length; i++) {
//                                fieldsInTemplate.push(angular.copy($scope.editableGridSublotOptions.template[i].model));
//                            }
//                        }
//                        if (fieldsInTemplate.length > 0 && $scope.mandatorySublotFields && $scope.mandatorySublotFields.length > 0) {
//                            for (var field = 0; field < $scope.mandatorySublotFields.length; field++) {
//                                if (fieldsInTemplate.indexOf($scope.mandatorySublotFields[field]) === -1) {
//                                    $scope.fieldNotConfigured = true;
//                                    break;
//                                }
//                            }
//                        } else {
//                            $scope.fieldNotConfigured = false;
//                        }
                        if ($scope.fieldNotConfigured) {
                            $scope.showSublotConfigMsg = true;
                        }
                        $scope.map = {};
                        $scope.map['dbFieldNames'] = $scope.editFieldName;
//                        $scope.map['fieldIds'] = $scope.fieldIdNameMap;
//                        $scope.map['featureName'] = ['subLot'];
                        if ($scope.editFieldName.length > 0) {
                            var data = {
                                parcelId: parcelId,
                                map: $scope.map,
                                ruleConfigMap: {
                                    fieldIdNameMap: $scope.fieldIdNameMap,
                                    featureName: "subLot"
                                }
                            };
                            SublotService.retrieveSublotsbyParcel(data, function (res) {
                                if (res !== undefined) {
                                    $scope.editableGridSublotOptions.datarows = [];
                                    $scope.editableGridSublotOptions.labelrows = [];
                                    angular.forEach(res, function (item) {
                                        item.isEditGridFlag = false;
                                        $scope.editableGridSublotOptions.datarows.push(item);
                                    });
                                    var cntSublot = 0;
                                    $scope.editableGridSublotOptions.datarowsFromDb = angular.copy($scope.editableGridSublotOptions.datarows);
                                    $scope.editableGridSublotOptions.datarows.push({isEditGridFlag: false, categoryCustom: {}});
                                }
                                var success = function (result) {
                                    $scope.editableGridSublotOptions.labelrows = angular.copy(result);
                                    $scope.editableGridSublotOptions.labelrows.push({isEditGridFlag: false, categoryCustom: {}});
                                    $scope.editableGridSublotOptions.labelrowsFromDb = angular.copy(result);
                                };
                                $scope.newVar = angular.copy(res);
                                if ($scope.newVar !== undefined) {
                                    DynamicFormService.convertorForCustomField($scope.newVar, success);
                                }
                                ;
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

            var rowSelected = function (rowEntity) {
                parcelId = parcelIdMap[rowEntity["parcelId$AG$String"]];
                $scope.retrieveSublots();
                $scope.isParcelSelected = true;
            };
            $scope.gridOptions = {
                enableRowSelection: true,
                enableSelectAll: false,
                enableFiltering: true,
                multiSelect: false,
                data: []
            };

            var rowDeSelected = function (rowEntity) {
                $scope.showSublotConfigMsg = true;
                $scope.isParcelSelected = false;
            };

            $scope.gridOptions.onRegisterApi = function (gridApi) {
                //set gridApi on scope
                $scope.gridApi = gridApi;
                gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                    $scope.selectedParcel = gridApi.selection.getSelectedRows();
                    if (row.isSelected) {
                        rowSelected(row.entity);
                    }
                    else {
                        rowDeSelected(row.entity);
                    }
//                    $scope.refreshFooterData();
                });
            };
            $scope.subLotParcelScreenRule = function (row, colField) {
                var color;

                if (!!row.entity.screenRuleDetailsWithDbFieldName && row.entity.screenRuleDetailsWithDbFieldName[colField] !== undefined && row.entity.screenRuleDetailsWithDbFieldName[colField] !== null) {
                    if ($scope.selectedParcel.length === 0 || ($scope.selectedParcel[0].$$parcelId$$ !== row.entity.$$parcelId$$)) {
                        color = row.entity.screenRuleDetailsWithDbFieldName[colField].colorCode;
                    }
                }
                return color;
            };
            $scope.parcelLabelListForUiGrid = [];
            $scope.invoiceLabelListForUiGrid = [];
            $scope.retrieveParcels = function () {
                $rootScope.maskLoading();
                CustomFieldService.retrieveDesignationBasedFieldsBySection(["lotSub", "INP"], function (response) {
                    $scope.response = angular.copy(response);
                    var templateDataForUpdate = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");

                    if (!($scope.dbTypeForUpdate !== null))
                        $scope.dbTypeForUpdate = {};
                    templateDataForUpdate.then(function (section) {
                        var invoiceTemplateDataForUpdate = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                        invoiceTemplateDataForUpdate.then(function (invoiceSection) {
                            $scope.parcelCommonTemplate = null;
                            $scope.parcelCommonTemplate = section['genralSection'];
                            $scope.invoiceCommonTemplate = invoiceSection['genralSection'];
                            $scope.listOfModelsOfDateType = [];
                            var parcelField = [];
                            var invoiceField = [];
                            var result = Object.keys(response).map(function (key, value) {
                                angular.forEach(this[key], function (itr) {
                                    if (key === 'Parcel') {
                                        parcelField.push({Parcel: itr});
                                    }
                                    else if (key === 'Invoice') {
                                        invoiceField.push({Invoice: itr});
                                    }
                                });
                            }, response);
                            $scope.editFieldName = [];
                            $scope.invoiceEditFieldName = [];
                            $scope.fieldIdNameMap = {};
                            $scope.invoiceFieldIdNameMap = {};
                            $scope.parcelCommonTemplate = DynamicFormService.retrieveCustomData($scope.parcelCommonTemplate, parcelField);
                            $scope.invoiceCommonTemplate = DynamicFormService.retrieveCustomData($scope.invoiceCommonTemplate, invoiceField);
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
                            angular.forEach($scope.invoiceCommonTemplate, function (itr) {
                                if (itr.fromModel) {
                                    $scope.invoiceFieldIdNameMap[itr.fieldId] = itr.fromModel;
                                    if ($scope.invoiceEditFieldName.indexOf(itr.fromModel) === -1)
                                        $scope.invoiceEditFieldName.push(itr.fromModel);
                                } else if (itr.toModel) {
                                    $scope.invoiceFieldIdNameMap[itr.fieldId] = itr.toModel;
                                    if ($scope.invoiceEditFieldName.indexOf(itr.toModel) === -1)
                                        $scope.invoiceEditFieldName.push(itr.toModel);
                                } else if (itr.model) {
                                    $scope.invoiceFieldIdNameMap[itr.fieldId] = itr.model;
                                    if ($scope.invoiceEditFieldName.indexOf(itr.model) === -1)
                                        $scope.invoiceEditFieldName.push(itr.model);
                                }
                            });

                            $scope.fieldNotConfigured = false;
                            $scope.showConfigMsg = false;
//                         //removed fieldsIntemplate related code to retrieve go to revision 7054
                            if ($scope.fieldNotConfigured) {
                                $scope.showConfigMsg = true;
                            }
                            angular.forEach($scope.parcelCommonTemplate, function (itr) {
                                if (itr.fromModel) {
                                    $scope.parcelLabelListForUiGrid.push({name: itr.fromModel, displayName: itr.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.subLotParcelScreenRule(row, \'' + itr.fromModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                } else if (itr.toModel) {
                                    $scope.parcelLabelListForUiGrid.push({name: itr.toModel, displayName: itr.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.subLotParcelScreenRule(row, \'' + itr.toModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                } else if (itr.model) {
                                    $scope.parcelLabelListForUiGrid.push({name: itr.model, displayName: itr.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.subLotParcelScreenRule(row, \'' + itr.model + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                }
                            });
                            angular.forEach($scope.invoiceCommonTemplate, function (itr) {
                                if (itr.fromModel) {
                                    $scope.parcelLabelListForUiGrid.push({name: itr.fromModel, displayName: itr.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.subLotParcelScreenRule(row, \'' + itr.fromModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                } else if (itr.toModel) {
                                    $scope.parcelLabelListForUiGrid.push({name: itr.toModel, displayName: itr.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.subLotParcelScreenRule(row, \'' + itr.toModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                } else if (itr.model) {
                                    $scope.parcelLabelListForUiGrid.push({name: itr.model, displayName: itr.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.subLotParcelScreenRule(row, \'' + itr.model + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                }
                            });
                            var invoiceParcelMap = {}
                            $scope.map = {};
                            $scope.parcelDataBean = {};
                            $scope.map['dbFieldNames'] = $scope.editFieldName;
                                                      $scope.parcelDataBean.featureDbFieldMap = $scope.map;
                            $scope.parcelDataBean.ruleConfigMap = {
                                fieldIdNameMap: $scope.fieldIdNameMap,
                                featureName: "subLot"
                            };
                            $scope.invoiceMap = {};
                            $scope.invoiceDataBean = {};
                            $scope.invoiceDataBean['dbFieldNames'] = $scope.invoiceEditFieldName;
//                            $scope.invoiceDataBean.featureCustomMapValue = $scope.invoiceMap;
                            $scope.invoiceDataBean.ruleConfigMap = {
                                fieldIdNameMap: $scope.invoiceFieldIdNameMap,
                                featureName: "subLot"
                            };
                            invoiceParcelMap.parcel = JSON.stringify($scope.parcelDataBean);
                            invoiceParcelMap.invoice = JSON.stringify($scope.invoiceDataBean);
                            if ($scope.editFieldName.length > 0) {
                                SublotService.retrieveAllottedParcelAndInvoice(invoiceParcelMap, function (res) {
//                                    console.log(JSON.stringify(res))
                                    if (!(res !== undefined && res !== null && res.length > 0)) {
                                        $scope.searchedDataFromDbForUiGrid = undefined;
                                        $rootScope.unMaskLoading();
                                    }
                                    var success = function (result) {
                                        $scope.parcelData = angular.copy(result);
                                        $scope.parcelLabelListForUiGrid.push({name: "Stock Carat", displayName: "Stock Carat", minWidth: 200}, {name: "Stock Pieces", displayName: "Stock Pieces", minWidth: 200});
                                        $scope.searchedDataFromDbForUiGrid = [];
                                        parcelIdMap = {};
                                        angular.forEach($scope.parcelData, function (itr) {
                                            parcelIdMap[itr.categoryCustom["parcelId$AG$String"]] = itr.value;
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
                                            //For rule purpose
                                            itr.categoryCustom["$$parcelId$$"] = itr.value;
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
                            } else {
                                $rootScope.unMaskLoading();
                            }
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
            };
            $scope.retrieveParcels();
            retrieveTemplateData();
        }]);
});



