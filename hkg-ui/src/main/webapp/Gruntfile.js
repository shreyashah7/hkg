'use strict';
module.exports = function(grunt) {
    // Project configuration.
    grunt.initConfig({
//        //location of Cucumber files and it's steps files. 
//        cucumber_src: 'specs/cucumber',
//        //Projects all javascript file paths
//        js_src: ['grunt.js', 'js/*.js'],
//        //Allowed us to reference properties we declared in package.json 
//        pkg: grunt.file.readJSON('package.json'),
//        //base folder will deploy on 8000 port till the grunt task is running
//        connect: {
//            server: {
//                options: {
//                    port: 8000,
//                    //keepalive:true,
////                  open:  {
////                     target: 'http://localhost:8000', 
////                     appName: 'esg',
////                     callback: function() {}
////                    },
//                    directory: '/'                
//                }
//            }
//        },
//        //Watch task
//        watch: {
//            cucumber: {
//                files: ['<%= cucumber_src %>/*.js', '<%= cucumber_src %>/*.feature'],
//                tasks: ['cucumberjs']
//            },
//            jshintlintwatch: {
//                files: '<%= js_src %>',
//                tasks: ['jshint', 'jslint']
//            }
//        },
//        //Cucumber task
//        cucumberjs: {
//            src: '<%= cucumber_src %>/', //location of feature file
//            options: {
//                steps: '<%= cucumber_src %>/', //location of the steps file
//                format: 'pretty'
//            }
//        },        
        //Configuration to be run (and then tested). https://www.npmjs.org/package/grunt-protractor-runner        
        protractor: {
            chrome: {
                options: {
                    configFile: 'protractor-config.js', //protractor config files,Don't remove this
                    keepAlive: false, // If false, the grunt process stops when the test fails.
                    noColor: false, // If true, protractor will not use colors in its output.
                    //debug: true,
                    args: {
                    }
                }
            }
        },
//        //Configuration of Jshint
//        jshint: {
//            client: {
//                src: '<%= js_src %>',
//                options: {
//                    passfail:false,
//                    curly: true,
//                    eqeqeq: true,
//                    eqnull: true,
//                    browser: true,
//                    camelcase: false,
//                    immed: true, 
//                    noempty: true,
//                    nonew: true,
//                    quotmark: 'Single',
//                    undef: true,
//                    unused: true,
//                    globals: {
//                        jQuery: true,
//                        console: true,
//                        module: true,
//                        document: true
//                    }
//                }
//            }
//        },
//        //Jslint task
//        jslint: {
//            // lint project's client code
//            client: {
//                src: '<%= js_src %>',
//                directives: {
//                    browser: true,
//                    predef: [
//                        'jQuery'
//                    ]
//                },
//                options: {
//                    errorsOnly: true,
//                    log: 'log/jslint.xml',
//                    failOnError: false
//                }
//            }
//        }
//
        uglify: {
            my_target: {
                files: [{
                        expand: true,
                        cwd: 'scripts',
                        src: '**/*.js',
                        dest: 'scripts-dest'
                    }]
            }
        }
    });
//    //These plugins provide necessary tasks.
//    grunt.loadNpmTasks('grunt-contrib-watch');
//    grunt.loadNpmTasks('grunt-cucumber');
    grunt.loadNpmTasks('grunt-protractor-runner');
    grunt.loadNpmTasks('grunt-contrib-uglify');
//    grunt.loadNpmTasks('grunt-jslint');
//    grunt.loadNpmTasks('grunt-contrib-jshint');
//    grunt.loadNpmTasks('grunt-contrib-connect');

    grunt.registerTask('protractor-test', 'Cucumber test case run', ['protractor']);
    grunt.registerTask('minify-css', 'Cucumber test case run', ['uglify:my_target']);
//    grunt.registerTask('test', 'Code analysis test.', ['jslint', 'jshint']);
//    grunt.registerTask('default', 'Starts a watch.', ['connect:server','watch:cucumber', 'watch:jshintlintwatch']);
};
