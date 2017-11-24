var removeClassSVG=function(d,a){var c=d.attr("class");
if(!c){return false
}var b=c.search(a);
if(b==-1){return false
}else{c=c.substring(0,b)+c.substring((b+a.length),c.length);
d.attr("class",c);
return true
}};
var hasClassSVG=function(d,b){var c=d.attr("class");
if(!c){return false
}var a=c.search(b);
if(a==-1){return false
}else{return true
}};