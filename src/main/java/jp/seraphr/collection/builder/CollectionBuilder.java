package jp.seraphr.collection.builder;

import java.util.Collection;

public abstract class CollectionBuilder<_Elem, _Build extends Collection<_Elem>> implements Builder<_Elem, _Build> {
    public CollectionBuilder(_Build aResult){
        mResult = aResult;
    }

    private _Build mResult;

    @Override
    public void add(_Elem aElement) {
        mResult.add(aElement);
    }

    @Override
    public _Build build() {
        return mResult;
    }
}
