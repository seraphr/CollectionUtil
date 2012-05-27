package jp.seraphr.collection.wrapper

import jp.seraphr.collection.builder.WrapperBuilder
import java.util.List
import jp.seraphr.collection.builder.ListWrapperBuilder
import jp.seraphr.collection.Converter
import java.util.Collections

class ListWrapper[_Elem](aBase: List[_Elem]) extends Wrapper[_Elem] {
  type _Container[X] = List[X]
  type _Base = List[_Elem]

  import java.util.{ List => JList }

  def map[_ToElem](aConvertor: Converter[_Elem, _ToElem]): ListWrapper[_ToElem] = {
    mapInner(aConvertor)(new ListWrapperBuilder[_ToElem])
  }

  override val unwrap = aBase

  override protected def toIterable(aBase: _Base): _Container[_Elem] = unwrap

}