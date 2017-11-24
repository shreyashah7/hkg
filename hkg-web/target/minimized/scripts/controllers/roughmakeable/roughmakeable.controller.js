define(["hkg","roughMakeableService","customFieldService","customsearch.directive","ngload!uiGrid","dynamicForm","ruleExecutionService","addMasterValue","rapCalcyDirective"],function(a){a.register.controller("RoughMakeableController",["$rootScope","$scope","RoughMakeableService","DynamicFormService","CustomFieldService","RuleExecutionService","$timeout","RapCalcyService",function(h,p,e,o,s,t,v,b){h.mainMenu="stockLink";
h.childMenu="RoughMakeable";
p.entity="ROUGHMAKEABLE.";
var u,q={},w,f,m,c={},r={},i={},k={},d=[],l=[];
p.entity="ROUGHMAKEABLE.";
p.staticRoughMakeableModel={};
p.categoryCustomRoughMakeable={};
p.categoryCustomRoughMakeableRoughCalc={};
p.fieldsNotConfiguredFlag={};
var n=function(){console.log("masking");
h.maskLoading();
p.gridOptionspacket={enableRowSelection:true,enableSelectAll:true,enableFiltering:true,multiSelect:false,data:[],columnDefs:[],onRegisterApi:function(y){u=y;
y.selection.on.rowSelectionChanged(p,function(z){p.roughMakeableToUpdateId=null;
if(z.isSelected==true){p.staticRoughMakeableModel.selectedPacket=z.entity;
j()
}else{p.staticRoughMakeableModel.selectedPacket=null
}})
}};
s.retrieveDesignationBasedFieldsBySection(["makeableRough","GEN"],function(y){if(y.Packet==null||y.Packet.length==0){p.fieldsNotConfiguredFlag.packet="No fields are configured for packet";
h.unMaskLoading()
}else{p.fieldsNotConfiguredFlag.packet=null;
p.packetDataBean={};
var z=o.retrieveSectionWiseCustomFieldInfo("packet");
z.then(function(C){q.template=null;
q.template=C.genralSection;
var A=[];
Object.keys(y).map(function(D,E){angular.forEach(this[D],function(F){if(D==="Packet"){A.push({packet:F})
}})
},y);
w=[];
f={};
q.template=o.retrieveCustomData(q.template,A);
angular.forEach(q.template,function(D){if(D.fromModel){f[D.fieldId]=D.fromModel;
p.gridOptionspacket.columnDefs.push({name:D.fromModel,displayName:D.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.addpacketScreenRule(row, '"+D.fromModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
if(w.indexOf(D.fromModel)===-1){w.push(D.fromModel)
}}else{if(D.toModel){f[D.fieldId]=D.toModel;
p.gridOptionspacket.columnDefs.push({name:D.toModel,displayName:D.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.addpacketScreenRule(row, '"+D.toModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
if(w.indexOf(D.toModel)===-1){w.push(D.toModel)
}}else{if(D.model){f[D.fieldId]=D.model;
p.gridOptionspacket.columnDefs.push({name:D.model,displayName:D.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.addpacketScreenRule(row, '"+D.model+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
if(w.indexOf(D.model)===-1){w.push(D.model)
}}}}});
m={};
m.dbFieldNames=w;
if(w.length>0){var B={featureDbFieldMap:m,ruleConfigMap:{fieldIdNameMap:f,featureName:"roughMakeable"}};
e.retrievePacketsInStockOf(B,function(D){if(D.length<=0){p.fieldsNotConfiguredFlag.numOfPkt="No packets available";
h.unMaskLoading()
}else{var F;
var G=function(H){F=angular.copy(H);
for(var J in F){var I={};
F[J].categoryCustom.$$packetId$$=F[J].value;
F[J].categoryCustom.screenRuleDetailsWithDbFieldName=F[J].screenRuleDetailsWithDbFieldName;
angular.extend(I,F[J].categoryCustom,{id:F[J].value});
p.gridOptionspacket.data.push(I)
}console.log("unmasking");
h.unMaskLoading()
};
var E=angular.copy(D);
if(E!==undefined){o.convertorForCustomField(E,G)
}}p.fieldsNotConfiguredFlag.numOfPkt=null
},function(){var E="Could not retrieve packets, please try again.";
var D=h.error;
h.addMessage(E,D);
console.log("unmasking");
h.unMaskLoading()
})
}},function(A){console.log("unmasking");
h.unMaskLoading()
})
}},function(){console.log("unmasking");
h.unMaskLoading();
var z="Failed to retrieve data";
var y=h.error;
h.addMessage(z,y)
});
p.addpacketScreenRule=function(A,z){var y;
if(!!A.entity.screenRuleDetailsWithDbFieldName&&A.entity.screenRuleDetailsWithDbFieldName[z]!==undefined&&A.entity.screenRuleDetailsWithDbFieldName[z]!==null){y=A.entity.screenRuleDetailsWithDbFieldName[z].colorCode
}return y
}
};
var j=function(){var y={featureName:"roughMakeable",entityId:p.staticRoughMakeableModel.selectedPacket.id,entityType:"packet",};
t.executePreRule(y,function(A){if(!!A.validationMessage){p.preRuleSatisfied=true;
var B=h.warning;
h.addMessage(A.validationMessage,B);
p.staticRoughMakeableModel.selectedPacket=null
}else{var D={};
angular.extend(D,i,k);
var C=d.concat(l);
if(C.length>0){var z={dbFieldNames:C,ruleConfigMap:{fieldIdNameMap:D,featureName:"roughMakeable"},packetId:p.staticRoughMakeableModel.selectedPacket.id}
}e.retrieveRoughMakeableByPktId(z,function(E){p.resetAddRoughMakeableForm(E)
})
}},function(z){console.log("unmasking");
h.unMaskLoading();
var B="Failed to retrieve pre rule.";
var A=h.error;
h.addMessage(B,A)
})
};
var x=function(){s.retrieveDesignationBasedFieldsBySection(["makeableRough","ARM"],function(y){if(y.RoughMakeable==null||y.RoughMakeable.length==0){p.fieldsNotConfiguredFlag.RoughMakeable="No fields are configured for Rough Makeable"
}else{p.fieldsNotConfiguredFlag.RoughMakeable=null
}var z=o.retrieveSectionWiseCustomFieldInfo("roughMakeableEntity");
z.then(function(B){var A=[];
Object.keys(y).map(function(C,D){angular.forEach(this[C],function(E){if(C==="RoughMakeable"){A.push({RoughMakeable:E})
}})
},y);
c=o.retrieveCustomData(B.genralSection,A);
angular.forEach(c,function(C){if(C.fromModel){i[C.fieldId]=C.fromModel;
if(d.indexOf(C.fromModel)===-1){d.push(C.fromModel)
}}else{if(C.toModel){i[C.fieldId]=C.toModel;
if(d.indexOf(C.toModel)===-1){d.push(C.toModel)
}}else{if(C.model){i[C.fieldId]=C.model;
if(d.indexOf(C.model)===-1){d.push(C.model)
}}}}});
s.retrieveDesignationBasedFieldsBySection(["makeableRough","ARMRC"],function(D){if(D.RoughCalcy==null||D.RoughCalcy.length==0){p.fieldsNotConfiguredFlag.RoughCalcy="No fields are configured for Rap Calc"
}else{p.fieldsNotConfiguredFlag.RoughCalcy=null
}var C=o.retrieveSectionWiseCustomFieldInfo("roughCalcyEntity");
C.then(function(G){var F=[];
Object.keys(D).map(function(H,I){angular.forEach(this[H],function(J){if(H==="RoughCalcy"){F.push({RoughMakeable:J})
}})
},D);
G.genralSection=o.retrieveCustomData(G.genralSection,F);
angular.forEach(G.genralSection,function(H){if(H.model){k[H.fieldId]=H.model;
if(l.indexOf(H.model)===-1){l.push(H.model)
}}else{if(H.toModel){k[H.fieldId]=H.toModel;
if(l.indexOf(H.toModel)===-1){l.push(H.toModel)
}}else{if(H.fromModel){k[H.fieldId]=H.fromModel;
if(l.indexOf(H.fromModel)===-1){l.push(H.fromModel)
}}}}});
for(var E=0;
E<h.fourCMap.length;
E++){if(l.indexOf(h.fourCMap[E])<0){p.mandatoryRoughCalcFieldNotconfigured="Mandatory fields are not configured for your designation, Please contact administrator"
}}r=G.genralSection;
p.categoryCustomRoughMakeable={};
p.categoryCustomRoughMakeableRoughCalc={};
p.roughMakeableDbType={};
p.roughMakeableDbTypeRoughCalc={};
p.templateAddRoughMakeAble=angular.copy(c);
p.templateAddRoughMakeAbleRoughCalc=angular.copy(r);
p.$watch("categoryCustomRoughMakeableRoughCalc",function(){g()
},true)
})
},function(){console.log("unmasking");
h.unMaskLoading();
var D="Failed to retrieve RoughCalcy data";
var C=h.error;
h.addMessage(D,C)
})
})
},function(){console.log("unmasking");
h.unMaskLoading();
var z="Failed to retrieve RoughMakeable data";
var y=h.error;
h.addMessage(z,y)
})
};
p.resetAddRoughMakeableForm=function(z,B){p.templateAddRoughMakeAble=angular.copy(c);
p.templateAddRoughMakeAbleRoughCalc=angular.copy(r);
p.categoryCustomRoughMakeable={};
p.categoryCustomRoughMakeableRoughCalc={};
if(z!=null&&z.data!=null){var A=z.data.categoryCustom;
p.roughMakeableToUpdateId=z.data.value;
if(A!=null){angular.forEach(d,function(C){p.categoryCustomRoughMakeable[C]=A[C]
});
angular.forEach(l,function(C){p.categoryCustomRoughMakeableRoughCalc[C]=A[C]
})
}}p.roughMakeableDbType={};
p.roughMakeableDbTypeRoughCalc={};
p.submitted=false;
if(B==true&&u!=null){u.selection.clearSelectedRows();
p.staticRoughMakeableModel.selectedPacket=null
}else{var y=p.staticRoughMakeableModel.selectedPacket;
p.staticRoughMakeableModel.selectedPacket=null;
v(function(){p.staticRoughMakeableModel.selectedPacket=y
},100)
}};
p.createOrUpdateRoughMakeable=function(y){p.submitted=true;
if(y.$valid){angular.extend(p.categoryCustomRoughMakeable,p.categoryCustomRoughMakeableRoughCalc);
angular.extend(p.roughMakeableDbType,p.roughMakeableDbTypeRoughCalc);
var z={featureName:"roughMakeable",entityId:null,entityType:"roughMakeableEntity",currentFieldValueMap:p.categoryCustomRoughMakeable,dbType:p.roughMakeableDbType,otherEntitysIdMap:{packetId:p.staticRoughMakeableModel.selectedPacket.id}};
p.submitted=false;
t.executePostRule(z,function(B){if(!!B.validationMessage){var C=h.warning;
h.addMessage(B.validationMessage,C)
}else{var A={id:p.roughMakeableToUpdateId,roughMakeableCustom:p.categoryCustomRoughMakeable,roughMakeableDbType:p.roughMakeableDbType,packetId:p.staticRoughMakeableModel.selectedPacket.id};
if(A.id==null){e.createRoughMakeable(A,function(){p.resetAddRoughMakeableForm(null,true)
})
}else{e.updateRoughMakeable(A,function(){p.resetAddRoughMakeableForm(null,true)
})
}}})
}};
var g=function(){var B=true;
var y={};
y.fourCMap={};
for(var z=0;
z<h.fourCMap.length;
z++){var A=p.categoryCustomRoughMakeable[h.fourCMap[z]];
if(!A){B=false;
break
}y.fourCMap[h.fourCMap[z]]=A
}if(B){y.discountDetailsMap=p.categoryCustomRoughMakeable;
b.calculateDiamondPrice(y,function(C){p.calcFinal=C.data;
if(p.calcFinal.baseAmount==null){p.calcFinal.baseAmount=0
}if(p.calcFinal.discount==null){p.calcFinal.discount=0
}if(p.calcFinal.amount==null){p.calcFinal.amount=0
}if(p.calcFinal.mixAmount==null){p.calcFinal.mixAmount=0
}})
}};
n();
x()
}])
});