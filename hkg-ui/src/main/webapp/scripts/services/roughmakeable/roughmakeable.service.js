define(['hkg'], function (hkg) {
    hkg.register.factory('RoughMakeableService', ['$resource', '$rootScope', function (resource, rootScope) {
            var roughMakeable = resource(rootScope.centerapipath + 'roughmakeable/:action',
                    {
                    },
                    {
                        retrievePacketsInStockOf: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'retrievepacketforroughmakeable'
                            }
                        },
                        createRoughMakeable: {
                            method: 'PUT',
                            params: {
                                action: 'createroughmakeable'
                            }
                        },
                        retrieveRoughMakeableByPktId: {
                            method: 'POST',
                            params: {
                                action: 'retrieveroughmakeablebypacketid'
                            }
                        },
                        updateRoughMakeable: {
                            method: 'POST',
                            params: {
                                action: 'updateroughmakeable'
                            }
                        }
                    }

            );
            return roughMakeable;
        }]);
});


