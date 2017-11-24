define(['hkg'], function(hkg) {
    hkg.register.factory('MergeParcelService', ['$resource', '$rootScope', function(resource, rootScope) {
            var stock = resource(rootScope.centerapipath + 'mergeparcel/:action', {}, {
                search: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: '/retrievesearchedparcels'
                    }
                },
                retrieveParcelsByIds: {
                    method: 'POST',
                    params: {
                        action: '/retrieveparcelsbyids'
                    }
                },
                mergeParcel: {
                    method: 'POST',
                    params: {
                        action: '/mergeparcel'
                    }
                }
            })
            return stock;
        }]);
});


