package jp.seraphr.collection.wrapper

import jp.seraphr.collection.builder.WrapperBuilder
import java.util.List
import jp.seraphr.collection.builder.ListWrapperBuilder

class ListWrapper2[_Elem](aBase: List[_Elem]) extends Wrapper[_Elem] {
  type _Container[X] = List[X]
  type _Base = List[_Elem]

  override val unwrap = aBase

  override protected def toIterable(aBase: _Base): _Container[_Elem] = unwrap

  /**
   * TODO このあたりから
   */
  override protected def builder[_ToElem, _To <: Wrapper[_ToElem]]: WrapperBuilder[_ToElem, _To] = {
    //new ListWrapperBuilder[_ToElem]
    throw new RuntimeException
  }

}