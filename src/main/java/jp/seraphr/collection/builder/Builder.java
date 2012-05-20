package jp.seraphr.collection.builder;

public interface Builder<_Elem, _Build> {
    public void add(_Elem aElement);
    public _Build build();
}
