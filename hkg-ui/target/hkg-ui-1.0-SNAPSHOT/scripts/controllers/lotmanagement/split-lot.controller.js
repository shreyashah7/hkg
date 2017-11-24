define(['hkg', 'lotService', 'customFieldService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive','printBarcodeValue', 'dynamicForm'], function (hkg, lotService) {
    hkg.register.controller('SplitLotController', ["$rootScope", "$scope", "LotService", "$timeout", "$filter", "$location", "$window", "DynamicFormService", "CustomFieldService", function ($rootScope, $scope, LotService, $timeout, $filter, $location, $window, DynamicFormService, CustomFieldService) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "splitLot";
            $rootScope.activateMenu();
            var featureMap = {};
            $scope.initializeData = function (flag) {
                if (flag) {
                    $scope.tempMergeList = [];
                    $scope.lotDataBean = {};
                    $scope.searchedData = [];
                    $scope.searchedDataFromDb = [];
                    $scope.listFilled = false;
                    $scope.lotListTodisplay = [];
                    $scope.searchCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);
                    $scope.lotDataBean.featureCustomMap = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("lotSplit");
                    $scope.flag = {};
                    $scope.dbType = {};
                    $scope.modelAndHeaderList = [];
                    $scope.modelAndHeaderListForLot = [];
                    templateData.then(function (section) {
                        $scope.generalSearchTemplate = section['genralSection'];
                        if ($scope.generalSearchTemplate != null && $scope.generalSearchTemplate.length > 0) {
                            for (var i = 0; i < $scope.generalSearchTemplate.length; i++) {
                                var item = $scope.generalSearchTemplate [i];
                                featureMap[item.model] = item.featureName;
                                $scope.modelAndHeaderList.push({model: item.model, header: item.label});
                            }
                        }
                        $scope.dataRetrieved = true;
                    }, function (reason) {

                    }, function (update) {

                    });
                }
            };
            $scope.initializeData(true);
            $scope.initSplitLotForm = function (splitLotForm) {
                $scope.splitLotForm = splitLotForm;
            };
            $scope.saveAllLot = function (splitLotForm) {
                var lotList = [];
                var lotDataBean = {};
                angular.forEach($scope.lotListToSave, function (itr) {
                    lotList.push({lotCustom: itr.categoryCustom, invoiceDataBean: {id: $scope.invoiceId}, lotDbType: $scope.splitLotDbType, parcelDataBean: {id: $scope.parcelId}});
                });
                lotDataBean.lotList = lotList;
                lotDataBean.invoiceDataBean = {id: $scope.invoiceId};
                lotDataBean.parcelDataBean = {id: $scope.parcelId};
                lotDataBean.id = $scope.lotId;


//                console.log(JSON.stringify(lotList));
                if (splitLotForm.$valid) {
                    LotService.splitLot(lotDataBean, function (response) {
                        $scope.lotListToSave = [];
                        $scope.lotListTodisplay = [];
                        $scope.invoiceCustom = DynamicFormService.resetSection($scope.generaInvoiceTemplate);
                        $scope.parcelCustom = DynamicFormService.resetSection($scope.generaParcelTemplate);
                        $scope.categoryCustom = DynamicFormService.resetSection($scope.generalLotTemplate);
                        $scope.searchedData = [];
                        $scope.searchedDataFromDb = [];
                        $scope.listFilled = false;
                        $scope.addLotForm.$dirty = false;
                        $scope.flag.showAddPage = false;
                    }, function () {
                        $rootScope.unMaskLoading();
                        var msg = "Could not create lot, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                }
            }
            $scope.retrieveSearchedData = function (splitLotForm) {

                $scope.lotDataBean.featureCustomMap = {}
                if (Object.getOwnPropertyNames($scope.searchCustom).length > 0) {
                    var mapHasValue = false;
                    for (var prop in $scope.searchCustom) {
                        if (!!($scope.searchCustom[prop])) {
                            mapHasValue = true;
                            break;
                        }
                    }
                    if (mapHasValue) {
                        $rootScope.maskLoading();
                        var finalMap = {};
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

                        $scope.lotDataBean.lotCustom = $scope.searchCustom;
                        $scope.lotDataBean.hasPacket = false;
                        LotService.search($scope.lotDataBean, function (res) {
                            $scope.searchedDataFromDb = angular.copy(res);
                            var data = DynamicFormService.getValuesOfComponentFromId(res, $scope.generalSearchTemplate);
                            $scope.searchedData = angular.copy(data);
                            $scope.listFilled = true;
                            $scope.searchCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);
                            $rootScope.unMaskLoading();
                        }, function () {
                            var msg = "Could not retrieve, please try again.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        });
                    }
                }
            };
            $scope.onCanelOfSearch = function (splitLotForm) {
                if ($scope.splitLotForm != null) {
                    $scope.splitLotForm.$dirty = false;
                }
                $scope.searchedData = [];
                $scope.searchCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);
                $scope.listFilled = false;
            };


            $scope.addLot = function (parcelObj) {
                $scope.lotListToSave = [];
                $scope.count = 0;
                $scope.invoiceId = parcelObj.description;
                $scope.parcelId = parcelObj.label;
                $scope.lotId = parcelObj.value;
                if (parcelObj != null) {
                    if ($scope.searchedDataFromDb != null && $scope.searchedDataFromDb.length > 0) {
                        for (var itr = 0; itr < $scope.searchedDataFromDb.length; itr++) {
                            if ($scope.searchedDataFromDb[itr].id === parcelObj.id) {
                                parcelObj = angular.copy($scope.searchedDataFromDb[itr]);
                                break;
                            }
                        }
                    }
                    $scope.flag.showAddPage = true;
                    $rootScope.maskLoading();
                    $scope.parcelDataBean = {invoiceDataBean: {invoiceCustom: '', invoiceDbType: ''}};
                    CustomFieldService.retrieveDesignationBasedFields("lotSplit", function (response) {
                        $scope.invoiceCustom = DynamicFormService.resetSection($scope.generaInvoiceTemplate);
                        var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                        $scope.invoiceDbType = {};
                        templateData.then(function (section) {
                            $scope.generaInvoiceTemplate = section['genralSection'];
                            $scope.generaInvoiceTemplate = DynamicFormService.retrieveCustomData($scope.generaInvoiceTemplate, response);
                            $scope.invoiceCustom = parcelObj.custom3;
                        }, function (reason) {

                        }, function (update) {

                        });
                        $scope.parcelCustom = DynamicFormService.resetSection($scope.generaParcelTemplate);
                        var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
                        templateData.then(function (section) {
                            $scope.generaParcelTemplate = section['genralSection'];
                            $scope.generaParcelTemplate = DynamicFormService.retrieveCustomData($scope.generaParcelTemplate, response);
                            $scope.parcelCustom = parcelObj.custom4;
                        }, function (reason) {

                        }, function (update) {

                        });
                        var splitLotField = [];
                        $scope.lotCustom = DynamicFormService.resetSection($scope.generalLotTemplate);
                        var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                        templateData.then(function (section) {
                            var parentLotField = [];
                            var result = Object.keys(response).map(function (key, value) {
                                angular.forEach(this[key], function (itr) {
                                    if (key === 'Lot#P#') {
                                        parentLotField.push({"Lot#P#": itr});
                                    } else if (key === 'Lot') {
                                        splitLotField.push({Lot: itr});
                                    }
                                });
                            }, response);
                            $scope.generalLotTemplate = section['genralSection'];
                            $scope.generalLotTemplate = DynamicFormService.retrieveCustomData($scope.generalLotTemplate, parentLotField);
                            $scope.lotCustom = parcelObj.custom1;
                        }, function (reason) {

                        }, function (update) {

                        });

                        $scope.categoryCustom = DynamicFormService.resetSection($scope.generalSplitLotTemplate);
                        var templateDataParcel = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                        $scope.splitLotDbType = {};
                        templateDataParcel.then(function (section) {
                            $scope.generalSplitLotTemplate = section['genralSection'];
                            $scope.generalSplitLotTemplate = DynamicFormService.retrieveCustomData($scope.generalSplitLotTemplate, splitLotField);
                            $scope.flag.customFieldGenerated = true;
                            angular.forEach($scope.generalSplitLotTemplate, function (itr) {
                                $scope.modelAndHeaderListForLot.push({model: itr.model, header: itr.label});
                            });
                            $scope.lotDataBean.lotDbType = $scope.splitLotDbType;
//
                        }, function (reason) {
//
                        }, function (update) {
                        });
                        $rootScope.unMaskLoading();
                    }, function () {
                        $rootScope.unMaskLoading();
                        var msg = "Failed to retrieve data";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                }
            };
            $scope.onCancel = function (addLotForm) {
                if (addLotForm != null) {
                    $scope.categoryCustom = DynamicFormService.resetSection($scope.generalLotTemplate);
                    $scope.flag.showAddPage = false;
                    $scope.lotListToSave = [];
                    $scope.lotListTodisplay = [];
                }
            };
            $scope.createLot = function (addParcelForm) {
                if (Object.getOwnPropertyNames($scope.categoryCustom).length > 0) {
                    var mapHasValue = false;
                    for (var prop in $scope.categoryCustom) {
                        if (!!($scope.categoryCustom[prop])) {
                            mapHasValue = true;
                            break;
                        }
                    }
                    if (addParcelForm.$valid && mapHasValue) {
                        var sectionData = [];
                        sectionData.push({categoryCustom: angular.copy($scope.categoryCustom)});
                        $scope.count++;
                        $scope.lotListToSave.push(angular.copy({id: $scope.count, categoryCustom: $scope.categoryCustom}));
                        var data = DynamicFormService.getValuesOfComponentFromId(sectionData, $scope.generalSplitLotTemplate);
                        $scope.lotListTodisplay.push(angular.copy({id: $scope.count, category: data[0]}));
                        $scope.categoryCustom = DynamicFormService.resetSection($scope.generalSplitLotTemplate);
                        $scope.listFilled = true;
                    }
                }
            };
            $scope.editLotLocally = function (lotObj) {
                $scope.oldObj = angular.copy(lotObj, $scope.oldObj);
                if (lotObj != null) {
                    $scope.flag.editLot = true;
                    $scope.lotDataBean = {};
                    if (!!($scope.lotListToSave && $scope.lotListToSave.length > 0)) {
                        for (var i = 0; i < $scope.lotListToSave.length; i++) {
                            if ($scope.lotListToSave[i].id === lotObj.id) {
                                $scope.categoryCustom = angular.copy($scope.lotListToSave[i].categoryCustom);
                                $scope.lotDataBean.lotCustom = $scope.categoryCustom;
                                break;
                            }
                        }
                    }
                    $scope.lotDataBean.lotDbType = $scope.splitLotDbType;
                }
            };
            $scope.showPopUp = function (lotObj) {
                $scope.lotObjectToDelete = lotObj;
                $("#deletePopUp").modal('show');
            };
            $scope.hidePopUp = function () {
                $("#deletePopUp").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();

            };
            $scope.deleteLot = function () {
                if (!!($scope.lotListToSave && $scope.lotListTodisplay && $scope.lotObjectToDelete && $scope.lotListToSave.length > 0 && $scope.lotListTodisplay.length > 0)) {
                    var id = $scope.lotObjectToDelete.id;
                    var j = $scope.lotListTodisplay.indexOf($scope.lotObjectToDelete);
                    if (j !== -1) {
                        $scope.lotListTodisplay.splice(j, 1);
                    }
                    angular.forEach($scope.lotListToSave, function (itr) {
                        if (itr.id === id) {
                            var i = $scope.lotListToSave.indexOf(itr);
                            if (i !== -1) {
                                $scope.lotListToSave.splice(i, 1);
                            }
                        }
                    });
                }
                $scope.categoryCustom = DynamicFormService.resetSection($scope.generalSplitLotTemplate);
                $scope.flag.editLot = false;
                $("#deletePopUp").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();

            };
            $scope.saveLot = function (addLotForm) {
                if (Object.getOwnPropertyNames($scope.categoryCustom).length > 0) {
                    var mapHasValue = false;
                    for (var prop in $scope.categoryCustom) {
                        if (!!($scope.categoryCustom[prop])) {
                            mapHasValue = true;
                            break;
                        }
                    }
                    if (!!(mapHasValue && addLotForm.$valid && $scope.lotListToSave && $scope.lotListTodisplay && $scope.oldObj && $scope.lotListToSave.length > 0 && $scope.lotListTodisplay.length > 0)) {
                        var id = $scope.oldObj.id;
                        angular.forEach($scope.lotListTodisplay, function (itr) {
                            if (itr.id === id) {
                                var j = $scope.lotListTodisplay.indexOf(itr);
                                if (j !== -1) {
                                    $scope.lotListTodisplay.splice(j, 1);
                                }
                            }
                        });
                        angular.forEach($scope.lotListToSave, function (itr) {
                            if (itr.id === id) {
                                var i = $scope.lotListToSave.indexOf(itr);
                                if (i !== -1) {
                                    $scope.lotListToSave.splice(i, 1);
                                }
                            }
                        });
                        var sectionData = [];
                        sectionData.push({categoryCustom: angular.copy($scope.categoryCustom)});
                        $scope.count++;
                        $scope.lotListToSave.push(angular.copy({id: $scope.count, categoryCustom: $scope.categoryCustom}));
                        var data = DynamicFormService.getValuesOfComponentFromId(sectionData, $scope.generalSplitLotTemplate);
                        $scope.lotListTodisplay.push(angular.copy({id: $scope.count, category: data[0]}));
                        $scope.categoryCustom = DynamicFormService.resetSection($scope.generalSplitLotTemplate);
                        $scope.listFilled = true;
                    }
                    $scope.categoryCustom = DynamicFormService.resetSection($scope.generalSplitLotTemplate);
                    $scope.flag.editLot = false;
                }
            };
        }]);
});