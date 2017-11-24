define(['hkg'], function (hkg) {
    hkg.register.factory('TrackStockService', ['$resource', '$rootScope', function (resource, rootScope) {
            var trackstock = resource(rootScope.centerapipath + 'trackstock/:action', {}, {
                search: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'searchlotsorpackets'
                    }
                },
                retrieveStockByLotIdOrPacketId: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'retrievelotorpacket'
                    }
                },
                retrieveLotOrPacketActivites: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrievelotorpacketactivities'
                    }
                },
                generateReportOfActivities: {
                    method: 'POST',
                    params: {
                        action: 'generatereportofactivities'
                    }
                },
                retrieveActivityFlowVersion: {
                    method: 'POST',
                    params: {
                        action: 'retrieveactivityflowversion'
                    }
                },
                retrieveVersionList: {
                    method: 'GET',
                    params: {
                        action: 'retrieveversionlist'
                    }
                },
                retrieveTotalStockByVersion: {
                    method: 'POST',
                    params: {
                        action: 'retrievetotalstockbyversion'
                    }
                },
                retrieveTotalStockByNode: {
                    method: 'POST',
                    params: {
                        action: 'retrievetotalstockbynode'
                    }
                },
                generateStockDetailsReport : {
                    method: 'POST',
                    params: {
                        action: 'generatestockdetailsreport'
                    }
                },
                generateTotalStockReport :{
                   method: 'POST',
                    params: {
                        action: 'generatetotalstockreport'
                    } 
                }

            }
            );
            return trackstock;
        }]);
});