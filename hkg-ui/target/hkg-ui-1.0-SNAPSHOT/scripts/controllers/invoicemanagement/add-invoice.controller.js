define(['hkg', 'invoiceService', 'customFieldService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'printBarcodeValue', 'dynamicForm'], function(hkg, invoiceService) {
    hkg.register.controller('AddInvoiceController', ["$rootScope", "$scope", "InvoiceService", "$timeout", "$filter", "$location", "$window", "DynamicFormService", "CustomFieldService", function($rootScope, $scope, InvoiceService, $timeout, $filter, $location, $window, DynamicFormService, CustomFieldService) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "addInvoice";
            $rootScope.activateMenu();
            $scope.flag = {};
            $rootScope.maskLoading();
            $scope.invoiceDataBean = {};
            $scope.showAddInvoicePage = true;
//            InvoiceService.fieldSequencExist("invoiceID$AG$String", function(response) {
//                if (response.data) {
            CustomFieldService.retrieveDesignationBasedFields("invoiceAdd", function(response) {
                $scope.response = response;
                $scope.invoiceCustom = {};
                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                $scope.dbType = {};
                templateData.then(function(section) {
                    var invoiceField = [];
                    var result = Object.keys(response).map(function(key, value) {
                        angular.forEach(this[key], function(itr) {
                            if (key === 'Invoice') {
                                invoiceField.push({Invoice: itr});
                            }
                        });
                    }, response);
                    $scope.generaInvoiceTemplate = section['genralSection'];
                    $scope.generaInvoiceTemplate = DynamicFormService.retrieveCustomData($scope.generaInvoiceTemplate, invoiceField);
                    $scope.fieldNotConfigured = false;
                    $scope.showConfigMsg = false;
                    var fieldsInTemplate = [];
                    $scope.mandatoryFields = ['carat_of_invoice$NM$Double', 'in_stock_of_invoice$UMS$String', 'invoiceNumber$TF$String'];
                    if ($scope.generaInvoiceTemplate !== undefined && $scope.generaInvoiceTemplate !== null && $scope.generaInvoiceTemplate.length > 0) {
                        for (var i = 0; i < $scope.generaInvoiceTemplate.length; i++) {
                            if ($scope.generaInvoiceTemplate[i].model === 'carat_of_invoice$NM$Double') {
                                $scope.generaInvoiceTemplate[i].required = true;
                            } else if ($scope.generaInvoiceTemplate[i].model === 'in_stock_of_invoice$UMS$String') {
                                $scope.generaInvoiceTemplate[i].required = true;
                            } else if ($scope.generaInvoiceTemplate[i].model === 'invoiceNumber$TF$String') {
                                $scope.generaInvoiceTemplate[i].required = true;
                            }
                            fieldsInTemplate.push(angular.copy($scope.generaInvoiceTemplate[i].model));
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
                    $scope.invoiceAddShow = true;
                    $rootScope.unMaskLoading();
                    $scope.flag.customFieldGenerated = true;
                    $scope.invoiceDataBean.invoiceDbType = $scope.dbType;
                }, function(reason) {
                }, function(update) {
                });
            }, function() {
                $rootScope.unMaskLoading();
                var msg = "Failed to retrieve data";
                var type = $rootScope.error;
                $rootScope.addMessage(msg, type);
            });
            $scope.onCanel = function() {

                $scope.invoiceAddShow = false;
                if ($scope.addInvoiceForm != null) {
                    $scope.addInvoiceForm.$dirty = false;
                }
                $scope.submitted = false;
                $scope.invoiceAddShow = false;
                $scope.reset();
                $scope.invoiceAddShow = true;
            };
            $scope.initAddInvoiceForm = function(addInvoiceForm) {
                $scope.addInvoiceForm = addInvoiceForm;
            };
            // This reset method is used to clear the form and to set the default values
            $scope.reset = function()
            {
                $scope.invoiceCustom = {};
                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                $scope.dbType = {};
                templateData.then(function(section) {
                    var invoiceField = [];
                    var result = Object.keys($scope.response).map(function(key, value) {
                        angular.forEach(this[key], function(itr) {
                            if (key === 'Invoice') {
                                invoiceField.push({Invoice: itr});
                            }
                        });
                    }, $scope.response);
                    $scope.generaInvoiceTemplate = section['genralSection'];
                    $scope.generaInvoiceTemplate = DynamicFormService.retrieveCustomData($scope.generaInvoiceTemplate, invoiceField);
                    $scope.invoiceAddShow = true;
                    $rootScope.unMaskLoading();
                    $scope.flag.customFieldGenerated = true;
                    $scope.invoiceDataBean.invoiceDbType = $scope.dbType;
                }, function(reason) {
                }, function(update) {
                });
            };
            $scope.saveInvoice = function(addInvoiceForm) {
                console.log("error in form" + JSON.stringify(addInvoiceForm.$error));

                $scope.submitted = true;
                $scope.invoiceDataBean.invoiceCustom = angular.copy($scope.invoiceCustom);
                if (Object.getOwnPropertyNames($scope.invoiceDataBean.invoiceCustom).length > 0) {
                    var mapHasValue = false;
                    for (var prop in $scope.invoiceCustom) {
                        if (typeof $scope.invoiceCustom[prop] === 'object' && $scope.invoiceCustom[prop] != null) {
                            var toString = angular.copy($scope.invoiceCustom[prop].toString());
                            if (typeof toString === 'string' && toString !== null && toString !== undefined && toString.length > 0) {
                                mapHasValue = true;
                                break;
                            }
                        }
                        if (typeof $scope.invoiceCustom[prop] === 'string' && $scope.invoiceCustom[prop] !== null && $scope.invoiceCustom[prop] !== undefined && $scope.invoiceCustom[prop].length > 0) {
                            mapHasValue = true;
                            break;
                        }
                        if (typeof $scope.invoiceCustom[prop] === 'number' && !!($scope.invoiceCustom[prop])) {
                            mapHasValue = true;
                            break;
                        }
                        if (typeof $scope.invoiceCustom[prop] === 'boolean') {
                            mapHasValue = true;
                            break;
                        }
                    }
                    if (addInvoiceForm.$valid && mapHasValue) {
                        $scope.invoiceAddShow = false;
                        $scope.createdInvoiceId = undefined;
                        InvoiceService.create($scope.invoiceDataBean, function(response) {
                            $rootScope.showParcelLink = true;
                            console.log(JSON.stringify(response));
                            $scope.createdInvoiceId = angular.copy(response.primaryKey);
                            $scope.onCanel();
                            $scope.invoiceAddShow = true;
                            $timeout(function() {
                                $rootScope.showParcelLink = false;
                            }, 30000);
                        }, function() {
                            $rootScope.unMaskLoading();
                            var msg = "Could not create invoice, please try again.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                        });
                    }
                }

            };
//                } else {
//                    $rootScope.unMaskLoading();
//                    $scope.flag.fieldSeqNotExist = true;
//                }
//            }, function() {
//                $rootScope.unMaskLoading();
//                var msg = "Failed to retrieve stock id";
//                var type = $rootScope.error;
//                $rootScope.addMessage(msg, type);
//            });

            $scope.addParcel = function() {
                if ($scope.createdInvoiceId !== undefined) {
                    $rootScope.maskLoading();
                    $rootScope.createdInvoiceId = angular.copy($scope.createdInvoiceId);
                    $location.path('/addparcel');
                    $rootScope.showParcelLink = false;

                }
            };
        }]);
});