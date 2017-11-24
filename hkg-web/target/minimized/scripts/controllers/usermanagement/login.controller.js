define(["hkg","ruleExecutionService"],function(a){a.register.controller("Login",["$rootScope","$scope","$http","$location","$window","$q","RuleExecutionService",function(j,k,h,e,b,f,i){j.maskLoading();
k.company="";
k.usernamePlaceholder="Enter Username";
k.actionbutton="Login";
var c=false;
k.submitted=false;
k.EMAIL_REGEXP=/^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$/;
k.$on("$viewContentLoaded",function(){localStorage.removeItem("user");
localStorage.removeItem("userDetail");
localStorage.removeItem("tmpUser");
var l=f.defer();
var n=retrieveCenterFranchise(h,e,l);
n.then(function(){k.usernamePlaceholder="Franchise Admin Username";
k.actionbutton="Setup";
c=true;
console.log("Is setup required: "+c)
},function(){c=false;
console.log("Is setup required: "+c)
});
var m=localStorage.getItem("userDetail");
m=JSON.parse(m);
if(m!==null){k.rememberme=true;
k.username=m.username;
k.password=j.encryptPass(m.password,false)
}});
function d(m){var l=f.defer();
j.maskLoading();
h.post(j.centerapipath+"sync/deployserver",m).success(function(n){if(n){localStorage.setItem("initialSetupData",JSON.stringify(n.data))
}j.isFirstReq=false;
l.resolve();
j.unMaskLoading()
}).error(function(n){l.reject();
if(n.messages&&n.messages[0]){j.addMessage(n.messages[0].message,n.messages[0].responseCode);
j.setMessage(n.messages[0].message,n.messages[0].responseCode,true)
}j.unMaskLoading()
});
return l.promise
}k.doLogin=function(l){k.submitted=true;
if(l.$valid){console.log("loginnnn");
console.log("-------------"+j.isMaster);
console.log("run : login request event");
var m={headers:{"Content-Type":"application/x-www-form-urlencoded; charset=UTF-8"}};
var n={userName:k.username,password:k.password};
if(c){console.log("here");
var o=d(n);
o.then(function(){g(n)
},function(){})
}else{g(n)
}}};
function g(m){var l={username:k.username,password:j.encryptPass(k.password,true),rememberme:k.rememberme};
var n={featureName:"login",entityId:null,entityType:"login",currentFieldValueMap:{username:k.username},dbType:null,otherEntitysIdMap:null};
if(!j.isMaster){i.executePostRule(n,function(o){if(!!o.validationMessage){j.setMessage(o.validationMessage,"warning",true,false);
j.unMaskLoading()
}else{h.post(j.centerapipath+"common/authenticate",JSON.stringify(m)).success(function(p){j.authToken=p.token;
localStorage.setItem("user",p.token);
if(k.rememberme){p.password=j.encryptPass(k.password,true);
localStorage.setItem("userDetail",JSON.stringify(p))
}else{localStorage.removeItem("userDetail")
}localStorage.setItem("tmpUser",JSON.stringify(l));
var q=j.loginToMaster(l);
q.then(function(){j.pingServer();
j.retrieveNotificationCount()
},function(){j.pingServer();
j.retrieveNotificationCount()
})
})
}console.log("res "+JSON.stringify(o))
},function(o){j.unMaskLoading();
var q="Failed to authenticate post rule.";
var p=j.error;
j.addMessage(q,p)
})
}else{h.post(j.centerapipath+"common/authenticate",JSON.stringify(m)).success(function(o){j.authToken=o.token;
localStorage.setItem("user",o.token);
if(k.rememberme){o.password=j.encryptPass(k.password,true);
localStorage.setItem("userDetail",JSON.stringify(o))
}else{localStorage.removeItem("userDetail")
}localStorage.setItem("tmpUser",JSON.stringify(l));
var p=j.loginToMaster(l);
p.then(function(){j.pingServer();
j.retrieveNotificationCount()
},function(){j.pingServer();
j.retrieveNotificationCount()
})
})
}}k.showLogin=function(){$("#login").modal("show")
};
j.unMaskLoading()
}])
});