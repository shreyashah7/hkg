/**
 * This controller is for dashboard
 * Author : Mayank Modi
 * Date : 31 Oct 2014
 */
define(['hkg'], function (hkg) {
    hkg.register.controller('LogoutController', ["$rootScope", "$scope", "$location", "$http", function ($rootScope, $scope, $location, $http) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "";
            $rootScope.childMenu = "";
            $rootScope.activateMenu();
            $rootScope.isLoggedIn = false;
            $rootScope.menu = undefined;
            $rootScope.session = undefined;

            // $location.path("j_spring_security_logout");
            var config = {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                    'X-Auth-Token': $rootScope.authToken
                }
            };
            $http.post('j_spring_security_logout', {}, config).success(function () {
                delete $rootScope.authToken;
                delete $rootScope.userProxy;
                localStorage.removeItem('user');
                localStorage.removeItem('masterAuthToken');
                localStorage.removeItem('tmpUser');
                localStorage.removeItem('proxyLogin');
                if (!$rootScope.isMaster) {
                    var beforeLogoutUrlMaster = $rootScope.apipath + "common/adduseroperationbeforelogout";
                    $http.get(beforeLogoutUrlMaster)
                            .success(function () {
                                console.log("run : master logout request event");
                                delete $rootScope.masterAuthToken;
                                $http.post($rootScope.masterHkgPath + 'j_spring_security_logout', {})
                                        .success(function () {
                                            $location.path("login");
                                        })
                                        .error(function () {
                                            console.log("failed master logout");
                                            $location.path("login");
                                        });
                            })
                            .error(function () {
                                console.log("failed master beforelogout");
                                delete $rootScope.masterAuthToken;
                                $location.path("login");
                            });
                } else {
                    $location.path("login");
                }

            });

            $rootScope.unMaskLoading();
        }]);

});
