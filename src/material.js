(function() {
  load('src/vector.js');
  Global.Material = {
    create: function(data) {
      data.force = function() {
        return this.vector().unit().scale(-this.elasicity * this.stretch());
      };
      data.vector = function() {
        return Vector.create(this.position[0], this.position[1]);
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
}).call(this);
