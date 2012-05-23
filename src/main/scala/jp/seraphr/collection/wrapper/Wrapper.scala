package jp.seraphr.collection.wrapper
import jp.seraphr.collection.builder.WrapperBuilder
import jp.seraphr.collection.Converter
import jp.seraphr.collection.CollectionUtils

trait Wrapper[_Elem] {
    type _Container[X] <: java.lang.Iterable[X]
    type _Base

    type To[From, To] = Converter[From, To]

    protected def mapInner[_ToElem, _ToBase, _To <: Wrapper[_ToElem]](aConverter: _Elem To _ToElem)(aDummy: _ToBase, aDummy2: _To): _To = {
        val tBuilder: WrapperBuilder[_ToElem, _To] = builder

        CollectionUtils.map(toIterable(unwrap), tBuilder, aConverter)
    }

    protected def toIterable(aBase: _Base): _Container[_Elem]

    protected def builder[_ToElem, _To <: Wrapper[_ToElem]]: WrapperBuilder[_ToElem, _To]

    val unwrap: _Base
}