define(['hkg'], function (hkg) {
    hkg.register.factory('StartOfTheDayService', ['$resource', '$rootScope', function (resource, rootScope) {
            var stock = resource(rootScope.centerapipath + 'startoftheday/:action', {}, {
                retrieveIssuestocks: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieveissuestocks'
                    }
                },
                retrieveRecievestocks: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieverecievestocks'
                    }
                },
                retrieveReturnstocks: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrievereturnstocks'
                    }
                },
                retrieveLot: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'retrievelot'
                    }
                },
                retrieveUsersForCarrierBoysId: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieveusers'
                    }
                },
                issueLotsOrPackets: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'issuelotsorpackets'
                    }
                },
                recieveLotsOrPackets: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'recievelotsorpackets'
                    }
                },
                returnLotsOrPackets: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'returnlotsorpackets'
                    }
                },
                retrieveFieldSequenceMap: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'retrievefieldsequencemap'
                    }
                }
            }

            );
            return stock;
        }]);
});


