load('dependency/underscore.js');

Global = this;

Global.create = function(parent, properties) {
  if (!parent) {throw "parent undefined!";}
  function F() {}
  F.prototype = parent;
  var child = new F();
  for (key in properties) { 
    child[key] = properties[key];
  }
  return child;
}
