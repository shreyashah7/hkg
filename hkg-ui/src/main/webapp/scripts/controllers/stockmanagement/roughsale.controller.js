define(['hkg', 'roughsaleService', 'customFieldService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'printBarcodeValue', 'dynamicForm', 'accordionCollapse', 'ParcelTemplateController', 'ParcelTemplateService'], function (hkg, roughsaleService) {
    hkg.register.controller('RoughSaleController', ["$rootScope", "$scope", "RoughSaleService", "DynamicFormService", "CustomFieldService", "$timeout", "ParcelTemplateService", "$route", function ($rootScope, $scope, RoughSaleService, DynamicFormService, CustomFieldService, $timeout, ParcelTemplateService, $route) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "roughSale";
            $rootScope.activateMenu();
            var featureMap = {};
            var featureDbFieldMap = {};
            $scope.selectedSale = [];
            $scope.retrieveSearchField = function () {
//                var invoiceDbFieldName = [];
//                var parcelDbFieldName = [];
                $scope.searchParcelTemplate = [];
                $scope.searchInvoiceTemplate = [];
                $scope.search = {};
                $scope.generalSearchTemplate = [];
                $scope.searchCustom = {};
                var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("saleRough");
                templateData.then(function (section) {
                    var serchTemplate = section['genralSection'];
                    angular.forEach(serchTemplate, function (itr) {
                        if (itr.featureName === 'invoice' || itr.featureName === 'parcel' || itr.featureName === 'sell') {
                            $scope.generalSearchTemplate.push(angular.copy(itr));
                        }
                    });
                    $scope.searchDirective = true;
                    angular.forEach($scope.generalSearchTemplate, function (item) {
                        featureMap[item.model] = item.featureName;
                        if (item.featureName.toLowerCase() === 'invoice') {
                            $scope.searchInvoiceTemplate.push(angular.copy(item));
                            invoiceDbFieldName.push(angular.copy(item.model));
                        } else if (item.featureName.toLowerCase() === 'parcel') {
                            parcelDbFieldName.push(angular.copy(item.model));
                            $scope.searchParcelTemplate.push(angular.copy(item));
                        }
                    })
                    if (invoiceDbFieldName.length > 0) {
                        featureDbFieldMap['invoice'] = invoiceDbFieldName;
                    }
                    if (parcelDbFieldName.length > 0) {
                        featureDbFieldMap['parcel'] = parcelDbFieldName;
                    }
                }, function (reason) {

                }, function (update) {

                });
                $scope.dbType = {};
                $scope.datePicker = {};
                $scope.open = function ($event, opened) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    $scope.datePicker[opened] = true;
                };
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
            };
            $scope.sellScreenRule = function (row, colField) {
                var color;

                if (!!row.entity.screenRuleDetailsWithDbFieldName && row.entity.screenRuleDetailsWithDbFieldName[colField] !== undefined && row.entity.screenRuleDetailsWithDbFieldName[colField] !== null) {
                    if ($scope.selectedSale.length === 0 || ($scope.selectedSale[0].$$sellId$$ !==row.entity.$$sellId$$)) {
                        color = row.entity.screenRuleDetailsWithDbFieldName[colField].colorCode;
                    }
                }
                return color;
            };
//            $scope.retrieveSearchField();
            $scope.initializeData = function () {

                $rootScope.maskLoading();
                $scope.searchInvoiceTemplate = [];
                $scope.searchParcelTemplate = [];
                $scope.searchSellTemplate = [];
                var invoiceDbFieldName = [];
                var parcelDbFieldName = [];
                var sellDbFieldName = [];
                $scope.flag = {};
                $scope.sellDbType = {};
                if ($scope.roughSaleForm != null) {
                    $scope.roughSaleForm.$dirty = false;
                }
                $scope.flag.template = false;
                $scope.flag.rowSelectedflag = false;
                $scope.flag.template = false;
                //Flag to load dynamic directive when required.
                $scope.flag.loadDynamicForm = false;
                $scope.searchCustom = {};
                $scope.roughSale = this;
                this.sellFieldAddShow = false;
                $scope.labelListForUiGrid = [];
                ParcelTemplateService.setFeatureName("saleRough");
                $scope.parameters = ["saleRough", "GEN"];
                CustomFieldService.retrieveDesignationBasedFieldsBySection($scope.parameters, function (response) {
                    $scope.response = angular.copy(response);
                    $scope.categoryCustom = {};
                    var templateDataForUpdate = DynamicFormService.retrieveSearchWiseCustomFieldInfo("saleRough");
                    $scope.generalSearchTemplate = [];
                    templateDataForUpdate.then(function (section) {
                        var serchTemplate = section['genralSection'];
                        angular.forEach(serchTemplate, function (itr) {
                            if (itr.featureName === 'invoice' || itr.featureName === 'parcel' || itr.featureName === 'sell') {
                                $scope.generalSearchTemplate.push(angular.copy(itr));
                            }
                        });
                        $scope.searchDirective = true;
                        $scope.sellDbFieldName = [];
                        $scope.roughSale = this;
//                        this.sellFieldAddShow = true;
                        $scope.labelListForUiGrid.push({name: 'totalPieces', displayName: 'Total Pieces', minWidth: 200});
                        $scope.labelListForUiGrid.push({name: 'totalCarat', displayName: 'Total Carat', minWidth: 200});
                        $scope.labelListForUiGrid.push({name: 'totalAmountInDollar', displayName: 'Total Amount($)', minWidth: 200});
                        $scope.labelListForUiGrid.push({name: 'totalAmountInRs', displayName: 'Total Amount(INR)', minWidth: 200});
                        $scope.fieldIdNameMap = {};
                        angular.forEach($scope.generalSearchTemplate, function (itr) {
                            if (itr.fromModel) {
                                $scope.fieldIdNameMap[itr.fieldId] = itr.fromModel;
                                if ($scope.sellDbFieldName.indexOf(itr.fromModel) === -1)
                                    $scope.sellDbFieldName.push(itr.fromModel);
                                if (itr.featureName === 'sell') {
                                    $scope.labelListForUiGrid.push({name: itr.fromModel, displayName: itr.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.sellScreenRule(row, \'' + itr.fromModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                }
                            } else if (itr.toModel) {
                                $scope.fieldIdNameMap[itr.fieldId] = itr.toModel;
                                if ($scope.sellDbFieldName.indexOf(itr.toModel) === -1)
                                    $scope.sellDbFieldName.push(itr.toModel);
                                if (itr.featureName === 'sell') {
                                    $scope.labelListForUiGrid.push({name: itr.toModel, displayName: itr.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.sellScreenRule(row, \'' + itr.toModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                }
                            } else if (itr.model) {
                                $scope.fieldIdNameMap[itr.fieldId] = itr.model;
                                featureMap[itr.model] = itr.featureName;
                                if (itr.featureName.toLowerCase() === 'invoice') {
                                    $scope.searchInvoiceTemplate.push(angular.copy(itr));
                                    invoiceDbFieldName.push(angular.copy(itr.model));
                                } else if (itr.featureName.toLowerCase() === 'parcel') {
                                    parcelDbFieldName.push(angular.copy(itr.model));
                                    $scope.searchParcelTemplate.push(angular.copy(itr));
                                }
                                else if (itr.featureName.toLowerCase() === 'sell') {
                                    sellDbFieldName.push(angular.copy(itr.model));
                                    $scope.searchSellTemplate.push(angular.copy(itr));
                                }
                                if ($scope.sellDbFieldName.indexOf(itr.model) === -1)
                                    $scope.sellDbFieldName.push(itr.model);
                                if (itr.featureName === 'sell') {
                                    $scope.labelListForUiGrid.push({name: itr.model, displayName: itr.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.sellScreenRule(row, \'' + itr.model + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                }
                            }
                        });
                        if (invoiceDbFieldName.length > 0) {
                            featureDbFieldMap['invoice'] = invoiceDbFieldName;
                        }
                        if (parcelDbFieldName.length > 0) {
                            featureDbFieldMap['parcel'] = parcelDbFieldName;
                        }
                        if (sellDbFieldName.length > 0) {
                            featureDbFieldMap['sell'] = sellDbFieldName;
                        }
                        var sellDataBean = {};
                        $scope.map = {};
                        $scope.map['dbFieldNames'] = $scope.sellDbFieldName;
                        sellDataBean.uiFieldMap = $scope.map;
                        sellDataBean.ruleConfigMap = {
                            fieldIdNameMap: $scope.fieldIdNameMap,
                            featureName: "roughSale"
                        };
                        $scope.listFilled = false;

                        $scope.gridOptions = {};
                        $scope.searchedData = [];
                        $scope.searchedDataFromDbForUiGrid = [];
                        RoughSaleService.retrieveAllSellDocument(sellDataBean, function (res) {
                            var lotField = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                            lotField.then(function (section) {
                                $scope.lotCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);
                                $scope.lotTemplate = section['genralSection'];
                                $scope.searchedDataFromDb = angular.copy(res);
                                var success = function (result) {
                                    $scope.searchedData = angular.copy(result);
                                    angular.forEach($scope.searchedData, function (itr) {
                                        ///////////////////////////////////////////UI-GRID/////////////////////////////////////////////////////
                                        angular.forEach($scope.labelListForUiGrid, function (list) {
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
                                        itr.categoryCustom.$$sellId$$ = itr.value;
                                        itr.categoryCustom.screenRuleDetailsWithDbFieldName = itr.screenRuleDetailsWithDbFieldName;
                                        $scope.searchedDataFromDbForUiGrid.push(itr.categoryCustom);
                                    });
                                    function rowTemplate() {
                                        return '<div ng-dblclick="grid.appScope.rowDblClick(row)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }"  ui-grid-cell></div>';
                                    }
                                    $scope.rowDblClick = function (row) {
                                        $scope.showParcelTemplate('callMethod', row);
                                    };
                                    $scope.gridOptions = {};
                                    $scope.gridOptions.enableFiltering = true;
                                    $scope.gridOptions.data = $scope.searchedDataFromDbForUiGrid;
                                    $scope.gridOptions.columnDefs = $scope.labelListForUiGrid;
                                    $scope.gridOptions.multiSelect = false;
                                    $scope.gridOptions.enableRowSelection = true;
                                    $scope.gridOptions.enableSelectAll = false;
                                    $scope.gridOptions.rowTemplate = rowTemplate();
                                    $scope.gridOptions.onRegisterApi = function (gridApi) {
                                        $scope.gridApi = gridApi;
                                        gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                                            if ($scope.gridApi.selection.getSelectedRows().length > 0) {
                                                $scope.selectedSale = $scope.gridApi.selection.getSelectedRows();
                                                $scope.flag.rowSelectedflag = true;
                                            } else {
                                                $scope.selectedSale = [];
                                                $scope.flag.rowSelectedflag = false;
                                            }
                                        });
                                        gridApi.selection.on.rowSelectionChangedBatch($scope, function (rows) {
                                            if ($scope.gridApi.selection.getSelectedRows().length > 0) {
                                                $scope.selectedSale = $scope.gridApi.selection.getSelectedRows();
                                                $scope.flag.rowSelectedflag = true;
                                            } else {
                                                $scope.selectedSale = [];
                                                $scope.flag.rowSelectedflag = false;
                                            }
                                        });
                                    };
                                    window.setTimeout(function () {
                                        $(window).resize();
                                        $(window).resize();
                                    }, 100);
                                    $scope.listFilled = true;
                                    $scope.dataRetrieved = true;
                                    $rootScope.unMaskLoading();
                                };
                                DynamicFormService.convertorForCustomField(res, success);

                            }, function (reason) {

                            }, function (update) {
                            });

                        }, function () {
                            var msg = "Could not retrieve, please try again.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        });
                    }, function (reason) {

                    }, function (update) {
                    });
                }, function () {
                    $rootScope.unMaskLoading();
                    var msg = "Could not retrieve, please try again.";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                });

            };
            $scope.initroughSaleForm = function (roughSaleForm) {
                $scope.roughSaleForm = roughSaleForm;
            };
            $scope.initializeData();

            $scope.retrieveSearchedData = function () {
                $scope.selectOneParameter = false;
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
                        $scope.gridOptionsForInvoice = {};
                        $scope.searchedParcel = [];
                        $scope.searchedParcelForUiGrid = [];
                        $scope.listFilled = false;
                        $scope.parcelDataBean = {};
                        $scope.parcelDataBean.featureCustomMapValue = {};
                        $rootScope.maskLoading();
                        var finalMap = {};
                        var searchResult = DynamicFormService.convertSearchData($scope.searchInvoiceTemplate, $scope.searchParcelTemplate, $scope.searchSellTemplate, null, $scope.searchCustom);
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
                        $scope.parcelDataBean.parcelCustom = angular.copy(searchResult);
                        $scope.parcelDataBean.featureDbFieldMap = featureDbFieldMap;
                        $scope.parcelDataBean.ruleConfigMap = {
                            fieldIdNameMap: $scope.fieldIdNameMap,
                            featureName: "roughSale"
                        };
                        RoughSaleService.search($scope.parcelDataBean, function (res) {

                            var success = function (result)
                            {
                                $scope.searchedParcel = angular.copy(result);
                                $scope.mapToArray = [];
                                angular.forEach($scope.searchedParcel, function (itr) {
                                    ///////////////////////////////////////////UI-GRID/////////////////////////////////////////////////////
                                    angular.forEach($scope.labelListForUiGrid, function (list) {
                                        if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                            itr.categoryCustom[list.name] = "NA";
                                        }
                                        else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                            if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                itr.categoryCustom[list.name] = "NA";
                                            }
                                        }
                                        itr.categoryCustom["value"] = itr.value;
                                    });
                                    itr.categoryCustom.$$sellId$$ = itr.value;
                                    itr.categoryCustom.screenRuleDetailsWithDbFieldName = itr.screenRuleDetailsWithDbFieldName;
                                    $scope.searchedParcelForUiGrid.push(itr.categoryCustom);
                                });
                                $scope.gridOptions.data = $scope.searchedParcelForUiGrid;
                                window.setTimeout(function () {
                                    $(window).resize();
                                    $(window).resize();
                                }, 100);
                                $scope.listFilled = true;
                            }
                            DynamicFormService.convertorForCustomField(res, success);
                            $rootScope.unMaskLoading();
                        }, function () {
                            var msg = "Could not retrieve, please try again.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        });
//                                }
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
            }

            $scope.showParcelTemplate = function (param, row) {
                $rootScope.maskLoading();
                $scope.categoryCustom = {};
                ParcelTemplateService.setSelectedParcels(null);
                $scope.flag.template = true;
                $scope.generalSellTemplate = [];
                $scope.roughSale = this;
                this.sellFieldAddShow = true;
                $scope.sellDataBean = {};
//                TODO fieldNotConfigured                 
                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("sell");
                templateData.then(function (section) {
                    $scope.generalSellTemplate = section['genralSection'];
                    var sellField = [];
                    var result = Object.keys($scope.response).map(function (key, value) {
                        angular.forEach(this[key], function (itr) {
                            if (key === 'Sell') {
                                sellField.push({Sell: itr});
                            }
                        });
                    }, $scope.response);
//                    $scope.mandatoryFieldsForSellTemplate = [""];
                    $scope.generalSellTemplate = DynamicFormService.retrieveCustomData($scope.generalSellTemplate, sellField);
                    var listOfModel = [];
                    $scope.sellTemplateFields = [];
                    $scope.fieldIdNameMap = {};
                    angular.forEach($scope.generalSellTemplate, function (itr) {
                        if (itr.model === 'sell_to$DRP$Long') {
                            itr.required = true;
                        }
                        $scope.fieldIdNameMap[itr.fieldId] = itr.model;
                        $scope.sellTemplateFields.push(itr.model);
                        listOfModel.push(itr.model);
                    });

                    $scope.fieldNotConfigured = false;
                    if (listOfModel.length > 0) {
                        if (listOfModel.indexOf("sell_to$DRP$Long") === -1) {
                            $scope.fieldNotConfigured = true;
                        }
                    } else {
                        $scope.fieldNotConfigured = true;
                    }
                    if (param === 'callMethod' && $scope.map !== undefined && $scope.map !== null) {
                        var selectedRow = row.entity;
                        var ids = [];
                        ids.push(selectedRow.sellObjectId);
                        $scope.sellDataBean.id = selectedRow.sellObjectId;
                        $scope.map['sellObjectId'] = ids;
                        $scope.map['dbFieldNames'] = $scope.sellTemplateFields;
                        var sellDataBean = {};
                        sellDataBean.uiFieldMap = $scope.map;
                        sellDataBean.ruleConfigMap = {
                            fieldIdNameMap: $scope.fieldIdNameMap,
                            featureName: "roughSale"
                        };
                        RoughSaleService.retrieveselldocumentforparcelbyid(sellDataBean, function (res) {
                            ParcelTemplateService.setSelectedParcels(res.custom6);
                            $scope.categoryCustom = res.categoryCustom;
                            $scope.screenRules = res.screenRuleDetailsWithDbFieldName;
                            $scope.flag.loadDynamicForm = true;
                            $rootScope.unMaskLoading();
                        }
                        , function () {
                            $scope.flag.loadDynamicForm = true;
                            var msg = "Could not retrieve parcel ids, please try again.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        });
                    }else{
                        $scope.flag.loadDynamicForm = true;
                    }
                    $scope.roughSale = this;
                    this.sellFieldAddShow = true;
                    $rootScope.unMaskLoading();
                }, function (reason) {

                }, function (update) {

                });
//                $scope.dbType = {};

//                TODO fieldNotConfigured                 

            };

            $scope.sellParceool = function (roughSaleForm) {
                $scope.submitted = true;
                if (roughSaleForm !== null && roughSaleForm.$valid) {
                    if (Object.getOwnPropertyNames($scope.categoryCustom).length > 0) {
                        $scope.submitted = false;
                        var data = ParcelTemplateService.getSelectedParcelData();
                        $scope.sellDataBean.sellCustom = $scope.categoryCustom;
                        $scope.sellDataBean.sellDbType = $scope.sellDbType;
                        $scope.sellDataBean.totalPieces = data.footerData.changedPieces;
                        $scope.sellDataBean.totalCarat = data.footerData.changedCarat;
                        $scope.sellDataBean.totalAmountInDollar = data.footerData.changedAmountInDollar;
                        $scope.sellDataBean.totalAmountInRs = data.footerData.changedAmountInRs;
                        $scope.sellDataBean.roughStockDetailDataBeans = [];
                        $scope.sellDataBean.roughStockDetailDataBeans = data.roughStock;
                        $scope.sellDataBean.parcels = [];
                        angular.forEach(data.roughStock, function (itr) {
                            $scope.sellDataBean.parcels.push(itr.parcel);
                        });
                        RoughSaleService.saleParcels($scope.sellDataBean, function (res) {
                            $scope.searchDirective = false;
                            $scope.initializeData();
                        }, function () {
                            $rootScope.unMaskLoading();
                            var msg = "Could not sell parcel, please try again.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                        });
                    } else {
                        var msg = "Please enter atleast one value";
                        var type = $rootScope.warning;
                        $rootScope.addMessage(msg, type);
                    }
                }
            };

            $scope.resetAddForm = function (roughSaleForm) {
                if ($scope.roughSaleForm != null) {
                    $scope.roughSaleForm.$dirty = false;
                }
                $scope.flag.template = false;
                $scope.flag.rowSelectedflag = false;
                $scope.roughSale = this;
                this.sellFieldAddShow = false;
                $scope.categoryCustom = {};
                $scope.flag.loadDynamicForm = false;
                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("sell");
                templateData.then(function (section) {
                    var sellField = [];
                    var result = Object.keys($scope.response).map(function (key, value) {
                        angular.forEach(this[key], function (itr) {
                            if (key === 'Sell') {
                                sellField.push({Invoice: itr});
                            }
                        });
                    }, $scope.response);
                    $scope.generalSellTemplate = section['genralSection'];
                    $scope.generalSellTemplate = DynamicFormService.retrieveCustomData($scope.generalSellTemplate, sellField);
                    $rootScope.unMaskLoading();
                }, function (reason) {
                }, function (update) {
                });
            };
            $scope.onCancelOfSearch = function (roughSaleForm) {
//                if ($scope.roughSaleForm != null) {
//                    $scope.roughSaleForm.$dirty = false;
//                }
//                $scope.searchDirective = false;
//                $scope.searchCustom = {};
//                var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("saleRough");
//                templateData.then(function (section) {
//                    $scope.generalSearchTemplate = section['genralSection'];
//                    $scope.searchDirective = true;
//                    $rootScope.unMaskLoading();
//                }, function (reason) {
//                }, function (update) {
//                });
                $route.reload();
            };

            $scope.isParcelSelected = function () {
                var data = ParcelTemplateService.getSelectedParcelData();
                if (data !== undefined && data.roughStock !== undefined && data.roughStock.length > 0 && data.footerData !== undefined) {
                    return false;
                }
                return true;
            };
        }
    ]);
});