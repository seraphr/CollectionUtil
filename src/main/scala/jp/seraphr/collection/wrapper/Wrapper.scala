package jp.seraphr.collection.wrapper
import jp.seraphr.collection.builder.WrapperBuilder
import jp.seraphr.collection.CollectionUtils
import jp.seraphr.common.Converter
import jp.seraphr.common.Predicate

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

//    def foldLeft[_Result](_Result aInitValue, Con)


    private def toIterable: _Container[_Elem] = toIterable(unwrap)
    protected def toIterable(aBase: _Base): _Container[_Elem]
    protected def myBuilder: WrapperBuilder[_Elem, _This]

    val unwrap: _Base
}