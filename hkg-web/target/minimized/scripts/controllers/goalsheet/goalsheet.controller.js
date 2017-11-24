define(["hkg","goalService"],function(a){a.register.controller("ManageGoalSheetController",["$rootScope","$scope","GoalService",function(b,c,d){b.maskLoading();
b.mainMenu="manageLink";
b.childMenu="manageGoalSheet";
b.activateMenu();
c.validate={};
c.verfiyData={};
c.format=b.dateFormat;
c.datePicker={};
c.dateOptions={"year-format":"'yy'","starting-day":1};
c.open=function(e,f){e.preventDefault();
e.stopPropagation();
c.datePicker[f]=true
};
c.minEventFromDate=b.getCurrentServerDate();
c.flag="show_submit";
c.flag1={};
c.userId=-1;
c.flag1.userIdVerify=-1;
c.entity="GOALSHEET.";
c.submitted=false;
c.typeOfUser="myGoalSheet";
c.flag1.typeOfUserVerify="myGoalSheet";
c.initData=function(e){console.log("userIdNew::::"+e);
if(e!==undefined&&e!==null){c.userId=e
}console.log("userIdNew1::::"+c.userId);
if(c.userId!==undefined&&c.userId!==null&&c.userId!==""){b.maskLoading();
d.retrievependinggoalsheet(c.userId,function(f){b.unMaskLoading();
c.goalSheetList=f;
c.goalSheetMap={empty:true};
if(!!c.goalSheetList){angular.forEach(c.goalSheetList,function(g){if(!!g.activityGroup){if(!!!c.goalSheetMap[g.activityGroup+" - "+g.activityNode]){c.goalSheetMap[g.activityGroup+" - "+g.activityNode]=[]
}g.validTarget=false;
g.submitted=false;
delete c.goalSheetMap.empty;
c.goalSheetMap[g.activityGroup+" - "+g.activityNode].push(g)
}if(!!g.department){if(!!!c.goalSheetMap["Department - "+g.department]){c.goalSheetMap["Department - "+g.department]=[]
}g.validTarget=false;
g.submitted=false;
delete c.goalSheetMap.empty;
c.goalSheetMap["Department - "+g.department].push(g)
}if(!!g.designation){if(!!!c.goalSheetMap["Designation - "+g.designation]){c.goalSheetMap["Designation - "+g.designation]=[]
}g.validTarget=false;
g.submitted=false;
delete c.goalSheetMap.empty;
c.goalSheetMap["Designation - "+g.designation].push(g)
}})
}},function(){b.unMaskLoading()
})
}else{c.goalSheetMap={empty:true}
}};
c.validateTarget=function(f){var e=false;
f.submitted=false;
if(f.minTarget!==undefined&&f.minTarget!==null&&f.maxTarget!==undefined&&f.maxTarget!==null){if(f.target>=f.minTarget&&f.target<=f.maxTarget){e=true
}}else{if(f.minTarget!==undefined&&f.minTarget!==null){if(f.target>=f.minTarget){e=true
}}else{if(f.maxTarget!==undefined&&f.maxTarget!==null){if(f.target<=f.maxTarget){e=true
}}}}f.validTarget=e
};
c.submitGoal=function(e,f){f.submitted=true;
if(e.$valid&&(f.validTarget||!!f.department||!!f.designation)){var g=angular.copy(f);
delete g.validTarget;
delete g.submitted;
b.maskLoading();
d.submitGoalSheet(g,function(h){b.unMaskLoading();
b.addMessage("Goal sheet submitted successfully",b.success);
c.initData()
},function(){b.unMaskLoading();
b.addMessage("Failed to submit goal sheet",b.failure)
})
}};
c.verifyGoalSheetClick=function(){c.flag="show_verify";
c.searchedFlag=false;
c.submitted=false;
c.flag1.userIdVerify=-1
};
c.backLink=function(){c.cancelVerify();
c.flag="show_submit";
c.initData()
};
c.cancelVerify=function(){c.verfiyData={};
c.verifyForm.$setPristine();
c.submitted=false;
c.verifyGoalSheetMap={empty:true}
};
c.searchInGoalSheet=function(){c.submitted=true;
if(c.verifyForm.$valid){var h={};
h.userId=c.flag1.userIdVerify;
var i=new Date(c.verfiyData.fromDate);
var g=i.getFullYear()+"-"+(i.getMonth()+1)+"-"+i.getDate();
h.fromDate=g;
var f=new Date(c.verfiyData.toDate);
var e=f.getFullYear()+"-"+(f.getMonth()+1)+"-"+f.getDate();
h.toDate=e;
c.searchedFlag=true;
b.maskLoading();
d.retrievesubmittedgoalsheet(h,function(j){b.unMaskLoading();
c.verifyGoalSheetList=j;
c.verifyGoalSheetMap={empty:true};
if(!!c.verifyGoalSheetList){angular.forEach(c.verifyGoalSheetList,function(k){if(!!k.activityGroup){if(!!!c.verifyGoalSheetMap[k.activityGroup+" - "+k.activityNode]){c.verifyGoalSheetMap[k.activityGroup+" - "+k.activityNode]=[]
}k.validTarget=false;
k.submitted=false;
delete c.verifyGoalSheetMap.empty;
c.verifyGoalSheetMap[k.activityGroup+" - "+k.activityNode].push(k)
}if(!!k.department){if(!!!c.verifyGoalSheetMap["Department - "+k.department]){c.verifyGoalSheetMap["Department - "+k.department]=[]
}k.validTarget=false;
k.submitted=false;
delete c.verifyGoalSheetMap.empty;
c.verifyGoalSheetMap["Department - "+k.department].push(k)
}if(!!k.designation){if(!!!c.verifyGoalSheetMap["Designation - "+k.designation]){c.verifyGoalSheetMap["Designation - "+k.designation]=[]
}k.validTarget=false;
k.submitted=false;
delete c.verifyGoalSheetMap.empty;
c.verifyGoalSheetMap["Designation - "+k.designation].push(k)
}})
}},function(){b.unMaskLoading()
})
}};
c.radioChange=function(){if(c.typeOfUser==="myGoalSheet"){c.userId=-1;
c.initData();
$("#selectUser").select2("val","");
c.flag1.userId=""
}if(c.flag1.typeOfUserVerify==="myGoalSheet"){c.flag1.userIdVerify=-1;
$("#selectUser1").select2("val","")
}};
c.userList=[];
d.retrieveusersforgoalsheet(function(e){angular.forEach(e,function(f){if(f.otherId!==b.session.id){c.userList.push(f)
}})
});
b.unMaskLoading()
}])
});