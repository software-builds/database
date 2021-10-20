package de.plk.database.sql;

import de.plk.database.meta.Column;
import de.plk.database.meta.Table;
import de.plk.database.model.AbstractModel;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ReflectionHandler {

    private static final String SQL_TABLE_CREATION = "CREATE TABLE IF NOT EXISTS %s (%s);";

    private final Class<? extends AbstractModel> model;

    public ReflectionHandler(Class<? extends AbstractModel> model) {
        this.model = model;
    }

    public String tableCreation() {
        Set<Column> columnSet = getColumns();

        if (columnSet.size() == 0)
            throw new RuntimeException("Model cannot have 0 column annotations.");

        StringBuilder builder = new StringBuilder();
        Iterator<Column> columnIterator = columnSet.iterator();

        while (columnIterator.hasNext()) {
            Column column = columnIterator.next();

            String dataType = column.dataType().withSize(column.size());
            builder.append(column.columnName())
                    .append(" ")
                    .append(dataType);

            if (column.primary()) {
                builder.append(" ")
                        .append("PRIMARY KEY");
            }

            builder.append(", ");
        }

        builder.setLength(builder.length() - 2);

        return String.format(SQL_TABLE_CREATION,
                getTable().tableName(),
                builder.toString()
        );
    }

    public Set<Column> getColumns() {
        final Set<Column> columnSet = new HashSet<>();

        for (Field field : model.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                columnSet.add(column);
            }
        }

        return columnSet;
    }

    public Table getTable() {
        if (!model.isAnnotationPresent(Table.class))
            throw new RuntimeException("Class mus have an Table Annotation present.");

        return model.getAnnotation(Table.class);
    }

}
