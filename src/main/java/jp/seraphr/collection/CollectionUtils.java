package jp.seraphr.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.seraphr.collection.builder.Builder;
import jp.seraphr.collection.builder.ListBuilder;
import jp.seraphr.common.Converter;
import jp.seraphr.common.Converter2;
import jp.seraphr.common.Equivalence;
import jp.seraphr.common.NotPredicate;
import jp.seraphr.common.Predicate;
import jp.seraphr.common.Tuple2;

/**
 * コレクションに対する高階関数群を提供するユーティリティクラスです。
 * このクラスのユーティリティは注記のない限り、副作用はありません。
 */
public final class CollectionUtils {
    private CollectionUtils() {

    }

    /**
     * 与えられたListの要素をそれぞれ{@link Converter} によって変換した、新しいListを作って返します。
     *
     * @param <_Source> 変換元要素の型
     * @param <_Dest> 変換先要素の型
     * @param aSource 変換元List
     * @param aConverter 変換関数
     * @return 変換結果List
     */
    public static <_Source, _Dest> List<_Dest> map(List<_Source> aSource, Converter<? super _Source, ? extends _Dest> aConverter) {
        return map(aSource, new ListBuilder<_Dest>(), aConverter);
    }

    /**
     * 与えられたIterableの要素をそれぞれ{@link Converter} によって変換し、変換結果をaBuilderに与えて{@link Builder#build()} 結果を返します。
     *
     * @param <_Source> 変換元要素の型
     * @param <_Dest> 変換先要素の型
     * @param <_Result> 変換先の型
     * @param aSource 変換元Iterable
     * @param aBuilder 結果オブジェクトのビルダ
     * @param aConverter 要素の変換関数
     * @return 変換結果オブジェクト
     */
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

    /**
     * 与えられたListの要素のうち、与えられた条件に合致するもののみを含むListを生成して返します。
     *
     * @param <_Element> 要素型
     * @param aSource 元となるList
     * @param aPredicate 条件関数
     * @return aPreicateの{@link Predicate#apply(Object)}がtrueを返すもののみを含むリスト
     */
    public static <_Element> List<_Element> filter(List<_Element> aSource, Predicate<? super _Element> aPredicate) {
        return filter(aSource, new ListBuilder<_Element>(), aPredicate);
    }

    /**
     * 与えられたIterableの要素のうち、与えられた条件に合致するもののみをビルダに与え、{@link Builder#build()} 結果を返します。
     *
     * @param <_Elem> 要素型
     * @param <_Result> 結果型
     * @param aSource 元となるIterable
     * @param aBuilder 結果オブジェクトのビルダ
     * @param aPredicate 要件関数
     * @return aBuilderによって生成されたオブジェクト
     */
    public static <_Elem, _Result> _Result filter(Iterable<_Elem> aSource, Builder<_Elem, _Result> aBuilder, Predicate<? super _Elem> aPredicate) {
        for (_Elem tElement : aSource) {
            if (aPredicate.apply(tElement))
                aBuilder.add(tElement);
        }

        return aBuilder.build();
    }

    /**
     * {@link #filter(List, Predicate)}の逆。
     * 条件に合致しないもののみを含むListを生成して返します。
     *
     * @see #filter(List, Predicate)
     * @param <_Element>
     * @param aSource
     * @param aPredicate
     * @return
     */
    public static <_Element> List<_Element> filterNot(List<_Element> aSource, Predicate<? super _Element> aPredicate) {
        return filter(aSource, NotPredicate.create(aPredicate));
    }

    /**
     * {@link #filter(Iterable, Builder, Predicate)}の逆。
     * 条件に合致しないもののみをビルダに与え、{@link Builder#build()} 結果を返します。
     *
     * @see #filter(Iterable, Builder, Predicate)
     * @param <_Elem>
     * @param <_Result>
     * @param aSource
     * @param aBuilder
     * @param aPredicate
     * @return
     */
    public static <_Elem, _Result> _Result filterNot(Iterable<_Elem> aSource, Builder<_Elem, _Result> aBuilder, Predicate<? super _Elem> aPredicate){
        return filter(aSource, aBuilder, NotPredicate.create(aPredicate));
    }

    /**
     * 与えられたIterableの要素内から、与えられた条件に合致するものを探して返します。
     *
     * @param <_Element> 要素型
     * @param aSource 探索対象Iterable
     * @param aPredicate 条件関数
     * @return 条件に合致するものがあった場合そのオブジェクト、そうでない場合null
     */
    public static <_Element> _Element find(Iterable<_Element> aSource, Predicate<? super _Element> aPredicate) {
        for (_Element tElement : aSource) {
            if (aPredicate.apply(tElement))
                return tElement;
        }

        return null;
    }

    /**
     * 与えられたListの要素内から、与えられた条件に合致するものを探し、その要素番号を返します。
     *
     * @param <_Element> 要素型
     * @param aSource 探索対象List
     * @param aPredicate 条件関数
     * @return 条件に合致するものが見つかった場合その要素番号、そうでない場合負の数
     */
    public static <_Element> int findIndex(List<_Element> aSource, Predicate<? super _Element> aPredicate) {
        int tLength = aSource.size();
        for (int i = 0; i < tLength; i++) {
            if (aPredicate.apply(aSource.get(i)))
                return i;
        }

        return -1;
    }

    /**
     * 与えられたIterableの要素から、与えられた条件に合致するものが存在するかどうかを返します。
     *
     * @param <_Element> 要素型
     * @param aSource 探索対象Iterable
     * @param aPredicate 条件関数
     * @return 条件に合致するものが見つかった場合true
     */
    public static <_Element> boolean contains(Iterable<_Element> aSource, Predicate<? super _Element> aPredicate) {
        return find(aSource, aPredicate) != null;
    }

    /**
     * 与えられたIterableの要素と、与えられた条件オブジェクトを、{@link Equivalence}で確認し、同一性が認められるものが存在するかどうかを返します。
     *
     * @param <_Element1> Iterableの要素型
     * @param <_Element2> 条件オブジェクト型
     * @param aSource 探索対象Iterable
     * @param aTarget 条件オブジェクト
     * @param aEquivalence 同一性検査器
     * @return 同一性が認められるものが存在したらtrue
     */
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
     * @param <_Elem> 要素型
     * @param <_Result> 結果型
     * @param aSource 畳み込み対象のコレクション
     * @param aFirst 畳み込み結果の初期値。 aSourceの長さが0の場合、この値が返る
     * @param aConverter 畳み込み演算を表す{@link Converter2}。 {@link Converter2#convert(Object, Object)}の第一引数は畳み込み演算の途中結果、第二引数はコレクションの要素を表す。
     * @return 畳み込み演算結果
     */
    public static <_Elem, _Result> _Result foldLeft(Iterable<_Elem> aSource, _Result aFirst, Converter2<_Result, _Elem, _Result> aConverter){
        _Result tResult = aFirst;
        for (_Elem tElem : aSource) {
            tResult = aConverter.convert(tResult, tElem);
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
     *
     * @param <_Elem>
     * @param aSource 畳み込み対象コレクション 長さが0の場合例外
     * @param aConverter 畳み込み演算を表す{@link Converter2}。 {@link Converter2#convert(Object, Object)}の第一引数は畳み込み演算の途中結果、第二引数はコレクションの要素を表す。
     * @return
     */
    public static <_Elem> _Elem reduceLeft(Iterable<_Elem> aSource, Converter2<_Elem, _Elem, _Elem> aConverter){
        Iterator<_Elem> tIterator = aSource.iterator();

        if(!tIterator.hasNext())
            throw new RuntimeException("aSource has no element.");

        _Elem tResult = tIterator.next();
        while(tIterator.hasNext()){
            tResult = aConverter.convert(tResult, tIterator.next());
        }

        return tResult;
    }

    /**
     * ２つのリストから、{@link Tuple2}のリストを生成して返します。
     * 結果Listの長さは、与えられた２つのリストの長さのうち短い方と同じになります。
     *
     * @param <_E1> 一つ目のListの要素型
     * @param <_E2> 二つ目のListの要素型
     * @param aList1
     * @param aList2
     * @return
     */
    public static <_E1, _E2> List<Tuple2<_E1, _E2>> zip(List<_E1> aList1, List<_E2> aList2) {
        return zip(aList1, aList2, new ListBuilder<Tuple2<_E1, _E2>>());
    }

    /**
     * 2つのIterableから、{@link Tuple2}を生成し、aBuilderに与え、{@link Builder#build()}の結果を返します。
     *
     * @param <_E1> 一つ目の要素型
     * @param <_E2> 二つ目の要素型
     * @param <_Result> 生成されるコンテナの型
     * @param aSource1
     * @param aSource2
     * @param aBuilder
     * @return 生成されたコンテナ
     * @see CollectionUtils#zip(List, List)
     */
    public static <_E1, _E2, _Result> _Result zip(Iterable<_E1> aSource1, Iterable<_E2> aSource2, Builder<Tuple2<_E1, _E2>, _Result> aBuilder){
        Iterator<_E1> tIterator1 = aSource1.iterator();
        Iterator<_E2> tIterator2 = aSource2.iterator();

        while(tIterator1.hasNext() && tIterator2.hasNext()){
            aBuilder.add(Tuple2.create(tIterator1.next(), tIterator2.next()));
        }

        return aBuilder.build();
    }

    /**
     * 与えられたリストを、(要素, 添字番号)の{@link Tuple2}のリストに変換して返します。
     *
     * @param <_E> 要素型
     * @param aList
     * @return
     */
    public static <_E> List<Tuple2<_E, Integer>> zipWithIndex(List<_E> aList) {
        List<Tuple2<_E, Integer>> tResult = new ArrayList<Tuple2<_E, Integer>>();
        int tLength = aList.size();
        for (int i = 0; i < tLength; i++) {
            tResult.add(Tuple2.create(aList.get(i), i));
        }

        return tResult;
    }

    /**
     * 与えられたリストの要素を、与えられた２つのConverterで変換し、新しいListを作って返します。
     *
     * @param <_Source>
     * @param <_E1>
     * @param <_E2>
     * @param aSourceList
     * @param aConverter1
     * @param aConverter2
     * @return
     */
    public static <_Source, _E1, _E2> List<Tuple2<_E1, _E2>> zip(List<_Source> aSourceList, Converter<? super _Source, ? extends _E1> aConverter1, Converter<? super _Source, ? extends _E2> aConverter2) {
        final Converter<? super _Source, ? extends _E1> tConverter1 = aConverter1;
        final Converter<? super _Source, ? extends _E2> tConverter2 = aConverter2;

        return map(aSourceList, new Converter<_Source, Tuple2<_E1, _E2>>() {
            @Override
            public Tuple2<_E1, _E2> convert(_Source aSource) {
                return Tuple2.<_E1, _E2>create(tConverter1.convert(aSource), tConverter2.convert(aSource));
            }
        });
    }

    /**
     * {@link Tuple2}のリストから、Tupleの要素を分離した２つのリストを作成して返します。
     *
     * @param <_E1>
     * @param <_E2>
     * @param aTupleList
     * @return
     */
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
