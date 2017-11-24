define(['hkg'], function (hkg) {
    hkg.register.factory('SplitStockService', ['$resource', '$rootScope', function (resource, rootScope) {
            var stock = resource(rootScope.centerapipath + 'stock/:action', {}, {
                retrieveSearchedLotsAndPacketsForSplit: {
                    method: 'POST',
                    params: {
                        action: '/retrieveSearchedLotsAndPacketsForSplit'
                    }
                },
                splitStock: {
                    method: 'POST',
                    params: {
                        action: '/splitStock'
                    }
                }, retrieveNextNodeDesignationIds: {
                    method: 'POST',
                    params: {
                        action: '/retrievenextnodedesignationids'
                    }
                },
                retrieveSplitPacket: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: '/retrievesplitpacket'
                    }
                }
            }

            );
            return stock;
        }]);
});


