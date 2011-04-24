
Global = {
  define: function(variable_name, object) {
            this[variable_name] = object;
            eval(variable_name+" = Global."+variable_name);
          }
};
