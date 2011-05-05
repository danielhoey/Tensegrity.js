load('src/.js/vector.js')
load('src/.js/node.js')

# Material represents an element in a tensegrity. 
# These elements have a resting length and response to the materials length being changed (elasticity and compressibility). 
# Elasticity and compressibility are how much a node should move in response to the material being streched (or compressed) by it's initial length.
Global.Material =
  elasticity: 0
  compressibility: 0
  init: () ->
    @initial_length = @length

  update: () ->
    @vector = Vector.create(@end_points[0], @end_points[1])
    @length = @vector.length
    @unit_vector = @vector.unit()
    @tension = (@length - @initial_length) / @initial_length
    @compression = -@tension
    @force_magnitude = @magnitude_of_response()

    @force_at = (point) ->
      throw "point is undefined!" if not point?
      if Node.match(@end_points[0], point)
        @unit_vector.scale(@force_magnitude)
      else if Node.match(@end_points[1], point)
        @unit_vector.scale(-@force_magnitude)
      else
        throw "force_at() called with a point #{Vector.text(point)} that is neither of the end points (#{Vector.text(@end_points[0])} and #{Vector.text(@end_points[1])})"

  magnitude_of_response: () ->
    if @tension > 0
      return @elasticity * @tension
    else
      return @compressibility * @compression

  to_s: () ->
    "Material(e: #{@elasticity}, c: #{@compressibility}, il: #{@initial_length}, t: #{@tension}, c: #{@compression}, v: [#{Vector.text(@end_points[0])}, #{Vector.text(@end_points[1])})"
