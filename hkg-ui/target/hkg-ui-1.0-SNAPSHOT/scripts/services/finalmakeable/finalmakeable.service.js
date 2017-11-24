define(['hkg'], function (hkg) {
    hkg.register.factory('FinalMakeableService', ['$resource', '$rootScope', function (resource, rootScope) {
            var finalMakeable = resource(rootScope.centerapipath + 'finalmakeable/:action',
                    {
                    },
                    {
                        retrievePacketsInStockOf: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'retrievepacketforfinalmakeable'
                            }
                        },
                        createFinalMakeable: {
                            method: 'PUT',
                            params: {
                                action: 'createfinalmakeable'
                            }
                        },
                        retrieveFinalMakeableByPktId: {
                            method: 'POST',
                            params: {
                                action: 'retrievefinalmakeablebypacketid'
                            }
                        },
                        updateFinalMakeable: {
                            method: 'POST',
                            params: {
                                action: 'updatefinalmakeable'
                            }
                        }
                    }

            );
            return finalMakeable;
        }]);
});


