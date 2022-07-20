package com.luxuryshop.repository.utils;

import org.jooq.Field;
import org.jooq.TableField;
import org.jooq.impl.TableRecordImpl;
import org.jooq.impl.UpdatableRecordImpl;

import java.util.*;

public class MysqlUtil {
    private MysqlUtil() {
    }

    public static <T extends UpdatableRecordImpl<T>> Map<Field<?>, Object>
    recordToUpdateQueries(T record, Object o, TableField<T, ?>... ignoreFields) {
        record.from(o);
        Map<Field<?>, Object> values = new HashMap<>();
        for (Field<?> f : record.fields()) {
            if (record.getValue(f) != null) {
                if (Arrays.stream(ignoreFields).noneMatch(f::equals))
                    values.put(f, record.getValue(f));
            }
        }
        return values;
    }

    public static <T extends TableRecordImpl<T>> Map<Field<?>, Object>
    toInsertQueries(T record, Object o) {
        record.from(o);
        Map<Field<?>, Object> values = new HashMap<>();
        for (Field<?> f : record.fields()) {
            if (record.getValue(f) != null) {
                values.put(f, record.getValue(f));
            }
        }
        return values;
    }

    public static <T extends TableRecordImpl<T>> Map<Field<?>, Object>
    toInsertQueries(T record, Object o, List<String> fields) {
        record.from(o);
        final HashSet<String> fieldSet = new HashSet<>(fields);
        Map<Field<?>, Object> values = new HashMap<>();
        for (Field<?> f : record.fields()) {
            if (fieldSet.contains(f.getName())) {
                values.put(f, record.getValue(f));
            }
        }
        return values;
    }

}
