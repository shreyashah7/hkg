define(["hkg","priceListService"],function(a){a.register.controller("PriceListController",["$rootScope","$scope","PriceListService",function(b,c,d){b.maskLoading();
b.mainMenu="manageLink";
b.childMenu="managePriceList";
b.activateMenu();
c.entity="PRICELIST.";
c.successUpload=false;
c.uploadFile={target:b.appendAuthToken(b.apipath+"pricelist/uploadpricelist"),singleFile:true,testChunks:true,query:{fileType:c.seletedFileType,model:"PriceList"}};
c.initData=function(){c.uploadPricelist=true;
d.retrieveallPriceList(function(e){c.allPriceList=e
},function(){})
};
c.priceListFileUploaded=function(f,e,i,h){c.responseData=JSON.parse(h);
if(!!c.responseData){if(c.responseData.messages[0].responseCode===1){var j=c.responseData.messages[0].message;
var g=b.failure;
b.addMessage(j,g);
c.successUpload=false;
e.files=[]
}if(c.responseData.messages[0].responseCode===0){c.fileData=c.responseData.data;
c.successUpload=true
}}};
c.priceListFileAdded=function(f,e){c.priceListUploaded=false;
if((f.getExtension()!=="xls")&&(f.getExtension()!=="xlsx")){var h="Not a valid file. Upload only xls or xlsx file";
var g=b.failure;
b.addMessage(h,g)
}else{c.seletedFileType=f.getExtension();
if(f.size>5242880){var h="File size too large. Upload file with size less than 5MB.";
var g=b.failure;
b.addMessage(h,g);
return false
}}return !!{xls:1,xlsx:1}[f.getExtension()]
};
c.savePriceList=function(e){b.maskLoading();
d.savePriceList(c.fileData,function(){b.unMaskLoading();
e.files=[];
delete c.fileData;
c.successUpload=false;
c.initData();
var g="Price list saved successfully";
var f=b.success;
b.addMessage(g,f)
},function(){b.unMaskLoading();
var g="Problem while saving price list.";
var f=b.failure;
b.addMessage(g,f)
})
};
c.cancelPriceList=function(e){e.files=[];
delete c.fileData;
c.successUpload=false
};
c.selectPriceList=function(e){if(!!e.currentNode.id){delete e.currentNode.selected;
b.maskLoading();
d.retrievepricelistByMonthYear(e.currentNode,function(f){b.unMaskLoading();
c.uploadPricelist=false;
c.listData=f
},function(){b.unMaskLoading();
var g="Failed to retrieve price list.";
var f=b.failure;
b.addMessage(g,f)
})
}};
c.addClick=function(e){e.files=[];
delete c.fileData;
c.successUpload=false;
c.uploadPricelist=true
};
b.unMaskLoading()
}])
});