load("src/material.js");

module("Material");
test("position vector", function() {
  element= Material.create({position:[[0,0,0], [11,0,0]]});
  deepEqual(element.vector().data, [11,0,0]);
});
test("elasticity - one dimension", function() {
  element = Material.create({length: 10, elasicity: 4});
  element.position = [[0,0,0], [11,0,0]];
  deepEqual(element.force(), [-4,0,0]);
});
test("elasticity - compression", function() { 
  element = Material.create({length: 10, elasicity: 4});
  element.position = [[0,0,0], [9,0,0]];
  deepEqual(element.force(), [0,0,0]);
});
test("elasticity - multiple dimension", function() {
  element = Material.create({length: 10, elasicity: 4});
  element.position = [[0,0,0], [6,6,7]];
  deepEqual(element.force(), [-24/11, -24/11, -28/11]);
});
