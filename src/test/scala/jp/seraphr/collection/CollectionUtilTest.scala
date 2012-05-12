package jp.seraphr.collection
import org.scalatest.junit.JUnitSuite
import org.junit.Assert._
import org.junit.Test
import org.scalatest.prop.Checkers
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class CollectionUtilTest extends JUnitSuite with Checkers with GeneratorDrivenPropertyChecks {
  import CollectionUtils._
  import scala.collection.JavaConversions._
  import scala.collection.JavaConverters._

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
  def testFilter: Unit = {
    check((aList: List[Int]) => {
      val tActual = filter(aList, (a: Int) => a % 2 == 0)
      val tExpected = aList.filter(a => a % 2 == 0).asJava

      assertEquals(tExpected, tActual)
      true
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
          assertNull(find(tList, (a: Int) => a % 2 != 0))
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

  private implicit def funcToConvertor[_F, _T](aFunc: _F => _T): Converter[_F, _T] = {
    return new Converter[_F, _T] {
      override def convert(aFrom: _F): _T = aFunc(aFrom)
    }
  }

  private implicit def funcToPredicate[_F](aFunc: _F => Boolean): Predicate[_F] = {
    return new Predicate[_F] {
      override def apply(aFrom: _F): Boolean = aFunc(aFrom)
    }
  }

  private implicit def funcToEquivalence[_F1, _F2](aFunc: (_F1, _F2) => Boolean): Equivalence[_F1, _F2] = {
    return new Equivalence[_F1, _F2] {
      override def apply(aLeft: _F1, aRight: _F2): Boolean = aFunc(aLeft, aRight)
    }
  }

  private implicit def tupleToTuple[_E1, _E2](aFrom: (_E1, _E2)): Tuple2[_E1, _E2] = aFrom match {
    case (tE1, tE2) => Tuple2.create(tE1, tE2)
  }
}