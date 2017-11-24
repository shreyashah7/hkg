define(['hkg'], function(hkg) {
    hkg.register.factory('PrintService', ['$resource', '$rootScope', function(resource, rootScope) {
            var pricelist = resource(rootScope.centerapipath + 'print/:action',
                    {
                    },
                    {
                        search: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: '/retrievesearcheddata'
                            }
                        },
                        generatePrintData: {
                            method: 'POST',
                            params: {
                                action: '/generatePrintData'
                            }
                        },
                        downloadPDFReport: {
                            method: 'GET',
                            params: {
                                action: '/downloadPDFReport'
                            }
                        },
                        retrieveSearchedLotsAndPackets: {
                            method: 'POST',
                            params: {
                                action: '/retrieveSearchedLotsAndPackets'
                            }
                        },
                        retrieveInformationByWorkallotment: {
                            method: 'POST',
                            params: {
                                action: '/retrieveInformationByWorkallotment'
                            }
                        },
                        retrieveInformationByWorkallotmentPrint: {
                            method: 'POST',
                            params: {
                                action: '/retrieveInformationByWorkallotmentPrint'
                            }
                        },
                        retrievePacketById: {
                            method: 'POST',
                            params: {
                                action: '/retrievepacketbyid'
                            }
                        }
                    }

            );
            return pricelist;
        }]);
});


