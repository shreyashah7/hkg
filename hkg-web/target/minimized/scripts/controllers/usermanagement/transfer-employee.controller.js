define(["hkg","transferemployeeService","employeeService","franchiseService","leaveWorkflowService","designationService","departmentService"],function(b,f,a,g,d,c,e){b.register.controller("TransferEmployeeController",["$rootScope","$scope","$filter","TransferEmployee","Employee","$location","$anchorScroll","FranchiseService","LeaveWorkflow","Designation","DepartmentService","DynamicFormService","$route","$timeout",function(r,t,p,o,m,k,l,n,h,u,i,s,q,j){r.maskLoading();
r.mainMenu="manageLink";
r.childMenu="manageTransferEmployees";
r.activateMenu();
t.entity="TRANSFEREMPLOYEE.";
t.departmentList=[];
t.shiftList=[];
t.selectedDepartments="";
t.selectedShifts="";
t.transfer={};
t.submitted=false;
t.$on("$viewContentLoaded",function(){t.retrieveShiftsWithDepartmentName();
t.retrieveEmployeeStatus();
t.retrieveUsers();
t.retrieveDepartments()
});
t.retrieveShiftsWithDepartmentName=function(){o.retrieveShiftsWithDepartmentName(function(x){t.deptShiftMap=x.data;
for(var w in x.data){if(w){var v=w.split("$@$");
t.departmentList.push({id:parseInt((v[0])),text:v[1]})
}}},function(){})
};
t.$watch("selectedDepartments",function(w){if(t.selectedDepartments!==undefined&&t.selectedDepartments instanceof Object===true&&angular.isArray(t.selectedDepartments)){var v="";
angular.forEach(t.selectedDepartments,function(y,x){if(x===0){v=y.id
}else{v+=","+y.id
}});
t.selectedDepartments=v
}else{t.retrieveShiftsFromDept(t.selectedDepartments)
}});
t.retrieveShiftsFromDept=function(w){if(w!==undefined&&w instanceof Object===false&&w!==""){if(t.shiftList.length>0){t.shiftList.splice(0,t.shiftList.length)
}var v=w.toString().split(",");
angular.forEach(v,function(z){for(var y in t.deptShiftMap){var x=y.split("$@$");
if(x[0]===z){t.tempshiftList=[];
t.tempshiftMap=t.deptShiftMap[y];
angular.forEach(t.tempshiftMap,function(A){t.tempshiftList.push({id:A.value,text:A.label})
});
if(t.tempshiftList.length!==0){t.shiftList.push({});
t.shiftList[t.shiftList.length-1].text=x[1];
t.shiftList[t.shiftList.length-1].children=t.tempshiftList
}}}})
}};
t.multiDepartments={allowClear:true,multiple:true,closeOnSelect:false,placeholder:"Select Department",data:t.departmentList,initSelection:function(v,x){var w=[];
x(w)
},formatResult:function(v){return v.text
},formatSelection:function(v){return v.text
}};
t.multiShifts={multiple:true,closeOnSelect:false,placeholder:"Select Shifts",data:t.shiftList,initSelection:function(v,x){var w=[];
x(w)
},formatResult:function(v){return v.text
}};
$(document).on("select2-selecting","#input_empReportsTo",function(v){t.obj={id:v.val,text:v.object.text};
t.flag=true;
angular.forEach(t.newEmployeeList,function(w){w.reportsToId=v.val;
var x="#input_empReportsToTable"+w.id;
$(x).select2("val",[])
})
});
$(document).on("select2-removing","#selectDepartment",function(A){var z=0;
var y=$("#selectShift").select2("data");
var w=[];
t.selectedShifts="";
var B=[];
var v=[];
for(var E in t.deptShiftMap){var C=E.split("$@$");
if(C[0]===A.val.toString()){angular.forEach(t.deptShiftMap[E],function(F){angular.forEach(y,function(G){if(G.id===parseInt((F.value))){for(var H=0;
H<w.length;
H++){if(w[H]===G){z++
}}if(z===0){w.push(G);
B.push(G.id)
}}})
})
}}Array.prototype.remove=function(){var I,G=arguments,F=G.length,H;
while(F&&this.length){I=G[--F];
while((H=this.indexOf(I))!==-1){this.splice(H,1)
}}return this
};
var D=[];
angular.forEach(y,function(F){D.push(F.id);
v.push(F)
});
angular.forEach(y,function(F){angular.forEach(B,function(G){if(F.id===G){for(var H=0;
H<D.length;
H++){if(D[H]===G){D.remove(G)
}}for(var H=0;
H<v.length;
H++){if(v[H].id===G){v.remove(v[H])
}}}})
});
for(var x=0;
x<D.length;
x++){if(t.selectedShifts.length===0){t.selectedShifts=t.selectedShifts+D[x]
}else{t.selectedShifts=t.selectedShifts+","+D[x]
}}$("#selectShift").select2("data",v)
});
t.changeShiftByToDept=function(){if(t.transfer.toDept){for(var w in t.deptShiftMap){var v=w.split("$@$");
if(v[0]===t.transfer.toDept){t.toShiftList=[];
t.tempshiftMap=t.deptShiftMap[w];
angular.forEach(t.tempshiftMap,function(x){t.toShiftList.push({id:parseInt(x.value),text:x.label})
})
}}}angular.forEach(t.newEmployeeList,function(x){x.workshift="";
x.empShift=angular.copy(t.toShiftList)
});
t.changeEmployeeDepartment()
};
t.open=function(v){v.preventDefault();
v.stopPropagation()
};
t.format=r.dateFormat;
t.changeEmployeeDepartment=function(){if(t.newEmployeeList&&t.transfer){angular.forEach(t.newEmployeeList,function(v){v.departmentId=parseInt((t.transfer.toDept))
})
}};
t.changeEmployeeShift=function(){if(t.newEmployeeList&&t.transfer){angular.forEach(t.newEmployeeList,function(v){v.workshift=parseInt((t.transfer.toShift))
})
}};
t.changeEmployeeReportsToId=function(){if(t.newEmployeeList&&t.transfer){angular.forEach(t.newEmployeeList,function(v){v.reportsToId=parseInt(t.transfer.reportsToId)
})
}};
t.changeEmployeeStatus=function(){if(t.newEmployeeList&&t.transfer){angular.forEach(t.newEmployeeList,function(v){v.workstatus=parseInt(t.transfer.workstatus)
})
}if(t.transfer.workstatus!==3){t.transfer.relievingDate=null;
if(t.newEmployeeList&&t.transfer){angular.forEach(t.newEmployeeList,function(v){v.relievingDate=null
})
}}t.count=0;
angular.forEach(t.newEmployeeList,function(v){if(v.workstatus===3){t.count++
}})
};
t.changeEmployeeRelievingDate=function(){if(t.newEmployeeList&&t.transfer){angular.forEach(t.newEmployeeList,function(v){v.relievingDate=t.transfer.relievingDate
})
}};
t.retrieveEmployeeStatus=function(){o.retrieveEmployeeStatus(function(v){if(v){t.statusList=v.data
}},function(){console.log("failed to load employee status")
})
};
t.autoCompleteApprover={multiple:true,closeOnSelect:false,allowClear:true,placeholder:"Select approvers",maximumSelectionSize:1,initSelection:function(v,w){},formatResult:function(v){return v.text
},formatSelection:function(v){return v.text
},query:function(y){var x=y.term;
t.names=[];
var z=function(A){if(A.length==0){y.callback({results:t.names})
}else{t.names=A;
angular.forEach(A,function(B){t.names.push({id:B.value+":"+B.description,text:B.label})
});
y.callback({results:t.names})
}};
var v=function(){};
if(x.substring(0,2)=="@E"||x.substring(0,2)=="@e"){var w=y.term.slice(2);
h.retrieveUserList(w.trim(),z,v)
}else{if(x.substring(0,2)=="@D"||x.substring(0,2)=="@d"){var w=y.term.slice(2);
h.retrieveDepartmentList(w.trim(),z,v)
}else{if(x.length>0){var w=x;
h.retrieveUserList(w.trim(),z,v)
}else{y.callback({results:t.names})
}}}}};
t.cancelSelectedValues=function(){t.selectedDepartments="";
t.selectedShifts="";
t.submitted=false;
t.transfer={};
if(t.shiftList.length>0){t.shiftList.splice(0,t.shiftList.length)
}if(t.newEmployeeList){t.newEmployeeList={}
}t.transferEmployeeForm.$setPristine()
};
t.selectDeselectEmployee=function(){if(t.newEmployeeList){if(t.transfer.selectAll){angular.forEach(t.newEmployeeList,function(v){v.selected=true
})
}else{angular.forEach(t.newEmployeeList,function(v){v.selected=false
})
}}};
t.retrieveEmployeesByShiftAndByDept=function(){t.submitted=true;
if(!t.selectedDepartments&&t.submitted){t.transferEmployeeForm.selectShift.$invalid=true;
t.transferEmployeeForm.selectShift.$valid=false;
t.transferEmployeeForm.selectShift.$error.required=true
}if(t.selectedDepartments&&t.selectedShifts!==undefined&&t.selectedShifts instanceof Object===false&&!angular.isArray(t.selectedShifts)){var v=t.selectedShifts.split(",");
t.send={shiftIds:t.selectedShifts,deptIds:t.selectedDepartments};
o.retrieveEmployeesByShiftByDept(t.send,function(w){t.orginalEmployeeList=w.data;
t.newEmployeeList=angular.copy(t.orginalEmployeeList);
t.employeeDepartment=t.departmentList;
if(t.newEmployeeList!==null&&t.newEmployeeList){t.transfer.selectAll=true;
angular.forEach(t.newEmployeeList,function(z){z.selected=true;
if(z.departmentId){for(var y in t.deptShiftMap){var x=y.split("$@$");
if(x[0]===z.departmentId.toString()){z.empShift=[];
t.tempshiftMap=t.deptShiftMap[y];
angular.forEach(t.tempshiftMap,function(A){z.empShift.push({id:A.value,text:A.label})
})
}}}t.flag=false;
z.select2Config={multiple:true,closeOnSelect:false,allowClear:true,placeholder:"Select approvers",maximumSelectionSize:1,initSelection:function(C,F){var D=[];
var B="";
if(t.flag){B=t.obj.id;
D.push(t.obj)
}if(!!z.reportsToId&&!t.flag){B=z.reportsToId;
for(var A=0;
A<t.users.length;
A++){var E=t.users[A].value+":"+t.users[A].description;
if(E==z.reportsToId){D.push({id:z.reportsToId,text:t.users[A].label});
break
}}for(var A=0;
A<t.Alldepartments.length;
A++){var E=t.Alldepartments[A].value+":"+t.Alldepartments[A].description;
if(E==z.reportsToId){D.push({id:z.reportsToId,text:t.Alldepartments[A].label});
break
}}}F(D);
z.reportsToId=B
},formatResult:function(A){return A.text
},formatSelection:function(A){return A.text
},query:function(D){var C=D.term;
t.names=[];
var E=function(F){if(F.length==0){D.callback({results:t.names})
}else{t.names=F;
angular.forEach(F,function(G){t.names.push({id:G.value+":"+G.description,text:G.label})
});
D.callback({results:t.names})
}};
var A=function(){};
if(C.substring(0,2)=="@E"||C.substring(0,2)=="@e"){var B=D.term.slice(2);
h.retrieveUserList(B.trim(),E,A)
}else{if(C.substring(0,2)=="@D"||C.substring(0,2)=="@d"){var B=D.term.slice(2);
h.retrieveDepartmentList(B.trim(),E,A)
}else{if(C.length>0){var B=C;
h.retrieveUserList(B.trim(),E,A)
}else{D.callback({results:t.names})
}}}}}
})
}},function(){})
}else{if(t.selectedShifts===undefined||t.selectedShifts instanceof Object===true&&angular.isArray(t.selectedShifts)){t.transferEmployeeForm.selectShift.$invalid=true;
t.transferEmployeeForm.selectShift.$valid=false;
t.transferEmployeeForm.selectShift.$error.required=true
}}};
t.retrieveUsers=function(){h.retrieveUserList("",function(v){t.users=v
})
};
t.retrieveDepartments=function(){h.retrieveDepartmentList("",function(v){t.Alldepartments=v
})
};
t.changeEmployeeSiftDropdown=function(v){var y=t.newEmployeeList[v].departmentId;
if(y){for(var x in t.deptShiftMap){var w=x.split("$@$");
if(w[0]==y){t.newEmployeeList[v].empShift=[];
t.tempshiftMap=t.deptShiftMap[x];
angular.forEach(t.tempshiftMap,function(z){t.newEmployeeList[v].empShift.push({id:z.value,text:z.label})
})
}}}};
t.changeEmployeeRelievingTableDateOnStatus=function(v){if(t.newEmployeeList[v].workstatus!==3){t.newEmployeeList[v].relievingDate=null
}t.count=0;
angular.forEach(t.newEmployeeList,function(w){if(w.workstatus===3){t.count++
}})
};
t.employeeTransfer=function(){if(t.employeeTableForm.$valid){angular.forEach(t.newEmployeeList,function(v){if(v.selected){angular.forEach(t.orginalEmployeeList,function(w){if(v.id===w.id){w.departmentId=v.departmentId;
w.workshift=v.workshift;
w.reportsToId=v.reportsToId;
w.workstatus=v.workstatus;
if(v.workstatus===3){w.relievingDate=v.relievingDate
}}})
}});
o.transferEmployeeByCriteria(t.orginalEmployeeList,function(){console.log("success")
},function(){console.log("faliure")
})
}else{console.log("inside not valid")
}};
if(r.getCurrentServerDate()){t.minRelievedDate=r.getCurrentServerDate()
}else{t.minRelievedDate=new Date()
}t.empDtOptions={aoColumnDefs:[{bSortable:false,aTargets:[-1,-2,-3,-4]}]};
r.unMaskLoading()
}])
});