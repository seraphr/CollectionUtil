package jp.seraphr.collection
import org.scalatest.junit.JUnitSuite
import org.junit.Assert._
import org.junit.Test
import org.scalatest.prop.Checkers

class CollectionUtilTest extends JUnitSuite with Checkers {

  @Test
  def t: Unit = {
    check((a: Int) => {
      true
    })
  }
}