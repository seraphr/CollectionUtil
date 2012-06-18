package jp.seraphr.collection.wrapper

import org.junit.Test
import org.scalatest.junit.JUnitSuite
import org.scalatest.prop.Checkers
import org.scalatest.Tracker
import org.junit.Assert._

import jp.seraphr.common.Tuple2

class MapWrapperTest extends MapWrapperTestJava with JUnitSuite with Checkers {
  import scala.collection.JavaConversions._
  import scala.collection.JavaConverters._
  import jp.seraphr.collection.TestUtils._
  import jp.seraphr.collection.wrapper.MapWrapper

  @Test
  def testMap: Unit = {
    check((aMap: Map[Int, Int]) => {
      val tWrapper = new MapWrapper(aMap)
      val tMapped = tWrapper.map((a: Tuple2[Int, Int]) => Tuple2.create(a.get1().toString(), a.get1() + a.get2()))

      assertEquals(aMap.size, tMapped.unwrap.size())

      aMap.forall {
        case (l, r) => {
          val tKey = l.toString()
          val tValue = l + r

          tMapped.unwrap.get(tKey) == tValue
        }
      }
    })
  }

  @Test
  def testZip: Unit = {
    def t[_L, _R](l: _L, r: _R) = Tuple2.create(l, r)

    check((aMap1: Map[Int, Int], aMap2: Map[String, String]) => {
      val tWrapper = new MapWrapper(aMap1)
      val tZipped = tWrapper.zip(aMap2.asJava)

      val tZ = aMap1.zip(aMap2)
      val tExpected = tZ.map({
        case ((ll, lr), (rl, rr)) => (t(ll, lr), t(rl, rr))
      }).toMap.asJava

      assertEquals(tExpected, tZipped.unwrap)
      true
    })
  }
}