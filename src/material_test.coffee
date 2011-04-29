load("src/.js/material.js")

module("Material")
test("position vector", () ->
  material = Material.create()
  element = create(material, {position: [[0,0,0], [11,0,0]]})
  arrayEquals(element.vector(), [11,0,0])
)
test("elasticity - one dimension", () ->
  element = Material.create({length: 10, elasicity: 4})
  element.position = [[0,0,0], [11,0,0]]
  deepEqual(element.force(), [-4,0,0])
)
test("elasticity - compression", () ->
  element = Material.create({length: 10, elasicity: 4})
  element.position = [[0,0,0], [9,0,0]]
  deepEqual(element.force(), [0,0,0])
)
test("elasticity - multiple dimension", () ->
  element = Material.create({length: 10, elasicity: 4})
  element.position = [[0,0,0], [6,6,7]]
  deepEqual(element.force(), [-24/11, -24/11, -28/11])
)


arrayEquals = (a1, a2) ->
  i = 0
  while(true)
    if (a1[i] != a2[i])
      QUnit.push(false, a1[i], a2[i], "element "+i+" does not match")
    else if (a1[i]?)
      QUnit.push(true)
      return
    else
      i = i + 1
