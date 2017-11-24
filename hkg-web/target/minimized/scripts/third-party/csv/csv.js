(function(){var j={};
j.RELAXED=false;
j.IGNORE_RECORD_LENGTH=false;
j.IGNORE_QUOTES=false;
j.LINE_FEED_OK=true;
j.CARRIAGE_RETURN_OK=true;
j.DETECT_TYPES=true;
j.IGNORE_QUOTE_WHITESPACE=true;
j.DEBUG=false;
j.COLUMN_SEPARATOR=",";
j.ERROR_EOF="UNEXPECTED_END_OF_FILE";
j.ERROR_CHAR="UNEXPECTED_CHARACTER";
j.ERROR_EOL="UNEXPECTED_END_OF_RECORD";
j.WARN_SPACE="UNEXPECTED_WHITESPACE";
var g='"',a="\r",d="\n",f=" ",h="\t";
var e=0,b=1,i=2,c=4;
j.parse=function(l){var k=j.result=[];
j.offset=0;
j.str=l;
j.record_begin();
j.debug("parse()",l);
var m;
while(1){m=l[j.offset++];
j.debug("c",m);
if(m==null){if(j.escaped){j.error(j.ERROR_EOF)
}if(j.record){j.token_end();
j.record_end()
}j.debug("...bail",m,j.state,j.record);
j.reset();
break
}if(j.record==null){if(j.RELAXED&&(m==d||m==a&&l[j.offset+1]==d)){continue
}j.record_begin()
}if(j.state==e){if((m===f||m===h)&&j.next_nonspace()==g){if(j.RELAXED||j.IGNORE_QUOTE_WHITESPACE){continue
}else{j.warn(j.WARN_SPACE)
}}if(m==g&&!j.IGNORE_QUOTES){j.debug("...escaped start",m);
j.escaped=true;
j.state=b;
continue
}j.state=b
}if(j.state==b&&j.escaped){if(m==g){if(l[j.offset]==g){j.debug("...escaped quote",m);
j.token+=g;
j.offset++
}else{j.debug("...escaped end",m);
j.escaped=false;
j.state=i
}}else{j.token+=m;
j.debug("...escaped add",m,j.token)
}continue
}if(m==a){if(l[j.offset]==d){j.offset++
}else{if(!j.CARRIAGE_RETURN_OK){j.error(j.ERROR_CHAR)
}}j.token_end();
j.record_end()
}else{if(m==d){if(!(j.LINE_FEED_OK||j.RELAXED)){j.error(j.ERROR_CHAR)
}j.token_end();
j.record_end()
}else{if(m==j.COLUMN_SEPARATOR){j.token_end()
}else{if(j.state==b){j.token+=m;
j.debug("...add",m,j.token)
}else{if(m===f||m===h){if(!j.IGNORE_QUOTE_WHITESPACE){j.error(j.WARN_SPACE)
}}else{if(!j.RELAXED){j.error(j.ERROR_CHAR)
}}}}}}}return k
};
j.json=function(){var k=new require("stream").Transform({objectMode:true});
k._transform=function(m,n,l){k.push(JSON.stringify(m.toString())+require("os").EOL);
l()
};
return k
};
j.stream=function(){var k=new require("stream").Transform({objectMode:true});
k.EOL="\n";
k.prior="";
k.emitter=function(l){return function(m){l.push(j.parse(m+l.EOL))
}
}(k);
k._transform=function(n,o,m){var l=(this.prior=="")?n.toString().split(this.EOL):(this.prior+n.toString()).split(this.EOL);
this.prior=l.pop();
l.forEach(this.emitter);
m()
};
k._flush=function(l){if(this.prior!=""){this.emitter(this.prior);
this.prior=""
}l()
};
return k
};
j.reset=function(){j.state=null;
j.token=null;
j.escaped=null;
j.record=null;
j.offset=null;
j.result=null;
j.str=null
};
j.next_nonspace=function(){var k=j.offset;
var l;
while(k<j.str.length){l=j.str[k++];
if(!(l==f||l===h)){return l
}}return null
};
j.record_begin=function(){j.escaped=false;
j.record=[];
j.token_begin();
j.debug("record_begin")
};
j.record_end=function(){j.state=c;
if(!(j.IGNORE_RECORD_LENGTH||j.RELAXED)&&j.result.length>0&&j.record.length!=j.result[0].length){j.error(j.ERROR_EOL)
}j.result.push(j.record);
j.debug("record end",j.record);
j.record=null
};
j.resolve_type=function(k){if(k.match(/^\d+(\.\d+)?$/)){k=parseFloat(k)
}else{if(k.match(/^(true|false)$/i)){k=Boolean(k.match(/true/i))
}else{if(k==="undefined"){k=undefined
}else{if(k==="null"){k=null
}}}}return k
};
j.token_begin=function(){j.state=e;
j.token=""
};
j.token_end=function(){if(j.DETECT_TYPES){j.token=j.resolve_type(j.token)
}j.record.push(j.token);
j.debug("token end",j.token);
j.token_begin()
};
j.debug=function(){if(j.DEBUG){console.log(arguments)
}};
j.dump=function(k){return[k,"at char",j.offset,":",j.str.substr(j.offset-50,50).replace(/\r/mg,"\\r").replace(/\n/mg,"\\n").replace(/\t/mg,"\\t")].join(" ")
};
j.error=function(k){var l=j.dump(k);
j.reset();
throw l
};
j.warn=function(k){var m=j.dump(k);
try{console.warn(m);
return
}catch(l){}try{console.log(m)
}catch(l){}};
(function(k,m,l){var n;
if(typeof module!="undefined"&&module.exports){module.exports=l()
}else{if(typeof n=="function"&&typeof n.amd=="object"){n(l)
}else{m[k]=l()
}}}("CSV",Function("return this")(),function(){return j
}))
})();