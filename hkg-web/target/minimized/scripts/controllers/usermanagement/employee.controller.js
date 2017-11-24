define(["hkg","employeeService","franchiseService","fileUploadService","leaveWorkflowService","designationService","departmentService","addMasterValue","dynamicForm","datepickercustom.directive","webcam"],function(b,a,g,d,e,c,f){b.register.controller("EmployeeController",["$rootScope","$scope","$filter","Employee","$location","$anchorScroll","FranchiseService","FileUploadService","LeaveWorkflow","Designation","DepartmentService","DynamicFormService","$route","$timeout","$modal",function(l,G,D,w,j,i,m,z,u,v,o,F,x,K,s){l.maskLoading();
G.searchRecords=[];
l.mainMenu="manageLink";
l.childMenu="manageEmployees";
l.activateMenu();
G.familyreload=true;
G.generalreload=true;
G.personalreload=true;
G.contactreload=true;
G.identificationreload=true;
G.tempeducationflag=true;
G.tempexperienceflag=true;
G.temppolicyflag=true;
G.otherDataflag=true;
G.hkgworkDataflag=true;
G.dt=null;
G.listOfModelsOfDateType={};
G.entity="EMPLOYEE.";
var y=D("orderBy");
G.today=l.getCurrentServerDate();
G.notAvailableSection="No Information Available for this section";
G.popover="<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td> \n '@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr> <tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr> </table> ";
G.searchpopover="<NOBR><font color='red;'>Use the shortcut to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td> \n '@R'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Designations</td></tr> </table> ";
G.temp='<input type="text" placeholder="mm/dd/yyyy" min="mindobDate" max="maxdobDate" ng-change="updateYearsOfPassing()" class="form-control" datepicker-popup="{{format}}" ng-model="employee.dob" is-open="emp_dob_opened" ng-required="true" close-text="Close" name="input_empdob"/>';
G.$on("$viewContentLoaded",function(){G.initializeEmployee()
});
G.initializeEmployee=function(){G.select2Locations=[];
A();
r();
p();
h()
};
G.open=function(M){M.preventDefault();
M.stopPropagation()
};
G.searchEmployeeInPrevious=function(M){console.log("franchiseId :"+M);
G.previousFranchise=M
};
function r(){l.maskLoading();
w.retrievePrerequisite(function(U){var P=l.getCurrentServerDate();
var Z=P.getFullYear()+1;
P.setFullYear(Z);
G.joiningdate=P;
var N=U.combovalues;
var ab=U.designations;
var V=U.locations;
var Y=U.franchise;
var T=U.departments;
var M=U.employeeConfig;
var X=U.agelimit;
var R=U.employeeStatus;
var S=Object.keys(M).length;
var ac=Object.keys(X).length;
var W=l.getCurrentServerDate();
var aa;
if(X.minAge!=null){aa=W.getFullYear()-X.minAge
}else{aa=W.getFullYear()-18
}W.setFullYear(aa);
G.initDate=new Date(W);
G.dateOptions={"init-date":"$parent.$parent.initDate"};
G.setJoiningDate();
if(S==0||ac==0){G.isFranchiseConfigured=false;
l.addMessage("Please configure employee type & its id along with age limit first and then try creating employee",1)
}if(ac!=0){var Q=X.minAge;
var O=X.maxAge;
if(angular.isDefined(Q)&&angular.isDefined(O)&&Q!==null&&O!=null){G.today=l.getCurrentServerDate();
G.mindobDate=new Date(G.today.getFullYear()-O,G.today.getMonth(),G.today.getDate());
G.maxdobDate=new Date(G.today.getFullYear()-Q,G.today.getMonth(),G.today.getDate())
}else{G.mindobDate=new Date(G.today.getFullYear()-55,G.today.getMonth(),G.today.getDay());
G.maxdobDate=new Date(G.today.getFullYear()-18,G.today.getMonth(),G.today.getDay())
}}q(N);
G.setYearOfPassing();
k(R);
B(T);
E(V);
G.franchiseList=Y;
l.unMaskLoading()
},function(){l.unMaskLoading()
})
}G.setYearOfPassing=function(){G.yearOfPassing=[];
if(!!G.employee.dob){var N=angular.copy(G.employee.dob.getFullYear());
var M=G.today.getFullYear();
var O;
for(O=M;
O>N;
O--){G.yearOfPassing.push(O)
}}};
G.setJoiningDate=function(){if(!!G.employee.dob){G.minJoiningDate=new Date(G.employee.dob.getFullYear()+1,0,1)
}};
G.setWorkExperienceEndDate=function(M){var N=M.getDate()+1;
G.endExperienceInitrange=M.setDate(N);
M.setDate(N-1)
};
G.setWorkExperienceStartDate=function(M){var N=M.getDate()-1;
G.startExperienceRange=M.setDate(N);
M.setDate(N+1)
};
G.setEditWorkExperienceStartDate=function(N){var M=N.getDate()-1;
G.startEditExperienceRange=N.setDate(M)
};
G.setEditWorkExperienceEndDate=function(N){var M=N.getDate()+1;
G.endEditExperienceInitrange=N.setDate(M)
};
function h(){G.isAccessAddEmployee=true;
G.isAccessUpdateEmployee=true;
if(!l.canAccess("employeesAdd")){G.isAccessAddEmployee=false
}if(!l.canAccess("employeesEdit")){G.isAccessUpdateEmployee=false
}}G.initializeDbType=function(){G.personaldbType={};
G.generaldbType={};
G.contactdbType={};
G.identificationdbType={};
G.otherdbType={};
G.hkgworkdbType={};
G.educationdbType={};
G.experiencedbType={};
G.familydbType={};
G.policydbType={};
G.editpolicydbType={};
G.editfamilydbType={};
G.editexperiencedbType={};
G.editeducationdbType={}
};
G.openPage=function(M){if(M=="C"){G.isCreate=true;
G.searchtext=undefined
}else{G.isCreate=false
}A();
r()
};
function A(){L();
G.initializeDbType()
}G.employee={};
function L(){G.reload=true;
G.numLimit=10;
G.modelName=[];
G.designationListForWorkAs=[];
G.isEmployeeTerminationConfirmed=false;
G.isEmployeeRelieved=false;
G.isResigned=false;
G.employee={};
G.empName="";
G.empId="";
G.empstatus="";
G.isFranchiseConfigured=true;
G.employee.gender="male";
if(G.employee.isNativeAddressSame==null){G.employee.isNativeAddressSame=true
}G.isCreate=true;
G.submitted=false;
G.isUpdate=false;
G.policyHoldersMap={};
G.tempAddress={};
G.isDepInvalid=true;
G.employeeList=[];
G.notUniqueEmpname=false;
G.isEmailInvalid=false;
G.isWorkEmailValidate=true;
G.joiningDateValidate=true;
G.ipValidate=true;
G.isfamilyNameDuplicate=true;
G.isfamilyContact=true;
G.iseditfamilyNameDuplicate=true;
G.displayEmployeeFlag="view";
G.isInValidSearch=false;
G.educationIndex=0;
G.hasAnyEdu=false;
G.degreeReq=false;
G.edusubmitted=false;
G.employee.edu=[];
G.tepedudata={};
G.employee.edu[G.educationIndex]={};
G.expIndex=0;
G.hasAnyExp=false;
G.prevCmpnyReq=false;
G.expsubmitted=false;
G.employee.exp=[];
G.employee.exp[G.expIndex]={};
G.familyIndex=0;
G.hasAnyFamily=false;
G.familynameReq=false;
G.familysubmitted=false;
G.employee.family=[];
G.employee.family[G.familyIndex]={index:"0"};
G.policyIndex=0;
G.hasAnypolicy=false;
G.policynameReq=false;
G.policysubmitted=false;
G.employee.policy=[];
G.employee.policy[G.policyIndex]={status:"Active",index:"0"};
G.wasHKGEmp=false;
G.shiftList=[];
G.formats=["dd-MMMM-yyyy","yyyy/MM/dd","shortDate","MM/dd/yyyy"];
G.format=l.dateFormat;
G.hasAnyDoc=false;
G.employee.otherdocs=[];
G.employee.otherdocsDate=[];
G.otherDocIndex=0;
$("#input_empReportsTo").select2("data",undefined);
$("#input_empReportsTo").select2("val",undefined);
$("#input_empcurrentaddress").select2("data",undefined);
$("#input_empcurrentaddress").select2("val",undefined);
$("#input_empnativeaddress").select2("data",undefined);
$("#input_empnativeaddress").select2("val",undefined);
G.educationData=[];
G.educationData[G.educationIndex]={index:G.educationIndex};
G.experienceData=[];
G.experienceData[G.expIndex]={index:G.expIndex};
G.familyData=[];
G.familyData[G.familyIndex]={index:G.familyIndex};
G.policyData=[];
G.policyData[G.policyIndex]={index:G.policyIndex};
G.personalData=F.resetSection(G.personalTemplate);
G.generalData=F.resetSection(G.generaltemplate);
G.contactData=F.resetSection(G.contactTemplate);
G.identificationData=F.resetSection(G.identificationTemplate);
G.otherData=F.resetSection(G.otherTemplate);
G.hkgworkData=F.resetSection(G.hkgworkTemplate);
G.tempeducationData=F.resetSection(G.educationTemplate);
G.tempexperienceData=F.resetSection(G.experienceTemplate);
G.tempfamilyData=F.resetSection(G.familyTemplate);
G.temppolicyData=F.resetSection(G.policyTemplate);
if(G.selectedParent!==undefined){G.selectedParent=undefined
}}function J(){if(!angular.isDefined(G.employee.edu)||G.employee.edu==null||G.employee.edu.length==0){G.employee.edu=[]
}if(!angular.isDefined(G.employee.exp)||G.employee.exp==null||G.employee.exp.length==0){G.employee.exp=[]
}if(!angular.isDefined(G.employee.policy)||G.employee.policy==null||G.employee.policy.length==0){G.employee.policy=[]
}if(!angular.isDefined(G.employee.family)||G.employee.family==null||G.employee.family.length==0){G.employee.family=[]
}G.employee.edu[G.educationIndex]={};
G.employee.exp[G.expIndex]={};
G.employee.policy[G.policyIndex]={status:"Active",index:G.policyIndex};
G.employee.family[G.familyIndex]={index:G.familyIndex}
}function q(N){if(N!=null){G.empTypes=[];
if(N.EMPTYPE!=null&&angular.isDefined(N.EMPTYPE)&&N.EMPTYPE.length>0){angular.forEach(N.EMPTYPE,function(O){O.label=l.translateValue("EMPTYPE."+O.label);
if(O.label.length>l.maxValueForTruncate){O.label=O.label.substring(0,l.maxValueForTruncate)+"..."
}G.empTypes.push(O);
G.empTypes=y(G.empTypes,["-isOftenUsed","shortcutCode","value"])
})
}G.bloodGroups=[];
if(N.BG!=null&&angular.isDefined(N.BG)&&N.BG.length>0){angular.forEach(N.BG,function(O){O.label=l.translateValue("BG."+O.label);
if(O.label.length>l.maxValueForTruncate){O.label=O.label.substring(0,l.maxValueForTruncate)+"..."
}G.bloodGroups.push(O);
G.bloodGroups=y(G.bloodGroups,["-isOftenUsed","shortcutCode","value"])
})
}G.maritalStatusList=[];
if(N.MS!=null&&angular.isDefined(N.MS)&&N.MS.length>0){angular.forEach(N.MS,function(O){O.label=l.translateValue("MS."+O.label);
if(O.label.length>l.maxValueForTruncate){O.label=O.label.substring(0,l.maxValueForTruncate)+"..."
}G.maritalStatusList.push(O);
G.maritalStatusList=y(G.maritalStatusList,["-isOftenUsed","shortcutCode","value"])
})
}G.casteList=[];
if(N.CASTE!=null&&angular.isDefined(N.CASTE)&&N.CASTE.length>0){angular.forEach(N.CASTE,function(O){O.label=l.translateValue("CASTE."+O.label);
if(O.label.length>l.maxValueForTruncate){O.label=O.label.substring(0,l.maxValueForTruncate)+"..."
}G.casteList.push(O);
G.casteList=y(G.casteList,["-isOftenUsed","shortcutCode","value"])
})
}G.expDepList=[];
if(N.DEG!=null&&angular.isDefined(N.DEG)&&N.DEG.length>0){angular.forEach(N.DEG,function(O){O.label=l.translateValue("DEG."+O.label);
if(O.label.length>l.maxValueForTruncate){O.label=O.label.substring(0,l.maxValueForTruncate)+"..."
}G.expDepList.push(O);
G.expDepList=y(G.expDepList,["-isOftenUsed","shortcutCode","value"])
})
}G.policyCompanyList=[];
if(N.POLICYCMPNY!=null&&angular.isDefined(N.POLICYCMPNY)&&N.POLICYCMPNY.length>0){angular.forEach(N.POLICYCMPNY,function(O){O.label=l.translateValue("POLICYCMPNY."+O.label);
if(O.label.length>l.maxValueForTruncate){O.label=O.label.substring(0,l.maxValueForTruncate)+"..."
}G.policyCompanyList.push(O);
G.policyCompanyList=y(G.policyCompanyList,["-isOftenUsed","shortcutCode","value"])
})
}G.nationalityMap=[];
if(N.NTNLTY!=null&&angular.isDefined(N.NTNLTY)&&N.NTNLTY.length>0){angular.forEach(N.NTNLTY,function(O){O.label=l.translateValue("NTNLTY."+O.label);
if(O.label.length>l.maxValueForTruncate){O.label=O.label.substring(0,l.maxValueForTruncate)+"..."
}G.nationalityMap.push(O);
G.nationalityMap=y(G.nationalityMap,["-isOftenUsed","shortcutCode","value"])
})
}G.educationDegreeMap=[];
if(N.EDUDEG!=null&&angular.isDefined(N.EDUDEG)&&N.EDUDEG.length>0){angular.forEach(N.EDUDEG,function(O){O.label=l.translateValue("EDUDEG."+O.label);
if(O.label.length>l.maxValueForTruncate){O.label=O.label.substring(0,l.maxValueForTruncate)+"..."
}G.educationDegreeMap.push(O);
G.educationDegreeMap=y(G.educationDegreeMap,["-isOftenUsed","shortcutCode","value"])
})
}G.mediumMap=[];
if(N.MDIUM!=null&&angular.isDefined(N.MDIUM)&&N.MDIUM.length>0){angular.forEach(N.MDIUM,function(O){O.label=l.translateValue("MDIUM."+O.label);
if(O.label.length>l.maxValueForTruncate){O.label=O.label.substring(0,l.maxValueForTruncate)+"..."
}G.mediumMap.push(O);
G.mediumMap=y(G.mediumMap,["-isOftenUsed","shortcutCode","value"])
})
}G.occupationList=[];
if(N.OCCUPSN!=null&&angular.isDefined(N.OCCUPSN)&&N.OCCUPSN.length>0){angular.forEach(N.OCCUPSN,function(O){O.label=l.translateValue("OCCUPSN."+O.label);
if(O.label.length>l.maxValueForTruncate){O.label=O.label.substring(0,l.maxValueForTruncate)+"..."
}G.occupationList.push(O);
G.occupationList=y(G.occupationList,["-isOftenUsed","shortcutCode","value"])
})
}G.relationList=[];
if(N.RELESN!=null&&angular.isDefined(N.RELESN)&&N.RELESN.length>0){angular.forEach(N.RELESN,function(O){O.label=l.translateValue("RELESN."+O.label);
if(O.label.length>l.maxValueForTruncate){O.label=O.label.substring(0,l.maxValueForTruncate)+"..."
}G.relationList.push(O);
G.relationList=y(G.relationList,["-isOftenUsed","shortcutCode","value"])
})
}G.universityList=[];
if(N.UNI!=null&&angular.isDefined(N.UNI)&&N.UNI.length>0){angular.forEach(N.UNI,function(O){O.label=l.translateValue("UNI."+O.label);
if(O.label.length>l.maxValueForTruncate){O.label=O.label.substring(0,l.maxValueForTruncate)+"..."
}G.universityList.push(O);
G.universityList=y(G.universityList,["-isOftenUsed","shortcutCode","value"])
})
}G.otherDetailsEmpMap=[];
if(N.EMPOTHRDTILS!=null&&angular.isDefined(N.EMPOTHRDTILS)&&N.EMPOTHRDTILS.length>0){angular.forEach(N.EMPOTHRDTILS,function(O){O.label=l.translateValue("EMPOTHRDTILS."+O.label);
if(O.label.length>l.maxValueForTruncate){O.label=O.label.substring(0,l.maxValueForTruncate)+"..."
}G.otherDetailsEmpMap.push(O);
G.otherDetailsEmpMap=y(G.otherDetailsEmpMap,["-isOftenUsed","shortcutCode","value"])
})
}if(angular.isDefined(G.otherDetailsEmpMap)&&G.otherDetailsEmpMap!==null&&G.otherDetailsEmpMap!==undefined){for(var M=0;
M<G.otherDetailsEmpMap.length;
M++){G.otherDetailsEmpMap[M].isActive=false
}}G.employee.gender="male";
G.employee.isNativeAddressSame=true;
if(angular.isDefined(G.statusListCreate)&&G.statusList!==null&&G.statusListCreate.length!==0){G.employee.workstatus=G.statusListCreate[0].value
}if(G.empTypes==null||G.bloodGroups==null||G.maritalStatusList==null||G.casteList==null||G.expDepList==null||G.policyCompanyList==null||G.nationalityMap==null||G.educationDegreeMap==null||G.mediumMap==null||G.occupationList==null||G.relationList==null||G.universityList==null||G.otherDetailsEmpMap==null){l.addMessage("Please enter the master values before creating employee",1)
}}}function k(N){G.statusList=N;
G.statusListCreate=[];
if(angular.isDefined(G.statusList)&&G.statusList!==null){for(var M=0;
M<G.statusList.length;
M++){if(G.statusList[M].label==="Selected"||G.statusList[M].label==="Awaiting Decision"){G.statusList[M].label=l.translateValue("EMPLOYEE."+G.statusList[M].label);
G.statusListCreate.push(G.statusList[M])
}}}if(angular.isDefined(G.statusListCreate)&&G.statusListCreate!==null&&G.statusListCreate.length!==0){G.employee.workstatus=G.statusListCreate[0].value
}}G.setDesignation=function(M){console.log("d detail"+JSON.stringify(M));
G.desigIds=[];
angular.forEach(M,function(N){G.desigIds.push(N.value)
});
G.employee.workdeg=angular.copy(G.desigIds)
};
function C(M){if(M!=null&&angular.isDefined(M)&&M.length>0){G.designationList=[];
angular.forEach(M,function(N){N.designationName=l.translateValue("DESIG_NM."+N.displayName);
G.designationList.push(N);
N.modelNumber=N.id;
G.designationListForWorkAs.push(N)
})
}if(!G.designationList||G.designationList===null){G.designationList=[]
}}function E(M){G.allLocations=M.sort(G.predicateBy("label"));
angular.forEach(G.allLocations,function(N){G.select2Locations.push({id:N.value,text:N.label})
});
$("#location").select2("val",null)
}function B(M){G.departmentList=[];
if(M!=null&&angular.isDefined(M)&&M.length>0){angular.forEach(M,function(N){G.departmentList.push(N)
})
}G.departmentListDropdown=[];
$.merge(G.departmentListDropdown,angular.copy(G.departmentList));
if(G.selectedParent!==undefined){G.selectedParent=undefined
}}function p(){var M=F.retrieveSectionWiseCustomFieldInfo("manageEmployees");
M.then(function(N){G.customPersonalTemplateDate=angular.copy(N.PERSONAL);
G.personalTemplate=l.getCustomDataInSequence(G.customPersonalTemplateDate);
G.addDateCustomField(G.personalTemplate,"PERSONAL");
G.customContactTemplateDate=angular.copy(N.CONTACT);
G.contactTemplate=l.getCustomDataInSequence(G.customContactTemplateDate);
G.addDateCustomField(G.contactTemplate,"CONTACT");
G.customIdentificationTemplateDate=angular.copy(N.IDENTIFICATION);
G.identificationTemplate=l.getCustomDataInSequence(G.customIdentificationTemplateDate);
G.addDateCustomField(G.identificationTemplate,"IDENTIFICATION");
G.customOtherTemplateDate=angular.copy(N.OTHER);
G.otherTemplate=l.getCustomDataInSequence(G.customOtherTemplateDate);
G.addDateCustomField(G.otherTemplate,"OTHER");
G.customWorkTemplateDate=angular.copy(N.HKGWORK);
G.hkgworkTemplate=l.getCustomDataInSequence(G.customWorkTemplateDate);
G.addDateCustomField(G.hkgworkTemplate,"HKGWORK");
G.customEducationTemplateDate=angular.copy(N.EDUCATION);
G.educationTemplate=l.getCustomDataInSequence(G.customEducationTemplateDate);
G.addDateCustomField(G.educationTemplate,"EDUCATION");
G.customExperienceTemplateDate=angular.copy(N.EXPERIENCE);
G.experienceTemplate=l.getCustomDataInSequence(G.customExperienceTemplateDate);
G.addDateCustomField(G.experienceTemplate,"EXPERIENCE");
G.customFamilyTemplateDate=angular.copy(N.FAMILY);
G.familyTemplate=l.getCustomDataInSequence(G.customFamilyTemplateDate);
G.addDateCustomField(G.familyTemplate,"FAMILY");
G.customPolicyTemplateDate=angular.copy(N.POLICY);
G.policyTemplate=l.getCustomDataInSequence(G.customPolicyTemplateDate);
G.addDateCustomField(G.policyTemplate,"POLICY");
G.customGeneralTemplateDate=angular.copy(N.genralSection);
G.generaltemplate=l.getCustomDataInSequence(G.customGeneralTemplateDate);
G.addDateCustomField(G.generaltemplate,"genralSection")
},function(N){},function(N){})
}G.addDateCustomField=function(N,M){if(N!==null&&N!==undefined){angular.forEach(N,function(O){if(O.type!==null&&O.type!==undefined&&O.type==="date"){if(!G.listOfModelsOfDateType[M]){G.listOfModelsOfDateType[M]=[]
}G.listOfModelsOfDateType[M].push(O.model)
}})
}};
G.checkIsUpdate=function(){G.isUpdate=true
};
G.retrieveAvatar=function(){return l.appendAuthToken(l.apipath+"employee/retrieve/avatar?decache="+l.randomCount)
};
G.setPassportExpiryDate=function(){var N=G.employee.empPassportIssueOn.getFullYear()+10;
G.expiry=angular.copy(G.employee.empPassportIssueOn);
var M=G.expiry.setFullYear(N)
};
G.changeholdername=function(){if(!angular.isDefined(G.employee.empName)){delete G.policyHoldersMap.E0
}else{G.policyHoldersMap.E0=G.employee.empName
}};
G.predicateBy=function(M){return function(O,N){if(O[M]>N[M]){return 1
}else{if(O[M]<N[M]){return -1
}}return 0
}
};
G.multiLocations={multiple:false,closeOnSelect:false,placeholder:"Select District",allowClear:true,data:function(){return{results:G.select2Locations}
}};
G.$watch("employee.nativeaddress",function(M){if(M!==undefined&&M!==null){if(M instanceof Object){G.employee.nativeaddress=M.id
}}});
G.multiLocations1={multiple:false,closeOnSelect:false,placeholder:"Select District",allowClear:true,initSelection:function(M,O){if(G.employee.nativeaddress!==undefined&&G.employee.nativeaddress!==null&&G.employee.nativeaddress.length>0){var N={};
angular.forEach(G.select2Locations,function(P){if(G.employee.nativeaddress instanceof Object===false){if(P.id===G.employee.nativeaddress){N=angular.copy(P)
}}else{if(P.id===G.employee.nativeaddress.id){N=angular.copy(P)
}}});
O(N)
}},data:function(){return{results:G.select2Locations}
}};
G.doesEmployeeNameExist=function(M){G.invalidname=true;
if(M&&M.length>0){w.doesEmployeeNameExist(M,function(O){if(O.data){if(!G.isCreate&&M!=G.oldname){G.notUniqueEmpname=true
}if(G.isCreate){G.notUniqueEmpname=true
}}else{G.notUniqueEmpname=false;
var N=M.trim().split(" ");
if(N.length==3){G.invalidname=false
}else{G.invalidname=true
}}},function(){})
}else{G.notUniqueEmpname=false
}};
G.empNameFormat=function(M){if(M&&M.length>0){var N=M.trim().split(" ");
if(N.length==3){G.invalidname=false
}else{G.invalidname=true
}}};
G.autoCompleteApprover={multiple:true,closeOnSelect:false,allowClear:true,placeholder:"Select approvers",maximumSelectionSize:1,initSelection:function(M,N){},formatResult:function(M){return M.text
},formatSelection:function(M){return M.text
},query:function(P){var O=P.term;
G.names=[];
var Q=function(R){if(R.length==0){P.callback({results:G.names})
}else{G.names=R;
angular.forEach(R,function(S){G.names.push({id:S.value+":"+S.description,text:S.label})
});
P.callback({results:G.names})
}};
var M=function(){};
if(O.substring(0,2)=="@E"||O.substring(0,2)=="@e"){var N=P.term.slice(2);
u.retrieveUserList(N.trim(),Q,M)
}else{if(O.substring(0,2)=="@D"||O.substring(0,2)=="@d"){var N=P.term.slice(2);
u.retrieveDepartmentList(N.trim(),Q,M)
}else{if(O.length>0){var N=O;
u.retrieveUserList(N.trim(),Q,M)
}else{P.callback({results:G.names})
}}}}};
function t(N,O){if(N===null||N===undefined){return
}if(N.id===O){G.selectedDepObj=N;
return
}if(N.children===null||N.children.length===0){return
}else{for(var M=0;
M<N.children.length>0;
M++){t(N.children[M],O)
}}}G.setSelectedParent=function(M){G.isDepInvalid=true;
if(!angular.equals(M,{})){G.searchDepId=M.id;
G.invalidParent=false;
G.selectedParent=M.currentNode;
G.parentId=M.currentNode.id;
G.isDepInvalid=false;
M.currentNode.displayName=l.translateValue("DPT_NM."+M.currentNode.displayName);
G.setShitsByDep(G.parentId)
}};
G.setShitsByDep=function(M){G.shiftList=[];
w.retrieveShiftDepMap({primaryKey:M},function(N){if(N!=null&&angular.isDefined(N)&&N.length>0){angular.forEach(N,function(O){O.label=l.translateValue("SHIFT_NM."+O.label);
G.shiftList.push(O)
})
}})
};
G.scrollTo=function(V){var T=R();
var M=W(V);
var N=M>T?M-T:T-M;
if(N<100){scrollTo(0,M-60);
return
}var Q=Math.round(N/100);
if(Q>=20){Q=20
}var P=Math.round(N/25);
var U=M>T?T+P:T-P;
var O=0;
if(M>T){for(var S=T;
S<M;
S+=P){setTimeout("window.scrollTo(0, "+(U-60)+")",O*Q);
U+=P;
if(U>M){U=M
}O++
}return
}for(var S=T;
S>M;
S-=P){setTimeout("window.scrollTo(0, "+(U-60)+")",O*Q);
U-=P;
if(U<M){U=M
}O++
}function R(){if(self.pageYOffset){return self.pageYOffset
}if(document.documentElement&&document.documentElement.scrollTop){return document.documentElement.scrollTop
}if(document.body.scrollTop){return document.body.scrollTop
}return 0
}function W(X){var aa=document.getElementById(X);
var Z=aa.offsetTop;
var Y=aa;
while(Y.offsetParent&&Y.offsetParent!=document.body){Y=Y.offsetParent;
Z+=Y.offsetTop
}return Z
}};
G.phoneValidate=function(){if(angular.isDefined(G.employee.phnno)&&G.employee.phnno!=null){if(G.employee.phnno.length>=10){if(!I(G.employee.phnno)){G.isPhoneValidate=false;
return
}else{G.isPhoneValidate=true
}}else{G.isPhoneValidate=false;
return
}}};
G.familyPhoneValidate=function(M){if(!I(M)){G.isfamilyContact=false;
return
}else{G.isfamilyContact=true
}};
G.altphoneValidate=function(){if(angular.isDefined(G.employee.altphnno)){if(G.employee.altphnno.length>=10){if(!I(G.employee.altphnno)){G.isAltPhoneInvalid=true;
return
}else{G.isAltPhoneInvalid=false
}}else{if(G.employee.altphnno.length==0){G.isAltPhoneInvalid=false;
return
}else{G.isAltPhoneInvalid=true;
return
}}}};
function I(M){var N=/^(\+91-|\+91|0|\0)?\d{10}$/;
M=M.replace(/ /g,"");
if(!N.test(M)){return false
}else{return true
}}G.retrieveTransferredEmployee=function(M){G.isCreate=true;
G.reload=false;
G.invalidname=false;
G.notUniqueEmpname=false;
G.invalidname=false;
G.searchtext=undefined;
G.isfamilyContact=true;
G.displayEmployeeFlag="view";
l.maskLoading();
w.retrieveByIdForTransfer({primaryKey:M},function(T){console.log("data :::"+JSON.stringify(T));
if(G.empTypes!=null){G.tempListForEmployeetypes=angular.copy(G.empTypes);
G.editempTypes=[];
var S;
if(T.empType==null){T.empType=G.tempListForEmployeetypes[0].value;
S=G.tempListForEmployeetypes[0].value
}for(var R=0;
R<G.tempListForEmployeetypes.length;
R++){if(G.tempListForEmployeetypes[R].value==T.empType){S=G.tempListForEmployeetypes[R].shortcutCode;
break
}}for(var R=0;
R<G.tempListForEmployeetypes.length;
R++){if(G.tempListForEmployeetypes[R].shortcutCode<=S){G.editempTypes.push(G.tempListForEmployeetypes[R])
}}}G.oldNativeId=angular.copy(T.nativeAddressId);
if(!T.isNativeAddressSame){G.employee.isNativeAddressSame=false
}else{G.employee.isNativeAddressSame=true
}if(T.nativepincode===null){T.nativepincode=undefined
}if(T.altphnno===null){T.altphnno=undefined
}if(angular.isDefined(G.otherDetailsEmpMap)&&G.otherDetailsEmpMap!==null){for(var R=0;
R<G.otherDetailsEmpMap.length;
R++){G.otherDetailsEmpMap[R].isActive=false
}}if(G.otherDetailsEmpMap!==undefined&&G.otherDetailsEmpMap!==null&&T.otherDetailsOfEmployeeKeysString!==undefined&&T.otherDetailsOfEmployeeKeysString!==null){var O=T.otherDetailsOfEmployeeKeysString.split(",");
for(var Q=0;
Q<O.length;
Q++){for(var R=0;
R<G.otherDetailsEmpMap.length;
R++){var N="#"+G.otherDetailsEmpMap[R].value+"#";
if(O[Q]===N){G.otherDetailsEmpMap[R].isActive=true;
break
}}}}G.policyHoldersMap.E0=T.empName;
for(var R=0;
R<T.family.length;
R++){G.policyHoldersMap["F"+R]=T.family[R].firstName
}if(angular.isDefined(T.edu)&&T.edu.length>0){G.hasAnyEdu=true
}else{G.hasAnyEdu=false
}if(angular.isDefined(T.exp)&&T.exp.length>0){G.hasAnyExp=true
}else{G.hasAnyExp=false
}if(angular.isDefined(T.family)&&T.family.length>0){G.hasAnyFamily=true
}else{G.hasAnyFamily=false
}if(angular.isDefined(T.policy)&&T.policy.length>0){G.hasAnyPolicy=true
}else{G.hasAnyPolicy=false
}if(angular.isDefined(T.otherdocs)&&T.otherdocs.length>0){G.hasAnyDoc=true
}else{G.hasAnyDoc=false
}$("#multiLocations").select2("data",T.currentaddress);
angular.forEach(G.select2Locations,function(V){if(V.id===T.currentaddress){T.currentaddress=V
}});
$("#multiLocations1").select2("data",T.nativeaddress);
angular.forEach(G.select2Locations,function(V){if(V.id===T.nativeaddress){T.nativeaddress=V
}});
$("#input_empReportsTo").select2("data","");
p();
if(angular.isDefined(T.personalCustom)&&T.personalCustom!=null){G.personalData=angular.copy(T.personalCustom);
if(!!G.personalData&&!!G.listOfModelsOfDateType.PERSONAL){angular.forEach(G.listOfModelsOfDateType.PERSONAL,function(V){if(G.personalData.hasOwnProperty(V)){if(G.personalData[V]!==null&&G.personalData[V]!==undefined){G.personalData[V]=new Date(G.personalData[V])
}else{G.personalData[V]=""
}}})
}}if(angular.isDefined(T.generalCustom)&&T.generalCustom!=null){G.generalData=angular.copy(T.generalCustom);
if(!!G.generalData&&!!G.listOfModelsOfDateType.genralSection){angular.forEach(G.listOfModelsOfDateType.genralSection,function(V){if(G.generalData.hasOwnProperty(V)){if(G.generalData[V]!==null&&G.generalData[V]!==undefined){G.generalData[V]=new Date(G.generalData[V])
}else{G.generalData[V]=""
}}})
}}if(angular.isDefined(T.contactCustom)&&T.contactCustom!=null){G.contactData=angular.copy(T.contactCustom);
if(!!G.contactData&&!!G.listOfModelsOfDateType.CONTACT){angular.forEach(G.listOfModelsOfDateType.CONTACT,function(V){if(G.contactData.hasOwnProperty(V)){if(G.contactData[V]!==null&&G.contactData[V]!==undefined){G.contactData[V]=new Date(G.contactData[V])
}else{G.contactData[V]=""
}}})
}}if(angular.isDefined(T.identificationCustom)&&T.identificationCustom!=null){G.identificationData=angular.copy(T.identificationCustom);
if(!!G.identificationData&&!!G.listOfModelsOfDateType.IDENTIFICATION){angular.forEach(G.listOfModelsOfDateType.IDENTIFICATION,function(V){if(G.identificationData.hasOwnProperty(V)){if(G.identificationData[V]!==null&&G.identificationData[V]!==undefined){G.identificationData[V]=new Date(G.identificationData[V])
}else{G.identificationData[V]=""
}}})
}}if(angular.isDefined(T.otherCustom)&&T.otherCustom!=null){G.otherData=angular.copy(T.otherCustom);
if(!!G.otherData&&!!G.listOfModelsOfDateType.OTHER){angular.forEach(G.listOfModelsOfDateType.OTHER,function(V){if(G.otherData.hasOwnProperty(V)){if(G.otherData[V]!==null&&G.otherData[V]!==undefined){G.otherData[V]=new Date(G.otherData[V])
}else{G.otherData[V]=""
}}})
}}G.reload=true;
G.educationIndex=T.edu.length;
G.expIndex=T.exp.length;
G.familyIndex=T.family.length;
G.policyIndex=T.policy.length;
G.otherDocIndex=T.otherdocs.length;
for(var R=0;
R<T.edu.length;
R++){if(angular.isDefined(T.edu[R].educationCustom)&&T.edu[R].educationCustom!=null){G.educationData[R]=angular.copy(T.edu[R].educationCustom)
}else{G.educationData[R]={}
}G.educationdbType=angular.copy(T.edu[R].educationDbType)
}for(var R=0;
R<T.exp.length;
R++){T.exp[R].startedFrom=new Date(T.exp[R].startedFrom);
T.exp[R].workedTill=new Date(T.exp[R].workedTill);
if(angular.isDefined(T.exp[R].experienceCustom)&&T.exp[R].experienceCustom!=null){G.experienceData[R]=angular.copy(T.exp[R].experienceCustom)
}else{G.experienceData[R]={}
}G.experiencedbType=angular.copy(T.exp[R].experienceDbType)
}for(var R=0;
R<T.family.length;
R++){T.family[R].dateOfBirth=new Date(T.family[R].dateOfBirth);
if(angular.isDefined(T.family[R].familyCustom)&&T.family[R].familyCustom!=null){G.familyData[R]=angular.copy(T.family[R].familyCustom)
}else{G.familyData[R]={index:G.familyIndex}
}G.familydbType=angular.copy(T.family[R].familyDbType)
}for(var R=0;
R<T.policy.length;
R++){if(angular.isDefined(T.policy[R].policyCustom)&&T.policy[R].policyCustom!=null){G.policyData[R]=angular.copy(T.policy[R].policyCustom)
}else{G.policyData[R]={}
}G.policydbType=angular.copy(T.policy[R].policyDbType)
}T.dob=new Date(T.dob);
T.joiningDate=new Date(T.joiningDate);
if(T.empPucExpiresOn!=null){T.empPucExpiresOn=new Date(T.empPucExpiresOn)
}if(T.empPassportIssueOn!=null){T.empPassportIssueOn=new Date(T.empPassportIssueOn)
}if(T.empPassportExpiresOn!=null){T.empPassportExpiresOn=new Date(T.empPassportExpiresOn)
}G.employee=angular.copy(T);
G.employee.custom5=T.id;
G.employee.employeeCode=undefined;
G.employee.joiningDate=undefined;
G.employee.workemailId=undefined;
G.employee.workstatus=undefined;
G.employee.workdeg=undefined;
G.employee.reportsToId=undefined;
G.employee.reportsToName=undefined;
G.employee.workshift=undefined;
G.employee.ipaddress=undefined;
G.employee.id=undefined;
G.employee.currentAddressId=undefined;
G.employee.nativeAddressId=undefined;
G.employee.departmentId=undefined;
G.employee.selecteddep=undefined;
G.employee.relievingDate=undefined;
for(var R=0;
R<G.employee.exp.length;
R++){if(angular.isDefined(G.employee.exp[R])&&G.employee.exp[R]!=null){G.employee.exp[R].id=undefined
}}for(var R=0;
R<G.employee.edu.length;
R++){if(angular.isDefined(G.employee.edu[R])&&G.employee.edu[R]!=null){G.employee.edu[R].id=undefined
}}for(var R=0;
R<G.employee.policy.length;
R++){if(angular.isDefined(G.employee.policy[R])&&G.employee.policy[R]!=null){G.employee.policy[R].id=undefined
}}G.viewAllFamilyDetails();
G.empName=T.empName;
J();
var P=new Date(G.employee.dob);
var U=P.getYear()+1900;
if(G.employee.gender==null){G.employee.gender="male"
}G.phoneValidate();
G.oldname=G.employee.empName;
G.nativevalid=false;
if(G.nativeValid=false&&angular.isUndefined(G.employee.nativeaddress)&&G.employee.nativeaddress==null&&angular.isUnDefined(G.employee.nativefulladdress)&&G.employee.nativefulladdress==null&&angular.isUnDefined(G.employee.nativepincode)&&G.employee.nativepincode==null){G.nativevalid=true
}l.unMaskLoading();
G.makeAScroll()
},function(){l.unMaskLoading();
G.makeAScroll()
})
};
G.retrieveEmployeeById=function(M){G.reload=false;
G.invalidname=false;
G.notUniqueEmpname=false;
G.invalidname=false;
G.isCreate=false;
G.searchtext=undefined;
G.isfamilyContact=true;
G.displayEmployeeFlag="view";
l.maskLoading();
w.retrieveEmployeeDetail({primaryKey:M},function(Q){if(!Q.isUserRoleHigher){j.path("/accessdenied")
}if(G.empTypes!=null){G.tempListForEmployeetypes=angular.copy(G.empTypes);
G.editempTypes=[];
var O;
if(Q.empType==null){Q.empType=G.tempListForEmployeetypes[0].value;
O=G.tempListForEmployeetypes[0].value
}for(var R=0;
R<G.tempListForEmployeetypes.length;
R++){if(G.tempListForEmployeetypes[R].value==Q.empType){O=G.tempListForEmployeetypes[R].shortcutCode;
break
}}for(var R=0;
R<G.tempListForEmployeetypes.length;
R++){if(G.tempListForEmployeetypes[R].shortcutCode<=O){G.editempTypes.push(G.tempListForEmployeetypes[R])
}}}G.oldNativeId=angular.copy(Q.nativeAddressId);
if(!Q.isNativeAddressSame){G.employee.isNativeAddressSame=false
}else{G.employee.isNativeAddressSame=true
}if(Q.nativepincode===null){Q.nativepincode=undefined
}if(Q.altphnno===null){Q.altphnno=undefined
}if(angular.isDefined(G.otherDetailsEmpMap)&&G.otherDetailsEmpMap!==null){for(var R=0;
R<G.otherDetailsEmpMap.length;
R++){G.otherDetailsEmpMap[R].isActive=false
}}if(G.otherDetailsEmpMap!==undefined&&G.otherDetailsEmpMap!==null&&Q.otherDetailsOfEmployeeKeysString!==undefined&&Q.otherDetailsOfEmployeeKeysString!==null){var V=Q.otherDetailsOfEmployeeKeysString.split(",");
for(var P=0;
P<V.length;
P++){for(var R=0;
R<G.otherDetailsEmpMap.length;
R++){var T="#"+G.otherDetailsEmpMap[R].value+"#";
if(V[P]===T){G.otherDetailsEmpMap[R].isActive=true;
break
}}}}if(G.departmentList!==null&&G.departmentList!==undefined){for(var R=0;
R<G.departmentList.length;
R++){t(G.departmentList[R],Q.selecteddep)
}}G.policyHoldersMap.E0=Q.empName;
for(var R=0;
R<Q.family.length;
R++){G.policyHoldersMap["F"+R]=Q.family[R].firstName
}if(angular.isDefined(Q.edu)&&Q.edu.length>0){G.hasAnyEdu=true
}else{G.hasAnyEdu=false
}if(angular.isDefined(Q.exp)&&Q.exp.length>0){G.hasAnyExp=true
}else{G.hasAnyExp=false
}if(angular.isDefined(Q.family)&&Q.family.length>0){G.hasAnyFamily=true
}else{G.hasAnyFamily=false
}if(angular.isDefined(Q.policy)&&Q.policy.length>0){G.hasAnyPolicy=true
}else{G.hasAnyPolicy=false
}if(angular.isDefined(Q.otherdocs)&&Q.otherdocs.length>0){G.hasAnyDoc=true
}else{G.hasAnyDoc=false
}G.invalidParent=false;
G.isDepInvalid=false;
G.selectedParent=angular.copy(G.selectedDepObj);
console.log("parent"+JSON.stringify(G.selectedParent));
if(angular.isDefined(G.selectedParent)&&G.selectedParent!==undefined){G.selectedParent.displayName=l.translateValue("DPT_NM."+G.selectedParent.displayName)
}if(G.selectedDepObj!=undefined){G.parentId=G.selectedDepObj.id
}else{G.isDepInvalid=true
}$("#multiLocations").select2("data",Q.currentaddress);
angular.forEach(G.select2Locations,function(W){if(W.id===Q.currentaddress){Q.currentaddress=W
}});
$("#multiLocations1").select2("data",Q.nativeaddress);
angular.forEach(G.select2Locations,function(W){if(W.id===Q.nativeaddress){Q.nativeaddress=W
}});
$("#input_empReportsTo").select2("data","");
if(Q.reportsToId!=null){G.names={id:Q.reportsToId,text:Q.reportsToName};
$("#input_empReportsTo").select2("data",G.names)
}if(G.selectedDepObj!=undefined){G.setShitsByDep(G.selectedDepObj.id)
}p();
if(angular.isDefined(Q.personalCustom)&&Q.personalCustom!=null){G.personalData=angular.copy(Q.personalCustom);
if(!!G.personalData&&!!G.listOfModelsOfDateType.PERSONAL){angular.forEach(G.listOfModelsOfDateType.PERSONAL,function(W){if(G.personalData.hasOwnProperty(W)){if(G.personalData[W]!==null&&G.personalData[W]!==undefined){G.personalData[W]=new Date(G.personalData[W])
}else{G.personalData[W]=""
}}})
}}if(angular.isDefined(Q.generalCustom)&&Q.generalCustom!=null){G.generalData=angular.copy(Q.generalCustom);
if(!!G.generalData&&!!G.listOfModelsOfDateType.genralSection){angular.forEach(G.listOfModelsOfDateType.genralSection,function(W){if(G.generalData.hasOwnProperty(W)){if(G.generalData[W]!==null&&G.generalData[W]!==undefined){G.generalData[W]=new Date(G.generalData[W])
}else{G.generalData[W]=""
}}})
}}if(angular.isDefined(Q.contactCustom)&&Q.contactCustom!=null){G.contactData=angular.copy(Q.contactCustom);
if(!!G.contactData&&!!G.listOfModelsOfDateType.CONTACT){angular.forEach(G.listOfModelsOfDateType.CONTACT,function(W){if(G.contactData.hasOwnProperty(W)){if(G.contactData[W]!==null&&G.contactData[W]!==undefined){G.contactData[W]=new Date(G.contactData[W])
}else{G.contactData[W]=""
}}})
}}if(angular.isDefined(Q.identificationCustom)&&Q.identificationCustom!=null){G.identificationData=angular.copy(Q.identificationCustom);
if(!!G.identificationData&&!!G.listOfModelsOfDateType.IDENTIFICATION){angular.forEach(G.listOfModelsOfDateType.IDENTIFICATION,function(W){if(G.identificationData.hasOwnProperty(W)){if(G.identificationData[W]!==null&&G.identificationData[W]!==undefined){G.identificationData[W]=new Date(G.identificationData[W])
}else{G.identificationData[W]=""
}}})
}}if(angular.isDefined(Q.otherCustom)&&Q.otherCustom!=null){G.otherData=angular.copy(Q.otherCustom);
if(!!G.otherData&&!!G.listOfModelsOfDateType.OTHER){angular.forEach(G.listOfModelsOfDateType.OTHER,function(W){if(G.otherData.hasOwnProperty(W)){if(G.otherData[W]!==null&&G.otherData[W]!==undefined){G.otherData[W]=new Date(G.otherData[W])
}else{G.otherData[W]=""
}}})
}}if(angular.isDefined(Q.hkgworkCustom)&&Q.hkgworkCustom!=null){G.hkgworkData=angular.copy(Q.hkgworkCustom);
if(!!G.hkgworkData&&!!G.listOfModelsOfDateType.HKGWORK){angular.forEach(G.listOfModelsOfDateType.HKGWORK,function(W){if(G.hkgworkData.hasOwnProperty(W)){if(G.hkgworkData[W]!==null&&G.hkgworkData[W]!==undefined){G.hkgworkData[W]=new Date(G.hkgworkData[W])
}else{G.hkgworkData[W]=""
}}})
}}G.reload=true;
G.educationIndex=Q.edu.length;
G.expIndex=Q.exp.length;
G.familyIndex=Q.family.length;
G.policyIndex=Q.policy.length;
G.otherDocIndex=Q.otherdocs.length;
for(var R=0;
R<Q.edu.length;
R++){if(angular.isDefined(Q.edu[R].educationCustom)&&Q.edu[R].educationCustom!=null){G.educationData[R]=angular.copy(Q.edu[R].educationCustom)
}else{G.educationData[R]={}
}G.educationdbType=angular.copy(Q.edu[R].educationDbType)
}for(var R=0;
R<Q.exp.length;
R++){Q.exp[R].startedFrom=new Date(Q.exp[R].startedFrom);
Q.exp[R].workedTill=new Date(Q.exp[R].workedTill);
if(angular.isDefined(Q.exp[R].experienceCustom)&&Q.exp[R].experienceCustom!=null){G.experienceData[R]=angular.copy(Q.exp[R].experienceCustom)
}else{G.experienceData[R]={}
}G.experiencedbType=angular.copy(Q.exp[R].experienceDbType)
}for(var R=0;
R<Q.family.length;
R++){Q.family[R].dateOfBirth=new Date(Q.family[R].dateOfBirth);
if(angular.isDefined(Q.family[R].familyCustom)&&Q.family[R].familyCustom!=null){G.familyData[R]=angular.copy(Q.family[R].familyCustom)
}else{G.familyData[R]={index:G.familyIndex}
}G.familydbType=angular.copy(Q.family[R].familyDbType)
}for(var R=0;
R<Q.policy.length;
R++){if(angular.isDefined(Q.policy[R].policyCustom)&&Q.policy[R].policyCustom!=null){G.policyData[R]=angular.copy(Q.policy[R].policyCustom)
}else{G.policyData[R]={}
}G.policydbType=angular.copy(Q.policy[R].policyDbType)
}Q.dob=new Date(Q.dob);
Q.joiningDate=new Date(Q.joiningDate);
if(Q.empPucExpiresOn!=null){Q.empPucExpiresOn=new Date(Q.empPucExpiresOn)
}if(Q.empPassportIssueOn!=null){Q.empPassportIssueOn=new Date(Q.empPassportIssueOn)
}if(Q.empPassportExpiresOn!=null){Q.empPassportExpiresOn=new Date(Q.empPassportExpiresOn)
}if(Q.relievingDate!=null){Q.relievingDate=new Date(Q.relievingDate)
}if(Q.workstatus==3){G.isResigned=true
}else{G.isResigned=false
}G.employee=angular.copy(Q);
G.todayDate=new Date(G.today).setHours(0,0,0,0);
if(G.employee.relievingDate!=null&&Q.workstatus==3){G.employee.relievingDateWithoutTime=new Date(G.employee.relievingDate).setHours(0,0,0,0);
if((angular.equals(G.employee.relievingDateWithoutTime,G.todayDate))||(G.employee.relievingDateWithoutTime<G.todayDate)){G.isEmployeeRelieved=true
}else{G.isEmployeeRelieved=false
}}else{G.isEmployeeRelieved=false
}G.viewAllFamilyDetails();
G.empName=Q.empName;
G.empId=Q.userId;
G.empstatus=Q.workstatus;
J();
var N=new Date(G.employee.dob);
var U=N.getYear()+1900;
if(G.employee.gender==null){G.employee.gender="male"
}if(G.employee.workstatus==null&&G.statusList!==null&&G.statusList!==undefined){G.employee.workstatus=G.statusList[0].value
}G.phoneValidate();
G.oldname=G.employee.empName;
if(!G.isCreate){console.log("inside");
G.designationList=[];
G.designationListForWorkAs=[];
var S;
if(G.selectedParent.parentId!==null&&G.selectedParent.parentId!==undefined&&G.selectedParent.parentId!==0){S=G.selectedParent.parentId
}else{S=G.selectedParent.id
}w.retrieveDesgByDept(S,function(W){angular.forEach(W,function(X){X.designationName=l.translateValue("DESIG_NM."+X.label);
G.designationList.push(X);
X.modelName=X.value;
G.designationListForWorkAs.push(X)
});
G.modelName=[];
if(G.designationListForWorkAs!==null&&G.designationListForWorkAs!==undefined){angular.forEach(G.designationListForWorkAs,function(Y){var X=D("filter")(G.employee.workdeg,function(Z){return Y.value.toString()===Z.toString()
})[0];
if(X!==null&&X!==undefined){Y.ticked=true;
G.modelName.push(Y)
}})
}})
}G.nativevalid=false;
if(G.nativeValid=false&&angular.isUndefined(G.employee.nativeaddress)&&G.employee.nativeaddress==null&&angular.isUnDefined(G.employee.nativefulladdress)&&G.employee.nativefulladdress==null&&angular.isUnDefined(G.employee.nativepincode)&&G.employee.nativepincode==null){G.nativevalid=true
}l.unMaskLoading();
G.makeAScroll()
},function(){l.unMaskLoading();
G.makeAScroll()
})
};
function H(){G.nativevalid=false;
if(angular.isDefined(G.employee.nativeaddress)&&G.employee.nativeaddress!=null&&G.employee.nativeaddress.toString().length>0&&angular.isDefined(G.employee.nativefulladdress)&&G.employee.nativefulladdress!=null&&G.employee.nativefulladdress.toString().length>0&&angular.isDefined(G.employee.nativepincode)&&G.employee.nativepincode!=null&&G.employee.nativepincode.toString().length>0){G.nativevalid=true
}}G.updateEmployee=function(M){if(G.isResigned){G.terminateEmpModal=s.open({templateUrl:"terminateEmpTmpl.html",scope:G,size:"lg"})
}else{G.addEmployee(M)
}};
G.addEmployee=function(N){console.log("form :"+JSON.stringify(N));
G.reload=false;
G.submitted=true;
G.eduAddAnother(true,false);
G.expAddAnother(true);
G.familyAddAnother(true);
G.policyAddAnother(true);
if(!G.employee.isNativeAddressSame){H()
}else{G.nativevalid=true
}var M=true;
for(var S in N){if(S!=="passwordPopUpForm"&&S!=="editMasterForm"){if(N[S].$invalid){M=false;
break
}}}var O=false;
if(!G.isDepInvalid&&!G.invalidname&&!G.notUniqueEmpname&&G.isPhoneValidate&&!G.isAltPhoneInvalid&&!G.isEmailInvalid&&G.isWorkEmailValidate&&G.isfamilyNameDuplicate&&!G.degreeReq&&!G.prevCmpnyReq&&!G.familynameReq&&!G.policynameReq&&G.ipValidate){O=true
}console.log("valid :::"+M);
console.log("allconditionStatisfied :::"+O);
if(M&&O){var T=function(){G.familyreload=false;
G.generalreload=false;
G.personalreload=false;
G.contactreload=false;
G.identificationreload=false;
G.tempeducationflag=false;
G.tempexperienceflag=false;
G.temppolicyflag=false;
G.otherDataflag=false;
G.hkgworkDataflag=false;
G.resetCustomFields(null);
G.reload=true;
if(N!==undefined&&N!==null){N.$setPristine()
}L();
G.searchtext=undefined;
G.scrollTo("mainPanel");
G.otherDocIndex=0;
if(angular.isDefined(G.otherDetailsEmpMap)&&G.otherDetailsEmpMap!==null){for(var V=0;
V<G.otherDetailsEmpMap.length;
V++){G.otherDetailsEmpMap[V].isActive=false
}}if(!G.isCreate){l.randomCount=Math.random()
}angular.forEach(G.designationList,function(W){W.ticked=false
});
G.designationListForWorkAs=angular.copy(G.designationList);
l.unMaskLoading()
};
var P=function(){p();
G.reload=true;
l.unMaskLoading()
};
var U="";
angular.forEach(G.otherDetailsEmpMap,function(V){if(V.isActive){if(U.length>0){U=U+","
}U=U+"#"+V.value+"#"
}});
var R=angular.copy(G.employee);
R.otherDetailsOfEmployeeKeysString=U;
R.selecteddep=G.parentId;
R.personalCustom=G.personalData;
R.generalCustom=G.generalData;
R.personalDbType=G.personaldbType;
R.generalDbType=G.generaldbType;
R.contactCustom=G.contactData;
R.contactDbType=G.contactdbType;
R.identificationCustom=G.identificationData;
R.identificationDbType=G.identificationdbType;
R.otherCustom=G.otherData;
R.otherDbType=G.otherdbType;
R.hkgworkCustom=G.hkgworkData;
R.hkgworkDbType=G.hkgworkdbType;
for(var Q=0;
Q<R.edu.length;
Q++){if(R.edu[Q]!=null&&angular.isDefined(G.educationData[Q])&&G.educationData[Q]!=="undefined"){G.educationData[Q].index=undefined;
R.edu[Q].educationCustom=angular.copy(G.educationData[Q]);
R.edu[Q].educationDbType=angular.copy(G.educationdbType)
}if(R.edu[Q]!=null&&angular.isDefined(G.editeducationData)&&G.editeducationData!=="undefined"){R.edu[Q].educationCustom=angular.copy(G.editeducationData);
R.edu[Q].educationDbType=angular.copy(G.editeducationdbType)
}}for(var Q=0;
Q<R.exp.length;
Q++){if(R.exp[Q]!=null&&angular.isDefined(G.experienceData[Q])&&G.experienceData[Q]!=="undefined"){G.experienceData[Q].index=undefined;
R.exp[Q].experienceCustom=angular.copy(G.experienceData[Q]);
R.exp[Q].experienceDbType=angular.copy(G.experiencedbType)
}if(R.exp[Q]!=null&&angular.isDefined(G.editexperienceData)&&G.editexperienceData!=="undefined"){R.exp[Q].experienceCustom=angular.copy(G.editexperienceData);
R.exp[Q].experienceDbType=angular.copy(G.editexperiencedbType)
}}for(var Q=0;
Q<R.family.length;
Q++){if(R.family[Q]!=null&&angular.isDefined(G.familyData[Q])&&G.familyData[Q]!=="undefined"){G.familyData[Q].index=undefined;
R.family[Q].familyCustom=angular.copy(G.familyData[Q]);
R.family[Q].familyDbType=angular.copy(G.familydbType)
}if(R.family[Q]!=null&&angular.isDefined(G.editfamilyData)&&G.editfamilyData!=="undefined"){R.family[Q].familyCustom=angular.copy(G.editfamilyData);
R.family[Q].familyDbType=angular.copy(G.editfamilydbType)
}}for(var Q=0;
Q<R.policy.length;
Q++){if(R.policy[Q]!=null&&angular.isDefined(G.policyData[Q])&&G.policyData[Q]!=="undefined"){G.policyData[Q].index=undefined;
R.policy[Q].policyCustom=angular.copy(G.policyData[Q]);
R.policy[Q].policyDbType=angular.copy(G.policydbType);
R.policy[Q].holdername=undefined
}if(R.policy[Q]!=null&&angular.isDefined(G.editpolicyData)&&G.editpolicyData!=="undefined"){R.policy[Q].policyCustom=angular.copy(G.editpolicyData);
R.policy[Q].policyDbType=angular.copy(G.editpolicydbType);
R.policy[Q].holdername=undefined
}}if(R.currentaddress instanceof Object){R.currentaddress=R.currentaddress.id
}if(R.nativeaddress instanceof Object){R.nativeaddress=R.nativeaddress.id
}console.log("Add employeeee"+JSON.stringify(R));
if(G.isCreate){l.maskLoading();
w.addEmployee(R,T,P)
}else{if(!G.isCreate&&!G.isEmployeeTerminationConfirmed){l.maskLoading();
if(R.currentaddress instanceof Object){R.currentaddress=R.currentaddress.id
}if(R.nativeaddress instanceof Object){R.nativeaddress=R.nativeaddress.id
}w.updateEmployee(R,T,P)
}else{if(G.isEmployeeTerminationConfirmed){if(R.currentaddress instanceof Object){R.currentaddress=R.currentaddress.id
}if(R.nativeaddress instanceof Object){R.nativeaddress=R.nativeaddress.id
}w.terminateEmployee(R,function(){G.terminateEmpModal.dismiss();
$(".modal-backdrop").remove();
T()
},P)
}}}}else{if(!M){for(var S in N){if(S.indexOf("$")!==0){if(N[S].$invalid){G.scrollTo(S.toString());
break
}}}}}};
G.openNativeAddressModal=function(M){if(G.employee.isNativeAddressSame===true){G.nativesubmitted=false;
G.nativeAddModal=s.open({templateUrl:"nativeAddTmpl.html",scope:G,size:"lg"});
M.preventDefault()
}else{G.employee.isNativeAddressSame=true;
G.resetNativeAddress()
}};
G.showNativeAddressModal=function(M){G.nativesubmitted=false;
G.nativeAddModal=s.open({templateUrl:"nativeAddTmpl.html",scope:G,size:"lg"})
};
G.resetNativeAddress=function(){G.nativesubmitted=false;
if(G.employee.isNativeAddressSame===true){G.employee.nativeaddress="";
G.employee.nativefulladdress=undefined;
G.employee.nativepincode=undefined
}else{if(G.employee.nativeaddress!==undefined&&G.employee.nativeaddress!==null&&G.employee.nativeaddress.length>0){G.tempAddress.nativeaddress=G.employee.nativeaddress
}if(G.employee.nativefulladdress!==undefined&&G.employee.nativefulladdress!==null&&G.employee.nativefulladdress.length>0){G.tempAddress.nativefulladdress=G.employee.nativefulladdress
}if(G.employee.nativepincode!==undefined&&G.employee.nativepincode!==null&&G.employee.nativepincode.length>0){G.tempAddress.nativepincode=G.employee.nativepincode
}G.employee.nativeaddress="";
G.employee.nativefulladdress=undefined;
G.employee.nativepincode=undefined
}};
G.cancelNativeAddress=function(){G.nativeAddModal.dismiss();
l.removeModalOpenCssAfterModalHide();
if(G.employee.isNativeAddressSame===true){G.nativesubmitted=false;
G.employee.nativeaddress="";
G.employee.nativefulladdress=undefined;
G.employee.nativepincode=undefined
}else{if(G.tempAddress.nativeaddress!==undefined&&G.tempAddress.nativeaddress!==null&&G.tempAddress.nativeaddress.length>0){G.employee.nativeaddress=G.tempAddress.nativeaddress
}if(G.tempAddress.nativefulladdress!==undefined&&G.tempAddress.nativefulladdress!==null&&G.tempAddress.nativefulladdress.length>0){G.employee.nativefulladdress=G.tempAddress.nativefulladdress
}if(G.tempAddress.nativepincode!==undefined&&G.tempAddress.nativepincode!==null&&G.tempAddress.nativepincode.length>0){G.employee.nativepincode=G.tempAddress.nativepincode
}}};
G.okNativeAddress=function(M){G.nativesubmitted=true;
if(M.$valid){G.nativesubmitted=false;
G.employee.isNativeAddressSame=false;
G.nativeAddModal.dismiss();
l.removeModalOpenCssAfterModalHide()
}};
G.eduAddAnother=function(N,M){G.edusubmitted=true;
G.degreeReq=false;
if((!G.employee.edu[G.educationIndex].degree)&&(!G.employee.edu[G.educationIndex].passingYear)&&(!G.employee.edu[G.educationIndex].empPercentage)&&(!G.employee.edu[G.educationIndex].university)&&(!G.employee.exp[G.expIndex].salary)&&(!G.employee.edu[G.educationIndex].medium)){G.edusubmitted=false
}if(!M){if((G.employee.edu[G.educationIndex].degree!==undefined&&G.employee.edu[G.educationIndex].degree!==null)&&(G.employee.edu[G.educationIndex].passingYear!==undefined&&G.employee.edu[G.educationIndex].passingYear!==null)&&(G.employee.edu[G.educationIndex].empPercentage!==undefined&&G.employee.edu[G.educationIndex].empPercentage!==null)&&(G.employee.edu[G.educationIndex].university!==undefined&&G.employee.edu[G.educationIndex].university!==null)&&(G.employee.edu[G.educationIndex].medium!==undefined&&G.employee.edu[G.educationIndex].medium!==null)){if(N){G.edusubmitted=false;
G.educationData[G.educationIndex]=angular.copy(G.tempeducationData);
G.educationIndex=G.educationIndex+1;
G.hasAnyEdu=true;
G.employee.edu[G.educationIndex]={};
G.tempeducationflag=false;
G.resetCustomFields("EDUCATION")
}}else{if((G.employee.edu[G.educationIndex].degree===undefined||G.employee.edu[G.educationIndex].degree===null&&G.employee.edu[G.educationIndex].degree===null)&&((G.employee.edu[G.educationIndex].passingYear!==undefined&&G.employee.edu[G.educationIndex].passingYear!==null)||(G.employee.edu[G.educationIndex].empPercentage!==undefined&&G.employee.edu[G.educationIndex].empPercentage!==null&&G.employee.edu[G.educationIndex].empPercentage)||(G.employee.edu[G.educationIndex].university!==undefined&&G.employee.edu[G.educationIndex].university!==null&&G.employee.edu[G.educationIndex].university)||(G.employee.edu[G.educationIndex].medium!==undefined&&G.employee.edu[G.educationIndex].medium!==null&&G.employee.edu[G.educationIndex].medium!==null))){G.degreeReq=true
}}}};
G.eduDegreeOnChange=function(){if(G.employee.edu[G.educationIndex].degree===null){G.resetEduDetails()
}};
G.resetEduDetails=function(){G.employee.edu[G.educationIndex].passingYear=undefined;
G.employee.edu[G.educationIndex].university=undefined;
G.employee.edu[G.educationIndex].medium=undefined;
G.employee.edu[G.educationIndex].empPercentage=undefined
};
G.viewAllEduDetails=function(){var M=G.educationIndex;
G.viewAllEdu=[];
$.each(G.employee.edu,function(O,Q){var N=angular.copy(Q);
if(O!==M&&N!==null){N.index=O;
for(var P=0;
P<G.educationDegreeMap.length;
P++){if(G.educationDegreeMap[P].value===N.degree){N.degreename=G.educationDegreeMap[P].label;
break
}}for(var P=0;
P<G.universityList.length;
P++){if(G.universityList[P].value===N.university){N.universityname=G.universityList[P].label;
break
}}for(var P=0;
P<G.mediumMap.length;
P++){if(G.mediumMap[P].value===N.medium){N.mediumname=G.mediumMap[P].label;
break
}}for(var P=0;
P<G.yearOfPassing.length;
P++){if(G.yearOfPassing===N.passingYear){N.yearname=G.yearOfPassing[P];
break
}}G.viewAllEdu.push(N)
}});
G.eduViewAllModal=s.open({templateUrl:"eduViewAllTmpl.html",scope:G,size:"lg"})
};
G.openEditEduModal=function(M){G.editedu=angular.copy(M);
G.editeducationData={};
G.editEducationTemplate=G.educationTemplate;
G.editeducationData=angular.copy(G.educationData[M.index]);
if(!!G.editeducationData&&!!G.listOfModelsOfDateType.EDUCATION){angular.forEach(G.listOfModelsOfDateType.EDUCATION,function(N){if(G.editeducationData.hasOwnProperty(N)){if(G.editeducationData[N]!==null&&G.editeducationData[N]!==undefined){G.editeducationData[N]=new Date(G.editeducationData[N])
}else{G.editeducationData[N]=""
}}})
}G.eduViewAllModal.dismiss();
l.removeModalOpenCssAfterModalHide();
G.editEduModal=s.open({templateUrl:"editEduTmpl.html",scope:G,size:"lg"})
};
G.removeEdu=function(Q){G.employee.edu[Q.index]=null;
G.educationData[Q.index]=null;
G.viewAllEdu.splice(Q.index,1);
var M=0;
var P=G.educationIndex;
for(var N=0;
N<G.employee.edu.length;
N++){var O=G.employee.edu[N];
if(N!==P&&O!==null){M=M+1
}}if(M===0){G.hasAnyEdu=false;
G.eduViewAllModal.dismiss();
l.removeModalOpenCssAfterModalHide()
}};
G.hideEduViewAll=function(){G.editEduModal.dismiss();
l.removeModalOpenCssAfterModalHide()
};
G.saveEditEduChanges=function(N){G.editedusubmitted=true;
if(N.$valid){var M=G.editedu.index;
G.editedu.index=undefined;
G.editedu.degreename=undefined;
G.editedu.mediumname=undefined;
G.editedu.universityname=undefined;
G.editedu.yearname=undefined;
G.employee.edu[M]=angular.copy(G.editedu);
G.employee.edu[M].educationDbType=angular.copy(G.editeducationdbType);
G.employee.edu[M].educationCustom=angular.copy(G.editeducationData);
G.educationData[M]=angular.copy(G.editeducationData);
G.editEduModal.dismiss();
l.removeModalOpenCssAfterModalHide()
}};
G.expAddAnother=function(M){G.expsubmitted=true;
G.prevCmpnyReq=false;
if((!G.employee.exp[G.expIndex].company)&&(!G.employee.exp[G.expIndex].designation)&&(!G.employee.exp[G.expIndex].startedFrom)&&(!G.employee.exp[G.expIndex].workedTill)&&(!G.employee.exp[G.expIndex].salary)&&(!G.employee.exp[G.expIndex].reasonOfLeaving)){G.expsubmitted=false
}if((G.employee.exp[G.expIndex].company!==undefined&&G.employee.exp[G.expIndex].company!==null)&&(G.employee.exp[G.expIndex].designation!==undefined&&G.employee.exp[G.expIndex].designation!==null)&&(G.employee.exp[G.expIndex].startedFrom!==undefined&&G.employee.exp[G.expIndex].startedFrom!==null)&&(G.employee.exp[G.expIndex].workedTill!==undefined&&G.employee.exp[G.expIndex].workedTill!==null)&&(G.employee.exp[G.expIndex].salary!==undefined&&G.employee.exp[G.expIndex].salary!==null)&&(G.employee.exp[G.expIndex].reasonOfLeaving!==undefined&&G.employee.exp[G.expIndex].reasonOfLeaving!==null)){if(M){G.expsubmitted=false;
G.experienceData[G.expIndex]=angular.copy(G.tempexperienceData);
G.expIndex=G.expIndex+1;
G.hasAnyExp=true;
G.employee.exp[G.expIndex]={};
G.tempexperienceflag=false;
G.resetCustomFields("EXPERIENCE")
}}else{if((G.employee.exp[G.expIndex].company===undefined||G.employee.exp[G.expIndex].company===null)&&((G.employee.exp[G.expIndex].designation!==undefined&&G.employee.exp[G.expIndex].designation!==null)||(G.employee.exp[G.expIndex].startedFrom!==undefined&&G.employee.exp[G.expIndex].startedFrom!==null&&G.employee.exp[G.expIndex].startedFrom)||(G.employee.exp[G.expIndex].workedTill!==undefined&&G.employee.exp[G.expIndex].workedTill!==null&&G.employee.exp[G.expIndex].workedTill)||(G.employee.exp[G.expIndex].salary!==undefined&&G.employee.exp[G.expIndex].salary!==null&&G.employee.exp[G.expIndex].salary)||(G.employee.exp[G.expIndex].reasonOfLeaving!==undefined&&G.employee.exp[G.expIndex].reasonOfLeaving!==null&&G.employee.exp[G.expIndex].reasonOfLeaving))){G.prevCmpnyReq=true
}}};
G.expComapnyOnBlur=function(){if(G.employee.exp[G.expIndex].company===null){G.resetExpDetails()
}};
G.resetExpDetails=function(){G.employee.exp[G.expIndex].designation=undefined;
G.employee.exp[G.expIndex].startedFrom=undefined;
G.employee.exp[G.expIndex].workedTill=undefined;
G.employee.exp[G.expIndex].salary=undefined;
G.employee.exp[G.expIndex].reasonOfLeaving=undefined
};
G.viewAllExpDetails=function(){var M=G.expIndex;
G.viewAllExp=[];
$.each(G.employee.exp,function(O,Q){var N=angular.copy(Q);
if(O!==M&&N!==null){N.index=O;
for(var P=0;
P<G.expDepList.length;
P++){if(G.expDepList[P].value===N.designation){N.designationname=G.expDepList[P].label;
break
}}G.viewAllExp.push(N)
}});
G.expViewAllModal=s.open({templateUrl:"expViewAllTmpl.html",scope:G,size:"lg"})
};
G.openEditExpModal=function(M){G.editexp=angular.copy(M);
G.editexperienceData={};
G.editexperienceTemplate=G.experienceTemplate;
G.editexperienceData=angular.copy(G.experienceData[M.index]);
if(!!G.editexperienceData&&!!G.listOfModelsOfDateType.EXPERIENCE){angular.forEach(G.listOfModelsOfDateType.EXPERIENCE,function(N){if(G.editexperienceData.hasOwnProperty(N)){if(G.editexperienceData[N]!==null&&G.editexperienceData[N]!==undefined){G.editexperienceData[N]=new Date(G.editexperienceData[N])
}else{G.editexperienceData[N]=""
}}})
}G.expViewAllModal.dismiss();
l.removeModalOpenCssAfterModalHide();
G.editExpModal=s.open({templateUrl:"editExpTmpl.html",scope:G,size:"lg"})
};
G.removeExp=function(Q){G.employee.exp[Q.index]=null;
G.experienceData[Q.index]=null;
G.viewAllExp.splice(Q.index,1);
var M=0;
var P=G.expIndex;
for(var N=0;
N<G.employee.exp.length;
N++){var O=G.employee.exp[N];
if(N!==P&&O!==null){M=M+1
}}if(M===0){G.hasAnyExp=false;
G.expViewAllModal.dismiss();
l.removeModalOpenCssAfterModalHide()
}};
G.hideExpViewAll=function(){G.editExpModal.dismiss();
l.removeModalOpenCssAfterModalHide()
};
G.saveEditExpChanges=function(N){G.editexpsubmitted=true;
if(N.$valid){var M=G.editexp.index;
G.editexp.index=undefined;
G.editexp.designationname=undefined;
G.employee.exp[M]=angular.copy(G.editexp);
G.employee.exp[M].experienceDbType=angular.copy(G.editexperiencedbType);
G.employee.exp[M].experienceCustom=angular.copy(G.editexperienceData);
G.experienceData[M]=angular.copy(G.editexperienceData);
G.editExpModal.dismiss();
l.removeModalOpenCssAfterModalHide()
}};
G.checkMemberName=function(M){console.log("viewAllFamily----"+JSON.stringify(G.viewAllFamily));
if(G.viewAllFamily!=null&&G.viewAllFamily.length>0){for(var N=0;
N<G.viewAllFamily.length;
N++){if(G.viewAllFamily[N].firstName.toLowerCase()===M.toLowerCase()){G.isfamilyNameDuplicate=false;
break
}else{G.isfamilyNameDuplicate=true
}}}};
G.familyAddAnother=function(M){G.familysubmitted=true;
G.familynameReq=false;
if((!G.employee.family[G.familyIndex].firstName)&&(!G.employee.family[G.familyIndex].dateOfBirth)&&(!G.employee.family[G.familyIndex].occupation)&&(!G.employee.family[G.familyIndex].relation)&&(!G.employee.family[G.familyIndex].bloodGroup)){G.familysubmitted=false
}if((G.employee.family[G.familyIndex].firstName!==undefined&&G.employee.family[G.familyIndex].firstName!==null)&&(G.employee.family[G.familyIndex].dateOfBirth!==undefined&&G.employee.family[G.familyIndex].dateOfBirth!==null)&&(G.employee.family[G.familyIndex].occupation!==undefined&&G.employee.family[G.familyIndex].occupation!==null)&&(G.employee.family[G.familyIndex].relation!==undefined&&G.employee.family[G.familyIndex].relation!==null)&&(G.employee.family[G.familyIndex].bloodGroup!==undefined&&G.employee.family[G.familyIndex].bloodGroup!==null)&&G.isfamilyContact){if(M&&(G.isfamilyNameDuplicate===true)){G.familysubmitted=false;
G.policyHoldersMap["F"+G.familyIndex]=G.employee.family[G.familyIndex].firstName;
G.familyData[G.familyIndex]=angular.copy(G.tempfamilyData);
G.familyIndex=G.familyIndex+1;
G.hasAnyFamily=true;
G.employee.family[G.familyIndex]={index:G.familyIndex};
G.familyreload=false;
G.resetCustomFields("FAMILY")
}}else{if((G.employee.family[G.familyIndex].firstName===undefined||G.employee.family[G.familyIndex].firstName===null)&&((G.employee.family[G.familyIndex].dateOfBirth!==undefined&&G.employee.family[G.familyIndex].dateOfBirth!==null&&G.employee.family[G.familyIndex].dateOfBirth)||(G.employee.family[G.familyIndex].occupation!==undefined&&G.employee.family[G.familyIndex].occupation!==null&&G.employee.family[G.familyIndex].occupation)||(G.employee.family[G.familyIndex].relation!==undefined&&G.employee.family[G.familyIndex].relation!==null&&G.employee.family[G.familyIndex].relation)||(G.employee.family[G.familyIndex].bloodGroup!==undefined&&G.employee.family[G.familyIndex].bloodGroup!==null&&G.employee.family[G.familyIndex].bloodGroup)||(G.employee.family[G.familyIndex].mobileNumber!==undefined&&G.employee.family[G.familyIndex].mobileNumber!==null))){G.familynameReq=true
}}G.viewAllFamilyDetails()
};
G.familynameOnBlur=function(){if(G.employee.family[G.familyIndex].company===null){G.resetFamilyDetails()
}};
G.resetFamilyDetails=function(){G.employee.family[G.familyIndex].dateOfBirth=undefined;
G.employee.family[G.familyIndex].occupation=undefined;
G.employee.family[G.familyIndex].relation=undefined;
G.employee.family[G.familyIndex].bloodGroup=undefined;
G.employee.family[G.familyIndex].mobileNumber=undefined
};
G.viewAllFamilyDetails=function(){var M=G.familyIndex;
G.viewAllFamily=[];
$.each(G.employee.family,function(O,Q){var N=angular.copy(Q);
if(O!==M&&N!==null){if(!angular.isDefined(N.mobileNumber)){N.mobileNumber="NA"
}N.index=O;
for(var P=0;
P<G.relationList.length;
P++){if(G.relationList[P].value===N.relation){N.relationname=G.relationList[P].label;
break
}}for(var P=0;
P<G.bloodGroups.length;
P++){if(G.bloodGroups[P].value===N.bloodGroup){N.bloodGroupname=G.bloodGroups[P].label;
break
}}for(var P=0;
P<G.occupationList.length;
P++){if(G.occupationList[P].value===N.occupation){N.occupationname=G.occupationList[P].label;
break
}}G.viewAllFamily.push(N)
}})
};
G.openModal=function(){G.viewAllFamilyDetails();
G.familyViewAllModal=s.open({templateUrl:"familyViewAllTmpl.html",scope:G,size:"lg"})
};
G.openEditFamilyModal=function(M){G.editfamily=angular.copy(M);
G.editfamilyData={};
G.editfamilyTemplate=G.familyTemplate;
G.editfamilyData=angular.copy(G.familyData[M.index]);
if(!!G.editfamilyData&&!!G.listOfModelsOfDateType.FAMILY){angular.forEach(G.listOfModelsOfDateType.FAMILY,function(N){if(G.editfamilyData.hasOwnProperty(N)){if(G.editfamilyData[N]!==null&&G.editfamilyData[N]!==undefined){G.editfamilyData[N]=new Date(G.editfamilyData[N])
}else{G.editfamilyData[N]=""
}}})
}G.familydbType={};
G.familyViewAllModal.dismiss();
l.removeModalOpenCssAfterModalHide();
G.editFamilyModal=s.open({templateUrl:"editFamilyTmpl.html",scope:G,size:"lg"})
};
G.removeFamily=function(P,N){G.employee.family[P.index]=null;
G.familyData[N]=null;
G.viewAllFamily.splice(N,1);
var R="F"+P.index;
delete G.policyHoldersMap[R];
for(var O=0;
O<G.employee.policy.length;
O++){var Q=G.employee.policy[O];
if(Q!=null&&Q.contactUser===R){G.removePolicy(Q)
}}var M=0;
var S=G.familyIndex;
for(var O=0;
O<G.employee.family.length;
O++){var Q=G.employee.family[O];
if(O!==S&&Q!==null){M=M+1
}}if(M===0){G.hasAnyFamily=false;
G.familyViewAllModal.dismiss();
l.removeModalOpenCssAfterModalHide()
}};
G.hideFamilyViewAll=function(){G.editFamilyModal.dismiss();
l.removeModalOpenCssAfterModalHide()
};
G.saveEditFamilyChanges=function(N){G.editfamilysubmitted=true;
if(N.$valid){var M,P;
P=G.employee.family.length;
var O=G.editfamily.index;
for(M=0;
M<P-1;
M++){if(M!=O){if(G.employee.family[M].firstName.toLowerCase().replace(/ /g,"")===G.editfamily.firstName.toLowerCase().replace(/ /g,"")){if(P>2){G.iseditfamilyNameDuplicate=false;
break
}}else{G.iseditfamilyNameDuplicate=true
}}}if(G.iseditfamilyNameDuplicate===true){var M=G.editfamily.index;
G.policyHoldersMap["F"+G.editfamily.index]=G.editfamily.firstName;
G.editfamily.relationname=undefined;
G.editfamily.bloodGroupname=undefined;
G.editfamily.occupationname=undefined;
G.employee.family[M]=angular.copy(G.editfamily);
G.employee.family[M].familyDbType=angular.copy(G.editfamilydbType);
G.employee.family[M].familyCustom=angular.copy(G.editfamilyData);
G.familyData[M]=angular.copy(G.editfamilyData);
G.editFamilyModal.dismiss();
l.removeModalOpenCssAfterModalHide()
}}};
G.policyAddAnother=function(M){G.policysubmitted=true;
G.policynameReq=false;
if((!G.employee.policy[G.policyIndex].contactUser)&&(!G.employee.policy[G.policyIndex].company)&&(!G.employee.policy[G.policyIndex].policyNumber)&&(!G.employee.family[G.familyIndex].relation)&&(!G.employee.policy[G.policyIndex].policyName)){G.policysubmitted=false
}if((G.employee.policy[G.policyIndex].contactUser!==undefined&&G.employee.policy[G.policyIndex].contactUser!==null)&&(G.employee.policy[G.policyIndex].company!==undefined&&G.employee.policy[G.policyIndex].company!==null)&&(G.employee.policy[G.policyIndex].policyNumber!==undefined&&G.employee.policy[G.policyIndex].policyNumber!==null)&&(G.employee.policy[G.policyIndex].policyName!==undefined&&G.employee.policy[G.policyIndex].policyName!==null)){if(M){G.policysubmitted=false;
G.policyData[G.policyIndex]=angular.copy(G.temppolicyData);
G.policyIndex=G.policyIndex+1;
G.hasAnyPolicy=true;
G.employee.policy[G.policyIndex]={status:"Active",index:G.policyIndex};
G.temppolicyflag=false;
G.resetCustomFields("POLICY")
}}else{if((G.employee.policy[G.policyIndex].contactUser===undefined||G.employee.policy[G.policyIndex].contactUser===null)&&((G.employee.policy[G.policyIndex].company!==undefined&&G.employee.policy[G.policyIndex].company!==null&&G.employee.policy[G.policyIndex].company)||(G.employee.policy[G.policyIndex].policyNumber!==undefined&&G.employee.policy[G.policyIndex].policyNumber!==null&&G.employee.policy[G.policyIndex].policyNumber)||(G.employee.policy[G.policyIndex].policyName!==undefined&&G.employee.policy[G.policyIndex].policyName!==null&&G.employee.policy[G.policyIndex].policyName))){G.policynameReq=true
}}};
G.policynameOnBlur=function(){if(G.employee.policy[G.policyIndex].contactUser===null){G.resetPolicyDetails()
}};
G.resetPolicyDetails=function(){G.employee.policy[G.policyIndex].company=undefined;
G.employee.policy[G.policyIndex].status=undefined;
G.employee.policy[G.policyIndex].policyNumber=undefined;
G.employee.policy[G.policyIndex].policyName=undefined
};
G.viewAllPolicyDetails=function(){var M=G.policyIndex;
G.viewAllPolicy=[];
$.each(G.employee.policy,function(O,R){var N=angular.copy(R);
if(O!==M&&N!==null){N.index=O;
for(var Q=0;
Q<G.policyCompanyList.length;
Q++){if(G.policyCompanyList[Q].value===N.company){N.companyname=G.policyCompanyList[Q].label;
break
}}for(var P in G.policyHoldersMap){if(P===N.contactUser){N.holdername=G.policyHoldersMap[P];
break
}}G.viewAllPolicy.push(N)
}});
G.policyViewAllModal=s.open({templateUrl:"policyViewAllTmpl.html",scope:G,size:"lg"})
};
G.openEditPolicyModal=function(M){G.editpolicy=angular.copy(M);
G.editpolicyData={};
G.editpolicyTemplate=angular.copy(G.policyTemplate);
G.editpolicyData=angular.copy(G.policyData[M.index]);
if(!!G.editpolicyData&&!!G.listOfModelsOfDateType.POLICY){angular.forEach(G.listOfModelsOfDateType.POLICY,function(N){if(G.editpolicyData.hasOwnProperty(N)){if(G.editpolicyData[N]!==null&&G.editpolicyData[N]!==undefined){G.editpolicyData[N]=new Date(G.editpolicyData[N])
}else{G.editpolicyData[N]=""
}}})
}G.policyViewAllModal.dismiss();
l.removeModalOpenCssAfterModalHide();
G.editPolicyModal=s.open({templateUrl:"editPolicyTmpl.html",scope:G,size:"lg"})
};
G.removePolicy=function(Q){G.employee.policy[Q.index]=null;
if(Q.index!=G.policyIndex){G.employee.policy[Q.index]=null
}else{G.employee.policy[Q.index]={status:"Active",index:Q.index}
}G.policyData[Q.index]=null;
if(G.viewAllPolicy!=undefined){G.viewAllPolicy.splice(Q.index,1)
}var M=0;
var P=G.policyIndex;
for(var N=0;
N<G.employee.policy.length;
N++){var O=G.employee.policy[N];
if(N!==P&&O!==null){M=M+1
}}if(M===0){G.hasAnyPolicy=false;
G.policyViewAllModal.dismiss();
l.removeModalOpenCssAfterModalHide()
}};
G.hidePolicyViewAll=function(){G.editPolicyModal.dismiss();
l.removeModalOpenCssAfterModalHide()
};
G.saveEditPolicyChanges=function(N){G.editpolicysubmitted=true;
if(N.$valid){var M=G.editpolicy.index;
G.editpolicy.companyname=undefined;
G.editpolicy.holdernaame=undefined;
G.employee.policy[M]=angular.copy(G.editpolicy);
G.employee.policy[M].policyDbType=angular.copy(G.editpolicydbType);
G.employee.policy[M].policyCustom=angular.copy(G.editpolicyData);
G.policyData[M]=angular.copy(G.editpolicyData);
G.editPolicyModal.dismiss();
l.removeModalOpenCssAfterModalHide()
}};
G.doesEmployeeNameExist=function(M){G.invalidname=true;
if(M&&M.length>0){w.doesEmployeeNameExist(M,function(O){if(O.data){if(!G.isCreate&&M!=G.oldname){G.notUniqueEmpname=true
}if(G.isCreate){G.notUniqueEmpname=true
}}else{G.notUniqueEmpname=false;
var N=M.trim().split(" ");
if(N.length==3){G.invalidname=false
}else{G.invalidname=true
}}},function(){})
}else{G.notUniqueEmpname=false
}};
G.validateEmailId=function(M){var N=/^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
if(!N.test(M)){return false
}else{return true
}};
G.validateEmail=function(M){if(!angular.isDefined(M)||M.trim().length==0){G.isEmailInvalid=false
}else{var N=G.validateEmailId(M);
if(!N){G.isEmailInvalid=true
}else{G.isEmailInvalid=false
}}};
G.validateWorkEmailId=function(M){if(G.employee.workemailId!==undefined){var N=G.validateEmailId(M);
if(!N){G.isWorkEmailValidate=false
}else{G.isWorkEmailValidate=true
}}};
G.ValidateIPaddress=function(M){if(M!==undefined){var N=/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
if(M.match(N)){G.ipValidate=true
}else{G.ipValidate=false
}}};
G.setMinDate=function(){if(G.employee.dob===""||G.employee.dob===undefined){G.minToDate=l.getCurrentServerDate()
}else{G.minToDate=G.employee.dob
}if(G.employee.dob>G.employee.joiningDate){G.employee.joiningDate=G.employee.dob
}};
G.joiningdate=new Date();
G.$watch("employee.joiningDate",function(P){if(P!==undefined){var N=new Date(P).getTime();
var O=new Date().getTime();
if(N>O||N===0||N===null){if(N===0||N===null){G.joiningDuration=""
}else{G.joiningDuration="0 Month(s)"
}}else{var R=(O-N)/(1000*60*60*24);
var M,Q;
if(R<30){M=0
}else{M=Math.floor(R/30)
}if(M<12){Q=0
}else{Q=Math.floor(M/12);
M=M%12
}if(Q>0){G.joiningDuration=Q+" year(s) "+M+" month(s) "
}else{G.joiningDuration=M+" month(s) "
}}}else{G.joiningDuration=""
}});
G.searchEmployee=function(){G.employeeList=[];
if(G.searchtext.length>0){w.searchEmployee({search:G.searchtext},function(M){G.employeeList=angular.copy(M)
})
}};
G.uploadFile={target:l.apipath+"fileUpload/uploadFile",singleFile:true,attributes:{accept:"application/pdf"},testChunks:true,query:{fileType:G.seletedFileType,model:"Franchise"}};
G.profileImg={};
G.profileFileAdded=function(N,M){G.profileFlow=M;
G.uploadFile.query.fileType="PROFILE";
G.profileUploaded=false;
if((N.getExtension()!="jpg")&&(N.getExtension()!="jpeg")&&(N.getExtension()!="png")&&(N.getExtension()!="gif")){G.profileImg.invalidFileFlag=true;
G.profileImg.fileName=N.name
}else{if(N.size>5000000){G.profileImg.invalidFileSizeFlag=true;
return false
}}return !!{jpg:1,jpeg:1,gif:1,png:1}[N.getExtension()]
};
G.getProfileImageEmployee=function(M){if(M){var N=G.employee.profileImageName;
N=N.substring(0,N.lastIndexOf("."));
N+="_T.jpg";
return l.appendAuthToken(l.apipath+"/employee/getimage?file_name="+N)
}else{G.profileImagePopup(G.employee.profileImageName)
}};
G.profileFileUploaded=function(P,N,Q,S){var O=[P.name,U,G.seletedFileType];
var V="PROFILE";
G.profileUploaded=true;
if(G.saveEventFlag===false){G.saveFilesAndEvent(Q)
}var U="Franchise";
var R=[P.name,V];
var T;
var M="true";
var O;
w.uploadFile(R,function(W){T=W.filename;
O=[P.name,U,T,M];
z.uploadFiles(O,function(X){G.employee.profileImageName=X.res
})
});
N.cancel()
};
G.profileFileRemove=function(){w.removeFileFromTemp(G.employee.profileImageName);
G.employee.profileImageName=undefined
};
G.familyImg={};
G.familyFileAdded=function(N,M){G.familyImg.invalidFileFlag=false;
G.familyImg.invalidFileSizeFlag=false;
G.familyFlow=M;
G.uploadFile.query.fileType="PROFILE";
G.familyUploaded=false;
if((N.getExtension()!="jpg")&&(N.getExtension()!="jpeg")&&(N.getExtension()!="png")&&(N.getExtension()!="gif")){G.familyImg.invalidFileFlag=true;
G.familyImg.fileName=N.name
}else{if(N.size>5000000){G.familyImg.invalidFileSizeFlag=true;
return false
}}return !!{jpg:1,jpeg:1,gif:1,png:1}[N.getExtension()]
};
G.familyFileUploaded=function(P,N,Q,S,U){var O=[P.name,V,G.seletedFileType];
var W="PROFILE";
if(U){G.editfamily.familyImageName=S
}else{G.employee.family[G.familyIndex].familyImageName=S
}G.familyUploaded=true;
if(G.saveEventFlag===false){G.saveFilesAndEvent(Q)
}var V="Franchise";
var R=[P.name,W];
var T;
var M="true";
var O;
w.uploadFile(R,function(X){T=X.filename;
O=[P.name,V,T,M];
z.uploadFiles(O,function(Y){if(U){G.editfamily.familyImageName=Y.res
}else{G.employee.family[G.familyIndex].familyImageName=Y.res
}})
});
N.cancel()
};
G.familyFileRemove=function(M){w.removeFileFromTemp(G.employee.family[M].familyImageName);
G.employee.family[M].familyImageName=undefined
};
G.familyFileRemoveEdit=function(){w.removeFileFromTemp(G.editfamily.familyImageName);
G.editfamily.familyImageName=undefined
};
G.salaryslipImg={};
G.salaryslipFileAdded=function(N,M){G.salaryslipImg.invalidFileFlag=false;
G.salaryslipImg.invalidFileSizeFlag=false;
G.salaryslipFlow=M;
G.uploadFile.query.fileType="SALARYSLIP";
G.salaryslipUploaded=false;
if((N.getExtension()!="jpg")&&(N.getExtension()!="jpeg")&&(N.getExtension()!="png")&&(N.getExtension()!="gif")&&(N.getExtension()!="pdf")){G.salaryslipImg.invalidFileFlag=true;
G.salaryslipImg.fileName=N.name
}else{if(N.size>5000000){G.salaryslipImg.invalidFileSizeFlag=true;
return false
}}return !!{jpg:1,jpeg:1,gif:1,png:1,pdf:1}[N.getExtension()]
};
G.salaryslipFileUploaded=function(P,N,Q,S,U){var O=[P.name,V,G.seletedFileType];
var W="SALARYSLIP";
G.salaryslipUploaded=true;
if(G.saveEventFlag===false){G.saveFilesAndEvent(Q)
}var V="Franchise";
var R=[P.name,W];
var T;
var M="false";
var O;
w.uploadFile(R,function(X){T=X.filename;
O=[P.name,V,T,M];
z.uploadFiles(O,function(Y){if(U){G.editexp.salaryslipImageName=Y.res
}else{G.employee.exp[G.expIndex].salaryslipImageName=Y.res
}})
});
N.cancel()
};
G.salaryslipRemove=function(M){w.removeFileFromTemp(G.employee.exp[M].salaryslipImageName);
G.employee.exp[M].salaryslipImageName=undefined
};
G.salaryslipRemoveEdit=function(){w.removeFileFromTemp(G.editexp.salaryslipImageName);
G.editexp.salaryslipImageName=undefined
};
G.otheruploadFile={target:l.apipath+"fileUpload/uploadFile",singleFile:false,testChunks:true,query:{fileType:G.seletedFileType,model:"Franchise"}};
G.otherdocsImg={};
G.employee.otherdocs=[];
G.otherDocIndex=0;
G.otherdocsFileAdded=function(N,M){G.otherdocsImg.invalidFileFlag=false;
G.otherdocsImg.invalidFileSizeFlag=false;
G.otherdocsFlow=M;
G.uploadFile.query.fileType="OTHER";
G.otherdocsUploaded=false;
if((N.getExtension()!="jpg")&&(N.getExtension()!="jpeg")&&(N.getExtension()!="png")&&(N.getExtension()!="gif")&&(N.getExtension()!="pdf")&&(N.getExtension()!="txt")&&(N.getExtension()!="doc")&&(N.getExtension()!="docx")){G.otherdocsImg.invalidFileFlag=true;
G.otherdocsImg.fileName=N.name
}else{if(N.size>5000000){G.otherdocsImg.invalidFileSizeFlag=true;
return false
}}return !!{jpg:1,jpeg:1,gif:1,png:1,pdf:1,txt:1,doc:1,docx:1}[N.getExtension()]
};
G.otherdocsFileUploaded=function(P,N,Q,S){var O=[P.name,U,G.seletedFileType];
var V="OTHER";
G.hasAnyDoc=true;
G.otherdocsUploaded=true;
if(G.saveEventFlag===false){G.saveFilesAndEvent(Q)
}var U="Franchise";
var R=[P.name,V];
var T;
var M="false";
var O;
w.uploadFile(R,function(W){T=W.filename;
O=[P.name,U,T,M];
z.uploadFiles(O,function(X){G.employee.otherdocs.push(X.res);
G.employee.otherdocsDate.push(new Date())
})
})
};
G.otherdocRemove=function(N){w.removeFileFromTemp(G.employee.otherdocs[N]);
G.employee.otherdocs[N]=null;
G.employee.otherdocs.splice(N,1);
G.employee.otherdocsDate[N]=null;
G.employee.otherdocsDate.splice(N,1);
var M=0;
for(var O=0;
O<G.employee.otherdocs.length;
O++){var P=G.employee.otherdocs[O];
if(P!==null){M=M+1
}}if(M===0){G.employee.otherdocs=[];
G.employee.otherdocsDate=[];
G.hasAnyDoc=false;
G.docsViewAllModal.dismiss();
l.removeModalOpenCssAfterModalHide()
}};
G.openViewDocs=function(){G.docsViewAllModal=s.open({templateUrl:"docsViewAllTmpl.html",scope:G,size:"lg"})
};
G.isResigned=false;
G.getStatusValue=function(){G.isResigned=false;
for(var N=0;
N<G.statusList.length;
N++){G.statusList[N].label=l.translateValue("EMPLOYEE."+G.statusList[N].label);
var M=l.translateValue("EMPLOYEE.Resigned");
if(G.statusList[N].value===G.employee.workstatus&&G.statusList[N].label===M){G.isResigned=true;
break
}}};
G.beforeStatus=function(){G.beforeStatus=G.empstatus
};
G.proceed=function(M){G.isEmployeeTerminationConfirmed=true;
G.addEmployee(M)
};
G.cancelproceed=function(){G.employee.workstatus=G.beforeStatus;
G.isResigned=false;
G.terminateEmpModal.dismiss();
l.removeModalOpenCssAfterModalHide()
};
G.resetPage=function(){x.reload()
};
G.getProfileImageContact=function(N,O,M){var P="";
if(O){P=G.editfamily.familyImageName
}else{P=G.employee.family[N].familyImageName
}if(M){P=P.substring(0,P.lastIndexOf("."));
P+="_T.jpg";
return l.appendAuthToken(l.apipath+"/employee/getimage?file_name="+P)
}else{G.familyImagePopup(P)
}};
G.setViewFlag=function(M){if(M!=="search"){$("#scrollable-dropdown-menu.typeahead").typeahead("val","")
}G.displayEmployeeFlag=M
};
G.getSearchedEmployee=function(N){var M=$("#scrollable-dropdown-menu.typeahead").typeahead("val");
if(M.length>0){if(M.length<3){G.searchRecords=[]
}else{G.searchRecords=[];
angular.forEach(N,function(O){O.empName=l.translateValue("EMP_NM."+O.empName);
O.department=l.translateValue("DPT_NM."+O.department);
G.searchRecords.push(O)
})
}G.setViewFlag("search")
}};
G.setEmployeeCode=function(){};
G.openChangePassword=function(){G.passwordObj={};
G.userid=G.employee.userId;
G.olduserid=G.employee.userId;
G.passwordObj.notUniqueUserId=false;
G.passwordObj.passsubmitted=false;
G.passworChangeModal=s.open({templateUrl:"changePasswordTmpl.html",scope:G,size:"lg"})
};
G.$watch("userid",function(){G.userid=D("lowercase")(G.userid)
});
G.doesUserIdExist=function(M){if(M&&M.length>0){if(M!==G.olduserid){w.doesUserIdExist(M,function(N){if(N.data){G.passwordObj.notUniqueUserId=true
}else{G.passwordObj.notUniqueUserId=false
}},function(){})
}else{G.passwordObj.notUniqueUserId=false
}}else{G.passwordObj.notUniqueUserId=false
}};
G.resetPassword=function(){G.passwordObj.confirmpassword=undefined;
G.passwordObj.password=undefined;
G.passwordObj.passsubmitted=false;
G.passwordObj={};
G.passworChangeModal.dismiss();
l.removeModalOpenCssAfterModalHide()
};
G.confirmPasswordValidation=function(){if(!angular.equals(G.passwordObj.password,G.passwordObj.confirmpassword)){G.passwordObj.isMatched=true
}else{G.passwordObj.isMatched=false
}};
G.changePassword=function(P){G.passwordObj.passsubmitted=true;
if(P.$valid&&!G.passwordObj.isMatched&&!G.passwordObj.notUniqueUserId){l.maskLoading();
var Q=function(){l.unMaskLoading();
G.passworChangeModal.dismiss();
l.removeModalOpenCssAfterModalHide();
G.passwordObj.passsubmitted=false
};
var N=function(){l.unMaskLoading();
G.passworChangeModal.dismiss();
l.removeModalOpenCssAfterModalHide();
G.passwordObj.passsubmitted=false
};
var M=G.employee.id;
var O={id:M.toString(),password:G.passwordObj.password,userid:G.userid};
w.changePassword(O,Q,N)
}};
G.profileImagePopup=function(M){G.selectedProfileImageName=M;
$("#profileImagePopup").modal("show")
};
G.familyImagePopup=function(M){G.selectedFamilyImageName=M;
$("#familyImagePopup").modal("show")
};
G.resetCustomFields=function(M){var N=F.retrieveSectionWiseCustomFieldInfo("manageEmployees");
N.then(function(O){if(M==="PERSONAL"||M===null){G.personalData={};
G.customPersonalTemplateDate=angular.copy(O.PERSONAL);
G.personalTemplate=l.getCustomDataInSequence(G.customPersonalTemplateDate)
}if(M==="CONTACT"||M===null){G.contactData={};
G.customContactTemplateDate=angular.copy(O.CONTACT);
G.contactTemplate=l.getCustomDataInSequence(G.customContactTemplateDate)
}if(M==="IDENTIFICATION"||M===null){G.identificationData={};
G.customIdentificationTemplateDate=angular.copy(O.IDENTIFICATION);
G.identificationTemplate=l.getCustomDataInSequence(G.customIdentificationTemplateDate)
}if(M==="OTHER"||M===null){G.otherData={};
G.customOtherTemplateDate=angular.copy(O.OTHER);
G.otherTemplate=l.getCustomDataInSequence(G.customOtherTemplateDate)
}if(M==="HKGWORK"||M===null){G.hkgworkData={};
G.customWorkTemplateDate=angular.copy(O.HKGWORK);
G.hkgworkTemplate=l.getCustomDataInSequence(G.customWorkTemplateDate)
}if(M==="EDUCATION"||M===null){G.tempeducationData={};
G.customEducationTemplateDate=angular.copy(O.EDUCATION);
G.educationTemplate=l.getCustomDataInSequence(G.customEducationTemplateDate)
}if(M==="EXPERIENCE"||M===null){G.tempexperienceData={};
G.customExperienceTemplateDate=angular.copy(O.EXPERIENCE);
G.experienceTemplate=l.getCustomDataInSequence(G.customExperienceTemplateDate)
}if(M==="FAMILY"||M===null){G.tempfamilyData={};
G.customFamilyTemplateDate=angular.copy(O.FAMILY);
G.familyTemplate=l.getCustomDataInSequence(G.customFamilyTemplateDate)
}if(M==="POLICY"||M===null){G.temppolicyData={};
G.customPolicyTemplateDate=angular.copy(O.POLICY);
G.policyTemplate=l.getCustomDataInSequence(G.customPolicyTemplateDate)
}if(M==="genralSection"||M===null){G.generalData={};
G.customGeneralTemplateDate=angular.copy(O.genralSection);
G.generaltemplate=l.getCustomDataInSequence(G.customGeneralTemplateDate)
}K(function(){G.personalreload=true;
G.contactreload=true;
G.identificationreload=true;
G.otherDataflag=true;
G.hkgworkDataflag=true;
G.tempeducationflag=true;
G.tempexperienceflag=true;
G.familyreload=true;
G.temppolicyflag=true;
G.generalreload=true
},50)
},function(O){},function(O){})
};
G.setDesignationForDept=function(O,N){G.designationList=[];
G.designationListForWorkAs=[];
var M;
console.log("parent,,,"+JSON.stringify(O));
if(N){if(O.currentNode.parentId!==null&&O.currentNode.parentId!==undefined&&O.currentNode.parentId!==0){console.log("in if");
M=O.currentNode.parentId
}else{M=O.currentNode.id
}}else{if(O.parentId!==null&&O.parentId!==undefined&&O.parentId!==0){console.log("in if");
M=O.parentId
}else{M=O.id
}}console.log("Dept"+M);
w.retrieveDesgByDept(M,function(P){console.log("Result..."+JSON.stringify(P));
angular.forEach(P,function(Q){Q.designationName=l.translateValue("DESIG_NM."+Q.label);
G.designationList.push(Q);
Q.modelName=Q.value;
G.designationListForWorkAs.push(Q)
})
})
};
$(function(){$(window).bind("mousewheel",function(M,N){$("#sidemenu").height($("#mainPanel").height())
});
$(window).bind("scroll",function(M){$("#sidemenu").height($("#mainPanel").height())
})
});
G.makeAScroll=function(){G.scrollTo("mainPanel")
};
G.closeViewAllEdu=function(){G.eduViewAllModal.dismiss()
};
G.closeViewAllExp=function(){G.expViewAllModal.dismiss()
};
G.closeViewAllFamily=function(){G.familyViewAllModal.dismiss()
};
G.closeViewAllPolicy=function(){G.policyViewAllModal.dismiss()
};
G.hidedocsViewAll=function(){G.docsViewAllModal.dismiss()
};
l.unMaskLoading()
}]);
b.register.filter("optionsWrap",function(){return function(j,h){var i=parseInt(n,10);
h=parseInt(h,10);
if(isNaN(i)||isNaN(h)){return n
}i=""+i;
while(i.length<h){i="0"+i
}return i
}
})
});