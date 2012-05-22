package jp.seraphr.collection.wrapper;

import java.util.Collections;
import java.util.List;

import jp.seraphr.collection.Converter;
import jp.seraphr.collection.builder.ListWrapperBuilder;
import jp.seraphr.collection.builder.WrapperBuilder;

public class ListWrapper<_Element> extends CollectionWrapper<List<_Element>, List<_Element>, _Element> {
    public ListWrapper(List<_Element> aBase) {
        super(aBase);
    }

    public <_ToElem> ListWrapper<_ToElem> map(Converter<_Element, _ToElem> aConverter){
        List<_ToElem> tDummy = Collections.emptyList();
        return super.mapInner(aConverter, tDummy, tDummy);
    }

    @Override
    protected List<_Element> toIterable(List<_Element> aBase) {
        return aBase;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <_ToBase, _ToIterable extends Iterable<_ToElem>, _ToElem, _To extends CollectionWrapper<_ToBase, _ToIterable, _ToElem>> WrapperBuilder<_ToBase, _ToIterable, _ToElem, _To> builder(_ToBase aDummy, _ToIterable aDummy2) {
        // 高階型引数が無くて、このキャストをなくすことができない…
        // 何故かここでコンパイルエラー・・・？
        return (WrapperBuilder<_ToBase, _ToIterable, _ToElem, _To>)new ListWrapperBuilder<_ToElem>();
    }
}
