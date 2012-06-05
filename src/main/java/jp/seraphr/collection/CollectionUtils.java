package jp.seraphr.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.seraphr.collection.builder.Builder;
import jp.seraphr.collection.builder.ListBuilder;
import jp.seraphr.common.Converter;
import jp.seraphr.common.Equivalence;
import jp.seraphr.common.NotPredicate;
import jp.seraphr.common.Predicate;
import jp.seraphr.common.Tuple2;

public final class CollectionUtils {
    private CollectionUtils() {

    }

    public static <_Source, _Dest> List<_Dest> map(List<_Source> aSource, Converter<? super _Source, ? extends _Dest> aConverter) {
        return map(aSource, new ListBuilder<_Dest>(), aConverter);
    }

    public static <_Source, _Dest, _Result> _Result map(Iterable<_Source> aSource, Builder<_Dest, _Result> aBuilder, Converter<? super _Source, ? extends _Dest> aConverter) {
        for (_Source tSource : aSource) {
            aBuilder.add(aConverter.convert(tSource));
        }

        return aBuilder.build();
    }

    /**
     * 副作用有り。
     *
     * @param <_Element>
     * @param aSource
     * @param aPredicate
     * @return
     * @deprecated つーかこれ消す。 use filterNot
     */
    public static <_Element> List<_Element> remove(List<_Element> aSource, Predicate<? super _Element> aPredicate) {
        List<_Element> tResult = new ArrayList<_Element>();

        Iterator<_Element> tIterator = aSource.iterator();
        while (tIterator.hasNext()) {
            _Element tElement = tIterator.next();
            if (aPredicate.apply(tElement)) {
                tIterator.remove();
                tResult.add(tElement);
            }
        }

        return tResult;
    }

    public static <_Element> List<_Element> filter(List<_Element> aSource, Predicate<? super _Element> aPredicate) {
        return filter(aSource, new ListBuilder<_Element>(), aPredicate);
    }

    public static <_Elem, _Result> _Result filter(Iterable<_Elem> aSource, Builder<_Elem, _Result> aBuilder, Predicate<? super _Elem> aPredicate) {
        for (_Elem tElement : aSource) {
            if (aPredicate.apply(tElement))
                aBuilder.add(tElement);
        }

        return aBuilder.build();
    }

    public static <_Element> List<_Element> filterNot(List<_Element> aSource, Predicate<? super _Element> aPredicate) {
        return filter(aSource, NotPredicate.create(aPredicate));
    }

    public static <_Elem, _Result> _Result filterNot(Iterable<_Elem> aSource, Builder<_Elem, _Result> aBuilder, Predicate<? super _Elem> aPredicate){
        return filter(aSource, aBuilder, NotPredicate.create(aPredicate));
    }

    public static <_Element> _Element find(Iterable<_Element> aSource, Predicate<? super _Element> aPredicate) {
        for (_Element tElement : aSource) {
            if (aPredicate.apply(tElement))
                return tElement;
        }

        return null;
    }

    public static <_Element> int findIndex(List<_Element> aSource, Predicate<? super _Element> aPredicate) {
        int tLength = aSource.size();
        for (int i = 0; i < tLength; i++) {
            if (aPredicate.apply(aSource.get(i)))
                return i;
        }

        return -1;
    }

    public static <_Element> boolean contains(Iterable<_Element> aSource, Predicate<? super _Element> aPredicate) {
        return find(aSource, aPredicate) != null;
    }

    public static <_Element1, _Element2> boolean contains(Iterable<_Element1> aSource, _Element2 aTarget, Equivalence<? super _Element1, ? super _Element2> aEquivalence) {
        final _Element2 tTarget = aTarget;
        final Equivalence<? super _Element1, ? super _Element2> tEquivalence = aEquivalence;
        Predicate<_Element1> tPredicate = new Predicate<_Element1>() {
            @Override
            public boolean apply(_Element1 aTarget) {
                return tEquivalence.apply(aTarget, tTarget);
            }
        };
        return contains(aSource, tPredicate);
    }

    /**
     * aSourceを畳み込み、_Result型の結果を返します。
     * 畳込みは先頭側から行われます。
     *
     * @param <_Elem>
     * @param <_Result>
     * @param aSource 畳み込み対象のコレクション
     * @param aFirst 畳み込み結果の初期値。 aSourceの長さが0の場合、この値が返る
     * @param aConverter 畳み込み演算を表すConverter
     * @return
     */
    public static <_Elem, _Result> _Result foldLeft(Iterable<_Elem> aSource, _Result aFirst, Converter<Tuple2<_Result, _Elem>, _Result> aConverter){
        _Result tResult = aFirst;
        for (_Elem tElem : aSource) {
            tResult = aConverter.convert(Tuple2.create(tResult, tElem));
        }

        return tResult;
    }

    /**
     * aSourceを畳み込み、_Elem型の結果を返します。
     * 畳込みは先頭側から行われます。
     *
     * 畳込みの初期値としてaSourceの先頭要素が使用されます。
     * aSourceの長さが0の場合例外を発生させます。
     * aSourceの長さが1の場合先頭の要素を返します。
     *
     * @param <_Elem>
     * @param aSource 畳み込み対象コレクション 長さが0の場合例外
     * @param aConverter
     * @return
     */
    public static <_Elem> _Elem reduceLeft(Iterable<_Elem> aSource, Converter<Tuple2<_Elem, _Elem>, _Elem> aConverter){
        Iterator<_Elem> tIterator = aSource.iterator();

        if(!tIterator.hasNext())
            throw new RuntimeException("aSource has no element.");

        _Elem tResult = tIterator.next();
        while(tIterator.hasNext()){
            tResult = aConverter.convert(Tuple2.create(tResult, tIterator.next()));
        }

        return tResult;
    }

    public static <_E1, _E2> List<Tuple2<_E1, _E2>> zip(List<_E1> aList1, List<_E2> aList2) {
        int tLength = Math.min(aList1.size(), aList2.size());

        List<Tuple2<_E1, _E2>> tResult = new ArrayList<Tuple2<_E1, _E2>>();
        for (int i = 0; i < tLength; i++) {
            tResult.add(new Tuple2<_E1, _E2>(aList1.get(i), aList2.get(i)));
        }

        return tResult;
    }

    public static <_E> List<Tuple2<_E, Integer>> zipWithIndex(List<_E> aList) {
        List<Tuple2<_E, Integer>> tResult = new ArrayList<Tuple2<_E, Integer>>();
        int tLength = aList.size();
        for (int i = 0; i < tLength; i++) {
            tResult.add(Tuple2.create(aList.get(i), i));
        }

        return tResult;
    }

    public static <_Source, _E1, _E2> List<Tuple2<_E1, _E2>> zip(List<_Source> aSourceList, Converter<? super _Source, ? extends _E1> aConverter1, Converter<? super _Source, ? extends _E2> aConverter2) {
        List<Tuple2<_E1, _E2>> tResult = new ArrayList<Tuple2<_E1, _E2>>();
        List<_Source> tSourcelist = aSourceList;

        for (_Source tSource : tSourcelist) {
            _E1 tElement1 = aConverter1.convert(tSource);
            _E2 tElement2 = aConverter2.convert(tSource);
            tResult.add(new Tuple2<_E1, _E2>(tElement1, tElement2));
        }

        return tResult;
    }

    public static <_E1, _E2> Tuple2<List<_E1>, List<_E2>> unzip(List<Tuple2<_E1, _E2>> aTupleList) {
        List<_E1> tResult1 = new ArrayList<_E1>();
        List<_E2> tResult2 = new ArrayList<_E2>();
        List<Tuple2<_E1, _E2>> tSource = aTupleList;
        for (Tuple2<_E1, _E2> tTuple2 : tSource) {
            tResult1.add(tTuple2.get1());
            tResult2.add(tTuple2.get2());
        }

        return Tuple2.create(tResult1, tResult2);
    }

    public static <_E1, _E2> List<_E1> unzip1(List<Tuple2<_E1, _E2>> aTupleList) {
        List<_E1> tResult = new ArrayList<_E1>();
        List<Tuple2<_E1, _E2>> tSource = aTupleList;
        for (Tuple2<_E1, _E2> tTuple2 : tSource) {
            tResult.add(tTuple2.get1());
        }

        return tResult;
    }

    public static <_E1, _E2> List<_E2> unzip2(List<Tuple2<_E1, _E2>> aTupleList) {
        List<_E2> tResult = new ArrayList<_E2>();
        List<Tuple2<_E1, _E2>> tSource = aTupleList;
        for (Tuple2<_E1, _E2> tTuple2 : tSource) {
            tResult.add(tTuple2.get2());
        }

        return tResult;
    }

    public static <_E1, _E2> Tuple2<_E1, _E2> findElement1(List<Tuple2<_E1, _E2>> aTupleList, _E1 aTarget) {
        int tIndex = findElementIndex1(aTupleList, aTarget);
        if (tIndex < 0)
            return null;

        return aTupleList.get(tIndex);
    }

    public static <_E1, _E2> int findElementIndex1(List<Tuple2<_E1, _E2>> aTupleList, final _E1 aTarget) {
        return findIndex(aTupleList, new Predicate<Tuple2<_E1, _E2>>() {
            @Override
            public boolean apply(Tuple2<_E1, _E2> aTuple) {
                return aTuple.get1().equals(aTarget);
            }
        });
    }

    public static <_E1, _E2> Tuple2<_E1, _E2> findElement2(List<Tuple2<_E1, _E2>> aTupleList, _E2 aTarget) {
        int tIndex = findElementIndex2(aTupleList, aTarget);
        if (tIndex < 0)
            return null;

        return aTupleList.get(tIndex);
    }

    public static <_E1, _E2> int findElementIndex2(List<Tuple2<_E1, _E2>> aTupleList, final _E2 aTarget) {
        return findIndex(aTupleList, new Predicate<Tuple2<_E1, _E2>>() {
            @Override
            public boolean apply(Tuple2<_E1, _E2> aTuple) {
                return aTuple.get2().equals(aTarget);
            }
        });
    }
}
