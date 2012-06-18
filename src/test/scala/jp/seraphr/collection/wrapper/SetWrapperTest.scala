package jp.seraphr.collection.wrapper
import org.scalatest.junit.JUnitSuite
import org.scalatest.prop.Checkers
import org.junit.Test
import org.junit.Assert._
import jp.seraphr.common.Tuple2


class SetWrapperTest extends JUnitSuite with Checkers {
  import scala.collection.JavaConversions._
  import scala.collection.JavaConverters._
  import jp.seraphr.collection.TestUtils._
  import jp.seraphr.collection.wrapper.SetWrapper

  @Test
  def testMap: Unit = {
    check((aList: Set[Int]) => {
      val tWrapper = new SetWrapper(aList)
      val tMapped = tWrapper.map((a: Int) => a * 2)

      tMapped.unwrap.asScala.forall(_ % 2 == 0)
    })
  }

  @Test
  def testZip: Unit = {
    check((aList: Set[Int], aList2: Set[String]) => {
      val tWrapper = new SetWrapper(aList)
      val tZipped = tWrapper.zip(aList2)

      val tExpected = for((l, r) <- aList.zip(aList2)) yield Tuple2.create(l, r)

      assertEquals(tExpected.asJava, tZipped.unwrap)
      true
    })
  }
}