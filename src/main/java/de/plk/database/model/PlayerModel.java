package de.plk.database.model;

import de.plk.database.meta.Column;
import de.plk.database.meta.Table;
import de.plk.database.sql.type.ColumnDataType;

import java.util.UUID;

@Table(tableName = "player__model")
public class PlayerModel extends AbstractModel {

    @Column(
            primary = true,
            dataType = ColumnDataType.VARCHAR,
            columnName = "uuid",
            size = 100
    )
    private UUID uuid;

    @Column(
            dataType = ColumnDataType.VARCHAR,
            columnName = "name",
            size = 16
    )
    private String name;

    /**
     * Creates a new instance of a {@link PlayerModel}.
     *
     * @param uuid The uuid of the player.
     * @param name The name of the player.
     */
    public PlayerModel(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    /**
     * Returns the players name.
     *
     * @return The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the players uuid.
     *
     * @return The uuid of the player.
     */
    public UUID getUuid() {
        return uuid;
    }
}
