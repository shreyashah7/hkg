define(["hkg","systemConfigSerivce"],function(a,b){a.register.controller("SystemConfiguration",["$rootScope","$scope","SysConfig",function(c,d,e){c.maskLoading();
d.retreiveAll=function(){var h=[];
var f=function(i){angular.forEach(i,function(j){h.push(j)
})
};
var g=function(){var j="Fail to load System Configurations";
var i=c.failure;
c.addMessage(j,i)
};
e.retreiveAll(f,g);
return h
};
d.allSysConfigs=d.retreiveAll();
d.saveSysConfig=function(){var f=function(){var i="Created";
var h=c.success;
c.addMessage(i,h);
d.allSysConfigs=d.retreiveAll();
d.key=null;
d.value=null
};
var g=function(){var i="Fail to create System Configuration";
var h=c.failure;
c.addMessage(i,h)
};
e.createSys({skey:d.key,svalue:d.value},f,g)
};
c.unMaskLoading()
}])
});