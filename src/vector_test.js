(function() {
  load("src/vector.js");
  module("Vector");
  test("sum", function() {
    var v1, v2, v3;
    v1 = Vector.create([1, 2, 3]);
    v2 = Vector.create([1, 1, 2]);
    v3 = Vector.sum(v1, v2);
    return deepEqual(v3, [2, 3, 5]);
  });
}).call(this);
