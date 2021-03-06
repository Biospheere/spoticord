package de.biosphere.spoticord.database.dao;

import java.util.Map;

public interface AlbumDao {

    /**
     * Returns a map of the albums with the most database entry on this guild. The
     * <code>count</code> argument specifies the length of the map. Map contains
     * {@link String} as name of the album and {@link Integer} as the amount of
     * entries in the database.
     * 
     * @param guildId  the Snowflake id of the guild that the user is part of
     * @param userId   the Snowflake id of the user
     * @param count    the length of the map
     * @param lastDays the last days for data collection when 0 all data
     * @return A sorted map with <code>count</code> entries
     */
    Map<String, Integer> getTopAlbum(final String guildId, final String userId, final Integer count,
            final Integer lastDays);

    Map<String, Integer> getTopAlbum(final String guildId, final Integer count, final Integer lastDays);

    /**
     * @return the amount of albums database entries
     */
    Integer getAlbumAmount();

}