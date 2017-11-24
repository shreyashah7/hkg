define(['hkg', 'systemConfigSerivce'], function(hkg, systemConfigSerivce) {
    hkg.register.controller('SystemConfiguration', ["$rootScope", "$scope", "SysConfig", function($rootScope, $scope, SysConfig) {
        $rootScope.maskLoading();
        $scope.retreiveAll = function() {
            var allSysConfig = [];

            var sucess = function(res) {
                angular.forEach(res, function(item) {
                    allSysConfig.push(item);
                });
            };

            var failure = function() {
                var msg = "Fail to load System Configurations";
                var type = $rootScope.failure;
                $rootScope.addMessage(msg, type);
            };

            SysConfig.retreiveAll(sucess, failure);
            return allSysConfig;
        };

        $scope.allSysConfigs = $scope.retreiveAll();

        $scope.saveSysConfig = function() {
            var sucess = function() {
                var msg = "Created";
                var type = $rootScope.success;
                $rootScope.addMessage(msg, type);
                $scope.allSysConfigs = $scope.retreiveAll();
                $scope.key = null;
                $scope.value = null;
            };

            var failure = function() {
                var msg = "Fail to create System Configuration";
                var type = $rootScope.failure;
                $rootScope.addMessage(msg, type);
            };

            SysConfig.createSys({
                skey: $scope.key,
                svalue: $scope.value
            }, sucess, failure);
        };
        $rootScope.unMaskLoading();
    }]);

});
