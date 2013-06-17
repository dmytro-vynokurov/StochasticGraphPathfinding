import _root_.graph.{Graph, Edge, Vertex}
import _root_.stochastic.NormalDistribution
import integration.Integrator
import scala.util.Random

object ModelRunner {

  def modelMultipleTravels(n:NormalDistribution,f:Double=>Double,number:Int){
    var totalProfit:Double=0
    val rand=new Random
    var randomTime:Double=0

    for(i<-1 to number){
      randomTime=rand.nextGaussian()*n.s2+n.m
      totalProfit+=f(randomTime)
    }

    print("Total profit is :\t")
    println(totalProfit)
  }


  def main(args: Array[String]) {
    val v1: Vertex = new Vertex("1")
    val v2: Vertex = new Vertex("2")
    val v3: Vertex = new Vertex("3")
    val v4: Vertex = new Vertex("4")
    val v5: Vertex = new Vertex("5")
    val v6: Vertex = new Vertex("6")
    val v7: Vertex = new Vertex("7")
    val v8: Vertex = new Vertex("8")
    val v9: Vertex = new Vertex("9")
    val v10: Vertex = new Vertex("10")

    Edge(v1, v2, 5.0,20.0)
    Edge(v1, v3, 6.0,3.0)
    Edge(v1, v4, 5.0,1.0)
    Edge(v2, v5, 6.0,25.0)
    Edge(v3, v5, 6.0,10.0)
    Edge(v4, v6, 11.0,1.0)
    Edge(v5, v7, 8.0,10.0)
    Edge(v5, v8, 7.0,8.0)
    Edge(v6, v8, 10.0,12.0)
    Edge(v6, v9, 3.0,1.0)
    Edge(v7, v10, 3.0,6.0)
    Edge(v8, v10, 3.0,6.0)
    Edge(v9, v10, 4.0,2.0)

//    NormalDistribution.comparator = NormalDistribution.percentComparator(30)
    NormalDistribution.comparator = NormalDistribution.profitComparator(profitFunction)

    def profitFunction(x:Double)={
      if(x<28) 100
      else 0
    }


    val g = new Graph(List(v1, v2, v3, v4,v5,v6,v7,v8,v9,v10))
    g setStart v1
    g setFinish v10

    println(g.start)
    println(g.finish)

    println("Edges:")
    println(g.vertexes.flatMap(_.edges).distinct.mkString("\n"))

    g.generateCheckpoints()

    println("\nResult of dividing into checkpoint lines:")
    println(g.checkpointLines.length)
    for (checkpointLine <- g.checkpointLines) println(checkpointLine)
    println(g.checkpointLines)

    println("\n\nResult of moving through the graph:")
    println("Vertexes:")
    println(g.vertexes.map(_.fullInfo) mkString "\n")

    println("\n\nBest path:")
    println(g.bestPath mkString "\n")

    val bestPathWeight=g.bestPathLength(g.bestPath)

    println("\n\nBest path length:")
    println(bestPathWeight)

    modelMultipleTravels(bestPathWeight,profitFunction,10)

    val lengthThrough2=g.bestPathLength(List(v1,v2,v5,v8,v10))
    val lengthThrough3=g.bestPathLength(List(v1,v3,v5,v8,v10))
    val lengthThrough4=g.bestPathLength(List(v1,v4,v6,v9,v10))

    println(lengthThrough2)
    println(lengthThrough3)
    println(lengthThrough4)

    println("Through2\t"+ Integrator.integrate(x => profitFunction(x) * lengthThrough2.density(x), lengthThrough2.leftBorder(), lengthThrough2.rightBorder()))
    println("Through3\t"+ Integrator.integrate(x => profitFunction(x) * lengthThrough3.density(x), lengthThrough3.leftBorder(), lengthThrough3.rightBorder()))
    println("Through4\t"+ Integrator.integrate(x => profitFunction(x) * lengthThrough4.density(x), lengthThrough4.leftBorder(), lengthThrough4.rightBorder()))

  }
}
