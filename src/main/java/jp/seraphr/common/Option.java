package jp.seraphr.common;

public abstract class Option<_Elem> {

    private static None<?> NONE = new None<Object>();

    public static <_E> Option<_E> some(_E aElement) {
        if (aElement == null)
            return none();
        else
            return new Some<_E>(aElement);
    }

    @SuppressWarnings("unchecked")
    public static <_E> Option<_E> none() {
        return (Option<_E>) NONE;
    }

    public abstract _Elem getOrNull();

    public abstract _Elem getOrElse(_Elem aElse);

    public abstract void match(OptionMatcher<_Elem> aMatcher);

    private static class Some<_E> extends Option<_E> {
        public Some(_E aElement) {
            mElement = aElement;
        }

        private _E mElement;

        @Override
        public _E getOrNull() {
            return mElement;
        }

        @Override
        public _E getOrElse(_E aElse) {
            return mElement;
        }

        @Override
        public void match(OptionMatcher<_E> aMatcher) {
            aMatcher.matchSome(mElement);
        }
    }

    private static class None<_E> extends Option<_E> {

        @Override
        public _E getOrNull() {
            return null;
        }

        @Override
        public _E getOrElse(_E aElse) {
            return aElse;
        }

        @Override
        public void match(OptionMatcher<_E> aMatcher) {
            aMatcher.matchNone();
        }
    }
}
