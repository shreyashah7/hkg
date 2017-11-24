define(['hkg'], function (hkg) {
    hkg.register.factory('InvoiceService', ['$resource', '$rootScope', function (resource, rootScope) {
            var invoice = resource(rootScope.centerapipath + 'invoice/:action',
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
                        createInvoice: {
                            method: 'PUT',
                            params: {
                                action: '/createinvoice'
                            }
                        },
                        updateInvoice: {
                            method: 'PUT',
                            params: {
                                action: '/updateinvoice'
                            }
                        },
                        deleteInvoice: {
                            method: 'POST',
                            params: {
                                action: '/deleteinvoice'
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
                                action: '/retrievesearchedinvoices'
                            }
                        },
                        fieldSequencExist: {
                            method: 'PUT',
                            params: {
                                action: '/doesfieldsequenceexist'
                            }
                        },
                        retrieveInvoice: {
                            method: 'POST',
                            params: {
                                action: '/retrieveinvoicebyid'
                            }
                        },
                        retrieveInvoices: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: '/retrieveinvoices'
                            }
                        },
                        getNextInvoiceSequence: {
                            method: 'GET',
                            params: {
                                action: '/getnextinvoicesequence'
                            }
                        },
                        isInvoiceIdExists: {
                            method: 'POST',
                            params: {
                                action: '/isinvoiceidexists'
                            }
                        },
                        linkRoughParcelWithPurchase:{
                            method: 'POST',
                            params: {
                                action: '/linkroughpurchase'
                            }
                        },
                        getTotalCountOfInvoices : {
                            method: 'GET',
                            params: {
                                action: '/countInvoices'
                            }
                        },
                        getTotalCountOfSearchInvoices : {
                            method: 'POST',
                            params: {
                                action: '/countSearchInvoices'
                            }
                        }
                    }

            );
            return invoice;
        }]);
});


