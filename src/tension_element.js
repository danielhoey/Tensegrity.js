
var TensionElement = {
  create: function(data) { 
            data.force = function() {
              var force_scalar = -this.elasicity * this.stretch();
              return Vector.scale(force_scalar, Vector.unit(this.vector()));
            }
            data.vector = function() {
              var p = this.position;
              return [p[1][0] - p[0][0], p[1][1]-p[0][1], p[1][2]-p[0][2]];
            }
            data.stretch = function() {
              var diff = length(this.vector()) - this.length;
              if (diff < 0) { diff = 0; }
              return diff;
            }

            function length(vector) {
              return Vector.length(vector);
            }

            return data; 
          }
};

var Vector = {
  scale: function(k, v) {
           return [k*v[0], k*v[1], k*v[2]];
         },
  unit: function(v) {
          var l = this.length(v);
          return [v[0]/l, v[1]/l, v[2]/l];
        },
  length: function(v) {
            return Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
          }

}
