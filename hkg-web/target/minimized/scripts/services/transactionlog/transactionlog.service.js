define(["hkg"],function(a){a.register.factory("TransactionLogService",["$resource","$rootScope",function(d,b){var c=d(b.centerapipath+"transactionlog/:action",{action:"@actionName"},{retrieveAllTransactionLogs:{method:"GET",isArray:true,params:{action:"retrieve"}},retrieveTransactionLogsBycriteria:{method:"POST",isArray:true,params:{action:"retrieve"}},resend:{method:"POST",isArray:true,params:{action:"resend"}},retrievePrerequisite:{method:"GET",params:{action:"retrieve/prerequisite"}},downloadErrorFile:{method:"GET",params:{action:"downloadDocument"}}});
return c
}])
});