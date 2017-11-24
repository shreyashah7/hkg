/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Author : Shreya
 */
define(['hkg', 'invoiceService', 'parcelService', 'roughPurchaseService', 'customFieldService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'printBarcodeValue', 'dynamicFormGrid', 'editableCustomGrid', 'accordionCollapse'], function (hkg) {
    hkg.register.controller('InvoiceController', ["$rootScope", "$scope", "InvoiceService", "ParcelService", "RoughPurchaseService", "DynamicFormService", "CustomFieldService", '$q', function ($rootScope, $scope, InvoiceService, ParcelService, RoughPurchaseService, DynamicFormService, CustomFieldService, $q) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "roughInvoice";
            $scope.entity = "INVOICE.";
            $scope.invoiceDataBean = {};
            $rootScope.activateMenu();
            var featureMap = {};
            $scope.searchCustom = DynamicFormService.resetSection($scope.generalInvoiceTemplate);
            var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("invoiceAddEdit");
            $scope.dbType = {};
            var currentDate = new Date();
            $scope.isSearch = false;
            var currentYear = currentDate.getFullYear();
            $scope.selectedAssociatedPurchases = [];
            $scope.selectedAssociatedParcels = [];
            $scope.initInvoiceForm = function (invoiceForm) {
                $scope.invoiceForm = invoiceForm;
            };
            $scope.createOrUpdateInvoice = function (createOrUpdateRecord, dbType, callback) {
                $scope.isInvoiceIdSequence(createOrUpdateRecord, function (result) {
                    if (result !== undefined && (result['invoiceExist'] == false || result['invoiceExist'] == 'false')) {
                        $scope.invoiceDataBean.invoiceCustom = createOrUpdateRecord.categoryCustom;
                        $scope.invoiceDataBean.id = createOrUpdateRecord.value;
                        $scope.invoiceDataBean.invoiceDbType = dbType;
                        $scope.invoiceDataBean.isArchive = false;
                        $scope.invoiceDataBean.uiFieldMap = $scope.map;
                        $scope.invoiceDataBean.ruleConfigMap = {
                            fieldIdNameMap: $scope.invoiceFieldIdNameMap,
                            featureName: "roughInvoice"
                        };
                        $scope.invoiceDataBean.year = createOrUpdateRecord.year;
                        if (createOrUpdateRecord.categoryCustom['invoiceId$AG$String'] != null) {
                            $scope.invoiceDataBean.sequenceNumber = createOrUpdateRecord.categoryCustom['invoiceId$AG$String'];
                        }
                        $rootScope.maskLoading();
                        if ($scope.invoiceDataBean.id == null || $scope.invoiceDataBean.id == undefined) {
                            InvoiceService.createInvoice($scope.invoiceDataBean, function (response) {
                                var deferred = $q.defer();
                                var promise = deferred.promise;
                                promise.then(function () {
                                    callback(response);
                                });
                                deferred.resolve();
                                $rootScope.unMaskLoading();
                            }, function () {
                                $rootScope.unMaskLoading();
                                var msg = "Could not create invoice, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                            });
                        } else {
                            InvoiceService.updateInvoice($scope.invoiceDataBean, function (response) {
                                var deferred = $q.defer();
                                var promise = deferred.promise;
                                promise.then(function () {
                                    callback(response);
                                });
                                deferred.resolve();
                                $rootScope.unMaskLoading();
                            }, function () {
                                $rootScope.unMaskLoading();
                                var msg = "Could not update invoice, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                            });
                        }
                    } else {
                        var msg = "Invoice already exists for the same id.";
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

            $scope.deleteInvoice = function (deleteRecord) {
                InvoiceService.deleteInvoice(deleteRecord.value, function (response) {
                    $scope.retrieveInvoices();
                    $rootScope.unMaskLoading();
                }, function () {
                    $rootScope.unMaskLoading();
                    var msg = "Could not delete invoice, please try again.";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                });
            };
            $scope.getNextInvoiceSequence = function (callback) {
                var cntInvoice = 0;
                if ($scope.editableGridInvoiceOptions.template !== undefined && $scope.editableGridInvoiceOptions.template !== null && $scope.editableGridInvoiceOptions.template.length > 0) {
                    for (var i = 0; i < $scope.editableGridInvoiceOptions.template.length; i++) {
                        if ($scope.editableGridInvoiceOptions.template[i].model === 'invoiceId$AG$String') {
                            cntInvoice++;
                        }
                    }
                }
                if (cntInvoice > 0) {
                    InvoiceService.getNextInvoiceSequence(function (response) {
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

            $scope.isInvoiceIdSequence = function (createOrUpdateRecord, callback) {
                if ('invoiceId$AG$String' in createOrUpdateRecord.categoryCustom && createOrUpdateRecord.categoryCustom['invoiceId$AG$String'] !== null) {
                    var invoiceObjectId;
                    if (createOrUpdateRecord.value !== null && createOrUpdateRecord.value !== undefined) {
                        invoiceObjectId = createOrUpdateRecord.value
                    } else {
                        invoiceObjectId = null;
                    }
                    var map = {
                        invoiceId: createOrUpdateRecord.categoryCustom['invoiceId$AG$String'],
                        invoiceObjectId: invoiceObjectId
                    };
                    InvoiceService.isInvoiceIdExists(map, function (result) {
                        var deferred = $q.defer();
                        var promise = deferred.promise;
                        promise.then(function () {
                            callback(result);
                        });
                        deferred.resolve();
                    });
                } else {
                    var result = {"invoiceExist": false};
                    var deferred = $q.defer();
                    var promise = deferred.promise;
                    promise.then(function () {
                        callback(result);
                    });
                    deferred.resolve();
                }
            };

            $scope.retrieveInvoicePaginatedData = function () {
//                $rootScope.maskLoading();
                if (!$scope.isSearch) {
                    $scope.parameters = ["invoiceAddEdit", "GEN"];
                    CustomFieldService.retrieveDesignationBasedFieldsBySection($scope.parameters, function (response) {
                        $scope.response = angular.copy(response);
                        $scope.invoiceDataBean = {};
                        var templateDataForUpdate = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                        if (!($scope.dbTypeForUpdate != null))
                            $scope.dbTypeForUpdate = {};
                        templateDataForUpdate.then(function (section) {
                            $scope.editableGridInvoiceOptions.template = null;
                            $scope.editableGridInvoiceOptions.template = section['genralSection'];
                            $scope.listOfModelsOfDateType = [];
                            var invoiceField = [];
                            var result = Object.keys(response).map(function (key, value) {
                                angular.forEach(this[key], function (itr) {
                                    if (key === 'Invoice') {
                                        invoiceField.push({Invoice: itr});
                                    }
                                });
                            }, response);
                            $scope.editFieldName = [];
                            $scope.invoiceFieldIdNameMap = {};
                            $scope.editableGridInvoiceOptions.template = DynamicFormService.retrieveCustomData($scope.editableGridInvoiceOptions.template, invoiceField);
                            angular.forEach($scope.editableGridInvoiceOptions.template, function (itr) {
                                if (itr.fromModel) {
                                    $scope.invoiceFieldIdNameMap[itr.fieldId] = itr.fromModel;
                                    if ($scope.editFieldName.indexOf(itr.fromModel) === -1)
                                        $scope.editFieldName.push(itr.fromModel);
                                } else if (itr.toModel) {
                                    $scope.invoiceFieldIdNameMap[itr.fieldId] = itr.toModel;
                                    if ($scope.editFieldName.indexOf(itr.toModel) === -1)
                                        $scope.editFieldName.push(itr.toModel);

                                } else if (itr.model) {
                                    $scope.invoiceFieldIdNameMap[itr.fieldId] = itr.model;
                                    if ($scope.editFieldName.indexOf(itr.model) === -1)
                                        $scope.editFieldName.push(itr.model);
                                }
                            });
                            $scope.invoiceShowConfigMsg = false;
                            var fieldsInTemplate = [];
                            if ($scope.editableGridInvoiceOptions.template !== undefined && $scope.editableGridInvoiceOptions.template !== null && $scope.editableGridInvoiceOptions.template.length > 0) {
                                for (var i = 0; i < $scope.editableGridInvoiceOptions.template.length; i++) {
                                    fieldsInTemplate.push(angular.copy($scope.editableGridInvoiceOptions.template[i].model));
                                }
                            }
                            if (fieldsInTemplate.length == 0) {
                                $scope.invoiceShowConfigMsg = true;
                            }
                            $scope.map = {};
                            $scope.map['dbFieldNames'] = $scope.editFieldName;
                            $scope.invoiceDataBean = {};
                            $scope.invoiceDataBean.uiFieldMap = $scope.map;
                            $scope.invoiceDataBean.offset = $scope.editableGridInvoiceOptions.datarows.length - 1;
                            $scope.invoiceDataBean.limit = 10;
                            $scope.invoiceDataBean.ruleConfigMap = {
                                fieldIdNameMap: $scope.invoiceFieldIdNameMap,
                                featureName: "roughInvoice"
                            };
                            if ($scope.editFieldName.length > 0) {
                                InvoiceService.retrieveInvoices($scope.invoiceDataBean, function (res) {
                                    if (res !== undefined) {
                                        angular.forEach(res, function (item) {
                                            var dataItem = angular.copy(item);
                                            dataItem.isEditGridFlag = false;
                                            dataItem.invoiceId = item.value;
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
                                            $scope.editableGridInvoiceOptions.datarows.push(dataItem);
                                            $scope.editableGridInvoiceOptions.datarowsFromDb.push(dataItem);
                                        });
                                    }
                                    var success = function (result) {
                                        angular.forEach(result, function (item) {
                                            $scope.editableGridInvoiceOptions.labelrows.push(angular.copy(item));
                                            $scope.editableGridInvoiceOptions.labelrowsFromDb.push(angular.copy(item));
                                        });
                                        if ($scope.editableGridInvoiceOptions.labelrows.length > 0) {
                                            $scope.allInvoiceDataRetrieved = true;
                                        } else {
                                            $scope.allInvoiceDataRetrieved = false;
                                        }
                                    }
                                    $scope.newVar = angular.copy(res);
                                    if ($scope.newVar !== undefined) {
                                        DynamicFormService.convertorForCustomField($scope.newVar, success, true);
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
                } else {
                    if (Object.getOwnPropertyNames($scope.searchCustom).length > 0) {
                        $scope.searchFinal = angular.copy($scope.searchCustom);
                        var finalMap = {};
                        var searchResult = DynamicFormService.convertSearchData($scope.generalInvoiceTemplate, null, null, null, $scope.searchFinal);
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
                        $scope.invoiceDataBean.featureCustomMapValue = finalMap;
                        $scope.invoiceDataBean.searchOnParameter = true;
                        $scope.invoiceDataBean.uiFieldMap = $scope.map;
                        $scope.invoiceDataBean.offset = $scope.editableGridInvoiceOptions.datarows.length - 1;
                        $scope.invoiceDataBean.limit = 10;
                        $scope.invoiceDataBean.ruleConfigMap = {
                            fieldIdNameMap: $scope.invoiceFieldIdNameMap,
                            featureName: "roughInvoice"
                        };
                        InvoiceService.search($scope.invoiceDataBean, function (res) {
                            if (res !== undefined && res !== null && res.length > 0) {
                                angular.forEach(res, function (item) {
                                    var dataItem = angular.copy(item);
                                    dataItem.isEditGridFlag = false;
                                    dataItem.invoiceId = item.value;
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
                                    $scope.editableGridInvoiceOptions.datarows.push(dataItem);
                                });

                                $scope.newVar = angular.copy(res);
                                var success = function (result) {
                                    $scope.editableGridInvoiceOptions.labelrows = angular.copy(result);
                                };
                                if ($scope.newVar !== undefined) {
                                    DynamicFormService.convertorForCustomField($scope.newVar, success, true);
                                }
                            }
                            if ($scope.editableGridInvoiceOptions.labelrows.length > 0) {
                                $scope.allInvoiceDataRetrieved = true;
                            } else {
                                $scope.allInvoiceDataRetrieved = false;
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
                        this.retrieveInvoicePaginatedData();
                        $rootScope.unMaskLoading();
                    }
                }
            };

            $scope.getTotalInvoiceCount = function (invoiceDataBean) {
                if (!$scope.isSearch) {
                    InvoiceService.getTotalCountOfInvoices(function (resCount) {
                        $scope.editableGridInvoiceOptions.totalItems = resCount.totalItems + 1;
                        $scope.invoiceTotalItems = resCount.totalItems + 1;
                    });
                } else {
                    InvoiceService.getTotalCountOfSearchInvoices(invoiceDataBean, function (resCount) {
                        $scope.editableGridInvoiceOptions.totalItems = resCount.totalItems + 1;
                        $scope.invoiceTotalItems = resCount.totalItems + 1;
                    });
                }
            };

            $scope.getTotalInvoiceCount();

            $scope.editableGridInvoiceOptions = {
                datarows: [],
                template: {},
                labelrows: [],
                labelrowsFromDb: [],
                datarowsFromDb: [],
                dbType: {},
                createOrUpdateRecord: $scope.createOrUpdateInvoice,
                deleteRecord: $scope.deleteInvoice,
                retrievePaginatedRecord: $scope.retrieveInvoicePaginatedData,
                totalItems: $scope.invoiceTotalItems,
                deleteModalId: 'invoiceModalPanel',
                cancelModalId: 'invoiceCancelModalPanel',
                featureName: 'Rough Invoice',
                seqId: $scope.getNextInvoiceSequence,
                tableName: 'invoiceTable',
                linked: false,
            };
            templateData.then(function (section) {
                featureDbFieldMap = {};
                $scope.generalInvoiceTemplate = [];
                $scope.generalInvoiceTemplate = section['genralSection'];
                $scope.searchInvoiceTemplate = [];
                $scope.searchParcelTemplate = [];
                if ($scope.generalInvoiceTemplate != null && $scope.generalInvoiceTemplate.length > 0) {
                    for (var i = 0; i < $scope.generalInvoiceTemplate.length; i++) {
                        var item = $scope.generalInvoiceTemplate[i];
                        if (item.featureName.toLowerCase() === 'invoice') {
                            $scope.searchInvoiceTemplate.push(angular.copy(item));
                        } else if (item.featureName.toLowerCase() === 'parcel') {
                            $scope.searchParcelTemplate.push(angular.copy(item));
                        }
                        featureMap[item.model] = item.featureName;
                    }
                }
                $scope.invoiceAddEditFlag = true;
            }, function (reason) {

            }, function (update) {

            });

            $scope.retrieveInvoices = function () {
                $scope.parameters = ["invoiceAddEdit", "GEN"];
                CustomFieldService.retrieveDesignationBasedFieldsBySection($scope.parameters, function (response) {
                    $scope.response = angular.copy(response);
                    $scope.invoiceDataBean = {};
                    var templateDataForUpdate = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                    if (!($scope.dbTypeForUpdate != null))
                        $scope.dbTypeForUpdate = {};
                    templateDataForUpdate.then(function (section) {
                        $scope.editableGridInvoiceOptions.template = null;
                        $scope.editableGridInvoiceOptions.template = section['genralSection'];
                        $scope.listOfModelsOfDateType = [];
                        var invoiceField = [];
                        var result = Object.keys(response).map(function (key, value) {
                            angular.forEach(this[key], function (itr) {
                                if (key === 'Invoice') {
                                    invoiceField.push({Invoice: itr});
                                }
                            });
                        }, response);
                        $scope.editFieldName = [];
                        $scope.invoiceFieldIdNameMap = {};
//                        $scope.editFieldIds = [];
                        $scope.editableGridInvoiceOptions.template = DynamicFormService.retrieveCustomData($scope.editableGridInvoiceOptions.template, invoiceField);
                        angular.forEach($scope.editableGridInvoiceOptions.template, function (itr) {
                            if (itr.fromModel) {
                                $scope.invoiceFieldIdNameMap[itr.fieldId] = itr.fromModel;
                                if ($scope.editFieldName.indexOf(itr.fromModel) === -1)
                                    $scope.editFieldName.push(itr.fromModel);
                            } else if (itr.toModel) {
                                $scope.invoiceFieldIdNameMap[itr.fieldId] = itr.toModel;
                                if ($scope.editFieldName.indexOf(itr.toModel) === -1)
                                    $scope.editFieldName.push(itr.toModel);

                            } else if (itr.model) {
                                $scope.invoiceFieldIdNameMap[itr.fieldId] = itr.model;
                                if ($scope.editFieldName.indexOf(itr.model) === -1)
                                    $scope.editFieldName.push(itr.model);
                            }
                        });
                        $scope.invoiceShowConfigMsg = false;
                        var fieldsInTemplate = [];
                        if ($scope.editableGridInvoiceOptions.template !== undefined && $scope.editableGridInvoiceOptions.template !== null && $scope.editableGridInvoiceOptions.template.length > 0) {
                            for (var i = 0; i < $scope.editableGridInvoiceOptions.template.length; i++) {
                                fieldsInTemplate.push(angular.copy($scope.editableGridInvoiceOptions.template[i].model));
                            }
                        }
                        if (fieldsInTemplate.length == 0) {
                            $scope.invoiceShowConfigMsg = true;
                        }
                        $scope.map = {};
                        $scope.map['dbFieldNames'] = $scope.editFieldName;
                        $scope.invoiceDataBean = {};
                        $scope.invoiceDataBean.uiFieldMap = $scope.map;
                        $scope.invoiceDataBean.offset = 0;
                        $scope.invoiceDataBean.limit = 9;
                        $scope.invoiceDataBean.ruleConfigMap = {
                            fieldIdNameMap: $scope.invoiceFieldIdNameMap,
                            featureName: "roughInvoice"
                        };
                        if ($scope.editFieldName.length > 0) {
                            $scope.getTotalInvoiceCount();
                            InvoiceService.retrieveInvoices($scope.invoiceDataBean, function (res) {
                                if (res !== undefined) {
                                    $scope.editableGridInvoiceOptions.datarows = [];
                                    $scope.editableGridInvoiceOptions.labelrows = [];
                                    var cntInvoice = 0;
                                    if ($scope.editableGridInvoiceOptions.template !== undefined && $scope.editableGridInvoiceOptions.template !== null && $scope.editableGridInvoiceOptions.template.length > 0) {
                                        for (var i = 0; i < $scope.editableGridInvoiceOptions.template.length; i++) {
                                            if ($scope.editableGridInvoiceOptions.template[i].model === 'invoiceId$AG$String') {
                                                cntInvoice++;
                                            }
                                        }
                                    }
                                    if (cntInvoice > 0) {
                                        InvoiceService.getNextInvoiceSequence(function (result) {
//                                            $scope.editableGridInvoiceOptions.datarowsFromDb = angular.copy($scope.editableGridInvoiceOptions.datarows);
                                            $scope.editableGridInvoiceOptions.datarows.push({isEditGridFlag: false, categoryCustom: {"invoiceId$AG$String": result['invoiceId$AG$String']}, beforeLabel: currentYear + "-"});
                                            angular.forEach(res, function (item) {
                                                var dataItem = angular.copy(item);
                                                dataItem.isEditGridFlag = false;
                                                dataItem.invoiceId = item.value;
                                                if (dataItem.categoryCustom != null) {
                                                    for (var key in dataItem.categoryCustom) {
                                                        var split = key.split('$');
                                                        if (split[1] == 'AG') {
//                                                    console.log("key :" + key)
                                                            var value = dataItem.categoryCustom[key];
                                                            if (value != null && value) {
                                                                var valueArr = value.split('-');
                                                                dataItem.categoryCustom[key] = valueArr[1];
                                                            }
                                                        }
                                                    }
                                                }
                                                $scope.editableGridInvoiceOptions.datarows.push(dataItem);
                                                $scope.editableGridInvoiceOptions.datarowsFromDb.push(dataItem);
                                            });
                                        });
                                    } else {
                                        $scope.editableGridInvoiceOptions.datarows.push({isEditGridFlag: false, categoryCustom: {}, beforeLabel: currentYear + "-"});
                                        angular.forEach(res, function (item) {
                                            var dataItem = angular.copy(item);
                                            dataItem.isEditGridFlag = false;
                                            dataItem.invoiceId = item.value;
                                            if (dataItem.categoryCustom != null) {
                                                for (var key in dataItem.categoryCustom) {
                                                    var split = key.split('$');
                                                    if (split[1] == 'AG') {
//                                                    console.log("key :" + key)
                                                        var value = dataItem.categoryCustom[key];
                                                        if (value != null && value) {
                                                            var valueArr = value.split('-');
                                                            dataItem.categoryCustom[key] = valueArr[1];
                                                        }
                                                    }
                                                }
                                            }
                                            $scope.editableGridInvoiceOptions.datarows.push(dataItem);
                                            $scope.editableGridInvoiceOptions.datarowsFromDb.push(dataItem);
                                        });
                                    }
                                }
                                var success = function (result) {
                                    $scope.editableGridInvoiceOptions.labelrows.push({isEditGridFlag: false});
                                    angular.forEach(result, function (item) {
                                        $scope.editableGridInvoiceOptions.labelrows.push(angular.copy(item));
                                        $scope.editableGridInvoiceOptions.labelrowsFromDb.push(angular.copy(item));
                                    });

                                    if ($scope.editableGridInvoiceOptions.labelrows.length > 0) {
                                        $scope.allInvoiceDataRetrieved = true;
                                    } else {
                                        $scope.allInvoiceDataRetrieved = false;
                                    }
                                }
                                $scope.newVar = angular.copy(res);
                                if ($scope.newVar !== undefined) {
                                    DynamicFormService.convertorForCustomField($scope.newVar, success, true);
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

            $scope.retrieveSearchedData = function () {
                $scope.isSearch = true;
                $scope.selectOneParameter = false;
                $scope.showParcels = false;
                $scope.searchedData = [];
                $scope.listFilled = false;
                if (Object.getOwnPropertyNames($scope.searchCustom).length > 0) {
                    $scope.searchFinal = angular.copy($scope.searchCustom);
                    var finalMap = {};
                    var searchResult = DynamicFormService.convertSearchData($scope.generalInvoiceTemplate, null, null, null, $scope.searchFinal);
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
                    $scope.invoiceDataBean.featureCustomMapValue = finalMap;
                    $scope.invoiceDataBean.searchOnParameter = true;
                    $scope.invoiceDataBean.uiFieldMap = $scope.map;
                    $scope.invoiceDataBean.offset = 1;
                    $scope.invoiceDataBean.limit = 10;
                    $scope.invoiceDataBean.ruleConfigMap = {
                        fieldIdNameMap: $scope.invoiceFieldIdNameMap,
                        featureName: "roughInvoice"
                    };
                    $scope.getTotalInvoiceCount();
                    InvoiceService.search($scope.invoiceDataBean, function (res) {
                        $scope.editableGridInvoiceOptions.datarows = [];
                        $scope.editableGridInvoiceOptions.labelrows = [];
                        var cntInvoice = 0;
                        if ($scope.editableGridInvoiceOptions.template !== undefined && $scope.editableGridInvoiceOptions.template !== null && $scope.editableGridInvoiceOptions.template.length > 0) {
                            for (var i = 0; i < $scope.editableGridInvoiceOptions.template.length; i++) {
                                if ($scope.editableGridInvoiceOptions.template[i].model === 'invoiceId$AG$String') {
                                    cntInvoice++;
                                }
                            }
                        }
                        if (cntInvoice > 0) {
                            InvoiceService.getNextInvoiceSequence(function (result) {
                                $scope.editableGridInvoiceOptions.datarows.push({isEditGridFlag: false, categoryCustom: {"invoiceId$AG$String": result['invoiceId$AG$String']}, beforeLabel: currentYear + "-"});
                                if (res !== undefined && res !== null && res.length > 0) {
                                    angular.forEach(res, function (item) {
                                        var dataItem = angular.copy(item);
                                        dataItem.isEditGridFlag = false;
                                        dataItem.invoiceId = item.value;
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
                                        $scope.editableGridInvoiceOptions.datarows.push(dataItem);
                                        $scope.editableGridInvoiceOptions.datarowsFromDb.push(dataItem);
                                    });

                                    $scope.newVar = angular.copy(res);
                                    var success = function (result) {
                                        $scope.editableGridInvoiceOptions.labelrows.push({isEditGridFlag: false});
                                        angular.forEach(result, function (item) {
                                            $scope.editableGridInvoiceOptions.labelrows.push(angular.copy(item));
                                            $scope.editableGridInvoiceOptions.labelrowsFromDb.push(angular.copy(item));
                                        });
                                    };
                                    if ($scope.newVar !== undefined) {
                                        DynamicFormService.convertorForCustomField($scope.newVar, success, true);
                                    }
                                }
                            });
                        } else {
                            $scope.editableGridInvoiceOptions.datarows.push({isEditGridFlag: false, categoryCustom: {}, beforeLabel: currentYear + "-"});
                            if (res !== undefined && res !== null && res.length > 0) {
                                angular.forEach(res, function (item) {
                                    var dataItem = angular.copy(item);
                                    dataItem.isEditGridFlag = false;
                                    dataItem.invoiceId = item.value;
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
                                    $scope.editableGridInvoiceOptions.datarows.push(dataItem);
                                    $scope.editableGridInvoiceOptions.datarowsFromDb.push(dataItem);
                                });

                                $scope.newVar = angular.copy(res);
                                var success = function (result) {
                                    $scope.editableGridInvoiceOptions.labelrows.push({isEditGridFlag: false});
                                    angular.forEach(result, function (item) {
                                        $scope.editableGridInvoiceOptions.labelrows.push(angular.copy(item));
                                        $scope.editableGridInvoiceOptions.labelrowsFromDb.push(angular.copy(item));
                                    });
                                };
                                if ($scope.newVar !== undefined) {
                                    DynamicFormService.convertorForCustomField($scope.newVar, success, true);
                                }
                            }
                        }
                        if ($scope.editableGridInvoiceOptions.labelrows.length > 0) {
                            $scope.allInvoiceDataRetrieved = true;
                        } else {
                            $scope.allInvoiceDataRetrieved = false;
                        }
                    }, function () {
                        var msg = "Could not retrieve, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    });
                }
                else {
                    this.retrieveInvoices();
                }
            };

            $scope.retrieveInvoices();

//-------------------------Code related to parcel starts from here----------------------------

            $scope.createOrUpdateParcel = function (createOrUpdateRecord, dbType, callback) {
                $scope.isParcelIdSequence(createOrUpdateRecord, function (result) {
                    if (result !== undefined && (result['parcelExist'] == false || result['parcelExist'] == 'false')) {
                        $scope.parcelDataBean = {};
                        $scope.parcelDataBean.parcelCustom = createOrUpdateRecord.categoryCustom;
                        $scope.parcelDataBean.id = createOrUpdateRecord.value;
                        $scope.parcelDataBean.parcelDbType = dbType;
                        $scope.parcelDataBean.invoiceDataBean = {id: $scope.selectedInvoice.value};
                        $scope.parcelDataBean.featureDbFieldMap = $scope.parcelMap;
                        $scope.parcelDataBean.ruleConfigMap = {
                            fieldIdNameMap: $scope.parcelFieldIdNameMap,
                            featureName: "roughInvoice"
                        };
                        if (createOrUpdateRecord.categoryCustom['parcelId$AG$String'] != null) {
                            $scope.parcelDataBean.sequenceNumber = createOrUpdateRecord.categoryCustom['parcelId$AG$String'];
                        }
                        $scope.parcelDataBean.year = createOrUpdateRecord.year;
                        $rootScope.maskLoading();
                        if ($scope.parcelDataBean.id == null || $scope.parcelDataBean.id == undefined) {
                            ParcelService.createParcel($scope.parcelDataBean, function (response) {
                                var deferred = $q.defer();
                                var promise = deferred.promise;
                                promise.then(function () {
                                    callback(response);
                                });
                                deferred.resolve();
                                $rootScope.unMaskLoading();
                            }, function () {
                                $rootScope.unMaskLoading();
                                var msg = "Could not create parcel, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                            });
                        } else {
                            ParcelService.updateParcel($scope.parcelDataBean, function (response) {
                                var deferred = $q.defer();
                                var promise = deferred.promise;
                                promise.then(function () {
                                    callback(response);
                                });
                                deferred.resolve();
                                $rootScope.unMaskLoading();
                            }, function () {
                                $rootScope.unMaskLoading();
                                var msg = "Could not update parcel, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                            });
                        }
                    } else {
                        var msg = "Parcel already exists for the same id.";
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

            $scope.isParcelIdSequence = function (createOrUpdateRecord, callback) {
                if ('parcelId$AG$String' in createOrUpdateRecord.categoryCustom && createOrUpdateRecord.categoryCustom['parcelId$AG$String'] !== null) {
                    var parcelObjectId;
                    if (createOrUpdateRecord.value !== null && createOrUpdateRecord.value !== undefined) {
                        parcelObjectId = createOrUpdateRecord.value;
                    } else {
                        parcelObjectId = null;
                    }
                    var map = {
                        parcelId: createOrUpdateRecord.categoryCustom['parcelId$AG$String'],
                        parcelObjectId: parcelObjectId
                    };
                    ParcelService.isParcelIdExists(map, function (result) {
                        var deferred = $q.defer();
                        var promise = deferred.promise;
                        promise.then(function () {
                            callback(result);
                        });
                        deferred.resolve();
                    });
                } else {
                    var result = {"parcelExist": false};
                    var deferred = $q.defer();
                    var promise = deferred.promise;
                    promise.then(function () {
                        callback(result);
                    });
                    deferred.resolve();
                }
            };

            $scope.deleteParcel = function (deleteRecord) {
                ParcelService.deleteParcel(deleteRecord.value, function (response) {
                    $scope.retrieveParcelByInvoiceId();
                    $rootScope.unMaskLoading();
                }, function () {
                    $rootScope.unMaskLoading();
                    var msg = "Could not delete parcel, please try again.";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                });
            };

            $scope.getNextParcelSequence = function (callback) {
                var cntParcel = 0;
                if ($scope.editableGridParcelOptions.template !== undefined && $scope.editableGridParcelOptions.template !== null && $scope.editableGridParcelOptions.template.length > 0) {
                    for (var i = 0; i < $scope.editableGridParcelOptions.template.length; i++) {
                        if ($scope.editableGridParcelOptions.template[i].model === 'parcelId$AG$String') {
                            cntParcel++;
                        }
                    }
                }
                if (cntParcel > 0) {
                    ParcelService.getNextParcelSequence(function (response) {
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

            $scope.delinkRoughPurchase = function (datarow) {
                if (datarow && datarow != null) {
                    ParcelService.deLinkRoughParcelWithPurchase(datarow.value, function (result) {
                        console.log("result");
                    });
                }
            };

            $scope.getTotalParcelCount = function (invoiceId) {
                ParcelService.getTotalCountOfParcels(invoiceId,function (resCount) {
                    $scope.editableGridParcelOptions.totalItems = resCount.totalItems + 1;
                    $scope.parcelTotalItems = resCount.totalItems + 1;
                });
            };

            $scope.retrieveParcelPaginatedData = function () {
                $scope.selectedInvoice = $scope.editableGridInvoiceOptions.getSelectedTableRows();
                if ($scope.selectedInvoice !== undefined && $scope.selectedInvoice !== null && $scope.selectedInvoice.value !== undefined && $scope.selectedInvoice.value !== null) {
                    $scope.parameters = ["invoiceAddEdit", "MRP"];
                    CustomFieldService.retrieveDesignationBasedFieldsBySection($scope.parameters, function (response) {
                        $scope.response = angular.copy(response);
                        var templateDataForUpdate = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
                        if (!($scope.dbTypeForUpdate != null))
                            $scope.dbTypeForUpdate = {};
                        templateDataForUpdate.then(function (section) {
                            $scope.editableGridParcelOptions.template = angular.copy(section['genralSection']);
                            var parcelField = [];
                            var result = Object.keys(response).map(function (key, value) {
                                angular.forEach(this[key], function (itr) {
                                    if (key === 'Parcel') {
                                        parcelField.push({Parcel: itr});
                                    }
                                });
                            }, response);
                            $scope.pareclEditFieldName = [];
                            $scope.parcelFieldIdNameMap = {};
                            $scope.editableGridParcelOptions.template = DynamicFormService.retrieveCustomData($scope.editableGridParcelOptions.template, parcelField);
                            angular.forEach($scope.editableGridParcelOptions.template, function (itr) {
                                if (itr.fromModel) {
                                    $scope.parcelFieldIdNameMap[itr.fieldId] = itr.fromModel;
                                    if ($scope.pareclEditFieldName.indexOf(itr.fromModel) === -1)
                                        $scope.pareclEditFieldName.push(itr.fromModel);
                                } else if (itr.toModel) {
                                    $scope.parcelFieldIdNameMap[itr.fieldId] = itr.toModel;
                                    if ($scope.pareclEditFieldName.indexOf(itr.toModel) === -1)
                                        $scope.pareclEditFieldName.push(itr.toModel);

                                } else if (itr.model) {
                                    $scope.parcelFieldIdNameMap[itr.fieldId] = itr.model;
                                    if ($scope.pareclEditFieldName.indexOf(itr.model) === -1)
                                        $scope.pareclEditFieldName.push(itr.model);
                                }
                            });
                            $scope.parcelMap = {};
                            var ids = [];
                            ids.push($scope.selectedInvoice.value);
                            $scope.editableGridParcelOptions.invoiceId = $scope.selectedInvoice.value;
                            $scope.parcelMap['dbFieldNames'] = angular.copy($scope.pareclEditFieldName);
                            $scope.parcelMap['invoiceObjectId'] = angular.copy(ids);
                            $scope.parcelDataBean = {};
                            $scope.parcelDataBean.featureDbFieldMap = $scope.parcelMap;
                            $scope.parcelDataBean.offset = $scope.editableGridParcelOptions.datarows.length - 1;
                            $scope.parcelDataBean.limit = 10;
                            $scope.parcelDataBean.ruleConfigMap = {
                                fieldIdNameMap: $scope.parcelFieldIdNameMap,
                                featureName: "roughInvoice"
                            };
                            if ($scope.pareclEditFieldName.length > 0) {
                                ParcelService.retrieveParcelByInvoiceId($scope.parcelDataBean, function (res) {
                                    if (res !== undefined) {
                                        angular.forEach(res, function (item) {
                                            var dataItem = angular.copy(item);
                                            dataItem.isEditGridFlag = false;
                                            dataItem.beforeLabel = currentYear + "-";
                                            dataItem.invoiceId = $scope.selectedInvoice;
                                            dataItem.parcelId = item.value;
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
                                                    if (key == 'linkPurchase') {
                                                        dataItem.isLinked = dataItem.categoryCustom[key];
                                                    }
                                                }
                                            }
                                            $scope.editableGridParcelOptions.datarows.push(dataItem);
                                            $scope.editableGridParcelOptions.datarowsFromDb.push(dataItem);
                                        });
                                    }
                                    var success = function (result) {
                                        angular.forEach(result, function (val) {
                                            $scope.editableGridParcelOptions.labelrows.push(val);
                                            $scope.editableGridParcelOptions.labelrowsFromDb.push(val);
                                        });
                                        if ($scope.editableGridParcelOptions.labelrows.length > 0) {
                                            $scope.allParcelDataRetrieved = true;
                                        } else {
                                            $scope.allParcelDataRetrieved = false;
                                        }
                                    };
                                    $scope.newVar = angular.copy(res);
                                    if ($scope.newVar !== undefined) {
                                        DynamicFormService.convertorForCustomField($scope.newVar, success, true);
                                    }
                                    $rootScope.unMaskLoading();
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
                }
            };

            $scope.editableGridParcelOptions = {
                datarows: [],
                datarowsFromDb: [],
                template: {},
                labelrows: [],
                labelrowsFromDb: [],
                dbType: {},
                createOrUpdateRecord: $scope.createOrUpdateParcel,
                deleteRecord: $scope.deleteParcel,
                deleteModalId: 'parcelModalPanel',
                cancelModalId: 'parcelCancelModalPanel',
                enableselection: false,
                featureName: 'Rough Parcel',
                seqId: $scope.getNextParcelSequence,
                tableName: 'parcelTable',
                linked: true,
                linkEntity: $scope.delinkRoughPurchase,
                invoiceId: $scope.selectedInvoice,
                retrievePaginatedRecord: $scope.retrieveParcelPaginatedData,
                totalItems: $scope.parcelTotalItems
            };

            $scope.$watch(function () {
                if (!!$scope.editableGridInvoiceOptions.getSelectedTableRows) {
                    return $scope.editableGridInvoiceOptions.getSelectedTableRows();
                }
            }, function (oldValue, newValue) {
                if (!!$scope.editableGridInvoiceOptions.getSelectedTableRows) {
                    if (angular.isArray($scope.editableGridInvoiceOptions.getSelectedTableRows())) {
                        if ($scope.editableGridInvoiceOptions.getSelectedTableRows().length > 0) {
                            $scope.showParcels = true;
                            $scope.retrieveParcelByInvoiceId();
                        }
                        else {
                            $scope.showParcels = false;
                        }
                    }
                    else if ($scope.editableGridInvoiceOptions.getSelectedTableRows() != null) {
                        $scope.showParcels = true;
                        $scope.retrieveParcelByInvoiceId();
                    }
                    else {
                        $scope.showParcels = false;
                    }
                }
            });

            $scope.retrieveParcelByInvoiceId = function () {
                $scope.selectedInvoice = $scope.editableGridInvoiceOptions.getSelectedTableRows();
                if ($scope.selectedInvoice !== undefined && $scope.selectedInvoice !== null && $scope.selectedInvoice.value !== undefined && $scope.selectedInvoice.value !== null) {
                    $scope.parameters = ["invoiceAddEdit", "MRP"];
                    CustomFieldService.retrieveDesignationBasedFieldsBySection($scope.parameters, function (response) {
                        $scope.response = angular.copy(response);
                        var templateDataForUpdate = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
                        if (!($scope.dbTypeForUpdate != null))
                            $scope.dbTypeForUpdate = {};
                        templateDataForUpdate.then(function (section) {
                            $scope.editableGridParcelOptions.template = angular.copy(section['genralSection']);
                            var parcelField = [];
                            var result = Object.keys(response).map(function (key, value) {
                                angular.forEach(this[key], function (itr) {
                                    if (key === 'Parcel') {
                                        parcelField.push({Parcel: itr});
                                    }
                                });
                            }, response);
                            $scope.pareclEditFieldName = [];
                            $scope.parcelFieldIdNameMap = {};
                            $scope.editableGridParcelOptions.template = DynamicFormService.retrieveCustomData($scope.editableGridParcelOptions.template, parcelField);
                            angular.forEach($scope.editableGridParcelOptions.template, function (itr) {
                                if (itr.fromModel) {
                                    $scope.parcelFieldIdNameMap[itr.fieldId] = itr.fromModel;
                                    if ($scope.pareclEditFieldName.indexOf(itr.fromModel) === -1)
                                        $scope.pareclEditFieldName.push(itr.fromModel);
                                } else if (itr.toModel) {
                                    $scope.parcelFieldIdNameMap[itr.fieldId] = itr.toModel;
                                    if ($scope.pareclEditFieldName.indexOf(itr.toModel) === -1)
                                        $scope.pareclEditFieldName.push(itr.toModel);

                                } else if (itr.model) {
                                    $scope.parcelFieldIdNameMap[itr.fieldId] = itr.model;
                                    if ($scope.pareclEditFieldName.indexOf(itr.model) === -1)
                                        $scope.pareclEditFieldName.push(itr.model);
                                }
                            });
                            $scope.fieldNotConfigured = false;
                            $scope.parcelShowConfigMsg = false;
                            var fieldsInTemplate = [];
                            $scope.mandatoryParcelFields = ['origPieces$NM$Long', 'origCarat$NM$Double', 'origRateInDollar$NM$Double', 'origAmountInDollar$FRM$Double'];
                            if ($scope.editableGridParcelOptions.template !== undefined && $scope.editableGridParcelOptions.template !== null && $scope.editableGridParcelOptions.template.length > 0) {
                                for (var i = 0; i < $scope.editableGridParcelOptions.template.length; i++) {
                                    if ($scope.editableGridParcelOptions.template[i].model === 'origPieces$NM$Long') {
                                        $scope.editableGridParcelOptions.template[i].required = true;
                                    } else if ($scope.editableGridParcelOptions.template[i].model === 'origCarat$NM$Double') {
                                        $scope.editableGridParcelOptions.template[i].required = true;
                                    } else if ($scope.editableGridParcelOptions.template[i].model === 'origRateInDollar$NM$Double') {
                                        $scope.editableGridParcelOptions.template[i].required = true;
                                    } else if ($scope.editableGridParcelOptions.template[i].model === 'origAmountInDollar$FRM$Double') {
                                        $scope.editableGridParcelOptions.template[i].required = true;
                                    }
                                    fieldsInTemplate.push(angular.copy($scope.editableGridParcelOptions.template[i].model));
                                }
                            }
                            if (fieldsInTemplate.length > 0 && $scope.mandatoryParcelFields != null && $scope.mandatoryParcelFields.length > 0) {
                                for (var field = 0; field < $scope.mandatoryParcelFields.length; field++) {
                                    if (fieldsInTemplate.indexOf($scope.mandatoryParcelFields[field]) === -1) {
                                        $scope.fieldNotConfigured = true;
                                        break;
                                    }
                                }
                            } else {
                                $scope.fieldNotConfigured = true;
                            }
                            if ($scope.fieldNotConfigured) {
                                $scope.parcelShowConfigMsg = true;
                            }
                            $scope.parcelMap = {};
                            var ids = [];
                            ids.push($scope.selectedInvoice.value);
                            $scope.editableGridParcelOptions.invoiceId = $scope.selectedInvoice.value;
                            $scope.parcelMap['dbFieldNames'] = angular.copy($scope.pareclEditFieldName);
                            $scope.parcelMap['invoiceObjectId'] = angular.copy(ids);
                            $scope.parcelDataBean = {};
                            $scope.parcelDataBean.featureDbFieldMap = $scope.parcelMap;
                            $scope.parcelDataBean.offset = 0;
                            $scope.parcelDataBean.limit = 9;
                            $scope.parcelDataBean.ruleConfigMap = {
                                fieldIdNameMap: $scope.parcelFieldIdNameMap,
                                featureName: "roughInvoice"
                            };
                            if ($scope.pareclEditFieldName.length > 0) {
                                $scope.getTotalParcelCount($scope.selectedInvoice.value);
                                ParcelService.retrieveParcelByInvoiceId($scope.parcelDataBean, function (res) {
                                    if (res !== undefined) {
                                        $scope.editableGridParcelOptions.datarows = [];
                                        $scope.editableGridParcelOptions.labelrows = [];
                                        var cntParcel = 0;
                                        if ($scope.editableGridParcelOptions.template !== undefined && $scope.editableGridParcelOptions.template !== null && $scope.editableGridParcelOptions.template.length > 0) {
                                            for (var i = 0; i < $scope.editableGridParcelOptions.template.length; i++) {
                                                if ($scope.editableGridParcelOptions.template[i].model === 'parcelId$AG$String') {
                                                    cntParcel++;
                                                }
                                            }
                                        }
                                        if (cntParcel > 0) {
                                            ParcelService.getNextParcelSequence(function (result) {
                                                $scope.editableGridParcelOptions.datarows.push({isEditGridFlag: false, categoryCustom: {"parcelId$AG$String": result['parcelId$AG$String']}, beforeLabel: currentYear + "-", isLinked: false});
                                                angular.forEach(res, function (item) {
                                                    var dataItem = angular.copy(item);
                                                    dataItem.isEditGridFlag = false;
                                                    dataItem.beforeLabel = currentYear + "-";
                                                    dataItem.invoiceId = $scope.selectedInvoice;
                                                    dataItem.parcelId = item.value;
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
                                                            if (key == 'linkPurchase') {
                                                                dataItem.isLinked = dataItem.categoryCustom[key];
                                                            }
                                                        }
                                                    }
                                                    $scope.editableGridParcelOptions.datarows.push(dataItem);
                                                    $scope.editableGridParcelOptions.datarowsFromDb.push(dataItem);
                                                });
                                            });
                                        } else {
                                            $scope.editableGridParcelOptions.datarows.push({isEditGridFlag: false, categoryCustom: {}, beforeLabel: currentYear + "-", isLinked: false});
                                            angular.forEach(res, function (item) {
                                                var dataItem = angular.copy(item);
                                                dataItem.isEditGridFlag = false;
                                                dataItem.beforeLabel = currentYear + "-";
                                                dataItem.invoiceId = $scope.selectedInvoice;
                                                dataItem.parcelId = item.value;
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
                                                        if (key == 'linkPurchase') {
                                                            dataItem.isLinked = dataItem.categoryCustom[key];
                                                        }
                                                    }
                                                }
                                                $scope.editableGridParcelOptions.datarows.push(dataItem);
                                                $scope.editableGridParcelOptions.datarowsFromDb.push(dataItem);
                                            });
                                        }
                                    }
                                    var success = function (result) {
                                        $scope.editableGridParcelOptions.labelrows.push({isEditGridFlag: false});
                                        angular.forEach(result, function (val) {
                                            $scope.editableGridParcelOptions.labelrows.push(val);
                                            $scope.editableGridParcelOptions.labelrowsFromDb.push(val);
                                        });
                                        if ($scope.editableGridParcelOptions.labelrows.length > 0) {
                                            $scope.allParcelDataRetrieved = true;
                                        } else {
                                            $scope.allParcelDataRetrieved = false;
                                        }
                                    };
                                    $scope.newVar = angular.copy(res);
                                    if ($scope.newVar !== undefined) {
                                        DynamicFormService.convertorForCustomField($scope.newVar, success, true);
                                    }
                                    $rootScope.unMaskLoading();
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
                }
            };

            $scope.onCancelOfSearch = function () {
                if ($scope.invoiceForm !== null && $scope.invoiceForm !== undefined) {
                    $scope.invoiceForm.$dirty = false;
                }
                $scope.searchedData = [];
                $scope.isSearch = false;
                $scope.submitted = false;
                $scope.selectedInvoice = [];
                $scope.reset("searchCustom");
                $scope.retrieveInvoices();
            };

            $scope.reset = function (sectionTobeReset) {
                if (sectionTobeReset === "searchCustom") {
                    $scope.searchCustom = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("invoiceAddEdit");
                    templateData.then(function (section) {
                        $scope.generalInvoiceTemplate = section['genralSection'];
                        $scope.invoiceAddEditFlag = true;
                        $rootScope.unMaskLoading();
                    }, function (reason) {
                    }, function (update) {
                    });
                }
            };

            //------Code For linking rough purchase starts from here

            $scope.linkRoughPurchase = function () {
                $scope.invoiceAddEditFlag = false;
                $scope.flag = {};
                $scope.retrieveAssociatedRoughPurchase();
                $scope.retrieveAssociatedRoughParcel();
//                $scope.onCancelOfSearch();
            };

            $scope.associatedParcelScreenRule = function (row, colField, toolTip) {
                var color;

                if (!!row.entity.screenRuleDetailsWithDbFieldName && row.entity.screenRuleDetailsWithDbFieldName[colField] !== undefined && row.entity.screenRuleDetailsWithDbFieldName[colField] !== null) {
                    if ($scope.selectedAssociatedParcels.length === 0 || ($scope.selectedAssociatedParcels.indexOf(row.entity.$$parcelId$$) === -1)) {
                        color = row.entity.screenRuleDetailsWithDbFieldName[colField].colorCode;
                    }
                    if (toolTip === true) {
                        color = row.entity.screenRuleDetailsWithDbFieldName[colField].tooltipMessage;
                    }
                }
                return color;
            };
            $scope.associatedPurchaseScreenRule = function (row, colField, toolTip) {
                var color;

                if (!!row.entity.screenRuleDetailsWithDbFieldName && row.entity.screenRuleDetailsWithDbFieldName[colField] !== undefined && row.entity.screenRuleDetailsWithDbFieldName[colField] !== null) {
                    if ($scope.selectedAssociatedPurchases.length === 0 || ($scope.selectedAssociatedPurchases.indexOf(row.entity.$$purchaseId$$) === -1)) {
                        color = row.entity.screenRuleDetailsWithDbFieldName[colField].colorCode;
                    }
                    if (toolTip === true) {
                        color = row.entity.screenRuleDetailsWithDbFieldName[colField].tooltipMessage;
                    }
                }
                return color;
            };

            $scope.retrieveAssociatedRoughPurchase = function () {
                $scope.searchedPurchaseData = [];
                $scope.searchedPurchaseDataFromDbForUiGrid = [];
                $scope.associatedPurchaseLabelListForUiGrid = [];
                $scope.associatedPurchaseListFilled = false;
                $scope.parameters = ["roughPurchaseLink", "GEN"];
                CustomFieldService.retrieveDesignationBasedFieldsBySection($scope.parameters, function (response) {
                    $scope.response = angular.copy(response);
                    var templateDataForAssociatedPurchase = DynamicFormService.retrieveSectionWiseCustomFieldInfo("purchase");
                    if (!($scope.dbTypeForAssociatedPurchase != null))
                        $scope.dbTypeForAssociatedPurchase = {};
                    templateDataForAssociatedPurchase.then(function (section) {
                        $scope.associatedRoughPurchaseTemplate = angular.copy(section['genralSection']);
                        var associatedPurchaseField = [];
                        var result = Object.keys(response).map(function (key, value) {
                            angular.forEach(this[key], function (itr) {
                                if (key === 'Purchase') {
                                    associatedPurchaseField.push({Purchase: itr});
                                }
                            });
                        }, response);
                        $scope.associatedPurchaseFieldName = [];
                        $scope.purchaseFieldIdToNameMap = {};
                        $scope.associatedRoughPurchaseTemplate = DynamicFormService.retrieveCustomData($scope.associatedRoughPurchaseTemplate, associatedPurchaseField);
                        angular.forEach($scope.associatedRoughPurchaseTemplate, function (itr) {
                            if (itr.fromModel) {
                                $scope.purchaseFieldIdToNameMap[itr.fieldId] = itr.fromModel;
                                if ($scope.associatedPurchaseFieldName.indexOf(itr.fromModel) === -1)
                                    $scope.associatedPurchaseFieldName.push(itr.fromModel);
                                $scope.associatedPurchaseLabelListForUiGrid.push({name: itr.fromModel, displayName: itr.label, minWidth: 200,
                                    cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.associatedPurchaseScreenRule(row, \'' + itr.fromModel + '\') }" title="{{grid.appScope.associatedPurchaseScreenRule(row, \'' + itr.fromModel + '\',true)}}"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                });
                            } else if (itr.toModel) {
                                $scope.purchaseFieldIdToNameMap[itr.fieldId] = itr.toModel;
                                if ($scope.associatedPurchaseFieldName.indexOf(itr.toModel) === -1)
                                    $scope.associatedPurchaseFieldName.push(itr.toModel);
                                $scope.associatedPurchaseLabelListForUiGrid.push({name: itr.toModel, displayName: itr.label, minWidth: 200,
                                    cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.associatedPurchaseScreenRule(row, \'' + itr.toModel + '\') }" title="{{grid.appScope.associatedPurchaseScreenRule(row, \'' + itr.toModel + '\',true)}}"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                });

                            } else if (itr.model) {
                                $scope.purchaseFieldIdToNameMap[itr.fieldId] = itr.model;
                                if ($scope.associatedPurchaseFieldName.indexOf(itr.model) === -1)
                                    $scope.associatedPurchaseFieldName.push(itr.model);
                                $scope.associatedPurchaseLabelListForUiGrid.push({name: itr.model, displayName: itr.label, minWidth: 200,
                                    cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.associatedPurchaseScreenRule(row, \'' + itr.model + '\') }" title="{{grid.appScope.associatedPurchaseScreenRule(row, \'' + itr.model + '\',true)}}"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                });
                            }
                        });
                        $scope.fieldNotConfiguredForPurchase = false;
                        $scope.showConfigMsgForPurchase = false;
                        if ($scope.associatedRoughPurchaseTemplate.length <= 0) {
                            $scope.showConfigMsgForPurchase = true;
                        }
                        var purchaseDataBean = {};
                        $scope.associatedPurchaseMap = {};
                        $scope.associatedPurchaseMap['dbFieldNames'] = angular.copy($scope.associatedPurchaseFieldName);
                        purchaseDataBean.uiFieldMap = $scope.associatedPurchaseMap;
                        purchaseDataBean.ruleConfigMap = {
                            fieldIdNameMap: $scope.purchaseFieldIdToNameMap,
                            featureName: "roughInvoice"
                        };
                        if ($scope.associatedPurchaseFieldName.length > 0) {
                            RoughPurchaseService.retrieveAssociatedRoughPurchase(purchaseDataBean, function (res) {
                                if (res !== undefined && res.length > 0) {
                                    var success = function (result) {
                                        $scope.searchedPurchaseData = angular.copy(result);
                                        angular.forEach($scope.searchedPurchaseData, function (itr) {
                                            angular.forEach($scope.associatedPurchaseLabelListForUiGrid, function (list) {
                                                if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                                    itr.categoryCustom[list.name] = "NA";
                                                }
                                                else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                                    if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                        itr.categoryCustom[list.name] = "NA";
                                                    }
                                                }
                                            });
                                            itr.categoryCustom.$$purchaseId$$ = itr.value;
                                            itr.categoryCustom.screenRuleDetailsWithDbFieldName = itr.screenRuleDetailsWithDbFieldName;
                                            $scope.searchedPurchaseDataFromDbForUiGrid.push(itr.categoryCustom);
                                        });
                                        $scope.associatedPurchaseGridOptions = {};
                                        $scope.associatedPurchaseGridOptions.enableFiltering = true;
                                        $scope.associatedPurchaseGridOptions.multiSelect = true;
                                        $scope.associatedPurchaseGridOptions.enableRowSelection = true;
                                        $scope.associatedPurchaseGridOptions.enableSelectAll = true;
                                        $scope.associatedPurchaseGridOptions.data = $scope.searchedPurchaseDataFromDbForUiGrid;
                                        $scope.associatedPurchaseGridOptions.columnDefs = $scope.associatedPurchaseLabelListForUiGrid;
                                        $scope.associatedPurchaseGridOptions.onRegisterApi = function (gridApi) {
                                            $scope.associatedPurchaseGridApi = gridApi;
                                            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                                                $scope.flag.purchaseRowSelectedflag = true;
                                                $scope.selectedAssociatedPurchases = [];
                                                angular.forEach(gridApi.selection.getSelectedRows(), function (item) {
                                                    $scope.selectedAssociatedPurchases.push(item.$$purchaseId$$);
                                                });
                                            });
                                            gridApi.selection.on.rowSelectionChangedBatch($scope, function (rows) {
                                                $scope.flag.purchaseRowSelectedflag = true;
                                                $scope.selectedAssociatedPurchases = [];
                                                angular.forEach(gridApi.selection.getSelectedRows(), function (item) {
                                                    $scope.selectedAssociatedPurchases.push(item.$$purchaseId$$);
                                                });
                                            });
                                        };
                                        window.setTimeout(function () {
                                            $(window).resize();
                                            $(window).resize();
                                        }, 100);
                                        $scope.associatedPurchaseListFilled = true;

                                        $rootScope.unMaskLoading();
                                    };
                                    DynamicFormService.convertorForCustomField(res, success, false);
                                }

                                $rootScope.unMaskLoading();
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

            $scope.retrieveAssociatedRoughParcel = function () {
                $scope.searchedParcelData = [];
                $scope.searchedParcelDataFromDbForUiGrid = [];
                $scope.associatedParcelLabelListForUiGrid = [];
                $scope.associatedParcelListFilled = false;
                $scope.parameters = ["roughPurchaseLink", "RPA"];
                CustomFieldService.retrieveDesignationBasedFieldsBySection($scope.parameters, function (response) {
                    $scope.response = angular.copy(response);
                    var templateDataForAssociatedParcel = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
                    if (!($scope.dbTypeForAssociatedParcel != null))
                        $scope.dbTypeForAssociatedParcel = {};
                    templateDataForAssociatedParcel.then(function (section) {
                        $scope.associatedRoughParcelTemplate = angular.copy(section['genralSection']);
                        var associatedParcelField = [];
                        var result = Object.keys(response).map(function (key, value) {
                            angular.forEach(this[key], function (itr) {
                                if (key === 'Parcel') {
                                    associatedParcelField.push({Parcel: itr});
                                }
                            });
                        }, response);
                        $scope.associatedParcelFieldName = [];
                        $scope.associatedParcelFieldIdName = {};
                        $scope.associatedRoughParcelTemplate = DynamicFormService.retrieveCustomData($scope.associatedRoughParcelTemplate, associatedParcelField);
                        angular.forEach($scope.associatedRoughParcelTemplate, function (itr) {
                            if (itr.fromModel) {
                                $scope.associatedParcelFieldIdName[itr.fieldId] = itr.fromModel;
                                if ($scope.associatedParcelFieldName.indexOf(itr.fromModel) === -1)
                                    $scope.associatedParcelFieldName.push(itr.fromModel);
                                $scope.associatedParcelLabelListForUiGrid.push({name: itr.fromModel, displayName: itr.label, minWidth: 200,
                                    cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.associatedParcelScreenRule(row, \'' + itr.fromModel + '\') }" title="{{grid.appScope.associatedParcelScreenRule(row, \'' + itr.fromModel + '\',true)}}"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                });
                            } else if (itr.toModel) {
                                $scope.associatedParcelFieldIdName[itr.fieldId] = itr.toModel;
                                if ($scope.associatedParcelFieldName.indexOf(itr.toModel) === -1)
                                    $scope.associatedParcelFieldName.push(itr.toModel);
                                $scope.associatedParcelLabelListForUiGrid.push({name: itr.toModel, displayName: itr.label, minWidth: 200,
                                    cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.associatedParcelScreenRule(row, \'' + itr.toModel + '\') }" title="{{grid.appScope.associatedParcelScreenRule(row, \'' + itr.toModel + '\',true)}}"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                });

                            } else if (itr.model) {
                                $scope.associatedParcelFieldIdName[itr.fieldId] = itr.model;
                                if ($scope.associatedParcelFieldName.indexOf(itr.model) === -1)
                                    $scope.associatedParcelFieldName.push(itr.model);
                                $scope.associatedParcelLabelListForUiGrid.push({name: itr.model, displayName: itr.label, minWidth: 200,
                                    cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.associatedParcelScreenRule(row, \'' + itr.model + '\') }" title="{{grid.appScope.associatedParcelScreenRule(row, \'' + itr.model + '\',true)}}"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                });
                            }
                        });
                        $scope.fieldNotConfiguredForAssParcel = false;
                        $scope.showConfigMsgForAssParcel = false;
                        if ($scope.associatedRoughParcelTemplate.length <= 0) {
                            $scope.showConfigMsgForAssParcel = true;
                        }
                        var parcelDataBean = {};
                        $scope.associatedParcelMap = {};
                        $scope.associatedParcelMap['dbFieldNames'] = angular.copy($scope.associatedParcelFieldName);
                        parcelDataBean.featureDbFieldMap = $scope.associatedParcelMap;
                        parcelDataBean.ruleConfigMap = {
                            fieldIdNameMap: $scope.associatedParcelFieldIdName,
                            featureName: "roughInvoice"
                        };
                        if ($scope.associatedParcelFieldName.length > 0) {
                            ParcelService.retrieveAssociatedRoughParcels(parcelDataBean, function (res) {
                                if (res !== undefined && res.length > 0) {
                                    var success = function (result) {
                                        $scope.searchedParcelData = angular.copy(result);
                                        angular.forEach($scope.searchedParcelData, function (itr) {
                                            angular.forEach($scope.associatedParcelLabelListForUiGrid, function (list) {
                                                if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                                    itr.categoryCustom[list.name] = "NA";
                                                }
                                                else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                                    if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                        itr.categoryCustom[list.name] = "NA";
                                                    }
                                                }

                                            });
                                            itr.categoryCustom.$$parcelId$$ = itr.value;
                                            itr.categoryCustom.screenRuleDetailsWithDbFieldName = itr.screenRuleDetailsWithDbFieldName;
                                            $scope.searchedParcelDataFromDbForUiGrid.push(itr.categoryCustom);
                                        });
                                        $scope.associatedParcelGridOptions = {};
                                        $scope.associatedParcelGridOptions.enableFiltering = true;
                                        $scope.associatedParcelGridOptions.multiSelect = true;
                                        $scope.associatedParcelGridOptions.enableRowSelection = true;
                                        $scope.associatedParcelGridOptions.enableSelectAll = true;
                                        $scope.associatedParcelGridOptions.data = $scope.searchedParcelDataFromDbForUiGrid;
                                        $scope.associatedParcelGridOptions.columnDefs = $scope.associatedParcelLabelListForUiGrid;
                                        $scope.associatedParcelGridOptions.onRegisterApi = function (gridApi) {
                                            $scope.associatedParcelGridApi = gridApi;
                                            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                                                $scope.flag.parcelRowSelectedflag = true;
                                                $scope.selectedAssociatedParcels = [];
                                                angular.forEach(gridApi.selection.getSelectedRows(), function (item) {
                                                    $scope.selectedAssociatedParcels.push(item.$$parcelId$$);
                                                });
                                            });
                                            gridApi.selection.on.rowSelectionChangedBatch($scope, function (rows) {
                                                $scope.flag.parcelRowSelectedflag = true;
                                                $scope.selectedAssociatedParcels = [];
                                                angular.forEach(gridApi.selection.getSelectedRows(), function (item) {
                                                    $scope.selectedAssociatedParcels.push(item.$$parcelId$$);
                                                });
                                            });
                                        };
                                        window.setTimeout(function () {
                                            $(window).resize();
                                            $(window).resize();
                                        }, 100);
                                        $scope.associatedParcelListFilled = true;

                                        $rootScope.unMaskLoading();
                                    };
                                    DynamicFormService.convertorForCustomField(res, success, false);
                                }

                                $rootScope.unMaskLoading();
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

            $scope.cancelLinkingRoughPurchase = function () {
                $scope.retrieveAssociatedRoughParcel();
                $scope.retrieveAssociatedRoughPurchase();
            };

            $scope.backToMainPage = function () {
                $scope.invoiceAddEditFlag = true;
            };

            $scope.linkPurchase = function () {
                if ($scope.associatedParcelGridApi.selection.getSelectedRows().length > 0 && $scope.associatedPurchaseGridApi.selection.getSelectedRows().length > 0) {
                    $scope.selectedParcels = angular.copy($scope.associatedParcelGridApi.selection.getSelectedRows());
                    $scope.selectedPurchases = angular.copy($scope.associatedPurchaseGridApi.selection.getSelectedRows());
                    $scope.associatedParcelIds = [];
                    $scope.associatedPurchaseIds = [];
                    angular.forEach($scope.selectedParcels, function (parcel) {
                        $scope.associatedParcelIds.push(parcel['$$parcelId$$']);
                    });
                    angular.forEach($scope.selectedPurchases, function (purchase) {
                        $scope.associatedPurchaseIds.push(purchase['$$purchaseId$$']);
                    });
                    $scope.sendData = {
                        parcelIds: $scope.associatedParcelIds,
                        purchaseIds: $scope.associatedPurchaseIds
                    };
                    InvoiceService.linkRoughParcelWithPurchase($scope.sendData, function (res) {
                        $scope.retrieveAssociatedRoughParcel();
                        $scope.retrieveAssociatedRoughPurchase();
                    });
                }
            };
        }]);
});