package jp.seraphr.collection
import java.util.ArrayList
import org.junit.Assert._
import org.junit.Ignore
import org.junit.Test
import org.scalatest.junit.JUnitSuite
import org.scalatest.prop.Checkers
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import jp.seraphr.common.Tuple2
import jp.seraphr.common.Converter
import jp.seraphr.common.Option

class CollectionUtilTest extends JUnitSuite with Checkers with GeneratorDrivenPropertyChecks {
  import CollectionUtils._
  import scala.collection.JavaConversions._
  import scala.collection.JavaConverters._
  import jp.seraphr.collection.TestUtils._

  @Test
  def testMap: Unit = {
    check((aList: List[Int]) => {
      val tActual = map(aList, (a: Int) => a / 2)
      val tExpected = aList.map(a => a / 2).asJava

      assertEquals(tExpected, tActual)
      true
    })
  }

  @Test
  def testMap2: Unit = {
    check((aList: List[String]) => {
      val tActual = map[String, Int](aList, (a: String) => a.length())
      val tExpected = aList.map(a => a.length()).asJava

      assertEquals(tExpected, tActual)
      true
    })
  }

  @Test
  def testFilter: Unit = {
    check((aList: List[Int]) => {
      val tActual = filter(aList, (a: Int) => a % 2 == 0)
      val tExpected = aList.filter(a => a % 2 == 0).asJava

      assertEquals(tExpected, tActual)
      true
    })
  }

  @Test
  def testFilterNot: Unit = {
    check((aList: List[Int]) => {
      val tActual = filterNot(aList, (a: Int) => a % 2 == 0)
      val tExpected = aList.filterNot(a => a % 2 == 0).asJava

      assertEquals(tExpected, tActual)
      true
    })
  }

  @Test
  def testCollect: Unit = {
    check((aList: List[Int]) => {
      val tConv: Converter[Int, Option[Int]] = (a: Int) => if (a % 2 == 0) Option.none[Int] else Option.some(a * 2)
      val tActual = collect(aList, tConv)

      tActual.forall(a => a % 2 == 0 && (a / 2) % 2 != 0)
    })
  }

  @Test
  def testFind: Unit = {
    import org.scalacheck.Gen

    val tGen = Gen.choose(-100000, 100000)
    forAll(Gen listOf tGen) {
      (aList: List[Int]) =>
        {
          val tList = aList.map(a => a * 2)
          assertNull(find(tList, (a: Int) => a % 2 != 0).getOrNull)
          true
        }
    }

    check((aList: List[Int], aSearch: Int) => {

      val tList = (aSearch :: aList).reverse
      assertNotNull(find(tList, (a: Int) => a == aSearch))

      true
    })
  }

  @Test
  def testFindIndex: Unit = {
    check((aList: List[Int]) => {
      val tList = aList.zipWithIndex
      val tJavaList = aList.asJava
      for ((v, i) <- tList) {
        val tFindIndex = findIndex(aList, ((a: Int) => a == v))
        assertEquals(tJavaList.get(i), tJavaList.get(tFindIndex))
      }

      true
    })
  }

  @Test
  def testContains: Unit = {
    check((aList: List[Int]) => {
      for (v <- aList) {
        assertTrue(contains(aList, (a: Int) => a == v))
        assertFalse(contains(aList.remove(a => a == v), (a: Int) => a == v))
      }

      true
    })

    import org.scalacheck.Gen
    val tGen = Gen listOf Gen.choose(0, Int.MaxValue)
    forAll(tGen) {
      (aList: List[Int]) =>
        {
          for (v <- aList) {
            assertFalse(contains(aList, (a: Int) => a < 0))
          }

          true
        }
    }
  }

  @Test
  def testContains2: Unit = {
    check((aList: List[Int]) => {
      for (v <- aList.map(_.toString(): AnyRef)) {
        assertFalse(contains(aList, (a: Int) => a == v))
        assertTrue(contains(aList, v, (a: Int, v: AnyRef) => a.toString == v))
      }

      true
    })
  }

  @Test
  def testForAll: Unit = {
    import org.scalacheck.Gen
    val tGen = Gen listOf Gen.choose(Int.MinValue / 2, Int.MaxValue / 2).map(_ * 2)
    forAll(tGen) {
      (aList: List[Int]) =>
        {
          CollectionUtils.forAll(aList.asJava, (a: Int) => a % 2 == 0)
        }
    }
    forAll(tGen) {
      (aList: List[Int]) =>
        {
          !CollectionUtils.forAll((1 :: aList).asJava, (a: Int) => a % 2 == 0)
        }
    }
  }

  @Test
  def testFoldLeft: Unit = {
    check((aList: List[Int], aInit: Short) => {
      val tExpected = aList.foldLeft(aInit.asInstanceOf[Int])(_ + _)
      val tActual = foldLeft(aList, aInit.asInstanceOf[Int], (a1: Int, a2: Int) => a1 + a2)

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
          val tActual = reduceLeft(aList, (a1: Int, a2: Int) => a1 + a2)
          assertEquals(tExpected, tActual)
        }
    }
  }

  @Test
  def testReduceLeftAtEmpty: Unit = {
    try {
      reduceLeft(new ArrayList[String], (a1: String, a2: String) => a2)
      fail()
    } catch {
      case _ =>
    }
  }

  @Test
  def testFlatten: Unit = {
    check((aList1: List[String], aList2: List[String]) => {
      val tFlatten = flatten[String, java.util.List[String]](List(aList1.asJava, aList2.asJava).asJava)

      val tExpected = (aList1 ++ aList2).asJava
      assertEquals(tExpected, tFlatten)

      true
    })
  }

  @Test
  def testZip: Unit = {
    check((aList1: List[String], aList2: List[String]) => {
      val tZipped = zip(aList1, aList2)
      val tExpedted = (aList1 zip aList2).map(tupleToTuple).asJava

      assertEquals(tExpedted, tZipped)

      true
    })
  }

  @Test
  def testZipWithIndex: Unit = {
    check((aList: List[String]) => {
      val tZipped = zipWithIndex(aList)
      val tExpected = aList.zipWithIndex.map(tupleToTuple).asJava

      assertEquals(tExpected, tZipped)
      true
    })
  }

  @Test
  def testZip2: Unit = {
    check((aList1: List[String]) => {

      val tC1: Converter[String, Int] = (a: String) => a.length()
      val tC2: Converter[String, String] = (a: String) => a.take(1)

      val tZipped = zip[String, Int, String](aList1, tC1, tC2)
      val tExpedted = zip(map[String, Int](aList1, tC1), map(aList1, tC2))

      assertEquals(tExpedted, tZipped)

      true
    })
  }

  @Test
  def testUnzip: Unit = {
    check((aList1: List[String], aList2: List[String]) => {
      val tZipped = zip(aList1, aList2)
      val tUnzipped = unzip(tZipped)

      val (tE1, tE2) = (aList1 zip aList2).unzip

      assertEquals(tE1.asJava, tUnzipped.get1())
      assertEquals(tE2.asJava, tUnzipped.get2())

      true
    })
  }

  @Test
  def testUnzip1: Unit = {
    check((aList1: List[String], aList2: List[String]) => {
      val tZipped = zip(aList1, aList2)
      val tUnzipped = unzip1(tZipped)

      val (tE1, _) = (aList1 zip aList2).unzip

      assertEquals(tE1.asJava, tUnzipped)

      true
    })
  }

  @Test
  def testUnzip2: Unit = {
    check((aList1: List[String], aList2: List[String]) => {
      val tZipped = zip(aList1, aList2)
      val tUnzipped = unzip2(tZipped)

      val (_, tE2) = (aList1 zip aList2).unzip

      assertEquals(tE2.asJava, tUnzipped)

      true
    })
  }

  @Test
  def testFindElement1: Unit = {
    import org.scalacheck.Gen

    val tIntGen = Gen.choose(0, 1000)
    val tTupleGen = for (l <- tIntGen; r <- tIntGen) yield (l, r)
    val tGen = Gen.listOf(tTupleGen)

    forAll(tGen) {
      (aList: List[(Int, Int)]) =>
        {
          val tTestList = (0 to 1000).toList
          val tZipped = aList.map(tupleToTuple)
          val (tLeftList, _) = aList.unzip

          for (tValue <- tTestList) {
            val tFind = findElement1(tZipped, tValue)
            if (tFind.isSome) {
              assertEquals(tValue, tFind.getOrNull.get1())
            } else {
              assertFalse(tLeftList.contains(tValue))
            }

          }
          true
        }
    }
  }

  @Test
  def testFindElement2: Unit = {
    import org.scalacheck.Gen

    val tIntGen = Gen.choose(0, 1000)
    val tTupleGen = for (l <- tIntGen; r <- tIntGen) yield (l, r)
    val tGen = Gen.listOf(tTupleGen)

    forAll(tGen) {
      (aList: List[(Int, Int)]) =>
        {
          val tTestList = (0 to 1000).toList
          val tZipped = aList.map(tupleToTuple)
          val (_, tRightList) = aList.unzip

          for (tValue <- tTestList) {
            val tFind = findElement2(tZipped, tValue)
            if (tFind.isSome) {
              assertEquals(tValue, tFind.getOrNull.get2())
            } else {
              assertFalse(tRightList.contains(tValue))
            }

          }
          true
        }
    }
  }

  @Test
  def testMkString: Unit ={
    check((aList: List[String]) => {
      val tExpected = aList.mkString("[[", ", ", "]]")
      val tActual = mkString(aList.asJava, "[[", ", ", "]]")
      assertEquals(tExpected, tActual)

      true
    })
  }

  @Test
  def testAppendString: Unit ={
    check((aList: List[String]) => {
      val tExpected = "prefix " + aList.mkString("[[", ", ", "]]")

      val tStringBuilder = new java.lang.StringBuilder
      tStringBuilder.append("prefix ")
      val tActual = appendString(tStringBuilder, aList.asJava, "[[", ", ", "]]").toString()
      assertEquals(tExpected, tActual)

      true
    })
  }

}