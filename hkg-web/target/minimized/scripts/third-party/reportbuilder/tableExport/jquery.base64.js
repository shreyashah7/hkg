jQuery.base64=(function(f){var d="=",b="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/",c="1.0";
function g(l,k){var j=b.indexOf(l.charAt(k));
if(j===-1){throw"Cannot decode base64"
}return j
}function a(m){var o=0,l,n,k=m.length,j=[];
m=String(m);
if(k===0){return m
}if(k%4!==0){throw"Cannot decode base64"
}if(m.charAt(k-1)===d){o=1;
if(m.charAt(k-2)===d){o=2
}k-=4
}for(l=0;
l<k;
l+=4){n=(g(m,l)<<18)|(g(m,l+1)<<12)|(g(m,l+2)<<6)|g(m,l+3);
j.push(String.fromCharCode(n>>16,(n>>8)&255,n&255))
}switch(o){case 1:n=(g(m,l)<<18)|(g(m,l+1)<<12)|(g(m,l+2)<<6);
j.push(String.fromCharCode(n>>16,(n>>8)&255));
break;
case 2:n=(g(m,l)<<18)|(g(m,l+1)<<12);
j.push(String.fromCharCode(n>>16));
break
}return j.join("")
}function e(l,k){var j=l.charCodeAt(k);
if(j>255){throw"INVALID_CHARACTER_ERR: DOM Exception 5"
}return j
}function h(m){if(arguments.length!==1){throw"SyntaxError: exactly one argument required"
}m=String(m);
var l,n,j=[],k=m.length-m.length%3;
if(m.length===0){return m
}for(l=0;
l<k;
l+=3){n=(e(m,l)<<16)|(e(m,l+1)<<8)|e(m,l+2);
j.push(b.charAt(n>>18));
j.push(b.charAt((n>>12)&63));
j.push(b.charAt((n>>6)&63));
j.push(b.charAt(n&63))
}switch(m.length-k){case 1:n=e(m,l)<<16;
j.push(b.charAt(n>>18)+b.charAt((n>>12)&63)+d+d);
break;
case 2:n=(e(m,l)<<16)|(e(m,l+1)<<8);
j.push(b.charAt(n>>18)+b.charAt((n>>12)&63)+b.charAt((n>>6)&63)+d);
break
}return j.join("")
}return{decode:a,encode:h,VERSION:c}
}(jQuery));