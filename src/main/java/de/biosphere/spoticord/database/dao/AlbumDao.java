package de.biosphere.spoticord.database.dao;

import java.util.Map;

public interface AlbumDao {

    /**
     * Returns a map of the albums with the most database entry on this guild. The
     * <code>code</code> argument specifies the length of the map. Map contains
     * {@link String} as name of the album and {@link Integer} as the amount of
     * entrys in the database.
     * 
     * @param guildId the Snowflake id of the guild that the user is part of
     * @param userId  the Snowflake id of the user
     * @param count   the length of the map
     * @return A sorted map with <code>code</code> entrys
     */
    Map<String, Integer> getTopAlbum(final String guildId, final String userId, final Integer count);

    Map<String, Integer> getTopAlbum(final String guildId, final Integer count);

}