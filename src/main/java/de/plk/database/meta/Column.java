package de.plk.database.meta;

import de.plk.database.sql.type.ColumnDataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents that the field with annotation
 * is column in the table of the model.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * Returns true if this column will be a primary key.
     *
     * @return True if this column will be a primary key.
     */
    boolean primary() default false;

    /**
     * Returns the data type of the column.
     *
     * @return The data type of the column.
     */
    ColumnDataType dataType();

    /**
     * Returns the name of the column.
     *
     * @return The name of the column.
     */
    String columnName();

    /**
     * Returns the length of data type of the column.
     *
     * @return The length of data type of the column.
     */
    int size() default 255;

}
