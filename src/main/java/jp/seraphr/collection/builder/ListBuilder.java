package jp.seraphr.collection.builder;

import java.util.ArrayList;
import java.util.List;

public class ListBuilder<_Elem> extends CollectionBuilder<_Elem, List<_Elem>> {
    public ListBuilder() {
        super(new ArrayList<_Elem>());
    }
}
