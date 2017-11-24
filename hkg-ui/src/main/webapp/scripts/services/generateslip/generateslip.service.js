define(['hkg'], function (hkg) {
    hkg.register.factory('GenerateSlipService', ['$resource', '$rootScope', function (resource, rootScope) {
            var slipNumberlist = resource(rootScope.centerapipath + 'generateslip/:action', {}, {
                retrieveLotsAndPackets: {
                    method: 'POST',
                    params: {
                        action: 'searchlotsorpackets'
                    }
                },
                generateSlip: {
                    method: 'POST',
                    params: {
                        action: 'generateslip'
                    }
                }
            }
            );
            return slipNumberlist;
        }]);
});