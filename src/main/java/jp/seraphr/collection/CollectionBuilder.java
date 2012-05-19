package jp.seraphr.collection;

public interface CollectionBuilder<_To, _Elem> {
    CollectionWrapper<_To, _Elem> build();
}
