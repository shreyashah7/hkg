define(['hkg'], function(hkg) {
    hkg.register.factory('Employee', ['$resource', '$rootScope', function(resource, rootScope) {
            var Employee = resource(
                    rootScope.apipath + 'employee/:action', //url being hit
                    {
                        action: '@actionName'
                    }, // url perameters

            {//methods

//                retrieveComboValuesOfEmpPage: {
//                    method: 'GET',
//                    params: {
//                        action: 'retrieve/combovaluesbykeycodes'
//                    }
//                },
                addEmployee: {
                    method: 'PUT',
                    params: {
                        action: 'create'
                    }
                },
                retrieveEmployeeDetail: {
                    method: 'POST',
                    params: {
                        action: 'retrieve'
                    }
                },
                searchEmployee: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'search'
                    }
                },
                terminateEmployee: {
                    method: 'POST',
                    params: {
                        action: 'terminate'
                    }
                },
                updateEmployee: {
                    method: 'POST',
                    params: {
                        action: 'update'
                    }
                },
                updateProfile: {
                    method: 'POST',
                    params: {
                        action: 'updateProfile'
                    }
                },
                updateProfileOfSuperAdmin: {
                    method: 'POST',
                    params: {
                        action: 'updateProfileOfSuperAdmin'
                    }
                },
                removeFileFromTemp: {
                    method: 'POST',
                    params: {
                        action: 'removeFileFromTemp'
                    }
                },
                doesEmployeeNameExist: {
                    method: 'PUT',
                    params: {
                        action: "doesempnameexist"
                    }
                },
//                retrieveEmployeeCode: {
//                    method: 'GET',
//                    params: {
//                        action: 'retrieve/employeeCodes'
//                    }
//                },
                retrieveShiftDepMap: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/shiftsofdep'
                    }
                },
                retrieveAvatar: {
                    method: 'GET',
                    params: {
                        action: 'retrieve/avatar'
                    }
                },
                uploadFile: {
                    method: 'POST',
                    params: {
                        action: 'uploadfile'
                    }
                },
                changePassword: {
                    method: 'POST',
                    params: {
                        action: 'changepassword'
                    }
                },
//                retrieveEmpForEdit: {
//                    method: 'POST',
//                    params: {
//                        action: 'retrieveempforedit'
//                    }
//                },
                doesUserIdExist: {
                    method: 'PUT',
                    params: {
                        action: "doesuseridexist"
                    }
                },
                retrievePrerequisite: {
                    method: 'GET',
                    params: {
                        action: 'retrieveprerequisite'
                    }
                },
                retrieveProfilePrerequisite: {
                    method: 'GET',
                    params: {
                        action: 'profile/retrieveprerequisite'
                    }
                },
                retrieveThemes: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrievethemes'
                    }
                },
                setTheme: {
                    method: 'POST',
                    params: {
                        action: 'settheme'
                    }
                },
                createPhotoGallery: {
                    method: 'POST',
                    params: {
                        action: 'createphotogallery'
                    }
                },
                retrieveImageThumbnailPaths: {
                    method: 'GET',
                    params: {
                        action: 'retrieveimagethumbnailpaths'
                    }
                },
                retrieveDesgByDept: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieveDesgByDept'
                    }
                },
                retrieveByIdForTransfer : {
                    method: 'POST',
                    params: {
                        action: 'retrieveByIdForTransfer'
                    }
                }
                
            }
            );
            return Employee;
        }]);
});


