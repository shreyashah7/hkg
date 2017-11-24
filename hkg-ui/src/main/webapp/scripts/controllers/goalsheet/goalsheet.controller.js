define(['hkg', 'goalService'], function(hkg) {
    hkg.register.controller('ManageGoalSheetController', ["$rootScope", "$scope", "GoalService", function($rootScope, $scope, GoalService) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageGoalSheet";
            $rootScope.activateMenu();
            $scope.validate = {};
            $scope.verfiyData = {};
            $scope.format = $rootScope.dateFormat;
            $scope.datePicker = {};
            $scope.dateOptions = {
                'year-format': "'yy'",
                'starting-day': 1
            };
            $scope.open = function($event, opened) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.datePicker[opened] = true;
            };
            $scope.minEventFromDate = $rootScope.getCurrentServerDate();
            $scope.flag = "show_submit";
            $scope.flag1 = {};
            $scope.userId = -1;
            $scope.flag1.userIdVerify = -1;
            $scope.entity = "GOALSHEET.";
            $scope.submitted = false;
            $scope.typeOfUser = 'myGoalSheet';
            $scope.flag1.typeOfUserVerify = 'myGoalSheet';
            $scope.initData = function(userIdNew) {
                console.log("userIdNew::::"+userIdNew);
                if(userIdNew!==undefined && userIdNew!==null){
                    $scope.userId=userIdNew;
                }
                console.log("userIdNew1::::"+$scope.userId);
                if ($scope.userId !== undefined && $scope.userId !== null && $scope.userId!=="") {
                    $rootScope.maskLoading();
                    GoalService.retrievependinggoalsheet($scope.userId, function(result) {
                        $rootScope.unMaskLoading();
                        $scope.goalSheetList = result;
                        $scope.goalSheetMap = {empty: true};
                        if (!!$scope.goalSheetList) {
                            angular.forEach($scope.goalSheetList, function(item) {
                                if (!!item.activityGroup) {
                                    if (!!!$scope.goalSheetMap[item.activityGroup + " - " + item.activityNode]) {
                                        $scope.goalSheetMap[item.activityGroup + " - " + item.activityNode] = [];
                                    }
                                    item.validTarget = false;
                                    item.submitted = false;
                                    delete $scope.goalSheetMap['empty'];
                                    $scope.goalSheetMap[item.activityGroup + " - " + item.activityNode].push(item);
                                }
                                if (!!item.department) {
                                    if (!!!$scope.goalSheetMap["Department" + " - " + item.department]) {
                                        $scope.goalSheetMap["Department" + " - " + item.department] = [];
                                    }
                                    item.validTarget = false;
                                    item.submitted = false;
                                    delete $scope.goalSheetMap['empty'];
                                    $scope.goalSheetMap["Department" + " - " + item.department].push(item);
                                }
                                if (!!item.designation) {
                                    if (!!!$scope.goalSheetMap["Designation" + " - " + item.designation]) {
                                        $scope.goalSheetMap["Designation" + " - " + item.designation] = [];
                                    }
                                    item.validTarget = false;
                                    item.submitted = false;
                                    delete $scope.goalSheetMap['empty'];
                                    $scope.goalSheetMap["Designation" + " - " + item.designation].push(item);
                                }
                            });

                        }
                    }, function() {
                        $rootScope.unMaskLoading();
                    });
                } else {
                    $scope.goalSheetMap = {empty: true};
                }
            }

            $scope.validateTarget = function(item) {
                var flag = false;
                item.submitted = false;
                if (item.minTarget !== undefined && item.minTarget !== null && item.maxTarget !== undefined && item.maxTarget !== null) {
                    if (item.target >= item.minTarget && item.target <= item.maxTarget) {
                        flag = true;
                    }
                } else if (item.minTarget !== undefined && item.minTarget !== null) {
                    if (item.target >= item.minTarget) {
                        flag = true;
                    }
                } else if (item.maxTarget !== undefined && item.maxTarget !== null) {
                    if (item.target <= item.maxTarget) {
                        flag = true;
                    }
                }
                item.validTarget = flag;
            };

            $scope.submitGoal = function(form, value) {
                value.submitted = true;
                if (form.$valid && (value.validTarget || !!value.department || !!value.designation)) {
                    var payload = angular.copy(value);
                    delete payload.validTarget;
                    delete payload.submitted;
                    $rootScope.maskLoading();
                    GoalService.submitGoalSheet(payload, function(res) {
                        $rootScope.unMaskLoading();
                        $rootScope.addMessage("Goal sheet submitted successfully", $rootScope.success);
                        $scope.initData();

                    }, function() {
                        $rootScope.unMaskLoading();
                        $rootScope.addMessage("Failed to submit goal sheet", $rootScope.failure);
                    });
                }
            };

            $scope.verifyGoalSheetClick = function() {
                $scope.flag = 'show_verify';
                $scope.searchedFlag = false;
                $scope.submitted = false;
                $scope.flag1.userIdVerify = -1;
            };
            $scope.backLink = function() {
                $scope.cancelVerify();
                $scope.flag = 'show_submit';
                $scope.initData();
            }
            $scope.cancelVerify = function() {
                $scope.verfiyData = {};
                $scope.verifyForm.$setPristine();
                $scope.submitted = false;
                $scope.verifyGoalSheetMap = {empty: true};
            }
            $scope.searchInGoalSheet = function() {
                $scope.submitted = true;
                if ($scope.verifyForm.$valid) {
                    var payload = {};
                    payload["userId"] = $scope.flag1.userIdVerify;
                    var dateq = new Date($scope.verfiyData.fromDate);
                    var fromdate = dateq.getFullYear() + '-' + (dateq.getMonth() + 1) + '-' + dateq.getDate();
                    payload["fromDate"] = fromdate;
                    var date = new Date($scope.verfiyData.toDate);
                    var toDate = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();

                    payload["toDate"] = toDate;
                    $scope.searchedFlag = true;
                    $rootScope.maskLoading();
                    GoalService.retrievesubmittedgoalsheet(payload, function(result) {
                        $rootScope.unMaskLoading();

                        $scope.verifyGoalSheetList = result;
                        $scope.verifyGoalSheetMap = {empty: true};
                        if (!!$scope.verifyGoalSheetList) {
                            angular.forEach($scope.verifyGoalSheetList, function(item) {
                                if (!!item.activityGroup) {
                                    if (!!!$scope.verifyGoalSheetMap[item.activityGroup + " - " + item.activityNode]) {
                                        $scope.verifyGoalSheetMap[item.activityGroup + " - " + item.activityNode] = [];
                                    }
                                    item.validTarget = false;
                                    item.submitted = false;
                                    delete $scope.verifyGoalSheetMap["empty"];
                                    $scope.verifyGoalSheetMap[item.activityGroup + " - " + item.activityNode].push(item);
                                }
                                if (!!item.department) {
                                    if (!!!$scope.verifyGoalSheetMap["Department" + " - " + item.department]) {
                                        $scope.verifyGoalSheetMap["Department" + " - " + item.department] = [];
                                    }
                                    item.validTarget = false;
                                    item.submitted = false;
                                    delete $scope.verifyGoalSheetMap["empty"];
                                    $scope.verifyGoalSheetMap["Department" + " - " + item.department].push(item);
                                }
                                if (!!item.designation) {
                                    if (!!!$scope.verifyGoalSheetMap["Designation" + " - " + item.designation]) {
                                        $scope.verifyGoalSheetMap["Designation" + " - " + item.designation] = [];
                                    }
                                    item.validTarget = false;
                                    item.submitted = false;
                                    delete $scope.verifyGoalSheetMap["empty"];
                                    $scope.verifyGoalSheetMap["Designation" + " - " + item.designation].push(item);
                                }
                            });
                        }
                    }, function() {
                        $rootScope.unMaskLoading();
                    });
                }
            };

            $scope.radioChange = function() {
                if ($scope.typeOfUser === 'myGoalSheet') {
                    $scope.userId = -1;
                    $scope.initData();
                    $("#selectUser").select2("val", "");
                    $scope.flag1.userId="";
                }
                if ($scope.flag1.typeOfUserVerify === 'myGoalSheet') {
                    $scope.flag1.userIdVerify = -1;
                    $("#selectUser1").select2("val", "");
                }
            }

//            if ($rootScope.canAccess("goalsheetAccess")) {
            $scope.userList = [];
            GoalService.retrieveusersforgoalsheet(function(result) {
                angular.forEach(result, function(item) {
                    if (item.otherId !== $rootScope.session.id) {
                        $scope.userList.push(item);
                    }
                });
            });
//            };
            $rootScope.unMaskLoading();
        }]);
});
