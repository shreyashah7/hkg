define(['hkg'], function(hkg) {
    hkg.register.factory('EditValueService', ['$resource', '$rootScope', function(resource, rootScope) {
            var stock = resource(rootScope.centerapipath + 'stock/:action', {}, {
                retrieveLotsPacketsEditValue: {
                    method: 'POST',
                    params: {
                        action: '/retrieveLotsAndPacketsEditValue'
                    }
                },
                retrievePlansByLotOrPacket: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: '/retrievePlansByLotOrPacket'
                    }
                },
                editValues: {
                    method: 'POST',
                    params: {
                        action: '/editValues'
                    }
                }
            }

            );
            return stock;
        }]);
});


