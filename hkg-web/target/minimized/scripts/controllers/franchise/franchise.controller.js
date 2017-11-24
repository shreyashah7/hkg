define(["hkg","franchiseService","addMasterValue","dynamicForm","whitespace-remove.directive"],function(a,b){a.register.controller("FranchiseController",["$rootScope","$scope","FranchiseService","DynamicFormService","$timeout",function(i,k,f,j,e){i.maskLoading();
k.searchRecords=[];
i.mainMenu="manageLink";
i.childMenu="manageFranchise";
i.activateMenu();
k.allLocations=[];
k.select2Locations=[];
k.entity="FRANCHISE.";
k.franchiseModelList=[];
k.isCreate=true;
k.reload=true;
k.$on("$viewContentLoaded",function(){k.initializtion();
c();
g()
});
k.initCreateForm=function(l){k.franchiseCreateForm=l
};
k.resetCustomFields=function(){k.addFranchiseData={};
var l=j.retrieveSectionWiseCustomFieldInfo("manageFranchise");
k.dbType={};
l.then(function(m){k.generalFranchiseTemplate=m.genralSection
},function(m){},function(m){})
};
function c(){k.isAccessCreateOrUpdate=true;
if(!i.canAccess("franchiseAddEdit")){k.isAccessCreateOrUpdate=false
}k.franchiseActivateDeactivate=true;
if(!i.canAccess("franchiseActivateDeactivate")){k.franchiseActivateDeactivate=false
}}k.initializtion=function(){k.reload=false;
k.selectedTab=1;
k.franchise={};
k.editFranchise={};
k.submitted=false;
k.isCreate=true;
k.isMatched=false;
k.isuserMatched=true;
k.isDesignationSelected=false;
k.isEmailValidate=true;
k.isPhoneValidate=true;
k.retrieveAllFranchise();
k.retrieveLocations();
k.designations=[];
k.machines=[];
k.editdesignations=[];
k.editmachines=[];
k.resetSelection();
k.notUniqueFranchise=false;
k.notUniqueUsername=false;
k.searchtext="";
k.isInValidSearch=false;
k.displayFlag="view";
k.showTab=true;
k.searchFranchiseId=undefined;
k.canActivated=false;
e(function(){k.resetCustomFields();
k.reload=true
},50);
$("#country").select2("data","");
var l=j.retrieveSectionWiseCustomFieldInfo("manageFranchise");
k.dbType={};
l.then(function(m){k.generalFranchiseTemplate=m.genralSection;
k.addFranchiseData=j.resetSection(k.generalFranchiseTemplate)
},function(m){},function(m){})
};
k.multiLocations={multiple:false,closeOnSelect:false,placeholder:"Select District",allowClear:true,initSelection:function(l,n){if(!k.isCreate){var m={};
m={id:k.editFranchise.location.id,text:k.editFranchise.location.text};
n(m)
}},data:function(){return{results:k.select2Locations}
}};
function g(){var l="T";
f.retrieveLocationsByType({type:l},function(m){k.allLocations=m.sort(k.predicateBy("label"));
angular.forEach(k.allLocations,function(n){k.select2Locations.push({id:n.value,text:n.label})
});
if(k.allLocations.length==0){i.addMessage("Please enter the locations to create franchise",1)
}$("#location").select2("val",null)
})
}k.openPage=function(l){k.selectedTab=1;
if(l=="C"){k.isCreate=true;
k.searchtext=undefined;
k.resetSelection();
k.designations=[];
k.machines=[];
k.addFranchiseData=j.resetSection(k.generalFranchiseTemplate)
}else{k.isCreate=false
}k.displayFlag="view";
k.retrievePreData()
};
k.retrievePreData=function(){k.submitted=false;
k.franchise={}
};
k.resetSelection=function(){if(k.selectedFranchiseTree!=undefined){k.selectedFranchiseTree.selected=undefined
}if(k.selectedFranchiseTree!=undefined&&k.selectedFranchiseTree.currentNode!=undefined){k.selectedFranchiseTree.currentNode.selected=undefined;
k.selectedFranchiseTree.currentNode=undefined
}};
k.isValidFormat=function(l){if(angular.isDefined(l)){k.invalidname=true;
var m=l.trim().split(" ");
k.firstName=m[0];
k.middleName=m[1];
k.lastName=m[2];
if(m.length==3){k.invalidname=false
}else{k.invalidname=true
}}};
k.createFranchise=function(q){k.relaod=false;
k.submitted=true;
k.isDesignationSelected=true;
if(k.designations===null||k.designations.length===0){k.isDesignationSelected=false
}else{for(var n=0;
n<k.designations.length;
n++){var t=k.designations[n];
if(angular.isDefined(t.requiredValue)&&t.requiredValue!==null&&t.requiredValue.toString().length>0){k.isDesignationSelected=false;
break
}}}k.isValidFormat(k.franchise.adminName);
if(k.isDesignationSelected){k.selectedTab=2
}if(q.$valid&&!k.invalidname&&k.isEmailValidate&&k.isPhoneValidate&&!k.notUniqueFranchise){if(k.existingFranchiseList.length===0){k.isDesignationSelected=false
}if(!k.isDesignationSelected){i.maskLoading();
var l=angular.copy(k.franchise);
l.firstName=k.firstName;
l.middleName=k.middleName;
l.lastName=k.lastName;
var o=l.location.toString().split("#");
if(o.length===3){l.city=o[0];
l.state=o[1];
l.country=o[2]
}else{if(o.length===4){l.city=o[0];
l.district=o[1];
l.state=o[2];
l.country=o[3]
}}l.adminRetypePwd=undefined;
var s=angular.copy(k.designations);
angular.forEach(k.machines,function(u){s.push(u)
});
l.franchiseMinReq=angular.copy(s);
l.franchiseCustom=k.addFranchiseData;
l.franchiseDbType=k.dbType;
l.firstFranchise=!(k.existingFranchiseList.length>0);
l.adminName=undefined;
var r=function(u){k.resetCustomFields();
k.reload=true;
k.selectedTab=1;
q.$setPristine();
k.initializtion();
k.searchtext=undefined;
i.unMaskLoading()
};
var m=function(){k.resetCustomFields();
k.reload=true;
i.addMessage("Could not save details, please try again.",1);
i.unMaskLoading()
};
f.createFranchise(l,r,m)
}}else{if(!q.$valid){for(var p in q){if(p.indexOf("$")!==0){if(q[p].$invalid){k.scrollTo(p.toString());
break
}}}}}};
k.changeSelectedTab=function(l){k.selectedTab=l
};
k.confirmPasswordValidation=function(){if(!angular.equals(k.franchise.password,k.franchise.adminRetypePwd)){k.isMatched=true
}else{k.isMatched=false
}};
k.userNameValidationCheck=function(){if(k.franchise.adminUserName&&k.franchise.password){if(angular.equals(k.franchise.adminUserName,k.franchise.password)){k.isuserMatched=false
}else{k.isuserMatched=true
}}};
k.cancelFranchise=function(l){l.$setPristine();
k.initializtion()
};
k.emailValidate=function(l){if(l){var n;
n=l.split(",");
for(var m=0;
m<n.length;
m++){if(n[m].length>0){if(!d(n[m])){k.isEmailValidate=false;
return
}else{k.isEmailValidate=true
}}else{k.isEmailValidate=true
}}}};
function d(l){var m=/^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
if(!m.test(l)){return false
}else{return true
}}k.phoneValidate=function(n){if(n){var l=n.split(",");
for(var m=0;
m<l.length;
m++){if(l[m].length>=10){if(!h(l[m])){k.isPhoneValidate=false;
return
}else{k.isPhoneValidate=true
}}else{if(l[m].length===0){}else{k.isPhoneValidate=false;
return
}}}}};
function h(l){var m=/^\d{10}$/;
if(l.match(m)){return true
}else{return false
}}k.retrieveAllFranchise=function(){i.maskLoading();
f.retrieveAllFranchise({tree:false},function(n){var m=[];
angular.forEach(n,function(o){o.franchiseName=i.translateValue("FRNCSE_NM."+o.franchiseName);
m.push(o)
});
k.franchiseModelList=m;
k.existingFranchiseList=[];
for(var l=0;
l<m.length;
l++){k.existingFranchiseList.push({id:m[l].id,displayName:m[l].franchiseName})
}i.unMaskLoading()
},function(){i.addMessage("Failed to retrieve franchises",1);
i.unMaskLoading()
})
};
k.predicateBy=function(l){return function(n,m){if(n[l]>m[l]){return 1
}else{if(n[l]<m[l]){return -1
}}return 0
}
};
k.doesFranchiseNameExist=function(l,n,m){if(l!=null){k.frnachiseNameDetails={franchiseName:l,id:k.retrievedFranchiseId};
if(k.frnachiseNameDetails.franchiseName&&k.frnachiseNameDetails.franchiseName.length>0){f.doesFranchiseNameExist(k.frnachiseNameDetails,function(o){if(o.data){k.notUniqueFranchise=true
}else{k.notUniqueFranchise=false;
if(m=="create"){k.createFranchise(n)
}else{k.updateFranchise(n)
}}},function(){})
}else{k.notUniqueFranchise=false
}}else{if(m=="create"){k.createFranchise(n)
}else{k.updateFranchise(n)
}}};
k.doesUserNameExist=function(l){if(l&&l.length>0){f.doesUserNameExist(l,function(m){if(m.data){k.notUniqueUsername=true
}else{k.notUniqueUsername=false
}},function(){})
}else{k.notUniqueUsername=false
}};
k.retrieveLocations=function(){i.maskLoading();
f.retrieveAllLocations({active:"false"},function(l){k.locationMap=l.locationMap;
k.countryList=l.countryList;
i.unMaskLoading()
},function(){var m="Fail to load Location Master";
var l=i.failure;
i.addMessage(m,l);
i.unMaskLoading()
})
};
k.getChilds=function(l,m){if(m==="S"){k.stateList=k.locationMap[l]
}else{if(m==="D"){k.districtList=k.locationMap[l]
}else{if(m==="C"){k.cityList=k.locationMap[l]
}}}};
k.selectFranchise=function(m){k.reload=false;
k.selectedTab=1;
if(angular.isDefined(k.franchiseCreateForm)){k.franchiseCreateForm.$setPristine()
}i.maskLoading();
k.searchtext="";
k.setViewFlag("view");
if(angular.isDefined(m)&&m.id!==null){var o=m.id;
k.isDesignationSelected=false;
k.addFranchiseData=j.resetSection(k.generalFranchiseTemplate);
k.editdesignations=null;
k.editmachines=null;
var n=function(s){k.retrievedFranchiseId=s.id;
k.editFranchise=angular.copy(s);
k.editFranchise.location=s.city+"#"+s.district+"#"+s.state+"#"+s.country;
$("#multiLocations").select2("data",[]);
angular.forEach(k.select2Locations,function(t){if(t.id===s.city+"#"+s.district+"#"+s.state+"#"+s.country){k.editFranchise.location=t
}});
k.editFranchise.adminName=s.firstName+" "+s.middleName+" "+s.lastName;
k.franchiseName=angular.copy(s.franchiseName);
k.created=false;
k.getChilds(s.country,"S");
k.getChilds(s.state,"D");
k.getChilds(s.district,"C");
if(angular.isDefined(s.franchiseCustom)&&s.franchiseCustom!=null){k.resetCustomFields();
k.addFranchiseData=angular.copy(s.franchiseCustom);
k.reload=true
}var r=angular.copy(k.editFranchise.franchiseMinReq);
k.isNotEmply=true;
if(r===null){k.isNotEmply=false;
k.editdesignations=null;
k.editmachines=null
}else{var q=[];
var p=[];
angular.forEach(r,function(t){if(t.requirementType=="DEG"){q.push(t)
}else{p.push(t)
}});
k.editdesignations=angular.copy(q);
k.editmachines=angular.copy(p)
}k.editFranchise.franchiseMinReq=[];
if(s.status=="P"){k.created=true;
k.canBeActivated()
}k.openPage("E");
i.unMaskLoading()
};
var l=function(){i.unMaskLoading()
};
f.retrieveFranchiseDetailById({primaryKey:o},n,l)
}else{k.openPage("C")
}};
k.updateFranchise=function(r){if(k.editFranchise.status==="R"){k.franchiseId=k.editFranchise.id;
$("#removeModal").modal("show")
}else{k.submitted=true;
k.isDesignationSelected=true;
if(k.editdesignations==null||k.editdesignations.length==0){k.isDesignationSelected=false
}else{for(var o=0;
o<k.editdesignations.length;
o++){var m=k.editdesignations[o];
if(angular.isDefined(m.requiredValue)&&m.requiredValue!==null&&m.requiredValue.toString().length>0){k.isDesignationSelected=false;
break
}}}if(r.$valid&&!k.isDesignationSelected){i.maskLoading();
if(k.editFranchise.status=="P"||k.canActivated){var p=angular.copy(k.editdesignations);
angular.forEach(k.editmachines,function(s){p.push(s)
});
k.editFranchise.franchiseMinReq=angular.copy(p)
}k.editFranchise.franchiseCustom=k.addFranchiseData;
k.editFranchise.franchiseDbType=k.dbType;
k.editFranchise.adminName=undefined;
var l=k.editFranchise.location.toString().split("#");
if(l.length===3){k.editFranchise.city=l[0];
k.editFranchise.state=l[1];
k.editFranchise.country=l[2]
}else{if(l.length===4){k.editFranchise.city=l[0];
k.editFranchise.district=l[1];
k.editFranchise.state=l[2];
k.editFranchise.country=l[3]
}}if(k.editFranchise.location instanceof Object){var q="";
angular.forEach(k.editFranchise.location,function(s){if(q===""){q=s.id
}else{q=q+","+s.id
}});
k.editFranchise.location=q
}f.updateFranchise(k.editFranchise,function(s){r.$setPristine();
k.initializtion();
k.searchtext=undefined;
i.unMaskLoading()
},function(){i.addMessage("Could not save details, please try again.",1);
i.unMaskLoading()
})
}else{if(!r.$valid){for(var n in r){if(n.indexOf("$")!==0){if(r[n].$invalid){k.scrollTo(n.toString());
break
}}}}}}};
k.onModelChange=function(l){k.showTab=true;
if(angular.isDefined(l)){k.designations=[];
k.machines=[];
if(l!==0&&l!==-1&&l!=null){k.setDesignationAndMachine(l)
}}};
k.setDesignationAndMachine=function(n){k.designations=[];
k.editdesignations=[];
k.machines=[];
k.editmachines=[];
var m=function(r){var q=angular.copy(r);
if(q==null||q.length==0){}else{var p=[];
var o=[];
angular.forEach(q,function(s){if(s.requirementType=="DEG"){p.push(s)
}else{o.push(s)
}});
k.designations=angular.copy(p);
k.machines=angular.copy(o);
k.editmachines=angular.copy(p);
k.editdesignations=angular.copy(o)
}};
var l=function(){};
f.retrieveAllFranchiseMinReq({id:n},m,l)
};
k.canBeActivated=function(){k.canActivated=false;
var l=true;
if(!((k.editdesignations!==null&&k.editdesignations.length>0)||(k.editmachines!==null&&k.editmachines.length>0))){l=false
}angular.forEach(k.editdesignations,function(m){if(l&&!(angular.isDefined(m.acquiredValue)&&angular.isDefined(m.requiredValue)&&m.acquiredValue>=m.requiredValue)){l=false
}});
if(l){angular.forEach(k.editmachines,function(m){if(l&&!(angular.isDefined(m.acquiredValue)&&angular.isDefined(m.requiredValue)&&m.acquiredValue>=m.requiredValue)){l=false
}})
}k.canActivated=l;
if(!l){k.editFranchise.status="P"
}};
k.isThereAnyLinkWithFranchise=function(){var l=k.franchiseId;
k.removeFranchise(l)
};
k.removeFranchise=function(n){var m=function(){$("#removeModal").modal("hide");
i.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove();
k.initializtion()
};
var l=function(){};
f.deleteFranchise({primaryKey:n},m,l)
};
k.hideRemovePanel=function(){k.editFranchise.status="P";
$("#removeModal").modal("hide");
i.removeModalOpenCssAfterModalHide()
};
k.ok=function(){k.editFranchise.status="P";
$("#infoModal").modal("hide");
i.removeModalOpenCssAfterModalHide()
};
k.setViewFlag=function(l){if(l!=="search"){$("#scrollable-dropdown-menu.typeahead").typeahead("val","")
}k.displayFlag=l
};
k.getSearchedFranchise=function(m){k.resetSelection();
var l=$("#scrollable-dropdown-menu.typeahead").typeahead("val");
if(l.length>0){if(l.length<3){k.searchRecords=[]
}else{k.searchRecords=[];
angular.forEach(m,function(n){k.searchRecords.push(n)
})
}k.setViewFlag("search")
}};
k.setFranchiseForEdit=function(m){var l={id:m};
k.searchFranchiseId=m;
k.selectFranchise(l)
};
i.unMaskLoading();
k.doesFranchiseCodeExists=function(n,l){if(n!==undefined&&n!==null){k.submitted=false;
if(l){k.franchiseCreateForm.franchiseCode.$setValidity("exists",true)
}if(k.franchiseModelList!==undefined&&k.franchiseModelList!==null&&k.franchiseModelList.length>0){for(var m=0;
m<k.franchiseModelList.length;
m++){if(k.franchiseModelList[m].franchiseCode!==undefined&&k.franchiseModelList[m].franchiseCode!==null&&k.franchiseModelList[m].franchiseCode.toUpperCase()===n.toUpperCase()){k.franchiseCreateForm.franchiseCode.$setValidity("exists",false);
break
}}}}}
}])
});