package convertion

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

/**
 * Created with IntelliJ IDEA.
 * User: Vinokurov
 * Date: 31.05.13
 * Time: 11:39
 * To change this template use File | Settings | File Templates.
 */
object CollectionsConverter {
  def toJavaList[T](list: List[T]): java.util.List[T] = {
    val buffer = ListBuffer(list: _*)
    val javaList: java.util.List[T] = bufferAsJavaList(buffer)
    javaList
  }
}
