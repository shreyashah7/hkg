/**
 * Copyright (c) 2009 Sergiy Kovalchuk (serg472@gmail.com)
 * 
 * Dual licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
 * and GPL (http://www.opensource.org/licenses/gpl-license.php) licenses.
 *  
 * Following code is based on Element.mask() implementation from ExtJS framework (http://extjs.com/)
 *
 *Modified By Akta Kalariya
 */
;
(function($) {

    /**
     * Displays loading mask over selected element(s). Accepts both single and multiple selectors.
     *
     * @param label Text message that will be displayed on top of the mask besides a spinner (optional). 
     * 				If not provided only mask will be displayed without a label or a spinner.  	
     * @param delay Delay in milliseconds before element is masked (optional). If unmask() is called 
     *              before the delay times out, no mask is displayed. This can be used to prevent unnecessary 
     *              mask display for quick processes.   	
     */
    $.fn.mask = function(label, delay, template, templateUrl, imagePath) {
        $(this).each(function() {
            if (delay !== undefined && delay > 0) {
                var element = $(this);
                element.data("_mask_timeout", setTimeout(function() {
                    $.maskElement(element, label, template, templateUrl, imagePath);
                }, delay));
            } else {
                $.maskElement($(this), label, template, templateUrl, imagePath);
            }
        });
    };

    /**
     * Removes mask from the element(s). Accepts both single and multiple selectors.
     */
    $.fn.unmask = function() {
        $(this).each(function() {
            $.unmaskElement($(this));
        });
    };

    /**
     * Checks if a single element is masked. Returns false if mask is delayed or not displayed. 
     */
    $.fn.isMasked = function() {
        return this.hasClass("masked");
    };

    $.maskElement = function(element, label, template, templateUrl, imagePath) {

        //if this element has delayed mask scheduled then remove it and display the new one
        if (element.data("_mask_timeout") !== undefined) {
            clearTimeout(element.data("_mask_timeout"));
            element.removeData("_mask_timeout");
        }

        if (element.isMasked()) {
            $.unmaskElement(element);
        }

        if (element.css("position") == "static") {
            element.addClass("masked-relative");
        }

        element.addClass("masked");

        var maskDiv = $('<div class="loadmask"></div>');

        //auto height fix for IE
        if (navigator.userAgent.toLowerCase().indexOf("msie") > -1) {
            maskDiv.height(element.height() + parseInt(element.css("padding-top")) + parseInt(element.css("padding-bottom")));
            maskDiv.width(element.width() + parseInt(element.css("padding-left")) + parseInt(element.css("padding-right")));
        }

        //fix for z-index bug with selects in IE6
        if (navigator.userAgent.toLowerCase().indexOf("msie 6") > -1) {
            element.find("select").addClass("masked-hidden");
        }

        element.append(maskDiv);

//        if (label !== undefined) {
//            var maskMsgDiv = $('<div class="loadmask-msg" style="display:none;"></div>');
        var maskMsgDiv;
        if (template) {
            maskMsgDiv = $('<div class="loadmask-msg"><img src="data:image/jpeg;base64,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"></div>');
            $.appendMaskDiv(maskMsgDiv, element);
        }
        else if (templateUrl) {
            $.ajax({
                url: templateUrl,
                success: function(data) {
                    maskMsgDiv = $('<div class="loadmask-msg">' + data + '</div>');
                    $.appendMaskDiv(maskMsgDiv, element);
                }
            });
        }
        else if (label && imagePath) {
            maskMsgDiv = $('<div class="loadmask-msg"><div align="center">' + label + '</div>' + '<div><img src="' + imagePath + '"></div></div>');
            $.appendMaskDiv(maskMsgDiv, element);
        }
        else if (imagePath) {
            maskMsgDiv = $('<div class="loadmask-msg"><img src="' + imagePath + '"/></div>');
            $.appendMaskDiv(maskMsgDiv, element);
        }
        else if (label) {
            maskMsgDiv = $('<div class="loadmask-msg">' + label + '</div>');
            $.appendMaskDiv(maskMsgDiv, element);
        }
        else {
            maskMsgDiv = $('<div class="loadmask-msg loadmask-msg-default" style="display:none;"></div>');
             maskMsgDiv.append('<div></div>');
            $.appendMaskDiv(maskMsgDiv, element);
        }

    };
    $.appendMaskDiv = function(maskMsgDiv, element) {
        element.append(maskMsgDiv);

//        calculate center position
//        console.log("element.height : " + element.height());
//        console.log("maskMsgDiv.height() : " + maskMsgDiv.height());
//        console.log('maskMsgDiv.css("padding-top") : ' + maskMsgDiv.css("padding-top"));
//        console.log('maskMsgDiv.css("padding-bottom") :' + maskMsgDiv.css("padding-bottom"));

        maskMsgDiv.css("top", Math.round(element.height() / 2 - (maskMsgDiv.height() - parseInt(maskMsgDiv.css("padding-top")) - parseInt(maskMsgDiv.css("padding-bottom"))) / 2) + "px");
        maskMsgDiv.css("left", Math.round(element.width() / 2 - (maskMsgDiv.width() - parseInt(maskMsgDiv.css("padding-left")) - parseInt(maskMsgDiv.css("padding-right"))) / 2) + "px");

        maskMsgDiv.show();
    }

    $.unmaskElement = function(element) {
        //if this element has delayed mask scheduled then remove it
        if (element.data("_mask_timeout") !== undefined) {
            clearTimeout(element.data("_mask_timeout"));
            element.removeData("_mask_timeout");
        }

        element.find(".loadmask-msg,.loadmask,loadmask-msg-default").remove();
        element.removeClass("masked");
        element.removeClass("masked-relative");
        element.find("select").removeClass("masked-hidden");
    };

})(jQuery);