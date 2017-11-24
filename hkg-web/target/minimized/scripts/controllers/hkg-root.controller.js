var MainController=["$scope","$rootScope","$route","CustomField","$location","$timeout","Notification","MessagingPopup","WorkAllocation","$filter","$translate","$q","$compile","$http","$modalStack","LABELMASTER","ReportMaster","Common","EmpTemp","MasterProxyLogin","FranchiseConfig","hotkeys",function(c,d,v,t,b,u,n,p,j,l,f,r,s,w,i,e,o,a,k,h,q,g){d.location=b;
d.mask={mask:false,imagePath:"images/ajax-loader.gif"};
d.uploadingFiles={fileName:[]};
d.maxValueForTruncate=25;
d.message={};
d.primaryFields=["carat_type$DRP$Long","COL$DRP$Long","CLR$DRP$Long","SHAPE$DRP$Long"];
d.fourCMap=["size$DRP$Long","COL$DRP$Long","CLR$DRP$Long","SHAPE$DRP$Long"];
d.isLabelEntryInDatabase=true;
c.$on("$viewContentLoaded",function(){setTimeout(function(){if($(".ui-grid-filter-input:input:enabled:visible:first, .form-control:not(#proxy):not(#designation):input:enabled:visible:first")!==undefined){$(".ui-grid-filter-input:input:enabled:visible:first, .form-control:not(#proxy):not(#designation):input:enabled:visible:first").focus()
}},500)
});
d.isPrint=false;
g.add({combo:"ctrl+m",allowIn:["INPUT","SELECT","TEXTAREA"],callback:function(y,x){document.body.getElementsByClassName("menu-trigger")[0].click()
}});
Pace.on("start",function(){if(angular.isDefined(d.maskLoading)){u(function(){d.maskLoading()
})
}});
Pace.on("hide",function(){if(angular.isDefined(d.unMaskLoading)){u(function(){d.unMaskLoading()
})
}});
d.retrieveMinAge=function(){if(d.canAccess("manageEmployees")){k.retrievePrerequisite(function(y){var x=y.agelimit;
if(x.minAge!==null){d.minAge=x.minAge
}else{d.minAge=18
}})
}};
if(!!localStorage.getItem("proxyLogin")){c.userProxy=localStorage.getItem("proxyLogin")
}d.loadLabels=function(){if(d.isLabelEntryInDatabase){d.isLabelEntryInDatabase=false;
e.initLabelMaster()
}else{d.isLabelEntryInDatabase=true
}};
d.retrieveFranchiseConfiguration=function(){q.retrieveAllConfiguration(function(x){c.franchiseConfig=angular.copy(x);
if(c.franchiseConfig.CURR_VISIBILITY_STATUS!==undefined){d.haveValue=c.franchiseConfig.CURR_VISIBILITY_STATUS
}},function(){})
};
d.commonentity="COMMON.";
d.viewEncryptedData=false;
d.registerLocationChangeEvent=function(x){c.$on("$locationChangeStart",function(y){if(x.$dirty&&d.isLoggedIn){if(!confirm("You have unsaved changes, go back?")){y.preventDefault()
}else{x.$dirty=false;
x.$setPristine()
}}})
};
d.switchLanguage=function(y,x){f.use(y+x)
};
d.containerHeight=window.innerHeight-140;
d.isLoggedIn=false;
d.success=0;
d.failure=1;
d.warning=2;
d.error=3;
d.getMessageCssClass=function(x){if(typeof x==="String"&&x.lenght>1){return x
}if(x===0){return"alert-success"
}if(x===1||x===3){return"alert-danger"
}if(x===2){return"alert-warning"
}};
d.successTimeout=30000;
d.failureTimeout=30000;
d.warningTimeout=30000;
d.createLabelAutomatically=false;
d.getCreateLabelAutomaticallyFlag=function(){return d.createLabelAutomatically
};
d.mainMenu="dashboard";
d.childMenu="dashboardLink";
d.activateMenu=function(){d.reportMenu=[];
if(d.menu!==undefined){angular.forEach(d.menu,function(x){if((x.menuType==="M"||x.menuType==="DM"||x.menuType==="RMI"||x.menuType==="DE")&&$("a#"+x.featureName).hasClass("active")){$("a#"+x.featureName).removeClass("active");
if(x.menuType==="RMI"){d.reportMenu.push(x)
}}})
}if(d.mainMenu.trim()===""){if($("a#manageLink").hasClass("active")){$("a#manageLink").click();
$("a#manageLink").removeClass("active")
}if($("a#stockLink").hasClass("active")){$("a#stockLink").click();
$("a#stockLink").removeClass("active")
}if($("a#dashboardLink").hasClass("active")){$("a#dashboardLink").removeClass("active")
}if($("a#reportLink").hasClass("active")){$("a#reportLink").click();
$("a#reportLink").removeClass("active")
}}else{if(!$("a#"+d.mainMenu).hasClass("active")){$("a#"+d.mainMenu).addClass("active");
$("a#"+d.mainMenu).click()
}}if(d.childMenu.trim()!==""){$("a#"+d.childMenu).addClass("active")
}};
d.activateMenu();
d.validations=[];
d.addMessage=function(E,C,B,A,z){d.scrollTo("topheader");
var x=d.getMessageCssClass(C);
var D={msg:E,type:x,translateValueMap:z,showLoginLink:B,linkText:A};
var y="";
if(z!==undefined&&z!==null){$.each(D.translateValueMap,function(F,G){var H=l("translate")(G);
z[F]=H
});
y=l("translate")("MSG."+D.msg,z)
}else{y=l("translate")("MSG."+D.msg)
}D.msg=y;
d.validations.push(D);
if(C===d.success){d.timeout=d.successTimeout
}else{if(C===d.failure||C===d.error){d.timeout=d.failureTimeout
}else{if(C===d.warning){d.timeout=d.warningTimeout
}}}if(d.timeout){u(function(){d.closeAlertMessage(d.validations.indexOf(D))
},d.timeout)
}};
d.closeAlertMessage=function(x){d.validations.splice(x,1)
};
d.maskLoading=function(){d.mask.mask=true
};
d.unMaskLoading=function(){d.mask.mask=false
};
d.showFirstLogin=function(){d.setMessage("","",false);
d.showLogin()
};
d.showLogin=function(){$("#login").modal("show")
};
d.loginCancel=function(){$("#login").modal("hide");
d.removeModalOpenCssAfterModalHide()
};
pingServerOnLoad();
d.fetchVersion();
d.loginTry=-1;
d.isFirstReq=true;
d.login={};
d.menuManageAccess=false;
d.menuManage="manage";
d.menuStockAccess=false;
d.menuStock="stock";
d.menuReportAccess=false;
d.menuReport="report";
d.ConvertTimeStamptodate=function(y){if(y!==null){var A=new Date(y);
var x=A.getDate();
x=(x<10)?"0"+x:x;
var z=A.getMonth()+1;
z=(z<10)?"0"+z:z;
return x+"/"+z+"/"+A.getFullYear()
}else{return"NA"
}};
d.ConvertTimeStampToDateTodayYesterday=function(A){if(A!==null){var F=new Date(A);
var C=new Date(A);
var D=new Date();
var B=C.setHours(0,0,0,0)===D.setHours(0,0,0,0);
if(B){return F.toLocaleTimeString()
}var y=new Date(D);
y.setDate(D.getDate()-1);
var x=C.setHours(0,0,0,0)===y.setHours(0,0,0,0);
if(x){return"yesterday"
}var E=C.getDate();
E=(E<10)?"0"+E:E;
var z=C.getMonth()+1;
z=(z<10)?"0"+z:z;
return E+"/"+z+"/"+C.getFullYear()
}else{return"NA"
}};
d.encryptPass=function(z,D){if(z!==null&&z!==undefined&&z!==""){var C=z.length;
var B="";
for(var A=0;
A<C;
A++){var x=z.charAt(A);
var y=d.printableChars.indexOf(x);
if(y!==-1){if(D){y+=(A+C);
if(y>=(d.printableChars.length)){y-=(d.printableChars.length)
}}else{y-=(A+C);
if(y<0){y+=(d.printableChars.length)
}}x=d.printableChars.charAt(y);
B+=x
}else{B+=x
}}return B
}else{return z
}};
d.logout=function(){d.user=null;
c.username=c.password=null;
delete c.userProxy;
c.$emit("event:logoutRequest")
};
d.alertId=undefined;
c.setAlert=function(y,x){d.alertId=y.messageObj;
d.$broadcast("alert:clicked",d.alertId);
b.path("/managealert");
$("#alert").popover("hide");
c.closeAlert(angular.copy(y),x)
};
c.setAlertId=function(){d.alertId=undefined
};
c.closeAlert=function(z,x){var A=function(){c.alerts.splice(x,1)
};
var y=function(){};
p.markAsClosed(z,A,y)
};
d.retrieveNotificationCount=function(){var y=function(z){d.notificationCount=z
};
var x=function(){};
n.retrieveNotificationCount(y)
};
d.translateValue=function(x){return l("translate")(x)
};
d.retrieveNotificatonPopUp=function(){var x=function(C){d.notificationsPopUp=angular.copy(C);
c.str=[];
c.notificationText=[];
for(var y=0;
y<C.length;
y++){var B="{";
var D;
var A=angular.copy(C[y].notificationDataBean.description);
c.descriptionInJson=JSON.parse(A);
c.str[y]=JSON.stringify(C[y].notificationDataBean.instanceType);
c.str[y]=c.str[y].replace(/"/g,"");
Object.keys(c.descriptionInJson).forEach(function(E){D=l("translate")(E+"."+c.descriptionInJson[E]);
B=B+"'"+E+"':'"+D+"',"
});
B=B.substring(0,B.length-1)+"}";
c.notificationText[y]=l("translate")("NTN."+c.str[y],B)
}var z='<div class="list-group list-special" ng-if="!notificationsPopUp || notificationsPopUp.length === 0"><div class = "row list-group-item" ><div class="col-xs-2 text-center notification-image-icon"><span class="glyphicon  glyphicon-user"></span></div><div class = "col-xs-10 hkg-bold">{{"NOTIFICATIONS." + "No notifications available." | translate}}</div></div></div><div style="overflow-y:auto; overflow-x:hidden; height:500px;" ng-if="notificationsPopUp.length > 0" class="list-group list-special"><a ng-repeat="notification in notificationsPopUp" class="row list-group-item" onclick="$(&quot;#Notifications&quot;).popover(&quot;hide&quot;);"href="#shownotification" " ><div class="col-xs-2 text-center notification-image-icon"><span class="glyphicon  glyphicon-user"></span></div><div class="col-xs-10"><div class="wordRap" ng-class="{\'hkg-bold\': notification.isSeen === false }">{{notificationText[$index]}}</div></div></a>  </div><a class="pull-right" ng-if="notificationsPopUp.length > 0" onclick="$(&quot;#Notifications&quot;).popover(&quot;hide&quot;);"href="#shownotification" " >&nbsp;&nbsp;&nbsp;&nbsp;{{"NOTIFICATIONS." + "Show all" | translate }}</a>';
$(".popover-content").html(s(z)(c));
d.retrieveNotificationCount()
};
n.retrieveNotificationsPopUp(x)
};
d.getCurrentServerDate=function(){if(d.session!==undefined){d.serverOffsetInMin=d.session.serverOffsetInMin;
var x=d.serverOffsetInMin+d.clientTimezoneOffset;
var y=new Date();
y.setMinutes(y.getMinutes()+x);
return y
}};
d.getCustomDataInSequence=function(x){if(x!==null){var y=[]
}angular.forEach(x,function(z){if(!angular.isDefined(z.sequenceNum)||z.sequenceNum===null){z.sequenceNum=1
}y.push(z)
});
y.sort(function(A,z){return parseInt(A.sequenceNum)-parseInt(z.sequenceNum)
});
return y
};
c.alerts=[];
c.retrieveAlerts=function(){var x=function(y){c.alerts=y.priority;
c.messagecount=y.count;
if(angular.isDefined(c.alerts)&&c.alerts.length>0){angular.forEach(c.alerts,function(z){z.shortMessage=""
});
$("#showalertmodal").modal({backdrop:"static",keyboard:false,show:true})
}};
p.retrieveAlerts(x)
};
c.reloadRoute=function(){v.reload()
};
d.isRefreshRequired=false;
d.$watch("allocationMap",function(z,x){var y=b.path().substring(1,b.path().length);
if(d.isRefreshRequired===false){if(y.length>0){angular.forEach(d.menu,function(B){if(y===B.featureURL){var A=B.menuLabel;
if(x===undefined||x===null){if(z!==undefined&&z!==null&&z[A]){d.isRefreshRequired=true
}else{d.isRefreshRequired=false
}}else{if(z===undefined||z===null){if(x!==undefined&&x!==null&&x[A]){d.isRefreshRequired=true
}else{d.isRefreshRequired=false
}}else{if(x[A]===undefined&&z[A]===undefined){d.isRefreshRequired=false
}else{if(x[A]!==z[A]){d.isRefreshRequired=true
}else{d.isRefreshRequired=false
}}}}}})
}}});
c.hideModal=function(){$("#showalertmodal").modal("hide");
$rootScope.removeModalOpenCssAfterModalHide()
};
c.markClosed=function(z,x){var A=function(){c.messagePopUp.splice(x,1);
c.messagecount=c.messagecount-1
};
var y=function(){};
p.markAsClosed(z,A,y)
};
c.markClosedPriorityMsg=function(z,x){var A=function(){c.alerts.splice(x,1);
c.messagecount=c.messagecount-1;
if(c.alerts.length===0){$("#showalertmodal").modal("hide");
$rootScope.removeModalOpenCssAfterModalHide()
}};
var y=function(){};
p.markAsClosed(z,A,y)
};
c.retrieveAlertCount=function(){c.retrieveAlerts()
};
c.refreshIntervalId=[];
c.$on("evaluateResultValue",function(y,x){alert("event"+x);
d.evaluateResultValue=x
});
c.$on("event:pollingStart",function(x){m()
});
c.$on("event:pollingStop",function(x){angular.forEach(c.refreshIntervalId,function(y){clearInterval(y)
});
c.alerts=[];
c.refreshIntervalId=[]
});
function m(){var x=setInterval(function(){c.retrieveAlertCount();
c.retrieveNotificationCount()
},10000);
if(c.refreshIntervalId.indexOf(x)===-1){c.refreshIntervalId.push(x)
}}d.$on("$routeChangeStart",function(y,z,x){if(b.path()!=="/viewreports"){localStorage.setItem("menuReportId",null)
}if(b.path()!=="/managemessage"){localStorage.removeItem("rootMessage")
}if(b.path()==="/printsvg"||b.path()==="/trackstockprint"||b.path()==="/printflow"){d.isPrint=true
}else{d.isPrint=false
}d.validations=[];
if(angular.isDefined(d.stream)){d.stream.stop()
}i.dismissAll();
d.isRefreshRequired=false;
if($(".select2-drop").length>0){$(".select2-drop").remove();
$(".select2-drop-mask").remove();
$(".select2-hidden-accessible").remove();
$(".select2-sizer").remove()
}});
d.$on("event:validate",function(y,x){var z=x.$name;
x.showValidation=false;
if(!x.$valid){x.showValidation=true;
if(x.$error.required){x.validationMsg=z+"RequiredMsg"
}else{if(x.$error.email){x.validationMsg=z+"EmailMsg"
}else{if(x.$error.minlength){x.validationMsg=z+"LengthMsg"
}}}}});
d.predicateBy=function(x){return function(z,y){if(z[x]>y[x]){return 1
}else{if(z[x]<y[x]){return -1
}}return 0
}
};
d.dateFormat="dd/MM/yyyy";
d.dateFormatWithTime="dd/MM/yyyy HH:mm a";
d.scrollTo=function(G){var E=C();
var x=H(G);
var y=x>E?x-E:E-x;
if(y<100){scrollTo(0,x-60);
return
}var B=Math.round(y/100);
if(B>=20){B=20
}var A=Math.round(y/25);
var F=x>E?E+A:E-A;
var z=0;
if(x>E){for(var D=E;
D<x;
D+=A){setTimeout("window.scrollTo(0, "+(F-60)+")",z*B);
F+=A;
if(F>x){F=x
}z++
}return
}for(var D=E;
D>x;
D-=A){setTimeout("window.scrollTo(0, "+(F-60)+")",z*B);
F-=A;
if(F<x){F=x
}z++
}function C(){if(self.pageYOffset){return self.pageYOffset
}if(document.documentElement&&document.documentElement.scrollTop){return document.documentElement.scrollTop
}if(document.body.scrollTop){return document.body.scrollTop
}return 0
}function H(I){var L=document.getElementById(I);
var K=L.offsetTop;
var J=L;
while(J.offsetParent&&J.offsetParent!==document.body){J=J.offsetParent;
K+=J.offsetTop
}return K
}};
d.doLogin=function(x){var y={username:d.login.username,password:d.encryptPass(d.login.password,true)};
if(d.session&&d.session.isProxyLogin){loginToMaster(y,true,h,c.userProxy)
}else{loginToMaster(y,true)
}};
d.retrieveUnreadMessages=function(){c.unreadMessages=[];
var x=function(y){c.unreadMessages=y;
if(angular.isDefined(c.unreadMessages)&&c.unreadMessages.length>0){$("#showmsgmodal").modal({keyboard:false,show:true})
}};
p.retrieveUnreadMessages(x)
};
d.retrieveUnreadMsgPopUp=function(){var x=function(z){d.messagePopUp=angular.copy(z);
var y='<div class="list-group list-special" ng-if="!messagePopUp || messagePopUp.length === 0"><div class = "row list-group-item" ><div class="col-xs-2 text-center notification-image-icon"><span class="glyphicon  glyphicon-user"></span></div><div class = "col-xs-10 hkg-bold">{{"MESSAGING." + "No unread messages available." | translate}}</div></div></div><div style="overflow-y:auto; overflow-x:hidden; height:500px;" ng-if="messagePopUp.length > 0" class="list-group list-special"><a class="row list-group-item" ng-repeat="message in messagePopUp"><div class="col-xs-2 text-center notification-image-icon"><span class="glyphicon glyphicon-user"></span></div><div class="col-xs-10"><div class="col-xs-10"><label class="control-label">{{message.createdBy}}</label></div><div class="col-xs-2 text-right" ng-show="{{message.status === \'P\'}}"><span title="Mark as read" class="glyphicon glyphicon-remove pointer pull-right" ng-click="markClosed(message, $index)"/></div><div class="col-xs-12"><label class="control-label">{{message.createdOn| date:\'mediumDate\'}} {{message.createdOn| date:\'shortTime\'}}</label></div><div class="col-xs-12 wordRap"  ng-show="{{message.messageBody !== null}}">{{message.messageBody}}</div><div class="col-xs-12"><span style="padding-left:0px" class="btn btn-link" ng-click="displayMessageDetails(message,$index)">{{ entity + \'View More Details\' | translate}}</span></div></div></a></div>';
$(".popover-content").html(s(y)(c))
};
p.retrieveUnreadMessages(x)
};
c.displayMessageDetails=function(y,x){if(y!==undefined){console.log("msg :"+JSON.stringify(y));
if(y.hasPriority===true){c.markClosedPriorityMsg(y,x)
}else{c.markClosed(y,x)
}if(b.path()==="/managemessage"){$("#messages").popover("hide");
$("#showalertmodal").modal("hide");
d.removeModalOpenCssAfterModalHide();
d.$broadcast("rootMessage",{rootMessage:y})
}else{localStorage.setItem("rootMessage",JSON.stringify(y));
$("#messages").popover("hide");
$("#showalertmodal").modal("hide");
d.removeModalOpenCssAfterModalHide();
b.path("/managemessage")
}}};
c.doProxyLogin=function(){var x=c.userProxy;
if(!!c.userProxy){a.authenticateProxy({id:c.userProxy},function(y){d.authToken=y.token;
localStorage.setItem("user",y.token);
if(c.rememberme){y.password=d.encryptPass(c.password,true);
localStorage.setItem("userDetail",JSON.stringify(y))
}else{localStorage.removeItem("userDetail")
}var z=d.pingServer();
z.then(function(){delete d.masterAuthToken;
localStorage.removeItem("masterAuthToken");
proxyAuthentication(h,x);
localStorage.setItem("proxyLogin",x);
d.retrieveNotificationCount()
})
},function(){console.log("failure..")
})
}};
c.changeDesignation=function(){w.post(d.appendAuthToken(d.centerapipath+"common/changepreferreddesignation"),d.session.currentDesignation).success(function(x){c.reloadRoute()
}).error(function(){})
};
d.removeModalOpenCssAfterModalHide=function(){$("body").removeClass("modal-open")
};
d.retrieveMsgPopUp=function(){$("#recipients").select2("data",undefined);
$("#recipients").select2("val",undefined);
d.i18EntityMessagingPopup="MESSAGING.";
d.message={};
d.addMessageData={};
d.dbType={};
var y=t.retrieveSectionWiseCustomFieldInfos("manageMessages");
y.then(function(z){d.customGeneralMessageTemplateData=angular.copy(z.genralSection);
d.generalMessageTemplate=d.getCustomDataInSequence(d.customGeneralMessageTemplateData)
},function(z){console.log("Failed: "+z)
},function(z){console.log("Got notification: "+z)
});
d.message.hasPriority=false;
d.clearMsgForm=function(){d.message.messageBody=null;
d.message.nameRecipient=null
};
d.prioritytooltip="Set as priority";
d.setpriority=function(){if(d.message.hasPriority===false){d.message.hasPriority=true
}else{d.message.hasPriority=false
}};
d.recipientValid=false;
d.$watch("message.nameRecipient",function(){d.recipientValid=false;
if(d.message.nameRecipient!=undefined){if(typeof(d.message.nameRecipient)=="string"){d.recipientValid=true
}}});
d.submitMsg=function(z){d.message.submitted=true;
if(z.$valid){if(d.message.nameRecipient!==null&&d.message.nameRecipient.length>0){d.message.submitted=false;
var A=function(){$("#messages").popover("hide")
};
d.message.messageCustom=d.addMessageData;
d.message.messageDbType=d.dbType;
p.saveMessage(d.message,A)
}else{d.message.nameRecipient=""
}}};
d.autoCompleteRecipient={multiple:true,closeOnSelect:false,placeholder:"Select recipients",initSelection:function(z,B){var A=[];
B(A)
},formatResult:function(z){return z.text
},formatSelection:function(z){return z.text
},query:function(C){var B=C.term;
d.tempNames=[];
var D=function(E){if(E.length==0){C.callback({results:d.tempNames})
}else{d.tempNames=E;
angular.forEach(E,function(F){d.tempNames.push({id:F.value+":"+F.description,text:F.label})
});
C.callback({results:d.tempNames})
}};
var z=function(){};
if(B.substring(0,2)=="@E"||B.substring(0,2)=="@e"){var A=C.term.slice(2);
p.retrieveUserList(A.trim(),D,z)
}else{if(B.substring(0,2)=="@R"||B.substring(0,2)=="@r"){var A=C.term.slice(2);
p.retrieveRoleList(A.trim(),D,z)
}else{if(B.substring(0,2)=="@D"||B.substring(0,2)=="@d"){var A=C.term.slice(2);
p.retrieveDepartmentList(A.trim(),D,z)
}else{if(B.substring(0,2)=="@G"||B.substring(0,2)=="@g"){var A=C.term.slice(2);
p.retrieveGroupList(A.trim(),D,z)
}else{if(B.substring(0,2)=="@A"||B.substring(0,2)=="@a"){var A=C.term.slice(2);
p.retrieveActivityList(A.trim(),D,z)
}else{if(B.length>0){var A=B;
p.retrieveUserList(A.trim(),D,z)
}else{C.callback({results:d.tempNames})
}}}}}}}};
var x='<div class="row">&nbsp;</div><form role="form" name="messaging" ng-init="clearMsgForm()"  class="form-horizontal" novalidate> <div class="row" ><div class="col-md-12" ><div class="form-group" ><span class="col-xs-12" ><label for="messagearea">{{i18EntityMessagingPopup + \'Message\'| translate }}</label></span><div class="col-xs-12" ><div ng-class="{\'has-error\': (messaging.message.$dirty || message.submitted) && messaging.message.$invalid}"><textarea name="message" id="messagearea" rows="3" class="form-control" ng-trim="false" maxlength="500" required ng-model="message.messageBody" placeholder="Message here..."></textarea><div class="error,help-block" ng-show="(messaging.message.$dirty || message.submitted) && messaging.message.$invalid"><span class="help-block" ng-show="messaging.message.$error.required">{{ i18EntityMessagingPopup + \'Enter message\' | translate }}</span></div></div><div id="messagecounter" class="pull-right center">{{500 - message.messageBody.length}}&nbsp;  {{i18EntityMessagingPopup + \'characters left\'| translate }}</div></div></div><div class="form-group" ><span class="col-xs-12" ><label for="recipients">  {{i18EntityMessagingPopup + \'Send to\'| translate }}</label></span><div class="col-xs-12"><div ng-class="{\'has-error\': !recipientValid && message.submitted}"><div class="input-group"><input type="text" class="col-xs-12 hkg-nopadding form-control" id="recipients" value="blank" required ui-select2="autoCompleteRecipient" ng-model="message.nameRecipient"/><span class="input-group-addon"><span class="glyphicon glyphicon-info-sign" tooltip-html-unsafe="{{popover}}"  tooltip-trigger="mouseenter" tooltip-placement="right"></span></span></div><div class="error,help-block" ng-show="(!recipientValid && message.submitted)"><span class="help-block" ng-show="!recipientValid">{{i18EntityMessagingPopup + \'Select recipients\'| translate }}</span></div></div></div></div><div><dynamic-form input-css ="col-xs-12" label-css="col-xs-12" db-map="dbType" internationalization-label="{{i18DynamicForm}}" form-name="form2" template="generalMessageTemplate" ng-model="addMessageData" ng-if="generalMessageTemplate" edit-flag="false" is-diamond="false" no-of-field-per-row = "1"></dynamic-form></div><div class="clearfix"></div><div class="form-group" ><div class="col-xs-12" ><div class="row"><hr/><div class="col-xs-12"><div class="col-xs-10"><span class="hkg-input-img glyphicon glyphicon-star-empty" tooltip-html-unsafe="{{prioritytooltip}}"  tooltip-trigger="mouseenter" tooltip-placement="bottom" ng-show="!message.hasPriority" ng-click="setpriority();"></span><span class="hkg-input-img glyphicon glyphicon-star" tooltip-html-unsafe="{{prioritytooltip}}"  tooltip-trigger="mouseenter" tooltip-placement="bottom" ng-show="message.hasPriority" ng-click="setpriority();"></span></div><button class="btn btn-hkg pull-right" ng-click="submitMsg(messaging)">{{i18EntityMessagingPopup + \'Send\'| translate }}</button></div></div></div></div></div></div></form>';
$(".popover-content").html(s(x)(c))
};
c.popover="<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td> \n '@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr> <tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr> <tr><td>'@R'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Designation</td></tr> </table> ";
d.analyticsLoginUrl="http://192.1.200.51:8080/pentaho/j_spring_security_check";
d.analyticsLogoutUrl="http://192.1.200.51:8080/pentaho/Logout";
d.analyticsPingUrl="http://192.1.200.51:8080/pentaho/Home"
}];