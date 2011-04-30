load('src/.js/vector.js')

Global.Node =
  force: () ->
    _.reduce(@links,
             (f,l) -> Vector.sum(f,l.force()),
             [0,0,0])
  match: (a, b) ->
    a[0] == b[0] and a[1] == b[1] and a[2] == b[2]
