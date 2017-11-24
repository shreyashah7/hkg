define(["angular","dateValidate"],function(){angular.module("hkg.directives").directive("agDatetimePicker",["$compile","$filter",function(a,b){return{restrict:"E",template:'<div><div ng-if="isDateTime" ng-init="initDateTimePopup()"><div  class="dropdown keep-open"><div class="input-group" ><input date-validate validate-format="{{directiveFormat}}" placeholder="{{directiveFormat}}" min="dateTimeMin" max="dateTimeMax" type="text" id="ngModel" name="ngModel" class="form-control" ng-model="$parent.$parent.ngModel"><span class="input-group-btn" id="dateTimeSpan" data-toggle="dropdown"  ng-click="datePickerFlag.dateTimePicker = true;"><button ng-disabled="isDisabled" class="btn btn-default" style="border-bottom-left-radius: 0;border-top-left-radius: 0;"><i class="glyphicon glyphicon-calendar" id="dateTimeI"/></button></span><ul class="dropdown-menu"><li><datepicker is-open="datePickerFlag.dateTimePicker" id="dd" ng-model="$parent.datepickerModel"  min="dateTimeMin" max="dateTimeMax" ng-change="setDate(datepickerModel,timeModel)"></datepicker></li><li class="text-center"><timepicker remove-required="{{ngRequired}}" align="center" ng-model="$parent.timeModel" hour-step="hstep" minute-step="mstep" show-meridian="true" ng-change="setTime(timeModel,datepickerModel)"></timepicker><button class="btn btn-hkg" ng-click=setDateAndTime("datetime",timeModel)>Clear</button>&nbsp;&nbsp;<button class="btn btn-hkg" ng-click="setDateAndTime()">Close</button></li></ul></div></div></div><div ng-if="isDate"><div class="input-group" ><input type="text" id="ngModel" is-open="$parent.dtPicker.dt_open"  name="ngModel" class="form-control" datepicker-popup="{{dateFormat}}"  ng-model="ngModel" min="onlyMinDate" max="onlyMaxDate" ng-change=setOnlyDate(ngModel) placeholder="{{dateFormat}}"><span class="input-group-btn" ng-click=(isDisabled||open($event,"dt_open"))><button ng-disabled="isDisabled" class="btn btn-default"><i class="glyphicon glyphicon-calendar" ></i></button></span></div></div><div ng-if="isTime" ng-init="initDateTimePopup()"><div  class="dropdown keep-open"><a style="cursor:default;text-decoration: none"  data-toggle="dropdown"><div class="input-group" ><input type="text" id="ngModel"  name="ngModel" class="form-control"   ng-model="ngModel"  ><span id="timeSpan" class="input-group-addon"><i id="timeI" class="glyphicon glyphicon-time"></i></span></div></a><ul class="dropdown-menu"><li class="text-center"><timepicker remove-required="{{ngRequired}}" align="center" ng-model="$parent.timeModel" defaultTime="false" hour-step="hstep" minute-step="mstep" show-meridian="true" ng-change=setTime(timeModel) ></timepicker><button class="btn btn-hkg" ng-click=setDateAndTime("datetime")>Clear</button></li></ul></div>\n                    </div><div ng-if="isDateRange" ng-init="initDateTimePopup()"><div  class="row" ><div ng-class="inputclass"><div ng-form="fromForm"><div ng-class="{\'has-error\': fromForm.fromModel.$invalid}"><div class="input-group" ><input type="text" id="fromModel" is-open="$parent.dtPicker.open_from"  name="fromModel" class="form-control" placeholder="{{dateRangeFormat}}" datepicker-popup="{{dateRangeFormat}}"  ng-model="$parent.fromModel" min="fromMinDate" max="fromMaxDate" ng-change="setFromDate(fromModel)" ng-required="ngRequired"><span class="input-group-btn" ng-click=(isDisabled||open($event,"open_from"))><button ng-disabled="isDisabled" class="btn btn-default"><i class="glyphicon glyphicon-calendar"></i></button></span></div><div ng-show="fromForm.fromModel.$invalid" class = "error,help-block ng-hide" ><span class="help-block" ng-show="fromForm.fromModel.$error.required && !(fromForm.fromModel.$error.date || fromForm.fromModel.$error.min || fromForm.fromModel.$error.max)">{{fromLabel}}{{ entity +  " not entered" | translate }}</span><span class="help-block" ng-show="fromForm.fromModel.$error.date">{{ entity +  "Invalid date format" | translate }}</span><span class="help-block" ng-show="fromForm.fromModel.$error.min">{{ entity +  "Violates minimum constraint" | translate }}</span><span class="help-block" ng-show="fromForm.fromModel.$error.max">{{ entity +  "Violates maximum constraint" | translate }}</span></span></div></div></div></div> <label ng-class="labelclass">{{toLabel}}</label><div ng-class="inputclass"><div ng-form="toForm"><div ng-class="{\'has-error\': toForm.toModel.$invalid}"><div class="input-group" ><input type="text" id="toModel" is-open="$parent.dtPicker.open_to"  name="toModel" class="form-control" min="toMinDate" placeholder="{{dateRangeFormat}}" max="toMaxDate" datepicker-popup="{{dateRangeFormat}}"  ng-model="$parent.toModel" ng-change="setToDate(toModel)" ng-required="ngRequired"><span class="input-group-btn" ng-click=(isDisabled||open($event,"open_to"))><button class="btn btn-default" style="border-bottom-left-radius: 0;border-top-left-radius: 0;"><i class="glyphicon glyphicon-calendar"></i></button></span></div><div ng-show="toForm.toModel.$invalid" class = "error,help-block ng-hide" ><span class="help-block" ng-show="toForm.toModel.$error.required && !(toForm.toModel.$error.date || toForm.toModel.$error.min || toForm.toModel.$error.max)">{{fromLabel}}{{ entity +  " not entered" | translate }}</span><span class="help-block" ng-show="toForm.toModel.$error.date">{{ entity +  "Invalid date format" | translate }}</span><span class="help-block" ng-show="toForm.toModel.$error.min">{{ entity +  "Violates minimum constraint" | translate }}</span><span class="help-block" ng-show="toForm.toModel.$error.max">{{ entity +  "Violates maximum constraint" | translate }}</span></span></div></div></div></div></div></div><div ng-if="isTimeRange" ng-init="initDateTimePopup()"><div class="row"><div ng-class="inputclass"><div  class="dropdown keep-open"><a style="cursor:default;text-decoration: none" data-toggle="dropdown"><div ng-if="ngRequired" ng-form="fromForm"><div ng-class="{\'has-error\': submitted && fromForm.fromModel.$invalid}"><div class="input-group" ><input type="text" id="fromModel"  name="fromModel" class="form-control"   ng-model="fromModel"  required><span id="fromTimeSpan" class="input-group-addon"><i id="fromTimeI" class="glyphicon glyphicon-time"></i></span></div><div ng-show="submitted && fromForm.fromModel.$invalid" class = "error,help-block ng-hide" ><span class = "help-block" ng-show = "fromForm.fromModel.$error.required" > {{fromLabel}}{{ entity +  " not entered" | translate }}</span></div></div></div><div ng-if="!ngRequired"><div class="input-group" ><input type="text" id="fromModel"  name="fromModel" class="form-control"   ng-model="fromModel"><span id="fromTimeSpan" class="input-group-addon"><i id="fromTimeI" class="glyphicon glyphicon-time"></i></span></div></div></a><ul class="dropdown-menu"><li class="text-center"><timepicker remove-required="{{ngRequired}}" align="center" ng-model="$parent.fromTimeModel" defaultTime="false" hour-step="hstep" minute-step="mstep" show-meridian="true" ng-change=setFromTime(fromTimeModel) ></timepicker><button class="btn btn-hkg" ng-click=setDateAndTime("fromTime")>Clear</button></li></ul></div></div> <label ng-class="labelclass">{{toLabel}}</label><div ng-class="inputclass"><div  class="dropdown keep-open"><a style="cursor:default;text-decoration: none"  data-toggle="dropdown"><div ng-if="ngRequired" ng-form="toForm"><div ng-class="{\'has-error\': submitted && toForm.toModel.$invalid}"><div class="input-group" ><input type="text" id="toModel"  name="toModel" class="form-control"   ng-model="toModel"  required><span id="toTimeSpan" class="input-group-addon"><i id="toTimeI" class="glyphicon glyphicon-time"></i></span></div><div ng-show="submitted && toForm.toModel.$invalid" class = "error,help-block ng-hide" ><span class = "help-block" ng-show = "toForm.toModel.$error.required" > {{toLabel}}{{ entity +  " not entered" | translate }}</span></div></div></div><div ng-if="!ngRequired"><div class="input-group" ><input type="text" id="toModel"  name="toModel" class="form-control"   ng-model="toModel" ><span id="toTimeSpan" class="input-group-addon"><i id="toTimeI" class="glyphicon glyphicon-time"></i></span></div></div></a><ul class="dropdown-menu"><li class="text-center"><div><timepicker remove-required="{{ngRequired}}" align="center" ng-model="$parent.toTimeModel" defaultTime="false" hour-step="hstep" minute-step="mstep" show-meridian="true" ng-change=setToTime(toTimeModel) ></timepicker><button class="btn btn-hkg" ng-click=setDateAndTime("toTime")>Clear</button></div></li></ul></div></div></div></div><div ng-if="isDateTimeRange" ng-init="initDateTimePopup()"><div  class="row" ><div ng-class="inputclass"><div  class="dropdown keep-open"><div ng-form="fromForm"><div ng-class="{\'has-error\': submitted && fromForm.fromModel.$invalid}"><div class="input-group" ><input date-validate validate-format="{{directiveFormat}}" placeholder="{{directiveFormat}}" min="minDate" max="toDatepickerModel" type="text" id="fromModel" name="fromModel" class="form-control"   ng-model="$parent.$parent.fromModel"  ng-required="ngRequired"><span id="frmDt" class="input-group-btn" data-toggle="dropdown" ng-click = "datePickerFlag.fromIsOpen = true;"><button class="btn btn-default" style="border-bottom-left-radius: 0;border-top-left-radius: 0;"><i id="fromI" class="glyphicon glyphicon-calendar"></i></button></span><ul class="dropdown-menu" role="menu" aria-labelledby="dLabel"><li><datepicker is-open="datePickerFlag.fromIsOpen" id="rangeFromDatepickerModel" ng-model="$parent.rangeFromDatepickerModel" min="minDate" max="toDatepickerModel" show-weeks="true" class="well well-sm" ng-change="setRangeFromDate(rangeFromDatepickerModel,rangFromTimeModel)"></datepicker></li> <li class="text-center"><timepicker remove-required="{{ngRequired}}" align="center" ng-model="$parent.rangFromTimeModel" hour-step=" " minute-step=" " show-meridian="true" ng-change="setRangeFromTime(rangFromTimeModel,rangeFromDatepickerModel)"></timepicker><button class="btn btn-hkg" ng-click=setDateAndTime("rangeFromClear")>Clear</button>&nbsp;&nbsp;<button class="btn btn-hkg" ng-click="setDateAndTime()">Close</button></li></div></ul><div ng-show="ngRequired && submitted && fromForm.fromModel.$invalid" class = "error,help-block ng-hide" ><span class="help-block" ng-show="fromForm.fromModel.$error.required && !(fromForm.fromModel.$error.date || fromForm.fromModel.$error.min || fromForm.fromModel.$error.max)">{{fromLabel}}{{ entity +  "From date not entered" | translate }}</span><span class="help-block" ng-show="fromForm.fromModel.$error.date">{{ entity +  "Invalid date format" | translate }}</span><span class="help-block" ng-show="fromForm.fromModel.$error.min">{{ entity +  "Violates minimum constraint" | translate }}</span><span class="help-block" ng-show="fromForm.fromModel.$error.max">{{ entity +  "Violates maximum constraint" | translate }}</span></span></div></div></div></div></div><label ng-class="labelclass">To</label> <div ng-class="inputclass"><div  class="dropdown keep-open" id="toDDD"><div ng-form="toForm"><div ng-class="{\'has-error\': submitted && toForm.toModel.$invalid}"><div class="input-group" ><input date-validate validate-format="{{directiveFormat}}" placeholder="{{directiveFormat}}" min="rangeFromDatepickerModel" max="maxDate" type="text" id="toModel" name="toModel" class="form-control"   ng-model="$parent.$parent.toModel"  ng-required="ngRequired"><span id="toDt" class="input-group-btn" data-toggle="dropdown" ng-click = "datePickerFlag.toIsOpen = true;"><button class="btn btn-default" style="border-bottom-left-radius: 0;border-top-left-radius: 0;"><i id="toI" class="glyphicon glyphicon-calendar" ></i></button></span><ul class="dropdown-menu" role="menu" aria-labelledby="dLabel" ><li><datepicker is-open="datePickerFlag.toIsOpen" id="toDatepickerModel" ng-model="$parent.toDatepickerModel" min="rangeFromDatepickerModel" max="maxDate" show-weeks="true" class="well well-sm" ng-change="setRangeToDate(toDatepickerModel,rangeToTimeModel)"></datepicker></li> <li class="text-center"> <timepicker remove-required="{{ngRequired}}" align="center" ng-model="$parent.rangeToTimeModel" hour-step=" " minute-step=" " show-meridian="true" ng-change="setRangeToTime(rangeToTimeModel,toDatepickerModel)"></timepicker><button class="btn btn-hkg" ng-click=setDateAndTime("rangeToClear")>Clear</button>&nbsp;&nbsp;<button class="btn btn-hkg" ng-click="setDateAndTime()">Close</button></li></div></ul><div ng-show="ngRequired && submitted && toForm.toModel.$invalid" class = "error,help-block ng-hide" ><span class="help-block" ng-show="toForm.toModel.$error.required && !(toForm.toModel.$error.date || toForm.toModel.$error.min || toForm.toModel.$error.max)">{{toLabel}}{{ entity +  "To date not entered" | translate }}</span><span class="help-block" ng-show="toForm.toModel.$error.date">{{ entity +  "Invalid date format" | translate }}</span><span class="help-block" ng-show="toForm.toModel.$error.min">{{ entity +  "Violates minimum constraint" | translate }}</span><span class="help-block" ng-show="toForm.toModel.$error.max">{{ entity +  "Violates maximum constraint" | translate }}</span></span></div></div></div></div></div></div></div></div>',scope:{ngModel:"=",fromModel:"=",toModel:"=",submitted:"=",form:"=",name:"="},link:function(n,g,j){n.datePickerFlag={};
n.datePickerFlag.dateTimePicker=false;
n.datePickerFlag.fromIsOpen=false;
n.datePickerFlag.toIsOpen=false;
n.currentDate=new Date();
if(j.format!==undefined){n.directiveFormat=j.format
}else{n.directiveFormat="dd/MM/yyyy hh:mm a"
}n.$watch("ngModel",function(q){if(!q||q===null){n.datepickerModel=undefined;
n.timeModel=undefined
}else{if(j.type==="time"||j.type==="datetime"){if(!isNaN(n.ngModel)){n.ngModel=c(q,j.format)
}else{n.datepickerModel=new Date(p(n.ngModel,j.format));
n.timeModel=new Date(p(n.ngModel,j.format))
}}}});
n.$watch("fromModel",function(q){if(!q||q===null){n.rangeFromDatepickerModel=undefined;
n.rangFromTimeModel=undefined
}else{if(j.type==="timerange"||j.type==="datetimerange"){if(!isNaN(n.fromModel)){n.fromModel=c(q,j.format)
}else{n.rangeFromDatepickerModel=new Date(p(n.fromModel,j.format));
n.rangFromTimeModel=new Date(p(n.fromModel,j.format))
}}}});
n.$watch("toModel",function(q){if(!q||q===null){n.rangeToTimeModel=undefined;
n.toDatepickerModel=undefined
}else{if(j.type==="timerange"||j.type==="datetimerange"){if(!isNaN(n.toModel)){n.toModel=c(q,j.format)
}else{n.rangeToTimeModel=new Date(p(n.toModel,j.format));
n.toDatepickerModel=new Date(p(n.toModel,j.format))
}}}});
if(angular.isDefined(j.form)){n.formName=j.form
}if(angular.isDefined(j.inputClass)){n.inputclass=j.inputClass
}else{n.inputclass="col-md-3"
}if(angular.isDefined(j.labelClass)){n.labelclass=j.labelClass
}else{n.labelclass="col-md-1 col-lg-2 col-sm-4 control-label"
}var p=function(q,r){if(!angular.isDefined(r)){r="dd/MM/yyyy hh:mm a"
}return getDateFromFormat(q,r)
};
var c=function(q,r){if(r===undefined){r="dd/MM/yyyy hh:mm a"
}return b("date")(q,r)
};
var k=function(q){if(q!==undefined){n.ngModel=c(q,"shortTime")
}else{n.ngModel=new Date()
}};
var l=function(y,t){if(angular.isDefined(y)){var v=new Date(y);
var q=(new Date().getHours()<10)?"0"+new Date().getHours():new Date().getHours();
var u=(new Date().getMinutes()<10)?"0"+new Date().getMinutes():new Date().getMinutes();
v.setHours(q);
v.setMinutes(u)
}if(t!==undefined){var w=c(y,"H:m"),x=w.match(/(\d+)\:(\d+)/),q=parseInt(x[1],10),u=parseInt(x[2],10);
var r=new Date(t);
r.setHours(q);
r.setMinutes(u);
return r
}return v
};
if((j.type&&j.type==="date")){n.isDate=true;
if(j.format!==undefined){n.dateFormat=j.format
}if(j.defaultdate!==undefined&&j.defaultdate==="true"){n.ngModel=new Date()
}if(j.ngRequired&&(j.ngRequired==="true"||j.ngRequired===true)){n.ngRequired=true
}else{n.ngRequired=false
}if(angular.isDefined(j.label)){n.dateLabel=j.label
}if(j.allow==="past"){n.onlyMaxDate=new Date()
}else{if(j.allow==="future"){n.onlyMinDate=new Date()
}else{if(j.allow==="onlyfuture"){n.onlyMinDate=new Date().setDate(new Date().getDate()+1)
}else{if(j.allow==="onlypast"){n.onlyMaxDate=new Date().setDate(new Date().getDate()-1)
}else{if(j.allow==="current"){n.onlyMaxDate=new Date();
n.onlyMinDate=new Date()
}else{}}}}}}else{if(j.type==="datetime"){n.isDateTime=true;
if(angular.isDefined(j.defaultdate)&&j.defaultdate==="true"){n.datepickerModel=new Date();
n.ngModel=c(new Date(),j.format)
}if(j.ngRequired&&(j.ngRequired==="true"||j.ngRequired===true)){n.ngRequired=true
}else{n.ngRequired=false
}if(angular.isDefined(j.label)){n.dateTimeLabel=j.label
}if(j.allow==="past"){n.dateTimeMax=new Date()
}else{if(j.allow==="future"){n.dateTimeMin=new Date()
}else{if(j.allow==="onlyfuture"){n.dateTimeMin=new Date().setDate(new Date().getDate()+1)
}else{if(j.allow==="onlypast"){n.dateTimeMax=new Date().setDate(new Date().getDate()-1)
}else{if(j.allow==="current"){n.dateTimeMax=new Date();
n.dateTimeMin=new Date()
}else{}}}}}}else{if(j.type==="time"){n.isTime=true;
if(j.ngRequired&&(j.ngRequired==="true"||j.ngRequired===true)){n.ngRequired=true
}else{n.ngRequired=false
}if(angular.isDefined(j.defaulttime)&&j.defaulttime==="true"){n.ngModel=c(new Date,"shortTime")
}}else{if(j.type==="daterange"){n.isDateRange=true;
n.name=j.name;
if(angular.isDefined(j.formName)){}if(j.format!==undefined){n.dateRangeFormat=j.format
}if(j.fromLabel!==undefined&&j.fromLabel.length>0){n.fromLabel=j.fromLabel
}if(j.toLabel!==undefined&&j.toLabel.length>0){n.toLabel=j.toLabel
}if(j.defaultdate!==undefined&&j.defaultdate==="true"){n.fromModel=new Date();
n.toModel=new Date()
}if(j.ngRequired&&(j.ngRequired==="true"||j.ngRequired===true)){n.ngRequired=true
}else{n.ngRequired=false
}if(j.allow==="past"){n.fromMaxDate=new Date();
n.toMaxDate=new Date()
}else{if(j.allow==="future"){n.fromMinDate=new Date();
n.toMinDate=new Date()
}else{if(j.allow==="onlyfuture"){n.fromMinDate=new Date().setDate(new Date().getDate()+1);
n.toMinDate=new Date().setDate(new Date().getDate()+1)
}else{if(j.allow==="onlypast"){n.fromMaxDate=new Date().setDate(new Date().getDate()-1);
n.toMaxDate=new Date().setDate(new Date().getDate()-1)
}else{if(j.allow==="current"){n.fromMaxDate=new Date();
n.toMaxDate=new Date();
n.fromMinDate=new Date();
n.toMinDate=new Date()
}else{}}}}}}else{if(j.type==="timerange"){n.isTimeRange=true;
if(j.ngRequired&&(j.ngRequired==="true"||j.ngRequired===true)){n.ngRequired=true
}else{n.ngRequired=false
}if(j.fromLabel!==undefined&&j.fromLabel.length>0){n.fromLabel=j.fromLabel
}if(j.toLabel!==undefined&&j.toLabel.length>0){n.toLabel=j.toLabel
}if(j.defaulttime!==undefined&&j.defaulttime==="true"){n.fromModel=c(new Date(),"shortTime");
n.toModel=c(new Date(),"shortTime")
}}else{if(j.type==="datetimerange"){n.isDateTimeRange=true;
if(j.ngRequired&&(j.ngRequired==="true"||j.ngRequired===true)){n.ngRequired=true
}else{n.ngRequired=false
}if(j.fromLabel!==undefined&&j.fromLabel.length>0){n.fromLabel=j.fromLabel
}if(j.toLabel!==undefined&&j.toLabel.length>0){n.toLabel=j.toLabel
}if(j.defaultdate!==undefined&&j.defaultdate==="true"&&!n.fromModel){var i=new Date();
if(angular.isDefined(j.adddaystodefaultdate)){var f=parseInt(j.adddaystodefaultdate);
i.setDate(i.getDate()+f)
}if(angular.isDefined(j.defaultstarttime)){var o=j.defaultstarttime,e=o.match(/(\d+)\.(\d+) (\w+)/),h=/am/i.test(e[3])?parseInt(e[1],10):parseInt(e[1],10)+12,d=parseInt(e[2],10);
i.setHours(h);
i.setMinutes(d);
n.rangFromTimeModel=new Date(i);
n.rangeFromDatepickerModel=new Date(i)
}n.fromModel=c(i,j.format);
if(angular.isDefined(j.defaultendtime)){var o=j.defaultendtime,e=o.match(/(\d+)\.(\d+) (\w+)/),h=/am/i.test(e[3])?parseInt(e[1],10):parseInt(e[1],10)+12,d=parseInt(e[2],10);
i.setHours(h);
i.setMinutes(d);
n.rangeToTimeModel=i;
n.toDatepickerModel=i
}n.toModel=c(i,j.format)
}else{if(n.fromModel){n.rangFromTimeModel=new Date(n.fromModel);
n.rangeFromDatepickerModel=new Date(n.fromModel);
n.fromModel=c(n.fromModel,j.format)
}if(n.toModel){n.rangeToTimeModel=new Date(n.toModel);
n.toModel=c(n.toModel,j.format);
n.toDatepickerModel=new Date(n.rangeToTimeModel)
}}if(j.allow==="past"&&!n.toDatepickerModel){n.maxDate=new Date();
n.toDatepickerModel=new Date()
}else{if(j.allow==="future"&&!n.rangeFromDatepickerModel){n.minDate=new Date();
n.rangeFromDatepickerModel=n.minDate
}else{if(j.allow==="onlyfuture"&&!n.rangeFromDatepickerModel){n.minDate=new Date().setDate(new Date().getDate()+1);
n.rangeFromDatepickerModel=n.minDate
}else{if(j.allow==="onlypast"&&!n.toDatepickerModel){n.maxDate=new Date().setDate(new Date().getDate()-1);
n.toDatepickerModel=new Date().setDate(new Date().getDate()-1)
}else{if(j.allow==="current"){n.maxDate=new Date();
n.minDate=new Date()
}else{}}}}}}}}}}}n.initDateTimePopup=function(){$(g).bind("click",function(r){r=r||window.event;
var q=r.target
});
$(g).find(".dropdown.keep-open").on({"shown.bs.dropdown":function(){this.closable=false
},click:function(q){if(n.closePopup){this.closable=true;
n.closePopup=false
}else{this.closable=false
}},"hide.bs.dropdown":function(){return this.closable
}})
};
n.setDateAndTime=function(q,r){switch(q){case"datetime":n.ngModel=undefined;
n.timeModel=new Date();
break;
case"fromTime":n.fromTimeModel=new Date();
n.fromModel=undefined;
break;
case"toTime":n.toTimeModel=new Date();
n.toModel=undefined;
case"rangeFromClear":n.rangFromTimeModel=new Date();
n.fromModel=undefined;
break;
case"rangeToClear":n.rangeToTimeModel=new Date();
n.toModel=undefined
}n.closePopup=true
};
function m(r){r=r||window.event;
var q=r.target;
if(!$(g).is(r.target)&&$(g).has(r.target).length===0&&$(".open").has(r.target).length===0&&document.contains(q)){n.datePickerFlag.dateTimePicker=false;
n.datePickerFlag.fromIsOpen=false;
n.datePickerFlag.toIsOpen=false;
n.$digest();
$(".open").removeClass("open")
}else{}}$("body").bind("click",m);
n.$on("$destroy",function(){$("body").unbind("click",m)
});
n.setOnlyDate=function(q){n.ngModel=q
};
n.setFromDate=function(q){n.fromModel=q;
n.toMinDate=q
};
n.setToDate=function(q){n.toModel=q;
n.fromMaxDate=q
};
n.setFromTime=function(q){n.fromModel=c(q,"shortTime")
};
n.setToTime=function(q){n.toModel=c(q,"shortTime")
};
n.setRangeFromDate=function(q,s){var r;
if(angular.isDefined(s)){r=l(s,q)
}else{var r=l(q)
}n.fromModel=c(r,j.format);
n.rangeFromDatepickerModel=r
};
n.setRangeToDate=function(q,s){var r;
if(angular.isDefined(s)){r=l(s,q)
}else{var r=l(q)
}n.toModel=c(r,j.format);
n.rangToTimeModel=r;
n.toDatepickerModel=r
};
n.setRangeFromTime=function(s,q){if(!q){q=new Date()
}var r=l(s,q);
n.fromModel=c(r,j.format);
n.rangeFromDatepickerModel=r
};
n.setRangeToTime=function(s,q){if(!q){q=new Date()
}var r=l(s,q);
n.toModel=c(r,j.format);
n.toDatepickerModel=r
};
n.dtPicker=[];
n.open=function(q,r){n.dtPicker=[];
q.preventDefault();
q.stopPropagation();
n.dtPicker[r]=true;
$(".open").removeClass("open")
};
n.setTime=function(v,t){if(!t){t=new Date()
}if(t!==undefined){var u=new Date(v);
var q=(u.getHours()<10)?"0"+u.getHours():u.getHours();
var s=(u.getMinutes()<10)?"0"+u.getMinutes():u.getMinutes();
var r=new Date(t);
r.setHours(q);
r.setMinutes(s);
n.ngModel=c(r,j.format)
}else{k(v)
}};
n.daysInMonth=function(r,q){return new Date(q,r,0).getDate()
};
n.setDate=function(r,u){var t=l(r);
if(u){var q=(u.getHours()<10)?"0"+u.getHours():u.getHours();
var s=(u.getMinutes()<10)?"0"+u.getMinutes():u.getMinutes();
t.setHours(q);
t.setMinutes(s)
}n.ngModel=c(t,j.format);
n.timeModel=t
};
n.hstep=1;
n.mstep=30;
n.ismeridian=false
}}
}])
});