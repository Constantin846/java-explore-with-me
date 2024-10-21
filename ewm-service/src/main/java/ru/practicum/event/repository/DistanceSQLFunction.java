package ru.practicum.event.repository;

import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.BasicTypeReference;
import org.hibernate.type.SqlTypes;

public class DistanceSQLFunction extends StandardSQLFunction { // todo delete
    private static final BasicTypeReference<Boolean> RETURN_TYPE =
            new BasicTypeReference<>("boolean", Boolean.class, SqlTypes.BOOLEAN);

    public DistanceSQLFunction(String name) {
        super(name, true, RETURN_TYPE);
    }

    public DistanceSQLFunction(String name, BasicTypeReference<?> type) {
        super(name, type);
    }

    public DistanceSQLFunction(String name, boolean useParentheses, BasicTypeReference<?> type) {
        super(name, useParentheses, type);
    }
}
