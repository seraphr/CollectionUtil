package jp.seraphr.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jp.seraphr.collection.builder.Builder;
import jp.seraphr.collection.builder.ListBuilder;
import jp.seraphr.collection.builder.MapBuilder;
import jp.seraphr.common.Equivalence;
import jp.seraphr.common.Function;
import jp.seraphr.common.Function0;
import jp.seraphr.common.Function2;
import jp.seraphr.common.NotPredicate;
import jp.seraphr.common.Option;
import jp.seraphr.common.Predicate;
import jp.seraphr.common.Tuple2;

/**
 * コレクションに対する高階関数を含めた便利メソッド群提供するユーティリティクラスです。 <br />
 * このクラスのユーティリティは注記のない限り、副作用はありません。
 */
public final class CollectionUtils {
    private CollectionUtils() {

    }

    /**
     * 与えられたListの要素をそれぞれ{@link Function} によって変換した、新しいListを作って返します。
     *
     * @param <_Source>
     *            変換元要素の型
     * @param <_Dest>
     *            変換先要素の型
     * @param aSource
     *            変換元List
     * @param aFunction
     *            変換関数
     * @return 変換結果List
     */
    public static <_Source, _Dest> List<_Dest> map(List<_Source> aSource, Function<? super _Source, ? extends _Dest> aFunction) {
        return map(aSource, new ListBuilder<_Dest>(), aFunction);
    }

    /**
     * 与えられたIterableの要素をそれぞれ{@link Function} によって変換し、変換結果をaBuilderに与えて
     * {@link Builder#build()} 結果を返します。
     *
     * @param <_Source>
     *            変換元要素の型
     * @param <_Dest>
     *            変換先要素の型
     * @param <_Result>
     *            変換先の型
     * @param aSource
     *            変換元Iterable
     * @param aBuilder
     *            結果オブジェクトのビルダ
     * @param aFunction
     *            要素の変換関数
     * @return 変換結果オブジェクト
     */
    public static <_Source, _Dest, _Result> _Result map(Iterable<_Source> aSource, Builder<_Dest, _Result> aBuilder, Function<? super _Source, ? extends _Dest> aFunction) {
        for (_Source tSource : aSource) {
            aBuilder.add(aFunction.apply(tSource));
        }

        return aBuilder.build();
    }

    /**
     *
     * @param aSource
     * @param aFunction
     * @return
     */
    public static <_Source, _Dest> List<_Dest> flatMap(List<_Source> aSource, Function<? super _Source, ? extends Iterable<? extends _Dest>> aFunction) {
        return flatMap(aSource, new ListBuilder<_Dest>(), aFunction);
    }

    public static <_Source, _Dest, _Result> _Result flatMap(Iterable<_Source> aSource, Builder<_Dest, _Result> aBuilder,  Function<? super _Source, ? extends Iterable<? extends _Dest>> aFunction){
        for (_Source tSource : aSource) {
            Iterable<? extends _Dest> tMapped = aFunction.apply(tSource);
            for (_Dest tDest : tMapped) {
                aBuilder.add(tDest);
            }
        }

        return aBuilder.build();
    }

    /**
     * 対象MapのValueのValueのみを与えられたコンバータを用いて変換します。
     *
     * @param aSource
     * @param aFunction
     * @return
     */
    public static <_Key, _Before, _After> Map<_Key, _After> mapValue(Map<_Key, _Before> aSource, Function<? super _Before, ? extends _After> aFunction) {
        Map<_Key, _After> tResult = new HashMap<_Key, _After>();
        for(Entry<_Key, _Before> tEntry : aSource.entrySet()){
            tResult.put(tEntry.getKey(), aFunction.apply(tEntry.getValue()));
        }

        return tResult;
    }

    /**
     * 副作用有り。
     *
     * @param <_Element>
     * @param aSource
     * @param aPredicate
     * @return
     * @deprecated つーかこれ消す。 use {@link #filterNot(List, Predicate)}
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
     * @param <_Element>
     *            要素型
     * @param aSource
     *            元となるList
     * @param aPredicate
     *            条件関数
     * @return aPreicateの{@link Predicate#apply(Object)}がtrueを返すもののみを含むリスト
     */
    public static <_Element> List<_Element> filter(List<_Element> aSource, Predicate<? super _Element> aPredicate) {
        return filter(aSource, new ListBuilder<_Element>(), aPredicate);
    }

    /**
     * 与えられたIterableの要素のうち、与えられた条件に合致するもののみをビルダに与え、{@link Builder#build()}
     * 結果を返します。
     *
     * @param <_Elem>
     *            要素型
     * @param <_Result>
     *            結果型
     * @param aSource
     *            元となるIterable
     * @param aBuilder
     *            結果オブジェクトのビルダ
     * @param aPredicate
     *            要件関数
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
     * {@link #filter(List, Predicate)}の逆。 条件に合致しないもののみを含むListを生成して返します。
     *
     * @see #filter(List, Predicate)
     * @param <_Element>
     * @param aSource
     * @param aPredicate
     * @return filter済みList
     */
    public static <_Element> List<_Element> filterNot(List<_Element> aSource, Predicate<? super _Element> aPredicate) {
        return filter(aSource, NotPredicate.create(aPredicate));
    }

    /**
     * {@link #filter(Iterable, Builder, Predicate)}の逆。 条件に合致しないもののみをビルダに与え、
     * {@link Builder#build()} 結果を返します。
     *
     * @see #filter(Iterable, Builder, Predicate)
     * @param <_Elem>
     * @param <_Result>
     * @param aSource
     * @param aBuilder
     * @param aPredicate
     * @return 生成されたコレクション
     */
    public static <_Elem, _Result> _Result filterNot(Iterable<_Elem> aSource, Builder<_Elem, _Result> aBuilder, Predicate<? super _Elem> aPredicate) {
        return filter(aSource, aBuilder, NotPredicate.create(aPredicate));
    }

    /**
     * {@link #filter(List, Predicate)}と{@link #map(List, Function)}を同時に行います。
     * aFunctionの{@linkplain Function#apply(Object)}
     * がSomeを返すもののみを結果のリストに加えます。
     *
     * @param aSource
     *            変換元リスト
     * @param aFunction
     *            変換関数
     * @return 生成されたリスト
     */
    public static <_Source, _Dest> List<_Dest> collect(List<_Source> aSource, Function<_Source, Option<_Dest>> aFunction) {
        return collect(aSource, new ListBuilder<_Dest>(), aFunction);
    }

    /**
     * {@link #filter(Iterable, Builder, Predicate)}と
     * {@link #map(Iterable, Builder, Function)}を同時に行います。 aFunctionの
     * {@linkplain Function#apply(Object)}がSomeを返すもののみを結果のコレクションに加えます。
     *
     * @param aSource
     *            変換元コレクション
     * @param aBuilder
     *            結果コレクションビルダ
     * @param aFunction
     *            変換関数
     * @return 生成されたコレクション
     */
    public static <_Source, _Dest, _Result> _Result collect(Iterable<_Source> aSource, Builder<_Dest, _Result> aBuilder, Function<_Source, Option<_Dest>> aFunction) {
        for (_Source tSource : aSource) {
            Option<_Dest> tOption = aFunction.apply(tSource);
            if (tOption.isSome()) {
                aBuilder.add(tOption.getOrNull());
            }
        }

        return aBuilder.build();
    }

    /**
     * 与えられたIterableの要素内から、与えられた条件に合致するものを探して返します。
     *
     * @param <_Element>
     *            要素型
     * @param aSource
     *            探索対象Iterable
     * @param aPredicate
     *            条件関数
     * @return 条件に合致するものがあった場合そのオブジェクト、そうでない場合None
     */
    public static <_Element> Option<_Element> find(Iterable<_Element> aSource, Predicate<? super _Element> aPredicate) {
        for (_Element tElement : aSource) {
            if (aPredicate.apply(tElement))
                return Option.some(tElement);
        }

        return Option.none();
    }

    /**
     * 与えられたListの要素内から、与えられた条件に合致するものを探し、その要素番号を返します。
     *
     * @param <_Element>
     *            要素型
     * @param aSource
     *            探索対象List
     * @param aPredicate
     *            条件関数
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
     * @param <_Element>
     *            要素型
     * @param aSource
     *            探索対象Iterable
     * @param aPredicate
     *            条件関数
     * @return 条件に合致するものが見つかった場合true
     */
    public static <_Element> boolean contains(Iterable<_Element> aSource, Predicate<? super _Element> aPredicate) {
        return find(aSource, aPredicate).isSome();
    }

    /**
     * 与えられたIterableの要素と、与えられた条件オブジェクトを、{@link Equivalence}
     * で確認し、同一性が認められるものが存在するかどうかを返します。
     *
     * @param <_Element1>
     *            Iterableの要素型
     * @param <_Element2>
     *            条件オブジェクト型
     * @param aSource
     *            探索対象Iterable
     * @param aTarget
     *            条件オブジェクト
     * @param aEquivalence
     *            同一性検査器
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
     * 与えられたコレクションのすべての要素が、与えられた条件をみたす({@linkplain Predicate#apply(Object)}
     * がtrueを返す)かどうかを検査します。
     *
     * @param aSource
     *            検査対象コレクション
     * @param aPredicate
     *            条件
     * @return すべての要素が条件を充たす場合true そうでない場合false
     */
    public static <_Elem> boolean forAll(Iterable<_Elem> aSource, Predicate<? super _Elem> aPredicate) {
        return find(aSource, NotPredicate.create(aPredicate)).isNone();
    }

    /**
     * aSourceを畳み込み、_Result型の結果を返します。 畳込みは先頭側から行われます。
     *
     * @param <_Elem>
     *            要素型
     * @param <_Result>
     *            結果型
     * @param aSource
     *            畳み込み対象のコレクション
     * @param aFirst
     *            畳み込み結果の初期値。 aSourceの長さが0の場合、この値が返る
     * @param aFunction
     *            畳み込み演算を表す{@link Function2}。
     *            {@link Function2#apply(Object, Object)}
     *            の第一引数は畳み込み演算の途中結果、第二引数はコレクションの要素を表す。
     * @return 畳み込み演算結果
     */
    public static <_Elem, _Result> _Result foldLeft(Iterable<_Elem> aSource, _Result aFirst, Function2<_Result, _Elem, _Result> aFunction) {
        _Result tResult = aFirst;
        for (_Elem tElem : aSource) {
            tResult = aFunction.apply(tResult, tElem);
        }

        return tResult;
    }

    /**
     * aSourceを畳み込み、_Elem型の結果を返します。 畳込みは先頭側から行われます。
     *
     * 畳込みの初期値としてaSourceの先頭要素が使用されます。 aSourceの長さが0の場合例外を発生させます。
     * aSourceの長さが1の場合先頭の要素を返します。
     *
     *
     * @param <_Elem>
     * @param aSource
     *            畳み込み対象コレクション 長さが0の場合例外
     * @param aFunction
     *            畳み込み演算を表す{@link Function2}。
     *            {@link Function2#apply(Object, Object)}
     *            の第一引数は畳み込み演算の途中結果、第二引数はコレクションの要素を表す。
     * @return 畳い込み演算結果
     */
    public static <_Elem> _Elem reduceLeft(Iterable<_Elem> aSource, Function2<_Elem, _Elem, _Elem> aFunction) {
        Iterator<_Elem> tIterator = aSource.iterator();

        if (!tIterator.hasNext())
            throw new RuntimeException("aSource has no element.");

        _Elem tResult = tIterator.next();
        while (tIterator.hasNext()) {
            tResult = aFunction.apply(tResult, tIterator.next());
        }

        return tResult;
    }

    /**
     * ネストされたリストを平坦化して返します。<br />
     * List(List(1,2,3), List(4,5,6)) => List(1,2,3,4,5,6)
     *
     * @param aNested
     *            ネストされたリスト
     * @return 平坦化されたリスト
     */
    public static <_Elem, _NestedElem extends Iterable<_Elem>> List<_Elem> flatten(List<_NestedElem> aNested) {
        return flatten(aNested, new ListBuilder<_Elem>());
    }

    /**
     * ネストされたコレクションを平坦化して返します。
     *
     * @param aNested
     *            ネストされたコレクション
     * @param aBuilder
     *            平坦化の際に使用するBuilder
     * @return aBuilderによって生成された、平坦化されたコレクション
     */
    public static <_Elem, _NestedElem extends Iterable<_Elem>, _Result> _Result flatten(Iterable<_NestedElem> aNested, Builder<_Elem, _Result> aBuilder) {
        for (_NestedElem tInner : aNested) {
            for (_Elem tElem : tInner) {
                aBuilder.add(tElem);
            }
        }

        return aBuilder.build();
    }

    /**
     * 与えられたリストを、与えられた関数による変換を用いてグループ化します。
     *
     * @param aSource
     * @param aFunction
     * @return
     */
    public static <_E, _Key> Map<_Key, List<_E>> groupBy(final List<_E> aSource, final Function<_E, _Key> aFunction){
        return groupBy(aSource, aFunction, new Function0<Builder<_E, List<_E>>>(){
            @Override
            public Builder<_E, List<_E>> apply() {
                return new ListBuilder<_E>();
            }
        });
    }

    /**
     * 与えられたコレクションを、与えられた関数による変換を用いてグループ化します。
     *
     * @param aSource グループ化対象コレクション
     * @param aFunction グループ化の方法を定義する関数
     * @param aBuilderProvider 『結果Valueの生成器』の供給機
     * @return
     */
    public static <_E, _Key, _Result> Map<_Key, _Result> groupBy(final Iterable<_E> aSource, final Function<_E, _Key> aFunction, final Function0<Builder<_E, _Result>> aBuilderProvider) {
        Map<_Key, Builder<_E, _Result>> tKeyMap = new HashMap<_Key, Builder<_E, _Result>>();

        for (_E tElem : aSource) {
            _Key tKey = aFunction.apply(tElem);
            Builder<_E, _Result> tBuilder = getBuilder(tKeyMap, tKey, aBuilderProvider);
            tBuilder.add(tElem);
        }

        return mapValue(tKeyMap, new Function<Builder<_E, _Result>, _Result>(){
            @Override
            public _Result apply(Builder<_E, _Result> aSource) {
                return aSource.build();
            }
        });
    }

    private static <_E, _Key, _Result> Builder<_E, _Result> getBuilder(final Map<_Key, Builder<_E, _Result>> aKeyMap, final _Key aKey, final Function0<Builder<_E, _Result>> aBuilderProvider) {
        Builder<_E, _Result> tBuilder = aKeyMap.get(aKey);
        if (tBuilder == null) {
            tBuilder = aBuilderProvider.apply();
            aKeyMap.put(aKey, tBuilder);
        }

        return tBuilder;
    }

    /**
     * 対象コレクションを、与えられたBuilderを使用して変換します。
     *
     * @param aSource
     * @param aBuilder
     * @return
     */
    public static <_E, _Result> _Result to(final Iterable<_E> aSource, final Builder<_E, _Result> aBuilder){
        // map使えばできるけど、Function噛ますコストを払いたくないので
        for (_E tE : aSource) {
            aBuilder.add(tE);
        }

        return aBuilder.build();
    }

    /**
     * 対象のMapをTupleのコレクションに変換します。
     *
     * @param aSource
     * @param aBuilder
     * @return
     */
    public static <_K, _V, _Result> _Result to(final Map<_K, _V> aSource, final Builder<Tuple2<_K, _V>, _Result> aBuilder){
        return map(aSource.entrySet(), aBuilder, new Function<Entry<_K, _V>, Tuple2<_K, _V>>() {
            @Override
            public Tuple2<_K, _V> apply(Entry<_K, _V> aSource) {
                return Tuple2.create(aSource.getKey(), aSource.getValue());
            }
        });
    }

    /**
     * 対象コレクションをリストに変換します
     *
     * @param aSource
     * @return
     */
    public static <_E> List<_E> toList(final Iterable<_E> aSource){
        return to(aSource, new ListBuilder<_E>());
    }

    /**
     * 対象のMapをTupleのリストに変換します。
     *
     * @param aSource
     * @return
     */
    public static <_K, _V> List<Tuple2<_K, _V>> toList(Map<_K, _V> aSource){
        return to(aSource, new ListBuilder<Tuple2<_K, _V>>());
    }

    /**
     * 対象のタプルコレクションをMapに変換する
     *
     * @param aSource
     * @return
     */
    public static <_K, _V> Map<_K, _V> toMap(final Iterable<Tuple2<_K, _V>> aSource){
        return to(aSource, new MapBuilder<_K, _V>());
    }

    /**
     * ２つのリストから、{@link Tuple2}のリストを生成して返します。
     * 結果Listの長さは、与えられた２つのリストの長さのうち短い方と同じになります。
     *
     * @param <_E1>
     *            一つ目のListの要素型
     * @param <_E2>
     *            二つ目のListの要素型
     * @param aList1
     * @param aList2
     * @return 与えられた2つのリストの結合結果
     */
    public static <_E1, _E2> List<Tuple2<_E1, _E2>> zip(List<_E1> aList1, List<_E2> aList2) {
        return zip(aList1, aList2, new ListBuilder<Tuple2<_E1, _E2>>());
    }

    /**
     * 2つのIterableから、{@link Tuple2}を生成し、aBuilderに与え、{@link Builder#build()}
     * の結果を返します。
     *
     * @param <_E1>
     *            一つ目の要素型
     * @param <_E2>
     *            二つ目の要素型
     * @param <_Result>
     *            生成されるコンテナの型
     * @param aSource1
     * @param aSource2
     * @param aBuilder
     * @return 生成されたコンテナ
     * @see CollectionUtils#zip(List, List)
     */
    public static <_E1, _E2, _Result> _Result zip(Iterable<_E1> aSource1, Iterable<_E2> aSource2, Builder<Tuple2<_E1, _E2>, _Result> aBuilder) {
        Iterator<_E1> tIterator1 = aSource1.iterator();
        Iterator<_E2> tIterator2 = aSource2.iterator();

        while (tIterator1.hasNext() && tIterator2.hasNext()) {
            aBuilder.add(Tuple2.create(tIterator1.next(), tIterator2.next()));
        }

        return aBuilder.build();
    }

    /**
     * 与えられたリストを、(要素, 添字番号)の{@link Tuple2}のリストに変換して返します。
     *
     * @param <_E>
     *            要素型
     * @param aList
     * @return (要素, 添字番号)リスト
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
     * 与えられたリストの要素を、与えられた２つのFunctionで変換し、新しいListを作って返します。
     *
     * @param <_Source>
     * @param <_E1>
     * @param <_E2>
     * @param aSourceList
     * @param aFunction1
     * @param aFunction2
     * @return zip結合結果List
     */
    public static <_Source, _E1, _E2> List<Tuple2<_E1, _E2>> zip(List<_Source> aSourceList, Function<? super _Source, ? extends _E1> aFunction1, Function<? super _Source, ? extends _E2> aFunction2) {
        final Function<? super _Source, ? extends _E1> tFunction1 = aFunction1;
        final Function<? super _Source, ? extends _E2> tFunction2 = aFunction2;

        return map(aSourceList, new Function<_Source, Tuple2<_E1, _E2>>() {
            @Override
            public Tuple2<_E1, _E2> apply(_Source aSource) {
                return Tuple2.<_E1, _E2> create(tFunction1.apply(aSource), tFunction2.apply(aSource));
            }
        });
    }

    /**
     * {@link Tuple2}のリストから、Tupleの要素を分離した２つのリストを作成して返します。
     *
     * @param <_E1>
     * @param <_E2>
     * @param aTupleList
     * @return 分離された2つのリストを含むタプル
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

    /**
     * {@link Tuple2}のリストから、{@linkplain Tuple2#get1()}のみを抽出したリストを生成して返します。
     *
     * @param aTupleList
     *            元となるリスト
     * @return get1の結果のみを含むリスト
     */
    public static <_E1, _E2> List<_E1> unzip1(List<Tuple2<_E1, _E2>> aTupleList) {
        List<_E1> tResult = new ArrayList<_E1>();
        List<Tuple2<_E1, _E2>> tSource = aTupleList;
        for (Tuple2<_E1, _E2> tTuple2 : tSource) {
            tResult.add(tTuple2.get1());
        }

        return tResult;
    }

    /**
     * {@link Tuple2}のリストから、{@linkplain Tuple2#get2()}のみを抽出したリストを生成して返します。
     *
     * @param aTupleList
     *            元となるリスト
     * @return get2の結果のみを含むリスト
     */
    public static <_E1, _E2> List<_E2> unzip2(List<Tuple2<_E1, _E2>> aTupleList) {
        List<_E2> tResult = new ArrayList<_E2>();
        List<Tuple2<_E1, _E2>> tSource = aTupleList;
        for (Tuple2<_E1, _E2> tTuple2 : tSource) {
            tResult.add(tTuple2.get2());
        }

        return tResult;
    }

    /**
     * {@link Tuple2}のコレクションから、{@linkplain Tuple2#get1()}
     * とaTargetが等価であるものを探して返します。
     *
     *
     * @param aTupleList
     *            検索対象となるコレクション
     * @param aTarget
     *            検索対象
     * @return {@linkplain Tuple2#get1()}.equals(aTarget)がtrueとなるTuple。
     *         見つからなかった場合はNone
     */
    public static <_E1, _E2> Option<Tuple2<_E1, _E2>> findElement1(Iterable<Tuple2<_E1, _E2>> aTupleList, final _E1 aTarget) {
        return find(aTupleList, new Predicate<Tuple2<_E1, _E2>>() {
            @Override
            public boolean apply(Tuple2<_E1, _E2> aTuple) {
                return aTarget.equals(aTuple.get1());
            }
        });
    }

    /**
     * {@link Tuple2}のリストから、{@linkplain Tuple2#get1()}
     * とaTargetが等価であるものを探し、インデックス番号を返します。
     *
     * @param aTupleList
     *            検索対象となるリスト
     * @param aTarget
     * @return
     */
    public static <_E1, _E2> int findElementIndex1(List<Tuple2<_E1, _E2>> aTupleList, final _E1 aTarget) {
        return findIndex(aTupleList, new Predicate<Tuple2<_E1, _E2>>() {
            @Override
            public boolean apply(Tuple2<_E1, _E2> aTuple) {
                return aTuple.get1().equals(aTarget);
            }
        });
    }

    /**
     * {@link Tuple2}のコレクションから、{@linkplain Tuple2#get2()}
     * とaTargetが等価であるものを探して返します。
     *
     * @param aTupleList
     *            検索対象となるコレクション
     * @param aTarget
     *            検索対象
     * @return {@linkplain Tuple2#get2()}.equals(aTarget)がtrueとなるTuple。
     *         見つからなかった場合はNone
     */
    public static <_E1, _E2> Option<Tuple2<_E1, _E2>> findElement2(Iterable<Tuple2<_E1, _E2>> aTupleList, final _E2 aTarget) {
        return find(aTupleList, new Predicate<Tuple2<_E1, _E2>>() {
            @Override
            public boolean apply(Tuple2<_E1, _E2> aTuple) {
                return aTarget.equals(aTuple.get2());
            }
        });
    }

    /**
     * {@link Tuple2}のリストから、{@linkplain Tuple2#get2()}
     * とaTargetが等価であるものを探し、インデックス番号を返します。
     *
     * @param aTupleList
     *            検索対象となるリスト
     * @param aTarget
     * @return
     */
    public static <_E1, _E2> int findElementIndex2(List<Tuple2<_E1, _E2>> aTupleList, final _E2 aTarget) {
        return findIndex(aTupleList, new Predicate<Tuple2<_E1, _E2>>() {
            @Override
            public boolean apply(Tuple2<_E1, _E2> aTuple) {
                return aTuple.get2().equals(aTarget);
            }
        });
    }

    /**
     * 与えられたコレクションを文字列化します。
     *
     * @param aSource
     *            文字列化するコレクション
     * @param aStart
     *            先頭につける文字列
     * @param aSep
     *            要素の間につける文字列
     * @param aEnd
     *            最後につける文字列
     * @return 生成された文字列
     */
    public static String mkString(Iterable<?> aSource, String aStart, String aSep, String aEnd) {
        return appendString(new StringBuilder(), aSource, aStart, aSep, aEnd).toString();
    }

    /**
     * 与えられたコレクションを文字列化し、与えられた{@linkplain StringBuilder}に追加します。
     *
     * @param aBuilder
     *            文字を追加するBuilder
     * @param aSource
     *            文字列化するコレクション
     * @param aStart
     *            先頭につける文字列
     * @param aSep
     *            要素の間につける文字列
     * @param aEnd
     *            最後につける文字列
     * @return aBuilderと同じインスタンス
     */
    public static StringBuilder appendString(StringBuilder aBuilder, Iterable<?> aSource, String aStart, String aSep, String aEnd) {
        boolean tIsFirst = true;

        StringBuilder tBuilder = aBuilder;
        tBuilder.append(aStart);

        for (Object tObject : aSource) {
            if (tIsFirst) {
                tIsFirst = false;
            } else {
                tBuilder.append(aSep);
            }

            tBuilder.append(tObject.toString());
        }

        tBuilder.append(aEnd);

        return tBuilder;
    }
}
