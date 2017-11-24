var defaults = function() {
    var chai = require('chai');
    var chaiAsPromised = require('chai-as-promised');
    var Q = require('q');
    chai.use(chaiAsPromised);

    var expect = chai.expect;
    var ptor = protractor.getInstance();

    this.getBaseUrl = function() {
        return 'http://localhost:9090/hkgmaster/#/';
    };

    this.gotoLogInPage = function(path, callback) {
        var count =0 ;
        var checkLoginPage = function(url) {
            if(count>100){
                callback.fail('It has waited a long for login page..');
            }
            if (/login/.test(url)) {
//                console.log(count);
                callback();
            }
            count++;
            return false;
        };
        browser.driver.manage().window().maximize();
        var url = this.getBaseUrl();
        browser.driver.get(url + path);
        browser.driver.wait(function() {
            return browser.driver.getCurrentUrl().then(function(url) {
                Q(expect(element(protractor.By.id("dropdownMenu1")).isDisplayed()).to.eventually.equal(true)).then(function(value) {
                    element(protractor.By.id("dropdownMenu1")).click().then(function() {
                        ptor.findElement(protractor.By.xpath("//ul[@id='user-settings']/li[2]")).click().then(function() {
                            checkLoginPage(url);
                        });
                    });
                }, function(value) {
                    checkLoginPage(url);
                });

            });
        });

        
    };

    this.checkPageTitle = function(pageTitle, callback) {
        expect(ptor.getTitle()).to.eventually.equal(pageTitle).then(function(value) {
            callback();
        }, function(error) {
            callback.fail(error);
        });
    };

    this.scrollInToElement = function(element) {
        var scrollIntoView = function() {
            arguments[0].scrollIntoView();
        }
        browser.executeScript(scrollIntoView, element);
    };

};

module.exports = new defaults();