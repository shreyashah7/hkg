define(['hkg', 'startOfTheDayService', 'customFieldService', 'assetService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'printBarcodeValue', 'dynamicForm', 'accordionCollapse'], function(hkg, startOfTheDayService, assetService) {
    hkg.register.controller('StartOfTheDayController', ["$rootScope", "$scope", "StartOfTheDayService", "DynamicFormService", "AssetService", "$q", "$timeout", function($rootScope, $scope, StartOfTheDayService, DynamicFormService, AssetService, $q, $timeout) {
            var featureMap = {};
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "startofDay";
            $rootScope.activateMenu();
            $scope.fieldSequenceToDataBeanMap = {
                'lotID$AG$String': 'lotId',
                'lotSlipID$AG$String': 'lotSlipId',
                'packetID$AG$String': 'packetId',
                'packetSlipID$AG$String': 'packetSlipId'
            };
            $scope.initializeData = function(isCancelLink) {
                $scope.recieveDataBean = {};
                $scope.recieveDataBean.barcodes = [];
                $scope.returnDataBean = {};
                $scope.returnDataBean.barcodes = [];
                $scope.uniqueScannedIds = [];
                if (isCancelLink !== true) {
                    $scope.selectedTab = "Issue";
                }
                $scope.finallotIdsOrPacketIds = [];
                $scope.totalLotIdsOrPacketIdsOrLotSlipIdsOrPacketSlipIds = [];
                $scope.flag = {};
                $scope.issueDataBean = {};
                $scope.issueDataBean.barcodes = [];
                $scope.dataRetrieved = false;
                $scope.flag.homePage = true;
                $scope.flag.submitted = false;
                $scope.flag.recieveSubmitted = false;
                $scope.flag.returnSubmitted = false;
                $scope.searchedIssueDataFromDb = [];
                $scope.searchedRecieveDataFromDb = [];
                $scope.searchedReturnDataFromDb = [];
                $scope.issueListFilled = false;
                $scope.recieveListFilled = false;
                $scope.returnListFilled = false;
                var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("dayEnd");
                $scope.stockLabelListForUiGrid = [];
                $scope.issueGridOptions = {};
                $scope.recieveGridOptions = {};
                $scope.returnGridOptions = {};
                $scope.issueBarcodeGridOptions = undefined;
                $scope.receiveBarcodeGridOptions = undefined;
                $scope.returnBarcodeGridOptions = undefined;
                $scope.issueGridOptions.enableFiltering = true;
//                $scope.issueGridOptions.multiSelect = true;
//                $scope.issueGridOptions.enableRowSelection = true;
//                $scope.issueGridOptions.enableSelectAll = true;
                $scope.issueGridOptions.columnDefs = [];
                $scope.issueGridOptions.data = [];
                $scope.recieveGridOptions.enableFiltering = true;
//                $scope.recieveGridOptions.multiSelect = true;
//                $scope.recieveGridOptions.enableRowSelection = true;
//                $scope.recieveGridOptions.enableSelectAll = true;
                $scope.recieveGridOptions.columnDefs = [];
                $scope.recieveGridOptions.data = [];
                $scope.returnGridOptions.enableFiltering = true;
//                $scope.returnGridOptions.multiSelect = true;
//                $scope.returnGridOptions.enableRowSelection = true;
//                $scope.returnGridOptions.enableSelectAll = true;
                $scope.returnGridOptions.columnDefs = [];
                $scope.returnGridOptions.data = [];
                $scope.scannedIds = [];
                $scope.lotIds = [];
                $scope.packetIds = [];
                $scope.issueStockList = [];
                $scope.recieveStockList = [];
                $scope.returnStockList = [];
                $scope.issueStockListDb = [];
                $scope.recieveStockListDb = [];
                $scope.returnStockListDb = [];
                $scope.issueBarcodeList = [];
                $scope.recieveBarcodeList = [];
                $scope.returnBarcodeList = [];
                $scope.issueSelectedRows = [];
                $scope.recieveSelectedRows = [];
                $scope.returnSelectedRows = [];
                $scope.issueListFilled = false;
                $scope.returnListFilled = false;
                $scope.recieveListFilled = false;
                templateData.then(function(section) {
                    $scope.dataRetrieved = true;
                    $scope.generalSearchTemplate = section['genralSection'];
                    $scope.generateFeatureMap = function(callback) {
                        var deferred = $q.defer();
                        var promise = deferred.promise;
                        promise.then(function() {
                            if ($scope.generalSearchTemplate != null && $scope.generalSearchTemplate.length > 0) {
                                for (var i = 0; i < $scope.generalSearchTemplate.length; i++) {
                                    var item = $scope.generalSearchTemplate [i];
                                    featureMap[item.model] = item.featureName;
                                    if (item.fromModel) {
                                        $scope.stockLabelListForUiGrid.push({name: item.fromModel, displayName: item.label, minWidth: 200});
                                    } else if (item.toModel) {
                                        $scope.stockLabelListForUiGrid.push({name: item.toModel, displayName: item.label, minWidth: 200});
                                    } else if (item.model) {
                                        $scope.stockLabelListForUiGrid.push({name: item.model, displayName: item.label, minWidth: 200});
                                    }
                                }
                            }
                        }).then(function() {
                            callback();
                        });
                        deferred.resolve();

                    };
                    $scope.generateFeatureMap(function() {
                        if (!!$scope.generalSearchTemplate) {
                            $rootScope.maskLoading();
                            StartOfTheDayService.retrieveIssuestocks(featureMap, function(result) {

//                                console.log("retrieveIssuestocks----" + JSON.stringify(result));
                                $scope.searchedIssueDataFromDb = angular.copy(result);
                                var success = function(result)
                                {
                                    angular.forEach($scope.searchedIssueDataFromDb, function(itr) {
                                        angular.forEach($scope.stockLabelListForUiGrid, function(list) {
                                            if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                                itr.categoryCustom[list.name] = "NA";
                                            }
                                            else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                                if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                    itr.categoryCustom[list.name] = "NA";
                                                }
                                            }
                                            if (itr.hasOwnProperty("lotId")) {
                                                itr.categoryCustom["~@lotid"] = itr.lotId;
                                            }
                                            if (itr.hasOwnProperty("label")) {
                                                itr.categoryCustom["~@parcelid"] = itr.label;
                                            }
                                            if (itr.hasOwnProperty("description")) {
                                                itr.categoryCustom["~@invoiceid"] = itr.description;
                                            }
                                            if (itr.hasOwnProperty("packetId")) {
                                                itr.categoryCustom["~@packetid"] = itr.packetId;
                                            }
                                            if (itr.hasOwnProperty("status")) {
                                                itr.categoryCustom["~@status"] = itr.status;
                                            }
                                        });
                                        $scope.issueStockList.push(itr.categoryCustom);
                                        $scope.issueStockListDb.push(itr);
                                    });
                                    $scope.issueGridOptions.data = $scope.issueStockList;
                                    $scope.issueGridOptions.columnDefs = $scope.stockLabelListForUiGrid;
                                    $scope.issueListFilled = true;
                                }
                                DynamicFormService.convertorForCustomField($scope.searchedIssueDataFromDb, success);
                                StartOfTheDayService.retrieveRecievestocks(featureMap, function(result) {

//                                    console.log("retrieveRecievestocks---" + JSON.stringify(result));
                                    $scope.searchedRecieveDataFromDb = angular.copy(result);

                                    var success = function(result)
                                    {
                                        angular.forEach($scope.searchedRecieveDataFromDb, function(itr) {
                                            angular.forEach($scope.stockLabelListForUiGrid, function(list) {
                                                if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                                    itr.categoryCustom[list.name] = "NA";
                                                }
                                                else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                                    if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                        itr.categoryCustom[list.name] = "NA";
                                                    }
                                                }
                                                if (itr.hasOwnProperty("lotId")) {
                                                    itr.categoryCustom["~@lotid"] = itr.lotId;
                                                }
                                                if (itr.hasOwnProperty("label")) {
                                                    itr.categoryCustom["~@parcelid"] = itr.label;
                                                }
                                                if (itr.hasOwnProperty("description")) {
                                                    itr.categoryCustom["~@invoiceid"] = itr.description;
                                                }
                                                if (itr.hasOwnProperty("packetId")) {
                                                    itr.categoryCustom["~@packetid"] = itr.packetId;
                                                }
                                                if (itr.hasOwnProperty("status")) {
                                                    itr.categoryCustom["~@status"] = itr.status;
                                                }
                                            });
                                            $scope.recieveStockList.push(itr.categoryCustom);
                                            $scope.recieveStockListDb.push(itr);
                                        });
                                        $scope.recieveGridOptions.data = $scope.recieveStockList;
                                        $scope.recieveGridOptions.columnDefs = $scope.stockLabelListForUiGrid;
                                        $scope.recieveListFilled = true;
                                    }
                                    DynamicFormService.convertorForCustomField($scope.searchedRecieveDataFromDb, success);
                                    StartOfTheDayService.retrieveReturnstocks(featureMap, function(result) {

//                                        console.log("retrieveReturnstocks-----" + JSON.stringify(result));
                                        $scope.searchedReturnDataFromDb = angular.copy(result);
                                        var success = function(result)
                                        {
                                            angular.forEach($scope.searchedReturnDataFromDb, function(itr) {
                                                angular.forEach($scope.stockLabelListForUiGrid, function(list) {
                                                    if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                                        itr.categoryCustom[list.name] = "NA";
                                                    }
                                                    else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                                        if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                            itr.categoryCustom[list.name] = "NA";
                                                        }
                                                    }
                                                    if (itr.hasOwnProperty("lotId")) {
                                                        itr.categoryCustom["~@lotid"] = itr.lotId;
                                                    }
                                                    if (itr.hasOwnProperty("label")) {
                                                        itr.categoryCustom["~@parcelid"] = itr.label;
                                                    }
                                                    if (itr.hasOwnProperty("description")) {
                                                        itr.categoryCustom["~@invoiceid"] = itr.description;
                                                    }
                                                    if (itr.hasOwnProperty("packetId")) {
                                                        itr.categoryCustom["~@packetid"] = itr.packetId;
                                                    }
                                                    if (itr.hasOwnProperty("status")) {
                                                        itr.categoryCustom["~@status"] = itr.status;
                                                    }
                                                });
                                                $scope.returnStockList.push(itr.categoryCustom);
                                                $scope.returnStockListDb.push(itr);
                                            });
                                            $scope.returnGridOptions.data = $scope.returnStockList;
                                            $scope.returnGridOptions.columnDefs = $scope.stockLabelListForUiGrid;
                                            $scope.returnListFilled = true;
                                        }
                                        DynamicFormService.convertorForCustomField($scope.searchedReturnDataFromDb, success);
//                                        $timeout(function() {
                                            $scope.tabChangeEvent($scope.selectedTab);
//                                        });
                                        
                                        $rootScope.unMaskLoading();
                                    }, function(reason) {
                                        $rootScope.unMaskLoading();
                                    }, function(update) {
                                    });
                                }, function(reason) {
                                    $rootScope.unMaskLoading();
                                }, function(update) {
                                });
                            }, function(reason) {
                                $rootScope.unMaskLoading();
                            }, function(update) {
                            });
                        }
                    });
                }, function(reason) {
                }, function(update) {

                });
            };


            $scope.tabChangeEvent = function(tabName) {
                $scope.flag.rowSelectedflag = false;
                $scope.issueListFilled = false;
                $scope.recieveListFilled = false;
                $scope.returnListFilled = false;
                if (tabName === 'Recieve') {
//                    window.setTimeout(function () {
//                        $rootScope.maskLoading();
                    $scope.recieveListFilled = true;
//                        $rootScope.unMaskLoading();
//                    }, 50);
                    if ($scope.recieveStockList.length > 0) {
                        $scope.flag.rowSelectedflag = true;
                        $scope.selectedTab = "Recieve";
                    }
                }
                else if (tabName === 'Return') {
//                    window.setTimeout(function () {
//                        $rootScope.maskLoading();
                    $scope.returnListFilled = true;
//                        $rootScope.unMaskLoading();
//                    }, 50);
                    if ($scope.returnStockList.length > 0) {
                        $scope.flag.rowSelectedflag = true;
                        $scope.selectedTab = "Return";

                    }
                }
                else if (tabName === 'Issue') {
//                    window.setTimeout(function () {
//                        $rootScope.maskLoading();
                    $scope.issueListFilled = true;
//                        $rootScope.unMaskLoading();

//                    }, 50);
                    if ($scope.issueStockList.length > 0) {
                        $scope.flag.rowSelectedflag = true;
                        $scope.selectedTab = "Issue";
                    }
                }
                if ($scope.flag.rowSelectedflag === true) {
                    $scope.nextScreen();
                }
            };
            $scope.nextScreen = function() {
//                $scope.flag.homePage = false;
                $scope.flag.issueSecondScreen = false;
                $scope.flag.recieveSecondScreen = false;
                $scope.flag.returnSecondScreen = false;

                if ($scope.selectedTab === "Issue") {
                    $scope.flag.issueSecondScreen = true;
//                    $scope.selectedTab = {};
                } else if ($scope.selectedTab === "Recieve") {

                    $scope.flag.recieveSecondScreen = true;
//                    $scope.selectedTab = {};
                }
                else if ($scope.selectedTab === "Return") {
                    $scope.flag.returnSecondScreen = true;
//                    $scope.selectedTab = {};
                }
                StartOfTheDayService.retrieveFieldSequenceMap(function(res) {
                    $scope.fieldSequenceMap = angular.copy(res.data);
                    console.log('success----' + JSON.stringify(res.data));
                }, function(res) {
                    console.log("some error occoured");
                });

                if ($scope.issueBarcodeGridOptions === undefined) {
                    $scope.issueBarcodeGridOptions = {};
                    $scope.issueBarcodeGridOptions.enableFiltering = true;
                    $scope.issueBarcodeGridOptions.columnDefs = angular.copy($scope.stockLabelListForUiGrid);
                    $scope.issueBarcodeGridOptions.columnDefs.push({name: 'Action',
                        cellTemplate: '<div class="ui-grid-cell-contents"><span style="cursor:pointer;" class="glyphicon glyphicon-remove text-danger" ng-click="grid.appScope.showPopUp(row.entity)"></span></div>', enableFiltering: false, minWidth: 200});
                    $scope.issueBarcodeGridOptions.data = [];
                }
                if ($scope.receiveBarcodeGridOptions === undefined) {
                    $scope.receiveBarcodeGridOptions = {};
                    $scope.receiveBarcodeGridOptions.enableFiltering = true;
                    $scope.receiveBarcodeGridOptions.columnDefs = angular.copy($scope.stockLabelListForUiGrid);
                    $scope.receiveBarcodeGridOptions.columnDefs.push({name: 'Action',
                        cellTemplate: '<div class="ui-grid-cell-contents"><span style="cursor:pointer;" class="glyphicon glyphicon-remove text-danger" ng-click="grid.appScope.showPopUp(row.entity)"></span></div>', enableFiltering: false, minWidth: 200});
                    $scope.receiveBarcodeGridOptions.data = [];
                }
                if ($scope.returnBarcodeGridOptions === undefined) {
                    $scope.returnBarcodeGridOptions = {};
                    $scope.returnBarcodeGridOptions.enableFiltering = true;
                    $scope.returnBarcodeGridOptions.columnDefs = angular.copy($scope.stockLabelListForUiGrid);
                    $scope.returnBarcodeGridOptions.columnDefs.push({name: 'Action',
                        cellTemplate: '<div class="ui-grid-cell-contents"><span style="cursor:pointer;" class="glyphicon glyphicon-remove text-danger" ng-click="grid.appScope.showPopUp(row.entity)"></span></div>', enableFiltering: false, minWidth: 200});
                    $scope.returnBarcodeGridOptions.data = [];
                }
            };

            $scope.showPopUp = function(stockObj) {
                $scope.lotObjectToDelete = stockObj;
                if (stockObj["~@lotid"]) {
                    $scope.stockId = stockObj["~@lotid"];
                } else {
                    $scope.stockId = stockObj["~@packetid"];
                }
                $("#deleteDialog").modal('show');
            };
            $scope.hidePopUp = function() {
                $("#deleteDialog").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();

            };
            $scope.deleteStock = function() {
                $scope.barcodeGridOptions = {};
                if ($scope.flag.issueSecondScreen === true) {
                    $scope.barcodeGridOptions = $scope.issueBarcodeGridOptions;
                }
                else if ($scope.flag.recieveSecondScreen === true) {
                    $scope.barcodeGridOptions = $scope.receiveBarcodeGridOptions;
                }
                else if ($scope.flag.returnSecondScreen === true) {
                    $scope.barcodeGridOptions = $scope.returnBarcodeGridOptions;
                }
                for (var i = 0; i < $scope.barcodeGridOptions.data.length; i++) {
                    var item = $scope.barcodeGridOptions.data[i];
                    var objectId;
                    if (item["~@lotid"]) {
                        objectId = item["~@lotid"];
                    } else {
                        objectId = item["~@packetid"];
                    }
                    if (objectId === $scope.stockId) {
                        $scope.barcodeGridOptions.data.splice(i, 1);
                        if ($scope.flag.issueSecondScreen === true) {
                            if ($scope.issueDataBean.barcodes.length > 0) {
                                for (var k = 0; k < $scope.issueDataBean.barcodes.length; k++) {
                                    if (item["~@barcode"] === $scope.issueDataBean.barcodes[k]) {
                                        $scope.issueDataBean.barcodes.splice(k, 1);
                                        break;
                                    }
                                }
                            }
                        }
                        else if ($scope.flag.recieveSecondScreen === true) {
                            if ($scope.recieveDataBean.barcodes.length > 0) {
                                for (var k = 0; k < $scope.recieveDataBean.barcodes.length; k++) {
                                    if (item["~@barcode"] === $scope.recieveDataBean.barcodes[k]) {
                                        $scope.recieveDataBean.barcodes.splice(k, 1);
                                        break;
                                    }
                                }
                            }
                        }
                        else if ($scope.flag.returnSecondScreen === true) {
                            if ($scope.returnDataBean.barcodes.length > 0) {
                                for (var k = 0; k < $scope.returnDataBean.barcodes.length; k++) {
                                    if (item["~@barcode"] === $scope.returnDataBean.barcodes[k]) {
                                        $scope.returnDataBean.barcodes.splice(k, 1);
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
                $("#deleteDialog").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };

            $scope.autoCompleteIssueTo = {
//                allowClear: true,
                multiple: true,
                closeOnSelect: false,
                maximumSelectionSize: 1,
                placeholder: 'Select',
                initSelection: function(element, callback) {

                },
                formatResult: function(item) {
                    return item.text;
                },
                formatSelection: function(item) {
                    return item.text;
                },
                query: function(query) {
                    var selected = query.term;
                    $scope.names = [];
                    var success = function(data) {
                        $scope.names = [];
                        if (data.length !== 0) {
                            $scope.names = data;
                            angular.forEach(data, function(item) {
                                $scope.names.push({
                                    id: item.value + ":" + item.description,
                                    text: item.label
                                });
                            });
                        }
                        query.callback({
                            results: $scope.names
                        });
                    };
                    var failure = function() {
                    };
                    if (selected.substring(0, 2) == '@E' || selected.substring(0, 2) == '@e') {
                        selected = query.term.slice(2);
                    }
                    StartOfTheDayService.retrieveUsersForCarrierBoysId(selected.trim(), success, failure);
                }
            };
            $scope.autoCompleteInStockOfDepartment = {
//                allowClear: true,
                multiple: true,
                closeOnSelect: false,
                maximumSelectionSize: 1,
                placeholder: 'Select',
                initSelection: function(element, callback) {

                },
                formatResult: function(item) {
                    return item.text;
                },
                formatSelection: function(item) {
                    return item.text;
                },
                query: function(query) {
                    var selected = query.term;
                    $scope.names = [];
                    var success = function(data) {
                        $scope.names = [];
                        if (data.length !== 0) {
                            $scope.names = data;
                            angular.forEach(data, function(item) {
                                $scope.names.push({
                                    id: item.value + ":" + item.description,
                                    text: item.label
                                });
                            });
                        }
                        query.callback({
                            results: $scope.names
                        });
                    };
                    var failure = function() {
                    };
                    if (selected.substring(0, 2) == '@D' || selected.substring(0, 2) == '@d') {
                        selected = query.term.slice(2);
                    }
                    AssetService.retrieveDepartmentList(selected.trim(), success, failure);
                }
            };

            $scope.issueSubmit = function(issueLotOrPacketForm) {
//                console.log("issueSubmit method called");
                $scope.flag.submitted = true;
                if (issueLotOrPacketForm.$valid) {
                    var barcodes = [];
//                    console.log("$scope.issueStockList :" + JSON.stringify($scope.issueStockList));
                    if ($scope.issueBarcodeGridOptions.data.length > 0) {
                        angular.forEach($scope.issueBarcodeGridOptions.data, function(rowEntity) {
                            var stockId = '';
                            if (rowEntity["~@lotid"]) {
                                stockId = rowEntity["~@lotid"];
                            } else {
                                stockId = rowEntity["~@packetid"];
                            }
                            if (stockId.length !== 0) {
                                barcodes.push(stockId);
                            }
                        });
                    }
                    $rootScope.maskLoading();
                    var payload = {};
                    payload.uniqueScannedIds = barcodes;
                    payload.carrierBoy = $scope.issueDataBean.carrierBoy;
//                    console.log("payload----" + JSON.stringify(payload));
                    StartOfTheDayService.issueLotsOrPackets(payload, function(response) {
                        $scope.flag.homePage = true;
//                        console.log("response----" + JSON.stringify(response));
                        if (response.hasOwnProperty("success")) {
                            var msg = "Issue lots and packets successfully.";
                            var type = $rootScope.success;
                            $scope.initializeData(true);
                            issueLotOrPacketForm.$dirty = false;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        }
                        else if (response.hasOwnProperty("failure")) {
                            var msg = response["failure"] + " are not present in current List";
                            var type = $rootScope.warning;
                            $scope.initializeData(true);
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        }
                    }, function() {
                        var msg = "Could not issue lots and packets.please try after sometime";
                        var type = $rootScope.error;
//                        $scope.initializeData();
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    });
                }
            };

            $scope.recieveSubmit = function(recieveLotOrPacketForm) {
//                console.log("recieve method called");
                $scope.flag.recieveSubmitted = true;
                if (recieveLotOrPacketForm.$valid) {
                    var barcodes = [];
                    if ($scope.receiveBarcodeGridOptions.data.length > 0) {
                        angular.forEach($scope.receiveBarcodeGridOptions.data, function(rowEntity) {
                            var stockId = '';
                            if (rowEntity["~@lotid"]) {
                                stockId = rowEntity["~@lotid"];
                            } else {
                                stockId = rowEntity["~@packetid"];
                            }
                            if (stockId.length !== 0) {
                                barcodes.push(stockId);
                            }
                        });
                    }
                    $rootScope.maskLoading();
                    var payload = {};
                    payload.uniqueScannedIds = barcodes;
//                    console.log("payload----" + JSON.stringify(payload));
                    StartOfTheDayService.recieveLotsOrPackets(payload, function(response) {
                        $scope.flag.homePage = true;
//                        console.log("response----" + JSON.stringify(response));
                        if (response.hasOwnProperty("success")) {
                            var msg = "Recieve lots and packets successfully.";
                            var type = $rootScope.success;
                            $scope.initializeData(true);
                            recieveLotOrPacketForm.$dirty = false;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        }
                        else if (response.hasOwnProperty("failure")) {
                            var msg = response["failure"] + " are not present in current List";
                            var type = $rootScope.warning;
                            $scope.initializeData(true);
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        }
                    }, function() {
                        var msg = "Could not recieve lots and packets.please try after sometime";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    });
                }
            };


            $scope.returnSubmit = function(returnLotOrPacketForm) {
//                console.log("returnSubmit method called");
                $scope.flag.returnSubmitted = true;
                if (returnLotOrPacketForm.$valid) {
                    var barcodes = [];
                    if ($scope.returnBarcodeGridOptions.data.length > 0) {
                        angular.forEach($scope.returnBarcodeGridOptions.data, function(rowEntity) {
                            var stockId = '';
                            if (rowEntity["~@lotid"]) {
                                stockId = rowEntity["~@lotid"];
                            } else {
                                stockId = rowEntity["~@packetid"];
                            }
                            if (stockId.length !== 0) {
                                barcodes.push(stockId);
                            }
                        });
                    }
                    $rootScope.maskLoading();
                    var payload = {};
                    payload.uniqueScannedIds = barcodes;
//                    console.log("payload----" + JSON.stringify(payload));
                    StartOfTheDayService.returnLotsOrPackets(payload, function(response) {
                        $scope.flag.homePage = true;
//                        console.log("response----" + JSON.stringify(response));
                        if (response.hasOwnProperty("success")) {
                            var msg = "Return lots and packets successfully.";
                            var type = $rootScope.success;
                            $scope.initializeData(true);
                            returnLotOrPacketForm.$dirty = false;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        }
                        else if (response.hasOwnProperty("failure")) {
                            var msg = response["failure"] + " are not present in current List";
                            var type = $rootScope.warning;
                            $scope.initializeData(true);
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        }
                    }, function() {
                        var msg = "Could not return lots and packets.please try after sometime";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    });
                }
            };


            $scope.changeBarcodeFormation = function(event, type) {
                if (event.keyCode === 13) {
                    //Blur the field.
//                    $(event.target).eq(0).blur();
                    $timeout(function() {
                        $(event.target).eq(0).focus();
                    });
                    if (type === 'issue') {
                        if ($scope.issueDataBean.barcode !== undefined && $scope.issueDataBean.barcode !== null && $scope.issueDataBean.barcode !== '') {
                            var actualPrefix = '';
                            var barcodeDuplicate = $scope.issueDataBean.barcode;
                            var barcodePrefix = $scope.issueDataBean.barcode.split("-")[0];
                            angular.forEach($scope.fieldSequenceMap, function(value, key) {
                                if (barcodePrefix.trim() === value) {
                                    actualPrefix = key;
                                }
                            });
                            if ($scope.fieldSequenceToDataBeanMap[actualPrefix] !== undefined) {
                                var barcodeElement = $scope.fieldSequenceToDataBeanMap[actualPrefix];
                                if ($scope.issueDataBean.barcodes.indexOf($scope.issueDataBean.barcode) === -1) {
                                    var primarySearchResult = false;
                                    if ($scope.issueStockListDb.length > 0) {
                                        angular.forEach($scope.issueStockListDb, function(stockItem) {
                                            if (stockItem[barcodeElement] !== null && stockItem[barcodeElement] !== undefined && stockItem[barcodeElement] === $scope.issueDataBean.barcode.trim()) {
                                                primarySearchResult = true;
                                                stockItem.categoryCustom['~@barcode'] = $scope.issueDataBean.barcode;
                                                $scope.issueBarcodeList.push(stockItem.categoryCustom);
                                            }
                                        });
                                    }
                                    if (!primarySearchResult) {
                                        $rootScope.maskLoading();
                                        var secondarySearchResult = false;
                                        var tempStockList = [];
                                        $scope.issueStockListDb = [];
                                        StartOfTheDayService.retrieveIssuestocks(featureMap, function(result) {
//                                            console.log("retrieveIssuestocks----" + JSON.stringify(result));
                                            $scope.searchedIssueDataFromDb = angular.copy(result);
                                            var success = function(result)
                                            {
                                                angular.forEach($scope.searchedIssueDataFromDb, function(itr) {
                                                    angular.forEach($scope.stockLabelListForUiGrid, function(list) {
                                                        if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                                            itr.categoryCustom[list.name] = "NA";
                                                        }
                                                        else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                                            if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                                itr.categoryCustom[list.name] = "NA";
                                                            }
                                                        }
                                                        if (itr.hasOwnProperty("lotId")) {
                                                            itr.categoryCustom["~@lotid"] = itr.lotId;
                                                        }
                                                        if (itr.hasOwnProperty("label")) {
                                                            itr.categoryCustom["~@parcelid"] = itr.label;
                                                        }
                                                        if (itr.hasOwnProperty("description")) {
                                                            itr.categoryCustom["~@invoiceid"] = itr.description;
                                                        }
                                                        if (itr.hasOwnProperty("packetId")) {
                                                            itr.categoryCustom["~@packetid"] = itr.packetId;
                                                        }
                                                        if (itr.hasOwnProperty("status")) {
                                                            itr.categoryCustom["~@status"] = itr.status;
                                                        }
                                                    });
                                                    tempStockList.push(itr.categoryCustom);
                                                    if (itr[barcodeElement] !== null && itr[barcodeElement] !== undefined && itr[barcodeElement] === $scope.issueDataBean.barcode.trim()) {
                                                        itr.categoryCustom['~@barcode'] = $scope.issueDataBean.barcode;
                                                        $scope.issueBarcodeList.push(itr.categoryCustom);
                                                        $scope.issueDataBean.barcodes.push($scope.issueDataBean.barcode);
                                                        $scope.issueDataBean.barcode = '';
                                                        secondarySearchResult = true;
                                                    }
                                                    $scope.issueStockListDb.push(itr);
                                                });
                                                $scope.issueStockList = angular.copy(tempStockList);
                                                $scope.issueGridOptions.data = $scope.issueStockList;
                                                $scope.issueBarcodeGridOptions.data = $scope.issueBarcodeList;
                                                if (!secondarySearchResult) {
                                                    $rootScope.addMessage("No stock found for " + barcodeDuplicate, $rootScope.error);
                                                    $scope.issueDataBean.barcode = '';
                                                }
                                                //$(event.target).eq(0).focus();
                                            };
                                            DynamicFormService.convertorForCustomField($scope.searchedIssueDataFromDb, success);
                                            $rootScope.unMaskLoading();
                                        }, function(reason) {
                                            $rootScope.unMaskLoading();
                                        }, function(update) {
                                        });
                                    } else {
                                        $scope.issueBarcodeGridOptions.data = $scope.issueBarcodeList;
                                        $scope.issueDataBean.barcodes.push($scope.issueDataBean.barcode);
                                        $scope.issueDataBean.barcode = '';
                                        //$(event.target).eq(0).focus();
                                    }
                                } else {
                                    $scope.issueDataBean.barcode = '';
                                    //$(event.target).eq(0).focus();
                                }
                            } else {
                                $rootScope.addMessage("Invalid barcode " + barcodeDuplicate, $rootScope.error);
                                $scope.issueDataBean.barcode = '';
                                //$(event.target).eq(0).focus();
                            }
                        }
                    } else if (type === 'receive') {
                        if ($scope.recieveDataBean.barcode !== undefined && $scope.recieveDataBean.barcode !== null && $scope.recieveDataBean.barcode !== '') {
                            var actualPrefix = '';
                            var barcodeDuplicate = $scope.recieveDataBean.barcode;
                            var barcodePrefix = $scope.recieveDataBean.barcode.split("-")[0];
                            angular.forEach($scope.fieldSequenceMap, function(value, key) {
                                if (barcodePrefix.trim() === value) {
                                    actualPrefix = key;
                                }
                            });
                            if ($scope.fieldSequenceToDataBeanMap[actualPrefix] !== undefined) {
                                var barcodeElement = $scope.fieldSequenceToDataBeanMap[actualPrefix];
                                if ($scope.recieveDataBean.barcodes.indexOf($scope.recieveDataBean.barcode) === -1) {
//                                    console.log(JSON.stringify($scope.recieveStockListDb));
                                    var primarySearchResult = false;
                                    if ($scope.recieveStockListDb.length > 0) {
                                        angular.forEach($scope.recieveStockListDb, function(stockItem) {
                                            if (stockItem[barcodeElement] !== null && stockItem[barcodeElement] !== undefined && stockItem[barcodeElement] === $scope.recieveDataBean.barcode.trim()) {
                                                primarySearchResult = true;
                                                stockItem.categoryCustom['~@barcode'] = $scope.recieveDataBean.barcode;
                                                $scope.recieveBarcodeList.push(stockItem.categoryCustom);
                                            }
                                        });
                                    }
                                    if (!primarySearchResult) {
                                        $rootScope.maskLoading();
                                        var secondarySearchResult = false;
                                        var tempStockList = [];
                                        $scope.recieveStockListDb = [];
                                        StartOfTheDayService.retrieveRecievestocks(featureMap, function(result) {
//                                            console.log("retrieveRecievestocks---" + JSON.stringify(result));
                                            $scope.searchedRecieveDataFromDb = angular.copy(result);
                                            var success = function(result)
                                            {
                                                angular.forEach($scope.searchedRecieveDataFromDb, function(itr) {
                                                    angular.forEach($scope.stockLabelListForUiGrid, function(list) {
                                                        if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                                            itr.categoryCustom[list.name] = "NA";
                                                        }
                                                        else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                                            if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                                itr.categoryCustom[list.name] = "NA";
                                                            }
                                                        }
                                                        if (itr.hasOwnProperty("lotId")) {
                                                            itr.categoryCustom["~@lotid"] = itr.lotId;
                                                        }
                                                        if (itr.hasOwnProperty("label")) {
                                                            itr.categoryCustom["~@parcelid"] = itr.label;
                                                        }
                                                        if (itr.hasOwnProperty("description")) {
                                                            itr.categoryCustom["~@invoiceid"] = itr.description;
                                                        }
                                                        if (itr.hasOwnProperty("packetId")) {
                                                            itr.categoryCustom["~@packetid"] = itr.packetId;
                                                        }
                                                        if (itr.hasOwnProperty("status")) {
                                                            itr.categoryCustom["~@status"] = itr.status;
                                                        }
                                                    });
                                                    tempStockList.push(itr.categoryCustom);
                                                    if (itr[barcodeElement] !== null && itr[barcodeElement] !== undefined && itr[barcodeElement] === $scope.recieveDataBean.barcode.trim()) {
                                                        itr.categoryCustom['~@barcode'] = $scope.recieveDataBean.barcode;
                                                        $scope.recieveBarcodeList.push(itr.categoryCustom);
                                                        $scope.recieveDataBean.barcodes.push($scope.recieveDataBean.barcode);
                                                        $scope.recieveDataBean.barcode = '';
                                                        secondarySearchResult = true;
                                                    }
                                                    $scope.recieveStockListDb.push(itr);
                                                });
                                                $scope.recieveStockList = angular.copy(tempStockList);
                                                $scope.recieveGridOptions.data = $scope.recieveStockList;
                                                $scope.receiveBarcodeGridOptions.data = $scope.recieveBarcodeList;
                                                if (!secondarySearchResult) {
                                                    $rootScope.addMessage("No stock found for " + barcodeDuplicate, $rootScope.error);
                                                    $scope.recieveDataBean.barcode = '';
                                                }
                                                //$(event.target).eq(0).focus();
                                            };
                                            DynamicFormService.convertorForCustomField($scope.searchedRecieveDataFromDb, success);
                                            $rootScope.unMaskLoading();
                                        }, function(reason) {
                                            $rootScope.unMaskLoading();
                                        }, function(update) {
                                        });
                                    } else {
                                        $scope.receiveBarcodeGridOptions.data = $scope.recieveBarcodeList;
                                        $scope.recieveDataBean.barcodes.push($scope.recieveDataBean.barcode);
                                        $scope.recieveDataBean.barcode = '';
                                        //$(event.target).eq(0).focus();
                                    }
                                } else {
                                    $scope.recieveDataBean.barcode = '';
                                    //$(event.target).eq(0).focus();
                                }
                            } else {
                                $rootScope.addMessage("Invalid barcode " + barcodeDuplicate, $rootScope.error);
                                $scope.recieveDataBean.barcode = '';
                                //$(event.target).eq(0).focus();
                            }
                        }
                    } else if (type === 'return') {
                        if ($scope.returnDataBean.barcode !== undefined && $scope.returnDataBean.barcode !== null && $scope.returnDataBean.barcode !== '') {
                            var actualPrefix = '';
                            var barcodeDuplicate = $scope.returnDataBean.barcode;
                            var barcodePrefix = $scope.returnDataBean.barcode.split("-")[0];
                            angular.forEach($scope.fieldSequenceMap, function(value, key) {
                                if (barcodePrefix.trim() === value) {
                                    actualPrefix = key;
                                }
                            });
                            if ($scope.fieldSequenceToDataBeanMap[actualPrefix] !== undefined) {
                                var barcodeElement = $scope.fieldSequenceToDataBeanMap[actualPrefix];
                                if ($scope.returnDataBean.barcodes.indexOf($scope.returnDataBean.barcode) === -1) {
                                    var primarySearchResult = false;
                                    if ($scope.returnStockListDb.length > 0) {
                                        angular.forEach($scope.returnStockListDb, function(stockItem) {
                                            if (stockItem[barcodeElement] !== null && stockItem[barcodeElement] !== undefined && stockItem[barcodeElement] === $scope.returnDataBean.barcode.trim()) {
                                                primarySearchResult = true;
                                                stockItem.categoryCustom['~@barcode'] = $scope.returnDataBean.barcode;
                                                $scope.returnBarcodeList.push(stockItem.categoryCustom);
                                            }
                                        });
                                    }
                                    if (!primarySearchResult) {
                                        $rootScope.maskLoading();
                                        var secondarySearchResult = false;
                                        var tempStockList = [];
                                        $scope.returnStockListDb = [];
                                        StartOfTheDayService.retrieveReturnstocks(featureMap, function(result) {
//                                            console.log("retrieveIssuestocks----" + JSON.stringify(result));
                                            $scope.searchedIssueDataFromDb = angular.copy(result);
                                            var success = function(result)
                                            {
                                                angular.forEach($scope.searchedIssueDataFromDb, function(itr) {
                                                    angular.forEach($scope.stockLabelListForUiGrid, function(list) {
                                                        if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                                            itr.categoryCustom[list.name] = "NA";
                                                        }
                                                        else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                                            if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                                itr.categoryCustom[list.name] = "NA";
                                                            }
                                                        }
                                                        if (itr.hasOwnProperty("lotId")) {
                                                            itr.categoryCustom["~@lotid"] = itr.lotId;
                                                        }
                                                        if (itr.hasOwnProperty("label")) {
                                                            itr.categoryCustom["~@parcelid"] = itr.label;
                                                        }
                                                        if (itr.hasOwnProperty("description")) {
                                                            itr.categoryCustom["~@invoiceid"] = itr.description;
                                                        }
                                                        if (itr.hasOwnProperty("packetId")) {
                                                            itr.categoryCustom["~@packetid"] = itr.packetId;
                                                        }
                                                        if (itr.hasOwnProperty("status")) {
                                                            itr.categoryCustom["~@status"] = itr.status;
                                                        }
                                                    });
                                                    tempStockList.push(itr.categoryCustom);
                                                    if (itr[barcodeElement] !== null && itr[barcodeElement] !== undefined && itr[barcodeElement] === $scope.returnDataBean.barcode.trim()) {
                                                        itr.categoryCustom['~@barcode'] = $scope.returnDataBean.barcode;
                                                        $scope.returnBarcodeList.push(itr.categoryCustom);
                                                        $scope.returnDataBean.barcodes.push($scope.returnDataBean.barcode);
                                                        $scope.returnDataBean.barcode = '';
                                                        secondarySearchResult = true;
                                                    }
                                                    $scope.returnStockListDb.push(itr);
                                                });
                                                $scope.returnStockList = angular.copy(tempStockList);
                                                $scope.returnGridOptions.data = $scope.returnStockList;
                                                $scope.returnBarcodeGridOptions.data = $scope.returnBarcodeList;
                                                if (!secondarySearchResult) {
                                                    $rootScope.addMessage("No stock found for " + barcodeDuplicate, $rootScope.error);
                                                    $scope.returnDataBean.barcode = '';
                                                }
                                                //$(event.target).eq(0).focus();
                                            };
                                            DynamicFormService.convertorForCustomField($scope.searchedIssueDataFromDb, success);
                                            $rootScope.unMaskLoading();
                                        }, function(reason) {
                                            $rootScope.unMaskLoading();
                                        }, function(update) {
                                        });
                                    } else {
                                        $scope.returnBarcodeGridOptions.data = $scope.returnBarcodeList;
                                        $scope.returnDataBean.barcodes.push($scope.returnDataBean.barcode);
                                        $scope.returnDataBean.barcode = '';
                                        //$(event.target).eq(0).focus();
                                    }
                                } else {
                                    $scope.returnDataBean.barcode = '';
                                    //$(event.target).eq(0).focus();
                                }
                            } else {
                                $rootScope.addMessage("Invalid barcode " + barcodeDuplicate, $rootScope.error);
                                $scope.returnDataBean.barcode = '';
                                //$(event.target).eq(0).focus();
                            }
                        }
                    }
                    event.stopPropagation();
                    event.preventDefault();
                }
            };

            $scope.onBack = function(lotOrPacketForm) {
                if (lotOrPacketForm != null) {
                    lotOrPacketForm.$dirty = false;
                    $scope.flag.homePage = true;
                    $scope.flag.issueSecondScreen = false;
                    $scope.flag.recieveSecondScreen = false;
                    $scope.flag.returnSecondScreen = false;
                    $scope.initializeData(true);
                }
            };
        }
    ]);
});