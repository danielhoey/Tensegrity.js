load('src/.js/vector.js')

Global.Material =
  create: (object={}) ->
    object.force = () -> @vector().unit().scale(-@elasicity * @stretch())
    object.vector = () -> Vector.create(@position[0], @position[1])
    object.stretch = () ->
           diff = @vector().length - @length
           diff = 0 if (diff < 0)
           return diff
    return object
