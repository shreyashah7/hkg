define(["hkg","employeeService","franchiseService","fileUploadService","leaveWorkflowService","designationService","departmentService","webcam","addMasterValue"],function(b,a,g,d,e,c,f){b.register.controller("EditProfile",["$rootScope","$timeout","$scope","$filter","Employee","$location","$anchorScroll","FranchiseService","LeaveWorkflow","Designation","DepartmentService","DynamicFormService","$route","FileUploadService",function(j,A,y,v,q,i,h,k,o,p,l,x,r,t){j.maskLoading();
j.mainMenu="";
j.childMenu="";
j.activateMenu();
y.entity="EMPLOYEE.";
y.today=new Date();
y.notAvailable="NA";
y.notAvailableSection="No Information Available for this section";
y.flag=false;
var s=v("orderBy");
y.popover="<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td> \n '@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr> <tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr> </table> ";
y.$on("$viewContentLoaded",function(){y.select2Locations=[];
u();
n()
});
function n(){j.maskLoading();
q.retrieveProfilePrerequisite(function(M){var E=M.combovalues;
var N=M.locations;
var K=M.employeedetail;
var O=K.employeeCode;
if(O!==null){y.isEmpCode=true
}else{y.isEmpCode=false;
j.addMessage("You don't have the permission to edit your profile",1)
}var D=M.employeeConfig;
var P=M.agelimit;
var H=M.languages;
var I=Object.keys(D).length;
var Q=Object.keys(P).length;
if(I==0&&Q==0){j.addMessage("please configure franchise first and then try creating employee",1)
}if(Q!=0){var G=P.minAge;
var F=P.maxAge;
if(angular.isDefined(G)&&angular.isDefined(F)&&G!==null&&F!=null){var J=P.minAge[0].label;
var L=P.maxAge[0].label;
y.mindobDate=new Date(y.today.getFullYear()-L,y.today.getMonth(),y.today.getDay());
y.maxdobDate=new Date(y.today.getFullYear()-J,y.today.getMonth(),y.today.getDay())
}}m(E);
w(N);
y.languageList=H.sort(j.predicateBy("label"));
B(K);
j.unMaskLoading()
},function(){j.unMaskLoading()
})
}function u(){C()
}y.employee={};
function C(){y.isSuperAdmin=false;
y.employee={};
y.isCreate=false;
y.submitted=false;
y.policyHoldersMap={};
y.hasImage={};
y.isDepInvalid=true;
y.employeeList=[];
y.notUniqueEmpname=false;
y.isEmailValidate=true;
y.isWorkEmailValidate=true;
y.joiningDateValidate=true;
y.ipValidate=true;
y.isfamilyNameDuplicate=true;
y.iseditfamilyNameDuplicate=true;
y.displayEmployeeFlag="view";
y.isInValidSearch=false;
y.educationIndex=0;
y.hasAnyEdu=false;
y.degreeReq=false;
y.edusubmitted=false;
y.employee.edu=[];
y.employee.edu[y.educationIndex]={};
y.expIndex=0;
y.hasAnyExp=false;
y.prevCmpnyReq=false;
y.expsubmitted=false;
y.employee.exp=[];
y.employee.exp[y.expIndex]={};
y.familyIndex=0;
y.hasAnyFamily=false;
y.familynameReq=false;
y.familysubmitted=false;
y.employee.family=[];
y.employee.family[y.familyIndex]={index:"F0"};
y.policyIndex=0;
y.hasAnypolicy=false;
y.policynameReq=false;
y.policysubmitted=false;
y.employee.policy=[];
y.employee.policy[y.policyIndex]={status:"Active"};
y.wasHKGEmp=false;
y.shiftList=[];
y.formats=["dd-MMMM-yyyy","yyyy/MM/dd","shortDate","MM/dd/yyyy"];
y.format=j.dateFormat;
y.hasAnyDoc=false;
$("#input_empReportsTo").select2("data",undefined);
$("#input_empReportsTo").select2("val",undefined);
$("#input_empcurrentaddress").select2("data",undefined);
$("#input_empcurrentaddress").select2("val",undefined);
$("#input_empnativeaddress").select2("data",undefined);
$("#input_empnativeaddress").select2("val",undefined);
y.personalData={};
y.contactData={};
y.identificationData={};
y.otherData={};
y.hkgworkData={};
y.educationData=[];
y.educationData[y.educationIndex]={index:y.educationIndex};
y.experienceData=[];
y.experienceData[y.expIndex]={index:y.expIndex};
y.familyData=[];
y.familyData[y.familyIndex]={index:y.familyIndex};
y.policyData=[];
y.policyData[y.policyIndex]={index:y.policyIndex};
y.personaldbType={};
y.contactdbType={};
y.identificationdbType={};
y.otherdbType={};
y.hkgworkdbType={};
y.educationdbType={};
y.experiencedbType={};
y.familydbType={};
y.policydbType={};
y.languageList=[]
}y.setYearOfPassing=function(G){G=new Date(G);
y.yearOfPassing=[];
var E=angular.copy(G.getFullYear());
var D=y.today.getFullYear();
var F;
for(F=D;
F>E;
F--){y.yearOfPassing.push(F)
}};
y.phoneValidate=function(D){if(D.length>=10){if(!z(D)){y.isPhoneValidate=false;
return
}else{y.isPhoneValidate=true
}}else{y.isPhoneValidate=false;
return
}};
y.altPhoneValidate=function(D){if(!!D){if(D.length>=10){if(!z(D)){y.isaltPhoneValidate=false;
return
}else{y.isaltPhoneValidate=true
}}else{y.isaltPhoneValidate=false;
return
}}else{y.isaltPhoneValidate=true
}};
function z(D){var E=/^(\+91-|\+91|0|\0)?\d{10}$/;
D=D.replace(/ /g,"");
if(!E.test(D)){return false
}else{return true
}}function m(E){if(E!=null){y.empTypes=[];
if(E.EMPTYPE!=null&&angular.isDefined(E.EMPTYPE)&&E.EMPTYPE.length>0){angular.forEach(E.EMPTYPE,function(F){F.label=j.translateValue("EMPTYPE."+F.label);
F.label=F.label.substring(0,j.maxValueForTruncate)+"...";
y.empTypes.push(F);
y.empTypes=s(y.empTypes,["-isOftenUsed","shortcutCode","value"])
})
}y.bloodGroups=[];
if(E.BG!=null&&angular.isDefined(E.BG)&&E.BG.length>0){angular.forEach(E.BG,function(F){F.label=j.translateValue("BG."+F.label);
F.label=F.label.substring(0,j.maxValueForTruncate)+"...";
y.bloodGroups.push(F);
y.bloodGroups=s(y.bloodGroups,["-isOftenUsed","shortcutCode","value"])
})
}y.maritalStatusList=[];
if(E.MS!=null&&angular.isDefined(E.MS)&&E.MS.length>0){angular.forEach(E.MS,function(F){F.label=j.translateValue("MS."+F.label);
F.label=F.label.substring(0,j.maxValueForTruncate)+"...";
y.maritalStatusList.push(F);
y.maritalStatusList=s(y.maritalStatusList,["-isOftenUsed","shortcutCode","value"])
})
}y.casteList=[];
if(E.CASTE!=null&&angular.isDefined(E.CASTE)&&E.CASTE.length>0){angular.forEach(E.CASTE,function(F){F.label=j.translateValue("CASTE."+F.label);
F.label=F.label.substring(0,j.maxValueForTruncate)+"...";
y.casteList.push(F);
y.casteList=s(y.casteList,["-isOftenUsed","shortcutCode","value"])
})
}y.expDepList=[];
if(E.DEG!=null&&angular.isDefined(E.DEG)&&E.DEG.length>0){angular.forEach(E.DEG,function(F){F.label=j.translateValue("DEG."+F.label);
F.label=F.label.substring(0,j.maxValueForTruncate)+"...";
y.expDepList.push(F);
y.expDepList=s(y.expDepList,["-isOftenUsed","shortcutCode","value"])
})
}y.policyCompanyList=[];
if(E.POLICYCMPNY!=null&&angular.isDefined(E.POLICYCMPNY)&&E.POLICYCMPNY.length>0){angular.forEach(E.POLICYCMPNY,function(F){F.label=j.translateValue("POLICYCMPNY."+F.label);
F.label=F.label.substring(0,j.maxValueForTruncate)+"...";
y.policyCompanyList.push(F);
y.policyCompanyList=s(y.policyCompanyList,["-isOftenUsed","shortcutCode","value"])
})
}y.nationalityMap=[];
if(E.NTNLTY!=null&&angular.isDefined(E.NTNLTY)&&E.NTNLTY.length>0){angular.forEach(E.NTNLTY,function(F){F.label=j.translateValue("NTNLTY."+F.label);
F.label=F.label.substring(0,j.maxValueForTruncate)+"...";
y.nationalityMap.push(F);
y.nationalityMap=s(y.nationalityMap,["-isOftenUsed","shortcutCode","value"])
})
}y.educationDegreeMap=[];
if(E.EDUDEG!=null&&angular.isDefined(E.EDUDEG)&&E.EDUDEG.length>0){angular.forEach(E.EDUDEG,function(F){F.label=j.translateValue("EDUDEG."+F.label);
F.label=F.label.substring(0,j.maxValueForTruncate)+"...";
y.educationDegreeMap.push(F);
y.educationDegreeMap=s(y.educationDegreeMap,["-isOftenUsed","shortcutCode","value"])
})
}y.mediumMap=[];
if(E.MDIUM!=null&&angular.isDefined(E.MDIUM)&&E.MDIUM.length>0){angular.forEach(E.MDIUM,function(F){F.label=j.translateValue("MDIUM."+F.label);
y.mediumMap.push(F);
y.mediumMap=s(y.mediumMap,["-isOftenUsed","shortcutCode","value"])
})
}y.occupationList=[];
if(E.OCCUPSN!=null&&angular.isDefined(E.OCCUPSN)&&E.OCCUPSN.length>0){angular.forEach(E.OCCUPSN,function(F){F.label=j.translateValue("OCCUPSN."+F.label);
F.label=F.label.substring(0,j.maxValueForTruncate)+"...";
y.occupationList.push(F);
y.occupationList=s(y.occupationList,["-isOftenUsed","shortcutCode","value"])
})
}y.relationList=[];
if(E.RELESN!=null&&angular.isDefined(E.RELESN)&&E.RELESN.length>0){angular.forEach(E.RELESN,function(F){F.label=j.translateValue("RELESN."+F.label);
F.label=F.label.substring(0,j.maxValueForTruncate)+"...";
y.relationList.push(F);
y.relationList=s(y.relationList,["-isOftenUsed","shortcutCode","value"])
})
}y.universityList=[];
if(E.UNI!=null&&angular.isDefined(E.UNI)&&E.UNI.length>0){angular.forEach(E.UNI,function(F){F.label=j.translateValue("UNI."+F.label);
F.label=F.label.substring(0,j.maxValueForTruncate)+"...";
y.universityList.push(F);
y.universityList=s(y.universityList,["-isOftenUsed","shortcutCode","value"])
})
}y.otherDetailsEmpMap=[];
if(E.EMPOTHRDTILS!=null&&angular.isDefined(E.EMPOTHRDTILS)&&E.EMPOTHRDTILS.length>0){angular.forEach(E.EMPOTHRDTILS,function(F){F.label=j.translateValue("EMPOTHRDTILS."+F.label);
F.label=F.label.substring(0,j.maxValueForTruncate)+"...";
y.otherDetailsEmpMap.push(F);
y.otherDetailsEmpMap=s(y.otherDetailsEmpMap,["-isOftenUsed","shortcutCode","value"])
})
}if(angular.isDefined(y.otherDetailsEmpMap)&&y.otherDetailsEmpMap!==null){for(var D=0;
D<y.otherDetailsEmpMap.length;
D++){y.otherDetailsEmpMap[D].isActive=false
}}y.employee.gender="male";
y.employee.isNativeAddressSame=true
}}function w(D){y.allLocations=D.sort(j.predicateBy("label"));
angular.forEach(y.allLocations,function(E){y.select2Locations.push({id:E.value,text:E.label})
});
$("#location").select2("val",null)
}y.retrieveAvatar=function(){return j.appendAuthToken(j.apipath+"employee/retrieve/avatar?decache="+j.randomCount)
};
y.setPassingYear=function(E){if(E&&angular.isDefined(y.passingYears)){y.passingYearArray=[];
for(var D=0;
D<y.passingYears.length;
D++){if(E<y.passingYears[D].label){y.passingYearArray.push(y.passingYears[D])
}}}};
y.updateYearsOfPassing=function(){var D=y.employee.dob.getYear()+1900;
if(y.employee.dob.getYear()){y.setPassingYear(D)
}};
y.changeholdername=function(){if(!angular.isDefined(y.employee.empName)){delete y.policyHoldersMap.E0
}else{y.policyHoldersMap.E0=y.employee.empName
}};
y.multiLocations={multiple:false,closeOnSelect:false,placeholder:"Select District",allowClear:true,data:function(){return{results:y.select2Locations}
}};
y.cancelEditProfile=function(){i.path("dashboard")
};
y.multiLocations1={multiple:false,closeOnSelect:false,placeholder:"Select District",allowClear:true,data:function(){return{results:y.select2Locations}
}};
y.scrollTo=function(M){var K=I();
var D=N(M);
var E=D>K?D-K:K-D;
if(E<100){scrollTo(0,D-60);
return
}var H=Math.round(E/100);
if(H>=20){H=20
}var G=Math.round(E/25);
var L=D>K?K+G:K-G;
var F=0;
if(D>K){for(var J=K;
J<D;
J+=G){setTimeout("window.scrollTo(0, "+(L-60)+")",F*H);
L+=G;
if(L>D){L=D
}F++
}return
}for(var J=K;
J>D;
J-=G){setTimeout("window.scrollTo(0, "+(L-60)+")",F*H);
L-=G;
if(L<D){L=D
}F++
}function I(){if(self.pageYOffset){return self.pageYOffset
}if(document.documentElement&&document.documentElement.scrollTop){return document.documentElement.scrollTop
}if(document.body.scrollTop){return document.body.scrollTop
}return 0
}function N(O){var R=document.getElementById(O);
var Q=R.offsetTop;
var P=R;
while(P.offsetParent&&P.offsetParent!=document.body){P=P.offsetParent;
Q+=P.offsetTop
}return Q
}};
function B(F){y.setYearOfPassing(F.dob);
if(F.companyId==0){y.isSuperAdmin=true
}else{y.isSuperAdmin=false
}y.isCreate=false;
j.editMyProfile=true;
y.searchtext=undefined;
y.displayEmployeeFlag="view";
y.flag=true;
if(F.nativepincode===null){F.nativepincode=undefined
}if(F.altphnno===null){F.altphnno=undefined
}if(angular.isDefined(F.profileImageName)&&F.profileImageName!=null&&F.profileImageName.length>0){y.hasImage[F.id]=true
}y.policyHoldersMap.E0=F.empName;
for(var E=0;
E<F.family.length;
E++){if(angular.isDefined(F.family[E].familyImageName)&&F.family[E].familyImageName!=null&&F.family[E].familyImageName.length>0){y.hasImage[F.family[E].id]=true
}y.policyHoldersMap["F"+E]=F.family[E].firstName
}if(angular.isDefined(F.edu)&&F.edu.length>1){y.hasAnyEdu=true
}if(angular.isDefined(F.exp)&&F.exp.length>1){y.hasAnyExp=true
}if(angular.isDefined(F.family)&&F.family.length>1){y.hasAnyFamily=true
}if(angular.isDefined(F.policy)&&F.policy.length>1){y.hasAnyPolicy=true
}if(angular.isDefined(F.otherdocs)&&F.otherdocs.length>0){y.hasAnyDoc=true
}y.invalidParent=false;
y.isDepInvalid=false;
if(angular.isDefined(F.personalCustom)&&F.personalCustom!=null){y.personalData=angular.copy(F.personalCustom)
}if(angular.isDefined(F.contactCustom)&&F.contactCustom!=null){y.contactData=angular.copy(F.contactCustom)
}if(angular.isDefined(F.identificationCustom)&&F.identificationCustom!=null){y.identificationData=angular.copy(F.identificationCustom)
}if(angular.isDefined(F.otherCustom)&&F.otherCustom!=null){y.otherData=angular.copy(F.otherCustom)
}if(angular.isDefined(F.hkgworkCustom)&&F.hkgworkCustom!=null){y.hkgworkData=angular.copy(F.hkgworkCustom)
}for(var E=0;
E<F.edu.length;
E++){if(angular.isDefined(F.edu[E].educationCustom)&&F.edu[E].educationCustom!=null){y.educationData[E]=angular.copy(F.edu[E].educationCustom)
}else{y.educationData[E]={}
}}for(var E=0;
E<F.exp.length;
E++){if(angular.isDefined(F.exp[E].experienceCustom)&&F.exp[E].experienceCustom!=null){y.experienceData[E]=angular.copy(F.exp[E].experienceCustom)
}else{y.experienceData[E]={}
}}for(var E=0;
E<F.family.length;
E++){if(angular.isDefined(F.family[E].familyCustom)&&F.family[E].familyCustom!=null){y.familyData[E]=angular.copy(F.family[E].familyCustom)
}else{y.familyData[E]={}
}}for(var E=0;
E<F.policy.length;
E++){if(angular.isDefined(F.policy[E].policyCustom)&&F.policy[E].policyCustom!=null){y.policyData[E]=angular.copy(F.policy[E].policyCustom)
}else{y.policyData[E]={}
}}y.employee=angular.copy(F);
$("#multiLocations1").select2("data",F.nativeaddress);
angular.forEach(y.select2Locations,function(H){if(H.id===F.nativeaddress){F.nativeaddress=H
}});
$("#input_empReportsTo").select2("data","");
y.names={id:F.reportsToId,text:F.reportsToName};
$("#input_empReportsTo").select2("data",y.names);
var D=new Date(y.employee.dob);
y.employee.dob=D;
var G=D.getYear()+1900;
y.setPassingYear(G);
y.isCreate=false;
y.viewAllEduDetails();
y.viewAllExpDetails();
y.viewAllFamilyDetails();
y.viewAllPolicyDetails();
angular.forEach(y.empTypes,function(H){if(H.value===y.employee.empType){y.employee.emplTypeLabel=H.label
}});
if(!y.employee.gender){y.employee.gender="male"
}y.oldname=y.employee.empName;
y.flag=true
}y.multiLocations1={multiple:false,closeOnSelect:false,placeholder:"Select District",allowClear:true,data:function(){return{results:y.select2Locations}
}};
y.addEmployee=function(E){y.submitted=true;
y.phoneValidate(y.employee.phnno);
y.altPhoneValidate(y.employee.altphnno);
y.validateEmail(y.employee.email);
var H=angular.copy(y.employee);
var D=true;
for(var K in E){if(E[K].$invalid){D=false;
break
}}var F=false;
if(!y.isDepInvalid&&!y.invalidname&&!y.notUniqueEmpname&&y.isPhoneValidate&&y.isaltPhoneValidate&&y.isEmailValidate&&y.isWorkEmailValidate&&y.isfamilyNameDuplicate&&!y.degreeReq&&!y.prevCmpnyReq&&!y.familynameReq&&!y.policynameReq&&y.ipValidate){F=true
}var L=function(M){j.randomCount=Math.random();
j.switchLanguage(y.employee.prefferedLang,j.session.companyId);
j.maskLoading();
j.mainMenu="";
j.childMenu="";
j.activateMenu();
y.entity="EMPLOYEE.";
y.today=new Date();
y.notAvailable="NA";
y.notAvailableSection="No Information Available for this section";
y.flag=false;
y.popover="<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td> \n '@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr> <tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr> </table> ";
y.select2Locations=[];
u();
n();
y.searchtext=undefined;
y.scrollTo("pageTop")
};
var G=function(){};
if(D&&!y.isDepInvalid&&!y.isSuperAdmin&&F){if(H.currentaddress instanceof Object){H.currentaddress=H.currentaddress.id
}if(H.nativeaddress instanceof Object){H.nativeaddress=H.nativeaddress.id
}var I={empName:H.empName,email:H.email,phnno:H.phnno,pincode:H.pincode,fulladdress:H.fulladdress,currentaddress:H.currentaddress,bloodGrp:H.bloodGrp,nationality:H.nationality,caste:H.caste,maritalstatus:H.maritalstatus,dob:H.dob,userId:H.userId,profileImageName:H.profileImageName,nativeaddress:H.currentaddress,altphnno:H.altphnno,gender:H.gender,prefferedLang:H.prefferedLang};
if(I.currentaddress instanceof Object){I.currentaddress=I.currentaddress.id
}q.updateProfile(I,L,G)
}if(y.isSuperAdmin){var J={empName:H.empName,dob:H.dob,gender:H.gender,userId:H.userId,profileImageName:H.profileImageName,profileImageId:H.profileImageId,prefferedLang:H.prefferedLang};
q.updateProfileOfSuperAdmin(J,L,G)
}};
y.openNativeAddressModal=function(D){if(!D){y.nativesubmitted=false;
$("#nativeAddressModal").modal("show")
}};
y.resetNativeAddress=function(){y.nativesubmitted=false;
y.employee.nativeaddress=undefined;
y.employee.nativefulladdress=undefined;
y.employee.nativepincode=undefined
};
y.cancelNativeAddress=function(){y.resetNativeAddress();
$("#nativeAddressModal").modal("hide");
j.removeModalOpenCssAfterModalHide()
};
y.okNativeAddress=function(D){y.nativesubmitted=true;
if(D.$valid){y.nativesubmitted=false;
$("#nativeAddressModal").modal("hide");
j.removeModalOpenCssAfterModalHide()
}};
y.viewAllEduDetails=function(){y.viewAllEdu=[];
$.each(y.employee.edu,function(E,G){var D=angular.copy(G);
D.index=E;
if(y.educationDegreeMap){for(var F=0;
F<y.educationDegreeMap.length;
F++){if(y.educationDegreeMap[F].value===D.degree){D.degreename=y.educationDegreeMap[F].label;
break
}}}if(y.universityList){for(var F=0;
F<y.universityList.length;
F++){if(y.universityList[F].value===D.university){D.universityname=y.universityList[F].label;
break
}}}if(y.mediumMap){for(var F=0;
F<y.mediumMap.length;
F++){if(y.mediumMap[F].value===D.medium){D.mediumname=y.mediumMap[F].label;
break
}}}for(var F=0;
F<y.yearOfPassing.length;
F++){if(y.yearOfPassing[F]===D.passingYear){D.yearname=y.yearOfPassing[F];
break
}}y.viewAllEdu.push(D)
})
};
y.viewAllExpDetails=function(){y.viewAllExp=[];
$.each(y.employee.exp,function(E,G){var D=angular.copy(G);
D.index=E;
if(y.expDepList){for(var F=0;
F<y.expDepList.length;
F++){if(y.expDepList[F].value===D.designation){D.designationname=y.expDepList[F].label;
break
}}}y.viewAllExp.push(D)
})
};
y.viewAllFamilyDetails=function(){y.viewAllFamily=[];
$.each(y.employee.family,function(E,G){var D=angular.copy(G);
D.index=E;
if(y.relationList){for(var F=0;
F<y.relationList.length;
F++){if(y.relationList[F].value===D.relation){D.relationname=y.relationList[F].label;
break
}}}if(y.bloodGroups){for(var F=0;
F<y.bloodGroups.length;
F++){if(y.bloodGroups[F].value===D.bloodGroup){D.bloodGroupname=y.bloodGroups[F].label;
break
}}}if(y.occupationList){for(var F=0;
F<y.occupationList.length;
F++){if(y.occupationList[F].value===D.occupation){D.occupationname=y.occupationList[F].label;
break
}}}y.viewAllFamily.push(D)
})
};
y.viewAllPolicyDetails=function(){y.viewAllPolicy=[];
$.each(y.employee.policy,function(E,H){var D=angular.copy(H);
D.index=E;
if(y.policyCompanyList){for(var G=0;
G<y.policyCompanyList.length;
G++){if(y.policyCompanyList[G].value===D.company){D.companyname=y.policyCompanyList[G].label;
break
}}}for(var F in y.policyHoldersMap){if(F===D.contactUser){D.holdername=y.policyHoldersMap[F];
break
}}y.viewAllPolicy.push(D)
})
};
y.doesEmployeeNameExist=function(D){if(D&&D.length>0){q.doesEmployeeNameExist(D,function(E){if(E.data&&D!=y.oldname){y.notUniqueEmpname=true
}else{y.notUniqueEmpname=false
}},function(){})
}else{y.notUniqueEmpname=false
}};
y.validateEmailId=function(D){var E=/^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
if(!E.test(D)){return false
}else{return true
}};
y.validateEmail=function(D){if(!!D){var E=y.validateEmailId(D);
if(!E){y.isEmailValidate=false
}else{y.isEmailValidate=true
}}else{y.isEmailValidate=true
}};
y.validateWorkEmailId=function(D){var E=y.validateEmailId(D);
if(!E){y.isWorkEmailValidate=false
}else{y.isWorkEmailValidate=true
}};
y.ValidateIPaddress=function(D){if(D!==undefined){var E=/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
if(D.match(E)){y.ipValidate=true
}else{y.ipValidate=false
}}};
y.open=function(D){D.preventDefault();
D.stopPropagation()
};
y.setMinDate=function(){if(y.employee.dob===""||y.employee.dob===undefined){y.minToDate=new Date()
}else{y.minToDate=y.employee.dob
}if(y.employee.dob>y.employee.joiningDate){y.employee.joiningDate=y.employee.dob
}};
y.uploadFile={target:j.apipath+"fileUpload/uploadFile",singleFile:true,testChunks:true,query:{fileType:y.seletedFileType,model:"Franchise"}};
y.profileImg={};
y.profileFileAdded=function(E,D){y.profileFlow=D;
y.uploadFile.query.fileType="PROFILE";
y.profileUploaded=false;
if((E.getExtension()!="jpg")&&(E.getExtension()!="jpeg")&&(E.getExtension()!="png")&&(E.getExtension()!="gif")){y.profileImg.invalidFileFlag=true;
y.profileImg.fileName=E.name
}else{if(E.size>10485760){y.profileImg.invalidFileSizeFlag=true;
return false
}}return !!{jpg:1,jpeg:1,gif:1,png:1}[E.getExtension()]
};
y.profileFileUploaded=function(G,E,H,J){var F=[G.name,L,y.seletedFileType];
var M="PROFILE";
y.hasImage[y.employee.id]=undefined;
y.profileUploaded=true;
if(y.saveEventFlag===false){y.saveFilesAndEvent(H)
}var L="Franchise";
var I=[G.name,M];
var K;
var D="true";
var F;
q.uploadFile(I,function(N){K=N.filename;
F=[G.name,L,K,D];
t.uploadFiles(F,function(O){y.employee.profileImageName=O.res
})
});
E.cancel()
};
y.profileFileRemove=function(){y.hasImage[y.employee.id]=undefined;
q.removeFileFromTemp(y.employee.profileImageName);
y.employee.profileImageName=undefined
};
y.familyImg={};
y.familyFileAdded=function(E,D){y.familyFlow=D;
y.uploadFile.query.fileType="PROFILE";
y.familyUploaded=false;
if((E.getExtension()!="jpg")&&(E.getExtension()!="jpeg")&&(E.getExtension()!="png")&&(E.getExtension()!="gif")){y.familyImg.invalidFileFlag=true;
y.familyImg.fileName=E.name
}else{if(E.size>10485760){y.familyImg.invalidFileSizeFlag=true;
return false
}}return !!{jpg:1,jpeg:1,gif:1,png:1}[E.getExtension()]
};
y.familyFileUploaded=function(E,F,D,G){if(G){y.hasImage[y.editfamily.id]=undefined;
y.editfamily.familyImageName=D
}else{y.employee.family[y.familyIndex].familyImageName=D
}y.familyUploaded=true;
if(y.saveEventFlag===false){y.saveFilesAndEvent(F)
}E.cancel()
};
y.familyFileRemove=function(D){y.hasImage[y.employee.family[D].id]=undefined;
q.removeFileFromTemp(y.employee.family[D].familyImageName);
y.employee.family[D].familyImageName=undefined
};
y.salaryslipImg={};
y.salaryslipFileAdded=function(E,D){y.salaryslipFlow=D;
y.uploadFile.query.fileType="SALARYSLIP";
y.salaryslipUploaded=false;
if((E.getExtension()!="jpg")&&(E.getExtension()!="jpeg")&&(E.getExtension()!="png")&&(E.getExtension()!="gif")&&(E.getExtension()!="pdf")){y.salaryslipImg.invalidFileFlag=true;
y.salaryslipImg.fileName=E.name
}else{if(E.size>10485760){y.salaryslipImg.invalidFileSizeFlag=true;
return false
}}return !!{jpg:1,jpeg:1,gif:1,png:1,pdf:1}[E.getExtension()]
};
y.salaryslipFileUploaded=function(E,F,D,G){if(G){y.editexp.salaryslipImageName=D
}else{y.employee.exp[y.expIndex].salaryslipImageName=D
}y.salaryslipUploaded=true;
if(y.saveEventFlag===false){y.saveFilesAndEvent(F)
}E.cancel()
};
y.salaryslipRemove=function(D){q.removeFileFromTemp(y.employee.exp[D].salaryslipImageName);
y.employee.exp[D].salaryslipImageName=undefined
};
y.otherdocsImg={};
y.employee.otherdocs=[];
y.otherDocIndex=0;
y.otherdocsFileAdded=function(E,D){y.otherdocsFlow=D;
y.uploadFile.query.fileType="OTHER";
y.otherdocsUploaded=false;
if((E.getExtension()!="jpg")&&(E.getExtension()!="jpeg")&&(E.getExtension()!="png")&&(E.getExtension()!="gif")&&(E.getExtension()!="pdf")&&(E.getExtension()!="txt")){y.otherdocsImg.invalidFileFlag=true;
y.otherdocsImg.fileName=E.name
}else{if(E.size>10485760){y.otherdocsImg.invalidFileSizeFlag=true;
return false
}}return !!{jpg:1,jpeg:1,gif:1,png:1,pdf:1,txt:1}[E.getExtension()]
};
y.otherdocsFileUploaded=function(E,F,D){y.employee.otherdocs[y.otherDocIndex]=D;
y.otherDocIndex=y.otherDocIndex+1;
y.hasAnyDoc=true;
y.otherdocsUploaded=true;
if(y.saveEventFlag===false){y.saveFilesAndEvent(F)
}};
y.otherdocRemove=function(E){q.removeFileFromTemp(y.employee.otherdocs[E]);
y.employee.otherdocs[E]=null;
y.employee.otherdocs.slice(E,1);
var D=0;
for(var F=0;
F<y.employee.otherdocs.length;
F++){var G=y.employee.otherdocs[F];
if(G!==null){D=D+1
}}if(D===0){y.employee.otherdocs=[];
y.hasAnyDoc=false;
$("#viewdocsModal").modal("hide");
j.removeModalOpenCssAfterModalHide()
}};
y.openViewDocs=function(){$("#viewdocsModal").modal("show")
};
y.isResigned=false;
y.getStatusValue=function(){y.isResigned=false;
for(var D=0;
D<y.statusList.length;
D++){if(y.statusList[D].value===y.employee.workstatus&&y.statusList[D].label==="Resigned"){y.isResigned=true;
$("#terminateEmployeeModal").modal("show");
break
}}};
y.beforeStatus=function(){y.beforeStatus=y.employee.workstatus
};
y.proceed=function(){var D={id:y.employee.id,relievingDate:y.employee.relievingDate,status:y.employee.workstatus};
q.terminateEmployee(D);
$("#terminateEmployeeModal").modal("hide");
j.removeModalOpenCssAfterModalHide();
y.resetPage()
};
y.cancelproceed=function(){y.employee.workstatus=y.beforeStatus;
$("#terminateEmployeeModal").modal("hide");
j.removeModalOpenCssAfterModalHide()
};
y.resetPage=function(){r.reload()
};
y.getProfileImageEmployee=function(){var D=y.employee.profileImageName;
D=D.substring(0,D.lastIndexOf("."));
D+="_T.jpg";
return j.appendAuthToken(j.apipath+"/employee/getimage?file_name="+D)
};
y.getProfileImageContact=function(D,E){var F="";
if(E){F=y.editfamily.familyImageName
}else{F=y.employee.family[D].familyImageName
}return j.appendAuthToken(j.apipath+"/employee/getimage?file_name="+F)
};
y.setViewFlag=function(D){y.displayEmployeeFlag=D
};
y.getSearchedEmployee=function(D){y.searchedEmployeeList=D;
y.setViewFlag("search")
};
y.openChangePassword=function(){y.passsubmitted=false;
$("#changePasswordModal").modal("show")
};
y.resetPassword=function(){y.confirmpassword=undefined;
y.password=undefined;
y.passsubmitted=false;
$("#changePasswordModal").modal("hide");
j.removeModalOpenCssAfterModalHide()
};
y.confirmPasswordValidation=function(){if(!angular.equals(y.password,y.confirmpassword)){y.isMatched=true
}else{y.isMatched=false
}};
y.changePassword=function(G){y.passsubmitted=true;
if(G.$valid&&!y.isMatched){j.maskLoading();
var H=function(){j.unMaskLoading();
$("#changePasswordModal").modal("hide");
j.removeModalOpenCssAfterModalHide();
y.passsubmitted=false
};
var E=function(){j.unMaskLoading();
$("#changePasswordModal").modal("hide");
j.removeModalOpenCssAfterModalHide();
y.passsubmitted=false
};
var D=j.session.id;
var F={id:D.toString(),password:y.password,userid:y.employee.userId};
q.changePassword(F,H,E)
}};
$(function(){$(window).bind("mousewheel",function(D,E){$("#sidemenu").height($("#mainPanel").height())
});
$(window).bind("scroll",function(D){$("#sidemenu").height($("#mainPanel").height())
})
});
j.unMaskLoading()
}])
});