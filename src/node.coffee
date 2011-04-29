load('src/.js/vector.js')

Global.Node =
  force: () ->
    _.reduce(@links,
             (f,l) -> Vector.sum(f,l.force()),
             [0,0,0])
