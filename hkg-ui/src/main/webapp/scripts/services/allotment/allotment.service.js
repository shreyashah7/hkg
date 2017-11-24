define(['hkg'], function (hkg) {
    hkg.register.factory('AllotmentService', ['$resource', '$rootScope', function (resource, rootScope) {
            var allot = resource(rootScope.centerapipath + 'allotment/:action', {}, {
                retrieveSearchedData: {
                    method: 'POST',
                    isArray:true,
                    params: {
                        action: '/retrievesearcheddata'
                    }
                },
                retrieveUserGradeSuggestion: {
                    method: 'POST',
                    isArray:true,
                    params: {
                        action: '/retrieveusergradesuggestion'
                    }
                },
                retrieveUsers :{
                    method: 'POST',
                    params: {
                        action: '/retrieveusers'
                    }
                },
                retrieveUserGradeSuggestionByUserId :{
                    method: 'POST',
                    params: {
                        action: '/retrieveusergradesuggestionbyuserid'
                    }
                },
                retrievePacketsAvailableInStock :{
                   method: 'GET',
                    params: {
                        action: '/retrievepacketsavailableinstock'
                    } 
                },
                allotPacket:{
                     method: 'POST',
                    params: {
                        action: '/allotpacket'
                    } 
                }
            });
            return allot;
        }]);
});