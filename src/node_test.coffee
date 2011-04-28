load("src/.js/node.js")

module("Node")
test("acts like array of numbers", () ->
  node = Node.create([1,2,3])

  equals(node[0], 1)
  equals(node[1], 2)
  equals(node[2], 3)
)

test("calculates force", () ->
  node = Node.create([1,2,3])
  link1 = force: (n) -> [-1,0,1]
  link2 = force: (n) -> [1,2,0]
  node.links = [link1, link2]

  deepEqual(node.force(), [0,2,1])
)
