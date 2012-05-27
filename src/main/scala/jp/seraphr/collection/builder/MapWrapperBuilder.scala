package jp.seraphr.collection.builder

import java.util.{ Map => JMap }
import jp.seraphr.collection.wrapper.MapWrapper
import java.util.HashMap

class MapWrapperBuilder[_Key, _Value] extends WrapperBuilder[JMap.Entry[_Key, _Value], MapWrapper[_Key, _Value]] {
  private val mBase = new HashMap[_Key, _Value]

  override def add(aElement: JMap.Entry[_Key, _Value]): Unit = mBase.put(aElement.getKey(), aElement.getValue())
  override def build(): MapWrapper[_Key, _Value] = new MapWrapper(mBase)
}