load('src/.js/vector.js')

Global.Node =
  init: () -> @links = []

  force: () ->
    node = this
    _.reduce(@links,
             (current_force, link) -> Vector.sum(current_force, link.force_at(node)),
             [0,0,0])

  match: (a, b) -> a[0] == b[0] and a[1] == b[1] and a[2] == b[2]

  apply_force: () ->
    f = @force()
    @[0] = f[0] + @[0]
    @[1] = f[1] + @[1]
    @[2] = f[2] + @[2]

  pos: () -> [@[0], @[1], @[2]]

