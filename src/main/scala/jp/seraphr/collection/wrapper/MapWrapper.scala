package jp.seraphr.collection.wrapper

import jp.seraphr.collection.Converter
import jp.seraphr.collection.builder.WrapperBuilder
import java.util.{ Map => JMap }
import jp.seraphr.collection.builder.MapWrapperBuilder

class MapWrapper[_Key, _Value](aBase: JMap[_Key, _Value]) extends Wrapper[JMap.Entry[_Key, _Value]] {
  type _Container[X] = java.util.Set[X]
  type _Base = JMap[_Key, _Value]
  type _Conv[X, Y] = Converter[JMap.Entry[_Key, _Value], JMap.Entry[X, Y]]

  def map[_ToKey, _ToElem](aConverter: _Conv[_ToKey, _ToElem]): MapWrapper[_ToKey, _ToElem] = {
    mapInner(aConverter)(new MapWrapperBuilder[_ToKey, _ToElem])
  }

  override val unwrap = aBase

  def toIterable(aBase: _Base): _Container[JMap.Entry[_Key, _Value]] = unwrap.entrySet()
}