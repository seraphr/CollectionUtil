package jp.seraphr.collection.wrapper;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jp.seraphr.common.Converter;

import org.junit.Test;

public class MapWrapperTestJava {

    @Test
    public void testMap() {
        Map<Integer, Integer> tMap = new HashMap<Integer, Integer>();
        tMap.put(1, 2);
        tMap.put(2, 3);
        tMap.put(3, 4);


        MapWrapper<Integer, Integer> tWrapper = new MapWrapper<Integer, Integer>(tMap);


        MapWrapper<String, String> tMapped = tWrapper.map(new Converter<Map.Entry<Integer, Integer>, Map.Entry<String, String>>() {
            @Override
            public Entry<String, String> convert(final Entry<Integer, Integer> aSource) {
                return new Entry<String, String>() {

                    @Override
                    public String setValue(String aValue) {
                        return null;
                    }

                    @Override
                    public String getValue() {
                        return aSource.getValue().toString();
                    }

                    @Override
                    public String getKey() {
                        return aSource.getKey().toString();
                    }
                };
            }
        });


        Map<String, String> tExpectedMap = new HashMap<String, String>();
        tExpectedMap.put("1", "2");
        tExpectedMap.put("2", "3");
        tExpectedMap.put("3", "4");

        assertEquals(tExpectedMap, tMapped.unwrap());
    }
}
