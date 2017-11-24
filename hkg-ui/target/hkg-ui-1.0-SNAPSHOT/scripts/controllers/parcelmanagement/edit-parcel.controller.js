define(['hkg', 'parcelService', 'customFieldService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'printBarcodeValue', 'dynamicForm', 'accordionCollapse'], function(hkg, parcelService, customFieldService) {
    hkg.register.controller('EditParcelController', ["$rootScope", "$scope", "ParcelService", "DynamicFormService", "CustomFieldService", "$location", function($rootScope, $scope, ParcelService, DynamicFormService, CustomFieldService, $location) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "editParcel";
            $rootScope.activateMenu();

            $scope.searchedDataFromDbForUiGrid = [];
            $scope.parcelDataForUiGrid = [];
            $scope.parcelListForUIGrid = [];
            $scope.parcelLabelListForUiGrid = [];
            var featureMap = {};
            var featureDbFieldMap = {};
            $scope.initializeData = function(flag) {
                if (flag) {
                    featureDbFieldMap = {};
                    $scope.parcelDataBean = {};
                    $scope.searchedData = [];
                    $scope.searchedDataFromDb = [];
                    $scope.listFilled = false;
                    $scope.lotListTodisplay = [];
                    $scope.searchCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);
//                    $scope.parcelDataBean.featureCustomMap = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("parcelEdit");
                    $scope.flag = {};
                    $scope.flag.searchFieldNotAvailable = false;
                    $scope.dbType = {};
                    $scope.modelAndHeaderList = [];
                    $scope.modelAndHeaderListForLot = [];
                    templateData.then(function(section) {
                        $scope.generalSearchTemplate = section['genralSection'];
                        $scope.searchInvoiceTemplate = [];
                        $scope.searchParcelTemplate = [];
                        var invoiceDbFieldName = [];
                        var parcelDbFieldName = [];
                        if ($scope.generalSearchTemplate != null && $scope.generalSearchTemplate.length > 0) {
                            for (var i = 0; i < $scope.generalSearchTemplate.length; i++) {
                                var item = $scope.generalSearchTemplate [i];
                                if (item.featureName.toLowerCase() === 'invoice') {
                                    $scope.searchInvoiceTemplate.push(angular.copy(item));
                                    invoiceDbFieldName.push(angular.copy(item.model));
                                } else if (item.featureName.toLowerCase() === 'parcel') {
                                    parcelDbFieldName.push(angular.copy(item.model));
                                    $scope.searchParcelTemplate.push(angular.copy(item));
                                }
                                featureMap[item.model] = item.featureName;
                                $scope.modelAndHeaderList.push({model: item.model, header: item.label});
                                if (item.fromModel) {
                                    $scope.parcelLabelListForUiGrid.push({name: item.fromModel, displayName: item.label, minWidth: 200});
                                } else if (item.toModel) {
                                    $scope.parcelLabelListForUiGrid.push({name: item.toModel, displayName: item.label, minWidth: 200});
                                } else if (item.model) {
                                    $scope.parcelLabelListForUiGrid.push({name: item.model, displayName: item.label, minWidth: 200});
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
                    }, function(reason) {

                    }, function(update) {

                    });
                }
            };
            $scope.initializeData(true);
            $scope.initEditParcelForm = function(editParcelForm) {
                $scope.editParcelForm = editParcelForm;
            };
            $scope.retrieveSearchedData = function(addLotForm) {
                $scope.selectOneParameter = false;
                $scope.gridOptions = {};
                $scope.searchedData = [];
                $scope.listFilled = false;
                $scope.searchedDataFromDbForUiGrid = [];
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
                        //                        $scope.parcelDataBean.featureCustomMap = {};
                        $scope.parcelDataBean.featureCustomMapValue = {};
                        $scope.map = {};

//                        angular.forEach($scope.searchCustom, function(value, key) {
//                            if (value != null) {
//                                angular.forEach(featureMap, function(val, label) {
//                                    if (key === label) {
////                                        if (!$scope.parcelDataBean.featureCustomMap[val])
////                                            $scope.parcelDataBean.featureCustomMap[val] = [];
//                                        if (!$scope.parcelDataBean.featureCustomMapValue[val])
//                                            $scope.parcelDataBean.featureCustomMapValue[val] = val;
//
////                                        $scope.parcelDataBean.featureCustomMap[val].push(key + ":" + value);
//                                        if (!$scope.map[key]) {
//                                            $scope.map[key] = {};
//                                        }
//                                        $scope.map[key] = value;
//                                        if (!$scope.parcelDataBean.featureCustomMapValue[val].map[key]) {
//                                            $scope.parcelDataBean.featureCustomMapValue[val] = $scope.map;
//                                        }
//                                    }
//                                });
//                            }
//                        });
                        var finalMap = {};
                        var searchResult = DynamicFormService.convertSearchData($scope.searchInvoiceTemplate, $scope.searchParcelTemplate, null, null, $scope.searchCustom);
                        angular.forEach(featureMap, function(val, label) {

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
                        $scope.parcelDataBean.featureDbFieldMap = featureDbFieldMap;
                        $scope.parcelDataBean.searchOnParameter = true;
//                        console.log(JSON.stringify(featureDbFieldMap));
//                        $scope.parcelDataBean.parcelCustom = $scope.searchCustom;
                        ParcelService.search($scope.parcelDataBean, function(res) {
                            var parcelField = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
                            parcelField.then(function(section) {
                                $scope.parcelCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);
                                $scope.parcelTemplate = section['genralSection'];
                                $scope.searchedDataFromDb = angular.copy(res);
                                var success = function(result)
                                {
                                    $scope.searchedData = angular.copy(result);
                                    angular.forEach($scope.searchedData, function(itr) {
                                        ///////////////////////////////////////////UI-GRID/////////////////////////////////////////////////////
                                        angular.forEach($scope.parcelLabelListForUiGrid, function(list) {
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
                                        $scope.searchedDataFromDbForUiGrid.push(itr.categoryCustom);
                                    });
                                    $scope.gridOptions = {};
                                    $scope.gridOptions.enableFiltering = true;
                                    $scope.gridOptions.multiSelect = false;
                                    $scope.gridOptions.enableRowSelection = true;
                                    $scope.gridOptions.enableSelectAll = false;
                                    $scope.selectedParcel = [];
                                    $scope.gridOptions.onRegisterApi = function(gridApi) {
                                        $scope.gridApi = gridApi;
                                        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                                            if ($scope.selectedParcel.length > 0) {
                                                $.each($scope.selectedParcel, function(index, result) {
                                                    if (result["$$hashKey"] === row["entity"]["$$hashKey"]) {
                                                        //Remove from array
                                                        $scope.flag.repeatedRow = true;
                                                        $scope.selectedParcel.splice(index, 1);
                                                        return false;
                                                    } else {
                                                        $scope.flag.repeatedRow = false;
                                                    }
                                                });
                                                if (!$scope.flag.repeatedRow) {
                                                    $scope.selectedParcel.push(row["entity"]);
                                                }
                                            } else {
                                                $scope.selectedParcel.push(row["entity"]);
                                            }
                                            if ($scope.selectedParcel.length > 0) {
                                                $scope.flag.rowSelectedflag = true;
                                            } else {
                                                $scope.flag.rowSelectedflag = false;
                                            }
                                        });
                                        gridApi.selection.on.rowSelectionChangedBatch($scope, function(rows) {
                                            if ($scope.selectedParcel.length > 0) {
                                                angular.forEach(rows, function(row) {
                                                    $.each($scope.selectedParcel, function(index, result) {
                                                        if (result["$$hashKey"] === row["entity"]["$$hashKey"]) {
                                                            //Remove from array
                                                            $scope.flag.repeatedRow = true;
                                                            $scope.selectedParcel = [];
                                                            return false;
                                                        } else {
                                                            $scope.flag.repeatedRow = false;
                                                        }
                                                    });
                                                    if (!$scope.flag.repeatedRow) {
                                                        $scope.selectedParcel.push(row["entity"]);
                                                    }
                                                });
                                            } else {
                                                angular.forEach(rows, function(row) {
                                                    $scope.selectedParcel.push(row["entity"]);
                                                });
                                            }
                                            if ($scope.selectedParcel.length > 0) {
                                                $scope.flag.rowSelectedflag = true;
                                            } else {
                                                $scope.flag.rowSelectedflag = false;
                                            }
                                        });
                                    };
                                    $scope.gridOptions.data = $scope.searchedDataFromDbForUiGrid;
                                    $scope.gridOptions.columnDefs = $scope.parcelLabelListForUiGrid;
                                    window.setTimeout(function() {
                                        $(window).resize();
                                        $(window).resize();
                                    }, 100);
                                    //////////////////
////////////////////////////UI-GRID///////////////////////////////////////////////
                                    $scope.listFilled = true;
                                    $rootScope.unMaskLoading();
                                };
                                DynamicFormService.convertorForCustomField(res, success);


                            }, function(reason) {
                            }, function(update) {
                            });
//                            $scope.searchedDataFromDb = angular.copy(res);
//                            var data = DynamicFormService.getValuesOfComponentFromId(res, $scope.generalSearchTemplate);
//                            $scope.searchedData = angular.copy(data);
//                            $scope.listFilled = true;
//                            $scope.searchCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);
//                            $rootScope.unMaskLoading();
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
                } else {
                    var msg = "Please select atleast one search criteria for search";
                    var type = $rootScope.warning;
                    $rootScope.addMessage(msg, type);
                }
            };

            $scope.reset = function(sectionTobeReset) {
                if (sectionTobeReset === "searchCustom") {
                    $scope.searchCustom = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("parcelEdit");
                    $scope.dbType = {};
                    templateData.then(function(section) {
                        $scope.generalSearchTemplate = section['genralSection'];
                        $scope.searchResetFlag = true;
                        $rootScope.unMaskLoading();
                        $scope.flag.customFieldGenerated = true;
                    }, function(reason) {
                    }, function(update) {
                    });
                } else if (sectionTobeReset === "parcelCustom") {
                    $scope.parcelCustom = {};
                    var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
//                    $scope.parcelDbType = {};
                    templateData.then(function(section) {
                        var parcelField = [];
                        var result = Object.keys($scope.response).map(function(key, value) {
                            angular.forEach(this[key], function(itr) {
                                if (key !== 'Parcel#P#') {
                                    parcelField.push({Parcel: itr});
                                }
                            });
                        }, $scope.response);
                        $scope.updateParcelTemplate = null;
                        $scope.updateParcelTemplate = section['genralSection'];
                        $scope.updateParcelTemplate = DynamicFormService.retrieveCustomData($scope.updateParcelTemplate, parcelField);
                        $scope.parcelAddShow = true;
                        $rootScope.unMaskLoading();
                        $scope.flag.customFieldGenerated = true;
                        $scope.parcelDataBean.parcelDbType = angular.copy($scope.parcelDbType);
                    }, function(reason) {
                    }, function(update) {
                    });
                }
            };

            $scope.onCanel = function(editParcelForm) {
                if ($scope.editParcelForm != null) {
                    $scope.editParcelForm.$dirty = false;
                }
                if ($scope.addParcelForm != null) {
                    $scope.addParcelForm.$dirty = false;
                    $location.path("/editparcel");
                }
                $scope.parcelCustom = DynamicFormService.resetSection($scope.updateParcelTemplate);
                $rootScope.editParcel = false;
                $rootScope.parcelByInvoice = null;
                $scope.flag.showUpdatePage = false
                $scope.submitted = false;
                $scope.parcelAddShow = false;
                $scope.selectedParcel = [];
                $scope.reset("parcelCustom");
            };

            $scope.onCanelOfSearch = function(addLotForm) {
                if ($scope.addLotForm != null) {
                    $scope.addLotForm.$dirty = false;
                }
                $scope.searchResetFlag = false;
                $scope.listFilled = false;
                $scope.searchedData = [];
                $scope.selectedParcel = [];
                $scope.gridOptions = {};
                $scope.reset("searchCustom");
            };

            $scope.saveParcel = function(editParcelForm) {
                $scope.submitted = true;
                angular.forEach($scope.listOfModelsOfDateType, function(listOfModel)
                {
                    if ($scope.parcelCustom.hasOwnProperty(listOfModel))
                    {
                        if ($scope.parcelCustom[listOfModel] !== null && $scope.parcelCustom[listOfModel] !== undefined)
                        {
                            $scope.parcelCustom[listOfModel] = new Date($scope.parcelCustom[listOfModel]);
                        } else
                        {
                            $scope.parcelCustom[listOfModel] = '';
                        }
                    }
                });
                $scope.parcelDataBean = {};
                $scope.parcelDataBean.parcelCustom = $scope.parcelCustom;
                $scope.parcelDataBean.parcelDbType = angular.copy($scope.parcelDbType);
                $scope.parcelDataBean.id = $scope.parcelId;

                if (editParcelForm.$valid) {
                    $rootScope.maskLoading();
                    $scope.submitted = false;
                    ParcelService.update($scope.parcelDataBean, function(response) {
//                        $scope.editParcelForm = editParcelForm;
                        $scope.editParcelForm.$dirty = false;
                        $scope.selectedParcel = [];
                        $scope.gridOptions = {};
                        if ($scope.addParcelForm != null) {
                            $scope.addParcelForm.$dirty = false;
                            $location.path("/editparcel");
                        } else {
                            $scope.initializeData(true);
                        }
                        $rootScope.editParcel = false;
                        $rootScope.parcelByInvoice = null;
                        $rootScope.unMaskLoading();
                    }, function() {
                        $rootScope.unMaskLoading();
                        var msg = "Could not update parcel, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                }
            };

            $scope.editParcel = function(lotObj) {
                var mapToSent = {};
                if ((lotObj === undefined || lotObj === null)) {
                    lotObj = $scope.selectedParcel[0];
                }
                if (lotObj != null) {
                    if ($scope.searchedDataFromDb !== undefined && $scope.searchedDataFromDb.length > 0) {
                        for (var i = 0; i < $scope.searchedDataFromDb.length; i++) {
                            var itr = $scope.searchedDataFromDb[i];
                            if (itr.categoryCustom["parcelDbObjectId"] === lotObj.parcelDbObjectId) {
                                lotObj = angular.copy($scope.searchedDataFromDb[i]);
                                break;
                            }
                        }
                    }
                    $scope.parcelAddShow = true;
                    $scope.flag.showUpdatePage = true;
                    $scope.parcelId = lotObj.value;
                    $scope.invoiceIdForConstraint = lotObj.label;
                    $scope.parcelIdIdForConstraint = lotObj.value;
                    CustomFieldService.retrieveDesignationBasedFields("parcelEdit", function(response) {
                        var templateDataForUpdate = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                        $scope.dbTypeForInvoice = {};
                        templateDataForUpdate.then(function(section) {
                            $scope.updateInvoiceTemplate = section['genralSection'];
                            var invoiceDbField = [];
                            var invoiceField = [];
                            var result = Object.keys(response).map(function(key, value) {
                                angular.forEach(this[key], function(itr) {
                                    if (key === 'Invoice#P#') {
                                        invoiceField.push({Invoice: itr});
                                    }
                                });
                            }, response);
                            $scope.response = angular.copy(response);
                            $scope.updateInvoiceTemplate = DynamicFormService.retrieveCustomData($scope.updateInvoiceTemplate, invoiceField);
                            angular.forEach($scope.updateInvoiceTemplate, function(itr) {
                                if (itr.model) {
                                    invoiceDbField.push(itr.model);
                                }
                            });
                            if (invoiceDbField.length > 0) {
                                mapToSent['invoiceDbFieldName'] = invoiceDbField;
                            }

                        }, function(reason) {

                        }, function(update) {
                        });
                        // For parcel parent fields
                        var parcelDbFieldName = [];
                        var templateDataOfParcelParent = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
                        $scope.parcelDbTypeParent = {};
                        templateDataOfParcelParent.then(function(section) {
//                            console.log(JSON.stringify(templateDataForUpdate));
                            $scope.updateParcelParentTemplate = section['genralSection'];

                            var parcelParent = [];
                            var result = Object.keys(response).map(function(key, value) {
                                angular.forEach(this[key], function(itr) {
                                    if (key === 'Parcel#P#') {
                                        parcelParent.push({Parcel: itr});
                                    }
                                });
                            }, response);
                            $scope.updateParcelParentTemplate = DynamicFormService.retrieveCustomData($scope.updateParcelParentTemplate, parcelParent);
                            angular.forEach($scope.updateParcelParentTemplate, function(itr) {
                                if (itr.model) {
                                    parcelDbFieldName.push(itr.model);
                                }
                            });
//                            $rootScope.unMaskLoading();

                            $scope.flag.customFieldGeneratedForUpdate = true;
                        }, function(reason) {

                        }, function(update) {
                        });
// For parcel fields
//                        $scope.parcelCustom = DynamicFormService.resetSection($scope.updateParcelTemplate);
                        var templateDataOfParcel = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
                        if (!($scope.parcelDbType != null))
                            $scope.parcelDbType = {};
                        templateDataOfParcel.then(function(section) {
                            $scope.updateParcelTemplate = section['genralSection'];
                            $scope.listOfModelsOfDateType = [];
                            if ($scope.updateParcelTemplate !== null && $scope.updateParcelTemplate !== undefined)
                            {
                                angular.forEach($scope.updateParcelTemplate, function(updateTemplate)
                                {
                                    if (updateTemplate.type !== null && updateTemplate.type !== undefined && updateTemplate.type === 'date') {
                                        $scope.listOfModelsOfDateType.push(updateTemplate.model);
                                    }
                                    if (updateTemplate.model === 'in_stock_of_parcel$UMS$String') {
                                        updateTemplate.isViewFromDesignation = true;
                                    }
                                });
                            }
                            var parcelFieldData = [];
                            var result = Object.keys(response).map(function(key, value) {
                                angular.forEach(this[key], function(itr) {
                                    if (key === 'Parcel') {
                                        parcelFieldData.push({Parcel: itr});
                                    }
                                });
                            }, response);
                            $scope.updateParcelTemplate = DynamicFormService.retrieveCustomData($scope.updateParcelTemplate, parcelFieldData);
                            angular.forEach($scope.updateParcelTemplate, function(itr) {
                                if (itr.model && parcelDbFieldName.indexOf(itr.model) === -1) {
                                    parcelDbFieldName.push(itr.model);
                                }
                            });
                            $rootScope.unMaskLoading();

                            if (lotObj.custom1 != null) {
                                $scope.parcelDataBean = {};
                                $scope.parcelCustom = angular.copy(lotObj.custom1);
                                $scope.parcelDataBean.parcelDbType = angular.copy($scope.parcelDbType);
                            }
                            var ids = [];
                            ids.push(lotObj.value);
                            mapToSent['parcelObjectId'] = ids;
                            if (parcelDbFieldName.length > 0) {
                                mapToSent['parcelDbFieldName'] = parcelDbFieldName;
                            }
                            ParcelService.retrieveParcelById(mapToSent, function(response) {
                                $scope.flag.customFieldGeneratedForUpdate = true;
                                $scope.parcelDataBean = {};
                                if (response.custom1 !== null && response.custom1 !== undefined) {
                                    angular.forEach($scope.listOfModelsOfDateType, function(listOfModel)
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
                                    $scope.parcelCustom = angular.copy(response.custom1);
                                    $scope.parcelParentCustom = angular.copy(response.custom1);
                                } else {
                                    $scope.parcelCustom = {};
                                    $scope.parcelParentCustom = {};
                                }
                                $scope.parcelDataBean.parcelDbType = angular.copy($scope.parcelDbType);
                                if (response.custom3 !== null && response.custom3 !== undefined) {
                                    angular.forEach($scope.listOfModelsOfDateType, function(listOfModel)
                                    {
                                        if (response.custom3.hasOwnProperty(listOfModel))
                                        {
                                            if (response.custom3[listOfModel] !== null && response.custom3[listOfModel] !== undefined)
                                            {
                                                response.custom3[listOfModel] = new Date(response.custom3[listOfModel]);
                                            } else
                                            {
                                                response.custom3[listOfModel] = '';
                                            }
                                        }
                                    })
                                    $scope.invoiceCustom = response.custom3;
                                }
                                else {
                                    $scope.invoiceCustom = {};
                                }
                                $rootScope.unMaskLoading();
                            }, function() {
                                $rootScope.unMaskLoading();
                                var msg = "Could not retrieve parcel, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                            });
                        }, function(reason) {
                        }, function(update) {
                        });


                        $scope.flag.customFieldGenerated = true;
                    }, function() {
                        $rootScope.unMaskLoading();
                        var msg = "Failed to retrieve data";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                }
            };

            if ($rootScope.editParcel) {
                $scope.editParcel($rootScope.parcelByInvoice);
            }
            $scope.deleteParcel = function() {
                $("#deleteParcelPopUp").modal("show");
            };
            $scope.deleteParcelFromPopup = function() {
                console.log($scope.parcelId);
                if ($scope.parcelId !== undefined && $scope.parcelId !== null) {
                    $rootScope.maskLoading();
                    ParcelService.deleteParcel($scope.parcelId, function() {
                        $rootScope.unMaskLoading();
                        $scope.hideParcelPopUp();
                        $scope.onCanel();
                        $scope.searchedData = [];
                        $scope.listFilled = false;
                    }, function() {
                        $rootScope.unMaskLoading();
                    });
                }
            };
            $scope.hideParcelPopUp = function() {
                $("#deleteParcelPopUp").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            };
        }]);
});