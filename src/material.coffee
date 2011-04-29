load('src/.js/vector.js')

Global.Material =
  update: () ->
    @vector = Vector.create(@end_points[0], @end_points[1])
    @stretch = stretch(@vector, @length)
    @force = @vector.unit().scale(-@elasticity * @stretch)


stretch = (vector, length) ->
  diff = vector.length - length
  diff = 0 if (diff < 0)
  return diff
