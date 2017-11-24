define(['hkg'], function (hkg) {
    hkg.register.factory('PrintBarcodeService', ['$resource', '$rootScope', function (resource, rootScope) {
            var printbarcodelist = resource(rootScope.centerapipath + 'printbarcode/:action', {}, {
                
                retrieveSearchedStock: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'searchlotsorpackets'
                    }
                },
                prepareBarcode: {
                    method: 'POST',
                    params: {
                        action: 'preparebarcode'
                    }
                }
            }
            );
            return printbarcodelist;
        }]);
});