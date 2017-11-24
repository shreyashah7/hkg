define(['hkg'], function(hkg) {
    hkg.register.factory('PriceListService', ['$resource', '$rootScope', function(resource, rootScope) {
            var pricelist = resource(rootScope.apipath + 'pricelist/:action',
                    {
                    },
                    {
                        downloadtemplate: {
                            method: 'GET',
                            params: {
                                action: '/downloadtemplate'
                            }
                        },
                        savePriceList: {
                            method: 'POST',
                            params: {
                                action: '/savepricelist'
                            }
                        },
                        retrieveallPriceList: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: '/retrieveallpricelist'
                            }
                        },
                        retrievepricelistByMonthYear: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: '/retrievepricelistByMonthYear'
                            }
                        }
                    }

            );
            return pricelist;
        }]);
});


