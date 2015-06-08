package graph

import stochastic.{NilDistribution, NormalDistribution}


class Vertex(_name: String) {
  var edges = Set[Edge]()
  val name = _name
  var distanceFromFinish: NormalDistribution = new NilDistribution
  var ancestor: Vertex = _

  def addEdge(edge: Edge) {
    edges = edges + edge
  }

  def neighbours: List[Vertex] = {
    (for (edge <- edges) yield (edge.getOtherVertex(this))).toList
  }

  def edgeConnectsWith(other: Vertex): Edge = edges.find(_ touches other).get

  def isConnectedTo(otherVertex: Vertex): Boolean = {
    var vertexesPassed: List[Vertex] = List(this)
    var currentLine: List[Vertex] = List(this)
    var nextLine: List[Vertex] = currentLine.flatMap(_.neighbours) diff vertexesPassed
    while (!nextLine.isEmpty) {
      if (nextLine.contains(otherVertex)) return true
      vertexesPassed = currentLine ::: vertexesPassed
      currentLine = nextLine
      nextLine = currentLine.flatMap(_.neighbours) diff vertexesPassed
    }
    return false
  }

  override def toString = "Vertex( " + name + " )"

  def fullInfo = "Vertex: " + name + "\tDistance from finish: " + distanceFromFinish + "\tAncestor: " + ancestorToString

  private def ancestorToString: String = if (ancestor != null) ancestor.name.toString else "None"
}
