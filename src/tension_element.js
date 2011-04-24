
var TensionElement = {
  create: function(data) { 
            data.set_position = function(a, b) {
              this.position = [a, b];
            };
            data.force = function() {
              -this.elasicity * Math.length(this.vector());
            }
            data.vector = function() {
              var p = this.position;
              return [p[1][0] - p[0][0], p[1][1]-p[0][1], p[1][2]-p[0][2]];
            }

            return data; 
          }
};

var Math = {
  length: function(vector) {
            var v = vector;
            //v[1][0] - v[0][0] + vector[1][1] -
          }
}
