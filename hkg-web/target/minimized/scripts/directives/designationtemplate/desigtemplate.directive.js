define(["angular","designationService","messageService"],function(){globalProvider.compileProvider.directive("designationTemplate",["$rootScope","$filter","$templateCache","Designation","Messaging",function(b,h,a,d,e){var f={diamondSystemFeatureList:"=?",systemFeaturesList:"=?",reportSystemFeatureList:"=?",groupFeatureMap:"=?",featureFieldMap:"=?",featureModifierMap:"=?",uniqueIndex:"@",designationId:"@"};
var g=function(k,j,i){};
var c=["$scope","$element","$attrs","$rootScope","$timeout","$filter","$location","$window","$log",function(q,r,p,o,j,m,k,i,s){if(q.uniqueIndex===undefined||q.uniqueIndex===null){q.uniqueIndex=null
}if(q.diamondSystemFeatureList===undefined||q.diamondSystemFeatureList===null){q.diamondSystemFeatureList=[]
}if(q.systemFeaturesList===undefined||q.systemFeaturesList===null){q.systemFeaturesList=[]
}if(q.reportSystemFeatureList===undefined||q.reportSystemFeatureList===null){q.reportSystemFeatureList=[]
}if(q.groupFeatureMap===undefined||q.groupFeatureMap===null){q.groupFeatureMap=[]
}q.flags={};
q.flags.modifierSubmitted=false;
q.tmp={text:"Show plans",id:"SP",value:true};
q.fieldList=[];
q.commonList=[];
if(q.featureFieldMap===undefined||q.featureFieldMap===null){q.featureFieldMap={}
}if(q.featureModifierMap===undefined||q.featureModifierMap===null){q.featureModifierMap={}
}q.isEditing=true;
q.firstPage=true;
q.fixedFeatureModifiersMap={issue_receive:["medium","type","mode","access"],allotment:["designation"],estimate_prediction:["plan"]};
q.AllModifiers=[{values:[{text:"Rough",id:"R",value:false},{text:"Lot",id:"L",value:false},{text:"Packet",id:"P",value:false}],text:"medium"},{values:[{text:"Inward",id:"IW",value:false},{text:"Outward",id:"OW",value:false}],text:"type"},{values:[{text:"Direct",id:"DR",value:false},{text:"Via StockRoom",id:"VS",value:false}],text:"mode"},{values:[{text:"Request",id:"RQ",value:false},{text:"Collect",id:"CL",value:false},{text:"Issue",id:"IS",value:false},{text:"Receive",id:"RC",value:false},{text:"Return",id:"RT",value:false}],text:"access"},{values:[{text:"Designation",id:"DES",value:""}],text:"designation"},{values:[{text:"Show plans",id:"SP",value:""},{text:"Add plans",id:"AP",value:""},{text:"Copy Plan",id:"CP",value:""},{text:"Delete Plan",id:"DP",value:""},{text:"Finalize Plan",id:"FP",value:""}],text:"plan"}];
q.featureSectionEntitysMap={rough_invoice_add_edit:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Invoice",name:"Invoice"}]},{sectionId:"MRP",sectionName:"Rough Parcel",entitys:[{id:"Parcel",name:"Parcel"}]}],rough_purchase_add_edit:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Purchase",name:"Purchase"}]}],link_rough_purchase:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Purchase",name:"Purchase"}]},{sectionId:"RPA",sectionName:"Rough Parcel",entitys:[{id:"Parcel",name:"Parcel"}]}],lot_add:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Lot",name:"Lot"}]},{sectionId:"ASL",sectionName:"Associate Sub Lot",entitys:[{id:"SubLot",name:"SubLot"}]}],lot_edit:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Lot",name:"Lot"}]},{sectionId:"ASL",sectionName:"Associate Sub Lot",entitys:[{id:"SubLot",name:"SubLot"}]}],stock_sell:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Sell",name:"Sell"}]}],rough_sale:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Parcel",name:"Parcel"},{id:"Sell",name:"Sell"}]}],stock_transfer:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Transfer",name:"Transfer"}]}],stock_merge:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"}]}],packet_split:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Packet",name:"Packet"}]}],packet_add:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Packet",name:"Packet"}]}],packet_edit:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Packet",name:"Packet"}]}],allotment:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Packet",name:"Packet"}]}],finalize:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Plan",name:"Plan"}]}],generate_barcode:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"},{id:"Plan",name:"Plan"}]}],generate_slip:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"}]}],issue_receive:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Issue",name:"Issue"}]},{sectionId:"II",sectionName:"Issue Inward",entitys:[{id:"Issue",name:"Issue"}]},{sectionId:"RI",sectionName:"Receive Inward",entitys:[{id:"Issue",name:"Issue"}]},{sectionId:"RQ",sectionName:"Request",entitys:[{id:"Issue",name:"Issue"}]},{sectionId:"CL",sectionName:"Collect",entitys:[{id:"Issue",name:"Issue"}]},{sectionId:"IS",sectionName:"Issue",entitys:[{id:"Issue",name:"Issue"}]},{sectionId:"RC",sectionName:"Receive",entitys:[{id:"Issue",name:"Issue"}]}],write_service:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Plan",name:"Plan"}]}],print_static:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"}]}],print_dynamic:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"}]}],status_change:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"}]}],rough_merge:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Parcel",name:"Parcel"}]}],rough_calcy:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"RoughCalcy",name:"RoughCalcy"}]}],estimate_prediction:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"RoughCalcy",name:"Rough Calcy"}]}],sub_lot:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"SubLot",name:"SubLot"}]},{sectionId:"INP",sectionName:"Invoice/Parcel",entitys:[{id:"Invoice",name:"Invoice"},{id:"Parcel",name:"Parcel"}]}],rough_makeable:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Packet",name:"Packet"}]},{sectionId:"ARM",sectionName:"Add Rough Makeable",entitys:[{id:"RoughMakeable",name:"RoughMakeable"}]},{sectionId:"ARMRC",sectionName:"Rough Calc",entitys:[{id:"RoughCalcy",name:"RoughCalcy"}]}],final_makeable:[{sectionId:"GEN",sectionName:"General Section",entitys:[{id:"Packet",name:"Packet"}]},{sectionId:"AFM",sectionName:"Add Final Makeable",entitys:[{id:"FinalMakeable",name:"FinalMakeable"}]},{sectionId:"AFMRC",sectionName:"Rough Calc",entitys:[{id:"RoughCalcy",name:"RoughCalcy"}]}]};
q.visibleFields={invoice_add:[{id:"Invoice",name:"Invoice"}],invoice_edit:[{id:"Invoice",name:"Invoice"}],rough_invoice_add_edit:[{id:"Invoice",name:"Invoice"}],parcel_add_edit:[{id:"Parcel",name:"Parcel"}],rough_purchase_add_edit:[{id:"Purchase",name:"Purchase"}],lot_add:[{id:"Lot",name:"Lot"}],lot_edit:[{id:"Lot",name:"Lot"}],stock_sell:[{id:"Sell",name:"Sell"}],rough_sale:[{id:"Parcel",name:"Parcel"},{id:"Sell",name:"Sell"}],stock_transfer:[{id:"Transfer",name:"Transfer"}],stock_merge:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"}],packet_split:[{id:"Packet",name:"Packet"}],packet_add:[{id:"Packet",name:"Packet"}],packet_edit:[{id:"Packet",name:"Packet"}],allotment:[{id:"Packet",name:"Packet"}],finalize:[{id:"Plan",name:"Plan"}],generate_barcode:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"},{id:"Plan",name:"Plan"},{id:"Issue",name:"Issue"}],generate_slip:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"}],issue_receive:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"},{id:"Issue",name:"Issue"}],write_service:[{id:"Plan",name:"Plan"}],print_static:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"}],print_dynamic:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"}],status_change:[{id:"Lot",name:"Lot"},{id:"Packet",name:"Packet"}],rough_merge:[{id:"Parcel",name:"Parcel"}],rough_calcy:[{id:"RoughCalcy",name:"Rough Calcy"}],estimate_prediction:[{id:"RoughCalcy",name:"Rough Calcy"},{id:"Plan",name:"Plan"}],sub_lot:[{id:"SubLot",name:"SubLot"},{id:"Parcel",name:"Parcel"},{id:"Invoice",name:"Invoice"}],rough_makeable:[{id:"Packet",name:"Packet"}],final_makeable:[{id:"Packet",name:"Packet"}]};
q.clearSelectedSearchFields=function(){if(q.combinedFieldListForSearch!==null&&q.combinedFieldListForSearch!==undefined&&q.combinedFieldListForSearch.length>0){angular.forEach(q.combinedFieldListForSearch,function(t){t.ticked=false
})
}};
q.clearSelectedParentFields=function(){if(q.combinedFieldListForParent!==null&&q.combinedFieldListForParent!==undefined&&q.combinedFieldListForParent.length>0){angular.forEach(q.combinedFieldListForParent,function(t){t.ticked=false
})
}};
q.dataTableOptions={columns:[null,null,{orderDataType:"dom-text-numeric"}],autoWidth:false};
q.retrieveAllSysFeatures=function(){o.maskLoading();
d.retrieveSystemFeatures(function(t){o.unMaskLoading();
q.systemFeaturesList=t;
if(!q.systemFeaturesList||q.systemFeaturesList===null){q.systemFeaturesList=[]
}else{q.tempSystemFeatureList=[];
q.diamondSystemFeatureList=[];
q.reportSystemFeatureList=[];
angular.forEach(q.systemFeaturesList,function(u){if(u.type==="DMI"||u.type==="DEI"){q.diamondSystemFeatureList.push(u)
}else{if(u.type==="RMI"){q.reportSystemFeatureList.push(u)
}else{q.tempSystemFeatureList.push(u)
}}});
q.systemFeaturesList=[];
q.systemFeaturesList=angular.copy(q.tempSystemFeatureList)
}},function(){o.unMaskLoading()
})
};
q.checkUncheckDiamondIA=function(v){var w=v.iteamAttributesList.length;
for(var u=0;
u<w;
u++){v.iteamAttributesList[u].checked=v.checked
}if(v.configure&&!v.checked){v.configure=false;
for(var t in q.featureFieldMap){if(t==v.id){delete q.featureFieldMap[t]
}}}};
q.retrieveFieldsByFeature=function(){d.retrieveFieldsByFeature(function(t){angular.forEach(t.data,function(u){if(u.length>0){angular.forEach(u,function(v){q.fieldList.push(v)
})
}})
})
};
q.checkAccessModifier=function(){if(q.modifiers.mode){var t=false;
angular.forEach(q.modifiers.mode,function(u){if(u.id==="VS"&&u.value===true){t=true
}});
if(t){q.modifiers.showAccessModifiers=true
}else{q.modifiers.showAccessModifiers=undefined
}}};
q.retrieveFieldsByFeature();
q.combinedFieldListForSearch=[];
q.configureFeature=function(B){if(B.checked){q.diamondFeature=angular.copy(B);
var C=q.diamondFeature.menuLabel.split("_");
if(C[1]){var x=C[1]+" "+C[0];
q.labels=x
}else{var x=C[0];
q.labels=x
}q.modifiers={};
if(q.fixedFeatureModifiersMap[q.diamondFeature.menuLabel]!==undefined){angular.forEach(q.AllModifiers,function(F){if(q.fixedFeatureModifiersMap[q.diamondFeature.menuLabel].indexOf(F.text)>-1){q.modifiers[F.text]=F.values
}})
}if(Object.keys(q.modifiers).length===0){q.modifiers.noRecords=true
}if(q.featureModifierMap[q.diamondFeature.id]!==undefined&&q.featureModifierMap[q.diamondFeature.id]!==null){var y=q.featureModifierMap[q.diamondFeature.id];
if(y.iRMediums!==undefined&&y.iRMediums!==null){angular.forEach(q.modifiers.medium,function(F){if(y.iRMediums.indexOf(F.id)>-1){F.value=true
}})
}if(y.iRTypes!==undefined&&y.iRTypes!==null){angular.forEach(q.modifiers.type,function(F){if(y.iRTypes.indexOf(F.id)>-1){F.value=true
}})
}if(y.iRModes!==undefined&&y.iRModes!==null){angular.forEach(q.modifiers.mode,function(F){if(y.iRModes.indexOf(F.id)>-1){F.value=true
}})
}if(y.iRVSRAccessRights!==undefined&&y.iRVSRAccessRights!==null){angular.forEach(q.modifiers.access,function(F){if(y.iRVSRAccessRights.indexOf(F.id)>-1){F.value=true
}})
}if(y.asDesignation!==undefined&&y.asDesignation!==null){angular.forEach(y.asDesignation,function(G){var F=G+":R";
if(q.modifiers.designation[0].value===""){q.modifiers.designation[0].value=q.modifiers.designation[0].value+F
}else{q.modifiers.designation[0].value=q.modifiers.designation[0].value+","+F
}})
}if(y.planAccess!==undefined&&y.planAccess!==null){angular.forEach(q.modifiers.plan,function(F){if(y.planAccess.indexOf(F.id)>-1){F.value=true
}});
if(y.showPlanUsers!==undefined&&y.showPlanUsers!==null){q.flags.planAccesArray=y.showPlanUsers;
var t=y.recepientCodeToName;
var w=0;
angular.forEach(q.flags.planAccesArray,function(F){var G=[];
if(angular.isObject(F.users)){if(F.users[0].id!==""){G=F.users[0].id.split(",")
}}else{if(F.users!==""){G=F.users.split(",")
}}F.autoCompleteInvitees={multiple:true,closeOnSelect:false,placeholder:"Select members",width:"resolve",initSelection:function(H,J){var I=[];
if(G!==undefined&&G!==null&&G.length>0){angular.forEach(G,function(K){I.push({id:K,text:t[K]})
})
}J(I)
},formatResult:function(H){return H.text
},formatSelection:function(H){return H.text
},query:function(K){var J=K.term;
q.names=[];
var L=function(M){if(M.length!==0){q.names=M;
angular.forEach(M,function(N){q.names.push({id:N.value+":"+N.description,text:N.label})
})
}K.callback({results:q.names})
};
var H=function(){};
if(J.substring(0,2)==="@E"||J.substring(0,2)==="@e"){var I=K.term.slice(2);
e.retrieveUserList(I.trim(),L,H)
}else{if(J.substring(0,2)==="@D"||J.substring(0,2)==="@d"){var I=K.term.slice(2);
e.retrieveDepartmentList(I.trim(),L,H)
}else{if(J.substring(0,2)==="@R"||J.substring(0,2)==="@r"){var I=K.term.slice(2);
e.retrieveRoleList(I.trim(),L,H)
}}}}};
++w
});
q.flags.planFlag=true
}}}q.checkAccessModifier();
q.type=q.diamondFeature.type;
q.featureEntity=C[0];
q.sectionList=q.featureSectionEntitysMap[q.diamondFeature.menuLabel];
angular.forEach(q.sectionList,function(F){if(F.sectionId==="GEN"){q.entityList=F.entitys
}});
if(!!q.entityList){if(!!!q.entitys){q.entitys={}
}q.entitys.section="GEN";
q.entitys.name=q.entityList[0].id
}var E="$scope.invoiceList";
q.invoiceList=[];
q.lotList=[];
q.parcelList=[];
q.packetList=[];
q.coatedRoughList=[];
q.issueList=[];
q.planList=[];
q.diamondList=[];
q.allotmentList=[];
q.transferList=[];
q.sellList=[];
q.roughCalcyList=[];
q.purchaseList=[];
q.selectedFieldsForSearch=[];
q.clearSelectedSearchFields();
q.clearSelectedParentFields();
q.selectedFieldsForParent=[];
q.combinedFieldListForSearch=[];
q.combinedFieldListForParent=[];
q.subLotList=[];
q.roughMakeableList=[];
q.finalMakeableList=[];
q.fieldListToSend=[];
q.fieldListToSendTemp=[];
angular.forEach(q.fieldList,function(F){F.sectionCode="GEN";
if(F.entity==="Invoice"){q.invoiceList.push(F)
}else{if(F.entity==="Parcel"){q.parcelList.push(F)
}else{if(F.entity==="Packet"){q.packetList.push(F)
}else{if(F.entity==="Lot"){q.lotList.push(F)
}else{if(F.entity==="Coated Rough"){q.coatedRoughList.push(F)
}else{if(F.entity==="Issue"){q.issueList.push(F)
}else{if(F.entity==="Plan"){q.planList.push(F)
}else{if(F.entity==="Diamond"){q.diamondList.push(F)
}else{if(F.entity==="Allotment"){q.allotmentList.push(F)
}else{if(F.entity==="Transfer"){q.transferList.push(F)
}else{if(F.entity==="Sell"){q.sellList.push(F)
}else{if(F.entity==="RoughCalcy"){q.roughCalcyList.push(F)
}else{if(F.entity==="SubLot"){q.subLotList.push(F)
}else{if(F.entity==="Purchase"){q.purchaseList.push(F)
}else{if(F.entity==="RoughMakeable"){q.roughMakeableList.push(F)
}else{if(F.entity==="FinalMakeable"){q.finalMakeableList.push(F)
}}}}}}}}}}}}}}}}if(q.designationId!==null&&q.designationId!==undefined){F.designation=parseInt(q.designationId)
}});
q.fieldListToSend=angular.copy(q.fieldList);
q.fieldListToSendTemp=angular.copy(q.fieldList);
if(q.invoiceList.length>0){q.combinedFieldListForSearch.push({modelName:"<strong>Invoice</strong>",multiSelectGroup:true});
if(q.invoiceList.length>0){angular.forEach(q.invoiceList,function(H){if(H.dbFieldName!==undefined){var G=H.dbFieldName.split("$");
if(G.length>0){var F=G[1];
if(F!=="IMG"&&F!=="UPD"){q.combinedFieldListForSearch.push(H)
}}}})
}}if(q.invoiceList.length!==0){q.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(q.parcelList.length>0){q.combinedFieldListForSearch.push({modelName:"<strong>Parcel</strong>",multiSelectGroup:true});
if(q.parcelList.length>0){angular.forEach(q.parcelList,function(H){if(H.dbFieldName!==undefined){var G=H.dbFieldName.split("$");
if(G.length>0){var F=G[1];
if(F!=="IMG"&&F!=="UPD"){q.combinedFieldListForSearch.push(H)
}}}})
}}if(q.parcelList.length!==0){q.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(q.lotList.length>0){q.combinedFieldListForSearch.push({modelName:"<strong>Lot</strong>",multiSelectGroup:true});
if(q.lotList.length>0){angular.forEach(q.lotList,function(H){if(H.dbFieldName!==undefined){var G=H.dbFieldName.split("$");
if(G.length>0){var F=G[1];
if(F!=="IMG"&&F!=="UPD"){q.combinedFieldListForSearch.push(H)
}}}})
}}if(q.lotList.length!==0){q.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(q.packetList.length>0){q.combinedFieldListForSearch.push({modelName:"<strong>Packet</strong>",multiSelectGroup:true});
if(q.packetList.length>0){angular.forEach(q.packetList,function(H){if(H.dbFieldName!==undefined){var G=H.dbFieldName.split("$");
if(G.length>0){var F=G[1];
if(F!=="IMG"&&F!=="UPD"){q.combinedFieldListForSearch.push(H)
}}}})
}}if(q.packetList.length!==0){q.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(q.issueList.length>0){q.combinedFieldListForSearch.push({modelName:"<strong>Issue</strong>",multiSelectGroup:true});
if(q.issueList.length>0){angular.forEach(q.issueList,function(H){if(H.dbFieldName!==undefined){var G=H.dbFieldName.split("$");
if(G.length>0){var F=G[1];
if(F!=="IMG"&&F!=="UPD"){q.combinedFieldListForSearch.push(H)
}}}})
}}if(q.issueList.length!==0){q.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(q.transferList.length>0){q.combinedFieldListForSearch.push({modelName:"<strong>Transfer</strong>",multiSelectGroup:true});
if(q.transferList.length>0){angular.forEach(q.transferList,function(H){if(H.dbFieldName!==undefined){var G=H.dbFieldName.split("$");
if(G.length>0){var F=G[1];
if(F!=="IMG"&&F!=="UPD"){q.combinedFieldListForSearch.push(H)
}}}})
}}if(q.transferList.length!==0){q.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(q.sellList.length>0){q.combinedFieldListForSearch.push({modelName:"<strong>Sell</strong>",multiSelectGroup:true});
if(q.sellList.length>0){angular.forEach(q.sellList,function(H){if(H.dbFieldName!==undefined){var G=H.dbFieldName.split("$");
if(G.length>0){var F=G[1];
if(F!=="IMG"&&F!=="UPD"){q.combinedFieldListForSearch.push(H)
}}}})
}}if(q.sellList.length!==0){q.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(q.roughCalcyList.length>0){q.combinedFieldListForSearch.push({modelName:"<strong>Rough Calcy</strong>",multiSelectGroup:true});
if(q.roughCalcyList.length>0){angular.forEach(q.roughCalcyList,function(H){if(H.dbFieldName!==undefined){var G=H.dbFieldName.split("$");
if(G.length>0){var F=G[1];
if(F!=="IMG"&&F!=="UPD"){q.combinedFieldListForSearch.push(H)
}}}})
}}if(q.roughCalcyList.length!==0){q.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(q.subLotList.length>0){q.combinedFieldListForSearch.push({modelName:"<strong>Sub Lot</strong>",multiSelectGroup:true});
if(q.subLotList.length>0){angular.forEach(q.subLotList,function(H){if(H.dbFieldName!==undefined){var G=H.dbFieldName.split("$");
if(G.length>0){var F=G[1];
if(F!=="IMG"&&F!=="UPD"){q.combinedFieldListForSearch.push(H)
}}}})
}}if(q.subLotList.length!==0){q.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(q.purchaseList.length>0){q.combinedFieldListForSearch.push({modelName:"<strong>Rough Purchase</strong>",multiSelectGroup:true});
if(q.purchaseList.length>0){angular.forEach(q.purchaseList,function(H){if(H.dbFieldName!==undefined){var G=H.dbFieldName.split("$");
if(G.length>0){var F=G[1];
if(F!=="IMG"&&F!=="UPD"){q.combinedFieldListForSearch.push(H)
}}}})
}}if(q.purchaseList.length!==0){q.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(q.roughMakeableList.length>0){q.combinedFieldListForSearch.push({modelName:"<strong>Sub Lot</strong>",multiSelectGroup:true});
if(q.roughMakeableList.length>0){angular.forEach(q.roughMakeableList,function(H){if(H.dbFieldName!==undefined){var G=H.dbFieldName.split("$");
if(G.length>0){var F=G[1];
if(F!=="IMG"&&F!=="UPD"){q.combinedFieldListForSearch.push(H)
}}}})
}}if(q.roughMakeableList.length!==0){q.combinedFieldListForSearch.push({multiSelectGroup:false})
}if(q.finalMakeableList.length>0){q.combinedFieldListForSearch.push({modelName:"<strong>Sub Lot</strong>",multiSelectGroup:true});
if(q.finalMakeableList.length>0){angular.forEach(q.finalMakeableList,function(H){if(H.dbFieldName!==undefined){var G=H.dbFieldName.split("$");
if(G.length>0){var F=G[1];
if(F!=="IMG"&&F!=="UPD"){q.combinedFieldListForSearch.push(H)
}}}})
}}if(q.finalMakeableList.length!==0){q.combinedFieldListForSearch.push({multiSelectGroup:false})
}q.valList={Invoice:q.invoiceList,Parcel:q.parcelList,Lot:q.lotList,Packet:q.packetList,Issue:q.issueList,Plan:q.planList,Allotment:q.allotmentList,Transfer:q.transferList,Sell:q.sellList,RoughCalcy:q.roughCalcyList,SubLot:q.subLotList,Purchase:q.purchaseList,RoughMakeable:q.roughMakeableList,FinalMakeable:q.finalMakeableList};
if(q.invoiceList.length>0){q.combinedFieldListForParent.push({modelName:"<strong>Invoice</strong>",multiSelectGroup1:true});
if(q.invoiceList.length>0){angular.forEach(q.invoiceList,function(F){q.combinedFieldListForParent.push(angular.copy(F))
})
}}if(q.invoiceList.length!==0){q.combinedFieldListForParent.push({multiSelectGroup1:false})
}if(q.parcelList.length>0){q.combinedFieldListForParent.push({modelName:"<strong>Parcel</strong>",multiSelectGroup1:true});
if(q.parcelList.length>0){angular.forEach(q.parcelList,function(F){q.combinedFieldListForParent.push(angular.copy(F))
})
}}if(q.parcelList.length!==0){q.combinedFieldListForParent.push({multiSelectGroup1:false})
}if(q.lotList.length>0){q.combinedFieldListForParent.push({modelName:"<strong>Lot</strong>",multiSelectGroup1:true});
if(q.lotList.length>0){angular.forEach(q.lotList,function(F){q.combinedFieldListForParent.push(angular.copy(F))
})
}}if(q.lotList.length!==0){q.combinedFieldListForParent.push({multiSelectGroup1:false})
}if(q.packetList.length>0){q.combinedFieldListForParent.push({modelName:"<strong>Packet</strong>",multiSelectGroup1:true});
if(q.packetList.length>0){angular.forEach(q.packetList,function(F){q.combinedFieldListForParent.push(angular.copy(F))
})
}}if(q.packetList.length!==0){q.combinedFieldListForParent.push({multiSelectGroup1:false})
}if(q.issueList.length>0){q.combinedFieldListForParent.push({modelName:"<strong>Issue</strong>",multiSelectGroup1:true});
if(q.issueList.length>0){angular.forEach(q.issueList,function(F){q.combinedFieldListForParent.push(angular.copy(F))
})
}}if(q.issueList.length!==0){q.combinedFieldListForParent.push({multiSelectGroup1:false})
}if(q.roughCalcyList.length>0){q.combinedFieldListForParent.push({modelName:"<strong>Rough Calcy</strong>",multiSelectGroup1:true});
if(q.roughCalcyList.length>0){angular.forEach(q.roughCalcyList,function(F){q.combinedFieldListForParent.push(angular.copy(F))
})
}}if(q.roughCalcyList.length!==0){q.combinedFieldListForParent.push({multiSelectGroup1:false})
}if(q.subLotList.length>0){q.combinedFieldListForParent.push({modelName:"<strong>Sub Lot</strong>",multiSelectGroup1:true});
if(q.subLotList.length>0){angular.forEach(q.subLotList,function(F){q.combinedFieldListForParent.push(angular.copy(F))
})
}}if(q.subLotList.length!==0){q.combinedFieldListForParent.push({multiSelectGroup1:false})
}if(q.purchaseList.length>0){q.combinedFieldListForParent.push({modelName:"<strong>Rough Purchase</strong>",multiSelectGroup1:true});
if(q.purchaseList.length>0){angular.forEach(q.purchaseList,function(F){q.combinedFieldListForParent.push(angular.copy(F))
})
}}if(q.purchaseList.length!==0){q.combinedFieldListForParent.push({multiSelectGroup1:false})
}if(q.roughMakeableList.length>0){q.combinedFieldListForParent.push({modelName:"<strong>Sub Lot</strong>",multiSelectGroup1:true});
if(q.roughMakeableList.length>0){angular.forEach(q.roughMakeableList,function(F){q.combinedFieldListForParent.push(angular.copy(F))
})
}}if(q.roughMakeableList.length!==0){q.combinedFieldListForParent.push({multiSelectGroup1:false})
}if(q.finalMakeableList.length>0){q.combinedFieldListForParent.push({modelName:"<strong>Sub Lot</strong>",multiSelectGroup1:true});
if(q.finalMakeableList.length>0){angular.forEach(q.finalMakeableList,function(F){q.combinedFieldListForParent.push(angular.copy(F))
})
}}if(q.finalMakeableList.length!==0){q.combinedFieldListForParent.push({multiSelectGroup1:false})
}var C=q.diamondFeature.menuLabel.split("_");
var z=C[0];
q.commonList=[];
angular.forEach(q.sectionList,function(F){angular.forEach(F.entitys,function(G){if(!!G){angular.forEach(q.valList[G.id],function(I){var H=angular.copy(I);
H.sectionCode=F.sectionId;
q.commonList.push(H)
})
}})
});
angular.forEach(q.commonList,function(F){F.selected="hide"
});
q.commonList.sort(l);
if(q.isEditing||q.isCopy||B.configure===true){q.editFieldList=[];
q.editFieldList=q.featureFieldMap[B.id];
angular.forEach(q.editFieldList,function(F){if(F.searchFlag&&F.sectionCode==="GEN"){F.ticked=true;
q.selectedFieldsForSearch.push(F);
F.ticked=false
}angular.forEach(q.combinedFieldListForSearch,function(G){if(F.field===G.field&&F.searchFlag){G.ticked=true
}})
});
angular.forEach(q.editFieldList,function(F){if(F.parentViewFlag&&F.sectionCode==="GEN"){F.ticked=true;
q.selectedFieldsForParent.push(F);
F.ticked=false
}angular.forEach(q.combinedFieldListForParent,function(G){if(F.field===G.field&&F.parentViewFlag){G.ticked=true
}})
})
}if(!q.isEditing&&!q.isCopy&&B.configure===false){var v=q.diamondSystemFeatureList.length;
for(var D in q.featureFieldMap){for(var A=0;
A<v;
A++){if(D===q.diamondSystemFeatureList[A].id&&B.configure===false){var u=q.diamondSystemFeatureList[A].menuLabel.split("_");
if(u[0]===C[0]){q.editFieldList=[];
q.editFieldList=q.featureFieldMap[D];
angular.forEach(q.editFieldList,function(F){if(F.searchFlag&&F.sectionCode==="GEN"){F.ticked=true;
q.selectedFieldsForSearch.push(F);
F.ticked=false
}angular.forEach(q.combinedFieldListForSearch,function(G){if(F.field===G.field&&F.searchFlag){G.ticked=true
}})
});
angular.forEach(q.editFieldList,function(F){if(F.parentViewFlag&&F.sectionCode==="GEN"){F.ticked=true;
q.selectedFieldsForParent.push(F);
F.ticked=false
}angular.forEach(q.combinedFieldListForParent,function(G){if(F.field===G.field&&F.parentViewFlag){G.ticked=true
}})
})
}}}}}$("#configurePopUp"+((q.uniqueIndex===null||q.uniqueIndex===undefined)?"":q.uniqueIndex)).modal("show")
}};
q.setSearchField=function(t){q.selectedFieldsForSearch=angular.copy(t)
};
q.setParentField=function(t){q.selectedFieldsForParent=angular.copy(t)
};
q.cancelConfirmPopup=function(){$("#notConfiguredPopup").modal("hide");
o.removeModalOpenCssAfterModalHide()
};
q.cancelConfigurationPopup=function(){n=0;
q.nextPage=false;
q.firstPage=true;
q.secondPage=false;
q.selectedFieldsForSearch=[];
q.clearSelectedSearchFields();
q.clearSelectedParentFields();
q.selectedFieldsForParent=[];
q.commonList=[];
q.fieldListToSend=[];
q.fieldListToSendTemp=[];
q.entitys=[];
for(var u=0;
u<q.systemFeaturesList.length;
u++){q.systemFeaturesList[u].configure=false
}$("#configurePopUp"+((q.uniqueIndex===null||q.uniqueIndex===undefined)?"":q.uniqueIndex)).modal("hide");
o.removeModalOpenCssAfterModalHide();
if((q.featureFieldMap[q.diamondFeature.id]!==undefined&&q.featureFieldMap[q.diamondFeature.id]!==null)&&q.featureFieldMap[q.diamondFeature.id].length>0){for(var t=0;
t<q.featureFieldMap[q.diamondFeature.id].length;
t++){delete q.featureFieldMap[q.diamondFeature.id][t].ticked
}}};
q.setNextPage=function(u,w){if(u===1){q.flags.modifierSubmitted=true;
if(w.$valid){q.secondPage=true;
q.firstPage=false
}}if(u===2){q.nextPage=true;
if(q.isEditing){if(q.editFieldList){for(var v=0;
v<q.editFieldList.length;
v++){for(var t=0;
t<q.commonList.length;
t++){if(q.editFieldList[v].field===q.commonList[t].field&&q.editFieldList[v].sectionCode===q.commonList[t].sectionCode){if(q.editFieldList[v].readonlyFlag){q.commonList[t].selected="view only"
}else{if(q.editFieldList[v].editableFlag){q.commonList[t].selected="view and edit"
}else{q.commonList[t].selected="hide"
}}q.commonList[t].sequenceNo=q.editFieldList[v].sequenceNo;
q.commonList[t].isRequired=q.editFieldList[v].isRequired
}}}q.commonList.sort(l)
}}}};
q.setPreviousPage=function(t){if(t===2){q.nextPage=false
}if(t===1){q.firstPage=true;
q.secondPage=false
}};
function l(t,u){if(t.sequenceNo===""||t.sequenceNo===null){return 1
}if(u.sequenceNo===""||u.sequenceNo===null){return -1
}return t.sequenceNo-u.sequenceNo
}q.sortSequence=function(){q.commonList.sort(l)
};
q.prepareModifiers=function(){if(q.modifiers.noRecords!==true){var t={};
angular.forEach(q.modifiers,function(x,u){switch(u){case"medium":t.iRMediums=[];
angular.forEach(x,function(y){if(y.value===true){t.iRMediums.push(y.id)
}});
break;
case"type":t.iRTypes=[];
angular.forEach(x,function(y){if(y.value===true){t.iRTypes.push(y.id)
}});
break;
case"mode":t.iRModes=[];
angular.forEach(x,function(y){if(y.value===true){t.iRModes.push(y.id)
}});
break;
case"access":t.iRVSRAccessRights=[];
if(q.modifiers.showAccessModifiers===true){angular.forEach(x,function(y){if(y.value===true){t.iRVSRAccessRights.push(y.id)
}})
}break;
case"designation":t.asDesignation=[];
var w=x[0].value;
if(w!==null&&w!==""&&!(w instanceof Array)){var v=w.split(",");
angular.forEach(v,function(y){var z=y.split(":");
t.asDesignation.push(z[0])
})
}else{if(w!==null&&w!==""&&(w instanceof Array)){angular.forEach(w,function(y){var z=y.id.split(":");
t.asDesignation.push(z[0])
})
}}break;
case"plan":t.planAccess=[];
angular.forEach(x,function(y){if(y.value===true){t.planAccess.push(y.id)
}});
if(q.flags.planAccesArray!==null&&q.flags.planAccesArray!==undefined&&q.flags.planAccesArray.length>0){t.showPlanUsers=angular.copy(q.flags.planAccesArray);
angular.forEach(t.showPlanUsers,function(z){if(angular.isObject(z.users)){var y=angular.copy(z.users[0]);
delete z.users;
if(y!==undefined&&y!==null){z.users=y.id
}else{z.users=""
}}delete z.autoCompleteInvitees
})
}break
}});
return t
}return null
};
q.savePermission=function(){var x={};
for(var v=0;
v<q.commonList.length;
v++){if(q.commonList[v].sectionCode==="GEN"){for(var u=0;
u<q.fieldListToSend.length;
u++){if(q.commonList[v].field===q.fieldListToSend[u].field){if(q.commonList[v].selected==="hide"){}else{if(q.commonList[v].selected==="view only"){q.fieldListToSend[u].readonlyFlag=true
}else{if(q.commonList[v].selected==="view and edit"){q.fieldListToSend[u].editableFlag=true
}}}q.fieldListToSend[u].sequenceNo=q.commonList[v].sequenceNo;
q.fieldListToSend[u].isRequired=q.commonList[v].isRequired;
q.fieldListToSend[u].sectionCode="GEN"
}}}else{angular.forEach(q.sectionList,function(z){if(q.commonList[v].sectionCode===z.sectionId){if(x[z.sectionId]===undefined||x[z.sectionId]===null){x[z.sectionId]={};
angular.forEach(z.entitys,function(A){if(!!A){console.log(A.id+q.valList[A.id].length+z.sectionId);
if(x[z.sectionId][A.id]===undefined||x[z.sectionId][A.id]===null){x[z.sectionId][A.id]=[]
}angular.forEach(q.valList[A.id],function(C){var B=angular.copy(C);
B.sectionCode=z.sectionId;
x[z.sectionId][A.id].push(B)
})
}})
}for(var y=0;
y<x[z.sectionId][q.commonList[v].entity].length;
y++){if(q.commonList[v].field===x[z.sectionId][q.commonList[v].entity][y].field){if(q.commonList[v].selected==="hide"){}else{if(q.commonList[v].selected==="view only"){x[z.sectionId][q.commonList[v].entity][y].readonlyFlag=true
}else{if(q.commonList[v].selected==="view and edit"){x[z.sectionId][q.commonList[v].entity][y].editableFlag=true
}}}x[z.sectionId][q.commonList[v].entity][y].sequenceNo=q.commonList[v].sequenceNo;
x[z.sectionId][q.commonList[v].entity][y].isRequired=q.commonList[v].isRequired
}}}})
}}for(var v=0;
v<q.selectedFieldsForSearch.length;
v++){for(var u=0;
u<q.fieldListToSend.length;
u++){if(q.selectedFieldsForSearch[v].field===q.fieldListToSend[u].field){q.fieldListToSend[u].searchFlag=true;
q.fieldListToSend[u].sectionCode="GEN"
}}}for(var v=0;
v<q.selectedFieldsForParent.length;
v++){for(var u=0;
u<q.fieldListToSend.length;
u++){if(q.selectedFieldsForParent[v].field===q.fieldListToSend[u].field){q.fieldListToSend[u].parentViewFlag=true;
q.fieldListToSend[u].sectionCode="GEN"
}}}if(Object.keys(x).length>0){angular.forEach(x,function(y){angular.forEach(y,function(z){q.fieldListToSend.push.apply(q.fieldListToSend,angular.copy(z))
})
})
}q.featureFieldMap[q.diamondFeature.id]=q.fieldListToSend;
var t=q.prepareModifiers();
console.log("modifiers :"+JSON.stringify(t));
if(t!=null){if(q.featureModifierMap[q.diamondFeature.id]===null||q.featureModifierMap[q.diamondFeature.id]===undefined){q.featureModifierMap[q.diamondFeature.id]=t
}else{angular.forEach(t,function(z,y){q.featureModifierMap[q.diamondFeature.id][y]=z
})
}}var w=q.diamondSystemFeatureList.length;
for(var v=0;
v<w;
v++){if(q.diamondSystemFeatureList[v].id===q.diamondFeature.id&&q.diamondSystemFeatureList[v].checked){q.diamondSystemFeatureList[v].configure=true
}}q.cancelConfigurationPopup();
$("#configurePopUp"+((q.uniqueIndex===null||q.uniqueIndex===undefined)?"":q.uniqueIndex)).modal("hide");
o.removeModalOpenCssAfterModalHide();
n=0
};
q.commonListDtOptions={aoColumnDefs:[{bSortable:false,aTargets:[-2,-3]}]};
q.changeEntityList=function(){angular.forEach(q.sectionList,function(t){if(t.sectionId===q.entitys.section){q.entityList=t.entitys
}});
if(!!q.entityList){q.entitys.name=q.entityList[0].id
}q.fieldTableContent()
};
var n=0;
q.fieldTableContent=function(){n++;
if(n<=0){q.commonList=[];
angular.forEach(q.visibleFields[q.diamondFeature.menuLabel],function(v){if(!!v){if(!!!q.commonList||q.commonList.length<1){q.commonList=[];
q.commonList=angular.copy(q.valList[v.id])
}else{$.merge(q.commonList,q.valList[v.id])
}}});
angular.forEach(q.commonList,function(v){v.selected="hide";
v.isRequired=false
});
if(q.editFieldList){for(var u=0;
u<q.editFieldList.length;
u++){for(var t=0;
t<q.commonList.length;
t++){if(q.editFieldList[u].field===q.commonList[t].field){if(q.editFieldList[u].readonlyFlag){q.commonList[t].selected="view only"
}else{if(q.editFieldList[u].editableFlag){q.commonList[t].selected="view and edit"
}else{q.commonList[t].selected="hide"
}}q.commonList[t].sequenceNo=q.editFieldList[u].sequenceNo;
q.commonList[t].isRequired=q.editFieldList[u].isRequired
}}}}}q.entitys.filteredList=m("commonListFilter")(q.commonList,q.entitys.name,q.entitys.section)
};
q.popover="<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>'@R'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Designations</td></tr> </table> ";
q.planpopover="<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>'@R'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Designations</td></tr> <tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employee</td></tr> <tr><td>'@D'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Department</td></tr> </table> ";
q.initDesignations=function(){q.autoCompleteDesignation={multiple:true,closeOnSelect:false,placeholder:"Select Designation",initSelection:function(v,y){var x=q.featureModifierMap[q.diamondFeature.id];
if(q.modifiers.designation[0].value&&x!=null&&x.asDesignationIdName!=null){var u=q.modifiers.designation[0].value;
if(!(u instanceof Array)){var t=u.split(",");
if(t.length>0){var w=[];
angular.forEach(t,function(z){w.push({id:z,text:x.asDesignationIdName[z]})
});
y(w)
}}}},formatResult:function(t){return t.text
},formatSelection:function(t){return t.text
},query:function(w){var v=w.term;
q.names=[];
var y=function(z){if(z.length!==0){q.names=z;
angular.forEach(z,function(A){q.names.push({id:A.value+":"+A.description,text:A.label})
})
}w.callback({results:q.names})
};
var t=function(){};
if(v.substring(0,2)==="@R"||v.substring(0,2)==="@r"){var u=w.term.slice(2);
var x={parentRole:q.designationId,searchRole:u.trim()};
d.retrieveChildRoles(x,y,t)
}else{w.callback({results:q.names})
}}}
};
q.initDesignations();
q.changeInPlanModifier=function(t){if(t!==null&&t!==undefined&&t.id==="SP"){if(t.value===true){q.flags.planFlag=true;
q.flags.planAccesArray=[{users:"",autoCompleteInvitees:angular.copy(q.autoCompleteInvitees)}]
}else{q.flags.planFlag=false;
q.flags.planAccesArray=[]
}}};
q.addnewPlanAccess=function(t){q.submitted=true;
if(t.$valid){q.flags.planAccesArray.push({users:"",autoCompleteInvitees:angular.copy(q.autoCompleteInvitees)})
}};
q.deletePlanAccess=function(t){if(q.flags.planAccesArray!==undefined&&q.flags.planAccesArray!==null&&q.flags.planAccesArray.length>0){q.flags.planAccesArray.splice(t,1);
if(q.flags.planAccesArray.length===0){q.flags.planAccesArray=[{users:"",autoCompleteInvitees:angular.copy(q.autoCompleteInvitees)}]
}}};
q.autoCompleteInvitees={multiple:true,closeOnSelect:false,placeholder:"Select members",width:"resolve",initSelection:function(t,u){},formatResult:function(t){return t.text
},formatSelection:function(t){return t.text
},query:function(w){var v=w.term;
q.names=[];
var x=function(y){if(y.length!==0){q.names=y;
angular.forEach(y,function(z){q.names.push({id:z.value+":"+z.description,text:z.label})
})
}w.callback({results:q.names})
};
var t=function(){};
if(v.substring(0,2)==="@E"||v.substring(0,2)==="@e"){var u=w.term.slice(2);
e.retrieveUserList(u.trim(),x,t)
}else{if(v.substring(0,2)==="@D"||v.substring(0,2)==="@d"){var u=w.term.slice(2);
e.retrieveDepartmentList(u.trim(),x,t)
}else{if(v.substring(0,2)==="@R"||v.substring(0,2)==="@r"){var u=w.term.slice(2);
e.retrieveRoleList(u.trim(),x,t)
}}}}}
}];
return{restrict:"E",link:g,scope:f,controller:c,templateUrl:"scripts/directives/designationtemplate/desigtemplate.tmpl.html"}
}]);
globalProvider.filterProvider.register("commonListFilter",function(){return function(a,e,f){if(angular.isDefined(a)&&angular.isDefined(e)){if(angular.isDefined(e)&&!!e){var b=[];
for(var c=0;
c<a.length;
c++){var d=a[c];
if((d.entity!==null&&d.entity.toLowerCase().indexOf(e.toLowerCase())>=0)&&d.sectionCode===f){b.push(d)
}}return b
}}else{if($scope.type==="DMI"){return a
}else{return null
}}}
})
});