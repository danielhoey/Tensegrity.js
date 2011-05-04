# Vector represents a 3D vector originating at (0,0,0)

Global.Vector =
  length: (v) -> Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2])
  sum: (v1, v2) -> [v1[0]+v2[0], v1[1]+v2[1], v1[2]+v2[2]]
  text: (v) -> "["+v[0]+", "+v[1]+", "+v[2]+"]" if v?

  create: (p1, p2) ->
    if not p2?
      p2 = p1
      p1 = [0,0,0]

    v = [p2[0] - p1[0], p2[1]-p1[1], p2[2]-p1[2]]
    l = Vector.length(v)

    return {
      0: v[0]
      1: v[1]
      2: v[2]
      length: l
      unit: () -> Vector.create([v[0]/l, v[1]/l, v[2]/l])
      scale: (k) -> [k*v[0], k*v[1], k*v[2]]
      data: v
    }
