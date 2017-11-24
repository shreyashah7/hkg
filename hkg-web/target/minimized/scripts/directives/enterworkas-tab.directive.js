define(["angular"],function(){angular.module("hkg.directives").directive("pdkNextInputOnEnter",[function(){function a(d,c,b){$("body").on("shown.bs.modal",".modal",function(){$("input,select,button,[multi-select],[ui-select2],[data-toggle],textarea",this).filter(":visible:first").focus()
});
c.on("keydown",function(h){if(h.keyCode==13){if(h.shiftKey){return true
}var i=c[0].querySelectorAll("input,select,button,[multi-select],[ui-select2],[data-toggle],textarea");
var g=Array.prototype.indexOf.call(i,h.target);
var f=g==i.length-1?0:g+1;
if(f>=0&&f<i.length){if(i[g]!==null&&i[g]!==undefined&&(i[g].localName!=="button"||(i[g].localName==="button")&&(i[g].className.indexOf("btn-hkg")===-1))){while((i[f].offsetWidth===0&&i[f].offsetHeight===0)||(i[f].id!==undefined&&i[f].id!==null&&c.offsetWidth===0&&c.offsetHeight===0)||(!i[f].id&&$(i[f].localName).is(":visible")===false)||(i[f].style.visibility==="hidden")||((i[f].localName==="button")&&(i[f].className.indexOf("btn-hkg")===-1)&&(i[f].className.indexOf("dropdown-toggle")===-1&&i[f].className.indexOf("multiSelectButton")===-1))||((i[f].localName==="input")&&(i[f].className.indexOf("inputFilter")>-1))||((i[f].localName==="input")&&(i[f].className.indexOf("checkbox")>-1))||(i[f].tabIndex===-1)){f++
}if(i[g].className.indexOf("dropdown-toggle")!==-1){$('[data-toggle="dropdown"]').parent().removeClass("open")
}i[f].focus();
return false
}else{return true
}}}})
}return{restrict:"A",link:a}
}])
});