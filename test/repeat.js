'use strict';
var config = {
  "background": "ffffff7f",
  "width": 102,
  "height": 30,
  "imagetype": "3",
  "images": [
    {
      "width": 5,
      "height": 1,
      "type": 3,
      "file_location": "./slice-08.png",
      "repeat": "repeat-x",
      "align": "",
      "spritepos_left": 0,
      "spritepos_top": 29
    },
    {
      "width": 96,
      "height": 19,
      "type": 3,
      "file_location": "./title2.png",
      "repeat": "no-repeat",
      "align": "",
      "spritepos_left": 0,
      "spritepos_top": 0
    }
  ]
};

exports.dest = './repeat-x/repeat-x.png';
exports.base = './repeat-x/';
exports.config = config;
