define(['hkg'], function (hkg) {
    hkg.register.factory('TransactionLogService', ['$resource', '$rootScope', function (resource, rootScope) {
            var transactionLog = resource(
                    rootScope.centerapipath + 'transactionlog/:action', //url being hit
                    {
                        action: '@actionName'
                    }, // url perameters

            {//methods
                retrieveAllTransactionLogs: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieve'
                    }
                },
                retrieveTransactionLogsBycriteria: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve'
                    }
                },
                resend: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'resend'
                    }
                },
                retrievePrerequisite: {
                    method: 'GET',
                                      params: {
                        action: 'retrieve/prerequisite'
                    }
                },
                downloadErrorFile: {
                    method: 'GET',
                    params: {
                        action: 'downloadDocument'
                    }
                }
            });
            return transactionLog;
        }]);
});