/**
 * This controller is used to manage carat ranges
 * Author : Raj Kantaria
 * Date : 24th Feb 2015
 */
define(['hkg', 'caratRangeService'], function(hkg) {
    hkg.register.controller('CaratRangeController', ["$rootScope", "$scope", "CaratRangeService", function($rootScope, $scope, CaratRangeService) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageCaratRange";
            $rootScope.activateMenu();
            $scope.entity = "CARATERANGE.";

            $scope.initData = function() {
                CaratRangeService.retrieveCaratWithNoRange(function(data) {
                    if (!!data) {
                        $scope.noCaratRangeList = [];
                        if (!!data["caratRangeWithNoCarat"]) {
                            angular.forEach(data["caratRangeWithNoCarat"], function(item) {
                                if ($scope.noCaratRangeList.indexOf(item) === -1) {
                                    $scope.noCaratRangeList.push(item);
                                }
                            });
                        }
                    }
                });
                $rootScope.maskLoading();
                CaratRangeService.retrieveAll(function(data) {
                    $rootScope.unMaskLoading();
                    $scope.caratRangeList = [];

                    if (data) {
                        angular.forEach(data, function(item) {
                            item.minValue = parseFloat(Math.round(item.minValue * 1000) / 1000).toFixed(3);
                            item.maxValue = parseFloat(Math.round(item.maxValue * 1000) / 1000).toFixed(3);
                            $scope.caratRangeList.push(item);
                        });
                    } else {
                        $scope.caratRangeList.push({minValue: "00.000", editingdone: false, newadded: true, isEditing: true});
                    }
                    $scope.flagNoCarat = true;
                }, function() {
                    $rootScope.unMaskLoading();
                    console.log("error in retrieving carat range..");
                });
            };

            $scope.editCaratRange = function(index) {
                $scope.caratRangeList[index].isEditing = true;
            };
            $scope.doneEditing = function(form, index) {
                $scope.caratRangeList[index].editingdone = true;
                $scope.caratRangeList[index].overlappingMin = false;
                $scope.caratRangeList[index].overlappingMax = false;
                if (form.$valid) {
                    var flag = false;
                    for (var i = 0; i < $scope.caratRangeList.length; i++) {
                        if (i !== index) {
                            if ((parseFloat($scope.caratRangeList[index].minValue) >= parseFloat($scope.caratRangeList[i].minValue) && parseFloat($scope.caratRangeList[index].minValue) <= parseFloat($scope.caratRangeList[i].maxValue))) {
                                $scope.caratRangeList[index].overlappingMin = true;
                                flag = true;
                            }
                            if ((parseFloat($scope.caratRangeList[index].maxValue) >= parseFloat($scope.caratRangeList[i].minValue) && parseFloat($scope.caratRangeList[index].maxValue) <= parseFloat($scope.caratRangeList[i].maxValue))) {
                                $scope.caratRangeList[index].overlappingMax = true;
                                flag = true;
                            }
                            if ((parseFloat($scope.caratRangeList[index].minValue) >= parseFloat($scope.caratRangeList[i].minValue) && parseFloat($scope.caratRangeList[index].minValue) <= parseFloat($scope.caratRangeList[i].maxValue))
                                    || (parseFloat($scope.caratRangeList[index].maxValue) >= parseFloat($scope.caratRangeList[i].minValue) && parseFloat($scope.caratRangeList[index].maxValue) <= parseFloat($scope.caratRangeList[i].maxValue))) {
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (parseFloat($scope.caratRangeList[index].minValue) < parseFloat($scope.caratRangeList[index].maxValue)) {
                        if (!flag) {
                            $scope.caratRangeList[index].isEditing = false;
                            if (!!$scope.caratRangeList[index].id)
                                $scope.caratRangeList[index].edited = true;
                        }
                        $scope.caratRangeList[index].minLessThanMaxFlag = false;
                    } else {
                        $scope.caratRangeList[index].minLessThanMaxFlag = true;
                    }

                }
            };
            $scope.formatNumber = function(index) {
                if (!!$scope.caratRangeList[index].minValue && !$scope.caratRangeList[index].minValue.isNaN) {
                    $scope.caratRangeList[index].minValue = parseFloat(Math.round($scope.caratRangeList[index].minValue * 1000) / 1000).toFixed(3);
                }
                if (!!$scope.caratRangeList[index].maxValue && !$scope.caratRangeList[index].maxValue.isNaN) {
                    $scope.caratRangeList[index].maxValue = parseFloat(Math.round($scope.caratRangeList[index].maxValue * 1000) / 1000).toFixed(3);
                }
                if (!!$scope.caratRangeList[index].minValue && !!$scope.caratRangeList[index].maxValue) {
                    if ($scope.caratRangeList[index].minValue < $scope.caratRangeList[index].maxValue) {
                        $scope.caratRangeList[index].minLessThanMaxFlag = false;
                    }
                }
            };
            $scope.addClick = function() {
                if ($scope.caratRangeForm.$valid) {

                    if (!!$scope.caratRangeList && $scope.caratRangeList.length > 0) {
                        var num = parseFloat($scope.caratRangeList[$scope.caratRangeList.length - 1].maxValue) + 0.001;
                        num = parseFloat(Math.round(num * 1000) / 1000).toFixed(3);
                    }
                    for (var i = 0; i < $scope.caratRangeList.length; i++) {
                        $scope.caratRangeList[i].isEditing = false;
                    }
                    $scope.caratRangeList.push({minValue: num, editingdone: false, newadded: true, isEditing: true});
                }
            };

            $scope.saveAll = function() {
                var flag = true;
                if (!!$scope.caratRangeList && $scope.caratRangeList.length > 0) {
                    for (var i = 0; i < $scope.caratRangeList.length; i++) {
                        $scope.doneEditing($scope.caratRangeForm, i);
                        if ($scope.caratRangeList[i].overlappingMin || $scope.caratRangeList[i].overlappingMax || $scope.caratRangeList[i].minLessThanMaxFlag) {
                            flag = false;
                            break;
                        }
                    }
                }
                if ($scope.caratRangeForm.$valid && flag) {
                    var payloadList = [];
                    var payloadListForDelete = [];
                    angular.forEach($scope.caratRangeList, function(item) {
                        var payload = {
                            id: item.id,
                            minValue: item.minValue,
                            maxValue: item.maxValue,
                            newadded: item.newadded,
                            edited: item.edited
                        };
                        payloadList.push(payload);
                    });
                    if (!!$scope.toDelete) {
                        payloadListForDelete = payloadList.concat($scope.toDelete);
                    }
                    if (!!$scope.toDelete) {
                        $rootScope.maskLoading();
                        CaratRangeService.saveAll(payloadListForDelete, function() {
                            $rootScope.unMaskLoading();
                            $scope.caratRangeList = [];
                            $scope.toDelete = [];
                            $scope.initData();
                        }, function() {
                            $rootScope.unMaskLoading();
                            console.log("Problem while saving...");
                        });
                    } else {
                        $rootScope.maskLoading();
                        CaratRangeService.saveAll(payloadList, function() {
                            $rootScope.unMaskLoading();
                            $scope.caratRangeList = [];
                            $scope.toDelete = [];
                            $scope.initData();
                        }, function() {
                            $rootScope.unMaskLoading();
                            console.log("Problem while saving...");
                        });
                    }
                }
            };
            $scope.deleteCaratRange = function(index) {
                if (!!$scope.caratRangeList[index].id) {
                    $scope.indexTodelete = index;
                    $('#removeCaratRangeModal').modal('show');
                } else {
                    $scope.caratRangeList.splice(index, 1);
                }
            };
            $scope.toDelete = [];
            $scope.removecaratrange = function() {
                $scope.caratRangeList[$scope.indexTodelete].toDelete = true;
                $scope.caratRangeList[$scope.indexTodelete].edited = false;
                $scope.toDelete.push($scope.caratRangeList[$scope.indexTodelete]);
                $scope.caratRangeList.splice($scope.indexTodelete, 1);
                $('#removeCaratRangeModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            }
            $scope.hideconformationForRemoveCaratRange = function() {
                $('#removeCaratRangeModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();

            };
            $rootScope.unMaskLoading();
        }]);
});
