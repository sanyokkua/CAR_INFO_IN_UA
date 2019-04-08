package ua.kostenko.carinfo.common;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

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
}
