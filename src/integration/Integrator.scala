package integration

object Integrator {
  private val MAX_STEPS = 30000

  def integrate(function: Double => Double, _from: Double, _to: Double, _precision: Double = 0.0001): Double = {
    val countFrom = math.min(_from, _to)
    val countTo = math.max(_from, _to)
    val (precision, steps) =
      if ((countTo - countFrom) / _precision > MAX_STEPS) ((countTo - countFrom) / MAX_STEPS, MAX_STEPS)
      else (_precision, ((countTo - countFrom) / _precision).asInstanceOf[Int])

    var x = countFrom
    var stepsMade: Int = 0
    var sum: Double = 0
    while (stepsMade < steps) {
      x += precision
      sum += precision * (function(x) + function(x - precision)) / 2.0
      stepsMade += 1
    }
    return sum
  }
}
