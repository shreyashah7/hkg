var factory = function() {
    var configFile = require('./defaults');
    var Factory = require('rosie').Factory;
    Factory.define('project')
            .attr('id', function() {
                return null;
            })
            .attr('title', function(i) {
                return 'Test project title';
            })
            .attr('po', function(i) {
                return 'Test po';
            })
            .attr('po_date', function() {
                return new Date();
            })
            .attr('po_value', function() {
                return Math.floor((Math.random() * 100) + 1);
            })
            .attr('project_value', function() {
                return Math.floor((Math.random() * 100) + 1);
            })
            .attr('company', function(i) {
                return configFile.companyList[1];
            })
            .attr('sales_person', function(i) {
                return 'Test sales person';
            })
            .attr('comment', function(i) {
                return 'Test comment';
            })
            .attr('status', function(i) {
                return 'Test status';
            })
            .attr('sub_business_unit', function(i) {
                return 'Test comment';
            })
            .attr('quote_reference', function(i) {
                return 'Test comment';
            });

    Factory.define('product_group')
            .attr('id', function() {
                return null;
            })
            .attr('project_id')
            .attr('product_id')
            .attr('description', function() {
                return 'Test description ';
            })
            .attr('qty', function() {
                return Math.floor((Math.random() * 10) + 1);
            })
            .attr('unit_value', function() {
                return Math.floor((Math.random() * 100) + 1);
            })
            .attr('value');

    Factory.define('activity')
            .attr('id')
            .attr('product_id')
            .attr('product_group_id')
            .attr('job_id')
            .attr('activity_name')
            .attr('company', function(i) {
                return configFile.companyList[1];
            })
            .attr('business_unit')
            .attr('sub_bu')
            .attr('allocated_to')
            .attr('status', function() {
                return 'Unscheduled';
            })
            .attr('created_date', function() {
                return new Date();
            })
            .attr('scheduled_date', function() {
                return new Date();
            })
            .attr('completed_date', function() {
                return new Date();
            })
            .attr('site_id');

    Factory.define('available_product')
            .attr('id')
            .attr('product_name');

    Factory.define('default_activities')
            .attr('id')
            .attr('activity_name')
            .attr('available_product_id');

    Factory.define('job_seq')
            .attr('id');

    Factory.define('site')
            .attr('id')
            .attr('site_name');

    return Factory;
}



module.exports = new factory();