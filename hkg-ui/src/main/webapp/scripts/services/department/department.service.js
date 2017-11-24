/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


define(['hkg'], function (hkg) {
    hkg.register.factory('DepartmentService', ['$resource', '$rootScope', function (resource, rootScope) {
            var DepartmentManagement = resource(
                    rootScope.apipath + 'department/:action', //url being hit
                    {
                        action: '@actionName'
                    }, // url perameters

            {
                retrieveUsersCount: {
                    method: 'POST',
                    params: {
                        action: 'activeuserscountindepartment'
                    }
                },
                retrieveDepartment: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: "retrieve"
                    }

                },
                addDepartment: {
                    method: 'PUT',
                    params: {
                        action: 'create'
                    }
                },
                deleteDepartment: {
                    method: 'POST',
                    params: {
                        action: 'delete'
                    }
                },
                updateDepartment: {
                    method: 'POST',
                    params: {
                        action: 'update'
                    }
                },
                retrieveDepartmentSimpleTreeView: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: "retrievedeptreeview"
                    }

                }, addCustomDataToDepartmentDataBean: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'addCustomDataToDepartmentDataBean'
                    }
                },
                checkIfDepartmentIsPresentInAnyFeature: {
                    method: 'POST',
                    params: {
                        action: 'checkIfDepartmentIsPresentInAnyFeature'
                    }
                }
//                
            });
            return DepartmentManagement;
        }]);
});