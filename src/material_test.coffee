load("src/.js/material.js")

module("Material")
test("position vector", () ->
  element = create(Material, {end_points: [[0,0,0], [11,0,0]]})
  element.update()
  arrayEquals(element.vector, [11,0,0])
)

test("elasticity", () ->
  element = create(Material, {length: 10, elasticity: 4})
 
  # compressing the element should not result in a force response
  element.end_points = [[0,0,0], [9,0,0]]
  element.update()
  equals(element.force_magnitude, 0)
  
  # stretching the element should result in a force response
  element.end_points = [[0,0,0], [12,0,0]]
  element.update()
  equals(element.force_magnitude, 0.8)
)

test("compressibility", () ->
  element = create(Material, {length: 10, compressibility: 4})
 
  # compressing the element should result in a force response
  element.end_points = [[0,0,0], [7,0,0]]
  element.update()
  equals(element.force_magnitude, 1.2)
  
  # stretching the element should not result in a force response
  element.end_points = [[0,0,0], [13,0,0]]
  element.update()
  equals(element.force_magnitude, 0)
)

test("force - one dimension", () ->
  element = create(Material, {length: 10, elasticity: 4, end_points: [[0,0,0], [11,0,0]]})
  element.update()
  deepEqual(element.force_at([11,0,0]), [-0.4,0,0])
)

test("force - multiple dimension", () ->
  element = create(Material, {length: 10, elasticity: 4, end_points: [[0,0,0], [6,6,7]]})
  element.update()
  pointEquals(element.force_at([6,6,7]), [-24/110, -24/110, -28/110], {delta:0.01})
)

test("force - bidirectional", () ->
  element = create(Material, {length: 10, elasticity: 4, end_points: [[0,0,0], [11,0,0]]})
  element.update()
  deepEqual(element.force_at([11,0,0]), [-0.4,0,0])
  deepEqual(element.force_at([0,0,0]), [0.4,0,0])
)

