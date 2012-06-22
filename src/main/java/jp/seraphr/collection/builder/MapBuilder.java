package jp.seraphr.collection.builder;

import java.util.HashMap;
import java.util.Map;

import jp.seraphr.common.Tuple2;

public class MapBuilder<_Key, _Value> implements Builder<Tuple2<_Key, _Value>, Map<_Key, _Value>> {
    private Map<_Key, _Value> mResult = new HashMap<_Key, _Value>();

    @Override
    public void add(Tuple2<_Key, _Value> aElement) {
        mResult.put(aElement.get1(), aElement.get2());
    }

    @Override
    public Map<_Key, _Value> build() {
        return mResult;
    }

}
