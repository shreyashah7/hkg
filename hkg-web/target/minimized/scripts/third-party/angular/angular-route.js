(function(d,c,g){var f=c.module("ngRoute",["ng"]).provider("$route",h);
function h(){function k(m,l){return c.extend(new (c.extend(function(){},{prototype:m}))(),l)
}var i={};
this.when=function(n,m){i[n]=c.extend({reloadOnSearch:true},m,n&&j(n,m));
if(n){var l=(n[n.length-1]=="/")?n.substr(0,n.length-1):n+"/";
i[l]=c.extend({redirectTo:n},j(l,m))
}return this
};
function j(p,o){var l=o.caseInsensitiveMatch,m={originalPath:p,regexp:p},n=m.keys=[];
p=p.replace(/([().])/g,"\\$1").replace(/(\/)?:(\w+)([\?\*])?/g,function(r,t,s,u){var q=u==="?"?u:null;
var v=u==="*"?u:null;
n.push({name:s,optional:!!q});
t=t||"";
return""+(q?"":t)+"(?:"+(q?t:"")+(v&&"(.+?)"||"([^/]+)")+(q||"")+")"+(q||"")
}).replace(/([\/$\*])/g,"\\$1");
m.regexp=new RegExp("^"+p+"$",l?"i":"");
return m
}this.otherwise=function(l){this.when(null,l);
return this
};
this.$get=["$rootScope","$location","$routeParams","$q","$injector","$http","$templateCache","$sce",function(w,p,x,q,y,t,l,v){var n=false,u={routes:i,reload:function(){n=true;
w.$evalAsync(s)
}};
w.$on("$locationChangeSuccess",s);
return u;
function m(E,F){var H=F.keys,B={};
if(!F.regexp){return null
}var A=F.regexp.exec(E);
if(!A){return null
}for(var C=1,D=A.length;
C<D;
++C){var G=H[C-1];
var z="string"==typeof A[C]?decodeURIComponent(A[C]):A[C];
if(G&&z){B[G.name]=z
}}return B
}function s(){var z=o(),A=u.current;
if(z&&A&&z.$$route===A.$$route&&c.equals(z.pathParams,A.pathParams)&&!z.reloadOnSearch&&!n){A.params=z.params;
c.copy(A.params,x);
w.$broadcast("$routeUpdate",A)
}else{if(z||A){n=false;
w.$broadcast("$routeChangeStart",z,A);
u.current=z;
if(z){if(z.redirectTo){if(c.isString(z.redirectTo)){p.path(r(z.redirectTo,z.params)).search(z.params).replace()
}else{p.url(z.redirectTo(z.pathParams,p.path(),p.search())).replace()
}}}q.when(z).then(function(){if(z){var D=c.extend({},z.resolve),B,C;
c.forEach(D,function(F,E){D[E]=c.isString(F)?y.get(F):y.invoke(F)
});
if(c.isDefined(B=z.template)){if(c.isFunction(B)){B=B(z.params)
}}else{if(c.isDefined(C=z.templateUrl)){if(c.isFunction(C)){C=C(z.params)
}C=v.getTrustedResourceUrl(C);
if(c.isDefined(C)){z.loadedTemplateUrl=C;
B=t.get(C,{cache:l}).then(function(E){return E.data
})
}}}if(c.isDefined(B)){D["$template"]=B
}return q.all(D)
}}).then(function(B){if(z==u.current){if(z){z.locals=B;
c.copy(z.params,x)
}w.$broadcast("$routeChangeSuccess",z,A)
}},function(B){if(z==u.current){w.$broadcast("$routeChangeError",z,A,B)
}})
}}}function o(){var A,z;
c.forEach(i,function(B,C){if(!z&&(A=m(p.path(),B))){z=k(B,{params:c.extend({},p.search(),A),pathParams:A});
z.$$route=B
}});
return z||i[null]&&k(i[null],{params:{},pathParams:{}})
}function r(A,B){var z=[];
c.forEach((A||"").split(":"),function(E,D){if(D===0){z.push(E)
}else{var F=E.match(/(\w+)(.*)/);
var C=F[1];
z.push(B[C]);
z.push(F[2]||"");
delete B[C]
}});
return z.join("")
}}]
}f.provider("$routeParams",e);
function e(){this.$get=function(){return{}
}
}f.directive("ngView",b);
f.directive("ngView",a);
b.$inject=["$route","$anchorScroll","$animate"];
function b(k,i,j){return{restrict:"ECA",terminal:true,priority:400,transclude:"element",link:function(v,u,q,l,r){var w,o,m,p=q.autoscroll,t=q.onload||"";
v.$on("$routeChangeSuccess",n);
n();
function s(){if(m){m.remove();
m=null
}if(w){w.$destroy();
w=null
}if(o){j.leave(o,function(){m=null
});
m=o;
o=null
}}function n(){var A=k.current&&k.current.locals,x=A&&A.$template;
if(c.isDefined(x)){var y=v.$new();
var z=k.current;
var B=r(y,function(D){j.enter(D,null,o||u,function C(){if(c.isDefined(p)&&(!p||v.$eval(p))){i()
}});
s()
});
o=B;
w=z.scope=y;
w.$emit("$viewContentLoaded");
w.$eval(t)
}else{s()
}}}}
}a.$inject=["$compile","$controller","$route"];
function a(j,i,k){return{restrict:"ECA",priority:-400,link:function(n,m){var q=k.current,p=q.locals;
m.html(p.$template);
var o=j(m.contents());
if(q.controller){p.$scope=n;
var l=i(q.controller,p);
if(q.controllerAs){n[q.controllerAs]=l
}m.data("$ngControllerController",l);
m.children().data("$ngControllerController",l)
}o(n)
}}
}})(window,window.angular);