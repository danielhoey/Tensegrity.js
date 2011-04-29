load('src/.js/vector.js')

Global.Material =
  force: () -> @vector().unit().scale(-@elasicity * @stretch())
  vector: () -> Vector.create(@end_points[0], @end_points[1])
  stretch: () ->
    diff = @vector().length - @length
    diff = 0 if (diff < 0)
    return diff
 
