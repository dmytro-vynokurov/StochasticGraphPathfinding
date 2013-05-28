package stochastic

/**
 * Created with IntelliJ IDEA.
 * User: Vinokurov
 * Date: 12.05.13
 * Time: 15:47
 * To change this template use File | Settings | File Templates.
 */

import math._
import integration.Integrator

class NormalDistribution(expectation: Double, sigmaSquare: Double)
  extends AnyRef with Ordered[NormalDistribution] {

  val m = expectation
  val s2 = sigmaSquare
  val s = math.sqrt(s2)

  def density(x: Double): Double = {
    1.0 / sqrt(Pi * 2.0 * s2) * exp(-1.0 * pow(x - m, 2) / 2 / s2)
  }

  def leftBorder(numberOfSigmas: Int = 3): Double = {
    return m - numberOfSigmas * s
  }

  def rightBorder(numberOfSigmas: Int = 3): Double = {
    return m + numberOfSigmas * s
  }

  def +(other: NormalDistribution) = new NormalDistribution(this.m + other.m, this.s2 + other.s2)

  override def toString = "N( " + m.toString + " , " + s2.toString + " )"

  def compare(that: NormalDistribution): Int = {
    if (this == NilDistribution) return 1
    if (that == NilDistribution) return -1
    val result = NormalDistribution.comparator(this, that)
    if (result) return 1
    else return -1
  }
}

object NormalDistribution {
  var comparator = determinedComparator

  def determinedComparator: (NormalDistribution, NormalDistribution) => Boolean =
    (a, b) => (a.m > b.m)

  def percentComparator(percent: Int): (NormalDistribution, NormalDistribution) => Boolean =

    (a: NormalDistribution, b: NormalDistribution) => {
      val coefficient = 1 + percent.asInstanceOf[Double] / 100.

      if ((a.m < b.m) && (a.s2 < b.s2)) false
      else if ((a.m > b.m) && (a.s2 > b.s2)) true
      else if ((a.m < b.m) && (a.m * (coefficient) > b.m)) true
      else if ((a.m > b.m) && (b.m * (coefficient) > a.m)) false
      else if (a.m < b.m) false
      else true
    }

  def profitComparator(profitFunction: Double => Double): (NormalDistribution, NormalDistribution) => Boolean =
    (a: NormalDistribution, b: NormalDistribution) => {
      val profitByA = Integrator.integrate(x => profitFunction(x) * a.density(x), a.leftBorder(), a.rightBorder())
      val profitByB = Integrator.integrate(x => profitFunction(x) * b.density(x), b.leftBorder(), b.rightBorder())
      if (profitByA > profitByB) false
      else true
    }

}

object NilDistribution extends NormalDistribution(0, 0) {
  override def density(x: Double) = 0

  override def toString = "Nil Distribution"
}
