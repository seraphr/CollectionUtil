package jp.seraphr.collection.wrapper
import java.util.Set
import java.lang.Iterable
import jp.seraphr.collection.builder.SetWrapperBuilder
import jp.seraphr.common.Converter
import jp.seraphr.common.Tuple2

class SetWrapper[_Elem](aBase: Set[_Elem]) extends Wrapper[_Elem] {
  type _Container[X] = Set[X]
  type _Base = Set[_Elem]
  type _This = SetWrapper[_Elem]

  def map[_ToElem](aConverter: Converter[_Elem, _ToElem]): SetWrapper[_ToElem] = {
    mapInner(aConverter)(new SetWrapperBuilder[_ToElem])
  }

  def zip[_ThatElem](aThat: Iterable[_ThatElem]): SetWrapper[Tuple2[_Elem, _ThatElem]] = {
    zipInner(aThat)(new SetWrapperBuilder[Tuple2[_Elem, _ThatElem]])
  }

  override protected def myBuilder = new SetWrapperBuilder[_Elem]

  override val unwrap = aBase

  override protected def toIterable(aBase: _Base): _Container[_Elem] = aBase
}