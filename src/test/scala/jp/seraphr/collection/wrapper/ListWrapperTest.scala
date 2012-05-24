package jp.seraphr.collection.wrapper
import org.scalatest.junit.JUnitSuite
import org.junit.Test
import org.scalatest.prop.Checkers
import jp.seraphr.collection.Converter
import org.junit.Ignore

class ListWrapperTest extends JUnitSuite with Checkers {
  import scala.collection.JavaConversions._
  import scala.collection.JavaConverters._

  @Ignore
  @Test
  def testMap: Unit = {
    check((aList: List[Int]) => {
      val tWrapper = new ListWrapper(aList)
      val tMapped = tWrapper.map((a: Int) => a * 2)

      tMapped.unwrap.asScala.forall(_ % 2 == 0)
    })
  }

  private implicit def funcToConvertor[_F, _T](aFunc: _F => _T): Converter[_F, _T] = {
    return new Converter[_F, _T] {
      override def convert(aFrom: _F): _T = aFunc(aFrom)
    }
  }
}