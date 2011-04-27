load("dependency/qunit/qunit-cli.js");
load("src/global.js");
load("src/material_test.js");

QUnit.begin(); // hacked b/c currently QUnit.begin is normally called on document.load
QUnit.start();
