
Global.arrayEquals = (a1, a2) ->
  i = 0
  while(true)
    if (a1[i] != a2[i])
      QUnit.push(false, a1[i], a2[i], "element "+i+" does not match")
    else if (!a1[i]?)
      QUnit.push(true)
      return
    i = i + 1
