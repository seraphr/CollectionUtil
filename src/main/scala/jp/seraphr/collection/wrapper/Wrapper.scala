package jp.seraphr.collection.wrapper
import jp.seraphr.collection.builder.WrapperBuilder
import jp.seraphr.collection.Converter
import jp.seraphr.collection.CollectionUtils
import jp.seraphr.collection.Predicate
import jp.seraphr.collection.builder.WrapperBuilder

trait Wrapper[_Elem] {
    type _Container[X] <: java.lang.Iterable[X]
    type _Base
    type _This <: Wrapper[_Elem]

    protected def mapInner[_ToElem, _To <: Wrapper[_ToElem]](aConverter: Converter[_Elem, _ToElem])(aBuilder: WrapperBuilder[_ToElem, _To]): _To = {
        CollectionUtils.map(toIterable, aBuilder, aConverter)
    }

    def filter(aPredicate: Predicate[_Elem]): _This = {
      CollectionUtils.filter(toIterable, myBuilder, aPredicate)
    }

    def filterNot(aPredicate: Predicate[_Elem]): _This = {
      CollectionUtils.filterNot(toIterable, myBuilder, aPredicate)
    }

    protected def filterInner[_To <: Wrapper[_Elem]](aPredicate: Predicate[_Elem])( aBuilder: WrapperBuilder[_Elem, _To]): _To = {
        CollectionUtils.filter(toIterable, aBuilder, aPredicate)
    }


    private def toIterable: _Container[_Elem] = toIterable(unwrap)
    protected def toIterable(aBase: _Base): _Container[_Elem]
    protected def myBuilder: WrapperBuilder[_Elem, _This]

    val unwrap: _Base
}