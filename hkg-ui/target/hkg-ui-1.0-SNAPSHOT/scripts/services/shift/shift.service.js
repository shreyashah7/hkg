define(['hkg'], function(hkg) {
    hkg.register.factory('ShiftService', ['$resource', '$rootScope', function(resource, rootScope) {
            var Shift = resource(rootScope.apipath + 'shift/:action', {}, {
                retrieveShiftsWithDetails: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieve'
                    }
                },
                retrieveCustomFieldDataById: {
                    method: 'POST',
                    params: {
                        action: 'retrieve'
                    }
                },
                retrieveDepartmentList: {
                    method: 'GET',
                    isArray: false,
                    params: {
                        action: 'retrieveDepartmentList'
                    }
                },
                retreiveShiftTree: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieveShiftsTree'
                    }
                },
                retrieveHolidayList: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieveHolidayList'
                    }
                },
                retrieveEventList: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieveevents'
                    }
                },
                retrieveDefaultShift: {
                    method: 'GET',
                    isArray: false,
                    params: {
                        action: 'retrieveDefaultShift'
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
                createTemporaryShift: {
                    method: 'POST',
                    params: {
                        action: 'createTempShift'
                    }
                },
                updateTemporaryShift: {
                    method: 'POST',
                    params: {
                        action: 'updateTempShift'
                    }
                },
                userExisrForShift: {
                    method: 'PUT',
                    params: {
                        action: 'checkusers'
                    }
                }
            });
            return Shift;
        }]);
});