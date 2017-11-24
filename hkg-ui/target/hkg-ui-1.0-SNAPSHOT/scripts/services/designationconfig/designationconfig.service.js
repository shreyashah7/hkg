define(['hkg'], function (hkg) {
    hkg.register.factory('DesignationConfigService', ['$resource', '$rootScope', function (resource, rootScope) {
            var DesignationManagement = resource(
                    rootScope.apipath + 'configdesign/:action', //url being hit
                    {
                        action: '@actionName'
                    }, // url perameters
            {
                retrieveAllDesignations: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: "retrieveAllDesignations"
                    }

                },
                retrieveDesignationConfiguration: {
                    method: 'POST',
                    params: {
                        action: 'retrieveDesignationConfiguration'
                    }
                },
                updateDesignationConfiguration :{
                    method: 'POST',
                    params: {
                        action: 'updateDesignationConfiguration'
                    }
                }
            });
            return DesignationManagement;
        }]);
});