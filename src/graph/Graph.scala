package graph

import scala.collection.mutable.Set

/**
 * Created with IntelliJ IDEA.
 * User: Vinokurov
 * Date: 12.05.13
 * Time: 15:54
 * To change this template use File | Settings | File Templates.
 */
class Graph(_vertexes: List[Vertex]) {
  def this() = this(Nil)

  var vertexes: List[Vertex] = _vertexes
  var start: Vertex = _
  var finish: Vertex = _
  var checkpointLines: List[CheckpointLine] = List()

  def addVertex(v: Vertex) = {
    vertexes = vertexes ::: List(v)
    updateStartFinish()
  }

  def removeVertex(v: Vertex) {
    vertexes = removeFromList(vertexes,v)
    updateStartFinish()
  }

  private def removeFromList[T](vertexes: List[T], v: T): List[T] = {
    if (vertexes contains v) return vertexes diff List(v)
    else return vertexes
  }

  def removeEdge(e: Edge) {
    val vertexesWithOneEdge = vertexes.filter(_.neighbours.length == 1)
    val edgesConnectingWithLonelyVertexes = vertexesWithOneEdge.flatMap(_.edges)
    if (edgesConnectingWithLonelyVertexes contains e)
      throw new IllegalArgumentException("Edge cannot be removed since it connects the lonely vertex to the graph")

    for (
      vertex <- vertexes;
    ) vertex.edges-=e
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

  private def updateStartFinish(){
    val vertexesButStart={
      if (start==null) vertexes
      else removeFromList(vertexes,start)
    }
    val vertexesButEnds={
      if(finish==null)vertexesButStart
      else removeFromList(vertexesButStart,finish)
    }

    if(start==null){
      start=vertexesButEnds head
    }

    if (finish==null){
      finish=vertexesButEnds last
    }
  }

  def edges=vertexes.flatMap(_.edges)

  class CheckpointLine(_vertexes: List[Vertex]) {

    val checkpoints = _vertexes

    override def toString = "Checkpoint line{\n" + (checkpoints mkString "\n") + "}"
  }

  implicit def checkpointLineToList(checkpointLine: CheckpointLine): List[Vertex] = checkpointLine.checkpoints

  implicit def listToCheckpointLine(list: List[Vertex]): CheckpointLine = new CheckpointLine(list)

  def generateCheckpoints() = {
    if ((start == null) || (finish == null)) throw new IllegalStateException("Start or Finish was not set for a graph")

    var currentCheckpointLine = new CheckpointLine(List(start))
    var checkpointsPassed: List[Vertex] = List(start)

    checkpointLines = currentCheckpointLine :: checkpointLines

    while (!(currentCheckpointLine isEmpty)) {
      checkpointsPassed = currentCheckpointLine ::: checkpointsPassed
      currentCheckpointLine = currentCheckpointLine.checkpoints
        .flatMap(_.neighbours)
        .filter(!checkpointsPassed.contains(_))
        .filter(_ != finish)

      if (currentCheckpointLine.length != 0) checkpointLines = List(currentCheckpointLine) ::: checkpointLines
    }

    checkpointLines = List(finish) :: checkpointLines
  }

  def moveToNextCheckpointLine(currentLine: CheckpointLine, vertexesPassed: Set[Vertex]) = {
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

  def moveThroughTheGraph() = {
    val vertexesPassed = Set[Vertex](finish)
    for (currentLine <- checkpointLines) moveToNextCheckpointLine(currentLine, vertexesPassed)
  }

  def bestPath: List[Vertex] = {
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

}