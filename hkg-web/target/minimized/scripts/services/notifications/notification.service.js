angular.module("notificationmodule",[]).factory("NotificationService",["$resource","$q","$rootScope",function(c,b,a){var d=c(a.apipath+"notification/:action",{action:"@actionName"},{removeNotification:{method:"POST",isArray:true,params:{action:"remove"}},retrieveNotifications:{method:"GET",isArray:true,params:{action:"retrievenotification"}},createNotificationConfiguration:{method:"POST",params:{action:"createNotificationConfiguration"}},retrieveAllNotificationConfigurations:{method:"POST",isArray:true,params:{action:"retrieveAllNotificationConfigurations"}},updateNotificationConfiguration:{method:"POST",params:{action:"updateNotificationConfiguration"}},searchNotificationConfigurations:{method:"POST",isArray:true,params:{action:"searchnotificationconfigurations"}},retrieveNotificationConfigurationById:{method:"POST",params:{action:"retrieveNotificationConfigurationById"}}});
return d
}]);