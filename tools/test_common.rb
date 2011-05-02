
def compile_src
  system("coffee -c -o src/.js src")
end

def compile_test
  system("coffee -c -o test/.js test")
end


def test_files(arguments, options={:suffix=>''})
  arguments.collect{ |arg|
    dir, file = File.split(arg)
    file = File.basename(file, '.coffee')
    file = "#{dir}/.js/#{file}#{options[:suffix]}.js"
    if File.exists?(file)
      file
    elsif File.directory?(arg)
      Dir["#{arg}/.js/*#{options[:suffix]}.js"]
    else
      puts "'#{file}' not found"
      nil
    end
  }.compact.flatten
end

def load(files)
  files.collect{|file_path| %{load("#{file_path}");}}.join("\n")
end
  
def script(test_files)
%{
load("dependency/qunit/qunit-cli.js");
load("dependency/underscore.js");
load("src/global.js");
load("src/.js/qunit_extensions.js");
#{load(test_files)}

QUnit.begin();
QUnit.start();
}
end

def run_tests(test_files)
  File.open('.test.js', 'w+'){|f| f << script(test_files)}
  system("js .test.js")
end

