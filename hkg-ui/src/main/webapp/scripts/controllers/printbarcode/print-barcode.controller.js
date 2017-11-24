define(['hkg', 'printBarcodeService', 'customFieldService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'printBarcodeValue', 'dynamicForm'], function (hkg, printBarcodeService) {
    hkg.register.controller('PrintBarcodeController', ["$rootScope", "$scope", "PrintBarcodeService", "DynamicFormService", "$q", function ($rootScope, $scope, PrintBarcodeService, DynamicFormService, $q) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "printBarcode";
            $rootScope.activateMenu();
            var featureMap = {};
            $scope.initializeData = function () {
                $scope.printBarcodeDataBean = {};
                $scope.submitted = false;
                $scope.flag = {};
                $scope.searchResetFlag = false;
                $scope.flag.rowSelectedflag = false;
                $scope.searchedData = [];
                $scope.searchedDataFromDb = [];
                $scope.listFilled = false;
                $scope.fieldList = [{"id": "Invoice", "text": "Invoice"}, {"id": "Parcel", "text": "Parcel"}, {"id": "Lot", "text": "Lot"}, {"id": "Packet", "text": "Packet"}, {"id": "Sell", "text": "Sell"}, {"id": "Transfer", "text": "Transfer"}, {"id": "Lot Slip", "text": "Lot Slip"}, {"id": "Packet Slip", "text": "Packet Slip"}];
                $scope.searchCustom = {};
                var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("barcodePrint");
                $scope.dbType = {};
                $scope.stockLabelListForUiGrid = [];
                $scope.gridOptions = {};
                $scope.gridOptions.enableFiltering = true;
                $scope.gridOptions.multiSelect = true;
                $scope.gridOptions.enableRowSelection = true;
                $scope.gridOptions.enableSelectAll = true;
                $scope.gridOptions.columnDefs = [];
                $scope.gridOptions.data = [];
                $scope.gridOptions.selectedItems = [];
                $scope.gridOptions.onRegisterApi = function (gridApi) {
                    //set gridApi on scope
                    $scope.gridApi = gridApi;
                    gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                        if ($scope.gridApi.selection.getSelectedRows().length > 0) {
                            $scope.flag.rowSelectedflag = true;
                        } else {
                            $scope.flag.rowSelectedflag = false;
                        }
                    });
                    gridApi.selection.on.rowSelectionChangedBatch($scope, function (rows) {
                        if ($scope.gridApi.selection.getSelectedRows().length > 0) {
                            $scope.flag.rowSelectedflag = true;
                        } else {
                            $scope.flag.rowSelectedflag = false;
                        }
                    });
                };
                templateData.then(function (section) {
                    $scope.searchInvoiceTemplate = [];
                    $scope.searchParcelTemplate = [];
                    $scope.searchLotTemplate = [];
                    $scope.searchPacketTemplate = [];
                    $scope.generalSearchTemplate = section['genralSection'];
                    if ($scope.generalSearchTemplate !== undefined && $scope.generalSearchTemplate !== null && $scope.generalSearchTemplate.length > 0) {
                        $scope.flag.configSearchFlag = false;
                        for (var i = 0; i < $scope.generalSearchTemplate.length; i++) {
                            var item = $scope.generalSearchTemplate [i];
                            if (item.featureName.toLowerCase() === 'invoice') {
                                $scope.searchInvoiceTemplate.push(angular.copy(item));
                            } else if (item.featureName.toLowerCase() === 'parcel') {
                                $scope.searchParcelTemplate.push(angular.copy(item));
                            }
                            else if (item.featureName.toLowerCase() === 'lot') {
                                $scope.searchLotTemplate.push(angular.copy(item));
                            }
                            else if (item.featureName.toLowerCase() === 'packet') {
                                $scope.searchPacketTemplate.push(angular.copy(item));
                            }
                            featureMap[item.model] = item.featureName;
                            if (item.fromModel) {
                                $scope.stockLabelListForUiGrid.push({name: item.fromModel, displayName: item.label, minWidth: 200});
                            } else if (item.toModel) {
                                $scope.stockLabelListForUiGrid.push({name: item.toModel, displayName: item.label, minWidth: 200});
                            } else if (item.model) {
                                $scope.stockLabelListForUiGrid.push({name: item.model, displayName: item.label, minWidth: 200});
                            }

                        }
                    } else {
                        $scope.flag.configSearchFlag = true;
                    }
                    $scope.searchResetFlag = true;
                }, function (reason) {

                }, function (update) {

                });
            };
            $scope.initPrintBarcodeForm = function (printBarcodeForm) {
                $scope.printBarcodeForm = printBarcodeForm;
            };

            $scope.reset = function (sectionTobeReset) {
                if (sectionTobeReset === "searchCustom") {
                    $scope.searchCustom = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("barcodePrint");
                    $scope.dbType = {};
                    templateData.then(function (section) {
                        $scope.generalSearchTemplate = section['genralSection'];
                        $scope.searchResetFlag = true;
                    }, function (reason) {
                        console.log("reason :" + reason);
                    }, function (update) {
                        console.log("update :" + update);
                    });
                }
            };

            $scope.onCancelOfSearch = function () {
                if ($scope.printBarcodeForm !== null) {
                    $scope.printBarcodeForm.$dirty = false;
                }
                $scope.listFilled = false;
                $scope.searchResetFlag = false;
                $scope.reset("searchCustom");
                $scope.initializeData();
                $scope.printBarcodeForm.$setPristine();
            };

            $scope.retrieveSearchedData = function () {
                $scope.selectOneParameter = false;
                $scope.searchedData = [];
                $scope.searchedDataFromDb = [];
                $scope.listFilled = false;
                $scope.stockList = [];
                $scope.submitted = true;
                $scope.gridOptions.columnDefs = [];
                $scope.gridOptions.data = [];
                if ($scope.printBarcodeForm.$valid) {
                    if (Object.getOwnPropertyNames($scope.searchCustom).length > 0) {
                        $rootScope.maskLoading();
                        var mapHasValue = false;
                        for (var prop in $scope.searchCustom) {
                            if (typeof $scope.searchCustom[prop] === 'object' && $scope.searchCustom[prop] !== null) {
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
                            $scope.printBarcodeDataBean.featureCustomMapValue = {};
                            $scope.map = {};
                            var finalMap = {};
                            var searchResult = DynamicFormService.convertSearchData($scope.searchInvoiceTemplate, $scope.searchParcelTemplate, $scope.searchLotTemplate, $scope.searchPacketTemplate, $scope.searchCustom);
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
                                    vlaueOfCus = searchResult['to' + label];
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
                                    vlaueOfCus = searchResult['from' + label];
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
                            $scope.printBarcodeDataBean.featureCustomMapValue = finalMap;
                            $scope.printBarcodeDataBean.featureMap = featureMap;
                            PrintBarcodeService.retrieveSearchedStock($scope.printBarcodeDataBean, function (res) {
                                $scope.searchedDataFromDb = angular.copy(res);
                                var success = function ()
                                {
                                    angular.forEach($scope.searchedDataFromDb, function (itr) {
                                        angular.forEach($scope.stockLabelListForUiGrid, function (list) {
                                            if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                                itr.categoryCustom[list.name] = "NA";
                                            }
                                            else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                                if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                    itr.categoryCustom[list.name] = "NA";
                                                }
                                            }
                                            if (itr.hasOwnProperty("commonId")) {
                                                itr.categoryCustom["~@commonId"] = itr.commonId;
                                            }
                                        });
                                        $scope.stockList.push(itr.categoryCustom);
                                    });
                                    $scope.gridOptions.data = $scope.stockList;
                                    $scope.gridOptions.columnDefs = $scope.stockLabelListForUiGrid;
                                    $scope.listFilled = true;
                                    $scope.flag.configSearchFlag = false;
                                    window.setTimeout(function () {
                                        $(window).resize();
                                        $(window).resize();
                                    }, 100);
                                };
                                DynamicFormService.convertorForCustomField($scope.searchedDataFromDb, success);
                                $rootScope.unMaskLoading();
                            }, function () {
                                var msg = "Could not retrieve, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                                $rootScope.unMaskLoading();
                            });
                        } else {
                            var msg = "Please select atleast one search criteria for search";
                            var type = $rootScope.warning;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        }
                    } else {
                        var msg = "Please select atleast one search criteria for search";
                        var type = $rootScope.warning;
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    }
                }
                $rootScope.unMaskLoading();
            };

            $scope.printBarcode = function () {
//                if ($scope.printBarcodeDataBean !== undefined && $scope.printBarcodeDataBean !== null) {
//                    $scope.commonField = $scope.printBarcodeDataBean.selectedField;
//                }
                $scope.selectedRows = $scope.gridApi.selection.getSelectedRows();
                if ($scope.selectedRows.length >= 1) {
                    $scope.fields = [];
                    angular.forEach($scope.selectedRows, function (item) {
                        $scope.fields.push(item["~@commonId"]);
                    }, function () {
                        console.log("failure");
                    });
                    $scope.printBarcodeDataBean = {};
//                    $scope.printBarcodeDataBean.selectedField = $scope.commonField;
                    $scope.printBarcodeDataBean.fieldIds = $scope.fields;
                    $rootScope.maskLoading();
                    PrintBarcodeService.prepareBarcode($scope.printBarcodeDataBean, function (result) {
                        $rootScope.unMaskLoading();
                        if (!!result) {
                            if (result[0] === 'T') {
                                window.location.href = $rootScope.appendAuthToken($rootScope.centerapipath + "printbarcode/downloadpdfofbarcodes");
                                $scope.onCancelOfSearch();
                            }
                        } else {
                            var msg = "Failed to download report";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                        }
                    });
                }
            };
            $rootScope.unMaskLoading();
        }
    ]);
});