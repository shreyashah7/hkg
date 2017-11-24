define(["hkg","leavemanagement/leave-workflow.controller","franchiseConfigService","shift/shift.controller","fileUploadService"],function(a){a.register.controller("ConfigureFranchise",["$rootScope","$scope","FranchiseConfigService","FileUploadService","$filter",function(b,d,c,e,f){b.maskLoading();
b.mainMenu="";
b.childMenu="";
b.activateMenu();
d.entity="CONFIGFRANCHISE.";
d.isHkadmin=b.session.isHKAdmin;
d.configKeyNames={msgsPerUser:"MESSAGING_MAX_MSG_PER_USER",mstAuthPwd:"MASTERS_AUTH_PWD",shiftReminderPeriod:"SHIFT_REMINDER_PERIOD",taskStatusUpdate:"TASK_STATUS_ON_LOGIN",taskArchivePeriod:"TASK_ARCHIVE_PERIOD",empSendMailTo:"EMP_SEND_MAIL_TO",empMinAge:"EMP_MIN_AGE",empMaxAge:"EMP_MAX_AGE",empDefaultImage:"EMP_DEFAULT_IMAGE",leaveMaxYearlyLeaves:"LEAVE_MAX_YEARLY_LEAVES",leaveBeyondLimit:"ALLOW_LEAVE_BEYOND_LIMIT",idConfiguration:"ID_CONFIGURATION",currentVisibilityStatus:"CURR_VISIBILITY_STATUS",noOfSnapids:"NO_OF_SNAP_IDS",email:"DEFAULT_XMPP_EMAIL_ADDRESS",noOfDiamondInQueue:"NO_OF_DIAMOND_ALLOWED_QUEUE",carrierBoyDesignation:"CARRIER_BOY_DESIGNATION",analyticEngineUsername:"ANALYTICS_ENGINE_USERNAME",analyticEnginePwd:"ANALYTICS_ENGINE_PWD",analyticServerUrl:"ANALYTICS_SERVER_URL",allowCarateVariation:"ALLOW_CARATE_VARIATION",};
d.resetConfigurationForm=function(g){d.submitted=false;
g.$setPristine()
};
d.retrieveAllConfiguration=function(){b.maskLoading();
d.responseComplete=false;
c.retrieveAllConfiguration(function(g){d.responseComplete=true;
b.unMaskLoading();
d.franchiseConfig=g;
if(d.franchiseConfig[d.configKeyNames.mstAuthPwd]!==undefined){d.configExist=true;
d.franchiseConfig[d.configKeyNames.mstAuthPwd]=undefined
}if(d.franchiseConfig[d.configKeyNames.analyticEngineUsername]!==undefined&&d.franchiseConfig[d.configKeyNames.analyticEnginePwd]!==undefined){d.analytics.analyticConfig=true;
d.analytics.resetAnalyticConfig=false;
d.franchiseConfig[d.configKeyNames.analyticEnginePwd]=undefined
}else{d.analytics.isAnalytics=false;
d.analytics.analyticConfig=false;
d.franchiseConfig[d.configKeyNames.analyticEngineUsername]=undefined;
d.franchiseConfig[d.configKeyNames.analyticEnginePwd]=undefined
}d.setDefaultValues();
d.configFranchiseForm.$dirty=false
},function(){d.responseComplete=true;
b.addMessage("Could not retrieve configurations, please try again.",1);
b.unMaskLoading()
})
};
d.retrieveEmployeeTypes=function(){b.maskLoading();
c.retrieveEmployeeTypes(function(g){b.unMaskLoading();
angular.forEach(g,function(i,h){d.employeeTypes.push({key:"EMP_TYPE_"+i.value,type:i.label})
});
d.retrieveAllConfiguration()
},function(){b.addMessage("Could not retrieve employee types, please try again.",1);
d.retrieveAllConfiguration();
b.unMaskLoading()
})
};
d.setDefaultValues=function(){if(d.franchiseConfig[d.configKeyNames.msgsPerUser]===undefined){d.franchiseConfig[d.configKeyNames.msgsPerUser]="30"
}if(d.franchiseConfig[d.configKeyNames.empSendMailTo]===undefined){d.franchiseConfig[d.configKeyNames.empSendMailTo]="work"
}if(d.franchiseConfig[d.configKeyNames.taskStatusUpdate]===undefined){d.franchiseConfig[d.configKeyNames.taskStatusUpdate]="false"
}if(d.franchiseConfig[d.configKeyNames.leaveBeyondLimit]===undefined){d.franchiseConfig[d.configKeyNames.leaveBeyondLimit]="false"
}if(d.franchiseConfig[d.configKeyNames.empMinAge]===undefined){d.franchiseConfig[d.configKeyNames.empMinAge]="18"
}if(d.franchiseConfig[d.configKeyNames.empMaxAge]===undefined){d.franchiseConfig[d.configKeyNames.empMaxAge]="55"
}if(d.franchiseConfig[d.configKeyNames.currentVisibilityStatus]===undefined){d.franchiseConfig[d.configKeyNames.currentVisibilityStatus]="on"
}};
d.initConfigureFranchise=function(g){b.configureDefaultShift=false;
b.configureWorkFlow=false;
d.configFranchiseForm=g;
d.franchiseConfig={};
d.pwd={};
d.resetPwd=undefined;
d.analytics={};
d.employeeTypes=[];
d.resetConfigurationForm(g);
d.retrieveEmployeeTypes()
};
d.displayAvatar=function(g){var h=d.franchiseConfig.EMP_DEFAULT_IMAGE;
if(angular.isDefined(h)){if(!g){return b.appendAuthToken(b.apipath+"franchiseconfig/getimage?file_name="+h)
}else{d.avtarImagePopup(h)
}}};
d.avtarImagePopup=function(g){d.selectedAvtarImageName=g;
$("#avtarImagePopup").modal("show")
};
d.avtarFileRemove=function(){c.removeFileFromTemp(d.franchiseConfig.EMP_DEFAULT_IMAGE);
d.franchiseConfig.EMP_DEFAULT_IMAGE=undefined
};
d.confirmPasswordValidation=function(g){if(!angular.equals(d.franchiseConfig[d.configKeyNames.mstAuthPwd],d.pwd.confirmPassword)){g.confPwd.$setValidity("pwdMismatch",false)
}else{g.confPwd.$setValidity("pwdMismatch",true)
}};
d.checkMaxLimit=function(h,i,g){if(parseInt(i)>100){h.$setValidity(g,false)
}else{h.$setValidity(g,true)
}};
d.checkAgeLimitValidation=function(i,h,g){if(parseInt(d.franchiseConfig[d.configKeyNames.empMinAge])>parseInt(d.franchiseConfig[d.configKeyNames.empMaxAge])){h.$setValidity(g,false)
}else{i.maxAge.$setValidity("maxAgeLimit",true);
i.minAge.$setValidity("minAgeLimit",true)
}};
d.setConfigureWorkFlow=function(g){b.configureWorkFlow=g
};
d.setConfigureDefaultShift=function(g){b.configureDefaultShift=g
};
d.resetPassword=function(){d.franchiseConfig[d.configKeyNames.mstAuthPwd]="";
d.resetPwd=true
};
d.cancelResetPassword=function(){d.franchiseConfig[d.configKeyNames.mstAuthPwd]=undefined;
d.resetPwd=false
};
d.checkOldPassword=function(g){c.checkOldPassword(d.pwd.oldPassword,function(h){if(h.messages===null){g.oldPwd.$setValidity("invalid",true)
}else{d.oldPwdMsg=h.messages[0].message;
g.oldPwd.$setValidity("invalid",false)
}},function(){})
};
d.saveConfiguration=function(g){d.submitted=true;
if(g.$valid){b.maskLoading();
if(d.analytics.isAnalytics===false||d.analytics.resetAnalyticConfig===false){d.franchiseConfig[d.configKeyNames.analyticEngineUsername]=undefined;
d.franchiseConfig[d.configKeyNames.analyticEnginePwd]=undefined;
d.franchiseConfig[d.configKeyNames.analyticServerUrl]=undefined
}c.saveConfiguration(d.franchiseConfig,function(h){b.haveValue=angular.copy(d.franchiseConfig[d.configKeyNames.currentVisibilityStatus]);
b.randomCount=Math.random();
b.minAge=d.franchiseConfig[d.configKeyNames.empMinAge];
d.resetAll=false;
b.unMaskLoading();
d.initConfigureFranchise(g);
d.scrollTo("pageTop");
b.addMessage("Configuration saved succesfully.",0)
},function(){d.scrollTo("pageTop");
b.unMaskLoading();
b.addMessage("Could not save details, please try again.",1)
})
}};
d.avtarImg={};
d.uploadFile={target:b.apipath+"fileUpload/uploadFile",singleFile:true,testChunks:true,query:{fileType:d.seletedFileType,model:"Franchise"}};
d.avtarFileUploaded=function(j,h,o,l){var n="Franchise";
var k=[j.name];
var m;
var g="false";
var i;
c.uploadAvtar(k,function(p){m=p.res;
console.log("filenameformat"+m);
i=[j.name,n,m,g];
e.uploadFiles(i,function(q){d.franchiseConfig[d.configKeyNames.empDefaultImage]=q.res;
d.avtarImg.fileName=q.res
})
})
};
d.avtarFileAdded=function(h,g){d.avtarFlow=g;
if((h.getExtension()!="jpg")&&(h.getExtension()!="jpeg")&&(h.getExtension()!="png")&&(h.getExtension()!="gif")){d.avtarImg.invalidFileFlag=true;
d.avtarImg.fileName=h.name
}else{if(h.size>5242880){d.avtarImg.invalidFileSizeFlag=true;
return false
}}return !!{jpg:1,jpeg:1,gif:1,png:1}[h.getExtension()]
};
d.heightInit=function(){$("#sidemenu").height($("#mainPanel").height())
};
d.scrollTo=function(q){d.setConfigureWorkFlow(false);
d.setConfigureDefaultShift(false);
var o=m();
var g=r(q);
var h=g>o?g-o:o-g;
if(h<100){scrollTo(0,g-60);
return
}var l=Math.round(h/100);
if(l>=20){l=20
}var k=Math.round(h/25);
var p=g>o?o+k:o-k;
var j=0;
if(g>o){for(var n=o;
n<g;
n+=k){setTimeout("window.scrollTo(0, "+(p-60)+")",j*l);
p+=k;
if(p>g){p=g
}j++
}return
}for(var n=o;
n>g;
n-=k){setTimeout("window.scrollTo(0, "+(p-60)+")",j*l);
p-=k;
if(p<g){p=g
}j++
}function m(){if(self.pageYOffset){return self.pageYOffset
}if(document.documentElement&&document.documentElement.scrollTop){return document.documentElement.scrollTop
}if(document.body.scrollTop){return document.body.scrollTop
}return 0
}function r(i){var u=document.getElementById(i);
var t=u.offsetTop;
var s=u;
while(s.offsetParent&&s.offsetParent!=document.body){s=s.offsetParent;
t+=s.offsetTop
}return t
}};
$(function(){$(window).bind("mousewheel",function(g,h){if(!b.configureWorkFlow&&!b.configureDefaultShift){$("#sidemenu").height($("#mainPanel").height())
}});
$(window).bind("scroll",function(g){if(!b.configureWorkFlow&&!b.configureDefaultShift){$("#sidemenu").height($("#mainPanel").height())
}})
});
d.onlyCharacters=function(i,g){var h=/^[a-zA-Z]+$/;
if(!h.test(i)){d.entities[g].prefixCode=d.entities[g].prefixCode.substring(0,d.entities[g].prefixCode.length-1)
}else{d.entities[g].prefixCode=d.entities[g].prefixCode.toUpperCase()
}};
d.onlyZeros=function(i,g){if(i.length<4){d.entities[g].pattern="0000"
}var h=/^[0]+$/;
if(!h.test(i)){d.entities[g].pattern=d.entities[g].pattern.substring(0,d.entities[g].pattern.length-1)
}};
d.confirmeReset=function(g){if(g){$("#confirmResetPopUp").modal("show")
}else{angular.forEach(d.entities,function(h){h.isReset=false
});
d.resetAll=false
}};
d.closeResetPopUp=function(){d.resetAll=false;
$("#confirmResetPopUp").modal("hide");
b.removeModalOpenCssAfterModalHide()
};
d.continueWithThis=function(){angular.forEach(d.entities,function(g){g.isReset=true
});
$("#confirmResetPopUp").modal("hide");
b.removeModalOpenCssAfterModalHide()
};
b.unMaskLoading()
}]);
a.register.filter("numberFixedLen",function(){return function(d,b){var c=parseInt(d,10);
b=parseInt(b,10);
if(isNaN(c)||isNaN(b)){return d
}c=""+c;
while(c.length<b){c="0"+c
}return c
}
})
});