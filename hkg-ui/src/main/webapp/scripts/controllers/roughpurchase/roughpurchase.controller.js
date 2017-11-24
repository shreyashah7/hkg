define(['hkg', 'customFieldService', 'roughPurchaseService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'printBarcodeValue', 'dynamicFormGrid', 'editableCustomGrid', 'accordionCollapse'], function (hkg) {
    hkg.register.controller('RoughPurchaseController', ["$rootScope", "$scope", "RoughPurchaseService", "DynamicFormService", "CustomFieldService", '$q', function ($rootScope, $scope, RoughPurchaseService, DynamicFormService, CustomFieldService, $q) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "RoughPurchase";
            $scope.roughPurchaseDataBean = {};
            $scope.search = {};
            $rootScope.activateMenu();
            $scope.isSearch = false;
            $scope.dbType = {};
            $scope.initPurchaseForm = function (purchaseForm) {
                $scope.purchaseForm = purchaseForm;
            };
            var featureMap = {};
            var currentDate = new Date();
            var currentYear = currentDate.getFullYear();
            $scope.searchCustom = DynamicFormService.resetSection($scope.generalPurchaseTemplate);
            var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("roughPurchaseAddEdit");

            $scope.createOrUpdateRoughPurchase = function (createOrUpdateRecord, dbType, callback) {
                $scope.isRoughPurchaseIdExists(createOrUpdateRecord, function (result) {
                    if (result !== undefined && (result['roughPurchaseExist'] == false || result['roughPurchaseExist'] == 'false')) {
                        $scope.roughPurchaseDataBean.roughPurchaseCustom = createOrUpdateRecord.categoryCustom;
                        $scope.roughPurchaseDataBean.id = createOrUpdateRecord.value;
                        $scope.roughPurchaseDataBean.roughPurchaseDbType = dbType;
                        $scope.roughPurchaseDataBean.isArchive = false;
                        $scope.roughPurchaseDataBean.uiFieldMap = $scope.map;
                        $scope.roughPurchaseDataBean.ruleConfigMap = {
                            fieldIdNameMap: $scope.purchaseFieldIdName,
                            featureName: "roughPurchase"
                        };
                        $scope.roughPurchaseDataBean.year = createOrUpdateRecord.year;
                        if (createOrUpdateRecord.categoryCustom['roughPurchaseID$AG$String'] != null) {
                            $scope.roughPurchaseDataBean.sequenceNumber = createOrUpdateRecord.categoryCustom['roughPurchaseID$AG$String'];
                        }
                        $rootScope.maskLoading();
                        if ($scope.roughPurchaseDataBean.id == null || $scope.roughPurchaseDataBean.id == undefined) {
                            RoughPurchaseService.createRoughPurchase($scope.roughPurchaseDataBean, function (response) {
                                var deferred = $q.defer();
                                var promise = deferred.promise;
                                promise.then(function () {
                                    callback(response);
                                });
                                deferred.resolve();
                                $rootScope.unMaskLoading();
                            }, function () {
                                $rootScope.unMaskLoading();
                                var msg = "Could not create purchase, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                            });
                        } else {
                            RoughPurchaseService.updateRoughPurchase($scope.roughPurchaseDataBean, function (response) {
                                var deferred = $q.defer();
                                var promise = deferred.promise;
                                promise.then(function () {
                                    callback(response);
                                });
                                deferred.resolve();
                                $rootScope.unMaskLoading();
                            }, function () {
                                $rootScope.unMaskLoading();
                                var msg = "Could not update rough purchase, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                            });
                        }
                    } else {
                        var msg = "Rough Purchase already exists for the same id.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                        var deferred = $q.defer();
                        var promise = deferred.promise;
                        promise.then(function () {
                            callback(null);
                        });
                        deferred.resolve();
                    }
                });

            };

            $scope.getNextPurchaseSequence = function (callback) {
                var cntPurchase = 0;
                if ($scope.editableGridPuchaseOptions.template !== undefined && $scope.editableGridPuchaseOptions.template !== null && $scope.editableGridPuchaseOptions.template.length > 0) {
                    for (var i = 0; i < $scope.editableGridPuchaseOptions.template.length; i++) {
                        if ($scope.editableGridPuchaseOptions.template[i].model === 'roughPurchaseID$AG$String') {
                            cntPurchase++;
                        }
                    }
                }
                if (cntPurchase > 0) {
                    RoughPurchaseService.getNextRoughPurchaseSequence(function (response) {
                        var deferred = $q.defer();
                        var promise = deferred.promise;
                        promise.then(function () {
                            callback(response);
                        });
                        deferred.resolve();
                    });
                } else {
                    var response = null;
                    var deferred = $q.defer();
                    var promise = deferred.promise;
                    promise.then(function () {
                        callback(response);
                    });
                    deferred.resolve();
                }
            };

            $scope.deleteRoughPurchase = function (deleteRecord) {
                RoughPurchaseService.deleteRoughPurchase(deleteRecord.value, function (response) {
                    $scope.retrieveRoughPurchase();
                    $rootScope.unMaskLoading();
                }, function () {
                    $rootScope.unMaskLoading();
                    var msg = "Could not delete purchase, please try again.";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                });
            };

            $scope.retrieveRoughPurchasePaginatedData = function () {
                if (!$scope.isSearch) {
                    var purchaseDataBean = {};
                    $scope.map = {};
                    $scope.map['dbFieldNames'] = $scope.editFieldName;
                    purchaseDataBean.uiFieldMap = $scope.map;
                    purchaseDataBean.offset = $scope.editableGridPuchaseOptions.datarows.length - 1;
                    purchaseDataBean.limit = 10;
                    purchaseDataBean.ruleConfigMap = {
                        fieldIdNameMap: $scope.purchaseFieldIdName,
                        featureName: "roughPurchase"
                    };
                    if ($scope.editFieldName.length > 0) {
                        RoughPurchaseService.retrieveRoughPurchases(purchaseDataBean, function (res) {
                            if (res !== undefined) {
                                angular.forEach(res, function (item) {
                                    var dataItem = angular.copy(item);
                                    dataItem.isEditGridFlag = false;
                                    if (dataItem.categoryCustom != null) {
                                        for (var key in dataItem.categoryCustom) {
                                            var split = key.split('$');
                                            if (split[1] === 'AG') {
                                                var value = dataItem.categoryCustom[key];
                                                if (value != null || value) {
                                                    var valueArr = value.split('-');
                                                    dataItem.categoryCustom[key] = valueArr[1];
                                                }
                                            }
                                        }
                                    }
                                    $scope.editableGridPuchaseOptions.datarows.push(angular.copy(dataItem));
                                    $scope.editableGridPuchaseOptions.datarowsFromDb.push(angular.copy(dataItem));
                                });
                            }
                            var success = function (result) {
                                angular.forEach(result, function (val) {
                                    $scope.editableGridPuchaseOptions.labelrows.push(val);
                                    $scope.editableGridPuchaseOptions.labelrowsFromDb.push(val);
                                })
                                if ($scope.editableGridPuchaseOptions.labelrows.length > 0) {
                                    $scope.allDataRetrieved = true;
                                } else {
                                    $scope.allDataRetrieved = false;
                                }
                            };
                            $scope.newVar = angular.copy(res);
                            if ($scope.newVar !== undefined) {
                                DynamicFormService.convertorForCustomField($scope.newVar, success);
                            }
                            $rootScope.unMaskLoading();
                        }, function () {
                            var msg = "Could not save, please try again.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        });
                    }
                } else {
                    if (Object.getOwnPropertyNames($scope.searchCustom).length > 0) {
                        $scope.searchFinal = angular.copy($scope.searchCustom);
                        var searchResult = DynamicFormService.convertSearchData($scope.generalPurchaseTemplate, null, null, null, $scope.searchFinal);
                        $scope.roughPurchaseDataBean.roughPurchaseCustom = searchResult;
                        $scope.roughPurchaseDataBean.searchOnParameter = true;
                        $scope.roughPurchaseDataBean.offset = $scope.editableGridPuchaseOptions.datarows.length - 1;
                        $scope.roughPurchaseDataBean.limit = 10;
                        $scope.roughPurchaseDataBean.uiFieldMap = $scope.map;
                        RoughPurchaseService.retrieveSearchedRoughPurchases($scope.roughPurchaseDataBean, function (res) {
                            if (res !== undefined && res !== null && res.length > 0) {
                                angular.forEach(res, function (item) {
                                    var dataItem = angular.copy(item);
                                    dataItem.isEditGridFlag = false;
                                    if (dataItem.categoryCustom != null) {
                                        for (var key in dataItem.categoryCustom) {
                                            var split = key.split('$');
                                            if (split[1] == 'AG') {
                                                var value = dataItem.categoryCustom[key];
                                                if (value != null && value) {
                                                    var valueArr = value.split('-');
                                                    dataItem.categoryCustom[key] = valueArr[1];
                                                }
                                            }
                                        }
                                    }
                                    $scope.editableGridPuchaseOptions.datarows.push(dataItem);
                                    $scope.editableGridPuchaseOptions.datarowsFromDb.push(dataItem);
                                });
                                $scope.newVar = angular.copy(res);
                                var success = function (result) {
                                    angular.forEach(result, function (item) {
                                        $scope.editableGridPuchaseOptions.labelrows.push(item);
                                        $scope.editableGridPuchaseOptions.labelrowsFromDb.push(item);
                                    })
                                };
                                if ($scope.newVar !== undefined) {
                                    DynamicFormService.convertorForCustomField($scope.newVar, success);
                                }
                            }
                        }, function () {
                            var msg = "Could not retrieve, please try again.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        });
                    }
                    else {
                        $scope.isSearch = false;
                        this.retrieveRoughPurchasePaginatedData();
                    }
                }

            };

            $scope.getTotalPurchaseCount = function (purchaseDataBean) {
                if (!$scope.isSearch) {
                    RoughPurchaseService.getTotalCountOfPurchases(function (resCount) {
                        $scope.editableGridPuchaseOptions.totalItems = resCount.totalItems + 1;
                        $scope.roughPurchaseTotalItems = resCount.totalItems + 1;
                    });
                } else {
                    RoughPurchaseService.getTotalCountOfSearchPurchases(purchaseDataBean, function (resCount) {
                        $scope.editableGridPuchaseOptions.totalItems = resCount.totalItems + 1;
                        $scope.roughPurchaseTotalItems = resCount.totalItems + 1;
                    });
                }
            };

            $scope.getTotalPurchaseCount();

            $scope.editableGridPuchaseOptions = {
                datarows: [],
                template: {},
                labelrows: [],
                labelrowsFromDb: [],
                datarowsFromDb: [],
                dbType: {},
                enableselection: false,
                createOrUpdateRecord: $scope.createOrUpdateRoughPurchase,
                deleteRecord: $scope.deleteRoughPurchase,
                deleteModalId: 'roughPurchaseModalPanel',
                cancelModalId: 'roughPurchaseCancelModalPanel',
                featureName: 'Rough Purchase',
                seqId: $scope.getNextPurchaseSequence,
                tableName: 'roughPurchaseTable',
                linked: false,
                retrievePaginatedRecord: $scope.retrieveRoughPurchasePaginatedData,
                totalItems: $scope.roughPurchaseTotalItems
            };
            templateData.then(function (section) {
                $scope.generalPurchaseTemplate = [];
                $scope.sectionData = [];
                angular.forEach(section['genralSection'], function (data) {
                    if (data.featureName == 'purchase') {
                        $scope.sectionData.push(data);
                        featureMap[data.model] = data.featureName;
                    }
                })
                $scope.generalPurchaseTemplate = angular.copy($scope.sectionData);
                $scope.searchResetFlag = true;
            }, function (reason) {
                console.log("reason :" + reason);
            }, function (update) {
                console.log("update :" + update);
            });

            $scope.retrieveRoughPurchase = function () {
                CustomFieldService.retrieveDesignationBasedFields("roughPurchaseAddEdit", function (response) {
                    $scope.response = angular.copy(response);
                    $scope.roughPurchaseDataBean = {};
                    var templateDataForUpdate = DynamicFormService.retrieveSectionWiseCustomFieldInfo("purchase");
                    if (!($scope.dbTypeForUpdate != null))
                        $scope.dbTypeForUpdate = {};
                    templateDataForUpdate.then(function (section) {
                        $scope.editableGridPuchaseOptions.template = null;
                        $scope.editableGridPuchaseOptions.template = section['genralSection'];
                        $scope.listOfModelsOfDateType = [];
                        var roughPurchaseField = [];
                        var result = Object.keys(response).map(function (key, value) {
                            angular.forEach(this[key], function (itr) {
                                if (key === 'Purchase') {
                                    roughPurchaseField.push({Purchase: itr});
                                }
                            });
                        }, response);
                        $scope.editFieldName = [];
                        $scope.purchaseFieldIdName = {};
                        $scope.editableGridPuchaseOptions.template = DynamicFormService.retrieveCustomData($scope.editableGridPuchaseOptions.template, roughPurchaseField);
                        angular.forEach($scope.editableGridPuchaseOptions.template, function (itr) {
                            if (itr.fromModel) {
                                $scope.purchaseFieldIdName[itr.fieldId] = itr.fromModel;
                                if ($scope.editFieldName.indexOf(itr.fromModel) === -1)
                                    $scope.editFieldName.push(itr.fromModel);
                            } else if (itr.toModel) {
                                $scope.purchaseFieldIdName[itr.fieldId] = itr.toModel;
                                if ($scope.editFieldName.indexOf(itr.toModel) === -1)
                                    $scope.editFieldName.push(itr.toModel);

                            } else if (itr.model) {
                                $scope.purchaseFieldIdName[itr.fieldId] = itr.model;
                                if ($scope.editFieldName.indexOf(itr.model) === -1)
                                    $scope.editFieldName.push(itr.model);
                            }
                        });
                        $scope.fieldNotConfigured = false;
                        $scope.showConfigMsg = false;
                        if ($scope.editableGridPuchaseOptions.template.length <= 0) {
                            $scope.showConfigMsg = true;
                        }
                        var purchaseDataBean = {};
                        $scope.map = {};
                        $scope.map['dbFieldNames'] = $scope.editFieldName;
                        purchaseDataBean.uiFieldMap = $scope.map;
                        purchaseDataBean.offset = 0;
                        purchaseDataBean.limit = 9;
                        purchaseDataBean.ruleConfigMap = {
                            fieldIdNameMap: $scope.purchaseFieldIdName,
                            featureName: "roughPurchase"
                        };
                        if ($scope.editFieldName.length > 0) {
                            $scope.getTotalPurchaseCount();
                            RoughPurchaseService.retrieveRoughPurchases(purchaseDataBean, function (res) {
                                if (res !== undefined) {
                                    $scope.editableGridPuchaseOptions.datarows = [];
                                    $scope.editableGridPuchaseOptions.labelrows = [];
                                    angular.forEach(res, function (item) {
                                        var dataItem = angular.copy(item);
                                        dataItem.isEditGridFlag = false;
                                        if (dataItem.categoryCustom != null) {
                                            for (var key in dataItem.categoryCustom) {
                                                var split = key.split('$');
                                                if (split[1] === 'AG') {
                                                    var value = dataItem.categoryCustom[key];
                                                    if (value != null || value) {
                                                        var valueArr = value.split('-');
                                                        dataItem.categoryCustom[key] = valueArr[1];
                                                    }
                                                }
                                            }
                                        }
                                        $scope.editableGridPuchaseOptions.datarows.push(angular.copy(dataItem));
                                    });
                                    var cntPurchase = 0;
                                    if ($scope.editableGridPuchaseOptions.template !== undefined && $scope.editableGridPuchaseOptions.template !== null && $scope.editableGridPuchaseOptions.template.length > 0) {
                                        for (var i = 0; i < $scope.editableGridPuchaseOptions.template.length; i++) {
                                            if ($scope.editableGridPuchaseOptions.template[i].model === 'roughPurchaseID$AG$String') {
                                                cntPurchase++;
                                            }
                                        }
                                    }
                                    if (cntPurchase > 0) {
                                        RoughPurchaseService.getNextRoughPurchaseSequence(function (result) {
                                            $scope.editableGridPuchaseOptions.datarowsFromDb = angular.copy($scope.editableGridPuchaseOptions.datarows);
                                            $scope.editableGridPuchaseOptions.datarows.push({isEditGridFlag: false, categoryCustom: {"roughPurchaseID$AG$String": result['roughPurchaseID$AG$String']}, beforeLabel: currentYear + "-"});
                                        });
                                    } else {
                                        $scope.editableGridPuchaseOptions.datarowsFromDb = angular.copy($scope.editableGridPuchaseOptions.datarows);
                                        $scope.editableGridPuchaseOptions.datarows.push({isEditGridFlag: false, categoryCustom: {}, beforeLabel: currentYear + "-"});
                                    }
                                }
                                var success = function (result) {
                                    $scope.editableGridPuchaseOptions.labelrows = angular.copy(result);
                                    $scope.editableGridPuchaseOptions.labelrows.push({isEditGridFlag: false});
                                    $scope.editableGridPuchaseOptions.labelrowsFromDb = angular.copy(result);
                                    if ($scope.editableGridPuchaseOptions.labelrows.length > 0) {
                                        $scope.allDataRetrieved = true;
                                    } else {
                                        $scope.allDataRetrieved = false;
                                    }
                                };
                                $scope.newVar = angular.copy(res);
                                if ($scope.newVar !== undefined) {
                                    DynamicFormService.convertorForCustomField($scope.newVar, success);
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

            $scope.retrieveRoughPurchase();

            $scope.isRoughPurchaseIdExists = function (createOrUpdateRecord, callback) {
                if ('roughPurchaseID$AG$String' in createOrUpdateRecord.categoryCustom && createOrUpdateRecord.categoryCustom['roughPurchaseID$AG$String'] !== null) {
                    var roughPurchaseObjectId;
                    if (createOrUpdateRecord.value !== null && createOrUpdateRecord.value !== undefined) {
                        roughPurchaseObjectId = createOrUpdateRecord.value;
                    } else {
                        roughPurchaseObjectId = null;
                    }
                    var map = {
                        roughPurchaseId: createOrUpdateRecord.categoryCustom['roughPurchaseID$AG$String'],
                        roughPurchaseObjectId: roughPurchaseObjectId
                    };
                    RoughPurchaseService.isRoughPurchaseIdExists(map, function (result) {
                        var deferred = $q.defer();
                        var promise = deferred.promise;
                        promise.then(function () {
                            callback(result);
                        });
                        deferred.resolve();
                    });
                } else {
                    var result = {"roughPurchaseExist": false};
                    var deferred = $q.defer();
                    var promise = deferred.promise;
                    promise.then(function () {
                        callback(result);
                    });
                    deferred.resolve();
                }
            };

            $scope.retrieveSearchedData = function () {
                $scope.isSearch = true;
                if (Object.getOwnPropertyNames($scope.searchCustom).length > 0) {
                    $scope.searchFinal = angular.copy($scope.searchCustom);
                    var searchResult = DynamicFormService.convertSearchData($scope.generalPurchaseTemplate, null, null, null, $scope.searchFinal);
                    $scope.roughPurchaseDataBean.roughPurchaseCustom = searchResult;
                    $scope.roughPurchaseDataBean.searchOnParameter = true;
                    $scope.roughPurchaseDataBean.offset = 0;
                    $scope.roughPurchaseDataBean.limit = 9;
                    $scope.roughPurchaseDataBean.uiFieldMap = $scope.map;
                    $scope.getTotalPurchaseCount(roughPurchaseDataBean);
                    RoughPurchaseService.retrieveSearchedRoughPurchases($scope.roughPurchaseDataBean, function (res) {
                        $scope.editableGridPuchaseOptions.datarows = [];
                        $scope.editableGridPuchaseOptions.labelrows = [];
                        var cntInvoice = 0;
                        if ($scope.editableGridPuchaseOptions.template !== undefined && $scope.editableGridPuchaseOptions.template !== null && $scope.editableGridPuchaseOptions.template.length > 0) {
                            for (var i = 0; i < $scope.editableGridPuchaseOptions.template.length; i++) {
                                if ($scope.editableGridPuchaseOptions.template[i].model === 'roughPurchaseID$AG$String') {
                                    cntInvoice++;
                                }
                            }
                        }
                        if (cntInvoice > 0) {
                            RoughPurchaseService.getNextRoughPurchaseSequence(function (result) {
                                $scope.editableGridPuchaseOptions.datarows.push({isEditGridFlag: false, categoryCustom: {"roughPurchaseID$AG$String": result['roughPurchaseID$AG$String']}, beforeLabel: currentYear + "-"});
                                angular.forEach(res, function (item) {
                                    var dataItem = angular.copy(item);
                                    dataItem.isEditGridFlag = false;
                                    if (dataItem.categoryCustom != null) {
                                        for (var key in dataItem.categoryCustom) {
                                            var split = key.split('$');
                                            if (split[1] == 'AG') {
                                                var value = dataItem.categoryCustom[key];
                                                if (value != null && value) {
                                                    var valueArr = value.split('-');
                                                    dataItem.categoryCustom[key] = valueArr[1];
                                                }
                                            }
                                        }
                                    }
                                    $scope.editableGridPuchaseOptions.datarows.push(dataItem);
                                    $scope.editableGridPuchaseOptions.datarowsFromDb.push(dataItem);
                                });
                            });
                        } else {
                            $scope.editableGridPuchaseOptions.datarows.push({isEditGridFlag: false, categoryCustom: {}, beforeLabel: currentYear + "-"});
                            angular.forEach(res, function (item) {
                                var dataItem = angular.copy(item);
                                dataItem.isEditGridFlag = false;
                                if (dataItem.categoryCustom != null) {
                                    for (var key in dataItem.categoryCustom) {
                                        var split = key.split('$');
                                        if (split[1] == 'AG') {
                                            var value = dataItem.categoryCustom[key];
                                            if (value != null && value) {
                                                var valueArr = value.split('-');
                                                dataItem.categoryCustom[key] = valueArr[1];
                                            }
                                        }
                                    }
                                }
                                $scope.editableGridPuchaseOptions.datarows.push(dataItem);
                                $scope.editableGridPuchaseOptions.datarowsFromDb.push(dataItem);
                            });
                        }
                        if (res !== undefined && res !== null && res.length > 0) {
                            $scope.newVar = angular.copy(res);
                            var success = function (result) {
                                $scope.editableGridPuchaseOptions.labelrows.push({isEditGridFlag: false});
                                angular.forEach(result, function (item) {
                                    $scope.editableGridPuchaseOptions.labelrows.push(item);
                                    $scope.editableGridPuchaseOptions.labelrowsFromDb.push(item);
                                })
                            };
                            if ($scope.newVar !== undefined) {
                                DynamicFormService.convertorForCustomField($scope.newVar, success);
                            }
                        }
                        if ($scope.editableGridPuchaseOptions.labelrows.length > 0) {
                            $scope.allDataRetrieved = true;
                        } else {
                            $scope.allDataRetrieved = false;
                        }
                    }, function () {
                        var msg = "Could not retrieve, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    });
                }
                else {
                    this.retrieveRoughPurchase()();
                }
            };

            $scope.onCancelOfSearch = function () {
                if ($scope.purchaseForm !== null && $scope.purchaseForm !== undefined) {
                    $scope.purchaseForm.$dirty = false;
                }
                $scope.isSearch = false;
                $scope.reset("searchCustom");
                $scope.retrieveRoughPurchase();
            };

            $scope.reset = function (sectionTobeReset) {
                if (sectionTobeReset === "searchCustom") {
                    $scope.searchCustom = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("roughPurchaseAddEdit");
                    templateData.then(function (section) {
                        $scope.generalPurchaseTemplate = section['genralSection'];
                        $rootScope.unMaskLoading();
                    }, function (reason) {
                    }, function (update) {
                    });
                }
            };
            $rootScope.unMaskLoading();
        }]);
});