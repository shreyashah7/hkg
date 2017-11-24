define(['hkg'], function (hkg) {
    hkg.register.controller('SetUpController', ['$rootScope', '$scope', '$http', '$location', '$controller', function ($rootScope, $scope, $http, $location, $controller) {
            $rootScope.maskLoading();
            //seting focus on email address
//            document.getElementById('emailaddress').focus();


            //        alert(localStorage.getItem('company'));
            $scope.doLogin = function (loginform) {
                $scope.submitted = true;
                if (loginform.$valid) {
                    console.log("setup");

                    var payload = {
                        j_username: $scope.username,
                        j_password: $scope.password
                    };
//                    var config = {
//                        headers: {
//                            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
//                        }
//                    };
//                    $http.defaults.useXDomain = true;
//                    delete $http.defaults.headers.common['X-Requested-With'];
                    $rootScope.maskLoading();
                    $http.post($rootScope.centerapipath + 'sync/deployserver', payload).success(function (response) {

                        localStorage.setItem('initialSetupData', JSON.stringify(response.data));
                        $rootScope.isFirstReq = false;
                        //Login 
                        var loginScope = $scope.$new();
                        $controller('Login', {$scope: loginScope});
                        loginScope.doLogin(loginform);

                        $rootScope.unMaskLoading();
                        $location.path('/dashboard');

                    }).error(function () {
                        $rootScope.unMaskLoading();
                    });
                }
            };


            $rootScope.unMaskLoading();
        }]);
});
