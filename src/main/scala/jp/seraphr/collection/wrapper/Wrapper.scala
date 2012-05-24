package jp.seraphr.collection.wrapper
import jp.seraphr.collection.builder.WrapperBuilder
import jp.seraphr.collection.Converter
import jp.seraphr.collection.CollectionUtils

trait Wrapper[_Elem] {
    type _Container[X] <: java.lang.Iterable[X]
    type _Base
    type _This[X] <: Wrapper[X]

    type To[From, To] = Converter[From, To]

    protected def mapInner[_ToElem, _ToBase](aConverter: _Elem To _ToElem)(aDummy: _ToBase): _This[_ToElem] = {
        val tBuilder: WrapperBuilder[_ToElem, _This[_ToElem]] = builder

        CollectionUtils.map(toIterable(unwrap), tBuilder, aConverter)
    }

    protected def toIterable(aBase: _Base): _Container[_Elem]

    protected def builder[_ToElem]: WrapperBuilder[_ToElem, _This[_ToElem]]

    val unwrap: _Base
}