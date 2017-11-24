define(["angular"],function(){angular.module("hkg.directives").directive("dateValidate",["$compile","$filter","orderByFilter","$parse","dateFilter",function(b,e,a,c,d){return{restrict:"A",require:"ngModel",scope:true,link:function(n,i,k,j){if(k.validateFormat!==undefined&&k.validateFormat!==null&&k.validateFormat!==""){n.validateFormat=k.validateFormat
}else{n.validateFormat="dd/MM/yyyy hh:mm a"
}n.watchData={};
angular.forEach(["min","max"],function(p){if(k[p]){n.$watch(c(k[p]),function(q){n.watchData[p]=q?new Date(q):null
})
}else{}});
n.parsers={};
var h={yyyy:{regex:"\\d{4}",apply:function(p){this.year=+p
}},yy:{regex:"\\d{2}",apply:function(p){this.year=+p+2000
}},y:{regex:"\\d{1,4}",apply:function(p){this.year=+p
}},MM:{regex:"0[1-9]|1[0-2]",apply:function(p){this.month=p-1
}},M:{regex:"[1-9]|1[0-2]",apply:function(p){this.month=p-1
}},dd:{regex:"[0-2][0-9]{1}|3[0-1]{1}",apply:function(p){this.date=+p
}},d:{regex:"[1-2]?[0-9]{1}|3[0-1]{1}",apply:function(p){this.date=+p
}},hh:{regex:"0?[0-9]|1[0-2]",apply:function(p){this.hours=+p
}},mm:{regex:"0?[0-9]|[1-5][0-9]",apply:function(p){this.minutes=+p
}},a:{regex:"AM|am|PM|pm",apply:function(p){this.meridiem=p
}}};
function o(r){var q=[],p=r.split("");
angular.forEach(h,function(x,w){var u=r.indexOf(w);
var t=x.apply;
var s=x.regex;
if(w==="dd"){s="0?[1-2]?[0-9]{1}|3[0-1]{1}"
}if(w==="MM"){s="0?[1-9]|1[0-2]"
}if(w==="yyyy"){s="(?:[0-9]{2})?[0-9]{2}";
t=function(z){if(z.length===2){this.year=+z+2000
}else{this.year=+z
}}
}if(u>-1){r=r.split("");
p[u]="("+s+")";
r[u]="$";
for(var v=u+1,y=u+w.length;
v<y;
v++){p[v]="";
r[v]="$"
}r=r.join("");
q.push({index:u,apply:t})
}});
return{regex:"^"+p.join("")+"$",map:a(q,"index")}
}n.parse=function(y,z){if(!angular.isString(y)||!z){return y
}if(!n.parsers[z]){n.parsers[z]=o(z)
}var q=this.parsers[z],x=new RegExp(q.regex.toString().split(" ").join("\\s")),r=q.map,v=y.match(x);
if(v&&v.length){var w={year:1900,month:0,date:1,hours:0,minutes:0,meridiem:null},s;
for(var u=1,t=v.length;
u<t;
u++){var p=r[u-1];
if(p.apply){p.apply.call(w,v[u])
}}if(m(w.year,w.month,w.date,w.hours,w.meridiem)){if(w.meridiem!==null&&w.meridiem!==undefined){if((w.meridiem==="pm"||w.meridiem==="PM")&&w.hours<12){w.hours=w.hours+12
}if((w.meridiem==="am"||w.meridiem==="AM")&&w.hours===12){w.hours=0
}}s=new Date(w.year,w.month,w.date,w.hours,w.minutes)
}return s
}};
function m(s,t,r,p,q){if(q!==undefined&&q!==null&&p===0){return false
}if(r===0){return false
}if(t===1&&r>28){return r===29&&((s%4===0&&s%100!==0)||s%400===0)
}if(t===3||t===5||t===8||t===10){return r<31
}return true
}function f(q){j.$setValidity("min",true);
j.$setValidity("max",true);
j.$setValidity("date",true);
if(!q){j.$setValidity("date",true);
return null
}else{if(angular.isDate(q)&&!isNaN(q)){j.$setValidity("date",true);
if(n.watchData.min!==undefined&&q>=new Date(n.watchData.min).setHours(0,0,0,0)){j.$setValidity("min",true)
}if(n.watchData.max!==undefined&&q<=new Date(n.watchData.max).setHours(0,0,0,0)){j.$setValidity("max",true)
}return g(q,n.validateFormat)
}else{if(angular.isString(q)||!isNaN(q)){var p=n.parse(q,n.validateFormat);
if(isNaN(p)){j.$setValidity("date",false);
return null
}else{if(n.watchData.min&&p<new Date(n.watchData.min).setHours(0,0,0,0)){j.$setValidity("min",false);
return null
}if(n.watchData.max){var r=new Date(new Date(n.watchData.max).setHours(0,0,0,0));
r=r.setDate(r.getDate()+1);
if(p>=r){j.$setValidity("max",false);
return null
}}return g(p,n.validateFormat)
}}else{j.$setValidity("date",false);
return null
}}}}j.$parsers.unshift(f);
function l(p){j.$setValidity("min",true);
j.$setValidity("max",true);
j.$setValidity("date",true);
return p
}j.$formatters.unshift(l);
var g=function(p,q){if(q===undefined){q="dd/MM/yyyy"
}return e("date")(p,q)
}
}}
}])
});