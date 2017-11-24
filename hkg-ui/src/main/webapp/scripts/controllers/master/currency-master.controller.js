/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['hkg', 'manageCurrencyMasterService'], function (hkg, manageCurrencyMasterService) {
    hkg.register.controller('ManageCurrencyMaster', ["$rootScope", "$scope", "$filter", "ManageCurrencyMasterService", function ($rootScope, $scope, $filter, ManageCurrencyMasterService) {
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageReferenceRate";
            $scope.entity = "CURRENCY.";
            var orderBy = $filter('orderBy');
            $scope.selectedEventCategoryDropdown = {currentNode: ''};
            $scope.currencyMasterDataBean = {};
            $scope.currencyDropdown = {};
            $scope.tabManager = {};
            $scope.retrieveArchivedCurrency = function (callback) {
                var currencyFailure = function () {
                    var msg = "Failed to retrieve currency. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                    if (callback) {
                        callback();
                    }
                };
                ManageCurrencyMasterService.retrieveArchivedCurrency(function (res) {
                    $scope.tabManager.archivedList = [];
                    angular.forEach(res, function (value, key) {
                        var obj = {code: key, list: value};
                        if (key !== '$promise' && key !== '$resolved') {
                            $scope.tabManager.archivedList.push(obj);
                        }
                    });
                    if (callback) {
                        callback();
                    }
                }, currencyFailure);
            };
            $scope.retrieveCurrentCurrency = function (callback) {
                var currencyFailure = function () {
                    var msg = "Failed to retrieve currency. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                    if (callback) {
                        callback();
                    }
                };
                ManageCurrencyMasterService.retrieveCurrentCurrency(function (res) {
                    res = orderBy(res, ['-lastModifiedOn']);
                    if (angular.isDefined(res) && res !== null && res.length > 0) {
                        angular.forEach(res, function (item) {
                            item.applicableFrom = new Date(item.applicableFrom);
                        });
                    }
                    $scope.currentCurrencyRateList = angular.copy(res);
                    $scope.currentCurrencyRateListFromDb = angular.copy(res);
                    $scope.currentRateListFilled = true;
                    $rootScope.unMaskLoading();
                    if (callback) {
                        callback();
                    }
                }, currencyFailure);
            };
            $scope.retrieveCurrencyDataBean = function () {
                var currencyFailure = function () {
                    var msg = "Failed to retrieve currency. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                };
                $scope.listFilled = false;
                ManageCurrencyMasterService.retrieveCurrencyDataBean(function (res) {
                    $scope.currencyList = angular.copy(res);
                    $scope.currencyListFromDb = angular.copy(res);
//                    $scope.tabManager.tabItems = angular.copy(res);
                    $scope.listFilled = true;
                    $rootScope.unMaskLoading();

                }, currencyFailure);
            };


            $scope.initCurrencyForm = function (currencyForm) {
                $scope.currencyForm = currencyForm;
            };
            $scope.initRefRateForm = function (referencerateform) {
                $scope.referencerateForm = referencerateform;
            };
            $scope.retrieveArchivedCurrency();
            $scope.retrieveCurrentCurrency();
            $scope.isEdit = false;
            $scope.setRootOption = function () {
                $scope.currencyDropdown.currentNode = {id: '0', displayName: 'Select'};
                $scope.currencyId = $scope.currencyDropdown.currentNode.id;
            };
            $scope.initializePage = function () {
                $rootScope.maskLoading();
                if ($scope.referencerateform != null) {
                    $scope.referencerateform.$dirty = false;
                }
                $scope.submitted = false;
                var failure = function () {
                    var msg = "Failed to retrieve currency. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                };
                ManageCurrencyMasterService.retrieveCurrency(function (data) {
                    $scope.currencyCombo = data;
                    $scope.comboLabelAndDescriptionList = [];
                    $scope.comboLabelAndDescriptionList.unshift({id: '0', displayName: 'Select'});
                    $scope.comboLabelAndDescriptionList = angular.copy($scope.currencyCombo);
//                    if (angular.isDefined($scope.currencyCombo)) {
//                        for (var i = 0; i < $scope.currencyCombo.length; i++) {
//                            var obj = $scope.comboLabelAndDescription[i];
//                                $scope.comboLabelAndDescription[i].displayName = obj.label + "(" + obj.description + ")";
//                                $scope.comboLabelAndDescription[i].id = obj.value;
//                        }
//                    }
                    $rootScope.unMaskLoading();
//                    $.merge($scope.comboLabelAndDescriptionList, angular.copy($scope.comboLabelAndDescription));
//                    if (!($scope.currencyDropdown != null && $scope.currencyDropdown.currentNode != null && $scope.currencyDropdown.currentNode.displayName !== undefined && $scope.currencyDropdown.currentNode.displayName !== null && $scope.currencyDropdown.currentNode.displayName.length > 0)) {
//                        $scope.setRootOption();
//                    }

                }, failure);

            }


            $scope.tabChangeEvent = function (tab) {
                $scope.tabListFilled = false;
                if (tab != null && $scope.tabManager.archivedList != null && $scope.tabManager.archivedList.length > 0) {
                    for (var i = 0; i < $scope.tabManager.archivedList.length; i++) {
                        if ($scope.tabManager.archivedList[i].code === tab.code) {
                            $scope.tabManager.tabItems = angular.copy(($scope.tabManager.archivedList[i]));
                            $scope.tabManager.tabItems.list = orderBy($scope.tabManager.tabItems.list, ['-isActive']);
                        }
                    }
                }

                $scope.tabListFilled = true;
            };
            $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'shortDate', 'MM/dd/yyyy'];
            $scope.format = $rootScope.dateFormat;
            $scope.datepicker = {from: false, end: false};
            //            $scope.setMinDate();
            $scope.open = function ($event, opened) {

                $event.preventDefault();
                $event.stopPropagation();
                $scope.datepicker[opened] = true;
            };
            var today = $rootScope.getCurrentServerDate();
            $scope.minDate = today;
            $scope.referenceRate = "";
            $scope.$watch('referenceRate', function (newValue, oldValue) {
                var arr = String(newValue).split("");
                if (arr.length === 0)
                    return;
                if (arr.length === 1 && (arr[0] === '.'))
                    return;
                if (arr.length === 2 && newValue === '.')
                    return;
                if (isNaN(newValue)) {
                    if (newValue == undefined) {
                        $scope.referenceRate = newValue;
                        return;
                    }
                    $scope.referenceRate = oldValue;
                }
                var afterDecimal = String(newValue).split('.');
                if (angular.isDefined(afterDecimal[1]) && afterDecimal[1].length > 3) {
                    $scope.referenceRate = oldValue;
                    return;
                }
            });
//            
            $scope.updateReferenceRate = function () {
                $scope.submitted = true;
                if (angular.isDefined($scope.currencyCombo)) {
                    for (var i = 0; i < $scope.currencyCombo.length; i++) {
                        var obj = $scope.currencyCombo[i];
                        for (var key in obj) {
                            if ($scope.currency == obj[key]) {
                                $scope.id = $scope.currencyCombo[i].value;
                            }
                        }
                    }
                }
                var referenceRateJson = {};
                referenceRateJson = {
                    id: $scope.refId,
                    applicableFrom: $scope.applicableFrom,
                    referenceRate: $scope.referenceRate
                }
                if ($scope.referencerateform.$valid) {
                    if ($scope.refId != null && $scope.isEdit) {
                        ManageCurrencyMasterService.updateReferenceRate(referenceRateJson, function (res) {
                            $scope.retrieveArchivedCurrency();
                            $scope.retrieveCurrentCurrency();
//                            $scope.initializePage();
                            $scope.submitted = false;
                            $scope.onCanelOfReferencePage($scope.referencerateform);
                        })
                    }
                    else {
                        referenceRateJson.code = $scope.currencyId;
                        ManageCurrencyMasterService.addReferenceRate(referenceRateJson, function (res) {
                            $scope.retrieveArchivedCurrency();
                            $scope.retrieveCurrentCurrency();
//                            $scope.initializePage();
                            $scope.submitted = false;
                            $scope.onCanelOfReferencePage($scope.referencerateform);
                        })
                    }
                }


            }

            $scope.retrieveCurrenciesForCombo = function (currencyForm) {
                var currencyFailure = function () {
                    var msg = "Failed to retrieve currency. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                };
                ManageCurrencyMasterService.retrieveCurrencies(function (res) {
                    $scope.eventCategoriesTemp = angular.copy(res);
                    $scope.eventCategoryListDropdown = [];
                    if ($scope.eventCategoriesTemp != null && angular.isDefined($scope.eventCategoriesTemp) && $scope.eventCategoriesTemp.length > 0) {
                        angular.forEach($scope.eventCategoriesTemp, function (item) {
                            item.id = item.value;
                            item.displayName = item.label;
                            $scope.eventCategoryListDropdown.push(item);
                        });
                    }
                    if (currencyForm === undefined || currencyForm === null) {
                        $scope.eventCategoryListDropdown.unshift({"id": "D", "displayName": "Select"});
                        $scope.selectedEventCategoryDropdown.currentNode = $scope.eventCategoryListDropdown[0];
                        $scope.eventCategoryId = 'D';
                    }
                }, currencyFailure);
            };


            $scope.initCurrency = function (currencyForm) {
                $scope.submitted = false;
                if (currencyForm != null) {
                    currencyForm.$setPristine();
                    if ($scope.currencyMasterDataBean != null) {
                        $scope.currencyMasterDataBean.symbolPosition = $scope.positon;
                    }

                } else {
                    $scope.currencyMasterDataBean = {};
                    $scope.currencyMasterDataBean.symbolPosition = false;
                }
                if ($scope.currencyForm != null) {
                    $scope.currencyForm.$dirty = false;
                }

                $scope.retrieveCurrenciesForCombo(currencyForm);
                $scope.retrieveCurrencyDataBean();
                if ($scope.listFilled) {

                }

            };
            $scope.setCurrencyOperation = function () {
                $rootScope.maskLoading();
                $scope.showCurrencyPage = true;
                $scope.initCurrency(undefined);
            };
            $scope.currencyDropdownClick = function (selectedCategory) {
                $scope.popupEventCategoryId = selectedCategory.currentNode;
                $scope.eventCategoryId = $scope.popupEventCategoryId.id;
                if ($scope.eventCategoryId === 'INR') {
                    $scope.currencyMasterDataBean.lastReferenceRate = 1;
                }
            };
            $scope.checkPattern = function (displayFormat) {
                var format = "^[#.,]*$";
                if (displayFormat != null && !displayFormat.match(format)) {
                    if ($scope.currencyMasterDataBean.format.length > 1) {
                        $scope.currencyMasterDataBean.format = $scope.currencyMasterDataBean.format.substring(0, $scope.currencyMasterDataBean.format.length - 1);
                    }
                    else {
                        $scope.currencyMasterDataBean.format = null;
                    }
                }
            };

            var showUpdateLink = function (id) {
                $scope.currencyMasterDataBean.id = id;
            };
            $scope.createCurrency = function (currencyForm) {
                $scope.submitted = true;
                if (currencyForm.$valid) {
                    if ($scope.currencyMasterDataBean != null) {
                        if ($scope.currencyMasterDataBean.id != null) {
                            $scope.updateCurrency(currencyForm);
                        } else {
                            $scope.currencyMasterDataBean.code = $scope.popupEventCategoryId.id;
                            $scope.currencyMasterDataBean.symbol = $scope.popupEventCategoryId.description;
                            if ($scope.currencyMasterDataBean.symbolPosition) {
                                $scope.positon = true;
                                $scope.currencyMasterDataBean.symbolPosition = "P";
                            } else {
                                $scope.positon = false;
                                $scope.currencyMasterDataBean.symbolPosition = "S";
                            }

                            var currencyFailure = function () {
                                var msg = "Currency could not be added, please try again";
                                var type = $rootScope.failure;
                                $rootScope.addMessage(msg, type);
                                $rootScope.unMaskLoading();
                            };
                            $rootScope.maskLoading();
                            ManageCurrencyMasterService.create($scope.currencyMasterDataBean, function (res) {
                                if ($scope.currencyForm != null) {
                                    $scope.currencyForm.$dirty = false;
                                }
                                $scope.initCurrency(currencyForm);
                                $scope.retrieveCurrentCurrency(function (callback) {
                                    $scope.retrieveArchivedCurrency(function (callback) {
                                        if (res !== undefined && res !== null) {
                                            showUpdateLink(res.primaryKey);
                                        }
                                    });
                                });

                            }, currencyFailure);

                        }
                    }
                }
            };

            $scope.editCurrency = function (currencyObject) {
                $scope.currencyMasterDataBean = {};
                $scope.currencyMasterDataBean = angular.copy(currencyObject);
                if (currencyObject != null) {
                    $rootScope.maskLoading();
                    $scope.currencyMasterDataBean.symbolPosition = false;
                    if (currencyObject.symbolPosition === 'P') {
                        $scope.currencyMasterDataBean.symbolPosition = true;
                    }
                    $scope.selectedEventCategoryDropdown.currentNode.id = currencyObject.code;
                    $scope.selectedEventCategoryDropdown.currentNode.displayName = currencyObject.currencyName;
                    $scope.eventCategoryId = currencyObject.code;
                    $rootScope.unMaskLoading();
                }
            };

            $scope.onCanel = function (currencyForm) {
                $scope.submitted = false;
                $(document).find("#referenceRate").val("");
                $(document).find("#format").val("");
                if (currencyForm != null) {
                    currencyForm.$setPristine();
                }
                if ($scope.referencerateform !== undefined && $scope.referencerateform !== null) {
                    $scope.referencerateform.$setPristine();
                }
                $scope.currencyMasterDataBean = {};
                $scope.currencyMasterDataBean.lastReferenceRate = undefined;
                $scope.currencyMasterDataBean.format = undefined;
                $scope.showCurrencyPage = false;
                $rootScope.maskLoading();
                $scope.retrieveArchivedCurrency();
                $scope.retrieveCurrentCurrency();
            }

            $scope.onCanelOfReferencePage = function (referencerateform) {
                if (referencerateform != null) {
                    referencerateform.$setPristine();
                }
                $scope.currencyDropdown = {};
                $scope.currencyId = undefined;                
                $scope.referenceRate = undefined;
                $scope.applicableFrom = undefined;
                $scope.refId = undefined;
                $scope.isEdit = false;
                $scope.initializePage();
            };

            var updateRefFromCurrecnyPage = function () {
                $rootScope.maskLoading();
                if (angular.isDefined($scope.currentCurrencyRateListFromDb) && $scope.currentCurrencyRateListFromDb !== null && $scope.currentCurrencyRateListFromDb.length > 0) {
                    if ($scope.currencyMasterDataBean !== undefined && $scope.currencyMasterDataBean != null) {
                        angular.forEach($scope.currentCurrencyRateListFromDb, function (item) {
                            if (item.currencyId === $scope.currencyMasterDataBean.id) {
                                $scope.currentReferenceRateObject = item;
                            }
                        });
                    }
                }
                $scope.currencyMasterDataBean = {};
                $scope.showCurrencyPage = false;
                if ($scope.currentReferenceRateObject !== undefined && $scope.currencyMasterDataBean !== null) {
                    $scope.isEdit = true;
                    $scope.refId = $scope.currentReferenceRateObject.id;
                    $scope.applicableFrom = $scope.currentReferenceRateObject.applicableFrom;
                    $scope.minDate = $scope.currentReferenceRateObject.applicableFrom;
                    $scope.referenceRate = $scope.currentReferenceRateObject.referenceRate;
                    $scope.currencyId = $scope.currentReferenceRateObject.currencyId;
                    if (angular.isDefined($scope.currencyDropdown.currentNode)) {
                        $scope.currencyDropdown.currentNode.displayName = $scope.currentReferenceRateObject.code + "(" + $scope.currentReferenceRateObject.currencyName + ")";
                        $scope.currencyDropdown.currentNode.id = $scope.currentReferenceRateObject.currencyId;
                    }

                }
                $rootScope.unMaskLoading();
            };
            $scope.showPopUp = function (currencyForm) {
                if (currencyForm != null && currencyForm.$valid) {
                    currencyForm.$setPristine();
                    $scope.detailChanged = false;
                    var detailChanged = false;
                    if ($scope.currencyMasterDataBean != null && $scope.currencyListFromDb != null && $scope.currencyListFromDb.length > 0) {
                        angular.forEach($scope.currencyListFromDb, function (item) {
                            if ($scope.currencyMasterDataBean.id != null && item.id === $scope.currencyMasterDataBean.id) {
                                var position = "S";
                                if ($scope.currencyMasterDataBean.symbolPosition) {
                                    position = "P";
                                }
                                if ($scope.currencyMasterDataBean.format !== item.format || position !== item.symbolPosition) {
                                    detailChanged = true;
                                }
                            }
                        });
                    }
                    if (detailChanged) {
                        $scope.detailChanged = true;
                        $("#confirmationPopUp").modal('show');
                    } else {
                        updateRefFromCurrecnyPage();
                    }
                }
            };

            $scope.hideConfirmationPopUp = function () {
                $scope.submitted = false;
                if ($scope.referencerateform !== undefined && $scope.referencerateform !== null) {
                    $scope.referencerateform.$setPristine();
                }
                updateRefFromCurrecnyPage();
                $("#confirmationPopUp").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();


            }
            $scope.updateCurrency = function (currencyForm) {
                $("#confirmationPopUp").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();

                $scope.submitted = true;
                if (currencyForm.$valid) {
                    if ($scope.currencyMasterDataBean != null) {
                        if ($scope.currencyMasterDataBean.symbolPosition) {
                            $scope.positon = true;
                            $scope.currencyMasterDataBean.symbolPosition = "P";
                        } else {
                            $scope.positon = false;
                            $scope.currencyMasterDataBean.symbolPosition = "S";
                        }
                    }
                    var currencyFailure = function () {
                        var msg = "Failed to add currency. Try again.";
                        var type = $rootScope.failure;
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    };
                    ManageCurrencyMasterService.update($scope.currencyMasterDataBean, function (res) {
                        if ($scope.currencyForm != null) {
                            $scope.currencyForm.$dirty = false;
                        }
                        if ($scope.detailChanged) {
                            $scope.showCurrencyPage = false;
                            updateRefFromCurrecnyPage();
                        } else {
                            if (currencyForm != null) {
                                currencyForm.$dirty = false;
                                currencyForm.$setPristine();
                            }
                            $scope.initCurrency(currencyForm);
                            $scope.retrieveArchivedCurrency();
                            $scope.retrieveCurrentCurrency();

                        }
                    }, currencyFailure);

                }
            };
            $scope.showdeletePopUp = function (row) {
                $scope.row = row;
                $("#deletePopUp").modal('show');
            };
            $scope.disableCurrency = function () {
                $("#deletePopUp").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();

                var id = {}
                if ($scope.row != null && $scope.row.id != null) {
                    id.primaryKey = $scope.row.id;
                }
                var currencyFailure = function () {
                    var msg = "Failed to disable. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                };
                ManageCurrencyMasterService.deleteById(id, function (res) {
                    $scope.row = undefined;
                    $scope.retrieveArchivedCurrency();
                    $scope.retrieveCurrentCurrency();
                }, currencyFailure);

            };

            $scope.hidedeletePopUp = function () {
                $("#deletePopUp").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.currencyRateDropdownClick = function (currencyDropdown, form) {
//                $scope.selectedParent.displayName=currencyDropdown

            }

            $scope.editReferenceRate = function (referenceRate) {
                $scope.isEdit = true;
                if (referenceRate != null) {
                    $scope.refId = referenceRate.id;
                    $scope.applicableFrom = referenceRate.applicableFrom;
                    $scope.minDate = $rootScope.getCurrentServerDate();
                    $scope.referenceRate = referenceRate.referenceRate;
                    $scope.currencyId = referenceRate.code;
                }
            };

            ;
        }]);
});
