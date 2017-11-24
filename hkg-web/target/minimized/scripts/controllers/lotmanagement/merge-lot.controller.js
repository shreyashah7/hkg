define(["hkg","lotService","customFieldService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm"],function(b,a){b.register.controller("MergeLotController",["$rootScope","$scope","LotService","$timeout","$filter","$location","$window","DynamicFormService","CustomFieldService",function(i,l,g,d,f,e,c,k,j){i.mainMenu="stockLink";
i.childMenu="mergeLot";
i.activateMenu();
var h={};
l.initializeData=function(m){if(m){l.tempMergeList=[];
l.lotDataBean={};
l.searchedData=[];
l.searchedDataFromDb=[];
l.listFilled=false;
l.lotListTodisplay=[];
l.searchCustom=k.resetSection(l.generalSearchTemplate);
l.lotDataBean.featureCustomMap={};
var n=k.retrieveSearchWiseCustomFieldInfo("lotMerge");
l.flag={};
l.dbType={};
l.modelAndHeaderList=[];
l.modelAndHeaderListForLot=[];
n.then(function(q){l.generalSearchTemplate=q.genralSection;
if(l.generalSearchTemplate!=null&&l.generalSearchTemplate.length>0){for(var o=0;
o<l.generalSearchTemplate.length;
o++){var p=l.generalSearchTemplate[o];
h[p.model]=p.featureName;
l.modelAndHeaderList.push({model:p.model,header:p.label})
}}l.dataRetrieved=true
},function(o){},function(o){})
}};
l.initializeData(true);
l.initEditLotForm=function(m){l.editLotForm=m
};
l.retrieveSearchedData=function(o){l.lotDataBean.featureCustomMap={};
if(Object.getOwnPropertyNames(l.searchCustom).length>0){var n=false;
for(var p in l.searchCustom){if(!!(l.searchCustom[p])){n=true;
break
}}if(n){i.maskLoading();
var m={};
angular.forEach(h,function(u,s){var t=l.searchCustom[s];
if(t!==undefined){var r={};
if(!m[u]){r[s]=t;
m[u]=r
}else{var q=m[u];
q[s]=t;
m[u]=q
}}else{t=l.searchCustom["to"+s];
if(t!==undefined){var r={};
if(!m[u]){r["to"+s]=t;
m[u]=r
}else{var q=m[u];
q["to"+s]=t;
m[u]=q
}}t=l.searchCustom["from"+s];
if(t!==undefined){var r={};
if(!m[u]){r["from"+s]=t;
m[u]=r
}else{var q=m[u];
q["from"+s]=t;
m[u]=q
}}}});
l.lotDataBean.featureCustomMapValue=m;
l.lotDataBean.lotCustom=l.searchCustom;
l.lotDataBean.hasPacket=false;
g.search(l.lotDataBean,function(q){l.searchedDataFromDb=angular.copy(q);
var r=k.getValuesOfComponentFromId(q,l.generalSearchTemplate);
l.searchedData=angular.copy(r);
l.listFilled=true;
l.searchCustom=k.resetSection(l.generalSearchTemplate);
i.unMaskLoading()
},function(){var r="Could not retrieve, please try again.";
var q=i.error;
i.addMessage(r,q);
i.unMaskLoading()
})
}}};
l.onCanelOfSearch=function(m){if(l.addLotForm!=null){l.addLotForm.$dirty=false
}l.searchedData=[];
l.searchCustom=k.resetSection(l.generalSearchTemplate);
l.listFilled=false
};
l.mergeLot=function(){angular.forEach(l.searchedData,function(n){var m=l.tempMergeList.indexOf(n);
if(n.hasPacket&&!(m!==-1)){n.hasPacket=false;
l.tempMergeList.push(n)
}else{n.hasPacket=false
}})
};
l.removeLot=function(m){if(!!(m&&l.searchedDataFromDb&&l.searchedDataFromDb.length>0)){angular.forEach(l.tempMergeList,function(o){var n=l.tempMergeList.indexOf(m);
if(n!==-1){l.tempMergeList.splice(n,1)
}});
if(l.tempMergeList.length===0){l.flag.twoPacketReqd=false
}}};
l.showMergeFields=function(){l.flag.twoPacketReqd=false;
if(!!(l.tempMergeList&&l.tempMergeList.length>1)){l.flag.showUpdatePage=true;
j.retrieveDesignationBasedFields("lotMerge",function(m){l.lotDataBean={};
l.lotCustom=k.resetSection(l.mergeLotTemplate);
var n=k.retrieveSectionWiseCustomFieldInfo("lot");
l.lotDbType={};
n.then(function(o){l.mergeLotTemplate=o.genralSection;
l.mergeLotTemplate=k.retrieveCustomData(l.mergeLotTemplate,m);
i.unMaskLoading();
l.lotDataBean.lotDbType=l.lotDbType;
l.parcelIds=[];
l.selectedParcelDropdown={currentNode:""};
angular.forEach(l.tempMergeList,function(p){if((l.parcelIds.length===0)){l.parcelIds.push({displayName:p.custom4.parcelID,id:p.label})
}angular.forEach(l.parcelIds,function(q){if(q.displayName!==p.custom5.lotID){l.parcelIds.push({displayName:p.custom4.parcelID,id:p.label})
}})
});
l.parcelIds.unshift({id:"D",displayName:"Select"});
l.selectedParcelDropdown.currentNode=l.parcelIds[0];
l.parcelId="D"
},function(o){},function(o){});
l.flag.customFieldGenerated=true
},function(){i.unMaskLoading();
var n="Failed to retrieve data";
var m=i.error;
i.addMessage(n,m)
})
}else{l.flag.twoPacketReqd=true
}};
l.clearTempMergeList=function(m){l.tempMergeList=[];
l.flag.mergeListFilled=false;
l.flag.twoPacketReqd=false
};
l.backToHomeScreen=function(m){if(m!=null){l.flag.showUpdatePage=false;
l.flag.showAddPage=false;
l.flag.selectParent=false
}};
l.backToMergeFieldScreen=function(m){if(m!=null){l.flag.showUpdatePage=true;
l.flag.selectParent=false
}};
l.selectParent=function(m){if(!!(m&&m.$valid)){l.lotDataBean.lotCustom=l.lotCustom;
l.lotDataBean.lotDbType=l.lotDbType;
l.flag.showUpdatePage=false;
l.flag.selectParent=true
}};
l.saveMergedLot=function(m){l.submitted=true;
if(!!(m&&m.$valid&&l.selectedParcelDropdown.currentNode.id!=="D")){l.submitted=false;
l.lotList=[];
angular.forEach(l.tempMergeList,function(n){l.lotList.push({id:n.value})
});
l.lotDataBean.lotList=angular.copy(l.lotList,l.lotDataBean.lotList);
l.lotDataBean.parcelDataBean={id:l.selectedParcelDropdown.currentNode.id};
g.mergeLot(l.lotDataBean,function(n){l.initializeData(true)
},function(){i.unMaskLoading();
var o="Could not merge lot, please try again.";
var n=i.error;
i.addMessage(o,n)
})
}}
}])
});