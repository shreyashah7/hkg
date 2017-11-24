exports.config = {
    //https://code.google.com/p/selenium/wiki/DesiredCapabilities,
    framework: 'cucumber',
    // The address of a running selenium server.
//    seleniumAddress: 'http://localhost:4444/wd/hub',
    //Protractor will automatically start a standalone server with the use of jar
    chromeDriver: '/usr/local/lib/node_modules/protractor/selenium/chromedriver',
    seleniumServerJar: '/usr/local/lib/node_modules/protractor/selenium/selenium-server-standalone-2.42.2.jar',
    // Spec patterns are relative to the location of this config.
    specs: [
        'specs/cucumber/*.feature'
    ],
    baseUrl: '',
    cucumberOpts: {
        format: 'pretty'
    },
    capabilities: {
        browserName: 'chrome',
//        'phantomjs.binary.path': 'node_modules/phantomjs/bin/phantomjs',
//        'phantomjs.cli.args': ['--logfile=./phantomjs.log', '--loglevel=DEBUG'],
        shardTestFiles: false,
        maxInstances: 3
    }
};
