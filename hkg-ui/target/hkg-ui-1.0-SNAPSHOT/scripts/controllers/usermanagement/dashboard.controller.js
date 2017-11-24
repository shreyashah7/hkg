/**
 * This controller is for dashboard
 * Author : Dhwani Mehta
 * Date : 24 Sep 2014
 */
define(['hkg', 'reportBuilderService'], function(hkg) {
    hkg.register.controller('DashboardController', ["$rootScope", "$scope", "$http", "ReportBuilderService", "$sce", "$q", function($rootScope, $scope, $http, ReportBuilderService, $sce, $q) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "dashboard";
            $rootScope.childMenu = "dashboardLink";
            $rootScope.activateMenu();
            $rootScope.unMaskLoading();
            $scope.showWarning = false;

            $scope.initialData = JSON.parse(localStorage.getItem('initialSetupData'));
            $scope.$on('$viewContentLoaded', function() {
                if ($scope.initialData && $scope.initialData.status !== 100) {
                    $scope.showWarning = true;
                }
            });
            $scope.initSetup = function() {
//                $rootScope.maskLoading();
                var userInfo = JSON.parse(localStorage.getItem('tmpUser'));
                var payload = {
                    j_username: userInfo.username,
                    j_password: $rootScope.encryptPass(userInfo.password, false)
                };
                $http.post($rootScope.centerapipath + 'sync/deployserver', JSON.stringify(payload)).success(function(response) {
                    if (!response.data.status) {
                    } else if (response.data.status === 100) {
                        $scope.showWarning = false;
                        localStorage.setItem('initialSetupData', JSON.stringify(response.data));
                    } else {
                        localStorage.setItem('initialSetupData', JSON.stringify(response.data));
                    }

                    $rootScope.isFirstReq = false;
//                    $rootScope.unMaskLoading();
                });
            };


            /**********************PENTAHO**********************/

//            $scope.loginUrl = 'http://192.1.200.51:8080/pentaho/j_spring_security_check';
//            $scope.logoutUrl = 'http://192.1.200.51:8080/pentaho/Logout';
//            $scope.homeUrl = 'http://192.1.200.51:8080/pentaho/Home';

            $scope.user = {};
            $scope.user.username = "prabhat";
            $scope.user.password = "testing123";

            $scope.testLogin = function(data, deferred) {
                var loginSuccessString1 = '<title>Pentaho Business Analytics</title>';
                var loginSuccessString2 = '<title>Pentaho User Console</title>';
                if (data.toString().indexOf(loginSuccessString1) >= 0 || data.toString().indexOf(loginSuccessString2) >= 0) {
                    deferred.resolve(data);
                } else {
                    $scope.invalidCredential = true;
                    deferred.resolve(data);
                }
            };

            $scope.login = function(deferred) {
                var payload = $.param({
                    j_username: $scope.user.username,
                    j_password: $scope.user.password,
                    _spring_security_remember_me: true
                });
                var config = {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                    }
                };
                $http.post($rootScope.analyticsLoginUrl, payload, config).success(function(data, status, headers, config) {
                    $scope.testLogin(data, deferred);
                }).error(function(data, status, headers, config) {
                    $rootScope.addMessage("Unable to connect analytics server", $rootScope.failure);
                    deferred.resolve(data);
                });
            };

            $scope.testPingResult = function(data, deferred) {
                var loginSuccessString1 = '<title>Pentaho Business Analytics</title>';
                var loginSuccessString2 = '<title>Pentaho User Console</title>';
                if (data.toString().indexOf(loginSuccessString1) >= 0 || data.toString().indexOf(loginSuccessString2) >= 0) {
                    //Already logged in
                    deferred.resolve(data);
                } else {
                    //Not logged in
                    $scope.login(deferred);
                }
            };

            $scope.pingAnalyticServer = function() {
                var deferred = $q.defer();
                $http.get($rootScope.analyticsPingUrl).success(function(data, status, headers, config) {
                    $scope.testPingResult(data, deferred);
                }).error(function(data, status, headers, config) {
                    $rootScope.addMessage("Unable to connect analytics server", $rootScope.failure);
                    deferred.resolve(data);
                });
                return deferred.promise;
            };

            /** Pentaho logout method. Will be called if user logs out from main system. **/
            $scope.analyticsLogout = function() {
                //Logout only if analytics credentials are configured.
                ReportBuilderService.retrieveAnalyticsCrendentials(function(response) {
                    if (response.data !== undefined && response.data !== null && response.data.hasOwnProperty('ANALYTICS_ENGINE_USERNAME')) {
                        if (response.data.hasOwnProperty('ANALYTICS_SERVER_URL')) {
                            $rootScope.analyticsLoginUrl = response.data['ANALYTICS_SERVER_URL'] + '/j_spring_security_check';
                            $rootScope.analyticsLogoutUrl = response.data['ANALYTICS_SERVER_URL'] + '/Logout';
                            $rootScope.analyticsPingUrl = response.data['ANALYTICS_SERVER_URL'] + '/Home';
                        }
                        if (response.data.hasOwnProperty('ANALYTICS_ENGINE_USERNAME')) {
                            $scope.user.username = response.data['ANALYTICS_ENGINE_USERNAME'];
                        }
                        if (response.data.hasOwnProperty('ANALYTICS_ENGINE_PWD')) {
                            $scope.user.password = response.data['ANALYTICS_ENGINE_PWD'];
                        }

                        var payload = $.param({
                        });
                        var config = {
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                            }
                        };
                        $http.post($rootScope.analyticsLogoutUrl, payload, config).success(function(data, status, headers, config) {
                            //Initialize reports.
                            $scope.initializeReports();
                        }).error(function(data, status, headers, config) {
                        });
                    } else {
                    }
                }, function(response) {
                });
            };


            /**********************PENTAHO ends**********************/

            $scope.initializeReports = function() {
                ReportBuilderService.retrieveDashboardReports(function(res) {
//                    console.log(JSON.stringify(res));
                    if (res.data !== undefined && res.data !== null) {
                        $scope.reports = res.data;
                        if ($scope.reports.length > 0) {
                            ReportBuilderService.retrieveAnalyticsCrendentials(function(response) {
                                if (response.data !== undefined && response.data !== null && response.data.hasOwnProperty('ANALYTICS_ENGINE_USERNAME')) {
                                    $scope.user.username = response.data.ANALYTICS_ENGINE_USERNAME;
                                    $scope.user.password = response.data.ANALYTICS_ENGINE_PWD;
                                    var prom = $scope.pingAnalyticServer();
                                    prom.then(function(res) {
                                        $scope.currentReport = $scope.reports[0];
                                        $scope.iframeUrl = $sce.trustAsResourceUrl($scope.currentReport.query);
                                        $scope.currentIndex = 0;
                                    });
                                } else {
                                    $rootScope.addMessage("Analytics credentials are not set, contact administrator", $rootScope.failure);
                                    $scope.reports = [];
                                }
                            }, function(response) {
                            });
                        }
                    }
                }, function(res) {
                    alert("failure");
                });
            };

            $scope.updateCurrentReport = function(type) {
                if (type === 'next') {
                    $scope.currentIndex++;
                } else {
                    $scope.currentIndex--;
                }
                var prom = $scope.pingAnalyticServer();
                prom.then(function(res) {
                    $scope.currentReport = $scope.reports[$scope.currentIndex];
                    $scope.iframeUrl = $sce.trustAsResourceUrl($scope.currentReport.query);
                });
            };

        }]);
});
