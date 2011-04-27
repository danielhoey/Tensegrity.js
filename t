#!/usr/bin/env ruby

compile_output = `coffee -c src`
if not compile_output.empty?
  puts compile_output
  exit
end

load_test_files = ARGV.collect{|file_path| %{load("#{file_path}_test.js");}}.join("\n")
    
script = %{
load("dependency/qunit/qunit-cli.js");
load("src/global.js");
#{load_test_files}

QUnit.begin(); // hacked b/c currently QUnit.begin is normally called on document.load
QUnit.start();
}

File.open('.test.js', 'w+'){|f| f << script}

test_output = `js .test.js`
puts test_output
