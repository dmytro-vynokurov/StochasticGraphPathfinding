package graph

import stochastic.{NilDistribution, NormalDistribution}
import scala.beans.BeanProperty


/**
 * Created with IntelliJ IDEA.
 * User: Vinokurov
 * Date: 12.05.13
 * Time: 15:46
 * To change this template use File | Settings | File Templates.
 */
class Vertex(_name: String) {
  var edges = Set[Edge]()
  val name = _name
  var distanceFromFinish: NormalDistribution = NilDistribution
  var ancestor: Vertex = _

  def addEdge(edge: Edge) {
    edges = edges + edge
  }

  def neighbours: List[Vertex] = {
    (for (edge <- edges) yield (edge.getOtherVertex(this))).toList
  }

  def edgeConnectsWith(other: Vertex): Edge = edges.find(_ touches other).get

  override def toString = "Vertex: " + name + "\tDistance from finish: " + distanceFromFinish + "\tAncestor: " + ancestorToString

  private def ancestorToString: String = if (ancestor != null) return ancestor.name.toString else return "None"
}
