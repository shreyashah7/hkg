define(['hkg'], function(hkg) {
    hkg.register.factory('ManageHolidayService', ['$resource', '$rootScope', function(resource, rootScope) {
            var Holiday = resource(rootScope.apipath + 'holiday/:action',
                    {
                        // action: '@actionName'
                    },
                    {
                        retrieveAllHoliday: {
                            method: 'GET',
                            isArray: false,
                            params: {
                                action: 'retrieve'
                            }
                        },
                        retrieveHolidayById: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'retrieve'
                            }
                        },
                        retrievePreviousYearDistinctHoliday: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'retrievePreviousYearDistinctHoliday'
                            }
                        },
                        saveHoliday: {
                            method: 'PUT',
                            isArray: false,
                            params: {
                                action: 'create'
                            }
                        },
                        editHoliday: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'update'
                            }
                        },
                        removeHoliday: {
                            method: 'POST',
                            params: {
                                action: 'delete'
                            }
                        },
                        forceHolidayAdd:{
                            method:'POST',
                            params:{
                                action:'forceHolidayAdd'
                            }
                        }
                    });
            return Holiday;
        }]);
});
