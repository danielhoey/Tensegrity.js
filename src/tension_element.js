
var TensionElement = {
  create: function(data) { 
            data.force = function() {
              return [-this.elasicity * this.stretch(), 0, 0];
            }
            data.vector = function() {
              var p = this.position;
              return [p[1][0] - p[0][0], p[1][1]-p[0][1], p[1][2]-p[0][2]];
            }
            data.stretch = function() {
              var diff = this.vector()[0] - this.length;
              if (diff < 0) { diff = 0; }
              return diff;
            }

            function length(vector) {
              var v = vector;
              return Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
            }

            return data; 
          }
};

var Vector = {
  scale: function(k, v) {
         }

}
