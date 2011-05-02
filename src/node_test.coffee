load("src/.js/node.js")

module("Node")
test("acts like array of numbers", () ->
  node = create(Node, [1,2,3])

  equals(node[0], 1)
  equals(node[1], 2)
  equals(node[2], 3)
)

test("calculates force", () ->
  node = create(Node, [1,2,3])
  link1 = force_at: (n) -> [-1,0,1] if n == node
  link2 = force_at: (n) -> [1,2,0] if n == node
  node.links = [link1, link2]

  deepEqual(node.force(), [0,2,1])
)

test("applies force", () ->
  node = create(Node, [1,2,3])
  link1 = force_at: (n) -> [-1,0,1] if n == node
  node.links = [link1]
  node.apply_force()
  arrayEquals(node, [0,2,4])
)
 

test("match other points", () ->
  node = create(Node, [1,2,3])
  ok(Node.match(node, [1,2,3]))
  ok(!Node.match(node, [2,3,4]))
  #ok(node.match([1,2,3]))
  #ok(!node.match([2,3,4]))
)
