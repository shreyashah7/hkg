define(['hkg'], function(hkg) {
    hkg.register.factory('IssueReceiveStockService', ['$resource', '$rootScope', function(resource, rootScope) {
            var issue = resource(rootScope.centerapipath + 'issue/:action', {}, {
                fieldSequenceExist: {
                    method: 'PUT',
                    params: {
                        action: '/doesfieldsequenceexist'
                    }
                },
                search: {
                    method: 'GET',
                    params: {
                        action: '/retrievelotandpacket'
                    }
                },
                issue: {
                    method: 'POST',
                    params: {
                        action: '/issue'
                    }
                },
                receive: {
                    method: 'POST',
                    params: {
                        action: '/receive'
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
                generatePDF: {
                    method: 'POST',
                    params: {
                        action: '/generatePDF'
                    }
                },
                retrieveDesignationIds: {
                    method: 'POST',
                    params: {
                        action: '/retrievedesginationids'
                    }
                },
                retrieveFieldSeqMap: {
                    method: 'POST',
                    params: {
                        action: '/retrievefieldseqmap'
                    }
                },
                retrieveModifiers: {
                    method: 'POST',
                    params: {
                        action: '/retrievemodifiers'
                    }
                },
                retrieveStockById: {
                    method: 'POST',
                    params: {
                        action: '/retrievestockbyid'
                    }
                },
                saveAll: {
                    method: 'POST',
                    params: {
                        action: '/saveall'
                    }
                },
                retrieveStockBySlip: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: '/retrievebyslip'
                    }
                },
                collect: {
                    method: "POST",
                    params: {
                        action: '/collect'
                    }
                },
                issueInward: {
                    method: "POST",
                    params: {
                        action: "/issueInward"
                    }
                },
                receiveInward: {
                    method: "POST",
                    params: {
                        action: "receiveinward"
                    }
                },
                retrieveUsersByDepartment: {
                    method: "POST",
                    isArray: true,
                    params: {
                        action: "/retrieveusersbydepartment"
                    }
                },
                reject: {
                    method: "POST",
                    params: {
                        action: "/reject"
                    }
                },
                retrieveAssociatedDepartments: {
                    method: "POST",
                    isArray: true,
                    params: {
                        action: "/retrieveAssociatedDepartments"
                    }
                },
                returnStock: {
                    method: "POST",
                    params: {
                        action: "/returnStock"
                    }
                },
                retrievePendingIssued: {
                    method: "GET",
                    isArray: true,
                    params: {
                        action: "/retrievependingissued"
                    }
                },
                retrieveDesignationByDept: {
                    method: "POST",
                    isArray: true,
                    params: {
                        action: "/retrieveDesignationForDept"
                    }
                }
            }

            );
            return issue;
        }]);
});

