load('src/.js/vector.js')

Global.Material =
  create: (object={}) ->
    return create(functions, object)

functions =
  force: () -> @vector().unit().scale(-@elasicity * @stretch())
  vector: () -> Vector.create(@position[0], @position[1])
  stretch: () ->
    diff = @vector().length - @length
    diff = 0 if (diff < 0)
    return diff
 
