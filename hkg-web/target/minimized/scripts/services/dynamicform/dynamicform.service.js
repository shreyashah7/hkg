angular.module("dynamicformmodule",[]).factory("DynamicFormService",["$rootScope","$resource","$q","$filter",function(b,d,a,e){var c=d(b.apipath+"customfield/:action",{action:"@actionName"},{retrieveAllCustomField:{method:"POST",isArray:false,params:{action:"retrieveallfeaturesection"}},retrieveCustomFieldByFeatureName:{method:"POST",isArray:false,params:{action:"retrievesectionandcustomfieldtemplate"},url:b.centerapipath+"customfield/:action"},retrieveCustomFieldBySeachCriteria:{method:"POST",isArray:false,params:{action:"retrievecustomfieldbyseachcriteria"},url:b.centerapipath+"customfield/:action"},retrieveSearchField:{method:"POST",isArray:false,params:{action:"retrieveSearchField"},url:b.centerapipath+"customfield/:action"},retrieveRecipientNames:{method:"POST",isArray:false,params:{action:"retrieveRecipientNames"}},retrieveCustomNamesOfComponentIds:{method:"POST",isArray:false,params:{action:"retrieveCustomNamesOfComponentIds"},url:b.centerapipath+"customfield/:action"},retrieveSubEntitiesByFieldId:{method:"POST",isArray:true,params:{action:"retrieveSubEntities"}}});
return{storeAllCustomFieldData:function(f){var h=localStorage.getItem("customFieldVersion");
var g={CUSTOM_FIELD_VERSION:h,companyId:localStorage.getItem("companyId")};
c.retrieveAllCustomField(g,function(j){if(j.CUSTOM_FIELD_VERSION!==undefined){clearLocalStorage()
}if(j.CUSTOM_FIELD_VERSION!==undefined){localStorage.setItem("customFieldVersion",j.CUSTOM_FIELD_VERSION)
}localStorage.setItem("companyId",f);
if(j.customFieldData!==undefined){for(var k in j.customFieldData){var m=encryptPass(JSON.stringify(j.customFieldData[k]));
try{localStorage.setItem(k,m)
}catch(l){break
}}}},function(){})
},retrieveSectionWiseCustomFieldInfo:function(g){var f=a.defer();
var h=null;
if(g){h=encryptPass(localStorage.getItem(g));
if(h!==null){f.resolve(JSON.parse(h));
return f.promise
}else{c.retrieveCustomFieldByFeatureName(g,function(j){if(j){var l=encryptPass(JSON.stringify(j));
try{localStorage.setItem(g,l)
}catch(k){}f.resolve(j)
}});
return f.promise
}}},resetSection:function(g){var f={};
if(g){for(var h=0;
h<g.length;
h++){if(angular.isDefined(g[h].val)){f[g[h].model]=g[h].val
}else{if(g[h].type==="password"){f[g[h].model]={}
}}}}return f
},convertToViewOnlyData:function(h,g){if(g){for(var f=0;
f<g.length;
f++){if(h[g[f].model]&&g[f].values){h[g[f].model]=g[f].values[h[g[f].model]]
}}}return h
},getValuesOfComponentFromId:function(h,g){if(g){for(var f=0;
f<g.length;
f++){angular.forEach(h,function(k){if(k.categoryCustom[g[f].model]&&g[f].values&&g[f].type==="multiSelect"){var j="";
angular.forEach(k.categoryCustom[g[f].model],function(l){j+=l.text+","
});
j=j.substring(0,j.length-1);
k.categoryCustom[g[f].model]=j
}if(k.categoryCustom[g[f].model]&&g[f].values){k.categoryCustom[g[f].model]=g[f].values[k.categoryCustom[g[f].model]]
}})
}}return h
},convertorForCustomField:function(o,q,y){if(!!o){if(y===undefined){y=false
}var u=[];
var C=[];
var l=[];
var w=[];
var D=[];
var B=[];
var j=[];
var r=[];
var A=[];
var n=[];
var s=[];
var m=new Object();
var E=new Object();
var h=new Object();
var k=new Object();
var z=new Object();
var t=new Object();
var x=new Object();
var g=new Object();
var v=new Object();
var f=new Object();
angular.forEach(o,function(J){var G=J.categoryCustom;
for(var I in G){var F=[];
if(G.hasOwnProperty(I)){F=I.toString().split("$");
var H=F[1];
var L=F[0];
var K=G[I];
if(H==="DRP"&&K!==undefined&&K!==null){if(L==="carate_range_of_lot"||L==="carate_range_of_packet"){if(K!==null&&K!==undefined){r.push(K);
A.push(I+"~"+J.value);
f[I+"~"+J.value]=K
}}else{u.push(K);
C.push(I+"~"+J.value);
m[I+"~"+J.value]=K
}}else{if(H==="MS"&&K!==undefined&&K!==null){l.push(K);
w.push(I+"~"+J.value);
E[I+"~"+J.value]=K
}else{if(H==="UMS"&&K!==undefined&&K!==null){D.push(K.toString().trim());
B.push(I+"~"+J.value);
h[I+"~"+J.value]=K.toString()
}else{if(H==="SE"&&K!==undefined&&K!==null){n.push(K);
v[I+"~"+J.value]=K;
s.push(I+"~"+J.value)
}else{if(H==="CB"&&K!==undefined&&K!==null){k[I+"~"+J.value]=K
}else{if(H==="DT"&&K!==undefined&&K!==null){z[I+"~"+J.value]=K
}else{if(H==="TF"&&K!==undefined&&K!==null){j.push(I);
t[I+"~"+J.value]=K
}else{if(H==="IMG"&&K!==undefined&&K!==null){x[I+"~"+J.value]=K
}else{if(H==="UPD"&&K!==undefined&&K!==null){g[I+"~"+J.value]=K
}}}}}}}}}}}});
var p=new Object();
p.Dropdown=u;
p.MultiSelect=l;
p.UserMultiSelect=D;
p.SubEntity=n;
p["Text field"]=j;
p.caratRange=r
}if(p!==null&&p!==undefined){c.retrieveCustomNamesOfComponentIds(p,function(I){var U=new Object();
var aw=JSON.parse(angular.toJson(I));
if(C.length>0){for(var ap=0;
ap<C.length;
ap++){var ad=m[C[ap]];
if(ad!==undefined&&ad!==null){var ag=aw[ad.toString()];
U[C[ap].toString()]=ag
}}}if(A.length>0){for(var ap=0;
ap<A.length;
ap++){var O=f[A[ap]];
var P=aw[O.toString()];
U[A[ap].toString()]=P
}}if(s.length>0){for(var ap=0;
ap<s.length;
ap++){var J=v[s[ap]];
var aa=aw[J.toString()];
U[s[ap].toString()]=aa
}}if(w.length>0){for(var an=0;
an<w.length;
an++){var ak=E[w[an]];
var T=[];
T=ak.split(",");
var Y="!";
for(var am=0;
am<T.length;
am++){Y+=aw[T[am].toString().trim()]+","
}var L=Y.substring(1,Y.toString().length-1);
U[w[an].toString()]=L
}}if(B.length>0){for(var ah=0;
ah<B.length;
ah++){var ar=h[B[ah]];
var H=[];
H=ar.split(",");
var F="!";
for(var al=0;
al<H.length;
al++){F+=aw[H[al].toString().trim()]+","
}var ac=F.substring(1,F.toString().length-1);
U[B[ah].toString()]=ac
}}if(k!==null){for(var W in k){if(k[W]===true||k[W]==="true"){k[W]="Yes"
}if(k[W]===false||k[W]==="false"){k[W]="No"
}U[W.toString()]=k[W]
}}if(z!==null){for(var aq in z){var S=new Date(z[aq]);
var V=S.getFullYear();
var X=S.getMonth()+1;
var aj=S.getDate();
var at=aj+"/"+X+"/"+V;
U[aq.toString()]=at
}}if(t!==null){for(var Z in t){var G=t[Z];
var N=[];
N=Z.split("~");
if(N[0] in aw){if(aw[N[0]]==="true"){if(b.viewEncryptedData===false){var av="~";
for(var an=0;
an<G.length;
an++){if(/\s/.test(G[an])){av+=" "
}else{av+="*"
}}av=av.toString().slice(1);
U[Z]=av
}}else{U[Z]=G
}}}}if(x!==null){for(var K in x){var ao=x[K];
if(ao!==undefined&&ao!==null){ai=ao.toString();
if(y){U[K]=ai
}else{console.log("in this>>>>");
var M=ao.lastIndexOf(")")+1;
var ae=ao.length;
if(M>-1&&ae>-1){var ai=ao.substring(M,ae);
U[K]=ai
}}}}}if(g!==null){for(var R in g){var Q=g[R];
var au=[];
var af="";
au=Q.toString().split(",");
if(y){for(var an=0;
an<au.length;
an++){af+=au[an]+" , "
}}else{for(var an=0;
an<au.length;
an++){var M=au[an].lastIndexOf("~~")+1;
var ae=au[an].length;
if(M>-1&&ae>-1){af+=au[an].substring(M,ae)+" , "
}}}var ab=af.replace(/,(?=[^,]*$)/,"");
U[R]=ab
}}angular.forEach(o,function(ax){var az=ax.categoryCustom;
for(var ay in az){if(az.hasOwnProperty(ay)){if(U[ay+"~"+ax.value]!==undefined){az[ay]=U[ay+"~"+ax.value]
}}}});
q(o)
})
}},convertSearchData:function(k,m,f,l,j){var g=new Object();
if(k!==null&&k!==undefined&&k.length>0){angular.forEach(k,function(o){g[o.model]=o.dbType;
g["Component@"+o.model]=o.type
})
}if(m!==null&&m!==undefined&&m.length>0){angular.forEach(m,function(o){g[o.model]=o.dbType;
g["Component@"+o.model]=o.type
})
}if(f!==null&&f!==undefined&&f.length>0){angular.forEach(f,function(o){g[o.model]=o.dbType;
g["Component@"+o.model]=o.type
})
}if(l!==null&&l!==undefined&&l.length>0){angular.forEach(k,function(o){g[o.model]=o.dbType;
g["Component@"+o.model]=o.type
})
}for(var n in j){if(!!g[n]&&g[n]==="Integer"){var h=parseInt(j[n]);
if(!!(h)){j[n]=angular.copy(h)
}}else{if(!!g[n]&&g[n]==="Double"){var h=parseFloat(j[n]);
if(!!(h)){j[n]=angular.copy(h)
}}else{if(!!g[n]&&g[n]==="Long"){var h=parseInt(j[n]);
if(!!(h)){j[n]=angular.copy(h)
}}else{if(g["Component@"+n]==="currency"){if(j[n]===undefined){j[n+"*CurrencyCode"]=""
}}}}}}return j
},retrieveCustomData:function(k,g){var j=[];
var h=[];
var f=Object.keys(g).map(function(l,m){angular.forEach(this[l],function(n){if(l!=="$promise"){h.push({parent:l,fieldId:n.fieldId,entityName:n.entityName,sequenceNo:n.sequenceNo,isEditable:n.isEditable,isRequired:n.isRequired})
}})
},g);
if(k){angular.forEach(k,function(l){angular.forEach(h,function(m){if(l.fieldId===m.fieldId){if(m.sequenceNo!=null){l.seq=m.sequenceNo
}else{l.seq=999
}if(!m.isEditable){l.isViewFromDesignation=true
}if(m.isRequired===true||m.isRequired==="true"){l.required=true
}else{l.required=false
}j.push(l)
}})
})
}j.sort(function(m,l){return(m.seq)-(l.seq)
});
return j
},retrieveSearchWiseCustomFieldInfo:function(g){var f=a.defer();
var h=null;
var j=[];
c.retrieveCustomFieldBySeachCriteria(g,function(p){var n=angular.fromJson(angular.toJson(p));
var l=[];
var r;
for(var m in n){o=encryptPass(localStorage.getItem(m.toString().toLowerCase()));
if(o!==null){r=true
}else{r=false;
break
}if(r){var q=JSON.parse(o);
var k=q.genralSection;
angular.forEach(n[m],function(u){var t=e("filter")(k,function(v){return u.label.toString()===v.model.toString().toString()
})[0];
var s=angular.copy(t);
if(!!s){if(s.type==="AutoGenerated"){s.placeholder=""
}else{if(s.type==="checkbox"){s.val=null
}}l.push(s)
}})
}}if(r){j.genralSection=l
}else{var o=null;
if(g){c.retrieveSearchField(g,function(s){if(s){f.resolve(s)
}});
return f.promise
}}f.resolve(j)
});
return f.promise
},retrieveSubEntities:function(h){var f=a.defer();
var g=null;
if(h){c.retrieveSubEntitiesByFieldId(h,function(j){if(j){var k=encryptPass(JSON.stringify(j));
f.resolve(j)
}});
return f.promise
}},mergeAndSortTemplates:function(){var f=[];
if(arguments!==undefined&&arguments!==null&&arguments.length>0){for(var g=0;
g<arguments.length;
g++){f=$.merge(f,arguments[g])
}}return f.sort(function(j,h){return j.seq-h.seq
})
}};
return c
}]);
var printableChars="~!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL:\"ZXCVBNM<>?`1234567890-=qwertyuiop[]\\asdfghjkl;'zxcvbnm,./";
var featureNames=["manageFranchise","manageLeaveWorkflow","manageAssets","manageHoliday","manageLocation","manageDepartment","manageFeature","manageLocales","manageDesignation","manageGoalSheet","manageNotifications","manageMessages","manageCustomField","manageTasks","manageMasters","manageShift","manageActivity","manageLeave","manageEmployees","manageEvents","applyLeave","invoice","parcel","lot","packet","plan","purchase"];
clearLocalStorage=function(){for(var a=0;
a<featureNames.length;
a++){localStorage.removeItem(featureNames[a])
}};
encryptPass=function(d){if(d!==null){var c="";
for(i=0;
i<d.length;
i++){var a=d.charAt(i);
var b=printableChars.indexOf(a);
if(b!==-1){b=printableChars.length-b;
a=printableChars.charAt(b);
c+=a
}else{c+=a
}}return c
}else{return d
}};