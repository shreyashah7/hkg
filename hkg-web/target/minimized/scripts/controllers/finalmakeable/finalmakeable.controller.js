define(["hkg","finalMakeableService","customFieldService","customsearch.directive","ngload!uiGrid","dynamicForm","ruleExecutionService","addMasterValue","rapCalcyDirective"],function(a){a.register.controller("FinalMakeableController",["$rootScope","$scope","FinalMakeableService","DynamicFormService","CustomFieldService","RuleExecutionService","$timeout","RapCalcyService",function(g,o,q,n,r,t,v,b){g.mainMenu="stockLink";
g.childMenu="FinalMakeable";
o.entity="ROUGHMAKEABLE.";
var u,p={},x,d,l,f={},s={},h={},j={},c=[],k=[];
o.entity="ROUGHMAKEABLE.";
o.staticFinalMakeableModel={};
o.categoryCustomFinalMakeable={};
o.categoryCustomFinalMakeableRoughCalc={};
o.fieldsNotConfiguredFlag={};
var m=function(){console.log("masking");
g.maskLoading();
o.gridOptionspacket={enableRowSelection:true,enableSelectAll:true,enableFiltering:true,multiSelect:false,data:[],columnDefs:[],onRegisterApi:function(y){u=y;
y.selection.on.rowSelectionChanged(o,function(z){o.finalMakeableToUpdateId=null;
if(z.isSelected==true){o.staticFinalMakeableModel.selectedPacket=z.entity;
w()
}else{o.staticFinalMakeableModel.selectedPacket=null
}})
}};
r.retrieveDesignationBasedFieldsBySection(["makeableFinal","GEN"],function(y){if(y.Packet==null||y.Packet.length==0){o.fieldsNotConfiguredFlag.packet="No fields are configured for packet";
g.unMaskLoading()
}else{o.fieldsNotConfiguredFlag.packet=null;
o.packetDataBean={};
var z=n.retrieveSectionWiseCustomFieldInfo("packet");
z.then(function(C){p.template=null;
p.template=C.genralSection;
var A=[];
Object.keys(y).map(function(D,E){angular.forEach(this[D],function(F){if(D==="Packet"){A.push({packet:F})
}})
},y);
x=[];
d={};
p.template=n.retrieveCustomData(p.template,A);
angular.forEach(p.template,function(D){if(D.fromModel){d[D.fieldId]=D.fromModel;
o.gridOptionspacket.columnDefs.push({name:D.fromModel,displayName:D.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.addpacketScreenRule(row, '"+D.fromModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
if(x.indexOf(D.fromModel)===-1){x.push(D.fromModel)
}}else{if(D.toModel){d[D.fieldId]=D.toModel;
o.gridOptionspacket.columnDefs.push({name:D.toModel,displayName:D.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.addpacketScreenRule(row, '"+D.toModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
if(x.indexOf(D.toModel)===-1){x.push(D.toModel)
}}else{if(D.model){d[D.fieldId]=D.model;
o.gridOptionspacket.columnDefs.push({name:D.model,displayName:D.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.addpacketScreenRule(row, '"+D.model+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'});
if(x.indexOf(D.model)===-1){x.push(D.model)
}}}}});
l={};
l.dbFieldNames=x;
if(x.length>0){var B={featureDbFieldMap:l,ruleConfigMap:{fieldIdNameMap:d,featureName:"finalMakeable"}};
q.retrievePacketsInStockOf(B,function(D){if(D.length<=0){o.fieldsNotConfiguredFlag.numOfPkt="No packets available";
g.unMaskLoading()
}else{var F;
var G=function(H){F=angular.copy(H);
for(var J in F){var I={};
F[J].categoryCustom.$$packetId$$=F[J].value;
F[J].categoryCustom.screenRuleDetailsWithDbFieldName=F[J].screenRuleDetailsWithDbFieldName;
angular.extend(I,F[J].categoryCustom,{id:F[J].value});
o.gridOptionspacket.data.push(I)
}console.log("unmasking");
g.unMaskLoading()
};
var E=angular.copy(D);
if(E!==undefined){n.convertorForCustomField(E,G)
}}o.fieldsNotConfiguredFlag.numOfPkt=null
},function(){var E="Could not retrieve packets, please try again.";
var D=g.error;
g.addMessage(E,D);
console.log("unmasking");
g.unMaskLoading()
})
}},function(A){console.log("unmasking");
g.unMaskLoading()
})
}},function(){console.log("unmasking");
g.unMaskLoading();
var z="Failed to retrieve data";
var y=g.error;
g.addMessage(z,y)
});
o.addpacketScreenRule=function(A,z){var y;
if(!!A.entity.screenRuleDetailsWithDbFieldName&&A.entity.screenRuleDetailsWithDbFieldName[z]!==undefined&&A.entity.screenRuleDetailsWithDbFieldName[z]!==null){y=A.entity.screenRuleDetailsWithDbFieldName[z].colorCode
}return y
}
};
var w=function(){var y={featureName:"finalMakeable",entityId:o.staticFinalMakeableModel.selectedPacket.id,entityType:"packet",};
t.executePreRule(y,function(z){if(!!z.validationMessage){o.preRuleSatisfied=true;
var A=g.warning;
g.addMessage(z.validationMessage,A);
o.staticFinalMakeableModel.selectedPacket=null
}else{var D={};
angular.extend(D,h,j);
var C=c.concat(k);
if(C.length>0){var B={dbFieldNames:C,ruleConfigMap:{fieldIdNameMap:D,featureName:"finalMakeable"},packetId:o.staticFinalMakeableModel.selectedPacket.id}
}q.retrieveFinalMakeableByPktId(B,function(E){o.resetAddFinalMakeableForm(E)
})
}},function(z){console.log("unmasking");
g.unMaskLoading();
var B="Failed to retrieve pre rule.";
var A=g.error;
g.addMessage(B,A)
})
};
var i=function(){r.retrieveDesignationBasedFieldsBySection(["makeableFinal","AFM"],function(z){if(z.FinalMakeable==null||z.FinalMakeable.length==0){o.fieldsNotConfiguredFlag.FinalMakeable="No fields are configured for Final Makeable"
}else{o.fieldsNotConfiguredFlag.FinalMakeable=null
}var y=n.retrieveSectionWiseCustomFieldInfo("finalMakeableEntity");
y.then(function(B){var A=[];
Object.keys(z).map(function(C,D){angular.forEach(this[C],function(E){if(C==="FinalMakeable"){A.push({FinalMakeable:E})
}})
},z);
f=n.retrieveCustomData(B.genralSection,A);
angular.forEach(f,function(C){if(C.fromModel){h[C.fieldId]=C.fromModel;
if(c.indexOf(C.fromModel)===-1){c.push(C.fromModel)
}}else{if(C.toModel){h[C.fieldId]=C.toModel;
if(c.indexOf(C.toModel)===-1){c.push(C.toModel)
}}else{if(C.model){h[C.fieldId]=C.model;
if(c.indexOf(C.model)===-1){c.push(C.model)
}}}}});
r.retrieveDesignationBasedFieldsBySection(["makeableFinal","AFMRC"],function(D){if(D.RoughCalcy==null||D.RoughCalcy.length==0){o.fieldsNotConfiguredFlag.RoughCalcy="No fields are configured for Rap Calc"
}else{o.fieldsNotConfiguredFlag.RoughCalcy=null
}var C=n.retrieveSectionWiseCustomFieldInfo("finalCalcyEntity");
C.then(function(G){var E=[];
Object.keys(D).map(function(H,I){angular.forEach(this[H],function(J){if(H==="RoughCalcy"){E.push({FinalMakeable:J})
}})
},D);
G.genralSection=n.retrieveCustomData(G.genralSection,E);
angular.forEach(G.genralSection,function(H){if(H.model){j[H.fieldId]=H.model;
if(k.indexOf(H.model)===-1){k.push(H.model)
}}else{if(H.toModel){j[H.fieldId]=H.toModel;
if(k.indexOf(H.toModel)===-1){k.push(H.toModel)
}}else{if(H.fromModel){j[H.fieldId]=H.fromModel;
if(k.indexOf(H.fromModel)===-1){k.push(H.fromModel)
}}}}});
for(var F=0;
F<g.fourCMap.length;
F++){if(k.indexOf(g.fourCMap[F])<0){o.mandatoryRoughCalcFieldNotconfigured="Mandatory fields are not configured for your designation, Please contact administrator"
}}s=G.genralSection;
o.categoryCustomFinalMakeable={};
o.categoryCustomFinalMakeableRoughCalc={};
o.finalMakeableDbType={};
o.finalMakeableDbTypeRoughCalc={};
o.templateAddFinalMakeAble=angular.copy(f);
o.templateAddFinalMakeAbleRoughCalc=angular.copy(s);
o.$watch("categoryCustomFinalMakeableRoughCalc",function(){e()
},true)
})
},function(){console.log("unmasking");
g.unMaskLoading();
var D="Failed to retrieve RoughCalcy data";
var C=g.error;
g.addMessage(D,C)
})
})
},function(){console.log("unmasking");
g.unMaskLoading();
var z="Failed to retrieve FinalMakeable data";
var y=g.error;
g.addMessage(z,y)
})
};
o.resetAddFinalMakeableForm=function(A,B){o.templateAddFinalMakeAble=angular.copy(f);
o.templateAddFinalMakeAbleRoughCalc=angular.copy(s);
o.categoryCustomFinalMakeable={};
o.categoryCustomFinalMakeableRoughCalc={};
if(A!=null&&A.data!=null){var z=A.data.categoryCustom;
o.finalMakeableToUpdateId=A.data.value;
if(z!=null){angular.forEach(c,function(C){o.categoryCustomFinalMakeable[C]=z[C]
});
angular.forEach(k,function(C){o.categoryCustomFinalMakeableRoughCalc[C]=z[C]
})
}}o.finalMakeableDbType={};
o.finalMakeableDbTypeRoughCalc={};
o.submitted=false;
if(B==true&&u!=null){u.selection.clearSelectedRows();
o.staticFinalMakeableModel.selectedPacket=null
}else{var y=o.staticFinalMakeableModel.selectedPacket;
o.staticFinalMakeableModel.selectedPacket=null;
v(function(){o.staticFinalMakeableModel.selectedPacket=y
},100)
}};
o.createOrUpdateFinalMakeable=function(y){o.submitted=true;
if(y.$valid){angular.extend(o.categoryCustomFinalMakeable,o.categoryCustomFinalMakeableRoughCalc);
angular.extend(o.finalMakeableDbType,o.finalMakeableDbTypeRoughCalc);
var z={featureName:"finalMakeable",entityId:null,entityType:"finalMakeableEntity",currentFieldValueMap:o.categoryCustomFinalMakeable,dbType:o.finalMakeableDbType,otherEntitysIdMap:{packetId:o.staticFinalMakeableModel.selectedPacket.id}};
o.submitted=false;
t.executePostRule(z,function(A){if(!!A.validationMessage){var B=g.warning;
g.addMessage(A.validationMessage,B)
}else{var C={id:o.finalMakeableToUpdateId,finalMakeableCustom:o.categoryCustomFinalMakeable,finalMakeableDbType:o.finalMakeableDbType,packetId:o.staticFinalMakeableModel.selectedPacket.id};
if(C.id==null){q.createFinalMakeable(C,function(){o.resetAddFinalMakeableForm(null,true)
})
}else{q.updateFinalMakeable(C,function(){o.resetAddFinalMakeableForm(null,true)
})
}}})
}};
var e=function(){var B=true;
var y={};
y.fourCMap={};
for(var z=0;
z<g.fourCMap.length;
z++){var A=o.categoryCustomFinalMakeable[g.fourCMap[z]];
if(!A){B=false;
break
}y.fourCMap[g.fourCMap[z]]=A
}if(B){y.discountDetailsMap=o.categoryCustomFinalMakeable;
b.calculateDiamondPrice(y,function(C){o.calcFinal=C.data;
if(o.calcFinal.baseAmount==null){o.calcFinal.baseAmount=0
}if(o.calcFinal.discount==null){o.calcFinal.discount=0
}if(o.calcFinal.amount==null){o.calcFinal.amount=0
}if(o.calcFinal.mixAmount==null){o.calcFinal.mixAmount=0
}})
}};
m();
i()
}])
});