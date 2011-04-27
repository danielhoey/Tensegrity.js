#!/usr/bin/env ruby

if not system("coffee -c src")
  exit
end

def test_files(arguments)
  arguments.collect{ |arg| 
    file = "#{arg}_test.js"
    if File.exists?(file)
      file
    elsif File.directory?(arg)
      Dir["#{arg}/*_test.js"]
    else
      nil
    end
  }.compact.flatten
end

load_test_files = test_files(ARGV).collect{|file_path| %{load("#{file_path}");}}.join("\n")
    
script = %{
load("dependency/qunit/qunit-cli.js");
load("dependency/underscore.js");
load("src/global.js");
#{load_test_files}

QUnit.begin();
QUnit.start();
}

File.open('.test.js', 'w+'){|f| f << script}

test_output = `js .test.js`
puts test_output
