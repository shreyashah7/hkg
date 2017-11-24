var chai = require('chai');
var chaiAsPromised = require('chai-as-promised');
var Q = require('q');
chai.use(chaiAsPromised);

var expect = chai.expect;
var Factory = require('./factory');
var configFile = require('./defaults');

module.exports = function() {
    var ptor = protractor.getInstance();

    this.Given(/^I am on login page$/, function(callback) {
        configFile.gotoLogInPage('', callback);
    });

    this.When(/^I enter login credential$/, function(callback) {
        Q(expect(element(protractor.By.id("dropdownMenu1")).isPresent()).to.eventually.equal(true)).then(function(value) {
            ptor.findElement(protractor.By.id("emailaddress")).sendKeys('superadmin');
            ptor.findElement(protractor.By.id("psw")).sendKeys('sadmin@123');
            callback();
        }, function(value) {
            callback.fail(value);
        });
    });

    this.When(/^click 'Sign in' button$/, function(callback) {
        element(protractor.By.xpath("//button[contains(text(),'Sign in')]")).click().then(function() {
            callback();
        });
    });

    this.Then(/^I should navigate to dashboard$/, function(callback) {
        Q(expect(element(protractor.By.xpath("//a[@id='dashboardLink']")).isPresent()).to.eventually.equal(true)).then(function(value) {
            callback();
        }, function(value) {
            callback.fail('It does not navigate to dashboard');
        });
    });

    this.Then(/^It should display error messages$/, function(callback) {
        Q.all([
            expect(element(protractor.By.xpath("//span[contains(text(),'Enter email address')]")).isDisplayed()).to.eventually.equal(true),
            expect(element(protractor.By.xpath("//span[contains(text(),'Enter password')]")).isDisplayed()).to.eventually.equal(true)
        ]).then(function() {
            callback();
        }, function(value) {
            callback.fail('No proper error messages.');
        });
    });

};