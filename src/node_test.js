(function() {
  load("src/node.js");
  module("Node");
  test("acts like array of numbers", function() {
    var node;
    node = Node.create([1, 2, 3]);
    equals(node[0], 1);
    equals(node[1], 2);
    return equals(node[2], 3);
  });
  test("calculates force", function() {
    var link1, link2, node;
    node = Node.create([1, 2, 3]);
    link1 = {
      force: function(n) {
        return [-1, 0, 1];
      }
    };
    link2 = {
      force: function(n) {
        return [1, 2, 0];
      }
    };
    node.links = [link1, link2];
    return equals(node.force(), [0, 2, 1]);
  });
}).call(this);
