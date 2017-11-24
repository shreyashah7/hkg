define(["hkg","leaveService","addMasterValue","dynamicForm"],function(a){a.register.controller("ApplyLeave",["$rootScope","$scope","$location","Leave","DynamicFormService","$filter",function(b,d,g,f,c,e){b.mainMenu="manageLink";
b.childMenu="applyLeave";
b.activateMenu();
d.entity="APPLYLEAVE.";
d.leaveOperation="";
d.leaveReasonTypes=[];
d.isEdit=false;
d.dateWarning="";
d.minDate=b.getCurrentServerDate();
d.leave={};
d.leave.fromDate;
d.leaveStartDate="";
d.leaveToDate="";
d.searchedApplyLeaveRecords=[];
d.leave.remarks;
d.listOfModelsOfDateType=[];
f.retrievePendingStock(b.session.id,function(h){d.pendingStockList=[];
d.stockList=[];
if(!!h.PendingStock&&h.PendingStock.length>0){d.pendingStockList=h.PendingStock;
angular.forEach(d.pendingStockList,function(i){i=b.translateValue("APPLYLEAVE."+i);
d.stockList.push({displayName:i})
})
}});
d.setLeaveOperation=function(h,j){var i=c.retrieveSectionWiseCustomFieldInfo("applyLeave");
d.dbType={};
i.then(function(k){d.listOfModelsOfDateType=[];
d.customTemplateDate=angular.copy(k.genralSection);
d.genralLeaveTemplate=b.getCustomDataInSequence(d.customTemplateDate);
if(d.genralLeaveTemplate!==null&&d.genralLeaveTemplate!==undefined){angular.forEach(d.genralLeaveTemplate,function(l){if(l.type!==null&&l.type!==undefined&&l.type==="date"){d.listOfModelsOfDateType.push(l.model)
}})
}},function(k){},function(k){});
d.leaveOperation=h;
if(d.leaveOperation==="addLeave"&&d.isEdit===false){d.leave.remarks="";
$("#scrollable-dropdown-menu.typeahead").typeahead("val","");
d.addLeaveData=c.resetSection(d.genralLeaveTemplate)
}if(d.leaveOperation!=="searchedLeave"){d.searchedApplyLeaveRecords=[];
$("#scrollable-dropdown-menu.typeahead").typeahead("val","")
}if(!!j){j()
}};
d.initApplyLeave=function(){d.setLeaveOperation("applyLeave");
d.retrieveAllLeave();
f.retrieveLeaveReason(function(h){angular.forEach(h.data,function(i){d.leaveReasonTypes.push({id:i.value,text:i.label,displayName:i.label})
})
},function(){b.addMessage("Failed to retrive leave reason",1)
})
};
d.retrieveAllLeave=function(){b.maskLoading();
d.addLeaveData=c.resetSection(d.genralLeaveTemplate);
f.retrieveLeave(function(h){d.records=h.data;
b.unMaskLoading()
},function(){b.addMessage("Failed to retrive leave reason",1)
})
};
d.initApplyLeave();
d.getReasonById=function(i){var h={};
angular.forEach(d.leaveReasonTypes,function(j){if(j.id===i){h=j
}});
return h
};
d.cancelLeave=function(h){d.addLeaveData=c.resetSection(d.genralLeaveTemplate);
d.isEdit=false;
d.leave={};
d.leave.reason="";
h.$setPristine();
d.submitted=false;
d.retrieveAllLeave();
d.setLeaveOperation("applyLeave")
};
d.initAddHolidayForm=function(h){h.$dirty=false
};
d.addLeave=function(l,j){d.submitted=true;
var k=true;
for(var i in l){if(i!=="passwordPopUpForm"&&i!=="editMasterForm"){if(l[i].$invalid){console.log("foem"+l[i]+"and key is"+i);
k=false;
break
}}}if(k){var h={fromDate:getDateFromFormat(j.fromDate,b.dateFormatWithTime),toDate:getDateFromFormat(j.toDate,b.dateFormatWithTime),description:j.description,reasonId:j.reason.id,reason:j.reason.text,edit:true,status:"Pending",requestType:"Leave",leaveCustom:d.addLeaveData,dbType:d.dbType,remarks:j.remarks};
b.maskLoading();
f.addLeave(h,function(m){if(m.messages!==undefined&&m.messages[0].responseCode){b.addMessage(m.messages[0].message,m.messages[0].responseCode);
l.$setPristine();
d.submitted=false;
d.leave.reason.id=j.reason.id;
d.leave.reason=d.getReasonById(j.reason.id)
}else{d.retrieveAllLeave();
d.leave={};
d.leave.reason="";
l.$setPristine();
d.submitted=false;
d.setLeaveOperation("applyLeave")
}b.pingurl=b.centerapipath+"common/getsession";
b.$broadcast("event:loginConfirmed",b.pingurl);
b.unMaskLoading()
},function(){b.addMessage("Could not save leave, please try again.",1);
b.unMaskLoading()
})
}};
d.updateLeave=function(i,h,l){d.submitted=true;
var k=true;
for(var j in l){if(j!=="passwordPopUpForm"&&j!=="editMasterForm"){if(l[j].$invalid){console.log("foem"+l[j]+"and key is"+j);
k=false;
break
}}}if(k){if(d.leave.cancel){d.showRemoveConfirmation(i)
}else{d.leave.reasonId=d.leave.reason.id;
d.leave.reason=" ";
d.leave.dbType=d.dbType;
d.leave.fromDate=getDateFromFormat(d.leave.fromDate,b.dateFormatWithTime);
d.leave.toDate=getDateFromFormat(d.leave.toDate,b.dateFormatWithTime);
d.leave.applyedOn=getDateFromFormat(d.leave.toDate,b.dateFormat);
f.updateLeave(d.leave,function(m){if(m.messages!==undefined&&m.messages[0].responseCode){b.addMessage(m.messages[0].message,m.messages[0].responseCode);
d.leave.reason={id:d.leave.reasonId,text:d.getReasonById(d.leave.reasonId)}
}else{d.retrieveAllLeave();
d.leave={};
d.isEdit=false;
l.$setPristine();
d.submitted=false;
d.setLeaveOperation("applyLeave")
}},function(){b.addMessage("Could not update leave, please try again.",1)
})
}}};
d.setEditLeave=function(h){f.retrieveLeaveByLeaveId(h.id,function(i){d.options=[{name:"Active",id:1},{name:"Remove",id:2}];
d.leaveStatus=d.options[0];
d.isEdit=true;
d.leave=i;
d.addLeaveData=i.leaveCustom;
d.leave.applyedOn=e("date")(h.applyedOn,"dd/MM/yyyy");
d.leave.reason=d.getReasonById(d.leave.reasonId);
d.setLeaveOperation("addLeave",function(){if(!!d.addLeaveData){angular.forEach(d.listOfModelsOfDateType,function(j){if(d.addLeaveData.hasOwnProperty(j)){if(d.addLeaveData[j]!==null&&d.addLeaveData[j]!==undefined){d.addLeaveData[j]=new Date(d.addLeaveData[j])
}else{d.addLeaveData[j]=""
}}})
}})
})
};
d.redirectToEdit=function(i){var h=d.getLeaveById(i);
d.setEditLeave(h)
};
d.redirectToSearchedPage=function(i){var h=$("#scrollable-dropdown-menu.typeahead").typeahead("val");
if(h.length>0){if(h.length<3){d.searchedApplyLeaveRecords=[];
d.setLeaveOperation("searchedLeave")
}else{d.searchedApplyLeaveRecords=angular.copy(i);
d.setLeaveOperation("searchedLeave")
}}};
d.getLeaveById=function(i){var h={};
angular.forEach(d.records,function(j){if(j.id===i){h=j
}});
return h
};
d.removeLeave=function(){var h={primaryKey:d.leave.id};
f.deleteLeave(h,function(i){d.retrieveAllLeave();
d.isEdit=false;
d.leave={};
d.leave.reason="";
d.setLeaveOperation("applyLeave")
},function(){b.addMessage("Could not remove leave, please try again.",1)
})
};
d.archiveLeave=function(h){var i={primaryKey:h.id};
f.archiveLeave(i,function(j){d.retrieveAllLeave()
},function(){b.addMessage("Could not archive leave, please try again.",1)
})
};
d.applyLeave=function(){d.isEdit=false;
d.leave={};
d.leave.reason="";
d.setLeaveOperation("addLeave")
};
d.showRemoveConfirmation=function(h){$(document).find($("#removeShowId")).modal("show");
d.leaveStartDate=h.fromDate;
d.leaveToDate=h.toDate
};
d.removeOk=function(){$(document).find($("#removeShowId")).modal("hide");
b.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove();
d.removeLeave()
};
d.removeCancel=function(){$(document).find($("#removeShowId")).modal("hide");
b.removeModalOpenCssAfterModalHide()
}
}])
});