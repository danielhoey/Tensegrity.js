
Global.arrayEquals = (a1, a2) ->
  i = 0
  while(true)
    if (a1[i] != a2[i])
      QUnit.push(false, a1[i], a2[i], "element "+i+" does not match")
    else if (!a1[i]?)
      QUnit.push(true)
      return
    i = i + 1

Global.pointEquals = (p1, p2, options={delta:0}) ->
  throw "require options.delta" if not options.delta?
  for i in [0..2]
    if Math.abs(p1[i] - p2[i]) > options.delta
      QUnit.push(false, p1[i], p2[i], "element "+i+" does not match")
      return
  QUnit.push(true)
