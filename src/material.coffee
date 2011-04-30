load('src/.js/vector.js')

Global.Material =
  update: () ->
    @vector = Vector.create(@end_points[0], @end_points[1])
    @stretch = stretch(@vector, @length)
    @unit_vector = @vector.unit()
    @force_at = (point) ->
      if Node.match(@end_points[1], point)
        @unit_vector.scale(-@elasticity * @stretch)
      else
        @unit_vector.scale(@elasticity * @stretch)


stretch = (vector, length) ->
  diff = vector.length - length
  diff = 0 if (diff < 0)
  return diff
