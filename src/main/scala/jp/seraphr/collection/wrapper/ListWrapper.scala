package jp.seraphr.collection.wrapper

import java.util.List
import java.lang.Iterable
import jp.seraphr.collection.builder.ListWrapperBuilder
import jp.seraphr.common.Converter
import jp.seraphr.common.Tuple2

class ListWrapper[_Elem](aBase: List[_Elem]) extends Wrapper[_Elem, ListWrapper[_Elem]] {
  type _Container[X] = List[X]
  type _Base = List[_Elem]
  type _This = ListWrapper[_Elem]

  def map[_ToElem](aConverter: Converter[_Elem, _ToElem]): ListWrapper[_ToElem] = {
    mapInner(aConverter)(new ListWrapperBuilder[_ToElem])
  }

  def zip[_ThatElem](aThat: Iterable[_ThatElem]): ListWrapper[Tuple2[_Elem, _ThatElem]] = {
    zipInner(aThat)(new ListWrapperBuilder[Tuple2[_Elem, _ThatElem]])
  }

  override protected def myBuilder = new ListWrapperBuilder[_Elem]

  override val unwrap = aBase

  override protected def toIterable(aBase: _Base): _Container[_Elem] = {
//    require(aBase != null)
    aBase
  }

}