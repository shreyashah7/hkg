define(['hkg'], function(hkg) {
    hkg.register.factory('LotService', ['$resource', '$rootScope', function(resource, rootScope) {
            var lot = resource(rootScope.centerapipath + 'lot/:action', {}, {
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
                        action: '/createlot'
                    }
                },
                /** delete is a keyword. Don't use it as object property or any variable name **/
//                delete: {
//                    method: 'POST',
//                    params: {
//                        action: '/delete'
//                    }
//                },
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
                        action: '/retrievesearchedlot'
                    }
                },
                searchLotFromWorkAllocation: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: '/retrievelotfromworkallocation'
                    }
                },
                retrieveLotByParcelId: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: '/retrievelotbyparcelid'
                    }
                },
                retrieveLotById: {
                    method: 'POST',
                    params: {
                        action: '/retrievelotbyid'
                    }
                },
                mergeLot: {
                    method: 'POST',
                    params: {
                        action: '/mergelot'
                    }
                },
                splitLot: {
                    method: 'POST',
                    params: {
                        action: '/splitlot'
                    }
                },
//                fieldSequencExist: {
//                    method: 'PUT',
//                    params: {
//                        action: '/doesfieldsequenceexist'
//                    }
//                },
                retrieveRootNodeDesignationIds: {
                    method: 'POST',
                    params: {
                        action: 'retrieverootnodedesignationid'
                    }
                },
                retrieveLot: {
                    method: 'POST',
                    params: {
                        action: 'retrievelot'
                    }
                },
                deleteLot: {
                    method: 'POST',
                    params: {
                        action: '/deletelot'
                    }
                },
                 retrieveFranchiseDetails: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: '/retrievefranchisedetails'
                    }
                },
                 isLotSequenceNumberExist: {
                    method: 'PUT',                    
                    params: {
                        action: '/doesfieldsequenceexist'
                    }
                },
                getnextlotsequence:{
                    method:"POST",
                    params:{
                        action:"/getnextlotsequence"
                    }
                }
            }

            );
            return lot;
        }]);
});


