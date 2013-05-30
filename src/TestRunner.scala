import _root_.integration.Integrator


object TestRunner {
  def main(args: Array[String]) {
    println(Integrator.integrate(x => x * x, 0, 1))
  }
}
