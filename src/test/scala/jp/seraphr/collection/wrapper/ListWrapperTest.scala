package jp.seraphr.collection.wrapper
import org.scalatest.junit.JUnitSuite
import org.junit.Test
import org.scalatest.prop.Checkers
import org.junit.Ignore

class ListWrapperTest extends ListWrapperTestJava with JUnitSuite with Checkers {
  import scala.collection.JavaConversions._
  import scala.collection.JavaConverters._
  import jp.seraphr.collection.TestUtils._

  @Test
  def testMap: Unit = {
    check((aList: List[Int]) => {
      val tWrapper = new ListWrapper(aList)
      val tMapped = tWrapper.map((a: Int) => a * 2)

      tMapped.unwrap.asScala.forall(_ % 2 == 0)
    })
  }
}