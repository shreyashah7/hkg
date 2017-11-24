define(["hkg","taskService","messageService","addMasterValue","dynamicForm"],function(a){a.register.controller("ManageTasks",["$rootScope","$scope","Task","Messaging","$filter","$timeout","DynamicFormService",function(h,j,e,g,f,b,i){h.maskLoading();
j.entity="TASKS.";
h.mainMenu="manageLink";
h.childMenu="manageTasks";
h.activateMenu();
j.searchTaskRecords=[];
j.addTaskData={};
j.addTaskData=i.resetSection(j.genralTaskTemplate);
j.dbType={};
j.categoryresult=[];
j.availableTaskStatus=[];
j.isCategoryEmpty=false;
j.categoryName="";
j.listOfModelsOfDateType=[];
j.listOfModelsOfDateType1=[];
var c=i.retrieveSectionWiseCustomFieldInfo("manageTasks");
c.then(function(k){j.customTaskTemplateData=angular.copy(k.genralSection);
j.genralTaskTemplate=h.getCustomDataInSequence(j.customTaskTemplateData);
if(j.genralTaskTemplate!==null&&j.genralTaskTemplate!==undefined){angular.forEach(j.genralTaskTemplate,function(l){if(l.type!==null&&l.type!==undefined&&l.type==="date"){j.listOfModelsOfDateType.push(l.model)
}})
}j.customCategoryTemplateData=angular.copy(k.category);
j.categoryTemplate=h.getCustomDataInSequence(j.customCategoryTemplateData);
if(j.categoryTemplate!==null&&j.categoryTemplate!==undefined){angular.forEach(j.categoryTemplate,function(l){if(l.type!==null&&l.type!==undefined&&l.type==="date"){j.listOfModelsOfDateType1.push(l.model)
}})
}},function(k){console.log("Failed: "+k)
},function(k){console.log("Got notification: "+k)
});
j.addCategoryData=i.resetSection(j.categoryTemplate);
j.dbTypeForCategory={};
j.initForm=function(k){j.addTaskForm=k
};
j.setTaskOperation=function(k){j.displayTaskFlag="view";
j.addCategoryData=i.resetSection(j.categoryTemplate);
if(k!=="search"){$("#scrollable-dropdown-menu.typeahead").typeahead("val","")
}if(k==="addTask"){j.task=undefined;
j.initAddTask();
j.editFlag=false
}else{if(k==="editTask"){j.editFlag=true;
j.initAddTask()
}else{j.editFlag=false
}}j.taskOperation=k
};
j.$watch("task.endRepeatMode",function(){if(j.task&&j.task.endRepeatMode){if(j.task.endRepeatMode==="OD"){j.endRepeat.afterDaysUnits=undefined;
j.endRepeat.afterRepititionsUnits=undefined
}else{if(j.task.endRepeatMode==="AD"){j.task.endDate=undefined;
j.endRepeat.afterRepititionsUnits=undefined
}else{if(j.task.endRepeatMode==="AR"){j.task.endDate=undefined;
j.endRepeat.afterDaysUnits=undefined
}}}}});
j.taskForYouDtOptions={aoColumnDefs:[{bSortable:false,aTargets:[1]}]};
j.taskFromYouDtOptions={aoColumnDefs:[{bSortable:false,aTargets:[1]}]};
j.taskRepeatitionDtOptions={order:[[0,"desc"]]};
j.openSearchedTask=function(k){e.retrieveTasksById({primaryKey:k},function(l){j.searchedTask=l;
if(j.searchedTask.assignedById===h.session.id){j.openEditTask(k)
}else{j.showTaskDetails(j.searchedTask)
}})
};
j.showTaskDetails=function(k){e.attendTask(k.taskRecipientDetailDataBeanList[0].id,function(l){k.taskRecipientDetailDataBeanList[0].taskEdited=undefined
},function(){});
j.selectedTask=k;
j.displayTaskDetails=true;
j.changeTaskCategoryFlag=false;
e.retrieveTasksById({primaryKey:k.id},function(l){h.unMaskLoading();
j.addTaskData=l.taskCustom;
if(!!j.addTaskData){angular.forEach(j.listOfModelsOfDateType,function(m){if(j.addTaskData.hasOwnProperty(m)){if(j.addTaskData[m]!==null&&j.addTaskData[m]!==undefined){j.addTaskData[m]=new Date(j.addTaskData[m])
}else{j.addTaskData[m]=""
}}})
}},function(){h.unMaskLoading()
});
j.displayTaskDetails=true;
j.changeTaskCategoryFlag=false
};
j.hideTaskDetails=function(){j.displayTaskDetails=false;
j.changeTaskCategoryFlag=false;
j.selectedTaskForYouList=[];
j.selectedTaskByYouList=[];
j.retrieveTasks()
};
j.setTaskCategoryFlag=function(k){j.changeTaskCategoryFlag=k
};
j.getCategoryNameById=function(l){for(var k=0;
k<j.taskCategories.length;
k++){if(j.taskCategories[k].id===l){return j.taskCategories[k].text
}}};
j.fillExistingTaskList=function(){j.taskList=[{id:"S",displayName:h.translateValue("TASKS.Smart List:"),children:[{id:"2",displayName:h.translateValue("TASKS.Recent tasks"),selected:"selected"},{id:"3",displayName:h.translateValue("TASKS.All tasks")},{id:"4",displayName:h.translateValue("TASKS.Due today")},{id:"5",displayName:h.translateValue("TASKS.Completed")},{id:"6",displayName:h.translateValue("TASKS.Received")},{id:"7",displayName:h.translateValue("TASKS.Assigned")}]}];
if(j.taskCategories.length>0){j.taskList.push({id:"C",displayName:h.translateValue("TASKS.Categories:"),children:j.taskCategories})
}if(j.taskAssigners.length>0){j.taskList.push({id:"P",displayName:h.translateValue("TASKS.People:"),children:j.taskAssigners})
}if(j.selectedSmartList){j.selectedSmartList.currentNode=j.taskList[0].children[0]
}};
j.fillExistingTaskListAfterCancel=function(k){j.taskList=[{id:"S",displayName:h.translateValue("TASKS.Smart List:"),children:[{id:"2",displayName:h.translateValue("TASKS.Recent tasks"),selected:"selected"},{id:"3",displayName:h.translateValue("TASKS.All tasks")},{id:"4",displayName:h.translateValue("TASKS.Due today")},{id:"5",displayName:h.translateValue("TASKS.Completed")},{id:"6",displayName:h.translateValue("TASKS.Received")},{id:"7",displayName:h.translateValue("TASKS.Assigned")}]}];
if(j.taskCategories.length>0){j.taskList.push({id:"C",displayName:h.translateValue("TASKS.Categories:"),children:j.taskCategories})
}if(j.taskAssigners.length>0){j.taskList.push({id:"P",displayName:h.translateValue("TASKS.People:"),children:j.taskAssigners})
}j.taskList[0].children[0].selected=undefined;
if(k!==undefined){if(k.type!=="C"){angular.forEach(j.taskList[0].children,function(l){if(l.id===k.id){l.selected="selected";
j.selectedSmartList.currentNode=l
}})
}else{j.selectedSmartList.currentNode=j.taskList[0].children[0];
j.setTask()
}}else{j.selectedSmartList.currentNode=j.taskList[0].children[0];
j.setTask()
}};
j.retrieveTaskCategories=function(l,k){h.maskLoading();
if(j.taskCategories===undefined){j.taskCategories=[]
}else{j.taskCategories.splice(0,j.taskCategories.length)
}e.retrieveCategories(function(m){h.unMaskLoading();
j.categoryresult=[];
if(m!=null&&angular.isDefined(m)&&m.length>0){angular.forEach(m,function(n){j.categoryresult.push(n)
})
}j.taskCategories=[];
angular.forEach(m,function(n){j.taskCategories.push({id:n.id,text:n.displayName,displayName:n.displayName+"("+n.categoryCount+")",categoryCustomData:null,type:"C"})
});
if(l){if(k){j.fillExistingTaskListAfterCancel(j.selectedSmartList.currentNode)
}else{j.fillExistingTaskList()
}}h.unMaskLoading()
},function(){h.unMaskLoading()
})
};
j.retrieveTaskAssignerNames=function(k){if(j.taskAssigners===undefined){j.taskAssigners=[]
}else{j.taskAssigners.splice(0,j.taskAssigners.length)
}h.maskLoading();
e.retrieveTaskAssignerNames(function(l){h.unMaskLoading();
angular.forEach(l,function(m){m.label=h.translateValue("TASKS."+m.label);
j.taskAssigners.push({id:m.value,text:m.label,displayName:m.label,type:"P"})
});
if(k){j.fillExistingTaskListAfterCancel(j.selectedSmartList.currentNode)
}else{j.fillExistingTaskList()
}h.unMaskLoading()
},function(){h.unMaskLoading()
})
};
j.retrieveTasks=function(){j.taskResponse=false;
h.maskLoading();
if(j.taskFilter!==undefined){e.retrieveAllTasks(j.taskFilter,function(k){h.unMaskLoading();
j.taskResponse=true;
if(k.tasksForUser!==""){j.allTaskForYouList=[];
if(k.tasksForUser!=null&&angular.isDefined(k.tasksForUser)&&k.tasksForUser.length>0){angular.forEach(k.tasksForUser,function(l){l.taskName=h.translateValue("TSK_NM."+l.taskName);
j.allTaskForYouList.push(l)
})
}else{j.allTaskForYouList=[]
}if(k.tasksByUser!==""){j.allTaskFromYouList=[];
if(k.tasksByUser!=null&&angular.isDefined(k.tasksByUser)&&k.tasksByUser.length>0){angular.forEach(k.tasksByUser,function(n){n.taskName=h.translateValue("TSK_NM."+n.taskName);
if(n.recipientNames!==null){var l=n.recipientNames.split(",");
var o=d(l);
if(o.length>5){for(var m=0;
m<5;
m++){if(m===0){n.recipientNames=o[m]
}else{n.recipientNames+=", "+o[m]
}}n.recipientNames+=" ..."+(o.length-5)+"more"
}else{n.recipientNames=o.toString()
}}j.allTaskFromYouList.push(n)
})
}}}else{j.allTaskFromYouList=[]
}if(j.addTaskForm!==undefined){j.addTaskForm.$dirty=false
}},function(){h.unMaskLoading()
})
}};
j.recipientNameSplitMethod=function(l){var k=l.split(",");
if(k.length<=1){return true
}else{return false
}};
function d(m){var p={},k=[];
for(var o=0,n=m.length;
o<n;
++o){if(!p.hasOwnProperty(m[o])){p[m[o]]=true;
k.push(m[o])
}}return k
}j.retrieveTasksByCategory=function(){h.maskLoading();
j.taskResponse=false;
j.allTaskForYouList=[];
j.allTaskFromYouList=[];
e.retrieveTasksByCategory(j.category.id,function(k){h.unMaskLoading();
j.taskResponse=true;
if(k.tasksForUser!==""){if(k.tasksForUser!=null&&angular.isDefined(k.tasksForUser)&&k.tasksForUser.length>0){angular.forEach(k.tasksForUser,function(l){l.taskName=h.translateValue("TSK_NM."+l.taskName);
j.allTaskForYouList.push(l)
})
}}else{j.allTaskForYouList=[]
}if(k.tasksByUser!==""){if(k.tasksByUser!=null&&angular.isDefined(k.tasksByUser)&&k.tasksByUser.length>0){angular.forEach(k.tasksByUser,function(l){l.taskName=h.translateValue("TSK_NM."+l.taskName);
j.allTaskFromYouList.push(l)
})
}}else{j.allTaskFromYouList=[]
}},function(){h.unMaskLoading()
})
};
j.retrieveTasksByAssigner=function(){h.maskLoading();
j.taskResponse=false;
j.allTaskForYouList=[];
j.assignerId=j.selectedSmartList.currentNode.id;
e.retrieveTasksByAssigner(j.assignerId,function(k){h.unMaskLoading();
j.taskResponse=true;
if(k!=null&&angular.isDefined(k)&&k.length>0){angular.forEach(k,function(l){l.taskName=h.translateValue("TSK_NM."+l.taskName);
j.allTaskForYouList.push(l)
})
}j.allTaskFromYouList=[]
},function(){})
};
j.setTask=function(){j.setTaskOperation("manageTask");
j.initSelectedTaskList();
j.displayTaskDetails=false;
j.changeTaskCategoryFlag=false;
j.editCategoryFlag=false;
j.displayTaskRepitition=false;
if(j.selectedSmartList.currentNode.type===undefined){j.smartListSelection=j.selectedSmartList.currentNode.id;
if(j.smartListSelection==="S"){j.selectedSmartList.currentNode.selected=undefined;
j.selectedSmartList.currentNode=j.taskList[0].children[0];
j.selectedSmartList.currentNode.selected="selected";
j.setTask()
}else{if(j.smartListSelection==="C"){j.selectedSmartList.currentNode.selected=undefined;
j.selectedSmartList.currentNode=j.taskCategories[0];
j.selectedSmartList.currentNode.selected="selected";
j.setTask()
}else{if(j.smartListSelection==="P"){j.selectedSmartList.currentNode.selected=undefined;
j.selectedSmartList.currentNode=j.taskAssigners[0];
j.selectedSmartList.currentNode.selected="selected";
j.setTask()
}else{if(j.smartListSelection==="5"){j.taskForYouHeader="Tasks completed by you";
j.taskFromYouHeader="Tasks completed for you";
j.emptyTaskListMsg="No completed tasks";
j.taskFilter="completed"
}else{if(j.smartListSelection==="4"){j.taskForYouHeader="Due today tasks for you";
j.taskFromYouHeader="Due today tasks from you";
j.emptyTaskListMsg="No due tasks";
j.taskFilter="duetoday"
}else{if(j.smartListSelection==="2"){j.taskForYouHeader="Recent tasks for you";
j.taskFromYouHeader="Recent tasks from you";
j.emptyTaskListMsg="No recent tasks";
j.taskFilter="recent"
}else{if(j.smartListSelection==="3"){j.taskForYouHeader="All tasks for you";
j.taskFromYouHeader="All tasks from you";
j.emptyTaskListMsg="No tasks found";
j.taskFilter="all"
}else{if(j.smartListSelection==="6"){j.taskForYouHeader="Tasks for you";
j.taskFromYouHeader="";
j.emptyTaskListMsg="No tasks found";
j.taskFilter="received"
}else{if(j.smartListSelection==="7"){j.taskForYouHeader="";
j.taskFromYouHeader="Tasks from you";
j.emptyTaskListMsg="No tasks found";
j.taskFilter="assigned"
}}}}}}}}}j.retrieveTasks()
}else{if(j.selectedSmartList.currentNode.type==="C"){j.smartListSelection=undefined;
j.editCategoryFlag=true;
j.taskForYouHeader="Tasks for you";
j.taskFromYouHeader="Tasks from you";
j.emptyTaskListMsg="No associated tasks";
if(j.manageTaskForm.categoryName!==undefined){j.manageTaskForm.categoryName.$setValidity("exists",true)
}for(var k=0;
k<j.categoryresult.length;
k++){if(j.categoryresult[k].id===j.selectedSmartList.currentNode.id){e.addCustomDataToCategoryDataBean(j.categoryresult[k],function(l){j.categoryresult[k].categoryCustomData=l.categoryCustomData;
j.selectedSmartList.currentNode.categoryCustomData=j.categoryresult[k].categoryCustomData;
j.category=angular.copy(j.selectedSmartList.currentNode);
j.addCategoryData=j.category.categoryCustomData;
if(!!j.addCategoryData){angular.forEach(j.listOfModelsOfDateType1,function(m){if(j.addCategoryData.hasOwnProperty(m)){if(j.addCategoryData[m]!==null&&j.addCategoryData[m]!==undefined){j.addCategoryData[m]=new Date(j.addCategoryData[m])
}else{j.addCategoryData[m]=""
}}})
}if(angular.isUndefined(j.category.status)){j.category.status="Active"
}j.retrieveTasksByCategory()
},function(){});
break
}}}else{if(j.selectedSmartList.currentNode.type==="P"){j.smartListSelection=undefined;
j.taskForYouHeader="Tasks for you";
j.retrieveTasksByAssigner()
}}}};
j.initSelectedTaskList=function(){j.selectedCategoryLabel="Change category";
j.selectedTaskForYouList=[];
j.selectedTaskByYouList=[]
};
j.initManageTask=function(){j.hideTaskDetails();
j.taskList=[];
j.initSelectedTaskList();
j.selectedTask={};
j.categoryViewForm={};
j.changeTaskCategoryFlag=false;
j.setTaskOperation("manageTask");
j.retrieveTaskCategories(true,true);
j.retrieveTaskAssignerNames(true);
j.taskFilter="recent";
j.taskForYouHeader="Recent tasks for you";
j.taskFromYouHeader="Recent tasks from you";
j.emptyTaskListMsg="No recent tasks";
j.editCategoryFlag=false;
j.retrieveTasks();
j.addTaskData=i.resetSection(j.genralTaskTemplate);
j.addCategoryData=i.resetSection(j.categoryTemplate);
$.fn.dataTable.ext.search=[];
$.fn.dataTable.ext.search.push(function(l,m,k){if(j.searchTask!==undefined&&j.searchTask!==""&&m[3]!==undefined){if(m[3].match(new RegExp(j.searchTask,"i"))){return true
}}else{return true
}})
};
j.cancelUpdatePage=function(){j.hideTaskDetails();
j.taskList=[];
j.initSelectedTaskList();
j.selectedTask={};
j.categoryViewForm={};
j.changeTaskCategoryFlag=false;
j.setTaskOperation("manageTask");
j.retrieveTaskCategories(true,true);
j.retrieveTaskAssignerNames(true);
j.editCategoryFlag=false;
j.retrieveTasks();
j.addTaskData=i.resetSection(j.genralTaskTemplate);
j.addCategoryData=i.resetSection(j.categoryTemplate);
$.fn.dataTable.ext.search=[];
$.fn.dataTable.ext.search.push(function(l,m,k){if(j.searchTask!==undefined&&j.searchTask!==""&&m[3]!==undefined){if(m[3].match(new RegExp(j.searchTask,"i"))){return true
}}else{return true
}})
};
j.initManageTask();
j.completeTask=function(){h.maskLoading();
e.completeTask(j.selectedTask.taskRecipientDetailDataBeanList[0].id,function(k){h.unMaskLoading();
j.retrieveTaskCategories(true,true);
j.retrieveTasks();
j.hideTaskDetails()
},function(){h.unMaskLoading()
})
};
j.removetTaskFromList=function(l,m,k){if(k==="Completed"||k==="Cancelled"){j.taskResponse=false;
h.maskLoading();
e.removeTaskOfUserFromList(m,function(n){j.allTaskForYouList.splice(l,1);
j.taskResponse=true;
h.unMaskLoading()
},function(){h.unMaskLoading()
})
}};
j.editTask=function(k){h.maskLoading();
j.displayTaskFlag="view";
e.retrieveTasksById({primaryKey:k.id},function(l){h.unMaskLoading();
j.task=l;
j.task.dueDate=new Date(j.task.dueDate);
if(j.task.monthlyOnDay===null){j.task.monthlyOnDay=1
}if(j.task.endDate!==null&&j.task.endDate!==undefined){j.task.endDate=new Date(j.task.endDate)
}else{j.task.endDate=null
}j.task.repeatTask=k.repeatTask.toString();
j.minDueDate=new Date(j.task.dueDate);
j.setEditTaskEndRepeatMinDate();
j.setTaskOperation("editTask");
j.setEditTaskModelValues();
j.recipients=l.taskRecipientDataBeanList;
$(document).find($("#recipients")).select2("val",[])
},function(){h.unMaskLoading()
})
};
j.getProfilePictureOfUser=function(k){return h.apipath+"employee/getprofilepicture/"+k+"?decache="+h.randomCount
};
j.setEditTaskModelValues=function(){j.addTaskData=j.task.taskCustom;
if(!!j.addTaskData){angular.forEach(j.listOfModelsOfDateType,function(m){if(j.addTaskData.hasOwnProperty(m)){if(j.addTaskData[m]!==null&&j.addTaskData[m]!==undefined){j.addTaskData[m]=new Date(j.addTaskData[m])
}else{j.addTaskData[m]=""
}}})
}if(j.task.endRepeatMode==="AD"){j.endRepeat.afterDaysUnits=j.task.afterUnits
}else{if(j.task.endRepeatMode==="AR"){j.endRepeat.afterRepititionsUnits=j.task.afterUnits
}}if(j.task.status==="Due"){if(j.task.repeatTask==="false"){j.task.tempStatus="Due";
j.availableTaskStatusTemp=[];
j.availableTaskStatusTemp={name:"Due",id:"Due"};
j.availableTaskStatusTemp.name=h.translateValue("TASKS."+j.availableTaskStatusTemp.name);
j.availableTaskStatus.unshift(j.availableTaskStatusTemp)
}else{j.task.tempStatus="Active";
j.availableTaskStatusTemp=[];
j.availableTaskStatusTemp={name:"Active",id:"Active"};
j.availableTaskStatusTemp.name=h.translateValue("TASKS."+j.availableTaskStatusTemp.name);
j.availableTaskStatus.unshift(j.availableTaskStatusTemp)
}}if(j.task.status==="Completed"){if(j.task.repeatTask==="false"){j.task.tempStatus="Completed";
j.availableTaskStatusTemp=[];
j.availableTaskStatusTemp={name:"Completed",id:"Completed"};
j.availableTaskStatusTemp.name=h.translateValue("TASKS."+j.availableTaskStatusTemp.name);
j.availableTaskStatus.unshift(j.availableTaskStatusTemp)
}else{j.task.tempStatus="Active";
j.availableTaskStatusTemp=[];
j.availableTaskStatusTemp={name:"Active",id:"Active"};
j.availableTaskStatusTemp.name=h.translateValue("TASKS."+j.availableTaskStatusTemp.name);
j.availableTaskStatus.unshift(j.availableTaskStatusTemp)
}}if(j.task.status==="Cancelled"){if(j.task.repeatTask==="false"){j.task.tempStatus="Cancelled";
j.availableTaskStatusTemp=[];
j.availableTaskStatusTemp={name:"Cancel",id:"Cancelled"};
j.availableTaskStatusTemp.name=h.translateValue("TASKS."+j.availableTaskStatusTemp.name);
j.availableTaskStatus.unshift(j.availableTaskStatusTemp)
}else{j.task.tempStatus="Active";
j.availableTaskStatusTemp=[];
j.availableTaskStatusTemp={name:"Active",id:"Active"};
j.availableTaskStatusTemp.name=h.translateValue("TASKS."+j.availableTaskStatusTemp.name);
j.availableTaskStatus.unshift(j.availableTaskStatusTemp)
}}if(j.task.repeatativeMode==="W"){var l=j.task.weeklyOnDays.split("|");
var k=0;
angular.forEach(j.weekList,function(n,m){if(n.value===l[k]){n.isChecked="true";
k++
}})
}};
j.findTaskCategory=function(l){for(var k=0;
k<j.taskCategories.length;
k++){if(j.taskCategories[k].id===l){return j.taskCategories[k]
}}};
j.setSelectedTaskList=function(k,l){if(k.selected){j.addTaskToList(k,l)
}else{j.removeTaskFromList(k,l)
}};
j.addTaskToList=function(l,k){k.push(l)
};
j.removeTaskFromList=function(m,l){var k=l.indexOf(m);
l.splice(k,1)
};
j.changeTaskCategoryFromTaskDetails=function(k,l){j.selectedTaskForYouList=[k];
j.changeTaskCategory(l)
};
j.checkDueDateFunct=function(k){if(k!==undefined&&j.editFlag){if(k>=h.getCurrentServerDate()){return true
}else{return false
}}else{return true
}};
j.changeTaskCategory=function(k){j.selectedCategoryLabel=k.text;
j.taskRecipeintDetailListToUpdate=[];
j.taskListToUpdate=[];
j.categoryIds=[k.id];
j.receivedTaskIds=[];
j.createdTaskIds=[];
angular.forEach(j.selectedTaskForYouList,function(l,m){if(l.assignedById===h.session.id){j.createdTaskIds.push(l.id)
}j.receivedTaskIds.push(l.taskRecipientDetailDataBeanList[0].id)
});
angular.forEach(j.selectedTaskByYouList,function(l,m){angular.forEach(l.taskRecipientDetailDataBeanList,function(n){if(n.userId===h.session.id){j.receivedTaskIds.push(l.taskRecipientDetailDataBeanList[0].id)
}});
j.createdTaskIds.push(l.id)
});
j.taskIdAndCategoryJSON={categoryId:j.categoryIds,receivedTasksIds:j.receivedTaskIds,createdTasksIds:j.createdTaskIds};
h.maskLoading();
e.updateTaskCategories(j.taskIdAndCategoryJSON,function(l){angular.forEach(j.selectedTaskForYouList,function(m,n){m.taskRecipientDetailDataBeanList[0].recipientCategory=j.categoryIds[0];
if(m.assignedById===h.session.id){m.taskCategory=j.categoryIds[0]
}m.selected=false
});
angular.forEach(j.selectedTaskByYouList,function(m,n){m.taskCategory=j.categoryIds[0];
angular.forEach(m.taskRecipientDetailDataBeanList,function(o){if(o.userId===h.session.id){o.recipientCategory=j.categoryIds[0]
}});
m.selected=false
});
j.initSelectedTaskList();
j.retrieveTaskCategories(true,true);
j.retrieveTasks();
j.changeTaskCategoryFlag=false;
j.selectedCategoryLabel="Change category";
h.unMaskLoading()
},function(){j.taskResponse=true;
j.initSelectedTaskList();
h.addMessage("Cannot update task categories.Try again.",1);
h.unMaskLoading()
})
};
j.calculateCompleted=function(l){var k=0;
angular.forEach(l,function(m){if(m.status==="Completed"||m.status==="Completed Archived"){k++
}});
return(k*100/l.length).toFixed(2)
};
j.compareFloat=function(k,l){if(parseFloat(k)===parseFloat(l)){return true
}else{return false
}};
j.getDateWithoutTimeStamp=function(k){if(k!==undefined){k.setHours(0);
k.setMinutes(0);
k.setSeconds(0);
k.setMilliseconds(0);
return k.getTime()
}return null
};
j.checkTaskRepititions=function(k){j.displayCompletionPopup=false;
j.displayTaskDetails=false;
j.displayTaskRepitition=true;
j.repeatitionResponse=false;
j.taskCompletionResponse=false;
j.selectedTaskForCompletionStatus=k;
if(!angular.equals({},j.selectedTaskForCompletionStatus.repititionTaskRecipeintDetailMap)){j.repeatitionResponse=true
}j.recipientsObjById={};
h.maskLoading();
e.retrieveTaskRecipients(k.id,function(m){h.unMaskLoading();
j.employeeList=[];
j.departmentList=[];
j.roleList=[];
angular.forEach(m,function(n){j.recipientsObjById[n.recipientInstance]=n.recipientValue;
if(n.recipientType==="E"){j.employeeList.push(n.recipientValue)
}else{if(n.recipientType==="R"){j.roleList.push(n.recipientValue)
}else{if(n.recipientType==="D"){j.departmentList.push(n.recipientValue)
}}}});
j.recipientsToDisplay="";
if(j.departmentList.length>0){if(j.recipientsToDisplay===""){j.recipientsToDisplay+="Department: "
}else{j.recipientsToDisplay+=", Department: "
}var l=0;
angular.forEach(j.departmentList,function(n){if(l===0){j.recipientsToDisplay+=n
}else{j.recipientsToDisplay+=", "+n
}l++
})
}if(j.roleList.length>0){if(j.recipientsToDisplay===""){j.recipientsToDisplay+="Role: "
}else{j.recipientsToDisplay+=", Role: "
}var l=0;
angular.forEach(j.roleList,function(n){if(l===0){j.recipientsToDisplay+=n
}else{j.recipientsToDisplay+=", "+n
}l++
})
}if(j.employeeList.length>0){if(j.recipientsToDisplay===""){j.recipientsToDisplay+="Employee: "
}else{j.recipientsToDisplay+=", Employee: "
}var l=0;
angular.forEach(j.employeeList,function(n){if(l===0){j.recipientsToDisplay+=n
}else{j.recipientsToDisplay+=", "+n
}l++
})
}j.taskCompletionResponse=true
},function(){h.unMaskLoading()
})
};
j.hideTaskRepitition=function(){j.displayTaskRepitition=false
};
j.showCompletionStatusPopup=function(k){j.selectedRepitition=k;
angular.forEach(j.selectedRepitition,function(l){var m=l.userName.split("-");
if(m[1]!==undefined&&m[1]!==null){l.name=m[1].trim()
}});
j.displayCompletionPopup=true;
$("#taskCompletedStatus").modal("show")
};
j.hideTaskCompleteStatusPopup=function(){j.displayCompletionPopup=false;
$("#taskCompletedStatus").modal("hide");
h.removeModalOpenCssAfterModalHide()
};
j.addDaysToDate=function(l){var k=new Date(j.getDateWithoutTimeStamp(h.getCurrentServerDate()));
k.setDate(k.getDate()+l);
return k.getTime()
};
j.editTaskCategory=function(k){k.categoryName.$setValidity("exists",true);
if(k.$valid){j.saveEditedCategory(k)
}};
j.saveEditedCategory=function(k){j.categoryToSave=angular.copy(j.category);
j.categoryToSave.displayName=j.categoryToSave.text;
j.categoryToSave.type=undefined;
j.categoryToSave.selected=undefined;
j.categoryToSave.text=undefined;
j.isCategoryEmpty=true;
j.categoryToSave.categoryCustomData=j.addCategoryData;
j.categoryToSave.dbTypeForCategory=j.dbTypeForCategory;
if((j.allTaskForYouList.length!==0||j.allTaskFromYouList.length!==0)&&j.categoryToSave.status==="Remove"){j.showRemoveCategoryPopup(j.categoryToSave.displayName,false)
}else{if(j.categoryToSave.status==="Remove"){j.categoryName=j.categoryToSave.displayName;
$("#removeCategoryPopup").modal("show")
}else{h.maskLoading();
e.editTaskCategory(j.categoryToSave,function(l){h.unMaskLoading();
if(l.messages!==undefined){j.categoryExistsMsg=l.messages[0].message;
k.categoryName.$setValidity("exists",false)
}j.initManageTask()
},function(){h.unMaskLoading();
h.addMessage("Category could not be updated. Try again.",1)
})
}}};
j.removeCategory=function(k){j.categoryToSave=angular.copy(j.category);
j.categoryToSave.displayName=j.categoryToSave.text;
j.categoryToSave.type=undefined;
j.categoryToSave.selected=undefined;
j.categoryToSave.text=undefined;
j.isCategoryEmpty=true;
j.categoryToSave.categoryCustomData=j.addCategoryData;
j.categoryToSave.dbTypeForCategory=j.dbTypeForCategory;
h.maskLoading();
e.editTaskCategory(j.categoryToSave,function(l){h.unMaskLoading();
if(l.messages!==undefined){j.categoryExistsMsg=l.messages[0].message;
k.categoryName.$setValidity("exists",false)
}j.initManageTask()
},function(){h.unMaskLoading();
h.addMessage("Category could not be updated. Try again.",1)
})
};
j.formatDate=function(k){if(j.getDateWithoutTimeStamp(new Date(k))===j.addDaysToDate(0)){return"Today"
}else{if(j.getDateWithoutTimeStamp(new Date(k))===j.addDaysToDate(-1)){return"Yesterday"
}else{if(j.getDateWithoutTimeStamp(new Date(k))===j.addDaysToDate(1)){return"Tomorrow"
}else{return f("date")(k,h.dateFormat)
}}}};
j.accessFlag=h.canAccess("tasksAssignToAll");
j.initRecipients=function(){j.autoCompleteRecipient={multiple:true,closeOnSelect:false,placeholder:"Select recipients",initSelection:function(k,m){if(j.editFlag&&j.accessFlag){var l=[];
angular.forEach(j.recipients,function(p){if(p.recipientType==="E"){var n=p.recipientValue.split("-");
n[1]=h.translateValue("EMP_NM."+n[1]);
var o=n[0]+"-"+n[1];
p.recipientValue=o
}else{if(p.recipientType==="D"){var n=p.recipientValue;
n=h.translateValue("DPT_NM."+n);
var o=n;
p.recipientValue=o
}else{if(p.recipientType==="R"){var n=p.recipientValue;
n=h.translateValue("DESIG_NM."+n);
var o=n;
p.recipientValue=o
}}}l.push({id:p.recipientInstance+":"+p.recipientType,text:p.recipientValue})
});
m(l)
}else{if(!j.accessFlag){var l=[];
l.push({id:h.session.id+":E",text:h.session.userCode+"-"+h.session.firstName+" "+h.session.lastName});
m(l)
}}},formatResult:function(k){return k.text
},formatSelection:function(o){var k=o.id.split(":");
var m=k[1];
if(m==="E"){var l=o.text.split("-");
l[1]=h.translateValue("EMP_NM."+l[1].trim());
var n=l[0].trim()+" - "+l[1];
o.text=n
}else{if(m==="D"){var l=o.text;
l=h.translateValue("DPT_NM."+l);
var n=l;
o.text=n
}else{if(m==="R"){var l=o.text;
l=h.translateValue("DESIG_NM."+l);
var n=l;
o.text=n
}}}return o.text
},query:function(n){var m=n.term;
j.names=[];
var o=function(p){if(p.length!==0){j.names=p;
angular.forEach(p,function(q){j.names.push({id:q.value+":"+q.description,text:q.label})
})
}n.callback({results:j.names})
};
var k=function(){};
if(m.substring(0,2)==="@E"||m.substring(0,2)==="@e"){var l=n.term.slice(2);
e.retrieveUsers(l.trim(),o,k)
}else{if(m.substring(0,2)==="@R"||m.substring(0,2)==="@r"){var l=n.term.slice(2);
g.retrieveRoleList(l.trim(),o,k)
}else{if(m.substring(0,2)==="@D"||m.substring(0,2)==="@d"){var l=n.term.slice(2);
g.retrieveDepartmentList(l.trim(),o,k)
}else{if(m.length>0){var l=m;
e.retrieveUsers(l.trim(),o,k)
}else{n.callback({results:j.names})
}}}}}}
};
j.initAddTaskForm=function(){j.addTaskData=i.resetSection(j.genralTaskTemplate);
j.taskName="";
j.disableWeeklyChecbox=[];
j.editCategoryFlag=false;
if(j.task===undefined){j.task={};
j.task.taskCategory="";
j.task.repeatTask="false";
j.task.taskRecipients=""
}j.endRepeat={};
if(j.task.repeatTask==="false"){j.task.repeatativeMode="D";
j.task.endRepeatMode="OD";
j.task.monthlyOnDay=1
}j.initRecipients();
j.selectedSmartList.currentNode.selected=undefined;
j.weekList=[{code:"M",value:"2"},{code:"Tu",value:"3"},{code:"W",value:"4"},{code:"Th",value:"5"},{code:"F",value:"6"},{code:"Sa",value:"7"},{code:"Su",value:"1"}];
j.categoryForm={};
j.submitted=false
};
j.initAddTask=function(){j.initAddTaskForm();
j.availableTaskStatusTemp=[];
j.availableTaskStatus=[];
j.availableTaskStatusTemp=[{name:"Remove",id:"Remove"}];
if(j.availableTaskStatusTemp!=null&&angular.isDefined(j.availableTaskStatusTemp)&&j.availableTaskStatusTemp.length>0){angular.forEach(j.availableTaskStatusTemp,function(l){l.name=h.translateValue("TASKS."+l.name);
j.availableTaskStatus.push(l)
})
}j.dayList=new Array();
for(var k=0;
k<31;
k++){j.dayList.push(k+1)
}j.popover="<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td> \n '@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr> <tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr> <tr><td>'@R'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Designations</td></tr> </table> ";
j.retrieveTaskSuggestions()
};
j.retrieveTaskSuggestions=function(){h.maskLoading();
e.retrieveTaskSuggestions(function(k){h.unMaskLoading();
j.taskSuggestions=k
},function(){h.unMaskLoading()
})
};
j.addTask=function(k){j.submitted=true;
if(j.task.repeatativeMode==="W"){j.countWeek=0;
angular.forEach(j.weekList,function(m,l){if(m.isChecked){j.countWeek++
}});
if(j.countWeek===0){j.noDaysSelected=true
}}if(k.$valid&&((j.task.repeatativeMode==="W"&&j.countWeek!==0)||j.task.repeatativeMode!=="W")){j.taskToSave=angular.copy(j.task);
j.taskToSave.taskCustom=j.addTaskData;
j.taskToSave.dbType=j.dbType;
if(!j.editFlag){j.setModelValues();
h.maskLoading();
e.createTask(j.taskToSave,function(l){h.unMaskLoading();
j.resetAddTaskForm(k);
j.initManageTask();
j.setTask()
},function(){h.unMaskLoading();
h.addMessage("Could not save details, please try again.",1)
})
}else{if(j.taskToSave.tempStatus==="Remove"){j.taskToSave.status="Remove";
j.taskToSave.tempStatus=undefined;
j.showDeleteTaskPopup(j.taskToSave.taskName)
}else{j.taskToSave.tempStatus=undefined;
j.setModelValues();
j.updateTask();
j.addTaskData=i.resetSection(j.genralTaskTemplate)
}}}};
j.showDeleteTaskPopup=function(k){j.taskName=j.taskToSave.taskName;
$("#deleteTaskPopup").modal("show")
};
j.cancelTask=function(){$("#deleteTaskPopup").modal("hide");
$(".modal-backdrop").remove();
h.removeModalOpenCssAfterModalHide();
j.setModelValues();
j.updateTask()
};
j.removeTaskCancel=function(){$("#deleteTaskPopup").modal("hide");
h.removeModalOpenCssAfterModalHide()
};
j.updateTask=function(){h.maskLoading();
e.updateTask(j.taskToSave,function(k){h.unMaskLoading();
j.initManageTask();
j.setTask()
},function(){h.unMaskLoading();
h.addMessage("Task could not be updated. Try again.",1)
})
};
j.removeTask=function(){e.removeTask(j.taskToSave.id,function(k){$("#deleteTaskPopup").modal("hide");
$(".modal-backdrop").remove();
h.removeModalOpenCssAfterModalHide();
j.initManageTask()
},function(){h.addMessage("Task could not be removed. Try again.",1);
$("#deleteTaskPopup").modal("hide");
$(".modal-backdrop").remove();
h.removeModalOpenCssAfterModalHide()
})
};
j.setModelValues=function(){if(j.editFlag){j.taskToSave.taskRecipients=$("#recipients").select2("val").toString().split(",")
}else{if(j.task.taskRecipients!==undefined&&j.task.taskRecipients.length>0){if(!j.accessFlag){j.taskToSave.taskRecipients=j.task.taskRecipients.id
}else{j.taskToSave.taskRecipients=j.task.taskRecipients.split(",")
}}}if(j.taskToSave.repeatativeMode==="W"){j.taskToSave.weeklyOnDays="";
angular.forEach(j.weekList,function(l,k){if(l.isChecked){j.taskToSave.weeklyOnDays+=l.value;
j.taskToSave.weeklyOnDays+="|"
}});
j.taskToSave.weeklyOnDays=j.taskToSave.weeklyOnDays.substring(0,j.taskToSave.weeklyOnDays.length-1)
}if(j.taskToSave.repeatTask==="true"){if(j.taskToSave.endRepeatMode==="AD"){j.taskToSave.afterUnits=j.endRepeat.afterDaysUnits
}else{if(j.taskToSave.endRepeatMode==="AR"){j.taskToSave.afterUnits=j.endRepeat.afterRepititionsUnits
}}}};
j.getSelectedWeekCount=function(){var k=0;
angular.forEach(j.weekList,function(m,l){j.disableWeeklyChecbox[l]=false;
if(m.isChecked){j.noDaysSelected=false;
k++
}});
return k
};
j.resetAddTaskForm=function(k){$("#recipients").select2("data",undefined);
$("#recipients").select2("val",undefined);
j.task=undefined;
j.taskToSave=undefined;
j.initAddTaskForm();
k.$setPristine()
};
j.createTaskCategory=function(k){if(!j.viewManageCategory){j.categoryForm.submitted=true
}else{j.categoryViewForm.submitted=true
}if(k.$valid){h.maskLoading();
j.category.categoryCustomData=j.addCategoryData;
j.category.dbTypeForCategory=j.dbTypeForCategory;
e.createTaskCategory(j.category,function(l){j.viewManageCategory=false;
h.unMaskLoading();
if(l.messages!==undefined){j.categoryExistsMsg=l.messages[0].message;
k.categoryName.$setValidity("exists",false)
}else{if(!j.viewManageCategory){j.hideAddCategoryPopup(k)
}else{j.hideCategoryPopupFromView(k)
}j.retrieveTaskCategories(true,true)
}},function(){h.unMaskLoading();
h.addMessage("Could not save details, please try again.",1)
})
}};
j.minDueDate=h.getCurrentServerDate();
j.setEditTaskMinDate=function(){j.minDueDate=h.getCurrentServerDate()
};
j.setEditTaskEndRepeatMinDate=function(){if(!!j.task&&j.task.dueDate!==undefined){j.minEndRepeatDate=j.task.dueDate
}else{j.minEndRepeatDate=h.getCurrentServerDate()
}};
j.setEditTaskEndRepeatMinDate();
j.datePicker={};
j.open=function(k,l){k.preventDefault();
k.stopPropagation();
j.datePicker[l]=true
};
j.dateOptions={"year-format":"'yy'","starting-day":1};
j.format=h.dateFormat;
j.showAddCategoryPopup=function(k){j.category={};
j.categoryForm.submitted=false;
k.categoryName.$setValidity("exists",true);
$("#addCategoryPopup").modal("show")
};
j.showCategoryPopupFromView=function(k){j.category={};
j.categoryViewForm.submitted=false;
j.viewManageCategory=true;
k.categoryName.$setValidity("exists",true);
$("#addCategoryPopup").modal("show")
};
j.hideAddCategoryPopup=function(k){j.category={};
j.addCategoryData=i.resetSection(j.categoryTemplate);
k.$setPristine();
$("#addCategoryPopup").modal("hide");
$(".modal-backdrop").remove();
h.removeModalOpenCssAfterModalHide()
};
j.hideCategoryPopupFromView=function(k){j.category={};
j.viewManageCategory=false;
j.addCategoryData=i.resetSection(j.categoryTemplate);
k.$setPristine();
$("#addCategoryPopup").modal("hide");
h.removeModalOpenCssAfterModalHide()
};
j.getSearchedTask=function(l){var k=$("#scrollable-dropdown-menu.typeahead").typeahead("val");
if(k.length>0){if(k.length<3){j.searchTaskRecords=[];
j.setViewFlag("search")
}else{j.searchTaskRecords=angular.copy(l);
j.setViewFlag("search")
}}};
j.setViewFlag=function(k){if(k!=="search"){$("#scrollable-dropdown-menu.typeahead").typeahead("val","")
}j.displayTaskFlag=k
};
j.openEditTask=function(k){h.maskLoading();
e.retrieveTasksById({primaryKey:k},function(l){j.displayTaskFlag="view";
h.unMaskLoading();
j.task=l;
j.task.dueDate=new Date(l.dueDate);
if(j.task.monthlyOnDay===null){j.task.monthlyOnDay=1
}j.task.repeatTask=l.repeatTask.toString();
j.minDueDate=new Date(j.task.dueDate);
j.setEditTaskEndRepeatMinDate();
j.setTaskOperation("editTask");
j.setEditTaskModelValues();
j.recipients=l.taskRecipientDataBeanList;
$(document).find($("#recipients")).select2("val",[])
},function(){h.unMaskLoading()
})
};
j.cancelRepeatition=function(k,l){if(!j.compareFloat(j.calculateCompleted(l),"100.00")&&l[0].status!=="Cancelled"){j.selectedRepeatitionKey=k;
j.repeatitionMap={taskId:l[0].task,repetitionCount:l[0].repetitionCount};
$("#cancelTaskRepatitionPopup").modal("show")
}};
j.cancelRepeatitionOk=function(){j.repeatitionResponse=false;
h.maskLoading();
e.cancelRepeatedTask(j.repeatitionMap,function(k){delete j.selectedTaskForCompletionStatus.repititionTaskRecipeintDetailMap[j.selectedRepeatitionKey];
j.repeatitionResponse=true;
j.checkTaskRepititions(j.selectedTaskForCompletionStatus);
h.unMaskLoading();
j.cancelRepeatitionCancel()
},function(){h.unMaskLoading();
h.addMessage("Task repeatation cannot be cancelled. Try again.",1)
})
};
j.cancelRepeatitionCancel=function(){$("#cancelTaskRepatitionPopup").modal("hide");
h.removeModalOpenCssAfterModalHide()
};
j.showRemoveCategoryPopup=function(l,k){j.isCategoryEmpty=k;
j.categoryName=j.categoryToSave.displayName;
$("#removeCategoryPopup").modal("show")
};
j.removeCategoryCancelPress=function(){j.isCategoryEmpty=false;
$("#removeCategoryPopup").modal("hide");
$(".modal-backdrop").remove();
h.removeModalOpenCssAfterModalHide()
};
j.removeCategoryOkPress=function(){j.isCategoryEmpty=false;
$("#removeCategoryPopup").modal("hide");
h.removeModalOpenCssAfterModalHide()
};
j.removeCategoryRemovePress=function(k){j.isCategoryEmpty=false;
$("#removeCategoryPopup").modal("hide");
h.removeModalOpenCssAfterModalHide();
j.removeCategory(k)
};
h.unMaskLoading()
}])
});