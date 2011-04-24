
var TensionElement = {
  create: function(data) { 
            data.force = function() {
              return this.vector().unit().scale(-this.elasicity * this.stretch());
            }
            data.vector = function() {
              return Vector.create(this.position);
            }
            data.stretch = function() {
              var diff = this.vector().length - this.length;
              if (diff < 0) { diff = 0; }
              return diff;
            }

            return data; 
          }
};

var Vector = {
  create: function(end_points) {
            var p = end_points;
            var v = [p[1][0] - p[0][0], p[1][1]-p[0][1], p[1][2]-p[0][2]];
            var l = length(v);

            var object = {
              length: l,
              unit: function() { return Vector.create([[0,0,0], [v[0]/l, v[1]/l, v[2]/l]]); },
              scale: function(k) { return [k*v[0], k*v[1], k*v[2]]; },
              data: v
            };

  
            function length(v) { return Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]); }

            return object;
        }
};
