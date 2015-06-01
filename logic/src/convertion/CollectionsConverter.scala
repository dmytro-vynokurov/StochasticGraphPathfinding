package convertion

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

object CollectionsConverter {
  def toJavaList[T](list: List[T]): java.util.List[T] = {
    val buffer = ListBuffer(list: _*)
    val javaList: java.util.List[T] = bufferAsJavaList(buffer)
    javaList
  }
}
