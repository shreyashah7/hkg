define(["hkg","internationalizationService","alert.directive"],function(a,b){a.register.controller("InternationalizationLabelList",["$rootScope","$scope","Internationalization",function(c,d,f){console.log("-->InternationalizationLabelList initialized");
d.i18Int="INTERNATIONALIZATION.";
d.changeAutomaticLabelFlag=c.createLabelAutomatically;
d.setCreateLabelAutomatically=function(){c.createLabelAutomatically=d.changeAutomaticLabelFlag
};
d.retrieveLanguages=function(){d.languageDetails=f.getAllLanguages(function(h){f.getConstants(function(i){d.labelTypes=i.types;
d.labelEnvironments=i.environments;
d.labelEntitys=i.entities;
d.setDefaultTable(d.labelTypes,d.labelEnvironments,d.labelEntitys)
},function(){var j="Failed to get Constants";
var i=c.failure;
c.addMessage(j,i)
});
for(var g=0;
g<h.length;
g++){if(h[g].code==="EN"){d.selectedLanguageObject=h[g];
d.selectedLanguageObj=h[g];
c.maskLoading();
d.translationPendingLabelList=f.getTranslationPendingLabels(h[g],function(i){c.unMaskLoading()
});
break
}}d.setDefaultTable=function(k,j,i){k[0]="All";
d.selectedLabelTypeForAll=Object.keys(k)[0];
j[0]="All";
d.selectedLabelEnvironmentForAll=Object.keys(j)[0];
i[i.length]="All";
d.selectedLabelEntityForAll=i[i.length-1];
d.setLetter("pending");
d.isDisable=false
}
},function(){var h="Failed to get All Languages";
var g=c.failure;
c.addMessage(h,g)
})
};
d.orderProp="defaultText";
d.direction=false;
d.sort=function(g){if(d.orderProp===g){d.direction=!d.direction
}else{d.orderProp=g;
d.direction=false
}};
d.setSelectedTypeAll=function(){d.isDisable=true
};
d.setSelectedEnvironmentAll=function(){d.isDisable=true
};
d.setSelectedEntityAll=function(){d.isDisable=true
};
d.setSelectedEnvironment=function(h,g){d.filters={};
g.environment=h;
d.selectedLabelEnvironment=h
};
d.setSelectedType=function(h,g){d.filters={};
g.type=h;
d.selectedLabelType=h
};
d.updatedTranslation=null;
d.setTranslationPending=function(g){if(g===true){d.updatedTranslation=false
}else{d.updatedTranslation=true
}d.filters={}
};
d.setLanguage=function(){d.isDisable=true;
d.selectedLanguageObject=d.selectedLanguageObj;
d.selectedLanguage=d.selectedLanguageObj.name;
d.selectedLanguageCode=d.selectedLanguageObj.code
};
var e;
d.setLetter=function(g){e=g;
d.selectedLetter=e;
d.filters={}
};
d.otherCondition=true;
d.alphabetCustomFilter=function(g){if(e!==null){if(e==="all"){return g&&d.otherCondition
}if(e==="pending"){return(g.translationPending===d.otherCondition)&&d.otherCondition
}else{var h=new RegExp(e,"gi");
return g.defaultText.substring(0,1).match(h)&&d.otherCondition
}}else{return g&&d.otherCondition
}};
c.showLabelDialog=function(g){d.selectedLabelEnvironment=g.environment;
d.selectedLabelType=g.type;
d.translationPendingUpdate=g.translationPending;
d.currentLanguageUpdate=d.selectedLanguageCode;
d.keyUpdate=g.key;
d.countryUpdate=g.country
};
d.updateLabel=function(g){d.labelsubmitted=true;
if(d.updatelabelform.$valid){if(d.updatedTranslation===null){d.updatedTranslation=g.translationPending
}var h={key:g.key,country:g.country,language:g.language,defaultText:g.defaultText,text:g.text,translationPending:d.updatedTranslation,environment:g.environment,type:g.type,entity:g.entity};
f.updateLabels(h,function(j){var k="Label updated Succesfully.";
var i=c.success;
c.addMessage(k,i);
d.applyFilter();
d.labelsubmitted=false;
d.updatelabelform.$setPristine()
},function(){d.retrieveLanguages();
var j="Failed to update Label.";
var i=c.failure;
c.addMessage(j,i)
})
}};
d.filters={};
d.applyFilter=function(){d.isDisable=false;
d.filters={};
c.maskLoading();
d.translationPendingLabelList=f.getTranslationPendingLabels(d.selectedLanguageObject,function(g){c.unMaskLoading()
},function(){toastr.error("failure")
});
if(d.selectedLabelEnvironmentForAll!=="0"){d.filters.environment=d.selectedLabelEnvironmentForAll
}if(d.selectedLabelTypeForAll!=="0"){d.filters.type=d.selectedLabelTypeForAll
}if(d.selectedLabelEntityForAll!=="All"){d.filters.entity=d.selectedLabelEntityForAll
}};
d.deleteLabel=function(g){d.labelsubmitted=true;
if(d.updatelabelform.$valid){f.deleteLabel(g,function(i){var j="Label deleted Succesfully.";
var h=c.success;
c.addMessage(j,h);
d.applyFilter()
},function(){d.retrieveLanguages();
var i="Failed to delete Label.";
var h=c.failure;
c.addMessage(i,h)
})
}}
}])
});