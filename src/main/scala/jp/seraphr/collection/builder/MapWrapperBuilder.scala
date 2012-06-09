package jp.seraphr.collection.builder

import java.util.{ Map => JMap }
import jp.seraphr.collection.wrapper.MapWrapper
import java.util.HashMap
import jp.seraphr.common.Tuple2

class MapWrapperBuilder[_Key, _Value] extends WrapperBuilder[Tuple2[_Key, _Value], MapWrapper[_Key, _Value]] {
  private val mBase = new HashMap[_Key, _Value]

  override def add(aElement: Tuple2[_Key, _Value]): Unit = mBase.put(aElement.get1(), aElement.get2())
  override def build(): MapWrapper[_Key, _Value] = new MapWrapper(mBase)
}