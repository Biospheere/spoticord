package de.biosphere.spoticord.database.dao;

import java.util.List;
import java.util.Map;

import de.biosphere.spoticord.database.model.SpotifyTrack;

public interface TrackDao {

    /**
     * @return the amount of track database entrys
     */
    Integer getTrackAmount();

    /**
     * 
     * @param guildId the Snowflake id of the guild
     * @return the amount of listen database entrys
     */
    Integer getListensAmount();

    Integer getListensAmount(final String guildId);

    Integer getListensAmount(final String guildId, final String userId);

    /**
     * Insert a new listen entry into the database
     * 
     * @param spotifyTrack the {@link SpotifyTrack}
     * @param userId       the Snowflake id of the user
     * @param guildId      the Snowflake id of the guild that the user is part of
     */
    void insertTrack(final SpotifyTrack spotifyTrack, final String userId, final String guildId);

    /**
     * 
     * @param guildId
     * @param userId
     * @param count
     * @param lastDays
     * @return
     */
    Map<SpotifyTrack, Integer> getTopTracks(final String guildId, final String userId, final Integer count,
            final Integer lastDays);

    List<SpotifyTrack> getLastTracks(final String guildId);

    List<SpotifyTrack> getLastTracks(final String guildId, final String userId);

}