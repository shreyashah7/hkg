angular.module("ui.bootstrap",["ui.bootstrap.tpls","ui.bootstrap.transition","ui.bootstrap.collapse","ui.bootstrap.accordion","ui.bootstrap.alert","ui.bootstrap.bindHtml","ui.bootstrap.buttons","ui.bootstrap.carousel","ui.bootstrap.dateparser","ui.bootstrap.position","ui.bootstrap.datepicker","ui.bootstrap.dropdown","ui.bootstrap.modal","ui.bootstrap.pagination","ui.bootstrap.tooltip","ui.bootstrap.popover","ui.bootstrap.progressbar","ui.bootstrap.rating","ui.bootstrap.tabs","ui.bootstrap.timepicker","ui.bootstrap.typeahead"]);
angular.module("ui.bootstrap.tpls",["template/accordion/accordion-group.html","template/accordion/accordion.html","template/alert/alert.html","template/carousel/carousel.html","template/carousel/slide.html","template/datepicker/datepicker.html","template/datepicker/day.html","template/datepicker/month.html","template/datepicker/popup.html","template/datepicker/year.html","template/modal/backdrop.html","template/modal/window.html","template/pagination/pager.html","template/pagination/pagination.html","template/tooltip/tooltip-html-unsafe-popup.html","template/tooltip/tooltip-popup.html","template/popover/popover.html","template/progressbar/bar.html","template/progressbar/progress.html","template/progressbar/progressbar.html","template/rating/rating.html","template/tabs/tab.html","template/tabs/tabset.html","template/timepicker/timepicker.html","template/typeahead/typeahead-match.html","template/typeahead/typeahead-popup.html"]);
angular.module("ui.bootstrap.transition",[]).factory("$transition",["$q","$timeout","$rootScope",function(d,e,c){var h=function(l,k,j){j=j||{};
var i=d.defer();
var n=h[j.animation?"animationEndEventName":"transitionEndEventName"];
var m=function(o){c.$apply(function(){l.unbind(n,m);
i.resolve(l)
})
};
if(n){l.bind(n,m)
}e(function(){if(angular.isString(k)){l.addClass(k)
}else{if(angular.isFunction(k)){k(l)
}else{if(angular.isObject(k)){l.css(k)
}}}if(!n){i.resolve(l)
}});
i.promise.cancel=function(){if(n){l.unbind(n,m)
}i.reject("Transition cancelled")
};
return i.promise
};
var f=document.createElement("trans");
var b={WebkitTransition:"webkitTransitionEnd",MozTransition:"transitionend",OTransition:"oTransitionEnd",transition:"transitionend"};
var g={WebkitTransition:"webkitAnimationEnd",MozTransition:"animationend",OTransition:"oAnimationEnd",transition:"animationend"};
function a(j){for(var i in j){if(f.style[i]!==undefined){return j[i]
}}}h.transitionEndEventName=a(b);
h.animationEndEventName=a(g);
return h
}]);
angular.module("ui.bootstrap.collapse",["ui.bootstrap.transition"]).directive("collapse",["$transition",function(a){return{link:function(i,d,h){var b=true;
var k;
function j(n){var l=a(d,n);
if(k){k.cancel()
}k=l;
l.then(m,m);
return l;
function m(){if(k===l){k=undefined
}}}function g(){if(b){b=false;
f()
}else{d.removeClass("collapse").addClass("collapsing");
j({height:d[0].scrollHeight+"px"}).then(f)
}}function f(){d.removeClass("collapsing");
d.addClass("collapse in");
d.css({height:"auto"})
}function e(){if(b){b=false;
c();
d.css({height:0})
}else{d.css({height:d[0].scrollHeight+"px"});
var l=d[0].offsetWidth;
d.removeClass("collapse in").addClass("collapsing");
j({height:0}).then(c)
}}function c(){d.removeClass("collapsing");
d.addClass("collapse")
}i.$watch(h.collapse,function(l){if(l){e()
}else{g()
}})
}}
}]);
angular.module("ui.bootstrap.accordion",["ui.bootstrap.collapse"]).constant("accordionConfig",{closeOthers:true}).controller("AccordionController",["$scope","$attrs","accordionConfig",function(c,a,b){this.groups=[];
this.closeOthers=function(e){var d=angular.isDefined(a.closeOthers)?c.$eval(a.closeOthers):b.closeOthers;
if(d){angular.forEach(this.groups,function(f){if(f!==e){f.isOpen=false
}})
}};
this.addGroup=function(e){var d=this;
this.groups.push(e);
e.$on("$destroy",function(f){d.removeGroup(e)
})
};
this.removeGroup=function(e){var d=this.groups.indexOf(e);
if(d!==-1){this.groups.splice(d,1)
}}
}]).directive("accordion",function(){return{restrict:"EA",controller:"AccordionController",transclude:true,replace:false,templateUrl:"template/accordion/accordion.html"}
}).directive("accordionGroup",function(){return{require:"^accordion",restrict:"EA",transclude:true,replace:true,templateUrl:"template/accordion/accordion-group.html",scope:{heading:"@",isOpen:"=?",isDisabled:"=?"},controller:function(){this.setHeading=function(a){this.heading=a
}
},link:function(d,b,a,c){c.addGroup(d);
d.$watch("isOpen",function(e){if(e){c.closeOthers(d)
}});
d.toggleOpen=function(){if(!d.isDisabled){d.isOpen=!d.isOpen
}}
}}
}).directive("accordionHeading",function(){return{restrict:"EA",transclude:true,template:"",replace:true,require:"^accordionGroup",link:function(d,c,a,e,b){e.setHeading(b(d,function(){}))
}}
}).directive("accordionTransclude",function(){return{require:"^accordionGroup",link:function(d,c,a,b){d.$watch(function(){return b[a.accordionTransclude]
},function(e){if(e){c.html("");
c.append(e)
}})
}}
});
angular.module("ui.bootstrap.alert",[]).controller("AlertController",["$scope","$attrs",function(b,a){b.closeable="close" in a
}]).directive("alert",function(){return{restrict:"EA",controller:"AlertController",templateUrl:"template/alert/alert.html",transclude:true,replace:true,scope:{type:"@",close:"&"}}
});
angular.module("ui.bootstrap.bindHtml",[]).directive("bindHtmlUnsafe",function(){return function(c,b,a){b.addClass("ng-binding").data("$binding",a.bindHtmlUnsafe);
c.$watch(a.bindHtmlUnsafe,function d(e){b.html(e||"")
})
}
});
angular.module("ui.bootstrap.buttons",[]).constant("buttonConfig",{activeClass:"active",toggleEvent:"click"}).controller("ButtonsController",["buttonConfig",function(a){this.activeClass=a.activeClass||"active";
this.toggleEvent=a.toggleEvent||"click"
}]).directive("btnRadio",function(){return{require:["btnRadio","ngModel"],controller:"ButtonsController",link:function(f,e,d,b){var a=b[0],c=b[1];
c.$render=function(){e.toggleClass(a.activeClass,angular.equals(c.$modelValue,f.$eval(d.btnRadio)))
};
e.bind(a.toggleEvent,function(){var g=e.hasClass(a.activeClass);
if(!g||angular.isDefined(d.uncheckable)){f.$apply(function(){c.$setViewValue(g?null:f.$eval(d.btnRadio));
c.$render()
})
}})
}}
}).directive("btnCheckbox",function(){return{require:["btnCheckbox","ngModel"],controller:"ButtonsController",link:function(h,f,g,e){var c=e[0],b=e[1];
function d(){return a(g.btnCheckboxTrue,true)
}function i(){return a(g.btnCheckboxFalse,false)
}function a(k,j){var l=h.$eval(k);
return angular.isDefined(l)?l:j
}b.$render=function(){f.toggleClass(c.activeClass,angular.equals(b.$modelValue,d()))
};
f.bind(c.toggleEvent,function(){h.$apply(function(){b.$setViewValue(f.hasClass(c.activeClass)?i():d());
b.$render()
})
})
}}
});
angular.module("ui.bootstrap.carousel",["ui.bootstrap.transition"]).controller("CarouselController",["$scope","$timeout","$transition",function(i,b,d){var k=this,a=k.slides=i.slides=[],f=-1,h,e;
k.currentSlide=null;
var j=false;
k.select=i.select=function(q,p){var m=a.indexOf(q);
if(p===undefined){p=m>f?"next":"prev"
}if(q&&q!==k.currentSlide){if(i.$currentTransition){i.$currentTransition.cancel();
b(o)
}else{o()
}}function o(){if(j){return
}if(k.currentSlide&&angular.isString(p)&&!i.noTransition&&q.$element){q.$element.addClass(p);
var r=q.$element[0].offsetWidth;
angular.forEach(a,function(s){angular.extend(s,{direction:"",entering:false,leaving:false,active:false})
});
angular.extend(q,{direction:p,active:true,entering:true});
angular.extend(k.currentSlide||{},{direction:p,leaving:true});
i.$currentTransition=d(q.$element,{});
(function(s,t){i.$currentTransition.then(function(){n(s,t)
},function(){n(s,t)
})
}(q,k.currentSlide))
}else{n(q,k.currentSlide)
}k.currentSlide=q;
f=m;
g()
}function n(r,s){angular.extend(r,{direction:"",active:true,leaving:false,entering:false});
angular.extend(s||{},{direction:"",active:false,leaving:false,entering:false});
i.$currentTransition=null
}};
i.$on("$destroy",function(){j=true
});
k.indexOfSlide=function(m){return a.indexOf(m)
};
i.next=function(){var m=(f+1)%a.length;
if(!i.$currentTransition){return k.select(a[m],"next")
}};
i.prev=function(){var m=f-1<0?a.length-1:f-1;
if(!i.$currentTransition){return k.select(a[m],"prev")
}};
i.isActive=function(m){return k.currentSlide===m
};
i.$watch("interval",g);
i.$on("$destroy",c);
function g(){c();
var m=+i.interval;
if(!isNaN(m)&&m>=0){h=b(l,m)
}}function c(){if(h){b.cancel(h);
h=null
}}function l(){if(e){i.next();
g()
}else{i.pause()
}}i.play=function(){if(!e){e=true;
g()
}};
i.pause=function(){if(!i.noPause){e=false;
c()
}};
k.addSlide=function(m,n){m.$element=n;
a.push(m);
if(a.length===1||m.active){k.select(a[a.length-1]);
if(a.length==1){i.play()
}}else{m.active=false
}};
k.removeSlide=function(m){var n=a.indexOf(m);
a.splice(n,1);
if(a.length>0&&m.active){if(n>=a.length){k.select(a[n-1])
}else{k.select(a[n])
}}else{if(f>n){f--
}}}
}]).directive("carousel",[function(){return{restrict:"EA",transclude:true,replace:true,controller:"CarouselController",require:"carousel",templateUrl:"template/carousel/carousel.html",scope:{interval:"=",noTransition:"=",noPause:"="}}
}]).directive("slide",function(){return{require:"^carousel",restrict:"EA",transclude:true,replace:true,templateUrl:"template/carousel/slide.html",scope:{active:"=?"},link:function(c,b,a,d){d.addSlide(c,b);
c.$on("$destroy",function(){d.removeSlide(c)
});
c.$watch("active",function(e){if(e){d.select(c)
}})
}}
});
angular.module("ui.bootstrap.dateparser",[]).service("dateParser",["$locale","orderByFilter",function(a,c){this.parsers={};
var b={yyyy:{regex:"\\d{4}",apply:function(g){this.year=+g
}},yy:{regex:"\\d{2}",apply:function(g){this.year=+g+2000
}},y:{regex:"\\d{1,4}",apply:function(g){this.year=+g
}},MMMM:{regex:a.DATETIME_FORMATS.MONTH.join("|"),apply:function(g){this.month=a.DATETIME_FORMATS.MONTH.indexOf(g)
}},MMM:{regex:a.DATETIME_FORMATS.SHORTMONTH.join("|"),apply:function(g){this.month=a.DATETIME_FORMATS.SHORTMONTH.indexOf(g)
}},MM:{regex:"0[1-9]|1[0-2]",apply:function(g){this.month=g-1
}},M:{regex:"[1-9]|1[0-2]",apply:function(g){this.month=g-1
}},dd:{regex:"[0-2][0-9]{1}|3[0-1]{1}",apply:function(g){this.date=+g
}},d:{regex:"[1-2]?[0-9]{1}|3[0-1]{1}",apply:function(g){this.date=+g
}},EEEE:{regex:a.DATETIME_FORMATS.DAY.join("|")},EEE:{regex:a.DATETIME_FORMATS.SHORTDAY.join("|")}};
function e(i){var h=[],g=i.split("");
angular.forEach(b,function(m,l){var j=i.indexOf(l);
if(j>-1){i=i.split("");
g[j]="("+m.regex+")";
i[j]="$";
for(var k=j+1,o=j+l.length;
k<o;
k++){g[k]="";
i[k]="$"
}i=i.join("");
h.push({index:j,apply:m.apply})
}});
return{regex:new RegExp("^"+g.join("")+"$"),map:c(h,"index")}
}function f(i){var h=[],g=i.split("");
angular.forEach(b,function(p,o){var l=i.indexOf(o);
var k=p.apply;
var j=p.regex;
if(o==="dd"){j="0?[1-2]?[0-9]{1}|3[0-1]{1}"
}if(o==="MM"){j="0?[1-9]|1[0-2]"
}if(o==="yyyy"){j="(?:[0-9]{2})?[0-9]{2}";
k=function(n){if(n.length===2){this.year=+n+2000
}else{this.year=+n
}}
}if(l>-1){i=i.split("");
g[l]="("+j+")";
i[l]="$";
for(var m=l+1,q=l+o.length;
m<q;
m++){g[m]="";
i[m]="$"
}i=i.join("");
h.push({index:l,apply:k})
}});
return{regex:new RegExp("^"+g.join("")+"$"),map:c(h,"index")}
}this.parse=function(r,s){if(!angular.isString(r)||!s){return r
}s=a.DATETIME_FORMATS[s]||s;
if(!this.parsers[s]){this.parsers[s]=f(s)
}var h=this.parsers[s],q=h.regex,j=h.map,o=r.match(q);
if(o&&o.length){var p={year:1900,month:0,date:1,hours:0},k;
for(var m=1,l=o.length;
m<l;
m++){var g=j[m-1];
if(g.apply){g.apply.call(p,o[m])
}}if(d(p.year,p.month,p.date)){k=new Date(p.year,p.month,p.date,p.hours)
}return k
}};
function d(h,i,g){if(i===1&&g>28){return g===29&&((h%4===0&&h%100!==0)||h%400===0)
}if(i===3||i===5||i===8||i===10){return g<31
}return true
}}]);
angular.module("ui.bootstrap.position",[]).factory("$position",["$document","$window",function(d,c){function a(f,g){if(f.currentStyle){return f.currentStyle[g]
}else{if(c.getComputedStyle){return c.getComputedStyle(f)[g]
}}return f.style[g]
}function e(f){return(a(f,"position")||"static")==="static"
}var b=function(g){var f=d[0];
var h=g.offsetParent||f;
while(h&&h!==f&&e(h)){h=h.offsetParent
}return h||f
};
return{position:function(g){var f=this.offset(g);
var j={top:0,left:0};
var h=b(g[0]);
if(h!=d[0]){j=this.offset(angular.element(h));
j.top+=h.clientTop-h.scrollTop;
j.left+=h.clientLeft-h.scrollLeft
}var i=g[0].getBoundingClientRect();
return{width:i.width||g.prop("offsetWidth"),height:i.height||g.prop("offsetHeight"),top:f.top-j.top,left:f.left-j.left}
},offset:function(f){var g=f[0].getBoundingClientRect();
return{width:g.width||f.prop("offsetWidth"),height:g.height||f.prop("offsetHeight"),top:g.top+(c.pageYOffset||d[0].documentElement.scrollTop),left:g.left+(c.pageXOffset||d[0].documentElement.scrollLeft)}
},positionElements:function(p,r,k,n){var i=k.split("-");
var q=i[0],o=i[1]||"center";
var l,f,h,j;
l=n?this.offset(p):this.position(p);
f=r.prop("offsetWidth");
h=r.prop("offsetHeight");
var g={center:function(){return l.left+l.width/2-f/2
},left:function(){return l.left
},right:function(){return l.left+l.width
}};
var m={center:function(){return l.top+l.height/2-h/2
},top:function(){return l.top
},bottom:function(){return l.top+l.height
}};
switch(q){case"right":j={top:m[o](),left:g[q]()};
break;
case"left":j={top:m[o](),left:l.left-f};
break;
case"bottom":j={top:m[q](),left:g[o]()};
break;
default:j={top:l.top-h,left:g[o]()};
break
}return j
}}
}]);
angular.module("ui.bootstrap.datepicker",["ui.bootstrap.dateparser","ui.bootstrap.position"]).constant("datepickerConfig",{formatDay:"dd",formatMonth:"MMMM",formatYear:"yyyy",formatDayHeader:"EEE",formatDayTitle:"MMMM yyyy",formatMonthTitle:"yyyy",datepickerMode:"day",minMode:"day",maxMode:"year",showWeeks:true,startingDay:0,yearRange:20,min:null,max:null}).controller("DatepickerController",["$scope","$attrs","$parse","$interpolate","$timeout","$log","dateFilter","datepickerConfig",function(h,g,c,f,b,k,e,d){var i=this,a={$setViewValue:angular.noop};
this.modes=["day","month","year"];
angular.forEach(["formatDay","formatMonth","formatYear","formatDayHeader","formatDayTitle","formatMonthTitle","minMode","maxMode","showWeeks","startingDay","yearRange"],function(m,l){i[m]=angular.isDefined(g[m])?(l<8?f(g[m])(h.$parent.$parent):h.$parent.$parent.$eval(g[m])):d[m]
});
angular.forEach(["min","max"],function(l){if(g[l]){h.$parent.$parent.$watch(c(g[l]),function(m){i[l]=m?new Date(m):null;
i.refreshView()
})
}else{i[l]=d[l]?new Date(d[l]):null
}});
h.datepickerMode=h.datepickerMode||d.datepickerMode;
h.uniqueId="datepicker-"+h.$id+"-"+Math.floor(Math.random()*10000);
this.activeDate=angular.isDefined(g.initDate)?h.$parent.$parent.$eval(g.initDate):new Date();
h.isActive=function(l){if(i.compare(l.date,i.activeDate)===0){h.activeDateId=l.uid;
return true
}return false
};
this.init=function(l){a=l;
a.$render=function(){i.render()
}
};
this.render=function(){if(a.$modelValue){var l=new Date(a.$modelValue),m=!isNaN(l);
if(m){this.activeDate=l
}else{k.error('Datepicker directive: "ng-model" value must be a Date object, a number of milliseconds since 01.01.1970 or a string representing an RFC2822 or ISO 8601 date.')
}a.$setValidity("date",m)
}else{this.activeDate=angular.isDefined(g.initDate)?h.$parent.$parent.$eval(g.initDate):new Date()
}this.refreshView()
};
this.refreshView=function(){if(this.element){this._refreshView();
var l=a.$modelValue?new Date(a.$modelValue):null;
a.$setValidity("date-disabled",!l||(this.element&&!this.isDisabled(l)))
}};
this.createDateObject=function(m,n){var l=a.$modelValue?new Date(a.$modelValue):null;
return{date:m,label:e(m,n),selected:l&&this.compare(m,l)===0,disabled:this.isDisabled(m),current:this.compare(m,new Date())===0}
};
this.isDisabled=function(l){return((this.min&&this.compare(l,this.min)<0)||(this.max&&this.compare(l,this.max)>0)||(g.dateDisabled&&h.dateDisabled({date:l,mode:h.datepickerMode})))
};
this.split=function(l,m){var n=[];
while(l.length>0){n.push(l.splice(0,m))
}return n
};
h.select=function(l){if(h.datepickerMode===i.minMode){var m=a.$modelValue?new Date(a.$modelValue):new Date(0,0,0,0,0,0,0);
m.setFullYear(l.getFullYear(),l.getMonth(),l.getDate());
a.$setViewValue(m);
a.$render()
}else{i.activeDate=l;
h.datepickerMode=i.modes[i.modes.indexOf(h.datepickerMode)-1]
}};
h.move=function(n){var l=i.activeDate.getFullYear()+n*(i.step.years||0),m=i.activeDate.getMonth()+n*(i.step.months||0);
i.activeDate.setFullYear(l,m,1);
i.refreshView()
};
h.toggleMode=function(l){l=l||1;
if((h.datepickerMode===i.maxMode&&l===1)||(h.datepickerMode===i.minMode&&l===-1)){return
}h.datepickerMode=i.modes[i.modes.indexOf(h.datepickerMode)+l]
};
h.keys={13:"enter",32:"space",33:"pageup",34:"pagedown",35:"end",36:"home",37:"left",38:"up",39:"right",40:"down"};
var j=function(){b(function(){i.element[0].focus()
},0,false)
};
h.$on("datepicker.focus",j);
h.keydown=function(l){var m=h.keys[l.which];
if(!m||l.shiftKey||l.altKey){return
}l.preventDefault();
l.stopPropagation();
if(m==="enter"||m==="space"){if(i.isDisabled(i.activeDate)){return
}h.select(i.activeDate);
j()
}else{if(l.ctrlKey&&(m==="up"||m==="down")){h.toggleMode(m==="up"?1:-1);
j()
}else{i.handleKeyDown(m,l);
i.refreshView()
}}}
}]).directive("datepicker",function(){return{restrict:"EA",replace:true,templateUrl:"template/datepicker/datepicker.html",scope:{datepickerMode:"=?",dateDisabled:"&",isOpen:"=?"},require:["datepicker","?^ngModel"],controller:"DatepickerController",link:function(e,d,c,a){var f=a[0],b=a[1];
if(b){f.init(b)
}}}
}).directive("daypicker",["dateFilter",function(a){return{restrict:"EA",replace:true,templateUrl:"template/datepicker/day.html",require:"^datepicker",link:function(f,e,c,g){f.showWeeks=g.showWeeks;
g.step={months:1};
g.element=e;
var i=[31,28,31,30,31,30,31,31,30,31,30,31];
function d(j,k){return((k===1)&&(j%4===0)&&((j%100!==0)||(j%400===0)))?29:i[k]
}function b(j,o){var m=new Array(o),l=new Date(j),k=0;
l.setHours(12);
while(k<o){m[k++]=new Date(l);
l.setDate(l.getDate()+1)
}return m
}g._refreshView=function(){var r=g.activeDate.getFullYear(),q=g.activeDate.getMonth(),s=new Date(r,q,1),n=g.startingDay-s.getDay(),u=(n>0)?7-n:-n,l=new Date(s);
if(u>0){l.setDate(-u+1)
}var t=b(l,42);
for(var p=0;
p<42;
p++){t[p]=angular.extend(g.createDateObject(t[p],g.formatDay),{secondary:t[p].getMonth()!==q,uid:f.uniqueId+"-"+p})
}f.labels=new Array(7);
for(var o=0;
o<7;
o++){f.labels[o]={abbr:a(t[o].date,g.formatDayHeader),full:a(t[o].date,"EEEE")}
}f.title=a(g.activeDate,g.formatDayTitle);
f.rows=g.split(t,7);
if(f.showWeeks){f.weekNumbers=[];
var k=h(f.rows[0][0].date),m=f.rows.length;
while(f.weekNumbers.push(k++)<m){}}};
g.compare=function(k,j){return(new Date(k.getFullYear(),k.getMonth(),k.getDate())-new Date(j.getFullYear(),j.getMonth(),j.getDate()))
};
function h(j){var l=new Date(j);
l.setDate(l.getDate()+4-(l.getDay()||7));
var k=l.getTime();
l.setMonth(0);
l.setDate(1);
return Math.floor(Math.round((k-l)/86400000)/7)+1
}g.handleKeyDown=function(l,j){var k=g.activeDate.getDate();
if(l==="left"){k=k-1
}else{if(l==="up"){k=k-7
}else{if(l==="right"){k=k+1
}else{if(l==="down"){k=k+7
}else{if(l==="pageup"||l==="pagedown"){var m=g.activeDate.getMonth()+(l==="pageup"?-1:1);
g.activeDate.setMonth(m,1);
k=Math.min(d(g.activeDate.getFullYear(),g.activeDate.getMonth()),k)
}else{if(l==="home"){k=1
}else{if(l==="end"){k=d(g.activeDate.getFullYear(),g.activeDate.getMonth())
}}}}}}}g.activeDate.setDate(k)
};
g.refreshView()
}}
}]).directive("monthpicker",["dateFilter",function(a){return{restrict:"EA",replace:true,templateUrl:"template/datepicker/month.html",require:"^datepicker",link:function(d,c,b,e){e.step={years:1};
e.element=c;
e._refreshView=function(){var f=new Array(12),h=e.activeDate.getFullYear();
for(var g=0;
g<12;
g++){f[g]=angular.extend(e.createDateObject(new Date(h,g,1),e.formatMonth),{uid:d.uniqueId+"-"+g})
}d.title=a(e.activeDate,e.formatMonthTitle);
d.rows=e.split(f,3)
};
e.compare=function(g,f){return new Date(g.getFullYear(),g.getMonth())-new Date(f.getFullYear(),f.getMonth())
};
e.handleKeyDown=function(h,f){var g=e.activeDate.getMonth();
if(h==="left"){g=g-1
}else{if(h==="up"){g=g-3
}else{if(h==="right"){g=g+1
}else{if(h==="down"){g=g+3
}else{if(h==="pageup"||h==="pagedown"){var i=e.activeDate.getFullYear()+(h==="pageup"?-1:1);
e.activeDate.setFullYear(i)
}else{if(h==="home"){g=0
}else{if(h==="end"){g=11
}}}}}}}e.activeDate.setMonth(g)
};
e.refreshView()
}}
}]).directive("yearpicker",["dateFilter",function(a){return{restrict:"EA",replace:true,templateUrl:"template/datepicker/year.html",require:"^datepicker",link:function(e,d,c,f){var b=f.yearRange;
f.step={years:b};
f.element=d;
function g(h){return parseInt((h-1)/b,10)*b+1
}f._refreshView=function(){var j=new Array(b);
for(var h=0,k=g(f.activeDate.getFullYear());
h<b;
h++){j[h]=angular.extend(f.createDateObject(new Date(k+h,0,1),f.formatYear),{uid:e.uniqueId+"-"+h})
}e.title=[j[0].label,j[b-1].label].join(" - ");
e.rows=f.split(j,5)
};
f.compare=function(i,h){return i.getFullYear()-h.getFullYear()
};
f.handleKeyDown=function(j,h){var i=f.activeDate.getFullYear();
if(j==="left"){i=i-1
}else{if(j==="up"){i=i-5
}else{if(j==="right"){i=i+1
}else{if(j==="down"){i=i+5
}else{if(j==="pageup"||j==="pagedown"){i+=(j==="pageup"?-1:1)*f.step.years
}else{if(j==="home"){i=g(f.activeDate.getFullYear())
}else{if(j==="end"){i=g(f.activeDate.getFullYear())+b-1
}}}}}}}f.activeDate.setFullYear(i)
};
f.refreshView()
}}
}]).constant("datepickerPopupConfig",{datepickerPopup:"yyyy-MM-dd",currentText:"Today",clearText:"Clear",closeText:"Done",closeOnDateSelection:true,appendToBody:false,showButtonBar:true}).directive("datepickerPopup",["$compile","$parse","$document","$position","dateFilter","dateParser","datepickerPopupConfig",function(b,c,f,e,d,a,g){return{restrict:"EA",require:"ngModel",scope:{isOpen:"=?",currentText:"@",clearText:"@",closeText:"@",dateDisabled:"&"},link:function(u,p,t,o){var k,q=angular.isDefined(t.closeOnDateSelection)?u.$parent.$eval(t.closeOnDateSelection):g.closeOnDateSelection,s=angular.isDefined(t.datepickerAppendToBody)?u.$parent.$eval(t.datepickerAppendToBody):g.appendToBody;
u.showButtonBar=angular.isDefined(t.showButtonBar)?u.$parent.$eval(t.showButtonBar):g.showButtonBar;
u.popup={};
u.getText=function(v){return u[v+"Text"]||g[v+"Text"]
};
t.$observe("datepickerPopup",function(v){k=v||g.datepickerPopup;
o.$render()
});
var l=angular.element('<div datepicker-popup-wrap><div datepicker is-open="isOpen"></div></div>');
l.attr({"ng-model":"popup.date","ng-change":"dateSelection()"});
function m(v){return v.replace(/([A-Z])/g,function(w){return"-"+w.toLowerCase()
})
}var i=angular.element(l.children()[0]);
if(t.datepickerOptions){angular.forEach(u.$parent.$eval(t.datepickerOptions),function(w,v){i.attr(m(v),w)
})
}u.watchData={};
angular.forEach(["min","max","datepickerMode"],function(w){if(t[w]){var v=c(t[w]);
u.$parent.$watch(v,function(y){u.watchData[w]=y
});
i.attr(m(w),"watchData."+w);
if(w==="datepickerMode"){var x=v.assign;
u.$watch("watchData."+w,function(y,z){if(y!==z){x(u.$parent,y)
}})
}}});
if(t.dateDisabled){i.attr("date-disabled","dateDisabled({ date: date, mode: mode })")
}function j(w){o.$setValidity("min",true);
o.$setValidity("max",true);
o.$setValidity("date",true);
if(!w){o.$setValidity("date",true);
return undefined
}else{if(angular.isDate(w)&&!isNaN(w)){o.$setValidity("date",true);
if(u.watchData.min!==undefined&&w>=new Date(u.watchData.min).setHours(0,0,0,0)){o.$setValidity("min",true)
}if(u.watchData.max!==undefined&&w<=new Date(u.watchData.max).setHours(0,0,0,0)){o.$setValidity("max",true)
}return w
}else{if(angular.isString(w)||!isNaN(w)){var v=a.parse(w,k);
if(isNaN(v)){o.$setValidity("date",false);
return undefined
}else{if(u.watchData.min&&v<new Date(u.watchData.min).setHours(0,0,0,0)){o.$setValidity("min",false);
return undefined
}if(u.watchData.max){var x=new Date(new Date(u.watchData.max).setHours(0,0,0,0));
x=x.setDate(x.getDate()+1);
if(v>x){o.$setValidity("max",false);
return undefined
}}return v
}}else{o.$setValidity("date",false);
return undefined
}}}}o.$parsers.unshift(j);
u.dateSelection=function(v){if(angular.isDefined(v)){u.popup.date=v
}o.$setViewValue(u.popup.date);
o.$render();
if(q){u.isOpen=false;
p[0].focus()
}};
p.bind("input change keyup",function(){u.$apply(function(){u.popup.date=o.$modelValue
})
});
o.$render=function(){var v=o.$viewValue?d(o.$viewValue,k):"";
p.val(v);
u.popup.date=j(o.$modelValue)
};
var r=function(v){if(u.isOpen&&v.target!==p[0]){u.$apply(function(){u.isOpen=false
})
}};
var n=function(w,v){u.keydown(w)
};
p.bind("keydown",n);
u.keydown=function(v){if(v.which===27){v.preventDefault();
v.stopPropagation();
u.close()
}else{if(v.which===40&&!u.isOpen){u.isOpen=true
}}};
u.$watch("isOpen",function(v){if(v){u.$broadcast("datepicker.focus");
u.position=s?e.offset(p):e.position(p);
u.position.top=u.position.top+p.prop("offsetHeight");
f.bind("click",r)
}else{f.unbind("click",r)
}});
u.select=function(w){if(w==="today"){var v=new Date();
if(angular.isDate(o.$modelValue)){w=new Date(o.$modelValue);
w.setFullYear(v.getFullYear(),v.getMonth(),v.getDate())
}else{w=new Date(v.setHours(0,0,0,0))
}}u.dateSelection(w)
};
u.close=function(){u.isOpen=false;
p[0].focus()
};
var h=b(l)(u);
l.remove();
if(s){f.find("body").append(h)
}else{p.after(h)
}u.$on("$destroy",function(){h.next().remove();
p.unbind("keydown",n);
f.unbind("click",r)
})
}}
}]).directive("datepickerPopupWrap",function(){return{restrict:"EA",replace:true,transclude:true,templateUrl:"template/datepicker/popup.html",link:function(c,b,a){b.bind("click",function(d){d.preventDefault();
d.stopPropagation()
})
}}
});
angular.module("ui.bootstrap.dropdown",[]).constant("dropdownConfig",{openClass:"open"}).service("dropdownService",["$document",function(c){var a=null;
this.open=function(e){if(!a){c.bind("click",d);
c.bind("keydown",b)
}if(a&&a!==e){a.isOpen=false
}a=e
};
this.close=function(e){if(a===e){a=null;
c.unbind("click",d);
c.unbind("keydown",b)
}};
var d=function(e){var f=a.getToggleElement();
if(e&&f&&f[0].contains(e.target)){return
}a.$apply(function(){a.isOpen=false
})
};
var b=function(e){if(e.which===27){a.focusToggleElement();
d()
}}
}]).controller("DropdownController",["$scope","$attrs","$parse","dropdownConfig","dropdownService","$animate",function(j,i,a,b,d,f){var l=this,k=j.$new(),c=b.openClass,e,h=angular.noop,g=i.onToggle?a(i.onToggle):angular.noop;
this.init=function(m){l.$element=m;
if(i.isOpen){e=a(i.isOpen);
h=e.assign;
j.$watch(e,function(n){k.isOpen=!!n
})
}};
this.toggle=function(m){return k.isOpen=arguments.length?!!m:!k.isOpen
};
this.isOpen=function(){return k.isOpen
};
k.getToggleElement=function(){return l.toggleElement
};
k.focusToggleElement=function(){if(l.toggleElement){l.toggleElement[0].focus()
}};
k.$watch("isOpen",function(m,n){f[m?"addClass":"removeClass"](l.$element,c);
if(m){k.focusToggleElement();
d.open(k)
}else{d.close(k)
}h(j,m);
if(angular.isDefined(m)&&m!==n){g(j,{open:!!m})
}});
j.$on("$locationChangeSuccess",function(){k.isOpen=false
});
j.$on("$destroy",function(){k.$destroy()
})
}]).directive("dropdown",function(){return{restrict:"CA",controller:"DropdownController",link:function(d,c,b,a){a.init(c)
}}
}).directive("dropdownToggle",function(){return{restrict:"CA",require:"?^dropdown",link:function(e,c,b,a){if(!a){return
}a.toggleElement=c;
var d=function(f){f.preventDefault();
if(!c.hasClass("disabled")&&!b.disabled){e.$apply(function(){a.toggle()
})
}};
c.bind("click",d);
c.attr({"aria-haspopup":true,"aria-expanded":false});
e.$watch(a.isOpen,function(f){c.attr("aria-expanded",!!f)
});
e.$on("$destroy",function(){c.unbind("click",d)
})
}}
});
angular.module("ui.bootstrap.modal",["ui.bootstrap.transition"]).factory("$$stackedMap",function(){return{createNew:function(){var a=[];
return{add:function(b,c){a.push({key:b,value:c})
},get:function(c){for(var b=0;
b<a.length;
b++){if(c==a[b].key){return a[b]
}}},keys:function(){var c=[];
for(var b=0;
b<a.length;
b++){c.push(a[b].key)
}return c
},top:function(){return a[a.length-1]
},remove:function(d){var b=-1;
for(var c=0;
c<a.length;
c++){if(d==a[c].key){b=c;
break
}}return a.splice(b,1)[0]
},removeTop:function(){return a.splice(a.length-1,1)[0]
},length:function(){return a.length
}}
}}
}).directive("modalBackdrop",["$timeout",function(a){return{restrict:"EA",replace:true,templateUrl:"template/modal/backdrop.html",link:function(d,c,b){d.backdropClass=b.backdropClass||"";
d.animate=false;
a(function(){d.animate=true
})
}}
}]).directive("modalWindow",["$modalStack","$timeout",function(b,a){return{restrict:"EA",scope:{index:"@",animate:"="},replace:true,transclude:true,templateUrl:function(c,d){return d.templateUrl||"template/modal/window.html"
},link:function(e,d,c){d.addClass(c.windowClass||"");
e.size=c.size;
a(function(){e.animate=true;
if(!d[0].querySelectorAll("[autofocus]").length){d[0].focus()
}});
e.close=function(f){var g=b.getTop();
if(g&&g.value.backdrop&&g.value.backdrop!="static"&&(f.target===f.currentTarget)){f.preventDefault();
f.stopPropagation();
b.dismiss(g.key,"backdrop click")
}}
}}
}]).directive("modalTransclude",function(){return{link:function(e,d,a,c,b){b(e.$parent,function(f){d.empty();
d.append(f)
})
}}
}).factory("$modalStack",["$transition","$timeout","$document","$compile","$rootScope","$$stackedMap",function(d,b,e,h,l,a){var i="modal-open";
var k,f;
var c=a.createNew();
var j={};
function o(){var p=-1;
var r=c.keys();
for(var q=0;
q<r.length;
q++){if(c.get(r[q]).value.backdrop){p=q
}}return p
}l.$watch(o,function(p){if(f){f.index=p
}});
function n(q){var p=e.find("body").eq(0);
var r=c.get(q).value;
c.remove(q);
m(r.modalDomEl,r.modalScope,300,function(){r.modalScope.$destroy();
p.toggleClass(i,c.length()>0);
g()
})
}function g(){if(k&&o()==-1){var p=f;
m(k,f,150,function(){p.$destroy();
p=null
});
k=undefined;
f=undefined
}}function m(q,r,v,p){r.animate=false;
var u=d.transitionEndEventName;
if(u){var s=b(t,v);
q.bind(u,function(){b.cancel(s);
t();
r.$apply()
})
}else{b(t)
}function t(){if(t.done){return
}t.done=true;
q.remove();
if(p){p()
}}}e.bind("keydown",function(p){var q;
if(p.which===27){q=c.top();
if(q&&q.value.keyboard){p.preventDefault();
l.$apply(function(){j.dismiss(q.key,"escape key press")
})
}}});
j.open=function(q,t){c.add(q,{deferred:t.deferred,modalScope:t.scope,backdrop:t.backdrop,keyboard:t.keyboard});
var p=e.find("body").eq(0),s=o();
if(s>=0&&!k){f=l.$new(true);
f.index=s;
var r=angular.element("<div modal-backdrop></div>");
r.attr("backdrop-class",t.backdropClass);
k=h(r)(f);
p.append(k)
}var v=angular.element("<div modal-window></div>");
v.attr({"template-url":t.windowTemplateUrl,"window-class":t.windowClass,size:t.size,index:c.length()-1,animate:"animate"}).html(t.content);
var u=h(v)(t.scope);
c.top().value.modalDomEl=u;
p.append(u);
p.addClass(i)
};
j.close=function(q,p){var r=c.get(q);
if(r){r.value.deferred.resolve(p);
n(q)
}};
j.dismiss=function(p,q){var r=c.get(p);
if(r){r.value.deferred.reject(q);
n(p)
}};
j.dismissAll=function(q){var p=this.getTop();
while(p){this.dismiss(p.key,q);
p=this.getTop()
}};
j.getTop=function(){return c.top()
};
return j
}]).provider("$modal",function(){var a={options:{backdrop:true,keyboard:true},$get:["$injector","$rootScope","$q","$http","$templateCache","$controller","$modalStack",function(j,h,d,f,c,b,g){var i={};
function e(l){return l.template?d.when(l.template):f.get(angular.isFunction(l.templateUrl)?(l.templateUrl)():l.templateUrl,{cache:c}).then(function(m){return m.data
})
}function k(m){var l=[];
angular.forEach(m,function(n){if(angular.isFunction(n)||angular.isArray(n)){l.push(d.when(j.invoke(n)))
}});
return l
}i.open=function(m){var r=d.defer();
var p=d.defer();
var l={result:r.promise,opened:p.promise,close:function(s){g.close(l,s)
},dismiss:function(s){g.dismiss(l,s)
}};
m=angular.extend({},a.options,m);
m.resolve=m.resolve||{};
if(!m.template&&!m.templateUrl){throw new Error("One of template or templateUrl options is required.")
}var q=d.all([e(m)].concat(k(m.resolve)));
q.then(function o(w){var t=(m.scope||h).$new();
t.$close=l.close;
t.$dismiss=l.dismiss;
var v,u={};
var s=1;
if(m.controller){u.$scope=t;
u.$modalInstance=l;
angular.forEach(m.resolve,function(y,x){u[x]=w[s++]
});
v=b(m.controller,u);
if(m.controllerAs){t[m.controllerAs]=v
}}g.open(l,{scope:t,deferred:r,content:w[0],backdrop:m.backdrop,keyboard:m.keyboard,backdropClass:m.backdropClass,windowClass:m.windowClass,windowTemplateUrl:m.windowTemplateUrl,size:m.size})
},function n(s){r.reject(s)
});
q.then(function(){p.resolve(true)
},function(){p.reject(false)
});
return l
};
return i
}]};
return a
});
angular.module("ui.bootstrap.pagination",[]).controller("PaginationController",["$scope","$attrs","$parse",function(c,a,f){var b=this,d={$setViewValue:angular.noop},e=a.numPages?f(a.numPages).assign:angular.noop;
this.init=function(h,g){d=h;
this.config=g;
d.$render=function(){b.render()
};
if(a.itemsPerPage){c.$parent.$watch(f(a.itemsPerPage),function(i){b.itemsPerPage=parseInt(i,10);
c.totalPages=b.calculateTotalPages()
})
}else{this.itemsPerPage=g.itemsPerPage
}};
this.calculateTotalPages=function(){var g=this.itemsPerPage<1?1:Math.ceil(c.totalItems/this.itemsPerPage);
return Math.max(g||0,1)
};
this.render=function(){c.page=parseInt(d.$viewValue,10)||1
};
c.selectPage=function(g){if(c.page!==g&&g>0&&g<=c.totalPages){d.$setViewValue(g);
d.$render()
}};
c.getText=function(g){return c[g+"Text"]||b.config[g+"Text"]
};
c.noPrevious=function(){return c.page===1
};
c.noNext=function(){return c.page===c.totalPages
};
c.$watch("totalItems",function(){c.totalPages=b.calculateTotalPages()
});
c.$watch("totalPages",function(g){e(c.$parent,g);
if(c.page>g){c.selectPage(g)
}else{d.$render()
}})
}]).constant("paginationConfig",{itemsPerPage:10,boundaryLinks:false,directionLinks:true,firstText:"First",previousText:"Previous",nextText:"Next",lastText:"Last",rotate:true}).directive("pagination",["$parse","paginationConfig",function(b,a){return{restrict:"EA",scope:{totalItems:"=",firstText:"@",previousText:"@",nextText:"@",lastText:"@"},require:["pagination","?ngModel"],controller:"PaginationController",templateUrl:"template/pagination/pagination.html",replace:true,link:function(m,g,l,e){var i=e[0],c=e[1];
if(!c){return
}var k=angular.isDefined(l.maxSize)?m.$parent.$eval(l.maxSize):a.maxSize,f=angular.isDefined(l.rotate)?m.$parent.$eval(l.rotate):a.rotate;
m.boundaryLinks=angular.isDefined(l.boundaryLinks)?m.$parent.$eval(l.boundaryLinks):a.boundaryLinks;
m.directionLinks=angular.isDefined(l.directionLinks)?m.$parent.$eval(l.directionLinks):a.directionLinks;
i.init(c,a);
if(l.maxSize){m.$parent.$watch(b(l.maxSize),function(n){k=parseInt(n,10);
i.render()
})
}function d(o,p,n){return{number:o,text:p,active:n}
}function j(q,s){var o=[];
var t=1,w=s;
var v=(angular.isDefined(k)&&k<s);
if(v){if(f){t=Math.max(q-Math.floor(k/2),1);
w=t+k-1;
if(w>s){w=s;
t=w-k+1
}}else{t=((Math.ceil(q/k)-1)*k)+1;
w=Math.min(t+k-1,s)
}}for(var p=t;
p<=w;
p++){var u=d(p,p,p===q);
o.push(u)
}if(v&&!f){if(t>1){var n=d(t-1,"...",false);
o.unshift(n)
}if(w<s){var r=d(w+1,"...",false);
o.push(r)
}}return o
}var h=i.render;
i.render=function(){h();
if(m.page>0&&m.page<=m.totalPages){m.pages=j(m.page,m.totalPages)
}}
}}
}]).constant("pagerConfig",{itemsPerPage:10,previousText:"« Previous",nextText:"Next »",align:true}).directive("pager",["pagerConfig",function(a){return{restrict:"EA",scope:{totalItems:"=",previousText:"@",nextText:"@"},require:["pager","?ngModel"],controller:"PaginationController",templateUrl:"template/pagination/pager.html",replace:true,link:function(f,e,d,b){var g=b[0],c=b[1];
if(!c){return
}f.align=angular.isDefined(d.align)?f.$parent.$eval(d.align):a.align;
g.init(c,a)
}}
}]);
angular.module("ui.bootstrap.tooltip",["ui.bootstrap.position","ui.bootstrap.bindHtml"]).provider("$tooltip",function(){var b={placement:"top",animation:true,popupDelay:0};
var d={mouseenter:"mouseleave",click:"click",focus:"blur"};
var c={};
this.options=function(f){angular.extend(c,f)
};
this.setTriggers=function e(f){angular.extend(d,f)
};
function a(f){var g=/[A-Z]/g;
var h="-";
return f.replace(g,function(i,j){return(j?h:"")+i.toLowerCase()
})
}this.$get=["$window","$compile","$timeout","$parse","$document","$position","$interpolate",function(m,h,g,i,l,k,j){return function f(t,p,q){var v=angular.extend({},b,c);
function o(x){var w=x||v.trigger||q;
var y=d[w]||w;
return{show:w,hide:y}
}var s=a(t);
var n=j.startSymbol();
var r=j.endSymbol();
var u="<div "+s+'-popup title="'+n+"tt_title"+r+'" content="'+n+"tt_content"+r+'" placement="'+n+"tt_placement"+r+'" animation="tt_animation" is-open="tt_isOpen"></div>';
return{restrict:"EA",scope:true,compile:function(z,x){var w=h(u);
return function y(B,D,N){var E;
var J;
var V;
var T=angular.isDefined(v.appendToBody)?v.appendToBody:false;
var A=o(undefined);
var H=angular.isDefined(N[p+"Enable"]);
var U=function(){var W=k.positionElements(D,E,B.tt_placement,T);
W.top+="px";
W.left+="px";
E.css(W)
};
B.tt_isOpen=false;
function M(){if(!B.tt_isOpen){I()
}else{O()
}}function I(){if(H&&!B.$eval(N[p+"Enable"])){return
}if(B.tt_popupDelay){if(!V){V=g(S,B.tt_popupDelay,false);
V.then(function(W){W()
})
}}else{S()()
}}function O(){B.$apply(function(){L()
})
}function S(){V=null;
if(J){g.cancel(J);
J=null
}if(!B.tt_content){return angular.noop
}P();
E.css({top:0,left:0,display:"block"});
if(T){l.find("body").append(E)
}else{D.after(E)
}var X=(window.innerWidth||document.documentElement.clientWidth);
var W=G(D);
if(W.left>X-(E.prop("offsetWidth")||200)){B.tt_placement="left"
}U();
B.tt_isOpen=true;
B.$digest();
return U
}function L(){B.tt_isOpen=false;
g.cancel(V);
V=null;
if(B.tt_animation){if(!J){J=g(K,500)
}}else{K()
}}function P(){if(E){K()
}E=w(B,function(){});
B.$digest()
}function K(){J=null;
if(E){E.remove();
E=null
}}function G(W){var X=W[0].getBoundingClientRect();
return{width:X.width||W.prop("offsetWidth"),height:X.height||W.prop("offsetHeight"),top:X.top+(m.pageYOffset||l[0].documentElement.scrollTop),left:X.left+(m.pageXOffset||l[0].documentElement.scrollLeft)}
}N.$observe(t,function(W){B.tt_content=W;
if(!W&&B.tt_isOpen){L()
}});
N.$observe(p+"Title",function(W){B.tt_title=W
});
N.$observe(p+"Placement",function(W){B.tt_placement=angular.isDefined(W)?W:v.placement
});
N.$observe(p+"PopupDelay",function(X){var W=parseInt(X,10);
B.tt_popupDelay=!isNaN(W)?W:v.popupDelay
});
var R=function(){D.unbind(A.show,I);
D.unbind(A.hide,O)
};
N.$observe(p+"Trigger",function(W){R();
A=o(W);
if(A.show===A.hide){D.bind(A.show,M)
}else{D.bind(A.show,I);
D.bind(A.hide,O)
}});
var Q=B.$eval(N[p+"Animation"]);
B.tt_animation=angular.isDefined(Q)?!!Q:v.animation;
N.$observe(p+"AppendToBody",function(W){T=angular.isDefined(W)?i(W)(B):T
});
if(T){B.$on("$locationChangeSuccess",function F(){if(B.tt_isOpen){L()
}})
}B.$on("$destroy",function C(){g.cancel(J);
g.cancel(V);
R();
K()
})
}
}}
}
}]
}).directive("tooltipPopup",function(){return{restrict:"EA",replace:true,scope:{content:"@",placement:"@",animation:"&",isOpen:"&"},templateUrl:"template/tooltip/tooltip-popup.html"}
}).directive("tooltip",["$tooltip",function(a){return a("tooltip","tooltip","mouseenter")
}]).directive("tooltipHtmlUnsafePopup",function(){return{restrict:"EA",replace:true,scope:{content:"@",placement:"@",animation:"&",isOpen:"&"},templateUrl:"template/tooltip/tooltip-html-unsafe-popup.html"}
}).directive("tooltipHtmlUnsafe",["$tooltip",function(a){return a("tooltipHtmlUnsafe","tooltip","mouseenter")
}]);
angular.module("ui.bootstrap.popover",["ui.bootstrap.tooltip"]).directive("popoverPopup",function(){return{restrict:"EA",replace:true,scope:{title:"@",content:"@",placement:"@",animation:"&",isOpen:"&"},templateUrl:"template/popover/popover.html"}
}).directive("popover",["$tooltip",function(a){return a("popover","popover","click")
}]);
angular.module("ui.bootstrap.progressbar",[]).constant("progressConfig",{animate:true,max:100}).controller("ProgressController",["$scope","$attrs","progressConfig",function(d,a,e){var c=this,b=angular.isDefined(a.animate)?d.$parent.$eval(a.animate):e.animate;
this.bars=[];
d.max=angular.isDefined(a.max)?d.$parent.$eval(a.max):e.max;
this.addBar=function(g,f){if(!b){f.css({transition:"none"})
}this.bars.push(g);
g.$watch("value",function(h){g.percent=+(100*h/d.max).toFixed(2)
});
g.$on("$destroy",function(){f=null;
c.removeBar(g)
})
};
this.removeBar=function(f){this.bars.splice(this.bars.indexOf(f),1)
}
}]).directive("progress",function(){return{restrict:"EA",replace:true,transclude:true,controller:"ProgressController",require:"progress",scope:{},templateUrl:"template/progressbar/progress.html"}
}).directive("bar",function(){return{restrict:"EA",replace:true,transclude:true,require:"^progress",scope:{value:"=",type:"@"},templateUrl:"template/progressbar/bar.html",link:function(d,c,b,a){a.addBar(d,c)
}}
}).directive("progressbar",function(){return{restrict:"EA",replace:true,transclude:true,controller:"ProgressController",scope:{value:"=",type:"@"},templateUrl:"template/progressbar/progressbar.html",link:function(d,c,b,a){a.addBar(d,angular.element(c.children()[0]))
}}
});
angular.module("ui.bootstrap.rating",[]).constant("ratingConfig",{max:5,stateOn:null,stateOff:null}).controller("RatingController",["$scope","$attrs","ratingConfig",function(b,a,d){var c={$setViewValue:angular.noop};
this.init=function(e){c=e;
c.$render=this.render;
this.stateOn=angular.isDefined(a.stateOn)?b.$parent.$eval(a.stateOn):d.stateOn;
this.stateOff=angular.isDefined(a.stateOff)?b.$parent.$eval(a.stateOff):d.stateOff;
var f=angular.isDefined(a.ratingStates)?b.$parent.$eval(a.ratingStates):new Array(angular.isDefined(a.max)?b.$parent.$eval(a.max):d.max);
b.range=this.buildTemplateObjects(f)
};
this.buildTemplateObjects=function(e){for(var f=0,g=e.length;
f<g;
f++){e[f]=angular.extend({index:f},{stateOn:this.stateOn,stateOff:this.stateOff},e[f])
}return e
};
b.rate=function(e){if(!b.readonly&&e>=0&&e<=b.range.length){c.$setViewValue(e);
c.$render()
}};
b.enter=function(e){if(!b.readonly){b.value=e
}b.onHover({value:e})
};
b.reset=function(){b.value=c.$viewValue;
b.onLeave()
};
b.onKeydown=function(e){if(/(37|38|39|40)/.test(e.which)){e.preventDefault();
e.stopPropagation();
b.rate(b.value+(e.which===38||e.which===39?1:-1))
}};
this.render=function(){b.value=c.$viewValue
}
}]).directive("rating",function(){return{restrict:"EA",require:["rating","ngModel"],scope:{readonly:"=?",onHover:"&",onLeave:"&"},controller:"RatingController",templateUrl:"template/rating/rating.html",replace:true,link:function(f,e,d,a){var b=a[0],c=a[1];
if(c){b.init(c)
}}}
});
angular.module("ui.bootstrap.tabs",[]).controller("TabsetController",["$scope",function TabsetCtrl(a){var d=this,b=d.tabs=a.tabs=[];
d.select=function(f){angular.forEach(b,function(g){if(g.active&&g!==f){g.active=false;
g.onDeselect()
}});
f.active=true;
f.onSelect()
};
d.addTab=function e(f){b.push(f);
if(b.length===1){f.active=true
}else{if(f.active){d.select(f)
}}};
d.removeTab=function c(g){var f=b.indexOf(g);
if(g.active&&b.length>1){var h=f==b.length-1?f-1:f+1;
d.select(b[h])
}b.splice(f,1)
}
}]).directive("tabset",function(){return{restrict:"EA",transclude:true,replace:true,scope:{type:"@"},controller:"TabsetController",templateUrl:"template/tabs/tabset.html",link:function(c,b,a){c.vertical=angular.isDefined(a.vertical)?c.$parent.$eval(a.vertical):false;
c.justified=angular.isDefined(a.justified)?c.$parent.$eval(a.justified):false
}}
}).directive("tab",["$parse",function(a){return{require:"^tabset",restrict:"EA",replace:true,templateUrl:"template/tabs/tab.html",transclude:true,scope:{active:"=?",heading:"@",onSelect:"&select",onDeselect:"&deselect"},controller:function(){},compile:function(e,d,c){return function b(h,i,f,g){h.$watch("active",function(j){if(j){g.select(h)
}});
h.disabled=false;
if(f.disabled){h.$parent.$watch(a(f.disabled),function(j){h.disabled=!!j
})
}h.select=function(){if(!h.disabled){h.active=true
}};
g.addTab(h);
h.$on("$destroy",function(){g.removeTab(h)
});
h.$transcludeFn=c
}
}}
}]).directive("tabHeadingTransclude",[function(){return{restrict:"A",require:"^tab",link:function(d,e,c,a){d.$watch("headingElement",function b(f){if(f){e.html("");
e.append(f)
}})
}}
}]).directive("tabContentTransclude",function(){return{restrict:"A",require:"^tabset",link:function(d,e,b){var c=d.$eval(b.tabContentTransclude);
c.$transcludeFn(c.$parent,function(f){angular.forEach(f,function(g){if(a(g)){c.headingElement=g
}else{e.append(g)
}})
})
}};
function a(b){return b.tagName&&(b.hasAttribute("tab-heading")||b.hasAttribute("data-tab-heading")||b.tagName.toLowerCase()==="tab-heading"||b.tagName.toLowerCase()==="data-tab-heading")
}});
angular.module("ui.bootstrap.timepicker",[]).constant("timepickerConfig",{hourStep:1,minuteStep:1,showMeridian:true,meridians:null,readonlyInput:false,mousewheel:true}).controller("TimepickerController",["$scope","$attrs","$parse","$log","$locale","timepickerConfig",function(l,g,f,n,q,d){var k=new Date(),i={$setViewValue:angular.noop},p=angular.isDefined(g.meridians)?l.$parent.$eval(g.meridians):d.meridians||q.DATETIME_FORMATS.AMPMS;
l.$watch(function(){return i.$modelValue
},function(s){if(s===undefined){l.hours=null;
l.minutes=null;
k=new Date();
k.setMinutes(0)
}});
this.init=function(v,t){i=v;
i.$render=this.render;
var w=t.eq(0),u=t.eq(1);
var s=angular.isDefined(g.mousewheel)?l.$parent.$eval(g.mousewheel):d.mousewheel;
if(s){this.setupMousewheelEvents(w,u)
}l.readonlyInput=angular.isDefined(g.readonlyInput)?l.$parent.$eval(g.readonlyInput):d.readonlyInput;
this.setupInputEvents(w,u)
};
var b=d.hourStep;
if(g.hourStep){l.$parent.$watch(f(g.hourStep),function(s){b=parseInt(s,10)
})
}var j=d.minuteStep;
if(g.minuteStep){l.$parent.$watch(f(g.minuteStep),function(s){j=parseInt(s,10)
})
}l.showMeridian=d.showMeridian;
if(g.showMeridian){l.$parent.$watch(f(g.showMeridian),function(u){l.showMeridian=!!u;
if(i.$error.time){var s=a(),t=h();
if(angular.isDefined(s)&&angular.isDefined(t)){k.setHours(s);
e()
}}else{c()
}})
}function a(){var s=parseInt(l.hours,10);
var t=(l.showMeridian)?(s>0&&s<13):(s>=0&&s<24);
if(!t){return undefined
}if(l.showMeridian){if(s===12){s=0
}if(l.meridian===p[1]){s=s+12
}}return s
}function h(){var s=parseInt(l.minutes,10);
return(s>=0&&s<60)?s:undefined
}function o(s){return(angular.isDefined(s)&&s.toString().length<2)?"0"+s:s
}this.setupMousewheelEvents=function(u,t){var s=function(v){if(v.originalEvent){v=v.originalEvent
}var w=(v.wheelDelta)?v.wheelDelta:-v.deltaY;
return(v.detail||w>0)
};
u.bind("mousewheel wheel",function(v){l.$apply((s(v))?l.incrementHours():l.decrementHours());
v.preventDefault()
});
t.bind("mousewheel wheel",function(v){l.$apply((s(v))?l.incrementMinutes():l.decrementMinutes());
v.preventDefault()
})
};
this.setupInputEvents=function(t,s){if(l.readonlyInput){l.updateHours=angular.noop;
l.updateMinutes=angular.noop;
return
}var u=function(v,w){i.$setViewValue(null);
i.$setValidity("time",false);
if(angular.isDefined(v)){l.invalidHours=v
}if(angular.isDefined(w)){l.invalidMinutes=w
}};
l.updateHours=function(){var v=a();
if(angular.isDefined(v)){k.setHours(v);
e("h")
}else{k.setHours(new Date().getHours());
u(true)
}};
t.bind("blur",function(v){if(!l.invalidHours&&l.hours<10){l.$apply(function(){l.hours=o(l.hours);
console.log("set from here")
})
}});
l.updateMinutes=function(){var v=h();
if(angular.isDefined(v)){k.setMinutes(v);
e("m")
}else{k.setMinutes(0);
u(undefined,true)
}};
s.bind("blur",function(v){if(!l.invalidMinutes&&l.minutes<10){l.$apply(function(){l.minutes=o(l.minutes)
})
}})
};
this.render=function(){var s=i.$modelValue?new Date(i.$modelValue):null;
if(isNaN(s)){i.$setValidity("time",false);
n.error('Timepicker directive: "ng-model" value must be a Date object, a number of milliseconds since 01.01.1970 or a string representing an RFC2822 or ISO 8601 date.')
}else{if(s){k=s
}m();
c();
l.firstTime=true
}};
function e(s){m();
i.$setViewValue(new Date(k));
c(s)
}function m(){i.$setValidity("time",true);
l.invalidHours=false;
l.invalidMinutes=false
}function c(u){var s=k.getHours(),t=k.getMinutes();
if(l.showMeridian){s=(s===0||s===12)?12:s%12
}l.hours=u==="h"?s:o(s);
l.minutes=u==="m"?t:o(t);
l.meridian=k.getHours()<12?p[0]:p[1]
}function r(s){var t=new Date(k.getTime()+s*60000);
k.setHours(t.getHours(),t.getMinutes());
e()
}l.incrementHours=function(){r(b*60)
};
l.decrementHours=function(){r(-b*60)
};
l.incrementMinutes=function(){r(j)
};
l.decrementMinutes=function(){r(-j)
};
l.toggleMeridian=function(){r(12*60*((k.getHours()<12)?1:-1))
}
}]).directive("timepicker",function(){return{restrict:"EA",require:["timepicker","?^ngModel"],controller:"TimepickerController",replace:true,scope:{},templateUrl:"template/timepicker/timepicker.html",link:function(f,e,d,b){var a=b[0],c=b[1];
f.removeRequired=d.removeRequired;
if(c){a.init(c,e.find("input"))
}}}
});
angular.module("ui.bootstrap.typeahead",["ui.bootstrap.position","ui.bootstrap.bindHtml"]).factory("typeaheadParser",["$parse",function(b){var a=/^\s*([\s\S]+?)(?:\s+as\s+([\s\S]+?))?\s+for\s+(?:([\$\w][\$\w\d]*))\s+in\s+([\s\S]+?)$/;
return{parse:function(c){var d=c.match(a);
if(!d){throw new Error('Expected typeahead specification in form of "_modelValue_ (as _label_)? for _item_ in _collection_" but got "'+c+'".')
}return{itemName:d[3],source:b(d[4]),viewMapper:b(d[2]||d[1]),modelMapper:b(d[1])}
}}
}]).directive("typeahead",["$compile","$parse","$q","$timeout","$document","$position","typeaheadParser",function(e,f,c,d,h,g,b){var a=[9,13,27,38,40];
return{require:"ngModel",link:function(u,m,E,i){var B=u.$eval(E.typeaheadMinLength)||1;
var n=u.$eval(E.typeaheadWaitMs)||0;
var v=u.$eval(E.typeaheadEditable)!==false;
var z=f(E.typeaheadLoading).assign||angular.noop;
var y=f(E.typeaheadOnSelect);
var A=E.typeaheadInputFormatter?f(E.typeaheadInputFormatter):undefined;
var G=E.typeaheadAppendToBody?u.$eval(E.typeaheadAppendToBody):false;
var j=f(E.ngModel).assign;
var F=b.parse(E.typeahead);
var o;
var k=u.$new();
u.$on("$destroy",function(){k.$destroy()
});
var C="typeahead-"+k.$id+"-"+Math.floor(Math.random()*10000);
m.attr({"aria-autocomplete":"list","aria-expanded":false,"aria-owns":C});
var t=angular.element("<div typeahead-popup></div>");
t.attr({id:C,matches:"matches",active:"activeIdx",select:"select(activeIdx)",query:"query",position:"position"});
if(angular.isDefined(E.typeaheadTemplateUrl)){t.attr("template-url",E.typeaheadTemplateUrl)
}var p=function(){k.matches=[];
k.activeIdx=-1;
m.attr("aria-expanded",false)
};
var l=function(H){return C+"-option-"+H
};
k.$watch("activeIdx",function(H){if(H<0){m.removeAttr("aria-activedescendant")
}else{m.attr("aria-activedescendant",l(H))
}});
var x=function(H){var I={$viewValue:H};
z(u,true);
c.when(F.source(u,I)).then(function(L){var J=(H===i.$viewValue);
if(J&&o){if(L.length>0){k.activeIdx=0;
k.matches.length=0;
for(var K=0;
K<L.length;
K++){I[F.itemName]=L[K];
k.matches.push({id:l(K),label:F.viewMapper(k,I),model:L[K]})
}k.query=H;
k.position=G?g.offset(m):g.position(m);
k.position.top=k.position.top+m.prop("offsetHeight");
m.attr("aria-expanded",true)
}else{p()
}}if(J){z(u,false)
}},function(){p();
z(u,false)
})
};
p();
k.query=undefined;
var s;
var w=function(H){s=d(function(){x(H)
},n)
};
var q=function(){if(s){d.cancel(s)
}};
i.$parsers.unshift(function(H){o=true;
if(H&&H.length>=B){if(n>0){q();
w(H)
}else{x(H)
}}else{z(u,false);
q();
p()
}if(v){return H
}else{if(!H){i.$setValidity("editable",true);
return H
}else{i.$setValidity("editable",false);
return undefined
}}});
i.$formatters.push(function(J){var I,H;
var K={};
if(A){K["$model"]=J;
return A(u,K)
}else{K[F.itemName]=J;
I=F.viewMapper(u,K);
K[F.itemName]=undefined;
H=F.viewMapper(u,K);
return I!==H?I:J
}});
k.select=function(J){var K={};
var H,I;
K[F.itemName]=I=k.matches[J].model;
H=F.modelMapper(u,K);
j(u,H);
i.$setValidity("editable",true);
y(u,{$item:I,$model:H,$label:F.viewMapper(u,K)});
p();
d(function(){m[0].focus()
},0,false)
};
m.bind("keydown",function(H){if(k.matches.length===0||a.indexOf(H.which)===-1){return
}H.preventDefault();
if(H.which===40){k.activeIdx=(k.activeIdx+1)%k.matches.length;
k.$digest()
}else{if(H.which===38){k.activeIdx=(k.activeIdx?k.activeIdx:k.matches.length)-1;
k.$digest()
}else{if(H.which===13||H.which===9){k.$apply(function(){k.select(k.activeIdx)
})
}else{if(H.which===27){H.stopPropagation();
p();
k.$digest()
}}}}});
m.bind("blur",function(H){o=false
});
var r=function(H){if(m[0]!==H.target){p();
k.$digest()
}};
h.bind("click",r);
u.$on("$destroy",function(){h.unbind("click",r)
});
var D=e(t)(k);
if(G){h.find("body").append(D)
}else{m.after(D)
}}}
}]).directive("typeaheadPopup",function(){return{restrict:"EA",scope:{matches:"=",query:"=",active:"=",position:"=",select:"&"},replace:true,templateUrl:"template/typeahead/typeahead-popup.html",link:function(c,b,a){c.templateUrl=a.templateUrl;
c.isOpen=function(){return c.matches.length>0
};
c.isActive=function(d){return c.active==d
};
c.selectActive=function(d){c.active=d
};
c.selectMatch=function(d){c.select({activeIdx:d})
}
}}
}).directive("typeaheadMatch",["$http","$templateCache","$compile","$parse",function(d,a,b,c){return{restrict:"EA",scope:{index:"=",match:"=",query:"="},link:function(h,g,f){var e=c(f.templateUrl)(h.$parent)||"template/typeahead/typeahead-match.html";
d.get(e,{cache:a}).success(function(i){g.replaceWith(b(i.trim())(h))
})
}}
}]).filter("typeaheadHighlight",function(){function a(b){return b.replace(/([.?*+^$[\]\\(){}|-])/g,"\\$1")
}return function(c,b){return b?(""+c).replace(new RegExp(a(b),"gi"),"<strong>$&</strong>"):c
}
});
angular.module("template/accordion/accordion-group.html",[]).run(["$templateCache",function(a){a.put("template/accordion/accordion-group.html",'<div class="panel panel-default">\n  <div class="panel-heading">\n    <h4 class="panel-title">\n      <a class="accordion-toggle" ng-click="toggleOpen()" accordion-transclude="heading"><span ng-class="{\'text-muted\': isDisabled}">{{heading}}</span></a>\n    </h4>\n  </div>\n  <div class="panel-collapse" collapse="!isOpen">\n	  <div class="panel-body" ng-transclude></div>\n  </div>\n</div>')
}]);
angular.module("template/accordion/accordion.html",[]).run(["$templateCache",function(a){a.put("template/accordion/accordion.html",'<div class="panel-group" ng-transclude></div>')
}]);
angular.module("template/alert/alert.html",[]).run(["$templateCache",function(a){a.put("template/alert/alert.html",'<div class="alert" ng-class="[\'alert-\' + (type || \'warning\'), closeable ? \'alert-dismissable\' : null]" role="alert">\n    <button ng-show="closeable" type="button" class="close" ng-click="close()">\n        <span aria-hidden="true">&times;</span>\n        <span class="sr-only">Close</span>\n    </button>\n    <div ng-transclude></div>\n</div>\n')
}]);
angular.module("template/carousel/carousel.html",[]).run(["$templateCache",function(a){a.put("template/carousel/carousel.html",'<div ng-mouseenter="pause()" ng-mouseleave="play()" class="carousel" ng-swipe-right="prev()" ng-swipe-left="next()">\n    <ol class="carousel-indicators" ng-show="slides.length > 1">\n        <li ng-repeat="slide in slides track by $index" ng-class="{active: isActive(slide)}" ng-click="select(slide)"></li>\n    </ol>\n    <div class="carousel-inner" ng-transclude></div>\n    <a class="left carousel-control" ng-click="prev()" ng-show="slides.length > 1"><span class="glyphicon glyphicon-chevron-left"></span></a>\n    <a class="right carousel-control" ng-click="next()" ng-show="slides.length > 1"><span class="glyphicon glyphicon-chevron-right"></span></a>\n</div>\n')
}]);
angular.module("template/carousel/slide.html",[]).run(["$templateCache",function(a){a.put("template/carousel/slide.html","<div ng-class=\"{\n    'active': leaving || (active && !entering),\n    'prev': (next || active) && direction=='prev',\n    'next': (next || active) && direction=='next',\n    'right': direction=='prev',\n    'left': direction=='next'\n  }\" class=\"item text-center\" ng-transclude></div>\n")
}]);
angular.module("template/datepicker/datepicker.html",[]).run(["$templateCache",function(a){a.put("template/datepicker/datepicker.html",'<div><div ng-if="isOpen"><div ng-switch="datepickerMode" role="application" ng-keydown="keydown($event)">\n  <daypicker ng-switch-when="day" tabindex="0"></daypicker>\n  <monthpicker ng-switch-when="month" tabindex="0"></monthpicker>\n  <yearpicker ng-switch-when="year" tabindex="0"></yearpicker>\n</div></div></div>')
}]);
angular.module("template/datepicker/day.html",[]).run(["$templateCache",function(a){a.put("template/datepicker/day.html",'<table role="grid" aria-labelledby="{{uniqueId}}-title" aria-activedescendant="{{activeDateId}}">\n  <thead>\n    <tr>\n      <th><button type="button" class="btn btn-default btn-xs pull-left" ng-click="move(-1)" tabindex="-1"><i class="glyphicon glyphicon-chevron-left"></i></button></th>\n      <th colspan="{{5 + showWeeks}}"><button id="{{uniqueId}}-title" role="heading" aria-live="assertive" aria-atomic="true" type="button" class="btn btn-default btn-xs" ng-click="toggleMode()" tabindex="-1" style="width:100%;"><strong>{{title}}</strong></button></th>\n      <th><button type="button" class="btn btn-default btn-xs pull-right" ng-click="move(1)" tabindex="-1"><i class="glyphicon glyphicon-chevron-right"></i></button></th>\n    </tr>\n    <tr>\n      <th ng-show="showWeeks" class="text-center"></th>\n      <th ng-repeat="label in labels track by $index" class="text-center"><small aria-label="{{label.full}}">{{label.abbr}}</small></th>\n    </tr>\n  </thead>\n  <tbody>\n    <tr ng-repeat="row in rows track by $index">\n      <td ng-show="showWeeks" class="text-center h6"><em>{{ weekNumbers[$index] }}</em></td>\n      <td ng-repeat="dt in row track by dt.date" class="text-center" role="gridcell" id="{{dt.uid}}" aria-disabled="{{!!dt.disabled}}">\n        <button type="button" style="width:100%;" class="btn btn-default btn-xs" ng-class="{\'btn-info\': dt.selected, active: isActive(dt)}" ng-click="select(dt.date)" ng-disabled="dt.disabled" tabindex="-1"><span ng-class="{\'text-muted\': dt.secondary, \'text-info\': dt.current}">{{dt.label}}</span></button>\n      </td>\n    </tr>\n  </tbody>\n</table>\n')
}]);
angular.module("template/datepicker/month.html",[]).run(["$templateCache",function(a){a.put("template/datepicker/month.html",'<table role="grid" aria-labelledby="{{uniqueId}}-title" aria-activedescendant="{{activeDateId}}">\n  <thead>\n    <tr>\n      <th><button type="button" class="btn btn-default btn-sm pull-left" ng-click="move(-1)" tabindex="-1"><i class="glyphicon glyphicon-chevron-left"></i></button></th>\n      <th><button id="{{uniqueId}}-title" role="heading" aria-live="assertive" aria-atomic="true" type="button" class="btn btn-default btn-sm" ng-click="toggleMode()" tabindex="-1" style="width:100%;"><strong>{{title}}</strong></button></th>\n      <th><button type="button" class="btn btn-default btn-sm pull-right" ng-click="move(1)" tabindex="-1"><i class="glyphicon glyphicon-chevron-right"></i></button></th>\n    </tr>\n  </thead>\n  <tbody>\n    <tr ng-repeat="row in rows track by $index">\n      <td ng-repeat="dt in row track by dt.date" class="text-center" role="gridcell" id="{{dt.uid}}" aria-disabled="{{!!dt.disabled}}">\n        <button type="button" style="width:100%;" class="btn btn-default" ng-class="{\'btn-info\': dt.selected, active: isActive(dt)}" ng-click="select(dt.date)" ng-disabled="dt.disabled" tabindex="-1"><span ng-class="{\'text-info\': dt.current}">{{dt.label}}</span></button>\n      </td>\n    </tr>\n  </tbody>\n</table>\n')
}]);
angular.module("template/datepicker/popup.html",[]).run(["$templateCache",function(a){a.put("template/datepicker/popup.html",'<ul class="dropdown-menu" ng-style="{display: (isOpen && \'block\') || \'none\', top: position.top+\'px\', left: position.left+\'px\'}" ng-keydown="keydown($event)">\n	<li ng-transclude></li>\n	<li ng-if="showButtonBar" style="padding:10px 9px 2px">\n		<span class="btn-group">\n			<button type="button" class="btn btn-sm btn-danger" ng-click="select(null)">{{ getText(\'clear\') }}</button>\n		</span>\n		<button type="button" class="btn btn-sm btn-success pull-right" ng-click="close()">{{ getText(\'close\') }}</button>\n	</li>\n</ul>\n')
}]);
angular.module("template/datepicker/year.html",[]).run(["$templateCache",function(a){a.put("template/datepicker/year.html",'<table role="grid" aria-labelledby="{{uniqueId}}-title" aria-activedescendant="{{activeDateId}}">\n  <thead>\n    <tr>\n      <th><button type="button" class="btn btn-default btn-sm pull-left" ng-click="move(-1)" tabindex="-1"><i class="glyphicon glyphicon-chevron-left"></i></button></th>\n      <th colspan="3"><button id="{{uniqueId}}-title" role="heading" aria-live="assertive" aria-atomic="true" type="button" class="btn btn-default btn-sm" ng-click="toggleMode()" tabindex="-1" style="width:100%;"><strong>{{title}}</strong></button></th>\n      <th><button type="button" class="btn btn-default btn-sm pull-right" ng-click="move(1)" tabindex="-1"><i class="glyphicon glyphicon-chevron-right"></i></button></th>\n    </tr>\n  </thead>\n  <tbody>\n    <tr ng-repeat="row in rows track by $index">\n      <td ng-repeat="dt in row track by dt.date" class="text-center" role="gridcell" id="{{dt.uid}}" aria-disabled="{{!!dt.disabled}}">\n        <button type="button" style="width:100%;" class="btn btn-default" ng-class="{\'btn-info\': dt.selected, active: isActive(dt)}" ng-click="select(dt.date)" ng-disabled="dt.disabled" tabindex="-1"><span ng-class="{\'text-info\': dt.current}">{{dt.label}}</span></button>\n      </td>\n    </tr>\n  </tbody>\n</table>\n')
}]);
angular.module("template/modal/backdrop.html",[]).run(["$templateCache",function(a){a.put("template/modal/backdrop.html",'<div class="modal-backdrop fade {{ backdropClass }}"\n     ng-class="{in: animate}"\n     ng-style="{\'z-index\': 1040 + (index && 1 || 0) + index*10}"\n></div>\n')
}]);
angular.module("template/modal/window.html",[]).run(["$templateCache",function(a){a.put("template/modal/window.html",'<div tabindex="-1" role="dialog" class="modal fade" ng-class="{in: animate}" ng-style="{\'z-index\': 1050 + index*10, display: \'block\'}" ng-click="close($event)">\n    <div class="modal-dialog" ng-class="{\'modal-sm\': size == \'sm\', \'modal-lg\': size == \'lg\'}"><div class="modal-content" modal-transclude></div></div>\n</div>')
}]);
angular.module("template/pagination/pager.html",[]).run(["$templateCache",function(a){a.put("template/pagination/pager.html",'<ul class="pager">\n  <li ng-class="{disabled: noPrevious(), previous: align}"><a href ng-click="selectPage(page - 1)">{{getText(\'previous\')}}</a></li>\n  <li ng-class="{disabled: noNext(), next: align}"><a href ng-click="selectPage(page + 1)">{{getText(\'next\')}}</a></li>\n</ul>')
}]);
angular.module("template/pagination/pagination.html",[]).run(["$templateCache",function(a){a.put("template/pagination/pagination.html",'<ul class="pagination">\n  <li ng-if="boundaryLinks" ng-class="{disabled: noPrevious()}"><a href ng-click="selectPage(1)">{{getText(\'first\')}}</a></li>\n  <li ng-if="directionLinks" ng-class="{disabled: noPrevious()}"><a href ng-click="selectPage(page - 1)">{{getText(\'previous\')}}</a></li>\n  <li ng-repeat="page in pages track by $index" ng-class="{active: page.active}"><a href ng-click="selectPage(page.number)">{{page.text}}</a></li>\n  <li ng-if="directionLinks" ng-class="{disabled: noNext()}"><a href ng-click="selectPage(page + 1)">{{getText(\'next\')}}</a></li>\n  <li ng-if="boundaryLinks" ng-class="{disabled: noNext()}"><a href ng-click="selectPage(totalPages)">{{getText(\'last\')}}</a></li>\n</ul>')
}]);
angular.module("template/tooltip/tooltip-html-unsafe-popup.html",[]).run(["$templateCache",function(a){a.put("template/tooltip/tooltip-html-unsafe-popup.html",'<div class="tooltip {{placement}}" ng-class="{ in: isOpen(), fade: animation() }">\n  <div class="tooltip-arrow"></div>\n  <div class="tooltip-inner" bind-html-unsafe="content"></div>\n</div>\n')
}]);
angular.module("template/tooltip/tooltip-popup.html",[]).run(["$templateCache",function(a){a.put("template/tooltip/tooltip-popup.html",'<div class="tooltip {{placement}}" ng-class="{ in: isOpen(), fade: animation() }">\n  <div class="tooltip-arrow"></div>\n  <div class="tooltip-inner" ng-bind="content"></div>\n</div>\n')
}]);
angular.module("template/popover/popover.html",[]).run(["$templateCache",function(a){a.put("template/popover/popover.html",'<div class="popover {{placement}}" ng-class="{ in: isOpen(), fade: animation() }">\n  <div class="arrow"></div>\n\n  <div class="popover-inner">\n      <h3 class="popover-title" ng-bind="title" ng-show="title"></h3>\n      <div class="popover-content" ng-bind="content"></div>\n  </div>\n</div>\n')
}]);
angular.module("template/progressbar/bar.html",[]).run(["$templateCache",function(a){a.put("template/progressbar/bar.html",'<div class="progress-bar" ng-class="type && \'progress-bar-\' + type" role="progressbar" aria-valuenow="{{value}}" aria-valuemin="0" aria-valuemax="{{max}}" ng-style="{width: percent + \'%\'}" aria-valuetext="{{percent | number:0}}%" ng-transclude></div>')
}]);
angular.module("template/progressbar/progress.html",[]).run(["$templateCache",function(a){a.put("template/progressbar/progress.html",'<div class="progress" ng-transclude></div>')
}]);
angular.module("template/progressbar/progressbar.html",[]).run(["$templateCache",function(a){a.put("template/progressbar/progressbar.html",'<div class="progress">\n  <div class="progress-bar" ng-class="type && \'progress-bar-\' + type" role="progressbar" aria-valuenow="{{value}}" aria-valuemin="0" aria-valuemax="{{max}}" ng-style="{width: percent + \'%\'}" aria-valuetext="{{percent | number:0}}%" ng-transclude></div>\n</div>')
}]);
angular.module("template/rating/rating.html",[]).run(["$templateCache",function(a){a.put("template/rating/rating.html",'<span ng-mouseleave="reset()" ng-keydown="onKeydown($event)" tabindex="0" role="slider" aria-valuemin="0" aria-valuemax="{{range.length}}" aria-valuenow="{{value}}">\n    <i ng-repeat="r in range track by $index" ng-mouseenter="enter($index + 1)" ng-click="rate($index + 1)" class="glyphicon" ng-class="$index < value && (r.stateOn || \'glyphicon-star\') || (r.stateOff || \'glyphicon-star-empty\')">\n        <span class="sr-only">({{ $index < value ? \'*\' : \' \' }})</span>\n    </i>\n</span>')
}]);
angular.module("template/tabs/tab.html",[]).run(["$templateCache",function(a){a.put("template/tabs/tab.html",'<li ng-class="{active: active, disabled: disabled}">\n  <a ng-click="select()" tab-heading-transclude>{{heading}}</a>\n</li>\n')
}]);
angular.module("template/tabs/tabset.html",[]).run(["$templateCache",function(a){a.put("template/tabs/tabset.html",'<div>\n  <ul class="nav nav-{{type || \'tabs\'}}" ng-class="{\'nav-stacked\': vertical, \'nav-justified\': justified}" ng-transclude></ul>\n  <div class="tab-content">\n    <div class="tab-pane" \n         ng-repeat="tab in tabs" \n         ng-class="{active: tab.active}"\n         tab-content-transclude="tab">\n    </div>\n  </div>\n</div>\n')
}]);
angular.module("template/timepicker/timepicker.html",[]).run(["$templateCache",function(a){a.put("template/timepicker/timepicker.html",'<table>\n	<tbody>\n		<tr class="text-center">\n			<td><a ng-click="incrementHours()" class="btn btn-link"><span class="glyphicon glyphicon-chevron-up"></span></a></td>\n			<td>&nbsp;</td>\n			<td><a ng-click="incrementMinutes()" class="btn btn-link"><span class="glyphicon glyphicon-chevron-up"></span></a></td>\n			<td ng-show="showMeridian"></td>\n		</tr>\n		<tr>\n			<td style="width:50px;" class="form-group" ng-class="{\'has-error\': invalidHours}">\n				<input type="text" ng-model="hours" ng-change="updateHours()" class="form-control text-center" numbers-only="numbers-only" ng-required="removeRequired === undefined ||removeRequired === \'true\'" ng-mousewheel="incrementHours()" ng-readonly="readonlyInput" maxlength="2">\n			</td>\n			<td>:</td>\n			<td style="width:50px;" class="form-group" ng-class="{\'has-error\': invalidMinutes}">\n				<input type="text" ng-model="minutes" ng-change="updateMinutes()" class="form-control text-center" numbers-only="numbers-only" ng-required="removeRequired === undefined ||removeRequired === \'true\'" ng-readonly="readonlyInput" maxlength="2">\n			</td>\n			<td ng-show="showMeridian"><button type="button" class="btn btn-default text-center" ng-click="toggleMeridian()">{{meridian}}</button></td>\n		</tr>\n		<tr class="text-center">\n			<td><a ng-click="decrementHours()" class="btn btn-link"><span class="glyphicon glyphicon-chevron-down"></span></a></td>\n			<td>&nbsp;</td>\n			<td><a ng-click="decrementMinutes()" class="btn btn-link"><span class="glyphicon glyphicon-chevron-down"></span></a></td>\n			<td ng-show="showMeridian"></td>\n		</tr>\n	</tbody>\n</table>\n')
}]);
angular.module("template/typeahead/typeahead-match.html",[]).run(["$templateCache",function(a){a.put("template/typeahead/typeahead-match.html",'<a tabindex="-1" bind-html-unsafe="match.label | typeaheadHighlight:query"></a>')
}]);
angular.module("template/typeahead/typeahead-popup.html",[]).run(["$templateCache",function(a){a.put("template/typeahead/typeahead-popup.html",'<ul class="dropdown-menu" ng-show="isOpen()" ng-style="{top: position.top+\'px\', left: position.left+\'px\'}" style="display: block;" role="listbox" aria-hidden="{{!isOpen()}}">\n    <li ng-repeat="match in matches track by $index" ng-class="{active: isActive($index) }" ng-mouseenter="selectActive($index)" ng-click="selectMatch($index)" role="option" id="{{match.id}}">\n        <div typeahead-match index="$index" match="match" query="query" template-url="templateUrl"></div>\n    </li>\n</ul>\n')
}]);