(function() {
  var TensionElement, Vector;
  load("src/global.js");
  TensionElement = {
    create: function(data) {
      data.force = function() {
        return this.vector().unit().scale(-this.elasicity * this.stretch());
      };
      data.vector = function() {
        return Vector.create(this.position);
      };
      data.stretch = function() {
        var diff;
        diff = this.vector().length - this.length;
        if (diff < 0) {
          diff = 0;
        }
        return diff;
      };
      return data;
    }
  };
  Global.define('TensionElement', TensionElement);
  Vector = {
    length: function(v) {
      return Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
    },
    create: function(end_points) {
      var l, object, p, v;
      p = end_points;
      v = [p[1][0] - p[0][0], p[1][1] - p[0][1], p[1][2] - p[0][2]];
      l = Vector.length(v);
      object = {
        length: l,
        unit: function() {
          return Vector.create([[0, 0, 0], [v[0] / l, v[1] / l, v[2] / l]]);
        },
        scale: function(k) {
          return [k * v[0], k * v[1], k * v[2]];
        },
        data: v
      };
      return object;
    }
  };
  Global.define('Vector', Vector);
}).call(this);
