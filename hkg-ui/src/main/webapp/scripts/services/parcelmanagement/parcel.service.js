define(['hkg'], function (hkg) {
    hkg.register.factory('ParcelService', ['$resource', '$rootScope', function (resource, rootScope) {
            var parcel = resource(rootScope.centerapipath + 'parcel/:action',
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
                        createParcel: {
                            method: 'PUT',
                            params: {
                                action: '/createparcel'
                            }
                        },
                        updateParcel: {
                            method: 'PUT',
                            params: {
                                action: '/updateparcel'
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
                                action: '/retrievesearchedparcel'
                            }
                        },
                        retrieveParcelByInvoiceId: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: '/retrieveparcelbyinvoiceids'
                            }
                        },
                        retrieveParcelById: {
                            method: 'POST',
                            params: {
                                action: '/retrieveparcelbyid'
                            }
                        },
                        fieldSequencExist: {
                            method: 'PUT',
                            params: {
                                action: '/doesfieldsequenceexist'
                            }
                        },
                        deleteParcel: {
                            method: 'POST',
                            params: {
                                action: '/deleteparcel'
                            }
                        },
                        retrieveAllParcels: {
                            method: 'POST',
                            params: {
                                action: '/retrieveallparcels'
                            },
                            isArray:true
                        },
                        getNextParcelSequence: {
                            method: 'GET',
                            params: {
                                action: '/getnextparcelsequence'
                            }
                        },
                        isParcelIdExists: {
                            method: 'POST',
                            params: {
                                action: '/isparcelidexists'
                            }
                        },
                        retrieveAssociatedRoughParcels: {
                            method: 'POST',
                            isArray:true,
                            params: {
                                action: '/retrieveassociatedroughparcels'
                            }
                        },
                        deLinkRoughParcelWithPurchase :{
                            method: 'POST',
                            params: {
                                action: '/delinkroughparcelwithpurchase'
                            }
                        },
                        getTotalCountOfParcels : {
                            method: 'POST',
                            params: {
                                action: '/countParcels'
                            }
                        }
                    }

            );
            return parcel;
        }]);
});


