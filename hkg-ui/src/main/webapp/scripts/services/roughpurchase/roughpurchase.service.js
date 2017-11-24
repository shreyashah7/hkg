define(['hkg'], function (hkg) {
    hkg.register.factory('RoughPurchaseService', ['$resource', '$rootScope', function (resource, rootScope) {
            var roughPurchase = resource(rootScope.centerapipath + 'roughpurchase/:action', {}, {
                createRoughPurchase: {
                    method: 'PUT',
                    params: {
                        action: '/createroughpurchase'
                    }
                },
                updateRoughPurchase: {
                    method: 'PUT',
                    params: {
                        action: '/updateroughpurchase'
                    }
                },
                deleteRoughPurchase: {
                    method: 'POST',
                    params: {
                        action: '/deleteroughpurchase'
                    }
                },
                retrieveSearchedRoughPurchases: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: '/retrievesearchedroughpurchases'
                    }
                },
                retrieveRoughPurchases: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: '/retrieveroughpurchases'
                    }
                },
                getNextRoughPurchaseSequence: {
                    method: 'GET',
                    params: {
                        action: '/getnextroughpurchasesequence'
                    }
                },
                isRoughPurchaseIdExists: {
                    method: 'POST',
                    params: {
                        action: '/isroughpurchaseidexists'
                    }
                },
                retrieveAssociatedRoughPurchase: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: '/retrieveassociatedroughpurchases'
                    }
                },
                getTotalCountOfPurchases: {
                    method: 'GET',
                    params: {
                        action: '/countPurchases'
                    }
                },
                getTotalCountOfSearchPurchases: {
                    method: 'POST',
                    params: {
                        action: '/countSearchPurchases'
                    }
                }
            });
            return roughPurchase;
        }]);
});