load('src/.js/vector.js')

Global.Material =
  create: (data) ->
    data.force = () -> @vector().unit().scale(-@elasicity * @stretch())
    data.vector = () -> Vector.create(@position[0], @position[1])
    data.stretch = () ->
           diff = @vector().length - @length
           diff = 0 if (diff < 0)
           return diff
    return data
