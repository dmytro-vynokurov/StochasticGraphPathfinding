package graph

import stochastic.NormalDistribution

/**
 * Created with IntelliJ IDEA.
 * User: Vinokurov
 * Date: 12.05.13
 * Time: 15:46
 * To change this template use File | Settings | File Templates.
 */
class Edge(_begin: Vertex, _end: Vertex, _weight: NormalDistribution) {
  _begin.addEdge(this)
  _end.addEdge(this)

  var begin = _begin
  var end = _end
  val weight = _weight

  def determinedWeight = weight.m

  def getOtherVertex(v: Vertex): Vertex = {
    if (v == begin) return end
    if (v == end) return begin
    throw new IllegalArgumentException("Vertex is not on current edge")
  }

  def setOtherVertex(v: Vertex) = {
    if (v == begin) end = v
    if (v == end) begin = v
    throw new IllegalArgumentException("Vertex is not on current edge")
  }

  def touches(v: Vertex): Boolean = ((v == begin) || (v == end))

  override def toString: String =
    return "Edge:\t" + begin.name + "\tto\t" + end.name + "\tWeight: " + weight
}

object Edge {
  def apply(begin: Vertex, end: Vertex, weight: NormalDistribution) = new Edge(begin, end, weight)

  def apply(begin: Vertex, end: Vertex, m: Double, s2: Double) = new Edge(begin, end, new NormalDistribution(m, s2))
}