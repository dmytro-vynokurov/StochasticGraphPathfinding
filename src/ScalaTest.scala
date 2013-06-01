import convertion.CollectionsConverter


object ScalaTest {
  def main(args: Array[String]) {
    val a = List(1, 2, 5, 4, 6)
    println(CollectionsConverter.toJavaList(a).getClass())
    print(CollectionsConverter.toJavaList(a))
    scala.collection.convert.Wrappers.MutableBufferWrapper
  }
}
