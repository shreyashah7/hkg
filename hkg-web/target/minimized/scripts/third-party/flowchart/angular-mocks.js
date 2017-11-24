(function(g,j,a){j.mock={};
j.mock.$BrowserProvider=function(){this.$get=function(){return new j.mock.$Browser()
}
};
j.mock.$Browser=function(){var n=this;
this.isMock=true;
n.$$url="http://server/";
n.$$lastUrl=n.$$url;
n.pollFns=[];
n.$$completeOutstandingRequest=j.noop;
n.$$incOutstandingRequestCount=j.noop;
n.onUrlChange=function(o){n.pollFns.push(function(){if(n.$$lastUrl!=n.$$url){n.$$lastUrl=n.$$url;
o(n.$$url)
}});
return o
};
n.cookieHash={};
n.lastCookieHash={};
n.deferredFns=[];
n.deferredNextId=0;
n.defer=function(p,o){o=o||0;
n.deferredFns.push({time:(n.defer.now+o),fn:p,id:n.deferredNextId});
n.deferredFns.sort(function(r,q){return r.time-q.time
});
return n.deferredNextId++
};
n.defer.now=0;
n.defer.cancel=function(p){var o;
j.forEach(n.deferredFns,function(r,q){if(r.id===p){o=q
}});
if(o!==a){n.deferredFns.splice(o,1);
return true
}return false
};
n.defer.flush=function(o){if(j.isDefined(o)){n.defer.now+=o
}else{if(n.deferredFns.length){n.defer.now=n.deferredFns[n.deferredFns.length-1].time
}else{throw new Error("No deferred tasks to be flushed")
}}while(n.deferredFns.length&&n.deferredFns[0].time<=n.defer.now){n.deferredFns.shift().fn()
}};
n.$$baseHref="";
n.baseHref=function(){return this.$$baseHref
}
};
j.mock.$Browser.prototype={poll:function i(){j.forEach(this.pollFns,function(n){n()
})
},addPollFn:function(n){this.pollFns.push(n);
return n
},url:function(n,o){if(n){this.$$url=n;
return this
}return this.$$url
},cookies:function(n,o){if(n){if(j.isUndefined(o)){delete this.cookieHash[n]
}else{if(j.isString(o)&&o.length<=4096){this.cookieHash[n]=o
}}}else{if(!j.equals(this.cookieHash,this.lastCookieHash)){this.lastCookieHash=j.copy(this.cookieHash);
this.cookieHash=j.copy(this.cookieHash)
}return this.cookieHash
}},notifyWhenNoOutstandingRequests:function(n){n()
}};
j.mock.$ExceptionHandlerProvider=function(){var n;
this.mode=function(o){switch(o){case"rethrow":n=function(q){throw q
};
break;
case"log":var p=[];
n=function(q){if(arguments.length==1){p.push(q)
}else{p.push([].slice.call(arguments,0))
}};
n.errors=p;
break;
default:throw new Error("Unknown mode '"+o+"', only 'log'/'rethrow' modes are allowed!")
}};
this.$get=function(){return n
};
this.mode("rethrow")
};
j.mock.$LogProvider=function(){var n=true;
function o(r,q,p){return r.concat(Array.prototype.slice.call(q,p))
}this.debugEnabled=function(p){if(j.isDefined(p)){n=p;
return this
}else{return n
}};
this.$get=function(){var p={log:function(){p.log.logs.push(o([],arguments,0))
},warn:function(){p.warn.logs.push(o([],arguments,0))
},info:function(){p.info.logs.push(o([],arguments,0))
},error:function(){p.error.logs.push(o([],arguments,0))
},debug:function(){if(n){p.debug.logs.push(o([],arguments,0))
}}};
p.reset=function(){p.log.logs=[];
p.info.logs=[];
p.warn.logs=[];
p.error.logs=[];
p.debug.logs=[]
};
p.assertEmpty=function(){var q=[];
j.forEach(["error","warn","info","log","debug"],function(r){j.forEach(p[r].logs,function(s){j.forEach(s,function(t){q.push("MOCK $log ("+r+"): "+String(t)+"\n"+(t.stack||""))
})
})
});
if(q.length){q.unshift("Expected $log to be empty! Either a message was logged unexpectedly, or an expected log message was not checked and removed:");
q.push("");
throw new Error(q.join("\n---------\n"))
}};
p.reset();
return p
}
};
j.mock.$IntervalProvider=function(){this.$get=["$rootScope","$q",function(n,p){var r=[],o=0,q=0;
var s=function(z,v,y,x){var A=p.defer(),B=A.promise,t=0,u=(j.isDefined(x)&&!x);
y=(j.isDefined(y))?y:0,B.then(null,null,z);
B.$$intervalId=o;
function w(){A.notify(t++);
if(y>0&&t>=y){var C;
A.resolve(t);
j.forEach(r,function(E,D){if(E.id===B.$$intervalId){C=D
}});
if(C!==a){r.splice(C,1)
}}if(!u){n.$apply()
}}r.push({nextTime:(q+v),delay:v,fn:w,id:o,deferred:A});
r.sort(function(D,C){return D.nextTime-C.nextTime
});
o++;
return B
};
s.cancel=function(u){var t;
j.forEach(r,function(w,v){if(w.id===u.$$intervalId){t=v
}});
if(t!==a){r[t].deferred.reject("canceled");
r.splice(t,1);
return true
}return false
};
s.flush=function(u){q+=u;
while(r.length&&r[0].nextTime<=q){var t=r[0];
t.fn();
t.nextTime+=t.delay;
r.sort(function(w,v){return w.nextTime-v.nextTime
})
}return u
};
return s
}]
};
var d=/^(\d{4})-?(\d\d)-?(\d\d)(?:T(\d\d)(?:\:?(\d\d)(?:\:?(\d\d)(?:\.(\d{3}))?)?)?(Z|([+-])(\d\d):?(\d\d)))?$/;
function b(r){var q;
if(q=r.match(d)){var p=new Date(0),o=0,n=0;
if(q[9]){o=k(q[9]+q[10]);
n=k(q[9]+q[11])
}p.setUTCFullYear(k(q[1]),k(q[2])-1,k(q[3]));
p.setUTCHours(k(q[4]||0)-o,k(q[5]||0)-n,k(q[6]||0),k(q[7]||0));
return p
}return r
}function k(n){return k(n,10)
}function l(o,p,n){var q="";
if(o<0){q="-";
o=-o
}o=""+o;
while(o.length<p){o="0"+o
}if(n){o=o.substr(o.length-p)
}return q+o
}j.mock.TzDate=function(s,q){var n=new Date(0);
if(j.isString(q)){var o=q;
n.origDate=b(q);
q=n.origDate.getTime();
if(isNaN(q)){throw {name:"Illegal Argument",message:"Arg '"+o+"' passed into TzDate constructor is not a valid date string"}
}}else{n.origDate=new Date(q)
}var r=new Date(q).getTimezoneOffset();
n.offsetDiff=r*60*1000-s*1000*60*60;
n.date=new Date(q+n.offsetDiff);
n.getTime=function(){return n.date.getTime()-n.offsetDiff
};
n.toLocaleDateString=function(){return n.date.toLocaleDateString()
};
n.getFullYear=function(){return n.date.getFullYear()
};
n.getMonth=function(){return n.date.getMonth()
};
n.getDate=function(){return n.date.getDate()
};
n.getHours=function(){return n.date.getHours()
};
n.getMinutes=function(){return n.date.getMinutes()
};
n.getSeconds=function(){return n.date.getSeconds()
};
n.getMilliseconds=function(){return n.date.getMilliseconds()
};
n.getTimezoneOffset=function(){return s*60
};
n.getUTCFullYear=function(){return n.origDate.getUTCFullYear()
};
n.getUTCMonth=function(){return n.origDate.getUTCMonth()
};
n.getUTCDate=function(){return n.origDate.getUTCDate()
};
n.getUTCHours=function(){return n.origDate.getUTCHours()
};
n.getUTCMinutes=function(){return n.origDate.getUTCMinutes()
};
n.getUTCSeconds=function(){return n.origDate.getUTCSeconds()
};
n.getUTCMilliseconds=function(){return n.origDate.getUTCMilliseconds()
};
n.getDay=function(){return n.date.getDay()
};
if(n.toISOString){n.toISOString=function(){return l(n.origDate.getUTCFullYear(),4)+"-"+l(n.origDate.getUTCMonth()+1,2)+"-"+l(n.origDate.getUTCDate(),2)+"T"+l(n.origDate.getUTCHours(),2)+":"+l(n.origDate.getUTCMinutes(),2)+":"+l(n.origDate.getUTCSeconds(),2)+"."+l(n.origDate.getUTCMilliseconds(),3)+"Z"
}
}var p=["getUTCDay","getYear","setDate","setFullYear","setHours","setMilliseconds","setMinutes","setMonth","setSeconds","setTime","setUTCDate","setUTCFullYear","setUTCHours","setUTCMilliseconds","setUTCMinutes","setUTCMonth","setUTCSeconds","setYear","toDateString","toGMTString","toJSON","toLocaleFormat","toLocaleString","toLocaleTimeString","toSource","toString","toTimeString","toUTCString","valueOf"];
j.forEach(p,function(t){n[t]=function(){throw new Error("Method '"+t+"' is not implemented in the TzDate mock")
}
});
return n
};
j.mock.TzDate.prototype=Date.prototype;
j.mock.animate=j.module("mock.animate",["ng"]).config(["$provide",function(n){n.decorator("$animate",function(p){var o={queue:[],enabled:p.enabled,flushNext:function(q){var r=o.queue.shift();
if(!r){throw new Error("No animation to be flushed")
}if(r.method!==q){throw new Error('The next animation is not "'+q+'", but is "'+r.method+'"')
}r.fn();
return r
}};
j.forEach(["enter","leave","move","addClass","removeClass"],function(q){o[q]=function(){var r=arguments;
o.queue.push({method:q,params:r,element:j.isElement(r[0])&&r[0],parent:j.isElement(r[1])&&r[1],after:j.isElement(r[2])&&r[2],fn:function(){p[q].apply(p,r)
}})
}
});
return o
})
}]);
j.mock.dump=function(n){return o(n);
function o(r){var q;
if(j.isElement(r)){r=j.element(r);
q=j.element("<div></div>");
j.forEach(r,function(s){q.append(j.element(s).clone())
});
q=q.html()
}else{if(j.isArray(r)){q=[];
j.forEach(r,function(s){q.push(o(s))
});
q="[ "+q.join(", ")+" ]"
}else{if(j.isObject(r)){if(j.isFunction(r.$eval)&&j.isFunction(r.$apply)){q=p(r)
}else{if(r instanceof Error){q=r.stack||(""+r.name+": "+r.message)
}else{q=j.toJson(r,true)
}}}else{q=String(r)
}}}return q
}function p(s,t){t=t||"  ";
var r=[t+"Scope("+s.$id+"): {"];
for(var q in s){if(Object.prototype.hasOwnProperty.call(s,q)&&!q.match(/^(\$|this)/)){r.push("  "+q+": "+j.toJson(s[q]))
}}var u=s.$$childHead;
while(u){r.push(p(u,t+"  "));
u=u.$$nextSibling
}r.push("}");
return r.join("\n"+t)
}};
j.mock.$HttpBackendProvider=function(){this.$get=["$rootScope",f]
};
function f(v,q,x){var w=[],p=[],t=[],r=j.bind(t,t.push),n=j.copy;
function s(y,z,A){if(j.isFunction(y)){return y
}return function(){return j.isNumber(y)?[y,z,A]:[200,y,z]
}
}function u(y,z,G,J,C,I,E){var K=new e(),A=p[0],H=false;
function D(M){return(j.isString(M)||j.isFunction(M)||M instanceof RegExp)?M:j.toJson(M)
}function L(M){if(!x&&I&&I.then){I.then(O)
}return N;
function N(){var P=M.response(y,z,G,C);
K.$$respHeaders=P[2];
J(n(P[0]),n(P[1]),K.getAllResponseHeaders())
}function O(){for(var P=0,Q=t.length;
P<Q;
P++){if(t[P]===N){t.splice(P,1);
J(-1,a,"");
break
}}}}if(A&&A.match(y,z)){if(!A.matchData(G)){throw new Error("Expected "+A+" with different data\nEXPECTED: "+D(A.data)+"\nGOT:      "+G)
}if(!A.matchHeaders(C)){throw new Error("Expected "+A+" with different headers\nEXPECTED: "+D(A.headers)+"\nGOT:      "+D(C))
}p.shift();
if(A.response){t.push(L(A));
return
}H=true
}var F=-1,B;
while((B=w[++F])){if(B.match(y,z,G,C||{})){if(B.response){(x?x.defer:r)(L(B))
}else{if(B.passThrough){q(y,z,G,J,C,I,E)
}else{throw new Error("No response defined !")
}}return
}}throw H?new Error("No response defined !"):new Error("Unexpected request: "+y+" "+z+"\n"+(A?"Expected "+A:"No more request expected"))
}u.when=function(D,y,B,C){var A=new c(D,y,B,C),z={respond:function(E,F,G){A.response=s(E,F,G)
}};
if(x){z.passThrough=function(){A.passThrough=true
}
}w.push(A);
return z
};
o("when");
u.expect=function(C,z,A,B){var y=new c(C,z,A,B);
p.push(y);
return{respond:function(D,E,F){y.response=s(D,E,F)
}}
};
o("expect");
u.flush=function(y){v.$digest();
if(!t.length){throw new Error("No pending request to flush !")
}if(j.isDefined(y)){while(y--){if(!t.length){throw new Error("No more pending request to flush !")
}t.shift()()
}}else{while(t.length){t.shift()()
}}u.verifyNoOutstandingExpectation()
};
u.verifyNoOutstandingExpectation=function(){v.$digest();
if(p.length){throw new Error("Unsatisfied requests: "+p.join(", "))
}};
u.verifyNoOutstandingRequest=function(){if(t.length){throw new Error("Unflushed requests: "+t.length)
}};
u.resetExpectations=function(){p.length=0;
t.length=0
};
return u;
function o(y){j.forEach(["GET","DELETE","JSONP"],function(z){u[y+z]=function(A,B){return u[y](z,A,a,B)
}
});
j.forEach(["PUT","POST","PATCH"],function(z){u[y+z]=function(A,B,C){return u[y](z,A,B,C)
}
})
}}function c(q,n,o,p){this.data=o;
this.headers=p;
this.match=function(r,s,v,t){if(q!=r){return false
}if(!this.matchUrl(s)){return false
}if(j.isDefined(v)&&!this.matchData(v)){return false
}if(j.isDefined(t)&&!this.matchHeaders(t)){return false
}return true
};
this.matchUrl=function(r){if(!n){return true
}if(j.isFunction(n.test)){return n.test(r)
}return n==r
};
this.matchHeaders=function(r){if(j.isUndefined(p)){return true
}if(j.isFunction(p)){return p(r)
}return j.equals(p,r)
};
this.matchData=function(r){if(j.isUndefined(o)){return true
}if(o&&j.isFunction(o.test)){return o.test(r)
}if(o&&j.isFunction(o)){return o(r)
}if(o&&!j.isString(o)){return j.equals(o,j.fromJson(r))
}return o==r
};
this.toString=function(){return q+" "+n
}
}function e(){e.$$lastInstance=this;
this.open=function(p,n,o){this.$$method=p;
this.$$url=n;
this.$$async=o;
this.$$reqHeaders={};
this.$$respHeaders={}
};
this.send=function(n){this.$$data=n
};
this.setRequestHeader=function(n,o){this.$$reqHeaders[n]=o
};
this.getResponseHeader=function(n){var o=this.$$respHeaders[n];
if(o){return o
}n=j.lowercase(n);
o=this.$$respHeaders[n];
if(o){return o
}o=a;
j.forEach(this.$$respHeaders,function(p,q){if(!o&&j.lowercase(q)==n){o=p
}});
return o
};
this.getAllResponseHeaders=function(){var n=[];
j.forEach(this.$$respHeaders,function(p,o){n.push(o+": "+p)
});
return n.join("\n")
};
this.abort=j.noop
}j.mock.$TimeoutDecorator=function(p,n){p.flush=function(q){n.defer.flush(q)
};
p.verifyNoPendingTasks=function(){if(n.deferredFns.length){throw new Error("Deferred tasks to flush ("+n.deferredFns.length+"): "+o(n.deferredFns))
}};
function o(r){var q=[];
j.forEach(r,function(s){q.push("{id: "+s.id+", time: "+s.time+"}")
});
return q.join(", ")
}return p
};
j.mock.$RootElementProvider=function(){this.$get=function(){return j.element("<div ng-app></div>")
}
};
j.module("ngMock",["ng"]).provider({$browser:j.mock.$BrowserProvider,$exceptionHandler:j.mock.$ExceptionHandlerProvider,$log:j.mock.$LogProvider,$interval:j.mock.$IntervalProvider,$httpBackend:j.mock.$HttpBackendProvider,$rootElement:j.mock.$RootElementProvider}).config(["$provide",function(n){n.decorator("$timeout",j.mock.$TimeoutDecorator)
}]);
j.module("ngMockE2E",["ng"]).config(["$provide",function(n){n.decorator("$httpBackend",j.mock.e2e.$httpBackendDecorator)
}]);
j.mock.e2e={};
j.mock.e2e.$httpBackendDecorator=["$rootScope","$delegate","$browser",f];
j.mock.clearDataCache=function(){var o,n=j.element.cache;
for(o in n){if(Object.prototype.hasOwnProperty.call(n,o)){var p=n[o].handle;
p&&j.element(p.elem).off();
delete n[o]
}}};
if(g.jasmine||g.mocha){var m=null,h=function(){return m&&(g.mocha||m.queue.running)
};
beforeEach(function(){m=this
});
afterEach(function(){var n=m.$injector;
m.$injector=null;
m.$modules=null;
m=null;
if(n){n.get("$rootElement").off();
n.get("$browser").pollFns.length=0
}j.mock.clearDataCache();
j.forEach(j.element.fragments,function(p,o){delete j.element.fragments[o]
});
e.$$lastInstance=null;
j.forEach(j.callbacks,function(p,o){delete j.callbacks[o]
});
j.callbacks.counter=0
});
g.module=j.mock.module=function(){var n=Array.prototype.slice.call(arguments,0);
return h()?o():o;
function o(){if(m.$injector){throw new Error("Injector already created, can not register a module!")
}else{var p=m.$modules||(m.$modules=[]);
j.forEach(n,function(q){if(j.isObject(q)&&!j.isArray(q)){p.push(function(r){j.forEach(q,function(t,s){r.value(s,t)
})
})
}else{p.push(q)
}})
}}};
g.inject=j.mock.inject=function(){var n=Array.prototype.slice.call(arguments,0);
var p=new Error("Declaration Location");
return h()?o():o;
function o(){var q=m.$modules||[];
q.unshift("ngMock");
q.unshift("ng");
var u=m.$injector;
if(!u){u=m.$injector=j.injector(q)
}for(var r=0,s=n.length;
r<s;
r++){try{u.invoke(n[r]||j.noop,this)
}catch(t){if(t.stack&&p){t.stack+="\n"+p.stack
}throw t
}finally{p=null
}}}}
}})(window,window.angular);