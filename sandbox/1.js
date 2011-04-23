
chord = create_tensile_element({length: 10, elasicity: 4})
chord = create_compression_element({length: 10, compressibility: 1})
c1 = create(chord, {x:1,y:1,z:1})

chord = TensionElement.create({length: 10, elasicity: 4})
struct = CompressionElement.create({length: 10, compressibility: 1})


chord = create(TensionElement, {length: 10, elasicity: 4})
struct = create(CompressionElement, {length: 10, compressibility: 1})
c1 = World.create(chord, {x:1,y:1,z:1})
