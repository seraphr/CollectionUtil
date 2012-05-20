package jp.seraphr.collection.wrapper;

import jp.seraphr.collection.CollectionUtils;
import jp.seraphr.collection.Converter;
import jp.seraphr.collection.builder.WrapperBuilder;

public abstract class CollectionWrapper<_Base extends Iterable<_Element>, _Element> {
    public CollectionWrapper(_Base aBase) {
        super();
        mBase = aBase;
    }

    private _Base mBase;

    protected <_ToBase extends Iterable<_ToElem>, _ToElem, _To extends CollectionWrapper<_ToBase, _ToElem>> _To mapInner(Converter<_Element, _ToElem> aConverter, _ToBase aDummy){
        WrapperBuilder<_ToBase, _ToElem, _To> tBuilder = builder(aDummy);

        return CollectionUtils.map(mBase, tBuilder, aConverter);
    }

    protected abstract <_ToBase extends Iterable<_ToElem>, _ToElem, _To extends CollectionWrapper<_ToBase, _ToElem>> WrapperBuilder<_ToBase, _ToElem, _To> builder(_ToBase aDummy);

    public _Base unwrap() {
        return mBase;
    }
}
