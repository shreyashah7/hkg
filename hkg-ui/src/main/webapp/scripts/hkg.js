//Define screen size
var screensize = "(max-width: 600px)";
var $body = document.body;

$(function () {
    var $body = document.body
            , $menu_trigger = $body.getElementsByClassName('menu-trigger')[0];

    if (typeof $menu_trigger !== 'undefined') {
        $menu_trigger.addEventListener('click', function () {

            var mq = window.matchMedia(screensize);
            var width = $(window).width();
            if (!mq.matches) {

                if ($body.className.match('menu-active')) {
                    $body.className = "";

                    //Change website content width stars
                    $('#content').addClass('col-xs-12');
                    $('#content').removeClass('col-xs-10');
                    $('#content').removeClass('col-xs-offset-2');
                    $('#content').addClass('slide-transition');
                    $('#leftmenu').addClass('left-col-hide');
                    $('#leftmenu').addClass('slide-transition');
                    //Change tablet content width ends

                } else {
                    $body.className = "menu-active";
                    //Change website content width stars
                    $('#content').removeClass('col-xs-12');
                    $('#content').addClass('col-xs-10');
                    $('#content').addClass(' col-xs-offset-2');
                    $('#content').addClass('slide-transition');
                    $('#leftmenu').removeClass('left-col-hide');
                    $('#leftmenu').addClass('slide-transition');
                    //Change tablet content width ends
                }

            } else {

                if ($body.className.match('menu-active')) {
                    $body.className = "";

                    //Change website content width stars
                    $('#content').addClass('col-xs-12');
                    $('#content').removeClass('col-xs-10');
                    $('#content').removeClass('col-xs-offset-2');
                    $('#content').addClass('slide-transition');
                    $('#leftmenu').addClass('left-col-hide');
                    $('#leftmenu').addClass('slide-transition');
                    //Change tablet content width ends

                } else {
                    $body.className = "menu-active";
                    //Change website content width stars
                    $('#content').removeClass('col-xs-12');
                    $('#content').addClass('col-xs-10');
                    $('#content').addClass(' col-xs-offset-2');
                    $('#content').addClass('slide-transition');
                    $('#leftmenu').removeClass('left-col-hide');
                    $('#leftmenu').addClass('slide-transition');
                    //Change tablet content width ends
                }
            }

        });
    }
});
$('#Notifications').popover({
    placement: 'bottom',
    container: 'body',
    html: true,
    //  trigger: 'manual',
    content: function () {
        return $(this).next('.popper-content').html();
    }
});
$('#messages').popover({
    placement: 'bottom',
    container: 'body',
    html: true,
    // trigger: 'manual',
    content: function () {
        return $(this).next('.popper-content').html();
    }
});
$('#readmessages').popover({
    placement: 'bottom',
    container: 'body',
    html: true,
    // trigger: 'manual',
    content: function () {
        return $(this).next('.popper-content').html();
    }
});

////Close popover after focus is changed
$('body').on('click', function (e) {
    $('[rel="popover"]').each(function () {
        //the 'is' for buttons that trigger popups
        //the 'has' for icons within a button that triggers a popup
        if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
            $(this).popover('hide');
        }
    }
    );
});

function resetMenu() {
    if ($('a#manageLink').hasClass("active")) {
        $('a#manageLink').click();
        $('a#manageLink').removeClass('active');
    }
    if ($('a#stockLink').hasClass("active")) {
        $('a#stockLink').click();
        $('a#stockLink').removeClass('active');
    }
    if ($('a#reportLink').hasClass("active")) {
        $('a#reportLink').click();
        $('a#reportLink').removeClass('active');
    }
    $('a#dashboardLink').removeClass('active');
}


$('#dashboard').on('click', function () {
    if ($('a#manageLink').hasClass("active")) {
        $('a#manageLink').click();
        $('a#manageLink').removeClass('active');
    }
    if ($('a#stockLink').hasClass("active")) {
        $('a#stockLink').click();
        $('a#stockLink').removeClass('active');
    }
    if ($('a#reportLink').hasClass("active")) {
        $('a#reportLink').click();
        $('a#reportLink').removeClass('active');
    }
    $('a#dashboardLink').addClass('active');
});

//Expand and Collapse Stock
$('#collapseStock').on('show.bs.collapse', function () {
    $('a span.stock').removeClass('glyphicon-plus').addClass('glyphicon-minus');
    if ($('a#manageLink').hasClass("active")) {
        $('a#manageLink').click();
        $('a#manageLink').removeClass('active');
    }
    if ($('a#reportLink').hasClass("active")) {
        $('a#reportLink').click();
        $('a#reportLink').removeClass('active');
    }
    if ($('a#dashboardLink').hasClass("active")) {
        $('a#dashboardLink').removeClass('active');
    }
    $('a#stockLink').addClass('active');
});
$('#collapseStock').on('hide.bs.collapse', function () {
    $('a span.stock').removeClass('glyphicon-minus').addClass('glyphicon-plus');
    $('a#stockLink').removeClass('active');
    if ($('a#manageLink').hasClass("active")) {
        $('a#manageLink').click();
        $('a#manageLink').removeClass('active');
    }
    if ($('a#reportLink').hasClass("active")) {
        $('a#reportLink').click();
        $('a#reportLink').removeClass('active');
    }
    if ($('a#dashboardLink').hasClass("active")) {
        $('a#dashboardLink').removeClass('active');
    }
});

//Expand and Collapse Manage
$('#collapseManage').on('show.bs.collapse', function () {
    $('a span.manage').removeClass('glyphicon-plus').addClass('glyphicon-minus');
    if ($('a#stockLink').hasClass("active")) {
        $('a#stockLink').click();
        $('a#stockLink').removeClass('active');
    }
    if ($('a#reportLink').hasClass("active")) {
        $('a#reportLink').click();
        $('a#reportLink').removeClass('active');
    }
    if ($('a#dashboardLink').hasClass("active")) {
        $('a#dashboardLink').removeClass('active');
    }
    $('a#manageLink').addClass('active');
});
$('#collapseManage').on('hide.bs.collapse', function () {
    $('a span.manage').removeClass('glyphicon-minus').addClass('glyphicon-plus');
    $('a#manageLink').removeClass('active');
    if ($('a#stockLink').hasClass("active")) {
        $('a#stockLink').click();
        $('a#stockLink').removeClass('active');
    }
    if ($('a#reportLink').hasClass("active")) {
        $('a#reportLink').click();
        $('a#reportLink').removeClass('active');
    }
    if ($('a#dashboardLink').hasClass("active")) {
        $('a#dashboardLink').removeClass('active');
    }
});

$('#collapseReport').on('show.bs.collapse', function () {
    $('a span.report').removeClass('glyphicon-plus').addClass('glyphicon-minus');    
    if ($('a#stockLink').hasClass("active")) {
        $('a#stockLink').click();
        $('a#stockLink').removeClass('active');
    }
    if ($('a#manageLink').hasClass("active")) {
        $('a#manageLink').click();
        $('a#manageLink').removeClass('active');
    }
    if ($('a#dashboardLink').hasClass("active")) {
        $('a#dashboardLink').removeClass('active');
    }
    $('a#reportLink').addClass('active');
});
$('#collapseReport').on('hide.bs.collapse', function () {
    $('a span.report').removeClass('glyphicon-minus').addClass('glyphicon-plus');
    $('a#reportLink').removeClass('active');
    if ($('a#stockLink').hasClass("active")) {
        $('a#stockLink').click();
        $('a#stockLink').removeClass('active');
    }
    if ($('a#manageLink').hasClass("active")) {
        $('a#manageLink').click();
        $('a#manageLink').removeClass('active');
    }
    if ($('a#dashboardLink').hasClass("active")) {
        $('a#dashboardLink').removeClass('active');
    }
});


/* JavaScript Media Queries */
if (matchMedia) {
    var mq = window.matchMedia(screensize);
    mq.addListener(WidthChange);
    WidthChange(mq);
}

// media query change
function WidthChange(mq) {

    var msg = (mq.matches ? "more" : "less") + " than 500 pixels";

    if (!mq.matches) {

//        alert("website");
//Website view
        $body.className = "menu-active";

        $("#leftmenutablet").hide();
        $("#leftmenu").show();

        $("#tablogo").hide();
        $("#weblogo").show();

        $('#topheader').removeClass('col-xs-11');
        $('#topheader').addClass('col-xs-10');

        //Change tablet content width stars
        $('#content').removeClass('col-xs-11');
        $('#content').removeClass('col-xs-offset-1');
        $('#content').addClass('col-xs-10');
        $('#content').addClass('col-xs-offset-2');

        $('#leftmenu').removeClass('left-col-hide');
        $('#leftmenu').removeClass('slide-transition');

    } else {
        $body.className = "menu-active";

        $("#leftmenutablet").hide();
        $("#leftmenu").show();

        $("#tablogo").hide();
        $("#weblogo").show();

        $('#topheader').removeClass('col-xs-11');
        $('#topheader').addClass('col-xs-10');

        //Change tablet content width stars
        $('#content').removeClass('col-xs-11');
        $('#content').removeClass('col-xs-offset-1');
        $('#content').addClass('col-xs-10');
        $('#content').addClass('col-xs-offset-2');

        $('#leftmenu').removeClass('left-col-hide');
        $('#leftmenu').removeClass('slide-transition');

//Tablet view
//        $body.className = "menu-active";
//        // code for tablet view
//        $("#leftmenu").hide();
//        $("#leftmenutablet").show();
//
//        $("#weblogo").hide();
//        $("#tablogo").show();
//
//        $('#topheader').removeClass('col-xs-10');
//        $('#topheader').addClass('col-xs-11');
//        //Change tablet content width stars
//        $('#content').addClass('col-xs-11');
//        $('#content').addClass('col-xs-offset-1');
//        $('#content').removeClass('col-xs-10');
//        $('#content').removeClass('col-xs-offset-2');
//
//        $('#leftmenutablet').removeClass('left-col-hide');
//        $('#leftmenutablet').removeClass('slide-transition');
    }
//    document.getElementById("current").firstChild.nodeValue = msg;

}