define(['hkg'], function (hkg) {
    hkg.register.factory('ReRoutStockService', ['$resource', '$rootScope', function (resource, rootScope) {
            var Stock = resource(rootScope.centerapipath + 'reroutstock/:action', {}, {
                search: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrievesearcheddata'
                    }
                },
                retrieveAllotToByActivityFlowNodeId: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieveallotto'
                    }
                },
                stockReRout: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'stockrerout'
                    }
                }
            });
            return Stock;
        }]);
});