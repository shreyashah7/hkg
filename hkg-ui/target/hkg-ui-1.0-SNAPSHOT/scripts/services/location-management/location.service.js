define(['hkg'], function(hkg) {
    hkg.register.factory('LocationService', ['$resource', '$rootScope', function(resource, rootScope) {
            var Location = resource(
                    rootScope.apipath + 'location/:action',{}, //url being hit
//                    {active: '@active'}, // url perameters
            {//methods
                retriveAllLocations: {
                    method: 'GET',
                    isArray: false,
                    params:{
                        action:"retrieve/locations"
                    }
                },
                retriveLocationstree: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: "retrieve/locationtree"
                    }
                },
                saveAllLocations: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: "create"
                    }

                },
                updateLocations: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: "update"
                    }

                }
            });
            return Location;
        }]);
});

