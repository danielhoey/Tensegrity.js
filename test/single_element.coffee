load("src/.js/node.js")
load("src/.js/material.js")

module("Single Element")
test "should return to initial length", () ->
  n1 = create(Node, [0,0,0])
  n2 = create(Node, [0,110,0])
  link = create(Material, {length: 100, elasticity: 5, end_points: [n1, n2]})
  n1.links = [link]
  n2.links = [link]
  link.update()

  for i in [0..90]
#    print("node 1: "+n1.pos()+" -> "+link.force_at(n1))
#    print("node 2: "+n2.pos()+" -> "+n2.force())
#    print(link.length)
    n1.apply_force()
    n2.apply_force()
    link.update()

  #equals(link.length, link.initial_length)
  pointEquals(n1, [0,5,0], {delta:0.1})
  pointEquals(n2, [0,105,0], {delta:0.1})
  
