define(['hkg'], function(hkg) {
    hkg.register.factory('CaratRangeService', ['$resource', '$rootScope', function(resource, rootScope) {
            var CaratRange = resource(rootScope.apipath + 'caratrange/:action',
                    {
                    },
                    {
                        retrieveAll: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: '/retrieve'
                            }
                        },
                        saveAll: {
                            method: 'POST',
                            params: {
                                action: 'saveAll'
                            }
                        },
                        retrieveCaratWithNoRange: {
                            method: 'GET',
//                            isArray: true,
                            params: {
                                action: 'retrieveCaratWithNoRange'
                            }
                        }
                    });
            return CaratRange;
        }]);
});