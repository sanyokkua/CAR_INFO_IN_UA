package ua.kostenko.carinfo.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import org.junit.Assert;
import org.junit.Test;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;

public class StringTest {

    @Test
    public void testString() {
        Map<String, Object> params = new HashMap<>();

        StringJoiner stringJoiner = new StringJoiner(" and ", " where ", " ");
        stringJoiner.setEmptyValue("");

        params.put("d.dep_code", null);
        params.put("d.dep_name", null);
        params.put("d.dep_email", null);
        params.put("d.dep_addr", null);

        params.entrySet().stream()
                .filter(stringObjectEntry -> Objects.nonNull(stringObjectEntry.getValue()))
                .map(entry -> entry.getKey() + " = " + entry.getValue())
                .forEach(stringJoiner::add);
        System.out.println(stringJoiner.toString());
    }

    @Test
    public void paramsHolder() {
        ParamsHolder build = new ParamsHolderBuilder().param("hello", 1).param("world", "2").build();
        ParamsHolder build2 = new ParamsHolderBuilder().param("hello", 1).param("world", "2").build();
        int i = build.hashCode();
        int i1 = build2.hashCode();
        Assert.assertEquals(i, i1);
    }

    @Test
    public void testParamsHolder() {
        ParamsHolder build1 = new ParamsHolderBuilder().param("hello", 1).param("world", "2").build();
        ParamsHolder build2 = new ParamsHolderBuilder().param("world", "2").param("hello", 1).build();
        ParamsHolder build3 = new ParamsHolderBuilder().param("hello", 1).param("world", 2).build();
        String s1 = build1.toString();
        String s2 = build2.toString();
        String s3 = build3.toString();
        Assert.assertEquals(s1, s2);
        Assert.assertNotEquals(s1, s3);
    }
}
