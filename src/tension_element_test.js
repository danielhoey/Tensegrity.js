load("src/tension_element.js");

module("TensionElement");
test("position vector", function() {
  element= TensionElement.create({position:[[0,0,0], [11,0,0]]});
  deepEqual(element.vector(), [11,0,0]);
});
test("elasticity - one dimension", function() {
  element = TensionElement.create({length: 10, elasicity: 4});
  element.position = [[0,0,0], [11,0,0]];
  deepEqual(element.force(), [-4,0,0]);
});
test("elasticity - compression", function() { 
  element = TensionElement.create({length: 10, elasicity: 4});
  element.position = [[0,0,0], [9,0,0]];
  deepEqual(element.force(), [0,0,0]);
});
test("elasticity - multiple dimension", function() {
  ok(false, "todo");
});
