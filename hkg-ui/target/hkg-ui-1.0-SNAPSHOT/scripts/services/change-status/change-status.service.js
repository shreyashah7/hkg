define(['hkg'], function (hkg) {
    hkg.register.factory('ChangeStausService', ['$resource', '$rootScope', function (resource, rootScope) {
            var lotlist = resource(rootScope.centerapipath + 'statuschange/:action', {}, {
                search: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'searchlots'
                    }
                },
                retrieveStockByLotIdOrPacketId: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrievelot'
                    }
                },
                retrieveStatusMapAndPraposedStatusMap: {
                    method: 'GET',
                    isArray: false,
                    params: {
                        action: 'retrievestatusmap'
                    }
                },
                onSave: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'onsave'
                    }
                }
            }

            );
            return lotlist;
        }]);
});


