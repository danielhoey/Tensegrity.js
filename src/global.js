load('dependency/underscore.js');

Global = this;

Global.create = function(parent, properties) {
  if (!parent) {throw "parent undefined!";}

  // set prototype
  function F() {}
  F.prototype = parent;
  var child = new F();

  // copy properties
  for (key in properties) { 
    child[key] = properties[key];
  }

  // initialization
  if (child.init) {child.init()}

  return child;
}
