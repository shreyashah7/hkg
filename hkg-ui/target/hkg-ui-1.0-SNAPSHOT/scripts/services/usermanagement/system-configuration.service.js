define(['hkg'], function(hkg) {
    hkg.register.factory('SysConfig', ['$resource', '$rootScope', function(resource, rootScope) {
            var SysConfig = resource(
                    rootScope.apipath + 'systemconfiguration/:method', //url being hit
                    {method: '@methodName'}, // url perameters
            {//methods
                createSys: {
                    method: 'POST',
                    url: rootScope.apipath + 'systemconfiguration/create/:key/:value',
                    params: {
                        key: '@skey',
                        value: '@svalue'
                    }
                },
                retreiveAll: {
                    method: 'GET',
                    isArray: true
                }
            }
            );
            return SysConfig;
        }]);
});

