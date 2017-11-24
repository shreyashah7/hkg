define(['hkg'], function(hkg) {
    hkg.register.factory('LeaveWorkflow', ['$resource', '$rootScope', function(resource, rootScope) {
            var LeaveWorkflow = resource(
                    rootScope.apipath + 'leave/workflow/:action', //url being hit
                    {
                        action: '@actionName'
                    }, // url perameters

            {//methods
                retrieveUserList: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/users'
                    }
                },
                retrieveDepartmentList: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/departments'
                    }
                },
                retrieveDepartmentsCombine: {
                    method: 'GET',
                    params: {
                        action: 'retrievedepartmentcombine'
                    }
                },
                createWorkflow: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'create'
                    }
                },
                updateWorkflow: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'update'
                    }
                },
                deleteWorkflow: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'delete'
                    }
                },
                retrieveWorkflowByDepartmentId: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'retrieveWorkflowByDepId'
                    }
                }
            }
            );
            return LeaveWorkflow;
        }]);
});


