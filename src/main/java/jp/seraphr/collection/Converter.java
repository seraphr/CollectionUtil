package jp.seraphr.collection;

public interface Converter<_Source, _Dest> {
    public _Dest convert(_Source aSource);
}
