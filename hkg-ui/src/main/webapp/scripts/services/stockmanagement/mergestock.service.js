define(['hkg'], function(hkg) {
    hkg.register.factory('MergeStockService', ['$resource', '$rootScope', function(resource, rootScope) {
            var stock = resource(rootScope.centerapipath + 'stock/:action', {}, {
                retrieveSearchedLotsAndPackets: {
                    method: 'POST',
                    params: {
                        action: '/retrieveSearchedLotsAndPackets'
                    }
                },
                retrieveSearchedLotsAndPacketsNew: {
                    method: 'POST',
                    params: {
                        action: '/retrieveSearchedLotsAndPacketsNew'
                    }
                },
                mergeStock: {
                    method: 'POST',
                    params: {
                        action: '/mergeStock'
                    }
                },
                retrieveLotOrPacketByAllotmentId: {
                    method: 'POST',
                    params: {
                        action: '/retrieveselectedstockdetails'
                    }
                }

            }

            );
            return stock;
        }]);
});


