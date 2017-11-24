define(['hkg'], function(hkg) {
    hkg.register.factory('DepartmentFlowService', ['$resource', '$rootScope', function(resource, rootScope) {
            var activity = resource(rootScope.apipath + 'departmentflow/:action',
                    {
                    },
                    {
                        retrieve: {
                            method: 'GET',
                            params: {
                                action: '/retrieve'
                            }
                        },
                        update: {
                            method: 'POST',
                            params: {
                                action: '/update'
                            }
                        },
                        create: {
                            method: 'PUT',
                            params: {
                                action: '/create'
                            }
                        },
                        /** delete is a keyword. Don't use it as object property or any variable name **/
//                        delete: {
//                            method: 'POST',
//                            params: {
//                                action: '/delete'
//                            }
//                        },
                        retrieveDataForProcessFlow: {
                            method: 'GET',
                            params: {
                                action: '/retrieveDataForProcessFlow'
                            }
                        }
                    }
            );
            return activity;
        }]);
});


