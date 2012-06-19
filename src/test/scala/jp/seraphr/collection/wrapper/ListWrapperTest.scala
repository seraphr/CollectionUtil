package jp.seraphr.collection.wrapper
import org.scalatest.junit.JUnitSuite
import org.junit.Test
import org.junit.Assert._
import org.scalatest.prop.Checkers
import jp.seraphr.common.Tuple2

class ListWrapperTest extends JUnitSuite with Checkers {
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

  @Test
  def testZip: Unit = {
    check((aList: List[Int], aList2: List[String]) => {
      val tWrapper = new ListWrapper(aList)
      val tZipped = tWrapper.zip(aList2)

      val tExpected = for((l, r) <- aList.zip(aList2)) yield Tuple2.create(l, r)

      assertEquals(tExpected.asJava, tZipped.unwrap)
      true
    })
  }
}