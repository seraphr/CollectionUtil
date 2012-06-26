package jp.seraphr.collection.wrapper

import jp.seraphr.collection.builder.WrapperBuilder
import jp.seraphr.collection.builder.MapWrapperBuilder
import jp.seraphr.collection.builder.WrapperBuilder
import jp.seraphr.common.Converter
import java.util.Map
import java.lang.Iterable
import jp.seraphr.common.Tuple2
import jp.seraphr.collection.CollectionUtils
import jp.seraphr.collection.builder.ListBuilder

class MapWrapper[_Key, _Value](aBase: Map[_Key, _Value]) extends Wrapper[Tuple2[_Key, _Value], MapWrapper[_Key, _Value]] {

  type _Container[X] = java.lang.Iterable[X]
  type _Base = Map[_Key, _Value]
  type _Conv[X, Y] = Converter[Tuple2[_Key, _Value], Tuple2[X, Y]]
  type _This = MapWrapper[_Key, _Value]

  def map[_ToKey, _ToElem](aConverter: _Conv[_ToKey, _ToElem]): MapWrapper[_ToKey, _ToElem] = {
    mapInner(aConverter)(new MapWrapperBuilder[_ToKey, _ToElem])
  }

  def zip[_ThatKey, _ThatValue](aThat: Map[_ThatKey, _ThatValue]): MapWrapper[Tuple2[_Key, _Value], Tuple2[_ThatKey, _ThatValue]] = {
    zip(toTupleIterable(aThat))
  }

  def zip[_ThatElem](aThat: Iterable[_ThatElem]): MapWrapper[Tuple2[_Key, _Value], _ThatElem] = {
    zipInner(aThat)(new MapWrapperBuilder[Tuple2[_Key, _Value], _ThatElem])
  }

  override protected def myBuilder = new MapWrapperBuilder[_Key, _Value]

  override val unwrap = aBase

  def toIterable(aBase: _Base): _Container[Tuple2[_Key, _Value]] = toTupleIterable(unwrap)

  private def toTupleIterable[_K, _V](aBase: Map[_K, _V]) = {
   CollectionUtils.map(aBase.entrySet(), new ListBuilder[Tuple2[_K, _V]], new Converter[Map.Entry[_K, _V], Tuple2[_K, _V]](){
     override def convert(aSource: Map.Entry[_K, _V]) = Tuple2.create(aSource.getKey, aSource.getValue)
   })
  }
}