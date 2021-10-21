package de.plk.database.sql;

import de.plk.database.meta.Column;
import de.plk.database.meta.Table;
import de.plk.database.model.AbstractModel;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Handles the reading of the annotation from the model classes.
 */
public class ReflectionHandler {

    /**
     * The template sql-command to create a new table.
     */
    private static final String SQL_TABLE_CREATION = "CREATE TABLE IF NOT EXISTS %s (%s);";

    /**
     * The class reference of the model we should handle with.
     */
    private final Class<? extends AbstractModel> modelClass;

    /**
     * Creates a new instance of this class.
     *
     * @param modelClass The class reference of the model we should handle with.
     */
    public ReflectionHandler(Class<? extends AbstractModel> modelClass) {
        this.modelClass = modelClass;
    }

    /**
     * Returns the sql-command to create the table with.
     *
     * @return The sql-command to create the table with.
     */
    public String tableCreation() {
        final Set<Column> columnSet = getColumns();

        if (columnSet.size() == 0)
            throw new RuntimeException("Model cannot have 0 column annotations in model class. (" + modelClass.getSimpleName() + ")");

        final StringBuilder builder = new StringBuilder();

        final Iterator<Column> columnIterator = columnSet.iterator();
        while (columnIterator.hasNext()) {
            final Column column = columnIterator.next();
            final String dataType = column.dataType().withSize(column.size());

            builder.append(column.columnName()).append(" ").append(dataType);

            if (column.primary())
                builder.append(" ").append("PRIMARY KEY");

            builder.append(", ");
        }

        builder.setLength(builder.length() - 2);

        return String.format(SQL_TABLE_CREATION,
                getTable().tableName(),
                builder.toString()
        );
    }

    /**
     * Get all column annotation of the model class.
     *
     * @return All column annotation of the model class.
     */
    public Set<Column> getColumns() {
        final Set<Column> columnSet = new HashSet<>();

        for (final Field field : modelClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                final Column column = field.getAnnotation(Column.class);
                columnSet.add(column);
            }
        }

        return columnSet;
    }

    /**
     * Get the table annotation of the model class.
     *
     * @return The table annotation of the model class.
     */
    public Table getTable() {
        if (!modelClass.isAnnotationPresent(Table.class))
            throw new RuntimeException("Model cannot have noone table annotation. (" + modelClass.getSimpleName() + ")");

        return modelClass.getAnnotation(Table.class);
    }

}
