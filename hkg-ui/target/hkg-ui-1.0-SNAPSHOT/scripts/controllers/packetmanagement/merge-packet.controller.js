define(['hkg', 'packetService', 'lotService', 'customFieldService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive','printBarcodeValue', 'dynamicForm'], function (hkg, packetService) {
    hkg.register.controller('MergePacketController', ["$rootScope", "$scope", "PacketService", "$timeout", "$filter", "$location", "$window", "DynamicFormService", "LotService", "CustomFieldService", function ($rootScope, $scope, PacketService, $timeout, $filter, $location, $window, DynamicFormService, LotService, CustomFieldService) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "mergeLot";
            $rootScope.activateMenu();
            var featureMap = {};
            $scope.initializeData = function (flag) {
                if (flag) {
                    $scope.tempMergeList = [];
                    $scope.packetDataBean = {};
                    $scope.searchedData = [];
                    $scope.searchedDataFromDb = [];
                    $scope.listFilled = false;
                    $scope.lotListTodisplay = [];
                    $scope.searchCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);
                    $scope.packetDataBean.featureCustomMap = {};
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
            $scope.initMergePacketForm = function (mergePacketForm) {
                $scope.mergePacketForm = mergePacketForm;
            };
            $scope.retrieveSearchedData = function (addLotForm) {
                $scope.packetDataBean.featureCustomMap = {};
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
                        $scope.packetDataBean.featureCustomMapValue = finalMap;
//                        angular.forEach($scope.searchCustom, function(value, key) {
//                            if (value != null) {
//                                angular.forEach(featureMap, function(val, label) {
//                                    if (key === label) {
//                                        if (!$scope.packetDataBean.featureCustomMap[val]) {
//                                            $scope.packetDataBean.featureCustomMap[val] = [];
//                                        }
//                                        $scope.packetDataBean.featureCustomMap[val].push(key + ":" + value);
//
//                                    }
//                                });
//                            }
//                        });

                        $scope.packetDataBean.packetCustom = $scope.searchCustom;
                        PacketService.search($scope.packetDataBean, function (res) {
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

            $scope.onCanel = function () {
                if ($scope.addLotForm != null) {
                    $scope.addLotForm.$dirty = false;
                }
                $scope.categoryCustom = DynamicFormService.resetSection($scope.generalLotTemplate);
            };
            $scope.backToHomeScreen = function (mergePacketForm) {
                if (mergePacketForm != null) {
                    $scope.flag.showUpdatePage = false;
                    $scope.flag.showAddPage = false;
                    $scope.flag.selectParent = false;
                }
            };
            $scope.backToMergeFieldScreen = function (mergePacketForm) {
                if (mergePacketForm != null) {
                    $scope.flag.showUpdatePage = true;
                    $scope.flag.selectParent = false;
                    $scope.submitted = false;
                }
            };
            $scope.onCanelOfSearch = function (mergePacketForm) {
                if ($scope.mergePacketForm != null) {
                    $scope.mergePacketForm.$dirty = false;
                }
                $scope.searchedData = [];
                $scope.searchCustom = DynamicFormService.resetSection($scope.generalSearchTemplate);
                $scope.listFilled = false;
            };
            $scope.mergePacket = function () {
                angular.forEach($scope.searchedData, function (itr) {
                    var i = $scope.tempMergeList.indexOf(itr);
                    if (itr.hasPacket && !(i !== -1)) {
                        itr.hasPacket = false;
                        $scope.tempMergeList.push(itr);
                    } else {
                        itr.hasPacket = false;
                    }
                });
                $scope.flag.mergeListFilled = true;
            };
            $scope.removePacket = function (searchDataObj) {
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
            $scope.clearTempMergeList = function (mergePacketForm) {
                $scope.tempMergeList = [];
                $scope.flag.mergeListFilled = false;
                $scope.flag.twoPacketReqd = false;
            };
            $scope.showMergeFields = function () {
                $scope.flag.twoPacketReqd = false;
                if (!!($scope.tempMergeList && $scope.tempMergeList.length > 1)) {
                    $scope.flag.showUpdatePage = true;
                    CustomFieldService.retrieveDesignationBasedFields("packetMerge", function (response) {
                        $scope.packetDataBean = {};
                        $scope.packetCustom = DynamicFormService.resetSection($scope.mergePacketTemplate);
                        var templateDataForUpdate = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                        $scope.packetDbType = {};
                        templateDataForUpdate.then(function (section) {
                            $scope.mergePacketTemplate = section['genralSection'];
                            $scope.mergePacketTemplate = DynamicFormService.retrieveCustomData($scope.mergePacketTemplate, response);
                            $rootScope.unMaskLoading();
                            $scope.packetDataBean.packetDbType = $scope.packetDbType;
                            $scope.lotIds = [];
                            $scope.selectedParcelDropdown = {currentNode: ''};
                            angular.forEach($scope.tempMergeList, function (itr) {
                                if (($scope.lotIds.length === 0)) {
                                    $scope.lotIds.push({displayName: itr.custom5.lotID, id: itr.label});
                                }
                                angular.forEach($scope.lotIds, function (lotItr) {
                                    if (lotItr.displayName !== itr.custom5.lotID) {
                                        $scope.lotIds.push({displayName: itr.custom5.lotID, id: itr.label});
                                    }
                                });
                            });
                            $scope.lotIds.unshift({"id": "D", "displayName": "Select"});
                            $scope.selectedParcelDropdown.currentNode = $scope.lotIds[0];
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

            $scope.selectParent = function (mergeLotForm) {
                $scope.submitted = true;
                if (!!(mergeLotForm && mergeLotForm.$valid)) {
                    $scope.submitted = true;
                    $scope.packetDataBean.packetCustom = $scope.packetCustom;
                    $scope.packetDataBean.packetDbType = $scope.packetDbType;
                    $scope.flag.showUpdatePage = false;
                    $scope.flag.selectParent = true;
                }
            };
            $scope.saveMergedPacket = function (mergeLotForm) {
                $scope.submitted = true;
                if (!!(mergeLotForm && mergeLotForm.$valid && $scope.selectedParcelDropdown.currentNode.id !== 'D')) {
                    $scope.submitted = false;
                    $scope.packetList = [];
                    angular.forEach($scope.tempMergeList, function (itr) {
                        $scope.packetList.push({id: itr.value});
                    });
                    $scope.packetDataBean.packetList = angular.copy($scope.packetList);
                    $scope.packetDataBean.lotDataBean = {id: $scope.selectedParcelDropdown.currentNode.id};
                    PacketService.mergePacket($scope.packetDataBean, function (response) {
                        $scope.initializeData(true);
                    }, function () {
                        $rootScope.unMaskLoading();
                        var msg = "Could not merge packet, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                }
            };
        }]);
});