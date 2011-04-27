(function() {
  Global.Vector = {
    length: function(v) {
      return Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
    },
    sum: function(v1, v2) {
      v1 = v1.data;
      v2 = v2.data;
      return [v1[0] + v2[0], v1[1] + v2[1], v1[2] + v2[2]];
    },
    create: function(p1, p2) {
      var l, v;
      if (!(p2 != null)) {
        p2 = p1;
        p1 = [0, 0, 0];
        print("no p2");
      } else {
        print("p2");
      }
      v = [p2[0] - p1[0], p2[1] - p1[1], p2[2] - p1[2]];
      l = Vector.length(v);
      return {
        length: l,
        unit: function() {
          return Vector.create([v[0] / l, v[1] / l, v[2] / l]);
        },
        scale: function(k) {
          return [k * v[0], k * v[1], k * v[2]];
        },
        data: v
      };
    }
  };
}).call(this);
