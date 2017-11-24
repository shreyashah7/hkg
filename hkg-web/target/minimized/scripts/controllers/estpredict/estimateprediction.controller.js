define(["hkg","dynamicForm","rapCalcyDirective","customFieldService","planService","rapCalcyService"],function(a){a.register.controller("EstPredctController",["$rootScope","$scope","DynamicFormService","CustomFieldService","PlanService","RapCalcyService",function(i,l,k,j,n,f){i.mainMenu="stockLink";
i.childMenu="estimatePrediction";
l.entity="ESTIMATEPREDCT.";
i.activateMenu();
l.editIndex=0;
l.vals={};
l.index=0;
l.tab={index:0};
l.rapcalcyflag=false;
l.previousPacketNumber="";
var g={ID:"id",PLAN_ID:"planId",TAG:"tag",INDEX:"index",CARAT:"size$DRP$long",GRAPH_CARAT:"gsize$DRP$long",EMP_ID:"empId",EMP_NAME:"empName",IS_ACTIVE:"isActive",IN_STOCK_OF:"packetInStockOf",COPIED_FROM:"copiedFrom",EMP_NAME_MODEL:"empname$DRP$String"};
var c=[{index:0,planId:1,tag:"A"}];
l.planToIds={};
l.tagVal={val:"A"};
l.headerList=["Carat","GraphCarat","Plan Id","Tag"];
l.headerListModel={Carat:"size$DRP$long",GraphCarat:"gsize$DRP$long","Plan Id":"planId",Tag:"tag"};
l.conditions=[{cond:{"carat_type$DRP$Long":"Expected","size$DRP$long":0.001,"fluroscene$DRP$String":18,"clarity$DRP$String":9,"color$DRP$String":14},def:{field1$DRP$Long:{id:28,text:"3-F3"}}}];
n.retrieveModifiers(function(o){l.modifiers=angular.copy(o.data)
});
j.retrieveDesignationBasedFields("predictionEstimate",function(o){var p=k.retrieveSectionWiseCustomFieldInfo("roughCalcyEntity");
p.then(function(t){l.generaRoughCalcyTemplate=angular.copy(t.genralSection);
var s=[];
l.response=angular.copy(o);
Object.keys(o).map(function(v,w){angular.forEach(this[v],function(x){if(v==="RoughCalcy"){s.push({RoughCalcy:x})
}})
},o);
l.generaRoughCalcyTemplate=k.retrieveCustomData(l.generaRoughCalcyTemplate,s);
angular.forEach(l.generaRoughCalcyTemplate,function(v){v.isViewFromDesignation=false
});
l.planTemplate=angular.copy(t.genralSection);
var r=[];
var q=Object.keys(o).map(function(v,w){angular.forEach(this[v],function(x){if(v==="RoughCalcy"){r.push({RoughCalcy:x})
}})
},o);
l.planTemplate=k.retrieveCustomData(l.planTemplate,r);
if((l.planTemplate!==undefined&&l.planTemplate!==null)){var u=k.mergeAndSortTemplates(l.planTemplate,[]);
angular.forEach(u,function(v){if(v.label!=="Carat Type"&&l.headerList.indexOf(v.label)===-1&&v.model!==g.EMP_NAME_MODEL){l.headerList.push(v.label);
l.headerListModel[v.label]=v.model
}if(v.model===g.EMP_NAME_MODEL){l.headerList.push("Emp Name");
l.headerListModel["Emp Name"]="empName"
}})
}},o)
},function(){i.unMaskLoading();
var p="Failed to retrieve data";
var o=i.error;
i.addMessage(p,o)
});
l.planData=[];
l.planList=[];
l.orgPlanList=[];
l.addPlan=function(){if(!m()){l.editIndex=l.planList.length;
l.tab={index:++l.index,isActive:true,empId:i.session.id,empName:i.session.userCode+"-"+i.session.firstName+" "+i.session.lastName,inStockOf:i.session.id};
l.planList[l.editIndex]=angular.copy(l.tab);
l.orgPlanList[l.editIndex]=angular.copy(l.tab);
if(l.planList[l.editIndex][g.PLAN_ID]===null||l.planList[l.editIndex][g.PLAN_ID]===undefined){for(var o=l.planList.length-2;
o>=0;
o--){var p=l.planList[o];
if(p.empId===i.session.id){if(l.tagList.indexOf(p.tag)>=l.tagList.indexOf(l.tagVal.val)){l.orgPlanList[l.editIndex][g.PLAN_ID]=l.planList[l.editIndex][g.PLAN_ID]=parseInt(p.planId+1);
l.orgPlanList[l.editIndex][g.TAG]=l.planList[l.editIndex][g.TAG]="A"
}else{if(l.planList[l.editIndex][g.PLAN_ID]===null||l.planList[l.editIndex][g.PLAN_ID]===undefined){l.orgPlanList[l.editIndex][g.PLAN_ID]=l.planList[l.editIndex][g.PLAN_ID]=p.planId;
l.orgPlanList[l.editIndex][g.TAG]=l.planList[l.editIndex][g.TAG]=l.tagList[parseInt(l.tagList.indexOf(p.tag)+1)]
}}break
}}}l.tab=angular.copy(l.orgPlanList[l.editIndex]);
d()
}};
l.tagValue=10;
l.prepareTagList=function(t){l.tagValue=parseInt(t);
var o=0;
var s=1;
var p=["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"];
var v=[""];
l.tagList=[];
while(s<=l.tagValue){var u=v[o];
for(var q in p){var r=u+p[q];
v.push(r);
s++;
l.tagList.push(r);
if(s===l.tagValue+1){break
}}o++
}};
l.$watch("tab",function(q){if(q!==undefined){var t=[];
var p=angular.copy(q[g.CARAT]);
var o=angular.copy(q[g.GRAPH_CARAT]);
t.push({categoryCustom:angular.copy(q)});
delete t[0]["categoryCustom"].carat_type$DRP$Long;
if(l.orgPlanList[l.editIndex]===undefined||l.orgPlanList[l.editIndex]===null){l.orgPlanList[l.editIndex]={}
}var s=q[g.PLAN_ID];
var r=q[g.TAG];
l.orgPlanList[l.editIndex]=angular.copy(q);
delete t[0]["categoryCustom"].undefined;
delete t[0]["categoryCustom"].size$DRP$long;
delete t[0]["categoryCustom"].gsize$DRP$long;
var u=function(v){if(l.planList[l.editIndex]===undefined||l.planList[l.editIndex]===null){l.planList[l.editIndex]={}
}v[0].categoryCustom[g.CARAT]=p;
v[0].categoryCustom[g.GRAPH_CARAT]=o;
if(l.planList.length===1){l.orgPlanList[l.editIndex][g.PLAN_ID]=v[0].categoryCustom[g.PLAN_ID]=1;
l.orgPlanList[l.editIndex][g.TAG]=v[0].categoryCustom[g.TAG]="A";
l.orgPlanList[l.editIndex][g.EMP_ID]=i.session.id;
l.orgPlanList[l.editIndex][g.EMP_NAME]=i.session.userCode+"-"+i.session.firstName+" "+i.session.lastName;
d()
}else{if(l.planList[l.editIndex][g.PLAN_ID]===null||l.planList[l.editIndex][g.PLAN_ID]===undefined){if(l.tagList.indexOf(l.planList[l.planList.length-2].tag)>=l.tagList.indexOf(l.tagVal.val)){}else{if(l.planList[l.editIndex][g.PLAN_ID]===null||l.planList[l.editIndex][g.PLAN_ID]===undefined){}else{l.orgPlanList[l.editIndex][g.PLAN_ID]=v[0].categoryCustom[g.PLAN_ID]=s;
l.orgPlanList[l.editIndex][g.TAG]=v[0].categoryCustom[g.TAG]=r
}}}else{l.orgPlanList[l.editIndex][g.PLAN_ID]=v[0].categoryCustom[g.PLAN_ID]=s;
l.orgPlanList[l.editIndex][g.TAG]=v[0].categoryCustom[g.TAG]=r
}}l.planList[l.editIndex]=v[0].categoryCustom
};
k.convertorForCustomField(t,u)
}},true);
l.editPlan=function(o){l.editIndex=o;
l.tab=l.orgPlanList[o]
};
var m=function(){var o=false;
for(index=0;
index<i.primaryFields.length;
index++){for(var p=0;
p<l.planList.length;
p++){if(l.planList[p][g.IS_ACTIVE]===true&&i.primaryFields[index]!=="carat_type$DRP$Long"&&(l.planList[p][i.primaryFields[index]]===undefined||l.planList[p][i.primaryFields[index]]===null)){alert("Please select mandatory fields first!");
o=true;
break
}}if(o===true){break
}}return o
};
var e=function(){angular.forEach(c,function(p){delete p.carat_type$DRP$Long
});
var o=angular.copy(l.planList);
angular.forEach(o,function(p){delete p.carat_type$DRP$Long
});
if(angular.equals(c,o)){return false
}else{return true
}};
l.savePlans=function(s){if(s!==undefined&&s!==null){l.packetNumber=s
}l.removeEventCancel();
l.submitted=true;
if(l.estimatePredictionForm.$valid){if(l.planList!==undefined&&l.planList!==null&&l.planList.length>0){if(!m()){var p={};
l.planToId={};
var r=[];
angular.forEach(l.orgPlanList,function(t){if(t.empId===i.session.id){if(p[t.planId]===undefined||p[t.planId]===null){p[t.planId]=[]
}p[t.planId].push(t);
if(t.id!==undefined&&t.id!==null){l.planToId[t.planId]=t.id
}delete p[t.planId].empId;
delete p[t.planId].empName
}});
for(var o in p){if(p.hasOwnProperty(o)){var q={planId:o,tags:p[o],packetNumber:l.packetNumber};
if(p[o][0].copiedFrom!==undefined&&p[o][0].copiedFrom!==null){q.copiedFrom=p[o][0].copiedFrom
}if(l.planToId!==undefined&&l.planToId!==null&&l.planToId[o]!==null){q[g.ID]=l.planToId[o]
}if(parseInt(q[g.PLAN_ID])===parseInt(l.vals.finalPlan)){q.finalPlan=true
}else{q.finalPlan=false
}r.push(q)
}}i.maskLoading();
n.savePlans(r,function(u){i.unMaskLoading();
if(u!==undefined&&u!==null){for(var t=0;
t<l.planList.length;
t++){var v=l.planList[t];
v[g.ID]=u[v.planId];
l.orgPlanList[t][g.ID]=u[v.planId];
l.planToIds[v.planId]=u[v.planId]
}c=angular.copy(l.planList);
h()
}},function(){i.unMaskLoading();
var u="Failed to save plans";
var t=i.error;
i.addMessage(u,t)
})
}}}};
l.copyPlan=function(){if(l.orgPlanList[l.editIndex][g.ID]!==null&&l.orgPlanList[l.editIndex][g.ID]!==undefined){var o={};
var p=[];
var s=[];
var v="";
for(index=0;
index<l.planList.length;
index++){var w=l.planList[index];
if(l.orgPlanList[l.editIndex][g.PLAN_ID]===w.planId&&l.orgPlanList[l.editIndex][g.EMP_ID]===w.empId){if(o[w.planId]===undefined||o[w.planId]===null){o[w.planId]=[];
v=w.planId
}o[w.planId].push(w);
p.push(angular.copy(l.orgPlanList[index]))
}if(s.indexOf(w.planId)===-1){s.push(parseInt(w.planId))
}}if(v!==""){var t=angular.copy(l.editIndex);
l.editIndex=l.planList.length-1;
var u=o[v];
for(index=0;
index<u.length;
index++){var q=angular.copy(u[index]);
var w=angular.copy(q);
++l.editIndex;
w.copiedFrom=angular.copy(l.planList[t][g.ID]);
delete w[g.ID];
w[g.PLAN_ID]=Math.max.apply(null,s)+1;
delete w["$$hashKey"];
w[g.INDEX]=++l.index;
w[g.EMP_ID]=i.session.id;
w[g.EMP_NAME]=i.session.userCode+"-"+i.session.firstName+" "+i.session.lastName;
l.planList.push(w);
var r=angular.copy(p[index]);
r[g.PLAN_ID]=w[g.PLAN_ID];
r[g.EMP_ID]=i.session.id;
r[g.EMP_NAME]=i.session.userCode+"-"+i.session.firstName+" "+i.session.lastName;
delete r[g.ID];
r[g.INDEX]=l.index;
r.copiedFrom=w.copiedFrom;
l.orgPlanList.push(r)
}l.tab=l.orgPlanList[l.editIndex];
delete l.tab.carat_type$DRP$Long
}}};
l.cancelPlan=function(){var p=l.editIndex;
var o=false;
for(var q=p+1;
q<l.planList.length;
q++){if(l.planList[q].isActive===undefined||l.planList[q].isActive===null||l.planList[q].isActive===true){l.editIndex=q;
o=true;
break
}}if(o===false){for(var q=0;
q<p;
q++){if(l.planList[q].isActive===undefined||l.planList[q].isActive===null||l.planList[q].isActive===true){l.editIndex=q;
o=true;
break
}}}l.planList[p][g.IS_ACTIVE]=false;
l.orgPlanList[p][g.IS_ACTIVE]=false;
if(o===false){l.editIndex=++l.index;
l.tab={index:l.editIndex,planId:1,tag:"A",isActive:true,empId:i.session.id,empName:i.session.userCode+"-"+i.session.firstName+" "+i.session.lastName,inStockOf:i.session.id};
d()
}else{l.tab=angular.copy(l.orgPlanList[l.editIndex])
}d()
};
l.filterFn=function(o){if(o.isActive===undefined||o.isActive===null||o.isActive===true){return true
}return false
};
l.searchPlan=function(){l.removeEventCancel();
if(l.packetNumber!==undefined&&l.packetNumber!==null&&l.packetNumber!==""){n.retrievePlansByPacket(l.packetNumber,function(o){var r=o.result;
var q=o.error;
if(r!==undefined&&r!==null){l.rapcalcyflag=true;
if(r.length>0){l.previousPacketNumber=r[0].packetNumber;
var p=[];
if(r!==undefined&&r!==null){angular.forEach(r,function(w){if(w.tags!==undefined&&w.tags!==null){angular.forEach(w.tags,function(y){var x=angular.copy(y);
x[g.ID]=w[g.ID];
x[g.EMP_ID]=w[g.EMP_ID];
x[g.EMP_NAME]=w[g.EMP_NAME];
x.inStockOf=w[g.IN_STOCK_OF];
p.push(x)
})
}if(w.finalPlan===true){l.vals.finalPlan=w.planId
}})
}l.orgPlanList=angular.copy(p);
if(p!==undefined&&p!==null){l.planList=[];
var v={};
var t=[];
angular.forEach(p,function(x){var y=""+x.planId+x.tag+x.empId;
var w=angular.copy(x.size$DRP$long);
var A=angular.copy(x.gsize$DRP$long);
var z={carat:w,gcarat:A};
v[y]=z;
delete x.size$DRP$long;
delete x.gsize$DRP$long;
t.push({categoryCustom:angular.copy(x),value:x.index})
});
var u=function(w){var x=[];
angular.forEach(w,function(z){z.categoryCustom[g.CARAT]=v[""+z.categoryCustom[g.PLAN_ID]+z.categoryCustom[g.TAG]+z.categoryCustom[g.EMP_ID]]["carat"];
z.categoryCustom[g.GRAPH_CARAT]=v[""+z.categoryCustom[g.PLAN_ID]+z.categoryCustom[g.TAG]+z.categoryCustom[g.EMP_ID]]["gcarat"];
x.push(z.categoryCustom)
});
l.planList=angular.copy(x);
l.index=l.planList.length-1;
for(var y=0;
y<l.planList.length;
y++){if(l.planToIds[l.planList[y].planId]===undefined||l.planToIds[l.planList[y].planId]===null){l.planToIds[l.planList[y].planId]=l.planList[y].id
}}c=angular.copy(x)
};
k.convertorForCustomField(t,u)
}l.editIndex=0;
l.tab=l.orgPlanList[0];
d();
h()
}else{b()
}}else{var s=i.error;
i.addMessage(q,s);
l.rapcalcyflag=false;
b();
c=[{index:0,isActive:true,empId:i.session.id,empName:i.session.userCode+"-"+i.session.firstName+" "+i.session.lastName,inStockOf:i.session.id,planId:1,tag:"A"}]
}},function(){console.log("Failure")
})
}};
l.beforeSearch=function(){if(e()===false){l.planList=[{index:0,planId:1,tag:"A"}];
l.orgPlanList=[];
l.editIndex=0;
l.index=0;
index=0;
l.planToIds={};
d();
l.searchPlan()
}else{$("#saveModificationPopup").modal("show")
}};
l.clearPlanData=function(){var q=l.tab.index;
var s=l.tab.planId;
var o=l.tab.tag;
var t=l.tab.empId;
var r=l.tab.empName;
var p=l.tab.inStockOf;
var u=l.tab.id;
l.tab={id:u,index:q,planId:s,tag:o,empId:t,empName:r,inStockOf:p}
};
l.calculateRate=function(){var r=true;
var q={fourCMap:{}};
q.discountDetailsMap=angular.copy(l.tab);
for(var o=0;
o<i.fourCMap.length;
o++){var p=q.discountDetailsMap[i.fourCMap[o]];
if(!p){r=false;
break
}q.fourCMap[i.fourCMap[o]]=p
}if(r){delete q.discountDetailsMap.fourCMap;
delete q.discountDetailsMap[g.INDEX];
delete q.discountDetailsMap[g.IS_ACTIVE];
delete q.discountDetailsMap[g.TAG];
delete q.discountDetailsMap[g.ID];
delete q.discountDetailsMap[g.COPIED_FROM];
delete q.discountDetailsMap[g.CARAT];
delete q.discountDetailsMap[g.GRAPH_CARAT];
delete q.discountDetailsMap[g.EMP_NAME];
delete q.discountDetailsMap[g.EMP_ID];
f.calculateDiamondPrice(q,function(s){l.calcFinal=s.data;
if(l.calcFinal.baseAmount===null){l.calcFinal.baseAmount=0
}if(l.calcFinal.discount===null){l.calcFinal.discount=0
}if(l.calcFinal.amount===null){l.calcFinal.amount=0
}if(l.calcFinal.mixAmount===null){l.calcFinal.mixAmount=0
}})
}};
l.removeEventCancel=function(){$("#saveModificationPopup").modal("hide");
i.removeModalOpenCssAfterModalHide()
};
var b=function(){l.planList=[];
l.orgPlanList=[];
l.editIndex=0;
l.index=0;
index=0;
l.planToIds={};
l.tab={index:0,isActive:true,empId:i.session.id,empName:i.session.userCode+"-"+i.session.firstName+" "+i.session.lastName,inStockOf:i.session.id};
d()
};
var d=function(){l.planIds=[];
if(l.orgPlanList!==null&&l.orgPlanList!==undefined&&l.orgPlanList.length>0){var o=[];
angular.forEach(l.orgPlanList,function(p){if(p.isActive!==false&&o.indexOf(p.planId)===-1&&p.empId===i.session.id){l.planIds.push({key:p.planId,value:p.planId});
o.push(p.planId)
}})
}};
var h=function(){if(l.packetNumber!==undefined&&l.packetNumber!==null){l.finalPlans=[];
n.retrieveFinalPlans(l.packetNumber,function(o){if(o!==null&&o!==undefined&&o.length>0){l.finalPlans=angular.copy(o)
}})
}};
l.finalizePlans=function(){if(l.vals.finalizedPlan!==undefined&&l.vals.finalizedPlan!==null){n.finalizePlan(l.vals.finalizedPlan,function(){var p="Plan finalized successfully";
var o=i.success;
i.addMessage(p,o)
},function(){var p="Error while finalizing plan";
var o=i.error;
i.addMessage(p,o)
})
}};
l.prepareTagList(l.tagValue)
}])
});