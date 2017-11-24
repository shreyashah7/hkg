/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var rootScope;
var scope;
var http;
var q;
var route;
function initializeAngularScope(rootScopeArg, httpArg, qArg, routeArg) {
    rootScope = rootScopeArg;
    http = httpArg;
    q = qArg;
    route = routeArg;
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
    var deferred = q.defer();
    rootScope.ping = false;
    rootScope.clientTimezoneOffset = new Date().getTimezoneOffset();
    http.post(rootScope.masterPingUrl, rootScope.clientTimezoneOffset).success(function (data)
    { //masterPingUrl variable should be set before pingServer first time called in $rootScope
        var currentDesignation = null;
        if(rootScope.session !== undefined && rootScope.session !== null 
                && rootScope.session.currentDesignation !== undefined && rootScope.session.currentDesignation !== null){
            currentDesignation = rootScope.session.currentDesignation;
        }
        rootScope.session = data;
        rootScope.session.currentDesignation = currentDesignation;
        //Allow ng-options to preselect the model as in key,value pair, js takes key as string
        if (rootScope.session.currentDesignation !== null) {
            rootScope.session.currentDesignation = rootScope.session.currentDesignation.toString();
        }
//                    rootScope.menu = undefined;
//                    if (data!==undefined&&data.features!==undefined) {
//                        rootScope.menu = data.features;
//                    }
        rootScope.switchLanguage(rootScope.session.prefferedLang, rootScope.session.companyId);
        rootScope.viewEncryptedData = rootScope.canAccess("view_encrypted_data");
        rootScope.getCurrentServerDate();
        rootScope.$broadcast('event:loginConfirmed', rootScope.masterPingUrl);
        deferred.resolve();
        return true;
    }).error(function () {
        deferred.reject();
        return false;
    });
    return deferred.promise;
}

function closeLoginModal() {
    $('#loginModal').modal('hide');
    rootScope.removeModalOpenCssAfterModalHide();
}

function loginToMaster(data, closeModalRequired, MasterProxyLogin, useProxy) {
    var deferred = q.defer();
    var payload = {
        userName: data.username,
        password: rootScope.encryptPass(data.password, false),
    };
    http.post(rootScope.masterHkgPath + 'j_spring_security_logout', {})
            .finally(function () {
                http.post(rootScope.apipath + 'common/authenticate', payload).success(function (data) {
                    rootScope.masterAuthToken = data.token;
                    localStorage.setItem('masterAuthToken', data.token);
                    if (rootScope.session && rootScope.session.isProxyLogin) {
                        var promisePingMaster = pingMaster();
                        promisePingMaster.then(function () {
                            var promiseProxyAuth = proxyAuthentication(MasterProxyLogin, useProxy);
                            promiseProxyAuth.then(function () {
                                if (closeModalRequired) {
                                    rootScope.closeLoginModal();
                                }
                            });
                        });
                        localStorage.setItem("proxyLogin", useProxy);
                    }
                    else {
                        var promisePingMaster1 = pingMaster();
                        promisePingMaster1.then(function () {
                            rootScope.isMasterDown = false;
                            if (closeModalRequired) {
                                rootScope.closeLoginModal();
                            }
                        });
                    }
                }).error(function (data, status, headers, config) {
                }).finally(function () {
                    deferred.resolve();
                });

            });
    return deferred.promise;
}

function appendAuthToken(uri) {
    var re = new RegExp("([?&])" + "token" + "=.*?(&|$)", "i");
    var separator = uri.indexOf('?') !== -1 ? "&" : "?";
    var authtoken = rootScope.authToken;
    if (uri.indexOf(rootScope.apipath) !== -1) {
        authtoken = rootScope.masterAuthToken;
    }
    if (uri.match(re)) {
        return uri.replace(re, '$1' + "token" + "=" + authtoken + '$2');
    }
    else {
        return uri + separator + "token" + "=" + authtoken;
    }
}

function addCustomRequestInterceptor(httpProvider) {
    var customRequestInterceptor = ['$rootScope', '$q', function (rootScope, q) {
            return {
                'request': function (config) {
//                            console.log("In INTERCEPTOR");
                    var isRestCall = config.url.indexOf('api') !== -1;
                    var isSecureCall = config.url.indexOf('views/secure') !== -1;
//                            console.log("1");
                    //Check if call is api call or secure resource call
                    if ((isRestCall || isSecureCall) && (angular.isDefined(rootScope.authToken) || angular.isDefined(rootScope.masterAuthToken))) {
//                                console.log("2");
                        //if so, send authorization token from header
                        var authToken = rootScope.authToken;
//                                var authtoken = rootScope.authToken;
//                                console.log("config.url=" + config.url);
                        if (config.url.indexOf(rootScope.apipath) !== -1) {

                            authToken = rootScope.masterAuthToken;
                        }
//                                config.headers['X-Auth-Token'] = authToken;
                        if (exampleAppConfig.useAuthTokenHeader) {
                            config.headers['X-Auth-Token'] = authToken;
                        } else {
                            config.url = config.url + "?token=" + authToken;
                        }
                        httpProvider.defaults.useXDomain = true;
                        httpProvider.defaults.withCredentials = true;
                        delete httpProvider.defaults.headers.common['X-Requested-With'];
//                        console.log(config.url + " config.header" + JSON.stringify(config.headers));
                    }
                    return config || q.when(config);
                }
            };
        }];
    httpProvider.interceptors.push(customRequestInterceptor);
}

function error401(response, deferred, req) {
    if ((response.config.url.indexOf(rootScope.apipath) !== -1)) {
        var config = response.config;
        var data = localStorage.getItem('tmpUser');
        data = JSON.parse(data);
        if (data !== null) {
            rootScope.login.username = data.username;
            rootScope.addMessage("Connection restored. ", rootScope.warning, true, "Login again");
            rootScope.setMessage("Connection restored.", rootScope.warning, true, true, "Login again");
//                                        if (rootScope.loginToMaster(data)) {
//                                            http(config);
//                                        }
        }
    } else {
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
            console.log("insdie true")
            rootScope.setMessage("Server and system timezone are not in sync.", rootScope.failure, true);
            rootScope.isLoggedIn = false;
        }
    }
//    return deferred.promise;
}
;
function error403(response) {
    if ((response.config.url.indexOf(rootScope.apipath + 'notification/retrievecount') === 0) && rootScope.isMasterDown) {
//                                rootScope.isMasterDown=false;

        var data = localStorage.getItem('tmpUser');
        data = JSON.parse(data);
        if (data !== null) {
            rootScope.login.username = data.username;
            rootScope.addMessage("Connection restored.", rootScope.warning, true, "Login again");
            rootScope.setMessage("Connection restored.", rootScope.warning, true, true, "Login again");
//                                    if (rootScope.loginToMaster(data)) {
//                                        http(config);
//                                    }
        }
    }
}
function error404(response, q) {
    var url = response.config.url;
//    alert(rootScope.apipath)
    if ((response.config.url.indexOf('common/getsession') !== -1)) {
        rootScope.isMasterDown = true;
        localStorage.removeItem('masterAuthToken');
        rootScope.addMessage("Connection lost", rootScope.failure);
        rootScope.setMessage("Connection lost", rootScope.failure, true);

    } else if ((url.indexOf(rootScope.apipath) !== -1) && !rootScope.isMasterDown) {
        pingMaster();
    }
//    else {
//        return q.reject(response);
//    }
}

function error500(response, q) {
    var url = response.config.url;
    if ((url.indexOf('common/getsession') !== -1)) {
        localStorage.removeItem('masterAuthToken');
        rootScope.isMasterDown = true;
        rootScope.addMessage("Connection lost", rootScope.failure);
        rootScope.setMessage("Connection lost", rootScope.failure, true);

    } else if ((url.indexOf(rootScope.apipath) !== -1) && (url.indexOf("common/getsession") === -1) && !rootScope.isMasterDown) {
        pingMaster();
    }
//    else {
//        return q.reject(response);
//    }
}

function retrieveCenterFranchise($http, $location, deferred) {

    if (rootScope.isFirstReq) {
        $http.get("api/centerfranchise/retrieve").success(function (res) {
//            alert(res.id + " here " + (res.id == undefined));
            if (res.id == undefined) {
                deferred.resolve();
            } else {
                deferred.reject();
            }
        });
    }
    return deferred.promise;
}
function proxyAuthentication(MasterProxyLogin, id) {
    var deferred = q.defer();
    console.log("proxy auth on master" + id)
    MasterProxyLogin.authenticateProxy({id: id}, function (data) {
        console.log("Proxy login successful on master..");
        rootScope.masterAuthToken = data.token;
        localStorage.setItem('masterAuthToken', data.token);
        var promise = pingMaster();
        promise.then(function () {
            route.reload();
            deferred.resolve();
        });
    }, function () {
        console.log("failure on master..");
        deferred.reject();
    });
    return deferred.promise;
}

function getProfilePicture() {
    return appendAuthToken(rootScope.centerapipath + "employee/getprofilepicture/" + rootScope.session.id + '?decache=' + rootScope.randomCount);
}

function pingServerOnLoad() {
    var promise = rootScope.pingServer();
    promise.then(function () {
        rootScope.pingMasterServer();
    });

}
;