package jp.seraphr.collection.wrapper

import jp.seraphr.collection.builder.WrapperBuilder
import java.util.List
import jp.seraphr.collection.builder.ListWrapperBuilder
import jp.seraphr.collection.Converter
import java.util.Collections

class ListWrapper[_Elem](aBase: List[_Elem]) extends Wrapper[_Elem] {
  type _Container[X] = List[X]
  type _Base = List[_Elem]
  type _This[X] = ListWrapper[X]

  import java.util.{ List => JList }

  override def map[_ToElem](aConvertor: Converter[_Elem, _ToElem]): _This[_ToElem] = {
    val tDummy: JList[_ToElem] = Collections.emptyList()
    mapInner(aConvertor)(tDummy)
  }

  override val unwrap = aBase

  override protected def toIterable(aBase: _Base): _Container[_Elem] = unwrap

  override protected def builder[_ToElem]: WrapperBuilder[_ToElem, _This[_ToElem]] = {
    new ListWrapperBuilder[_ToElem]
  }

}