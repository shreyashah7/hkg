define(["hkg","notificationService"],function(a,b){a.register.controller("showNotification",["$rootScope","$translate","$scope","$filter","NotificationService","$location",function(c,g,d,f,e,h){c.mainMenu="manageLink";
c.childMenu="manageNotifications";
c.activateMenu();
d.initializePage=function(){d.retrieveNotifications()
};
d.i18Notification="NOTIFICATIONS.";
d.removeNotifications=function(i){e.removeNotification(i,function(j){d.retrieveNotifications()
},function(){var k="Failed to remove notification";
var j=c.error;
c.addMessage(k,j)
})
};
d.retrieveNotifications=function(){e.retrieveNotifications(function(l){c.notifications=angular.copy(l);
d.str=[];
d.notificationText=[];
d.shortNotificationText=[];
for(var i=0;
i<l.length;
i++){var k="{";
var m;
var j=angular.copy(l[i].notificationDataBean.description);
d.descriptionInJson=JSON.parse(j);
d.str[i]=JSON.stringify(l[i].notificationDataBean.instanceType);
d.str[i]=d.str[i].replace(/"/g,"");
Object.keys(d.descriptionInJson).forEach(function(n){m=f("translate")(n+"."+d.descriptionInJson[n]);
k=k+"'"+n+"':'"+m+"',"
});
k=k.substring(0,k.length-1)+"}";
d.notificationText[i]=f("translate")("NTN."+d.str[i],k);
d.shortNotificationText[i]=angular.copy(d.notificationText[i]).slice(0,135);
console.log(d.notificationText[i].length)
}})
};
d.goToNotificationConfig=function(){h.path("/managenotificationconfig")
}
}])
});