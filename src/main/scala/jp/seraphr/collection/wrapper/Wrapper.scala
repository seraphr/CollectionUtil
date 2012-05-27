package jp.seraphr.collection.wrapper
import jp.seraphr.collection.builder.WrapperBuilder
import jp.seraphr.collection.Converter
import jp.seraphr.collection.CollectionUtils

trait Wrapper[_Elem] {
    type _Container[X] <: java.lang.Iterable[X]
    type _Base

    protected def mapInner[_ToElem, _To <: Wrapper[_ToElem]](aConverter: Converter[_Elem, _ToElem])(aBuilder: WrapperBuilder[_ToElem, _To]): _To = {
        val tBuilder: WrapperBuilder[_ToElem, _To] = aBuilder

        CollectionUtils.map(toIterable(unwrap), tBuilder, aConverter)
    }

    protected def toIterable(aBase: _Base): _Container[_Elem]

    val unwrap: _Base
}