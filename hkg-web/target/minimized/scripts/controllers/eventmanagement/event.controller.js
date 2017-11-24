define(["hkg","eventService","messageService","fileUploadService","colorpicker.directive","addMasterValue","dynamicForm"],function(a){a.register.controller("ManageEvents",["$rootScope","$scope","Event","Messaging","$filter","$compile","$templateCache","$timeout","DynamicFormService","FileUploadService","$location",function(j,n,l,i,h,f,d,b,k,e,g){j.maskLoading();
j.mainMenu="manageLink";
j.childMenu="manageEvents";
j.activateMenu();
n.entity="EVENTS.";
n.ADD_EVENT="addEvent";
n.MANAGE_EVENT="manageEvent";
n.EDIT_EVENT="editEvent";
n.REGISTER_EVENT="registerEvent";
n.VIEW_PHOTO_GALLERY="photoGallery";
n.searchEventRecords=[];
n.attendeesOnstatus=[];
n.eventCategories=[];
n.eventToSaveGallery={};
n.eventForGallery={};
n.attendeeList=[];
var m="";
n.editRegistrationFlag=false;
n.viewMeetingDetailsPopup=false;
n.eventToSaveGallery.fileList=[];
n.availableEventStatus=[];
n.fileNames=[];
n.attendeeStatus=[];
n.exportTypeList=[];
n.attendee={};
n.photos=[];
n.viewPhotos=[];
n.selection=[];
n.eventDetails={};
n.addEventForm={};
n.count=1;
n.addCategoryData={};
n.printTypeList=[];
n.dbTypeForCategory={};
n.accessFlag=j.canAccess("eventsAddEdit");
n.categoryFormSubmitted=false;
n.getSearchedEvents=function(o){n.searchedEventList=o
};
n.checkFranchise=function(p){var o=j.session.companyId;
if(p.franchise===o){return true
}else{return false
}};
n.attendeeStatus=[{id:"AwaitingResponse",label:"Awaiting Response"},{id:"Attending",label:"Attending"},{id:"NonAttending",label:"Non-Attending"}];
n.setDefaultStatus=function(){n.attendee.status=n.attendeeStatus[0].id
};
n.exportTypeList=[{id:"PDF",label:"PDF"},{id:"DOC",label:"DOC"},{id:"Image",label:"Image"}];
n.setDefaultExportType=function(){n.exportType=n.exportTypeList[0].id
};
n.printTypeList=[{id:"Attending",label:"Attending"},{id:"All",label:"All"}];
n.setDefaultPrintType=function(){n.printType=n.printTypeList[1].id
};
n.initForm=function(o){n.addEventForm=o
};
n.setEventOperation=function(o){n.addCategoryData={};
if(o!=="searchedEvent"){$("#scrollable-dropdown-menu.typeahead").typeahead("val","")
}n.editFlag=false;
if(o===n.ADD_EVENT){n.addEventData=k.resetSection(n.generalEventTemplate);
n.addRegFormData=k.resetSection(n.generalRegFormTemplate);
n.addInvitationData=k.resetSection(n.generalInviTemplate);
n.editFlag=false;
n.event=undefined;
n.initAddEvent()
}else{if(o===n.EDIT_EVENT){n.editFlag=true;
n.initAddEvent()
}}n.eventOperation=o
};
n.checkDate=function(q){var o=q.publishedOn;
var p=q.registrationLastDate;
if(q.registrationType!=="OFLN"){if(p!==undefined||p!==null){if((o<=j.getCurrentServerDate().setHours(0,0,0,0))&&(p>=j.getCurrentServerDate().setHours(0,0,0,0))){if(q.enableRegister){return false
}return true
}else{return true
}}}else{return true
}};
n.showEventDetailsPopup=function(p,o){n.eventsId=p.id;
n.attendee.status=o;
if(o==="Attending"){n.statusField="AT"
}else{if(o==="NonAttending"){n.statusField="NAT"
}else{if(o==="AwaitingResponse"){n.statusField=null
}}}n.retrieveUserRegistration(n.eventsId);
n.viewMeetingDetailsPopup=true
};
n.enableCard=function(o){if(o.enableRegister){return true
}else{return false
}};
n.initManageEvent=function(){if(j.getCurrentServerDate()!==undefined){var o=j.getCurrentServerDate();
o.setHours(0);
o.setMinutes(0);
o.setSeconds(0);
o.setMilliseconds(0);
n.todayDate=o.getTime()
}else{var o=new Date();
o.setHours(0);
o.setMinutes(0);
o.setSeconds(0);
o.setMilliseconds(0);
n.todayDate=o.getTime()
}n.editCategory=false;
n.configForm=false;
n.upcomingEventsDtOptions={aoColumnDefs:[{bSortable:false,aTargets:[-1,-2,-3,-7]}]};
n.completedEventsDtOptions={aoColumnDefs:[{bSortable:false,aTargets:[-1,-2,-4]}]};
n.attendeeDtOptions={aoColumnDefs:[{bSortable:false,aTargets:[-1,-2]}]};
n.setEventOperation(n.MANAGE_EVENT);
n.retrieveEvents("null");
n.retriveEventCategories();
n.retrieveActiveEventCategories();
n.addEventData={};
n.preview=true;
n.addRegFormData={};
n.addInvitationData={};
n.dbType={};
n.dbType1={};
n.dbType2={};
var p=k.retrieveSectionWiseCustomFieldInfo("manageEvents");
p.then(function(q){n.customGeneralEventTemplateData=angular.copy(q.genralSection);
n.generalEventTemplate=j.getCustomDataInSequence(n.customGeneralEventTemplateData);
n.customGeneralRegTemplateData=angular.copy(q.registration);
n.generalRegFormTemplate=j.getCustomDataInSequence(n.customGeneralRegTemplateData);
n.customInviTemplateData=angular.copy(q.Invitationcard);
n.generalInviTemplate=j.getCustomDataInSequence(n.customInviTemplateData);
n.customCategoryTemplateData=angular.copy(q.category);
n.categoryTemplate=j.getCustomDataInSequence(n.customCategoryTemplateData)
},function(q){console.log("Failed: "+q)
},function(q){console.log("Got notification: "+q)
})
};
n.getThumbnailPath=function(o){var p=o.substring(0,o.lastIndexOf("."));
p+="_T.jpg";
return p
};
n.searchpopover="<NOBR><font color='red;'>Use the shortcut to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td> \n '@C'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Categories</td></tr> </table> ";
n.initEvnetCategories=function(){n.eventCategoryListDropdown=n.eventCategories;
if(n.eventCategoryListDropdown!==undefined){n.eventCategoryListDropdown.unshift({id:"D",displayName:"Select event category"})
}n.selectedEventCategoryDropdown={};
n.selectedEventCategoryDropdown.currentNode=n.eventCategoryListDropdown[0];
n.selectedEventCategoryPopup={};
n.eventCategoryListPopup=angular.copy(n.eventCategoryListDropdown);
n.eventCategoryListPopup[0].displayName="Select category or leave blank";
n.selectedEventCategoryPopup.currentNode=n.eventCategoryListPopup[0];
n.eventCategoryId="D";
n.popupEventCategoryId="D"
};
n.retrieveEventCategorySuggestions=function(){j.maskLoading();
l.retrieveCategorySuggestions(function(o){j.unMaskLoading();
n.eventCategorySuggestions=o
},function(){j.unMaskLoading()
})
};
n.retriveEventCategories=function(){n.retrieveEventCategorySuggestions();
j.maskLoading();
l.retrieveEventCategories(function(o){j.unMaskLoading();
n.eventCategoriesTemp=[];
n.eventCategories=[];
n.eventCategoriesTemp=o;
if(n.eventCategoriesTemp!=null&&angular.isDefined(n.eventCategoriesTemp)&&n.eventCategoriesTemp.length>0){angular.forEach(n.eventCategoriesTemp,function(p){n.eventCategories.push(p)
})
}n.initEvnetCategories()
},function(){j.unMaskLoading();
n.initEvnetCategories()
})
};
n.retrieveActiveEventCategories=function(){j.maskLoading();
l.retrieveActiveEventCategories(function(o){j.unMaskLoading();
n.existingEventCategoryListTemp=[];
n.existingEventCategoryList=[];
n.existingEventCategoryListTemp=o;
if(n.existingEventCategoryListTemp!=null&&angular.isDefined(n.existingEventCategoryListTemp)&&n.existingEventCategoryListTemp.length>0){angular.forEach(n.existingEventCategoryListTemp,function(p){n.existingEventCategoryList.push(p)
})
}},function(){j.unMaskLoading()
})
};
n.archieveEvent=function(o){j.maskLoading();
n.eventResponse=false;
l.archieveEvent(n.allCompletedEvents[o].id,function(p){n.allCompletedEvents.splice(o,1);
n.eventResponse=true;
j.unMaskLoading()
},function(){j.unMaskLoading()
})
};
n.retrieveEvents=function(o){j.maskLoading();
n.eventResponse=false;
var p={categoryId:o,haveAddEditRights:j.canAccess("eventsAddEdit")};
l.retrieveAllEvents(p,function(q){j.unMaskLoading();
n.eventResponse=true;
n.allUpcomingEvents=[];
n.allCompletedEvents=[];
if(q.upcomingEvents!=null&&angular.isDefined(q.upcomingEvents)&&q.upcomingEvents.length>0){angular.forEach(q.upcomingEvents,function(r){r.eventTitle=j.translateValue("EVNT_TITLE."+r.eventTitle);
n.allUpcomingEvents.push(r)
})
}if(q.completedEvents!=null&&angular.isDefined(q.completedEvents)&&q.completedEvents.length>0){angular.forEach(q.completedEvents,function(r){r.eventTitle=j.translateValue("EVNT_TITLE."+r.eventTitle);
n.allCompletedEvents.push(r)
})
}if(n.allUpcomingEvents.length>0){n.eventDetails={employeeName:n.allUpcomingEvents[0].employeeName,employeeAddress:n.allUpcomingEvents[0].employeeAddress,employeeEmail:n.allUpcomingEvents[0].employeeEmail,employeePhoneNo:n.allUpcomingEvents[0].employeePhoneNo}
}},function(){j.unMaskLoading();
n.eventResponse=true
})
};
n.initManageEvent();
n.bannerImagePopup=function(o){n.selectedBannerImageName=o;
$("#bannerImagePopup").modal("show")
};
n.galleryPopup=function(o){var p=n.eventForGallery.id;
n._Index=o;
n.viewPhotos=[];
if(n.viewPhotos.length===0){l.retrieveImagePaths(p,function(q){var r=q.src;
angular.forEach(r,function(s){n.viewPhotos.push({src:j.appendAuthToken(j.apipath+"event/getimage?file_name="+s)})
});
$("#galleryPopup").modal("show")
})
}else{$("#galleryPopup").modal("show")
}};
n.hideSlidergalleryPopup=function(){n.viewPhotos=[];
n.selection=[];
n._Index=0;
$("#galleryPopup").modal("hide");
j.removeModalOpenCssAfterModalHide()
};
n.hideRegDetialsOfUserCancel=function(){$("#registrationDetailsOfUserPopup").modal("hide");
j.removeModalOpenCssAfterModalHide()
};
n.hideMeetingDetailCancel=function(){n.viewMeetingDetailsPopup=false
};
n.initAddEvent=function(){n.configForm=false;
n.editCategory=false;
n.previewConfigureForm=false;
n.viewMeetingDetailsPopup=false;
n.eventCategoryId="D";
n.regFields={noOfAdults:true,noOfChildren:true,relWithGuest:true,nameOfGuest:true};
if(angular.isDefined(n.eventCategoryListDropdown)){n.selectedEventCategoryDropdown.currentNode=n.eventCategoryListDropdown[0]
}n.initInvitees();
n.retrieveRegistrationFormNames();
if(!n.editFlag){n.event={};
n.event.invitees="";
n.event.registrationType="ONLN";
n.event.selectedRegForm=""
}else{if(n.event.status==="Created"){n.availableEventStatusTemp=[];
n.availableEventStatus=[];
n.availableEventStatusTemp=[{name:"Created"},{name:"Published"},{name:"Cancelled"},{name:"Remove event"}];
if(n.availableEventStatusTemp!=null&&angular.isDefined(n.availableEventStatusTemp)&&n.availableEventStatusTemp.length>0){angular.forEach(n.availableEventStatusTemp,function(o){o.name=j.translateValue("EVENTS."+o.name);
n.availableEventStatus.push(o)
})
}}else{if(n.event.status==="Upcoming"){n.availableEventStatusTemp=[];
n.availableEventStatus=[];
n.availableEventStatusTemp=[{name:"Upcoming"},{name:"Cancelled"}];
if(n.availableEventStatusTemp!=null&&angular.isDefined(n.availableEventStatusTemp)&&n.availableEventStatusTemp.length>0){angular.forEach(n.availableEventStatusTemp,function(o){o.name=j.translateValue("EVENTS."+o.name);
n.availableEventStatus.push(o)
})
}}else{if(n.event.status==="Ongoing"){n.availableEventStatusTemp=[];
n.availableEventStatus=[];
n.availableEventStatusTemp=[{name:"Ongoing"},{name:"Cancelled"}];
if(n.availableEventStatusTemp!=null&&angular.isDefined(n.availableEventStatusTemp)&&n.availableEventStatusTemp.length>0){angular.forEach(n.availableEventStatusTemp,function(o){o.name=j.translateValue("EVENTS."+o.name);
n.availableEventStatus.push(o)
})
}}else{if(n.event.status==="Cancelled"){n.availableEventStatusTemp=[];
n.availableEventStatus=[];
n.availableEventStatusTemp=[{name:"Cancelled"},{name:"Remove event"}];
if(n.availableEventStatusTemp!=null&&angular.isDefined(n.availableEventStatusTemp)&&n.availableEventStatusTemp.length>0){angular.forEach(n.availableEventStatusTemp,function(o){o.name=j.translateValue("EVENTS."+o.name);
n.availableEventStatus.push(o)
})
}}}}}n.eventCategoryId=n.event.categoryDataBean.id;
n.selectedEventCategoryDropdown.currentNode=n.event.categoryDataBean
}n.submitted=false;
n.categoryForm={};
n.bannerImg={};
n.backgroundImg={};
n.popover="<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td> \n '@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr> <tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr> <tr><td>'@F'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Franchise</td></tr> </table> ";
$(document).on("select2-selecting","#copyEventName",function(o){if(o.val!=="0"){n.retrieveRegistrationFields(o.val)
}else{n.event.registrationFieldsDataBean=n.configuredRegistrationFieldDataBean
}});
n.retrieveRegistrationFields=function(o){j.maskLoading();
l.retrieveRegistrationFields(o,function(p){j.unMaskLoading();
n.event.registrationFieldsDataBean=p
},function(){j.unMaskLoading()
})
}
};
n.initInvitees=function(){n.autoCompleteInvitees={multiple:true,closeOnSelect:false,placeholder:"Select invitees",initSelection:function(o,q){if(n.editFlag){var p=[];
angular.forEach(n.recipients,function(r){p.push({id:r.recipientInstance+":"+r.recipientType,text:r.recipientValue})
});
q(p)
}},formatResult:function(o){return o.text
},formatSelection:function(o){return o.text
},query:function(r){var q=r.term;
n.names=[];
var s=function(t){if(t.length!==0){n.names=t;
angular.forEach(t,function(u){n.names.push({id:u.value+":"+u.description,text:u.label})
})
}r.callback({results:n.names})
};
var o=function(){};
if(q.substring(0,2)==="@F"||q.substring(0,2)==="@f"){if(q.substring(2,3)===":"&&(q.substring(3,5)==="@D"||q.substring(3,5)==="@D")){var p=r.term.slice(5);
i.retrieveDepartmentListOfOtherFranchise(p.trim(),s,o)
}}else{if(q.substring(0,2)==="@E"||q.substring(0,2)==="@e"){var p=r.term.slice(2);
i.retrieveUserList(p.trim(),s,o)
}else{if(q.substring(0,2)==="@D"||q.substring(0,2)==="@d"){var p=r.term.slice(2);
i.retrieveDepartmentList(p.trim(),s,o)
}else{if(q.length>0){var p=q;
i.retrieveUserList(p.trim(),s,o)
}else{r.callback({results:n.names})
}}}}}}
};
n.popupEventCategoryDropdownClick=function(o){n.popupEventCategoryId=o.currentNode.id
};
n.eventCategoryDropdownClick=function(p,o){n.eventCategoryId=p.currentNode.id;
if(p!==null){n.addEventForm=o;
n.addEventForm.$dirty=true
}};
n.showAddCategoryPopup=function(o){n.showCategoryModal=true;
n.category={};
o.$setPristine();
n.categoryForm.submitted=false;
o.categoryName.$setValidity("exists",true);
$("#addCategoryPopup").modal("show")
};
n.initPopupParentCategory=function(){n.selectedEventCategoryPopup.currentNode=n.eventCategoryListPopup[0]
};
n.hideAddCategoryPopup=function(o){n.showCategoryModal=false;
n.category={};
n.addCategoryData=k.resetSection(n.categoryTemplate);
o.$setPristine();
$("#addCategoryPopup").modal("hide");
j.removeModalOpenCssAfterModalHide()
};
n.setConfigFormFlag=function(o){n.regformSubmitted=false;
n.field={};
if(angular.isUndefined(n.event.registrationFieldsDataBean)||n.event.registrationFieldsDataBean===null){n.event.registrationFieldsDataBean=[]
}if(o){n.prevRegistrationFormName=angular.copy(n.event.registrationFormName);
n.prevRegistrationFieldsDataBean=angular.copy(n.event.registrationFieldsDataBean);
n.event.selectedEventToCopy=""
}n.configForm=o
};
n.cancelRegForm=function(o){n.event.registrationFormName=n.prevRegistrationFormName;
n.event.registrationFieldsDataBean=n.prevRegistrationFieldsDataBean;
o.$setPristine();
n.setConfigFormFlag(false)
};
n.regFormNames={multiple:false,formatResult:function(o){return o.text
},data:function(){return{results:n.select2FormNames}
}};
n.retrieveRegistrationFormNames=function(){j.maskLoading();
n.select2FormNames=[];
l.retrieveRegistrationFormNames(function(o){o.$promise=undefined;
o.$resolved=undefined;
angular.forEach(o,function(p,q){n.select2FormNames.push({id:q.toString(),text:p})
});
if(n.editFlag){n.event.selectedRegForm=n.event.id
}if(!n.editFlag){if(n.addEventForm!==undefined){n.addEventForm.$dirty=false
}}j.unMaskLoading()
},function(){j.unMaskLoading()
})
};
n.setBackground=function(){if(!n.preview){$(".draggable").draggable({cursor:"move",opacity:0.7,revert:"invalid"});
$("#templateBackground").droppable({drop:function(s,t){var r=$(t.draggable).parent().attr("id");
if(r!="templateBackground"){f($("#templateBackground").append("<div class='draggable' style='display:'inline-block''>"+$(t.draggable).html()+"</div>"))(n);
$(t.draggable).remove();
$(".draggable").draggable({cursor:"move",opacity:0.7,revert:"invalid"})
}}});
var p=document.getElementById("drop");
p.style.display="block";
$("#drop").droppable({hoverClass:"ui-state-active",drop:function(r,s){f($("#drop").append("<div class='draggable' style='display:'inline-block''>"+$(s.draggable).html()+"</div>"))(n);
$(s.draggable).remove();
$(".draggable").draggable({cursor:"move",opacity:0.7,revert:"invalid"})
}})
}else{var p=document.getElementById("drop");
p.style.display="none";
var o=document.getElementById("closebtn");
o.style.display="none";
var q=document.getElementById("footer");
q.style.display="none"
}if($("#imgId").attr("src")!==undefined&&$("#imgId").attr("src")!==""){$(document).find("#templateBackground").css("background","url("+$("#imgId").attr("src")+")");
$(document).find("#templateBackground").css("background-size","700px 400px");
$(document).find("#templateBackground").css("background-repeat","no-repeat")
}n.preview=false
};
n.redirectToSearchedPage=function(q){var p=$("#scrollable-dropdown-menu.typeahead").typeahead("val");
if(p.length>0){if(p.length<3){n.searchEventRecords=[];
n.setEventOperation("searchedEvent")
}else{n.searchEventRecords=angular.copy(q);
n.setEventOperation("searchedEvent");
for(var o=0;
o<n.searchEventRecords.length;
o++){n.startDate=n.searchEventRecords[o].fromDate;
n.startDates=(new Date(n.startDate))
}}}};
n.removeFromPage=function(){n.imageName=null;
n.event.bannerImageName=null
};
n.removeFromInvitationPage=function(){n.invitationImageName=null;
e.removeImageFile(n.event.invitationTemplateName,function(){n.event.invitationTemplateName=undefined
});
$(document).find("#templateBackground").css("background","url('')")
};
n.editEvent=function(o){j.maskLoading();
l.retrieveEventById({primaryKey:o},function(p){j.unMaskLoading();
n.event=p;
if(n.event.fromDate!==null||n.event.fromDate!==undefined){n.event.fromDate=new Date(n.event.fromDate)
}if(n.event.toDate!==null||n.event.toDate){n.event.toDate=new Date(n.event.toDate)
}if(n.event.publishedOn!==null||n.event.publishedOn){n.event.publishedOn=new Date(n.event.publishedOn)
}if(n.event.registrationLastDate!==null&&n.event.registrationLastDate){n.event.registrationLastDate=new Date(n.event.registrationLastDate)
}if(n.event.bannerImageName!==null&&n.event.bannerImageName!==undefined){n.imageName=n.event.bannerImageName.split("~~")[1]
}if(n.event.invitationTemplateName!==null&&n.event.invitationTemplateName!==undefined){n.invitationImageName=n.event.invitationTemplateName.split("~~")[1]
}if(angular.isDefined(p.eventCustom)&&p.eventCustom!=null){n.addEventData=angular.copy(p.eventCustom)
}if(angular.isDefined(p.regCustom)&&p.regCustom!=null){n.addRegFormData=angular.copy(p.regCustom)
}if(angular.isDefined(p.invitationCustom)&&p.invitationCustom!=null){n.addInvitationData=angular.copy(p.invitationCustom)
}n.eventPrevStatus=n.event.status;
n.recipients=p.eventRecipientDataBeanList;
$(document).find($("#invitees")).select2("val",[]);
if(n.event.fromDate!==undefined){n.minEventFromDate=new Date(n.event.fromDate)
}if(n.event.publishedOn!==undefined){n.minEventPublisedDate=new Date(n.event.publishedOn)
}n.setEventOperation(n.EDIT_EVENT)
},function(){j.unMaskLoading()
})
};
n.saveFilesAndEvent=function(o){n.submitted=true;
j.maskLoading();
if(o.$valid&&n.eventCategoryId!=="D"&&!n.invalidTime){n.saveEventFlag=true;
if(angular.isDefined(n.backgroundFlow)&&n.backgroundFlow.files.length>0){n.saveEventFlag=false;
if(n.backgroundUploaded){n.saveEventFlag=true
}}if(angular.isDefined(n.bannerFlow)&&n.bannerFlow.files.length>0){n.saveEventFlag=false;
if(n.bannerUploaded){n.saveEventFlag=true
}}if(n.saveEventFlag){n.saveEvent(o)
}}j.unMaskLoading()
};
n.saveEvent=function(o){if(n.event.registrationType==="ONLN"){if(n.event.selectedRegForm.id===undefined){if(n.event.selectedRegForm!=="0"){n.event.registrationFormEventId=n.event.selectedRegForm
}}else{if(n.event.selectedRegForm.id!=="0"){n.event.registrationFormEventId=n.event.selectedRegForm.id
}}}n.eventToSave=angular.copy(n.event);
n.eventToSave.eventCustom=n.addEventData;
n.eventToSave.eventDbType=n.dbType;
n.eventToSave.regCustom=n.addRegFormData;
n.eventToSave.regDbType=n.dbType1;
n.eventToSave.invitationCustom=n.addInvitationData;
n.eventToSave.invitationDbType=n.dbType2;
n.clearImageOfInvitationTemplate();
if(n.eventToSave.invitationTemplateName===undefined&&!n.previewConfigureForm&&!n.editFlag){n.eventToSave.invitationTemplateName="ND"
}else{if(n.eventToSave.invitationTemplateName===null&&!n.previewConfigureForm&&n.editFlag){n.eventToSave.invitationTemplateName="ND"
}}n.setModelValues();
if(!n.editFlag){n.createEvent(o)
}else{if(n.eventToSave.status==="Published"){n.eventToSave.status="Upcoming";
n.eventToSave.publishedOn=j.getCurrentServerDate().setHours(0,0,0,0)
}if(n.eventToSave.status==="Remove event"){$("#removeEventPopup").modal("show")
}else{if(n.eventToSave.status==="Cancelled"){$("#cancelEventPopup").modal("show")
}else{n.updateEvent(o)
}}}n.preview=true
};
n.createEvent=function(o){j.maskLoading();
n.eventToSave.token=j.authToken;
l.createEvent(n.eventToSave,function(p){o.$setPristine();
n.preview=false;
j.unMaskLoading();
n.initManageEvent()
},function(){j.addMessage("Could not save details, please try again.",1);
n.resetImageOfInvitationTemplate();
j.unMaskLoading()
})
};
n.updateEvent=function(o){j.maskLoading();
n.eventToSave.token=j.authToken;
l.updateEvent(n.eventToSave,function(p){o.$setPristine();
n.preview=false;
j.unMaskLoading();
n.initManageEvent()
},function(){j.addMessage("Event could not be updated. Try again.",1);
n.resetImageOfInvitationTemplate();
j.unMaskLoading()
})
};
n.removeEventOk=function(o){$("#removeEventPopup").modal("hide");
$(".modal-backdrop").remove();
j.removeModalOpenCssAfterModalHide();
j.maskLoading();
l.removeEvent({primaryKey:n.eventToSave.id},function(p){o.$setPristine();
j.unMaskLoading();
n.initManageEvent()
},function(){j.addMessage("Event could not be removed. Try again.",1);
j.unMaskLoading()
})
};
n.cancelEventOk=function(o){$("#cancelEventPopup").modal("hide");
$(".modal-backdrop").remove();
j.removeModalOpenCssAfterModalHide();
j.maskLoading();
n.updateEvent(o)
};
n.removeEventCancel=function(){n.event.status=n.eventPrevStatus;
$("#removeEventPopup").modal("hide");
j.removeModalOpenCssAfterModalHide()
};
n.cancelEventCancel=function(){n.event.status=n.eventPrevStatus;
$("#cancelEventPopup").modal("hide");
j.removeModalOpenCssAfterModalHide()
};
n.clearImageOfInvitationTemplate=function(){n.imgDiv=$("#templateImg").html();
if($("#imgId").attr("src")!==undefined&&$("#imgId").attr("src")!==""){$(document).find("#templateBackground").css("background","");
$("#imgId").attr("src","")
}};
n.resetImageOfInvitationTemplate=function(){$(document).find("#templateImg").html(n.imgDiv)
};
n.setModelValues=function(){n.eventToSave.templateHtml=$("#templateModal").html();
n.eventToSave.category=n.eventCategoryId;
n.eventToSave.eventRecipientDataBeanList=[];
if(n.editFlag){n.eventToSave.invitees=$("#invitees").select2("val").toString()
}if(n.eventToSave.invitees.length>0){var q=n.eventToSave.invitees.split(",");
for(var o=0;
o<q.length;
o++){var p=q[o].split(":");
n.eventToSave.eventRecipientDataBeanList.push({recipientInstance:p[0],recipientType:p[1]})
}}n.eventToSave.selectedRegForm=undefined;
n.eventToSave.selectedEventToCopy=undefined;
n.eventToSave.invitees=undefined
};
n.eventStartTimeChange=function(){};
n.eventEndTimeChange=function(){};
n.checkEventTime=function(){n.invalidTime=false;
var s,q,o;
if(n.event.fromDate){o=new Date(n.event.fromDate)
}else{o=new Date()
}if(o.getTime()<=new Date().getTime()){if(n.event.strtTime){var p=new Date(n.event.strtTime);
if(p.getHours()<new Date().getHours()){n.invalidTime=true
}else{if(p.getHours()===new Date().getHours()&&p.getMinutes()<=new Date().getMinutes()){n.invalidTime=true
}}}if(n.event.endTime){var r=new Date(n.event.endTime);
if(r.getHours()<new Date().getHours()){n.invalidTime=true
}else{if(r.getHours()===new Date().getHours()&&r.getMinutes()<=new Date().getMinutes()){n.invalidTime=true
}else{if(n.event.strtTime){var p=new Date(n.event.strtTime);
if(r.getHours()<p.getHours()){n.invalidTime=true
}else{if(r.getHours()===p.getHours()&&r.getMinutes()<=p.getMinutes()){n.invalidTime=true
}}}}}}}else{if(n.event.strtTime&&n.event.endTime){var p=new Date(n.event.strtTime);
var r=new Date(n.event.endTime);
if(r.getHours()<p.getHours()){n.invalidTime=true
}else{if(r.getHours()===p.getHours()&&r.getMinutes()<=p.getMinutes()){n.invalidTime=true
}}}}};
n.setEventCategory=function(){j.maskLoading();
if(angular.isDefined(n.selectedEventCategory.currentNode)){n.setEventOperation(n.MANAGE_EVENT);
n.editCategory=true;
n.retrieveEvents(n.selectedEventCategory.currentNode.id);
if(angular.isUndefined(n.editEventCategoryDropdown)){n.editEventCategoryDropdown={}
}n.parentEventCategory={};
n.selectedCategory=angular.copy(n.selectedEventCategory.currentNode);
n.parentId=n.selectedCategory.parentId;
n.parentEventCategory.id=n.selectedCategory.parentId;
n.parentCategoryHeader=n.selectedCategory.parentName;
n.childCategoryHeader=n.selectedCategory.displayName;
n.parentEventCategory.displayName=n.selectedCategory.parentName;
n.category={};
n.categoryForm={};
n.category.id=n.selectedCategory.id;
n.category.displayName=n.selectedCategory.displayName;
n.category.status=n.selectedCategory.status;
l.addCustomDataToCategoryDataBean(n.category,function(o){n.category.categoryCustomData=o.categoryCustomData;
n.category=o;
n.addCategoryData=n.category.categoryCustomData;
n.editCategoryEventCategoryListDropdown=angular.copy(n.eventCategories);
n.deleteSelectedNode(n.editCategoryEventCategoryListDropdown);
j.unMaskLoading()
},function(){j.unMaskLoading()
})
}};
n.deleteSelectedNode=function(q){if(q===null||q===undefined){return
}for(var p=0;
p<q.length;
p++){if(n.selectedEventCategory.currentNode.id===q[p].id){q.splice(p,1)
}else{for(var o=0;
o<q.length;
o++){if(q[o].children!==null){n.deleteSelectedNode(q[o].children)
}}}}n.editCategoryEventCategoryListDropdown=angular.copy(q)
};
n.editCategoryDropdownClick=function(){if(angular.isDefined(n.editEventCategoryDropdown.currentNode)){n.parentId=n.editEventCategoryDropdown.currentNode.id;
n.parentEventCategory.displayName=n.editEventCategoryDropdown.currentNode.displayName
}};
n.checkCategoryUniqueName=function(o){l.doesCategoryNameExist(n.category,function(p){if(p.messages===null){o.categoryName.$setValidity("exists",true)
}else{n.categoryExistsMsg=p.messages[0].message;
o.categoryName.$setValidity("exists",false)
}},function(){})
};
n.checkEventUniqueName=function(o){l.doesEventNameExist({id:n.event.id,eventTitle:n.event.eventTitle},function(p){if(p.messages===null){o.eventTitle.$setValidity("exists",true)
}else{n.eventExistsMsg=p.messages[0].message;
o.eventTitle.$setValidity("exists",false)
}},function(){})
};
n.saveEventCategory=function(o){n.categoryForm.submitted=true;
if(o.$valid){j.maskLoading();
var p=0;
n.categoryToSave=angular.copy(n.category);
n.categoryToSave.categoryCustomData=n.addCategoryData;
n.categoryToSave.dbTypeForCategory=n.dbTypeForCategory;
if(!n.editCategory){if(n.popupEventCategoryId!=="D"){n.categoryToSave.parentId=n.popupEventCategoryId
}n.createEventCategory(o)
}else{if(n.parentId!=="D"){n.categoryToSave.parentId=n.parentId
}if(n.categoryToSave.status==="Remove"){n.categoryName=n.categoryToSave.displayName;
if(n.allUpcomingEvents.length>0){j.addMessage("Category is not empty. Remove events and try again.",1);
j.unMaskLoading()
}else{angular.forEach(n.existingEventCategoryList,function(q){if(n.categoryToSave.id===q.id){p++;
if(q.children!==null&&q.children.length>0){j.addMessage("Category is not empty. Remove child categories and try again.",1);
j.unMaskLoading()
}else{$("#removeEventCategoryPopup").modal("show");
j.unMaskLoading()
}}});
if(p>0){}else{$("#removeEventCategoryPopup").modal("show");
j.unMaskLoading()
}}}else{n.editEventCategory(o)
}}j.unMaskLoading()
}};
n.removeAndSaveCategory=function(o){$("#removeEventCategoryPopup").modal("hide");
j.removeModalOpenCssAfterModalHide();
n.editEventCategory(o)
};
n.removeCategoryCancel=function(){n.category.status="Active";
$("#removeEventCategoryPopup").modal("hide");
j.removeModalOpenCssAfterModalHide()
};
n.createEventCategory=function(o){l.createEventCategory(n.categoryToSave,function(p){j.unMaskLoading();
n.retriveEventCategories();
n.retrieveActiveEventCategories();
n.hideAddCategoryPopup(o)
},function(){j.unMaskLoading();
j.addMessage("Could not save details, please try again.",1)
})
};
n.editEventCategory=function(o){l.updateEventCategory(n.categoryToSave,function(p){j.unMaskLoading();
n.initManageEvent();
$(".modal-backdrop").remove()
},function(){j.unMaskLoading();
if(n.categoryToSave.status==="Remove"){j.addMessage("Event category could not be removed. Try again.",1)
}else{j.addMessage("Could not save details, please try again.",1)
}})
};
n.uploadFile={target:j.apipath+"fileUpload/uploadFile",singleFile:true,testChunks:true,query:{fileType:n.seletedFileType,model:"Event"}};
n.bannerFileUploaded=function(r,p,s,u){var q=[r.name,w,n.seletedFileType];
var x="banner";
n.bannerUploaded=true;
if(n.saveEventFlag===false){n.saveFilesAndEvent(s)
}var w="Event";
var t=[r.name,x];
var v;
var o="true";
var q;
l.uploadFile(t,function(y){v=y.filename;
q=[r.name,w,v,o];
e.uploadFiles(q,function(z){n.event.bannerImageName=z.res;
n.imageName=null
})
})
};
n.removeFromList=function(p,o){p.files.splice(o,1);
n.event.bannerImageName=null
};
n.removeBackground=function(p,o){p.files.splice(o,1);
p.cancel();
e.removeImageFile(n.event.invitationTemplateName,function(){n.event.invitationTemplateName=undefined
});
$(document).find("#templateBackground").css("background","url('')")
};
n.getTemplateName=function(){return j.apipath+"event/getbackgroundimage?file_name="+n.event.invitationTemplatePath
};
n.backgroundFileUploaded=function(r,p,s,u){j.maskLoading();
var q=[r.name,w,n.seletedFileType];
var x="background";
n.backgroundUploaded=true;
b(function(){n.showInvitationCardPopup();
n.previewForm=true;
n.setBackground()
});
if(n.saveEventFlag===false){n.saveFilesAndEvent(s)
}var w="Event";
var t=[r.name,x];
var v;
var o="true";
var q;
l.uploadFile(t,function(y){v=y.filename;
q=[r.name,w,v,o,700,400];
e.uploadFiles(q,function(z){j.unMaskLoading();
n.event.invitationTemplateName=z.res;
n.invitationImageName=null
})
})
};
n.bannerFileAdded=function(p,o){n.bannerFlow=o;
n.uploadFile.query.fileType="banner";
n.bannerUploaded=false;
if((p.getExtension()!=="jpg")&&(p.getExtension()!=="jpeg")&&(p.getExtension()!=="png")&&(p.getExtension()!=="gif")&&(p.getExtension()!=="bmp")){n.bannerImg.invalidFileFlag=true;
n.bannerImg.fileName=p.name
}else{if(p.size>10485760){n.bannerImg.invalidFileSizeFlag=true;
return false
}}return !!{jpg:1,jpeg:1,gif:1,png:1,bmp:1}[p.getExtension()]
};
n.backgroundFileAdded=function(p,o){n.backgroundFlow=o;
n.backgroundUploaded=false;
n.uploadFile.query.fileType="background";
if((p.getExtension()!="jpg")&&(p.getExtension()!="jpeg")&&(p.getExtension()!="png")&&(p.getExtension()!="gif")){n.backgroundImg.invalidFileFlag=true;
n.backgroundImg.fileName=p.name
}else{if(p.size>5242880){n.backgroundImg.invalidFileSizeFlag=true;
return false
}}return !!{jpg:1,jpeg:1,gif:1,png:1}[p.getExtension()]
};
n.galleryUploadFile={target:j.apipath+"fileUpload/uploadFile",singleFile:false,testChunks:false,query:{fileType:n.seletedFileType,model:"Event"}};
n.galleryFileAdded=function(r,p){n.galleryFlow=p;
n.galleryUploadFile.query.fileType="gallery";
var o=5000000;
var q=r.size;
if((r.getExtension()!=="jpg")&&(r.getExtension()!=="jpeg")&&(r.getExtension()!=="png")&&(r.getExtension()!=="gif")){n.validFileFlag=true;
n.fileNames.push(r.name);
alert("Only images are supported");
return false
}else{if(o<q){n.validFileFlag=true;
n.fileNames.push(r.name);
alert("You can upload a file upto 5 MB ");
return false
}return true
}};
n.galleryFileUploaded=function(r,p,t){var q=[r.name,v,n.seletedFileType];
var w="gallery";
var v="Event";
var s=[r.name,w];
var u;
var o="false";
var q;
l.uploadFile(s,function(x){u=x.filename;
q=[r.name,v,u,o];
e.uploadFiles(q,function(y){n.eventToSaveGallery=j.eventToSaveGallery;
r.msg=y.res;
n.eventToSaveGallery.fileList.push(y.res)
})
})
};
n.removeUploadedImage=function(p,q){var o=n.eventToSaveGallery.fileList.indexOf(q);
if(o>-1){n.eventToSaveGallery.fileList.splice(o,1)
}p.cancel()
};
n.createPhotoGallery=function(o){j.eventToSaveGallery=o;
j.eventToSaveGallery.fileList=[]
};
n.submitFilesAndSaveGallery=function(o){j.maskLoading();
if(o.files.length>0){l.createPhotoGallery(n.eventToSaveGallery,function(p){j.unMaskLoading();
n.displayGallery(n.eventToSaveGallery.id);
n.eventToSaveGallery={};
o.cancel();
n.hideGalleryPopup();
n.retrieveEvents("null")
},function(){j.unMaskLoading()
})
}else{j.addMessage("Select Photos for the album.",1);
n.hideGalleryPopup();
n.eventToSaveGallery={};
o.cancel();
j.unMaskLoading()
}};
n.cancelGallery=function(o){j.maskLoading();
n.hideGalleryPopup();
n.eventToSaveGallery={};
o.cancel();
j.unMaskLoading()
};
n.closeGalleryDisplay=function(){n.eventForGallery={};
n.initManageEvent()
};
n.toggleSelection=function c(p){var o=n.selection.indexOf(p);
if(o>-1){n.selection.splice(o,1)
}else{n.selection.push(p)
}};
n.deletePhotos=function(){for(var o=0;
o<n.selection.length;
o++){e.removeImageFile(n.selection[o],function(){})
}n.selection=[];
n.displayGallery(n.eventForGallery.id)
};
n.initInvtitationCard=function(){n.invitationCard={eventTitle:n.event.eventTitle,eventDescription:n.event.description,address:n.event.address,fromDate:n.event.fromDate,strtTime:n.event.strtTime,contentColor:n.event.contentColor,labelColor:n.event.labelColor,employeeName:"$Employee Name",employeeAddress:"$Employee Address Line 1\n$Employee Address Line 2",employeeEmail:"$Employee Email",employeePhoneNo:"$Employee Phone No.",totalAdults:"$Total adults accompaning",totalChildren:"$Total children accompaning"}
};
n.showInvitationCardPopup=function(){n.previewConfigureForm=true;
n.preview=false;
d.remove(j.apipath+"event/getbackgroundimage?file_name="+n.event.invitationTemplatePath);
n.initInvtitationCard();
$("#invitationCardPopup").modal("show")
};
n.showPreviewPopup=function(o){n.event=o;
n.previewForm=true;
n.preview=true;
d.remove(j.apipath+"event/getbackgroundimage?file_name="+n.event.invitationTemplatePath);
n.initInvtitationCard();
n.invitationCard.employeeName=n.eventDetails.employeeName;
n.invitationCard.employeeAddress=n.eventDetails.employeeAddress;
n.invitationCard.employeeEmail=n.eventDetails.employeeEmail;
n.invitationCard.employeePhoneNo=n.eventDetails.employeePhoneNo;
n.invitationCard.totalAdults=n.event.adultCount;
n.invitationCard.totalChildren=n.event.childCount;
n.eventsId=n.event.id;
$("#invitationCardPopup").modal("show")
};
n.hideInvitationCardPopup=function(){$("#invitationCardPopup").modal("hide");
j.removeModalOpenCssAfterModalHide()
};
n.showGalleryPopup=function(){$("#addGalleryPopup").modal("show")
};
n.viewPhotoGallery=function(o){n.eventForGallery=angular.copy(o);
n.setEventOperation(n.VIEW_PHOTO_GALLERY);
n.displayGallery(n.eventForGallery.id)
};
n.hideGalleryPopup=function(){$("#addGalleryPopup").modal("hide");
j.removeModalOpenCssAfterModalHide()
};
n.checkUniqueLabelName=function(p,o,q){if(n.findFieldLabel(p,q)){o.fieldLabel.$setValidity("exists",false)
}};
n.addNewField=function(o){n.newFieldFormSubmitted=true;
if(o.$valid){n.fieldToAdd=angular.copy(n.field);
n.event.registrationFieldsDataBean.push(n.fieldToAdd);
n.newFieldFormSubmitted=false;
n.field={};
o.$setPristine()
}};
n.removeField=function(o){n.event.registrationFieldsDataBean.splice(o,1)
};
n.findFieldLabel=function(q,p){if(q!==undefined){for(var o=0;
o<n.event.registrationFieldsDataBean.length;
o++){if(o!==p&&n.event.registrationFieldsDataBean[o].fieldName.toLowerCase()===q.toLowerCase()){return true
}}}return false
};
n.saveRegistrationForm=function(o){n.regformSubmitted=true;
if(o.configureRegForm.$valid&&(o.addedFieldForm===undefined||o.addedFieldForm.$valid)){if(n.select2FormNames[0].id!=="0"){n.select2FormNames.unshift({id:"0",text:n.event.registrationFormName})
}else{n.select2FormNames[0]={id:"0",text:n.event.registrationFormName}
}j.addMessage("Registration form configured.",0);
n.setConfigFormFlag(false);
n.event.selectedRegForm="0";
n.configuredRegistrationFieldDataBean=n.event.registrationFieldsDataBean;
n.event.registrationFormUpdated=true
}};
n.minEventFromDate=j.getCurrentServerDate();
n.minEventPublisedDate=j.getCurrentServerDate();
n.setEditEventMinFromDate=function(){n.minEventFromDate=j.getCurrentServerDate()
};
n.setEditEventMinPublishedOnDate=function(){n.minEventPublisedDate=j.getCurrentServerDate()
};
n.datePicker={};
n.open=function(o,p){o.preventDefault();
o.stopPropagation();
n.datePicker[p]=true
};
n.dateOptions={"year-format":"'yy'","starting-day":1};
n.format=j.dateFormat;
n._Index=0;
n.isActive=function(o){return n._Index===o
};
n.showPrev=function(){n._Index=(n._Index>0)?--n._Index:n.viewPhotos.length-1
};
n.showNext=function(){n._Index=(n._Index<n.viewPhotos.length-1)?++n._Index:0
};
n.showPhoto=function(o){n._Index=o
};
n.initpop=function(){$("body").keydown(function(o){if(o.keyCode==37){alert("left")
}else{if(o.keyCode==39){alert("right")
}}})
};
n.viewlist=true;
n.reg=[];
n.viewRegistrationDetails=function(p){if(p.registrationType!=="OFLN"){n.viewMeetingDetailsPopup=false;
if(p.status==="Upcoming"||p.status==="Ongoing"){n.viewRegistrationDetail=true;
n.setEventOperation(n.REGISTER_EVENT);
var o=p.id;
n.retrieveUserRegistration(o);
n.selectedEventForRegistration=p
}}};
n.registerForEvent=function(o,p){n.viewRegistrationDetail=false;
n.editRegistrationFlag=p;
n.setEventOperation(n.REGISTER_EVENT);
l.retrieveRegistrationFields(o.id,function(q){o.registrationFieldsDataBean=q;
n.selectedEventForRegistration=o;
l.retrieveCustomValues(o.id,function(r){if(r!==undefined&&r!==null&&r.length>0){n.selectedEventForRegistration.registrationFieldsDataBean=r
}},function(r){})
},function(){});
if(o.registrationStatus!==undefined){n.reg.adults=o.userAdultCount;
n.reg.notAttending=!o.registrationStatus;
n.reg.reason=o.reason;
n.reg.children=o.userChildCount;
if(o.guests===null){n.guests=[]
}else{n.guests=o.guests
}}else{n.adults=undefined;
n.children=undefined;
n.guests=[]
}};
n.openRegDetails=function(p,o){if((o.adultCount!==undefined&&o.adultCount!==0)||(o.childCount!==undefined&&o.childCount!==0)){n.registrationDetailsForUser(p,o,true);
$("#registrationDetailsOfUserPopup").modal("show")
}else{j.addMessage("User have not registered yet.",1)
}};
n.registrationDetailsForUser=function(p,o,q){n.editRegistrationFlag=q;
l.retrieveRegistrationFields(p.id,function(r){p.registrationFieldsDataBean=r
},function(){});
n.regDetails=p;
n.regDtls=[];
n.regDtls.empName=o.empName;
if(p.registrationStatus!==undefined){n.regDtls.adults=o.adultCount;
n.regDtls.children=o.childCount;
if(o.guests===null){n.guestsDtls=[]
}else{n.guestsDtls=o.guests
}n.dataToSend={eventId:p.id,userId:o.userId};
l.retrieveCustomValuesByUser(n.dataToSend,function(r){n.regDetails.registrationFieldsDataBean=r
},function(r){})
}};
n.cancel=function(o){n.formSubmitted=false;
n.newFieldFormSubmitted=false;
o.$setPristine();
n.reg=[];
n.initManageEvent()
};
n.returnFromDetails=function(){n.initManageEvent()
};
n.guests=[];
n.addGuest=function(p,o){n.regGuest=o;
n.newFieldFormSubmitted=true;
if(p.$valid){n.guests.push({name:n.regGuest.gname,relation:n.regGuest.grel});
n.regGuest.gname="";
n.newFieldFormSubmitted=false;
n.regGuest.grel="";
p.$setPristine()
}};
n.removeGuest=function(o){n.guests.splice(o,1)
};
n.reg=[];
n.register=function(s,p){n.formSubmitted=true;
n.reg=p;
if(((s.countForm!==undefined&&s.countForm.$valid)||(s.reasonForm!==undefined&&s.reasonForm.$valid))){if(s.addedGuestForm===undefined||(s.addedGuestForm!==undefined&&s.addedGuestForm.$valid)){n.formSubmitted=false
}n.newFieldFormSubmitted=false;
if(n.notAttending===undefined){n.notAttending=false
}var r={id:n.selectedEventForRegistration.id,adultCount:n.reg.adults,reason:n.reg.reason,isAttending:n.reg.notAttending,childCount:n.reg.children,guestCount:n.guests.length,registrationFieldsDataBean:[],guests:[]};
n.selectedEventForRegistration.adultCount=n.reg.adults;
n.selectedEventForRegistration.childCount=n.reg.children;
n.selectedEventForRegistration.guests=n.guests;
if(n.selectedEventForRegistration.registrationFieldsDataBean!==undefined&&n.selectedEventForRegistration.registrationFieldsDataBean!==null){for(var o=0;
o<n.selectedEventForRegistration.registrationFieldsDataBean.length;
o++){var q=n.selectedEventForRegistration.registrationFieldsDataBean[o];
if(q.value!==undefined&&q.value!==null){r.registrationFieldsDataBean.push({id:q.id,fieldName:q.fieldName,value:q.value})
}}}for(var o=0;
o<n.guests.length;
o++){var q=n.guests[o];
r.guests.push({name:q.name,relation:q.relation})
}if(!n.editRegistrationFlag){j.maskLoading();
l.registerForEvent(r,function(t){n.selectedEventForRegistration.registrationStatus=!n.reg.notAttending;
n.selectedEventForRegistration.reason=n.reg.reason;
s.$setPristine();
n.reg={};
j.unMaskLoading();
n.initManageEvent()
},function(t){})
}else{j.maskLoading();
l.editRegistrationforevent(r,function(t){n.selectedEventForRegistration.registrationStatus=!n.reg.notAttending;
n.selectedEventForRegistration.reason=n.reg.reason;
s.$setPristine();
n.editRegistrationFlag=false;
n.reg={};
j.unMaskLoading();
n.initManageEvent()
},function(t){})
}}};
n.cancelRegistration=function(o){n.selectedEventForRegistration=o;
l.cancelRegistration(o.id,function(q){n.selectedEventForRegistration.guests=[];
n.initManageEvent();
var r=n.selectedEventForRegistration.registrationFieldsDataBean;
for(var p=0;
p<r.length;
p++){r[p].value=""
}},function(){})
};
n.retrieveUserRegistration=function(o){n.regResponse=false;
l.retrieveUserRegistrationEntities(o,function(p){n.regResponse=true;
n.attendeeList=p;
n.totalCount=n.attendeeList.length;
n.attendeeCount=0;
n.notAttendingCount=0;
n.awaitingResponseCount=0;
angular.forEach(n.attendeeList,function(q){if(q.status==="AT"){n.attendeeCount=n.attendeeCount+1
}else{if(q.status==="NAT"){n.notAttendingCount=n.notAttendingCount+1
}else{if(q.status===null){n.awaitingResponseCount=n.awaitingResponseCount+1
}}}});
n.attendeesOnstatus=[];
if(n.attendeeList!==null&&angular.isDefined(n.attendeeList)&&n.attendeeList.length>0){angular.forEach(n.attendeeList,function(q){if(q.status===n.statusField){n.attendeesOnstatus.push(q)
}})
}})
};
n.retrieveAttendeeListOnStatus=function(o){n.attendeesOnstatus=[];
if(n.attendeeList!==null&&angular.isDefined(n.attendeeList)&&n.attendeeList.length>0){if(o==="Attending"){angular.forEach(n.attendeeList,function(p){if(p.status==="AT"){n.attendeesOnstatus.push(p)
}})
}else{if(o==="NonAttending"){angular.forEach(n.attendeeList,function(p){if(p.status==="NAT"){n.attendeesOnstatus.push(p)
}})
}else{if(o==="AwaitingResponse"){angular.forEach(n.attendeeList,function(p){if(p.status===null){n.attendeesOnstatus.push(p)
}})
}}}}};
n.displayGallery=function(o){n.photos=[];
if(n.photos.length===0){l.retrieveImageThumbnailPaths(o,function(p){var q=p.src;
angular.forEach(q,function(r){var s=r.replace("_T","");
n.photos.push({src:j.appendAuthToken(j.apipath+"event/getimage?file_name="+r),name:s})
})
})
}};
n.exportChange=function(o){if(o==="Attending"){if(n.attendeeCount===0){n.disablePdf=true
}else{n.disablePdf=false
}}else{n.disablePdf=false
}};
j.unMaskLoading()
}])
});