(function() {
  Global.Node = {
    create: function(object) {
      object.force = function() {
        return 0;
      };
      return object;
    }
  };
}).call(this);
