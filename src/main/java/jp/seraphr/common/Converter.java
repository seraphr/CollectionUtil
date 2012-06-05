package jp.seraphr.common;

public interface Converter<_Source, _Dest> {
    public _Dest convert(_Source aSource);
}
