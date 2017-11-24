define(['hkg'], function(hkg) {
    hkg.register.factory('SysFeature', ['$resource', '$rootScope', function(resource, rootScope) {
            var SysFeature = resource(
                    rootScope.apipath + 'feature/:action', //url being hit
                    {
                        action: '@actionName'
                    }, // url perameters

            {//methods
                retreiveSystemFeatures: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: "retrieve/features"
                    }
                },
                retreiveFeatureCategoryList: {
                    method: 'GET',
                    params: {
                        action: "retrievefeaturecategory"
                    }
                },
                createSysFeature: {
                    method: 'POST',
                    params: {
                        action: "create/feature"
                    },
                    isArray: false
                },
                updateSysFeature: {
                    method: 'POST',
                    params: {
                        action: "update/feature"
                    }
                },
                saveSequence: {
                    method: 'POST',
                    params: {
                        action: "savesequencing"
                    }
                }

            });
            return SysFeature;
        }]);
});

