package jp.seraphr.common;

public interface OptionMatcher<_Elem> {
    public void matchSome(_Elem aElem);
    public void matchNone();
}
