package jp.seraphr.collection.wrapper;

import jp.seraphr.collection.CollectionUtils;
import jp.seraphr.collection.Converter;
import jp.seraphr.collection.builder.WrapperBuilder;

public abstract class CollectionWrapper<_Base, _Iterable extends Iterable<_Element>, _Element> {
    public CollectionWrapper(_Base aBase) {
        super();
        mBase = aBase;
    }

    private _Base mBase;

    protected <_ToBase, _ToIterable extends Iterable<_ToElem>, _ToElem, _To extends CollectionWrapper<_ToBase, _ToIterable, _ToElem>> _To mapInner(Converter<_Element, _ToElem> aConverter, _ToBase aDummy, _ToIterable aDummy2){
        WrapperBuilder<_ToBase, _ToIterable, _ToElem, _To> tBuilder = builder(aDummy, aDummy2);

        return CollectionUtils.map(toIterable(mBase), tBuilder, aConverter);
    }

    protected abstract _Iterable toIterable(_Base aBase);

    protected abstract <_ToBase, _ToIterable extends Iterable<_ToElem>, _ToElem, _To extends CollectionWrapper<_ToBase, _ToIterable, _ToElem>> WrapperBuilder<_ToBase, _ToIterable, _ToElem, _To> builder(_ToBase aDummy, _ToIterable aDummy2);

    public _Base unwrap() {
        return mBase;
    }
}
