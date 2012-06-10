/**
 *
 */
package jp.seraphr.collection.builder
import jp.seraphr.collection.wrapper.SetWrapper
import java.util.HashSet

/**
 *
 */
class SetWrapperBuilder[_Elem] extends WrapperBuilder[_Elem, SetWrapper[_Elem]] {

  private val mBase = new HashSet[_Elem]

  override def add(aElement: _Elem): Unit = mBase.add(aElement)
  override def build(): SetWrapper[_Elem] = new SetWrapper(mBase)
}