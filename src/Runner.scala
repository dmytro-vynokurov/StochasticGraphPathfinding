import graph.{Graph, Edge, Vertex}
import stochastic.NormalDistribution

/**
 * Created with IntelliJ IDEA.
 * User: Vinokurov
 * Date: 12.05.13
 * Time: 15:47
 * To change this template use File | Settings | File Templates.
 */
object Runner {
  def main(args: Array[String]) {
    val v1 = new Vertex("1")
    val v2 = new Vertex("2")
    val v3 = new Vertex("3")
    val v4 = new Vertex("4")
    val v5 = new Vertex("5")
    val v6 = new Vertex("6")
    val v7 = new Vertex("7")
    val v8 = new Vertex("8")
    Edge(v1, v2, 5, 2)
    Edge(v2, v4, 5, 2)
    Edge(v4, v5, 5, 2)
    Edge(v1, v3, 10, 1)
    Edge(v3, v5, 10, 1)
    Edge(v1, v6, 3, 30)
    Edge(v6, v7, 3, 30)
    Edge(v7, v8, 3, 30)
    Edge(v8, v5, 3, 30)

    NormalDistribution.comparator = NormalDistribution.percentComparator(30)


    val g = new Graph(List(v1, v2, v3, v4, v5))
    g setStart v1
    g setFinish v5

    println("Edges:")
    println(g.vertexes.flatMap(_.edges).distinct.mkString("\n"))

    g.generateCheckpoints()

    println("\nResult of dividing into checkpoint lines:")
    println(g.checkpointLines.length)
    for (checkpointLine <- g.checkpointLines) println(checkpointLine)


    g.moveThroughTheGraph()

    println("\n\nResult of moving through the graph:")
    println("Vertexes:")
    println(g.vertexes mkString "\n")


    println("\n\nBest path:")
    println(g.bestPath mkString "\n")

  }
}
