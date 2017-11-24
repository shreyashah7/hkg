/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var rootScope;
var scope;
var http;
var q;
function initializeAngularScope(rootScopeArg, httpArg, qArg) {
    rootScope = rootScopeArg;
    http = httpArg;
    q = qArg;
}
function setMessage(text, type, show, showLoginLink, linkText) {
    rootScope.message = {
        text: text,
        type: type,
        show: show,
        showLoginLink: showLoginLink,
        linkText: linkText
    };
}
;

function pingMaster() {

}

function closeLoginModal() {
}

function loginToMaster(data, closeModalRequired) {
    var deferred = q.defer();
    deferred.resolve();
    return deferred.promise;
}

function appendAuthToken(uri) {
    var re = new RegExp("([?&])" + "token" + "=.*?(&|$)", "i");
    var separator = uri.indexOf('?') !== -1 ? "&" : "?";
    if (uri.match(re)) {
        return uri.replace(re, '$1' + "token" + "=" + rootScope.authToken + '$2');
    }
    else {
        return uri + separator + "token" + "=" + rootScope.authToken;
    }
}

function addCustomRequestInterceptor(httpProvider) {
    var customRequestInterceptor = ['$rootScope', '$q', function (rootScope, q) {
            return {
                'request': function (config) {
                    var isRestCall = config.url.indexOf('api') === 0;
                    var isSecureCall = config.url.indexOf('views/secure') === 0;

                    //Check if call is api call or secure resource call
                    if ((isRestCall || isSecureCall) && angular.isDefined(rootScope.authToken)) {
                        //if so, send authorization token from header
                        var authToken = rootScope.authToken;
//                                config.headers['X-Auth-Token'] = authToken;
                        if (exampleAppConfig.useAuthTokenHeader) {
                            config.headers['X-Auth-Token'] = authToken;
                        } else {
                            config.url = config.url + "?token=" + authToken;
                        }
                    }
                    //Send cross domain request for external analytics report
                    httpProvider.defaults.useXDomain = true;
                    httpProvider.defaults.withCredentials = true;
                    delete httpProvider.defaults.headers.common['X-Requested-With'];
                    return config || q.when(config);
                }
            };
        }];

    httpProvider.interceptors.push(customRequestInterceptor);

}

function error401(response, deferred, req) {
    console.log("401");
    rootScope.requests401.push(req);
    rootScope.$broadcast('event:loginRequired');
    console.log('interceptor : error : isLoggedIn : ' + rootScope.isLoggedIn);
    console.log('interceptor : error :  ' + response.data);
    console.log("rootScope.invalidTimezone : " + rootScope.invalidTimezone);
    if (response.data !== "") {
        rootScope.setMessage(response.data, rootScope.failure, true);
    }
    if (rootScope.isLoggedIn === true) {
        console.log('interceptor : error : session expire');
        console.log("interceptor : error : session expire : loginTry : " + rootScope.loginTry);
        rootScope.setMessage("Session is expired", rootScope.failure, true);
        rootScope.$broadcast('event:pollingStop');
    }

    rootScope.isLoggedIn = false;
    if (rootScope.loginTry >= 1) {
        console.log("interceptor : error : invalid credentials : loginTry1 : " + rootScope.loginTry);
        rootScope.loginTry += 1;
        console.log("interceptor : error : invalid credentials : loginTry2 : " + rootScope.loginTry);
//                                setMessage("msginvalidcredentials", "danger", true);
    } else if (rootScope.loginTry === 0) {
        rootScope.loginTry = 1;
    }
    if (rootScope.invalidTimezone === true) {
        console.log("insdie true");
        rootScope.setMessage("Server and system timezone are not in sync.", rootScope.failure, true);
        rootScope.isLoggedIn = false;
    }
//    return deferred.promise;
}
;

function error403(response) {

}
function error404(response, q) {

}

function error500(response, q) {

}

function retrieveCenterFranchise($http, $location, deferred) {
    deferred.reject();
    return deferred.promise;
}
function proxyAuthentication(MasterProxyLogin, id) {
    location.reload();
}

function getProfilePicture() {
    return appendAuthToken(rootScope.apipath + "employee/getprofilepicture/" + rootScope.session.id + '?decache=' + rootScope.randomCount);
}

function pingServerOnLoad() {
    rootScope.pingServer();

}
;