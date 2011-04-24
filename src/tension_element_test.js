load("src/tension_element.js");

module("TensionElement");
test("position vector", function() {
  element= TensionElement.create({position:[[0,0,0], [11,0,0]]});
  deepEqual(element.vector(), [11,0,0]);
});
test("elasticity", function() {
  return;
  element = TensionElement.create({length: 10, elasicity: 4});
  element.set_position([0,0,0], [11,0,0]);
  equals(element.force(), [4,0,0]);
});
