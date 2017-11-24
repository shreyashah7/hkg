define(['hkg'], function (hkg) {
    hkg.register.factory('RapCalcyService', ['$resource', '$rootScope', function (resource, rootScope) {
            var Calcy = resource(rootScope.centerapipath + 'rapcalc/:action',
                    {
                    },
                    {
                        create: {
                            method: 'PUT',
                            params: {
                                action: '/create'
                            }
                        },
                        calculateDiamondPrice: {
                            method: 'POST',
                            params: {
                                action: '/calculatediamondprice'
                            }
                        }
                    });
            return Calcy;
        }]);
});
