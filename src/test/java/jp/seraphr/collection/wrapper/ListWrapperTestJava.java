package jp.seraphr.collection.wrapper;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import jp.seraphr.common.Converter;
import jp.seraphr.common.Predicate;

import org.junit.Test;

public class ListWrapperTestJava {

    @Test
    public void testMapJava() {
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

    @Test
    public void testFilterJava() {
        List<Integer> tList = Arrays.asList(1, 2, 3, 4);
        ListWrapper<Integer> tWrapper = new ListWrapper<Integer>(tList);

        ListWrapper<Integer> t = tWrapper.filter(new Predicate<Integer>() {
            @Override
            public boolean apply(Integer aTarget) {
                return aTarget % 2 == 0;
            }
        });
        assertEquals(Arrays.asList(2, 4), t.unwrap());
    }
}
