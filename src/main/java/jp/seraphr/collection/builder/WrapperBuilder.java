package jp.seraphr.collection.builder;

import jp.seraphr.collection.wrapper.CollectionWrapper;

public interface WrapperBuilder<_Base extends Iterable<_Elem>, _Elem, _Wrapper extends CollectionWrapper<_Base, _Elem>> extends Builder<_Elem, _Wrapper> {

}
