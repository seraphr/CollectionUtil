package jp.seraphr.collection.wrapper;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import jp.seraphr.collection.Converter;

import org.junit.Test;

public class ListWrapperTestJava {

    @Test
    public void test() {
        List<String> tList = Arrays.asList("1", "2", "3", "4");
        ListWrapper<String> tWrapper = new ListWrapper<String>(tList);

        List<Integer> tMapped = tWrapper.map(new Converter<String, Integer>() {
            @Override
            public Integer convert(String aSource) {
                return Integer.valueOf(aSource);
            }
        }).unwrap();

        assertEquals(Arrays.asList(1, 2, 3, 4), tMapped);
    }
}
