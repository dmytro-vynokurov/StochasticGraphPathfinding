import integration.Integrator

/**
 * Created with IntelliJ IDEA.
 * User: Vinokurov
 * Date: 21.05.13
 * Time: 11:27
 * To change this template use File | Settings | File Templates.
 */
object TestRunner {
  def main(args: Array[String]) {
    println(Integrator.integrate(x => x * x, 0, 1))
  }
}
