define(["angular","dateValidate"],function(){globalProvider.compileProvider.directive("agDatePickerCustomSearch",["$compile","$filter","orderByFilter","$document",function(b,c,a,d){return{restrict:"E",template:'<div><div ng-if="isDateTime" ng-init="initDateTimePopup()"><div  class="dropdown keep-open"><div class="input-group" ><input date-validate validate-format="{{directiveFormat}}" placeholder="{{directiveFormat}}" min="dateTimeMin" max="dateTimeMax" type="text" id="ngModel" name="ngModel" class="form-control" ng-model="$parent.$parent.ngModel"><span class="input-group-btn" id="dateTimeSpan" data-toggle="dropdown"  ng-click="datePickerFlag.dateTimePicker = true;"><button ng-disabled="isDisabled" class="btn btn-default" style="border-bottom-left-radius: 0;border-top-left-radius: 0;"><i class="glyphicon glyphicon-calendar" id="dateTimeI"/></button></span><ul class="dropdown-menu"><li><datepicker is-open="datePickerFlag.dateTimePicker" id="dd" ng-model="$parent.datepickerModel"  min="dateTimeMin" max="dateTimeMax" ng-change="setDate(datepickerModel,timeModel)"></datepicker></li><li class="text-center"><timepicker remove-required="{{ngRequired}}" align="center" ng-model="$parent.timeModel" hour-step="hstep" minute-step="mstep" show-meridian="true" ng-change="setTime(timeModel,datepickerModel)"></timepicker><button class="btn btn-hkg" ng-click=setDateAndTime("datetime",timeModel)>Clear</button>&nbsp;&nbsp;<button class="btn btn-hkg" ng-click="setDateAndTime()">Close</button></li></ul></div></div></div><div ng-if="isDate"><div class="input-group" ><input type="text" id="ngModel" is-open="$parent.dtPicker.dt_open"  name="ngModel" class="form-control" datepicker-popup="{{dateFormat}}"  ng-model="ngModel" min="onlyMinDate" max="onlyMaxDate" ng-change=setOnlyDate(ngModel) placeholder="{{dateFormat}}"><span class="input-group-btn" ng-click=(isDisabled||open($event,"dt_open"))><button ng-disabled="isDisabled" class="btn btn-default"><i class="glyphicon glyphicon-calendar" ></i></button></span></div></div><div ng-if="isTime" ng-init="initDateTimePopup()"><div  class="dropdown keep-open"><a style="cursor:default;text-decoration: none"  data-toggle="dropdown"><div class="input-group" ><input type="text" id="ngModel"  name="ngModel" class="form-control"   ng-model="ngModel"  ><span id="timeSpan" class="input-group-addon"><i id="timeI" class="glyphicon glyphicon-time"></i></span></div></a><ul class="dropdown-menu"><li class="text-center"><timepicker remove-required="{{ngRequired}}" align="center" ng-model="$parent.timeModel" defaultTime="false" hour-step="hstep" minute-step="mstep" show-meridian="true" ng-change=setTime(timeModel) ></timepicker><button class="btn btn-hkg" ng-click=setDateAndTime("datetime")>Clear</button></li></ul></div>\n                    </div><div ng-if="isDateRange" ng-init="initDateTimePopup()"><div  class="row" ><div class="col-md-6" style="padding-right:1px;"><div ng-form="fromForm"><div ng-class="{\'has-error\': fromForm.fromModel.$invalid}"><div class="input-group" ><input type="text" id="fromModel" is-open="$parent.dtPicker.open_from"  name="fromModel" class="form-control" placeholder="From" datepicker-popup="{{dateRangeFormat}}"  ng-model="$parent.fromModel" max="toModel" ng-change="setFromDate(fromModel)" ng-required="ngRequired"><span class="input-group-btn" ng-click=(isDisabled||open($event,"open_from"))><button ng-disabled="isDisabled" class="btn btn-default"><i class="glyphicon glyphicon-calendar"></i></button></span></div><div ng-show="fromForm.fromModel.$invalid" class = "error,help-block ng-hide" ><span class="help-block" ng-show="fromForm.fromModel.$error.required && !(fromForm.fromModel.$error.date || fromForm.fromModel.$error.min || fromForm.fromModel.$error.max)">{{fromLabel}}{{ entity +  " not entered" | translate }}</span><span class="help-block" ng-show="fromForm.fromModel.$error.date">{{ entity +  "Invalid date format" | translate }}</span><span class="help-block" ng-show="fromForm.fromModel.$error.min">{{ entity +  "Violates minimum constraint" | translate }}</span><span class="help-block" ng-show="fromForm.fromModel.$error.max">{{ entity +  "Violates maximum constraint" | translate }}</span></span></div></div></div></div><div class="col-md-6" style="padding-left:1px;"><div ng-form="toForm"><div ng-class="{\'has-error\': toForm.toModel.$invalid}"><div class="input-group" ><input type="text" id="toModel" is-open="$parent.dtPicker.open_to"  name="toModel" class="form-control" min="fromModel" placeholder="To"  datepicker-popup="{{dateRangeFormat}}"  ng-model="$parent.toModel" ng-change="setToDate(toModel)" ng-required="ngRequired"><span class="input-group-btn" ng-click=(isDisabled||open($event,"open_to"))><button class="btn btn-default" style="border-bottom-left-radius: 0;border-top-left-radius: 0;"><i class="glyphicon glyphicon-calendar"></i></button></span></div><div ng-show="toForm.toModel.$invalid" class = "error,help-block ng-hide" ><span class="help-block" ng-show="toForm.toModel.$error.required && !(toForm.toModel.$error.date || toForm.toModel.$error.min || toForm.toModel.$error.max)">{{fromLabel}}{{ entity +  " not entered" | translate }}</span><span class="help-block" ng-show="toForm.toModel.$error.date">{{ entity +  "Invalid date format" | translate }}</span><span class="help-block" ng-show="toForm.toModel.$error.min">{{ entity +  "Violates minimum constraint" | translate }}</span><span class="help-block" ng-show="toForm.toModel.$error.max">{{ entity +  "Violates maximum constraint" | translate }}</span></span></div></div></div></div></div></div><div ng-if="isTimeRange" ng-init="initDateTimePopup()"><div class="row"><div ng-class="inputclass"><div  class="dropdown keep-open"><a style="cursor:default;text-decoration: none" data-toggle="dropdown"><div ng-if="ngRequired" ng-form="fromForm"><div ng-class="{\'has-error\': submitted && fromForm.fromModel.$invalid}"><div class="input-group" ><input type="text" id="fromModel"  name="fromModel" class="form-control"   ng-model="fromModel"  required><span id="fromTimeSpan" class="input-group-addon"><i id="fromTimeI" class="glyphicon glyphicon-time"></i></span></div><div ng-show="submitted && fromForm.fromModel.$invalid" class = "error,help-block ng-hide" ><span class = "help-block" ng-show = "fromForm.fromModel.$error.required" > {{fromLabel}}{{ entity +  " not entered" | translate }}</span></div></div></div><div ng-if="!ngRequired"><div class="input-group" ><input type="text" id="fromModel"  name="fromModel" class="form-control"   ng-model="fromModel"><span id="fromTimeSpan" class="input-group-addon"><i id="fromTimeI" class="glyphicon glyphicon-time"></i></span></div></div></a><ul class="dropdown-menu"><li class="text-center"><timepicker remove-required="{{ngRequired}}" align="center" ng-model="$parent.fromTimeModel" defaultTime="false" hour-step="hstep" minute-step="mstep" show-meridian="true" ng-change=setFromTime(fromTimeModel) ></timepicker><button class="btn btn-hkg" ng-click=setDateAndTime("fromTime")>Clear</button></li></ul></div></div> <label ng-class="labelclass">{{toLabel}}</label><div ng-class="inputclass"><div  class="dropdown keep-open"><a style="cursor:default;text-decoration: none"  data-toggle="dropdown"><div ng-if="ngRequired" ng-form="toForm"><div ng-class="{\'has-error\': submitted && toForm.toModel.$invalid}"><div class="input-group" ><input type="text" id="toModel"  name="toModel" class="form-control"   ng-model="toModel"  required><span id="toTimeSpan" class="input-group-addon"><i id="toTimeI" class="glyphicon glyphicon-time"></i></span></div><div ng-show="submitted && toForm.toModel.$invalid" class = "error,help-block ng-hide" ><span class = "help-block" ng-show = "toForm.toModel.$error.required" > {{toLabel}}{{ entity +  " not entered" | translate }}</span></div></div></div><div ng-if="!ngRequired"><div class="input-group" ><input type="text" id="toModel"  name="toModel" class="form-control"   ng-model="toModel" ><span id="toTimeSpan" class="input-group-addon"><i id="toTimeI" class="glyphicon glyphicon-time"></i></span></div></div></a><ul class="dropdown-menu"><li class="text-center"><div><timepicker remove-required="{{ngRequired}}" align="center" ng-model="$parent.toTimeModel" defaultTime="false" hour-step="hstep" minute-step="mstep" show-meridian="true" ng-change=setToTime(toTimeModel) ></timepicker><button class="btn btn-hkg" ng-click=setDateAndTime("toTime")>Clear</button></div></li></ul></div></div></div></div><div ng-if="isDateTimeRange" ng-init="initDateTimePopup()"><div  class="row" ><div ng-class="inputclass"><div  class="dropdown keep-open"><div ng-form="fromForm"><div ng-class="{\'has-error\': fromForm.fromModel.$invalid}"><div class="input-group" ><input date-validate validate-format="{{directiveFormat}}" placeholder="{{directiveFormat}}" min="minDate" max="toDatepickerModel" type="text" id="fromModel" name="fromModel" class="form-control"   ng-model="$parent.$parent.fromModel"  ng-required="ngRequired"><span id="frmDt" class="input-group-btn" data-toggle="dropdown" ng-click = "datePickerFlag.fromIsOpen = true;"><button class="btn btn-default" style="border-bottom-left-radius: 0;border-top-left-radius: 0;"><i id="fromI" class="glyphicon glyphicon-calendar"></i></button></span><ul class="dropdown-menu" role="menu" aria-labelledby="dLabel"><li><datepicker is-open="datePickerFlag.fromIsOpen" id="rangeFromDatepickerModel" ng-model="$parent.rangeFromDatepickerModel" min="minDate" max="toDatepickerModel" show-weeks="true" class="well well-sm" ng-change="setRangeFromDate(rangeFromDatepickerModel,rangFromTimeModel)"></datepicker></li> <li class="text-center"><timepicker remove-required="{{ngRequired}}" align="center" ng-model="$parent.rangFromTimeModel" hour-step=" " minute-step=" " show-meridian="true" ng-change="setRangeFromTime(rangFromTimeModel,rangeFromDatepickerModel)"></timepicker><button class="btn btn-hkg" ng-click=setDateAndTime("rangeFromClear")>Clear</button>&nbsp;&nbsp;<button class="btn btn-hkg" ng-click="setDateAndTime()">Close</button></li></div></ul><div ng-show="fromForm.fromModel.$invalid" class = "error,help-block ng-hide" ><span class="help-block" ng-show="fromForm.fromModel.$error.required && !(fromForm.fromModel.$error.date || fromForm.fromModel.$error.min || fromForm.fromModel.$error.max)">{{fromLabel}}{{ entity +  " not entered" | translate }}</span><span class="help-block" ng-show="fromForm.fromModel.$error.date">{{ entity +  "Invalid date format" | translate }}</span><span class="help-block" ng-show="fromForm.fromModel.$error.min">{{ entity +  "Violates minimum constraint" | translate }}</span><span class="help-block" ng-show="fromForm.fromModel.$error.max">{{ entity +  "Violates maximum constraint" | translate }}</span></span></div></div></div></div></div><label ng-class="labelclass">To</label> <div ng-class="inputclass"><div  class="dropdown keep-open" id="toDDD"><div ng-form="toForm"><div ng-class="{\'has-error\':  toForm.toModel.$invalid}"><div class="input-group" ><input date-validate validate-format="{{directiveFormat}}" placeholder="{{directiveFormat}}" min="rangeFromDatepickerModel" max="maxDate" type="text" id="toModel" name="toModel" class="form-control"   ng-model="$parent.$parent.toModel"  ng-required="ngRequired"><span id="toDt" class="input-group-btn" data-toggle="dropdown" ng-click = "datePickerFlag.toIsOpen = true;"><button class="btn btn-default" style="border-bottom-left-radius: 0;border-top-left-radius: 0;"><i id="toI" class="glyphicon glyphicon-calendar" ></i></button></span><ul class="dropdown-menu" role="menu" aria-labelledby="dLabel" ><li><datepicker is-open="datePickerFlag.toIsOpen" id="toDatepickerModel" ng-model="$parent.toDatepickerModel" min="rangeFromDatepickerModel" max="maxDate" show-weeks="true" class="well well-sm" ng-change="setRangeToDate(toDatepickerModel,rangeToTimeModel)"></datepicker></li> <li class="text-center"> <timepicker remove-required="{{ngRequired}}" align="center" ng-model="$parent.rangeToTimeModel" hour-step=" " minute-step=" " show-meridian="true" ng-change="setRangeToTime(rangeToTimeModel,toDatepickerModel)"></timepicker><button class="btn btn-hkg" ng-click=setDateAndTime("rangeToClear")>Clear</button>&nbsp;&nbsp;<button class="btn btn-hkg" ng-click="setDateAndTime()">Close</button></li></div></ul><div ng-show="toForm.toModel.$invalid" class = "error,help-block ng-hide" ><span class="help-block" ng-show="toForm.toModel.$error.required && !(toForm.toModel.$error.date || toForm.toModel.$error.min || toForm.toModel.$error.max)">{{toLabel}}{{ entity +  " not entered" | translate }}</span><span class="help-block" ng-show="toForm.toModel.$error.date">{{ entity +  "Invalid date format" | translate }}</span><span class="help-block" ng-show="toForm.toModel.$error.min">{{ entity +  "Violates minimum constraint" | translate }}</span><span class="help-block" ng-show="toForm.toModel.$error.max">{{ entity +  "Violates maximum constraint" | translate }}</span></span></div></div></div></div></div></div></div></div>',scope:{ngModel:"=",fromModel:"=",toModel:"=",submitted:"=",form:"=",name:"="},link:function(q,i,m){q.currentDate=new Date();
q.datePickerFlag={};
q.datePickerFlag.dateTimePicker=false;
q.datePickerFlag.fromIsOpen=false;
q.datePickerFlag.toIsOpen=false;
if(m.format!==undefined){q.directiveFormat=m.format
}else{q.directiveFormat="dd/MM/yyyy"
}q.$watch("ngModel",function(s){if(!s||s===null){}else{if(m.type==="time"||m.type==="datetime"){if(!isNaN(q.ngModel)){q.ngModel=e(s,m.format)
}else{q.datepickerModel=new Date(t(q.ngModel,m.format));
q.timeModel=new Date(t(q.ngModel,m.format))
}}}});
q.$watch("fromModel",function(s){if(!s||s===null){}else{if(m.type==="daterange"){if(angular.isDate(q.fromModel)&&(q.toModel===undefined||q.toModel===null)){q.toModel=new Date(q.fromModel)
}}if(m.type==="timerange"||m.type==="datetimerange"){if(!isNaN(q.fromModel)){q.fromModel=e(s,m.format)
}else{q.rangeFromDatepickerModel=new Date(t(q.fromModel,m.format));
q.rangFromTimeModel=new Date(t(q.fromModel,m.format))
}}}});
q.$watch("toModel",function(s){if(!s||s===null){}else{if(m.type==="daterange"){if(angular.isDate(q.toModel)&&(q.fromModel===undefined||q.fromModel===null)){q.fromModel=new Date(q.toModel)
}}if(m.type==="timerange"||m.type==="datetimerange"){if(!isNaN(q.toModel)){q.toModel=e(s,m.format)
}else{q.rangeToTimeModel=new Date(t(q.toModel,m.format));
q.toDatepickerModel=new Date(t(q.toModel,m.format))
}}}});
if((angular.isDefined(m.disableFlag)&&m.disableFlag==="true")||m.disableFlag===true){q.isDisabled=true
}else{q.isDisabled=false
}if(angular.isDefined(m.form)){q.formName=m.form
}if(angular.isDefined(m.inputClass)){q.inputclass=m.inputClass
}else{q.inputclass="col-md-3"
}if(angular.isDefined(m.labelClass)){q.labelclass=m.labelClass
}else{q.labelclass="col-md-1 col-lg-2 col-sm-4 control-label"
}var t=function(s,u){if(!angular.isDefined(u)){u="dd/MM/yyyy hh:mm a"
}return getDateFromFormat(s,u)
};
var e=function(s,u){if(u===undefined){u="dd/MM/yyyy"
}return c("date")(s,u)
};
var n=function(s){if(s!==undefined){q.ngModel=e(s,"shortTime")
}else{q.ngModel=new Date()
}};
var o=function(B,w){if(angular.isDefined(B)){var y=new Date(B);
var u=(new Date().getHours()<10)?"0"+new Date().getHours():new Date().getHours();
var x=(new Date().getMinutes()<10)?"0"+new Date().getMinutes():new Date().getMinutes();
y.setHours(u);
y.setMinutes(x)
}if(w!==undefined){var z=e(B,"H:m"),A=z.match(/(\d+)\:(\d+)/),u=parseInt(A[1],10),x=parseInt(A[2],10);
var v=new Date(w);
v.setHours(u);
v.setMinutes(x);
return v
}return y
};
if((m.type&&m.type==="date")){q.isDate=true;
if(m.format!==undefined){q.dateFormat=m.format
}if(m.ngRequired&&(m.ngRequired==="true"||m.ngRequired===true)){q.ngRequired=true
}else{q.ngRequired=false
}if(angular.isDefined(m.label)){q.dateLabel=m.label
}}else{if(m.type==="datetime"){q.isDateTime=true;
if(m.ngRequired&&(m.ngRequired==="true"||m.ngRequired===true)){q.ngRequired=true
}else{q.ngRequired=false
}if(angular.isDefined(m.label)){q.dateTimeLabel=m.label
}}else{if(m.type==="time"){q.isTime=true;
if(m.ngRequired&&(m.ngRequired==="true"||m.ngRequired===true)){q.ngRequired=true
}else{q.ngRequired=false
}if(angular.isDefined(m.defaulttime)&&m.defaulttime==="true"){q.ngModel=e(new Date,"shortTime")
}}else{if(m.type==="daterange"){console.log("in dtae range");
q.isDateRange=true;
q.name=m.name;
if(angular.isDefined(m.formName)){}if(m.format!==undefined){q.dateRangeFormat=m.format
}if(m.fromLabel!==undefined&&m.fromLabel.length>0){q.fromLabel=m.fromLabel
}if(m.toLabel!==undefined&&m.toLabel.length>0){q.toLabel=m.toLabel
}if(m.ngRequired&&(m.ngRequired==="true"||m.ngRequired===true)){q.ngRequired=true
}else{q.ngRequired=false
}q.fromModel=new Date();
var j=new Date();
q.fromModel=new Date(new Date().setMonth(j.getMonth()-1));
q.toModel=new Date()
}else{if(m.type==="timerange"){q.isTimeRange=true;
if(m.ngRequired&&(m.ngRequired==="true"||m.ngRequired===true)){q.ngRequired=true
}else{q.ngRequired=false
}if(m.fromLabel!==undefined&&m.fromLabel.length>0){q.fromLabel=m.fromLabel
}if(m.toLabel!==undefined&&m.toLabel.length>0){q.toLabel=m.toLabel
}if(m.defaulttime!==undefined&&m.defaulttime==="true"){q.fromModel=e(new Date(),"shortTime");
q.toModel=e(new Date(),"shortTime")
}}else{if(m.type==="datetimerange"){q.isDateTimeRange=true;
if(m.ngRequired&&(m.ngRequired==="true"||m.ngRequired===true)){q.ngRequired=true
}else{q.ngRequired=false
}if(m.fromLabel!==undefined&&m.fromLabel.length>0){q.fromLabel=m.fromLabel
}if(m.toLabel!==undefined&&m.toLabel.length>0){q.toLabel=m.toLabel
}if(m.defaultdate!==undefined&&m.defaultdate==="true"&&!q.fromModel){var l=new Date();
if(angular.isDefined(m.adddaystodefaultdate)){var h=parseInt(m.adddaystodefaultdate);
l.setDate(l.getDate()+h)
}if(angular.isDefined(m.defaultstarttime)){var r=m.defaultstarttime,g=r.match(/(\d+)\.(\d+) (\w+)/),k=/am/i.test(g[3])?parseInt(g[1],10):parseInt(g[1],10)+12,f=parseInt(g[2],10);
l.setHours(k);
l.setMinutes(f);
q.rangFromTimeModel=new Date(l);
q.rangeFromDatepickerModel=new Date(l)
}q.fromModel=e(l,m.format);
if(angular.isDefined(m.defaultendtime)){var r=m.defaultendtime,g=r.match(/(\d+)\.(\d+) (\w+)/),k=/am/i.test(g[3])?parseInt(g[1],10):parseInt(g[1],10)+12,f=parseInt(g[2],10);
l.setHours(k);
l.setMinutes(f);
q.rangeToTimeModel=l;
q.toDatepickerModel=l
}q.toModel=e(l,m.format)
}else{if(q.fromModel){q.rangFromTimeModel=new Date(q.fromModel);
q.rangeFromDatepickerModel=new Date(q.fromModel);
q.fromModel=e(q.fromModel,m.format)
}if(q.toModel){q.rangeToTimeModel=new Date(q.toModel);
q.toModel=e(q.toModel,m.format);
q.toDatepickerModel=new Date(q.rangeToTimeModel)
}}}}}}}}q.initDateTimePopup=function(){$(i).bind("click",function(u){u=u||window.event;
var s=u.target
});
$(i).find(".dropdown.keep-open").on({"shown.bs.dropdown":function(){this.closable=false
},click:function(s){if(q.closePopup){this.closable=true;
q.closePopup=false
}else{this.closable=false
}},"hide.bs.dropdown":function(){return this.closable
}})
};
q.setDateAndTime=function(s,u){switch(s){case"datetime":q.ngModel=undefined;
q.datepickerModel=undefined;
q.timeModel=undefined;
break;
case"fromTime":q.fromTimeModel=new Date();
q.fromModel=undefined;
break;
case"toTime":q.toTimeModel=new Date();
q.toModel=undefined;
case"rangeFromClear":q.rangFromTimeModel=new Date();
q.fromModel=undefined;
break;
case"rangeToClear":q.rangeToTimeModel=new Date();
q.toModel=undefined
}q.closePopup=true
};
function p(u){u=u||window.event;
var s=u.target;
if(!$(i).is(u.target)&&$(i).has(u.target).length===0&&$(".open").has(u.target).length===0&&document.contains(s)){q.datePickerFlag.dateTimePicker=false;
q.datePickerFlag.fromIsOpen=false;
q.datePickerFlag.toIsOpen=false;
q.$digest();
$(".open").removeClass("open")
}else{}}$("body").bind("click",p);
q.$on("$destroy",function(){$("body").unbind("click",p)
});
q.setOnlyDate=function(s){q.ngModel=s
};
q.setFromDate=function(s){q.fromModel=s;
q.toMinDate=s
};
q.setToDate=function(s){q.toModel=s;
q.fromMaxDate=s
};
q.setFromTime=function(s){q.fromModel=e(s,"shortTime")
};
q.setToTime=function(s){q.toModel=e(s,"shortTime")
};
q.setRangeFromDate=function(s,v){var u;
if(angular.isDefined(v)){u=o(v,s)
}else{var u=o(s)
}q.fromModel=e(u,m.format);
q.rangeFromDatepickerModel=u
};
q.setRangeToDate=function(s,v){var u;
if(angular.isDefined(v)){u=o(v,s)
}else{var u=o(s)
}q.toModel=e(u,m.format);
q.rangToTimeModel=u;
q.toDatepickerModel=u
};
q.setRangeFromTime=function(v,s){if(!s){s=new Date()
}var u=o(v,s);
q.fromModel=e(u,m.format);
q.rangeFromDatepickerModel=u
};
q.setRangeToTime=function(v,s){if(!s){s=new Date()
}var u=o(v,s);
q.toModel=e(u,m.format);
q.toDatepickerModel=u
};
q.dtPicker=[];
q.open=function(s,u){q.dtPicker=[];
s.preventDefault();
s.stopPropagation();
q.dtPicker[u]=true;
$(".open").removeClass("open")
};
q.setTime=function(y,w){if(!w){w=new Date()
}if(w!==undefined){var x=new Date(y);
var s=(x.getHours()<10)?"0"+x.getHours():x.getHours();
var v=(x.getMinutes()<10)?"0"+x.getMinutes():x.getMinutes();
var u=new Date(w);
u.setHours(s);
u.setMinutes(v);
q.ngModel=e(u,m.format)
}else{n(y)
}};
q.daysInMonth=function(u,s){return new Date(s,u,0).getDate()
};
q.setDate=function(u,x){var w=o(u);
if(x){var s=(x.getHours()<10)?"0"+x.getHours():x.getHours();
var v=(x.getMinutes()<10)?"0"+x.getMinutes():x.getMinutes();
w.setHours(s);
w.setMinutes(v)
}q.ngModel=e(w,m.format);
q.timeModel=w
};
q.hstep=1;
q.mstep=30;
q.ismeridian=false
}}
}])
});