define(['hkg'], function(hkg) {
    hkg.register.factory('Event', ['$resource', '$rootScope', function(resource, rootScope) {
            var Event = resource(rootScope.apipath + 'event/:action',
                    {
                    },
                    {
                        createEvent: {
                            method: 'PUT',
                            params: {
                                action: 'create'
                            }
                        },
                        updateEvent: {
                            method: 'POST',
                            params: {
                                action: 'update'
                            }
                        },
                        doesEventNameExist: {
                            method: 'POST',
                            params: {
                                action: 'doeseventnameexist'
                            }
                        },
                        retrieveEventById: {
                            method: 'POST',
                            params: {
                                action: 'retrieve'
                            }
                        },
                        retrieveEventCategories: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'retrieveeventcategories'
                            }
                        },
                        doesCategoryNameExist: {
                            method: 'POST',
                            params: {
                                action: 'doescategorynameexist'
                            }
                        },
                        createEventCategory: {
                            method: 'POST',
                            params: {
                                action: 'createeventcategory'
                            }
                        },
                        updateEventCategory: {
                            method: 'POST',
                            params: {
                                action: 'updateeventcategory'
                            }
                        },
                        retrieveAllEvents: {
                            method: 'POST',
                            params: {
                                action: 'retrieveevents'
                            }
                        },
                        removeEvent: {
                            method: 'POST',
                            params: {
                                action: 'delete'
                            }
                        },
                        archieveEvent: {
                            method: 'POST',
                            params: {
                                action: 'archieveevent'
                            }
                        },
                        retrieveCategorySuggestions: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'retrievecategorysuggestions'
                            }
                        },
                        retrieveRegistrationFormNames: {
                            method: 'GET',
                            isArray: false,
                            params: {
                                action: 'retrieveregistrationformnames'
                            }
                        },
                        retrieveRegistrationFields: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'retrieveregistrationfields'
                            }
                        },
                        retrieveForUser: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'retrievebyuser'
                            }
                        },
                         editRegistrationforevent: {
                            method: 'POST',
                            params: {
                                action: 'editRegistrationforevent'
                            }
                        },
                        registerForEvent: {
                            method: 'POST',
                            params: {
                                action: 'registerforevent'
                            }
                        },
                        cancelRegistration: {
                            method: 'POST',
                            params: {
                                action: 'cancelregistration'
                            }
                        },
                        retrieveCustomValues: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'retrievecustomvalues'
                            }
                        },
                        retrieveCustomValuesByUser : {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'retrievecustomvaluesbyuser'
                            }
                        },
                        uploadFile: {
                            method: 'POST',
                            params: {
                                action: 'uploadfile'
                            }
                        },
                        retrieveActiveEventCategories: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'retriactiveeveeventcategories'
                            }
                        },
                        createPhotoGallery:{
                            method: 'POST',
                            params: {
                                action: 'createphotogallery'
                            }
                        },
                        retrieveImagePaths:{
                            method: 'POST',
                            params: {
                                action: 'retrieveimagepaths'
                            }
                        },
                         retrieveImageThumbnailPaths:{
                          method: 'POST',
                            params: {
                                action: 'retrieveimagethumbnailpaths'
                            }  
                        },
                        addCustomDataToCategoryDataBean: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'addCustomDataToCategoryDataBean'
                            }
                        },
                        retrieveUserRegistrationEntities : {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'retrieveUserRegistrationEntities'
                            }
                        }
                        
                    }
            );
            return Event;
        }]);
});

                