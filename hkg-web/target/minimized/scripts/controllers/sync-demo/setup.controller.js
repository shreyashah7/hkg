define(["hkg"],function(a){a.register.controller("SetUpController",["$rootScope","$scope","$http","$location","$controller",function(c,d,f,e,b){c.maskLoading();
d.doLogin=function(g){d.submitted=true;
if(g.$valid){console.log("setup");
var h={j_username:d.username,j_password:d.password};
c.maskLoading();
f.post(c.centerapipath+"sync/deployserver",h).success(function(i){localStorage.setItem("initialSetupData",JSON.stringify(i.data));
c.isFirstReq=false;
var j=d.$new();
b("Login",{$scope:j});
j.doLogin(g);
c.unMaskLoading();
e.path("/dashboard")
}).error(function(){c.unMaskLoading()
})
}};
c.unMaskLoading()
}])
});