define(["hkg","manageHolidayService","addMasterValue","dynamicForm"],function(a){a.register.controller("ManageHoliday",["$rootScope","$scope","$filter","ManageHolidayService","$location","$route","$timeout","$http","limitToFilter","DynamicFormService",function(j,l,g,c,f,i,d,h,b,k){j.maskLoading();
j.mainMenu="manageLink";
j.childMenu="manageHoliday";
j.activateMenu();
l.holidayOperation;
l.holidayName="";
l.searchedHolidayRecords=[];
l.allHolidays=[];
l.listOfModelsOfDateType=[];
var e=k.retrieveSectionWiseCustomFieldInfo("manageHoliday");
l.dbType={};
e.then(function(m){l.listOfModelsOfDateType=[];
l.customTemplateDate=angular.copy(m.genralSection);
l.genralHolidayTemplate=j.getCustomDataInSequence(l.customTemplateDate);
if(l.genralHolidayTemplate!==null&&l.genralHolidayTemplate!==undefined){angular.forEach(l.genralHolidayTemplate,function(n){if(n.type!==null&&n.type!==undefined&&n.type==="date"){l.listOfModelsOfDateType.push(n.model)
}})
}},function(m){},function(m){});
l.retriveAllHoliday=function(){j.maskLoading();
c.retrieveAllHoliday(function(m){l.allHolidays=[];
angular.forEach(m.data,function(n){n.title=j.translateValue("H_NAME."+n.title);
l.allHolidays.push(n)
});
j.editHoliday=undefined;
if(l.allHolidays.length>0){l.renderTable=true
}else{l.renderTable=false
}l.backupHoliday=m.data;
j.unMaskLoading()
},function(){j.addMessage("Failed to load all holidays.",j.failure);
j.unMaskLoading()
})
};
l.setHolidayForEdit=function(m){l.setHolidayOperation("addHoliday",function(){l.isEdit=true;
l.holiday=m;
l.holiday.status="Active";
l.holiday.startDt=new Date(l.holiday.startDt);
l.holiday.endDt=new Date(l.holiday.endDt);
l.addHolidayData=m.holidayCustom;
if(!!l.addHolidayData){angular.forEach(l.listOfModelsOfDateType,function(n){if(l.addHolidayData.hasOwnProperty(n)){if(l.addHolidayData[n]!==null&&l.addHolidayData[n]!==undefined){l.addHolidayData[n]=new Date(l.addHolidayData[n])
}else{l.addHolidayData[n]=""
}}})
}l.selectedHolidayTitle={id:"selected",text:l.holiday.title}
})
};
l.redirectToEdit=function(n){var m=true;
var o={primaryKey:n};
j.maskLoading();
c.retrieveHolidayById(o,function(p){l.holiday=p.data;
l.setHolidayForEdit(l.holiday)
},function(){j.addMessage("Failed to retrieve  holiday.",j.failure);
j.unMaskLoading()
});
if(m){l.renderTable=false
}};
l.getSearchedRecords=function(n){var m=$("#scrollable-dropdown-menu.typeahead").typeahead("val");
if(m.length>0){if(m.length<3){l.searchedHolidayRecords=[];
l.setHolidayOperation("searchedHoliday")
}else{l.searchedHolidayRecords=angular.copy(n);
l.setHolidayOperation("searchedHoliday")
}}};
l.resetList=function(){l.allHolidays=l.backupHoliday;
l.isRecordsExits=true
};
l.setHolidayOperation=function(m,n){l.holidayOperation=m;
if(m==="manageHoliday"){if(j.getCurrentServerDate()!==undefined){l.currentYear=j.getCurrentServerDate().getFullYear()
}l.removeFlage=false;
l.holidayForRemove={};
l.entity="HOLIDAY.";
l.searchedHoliday=[];
l.filteredValue=[];
l.backupHoliday=[];
l.isRecordsExits=true;
l.retriveAllHoliday();
$("#scrollable-dropdown-menu.typeahead").typeahead("val","")
}else{if(m==="addHoliday"){$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
l.currentYear=j.getCurrentServerDate().getFullYear();
l.holiday={};
l.selectedHolidayTitle="";
l.holiday.startDt="";
l.isEdit=false;
l.uploadResponseMsgs=[];
l.distinctHoliday=[];
l.uploadStatus=false;
l.updatedTitle="";
j.fileUploadMsg="";
j.crudMsg="";
j.crudMsgType="";
l.holidayStatus="";
l.errorMsg=[];
l.warningMsg=[];
l.entity="HOLIDAY.";
l.holidaySuggestion;
l.holidayList=[];
l.uploadWorksheetFile="";
l.holidayForm;
l.holidayIdForRemove;
l.options=[{name:"Active",id:1},{name:"Remove",id:2}];
l.addHolidayData=k.resetSection(l.genralHolidayTemplate);
l.cancelHoliday=function(o){l.holiday={};
o.$setPristine();
l.submitted=false;
l.setHolidayOperation("manageHoliday")
};
l.retriveDistinctHoliday=function(){if(j.suggestedHolidays!==undefined){l.holidaySuggestion=j.suggestedHolidays
}j.maskLoading();
c.retrievePreviousYearDistinctHoliday(function(p){var o=p.data;
$.each(o,function(q,r){l.distinctHoliday.push({id:r,text:r})
});
j.unMaskLoading()
},function(){j.addMessage("Failed to load all holidays.",j.failure);
j.unMaskLoading()
})
};
l.retriveDistinctHoliday();
l.initAddHolidayForm=function(o){l.datepicker={from:false,end:false};
l.open=function(p,q){p.preventDefault();
p.stopPropagation();
l.datepicker[q]=true
};
o.$dirty=false
};
l.updateHoliday=function(r,p){l.holidayForm=r;
var s=function(t){l.edituploadResponseMsgs=t.messages;
var v=false;
var u=false;
$.each(l.edituploadResponseMsgs,function(w,x){if(x.responseCode===1){v=true
}if(x.responseCode===2){u=true
}});
if(!v&&u){$("#warningShowId").modal("show")
}if(l.edituploadResponseMsgs!==undefined&&l.edituploadResponseMsgs[0].responseCode){l.holiday=p;
j.unMaskLoading()
}else{r.$setPristine();
l.submitted=false;
l.holiday={};
l.isEdit=false;
j.editHoliday=undefined;
l.uploadResponseMsgs=[];
j.addMessage(t.messages[0].message,j.success);
l.setHolidayOperation("manageHoliday");
j.unMaskLoading()
}};
var o=function(){j.crudMsg="Holiday could not be saved. Try again.";
j.crudMsgType=j.failure;
j.unMaskLoading()
};
l.submitted=true;
if(r.$valid){r.$setPristine();
l.submitted=false;
if(l.holiday.status==="Remove"){l.showRemoveConfirmation(p);
l.holidayIdForRemove=p.id
}else{var q={id:p.id,startDt:p.startDt,title:p.title,endDt:p.endDt,holidayCustom:l.addHolidayData,dbType:l.dbType};
var q={};
if(p.forceEdit===true){q={id:p.id,startDt:p.startDt,title:p.title,endDt:p.endDt,holidayCustom:l.addHolidayData,dbType:l.dbType,forceEdit:"true"}
}else{q={id:p.id,startDt:p.startDt,title:p.title,endDt:p.endDt,holidayCustom:l.addHolidayData,dbType:l.dbType}
}j.maskLoading();
c.editHoliday(q,s,o);
l.holiday={}
}}};
l.saveHoliday=function(r,p){l.holidayForm=r;
var s=function(t){r.$setPristine();
l.submitted=false;
l.messages=t.messages;
var v=false;
var u=false;
$.each(l.messages,function(w,x){if(x.responseCode===1){v=true
}if(x.responseCode===2){u=true
}});
if(!v&&u){$("#warningShowId").modal("show")
}if(!(l.messages!==undefined&&l.messages[0].responseCode)){l.messages=[];
j.addMessage(t.messages[0].message,j.success);
l.setHolidayOperation("manageHoliday")
}j.unMaskLoading()
};
var o=function(){j.crudMsg="Holidays could not be updated";
j.crudMsgType=j.failure;
j.unMaskLoading()
};
l.submitted=true;
if(r.$valid){var q={startDt:p.startDt,title:p.title,endDt:p.endDt,forceEdit:p.forceEdit,holidayCustom:l.addHolidayData,dbType:l.dbType};
j.maskLoading();
if(l.holidayForm.$valid){c.saveHoliday(q,s,o)
}}};
l.proceedOk=function(o){$("#warningShowId").modal("hide");
j.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove();
l.holiday.forceEdit=true;
if(l.isEdit){l.updateHoliday(l.holidayForm,l.holiday)
}else{l.saveHoliday(l.holidayForm,l.holiday)
}};
l.proceedCancel=function(){$("#warningShowId").modal("hide");
j.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove()
};
l.uploadHolidayConfig={target:j.appendAuthToken(j.apipath+"holiday/uploadholidayworksheet"),singleFile:true,testChunks:false,query:{fileName:"shreya"}};
l.validateExtension=function(o){l.uploadHolidayConfig.query.fileName=o.name;
l.uploadWorksheetFile=o.name;
if((o.getExtension()!=="xls")&&(o.getExtension()!=="xlsx")){j.addMessage("Only xls files are allowed.Please upload valid file.",j.failure)
}else{l.seletedFileType=o.getExtension();
if(o.size>5242880){var q="File size too large. Upload file with size less than 5MB.";
var p=j.failure;
j.addMessage(q,p);
return false
}}return !!{xls:1,xlsx:1}[o.getExtension()]
};
l.successUpload=function(o,p){l.holidayForm=p;
l.holidayForm.$setPristine();
l.submitted=false;
o=(JSON.parse(o));
l.uploadResponseMsgs=o.messages;
if(!l.uploadResponseMsgs[0].responseCode){l.uploadStatus=true;
j.addMessage(o.messages[0].message,j.success);
l.setHolidayOperation("manageHoliday")
}else{$.each(l.uploadResponseMsgs,function(q,r){if(r.responseCode===1){l.errorMsg.push(r)
}if(r.responseCode===2){l.warningMsg.push(r)
}});
l.uploadStatus=false;
l.holiday={};
$("#showId").modal("show")
}};
l.showDialog=function(){$("#showId").modal("show")
};
l.hideErrorPopup=function(){l.errorMsg=[];
l.warningMsg=[];
$("#showId").modal("hide");
j.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove()
};
l.forceAddHoliday=function(){var p=function(q){$("#showId").modal("hide");
j.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove();
if(!q.responseCode){l.uploadStatus=true;
j.fileUploadMsg=q.messages[0].message;
l.setHolidayOperation("manageHoliday")
}j.unMaskLoading()
};
var o=function(){j.unMaskLoading()
};
j.maskLoading();
c.forceHolidayAdd(p,o)
};
l.removeHolidayById=function(){var p={primaryKey:l.holidayIdForRemove};
var q=function(r){var t=r.messages[0].message;
var s=j.success;
j.addMessage(t,s);
l.setHolidayOperation("manageHoliday");
j.unMaskLoading()
};
var o=function(){var s=" Holiday could not be removed. Try again.";
var r=j.failure;
j.addMessage(s,r);
j.unMaskLoading()
};
j.maskLoading();
c.removeHoliday(p,q,o);
l.holidayIdForRemove=""
};
l.showRemoveConfirmation=function(o){$("#removeShowId").modal("show");
l.holidayName=o.title
};
l.removeOk=function(){$("#removeShowId").modal("hide");
j.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove();
l.removeHolidayById()
};
l.removeCancel=function(){l.holiday.status="Active";
$("#removeShowId").modal("hide");
j.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove()
};
l.today=function(){l.dt=new Date()
};
l.today();
l.showWeeks=true;
l.toggleWeeks=function(){l.showWeeks=!l.showWeeks
};
l.clear=function(){l.dt=null
};
l.disabled=function(o,p){return(p==="day"&&(o.getDay()===0))
};
l.toggleMin=function(){var o=j.getCurrentServerDate();
l.minFromDate=j.getCurrentServerDate();
l.minToDate=new Date(o.getFullYear()-1,12,1);
l.maxFromDate=new Date(o.getFullYear(),11,31);
l.maxToDate=new Date(o.getFullYear(),11,31)
};
l.toggleMin();
l.setMaxDate=function(){l.maxDate=l.holiday.endDt
};
l.setToDate=function(){if(l.holiday.startDt===""||l.holiday.startDt===undefined){l.minToDate=j.getCurrentServerDate()
}else{l.minToDate=l.holiday.startDt;
if(!l.isEdit){l.holiday.endDt=l.holiday.startDt
}}if(l.holiday.startDt>l.holiday.endDt){l.holiday.endDt=l.holiday.startDt
}};
l.setFromDate=function(){l.maxFromDate=l.holiday.endDt
};
l.setMaxDate=function(){};
l.dateOptions={"year-format":"'yy'","starting-day":1};
l.formats=["dd-MMMM-yyyy","yyyy/MM/dd","shortDate"];
l.format=j.dateFormat;
l.today();
l.showWeeks=true;
l.toggleWeeks=function(){l.showWeeks=!l.showWeeks
};
l.clear=function(){l.dt=null
}
}}if(!!n){n()
}};
l.setHolidayOperation("manageHoliday");
l.today=function(){l.dt=j.getCurrentServerDate()
};
l.showConfirmDialog=function(m){l.holidayForRemove=m;
l.holidayName=m.title;
$("#removePopupId").modal("show")
};
l.removeHoliday=function(){var o=function(p){var r=p.messages[0].message;
var q=j.success;
j.addMessage(r,q);
l.setHolidayOperation("manageHoliday");
j.unMaskLoading()
};
var n=function(){var q=" Holiday could not be removed. Try again.";
var p=j.failure;
j.addMessage(q,p);
j.unMaskLoading()
};
var m={primaryKey:l.holidayForRemove.id};
if(l.removeFlage){j.maskLoading();
c.removeHoliday(m,o,n);
l.holidayForRemove={};
l.removeFlage=false
}};
l.removeOkPress=function(){$("#removePopupId").modal("hide");
j.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove();
l.removeFlage=true;
l.removeHoliday()
};
l.removeCancelPress=function(){$("#removePopupId").modal("hide");
j.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove();
l.removeFlage=false;
l.holidayForRemove={}
};
l.retrievFullDayName=function(r,o,n,p){if(r!==null){var s=new Date(r);
var q=new Array(7);
q[0]="Sunday";
q[1]="Monday";
q[2]="Tuesday";
q[3]="Wednesday";
q[4]="Thursday";
q[5]="Friday";
q[6]="Saturday";
var m=q[s.getDay()];
return m
}else{return"NA"
}};
l.combineStartAndToDate=function(n,s){if(n!==null&&s!==null){var r=new Date(n);
var p=new Date(s);
var o=r.getDate();
o=(o<10)?"0"+o:o;
var q=r.getMonth()+1;
q=(q<10)?"0"+q:q;
if(r<p){var m=p.getDate();
m=(m<10)?"0"+m:m;
var t=p.getMonth()+1;
t=(t<10)?"0"+t:t;
return o+"/"+q+"/"+r.getFullYear()+" To "+m+"/"+t+"/"+p.getFullYear()
}else{return o+"/"+q+"/"+r.getFullYear()
}}else{return"NA"
}};
j.unMaskLoading()
}])
});