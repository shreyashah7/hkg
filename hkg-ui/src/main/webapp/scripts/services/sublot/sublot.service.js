define(['hkg'], function (hkg) {
    hkg.register.factory('SublotService', ['$resource', '$rootScope', function (resource, rootScope) {
            var sublot = resource(rootScope.centerapipath + 'sublot/:action',
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
                        retrievePrerequisite: {
                            method: 'GET',
                            params: {
                                action: '/retrieveprerequisite'
                            }
                        },
                        search: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: '/retrievesearchedsublots'
                            }
                        },
                        retrieveSublots: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: '/retrievesublots'
                            }
                        },
                        retrieveSublotsbyParcel: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: '/retrievesublotsbyparcel'
                            }
                        },
                        createSublot: {
                            method: 'PUT',
                            params: {
                                action: '/createsublot'
                            }
                        },
                        updateSublot: {
                            method: 'PUT',
                            params: {
                                action: '/updatesublot'
                            }
                        },
                        deleteSublot: {
                            method: 'POST',
                            params: {
                                action: '/deletesublot'
                            }
                        },
                        retrieveAllottedParcelAndInvoice: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: '/retrieveallottedparcelandinvoice'
                            }
                        }

                    }

            );
            return sublot;
        }]);
});


