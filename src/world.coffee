
Global.World =
  create_element: (parent, properties) ->
    element = create(parent, properties)
    element.end_points[0].links.push(element)
    element.end_points[1].links.push(element)
    return element
  

