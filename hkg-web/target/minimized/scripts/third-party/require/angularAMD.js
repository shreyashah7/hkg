/*
 angularAMD v0.2.1
 (c) 2013-2014 Marcos Lin https://github.com/marcoslin/
 License: MIT
*/
define(function(){var a=false,m,b,f,l,q,r={},o,h,n={},c=[],j={},g={},d=[];
function e(){if(!a){throw new Error("angularAMD not initialized.  Need to call angularAMD.bootstrap(app) first.")
}}function k(){if(h){throw new Error("setAlternateAngular can only be called once.")
}else{h={}
}e();
o.extend(h,o);
h.module=function(s,t){if(typeof t==="undefined"){if(g.hasOwnProperty(s)){return j[s]
}else{return o.module(s)
}}else{var v=o.module.apply(null,arguments),u={name:s,module:v};
d.push(u);
o.extend(v,n);
g[s]=true;
j[s]=v;
return v
}};
window.angular=h
}function i(){}i.prototype.route=function(s){var t;
if(s.hasOwnProperty("controllerUrl")){t=s.controllerUrl;
delete s.controllerUrl;
if(typeof s.controller==="undefined"){s.controller=["$scope","__AAMDCtrl","$injector",function(v,w,x){if(typeof w!=="undefined"){x.invoke(w,this,{"$scope":v})
}}]
}}else{if(typeof s.controller==="string"){t=s.controller
}}if(t){var u=s.resolve||{};
u.__AAMDCtrl=["$q","$rootScope",function(w,v){var x=w.defer();
require([t],function(y){x.resolve(y);
v.$apply()
});
return x.promise
}];
s.resolve=u
}return s
};
i.prototype.appname=function(){e();
return m
};
i.prototype.processQueue=function(){e();
if(typeof h==="undefined"){throw new Error("Alternate angular not set.  Make sure that `enable_ngload` option has been set when calling angularAMD.bootstrap")
}function z(y){l.invoke(y)
}while(d.length){var E=d.shift(),x=E.module._invokeQueue,C;
for(C=0;
C<x.length;
C+=1){var u=x[C],D=u[0],t=u[1],B=u[2];
if(r.hasOwnProperty(D)){var s;
if(D==="$injector"&&t==="invoke"){s=q
}else{s=r[D]
}s[t].apply(null,B)
}else{if(window.console){window.console.error('"'+D+'" not found!!!')
}}}if(E.module._configBlocks){var F=E.module._configBlocks;
for(C=0;
C<F.length;
C+=1){var v=F[C],w=v[1],A=v[2];
q[w].apply(null,A)
}}if(E.module._runBlocks){angular.forEach(E.module._runBlocks,z)
}j={}
}};
i.prototype.getCachedProvider=function(s){e();
var t;
switch(s){case"__orig_angular":t=o;
break;
case"__alt_angular":t=h;
break;
case"__orig_app":t=b;
break;
case"__alt_app":t=f;
break;
default:t=r[s]
}return t
};
i.prototype.inject=function(){e();
return l.invoke.apply(null,arguments)
};
i.prototype.config=function(){e();
return q.invoke.apply(null,arguments)
};
i.prototype.reset=function(){if(typeof o==="undefined"){return
}window.angular=o;
b=undefined;
f=undefined;
h=undefined;
o=undefined;
n={};
c=[];
d=[];
m=undefined;
l=undefined;
q=undefined;
r={};
a=false
};
i.prototype.bootstrap=function(w,v,t){if(a){throw Error("bootstrap can only be called once.")
}if(typeof v==="undefined"){v=true
}o=angular;
b=w;
f={};
o.extend(f,b);
t=t||document.documentElement;
w.config(["$controllerProvider","$compileProvider","$filterProvider","$animateProvider","$provide","$injector",function(B,x,A,y,z,C){q=C;
r={$controllerProvider:B,$compileProvider:x,$filterProvider:A,$animateProvider:y,$provide:z};
angular.extend(n,{provider:function(D,E){z.provider(D,E);
return this
},controller:function(D,E){B.register(D,E);
return this
},directive:function(D,E){x.directive(D,E);
return this
},filter:function(D,E){A.register(D,E);
return this
},factory:function(D,E){z.factory(D,E);
return this
},service:function(D,E){z.service(D,E);
return this
},constant:function(D,E){z.constant(D,E);
return this
},value:function(D,E){z.value(D,E);
return this
},animation:angular.bind(y,y.register)});
angular.extend(f,n)
}]);
w.run(["$injector",function(x){l=x;
r.$injector=l
}]);
m=w.name;
if(c.length>0){for(var u=0;
u<c.length;
u+=1){var s=c[u];
b[s.recipe](s.name,s.constructor)
}c=[]
}b.register=n;
o.element(document).ready(function(){o.bootstrap(t,[m]);
a=true;
if(v){k()
}});
return f
};
function p(s){return function(t,u){if(a){n[s](t,u)
}else{c.push({recipe:s,name:t,constructor:u})
}return this
}
}i.prototype.provider=p("provider");
i.prototype.controller=p("controller");
i.prototype.directive=p("directive");
i.prototype.filter=p("filter");
i.prototype.factory=p("factory");
i.prototype.service=p("service");
i.prototype.constant=p("constant");
i.prototype.value=p("value");
i.prototype.animation=p("animation");
return new i()
});