//@Author  Akta Kalariya
//Service used to commnicate with designation REST API methods.
define(['hkg'], function (hkg) {
    hkg.register.factory('Designation', ['$resource', '$rootScope', function (resource, rootScope) {
            var Designation = resource(rootScope.apipath + 'designation/:action', {}, {
                retrieveDesignations: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieve'
                    }
                },
                retrieveUsersCount: {
                    method: 'POST',
                    params: {
                        action: 'activeuserscountindesignation'
                    }
                },
                retrieveDesignation: {
                    method: 'POST',
                    params: {
                        action: 'retrieve'
                    }
                },
                update: {
                    method: 'POST',
                    params: {
                        action: 'update'
                    }
                },
                create: {
                    method: 'PUT',
                    params: {
                        action: 'create'
                    }
                },
                deleteById: {
                    method: 'POST',
                    params: {
                        action: 'delete'
                    }
                },
                retrieveSystemFeatures: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieve/systemfeatures'
                    }
                },
                retrieveFieldsByFeature: {
                    method: 'GET',
                    params: {
                        action: 'retrievefieldsbyfeature'
                    }
                },
                retrieveDesignationBySearch: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'searchdesignation'
                    }
                },
                saveGoalPermission: {
                    method: 'POST',
                    params: {
                        action: 'savegoalpermission'
                    }
                },
                retrieveActiveGoalPermission: {
                    method: 'GET',
                    params: {
                        action: 'retrievegoalpermissionbydesignation'
                    }
                },
                retrieveActiveGoalSheetPermission: {
                    method: 'GET',
                    params: {
                        action: 'retrievegoalsheetpermissionbydesignation'
                    }
                },
                retrieveReportGroupNames: {
                    method: 'POST',
                    params: {
                        action: 'retrievereportgroupnames'
                    }
                },
                checkCircularDependency: {
                    method: 'POST',
                    params: {
                        action: 'checkcirculardependency'
                    }
                },
                retrieveChildRoles: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrievechildroles'
                    }
                },
                retrieveFeaturesSelectedForDesignation:
                        {
                            method: 'POST',
                            params: {
                                action: 'retrieveFeaturesSelectedForDesignation'
                            }

                        }
            });
            return Designation;
        }]);
});