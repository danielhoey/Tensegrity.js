load('src/.js/vector.js')
load('src/.js/node.js')

Global.Material =
  elasticity: 0
  compressibility: 0
  update: () ->
    @vector = Vector.create(@end_points[0], @end_points[1])
    @unit_vector = @vector.unit()
    @stretch = @vector.length - @length
    @force_magnitude = @magnitude_of_response()

    @force_at = (point) ->
      if Node.match(@end_points[1], point)
        @unit_vector.scale(-@force_magnitude)
      else
        @unit_vector.scale(@force_magnitude)

  magnitude_of_response: () ->
    if @stretch > 0
      return @elasticity * @stretch
    else
      return @compressibility * -@stretch
