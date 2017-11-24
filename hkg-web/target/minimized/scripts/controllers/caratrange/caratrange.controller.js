define(["hkg","caratRangeService"],function(a){a.register.controller("CaratRangeController",["$rootScope","$scope","CaratRangeService",function(b,d,c){b.maskLoading();
b.mainMenu="manageLink";
b.childMenu="manageCaratRange";
b.activateMenu();
d.entity="CARATERANGE.";
d.initData=function(){c.retrieveCaratWithNoRange(function(e){if(!!e){d.noCaratRangeList=[];
if(!!e.caratRangeWithNoCarat){angular.forEach(e.caratRangeWithNoCarat,function(f){if(d.noCaratRangeList.indexOf(f)===-1){d.noCaratRangeList.push(f)
}})
}}});
b.maskLoading();
c.retrieveAll(function(e){b.unMaskLoading();
d.caratRangeList=[];
if(e){angular.forEach(e,function(f){f.minValue=parseFloat(Math.round(f.minValue*1000)/1000).toFixed(3);
f.maxValue=parseFloat(Math.round(f.maxValue*1000)/1000).toFixed(3);
d.caratRangeList.push(f)
})
}else{d.caratRangeList.push({minValue:"00.000",editingdone:false,newadded:true,isEditing:true})
}d.flagNoCarat=true
},function(){b.unMaskLoading();
console.log("error in retrieving carat range..")
})
};
d.editCaratRange=function(e){d.caratRangeList[e].isEditing=true
};
d.doneEditing=function(h,f){d.caratRangeList[f].editingdone=true;
d.caratRangeList[f].overlappingMin=false;
d.caratRangeList[f].overlappingMax=false;
if(h.$valid){var e=false;
for(var g=0;
g<d.caratRangeList.length;
g++){if(g!==f){if((parseFloat(d.caratRangeList[f].minValue)>=parseFloat(d.caratRangeList[g].minValue)&&parseFloat(d.caratRangeList[f].minValue)<=parseFloat(d.caratRangeList[g].maxValue))){d.caratRangeList[f].overlappingMin=true;
e=true
}if((parseFloat(d.caratRangeList[f].maxValue)>=parseFloat(d.caratRangeList[g].minValue)&&parseFloat(d.caratRangeList[f].maxValue)<=parseFloat(d.caratRangeList[g].maxValue))){d.caratRangeList[f].overlappingMax=true;
e=true
}if((parseFloat(d.caratRangeList[f].minValue)>=parseFloat(d.caratRangeList[g].minValue)&&parseFloat(d.caratRangeList[f].minValue)<=parseFloat(d.caratRangeList[g].maxValue))||(parseFloat(d.caratRangeList[f].maxValue)>=parseFloat(d.caratRangeList[g].minValue)&&parseFloat(d.caratRangeList[f].maxValue)<=parseFloat(d.caratRangeList[g].maxValue))){e=true;
break
}}}if(parseFloat(d.caratRangeList[f].minValue)<parseFloat(d.caratRangeList[f].maxValue)){if(!e){d.caratRangeList[f].isEditing=false;
if(!!d.caratRangeList[f].id){d.caratRangeList[f].edited=true
}}d.caratRangeList[f].minLessThanMaxFlag=false
}else{d.caratRangeList[f].minLessThanMaxFlag=true
}}};
d.formatNumber=function(e){if(!!d.caratRangeList[e].minValue&&!d.caratRangeList[e].minValue.isNaN){d.caratRangeList[e].minValue=parseFloat(Math.round(d.caratRangeList[e].minValue*1000)/1000).toFixed(3)
}if(!!d.caratRangeList[e].maxValue&&!d.caratRangeList[e].maxValue.isNaN){d.caratRangeList[e].maxValue=parseFloat(Math.round(d.caratRangeList[e].maxValue*1000)/1000).toFixed(3)
}if(!!d.caratRangeList[e].minValue&&!!d.caratRangeList[e].maxValue){if(d.caratRangeList[e].minValue<d.caratRangeList[e].maxValue){d.caratRangeList[e].minLessThanMaxFlag=false
}}};
d.addClick=function(){if(d.caratRangeForm.$valid){if(!!d.caratRangeList&&d.caratRangeList.length>0){var e=parseFloat(d.caratRangeList[d.caratRangeList.length-1].maxValue)+0.001;
e=parseFloat(Math.round(e*1000)/1000).toFixed(3)
}for(var f=0;
f<d.caratRangeList.length;
f++){d.caratRangeList[f].isEditing=false
}d.caratRangeList.push({minValue:e,editingdone:false,newadded:true,isEditing:true})
}};
d.saveAll=function(){var f=true;
if(!!d.caratRangeList&&d.caratRangeList.length>0){for(var g=0;
g<d.caratRangeList.length;
g++){d.doneEditing(d.caratRangeForm,g);
if(d.caratRangeList[g].overlappingMin||d.caratRangeList[g].overlappingMax||d.caratRangeList[g].minLessThanMaxFlag){f=false;
break
}}}if(d.caratRangeForm.$valid&&f){var h=[];
var e=[];
angular.forEach(d.caratRangeList,function(i){var j={id:i.id,minValue:i.minValue,maxValue:i.maxValue,newadded:i.newadded,edited:i.edited};
h.push(j)
});
if(!!d.toDelete){e=h.concat(d.toDelete)
}if(!!d.toDelete){b.maskLoading();
c.saveAll(e,function(){b.unMaskLoading();
d.caratRangeList=[];
d.toDelete=[];
d.initData()
},function(){b.unMaskLoading();
console.log("Problem while saving...")
})
}else{b.maskLoading();
c.saveAll(h,function(){b.unMaskLoading();
d.caratRangeList=[];
d.toDelete=[];
d.initData()
},function(){b.unMaskLoading();
console.log("Problem while saving...")
})
}}};
d.deleteCaratRange=function(e){if(!!d.caratRangeList[e].id){d.indexTodelete=e;
$("#removeCaratRangeModal").modal("show")
}else{d.caratRangeList.splice(e,1)
}};
d.toDelete=[];
d.removecaratrange=function(){d.caratRangeList[d.indexTodelete].toDelete=true;
d.caratRangeList[d.indexTodelete].edited=false;
d.toDelete.push(d.caratRangeList[d.indexTodelete]);
d.caratRangeList.splice(d.indexTodelete,1);
$("#removeCaratRangeModal").modal("hide");
b.removeModalOpenCssAfterModalHide()
};
d.hideconformationForRemoveCaratRange=function(){$("#removeCaratRangeModal").modal("hide");
b.removeModalOpenCssAfterModalHide()
};
b.unMaskLoading()
}])
});