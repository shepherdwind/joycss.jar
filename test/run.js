'use strict';
var path = require('path');
var spawn = require('child_process').spawn;

function init(){
  var repeatx = require('./repeat');
  var repeaty = require('./repeat-y.js');
  var simple = require('./simple.js');
  run(simple, function(){
    run(repeatx, function(){
      run(repeaty);
    });
  });
}

function run(obj, done){
  done = done || function(){};
  var dest = obj.dest;
  var base = obj.base;
  var config = obj.config;
  var jar = path.join(__dirname, '../target/spritebuilder-1.0.0.jar');
  var dest = path.join(__dirname, dest);
  var base = path.join(__dirname, base);
  var args = ['-jar', jar, JSON.stringify(config), base, dest];

  var t = Date.now();
  var cli = spawn('java', args);

  cli.stdout.on('data', function(buf){
    console.log(buf.toString());
  });

  cli.stderr.on('data', function(buf){
    console.log(buf.toString());
  });

  cli.on('exit', function(){
    console.log('end: %s', Date.now() - t);
    done();
  });
}

init();
