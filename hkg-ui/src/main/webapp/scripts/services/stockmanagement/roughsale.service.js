define(['hkg'], function (hkg) {
    hkg.register.factory('RoughSaleService', ['$resource', '$rootScope', function (resource, rootScope) {
            var stock = resource(rootScope.centerapipath + 'roughsale/:action', {}, {
                saleParcels:{
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'sell'
                    }
                },
                retrieveAllSellDocument:{
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve'
                    }
                },
                retrieveselldocumentforparcelbyid :{
                    method: 'POST',
                    params: {
                        action: 'retrieveselldocumentbyid'
                    }
                },
                search:{
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrievesearcheddata'
                    }
                }
            });
            return stock;
        }]);
});


