package jp.seraphr.collection.wrapper;

import java.util.Collections;
import java.util.List;

import jp.seraphr.collection.Converter;
import jp.seraphr.collection.builder.WrapperBuilder;

public class ListWrapper<_Element> extends CollectionWrapper<List<_Element>, _Element> {
    public ListWrapper(List<_Element> aBase) {
        super(aBase);
    }

    public <_ToElem> ListWrapper<_ToElem> map(Converter<_Element, _ToElem> aConverter){
        List<_ToElem> tDummy = Collections.emptyList();
        return super.mapInner(aConverter, tDummy);
    }

    @Override
    protected <_ToBase extends Iterable<_ToElem>, _ToElem, _To extends CollectionWrapper<_ToBase, _ToElem>> WrapperBuilder<_ToBase, _ToElem, _To> builder(_ToBase aDummy) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }
}
