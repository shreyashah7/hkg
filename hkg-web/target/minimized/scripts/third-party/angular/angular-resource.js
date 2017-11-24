(function(f,b,h){var e=b.$$minErr("$resource");
var a=/^(\.[a-zA-Z_$][0-9a-zA-Z_$]*)+$/;
function c(i){return(i!=null&&i!==""&&i!=="hasOwnProperty"&&a.test("."+i))
}function d(o,n){if(!c(n)){throw e("badmember",'Dotted member path "@{0}" is invalid.',n)
}var m=n.split(".");
for(var k=0,l=m.length;
k<l&&o!==h;
k++){var j=m[k];
o=(o!==null)?o[j]:h
}return o
}function g(j,k){k=k||{};
b.forEach(k,function(m,l){delete k[l]
});
for(var i in j){if(j.hasOwnProperty(i)&&!(i.charAt(0)==="$"&&i.charAt(1)==="$")){k[i]=j[i]
}}return k
}b.module("ngResource",["ng"]).factory("$resource",["$http","$q",function(s,n){var o={get:{method:"GET"},save:{method:"POST"},query:{method:"GET",isArray:true},remove:{method:"DELETE"},"delete":{method:"DELETE"}};
var t=b.noop,m=b.forEach,r=b.extend,i=b.copy,k=b.isFunction;
function l(u){return j(u,true).replace(/%26/gi,"&").replace(/%3D/gi,"=").replace(/%2B/gi,"+")
}function j(v,u){return encodeURIComponent(v).replace(/%40/gi,"@").replace(/%3A/gi,":").replace(/%24/g,"$").replace(/%2C/gi,",").replace(/%20/g,(u?"%20":"+"))
}function q(u,v){this.template=u;
this.defaults=v||{};
this.urlParams={}
}q.prototype={setUrlParams:function(y,B,z){var w=this,x=z||w.template,A,u;
var v=w.urlParams={};
m(x.split(/\W/),function(C){if(C==="hasOwnProperty"){throw e("badname","hasOwnProperty is not a valid parameter name.")
}if(!(new RegExp("^\\d+$").test(C))&&C&&(new RegExp("(^|[^\\\\]):"+C+"(\\W|$)").test(x))){v[C]=true
}});
x=x.replace(/\\:/g,":");
B=B||{};
m(w.urlParams,function(D,C){A=B.hasOwnProperty(C)?B[C]:w.defaults[C];
if(b.isDefined(A)&&A!==null){u=l(A);
x=x.replace(new RegExp(":"+C+"(\\W|$)","g"),function(E,F){return u+F
})
}else{x=x.replace(new RegExp("(/?):"+C+"(\\W|$)","g"),function(F,G,E){if(E.charAt(0)=="/"){return E
}else{return G+E
}})
}});
x=x.replace(/\/+$/,"")||"/";
x=x.replace(/\/\.(?=\w+($|\?))/,".");
y.url=x.replace(/\/\\\./,"/.");
m(B,function(D,C){if(!w.urlParams[C]){y.params=y.params||{};
y.params[C]=D
}})
}};
function p(w,y,A){var u=new q(w);
A=r({},o,A);
function v(D,C){var B={};
C=r({},y,C);
m(C,function(F,E){if(k(F)){F=F()
}B[E]=F&&F.charAt&&F.charAt(0)=="@"?d(D,F.substr(1)):F
});
return B
}function x(B){return B.resource
}function z(B){g(B||{},this)
}m(A,function(D,B){var C=/^(POST|PUT|PATCH)$/i.test(D.method);
z[B]=function(H,F,E,R){var J={},K,O,M;
switch(arguments.length){case 4:M=R;
O=E;
case 3:case 2:if(k(F)){if(k(H)){O=H;
M=F;
break
}O=F;
M=E
}else{J=H;
K=F;
O=E;
break
}case 1:if(k(H)){O=H
}else{if(C){K=H
}else{J=H
}}break;
case 0:break;
default:throw e("badargs","Expected up to 4 arguments [params, data, success, error], got {0} arguments",arguments.length)
}var G=this instanceof z;
var N=G?K:(D.isArray?[]:new z(K));
var I={};
var L=D.interceptor&&D.interceptor.response||x;
var P=D.interceptor&&D.interceptor.responseError||h;
m(D,function(T,S){if(S!="params"&&S!="isArray"&&S!="interceptor"){I[S]=i(T)
}});
if(C){I.data=K
}u.setUrlParams(I,r({},v(K,D.params||{}),J),D.url);
var Q=s(I).then(function(S){var T=S.data,U=N.$promise;
if(T){if(b.isArray(T)!==(!!D.isArray)){throw e("badcfg","Error in resource configuration. Expected response to contain an {0} but got an {1}",D.isArray?"array":"object",b.isArray(T)?"array":"object")
}if(D.isArray){N.length=0;
m(T,function(V){N.push(new z(V))
})
}else{g(T,N);
N.$promise=U
}}N.$resolved=true;
S.resource=N;
return S
},function(S){N.$resolved=true;
(M||t)(S);
return n.reject(S)
});
Q=Q.then(function(S){var T=L(S);
(O||t)(T,S.headers);
return T
},P);
if(!G){N.$promise=Q;
N.$resolved=false;
return N
}return Q
};
z.prototype["$"+B]=function(H,G,F){if(k(H)){F=G;
G=H;
H={}
}var E=z[B].call(this,H,this,G,F);
return E.$promise||E
}
});
z.bind=function(B){return p(w,r({},y,B),A)
};
return z
}return p
}])
})(window,window.angular);