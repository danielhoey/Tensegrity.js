load("src/vector.js")

module("Vector")
test("sum", () ->
  v1 = Vector.create([1,2,3])
  v2 = Vector.create([1,1,2])
  v3 = Vector.sum(v1, v2)
  deepEqual(v3, [2,3,5])
)
