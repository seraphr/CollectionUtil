package jp.seraphr.collection.wrapper
import org.scalatest.junit.JUnitSuite
import org.scalatest.prop.Checkers
import org.junit.Test
import org.junit.Assert._
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class WrapperTest extends JUnitSuite with Checkers with GeneratorDrivenPropertyChecks {
  import scala.collection.JavaConversions._
  import scala.collection.JavaConverters._
  import jp.seraphr.collection.TestUtils._

  @Test
  def testFilter: Unit = {
    check((aList: List[Int]) => {
      val tWrapper = new ListWrapper(aList)
      val tFiltered = tWrapper.filter((a: Int) => a % 3 == 2)

      tFiltered.unwrap.asScala.forall(_ % 3 == 2)
    })
  }

  @Test
  def testFilterNot: Unit = {
    check((aList: List[Int]) => {
      val tWrapper = new ListWrapper(aList)
      val tFiltered = tWrapper.filterNot((a: Int) => a % 3 == 1)

      tFiltered.unwrap.asScala.forall(_ % 3 != 1)
    })
  }

  @Test
  def testfoldLeft: Unit = {
    check((aList: List[Int], aInit: Short) => {
      val tWrapper = new ListWrapper(aList)
      val tActual = tWrapper.foldLeft(aInit.asInstanceOf[Int], (a1: Int, a2: Int) => a1 + a2)
      val tExpected = aList.foldLeft(aInit.asInstanceOf[Int])(_ + _)

      assertEquals(tExpected, tActual)
      true
    })
  }

  @Test
  def testReduceLeft: Unit = {
    import org.scalacheck.Gen

    val tGen = Gen.choose(-100000, 100000)
    val tListGen = (Gen listOf tGen)

    forAll(tListGen.filter(!_.isEmpty)) {
      (aList: List[Int]) =>
        {
          val tExpected = aList.reduceLeft(_ + _)
          val tWrapper = new ListWrapper(aList)
          val tActual = tWrapper.reduceLeft((a: Int, b: Int) => a + b)
          assertEquals(tExpected, tActual)
          true
        }
    }
  }
}