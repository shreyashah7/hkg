define(['hkg', 'lotService', 'customFieldService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive','printBarcodeValue', 'dynamicForm'], function (hkg, lotService) {
    hkg.register.controller('MergeLotController', ["$rootScope", "$scope", "LotService", "$timeout", "$filter", "$location", "$window", "DynamicFormService", "CustomFieldService", function ($rootScope, $scope, LotService, $timeout, $filter, $location, $window, DynamicFormService, CustomFieldService) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "mergeLot";
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
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("lotMerge");
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
            $scope.initEditLotForm = function (editLotForm) {
                $scope.editLotForm = editLotForm;
            };
            $scope.retrieveSearchedData = function (addLotForm) {
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
//                        angular.forEach($scope.searchCustom, function(value, key) {
//                            if (value != null) {
//                                angular.forEach(featureMap, function(val, label) {
//                                    if (key === label) {
//                                        if (!$scope.lotDataBean.featureCustomMap[val])
//                                            $scope.lotDataBean.featureCustomMap[val] = [];
//
//
//                                        $scope.lotDataBean.featureCustomMap[val].push(key + ":" + value);
//
//                                    }
//                                });
//                            }
//                        });
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
            $scope.onCanelOfSearch = function (addLotForm) {
                if ($scope.addLotForm != null) {
                    $scope.addLotForm.$dirty = false;
                }
                $scope.searchedData = [];
                $scope.searchCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);
                $scope.listFilled = false;
            };
            $scope.mergeLot = function () {
                angular.forEach($scope.searchedData, function (itr) {
                    var i = $scope.tempMergeList.indexOf(itr);
                    if (itr.hasPacket && !(i !== -1)) {
                        itr.hasPacket = false;
                        $scope.tempMergeList.push(itr);
                    } else {
                        itr.hasPacket = false;
                    }
                });
            };
            $scope.removeLot = function (searchDataObj) {
                if (!!(searchDataObj && $scope.searchedDataFromDb && $scope.searchedDataFromDb.length > 0)) {
                    angular.forEach($scope.tempMergeList, function (itr) {
                        var index = $scope.tempMergeList.indexOf(searchDataObj);
                        if (index !== -1) {
                            $scope.tempMergeList.splice(index, 1);
                        }
                    });
                    if ($scope.tempMergeList.length === 0) {
                        $scope.flag.twoPacketReqd = false;
                    }
                }
            };
            $scope.showMergeFields = function () {
                $scope.flag.twoPacketReqd = false;
                if (!!($scope.tempMergeList && $scope.tempMergeList.length > 1)) {
                    $scope.flag.showUpdatePage = true;
                    CustomFieldService.retrieveDesignationBasedFields("lotMerge", function (response) {
                        $scope.lotDataBean = {};
                        $scope.lotCustom = DynamicFormService.resetSection($scope.mergeLotTemplate);
                        var templateDataForUpdate = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                        $scope.lotDbType = {};
                        templateDataForUpdate.then(function (section) {
                            $scope.mergeLotTemplate = section['genralSection'];
                            $scope.mergeLotTemplate = DynamicFormService.retrieveCustomData($scope.mergeLotTemplate, response);
                            $rootScope.unMaskLoading();
                            $scope.lotDataBean.lotDbType = $scope.lotDbType;
                            $scope.parcelIds = [];
                            $scope.selectedParcelDropdown = {currentNode: ''};
                            angular.forEach($scope.tempMergeList, function (itr) {
                                if (($scope.parcelIds.length === 0)) {
                                    $scope.parcelIds.push({displayName: itr.custom4.parcelID, id: itr.label});
                                }
                                angular.forEach($scope.parcelIds, function (parcelItr) {
                                    if (parcelItr.displayName !== itr.custom5.lotID) {
                                        $scope.parcelIds.push({displayName: itr.custom4.parcelID, id: itr.label});
                                    }
                                });
                            });
                            $scope.parcelIds.unshift({"id": "D", "displayName": "Select"});
                            $scope.selectedParcelDropdown.currentNode = $scope.parcelIds[0];
                            $scope.parcelId = 'D';
                        }, function (reason) {

                        }, function (update) {
                        });
                        $scope.flag.customFieldGenerated = true;
                    }, function () {
                        $rootScope.unMaskLoading();
                        var msg = "Failed to retrieve data";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                } else {
                    $scope.flag.twoPacketReqd = true;
                }
            };
            $scope.clearTempMergeList = function (mergePacketForm) {
                $scope.tempMergeList = [];
                $scope.flag.mergeListFilled = false;
                $scope.flag.twoPacketReqd = false;
            };
            $scope.backToHomeScreen = function (mergeLotForm) {
                if (mergeLotForm != null) {
                    $scope.flag.showUpdatePage = false;
                    $scope.flag.showAddPage = false;
                    $scope.flag.selectParent = false;
                }
            };
            $scope.backToMergeFieldScreen = function (mergeLotForm) {
                if (mergeLotForm != null) {
                    $scope.flag.showUpdatePage = true;
                    $scope.flag.selectParent = false;
                }
            };
            $scope.selectParent = function (mergeLotForm) {
                if (!!(mergeLotForm && mergeLotForm.$valid)) {
                    $scope.lotDataBean.lotCustom = $scope.lotCustom;
                    $scope.lotDataBean.lotDbType = $scope.lotDbType;
                    $scope.flag.showUpdatePage = false;
                    $scope.flag.selectParent = true;
                }
            };
            $scope.saveMergedLot = function (mergeLotForm) {
                $scope.submitted = true;
                if (!!(mergeLotForm && mergeLotForm.$valid && $scope.selectedParcelDropdown.currentNode.id !== 'D')) {
                    $scope.submitted = false;
                    $scope.lotList = [];
                    angular.forEach($scope.tempMergeList, function (itr) {
                        $scope.lotList.push({id: itr.value});
                    });
                    $scope.lotDataBean.lotList = angular.copy($scope.lotList, $scope.lotDataBean.lotList);
                    $scope.lotDataBean.parcelDataBean = {id: $scope.selectedParcelDropdown.currentNode.id};
                    LotService.mergeLot($scope.lotDataBean, function (response) {
                        $scope.initializeData(true);
                    }, function () {
                        $rootScope.unMaskLoading();
                        var msg = "Could not merge lot, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                }
            };
        }]);
});