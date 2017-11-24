define(['hkg', 'parcelService', 'customFieldService', 'invoiceService', 'parcelmanagement/edit-parcel.controller', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'printBarcodeValue', 'dynamicForm', 'accordionCollapse'], function(hkg, parcelService, customFieldService, invoiceService) {
    hkg.register.controller('AddParcelController', ["$rootScope", "$scope", "ParcelService", "$timeout", "$filter", "$location", "$window", "DynamicFormService", "CustomFieldService", "InvoiceService", function($rootScope, $scope, ParcelService, $timeout, $filter, $location, $window, DynamicFormService, CustomFieldService, InvoiceService) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "addParcel";
            $rootScope.activateMenu();
            $scope.AddParcel = this;
            $scope.flag = {};
            $scope.flag.searchFieldNotAvailable = false;
            $scope.remCaratMsg = "";
            $scope.fieldsToClear = ['carat_of_parcel$NM$Double', 'no_of_pieces_of_parcel$NM$Long'];
            $rootScope.maskLoading();
//            ParcelService.fieldSequencExist("parcelID$AG$String", function(response) {
            //////////////////////ui-grid////////////////////////////////////////////
//                if (response.data) {
            $scope.invoiceLabelListForUiGrid = [];
            $scope.searchedDataFromDbForUiGrid = [];
            $scope.parcelDataForUiGrid = [];
            $scope.parcelListForUIGrid = [];
//////////////////////ui-grid/////////////////////////////////////
            $scope.parcelDataBean = {};
            $scope.lotListTodisplay = [];
            $scope.modifiedListToDisplay = [];
            $scope.searchCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);
            $scope.parcelDataBean.featureCustomMap = {};
            var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("parcelAdd");
            if ($scope.parcelDbType != null) {
            } else {
                $scope.parcelDbType = {};
            }
            var featureMap = {};
            $scope.modelAndHeaderList = [];
            $scope.modelAndHeaderListForLot = [];
            templateData.then(function(section) {
                $scope.generalSearchTemplate = section['genralSection'];
                if ($scope.generalSearchTemplate != null && $scope.generalSearchTemplate.length > 0) {
                    for (var i = 0; i < $scope.generalSearchTemplate.length; i++) {
                        var item = $scope.generalSearchTemplate [i];
                        featureMap[item.model] = item.featureName;
                        $scope.modelAndHeaderList.push({model: item.model, header: item.label});
                    }
                } else {
                    $scope.flag.searchFieldNotAvailable = true;
                }
                $scope.generalTemplate = angular.copy(section['genralSection']);
                $scope.invoiceLabelList = [];
                $scope.dbFieldNames = [];
                angular.forEach($scope.generalTemplate, function(itr) {
                    $scope.invoiceLabelList.push({model: itr.model, label: itr.label});
                    if (itr.fromModel) {
                        $scope.dbFieldNames.push(itr.fromModel);
                        $scope.invoiceLabelListForUiGrid.push({name: itr.fromModel, displayName: itr.label, minWidth: 200});
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
                $rootScope.unMaskLoading();
            }, function(reason) {

            }, function(update) {

            });
            $scope.initAddParcelForm = function(addParcelForm) {
                $scope.addParcelForm = addParcelForm;
            };
            $scope.retrieveSearchedData = function(addParcelForm) {
                $scope.selectOneParameter = false;
                $scope.gridOptionsForInvoice = {};
                $scope.searchedData = [];
                $scope.searchedDataFromDbForUiGrid = [];
                $scope.listFilled = false;
                if (Object.getOwnPropertyNames($scope.searchCustom).length > 0 || $rootScope.createdInvoiceId) {
                    $scope.searchedDataFromDbForUiGrid = [];
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
                    if (mapHasValue || $rootScope.createdInvoiceId) {
                        $scope.gridOptionsForInvoice.enableFiltering = true;
                        $scope.gridOptionsForInvoice.data = [];
                        $scope.gridOptionsForInvoice.multiSelect = false;
                        $scope.gridOptionsForInvoice.enableRowSelection = true;
                        $scope.gridOptionsForInvoice.enableSelectAll = false;
                        $scope.selectedInvoice = [];
                        $scope.gridOptionsForInvoice.onRegisterApi = function(gridApi) {
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
                        $scope.searchedDataFromDbForUiGrid = [];
                        $scope.parcelDataBean = {};
                        $scope.parcelDataBean.featureCustomMap = {};
                        angular.forEach($scope.searchCustom, function(value, key) {
                            if (value != null) {
                                angular.forEach(featureMap, function(val, label) {
                                    if (key === label) {
                                        if (!$scope.parcelDataBean.featureCustomMap[val])
                                            $scope.parcelDataBean.featureCustomMap[val] = [];


                                        $scope.parcelDataBean.featureCustomMap[val].push(key + ":" + value);

                                    } else {
                                        if (key === 'to' + label) {
                                            if (!$scope.parcelDataBean.featureCustomMap[val])
                                                $scope.parcelDataBean.featureCustomMap[val] = [];
                                            $scope.parcelDataBean.featureCustomMap[val].push(key + ":" + value);
                                        }
                                        if (key === 'from' + label) {
                                            if (!$scope.parcelDataBean.featureCustomMap[val])
                                                $scope.parcelDataBean.featureCustomMap[val] = [];
                                            $scope.parcelDataBean.featureCustomMap[val].push(key + ":" + value);
                                        }
                                    }
                                });
                            }
                        });
                        $rootScope.maskLoading();
                        $scope.invoiceDataBean = {};
                        var searchResult = DynamicFormService.convertSearchData($scope.generalSearchTemplate, null, null, null, $scope.searchCustom);
                        $scope.invoiceDataBean.invoiceCustom = angular.copy(searchResult);
                        $scope.invoiceDataBean.dbFieldNames = angular.copy($scope.dbFieldNames);
                        $scope.invoiceDataBean.searchOnParameter = true;
                        $scope.invoiceDataBean.id = null;
                        if ($rootScope.createdInvoiceId) {
                            $scope.invoiceDataBean.searchOnParameter = false;
                            $scope.invoiceDataBean.id = angular.copy($rootScope.createdInvoiceId);
                            $rootScope.createdInvoiceId = undefined;
                        }
                        console.log($scope.invoiceDataBean.id);
                        console.log($scope.invoiceDataBean.searchOnParameter);
                        console.log($scope.invoiceDataBean.dbFieldNames);
//                                if ($scope.db) {
                        InvoiceService.search($scope.invoiceDataBean, function(res) {
                            $scope.searchedDataFromDb = angular.copy(res);
                            console.log("res:" + JSON.stringify(res[0]));
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
                                        itr.categoryCustom["value"] = itr.value;
                                    });
                                    $scope.searchedDataFromDbForUiGrid.push(itr.categoryCustom);
                                });
                                $scope.gridOptionsForInvoice.data = $scope.searchedDataFromDbForUiGrid;
                                $scope.gridOptionsForInvoice.columnDefs = $scope.invoiceLabelListForUiGrid;
                                window.setTimeout(function() {
                                    $(window).resize();
                                    $(window).resize();
                                }, 100);
                                //////////////////
////////////////////////////UI-GRID///////////////////////////////////////////////
                                $scope.listFilled = true;
                            }
                            DynamicFormService.convertorForCustomField(res, success);
//                            $scope.searchedData = res;
//                            $scope.listFilled = true;
//                            $scope.searchCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);
                            $rootScope.unMaskLoading();
                        }, function() {
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
            };
            if ($rootScope.createdInvoiceId !== undefined) {
                $timeout(function() {
                    $scope.retrieveSearchedData();
                }, 1000);
            }
            $scope.addParcel = function() {
                var parcelObj = angular.copy($scope.selectedInvoice[0]);
                $scope.parcelDataForUiGrid = [];
                $scope.gridOptions = {};
                $scope.gridOptions.enableFiltering = true;
//                $scope.AddParcel = this;
//                this.parcelAddShow = true;
                $scope.modelAndHeaderListForLot = [];
                $scope.lotListToSave = [];
                $scope.count = 0;
                $scope.caratOfInvoice = undefined;
                if (parcelObj != null) {
                    $scope.caratOfInvoice = parseFloat(parcelObj.carat_of_invoice$NM$Double);
                    $scope.remainingCarat = parseFloat(parcelObj.carat_of_invoice$NM$Double);
                    if ($scope.remainingCarat < 0) {
                        $scope.remCaratMsg = "Carat value limit exceeded"
                    } else {
                        $scope.remCaratMsg = "Carat remaining is " + $scope.remainingCarat;
                    }
                    if ($scope.caratOfInvoice !== undefined && $scope.caratOfInvoice !== null && $scope.caratOfInvoice.toString() !== 'NaN') {
                        $scope.caratDoesNotMatch = false;
                    } else {
                        $scope.caratDoesNotMatch = true;
                    }
                    console.log(parcelObj);
                    console.log($scope.caratOfInvoice);
                    $scope.invoiceId = parcelObj.value;
                    $scope.invoiceIdForConstraint = parcelObj.value;
//                            if ($scope.searchedDataFromDb != null && $scope.searchedDataFromDb.length > 0) {
//                                for (var itr = 0; itr < $scope.searchedDataFromDb.length; itr++) {
//                                    if ($scope.searchedDataFromDb[itr].value === parcelObj.value) {
////                                        $scope.parentInvoiceData = angular.copy($scope.searchedDataFromDb[itr].categoryCustom);
//                                        break;
//                                    }
//                                }
//                            }
                    $scope.flag.showAddPage = true;
                    $rootScope.maskLoading();
                    $scope.parcelDataBean = {invoiceDataBean: {id: $scope.invoiceId, invoiceCustom: '', invoiceDbType: ''}};
                    CustomFieldService.retrieveDesignationBasedFields("parcelAdd", function(response) {
                        $scope.response = angular.copy(response);
                        this.parcelAddShow = false;
                        $scope.reset("categoryCustom", false);
                        var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                        $scope.invoiceDbType = {};
                        $scope.invoiceDbFieldName = [];
                        templateData.then(function(section) {
                            $scope.generaInvoiceTemplate = section['genralSection'];
                            var invoiceField = [];
                            var result = Object.keys(response).map(function(key, value) {
                                angular.forEach(this[key], function(itr) {
                                    if (key === 'Invoice#P#') {
                                        invoiceField.push({Invoice: itr});
                                    }
                                });
                            }, response);
                            $scope.generaInvoiceTemplate = DynamicFormService.retrieveCustomData($scope.generaInvoiceTemplate, invoiceField);
                            angular.forEach($scope.generaInvoiceTemplate, function(itr) {
                                if (itr.fromModel) {
                                    if ($scope.invoiceDbFieldName.indexOf(itr.fromModel) === -1)
                                        $scope.invoiceDbFieldName.push(itr.fromModel);
                                } else if (itr.toModel) {
                                    if ($scope.invoiceDbFieldName.indexOf(itr.toModel) === -1)
                                        $scope.invoiceDbFieldName.push(itr.tiModel);
                                } else if (itr.model) {
                                    if ($scope.invoiceDbFieldName.indexOf(itr.model) === -1)
                                        $scope.invoiceDbFieldName.push(itr.model);
                                }
                            });
                        }, function(reason) {

                        }, function(update) {

                        });
                        $scope.parcelDbFieldName = [];
                        $scope.categoryCustom = DynamicFormService.resetSection($scope.templateToSent);
                        var templateDataParcel = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
                        templateDataParcel.then(function(section) {
                            $scope.generalParcelTemplate = section['genralSection'];
                            var parcelField = [];
                            var result = Object.keys(response).map(function(key, value) {
                                angular.forEach(this[key], function(itr) {
                                    if (key === 'Parcel') {
                                        parcelField.push({Parcel: itr});
                                    }
                                });
                            }, response);
                            $scope.generalParcelTemplate = DynamicFormService.retrieveCustomData($scope.generalParcelTemplate, parcelField);
                            var fieldsInTemplate = [];
                            $scope.fieldNotConfigured = false;
                            $scope.mandatoryFields = ['carat_of_parcel$NM$Double', 'parcelNumber$TF$String', 'no_of_pieces_of_parcel$NM$Long'];
                            $scope.flag.customFieldGenerated = true;
                            angular.forEach($scope.generalParcelTemplate, function(itr) {
                                if (itr.model === 'carat_of_parcel$NM$Double') {
                                    itr.required = true;
                                } else if (itr.model === 'parcelNumber$TF$String') {
                                    itr.required = true;
                                } else if (itr.model === 'no_of_pieces_of_parcel$NM$Long') {
                                    itr.required = true;
                                }
                                fieldsInTemplate.push(angular.copy(itr.model));
                                $scope.modelAndHeaderListForLot.push({model: itr.model, header: itr.label});
                                if (itr.fromModel) {
                                    if ($scope.parcelDbFieldName.indexOf(itr.fromModel) === -1)
                                        $scope.parcelDbFieldName.push(itr.fromModel);
                                    $scope.parcelListForUIGrid.push({name: itr.fromModel, displayName: itr.label, minWidth: 200});
                                } else if (itr.toModel) {
                                    if ($scope.parcelDbFieldName.indexOf(itr.toModel) === -1)
                                        $scope.parcelDbFieldName.push(itr.toModel);
                                    $scope.parcelListForUIGrid.push({name: itr.toModel, displayName: itr.label, minWidth: 200});
                                } else if (itr.model) {
                                    if ($scope.parcelDbFieldName.indexOf(itr.model) === -1)
                                        $scope.parcelDbFieldName.push(itr.model);
                                    $scope.parcelListForUIGrid.push({name: itr.model, displayName: itr.label, minWidth: 200});
                                }
                            });
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
                            if (!$scope.fieldNotConfigured) {
                                $scope.parcelDataBean.parcelDbType = angular.copy($scope.parcelDbType);
                                console.log($scope.parcelDbFieldName + "---------" + parcelObj.value);
                                var mapToSent = {};
                                var ids = [];
                                ids.push(parcelObj.value);
                                mapToSent['invoiceObjectId'] = ids;
                                mapToSent['parcelDbFieldName'] = $scope.parcelDbFieldName;
                                mapToSent['invoiceDbFieldName'] = $scope.invoiceDbFieldName;
//                                console.log(mapToSent);
                                ParcelService.retrieveParcelByInvoiceId(mapToSent, function(response) {
                                    console.log(JSON.stringify(response));
                                    $scope.flag.parcelsAlreadyCreated = false;
                                    if (response !== undefined) {
                                        if (response.length > 1) {
//                                            $scope.flag.parcelsAlreadyCreated = true;
                                        }
                                        if (response[0] !== undefined) {
                                            console.log("flag set from here");
                                            if (response[0].custom1 !== null && response[0].custom1 !== undefined) {
                                                $scope.invoiceCustom = angular.copy(response[0].custom1);
                                            } else {
                                                $scope.invoiceCustom = {};
                                            }
                                        } else {
                                            $scope.invoiceCustom = {};
                                        }
                                        $scope.lotInDb = angular.copy(response);

                                    }
//                            console.log("response" + JSON.stringify(response));
//--------------------------------------------------------------------------------------------------------------------------------
                                    var sucTemp = function(result) {
                                        angular.forEach(result, function(itr) {
                                            if (itr.categoryCustom !== undefined && itr.categoryCustom !== null) {
                                                itr.categoryCustom['$$objectId'] = itr.value;
                                                angular.forEach($scope.parcelListForUIGrid, function(list) {
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
                                                $scope.parcelDataForUiGrid.push(itr.categoryCustom);
                                            }
                                        });
                                        $scope.gridOptions.data = $scope.parcelDataForUiGrid;
                                        $scope.gridOptions.columnDefs = $scope.parcelListForUIGrid;
                                        $scope.gridOptions.columnDefs.push({name: 'Action',
                                            cellTemplate: '<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.updateLot(row.entity)">Update</i></a>', enableFiltering: false, minWidth: 200});
                                        $scope.countRemainingCarat();
                                    }
                                    DynamicFormService.convertorForCustomField(response, sucTemp);
                                    window.setTimeout(function() {
                                        $(window).resize();
                                        $(window).resize();
                                    }, 100);
//--------------------------------------------------------------------------------------------------------------------------------
//                                var success = function(result)
//                                {
//                                    if (result != null && result.length > 0) {
//                                        angular.forEach(result, function(res)
//                                        {
//                                            $scope.modifiedListToDisplay.push(angular.copy({id: $scope.count, category: res}));
//                                            $scope.count++;
//                                        })
//
//
//
//                                    }
//                                }
//                                DynamicFormService.getValuesOfComponentFromIdTemp(response, $scope.generalParcelTemplate, success);
//                                angular.forEach($scope.modifiedListToDisplay, function(itr) {
//                                    itr.category = {};
//                                    itr.category.categoryCustom = itr.categoryCustom;
//                                });
                                    if ($scope.flag.parcelsAlreadyCreated) {
                                        var msg = "Parcel(s) already created for current invoice";
                                        var type = $rootScope.error;
                                        $rootScope.addMessage(msg, type);

                                    }
                                    $scope.listFilled = true;
                                }, function() {
                                    $rootScope.unMaskLoading();
                                    var msg = "Could not retrieve parcel, please try again.";
                                    var type = $rootScope.error;
                                    $rootScope.addMessage(msg, type);
                                });
                                $rootScope.unMaskLoading();

                            } else {
                                $rootScope.unMaskLoading();
                            }
                        }, function(reason) {

                        }, function(update) {

                        });
                    }, function() {
                        $rootScope.unMaskLoading();
                        var msg = "Failed to retrieve data";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                }
            };
            $scope.reset = function(sectionTobeReset, isCreate) {
                if (sectionTobeReset === "searchCustom") {
                    $scope.searchCustom = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("parcelAdd");
                    $scope.dbType = {};
                    templateData.then(function(section) {
                        $scope.generalSearchTemplate = section['genralSection'];
                        $scope.searchResetFlag = true;
                        $rootScope.unMaskLoading();
                        $scope.flag.customFieldGenerated = true;
                    }, function(reason) {
                    }, function(update) {
                    });
                } else if (sectionTobeReset === "categoryCustom") {
                    if (!isCreate) {
                        $scope.categoryCustom = {};
                    } else {
                        angular.forEach($scope.fieldsToClear, function(item) {
                            delete $scope.categoryCustom[item];
                        });
                    }
                    var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
//                            $scope.parcelDbType = {};
                    templateData.then(function(section) {
                        var parcelField = [];
                        var result = Object.keys($scope.response).map(function(key, value) {
                            angular.forEach(this[key], function(itr) {
                                if (key !== 'Parcel#P#') {
                                    parcelField.push({Parcel: itr});
                                }
                            });
                        }, $scope.response);
                        $scope.generalParcelTemplate = section['genralSection'];
                        $scope.generalParcelTemplate = DynamicFormService.retrieveCustomData($scope.generalParcelTemplate, parcelField);
                        $timeout(function() {
                            // Controller As
                            console.log("categoryCustom::::::::;" + JSON.stringify($scope.categoryCustom))
                            $scope.AddParcel = this;
                            this.parcelAddShow = true;
                        }, 100);
                        $rootScope.unMaskLoading();
                        $scope.flag.customFieldGenerated = true;
                        $scope.parcelDataBean.parcelDbType = $scope.parcelDbType;
                    }, function(reason) {
                    }, function(update) {
                    });
                }
            };
            $scope.resetAddForm = function(isCreate) {
                if ($scope.addParcelForm != null) {
                    $scope.addParcelForm.$dirty = false;
                }
//                $scope.categoryCustom = DynamicFormService.resetSection($scope.generalLotTemplate);
                $scope.AddParcel = this;
                this.parcelAddShow = false;
                $scope.flag.editLot = false;
                $scope.reset("categoryCustom", isCreate);
            };

            $scope.onCanelOfSearch = function(addParcelForm) {
                if ($scope.addParcelForm != null) {
                    $scope.addParcelForm.$dirty = false;
                }
                $scope.searchedData = [];
                $scope.searchResetFlag = false;
                $scope.listFilled = false;
                $scope.selectedInvoice = [];
                $scope.gridOptionsForInvoice = {};
                $scope.reset("searchCustom");

            };
            $scope.createParcel = function(addParcelForm) {
                $scope.parcelDbTypeToSent = angular.copy($scope.parcelDbType);
                $scope.submitted = true;
//                        if (Object.getOwnPropertyNames($scope.categoryCustom).length > 0) {
//                            var mapHasValue = false;
//                            for (var prop in $scope.categoryCustom) {
//                                if (typeof $scope.categoryCustom[prop] === 'object' && $scope.categoryCustom[prop] != null) {
//                                    var toString = angular.copy($scope.categoryCustom[prop].toString());
//                                    if (typeof toString === 'string' && toString !== null && toString !== undefined && toString.length > 0) {
//                                        mapHasValue = true;
//                                        break;
//                                    }
//                                }
//                                if (typeof $scope.categoryCustom[prop] === 'string' && $scope.categoryCustom[prop] !== null && $scope.categoryCustom[prop] !== undefined && $scope.categoryCustom[prop].length > 0) {
//                                    mapHasValue = true;
//                                    break;
//                                }
//                                if (typeof $scope.categoryCustom[prop] === 'number' && !!($scope.categoryCustom[prop])) {
//                                    mapHasValue = true;
//                                    break;
//                                }
//                                if (typeof $scope.categoryCustom[prop] === 'boolean') {
//                                    mapHasValue = true;
//                                    break;
//                                }
//                            }
                if (addParcelForm.$valid) {
                    $scope.submitted = false;
                    var sectionData = [];
                    sectionData.push({categoryCustom: angular.copy($scope.categoryCustom)});
                    $scope.count++;
                    $scope.lotListToSave.push(angular.copy({$$objectId: $scope.count, categoryCustom: $scope.categoryCustom}));
//save to db                    
                    $scope.caratDoesNotMatch = false;
                    var parcelList = [];
                    var parcelDataBean = {};
                    var totalCaratFromUI = undefined;
                    angular.forEach($scope.lotListToSave, function(itr) {
                        if (itr.category !== null && itr.category !== undefined) {
//                        parcelList.push({parcelCustom: itr.category.categoryCustom, invoiceDataBean: {id: $scope.invoiceId}, parcelDbType: $scope.parcelDbType});
                        } else {
                            if (!$scope.caratDoesNotMatch && itr.categoryCustom['carat_of_parcel$NM$Double'] !== undefined && itr.categoryCustom['carat_of_parcel$NM$Double'] !== null) {
                                if (totalCaratFromUI !== undefined) {
                                    totalCaratFromUI = totalCaratFromUI + parseFloat(itr.categoryCustom['carat_of_parcel$NM$Double']);
                                } else {
                                    totalCaratFromUI = parseFloat(itr.categoryCustom['carat_of_parcel$NM$Double']);
                                }
                            } else {
                                $scope.caratDoesNotMatch = true;
                            }
                            parcelList.push({parcelCustom: itr.categoryCustom, invoiceDataBean: {id: $scope.invoiceId}, parcelDbType: $scope.parcelDbTypeToSent});
                        }
                    });
                    if (!$scope.caratDoesNotMatch) {
                        if ($scope.caratOfInvoice !== totalCaratFromUI) {
                            $scope.caratDoesNotMatch = true;
                        }
                    }
                    parcelDataBean.parcelList = parcelList;
                    parcelDataBean.invoiceDataBean = {id: $scope.invoiceId};
//                    check carat condition
//                    if (!$scope.caratDoesNotMatch) {
                    $rootScope.maskLoading();
                    $rootScope.createdParcelIds = undefined;
                    $scope.createdParcelIds = undefined;
                    ParcelService.create(parcelDataBean, function(response) {
                        console.log(JSON.stringify(response));
                        $scope.createdParcelIds = angular.copy(response.primaryKey);
                        console.log(JSON.stringify($scope.createdParcelIds));
                        $rootScope.showLotLink = true;
                        $scope.lotListToSave = [];
//                            $scope.lotListTodisplay = [];
//                            $scope.modifiedListToDisplay = [];
//                            $scope.searchedData = [];
//                            $scope.searchedDataFromDb = [];
                        $scope.listFilled = false;
                        $scope.addParcelForm.$dirty = false;
//                            $scope.flag.showAddPage = false;
//                            $scope.onCancel();
//                            $scope.onCanelOfSearch();
                        $scope.AddParcel = this;
                        this.parcelAddShow = true;
                        $timeout(function() {
                            $rootScope.showLotLink = false;
                        }, 30000);
//old code
                        $scope.listFilled = true;
                        var success = function(result)
                        {
                            result[0].categoryCustom["value"] = $scope.createdParcelIds.substring(1, $scope.createdParcelIds.length - 1);
                            result[0].categoryCustom["label"] = angular.copy($scope.invoiceIdForConstraint);
//                            $scope.lotInDb.push({categoryCustom: {parcelDbObjectId: $scope.createdParcelIds.substring(1, $scope.createdParcelIds.length - 1)}});
                            angular.forEach($scope.parcelListForUIGrid, function(list) {
                                if (!result[0].categoryCustom.hasOwnProperty(list.name)) {
                                    result[0].categoryCustom[list.name] = "NA";
                                }
                                else if (result[0].categoryCustom.hasOwnProperty(list.name)) {
                                    if (result[0].categoryCustom[list.name] === null || result[0].categoryCustom[list.name] === '' || result[0].categoryCustom[list.name] === undefined || result[0].categoryCustom[list.name] === 'undefined') {
                                        result[0].categoryCustom[list.name] = "NA";
                                    }
                                }
//                                result[0].categoryCustom["value"] = itr.value;
                            });
                            $scope.gridOptions.data.push(angular.copy(result[0].categoryCustom));
                            $scope.gridOptions.columnDefs.push({name: 'Action',
                                cellTemplate: '<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.updateLot(row.entity)">Update</i></a>', enableFiltering: false, minWidth: 200});
                            $scope.countRemainingCarat();
                        }
                        DynamicFormService.convertorForCustomField(sectionData, success);
                        $scope.resetAddForm(true);
//
                        $rootScope.unMaskLoading();
                    }, function() {
                        $rootScope.unMaskLoading();
                        var msg = "Could not create parcel, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
//                    } else {
//                        var msg = "Carat value does not match, please try again.";
//                        var type = $rootScope.error;
//                        $rootScope.addMessage(msg, type);
//                    }
//


                }
//                        }
            };
            $scope.editParcelLocally = function(lotObj, index) {
                $scope.index = lotObj.$$objectId;
//                lotObj.id=index;
                $scope.oldObj = angular.copy(lotObj, $scope.oldObj);
                if (lotObj != null) {
                    $scope.AddParcel = this;
                    this.parcelAddShow = false;
                    $scope.flag.editLot = true;
                    $scope.lotDataBean = {};
                    if (!!($scope.lotListToSave && $scope.lotListToSave.length > 0)) {
                        for (var i = 0; i < $scope.lotListToSave.length; i++) {
                            if (lotObj.$$objectId != null) {
                                if ($scope.lotListToSave[i].$$objectId === lotObj.$$objectId) {
                                    $scope.categoryCustom = angular.copy($scope.lotListToSave[i].categoryCustom);
                                    $scope.lotDataBean.lotCustom = $scope.categoryCustom;
                                    break;
                                }
                            } else {
                                if ($scope.lotListToSave[i].id === lotObj.id) {
                                    $scope.categoryCustom = angular.copy($scope.lotListToSave[i].categoryCustom);
                                    $scope.lotDataBean.lotCustom = $scope.categoryCustom;
                                    break;
                                }
                            }
                        }
                    }
                    $scope.lotDataBean.lotDbType = $scope.lotDbType;
                }
                $timeout(function() {
                    // Controller As
                    $scope.AddParcel = this;
                    this.parcelAddShow = true;
                }, 100);
            };
            $scope.saveLot = function(addParcelForm) {

                $scope.submitted = true;
//                        if (Object.getOwnPropertyNames($scope.categoryCustom).length > 0) {
//                            var mapHasValue = false;
//                            for (var prop in $scope.categoryCustom) {
//                                if (!!($scope.categoryCustom[prop])) {
//                                    mapHasValue = true;
//                                    break;
//                                }
//                            }

                if (addParcelForm.$valid) {
//                        $scope.submitted = false;
//                        var id = $scope.oldObj.id;
//                        alert("Id 6"+id);
//                        angular.forEach($scope.modifiedListToDisplay, function (itr) {
//                            if (itr.value === id) {
//                                var j = $scope.modifiedListToDisplay.indexOf(itr);
//                                alert("j"+j);
//                                if (j !== -1) {
//                                    $scope.modifiedListToDisplay.splice(j, 1);
//                                }
//                            }
//                        });
//                        angular.forEach($scope.lotListToSave, function (itr) {
//                            if (itr.value === id) {
//                                var i = $scope.lotListToSave.indexOf(itr);
//                                if (i !== -1) {
//                                    $scope.lotListToSave.splice(i, 1);
//                                }
//                            }
//                        });
                    var sectionData = [];
                    sectionData.push({categoryCustom: angular.copy($scope.categoryCustom)});
//                    $scope.count++;
//                        $scope.lotListToSave.push(angular.copy({id: $scope.count, categoryCustom: $scope.categoryCustom}));
                    angular.forEach($scope.lotListToSave, function(itr) {
                        if (itr.$$objectId === $scope.index) {
                            itr.categoryCustom = ($scope.categoryCustom);
                        }
                    });
//                    $scope.lotListToSave[$scope.index] = {parcelID: $scope.index, categoryCustom: $scope.categoryCustom};
//                        var data = DynamicFormService.getValuesOfComponentFromId(sectionData, $scope.generalParcelTemplate);
//                       
//                        $scope.lotListTodisplay.push(angular.copy({id: $scope.count, category: data[0]}));
                    var success = function(result)
                    {
                        var tempParcel = angular.copy(result[0].categoryCustom);
                        if (tempParcel !== undefined && tempParcel !== null) {
                            angular.forEach($scope.parcelListForUIGrid, function(list) {
                                if (!tempParcel.hasOwnProperty(list.name)) {
                                    tempParcel[list.name] = "NA";
                                }
                                else if (tempParcel.hasOwnProperty(list.name)) {
                                    console.log(tempParcel[list.name]);
                                    if (tempParcel[list.name] === null || tempParcel[list.name] === '' || tempParcel[list.name] === undefined || tempParcel[list.name] === 'undefined') {
                                        tempParcel[list.name] = "NA";
                                    }
                                }
                            });
//                                        $scope.lotDataForUiGrid.push(tempParcel);
                        }
                        for (var i = 0; i < $scope.gridOptions.data.length; i++) {
                            var item = $scope.gridOptions.data[i];
                            if (item.$$objectId === $scope.index) {
                                $scope.gridOptions.data[i] = angular.copy(tempParcel);
                                $scope.gridOptions.data[i].$$objectId = $scope.index;
                                break;
                            }
                        }
//                            console.log("$scope.gridOptions :"+JSON.stringify($scope.gridOptions));
                    };
                    DynamicFormService.convertorForCustomField(sectionData, success);
                    $scope.categoryCustom = DynamicFormService.resetSection($scope.generalParcelTemplate);
                    $scope.listFilled = true;
                    $scope.submitted = false;
//                    }
                    $scope.flag.editLot = false;
                    $scope.resetAddForm(false);
                }
//                        }
            };
            $scope.showPopUp = function(lotObj) {
                $scope.lotObjectToDelete = lotObj;
                $scope.index = lotObj.$$objectId;
                $("#deleteDialog").modal('show');
            };
            $scope.hidePopUp = function() {
                $("#deleteDialog").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.deleteLot = function() {
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
            $scope.onCancel = function(addParcelForm) {
                if (addParcelForm != null) {
//                            $scope.categoryCustom = DynamicFormService.resetSection($scope.generalParcelTemplate);
                    $scope.flag.showAddPage = false;
//                            $scope.lotListToSave = [];
//                            $scope.lotListTodisplay = [];
//                            $scope.modifiedListToDisplay = [];
                    $scope.submitted = false;
                    $scope.selectedInvoice = [];
                    $scope.listFilled = true;
                    $scope.flag.parcelsAlreadyCreated = false;
                    $scope.reset("categoryCustom", false);
                }
            };
            $scope.saveAllLot = function(addParcelForm) {
                $scope.submitted = false;
                $scope.caratDoesNotMatch = false;
                var parcelList = [];
                var parcelDataBean = {};
                var totalCaratFromUI = undefined;
                angular.forEach($scope.lotListToSave, function(itr) {
                    if (itr.category !== null && itr.category !== undefined) {
//                        parcelList.push({parcelCustom: itr.category.categoryCustom, invoiceDataBean: {id: $scope.invoiceId}, parcelDbType: $scope.parcelDbType});
                    } else {
                        if (!$scope.caratDoesNotMatch && itr.categoryCustom['carat_of_parcel$NM$Double'] !== undefined && itr.categoryCustom['carat_of_parcel$NM$Double'] !== null) {
                            if (totalCaratFromUI !== undefined) {
                                totalCaratFromUI = totalCaratFromUI + parseFloat(itr.categoryCustom['carat_of_parcel$NM$Double']);
                            } else {
                                totalCaratFromUI = parseFloat(itr.categoryCustom['carat_of_parcel$NM$Double']);
                            }
                        } else {
                            $scope.caratDoesNotMatch = true;
                        }
                        parcelList.push({parcelCustom: itr.categoryCustom, invoiceDataBean: {id: $scope.invoiceId}, parcelDbType: $scope.parcelDbTypeToSent});
                    }
                });
                if (!$scope.caratDoesNotMatch) {
                    if ($scope.caratOfInvoice !== totalCaratFromUI) {
                        $scope.caratDoesNotMatch = true;
                    }
                }
                parcelDataBean.parcelList = parcelList;
                parcelDataBean.invoiceDataBean = {id: $scope.invoiceId};
                if (!$scope.caratDoesNotMatch) {
                    $rootScope.maskLoading();
                    $rootScope.createdParcelIds = undefined;
                    $scope.createdParcelIds = undefined;
                    ParcelService.create(parcelDataBean, function(response) {
                        console.log(JSON.stringify(response));
                        $scope.createdParcelIds = angular.copy(response.primaryKey);
                        $rootScope.showLotLink = true;
                        $scope.lotListToSave = [];
                        $scope.lotListTodisplay = [];
                        $scope.modifiedListToDisplay = [];
                        $scope.searchedData = [];
                        $scope.searchedDataFromDb = [];
                        $scope.listFilled = false;
                        $scope.addParcelForm.$dirty = false;
                        $scope.flag.showAddPage = false;
                        $scope.onCancel();
                        $scope.onCanelOfSearch();
                        $scope.AddParcel = this;
                        this.parcelAddShow = true;
                        $timeout(function() {
                            $rootScope.showLotLink = false;
                        }, 30000);
                        $rootScope.unMaskLoading();
                    }, function() {
                        $rootScope.unMaskLoading();
                        var msg = "Could not create parcel, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                } else {
                    var msg = "Carat value does not match, please try again.";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                }
            };

            $scope.updateLot = function(searchDataObj) {
                if ((!!(searchDataObj))) {
                    for (var i = 0; i < $scope.lotInDb.length; i++) {
//                        console.log(JSON.stringify($scope.lotInDb[i]));
//                        console.log(JSON.stringify(searchDataObj));
                        if ($scope.createdParcelIds === null && $scope.lotInDb[i] !== undefined && $scope.lotInDb[i] !== null && searchDataObj.parcelDbObjectId === $scope.lotInDb[i].value) {
                            searchDataObj = angular.copy($scope.lotInDb[i]);
                            console.log("JSON.stringify(searchDataObj)");
                            break;
                        }
                    }
                    console.log(JSON.stringify(searchDataObj));
                    $location.path('/editparcel');
                    $rootScope.unMaskLoading();
                    $scope.flag.showUpdatePage = true;
                    $scope.addParcelForm.$dirty = false;
                    $rootScope.parcelByInvoice = angular.copy(searchDataObj);
                    $rootScope.editParcel = true;
                    //                        $scope.$broadcast('editParcel', response);                            
                }

            };
//                } else {
//                    $rootScope.unMaskLoading();
//                    $scope.flag.fieldSeqNotExist = true;
//                }
//            }
//            , function() {
//                $rootScope.unMaskLoading();
//                var msg = "Failed to retrieve stock id";
//                var type = $rootScope.error;
//                $rootScope.addMessage(msg, type);
//            });
            $scope.addLotDirectly = function() {
                if ($scope.createdParcelIds !== undefined) {
                    $rootScope.maskLoading();
                    $rootScope.createdParcelIds = angular.copy($scope.createdParcelIds);
                    $scope.addParcelForm.$dirty = false;
                    $location.path('/addlot');
                    $rootScope.showLotLink = false;
                }
            };
            $scope.countRemainingCarat = function() {
                if ($scope.gridOptions.data !== undefined && $scope.gridOptions.data !== null) {
                    var totalCreated = 0;
                    angular.forEach($scope.gridOptions.data, function(item) {
                        if (item["carat_of_parcel$NM$Double"] !== undefined && item["carat_of_parcel$NM$Double"] !== null) {
                            totalCreated += parseFloat(item["carat_of_parcel$NM$Double"]);
                        }
                    });
                    $scope.remainingCarat = parseFloat($scope.caratOfInvoice) - parseFloat(totalCreated);
                    if ($scope.remainingCarat < 0) {
                        $scope.remCaratMsg = "Carat value limit exceeded"
                    } else {
                        $scope.remCaratMsg = "Carat remaining is " + $scope.remainingCarat;
                    }
                }
            };
        }]);
});