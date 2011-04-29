load("src/global.js")

module("Global")
test("instantiate", () ->
  object = {type: 'test object'}
  child = instantiate(object, {name: 'child object'})
  equals(child.type, 'test object')
  equals(child.name, 'child object')
)
test("instantiate (with length)", () ->
  # catch bug caused by iterator assuming that objects with the length property are arrays
  object = {width: 5}
  child = instantiate(object, {length: 10})
  equals(child.length, 10)
  equals(child.width, 5)
)

