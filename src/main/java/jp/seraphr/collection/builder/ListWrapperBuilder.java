package jp.seraphr.collection.builder;

import java.util.ArrayList;
import java.util.List;

import jp.seraphr.collection.wrapper.ListWrapper;

public class ListWrapperBuilder<_Elem> implements WrapperBuilder<List<_Elem>, List<_Elem>, _Elem, ListWrapper<_Elem>> {
    List<_Elem> mList = new ArrayList<_Elem>();

    @Override
    public void add(_Elem aElement) {
        mList.add(aElement);
    }

    @Override
    public ListWrapper<_Elem> build() {
        return new ListWrapper<_Elem>(mList);
    }

}
