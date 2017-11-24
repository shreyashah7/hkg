define(['hkg'], function (hkg) {
    hkg.register.factory('SellStockService', ['$resource', '$rootScope', function (resource, rootScope) {
            var stock = resource(rootScope.centerapipath + 'sellstock/:action', {}, {
                retrieve: {
                    method: 'GET',
                    params: {
                        action: '/retrieve'
                    }
                },
                update: {
                    method: 'POST',
                    params: {
                        action: '/update'
                    }
                },
                create: {
                    method: 'PUT',
                    params: {
                        action: '/create'
                    }
                },
                /** delete is a keyword. Don't use it as object property or any variable name **/
//                delete: {
//                    method: 'POST',
//                    params: {
//                        action: '/delete'
//                    }
//                },
                retrieveSellstocks: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'retrievesearchedstock'
                    }
                },
                retrieveStockByWorkAllotmentId: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'retrievestockbyworkallotmentid'
                    }
                },
                sellStock: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'sell'
                    }
                }
            }

            );
            return stock;
        }]);
});


