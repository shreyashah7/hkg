define(["hkg","reportBuilderService"],function(a){a.register.controller("DashboardController",["$rootScope","$scope","$http","ReportBuilderService","$sce","$q",function(b,e,g,f,c,d){b.maskLoading();
b.mainMenu="dashboard";
b.childMenu="dashboardLink";
b.activateMenu();
b.unMaskLoading();
e.showWarning=false;
e.initialData=JSON.parse(localStorage.getItem("initialSetupData"));
e.$on("$viewContentLoaded",function(){if(e.initialData&&e.initialData.status!==100){e.showWarning=true
}});
e.initSetup=function(){var h=JSON.parse(localStorage.getItem("tmpUser"));
var i={j_username:h.username,j_password:b.encryptPass(h.password,false)};
g.post(b.centerapipath+"sync/deployserver",JSON.stringify(i)).success(function(j){if(!j.data.status){}else{if(j.data.status===100){e.showWarning=false;
localStorage.setItem("initialSetupData",JSON.stringify(j.data))
}else{localStorage.setItem("initialSetupData",JSON.stringify(j.data))
}}b.isFirstReq=false
})
};
e.user={};
e.user.username="prabhat";
e.user.password="testing123";
e.testLogin=function(k,h){var j="<title>Pentaho Business Analytics</title>";
var i="<title>Pentaho User Console</title>";
if(k.toString().indexOf(j)>=0||k.toString().indexOf(i)>=0){h.resolve(k)
}else{e.invalidCredential=true;
h.resolve(k)
}};
e.login=function(h){var j=$.param({j_username:e.user.username,j_password:e.user.password,_spring_security_remember_me:true});
var i={headers:{"Content-Type":"application/x-www-form-urlencoded; charset=UTF-8"}};
g.post(b.analyticsLoginUrl,j,i).success(function(m,k,n,l){e.testLogin(m,h)
}).error(function(m,k,n,l){b.addMessage("Unable to connect analytics server",b.failure);
h.resolve(m)
})
};
e.testPingResult=function(k,h){var j="<title>Pentaho Business Analytics</title>";
var i="<title>Pentaho User Console</title>";
if(k.toString().indexOf(j)>=0||k.toString().indexOf(i)>=0){h.resolve(k)
}else{e.login(h)
}};
e.pingAnalyticServer=function(){var h=d.defer();
g.get(b.analyticsPingUrl).success(function(k,i,l,j){e.testPingResult(k,h)
}).error(function(k,i,l,j){b.addMessage("Unable to connect analytics server",b.failure);
h.resolve(k)
});
return h.promise
};
e.analyticsLogout=function(){f.retrieveAnalyticsCrendentials(function(h){if(h.data!==undefined&&h.data!==null&&h.data.hasOwnProperty("ANALYTICS_ENGINE_USERNAME")){if(h.data.hasOwnProperty("ANALYTICS_SERVER_URL")){b.analyticsLoginUrl=h.data.ANALYTICS_SERVER_URL+"/j_spring_security_check";
b.analyticsLogoutUrl=h.data.ANALYTICS_SERVER_URL+"/Logout";
b.analyticsPingUrl=h.data.ANALYTICS_SERVER_URL+"/Home"
}if(h.data.hasOwnProperty("ANALYTICS_ENGINE_USERNAME")){e.user.username=h.data.ANALYTICS_ENGINE_USERNAME
}if(h.data.hasOwnProperty("ANALYTICS_ENGINE_PWD")){e.user.password=h.data.ANALYTICS_ENGINE_PWD
}var j=$.param({});
var i={headers:{"Content-Type":"application/x-www-form-urlencoded; charset=UTF-8"}};
g.post(b.analyticsLogoutUrl,j,i).success(function(m,k,n,l){e.initializeReports()
}).error(function(m,k,n,l){})
}else{}},function(h){})
};
e.initializeReports=function(){f.retrieveDashboardReports(function(h){if(h.data!==undefined&&h.data!==null){e.reports=h.data;
if(e.reports.length>0){f.retrieveAnalyticsCrendentials(function(j){if(j.data!==undefined&&j.data!==null&&j.data.hasOwnProperty("ANALYTICS_ENGINE_USERNAME")){e.user.username=j.data.ANALYTICS_ENGINE_USERNAME;
e.user.password=j.data.ANALYTICS_ENGINE_PWD;
var i=e.pingAnalyticServer();
i.then(function(k){e.currentReport=e.reports[0];
e.iframeUrl=c.trustAsResourceUrl(e.currentReport.query);
e.currentIndex=0
})
}else{b.addMessage("Analytics credentials are not set, contact administrator",b.failure);
e.reports=[]
}},function(i){})
}}},function(h){alert("failure")
})
};
e.updateCurrentReport=function(i){if(i==="next"){e.currentIndex++
}else{e.currentIndex--
}var h=e.pingAnalyticServer();
h.then(function(j){e.currentReport=e.reports[e.currentIndex];
e.iframeUrl=c.trustAsResourceUrl(e.currentReport.query)
})
}
}])
});