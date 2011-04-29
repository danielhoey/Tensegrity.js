load('dependency/underscore.js');

Global = this;

Global.instantiate = function(parent, properties) {
  if (!parent) {throw "parent undefined!";}
  function F() {}
  F.prototype = parent;
  var child = new F();
  _.each(properties, function(value, key){child[key] = value});
  return child;
}
