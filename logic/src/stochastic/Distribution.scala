package stochastic

import math._
import integration.Integrator
import scala.beans.BeanProperty

sealed trait AbstractDistribution[T<:AbstractDistribution[T]] extends Ordered[T]{
  def m(): Double
  def s2(): Double
  def +(distribution: AbstractDistribution[_]): T
  def density(x:Double):Double
  def leftBorder(): Double
  def rightBorder(): Double
}

class Distribution(_density:Double=>Double,_leftBorder: Double, _rightBorder:Double, _name: String) extends AbstractDistribution[Distribution]{
  val expectation = Integrator.integrate(_density,_leftBorder,_rightBorder)
  override def m(): Double = expectation

  lazy val variance = Integrator.integrate(x=>pow(x - expectation,2),leftBorder(),rightBorder())
  override def s2(): Double = variance

  override def +(that: AbstractDistribution[_]): Distribution = that match{
    case nil:NilDistribution => this
    case _ =>
      val density: (Double) => Double = y => Integrator.integrate(x => this.density(x) * that.density(y-x),leftBorder,rightBorder)
      new Distribution(density,leftBorder,rightBorder, s"$this+$that")
  }

  override def compare(that: Distribution): Int = if (DistributionComparisons.comparator(this, that)) 1 else -1

  override def density(x: Double): Double = _density(x)

  override def leftBorder(): Double = _leftBorder

  override def rightBorder(): Double = _rightBorder

  override def toString: String = _name
}

class NormalDistribution(expectation: Double, sigmaSquare: Double) extends AbstractDistribution[NormalDistribution] {

  val m = expectation
  val s2 = sigmaSquare
  val s = math.sqrt(s2)

  def density(x: Double): Double = {
    1.0 / sqrt(Pi * 2.0 * s2) * exp(-1.0 * pow(x - m, 2) / 2 / s2)
  }

  def leftBorder(numberOfSigmas: Int): Double = m - numberOfSigmas * s

  def rightBorder(numberOfSigmas: Int): Double = m + numberOfSigmas * s

  def +(other: AbstractDistribution[_]) = other match{
    case nil: NilDistribution => this
    case normal: NormalDistribution => new NormalDistribution(this.m + normal.m, this.s2 + normal.s2)
    case _ => throw new IllegalArgumentException("Normal distribution can be added to normal distribution only")
  }

  override def toString = "N( " + m.toString + " , " + s2.toString + " )"

  def compare(that: NormalDistribution): Int = (this,that) match {
    case (n1:NilDistribution,n2:NilDistribution)=> 0
    case (n1:NilDistribution,n2:NormalDistribution)=> 1
    case (n1:NormalDistribution,n2:NilDistribution)=> -1
    case (n1:NormalDistribution,n2:NormalDistribution)=> if (DistributionComparisons.comparator(this, that)) 1 else -1
  }

  override def leftBorder(): Double = leftBorder(3)

  override def rightBorder(): Double = rightBorder(3)
}

object DistributionComparisons {
  @BeanProperty
  var comparator = determinedComparator

  def determinedComparator: (AbstractDistribution[_], AbstractDistribution[_]) => Boolean =
    (a, b) => a.m > b.m

  def percentComparator(percent: Int): (AbstractDistribution[_], AbstractDistribution[_]) => Boolean =
    (a: AbstractDistribution[_], b: AbstractDistribution[_]) => {
      val coefficient = 1 + percent.asInstanceOf[Double] / 100.0

      if ((a.m < b.m) && (a.s2 < b.s2)) false
      else if ((a.m > b.m) && (a.s2 > b.s2)) true
      else if ((a.m < b.m) && (a.m * coefficient > b.m)) true
      else if ((a.m > b.m) && (b.m * coefficient > a.m)) false
      else if (a.m < b.m) false
      else true
    }

  def profitComparator(profitFunction: Double => Double): (AbstractDistribution[_], AbstractDistribution[_]) => Boolean =
    (a: AbstractDistribution[_], b: AbstractDistribution[_]) => {
      val profitByA = Integrator.integrate(x => profitFunction(x) * a.density(x), a.leftBorder(), a.rightBorder())
      val profitByB = Integrator.integrate(x => profitFunction(x) * b.density(x), b.leftBorder(), b.rightBorder())
      println("Profit by "+ a + profitByA)
      println("Profit by "+ b + profitByB)
      if (profitByA > profitByB) {
        println("Selected first")
        false
      }
      else {
        println("Selected second")
        true
      }
    }


  def oneZeroFunction(changePoint:Double)(x:Double): Double={
    if (x<=changePoint) 1
    else 0
  }

  def stepProfitComparator(changePoint:Double):(AbstractDistribution[_], AbstractDistribution[_]) => Boolean=
    profitComparator(oneZeroFunction(changePoint))

}

class NilDistribution extends NormalDistribution(0, 0) {
  override def density(x: Double) = 0

  override def toString = "Nil Distribution"
}
