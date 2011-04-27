# Vector represents a 3D vector originating at (0,0,0)

Global.Vector =
  length: (v) -> Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2])
  sum: (v1, v2) ->
    v1 = v1.data
    v2 = v2.data
    [v1[0]+v2[0], v1[1]+v2[1], v1[2]+v2[2]]

  create: (end_points) ->
    p = end_points
    v = [p[1][0] - p[0][0], p[1][1]-p[0][1], p[1][2]-p[0][2]]
    l = Vector.length(v)

    return {
      length: l
      unit: () -> Vector.create([[0,0,0], [v[0]/l, v[1]/l, v[2]/l]])
      scale: (k) -> [k*v[0], k*v[1], k*v[2]]
      data: v
    }
