/*
 * angular-translate - v2.2.0 - 2014-06-03
 * http://github.com/PascalPrecht/angular-translate
 * Copyright (c) 2014 ; Licensed MIT
 */
angular.module("hkg.directives").factory("I18nDirectiveService",["$resource",function(c){var a=corsConfig.masterHkgPath+"api/";
var b=c(a+"locale/:action",{},{createLabel:{method:"POST",params:{action:"createlabel"}}});
return b
}]);
angular.module("pascalprecht.translate",["ng"]).run(["$translate",function(b){var a=b.storageKey(),c=b.storage();
if(c){if(!c.get(a)){if(angular.isString(b.preferredLanguage())){b.use(b.preferredLanguage())
}else{c.set(a,b.use())
}}else{b.use(c.get(a))
}}else{if(angular.isString(b.preferredLanguage())){b.use(b.preferredLanguage())
}}}]);
angular.module("pascalprecht.translate").provider("$translate",["$STORAGE_KEY",function(e){var j={},y,m=[],r,s,p,f,B,k,h=e,a,n,u,o=[],x=false,z,i="translate-cloak",w,q,l,A=false,d=".";
var v=function(){var D=window.navigator;
return(D.language||D.browserLanguage||D.systemLanguage||D.userLanguage||"").split("-").join("_")
};
var C=function(L){var J=[],K=angular.lowercase(L),G=0,D=m.length;
for(;
G<D;
G++){J.push(angular.lowercase(m[G]))
}if(J.indexOf(K)>-1){return L
}if(r){var H;
for(var I in r){var E=false;
var M=r.hasOwnProperty(I)&&angular.lowercase(I)===angular.lowercase(L);
if(I.slice(-1)==="*"){E=I.slice(0,-1)===L.slice(0,I.length-1)
}if(M||E){H=r[I];
if(J.indexOf(angular.lowercase(H))>-1){return H
}}}}var F=L.split("_");
if(F.length>1&&J.indexOf(angular.lowercase(F[0]))>-1){return F[0]
}return L
};
var g=function(E,D){if(!E&&!D){return j
}if(E&&!D){if(angular.isString(E)){return j[E]
}}else{if(!angular.isObject(j[E])){j[E]={}
}angular.extend(j[E],b(D))
}return this
};
this.translations=g;
this.cloakClassName=function(D){if(!D){return i
}i=D;
return this
};
var b=function(I,J,D,H){var G,F,E,K;
if(!J){J=[]
}if(!D){D={}
}for(G in I){if(!I.hasOwnProperty(G)){continue
}K=I[G];
if(angular.isObject(K)){b(K,J.concat(G),D,G)
}else{F=J.length?""+J.join(d)+d+G:G;
if(J.length&&G===H){E=""+J.join(d);
D[E]="@:"+F
}D[F]=K
}}return D
};
this.addInterpolation=function(D){o.push(D);
return this
};
this.useMessageFormatInterpolation=function(){return this.useInterpolation("$translateMessageFormatInterpolation")
};
this.useInterpolation=function(D){u=D;
return this
};
this.useSanitizeValueStrategy=function(D){x=D;
return this
};
this.preferredLanguage=function(D){if(D){y=D;
return this
}return y
};
this.translationNotFoundIndicator=function(D){this.translationNotFoundIndicatorLeft(D);
this.translationNotFoundIndicatorRight(D);
return this
};
this.translationNotFoundIndicatorLeft=function(D){if(!D){return q
}q=D;
return this
};
this.translationNotFoundIndicatorRight=function(D){if(!D){return l
}l=D;
return this
};
this.fallbackLanguage=function(D){c(D);
return this
};
var c=function(D){if(D){if(angular.isString(D)){p=true;
s=[D]
}else{if(angular.isArray(D)){p=false;
s=D
}}if(angular.isString(y)){s.push(y)
}return this
}else{if(p){return s[0]
}else{return s
}}};
this.use=function(D){if(D){if(!j[D]&&!z){throw new Error("$translateProvider couldn't find translationTable for langKey: '"+D+"'")
}f=D;
return this
}return f
};
var t=function(D){if(!D){if(a){return a+h
}return h
}h=D
};
this.storageKey=t;
this.useUrlLoader=function(D){return this.useLoader("$translateUrlLoader",{url:D})
};
this.useStaticFilesLoader=function(D){return this.useLoader("$translateStaticFilesLoader",D)
};
this.useLoader=function(D,E){z=D;
w=E||{};
return this
};
this.useLocalStorage=function(){return this.useStorage("$translateLocalStorage")
};
this.useCookieStorage=function(){return this.useStorage("$translateCookieStorage")
};
this.useStorage=function(D){k=D;
return this
};
this.storagePrefix=function(D){if(!D){return D
}a=D;
return this
};
this.useMissingTranslationHandlerLog=function(){return this.useMissingTranslationHandler("$translateMissingTranslationHandlerLog")
};
this.useMissingTranslationHandler=function(D){n=D;
return this
};
this.usePostCompiling=function(D){A=!!D;
return this
};
this.determinePreferredLanguage=function(E){var D=E&&angular.isFunction(E)?E():v();
if(!m.length){y=D
}else{y=C(D)
}return this
};
this.registerAvailableLanguageKeys=function(E,D){if(E){m=E;
if(D){r=D
}return this
}return m
};
this.$get=["$log","$injector","$rootScope","$q","I18nDirectiveService",function(Q,T,L,Z,ad){var ae,J=T.get(u||"$translateDefaultInterpolation"),ac=false,G={},M={},E,O;
var R=function(ag,aj,ak){if(angular.isArray(ag)){var ai=function(ar){var ao={};
var an=[];
var ap=function(at){var av=Z.defer();
var au=function(aw){ao[at]=aw;
av.resolve([at,aw])
};
R(at,aj,ak).then(au,au);
return av.promise
};
for(var am=0,aq=ar.length;
am<aq;
am++){an.push(ap(ar[am]))
}return Z.all(an).then(function(){return ao
})
};
return ai(ag)
}var ah=Z.defer();
if(ag){ag=ag.trim()
}var al=function(){var an=y?M[y]:M[f];
E=0;
if(k&&!an){var ao=ae.get(h);
an=M[ao];
if(s&&s.length){var am=D(s,ao);
E=am>-1?am+=1:0;
s.push(y)
}}return an
}();
if(!al){af(ag,aj,ak).then(ah.resolve,ah.reject)
}else{al.then(function(){af(ag,aj,ak).then(ah.resolve,ah.reject)
},ah.reject)
}return ah.promise
};
var D=function(aj,ah){for(var ai=0,ag=aj.length;
ai<ag;
ai++){if(aj[ai]===ah){return ai
}}return -1
};
var N=function(ag){if(q){ag=[q,ag].join(" ")
}if(l){ag=[ag,l].join(" ")
}return ag
};
var X=function(ag){f=ag;
L.$emit("$translateChangeSuccess");
if(k){ae.set(R.storageKey(),f)
}J.setLocale(f);
angular.forEach(G,function(ah,ai){G[ai].setLocale(f)
});
L.$emit("$translateChangeEnd")
};
var aa=function(ah){if(!ah){throw"No language key specified for loading."
}var ag=Z.defer();
L.$emit("$translateLoadingStart");
ac=true;
T.get(z)(angular.extend(w,{key:ah})).then(function(aj){var ai={};
L.$emit("$translateLoadingSuccess");
if(angular.isArray(aj)){angular.forEach(aj,function(ak){angular.extend(ai,b(ak))
})
}else{angular.extend(ai,b(aj))
}ac=false;
ag.resolve({key:ah,table:ai});
L.$emit("$translateLoadingEnd")
},function(ai){L.$emit("$translateLoadingError");
ag.reject(ai);
L.$emit("$translateLoadingEnd")
});
return ag.promise
};
if(k){ae=T.get(k);
if(!ae.get||!ae.set){throw new Error("Couldn't use storage '"+k+"', missing get() or set() method!")
}}if(angular.isFunction(J.useSanitizeValueStrategy)){J.useSanitizeValueStrategy(x)
}if(o.length){angular.forEach(o,function(ag){var ah=T.get(ag);
ah.setLocale(y||f);
if(angular.isFunction(ah.useSanitizeValueStrategy)){ah.useSanitizeValueStrategy(x)
}G[ah.getInterpolationIdentifier()]=ah
})
}var S=function(ah){var ag=Z.defer();
if(j.hasOwnProperty(ah)){ag.resolve(j[ah]);
return ag.promise
}else{M[ah].then(function(ai){g(ai.key,ai.table);
ag.resolve(ai.table)
},ag.reject)
}return ag.promise
};
var H=function(ak,ah,aj,ag){var ai=Z.defer();
S(ak).then(function(al){if(al.hasOwnProperty(ah)){ag.setLocale(ak);
ai.resolve(ag.interpolate(al[ah],aj));
ag.setLocale(f)
}else{ai.reject()
}},ai.reject);
return ai.promise
};
var ab=function(al,ai,ak,ah){var ag,aj=j[al];
if(aj.hasOwnProperty(ai)){ah.setLocale(al);
ag=ah.interpolate(aj[ai],ak);
ah.setLocale(f)
}return ag
};
var K=function(ak,ah,aj,ag){var ai=Z.defer();
if(ak<s.length){var am=s[ak];
H(am,ah,aj,ag).then(function(an){ai.resolve(an)
},function(){var an=K(ak+1,ah,aj,ag);
ai.resolve(an)
})
}else{if(n){var al=T.get(n)(ah,f);
if(al!==undefined){ai.resolve(al)
}else{ai.resolve(ah)
}}else{ai.resolve(ah)
}}return ai.promise
};
var I=function(ak,ai,aj,ah){var ag;
if(ak<s.length){var al=s[ak];
ag=ab(al,ai,aj,ah);
if(!ag){ag=I(ak+1,ai,aj,ah)
}}return ag
};
var P=function(ah,ai,ag){return K(O>0?O:E,ah,ai,ag)
};
var F=function(ah,ai,ag){return I(O>0?O:E,ah,ai,ag)
};
var af=function(ah,aj,al){var ai=Z.defer();
var ak=f?j[f]:j,ag=al?G[al]:J;
if(ak&&ak.hasOwnProperty(ah)){var am=ak[ah];
if(am.substr(0,2)==="@:"){R(am.substr(2),aj,al).then(ai.resolve,ai.reject)
}else{ai.resolve(ag.interpolate(am,aj))
}}else{if(n&&!ac){T.get(n)(ah,f)
}if(f&&s&&s.length){P(ah,aj,ag).then(function(an){ai.resolve(an)
},function(an){ai.reject(N(an))
})
}else{ai.reject(N(ah))
}}return ai.promise
};
var W=function(ai,aj,al){var ah,ak=f?j[f]:j,ag=al?G[al]:J;
if(ak&&ak.hasOwnProperty(ai)){var am=ak[ai];
if(am.substr(0,2)==="@:"){ah=W(am.substr(2),aj,al)
}else{ah=ag.interpolate(am,aj)
}}else{if(n&&!ac){T.get(n)(ai,f)
}if(f&&s&&s.length){E=0;
ah=F(ai,aj,ag)
}else{ah=N(ai)
}}return ah
};
R.preferredLanguage=function(){return y
};
R.cloakClassName=function(){return i
};
R.fallbackLanguage=function(ai){if(ai!==undefined&&ai!==null){c(ai);
if(z){if(s&&s.length){for(var ah=0,ag=s.length;
ah<ag;
ah++){if(!M[s[ah]]){M[s[ah]]=aa(s[ah])
}}}}R.use(R.use())
}if(p){return s[0]
}else{return s
}};
R.useFallbackLanguage=function(ah){if(ah!==undefined&&ah!==null){if(!ah){O=0
}else{var ag=D(s,ah);
if(ag>-1){O=ag
}}}};
R.proposedLanguage=function(){return B
};
R.storage=function(){return ae
};
R.use=function(ah){if(!ah){return f
}var ag=Z.defer();
L.$emit("$translateChangeStart");
var ai=C(ah);
if(ai){ah=ai
}if(!j[ah]&&z){B=ah;
M[ah]=aa(ah).then(function(aj){g(aj.key,aj.table);
ag.resolve(aj.key);
if(B===ah){X(aj.key);
B=undefined
}},function(aj){B=undefined;
L.$emit("$translateChangeError");
ag.reject(aj);
L.$emit("$translateChangeEnd")
})
}else{ag.resolve(ah);
X(ah)
}return ag.promise
};
R.storageKey=function(){return t()
};
R.isPostCompilingEnabled=function(){return A
};
R.refresh=function(am){if(!z){throw new Error("Couldn't refresh translation table, no loader registered!")
}var ah=Z.defer();
function al(){ah.resolve();
L.$emit("$translateRefreshEnd")
}function ak(){ah.reject();
L.$emit("$translateRefreshEnd")
}L.$emit("$translateRefreshStart");
if(!am){var aj=[];
if(s&&s.length){for(var ai=0,ag=s.length;
ai<ag;
ai++){aj.push(aa(s[ai]))
}}if(f){aj.push(aa(f))
}Z.all(aj).then(function(an){angular.forEach(an,function(ao){if(j[ao.key]){delete j[ao.key]
}g(ao.key,ao.table)
});
if(f){X(f)
}al()
})
}else{if(j[am]){aa(am).then(function(an){g(an.key,an.table);
if(am===f){X(f)
}al()
},ak)
}else{ak()
}}return ah.promise
};
R.removeEntity=function(ag){var ah=ag;
return ah.substring(ah.indexOf(".")+1,ah.length)
};
R.getEntity=function(ag){var ah=ag;
return ah.substring(0,ah.indexOf(".")+1)
};
var V=[];
R.instant=function(av,aw,at,aj,au){if(av===null||angular.isUndefined(av)){return av
}if(angular.isArray(av)){var ak={};
for(var al=0,ar=av.length;
al<ar;
al++){ak[av[al]]=R.instant(av[al].replace(/\s+/g,""),aw,at,R.removeEntity(av[al]))
}return ak
}if(angular.isString(av)&&av.length<1){return av
}if(av){av=av.trim()
}var ax,aq=[];
if(y){aq.push(y)
}if(f){aq.push(f)
}if(s&&s.length){aq=aq.concat(s)
}var ai;
for(var ah=0,ap=aq.length;
ah<ap;
ah++){var an=aq[ah];
if(j[an]){ai=an;
if(typeof j[an][av]!=="undefined"){ax=W(av,aw,at)
}}if(typeof ax!=="undefined"){break
}}if(j[an]&&ax===undefined){ax=aj;
var am=Object.keys(V).length;
var ao=false;
for(var al=0;
al<am;
al++){if(ax==V[al]){ao=true
}}if(!ao){V.push(ax);
if(L.isLabelEntryInDatabase){var ag={key:ax,country:"IN",language:ai,defaultText:"",translationPending:true,text:ax,environment:"w",type:"LABEL",entity:au};
ad.createLabel(ag,function(ay){R.refresh(an)
},function(){})
}}if(n&&!ac){T.get(n)(av,f)
}}return ax
};
if(z){if(angular.equals(j,{})){R.use(R.use())
}if(s&&s.length){for(var U=0,Y=s.length;
U<Y;
U++){M[s[U]]=aa(s[U])
}}}return R
}]
}]);
angular.module("pascalprecht.translate").factory("$translateDefaultInterpolation",["$interpolate",function(e){var d={},b,a="default",c=null,g={escaped:function(j){var h={};
for(var i in j){if(j.hasOwnProperty(i)){h[i]=angular.element("<div></div>").text(j[i]).html()
}}return h
}};
var f=function(i){var h;
if(angular.isFunction(g[c])){h=g[c](i)
}else{h=i
}return h
};
d.setLocale=function(h){b=h
};
d.getInterpolationIdentifier=function(){return a
};
d.useSanitizeValueStrategy=function(h){c=h;
return this
};
d.interpolate=function(i,h){if(c){h=f(h)
}return e(i)(h||{})
};
return d
}]);
angular.module("pascalprecht.translate").constant("$STORAGE_KEY","NG_TRANSLATE_LANG_KEY");
angular.module("pascalprecht.translate").directive("translate",["$translate","$q","$interpolate","$compile","$parse","$rootScope",function(e,b,f,c,d,a){return{restrict:"AE",scope:true,compile:function(j,h){var i=h.translateValues?h.translateValues:undefined;
var k=h.translateInterpolation?h.translateInterpolation:undefined;
var g=j[0].outerHTML.match(/translate-value-+/i);
return function l(s,t,o){s.interpolateParams={};
o.$observe("translate",function(u){if(angular.equals(u,"")||!angular.isDefined(u)){s.translationId=f(t.text().replace(/^\s+|\s+$/g,""))(s.$parent)
}else{s.translationId=u
}});
o.$observe("translateDefault",function(u){s.defaultText=u
});
if(i){o.$observe("translateValues",function(u){if(u){s.$parent.$watch(function(){angular.extend(s.interpolateParams,d(u)(s.$parent))
})
}})
}if(g){var r=function(u){o.$observe(u,function(v){s.interpolateParams[angular.lowercase(u.substr(14,1))+u.substr(15)]=v
})
};
for(var n in o){if(o.hasOwnProperty(n)&&n.substr(0,14)==="translateValue"&&n!=="translateValues"){r(n)
}}}var m=function(w,u,z){if(!z&&typeof u.defaultText!=="undefined"){w=u.defaultText
}t.html(w);
var x=e.isPostCompilingEnabled();
var v=typeof h.translateCompile!=="undefined";
var y=v&&h.translateCompile!=="false";
if(x&&!v||y){c(t.contents())(u)
}};
var p=function(){if(!i&&!g){return function(){var u=s.$watch("translationId",function(v){if(s.translationId&&v){e(v,{},k).then(function(w){m(w,s,true);
u()
},function(w){m(w,s,false);
u()
})
}},true)
}
}else{return function(){var u=function(){if(s.translationId&&s.interpolateParams){e(s.translationId,s.interpolateParams,k).then(function(v){m(v,s,true)
},function(v){m(v,s,false)
})
}};
s.$watch("interpolateParams",u,true);
s.$watch("translationId",u)
}
}}();
var q=a.$on("$translateChangeSuccess",p);
p();
s.$on("$destroy",q)
}
}}
}]);
angular.module("pascalprecht.translate").directive("translateCloak",["$rootScope","$translate",function(a,b){return{compile:function(c){a.$on("$translateLoadingSuccess",function(){c.removeClass(b.cloakClassName())
});
c.addClass(b.cloakClassName())
}}
}]);
angular.module("pascalprecht.translate").filter("translate",["$parse","$translate",function(b,a){return function(c,e,d){if(!angular.isObject(e)){e=b(e)(this)
}return a.instant(c.replace(/\s+/g,""),e,d,a.removeEntity(c),a.getEntity(c))
}
}]);