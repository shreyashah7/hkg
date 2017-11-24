define(["hkg"],function(a){a.register.controller("LogoutController",["$rootScope","$scope","$location","$http",function(b,c,f,e){b.maskLoading();
b.mainMenu="";
b.childMenu="";
b.activateMenu();
b.isLoggedIn=false;
b.menu=undefined;
b.session=undefined;
var d={headers:{"Content-Type":"application/x-www-form-urlencoded; charset=UTF-8","X-Auth-Token":b.authToken}};
e.post("j_spring_security_logout",{},d).success(function(){delete b.authToken;
delete b.userProxy;
localStorage.removeItem("user");
localStorage.removeItem("masterAuthToken");
localStorage.removeItem("tmpUser");
localStorage.removeItem("proxyLogin");
if(!b.isMaster){var g=b.apipath+"common/adduseroperationbeforelogout";
e.get(g).success(function(){console.log("run : master logout request event");
delete b.masterAuthToken;
e.post(b.masterHkgPath+"j_spring_security_logout",{}).success(function(){f.path("login")
}).error(function(){console.log("failed master logout");
f.path("login")
})
}).error(function(){console.log("failed master beforelogout");
delete b.masterAuthToken;
f.path("login")
})
}else{f.path("login")
}});
b.unMaskLoading()
}])
});