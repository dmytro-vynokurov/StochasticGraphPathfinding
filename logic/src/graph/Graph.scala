package graph

import scala.collection.mutable.Set
import stochastic.{NilDistribution, NormalDistribution}


class Graph(_vertexes: List[Vertex]) {
  def this() = this(Nil)

  var vertexes: List[Vertex] = _vertexes
  var start: Vertex = _
  var finish: Vertex = _
  var checkpointLines: List[CheckpointLine] = List()

  updateStartFinish()

  def addVertex(v: Vertex) {
    vertexes = v :: vertexes
    updateStartFinish()
  }

  def removeVertex(v: Vertex) {
    vertexes = removeIfExists(vertexes, v)
    updateStartFinish()
  }

  private def removeIfExists[T](vertexes: List[T], v: T): List[T] = {
    if (vertexes contains v) vertexes diff List(v)
    else vertexes
  }

  def removeEdge(e: Edge) {
    val vertexesWithOneEdge = vertexes.filter(_.neighbours.length == 1)
    val edgesConnectingWithLonelyVertexes = vertexesWithOneEdge.flatMap(_.edges)
    if (edgesConnectingWithLonelyVertexes contains e)
      throw new IllegalArgumentException("Edge cannot be removed since it connects the lonely vertex to the graph")

    for (v <- vertexes) v.edges -= e
  }

  def setStart(v: Vertex) {
    if (!(vertexes contains v)) this.addVertex(v)
    start = v
    updateStartFinish()
  }

  def setFinish(v: Vertex) {
    if (!(vertexes contains v)) this.addVertex(v)
    finish = v
    updateStartFinish()
  }

  private def updateStartFinish() {
    if (start == null) {
      if (finish == null) {
        if (vertexes.length > 0) {
          start = vertexes(0)
          if (start.neighbours.length > 0) finish = start.neighbours(0)
          else finish = start
        }
      } else {
        if (finish.neighbours.length > 0) start = finish.neighbours(0)
        else start = finish
      }
    } else {
      if (finish == null) {
        if (start.neighbours.length > 0) finish = start.neighbours(0)
        else finish = start
      }
    }
  }

  def edges = vertexes.flatMap(_.edges).distinct

  class CheckpointLine(_vertexes: List[Vertex]) {

    val checkpoints = _vertexes

    override def toString = "Checkpoint line{\n" + (checkpoints mkString "\n") + "}"
  }

  implicit def checkpointLineToList(checkpointLine: CheckpointLine): List[Vertex] = checkpointLine.checkpoints

  implicit def listToCheckpointLine(list: List[Vertex]): CheckpointLine = new CheckpointLine(list)

  def generateCheckpoints() {
    if ((start == null) || (finish == null)) return

    var currentCheckpointLine = new CheckpointLine(List(start))
    var checkpointsPassed: List[Vertex] = List(start)

    checkpointLines = currentCheckpointLine :: Nil

    while (!(currentCheckpointLine isEmpty)) {
      checkpointsPassed = currentCheckpointLine ::: checkpointsPassed
      currentCheckpointLine = currentCheckpointLine.checkpoints
        .flatMap(_.neighbours)
        .filter(!checkpointsPassed.contains(_))
        .filter(_ != finish)
        .distinct

      if (currentCheckpointLine.length != 0) checkpointLines = List(currentCheckpointLine) ::: checkpointLines
    }

    if (start != finish) checkpointLines = List(finish) :: checkpointLines

    val vertexesLeft = vertexes diff checkpointsPassed diff List(finish)
    if (checkpointLines.flatMap(_.checkpoints).length < vertexes.length)
      checkpointLines = checkpointLines ::: List(new CheckpointLine(vertexesLeft))
  }

  private def moveToNextCheckpointLine(currentLine: CheckpointLine, vertexesPassed: Set[Vertex]) {
    for (vertex <- currentLine) {
      val neighboursToGo = vertex.neighbours.filter(!vertexesPassed.contains(_))
      for (neighbour <- neighboursToGo) {
        val pathThroughVertex = vertex.distanceFromFinish + vertex.edgeConnectsWith(neighbour).weight
        if (pathThroughVertex < neighbour.distanceFromFinish) {
          neighbour.distanceFromFinish = pathThroughVertex
          neighbour.ancestor = vertex
        }
      }
      vertexesPassed += vertex
    }
  }

  private def moveThroughTheGraph() {
    val vertexesPassed = Set[Vertex](finish)
    for (currentLine <- checkpointLines) moveToNextCheckpointLine(currentLine, vertexesPassed)
  }

  def bestPath: List[Vertex] = {
    moveThroughTheGraph()

    var path = List[Vertex]()
    var currentCheckpoint: Vertex = start
    while (currentCheckpoint.ancestor != null) {
      path = List(currentCheckpoint) ::: path
      currentCheckpoint = currentCheckpoint.ancestor
    }
    path = finish :: path
    path = path.reverse
    return path
  }

  def bestPathLength(bestPath: List[Vertex]): NormalDistribution = {
    if (bestPath.length <= 1) return new NilDistribution()
    else {
      var vertexesLeft = bestPath.tail
      var prevVertex = bestPath.head
      var sum: NormalDistribution = new NormalDistribution(0, 0);

      while (vertexesLeft.nonEmpty) {
        sum = sum + prevVertex.edgeConnectsWith(vertexesLeft.head).weight
        prevVertex = vertexesLeft.head
        vertexesLeft = vertexesLeft.tail
      }
      return sum
    }
  }
}
