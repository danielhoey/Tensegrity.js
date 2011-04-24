load("src/global.js")

TensionElement =
  create: (data) ->
    data.force = () -> this.vector().unit().scale(-this.elasicity * this.stretch())
    data.vector = () -> Vector.create(this.position)
    data.stretch = () -> 
              diff = this.vector().length - this.length
              diff = 0 if (diff < 0) 
              return diff

    return data; 

Global.define('TensionElement', TensionElement);

Vector = 
  length: (v) -> Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2])

  create: (end_points) -> 
    p = end_points;
    v = [p[1][0] - p[0][0], p[1][1]-p[0][1], p[1][2]-p[0][2]];
    l = Vector.length(v);

    object = 
      length: l
      unit: () -> Vector.create([[0,0,0], [v[0]/l, v[1]/l, v[2]/l]])
      scale: (k) -> [k*v[0], k*v[1], k*v[2]]
      data: v


    return object;

Global.define('Vector', Vector);
