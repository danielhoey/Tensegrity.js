load("src/.js/node.js")
load("src/.js/material.js")
load("src/.js/world.js")

module("3 Struct Prism")
test "setup", () ->
  short_chord = create(Material, {length: 76, elasticity: 5})
  long_chord = create(Material, {length: 178, elasticity: 5})
  strut = create(Material, {length: 225, compressibility: 1})

  # pre-tense everything by (atleast?) 1 unit so short cord end points should be 77 apart rather then 76 (their initial length)
  n = []
  c = []
  n[0] = create(Node, [0,0,0])
  n[1] = create(Node, [0,77,0])
  n[2] = create(Node, [66.5,38.5,0])
  c[0] = World.create_element(short_chord, {end_points: [n[0], n[1]]})
  c[1] = World.create_element(short_chord, {end_points: [n[0], n[2]]})
  c[2] = World.create_element(short_chord, {end_points: [n[1], n[2]]})
  #print("n[0]")
  #print(n[0].links[0].to_s())
  #print(n[0].links[1].to_s())
  #print("n[1]")
  #print(n[1].links[0].to_s())
  #print(n[1].links[1].to_s())
  #print("n[2]")
  #print(n[2].links[0].to_s())
  #print(n[2].links[1].to_s())

  n[3] = create(Node, [0,0,224.9])
  n[4] = create(Node, [0,77,224.9])
  n[5] = create(Node, [66.5,38.5,224.9])
  c[3] = World.create_element(short_chord, {end_points: [n[3], n[4]]})
  c[4] = World.create_element(short_chord, {end_points: [n[3], n[5]]})
  c[5] = World.create_element(short_chord, {end_points: [n[4], n[5]]})

  c[6] = World.create_element(long_chord, {end_points: [n[0], n[4]]})
  c[7] = World.create_element(long_chord, {end_points: [n[1], n[5]]})
  c[8] = World.create_element(long_chord, {end_points: [n[2], n[3]]})

  c[9] = World.create_element(strut, {end_points: [n[0], n[3]]})
  c[10] = World.create_element(strut, {end_points: [n[1], n[4]]})
  c[11] = World.create_element(strut, {end_points: [n[2], n[5]]})

#    print("node 2: "+n2.pos()+" -> "+n2.force())
#    print(link.length)
 
  test_tension_and_compression = (iteration=0) ->
    _.each(c, (element) ->
        throw "element #{element.to_s()} not under tension (iteration #{iteration})" if element.elasticity > 0 and element.tension <= 0
        throw "element #{element.to_s()} not under compression (iteration #{iteration})" if element.compressibility > 0 and element.compression <= 0
    )
    
  _.each(c, (element) -> element.update())
  test_tension_and_compression()

  print("\n---------------------------")
  for i in [1..100]
    _.each(c, (element) -> element.update())
    _.each(n, (node) -> node.apply_force())
    test_tension_and_compression(i)
    #print("\n---------------------------")
    #_.each(n, (node) -> print("#{node.pos()} -> #{node.force()}"))
    #print("#{n[5].pos()} -> #{n[5].force()}")


  #World.create [
  #  [short_chord, [[0,0,0], [1,1,1]],
  #                [[0,0,0], [0,1,1]]],
  #  [long_chord, [[0,0,0], [0,0,1]]],
  #  [struct, [[0,0,0], [0,1,0]]]
  #]

