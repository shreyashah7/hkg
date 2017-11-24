define(["hkg","messageService","dynamicForm"],function(b,a){b.register.controller("MessageController",["$rootScope","$scope","$filter","DynamicFormService","Messaging",function(c,e,i,d,f){c.maskLoading();
e.searchRecords=[];
c.mainMenu="manageLink";
c.childMenu="manageMessages";
c.activateMenu();
e.entity="MESSAGING.";
e.message={};
e.searchFlag=false;
e.message.messageType="";
e.submitted=false;
e.isViewPage=false;
e.totalItems=0;
e.selectedTreeMessage={};
e.searchtext="";
e.allChecked=false;
e.treeMessages=[];
e.checkedCount=0;
e.filteredmessages=[];
e.selectedType="";
e.displayMessageDetails=false;
e.addMessageData=d.resetSection(e.generalMessageTemplate);
e.temp={};
e.popover="<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td> \n '@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr> <tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr> <tr><td>'@R'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Roles</td></tr> </table> ";
e.prioritytooltip="Set as priority";
e.hasprioritytooltip="Has priority";
$("#recipients").select2("data",undefined);
$("#recipients").select2("val",undefined);
e.messageTreeListOption=[];
e.$on("$viewContentLoaded",function(){c.maskLoading();
e.messageTreeListOption.push({id:1,displayName:"Inbox",children:null,parentId:null,parentName:null});
e.messageTreeListOption.push({id:2,displayName:"Priority",children:null,parentId:null,parentName:null});
e.messageTreeListOption.push({id:3,displayName:"Sent",children:null,parentId:null,parentName:null});
e.retrieveMessageInitializationDetails();
h();
if(localStorage.getItem("rootMessage")!==null&&localStorage.getItem("rootMessage")!==undefined){var j=localStorage.getItem("rootMessage");
j=JSON.parse(j);
e.retrieveMessageDetailsById(j);
e.selectedMsgType="Inbox";
localStorage.removeItem("rootMessage")
}else{e.displayMessageDetails=false
}c.unMaskLoading()
});
e.isAccessSendMessage=false;
function h(){e.isAccessSendMessage=c.canAccess("messageSend")
}e.retrieveMessageInitializationDetails=function(){e.messageTypes=[];
e.users=[];
e.roles=[];
e.departments=[];
e.groups=[];
e.activities=[];
e.messages=[]
};
c.$on("rootMessage",function(l,k){var j=k.rootMessage;
e.retrieveMessageDetailsById(j);
localStorage.removeItem("rootMessage")
});
e.dbType={};
var g=d.retrieveSectionWiseCustomFieldInfo("manageMessages");
g.then(function(j){e.customGeneralMessageTemplateData=angular.copy(j.genralSection);
e.generalMessageTemplate=c.getCustomDataInSequence(e.customGeneralMessageTemplateData)
},function(j){console.log("Failed: "+j)
},function(j){console.log("Got notification: "+j)
});
e.autoCompleteRecipient={multiple:true,closeOnSelect:false,placeholder:"Select recipients",initSelection:function(j,l){var k=[];
l(k)
},formatResult:function(j){return j.text
},formatSelection:function(j){return j.text
},query:function(m){var l=m.term;
e.names=[];
var n=function(o){if(o.length==0){m.callback({results:e.names})
}else{e.names=o;
angular.forEach(o,function(p){e.names.push({id:p.value+":"+p.description,text:p.label})
});
m.callback({results:e.names})
}};
var j=function(){};
if(l.substring(0,2)=="@E"||l.substring(0,2)=="@e"){var k=m.term.slice(2);
f.retrieveUserList(k.trim(),n,j)
}else{if(l.substring(0,2)=="@R"||l.substring(0,2)=="@r"){var k=m.term.slice(2);
f.retrieveRoleList(k.trim(),n,j)
}else{if(l.substring(0,2)=="@D"||l.substring(0,2)=="@d"){var k=m.term.slice(2);
f.retrieveDepartmentList(k.trim(),n,j)
}else{if(l.length>0){var k=l;
f.retrieveUserList(k.trim(),n,j)
}else{m.callback({results:e.names})
}}}}}};
e.recipientValid=false;
e.$watch("message.nameRecipient",function(){e.recipientValid=false;
if(e.message.nameRecipient!==undefined){if(typeof(e.message.nameRecipient)==="string"){e.recipientValid=true
}}});
e.retrieveMessageTemplates=function(){if(e.message.messageBody!=undefined){var k={message:e.message.messageBody};
var l=function(m){e.messages=m;
e.searchMessages(false)
};
var j=function(){var n="Failed to retrieve message templates. Try again.";
var m=c.failure;
c.addMessage(n,m)
};
f.retrieveMessages(k,l,j)
}};
e.archiveMessage=function(l,j){var m=function(){e.message={};
e.message.messageType="";
e.messages.splice(j,1);
e.selectedIndex=undefined;
e.clear();
e.searchMessages(false)
};
var k=function(){var o="Failed to archive message. Try again.";
var n=c.failure;
c.addMessage(o,n)
};
f.archiveTemplate(l.messageObj,m,k)
};
e.saveMessageDetail=function(){c.maskLoading();
var k=function(l){e.submitted=false;
e.clear();
c.unMaskLoading()
};
var j=function(){var m="Failed to save message. Try again.";
var l=c.failure;
c.unMaskLoading();
c.addMessage(m,l)
};
e.message.messageCustom=e.addMessageData;
e.message.messageDbType=e.dbType;
f.saveMessage(e.message,k,j)
};
e.save={submit:function(j){e.submitted=true;
if(j.$valid){e.selectedIndex=undefined;
if(e.message.nameRecipient!==null&&e.message.nameRecipient.length>0){e.saveMessageDetail()
}else{e.message.nameRecipient=""
}}}};
e.message.hasPriority=false;
e.setpriority=function(){if(e.message.hasPriority===false){e.message.hasPriority=true
}else{e.message.hasPriority=false
}};
e.changeStatus=function(){if(e.message.messageType==undefined){e.message.messageType=""
}};
e.setSelectedMessage=function(k,j){e.selectedIndex=j;
e.setSelectedMessageSearch(k)
};
e.setSelectBlank=function(){e.selectedIndex=undefined;
e.clear()
};
e.setZero=function(){e.totalItems=0;
e.filteredmessages=[]
};
e.clear=function(){e.searchFlag=false;
e.totalItems=0;
e.filteredmessages=[];
e.selectedType="";
if(e.selectedTreeMessage!==undefined&&e.selectedTreeMessage.currentNode!==undefined){e.selectedTreeMessage.selected=undefined;
e.selectedTreeMessage.currentNode.selected=undefined
}e.searchtext="";
$("#recipients").select2("data","");
e.message={};
e.message.messageType="";
e.message.nameRecipient="";
if(e.messaging!=undefined){e.messaging.$setPristine()
}e.submitted=false;
e.selectedIndex=undefined;
e.isViewPage=false;
e.checkedCount=0;
e.temp={};
e.addMessageData=d.resetSection(e.generalMessageTemplate)
};
e.resetSelected=function(){if(e.selectedTreeMessage.currentNode!==undefined){e.selectedTreeMessage.selected=undefined;
e.selectedTreeMessage.currentNode.selected=undefined
}};
e.searchMessages=function(j){e.filteredmessages=[];
e.pageSize=10;
e.maxSize=5;
e.totalItems=e.messages.length;
e.selectedIndex=undefined;
if(e.messages!==undefined){e.filteredmessages=i("filter")(e.messages);
$.each(e.filteredmessages,function(m,n){var k=n.nameRecipient.split(",");
var l="";
$.each(k,function(o,p){l=l+p+"\n"
});
n.nameRecipient=l
});
e.totalItems=e.filteredmessages.length;
e.noOfPages=e.filteredmessages.length/e.pageSize
}};
e.openViewPage=function(j){if(angular.isDefined(j)){if(e.messaging!=undefined){e.messaging.$setPristine()
}e.selectedType=j.displayName+" messages";
e.selectedMsgType=j.displayName;
e.isViewPage=true;
e.searchFlag=false;
e.searchtext="";
e.treeMessages=[];
e.temp={};
e.itemsPerPage=5;
e.currentPage=0;
f.retrieveTotalMessagesLength(e.selectedMsgType,function(k){e.total=k
});
e.retrieveMessagesForTree()
}};
e.retrieveMessagesForTree=function(){c.maskLoading();
e.displayMessageDetails=false;
var k=function(l){angular.forEach(l,function(n){if(e.selectedMsgType=="Sent"){var m=n.nameRecipient.split("\n");
if(m.length==1){}else{n.fullname=angular.copy(m.toString());
n.nameRecipient=m[0]
}}else{n.nameRecipient=n.createdBy
}});
e.treeMessages=l;
c.unMaskLoading()
};
var j=function(){var m="Failed to retrieve messages. Try again.";
var l=c.failure;
c.addMessage(m,l);
c.unMaskLoading()
};
f.retrieveMessagesByType(e.selectedMsgType,k,j)
};
e.onSelectCheckbox=function(j){if(j.isChecked){e.checkedCount++
}else{e.checkedCount--;
e.temp.allChecked=false
}};
e.onSelectRow=function(j){j.isChecked=!j.isChecked;
e.onSelectCheckbox(j)
};
e.onSelectCheckboxAll=function(){if(e.temp.allChecked){e.checkedCount=e.treeMessages.length;
angular.forEach(e.treeMessages,function(j){j.isChecked=true
})
}else{e.checkedCount=0;
angular.forEach(e.treeMessages,function(j){j.isChecked=false
})
}};
e.archieveMessages=function(){c.maskLoading();
var k=[];
angular.forEach(e.treeMessages,function(n){if(n.isChecked){var m={};
m.messageObj=n.messageObj;
m.id=n.id;
m.messageType=n.messageType;
k.push(m)
}});
var l=function(){e.retrieveMessagesForTree();
c.unMaskLoading()
};
var j=function(){var n="Failed to archive message. Try again.";
var m=c.failure;
c.addMessage(n,m);
c.unMaskLoading()
};
f.archiveMessages(k,l,j)
};
e.formatDate=function(j){if(e.getDateWithoutTimeStamp(new Date(j))===e.addDaysToDate(0)){return new Date(j).toLocaleTimeString()
}else{if(e.getDateWithoutTimeStamp(new Date(j))===e.addDaysToDate(-1)){return"Yesterday"
}else{if(e.getDateWithoutTimeStamp(new Date(j))===e.addDaysToDate(1)){return"Tomorrow"
}else{return i("date")(j,c.dateFormat)
}}}};
e.getDateWithoutTimeStamp=function(j){j.setHours(0);
j.setMinutes(0);
j.setSeconds(0);
j.setMilliseconds(0);
return j.getTime()
};
e.addDaysToDate=function(k){var j=new Date(e.getDateWithoutTimeStamp(new Date()));
j.setDate(j.getDate()+k);
return j.getTime()
};
e.callsearch=function(){if(e.searchtext!=undefined){var k={message:e.searchtext};
var l=function(m){e.messages=m;
e.searchMessages(true);
if(e.totalItems.length>0){e.isInValidSearch=false
}else{e.isInValidSearch=true
}};
var j=function(){var n="Failed to retrieve message templates. Try again.";
var m=c.failure;
c.addMessage(n,m)
};
f.retrieveMessages(k,l,j)
}};
e.setSelectedMessageSearch=function(k){e.clear();
$("#recipients").select2("data","");
e.names=[];
if(k.nameRecipientIds){var l=k.nameRecipientIds.split(",");
var j=k.nameRecipient.split("\n");
$.each(l,function(m,n){$.each(j,function(p,o){if(m==p){e.names.push({id:n,text:o})
}})
});
$("#recipients").select2("data",e.names)
}e.message=angular.copy(k);
if(!!k.messageCustom){e.addMessageData=k.messageCustom
}else{e.addMessageData=d.resetSection(e.generalMessageTemplate)
}e.message.nameRecipient=k.nameRecipientIds;
e.messages=null;
e.totalItems=0;
e.isViewPage=false
};
e.getSearchedMessage=function(k){var j=$("#scrollable-dropdown-menu.typeahead").typeahead("val");
if(j.length>0){if(j.length<3){e.searchRecords=[]
}else{e.resetSelected();
e.searchRecords=angular.copy(k);
angular.forEach(e.searchRecords,function(m){var l=m.nameRecipient.split("\n");
if(l.length==1){}else{m.fullname=angular.copy(l.toString());
m.nameRecipient=l[0]
}})
}e.searchFlag=true
}};
e.setMessageForEdit=function(j){f.retrieve({primaryKey:j},function(k){e.setSelectedMessageSearch(k);
e.isViewPage=false;
e.searchFlag=false
},function(){c.addMessage("Could not retrive Message , please try again.",1)
})
};
e.retrieveUnreadMessages=function(){e.unreadMessages=[];
var j=function(k){e.unreadMessages=k;
if(angular.isDefined(e.unreadMessages)&&e.unreadMessages.length>0){$("#showmsgmodal").modal({keyboard:false,show:true})
}};
f.retrieveUnreadMessages(j)
};
e.retrieveMessageDetailsById=function(j){e.addMessageData=j.messageCustom;
e.selectedMessage=angular.copy(j);
e.displayMessageDetails=true
};
e.markClosedMsg=function(k){var l=function(){e.displayMessageDetails=false;
e.retrieveMessagesForTree();
e.selectedMessage={};
e.addMessageData=d.resetSection(e.generalMessageTemplate)
};
var j=function(){};
if(k!==undefined&&k.isAttended===false){f.markAsClosed(k,l,j)
}else{e.displayMessageDetails=false;
e.selectedMessage={};
e.addMessageData=d.resetSection(e.generalMessageTemplate);
e.retrieveMessagesForTree()
}if(e.selectedTreeMessage!==undefined&&e.selectedTreeMessage.currentNode!==undefined){}else{e.selectedMsgType="Inbox";
e.selectedTreeMessage.currentNode={};
e.selectedTreeMessage.currentNode.selected=e.messageTreeListOption[0];
e.openViewPage(e.messageTreeListOption[0])
}};
e.checkDate=function(k){var j=e.addDaysToDate(-2);
if(k>=j){return false
}else{return true
}};
c.unMaskLoading()
}])
});