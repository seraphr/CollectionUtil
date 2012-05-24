package jp.seraphr.collection.builder
import jp.seraphr.collection.wrapper.ListWrapper
import java.util.ArrayList

/**
 */
class ListWrapperBuilder[_Elem] extends WrapperBuilder[_Elem, ListWrapper[_Elem]] {
  import java.util.{ List => JList }

  private val mBase = new ArrayList[_Elem]

  override def add(aElement: _Elem): Unit = mBase.add(aElement)
  override def build(): ListWrapper[_Elem] = new ListWrapper(mBase)
}