define(["hkg","leaveService","addMasterValue","dynamicForm"],function(a){a.register.controller("ManageLeave",["$rootScope","$scope","Leave","$filter","DynamicFormService",function(b,d,h,g,c){b.maskLoading();
b.mainMenu="manageLink";
b.childMenu="manageLeave";
b.activateMenu();
d.manageLeaveOperation="";
d.allUserLeaves=[];
d.searchedManageLeaveRecords=[];
d.entity="MANAGELEAVE.";
d.dbType={};
var f=c.retrieveSectionWiseCustomFieldInfo("applyLeave");
d.applyedLeaveData=c.resetSection(d.genralApplyedLeaveTemplate);
d.listOfModelsOfDateType1=[];
f.then(function(i){d.customTemplateDate=angular.copy(i.genralSection);
d.genralApplyedLeaveTemplate=b.getCustomDataInSequence(d.customTemplateDate);
if(d.genralApplyedLeaveTemplate!==null&&d.genralApplyedLeaveTemplate!==undefined){angular.forEach(d.genralApplyedLeaveTemplate,function(j){if(j.type!==null&&j.type!==undefined&&j.type==="date"){d.listOfModelsOfDateType1.push(j.model)
}})
}},function(i){},function(i){});
d.addRespondLeaveData=c.resetSection(d.genralRespondLeaveTemplate);
d.listOfModelsOfDateType=[];
var e=c.retrieveSectionWiseCustomFieldInfo("manageLeave");
e.then(function(i){d.genralRespondLeaveTemplate=i.genralSection;
if(d.genralRespondLeaveTemplate!==null&&d.genralRespondLeaveTemplate!==undefined){angular.forEach(d.genralRespondLeaveTemplate,function(j){if(j.type!==null&&j.type!==undefined&&j.type==="date"){d.listOfModelsOfDateType.push(j.model)
}})
}},function(i){console.log("Faile: "+i)
},function(i){console.log("Got notification: "+i)
});
d.showEmployeeLeavePopup=function(){$("#employeeLeavePopup").modal("show")
};
d.proceedCancel=function(){$("#employeeLeavePopup").modal("hide");
b.removeModalOpenCssAfterModalHide()
};
d.proceedOk=function(){$("#employeeLeavePopup").modal("hide");
b.removeModalOpenCssAfterModalHide()
};
d.redirectToRespondLeave=function(i){h.searchLeaveForRespondById(i,function(j){d.setManageLeaveOperation("respondLeave",j)
},function(){b.addMessage("could not retrive leave",1)
})
};
d.redirectToSearchePage=function(j){var i=$("#scrollable-dropdown-menu.typeahead").typeahead("val");
if(i.length>0){if(i.length<3){d.searchedManageLeaveRecords=[];
d.setManageLeaveOperation("searchedApprover")
}else{d.searchedManageLeaveRecords=angular.copy(j);
d.setManageLeaveOperation("searchedApprover")
}}};
d.initRespondLeave=function(i){console.log("leave::::"+JSON.stringify(i));
h.retrievePendingStock(i.userId,function(j){console.log("Pending stock::"+JSON.stringify(j));
d.pendingStockList=[];
d.stockList=[];
if(!!j.PendingStock&&j.PendingStock.length>0){d.pendingStockList=j.PendingStock;
angular.forEach(d.pendingStockList,function(k){k=b.translateValue("APPLYLEAVE."+k);
d.stockList.push({displayName:k})
})
}});
d.cancelRespondLeave=function(){d.setManageLeaveOperation("manageLeave")
};
d.setLeaveForRespond=function(j){d.leave=j;
h.addCustomDataToLeaveDataBean(d.leave,function(k){d.leave.approverCustom=k.approverCustom;
if(!!d.leave.approverCustom){d.addRespondLeaveData=d.leave.approverCustom
}else{d.addRespondLeaveData={}
}if(!!d.addRespondLeaveData){angular.forEach(d.listOfModelsOfDateType,function(l){if(d.addRespondLeaveData.hasOwnProperty(l)){if(d.addRespondLeaveData[l]!==null&&d.addRespondLeaveData[l]!==undefined){d.addRespondLeaveData[l]=new Date(d.addRespondLeaveData[l])
}else{d.addRespondLeaveData[l]=""
}}})
}d.leave.leaveCustom=k.leaveCustom;
if(!!d.leave.leaveCustom){d.applyedLeaveData=d.leave.leaveCustom
}else{d.applyedLeaveData={}
}if(!!d.applyedLeaveData){angular.forEach(d.listOfModelsOfDateType1,function(l){if(d.applyedLeaveData.hasOwnProperty(l)){if(d.applyedLeaveData[l]!==null&&d.applyedLeaveData[l]!==undefined){d.applyedLeaveData[l]=new Date(d.applyedLeaveData[l])
}else{d.applyedLeaveData[l]=""
}}})
}d.leave.fromDate=g("date")(j.fromDate,"dd/MM/yyyy HH:mm a");
d.leave.toDate=g("date")(j.toDate,"dd/MM/yyyy HH:mm a");
d.leave.applyedOn=g("date")(j.applyedOn,"dd/MM/yyyy")
},function(){})
};
d.approveLeave=function(){var j={id:d.leave.id,remarks:d.leave.remarks,respondCustom:d.addRespondLeaveData,dbType:d.dbType};
h.approveLeave(j,function(k){d.setManageLeaveOperation("manageLeave")
},function(){b.addMessage("Failed to approve leave",1)
})
};
d.disApproveLeave=function(){var j={id:d.leave.id,remarks:d.leave.remarks,respondCustom:d.addRespondLeaveData,dbType:d.dbType};
h.disApproveLeave(j,function(k){d.setManageLeaveOperation("manageLeave")
},function(){b.addMessage("Failed to disapprove leave",1)
})
};
d.cancelApproveLeave=function(j){d.isCancelApprove=false;
h.cancelApproveLeave(j.id,function(k){d.retrieveApprovalByCondition("false");
d.setManageLeaveOperation("manageLeave")
},function(){b.addMessage("Failed to Archiev leave",1)
})
};
d.setLeaveForRespond(i)
};
d.retrieveAllleaveByUserId=function(i){d.leaveResponse=false;
d.empName=i.requestFrom;
h.retriveAllLeaveByUserId(i.userId,function(j){d.leaveResponse=true;
d.allUserLeaves=j;
d.showEmployeeLeavePopup()
},function(){b.addMessage("Failed to retrive leave list",1)
})
};
d.retrieveApprovalByCondition=function(i){if(i===false){i="false"
}else{i="true"
}d.searchedApproverList=[];
b.maskLoading();
h.retrieveAllApproval(i,function(j){d.records=j;
b.unMaskLoading()
},function(){b.addMessage("Failed to retrive approval list",1)
})
};
d.setManageLeaveOperation=function(i,k,j){d.manageLeaveOperation=i;
d.isChecked=false;
if(i==="respondLeave"){d.proceedOk();
h.retrieveApprovalByID(k.id,function(l){console.log("Result:::::"+JSON.stringify(l));
d.initRespondLeave(l)
});
if(j){d.isCancelApprove=true
}}else{if(i==="manageLeave"){d.retrieveApprovalByCondition(d.isChecked);
$("#scrollable-dropdown-menu.typeahead").typeahead("val","")
}}if(i!=="searchedApprover"){d.searchedApproverList=[]
}};
d.sendCommentNotification=function(){var i=[];
i[0]=d.leave.userId;
i[1]=d.leave.remarks;
i[2]=d.leave.id;
i[3]=d.leave.approvalId;
h.sendCommentNotification(i,function(j){d.setManageLeaveOperation("manageLeave")
},function(){b.addMessage("Failed to comment on leave",1)
})
};
b.unMaskLoading()
}])
});