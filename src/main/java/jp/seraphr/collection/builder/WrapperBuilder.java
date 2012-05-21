package jp.seraphr.collection.builder;

import jp.seraphr.collection.wrapper.CollectionWrapper;

public interface WrapperBuilder<_Base, _Iterable extends Iterable<_Elem>, _Elem, _Wrapper extends CollectionWrapper<_Base, _Iterable, _Elem>> extends Builder<_Elem, _Wrapper> {

}
