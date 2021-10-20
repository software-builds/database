package de.plk.database.model;

import de.plk.database.DatabaseType;
import de.plk.database.meta.Column;
import de.plk.database.meta.Table;
import de.plk.database.sql.type.ColumnDataType;

@Table(tableName = "player__models2")
public class PlayerModel extends AbstractModel {

    @Column(
            primary = true,
            dataType = ColumnDataType.INT,
            columnName = "id"
    )
    private int id;

    @Column(
            dataType = ColumnDataType.VARCHAR,
            columnName = "name"
    )
    private String name;

    public PlayerModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
