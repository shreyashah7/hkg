define(['hkg'], function (hkg) {
    hkg.register.factory('PacketService', ['$resource', '$rootScope', function (resource, rootScope) {
            var packet = resource(rootScope.centerapipath + 'packet/:action',
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
                        createPacket: {
                            method: 'POST',
                            params: {
                                action: '/createpacket'
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
                        retrievePacketByLotId: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: '/retrievepacketbylotid'
                            }
                        },
                        search: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: '/retrievesearchedpacket'
                            }
                        },
                        retrievePacketById: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: '/retrievepacketbyid'
                            }
                        },
                        mergePacket: {
                            method: 'POST',
                            params: {
                                action: '/mergepacket'
                            }
                        },
                        getlots: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'lots'
                            }
                        }, retrieveNextNodeDesignationIds: {
                            method: 'POST',
                            params: {
                                action: '/retrievenextnodedesignationids'
                            }
                        },
                        generateBarcode: {
                            method: 'POST',
                            params: {
                                action: '/generateBarcode'
                            }
                        },
                        isPacketSequenceNumberExist: {
                            method: 'PUT',
                            params: {
                                action: '/ispacketsequencexist'
                            }
                        },
                        deletePacket: {
                            method: 'PUT',
                            params: {
                                action: '/deletepacket'
                            }
                        },
                        getNextPacketSequence: {
                            method: 'PUT',
                            params: {
                                action: '/getnextpacketsequence'
                            }
                        }
                    }

            );
            return packet;
        }]);
});


