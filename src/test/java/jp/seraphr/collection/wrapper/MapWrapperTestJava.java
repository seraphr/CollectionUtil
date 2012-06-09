package jp.seraphr.collection.wrapper;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import jp.seraphr.common.Converter;
import jp.seraphr.common.Tuple2;

import org.junit.Test;

public class MapWrapperTestJava {

    @Test
    public void testMap() {
        Map<Integer, Integer> tMap = new HashMap<Integer, Integer>();
        tMap.put(1, 2);
        tMap.put(2, 3);
        tMap.put(3, 4);


        MapWrapper<Integer, Integer> tWrapper = new MapWrapper<Integer, Integer>(tMap);


        MapWrapper<String, String> tMapped = tWrapper.map(new Converter<Tuple2<Integer, Integer>, Tuple2<String, String>>() {
            @Override
            public Tuple2<String, String> convert(final Tuple2<Integer, Integer> aSource) {
                return Tuple2.create(aSource.get1().toString(), aSource.get2().toString());
            }
        });


        Map<String, String> tExpectedMap = new HashMap<String, String>();
        tExpectedMap.put("1", "2");
        tExpectedMap.put("2", "3");
        tExpectedMap.put("3", "4");

        assertEquals(tExpectedMap, tMapped.unwrap());
    }
}
