package jp.seraphr.collection.wrapper
import org.scalatest.junit.JUnitSuite
import org.scalatest.prop.Checkers
import org.junit.Test

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
}