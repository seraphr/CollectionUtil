package jp.seraphr.collection.builder
import jp.seraphr.collection.wrapper.Wrapper

trait WrapperBuilder[_Elem, _Wrapper <: Wrapper[_Elem, _Wrapper]] extends Builder[_Elem, _Wrapper] {

}