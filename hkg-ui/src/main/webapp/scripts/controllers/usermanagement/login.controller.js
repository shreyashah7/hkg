define(['hkg', 'ruleExecutionService'], function (hkg) {
    hkg.register.controller('Login', ['$rootScope', '$scope', '$http', '$location', '$window', '$q', 'RuleExecutionService', function ($rootScope, $scope, $http, $location, $window, $q, RuleExecutionService) {
            $rootScope.maskLoading();
            //seting focus on email address
//            document.getElementById('emailaddress').focus();
            $scope.company = "";
            $scope.usernamePlaceholder = "Enter Username";
            $scope.actionbutton = "Login";
            var isSetup = false;
            $scope.submitted = false;
            $scope.EMAIL_REGEXP = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$/;
            $scope.$on('$viewContentLoaded', function () {

//                $rootScope.logout();
//                //sync helper code start
//                if ($rootScope.isFirstReq) {
//                    $http.get("api/centerfranchise/retrieve").success(function (res) {
//                        alert(res.id + " here " + (res.id == undefined));
//                        if (res.id == undefined) {
//
//                            $location.path("/setup");
//                        }
//                    })
//                }
//                //sync helper code end
                localStorage.removeItem("user");
                localStorage.removeItem("userDetail");
                localStorage.removeItem("tmpUser");

                var deferred = $q.defer();
                var promise = retrieveCenterFranchise($http, $location, deferred);
                promise.then(function () {

                    $scope.usernamePlaceholder = "Franchise Admin Username";
                    $scope.actionbutton = "Setup";
                    isSetup = true;
                    console.log("Is setup required: " + isSetup);
                }, function () {
                    isSetup = false;
                    console.log("Is setup required: " + isSetup);
                });
//                var data = localStorage.getItem('user');

                var data = localStorage.getItem('userDetail');
                data = JSON.parse(data);
                if (data !== null) {
                    $scope.rememberme = true;
                    $scope.username = data.username;
                    $scope.password = $rootScope.encryptPass(data.password, false);
                }
//                if ($rootScope.isLoggedIn === true) {
//                    $rootScope.retrieveMinAge();
//                }
            });

            function doSetup(payload) {
                var deferred = $q.defer();
                $rootScope.maskLoading();
                $http.post($rootScope.centerapipath + 'sync/deployserver', payload).success(function (response) {
                    if (response) {
                        localStorage.setItem('initialSetupData', JSON.stringify(response.data));
                    }
                    $rootScope.isFirstReq = false;
                    deferred.resolve();
                    $rootScope.unMaskLoading();
                }).error(function (data) {
                    deferred.reject();
                    if (data.messages && data.messages[0]) {
                        $rootScope.addMessage(data.messages[0].message, data.messages[0].responseCode);
                        $rootScope.setMessage(data.messages[0].message, data.messages[0].responseCode, true);
                    }
                    $rootScope.unMaskLoading();
                });
                return deferred.promise;
            }   
            $scope.doLogin = function (loginform) {
                $scope.submitted = true;
                if (loginform.$valid) {
                    console.log("loginnnn");
                    console.log("-------------" + $rootScope.isMaster);
                    console.log("run : login request event");

                    var config = {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                        }
                    };
                    var payload = {userName: $scope.username, password: $scope.password};

                    if (isSetup) {
                        console.log("here");
                        var promise = doSetup(payload);
                        promise.then(function () {
                            login(payload);
                        }, function () {

                        });
                    } else {
                        login(payload);
                    }

                }
            };
            function login(payload) {
                var user = {
                    username: $scope.username,
                    password: $rootScope.encryptPass($scope.password, true),
                    rememberme: $scope.rememberme
                };

                var dataToSend = {
                    featureName: "login",
                    entityId: null,
                    entityType: 'login',
                    currentFieldValueMap: {username: $scope.username},
                    dbType: null,
                    otherEntitysIdMap: null
                };
                if (!$rootScope.isMaster) {
                    RuleExecutionService.executePostRule(dataToSend, function (res) {
                        if (!!res.validationMessage) {
                            $rootScope.setMessage(res.validationMessage, 'warning', true, false);
                            $rootScope.unMaskLoading();
                        } else {
                            $http.post($rootScope.centerapipath + 'common/authenticate', JSON.stringify(payload)).success(function (data) {
                                $rootScope.authToken = data.token;
                                localStorage.setItem('user', data.token);
                                if ($scope.rememberme) {
                                    data.password = $rootScope.encryptPass($scope.password, true);
                                    localStorage.setItem('userDetail', JSON.stringify(data));
                                } else {
                                    localStorage.removeItem('userDetail');
                                }
                                localStorage.setItem('tmpUser', JSON.stringify(user));
                                var promise = $rootScope.loginToMaster(user);
                                promise.then(function () {
                                    $rootScope.pingServer();
                                    $rootScope.retrieveNotificationCount();
                                }, function () {
                                    $rootScope.pingServer();
                                    $rootScope.retrieveNotificationCount();
                                });
                            });
                        }
                        console.log("res " + JSON.stringify(res));
                    }, function (failure) {
                        $rootScope.unMaskLoading();
                        var msg = "Failed to authenticate post rule.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                } else {
                    $http.post($rootScope.centerapipath + 'common/authenticate', JSON.stringify(payload)).success(function (data) {
                        $rootScope.authToken = data.token;
                        localStorage.setItem('user', data.token);
                        if ($scope.rememberme) {
                            data.password = $rootScope.encryptPass($scope.password, true);
                            localStorage.setItem('userDetail', JSON.stringify(data));
                        } else {
                            localStorage.removeItem('userDetail');
                        }
                        localStorage.setItem('tmpUser', JSON.stringify(user));
                        var promise = $rootScope.loginToMaster(user);
                        promise.then(function () {
                            $rootScope.pingServer();
                            $rootScope.retrieveNotificationCount();
                        }, function () {
                            $rootScope.pingServer();
                            $rootScope.retrieveNotificationCount();
                        });
                    });
                }
//                    if (!$rootScope.isMaster) {
//                        $scope.retrieveWorkAllocationCount();
//                    }




            }
            $scope.showLogin = function () {
                $('#login').modal('show');
            };
            $rootScope.unMaskLoading();
        }]);
});
