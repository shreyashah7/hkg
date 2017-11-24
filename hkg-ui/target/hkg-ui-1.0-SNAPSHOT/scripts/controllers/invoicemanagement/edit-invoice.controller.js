
define(['hkg', 'invoiceService', 'customFieldService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'printBarcodeValue', 'dynamicForm', 'accordionCollapse'], function(hkg, invoiceService) {
    hkg.register.controller('EditInvoiceController', ["$rootScope", "$scope", "InvoiceService", "$timeout", "$filter", "$location", "$window", "DynamicFormService", "CustomFieldService", '$interval', '$q', function($rootScope, $scope, InvoiceService, $timeout, $filter, $location, $window, DynamicFormService, CustomFieldService, $interval, $q) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "editInvoice";
//////////////////////ui-grid////////////////////////////////////////////
            $scope.invoiceLabelListForUiGrid = [];
            $scope.searchedDataFromDbForUiGrid = [];
//////////////////////ui-grid/////////////////////////////////////

            $rootScope.activateMenu();
            $scope.invoiceDataBean = {};
            $scope.searchCustom = DynamicFormService.resetSection($scope.generalInvoiceTemplate);
            var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("invoiceEdit");
            $scope.flag = {};
            $scope.flag.searchFieldNotAvailable = false;
            $scope.flag.showUpdatePage = false;
            $scope.dbType = {};
            var featureMap = {};
            $scope.invoiceDataBean.invoiceDbType = angular.copy($scope.dbType);
            templateData.then(function(section) {
                $scope.generalInvoiceTemplate = [];
                $scope.generalInvoiceTemplate = section['genralSection'];
                if ($scope.generalInvoiceTemplate === undefined || $scope.generalInvoiceTemplate.length === 0) {
                    $scope.flag.searchFieldNotAvailable = true;
                }
//                console.log("general inv template"+JSON.stringify(section['genralSection']));
                $scope.generalTemplate = angular.copy(section['genralSection']);
                $scope.invoiceLabelList = [];
                $scope.dbFieldNames = [];
                angular.forEach($scope.generalTemplate, function(itr) {
                    $scope.invoiceLabelList.push({model: itr.model, label: itr.label});
                    if (itr.fromModel) {
                        $scope.invoiceLabelListForUiGrid.push({name: itr.fromModel, displayName: itr.label, minWidth: 200});
                        $scope.dbFieldNames.push(itr.fromModel);
                    } else if (itr.toModel) {
                        $scope.dbFieldNames.push(itr.toModel);
                        $scope.invoiceLabelListForUiGrid.push({name: itr.toModel, displayName: itr.label, minWidth: 200});
                    } else if (itr.model) {
                        $scope.dbFieldNames.push(itr.model);
                        $scope.invoiceLabelListForUiGrid.push({name: itr.model, displayName: itr.label, minWidth: 200});
                    }
                });
                $scope.searchResetFlag = true;
                $scope.dataRetrieved = true;
            }, function(reason) {

            }, function(update) {

            });
            ///
            var invoiceFld = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
            invoiceFld.then(function(section) {
                $scope.invoiceCustomData = angular.copy(section['genralSection']);
            });
            ///
            $scope.initEditInvoiceForm = function(editInvoiceForm) {
                $scope.editInvoiceForm = editInvoiceForm;
            };
            $scope.initUpdateInvoiceForm = function(updateInvoiceForm) {
                $scope.updateInvoiceForm = updateInvoiceForm;
            };
            $scope.retrieveSearchedData = function() {
                $scope.selectOneParameter = false;
                $scope.searchedData = [];
                $scope.listFilled = false;
                if (Object.getOwnPropertyNames($scope.searchCustom).length > 0) {
                    $scope.gridOptions = {};
                    /////////////////////UI_GRID///////////////////// 
                    $scope.gridOptions.data = [];
                    $scope.searchedDataFromDbForUiGrid = [];
                    /////////////////////UI_GRID/////////////////////
                    var mapHasValue = false;
                    $scope.searchFinal = angular.copy($scope.searchCustom);
                    for (var prop in $scope.searchFinal) {
                        if (typeof $scope.searchFinal[prop] === 'object' && $scope.searchFinal[prop] != null) {
                            var toString = angular.copy($scope.searchFinal[prop].toString());
                            if (typeof toString === 'string' && toString !== null && toString !== undefined && toString.length > 0) {
                                mapHasValue = true;
                                break;
                            }
                        }
                        if (typeof $scope.searchFinal[prop] === 'string' && $scope.searchFinal[prop] !== null && $scope.searchFinal[prop] !== undefined && $scope.searchCustom[prop].length > 0) {
                            mapHasValue = true;
                            break;
                        }
                        if (typeof $scope.searchFinal[prop] === 'number' && !!($scope.searchFinal[prop])) {
                            mapHasValue = true;
                            break;
                        }
                        if (typeof $scope.searchFinal[prop] === 'boolean') {
                            mapHasValue = true;
                            break;
                        }
                    }
                    if (mapHasValue) {
                        $rootScope.maskLoading();

                        var searchResult = DynamicFormService.convertSearchData($scope.invoiceCustomData, null, null, null, $scope.searchFinal);
//                        console.log("Search Result.." + JSON.stringify(searchResult));
                        $scope.invoiceDataBean.invoiceCustom = angular.copy(searchResult);
                        $scope.invoiceDataBean.dbFieldNames = angular.copy($scope.dbFieldNames);
                        $scope.invoiceDataBean.searchOnParameter = true;
                        InvoiceService.search($scope.invoiceDataBean, function(res) {
//                            console.log("Section Dataaaa" + JSON.stringify(res))
                            var invoiceField = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                            invoiceField.then(function(section) {
//                                $scope.invoiceCustom = DynamicFormService.resetSection($scope.invoiceTemplate);

                                $scope.gridOptions.enableFiltering = true;
                                $scope.gridOptions.multiSelect = false;
                                $scope.gridOptions.enableRowSelection = true;
                                $scope.gridOptions.enableSelectAll = false;
                                $scope.selectedInvoice = [];
                                $scope.gridOptions.onRegisterApi = function(gridApi) {
                                    $scope.gridApi = gridApi;
                                    gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                                        if ($scope.selectedInvoice.length > 0) {
                                            $.each($scope.selectedInvoice, function(index, result) {
                                                if (result["$$hashKey"] === row["entity"]["$$hashKey"]) {
                                                    //Remove from array
                                                    $scope.flag.repeatedRow = true;
                                                    $scope.selectedInvoice.splice(index, 1);
                                                    return false;
                                                } else {
                                                    $scope.flag.repeatedRow = false;
                                                }
                                            });
                                            if (!$scope.flag.repeatedRow) {
                                                $scope.selectedInvoice.push(row["entity"]);
                                            }
                                        } else {
                                            $scope.selectedInvoice.push(row["entity"]);
                                        }
                                        if ($scope.selectedInvoice.length > 0) {
                                            $scope.flag.rowSelectedflag = true;
                                        } else {
                                            $scope.flag.rowSelectedflag = false;
                                        }
                                    });
                                    gridApi.selection.on.rowSelectionChangedBatch($scope, function(rows) {
                                        if ($scope.selectedInvoice.length > 0) {
                                            angular.forEach(rows, function(row) {
                                                $.each($scope.selectedInvoice, function(index, result) {
                                                    if (result["$$hashKey"] === row["entity"]["$$hashKey"]) {
                                                        //Remove from array
                                                        $scope.flag.repeatedRow = true;
                                                        $scope.selectedInvoice = [];
                                                        return false;
                                                    } else {
                                                        $scope.flag.repeatedRow = false;
                                                    }
                                                });
                                                if (!$scope.flag.repeatedRow) {
                                                    $scope.selectedInvoice.push(row["entity"]);
                                                }
                                            });
                                        } else {
                                            angular.forEach(rows, function(row) {
                                                $scope.selectedInvoice.push(row["entity"]);
                                            });
                                        }
                                        if ($scope.selectedInvoice.length > 0) {
                                            $scope.flag.rowSelectedflag = true;
                                        } else {
                                            $scope.flag.rowSelectedflag = false;
                                        }
                                    });
                                };
                                $scope.invoiceTemplate = section['genralSection'];
                                $scope.searchedDataFromDb = angular.copy(res);
                                var success = function(result)
                                {
                                    $scope.searchedData = angular.copy(result);
                                    $scope.searchedDataFromDbForUiGrid = [];
                                    $scope.mapToArray = [];
                                    angular.forEach($scope.searchedData, function(itr) {
///////////////////////////////////////////UI-GRID/////////////////////////////////////////////////////
                                        angular.forEach($scope.invoiceLabelListForUiGrid, function(list) {
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
                                        $scope.searchedDataFromDbForUiGrid.push(itr.categoryCustom);
                                    });
                                    $scope.gridOptions.data = $scope.searchedDataFromDbForUiGrid;
                                    $scope.gridOptions.columnDefs = $scope.invoiceLabelListForUiGrid;
                                    window.setTimeout(function() {
                                        $(window).resize();
                                        $(window).resize();
                                    }, 100);
                                    //////////////////
////////////////////////////UI-GRID///////////////////////////////////////////////
                                    $scope.listFilled = true;
                                    $rootScope.unMaskLoading();
//                                    $scope.onCanel();
                                }
                                DynamicFormService.convertorForCustomField(res, success);
                            }
                            , function(reason) {

                            }, function(update) {

                            });
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
                    }
                }
                else {
                    var msg = "Please select atleast one search criteria for search";
                    var type = $rootScope.warning;
                    $rootScope.addMessage(msg, type);
                }
            };
            $scope.editInvoice = function() {
                $scope.parcelAddShow = false;
                var invoiceObj = angular.copy($scope.selectedInvoice[0]);
                $scope.invoiceId = angular.copy(invoiceObj.$$$value);
                $scope.invoiceIdForConstraint = invoiceObj.$$$value;
                if (invoiceObj != null) {
                    $rootScope.maskLoading();
                    angular.forEach($scope.searchedDataFromDb, function(itr) {
                        if (itr.value === invoiceObj.$$$value) {
//                            $scope.parentEntityValue = angular.copy(itr.categoryCustom);
                            invoiceObj = angular.copy(itr.categoryCustom);
                        }
                    });
                    $scope.flag.showUpdatePage = true;
                    CustomFieldService.retrieveDesignationBasedFields("invoiceEdit", function(response) {
                        $scope.response = angular.copy(response);
                        $scope.invoiceDataBean = {};
                        $scope.invoiceCustom = DynamicFormService.resetSection($scope.updateInvoiceTemplate);
                        var templateDataForUpdate = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                        if (!($scope.dbTypeForUpdate != null))
                            $scope.dbTypeForUpdate = {};
                        templateDataForUpdate.then(function(section) {
                            $scope.updateInvoiceTemplate = null;
                            $scope.updateInvoiceTemplate = section['genralSection'];
                            $scope.listOfModelsOfDateType = [];
                            if ($scope.updateInvoiceTemplate !== null && $scope.updateInvoiceTemplate !== undefined)
                            {
                                angular.forEach($scope.updateInvoiceTemplate, function(updateTemplate)
                                {
                                    if (updateTemplate.type !== null && updateTemplate.type !== undefined && updateTemplate.type === 'date')
                                    {
                                        $scope.listOfModelsOfDateType.push(updateTemplate.model);
                                    }
                                })
                            }
                            var invoiceField = [];
                            var result = Object.keys(response).map(function(key, value) {
                                angular.forEach(this[key], function(itr) {
                                    if (key === 'Invoice') {
                                        invoiceField.push({Invoice: itr});
                                    }
                                });
                            }, response);
                            $scope.editFieldName = [];
                            $scope.updateInvoiceTemplate = DynamicFormService.retrieveCustomData($scope.updateInvoiceTemplate, invoiceField);
                            angular.forEach($scope.updateInvoiceTemplate, function(itr) {
                                if (itr.fromModel) {
                                    if ($scope.editFieldName.indexOf(itr.fromModel) === -1)
                                        $scope.editFieldName.push(itr.fromModel);
                                } else if (itr.toModel) {
                                    if ($scope.editFieldName.indexOf(itr.toModel) === -1)
                                        $scope.editFieldName.push(itr.toModel);

                                } else if (itr.model) {
                                    if ($scope.editFieldName.indexOf(itr.model) === -1)
                                        $scope.editFieldName.push(itr.model);
                                }
                            });
                            var templateDataForParent = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                            $scope.dbTypeForUpdateParent = {};
                            templateDataForParent.then(function(section) {
                                $scope.updateInvoiceParentTemplate = null;
                                $scope.updateInvoiceParentTemplate = section['genralSection'];
                                var utem = angular.copy(section['genralSection']);
                                var invoiceFieldUpdate = [];
                                var result = Object.keys(response).map(function(key, value) {
                                    angular.forEach(this[key], function(itr) {
                                        if (key === 'Invoice#P#') {
                                            invoiceFieldUpdate.push({Invoice: itr});
                                        }
                                    });
                                }, response);
                                $scope.updateInvoiceParentTemplate = DynamicFormService.retrieveCustomData($scope.updateInvoiceParentTemplate, invoiceFieldUpdate);
                                $scope.parcelAddShow = true;
                                angular.forEach($scope.updateInvoiceParentTemplate, function(itr) {
                                    if (itr.fromModel) {
                                        if ($scope.editFieldName.indexOf(itr.fromModel) === -1)
                                            $scope.editFieldName.push(itr.fromModel);
                                    } else if (itr.toModel) {
                                        if ($scope.editFieldName.indexOf(itr.toModel) === -1)
                                            $scope.editFieldName.push(itr.toModel);

                                    } else if (itr.model) {
                                        if ($scope.editFieldName.indexOf(itr.model) === -1)
                                            $scope.editFieldName.push(itr.model);
                                    }
                                });
                                $scope.flag.customFieldGeneratedForUpdate = true;

                            }, function(reason) {

                            }, function(update) {
                            });
                            var map = {};
                            var ids = [];
                            ids.push($scope.invoiceId)
                            map['invoiceObjectId'] = ids;
                            map['dbFieldNames'] = $scope.editFieldName;
//                            console.log(JSON.stringify($scope.editFieldName));
                            InvoiceService.retrieveInvoice(map, function(res) {
//                                console.log(JSON.stringify(res.categoryCustom));
                                $scope.invoiceEditShow = true;
                                $scope.flag.customFieldGeneratedForUpdate = true;
                                if (res !== undefined) {
                                    $scope.invoiceCustom = angular.copy(res.categoryCustom);
                                    if (!!$scope.invoiceCustom) {
                                        angular.forEach($scope.listOfModelsOfDateType, function(listOfModel)
                                        {
                                            if ($scope.invoiceCustom.hasOwnProperty(listOfModel))
                                            {
                                                if ($scope.invoiceCustom[listOfModel] !== null && $scope.invoiceCustom[listOfModel] !== undefined)
                                                {
                                                    $scope.invoiceCustom[listOfModel] = new Date($scope.invoiceCustom[listOfModel]);
                                                } else
                                                {
                                                    $scope.invoiceCustom[listOfModel] = '';
                                                }
                                            }
                                        })
                                    }
//                                    console.log("Invoice custommmm" + JSON.stringify($scope.invoiceCustom))
                                    $scope.invoiceCustomForUpdate = angular.copy(res.categoryCustom);
//                                    console.log("invoice Aftreeeeeeee" + JSON.stringify($scope.invoiceCustomForUpdate));
                                    $scope.parentEntityValue = angular.copy(res.categoryCustom);
                                }
                                $scope.invoiceDataBean.id = angular.copy($scope.invoiceId);
                                $scope.invoiceDataBean.invoiceDbType = angular.copy($scope.dbTypeForUpdate);
                                $rootScope.unMaskLoading();
                            }
                            , function() {
                                var msg = "Could not save, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                                $rootScope.unMaskLoading();
                            });

                        }, function(reason) {

                        }, function(update) {
                        });
                        //For invoice Parent
//                        $scope.invoiceCustomForUpdate = DynamicFormService.resetSection($scope.updateInvoiceParentTemplate);

                    }, function() {
                        $rootScope.unMaskLoading();
                        var msg = "Failed to retrieve data";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });


                }
            };
            $scope.reset = function(sectionTobeReset) {
                if (sectionTobeReset === "searchCustom") {
                    $scope.searchCustom = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("invoiceEdit");
                    templateData.then(function(section) {
                        $scope.generalInvoiceTemplate = section['genralSection'];
                        $scope.searchResetFlag = true;
                        $rootScope.unMaskLoading();
                        $scope.flag.customFieldGenerated = true;
                    }, function(reason) {
                    }, function(update) {
                    });
                } else if (sectionTobeReset === "updateInvoiceCustom") {
                    $scope.invoiceCustom = {};
                    var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                    templateData.then(function(section) {
                        $scope.updateInvoiceTemplate = angular.copy(section['genralSection']);
                        var invoiceFieldUpdate = [];
                        var result = Object.keys($scope.response).map(function(key, value) {
                            angular.forEach(this[key], function(itr) {
                                if (key === 'Invoice#P#') {
                                    invoiceFieldUpdate.push({Invoice: itr});
                                }
                            });
                        }, $scope.response);
                        $scope.updateInvoiceTemplate = [];
                        $scope.updateInvoiceTemplate = angular.copy(section['genralSection']);
                        $scope.updateInvoiceTemplate = DynamicFormService.retrieveCustomData($scope.updateInvoiceTemplate, invoiceFieldUpdate);
                        $scope.invoiceEditShow = true;
                        $rootScope.unMaskLoading();
                        $scope.flag.customFieldGenerated = true;
//                        $scope.invoiceDataBean.invoiceDbType = angular.copy($scope.dbTypeForUpdate);
                    }, function(reason) {
                    }, function(update) {
                    });
                }
            };
            $scope.onCanel = function() {
                if ($scope.updateInvoiceForm != null) {
                    $scope.updateInvoiceForm.$dirty = false;
                }
//                $scope.invoiceCustom = DynamicFormService.resetSection($scope.generaInvoiceTemplate);
                $scope.flag.showUpdatePage = false;
//                $scope.searchResetFlag = false;
                $scope.invoiceEditShow = false;
                $scope.flag.showUpdatePage = false;
                $scope.selectedInvoice = [];
                $scope.reset("updateInvoiceCustom");
            };
            $scope.onCanelOfSearch = function() {
//                $scope.invoiceCustom = DynamicFormService.resetSection($scope.generaInvoiceTemplate);
                if ($scope.editInvoiceForm != null) {
                    $scope.editInvoiceForm.$dirty = false;
                }
                $scope.searchedData = [];
                $scope.listFilled = false;
                $scope.submitted = false;
                $scope.flag.showUpdatePage = false;
                $scope.searchResetFlag = false;
                $scope.selectedInvoice = [];
                $scope.gridOptions = {};
                $scope.reset("searchCustom");
            };
            $scope.updateInvoice = function(updateInvoiceForm) {
                angular.forEach($scope.listOfModelsOfDateType, function(listOfModel)
                {
                    if ($scope.invoiceCustom.hasOwnProperty(listOfModel))
                    {
                        if ($scope.invoiceCustom[listOfModel] !== null && $scope.invoiceCustom[listOfModel] !== undefined)
                        {
                            $scope.invoiceCustom[listOfModel] = new Date($scope.invoiceCustom[listOfModel]);
                        } else
                        {
                            $scope.invoiceCustom[listOfModel] = '';
                        }
                    }
                });
                $scope.submitted = true;
                $scope.invoiceDataBean.invoiceCustom = $scope.invoiceCustom;
                $scope.invoiceDataBean.id = $scope.invoiceId;
                $scope.invoiceDataBean.invoiceDbType = $scope.dbTypeForUpdate;
                $scope.invoiceDataBean.isArchive = false;
                if (updateInvoiceForm.$valid) {
                    $rootScope.maskLoading();
                    InvoiceService.update($scope.invoiceDataBean, function(response) {
                        if ($scope.editInvoiceForm != null) {
                            $scope.editInvoiceForm.$dirty = false;
                        }
                        $scope.onCanelOfSearch();
                    }, function() {
                        $rootScope.unMaskLoading();
                        var msg = "Could not update invoice, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                }
            };

            $scope.showPopUp = function(lotObj) {
                $("#deleteInvoiceDialog").modal('show');
            };
            $scope.hidePopUp = function() {
                $("#deleteInvoiceDialog").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();

            };
            $scope.deleteInvoice = function(updateInvoiceForm) {
                $scope.invoiceDataBean.id = $scope.invoiceId;
                $scope.invoiceDataBean.isArchive = true;
//                $scope.submitted = true;
//                if (updateInvoiceForm.$valid) {
                $rootScope.maskLoading();
                $("#deleteInvoiceDialog").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                InvoiceService.update($scope.invoiceDataBean, function(response) {
                    if ($scope.editInvoiceForm != null) {
                        $scope.editInvoiceForm.$dirty = false;
                    }
                    $scope.onCanelOfSearch();
                }, function() {
                    $rootScope.unMaskLoading();
                    var msg = "Could not update invoice, please try again.";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                });
//                }
            };
            ;
        }]);
});
