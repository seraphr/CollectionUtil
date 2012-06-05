package jp.seraphr.common;


public class Tuple2<_Element1, _Element2> {
    public Tuple2(_Element1 aElement1, _Element2 aElement2) {
        mElement1 = aElement1;
        mElement2 = aElement2;
    }

    public static <_E1, _E2> Tuple2<_E1, _E2> create(_E1 aElement1, _E2 aElement2){
        return new Tuple2<_E1, _E2>(aElement1, aElement2);
    }

    private _Element1 mElement1;
    private _Element2 mElement2;

    public _Element1 get1() {
        return mElement1;
    }

    public _Element2 get2() {
        return mElement2;
    }

    @Override
    public int hashCode() {
        return this.get1().hashCode();
    }

    @Override
    public boolean equals(Object aObj) {
        if(!(aObj instanceof Tuple2))
            return false;

        Tuple2<?, ?> tObj = (Tuple2<?, ?>)aObj;

        return tObj.get1().equals(this.get1()) && tObj.get2().equals(this.get2());
    }


}
