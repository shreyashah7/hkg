define(["hkg","shiftService","treeviewMultiselect.directive","addMasterValue","dynamicForm"],function(a,b){a.register.controller("ShiftController",["$rootScope","$scope","ShiftService","$location","DynamicFormService","$timeout",function(o,q,c,l,p,e){o.maskLoading();
o.mainMenu="manageLink";
o.childMenu="manageShift";
o.activateMenu();
q.defaultSelectedids=[];
if(l.path()==="/manageshift"){o.configureDefaultShift=false
}q.initShiftManagementForm=function(i){q.shiftManagementForm=i
};
q.initTempShiftManagementForm=function(i){q.tempShiftForm=i
};
q.ovrDeptNames=[];
q.ovrDeptNamesString="";
q.entity="SHIFT.";
q.addDefaultShiftPanel=true;
q.temporaryShift=false;
q.searchPage=false;
q.tempShiftNameInPopUp="";
q.begins={};
q.shift={};
q.shift.selectedInString=[];
q.isEditing=false;
q.updateButton=false;
q.endsHolidayOrEventFlag1=false;
q.beginsHoliday=false;
q.beginsEvent=false;
q.endsHoliday=false;
q.endsEvent=false;
q.allDaysAreSelectedAsWorking=false;
q.breakCountZero=false;
q.breakCountZeroForTempShift=false;
q.updateShiftPanel=false;
q.hideDatePicker=true;
q.departmentRequired=true;
q.shift.breakList=[];
q.holidayList=[];
q.eventList=[];
q.list=[];
q.listToShow=[];
q.shift.tempShiftBreakList=[];
q.temp=true;
q.beginitemFlag=false;
q.deptNameInPopUp="";
q.shiftNameInPopUp="";
q.mainShiftName="";
q.reload=true;
var h={};
q.addShiftData={};
q.listOfModelsOfDateType=[];
var g=p.retrieveSectionWiseCustomFieldInfo("manageShift");
q.dbType={};
g.then(function(i){q.customTemplateDate=angular.copy(i.genralSection);
q.generalShiftTemplate=o.getCustomDataInSequence(q.customTemplateDate);
if(q.generalShiftTemplate!==null&&q.generalShiftTemplate!==undefined){angular.forEach(q.generalShiftTemplate,function(r){if(r.type!==null&&r.type!==undefined&&r.type==="date"){q.listOfModelsOfDateType.push(r.model)
}})
}},function(i){},function(i){});
q.multiselecttree={text:"All Dep",isChecked:false,items:[]};
q.$on("$viewContentLoaded",function(){q.departments=q.retrieveAllDepartments();
q.initializWeekday();
q.initializWeekdayFortemp()
});
q.initializWeekday=function(){q.weekDay={};
q.weekDay.WeekOffList=[{key:"2",code:"M",value:"Monday",isChecked:true},{key:"3",code:"Tu",value:"Tuesday",isChecked:true},{key:"4",code:"W",value:"Wednesday",isChecked:true},{key:"5",code:"Th",value:"Thursday",isChecked:true},{key:"6",code:"F",value:"Friday",isChecked:true},{key:"7",code:"Sa",value:"Saturday",isChecked:true},{key:"1",code:"S",value:"Sunday",isChecked:false}]
};
q.weekDay={};
q.weekDay.WeekOffList=[{key:"2",code:"M",value:"Monday",isChecked:true},{key:"3",code:"Tu",value:"Tuesday",isChecked:true},{key:"4",code:"W",value:"Wednesday",isChecked:true},{key:"5",code:"Th",value:"Thursday",isChecked:true},{key:"6",code:"F",value:"Friday",isChecked:true},{key:"7",code:"Sa",value:"Saturday",isChecked:true},{key:"1",code:"S",value:"Sunday",isChecked:false}];
q.initializWeekdayFortemp=function(){q.shift.WeekOffListForTempShift=[{key:"2",code:"M",value:"Monday",isChecked:true},{key:"3",code:"Tu",value:"Tuesday",isChecked:true},{key:"4",code:"W",value:"Wednesday",isChecked:true},{key:"5",code:"Th",value:"Thursday",isChecked:true},{key:"6",code:"F",value:"Friday",isChecked:true},{key:"7",code:"Sa",value:"Saturday",isChecked:true},{key:"1",code:"S",value:"Sunday",isChecked:false}]
};
q.beforeOrAfterList=[{id:1,title:"before"},{id:2,title:"after"}];
if(q.beforeOrAfterList!==undefined&&q.beforeOrAfterList!==null&&q.beforeOrAfterList.length>0){for(var k=0;
k<q.beforeOrAfterList.length;
k++){q.beforeOrAfterList[k].title=o.translateValue("SHIFTS."+q.beforeOrAfterList[k].title)
}}q.holidayOrEventList=[{id:1,title:"Holiday"},{id:2,title:"Event"}];
if(q.holidayOrEventList!==undefined&&q.holidayOrEventList!==null&&q.holidayOrEventList.length>0){for(var k=0;
k<q.holidayOrEventList.length;
k++){q.holidayOrEventList[k].title=o.translateValue("SHIFTS."+q.holidayOrEventList[k].title)
}}q.setSearchflag=function(t){var s=$("#scrollable-dropdown-menu.typeahead").typeahead("val");
if(s.length>0){if(q.selectedShift!=undefined){q.selectedShift.selected=undefined
}if(q.selectedShift!=undefined&&q.selectedShift.currentNode){q.selectedShift.currentNode.selected=undefined;
q.selectedShift.currentNode=undefined
}if(q.searchedShiftTree!==undefined){q.searchedShiftTree=undefined
}if(s.length<3){q.searchedShiftRecord=[];
q.searchPage=true
}else{if(t!==null&&angular.isDefined(t)&&t.length>0){for(var r=0;
r<t.length;
r++){t[r].text=o.translateValue("SHIFT."+t[r].text)
}}q.searchedShiftRecord=angular.copy(t);
q.searchPage=true;
q.isEditing=false
}}};
q.retrieveAllDepartments=function(){var s=function(){var u="Failed to retrieve departments. Try again.";
var t=o.failure;
o.addMessage(u,t);
o.unMaskLoading()
};
c.retrieveDepartmentList(function(u){q.multiselecttree.items=u.items;
if(u!==null&&angular.isDefined(u)&&u.length>0){for(var t=0;
t<q.multiselecttree.items.length;
t++){q.multiselecttree.items[t].text=o.translateValue("DPT_NM."+q.multiselecttree.items.text)
}}q.dep=u;
q.setDefaultWorkflowRootOption();
for(var t=0;
t<q.dep.length;
t++){i(q.dep[t])
}},s);
c.retreiveShiftTree(function(t){q.allShifts=t;
q.shiftManagementForm.$setPristine()
},function(){var u="Failed to retrieve shifts. Try again.";
var t=o.failure;
o.addMessage(u,t);
o.unMaskLoading()
});
c.retrieveShiftsWithDetails(function(w){q.shiftsDetail=w;
q.shiftsDetailFromDB=angular.copy(w);
if(q.pId){q.editShift(q.pId)
}q.allShiftNames=[];
if(q.shiftsDetailFromDB!==undefined&&q.shiftsDetailFromDB.length!==0){for(var u=0;
u<q.shiftsDetailFromDB.length;
u++){q.allShiftNames.push({label:q.shiftsDetailFromDB[u].shiftName})
}for(var v=0;
v<q.shiftsDetailFromDB.length;
v++){if(!!(q.shiftsDetailFromDB[v].temporaryShifts)&&q.shiftsDetailFromDB[v].temporaryShifts.length>0){for(var t=0;
t<q.shiftsDetailFromDB[v].temporaryShifts.length;
t++){q.allShiftNames.push({label:q.shiftsDetailFromDB[v].temporaryShifts[t].tempShiftName})
}}}for(var v=0;
v<q.shiftsDetailFromDB.length;
v++){if(!!(q.shiftsDetailFromDB[v].overRideShifts)&&q.shiftsDetailFromDB[v].overRideShifts.length>0){for(var t=0;
t<q.shiftsDetailFromDB[v].overRideShifts.length;
t++){q.allShiftNames.push({label:q.shiftsDetailFromDB[v].overRideShifts[t].shiftName})
}}}}q.shiftManagementForm.$setPristine()
},function(){var u="Failed to retrieve shifts. Try again.";
var t=o.failure;
o.addMessage(u,t);
o.unMaskLoading()
});
var r=function(){var u="Failed to retrieve Holiday. Try again.";
var t=o.failure;
o.addMessage(u,t);
o.unMaskLoading()
};
c.retrieveHolidayList(function(v){q.holidayList=v;
if(v!==null&&angular.isDefined(v)&&v.length>0){for(var t=0;
t<q.holidayList.length;
t++){q.holidayList[t].title=o.translateValue("H_NAME."+q.holidayList[t].title)
}}q.holidayListFromDb=angular.copy(v);
for(var t=0;
t<q.holidayList.length;
t++){var u=q.holidayList[t];
u.type="Holiday";
q.list.push(u)
}},r);
var s=function(){var u="Failed to retrieve List. Try again.";
var t=o.failure;
o.addMessage(u,t);
o.unMaskLoading()
};
c.retrieveEventList(function(u){q.eventList=u;
q.eventListFromDb=angular.copy(u);
if(u!==null&&angular.isDefined(u)&&u.length>0){for(var t=0;
t<q.eventList.length;
t++){q.eventList[t].eventTitle=o.translateValue("EVNT_TITLE."+q.eventList[t].eventTitle)
}}if(u!==null&&angular.isDefined(u)&&u.length>0){for(var t=0;
t<q.eventListFromDb.length;
t++){q.eventListFromDb[t].startDt=q.eventListFromDb[t].fromDate;
q.eventListFromDb[t].endDt=q.eventListFromDb[t].toDate;
q.eventListFromDb[t].title=q.eventListFromDb[t].eventTitle
}}for(var t=0;
t<q.eventList.length;
t++){var v=q.eventList[t];
v.type="Event";
v.title=v.eventTitle;
q.list.push(v)
}},s);
q.departmentListOption=[];
function i(u){if(u===null||u===undefined){return
}q.departmentListOption.push({id:u.id,displayName:u.displayName,description:u.description,children:u.children,parentId:u.parentId,parentName:u.parentName,companyId:u.companyId,isActive:u.isActive,showAllDepartment:u.showAllDepartment,addDepartmentData:u.departmentCustom});
if(u.children===null||u.children.length===0){return
}else{for(var t=0;
t<u.children.length>0;
t++){i(u.children[t])
}}}o.unMaskLoading();
q.shiftManagementForm.$dirty=false;
if(q.tempShiftForm!==undefined){q.tempShiftForm.$dirty=false
}};
q.shiftTitleExists=function(r){q.shiftNameInDb=false;
q.shiftTitleIsNull=false;
var v=0;
if(r){q.shift.shiftNameFromUI=q.shift.shiftName
}else{q.shift.shiftNameFromUI=q.shift.tempShiftName
}if(q.shift.shiftNameFromUI!==undefined&&q.shift.shiftNameFromUI!==""&&q.shift.shiftNameFromUI!==null){q.shiftTitleIsNull=false;
if(q.shiftsDetailFromDB!==undefined&&q.shiftsDetailFromDB.length!==0){for(var t=0;
t<q.shiftsDetailFromDB.length;
t++){if(q.shiftsDetailFromDB[t].shiftName.toUpperCase()===q.shift.shiftNameFromUI.toUpperCase()){if(!q.isEditing||q.temporaryShift){q.shiftNameInDb=true;
break
}if(q.isEditing&&q.shift.shiftNameFromUI.toUpperCase()!==q.oldShiftName.toUpperCase()){q.shiftNameInDb=true;
break
}}if(!q.shiftNameInDb){for(var u=0;
u<q.shiftsDetailFromDB.length;
u++){if(!!(q.shiftsDetailFromDB[u].temporaryShifts)&&q.shiftsDetailFromDB[u].temporaryShifts.length>0){for(var s=0;
s<q.shiftsDetailFromDB[u].temporaryShifts.length;
s++){if(q.shiftsDetailFromDB[u].temporaryShifts[s].tempShiftName.toUpperCase()===q.shift.shiftNameFromUI.toUpperCase()){if(!q.updateButton){q.shiftNameInDb=true;
break
}if(q.updateButton&&q.shift.shiftNameFromUI.toUpperCase()!==q.oldShiftName.toUpperCase()){q.shiftNameInDb=true;
break
}}}}}}if(!q.shiftNameInDb){for(var u=0;
u<q.shiftsDetailFromDB.length;
u++){if(!!(q.shiftsDetailFromDB[u].overRideShifts)&&q.shiftsDetailFromDB[u].overRideShifts.length>0){for(var s=0;
s<q.shiftsDetailFromDB[u].overRideShifts.length;
s++){if(q.shiftsDetailFromDB[u].overRideShifts[s].shiftName.toUpperCase()===q.shift.shiftNameFromUI.toUpperCase()){if(!q.updateButton){q.shiftNameInDb=true;
break
}if(q.updateButton&&q.shift.shiftNameFromUI.toUpperCase()!==q.oldShiftName.toUpperCase()){q.shiftNameInDb=true;
break
}}}}}}}}}else{q.shiftTitleIsNull=true
}};
if(o.configureDefaultShift){c.retrieveShiftsWithDetails(function(w){q.list1=[];
q.shiftsDetail=w;
q.shiftsDetailFromDB=angular.copy(w);
q.allShiftNames=[];
if(q.shiftsDetailFromDB!==undefined&&q.shiftsDetailFromDB.length!==0){for(var u=0;
u<q.shiftsDetailFromDB.length;
u++){q.allShiftNames.push({label:q.shiftsDetailFromDB[u].shiftName});
if(q.shiftsDetailFromDB[u].defaultShift){q.defaultShift=angular.copy(q.shiftsDetailFromDB[u]);
q.mainShiftName=q.defaultShift.shiftName
}}for(var v=0;
v<q.shiftsDetailFromDB.length;
v++){if(!!(q.shiftsDetailFromDB[v].temporaryShifts)&&q.shiftsDetailFromDB[v].temporaryShifts.length>0){for(var t=0;
t<q.shiftsDetailFromDB[v].temporaryShifts.length;
t++){q.allShiftNames.push({label:q.shiftsDetailFromDB[v].temporaryShifts[t].tempShiftName})
}}}}if(q.defaultShift!==undefined&&q.defaultShift.id!==undefined){q.isEditing=true;
q.shift.id=q.defaultShift.id;
q.oldShiftName=angular.copy(q.defaultShift.shiftName);
q.shift=q.defaultShift
}var r=function(){var x="Failed to retrieve Holiday. Try again.";
var i=o.failure;
o.addMessage(x,i);
o.unMaskLoading()
};
c.retrieveHolidayList(function(z){q.holidayList=z;
if(z!==null&&angular.isDefined(z)&&z.length>0){for(var x=0;
x<q.holidayList.length;
x++){q.holidayList[x].title=o.translateValue("H_NAME."+q.holidayList[x].title)
}}q.holidayListFromDb=angular.copy(z);
for(var x=0;
x<q.holidayList.length;
x++){var y=q.holidayList[x];
y.type="Holiday";
q.list.push(y)
}},r);
var s=function(){var x="Failed to retrieve List. Try again.";
var i=o.failure;
o.addMessage(x,i);
o.unMaskLoading()
};
c.retrieveEventList(function(y){q.eventList=y;
q.eventListFromDb=angular.copy(y);
if(y!==null&&angular.isDefined(y)&&y.length>0){for(var x=0;
x<q.eventList.length;
x++){q.eventList[x].eventTitle=o.translateValue("EVNT_TITLE."+q.eventList[x].eventTitle)
}}if(y!==null&&angular.isDefined(y)&&y.length>0){for(var x=0;
x<q.eventListFromDb.length;
x++){q.eventListFromDb[x].startDt=q.eventListFromDb[x].fromDate;
q.eventListFromDb[x].endDt=q.eventListFromDb[x].toDate;
q.eventListFromDb[x].title=q.eventListFromDb[x].eventTitle
}}for(var x=0;
x<q.eventList.length;
x++){var z=q.eventList[x];
z.type="Event";
z.title=z.eventTitle;
q.list.push(z)
}},s);
if(q.defaultShift!=null){if(angular.isDefined(q.defaultShift.shiftCustom)&&q.defaultShift.shiftCustom!=null){q.addShiftData=angular.copy(q.defaultShift.shiftCustom);
if(!!q.addShiftData){angular.forEach(q.listOfModelsOfDateType,function(i){if(q.addShiftData.hasOwnProperty(i)){if(q.addShiftData[i]!==null&&q.addShiftData[i]!==undefined){q.addShiftData[i]=new Date(q.addShiftData[i])
}else{q.addShiftData[i]=""
}}})
}}q.weekOffId=q.defaultShift.workingDayIds;
if(q.weekOffId){q.weekOffIdCode=q.weekOffId.split(",")
}if(q.weekOffId){$.each(q.weekDay.WeekOffList,function(y,x){var i=false;
if(q.weekOffIdCode){$.each(q.weekOffIdCode,function(z,A){if(A===x.key){i=true
}})
}if(i){x.isChecked=true
}else{x.isChecked=false
}})
}}if(q.shiftManagementForm!=null){q.shiftManagementForm.$setPristine()
}},function(){var r="Failed to retrieve shifts. Try again.";
var i=o.failure;
o.addMessage(r,i);
o.unMaskLoading()
});
c.retreiveShiftTree(function(i){q.ShiftDetailList=[];
q.ShiftDetailList=i;
if(q.ShiftDetailList!==undefined&&q.ShiftDetailList.length!==0){q.updateButton=true
}},function(){var r="Failed to retrieve shifts. Try again.";
var i=o.failure;
o.addMessage(r,i)
});
q.breakNameExist=function(i){q.breakNameInDb=false;
if(i===undefined||i===""||i===null){q.breakNameInDb=true
}}
}q.addNewBreak=function(){if(q.shift.breakList===undefined){q.shift.breakList=[]
}q.submitted=true;
if((!q.shiftManagementForm.breakForm)||(q.shiftManagementForm.breakForm!==undefined&&q.shiftManagementForm.breakForm.$valid)){q.submitted=false;
q.shift.breakList.push({breakSlotTitle:"",breakStartTime:new Date(),breakEndTime:new Date()})
}};
q.removeBreak=function(r){var s=undefined;
$.each(q.shift.breakList,function(t,i){if(i===r){s=t
}});
if(s!==undefined){if(q.shift.breakList.length===1){q.localBreak=angular.copy(q.shift.breakList)
}q.shift.breakList.splice(s,1);
if(q.shift.breakList!==undefined&&q.shift.breakList.length===0){q.breakCountZero=true;
$("#passwordPopUp").modal("show")
}}};
q.cancelBreak=function(i){$("#passwordPopUp").modal("hide");
o.removeModalOpenCssAfterModalHide();
if(i===true){if(q.localBreak!=null){q.shift.breakList=q.localBreak
}q.addNewBreak()
}else{q.addNewBreakForTempShift();
if(q.localBreak!=null){q.shift.tempShiftBreakList=q.localBreak
}}};
q.noBreaks=function(){if(!q.breakCountZero&&q.overRideShiftFlag&&!q.isEditing){q.breakCountZero=true;
q.addNewOverRideShift()
}else{if(!q.breakCountZero&&q.overRideShiftFlag&&q.isEditing){q.breakCountZero=true;
q.saveOverRideShift()
}}if(!q.breakCountZero&&!q.temporaryShift){q.breakCountZero=true;
q.addNewShift()
}if(!q.breakCountZeroForTempShift&&q.temporaryShift){q.breakCountZeroForTempShift=true;
q.addTemporaryShift()
}q.breakCountZero=true;
q.invalidBreakTime=false;
$("#passwordPopUp").modal("hide");
o.removeModalOpenCssAfterModalHide();
if(o.configureDefaultShift){$(".modal-backdrop").remove()
}};
q.addNewBreakForTempShift=function(){q.submitted=true;
if((!q.tempShiftForm.tempBreakForm)||(q.tempShiftForm.tempBreakForm!==undefined&&q.tempShiftForm.tempBreakForm.$valid)){q.submitted=false;
q.shift.tempShiftBreakList.push({breakSlotTitle:"",breakStartTime:new Date(),breakEndTime:new Date()})
}};
q.removeBreakForTempShift=function(r){var s=undefined;
$.each(q.shift.tempShiftBreakList,function(t,i){if(i===r){s=t
}});
if(s!==undefined){if(q.shift.tempShiftBreakList.length===1){q.localBreak=angular.copy(q.shift.tempShiftBreakList)
}q.shift.tempShiftBreakList.splice(s,1)
}if(q.shift.tempShiftBreakList!==undefined&&q.shift.tempShiftBreakList.length===0){q.breakCountZeroForTempShift=true;
$("#passwordPopUp").modal("show")
}};
q.setDefaultWorkflowRootOption=function(){q.allShiftDropdown1=[];
q.allShiftDropdown1.push({id:"0",displayName:"None",children:null});
$.merge(q.allShiftDropdown1,angular.copy(q.dep))
};
q.iterateChild=function(x,s,r,y,w,v){var u=x;
for(var t=0;
t<u.length;
t++){if(s&&u[t].isChecked){if(r){if(u[t].id==y){if(v){q.selectedDepValues.push({id:u[t].id,text:u[t].text})
}else{if(w){q.selectedDepValues.push({id:u[t].id})
}else{q.selectedDepValues.push({text:u[t].text})
}}}}else{if(v){q.selectedDepValues.push({id:u[t].id,text:u[t].text})
}else{if(w){q.selectedDepValues.push({id:u[t].id})
}else{q.selectedDepValues.push({text:u[t].text})
}}}}if(u[t].items){q.iterateChild(u[t].items,s,r,y,w,v)
}}};
function j(s){if(s===null||s===undefined){return
}q.existingIds.push({id:s.id});
if(s.children===null||s.children.length===0){return
}else{for(var r=0;
r<s.children.length>0;
r++){j(s.children[r])
}}}q.$watch("selectedItemList",function(){if(q.selectedItemList!==undefined){var u=[];
var s=[];
q.shift.selectedInString=[];
q.shift.selectedInStringIds=[];
if(q.selectedItemList.items!==undefined&&q.selectedItemList.items!==null){if(q.selectedItemList.items.length>0){q.existingIds=[];
for(var r=0;
r<q.allShiftDropdown1.length;
r++){j(q.allShiftDropdown1[r])
}for(var r=0;
r<q.existingIds.length;
r++){var t=q.existingIds[r].id;
q.selectedDepValues=[];
if(r===0){q.iterateChild(q.selectedItemList.items,true,false,t,false,true);
s=angular.copy(q.selectedDepValues)
}q.selectedDepValues=[];
q.iterateChild(q.selectedItemList.items,true,true,t,false,true);
$.merge(u,angular.copy(q.selectedDepValues))
}}}if(s!==undefined&&s!==null){if(s.length>0){angular.forEach(s,function(i){q.shift.selectedInString.push(i.text);
q.shift.selectedInStringIds.push(i.id)
})
}}q.selectedDepValues=[];
if(u.length>0){q.overrideDep=u;
$("#overrideModal").modal("show")
}if(!q.shift.selectedInStringIds.length>0){q.departmentRequired=false
}else{q.departmentRequired=true
}}if(q.shift.selectedInString!==undefined&&q.shift.selectedInString!==null&&q.shift.selectedInString.toString().length>0){if(q.shift.selectedInString.toString().length>25){q.shift.selectedInString=q.shift.selectedInString.toString().substr(0,25)
}}});
var n=function(u,s){if(u!==undefined&&u!==null){for(var r=0;
r<u.length;
r++){var t=u[r];
t.isChecked=false;
if(t.items&&t.items!==null){n(t.items,s)
}}}};
q.clearData=function(i){q.overRideShiftFlag=false;
q.reload=false;
var r=angular.copy(q.temporaryShift);
var s=null;
if(r){s=angular.copy(q.mainShiftId)
}$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
q.submitted=false;
q.shiftManagementForm.$setPristine();
if(q.tempShiftForm!==undefined){q.tempShiftForm.$setPristine()
}q.searchedShift=[];
if(q.selectedShift!=undefined){q.selectedShift.selected=undefined
}if(q.selectedShift!=undefined&&q.selectedShift.currentNode){q.selectedShift.currentNode.selected=undefined;
q.selectedShift.currentNode=undefined
}if(q.shift.tempShiftStartTime!==undefined&&q.shift.tempShiftStartTime!==null){q.shift.tempShiftStartTime=undefined
}q.ovrDeptNames="";
q.searchedShiftTree=undefined;
q.searchPage=false;
q.breakCountZero=false;
q.temporaryShift=false;
q.addDefaultShiftPanel=true;
q.breakCountZeroForTempShift=false;
q.isEditing=false;
q.updateButton=false;
q.shiftTitleIsNull=false;
q.departmentRequired=true;
q.beginitemFlag=false;
q.beginsDurationFlag=false;
q.beginsHolidayOrEventFlag=false;
q.startDayCount=false;
q.enditemFlag=false;
q.endsDayCount=false;
q.endsDurationFlag=false;
q.shiftNameInDb=false;
q.endsHolidayOrEventFlag1=false;
q.dateRangeNotValid=false;
q.invalidDate=false;
q.shiftTitleIsNull=false;
q.invalidTime=false;
q.invalidBreakTime=false;
q.alreadyCreated=false;
q.tempShiftNameInPopUp="";
q.ovrDeptNames=[];
q.shift={};
q.shift.status=true;
q.shift.breakList=[];
q.listToShowBegin=[];
q.listToShowEnd=[];
q.initializWeekday();
q.initializWeekdayFortemp();
q.allDaysAreSelectedAsWorking=false;
q.localBreak=undefined;
e(function(){q.reload=true
},50);
if(o.configureDefaultShift){o.configureDefaultShift=false
}else{q.shift.selectedInString="";
q.shift.selectedInStringIds=undefined;
n(q.multiselecttree.items);
q.defaultSelectedids=undefined
}if(!i&&r){q.editShift(s)
}};
q.checkConditionForDays=function(s){q.selectedDays=[];
$.each(q.weekDay.WeekOffList,function(t,i){if(i.isChecked==="true"||i.isChecked===true){q.selectedDays.push(i)
}});
if(q.selectedDays.length===7){q.allDaysAreSelectedAsWorking=true
}else{q.allDaysAreSelectedAsWorking=false
}if(q.selectedDays.length===0){q.weekDay={};
q.weekDay.WeekOffList=[{key:"2",code:"M",value:"Monday",isChecked:false},{key:"3",code:"Tu",value:"Tuesday",isChecked:false},{key:"4",code:"W",value:"Wednesday",isChecked:false},{key:"5",code:"Th",value:"Thursday",isChecked:false},{key:"6",code:"F",value:"Friday",isChecked:false},{key:"7",code:"Sa",value:"Saturday",isChecked:false},{key:"1",code:"S",value:"Sunday",isChecked:false}];
for(var r=0;
r<q.weekDay.WeekOffList.length;
r++){if(q.weekDay.WeekOffList[r].code===s){q.weekDay.WeekOffList[r].isChecked=true;
break
}}}};
q.checkOverlapOfTime=function(X){var P=false;
var O=[];
var Q=[];
var L=[];
q.timeClash=false;
if(q.shiftsDetailFromDB!==undefined&&q.shiftsDetailFromDB!==null&&q.shiftsDetailFromDB.length>0){var D=angular.copy(q.shiftsDetailFromDB);
for(var ab=0;
ab<D.length;
ab++){if(q.shift.id&&q.shift.id!==undefined){if(q.shift.id===D[ab].id){D.splice(ab,1)
}}}for(var I=0;
I<q.weekDay.WeekOffList.length;
I++){if(q.weekDay.WeekOffList[I].isChecked===true||q.weekDay.WeekOffList[I].isChecked==="true"){L.push(parseInt(q.weekDay.WeekOffList[I].key))
}}var ac=false;
for(var R=0;
R<D.length;
R++){var y=D[R].workingDayIds.split(",");
var W=d(L,y);
if(W&&W.length>0){ac=true;
break
}}if(D&&D.length>0&&ac){for(var ab=0;
ab<D.length;
ab++){var F=D[ab].departmentIds.split(",");
for(var aa=0;
aa<X.length;
aa++){var N=($.inArray(X[aa]+"",F)!==-1);
if(N){P=true;
O.push(X[aa]);
Q.push(D[ab])
}}}}}if(P){var v=new Date(q.shift.shiftStartTime);
var E=new Date(q.shift.shiftEndTime);
var S=parseInt(v.getHours());
var U=parseInt(v.getMinutes());
var ai=parseInt(E.getHours());
var u=parseInt(E.getMinutes());
q.givenHourSet=[];
var H=S;
var w=H;
while((H!==ai)){q.givenHourSet.push(H);
H++;
if(H>23){H-=24
}}if(H===ai){if(ai>23){ai-=24
}q.givenHourSet.push(ai)
}var T=S+(U/60);
var M=ai+(u/60);
var s=[];
for(var Y=0;
Y<Q.length;
Y++){var J=new Date(Q[Y].shiftStartTime);
var z=new Date(Q[Y].shiftEndTime);
var B=parseInt(J.getHours());
var x=parseInt(J.getMinutes());
var ad=parseInt(z.getHours());
var Z=parseInt(z.getMinutes());
var C=B+(x/60);
var t=ad+(Z/60);
var ah=C;
var ag=t;
var af=T;
var ae=M;
q.dbHourSet=[];
q.dbMinSet=[];
var K=B;
var r=K;
while((K!==ad)){q.dbHourSet.push(K);
K++;
if(K>23){K-=24
}}if(K===ad){if(K>23){K-=24
}q.dbHourSet.push(K)
}var G=d(q.givenHourSet,q.dbHourSet);
q.timeClash=false;
q.givenMinSet=[];
if(G.length>2){q.timeClash=true;
s.push(Q[Y]);
break
}if(G.length===2&&!q.timeClash){if(S===ad&&ai===B){if(U>=Z&&u<=x){}else{q.timeClash=true;
s.push(Q[Y]);
break
}}else{q.timeClash=true;
s.push(Q[Y]);
break
}}if(G.length===1&&!q.timeClash){console.log("startHour"+S);
console.log("endHourInLoop"+ad);
console.log("-----------------------------");
console.log("startHourInLoop"+B);
console.log("endHour"+ai);
if(S===ad){if(U<Z){q.timeClash=true;
s.push(Q[Y]);
break
}}else{if(B===ai){if(u>x&&!q.timeClash){q.timeClash=true;
s.push(Q[Y]);
break
}}}}}if(q.timeClash){q.finalIds=[];
for(var ab=0;
ab<s.length;
ab++){var F=s[ab].departmentIds.split(",");
for(var aa=0;
aa<O.length;
aa++){var N=$.inArray(O[aa].toString(),F)!==-1;
if(N){var V=$.inArray(O[aa],q.finalIds)!==-1;
if(!V){q.finalIds.push(O[aa])
}}}}q.listOfDeptIds=q.finalIds;
var A=angular.copy(q.multiselecttree);
q.ovrDeptNames=[];
m(A.items,q.listOfDeptIds);
q.ovrDeptNamesString="";
q.ovrDeptNamesString=q.ovrDeptNames.toString()
}}};
function d(s,r){var w={};
var v=[];
for(var u=0;
u<r.length;
u++){w[parseInt(r[u])]=true
}for(var t=0;
t<s.length;
t++){if(w[s[t]]){v.push(s[t])
}}return v
}q.checkOvrlapOfDate=function(){q.alreadyCreated=false;
var u="";
if(q.shift.id){u=q.shift.id
}else{u=q.mainShiftId
}if(u){var A=[];
for(var s=0;
s<q.shiftsDetailFromDB.length;
s++){if(u===q.shiftsDetailFromDB[s].id){A.push(q.shiftsDetailFromDB[s]);
break
}}var y=[];
for(var s=0;
s<A.length;
s++){if(A[s].temporaryShifts){for(var r=0;
r<A[s].temporaryShifts.length;
r++){y.push(A[s].temporaryShifts)
}for(r=0;
r<A[s].temporaryShifts.length;
r++){var t=A[s].temporaryShifts[r];
if(q.shift.tempShiftId&&q.shift.tempShiftId!==undefined){if(q.shift.tempShiftId===A[s].temporaryShifts[r].tempShiftId){A[s].temporaryShifts.splice(r,1);
var t=A[s].temporaryShifts[r]
}}if(t!==undefined){var x=new Date(t.tempShiftFromDate);
var w=new Date(t.tempShiftEndDate);
var B=new Date(q.fromDateForTempShit);
var z=new Date(q.toDateForTempShit);
var v=((B<x&&x<z)||(B<w&&w<z));
if((B<x||B>w)&&(z<x||z>w)&&(z>B)&&(!v)){}else{q.alreadyCreated=true
}}}}}}};
var m=function(u,s){if(u!==undefined&&u!==null){for(var r=0;
r<u.length;
r++){var t=u[r];
if($.inArray(t.id+"",s)!==-1||$.inArray(t.id,s)!==-1){q.ovrDeptNames.push(t.text)
}if(t.items&&t.items!==null){m(t.items,s)
}}}};
q.addNewShift=function(P,B){q.reload=false;
q.submitted=true;
if(o.configureDefaultShift){if(!q.shift.shiftStartTime){q.shift.shiftStartTime=new Date()
}if(!q.shift.shiftEndTime){q.shift.shiftEndTime=new Date()
}}var t=new Date(q.shift.shiftStartTime);
var v=new Date(q.shift.shiftEndTime);
var ae=t.getHours();
var X=angular.copy(ae);
var R=v.getHours();
var V=t.getMinutes();
var r=v.getMinutes();
q.validBreakTimeSlot=true;
q.invalidTime=false;
if(ae===R&&V===r){q.invalidTime=true
}q.invalidBreakTime=false;
var D=[];
var H=[];
if(q.shift.breakList!==undefined&&q.shift.breakList.length>0&&!q.invalidTime){while((X!==R)){D.push(X);
X++;
if(X>23){X-=24
}}if(X===R){if(X>23){X-=24
}D.push(X)
}for(var aa=0;
aa<q.shift.breakList.length;
aa++){var y=new Date(q.shift.breakList[aa].breakStartTime);
var N=new Date(q.shift.breakList[aa].breakEndTime);
var A=y.getHours();
var ab=angular.copy(A);
var x=N.getHours();
var M=y.getMinutes();
var z=N.getMinutes();
var S=[];
while((ab!==x)){S.push(ab);
ab++;
if(ab>23){ab-=24
}}if(ab===x){if(ab>23){ab-=24
}S.push(ab)
}console.log(D);
console.log(S);
for(var G=0;
G<S.length;
G++){if(D.indexOf(S[G])===-1){q.invalidBreakTime=true;
break
}else{}}if(q.invalidBreakTime){break
}else{if(A===ae){if(M<=V){q.invalidBreakTime=true;
break
}}if(x===R){if(z>=r){q.invalidBreakTime=true;
break
}}if(x===A){if(M>=z){q.invalidBreakTime=true;
break
}}if(A===ae&&(x===R)&&(ae===R)){if(M<=V||z>=r||M>=r||z<=V){q.invalidBreakTime=true;
break
}}}if(!q.invalidBreakTime){for(var K=0;
K<aa;
K++){var s=new Date(q.shift.breakList[K].breakStartTime);
var E=new Date(q.shift.breakList[K].breakEndTime);
var U=s.getHours();
var J=E.getHours();
var ah=s.getMinutes();
var Y=E.getMinutes();
var C=parseInt(Y);
var F=parseInt(J);
var af=F+(C/60);
var u=parseInt(ah);
var w=parseInt(U);
var ag=w+(u/60);
var L=parseInt(A);
var I=parseInt(M);
var ad=L+(I/60);
var Z=parseInt(x);
var W=parseInt(z);
var ac=Z+(W/60);
if(ag!==undefined&&af!==undefined&&ad!==undefined&&ac!==undefined){var P=((ag<ad&&ad<af)||(ag<ac&&ac<af));
if((ag<ad||ag>ac)&&(af<ad||af>ac)&&(!P)){}else{q.invalidBreakTime=true
}}}}}}q.selectedDays=[];
$.each(q.weekDay.WeekOffList,function(ai,i){if(i.isChecked==="true"||i.isChecked===true){q.selectedDays.push(i)
}});
q.weekOffId="";
for(var aa in q.selectedDays){var Q=q.selectedDays[aa].key;
if(Q!=="undefined"||Q!==null||Q!==""){if(q.weekOffId===""){q.weekOffId=Q
}else{q.weekOffId+=","+Q
}}}var T={};
q.departmentRequired=false;
if(q.shift.shiftName===undefined||q.shift.shiftName===null||q.shift.shiftName===""){q.shiftTitleIsNull=true
}if(o.configureDefaultShift||q.shift.defaultShift){q.departmentRequired=true
}else{if(q.shift.selectedInStringIds!==undefined&&q.shift.selectedInStringIds.length>0){T.departmentIds=q.shift.selectedInStringIds.toString();
T.defaultShift=false;
q.departmentRequired=true
}else{q.departmentRequired=false
}}q.breakNameInDb=false;
if(q.shiftManagementForm.$valid&&q.shift.shiftStartTime!==undefined&&q.shift.shiftEndTime!==undefined){T={status:q.shift.status};
if((T.status||!q.isEditing)&&!q.breakCountZero&&q.shift.breakList!==undefined&&q.shift.breakList.length===0){$("#passwordPopUp").modal("show");
return
}if(o.configureDefaultShift&&q.shift.breakList===undefined){$("#passwordPopUp").modal("show");
q.shift.breakList=[];
return
}if(!q.shiftNameInDb&&q.selectedDays.length!==0&&!q.breakNameInDb&&q.departmentRequired&&!q.invalidTime&&!q.invalidBreakTime&&((!T.status&&q.shift.id!==undefined)||((q.breakCountZero&&q.shift.breakList!==undefined&&q.shift.breakList.length===0)||(q.shift.breakList!==undefined&&q.shift.breakList.length>0)))){if(!o.configureDefaultShift){q.checkOverlapOfTime(q.shift.selectedInStringIds)
}var O=q.timeClash;
T={shiftName:q.shift.shiftName,shiftStartTime:q.shift.shiftStartTime,shiftEndTime:q.shift.shiftEndTime,workingDayIds:q.weekOffId,breakList:q.shift.breakList,status:q.shift.status};
if(!o.configureDefaultShift){T.departmentIds=q.shift.selectedInStringIds.toString()
}else{T.departmentIds="0";
T.defaultShift=true
}T.shiftCustom=q.addShiftData;
T.shiftDbType=q.dbType;
if(!q.isEditing&&O){h=T;
q.overridePopUpp()
}else{if((!q.isEditing&&!O)||(o.configureDefaultShift&&!q.isEditing)){c.create(T,function(){q.reload=true;
q.clearData();
if(!o.configureDefaultShift){q.retrieveAllDepartments()
}q.addShiftData={};
o.unMaskLoading()
},function(){q.reload=true;
q.addShiftData={};
var ai="Could not create shift, please try again.";
var i=o.error;
o.addMessage(ai,i);
o.unMaskLoading()
})
}else{if(q.isEditing){T.id=q.shift.id;
if(!T.status&&!o.configureDefaultShift){c.userExisrForShift(q.shift.id,function(ai){q.useresExist=false;
if(ai.data){q.useresExist=true
}else{var ak=q.shift.departmentIds.split(",");
q.ovrDeptNames=[];
m(q.multiselecttree.items,ak);
q.deptNameInPopUp=q.ovrDeptNames.toString();
q.tempShiftNotAvailable=false;
if(q.shift.temporaryShifts!==undefined&&q.shift.temporaryShifts!==null){var aj=[];
for(var al=0;
al<q.shift.temporaryShifts.length;
al++){var i=q.shift.temporaryShifts[al];
aj.push(i.tempShiftName)
}q.shiftNameInPopUp="";
q.shiftNameInPopUp=aj.toString()
}else{q.shiftNameInPopUp="";
q.shiftNameInPopUp=q.shift.shiftName;
q.tempShiftNotAvailable=true
}}});
q.update=T;
q.showConfirmationPopup()
}else{if((!O&&q.isEditing)||o.configureDefaultShift){o.maskLoading();
c.update(T,function(){q.clearData();
q.retrieveAllDepartments();
q.addShiftData={};
o.unMaskLoading()
},function(){var ai="Could not update shift, please try again.";
var i=o.error;
o.addMessage(ai,i);
o.unMaskLoading()
})
}else{if(O&&q.isEditing&&T.status){h=T;
q.overridePopUpp()
}}}}}}}}q.reload=true
};
q.removeMainShift=function(i){if(i){c.update(q.update,function(){q.hideConfirmationPopup();
q.clearData();
q.retrieveAllDepartments()
},function(){var s="Could not update shift, please try again.";
var r=o.error;
o.addMessage(s,r)
})
}};
q.removeTempShift=function(i){if(i){o.maskLoading();
q.pId=q.mainShiftId;
c.updateTemporaryShift(q.updateTempShift,function(){q.hideConfirmationPopup();
q.clearData();
q.retrieveAllDepartments();
o.unMaskLoading()
},function(){var s="Could not update shift, please try again.";
var r=o.error;
o.addMessage(s,r);
o.unMaskLoading()
})
}};
q.overridePopUpp=function(){$("#overridePopUp").modal("show")
};
q.hideOverridePopUpp=function(){$("#overridePopUp").modal("hide");
o.removeModalOpenCssAfterModalHide()
};
q.showConfirmationPopup=function(){$("#confirmationPopUp").modal("show")
};
q.hideConfirmationPopup=function(){q.shiftManagementForm.$dirty=false;
q.update={};
q.tempShiftNameInPopUp=undefined;
if(q.shift.tempShiftStatus!==undefined){q.shift.tempShiftStatus=true
}if(q.shift.status!==undefined){q.shift.status=true
}$("#confirmationPopUp").modal("hide");
o.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove()
};
q.showFieldsForTempShift=function(i){q.submitted=false;
q.shift.tempShiftBreakList=[];
q.addTempShift=false;
q.temporaryShift=true;
q.addDefaultShiftPanel=false;
q.alreadyCreated=false;
q.dateRangeNotValid=false;
q.invalidDate=false;
q.alreadyCreated=false;
q.beginitemFlag=false;
q.beginsDurationFlag=false;
q.beginsHolidayOrEventFlag=false;
q.startDayCount=false;
q.enditemFlag=false;
q.endsDayCount=false;
q.endsDurationFlag=false;
q.endsHolidayOrEventFlag1=false;
q.invalidDate=false;
q.alreadyCreated=false;
if(q.tempShiftForm!==undefined){q.tempShiftForm.$setPristine()
}if(i){q.shift.setRule=true;
q.begins.beginsDuration="";
q.begins.beginsHolidayOrEvent="";
q.begins.beginitem="";
q.begins.endsDuration="";
q.begins.endsHolidayOrEvent="";
q.begins.enditem="";
q.listToShowEnd=[];
q.listToShowBegin=[];
q.shift.tempShiftFromDate=null;
q.shift.tempShiftEndDate=null
}q.initializWeekdayFortemp()
};
q.showFieldsForOverrideShift=function(i){if(i){q.departmentIds=undefined;
if(q.shift.selectedInStringIds!==undefined&&q.shift.selectedInStringIds!==null&&q.shift.selectedInStringIds.length>0){q.departmentIds=angular.copy(q.shift.selectedInStringIds.toString())
}q.clearData();
if(o.configureDefaultShift){o.configureDefaultShift=true
}q.overRideShiftFlag=true
}};
q.onTypeChangeBegin=function(){var s=q.begins.beginsHolidayOrEvent;
q.begins.beginitem=undefined;
q.listToShowBegin=[];
if(s===undefined||s===null){q.beginsHolidayOrEventFlag=true
}else{q.beginsHolidayOrEventFlag=false;
for(var r=0;
r<q.list.length;
r++){var t=q.list[r];
if(t.type===s){q.listToShowBegin.push(t)
}}}if(q.listToShowBegin&&q.listToShowBegin.length===0){q.listToShowBegin.push({title:"Not available",type:s})
}};
q.onTypeChangeEnd=function(){q.begins.enditem=undefined;
var s=q.begins.endsHolidayOrEvent;
q.listToShowEnd=[];
if(s===undefined||s===null){q.endsHolidayOrEventFlag1=true
}else{q.listToShowEnd=[];
q.endsHolidayOrEventFlag1=false;
for(var r=0;
r<q.list.length;
r++){var t=q.list[r];
if(t.type===s){q.listToShowEnd.push(t)
}}}if(q.listToShowEnd&&q.listToShowEnd.length===0){q.listToShowEnd.push({title:"Not available",type:s})
}};
q.checkCountForBegins=function(i){if(q.shift.setRule){q.startDayCount=true;
if(q.shift.beginsDayCount){q.startDayCount=false
}}};
q.checkCountForEnds=function(){if(q.shift.setRule){q.endsDayCount=true;
if(q.shift.endsDayCount){q.endsDayCount=false
}}};
q.checkDurationForEnd=function(i){if(q.shift.setRule){q.endsDurationFlag=true;
if(q.begins.endsDuration){q.endsDurationFlag=false
}}};
q.checkDurationForStart=function(){q.beginsDurationFlag=true;
if(q.begins.beginsDuration!==undefined&&q.begins.beginsDuration!==null){q.beginsDurationFlag=false
}};
q.endItemChanged=function(){q.enditemFlag=true;
if(q.begins.enditem!=="--pick holiday--"&&q.begins.enditem!=="--pick event--"){q.enditemFlag=false
}};
q.startItemChanged=function(){q.beginitemFlag=true;
if(q.begins.beginitem!=="--pick holiday--"&&q.begins.beginitem!=="--pick event--"){q.beginitemFlag=false
}};
q.checkConditionForDaysForTempShift=function(s){q.shift.selectedDaysForTempShift=[];
$.each(q.shift.WeekOffListForTempShift,function(t,i){if(i.isChecked==="true"||i.isChecked===true){q.shift.selectedDaysForTempShift.push(i)
}});
if(q.shift.selectedDaysForTempShift.length===7){q.allDaysAreSelectedAsWorkingTempShift=true
}else{q.allDaysAreSelectedAsWorkingTempShift=false
}if(q.shift.selectedDaysForTempShift.length===0){q.shift.WeekOffListForTempShift=[{key:"2",code:"M",value:"Monday",isChecked:false},{key:"3",code:"Tu",value:"Tuesday",isChecked:false},{key:"4",code:"W",value:"Wednesday",isChecked:false},{key:"5",code:"Th",value:"Thursday",isChecked:false},{key:"6",code:"F",value:"Friday",isChecked:false},{key:"7",code:"Sa",value:"Saturday",isChecked:false},{key:"1",code:"S",value:"Sunday",isChecked:false}];
for(var r=0;
r<q.shift.WeekOffListForTempShift.length;
r++){if(q.shift.WeekOffListForTempShift[r].code===s){q.shift.WeekOffListForTempShift[r].isChecked=true;
break
}}}};
q.addTemporaryShift=function(){q.reload=false;
q.shiftTitleIsNull=true;
if(q.shift.tempShiftName){q.shiftTitleIsNull=false
}q.shift.beginsEventOrHolidayId="";
q.shift.endsHolidayOrEventId="";
q.shift.selectedDaysForTempShift=[];
$.each(q.shift.WeekOffListForTempShift,function(ac,i){if(i.isChecked==="true"||i.isChecked===true){q.shift.selectedDaysForTempShift.push(i)
}});
q.shift.weekOffIdForTempShift="";
for(var V in q.shift.selectedDaysForTempShift){var O=q.shift.selectedDaysForTempShift[V].key;
if(O!==undefined&&O!==null&&O!==""){if(q.shift.weekOffIdForTempShift===""){q.shift.weekOffIdForTempShift=O
}else{q.shift.weekOffIdForTempShift+=","+O
}}}q.invalidDate=false;
if(q.shift.setRule===true){q.dateRangeNotValid=false;
if(!(q.begins.beginsDuration!==undefined&&q.begins.beginsDuration.length>0)){q.beginsDurationFlag=true
}if(!(q.begins.beginsHolidayOrEvent!==undefined&&q.begins.beginsHolidayOrEvent.length>0)){q.beginsHolidayOrEventFlag=true
}if(!(q.begins.beginitem!==undefined&&q.begins.beginitem.length>0&&q.begins.beginitem!=="Not available")){q.beginitemFlag=true
}if(!(q.begins.endsDuration!==undefined&&q.begins.endsDuration.length>0)){q.endsDurationFlag=true
}if(!(q.begins.endsHolidayOrEvent!==undefined&&q.begins.endsHolidayOrEvent.length>0)){q.endsHolidayOrEventFlag1=true
}if(!(q.begins.enditem!==undefined&&q.begins.enditem.length>0&&q.begins.enditem!=="Not available")){q.enditemFlag=true
}if((q.shift.beginsDayCount!==undefined&&q.shift.beginsDayCount!==null)){q.startDayCount=false;
var H=parseInt(q.shift.beginsDayCount)
}else{q.startDayCount=true
}if(q.shift.endsDayCount!==undefined&&q.shift.endsDayCount!==null){q.endsDayCount=false;
var C=parseInt(q.shift.endsDayCount)
}else{q.endsDayCount=true
}if(q.begins.endsHolidayOrEvent!==undefined&&q.begins.endsHolidayOrEvent!==null){if(q.begins.endsHolidayOrEvent.charAt(0)==="h"||q.begins.endsHolidayOrEvent.charAt(0)==="H"){for(var V=0;
V<q.holidayListFromDb.length;
V++){if(q.holidayListFromDb[V].title===q.begins.enditem){q.enditemToSent=q.holidayListFromDb[V]
}}}else{for(var V=0;
V<q.eventListFromDb.length;
V++){if(q.eventListFromDb[V].title===q.begins.enditem){q.enditemToSent=q.eventListFromDb[V]
}}}}if(q.begins.beginsHolidayOrEvent!==undefined&&q.begins.beginsHolidayOrEvent!==null){if(q.begins.beginsHolidayOrEvent.charAt(0)==="h"||q.begins.beginsHolidayOrEvent.charAt(0)==="H"){for(var V=0;
V<q.holidayListFromDb.length;
V++){if(q.holidayListFromDb[V].title===q.begins.beginitem){q.beginitemToSent=q.holidayListFromDb[V]
}}}else{for(var V=0;
V<q.eventListFromDb.length;
V++){if(q.eventListFromDb[V].title===q.begins.beginitem){q.beginitemToSent=q.eventListFromDb[V]
}}}}if(q.begins.beginsDuration!==undefined&&q.begins.beginsDuration.length>0){if(q.listToShowBegin!==undefined&&q.listToShowBegin.length>0){if(q.begins.beginsDuration.charAt(0)==="A"||q.begins.beginsDuration.charAt(0)==="a"){q.fromDateForTempShit=q.beginitemToSent.endDt+(H*24*3600*1000)
}else{q.fromDateForTempShit=q.beginitemToSent.startDt-(H*24*3600*1000)
}}}if(q.begins.endsDuration!==undefined&&q.begins.endsDuration.length>0){if(q.listToShowEnd!==undefined&&q.listToShowEnd.length>0){if(q.begins.endsDuration.charAt(0)==="A"||q.begins.endsDuration.charAt(0)==="a"){q.toDateForTempShit=q.enditemToSent.endDt+(C*24*3600*1000)
}else{q.toDateForTempShit=q.enditemToSent.startDt-(C*24*3600*1000)
}}}}else{q.beginitemFlag=false;
q.beginsDurationFlag=false;
q.beginsHolidayOrEventFlag=false;
q.startDayCount=false;
q.enditemFlag=false;
q.endsDayCount=false;
q.endsDurationFlag=false;
q.endsHolidayOrEventFlag1=false;
q.invalidDate=false;
q.dateRangeNotValid=false;
if(q.shift.tempShiftFromDate!==undefined&&q.shift.tempShiftFromDate!==null){q.fromDateForTempShit=new Date(q.shift.tempShiftFromDate)
}else{q.dateRangeNotValid=true
}if(q.shift.tempShiftEndDate!==undefined&&q.shift.tempShiftEndDate!==null){q.toDateForTempShit=new Date(q.shift.tempShiftEndDate)
}else{q.dateRangeNotValid=true
}}if(q.shift.setRule===false){q.fromDateForTempShit=q.fromDateForTempShit.getTime();
q.toDateForTempShit=q.toDateForTempShit.getTime()
}if(q.fromDateForTempShit>q.toDateForTempShit||new Date(q.fromDateForTempShit)<new Date().setHours(0,0,0,0)){q.invalidDate=true
}var t=new Date(q.shift.tempShiftStartTime);
var v=new Date(q.shift.tempShiftEndTime);
var Y=t.getHours();
var P=v.getHours();
var R=t.getMinutes();
var r=v.getMinutes();
q.validBreakTimeSlot=true;
q.invalidTime=false;
if(Y===P&&R===r){q.invalidTime=true
}q.invalidBreakTime=false;
if(q.shift.tempShiftBreakList!==undefined&&q.shift.tempShiftBreakList.length>0&&!q.invalidTime){for(var V=0;
V<q.shift.tempShiftBreakList.length;
V++){var z=new Date(q.shift.tempShiftBreakList[V].breakStartTime);
var M=new Date(q.shift.tempShiftBreakList[V].breakEndTime);
var B=z.getHours();
var y=M.getHours();
var L=z.getMinutes();
var A=M.getMinutes();
if(B===y&&L===A){q.invalidBreakTime=true
}else{if(B===Y&&L===R){q.invalidBreakTime=true
}else{if(y===P&&A===r){q.invalidBreakTime=true
}else{if(Y<P){if(B<Y||B>P||y<Y||y>P){q.invalidBreakTime=true
}else{if((y<B)||(B===y&&A<L)||(B===y&&A<L)){q.invalidBreakTime=true
}else{if((B===Y&&L<R)||(y===P&&A>r)||(B===P&&A>r)){q.invalidBreakTime=true
}}}}else{if((B<Y&&B>P)||(y<Y&&y>P)){q.invalidBreakTime=true
}else{if((B===y&&A<L)){q.invalidBreakTime=true
}else{if((B===Y&&L<R)||(y===P&&A>r)||(B===P&&A>r)){q.invalidBreakTime=true
}else{if((B>y&&y>P)){q.invalidBreakTime=true
}}}}}}}}if(!q.invalidBreakTime){for(var J=0;
J<V;
J++){var s=new Date(q.shift.tempShiftBreakList[J].breakStartTime);
var E=new Date(q.shift.tempShiftBreakList[J].breakEndTime);
var Q=s.getHours();
var I=E.getHours();
var ab=s.getMinutes();
var T=E.getMinutes();
var D=parseInt(T);
var F=parseInt(I);
var Z=F+(D/60);
var u=parseInt(ab);
var w=parseInt(Q);
var aa=w+(u/60);
var K=parseInt(B);
var G=parseInt(L);
var X=K+(G/60);
var U=parseInt(y);
var S=parseInt(A);
var W=U+(S/60);
var N=((aa<X&&X<Z)||(aa<W&&W<Z));
if(!((aa<X||aa>W)&&(Z<X||Z>W)&&(Z>aa)&&(!N))){q.invalidBreakTime=true;
break
}}}}}var x={};
x={tempShiftStatus:q.shift.tempShiftStatus};
q.submitted=true;
if((q.fromDateForTempShit<=q.toDateForTempShit)){if(!q.alreadyCreated&&!q.invalidTime&&!q.invalidBreakTime&&!q.shiftNameInDb&&!q.invalidDate&&!q.beginitemFlag&&!q.enditemFlag&&q.tempShiftForm.$valid){if((x.tempShiftStatus||q.shift.tempShiftId===undefined)&&!q.breakCountZeroForTempShift&&q.shift.tempShiftBreakList!==undefined&&q.shift.tempShiftBreakList.length===0){$("#passwordPopUp").modal("show")
}if(((!x.tempShiftStatus&&q.shift.tempShiftId!==undefined)||((q.breakCountZeroForTempShift&&q.shift.tempShiftBreakList!==undefined&&q.shift.tempShiftBreakList.length===0)||(q.shift.tempShiftBreakList!==undefined&&q.shift.tempShiftBreakList.length>0)))){x={parentShiftId:q.shift.id,tempShiftName:q.shift.tempShiftName,tempShiftStartTime:q.shift.tempShiftStartTime,tempShiftEndTime:q.shift.tempShiftEndTime,tempShiftBreakList:q.shift.tempShiftBreakList,tempShiftWorkingDayIds:q.shift.weekOffIdForTempShift,tempShiftFromDate:q.shift.tempShiftFromDate,tempShiftEndDate:q.shift.tempShiftEndDate,setRule:q.shift.setRule,beginRuleId:q.shift.beginRuleId,endRuleId:q.shift.endRuleId,dateRangeRuleId:q.shift.dateRangeRuleId,tempShiftStatus:q.shift.tempShiftStatus};
if(q.shift.setRule===true){x.beginsDayCount=q.shift.beginsDayCount;
x.beginsAction=q.begins.beginsDuration.charAt(0);
x.beginsEventType=q.begins.beginsHolidayOrEvent;
x.beginsEventOrHolidayId=q.beginitemToSent.id;
x.endsDayCount=q.shift.endsDayCount;
x.endsAction=q.begins.endsDuration.charAt(0);
x.endsEventType=q.begins.endsHolidayOrEvent;
x.endsEventOrHolidayId=q.enditemToSent.id;
x.tempShiftFromDate=new Date(q.fromDateForTempShit);
x.tempShiftEndDate=new Date(q.toDateForTempShit)
}if(q.shift.tempShiftId!==undefined&&q.shift.tempShiftId!==null){x.tempShiftId=q.shift.tempShiftId;
if(!q.shift.tempShiftStatus){c.userExisrForShift(q.shift.tempShiftId,function(ae){q.reload=true;
q.useresExist=false;
if(ae.data){q.useresExist=true
}else{q.tempShiftNotAvailable=true;
q.shift.id=q.shift.tempShiftId.parentShiftId;
q.ovrDeptNames=[];
q.tempShiftNameInPopUp=undefined;
q.tempShiftNameInPopUp=x.tempShiftName;
for(var ad=0;
ad<q.shiftsDetailFromDB.length;
ad++){if(q.mainShiftId===q.shiftsDetailFromDB[ad].id){var ac=q.shiftsDetailFromDB[ad].departmentIds.split(",");
var af=angular.copy(q.multiselecttree);
m(af.items,ac)
}}q.deptNameInPopUp="";
q.deptNameInPopUp=q.ovrDeptNames.toString()
}});
x.tempShiftId=q.shift.tempShiftId;
q.updateTempShift=x;
q.showConfirmationPopup()
}else{o.maskLoading();
q.pId=q.mainShiftId;
c.updateTemporaryShift(x,function(){q.reload=true;
q.hideConfirmationPopup();
q.clearData();
q.retrieveAllDepartments();
o.unMaskLoading()
},function(){var ac="Could not update shift, please try again.";
var i=o.error;
o.addMessage(ac,i);
o.unMaskLoading()
})
}}else{o.maskLoading();
q.pId=q.mainShiftId;
c.createTemporaryShift(x,function(){q.reload=true;
q.addShiftData={};
q.shiftManagementForm.$setPristine();
q.reload=true;
q.clearData();
q.retrieveAllDepartments();
q.temporaryShift=false;
q.addDefaultShiftPanel=true;
o.unMaskLoading()
},function(){var ac="Could not create temporary shift, please try again.";
var i=o.error;
o.addMessage(ac,i);
o.unMaskLoading()
})
}q.reload=true
}}}};
var f=function(u,s){if(u!==undefined&&u!==null){for(var r=0;
r<u.length;
r++){var t=u[r];
if($.inArray(t.id+"",s)!==-1||$.inArray(t.id,s)!==-1){t.isChecked=true
}else{t.isChecked=false
}if(angular.isDefined(t.items)&&t.items!==null){f(t.items,s)
}}}};
q.editShift=function(u){if(angular.isDefined(q.shiftManagementForm)){q.shiftManagementForm.$setPristine()
}q.reload=false;
var y={currentNode:{id:""}};
q.searchedShiftTree=undefined;
if(u.id){y.currentNode.id=u.id;
q.searchedShiftTree=u.id
}else{if(u.currentNode){y.currentNode.id=u.currentNode.id;
q.searchedShiftTree=u.currentNode.id;
$("#scrollable-dropdown-menu.typeahead").typeahead("val","")
}else{if(u.parentShiftId){y.currentNode.id=u.parentShiftId;
q.searchedShiftTree=u.parentShiftId
}else{if(u){y.currentNode.id=u;
q.searchedShiftTree=u
}}}}q.allDaysAreSelectedAsWorking=false;
q.invalidTime=false;
if(y.currentNode.id!==undefined&&y.currentNode.id!==null){q.shiftsDetail=angular.copy(q.shiftsDetailFromDB);
q.shiftNameInDb=false;
q.shiftTitleIsNull=false;
q.searchPage=false;
if(q.shiftsDetail!==undefined&&q.shiftsDetail.length!==0){var A=false;
q.isEditing=true;
for(var r=0;
r<q.shiftsDetail.length;
r++){if(q.shiftsDetail[r].id===y.currentNode.id){q.overRideShiftFlag=false;
if(!angular.isDefined(q.shiftsDetail[r].shiftCustom)||q.shiftsDetail[r].shiftCustom==null||q.shiftsDetail[r].shiftCustom==undefined){var x={primaryKey:parseInt(y.currentNode.id)};
c.retrieveCustomFieldDataById(x,function(i){if(i.shiftCustom!=null){q.shiftsDetail[r].shiftCustom=angular.copy(i.shiftCustom);
q.addShiftData=angular.copy(i.shiftCustom);
if(!!q.addShiftData){angular.forEach(q.listOfModelsOfDateType,function(B){if(q.addShiftData.hasOwnProperty(B)){if(q.addShiftData[B]!==null&&q.addShiftData[B]!==undefined){q.addShiftData[B]=new Date(q.addShiftData[B])
}else{q.addShiftData[B]=""
}}})
}q.reload=true
}},function(){var B="Failed to retrieve custom field. Try again.";
var i=o.failure;
o.addMessage(B,i);
o.unMaskLoading()
})
}q.mainShiftName=q.shiftsDetailFromDB[r].shiftName;
A=true;
q.mainShiftId=y.currentNode.id;
q.shift={};
q.shift=q.shiftsDetail[r];
q.oldShiftName=q.shift.shiftName;
q.shift.selectedInStringIds=q.shiftsDetail[r].departmentIds.split(",");
q.defaultSelectedids=q.shift.selectedInStringIds;
var w=angular.copy(q.multiselecttree);
f(w.items,q.shift.selectedInStringIds);
q.selectedItemList=w;
if(q.shiftsDetail[r].parentShiftId===undefined){q.weekOffIdCode=q.shiftsDetail[r].workingDayIds.split(",");
$.each(q.weekDay.WeekOffList,function(C,B){var i=false;
$.each(q.weekOffIdCode,function(D,E){if(E===B.key){i=true
}});
if(i){B.isChecked=true
}else{B.isChecked=false
}});
q.updateButton=true;
q.temporaryShift=false;
q.addDefaultShiftPanel=true;
break
}break
}}if(A===false){for(var s=0;
s<q.shiftsDetail.length;
s++){if(q.shiftsDetail[s].temporaryShifts!==undefined&&q.shiftsDetail[s].temporaryShifts!==null&&q.shiftsDetail[s].temporaryShifts.length>0){for(var v=0;
v<q.shiftsDetail[s].temporaryShifts.length;
v++){if(q.shiftsDetail[s].temporaryShifts[v].tempShiftId===y.currentNode.id){q.showFieldsForTempShift(false);
q.overRideShiftFlag=false;
q.addTempShift=true;
q.shift={};
q.mainShiftId=q.shiftsDetail[s].temporaryShifts[v].parentShiftId;
for(var t=0;
t<q.shiftsDetailFromDB.length;
t++){if(q.mainShiftId===q.shiftsDetailFromDB[t].id){q.mainShiftName=q.shiftsDetailFromDB[t].shiftName
}}q.shift=q.shiftsDetail[s].temporaryShifts[v];
q.oldShiftName=q.shift.tempShiftName;
if(!q.shift.setRule){q.begins.beginsDuration="";
q.begins.beginsHolidayOrEvent="";
q.begins.beginitem="";
q.begins.endsDuration="";
q.begins.endsHolidayOrEvent="";
q.begins.enditem="";
q.listToShowEnd=[];
q.listToShowBegin=[];
q.shift.tempShiftEndDate=new Date(q.shift.tempShiftEndDate);
q.shift.tempShiftFromDate=new Date(q.shift.tempShiftFromDate);
q.minToDate=new Date(q.shift.tempShiftFromDate)
}else{q.shift.tempShiftFromDate=null;
q.shift.tempShiftEndDate=null
}if(q.shift.setRule){if(q.shift.beginsAction==="a"||q.shift.beginsAction==="A"){q.begins.beginsDuration="after"
}else{q.begins.beginsDuration="before"
}if(q.shift.endsAction==="a"||q.shift.endsAction==="A"){q.begins.endsDuration="after"
}else{q.begins.endsDuration="before"
}if(q.shift.beginsEventType==="h"||q.shift.beginsEventType==="H"){q.begins.beginsHolidayOrEvent="Holiday";
for(var r=0;
r<q.holidayListFromDb.length;
r++){if(q.holidayListFromDb[r].id===q.shift.beginsEventOrHolidayId){q.listToShowBegin=[];
q.begins.beginitem=q.holidayListFromDb[r].title;
for(var r=0;
r<q.list.length;
r++){var z=q.list[r];
if(z.type==="Holiday"){q.listToShowBegin.push(z)
}}}}}else{q.begins.beginsHolidayOrEvent="Event";
for(var r=0;
r<q.eventListFromDb.length;
r++){if(q.eventListFromDb[r].id===q.shift.beginsEventOrHolidayId){q.listToShowBegin=[];
q.begins.beginitem=q.eventListFromDb[r].title;
for(var r=0;
r<q.list.length;
r++){var z=q.list[r];
if(z.type==="Event"){q.listToShowBegin.push(z)
}}}}}if(q.shift.endsEventType==="h"||q.shift.endsEventType==="H"){q.begins.endsHolidayOrEvent="Holiday";
for(var r=0;
r<q.holidayListFromDb.length;
r++){if(q.holidayListFromDb[r].id===q.shift.endsEventOrHolidayId){q.begins.enditem=q.holidayListFromDb[r];
q.listToShowEnd=[];
q.begins.enditem=q.holidayListFromDb[r].title;
for(var r=0;
r<q.list.length;
r++){var z=q.list[r];
if(z.type==="Holiday"){q.listToShowEnd.push(z)
}}}}}else{q.begins.endsHolidayOrEvent="Event";
for(var r=0;
r<q.eventListFromDb.length;
r++){if(q.eventListFromDb[r].id===q.shift.endsEventOrHolidayId){q.listToShowEnd=[];
q.begins.enditem=q.eventListFromDb[r].title;
for(var r=0;
r<q.list.length;
r++){var z=q.list[r];
if(z.type==="Event"){q.listToShowEnd.push(z)
}}}}}}q.shift.selectedInString=[];
q.shift.selectedInString=q.shiftsDetail[s].temporaryShifts[v].departmentIds;
q.temporaryShift=true;
q.updateButton=true;
q.addDefaultShiftPanel=false;
q.shift.weekOffIdForTempShift=q.shiftsDetail[s].temporaryShifts[v].tempShiftWorkingDayIds.split(",");
q.initializWeekdayFortemp();
$.each(q.shift.WeekOffListForTempShift,function(C,B){var i=false;
$.each(q.shift.weekOffIdForTempShift,function(D,E){if(E===B.key){i=true
}});
if(i){B.isChecked=true
}else{B.isChecked=false
}});
A=true;
break
}}}}}if(A===false){for(var s=0;
s<q.shiftsDetail.length;
s++){if(q.shiftsDetail[s].overRideShifts!==undefined&&q.shiftsDetail[s].overRideShifts!==null&&q.shiftsDetail[s].overRideShifts.length>0){for(var v=0;
v<q.shiftsDetail[s].overRideShifts.length;
v++){if(q.shiftsDetail[s]!==undefined&&q.shiftsDetail[s].overRideShifts[v].id===y.currentNode.id){q.overRideShiftFlag=true;
q.ovrShiftId=y.currentNode.id;
if(!angular.isDefined(q.shiftsDetail[s].overRideShifts[v].shiftCustom)||q.shiftsDetail[s].overRideShifts[v].shiftCustom==null||q.shiftsDetail[s].overRideShifts[v].shiftCustom==undefined){var x={primaryKey:parseInt(y.currentNode.id)};
c.retrieveCustomFieldDataById(x,function(i){if(Object.getOwnPropertyNames(i.shiftCustom).length>0){q.shiftsDetail[s].overRideShifts[v].shiftCustom=angular.copy(i.shiftCustom);
q.addShiftData=angular.copy(i.shiftCustom);
if(!!q.addShiftData){angular.forEach(q.listOfModelsOfDateType,function(B){if(q.addShiftData.hasOwnProperty(B)){if(q.addShiftData[B]!==null&&q.addShiftData[B]!==undefined){q.addShiftData[B]=new Date(q.addShiftData[B])
}else{q.addShiftData[B]=""
}}})
}q.reload=true
}},function(){var B="Failed to retrieve custom field. Try again.";
var i=o.failure;
o.addMessage(B,i);
o.unMaskLoading()
})
}A=true;
q.mainShiftId=y.currentNode.id;
q.shift={};
q.shift=q.shiftsDetail[s].overRideShifts[v];
q.shift.status=true;
q.oldShiftName=q.shift.shiftName;
q.shift.selectedInStringIds=q.shiftsDetail[s].overRideShifts[v].departmentIds.split(",");
q.departmentIds=angular.copy(q.shift.selectedInStringIds.toString());
q.defaultSelectedids=q.shift.selectedInStringIds;
var w=angular.copy(q.multiselecttree);
f(w.items,q.shift.selectedInStringIds);
q.selectedItemList=w;
if(q.shiftsDetail[s].overRideShifts[v].parentShiftId===undefined){q.weekOffIdCode=q.shiftsDetail[s].overRideShifts[v].workingDayIds.split(",");
$.each(q.weekDay.WeekOffList,function(C,B){var i=false;
$.each(q.weekOffIdCode,function(D,E){if(E===B.key){i=true
}});
if(i){B.isChecked=true
}else{B.isChecked=false
}});
q.updateButton=true;
q.temporaryShift=false;
q.addDefaultShiftPanel=true;
break
}break
}}}}}}}};
q.hstep=1;
q.mstep=1;
q.options={hstep:[1,2,3],mstep:[1,5,10,15,25,30]};
q.ismeridian=true;
q.today=function(){q.dt=new Date()
};
q.today();
q.showWeeks=true;
q.toggleWeeks=function(){q.showWeeks=!q.showWeeks
};
q.clear=function(){q.dt=null
};
q.disabled=function(i,r){return(r==="day"&&(i.getDay()===0))
};
q.toggleMin=function(){q.minDate=(q.minDate)?null:new Date();
var i=new Date();
q.maxDate=(q.maxDate)?null:new Date(i.getFullYear(),11,31)
};
q.toggleMin();
q.setMinDate=function(){if(q.shift.tempShiftFromDate===""||q.shift.tempShiftFromDate===undefined){q.dateRangeNotValid=false;
q.minToDate=new Date()
}else{q.dateRangeNotValid=false;
q.minToDate=q.shift.tempShiftFromDate;
if(!q.isEdit){}}if(q.shift.tempShiftFromDate>q.shift.tempShiftEndDate){q.shift.tempShiftEndDate=q.shift.tempShiftFromDate
}};
q.setMinDate();
q.datePicker={};
q.open=function(i,r){i.preventDefault();
i.stopPropagation();
q.datePicker[r]=true
};
q.dateOptions={"year-format":"'yy'","starting-day":1};
q.formats=["dd-MMMM-yyyy","yyyy/MM/dd","shortDate"];
q.format=o.dateFormat;
q.setRuleChanged=function(){if(q.shift.setRule){q.dateRangeNotValid=false;
q.invalidDate=false;
q.alreadyCreated=false
}else{q.beginitemFlag=false;
q.beginsDurationFlag=false;
q.beginsHolidayOrEventFlag=false;
q.startDayCount=false;
q.enditemFlag=false;
q.endsDayCount=false;
q.endsDurationFlag=false;
q.endsHolidayOrEventFlag1=false;
q.invalidDate=false;
q.alreadyCreated=false
}};
q.createOsverrideShift=function(){o.maskLoading();
c.create(h,function(){q.reload=true;
q.addShiftData={};
q.clearData();
q.retrieveAllDepartments();
q.hideOverridePopUpp();
o.unMaskLoading()
},function(){var r="Could not create shift, please try again.";
var i=o.error;
o.addMessage(r,i);
o.unMaskLoading()
})
};
q.updateOsverrideShift=function(){o.maskLoading();
c.update(h,function(){q.reload=true;
q.addShiftData={};
q.clearData();
q.retrieveAllDepartments();
q.hideOverridePopUpp();
o.unMaskLoading()
},function(){var r="Could not update shift, please try again.";
var i=o.error;
o.addMessage(r,i);
o.unMaskLoading()
})
};
q.addNewOverRideShift=function(O,B){q.reload=false;
q.submitted=true;
if(o.configureDefaultShift){if(!q.shift.shiftStartTime){q.shift.shiftStartTime=new Date()
}if(!q.shift.shiftEndTime){q.shift.shiftEndTime=new Date()
}}var t=new Date(q.shift.shiftStartTime);
var v=new Date(q.shift.shiftEndTime);
var ad=t.getHours();
var W=angular.copy(ad);
var Q=v.getHours();
var U=t.getMinutes();
var r=v.getMinutes();
q.validBreakTimeSlot=true;
q.invalidTime=false;
if(ad===Q&&U===r){q.invalidTime=true
}q.invalidBreakTime=false;
var D=[];
var H=[];
if(q.shift.breakList!==undefined&&q.shift.breakList.length>0&&!q.invalidTime){while((W!==Q)){D.push(W);
W++;
if(W>23){W-=24
}}for(var Z=0;
Z<q.shift.breakList.length;
Z++){var y=new Date(q.shift.breakList[Z].breakStartTime);
var N=new Date(q.shift.breakList[Z].breakEndTime);
var A=y.getHours();
var aa=angular.copy(A);
var x=N.getHours();
var M=y.getMinutes();
var z=N.getMinutes();
var R=[];
while((aa!==x)){R.push(aa);
aa++;
if(aa>23){aa-=24
}}for(var G=0;
G<R.length;
G++){if(D.indexOf(R[G])===-1){q.invalidBreakTime=true;
break
}}if(q.invalidBreakTime){break
}else{if(A===ad){if(M<=U){q.invalidBreakTime=true;
break
}}if(x===Q){if(z>=r){q.invalidBreakTime=true;
break
}}if(x===A){if(M>=z){q.invalidBreakTime=true;
break
}}if(A===ad&&(x===Q)&&(ad===Q)){if(M<=U||z>=r||M>=r||z<=U){q.invalidBreakTime=true;
break
}}}if(!q.invalidBreakTime){for(var K=0;
K<Z;
K++){var s=new Date(q.shift.breakList[K].breakStartTime);
var E=new Date(q.shift.breakList[K].breakEndTime);
var T=s.getHours();
var J=E.getHours();
var ag=s.getMinutes();
var X=E.getMinutes();
var C=parseInt(X);
var F=parseInt(J);
var ae=F+(C/60);
var u=parseInt(ag);
var w=parseInt(T);
var af=w+(u/60);
var L=parseInt(A);
var I=parseInt(M);
var ac=L+(I/60);
var Y=parseInt(x);
var V=parseInt(z);
var ab=Y+(V/60);
if(af!==undefined&&ae!==undefined&&ac!==undefined&&ab!==undefined){var O=((af<ac&&ac<ae)||(af<ab&&ab<ae));
if((af<ac||af>ab)&&(ae<ac||ae>ab)&&(!O)){}else{q.invalidBreakTime=true
}}}}}}q.selectedDays=[];
$.each(q.weekDay.WeekOffList,function(ah,i){if(i.isChecked==="true"||i.isChecked===true){q.selectedDays.push(i)
}});
q.weekOffId="";
for(var Z in q.selectedDays){var P=q.selectedDays[Z].key;
if(P!=="undefined"||P!==null||P!==""){if(q.weekOffId===""){q.weekOffId=P
}else{q.weekOffId+=","+P
}}}var S={};
q.departmentRequired=false;
if(q.shift.shiftName===undefined||q.shift.shiftName===null||q.shift.shiftName===""){q.shiftTitleIsNull=true
}if(o.configureDefaultShift||q.shift.defaultShift||q.overRideShiftFlag){q.departmentRequired=true
}else{if(q.shift.selectedInStringIds!==undefined&&q.shift.selectedInStringIds.length>0){S.departmentIds=q.shift.selectedInStringIds.toString();
S.defaultShift=false;
q.departmentRequired=true
}else{q.departmentRequired=false
}}q.breakNameInDb=false;
if(q.shiftManagementForm.$valid&&q.shift.shiftStartTime!==undefined&&q.shift.shiftEndTime!==undefined){S={status:q.shift.status};
if((S.status||!q.isEditing)&&!q.breakCountZero&&q.shift.breakList!==undefined&&q.shift.breakList.length===0){$("#passwordPopUp").modal("show")
}if(o.configureDefaultShift&&q.shift.breakList===undefined){$("#passwordPopUp").modal("show");
q.shift.breakList=[]
}if(!q.shiftNameInDb&&q.selectedDays.length!==0&&!q.breakNameInDb&&q.departmentRequired&&!q.invalidTime&&!q.invalidBreakTime&&((!S.status&&q.shift.id!==undefined)||((q.breakCountZero&&q.shift.breakList!==undefined&&q.shift.breakList.length===0)||(q.shift.breakList!==undefined&&q.shift.breakList.length>0)))){var P;
if(o.configureDefaultShift){P=q.defaultShift.id
}else{P=q.mainShiftId
}S={id:P,shiftName:q.shift.shiftName,shiftStartTime:q.shift.shiftStartTime,shiftEndTime:q.shift.shiftEndTime,workingDayIds:q.weekOffId,breakList:q.shift.breakList,status:q.shift.status,overrideShift:true,departmentIds:q.departmentIds};
if(o.configureDefaultShift){S.departmentIds="0";
S.defaultShift=true
}S.shiftCustom=q.addShiftData;
S.shiftDbType=q.dbType;
if(q.overRideShiftFlag){c.create(S,function(){q.reload=true;
q.clearData();
if(!o.configureDefaultShift){q.retrieveAllDepartments()
}q.addShiftData={};
o.unMaskLoading()
},function(){q.reload=true;
q.addShiftData={};
var ah="Could not create shift, please try again.";
var i=o.error;
o.addMessage(ah,i);
o.unMaskLoading()
})
}else{if(q.isEditing){S.id=q.shift.id;
if(!S.status&&!o.configureDefaultShift){c.userExisrForShift(q.shift.id,function(ah){q.useresExist=false;
if(ah.data){q.useresExist=true
}else{var aj=q.shift.departmentIds.split(",");
q.ovrDeptNames=[];
m(q.multiselecttree.items,aj);
q.deptNameInPopUp=q.ovrDeptNames.toString();
q.tempShiftNotAvailable=false;
if(q.shift.temporaryShifts!==undefined&&q.shift.temporaryShifts!==null){var ai=[];
for(var ak=0;
ak<q.shift.temporaryShifts.length;
ak++){var i=q.shift.temporaryShifts[ak];
ai.push(i.tempShiftName)
}q.shiftNameInPopUp="";
q.shiftNameInPopUp=ai.toString()
}else{q.shiftNameInPopUp="";
q.shiftNameInPopUp=q.shift.shiftName;
q.tempShiftNotAvailable=true
}}});
q.update=S;
q.showConfirmationPopup()
}else{if((!res&&q.isEditing)||o.configureDefaultShift){o.maskLoading();
c.update(S,function(){q.clearData();
q.retrieveAllDepartments();
q.addShiftData={};
o.unMaskLoading()
},function(){var ah="Could not update shift, please try again.";
var i=o.error;
o.addMessage(ah,i);
o.unMaskLoading()
})
}else{if(res&&q.isEditing&&S.status){h=S;
q.overridePopUpp()
}}}}}}}q.reload=true
};
q.saveOverRideShift=function(O,B){q.reload=false;
q.submitted=true;
if(o.configureDefaultShift){if(!q.shift.shiftStartTime){q.shift.shiftStartTime=new Date()
}if(!q.shift.shiftEndTime){q.shift.shiftEndTime=new Date()
}}var t=new Date(q.shift.shiftStartTime);
var v=new Date(q.shift.shiftEndTime);
var ad=t.getHours();
var W=angular.copy(ad);
var Q=v.getHours();
var U=t.getMinutes();
var r=v.getMinutes();
q.validBreakTimeSlot=true;
q.invalidTime=false;
if(ad===Q&&U===r){q.invalidTime=true
}q.invalidBreakTime=false;
var D=[];
var H=[];
if(q.shift.breakList!==undefined&&q.shift.breakList.length>0&&!q.invalidTime){while((W!==Q)){D.push(W);
W++;
if(W>23){W-=24
}}for(var Z=0;
Z<q.shift.breakList.length;
Z++){var y=new Date(q.shift.breakList[Z].breakStartTime);
var N=new Date(q.shift.breakList[Z].breakEndTime);
var A=y.getHours();
var aa=angular.copy(A);
var x=N.getHours();
var M=y.getMinutes();
var z=N.getMinutes();
var R=[];
while((aa!==x)){R.push(aa);
aa++;
if(aa>23){aa-=24
}}for(var G=0;
G<R.length;
G++){if(D.indexOf(R[G])===-1){q.invalidBreakTime=true;
break
}}if(q.invalidBreakTime){break
}else{if(A===ad){if(M<=U){q.invalidBreakTime=true;
break
}}if(x===Q){if(z>=r){q.invalidBreakTime=true;
break
}}if(x===A){if(M>=z){q.invalidBreakTime=true;
break
}}if(A===ad&&(x===Q)&&(ad===Q)){if(M<=U||z>=r||M>=r||z<=U){q.invalidBreakTime=true;
break
}}}if(!q.invalidBreakTime){for(var K=0;
K<Z;
K++){var s=new Date(q.shift.breakList[K].breakStartTime);
var E=new Date(q.shift.breakList[K].breakEndTime);
var T=s.getHours();
var J=E.getHours();
var ag=s.getMinutes();
var X=E.getMinutes();
var C=parseInt(X);
var F=parseInt(J);
var ae=F+(C/60);
var u=parseInt(ag);
var w=parseInt(T);
var af=w+(u/60);
var L=parseInt(A);
var I=parseInt(M);
var ac=L+(I/60);
var Y=parseInt(x);
var V=parseInt(z);
var ab=Y+(V/60);
if(af!==undefined&&ae!==undefined&&ac!==undefined&&ab!==undefined){var O=((af<ac&&ac<ae)||(af<ab&&ab<ae));
if((af<ac||af>ab)&&(ae<ac||ae>ab)&&(!O)){}else{q.invalidBreakTime=true
}}}}}}q.selectedDays=[];
$.each(q.weekDay.WeekOffList,function(ah,i){if(i.isChecked==="true"||i.isChecked===true){q.selectedDays.push(i)
}});
q.weekOffId="";
for(var Z in q.selectedDays){var P=q.selectedDays[Z].key;
if(P!=="undefined"||P!==null||P!==""){if(q.weekOffId===""){q.weekOffId=P
}else{q.weekOffId+=","+P
}}}var S={};
q.departmentRequired=false;
if(q.shift.shiftName===undefined||q.shift.shiftName===null||q.shift.shiftName===""){q.shiftTitleIsNull=true
}if(o.configureDefaultShift||q.shift.defaultShift||q.overRideShiftFlag){q.departmentRequired=true
}else{if(q.shift.selectedInStringIds!==undefined&&q.shift.selectedInStringIds.length>0){S.departmentIds=q.shift.selectedInStringIds.toString();
S.defaultShift=false;
q.departmentRequired=true
}else{q.departmentRequired=false
}}q.breakNameInDb=false;
if(q.shiftManagementForm.$valid&&q.shift.shiftStartTime!==undefined&&q.shift.shiftEndTime!==undefined){S={status:q.shift.status};
if((S.status||!q.isEditing)&&!q.breakCountZero&&q.shift.breakList!==undefined&&q.shift.breakList.length===0){$("#passwordPopUp").modal("show")
}if(o.configureDefaultShift&&q.shift.breakList===undefined){$("#passwordPopUp").modal("show");
q.shift.breakList=[]
}if(!q.shiftNameInDb&&q.selectedDays.length!==0&&!q.breakNameInDb&&q.departmentRequired&&!q.invalidTime&&!q.invalidBreakTime&&((!S.status&&q.shift.id!==undefined)||((q.breakCountZero&&q.shift.breakList!==undefined&&q.shift.breakList.length===0)||(q.shift.breakList!==undefined&&q.shift.breakList.length>0)))){S={id:q.ovrShiftId,shiftName:q.shift.shiftName,shiftStartTime:q.shift.shiftStartTime,shiftEndTime:q.shift.shiftEndTime,workingDayIds:q.weekOffId,breakList:q.shift.breakList,status:q.shift.status,overrideShift:true,departmentIds:q.departmentIds};
if(o.configureDefaultShift){S.departmentIds="0";
S.defaultShift=true
}S.shiftCustom=q.addShiftData;
S.shiftDbType=q.dbType;
if(q.overRideShiftFlag){c.update(S,function(){q.clearData();
q.retrieveAllDepartments();
q.addShiftData={};
o.unMaskLoading()
},function(){q.reload=true;
q.addShiftData={};
var ah="Could not create shift, please try again.";
var i=o.error;
o.addMessage(ah,i);
o.unMaskLoading()
})
}else{if(q.isEditing){S.id=q.shift.id;
if(!S.status&&!o.configureDefaultShift){c.userExisrForShift(q.shift.id,function(ah){q.useresExist=false;
if(ah.data){q.useresExist=true
}else{var aj=q.shift.departmentIds.split(",");
q.ovrDeptNames=[];
m(q.multiselecttree.items,aj);
q.deptNameInPopUp=q.ovrDeptNames.toString();
q.tempShiftNotAvailable=false;
if(q.shift.temporaryShifts!==undefined&&q.shift.temporaryShifts!==null){var ai=[];
for(var ak=0;
ak<q.shift.temporaryShifts.length;
ak++){var i=q.shift.temporaryShifts[ak];
ai.push(i.tempShiftName)
}q.shiftNameInPopUp="";
q.shiftNameInPopUp=ai.toString()
}else{q.shiftNameInPopUp="";
q.shiftNameInPopUp=q.shift.shiftName;
q.tempShiftNotAvailable=true
}}});
q.update=S;
q.showConfirmationPopup()
}else{if((!res&&q.isEditing)||o.configureDefaultShift){o.maskLoading();
c.update(S,function(){q.clearData();
q.retrieveAllDepartments();
q.addShiftData={};
o.unMaskLoading()
},function(){var ah="Could not update shift, please try again.";
var i=o.error;
o.addMessage(ah,i);
o.unMaskLoading()
})
}else{if(res&&q.isEditing&&S.status){h=S;
q.overridePopUpp()
}}}}}}}q.reload=true
};
o.unMaskLoading()
}])
});