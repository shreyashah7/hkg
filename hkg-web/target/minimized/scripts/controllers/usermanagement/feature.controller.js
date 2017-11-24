define(["hkg","featureService"],function(a,b){a.register.controller("ManageFeature",["$rootScope","$scope","SysFeature",function(c,d,f){c.maskLoading();
c.mainMenu="manageLink";
c.childMenu="manageFeature";
c.activateMenu();
d.String1=/^[A-z]+$/;
d.showadd=false;
d.showedit=false;
d.i18EntityFeature="FEATURE.";
d.submitted=false;
d.viewPage=true;
d.hasFeatureAccess=c.canAccess("featureAddEdit");
d.precedences=[-1,0,1,2,3,4,5];
d.initFeatureForm=function(g){d.feature_form=g
};
d.$on("$viewContentLoaded",function(){e()
});
function e(){f.retreiveFeatureCategoryList(function(g){d.featurecategorymap=g;
d.menucategory="manage";
d.onChangeCategory()
})
}d.onChangeCategory=function(){d.newlist=d.featurecategorymap[d.menucategory]
};
d.changeSeq=function(){d.submitted=false;
d.viewPage=true
};
d.seqSavechanges=function(){var g=[];
angular.forEach(d.newlist,function(h){g.push(h.id)
});
f.saveSequence(g)
};
d.up=function(g){if(g!=0){var i=g;
var h=d.newlist[i];
d.newlist[i]=d.newlist[i-1];
d.newlist[i-1]=h
}};
d.down=function(g,j){if(j!=true){var i=g;
var h=d.newlist[i];
d.newlist[i]=d.newlist[i+1];
d.newlist[i+1]=h
}};
d.retreiveSystemFeaturesTree=function(){var i=[];
var g=function(j){angular.forEach(j,function(k){i.push(k)
});
d.feature_form.$dirty=false;
c.unMaskLoading()
};
var h=function(){c.unMaskLoading();
var k="Failed to load features";
var j=c.failure;
c.addMessage(k,j)
};
c.maskLoading();
f.retreiveSystemFeatures(g,h);
return i
};
d.featureList=d.retreiveSystemFeaturesTree();
d.featureListDropDown=d.featureList;
d.save={submit:function(g){d.submitted=true;
if(g.$valid){if(d.showadd){d.addSysFeature()
}if(d.showedit){d.editSysFeature()
}d.feature_form.$setPristine()
}}};
d.rootClick=function(){d.feature.parentId=null;
d.feature.parentName="Root";
if(d.features.currentNode!=undefined){d.features.currentNode.selected=undefined
}if(d.featuresDropDown.currentNode!=undefined){d.featuresDropDown.currentNode.selected=undefined
}};
d.featureClick=function(g){d.feature.parentId=g.currentNode.id;
d.feature.parentName=g.currentNode.displayName
};
d.showAdd=function(){d.viewPage=false;
d.showadd=true;
d.showedit=false;
d.feature={};
if(d.features.currentNode!=undefined){d.features.currentNode.selected=undefined
}if(d.featuresDropDown.currentNode!=undefined){d.featuresDropDown.currentNode.selected=undefined
}d.rootClick()
};
d.addSysFeature=function(){var g=function(i){if(i.result==0){d.feature.featureName=""
}else{d.featureList=d.retreiveSystemFeaturesTree();
d.featureListDropDown=d.featureList;
d.feature={};
d.showadd=false;
d.showedit=false;
d.submitted=false;
if(d.features.currentNode!=undefined){d.features.currentNode.selected=undefined
}if(d.featuresDropDown.currentNode!=undefined){d.featuresDropDown.currentNode.selected=undefined
}e()
}d.feature_form.$setPristine();
c.unMaskLoading()
};
var h=function(){var j="Failed to create feature";
var i=c.error;
c.addMessage(j,i);
c.unMaskLoading()
};
c.maskLoading();
f.createSysFeature(d.feature,g,h)
};
d.showEdit=function(g){if(angular.isDefined(g)){d.viewPage=false;
d.showedit=true;
d.showadd=false;
d.feature=angular.copy(g.currentNode);
if(d.feature.menuType==="MI"||d.feature.menuType==="EI"||d.feature.menuType==="DEI"||d.feature.menuType==="DMI"){d.title=""
}}};
d.editSysFeature=function(){d.feature.selected=undefined;
var g=function(i){if(i.result==0){d.feature.featureName=""
}else{d.featureList=d.retreiveSystemFeaturesTree();
d.featureListDropDown=d.featureList;
d.feature={};
d.showedit=false;
d.showadd=false;
d.submitted=false;
if(d.features.currentNode!=undefined){d.features.currentNode.selected=undefined
}if(d.featuresDropDown.currentNode!=undefined){d.featuresDropDown.currentNode.selected=undefined
}e()
}c.unMaskLoading()
};
var h=function(){c.unMaskLoading();
var j="Failed to update feature";
var i=c.failure;
c.addMessage(j,i)
};
d.feature.children=null;
c.maskLoading();
f.updateSysFeature(d.feature,g,h)
};
c.unMaskLoading()
}])
});