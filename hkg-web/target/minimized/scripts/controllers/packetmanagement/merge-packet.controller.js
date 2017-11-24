define(["hkg","packetService","lotService","customFieldService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm"],function(a,b){a.register.controller("MergePacketController",["$rootScope","$scope","PacketService","$timeout","$filter","$location","$window","DynamicFormService","LotService","CustomFieldService",function(j,m,f,d,g,e,c,l,h,k){j.mainMenu="stockLink";
j.childMenu="mergeLot";
j.activateMenu();
var i={};
m.initializeData=function(n){if(n){m.tempMergeList=[];
m.packetDataBean={};
m.searchedData=[];
m.searchedDataFromDb=[];
m.listFilled=false;
m.lotListTodisplay=[];
m.searchCustom=l.resetSection(m.generalSearchTemplate);
m.packetDataBean.featureCustomMap={};
var o=l.retrieveSearchWiseCustomFieldInfo("lotMerge");
m.flag={};
m.dbType={};
m.modelAndHeaderList=[];
m.modelAndHeaderListForLot=[];
o.then(function(r){m.generalSearchTemplate=r.genralSection;
if(m.generalSearchTemplate!=null&&m.generalSearchTemplate.length>0){for(var p=0;
p<m.generalSearchTemplate.length;
p++){var q=m.generalSearchTemplate[p];
i[q.model]=q.featureName;
m.modelAndHeaderList.push({model:q.model,header:q.label})
}}m.dataRetrieved=true
},function(p){},function(p){})
}};
m.initializeData(true);
m.initMergePacketForm=function(n){m.mergePacketForm=n
};
m.retrieveSearchedData=function(p){m.packetDataBean.featureCustomMap={};
if(Object.getOwnPropertyNames(m.searchCustom).length>0){var o=false;
for(var q in m.searchCustom){if(!!(m.searchCustom[q])){o=true;
break
}}if(o){j.maskLoading();
var n={};
angular.forEach(i,function(v,t){var u=m.searchCustom[t];
if(u!==undefined){var s={};
if(!n[v]){s[t]=u;
n[v]=s
}else{var r=n[v];
r[t]=u;
n[v]=r
}}else{u=m.searchCustom["to"+t];
if(u!==undefined){var s={};
if(!n[v]){s["to"+t]=u;
n[v]=s
}else{var r=n[v];
r["to"+t]=u;
n[v]=r
}}u=m.searchCustom["from"+t];
if(u!==undefined){var s={};
if(!n[v]){s["from"+t]=u;
n[v]=s
}else{var r=n[v];
r["from"+t]=u;
n[v]=r
}}}});
m.packetDataBean.featureCustomMapValue=n;
m.packetDataBean.packetCustom=m.searchCustom;
f.search(m.packetDataBean,function(r){m.searchedDataFromDb=angular.copy(r);
var s=l.getValuesOfComponentFromId(r,m.generalSearchTemplate);
m.searchedData=angular.copy(s);
m.listFilled=true;
m.searchCustom=l.resetSection(m.generalSearchTemplate);
j.unMaskLoading()
},function(){var s="Could not retrieve, please try again.";
var r=j.error;
j.addMessage(s,r);
j.unMaskLoading()
})
}}};
m.onCanel=function(){if(m.addLotForm!=null){m.addLotForm.$dirty=false
}m.categoryCustom=l.resetSection(m.generalLotTemplate)
};
m.backToHomeScreen=function(n){if(n!=null){m.flag.showUpdatePage=false;
m.flag.showAddPage=false;
m.flag.selectParent=false
}};
m.backToMergeFieldScreen=function(n){if(n!=null){m.flag.showUpdatePage=true;
m.flag.selectParent=false;
m.submitted=false
}};
m.onCanelOfSearch=function(n){if(m.mergePacketForm!=null){m.mergePacketForm.$dirty=false
}m.searchedData=[];
m.searchCustom=l.resetSection(m.generalSearchTemplate);
m.listFilled=false
};
m.mergePacket=function(){angular.forEach(m.searchedData,function(o){var n=m.tempMergeList.indexOf(o);
if(o.hasPacket&&!(n!==-1)){o.hasPacket=false;
m.tempMergeList.push(o)
}else{o.hasPacket=false
}});
m.flag.mergeListFilled=true
};
m.removePacket=function(n){if(!!(n&&m.searchedDataFromDb&&m.searchedDataFromDb.length>0)){angular.forEach(m.tempMergeList,function(p){var o=m.tempMergeList.indexOf(n);
if(o!==-1){m.tempMergeList.splice(o,1)
}});
if(m.tempMergeList.length===0){m.flag.twoPacketReqd=false
}}};
m.clearTempMergeList=function(n){m.tempMergeList=[];
m.flag.mergeListFilled=false;
m.flag.twoPacketReqd=false
};
m.showMergeFields=function(){m.flag.twoPacketReqd=false;
if(!!(m.tempMergeList&&m.tempMergeList.length>1)){m.flag.showUpdatePage=true;
k.retrieveDesignationBasedFields("packetMerge",function(n){m.packetDataBean={};
m.packetCustom=l.resetSection(m.mergePacketTemplate);
var o=l.retrieveSectionWiseCustomFieldInfo("packet");
m.packetDbType={};
o.then(function(p){m.mergePacketTemplate=p.genralSection;
m.mergePacketTemplate=l.retrieveCustomData(m.mergePacketTemplate,n);
j.unMaskLoading();
m.packetDataBean.packetDbType=m.packetDbType;
m.lotIds=[];
m.selectedParcelDropdown={currentNode:""};
angular.forEach(m.tempMergeList,function(q){if((m.lotIds.length===0)){m.lotIds.push({displayName:q.custom5.lotID,id:q.label})
}angular.forEach(m.lotIds,function(r){if(r.displayName!==q.custom5.lotID){m.lotIds.push({displayName:q.custom5.lotID,id:q.label})
}})
});
m.lotIds.unshift({id:"D",displayName:"Select"});
m.selectedParcelDropdown.currentNode=m.lotIds[0];
m.parcelId="D"
},function(p){},function(p){});
m.flag.customFieldGenerated=true
},function(){j.unMaskLoading();
var o="Failed to retrieve data";
var n=j.error;
j.addMessage(o,n)
})
}else{m.flag.twoPacketReqd=true
}};
m.selectParent=function(n){m.submitted=true;
if(!!(n&&n.$valid)){m.submitted=true;
m.packetDataBean.packetCustom=m.packetCustom;
m.packetDataBean.packetDbType=m.packetDbType;
m.flag.showUpdatePage=false;
m.flag.selectParent=true
}};
m.saveMergedPacket=function(n){m.submitted=true;
if(!!(n&&n.$valid&&m.selectedParcelDropdown.currentNode.id!=="D")){m.submitted=false;
m.packetList=[];
angular.forEach(m.tempMergeList,function(o){m.packetList.push({id:o.value})
});
m.packetDataBean.packetList=angular.copy(m.packetList);
m.packetDataBean.lotDataBean={id:m.selectedParcelDropdown.currentNode.id};
f.mergePacket(m.packetDataBean,function(o){m.initializeData(true)
},function(){j.unMaskLoading();
var p="Could not merge packet, please try again.";
var o=j.error;
j.addMessage(p,o)
})
}}
}])
});