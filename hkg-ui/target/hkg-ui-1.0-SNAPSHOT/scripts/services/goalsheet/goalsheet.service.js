define(['hkg'], function(hkg) {
    hkg.register.factory('GoalSheetService', ['$resource', '$rootScope', function(resource, rootScope) {
            var invoice = resource(rootScope.apipath + 'goalsheet/:action',
                    {
                    },
                    {
                        retrieve: {
                            method: 'GET',
                            params: {
                                action: '/retrieve'
                            }
                        }
                    }

            );
            return invoice;
        }]);
});


