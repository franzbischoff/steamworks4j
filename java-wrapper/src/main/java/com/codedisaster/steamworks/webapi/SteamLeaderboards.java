package com.codedisaster.steamworks.webapi;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Francisco "Franz" Bischoff
 */
public class SteamLeaderboards {

    public enum DisplayType {

        Numeric,
        Seconds,
        Milliseconds;

        private static final DisplayType[] values = values();

        static DisplayType byOrdinal(int value) {
            return values[value];
        }

        static DisplayType fromString(String string) {
            if (string != null) {
                try {
                    return valueOf(string);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(SteamLeaderboards.class.getName()).log(Level.WARNING, null, ex);
                }
            }
            return null;
        }
    }

    public enum RequestType {

        RequestGlobal,
        RequestAroundUser,
        RequestFriends,
        RequestUsers;

        private static final RequestType[] values = values();

        static RequestType byOrdinal(int value) {
            return values[value];
        }

        static RequestType fromString(String string) {
            if (string != null) {
                try {
                    return valueOf(string);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(SteamLeaderboards.class.getName()).log(Level.WARNING, null, ex);
                }
            }
            return null;
        }
    }

    public enum SortMethod {

        Ascending,
        Descending;

        private static final SortMethod[] values = values();

        static SortMethod byOrdinal(int value) {
            return values[value];
        }

        static SortMethod fromString(String string) {
            if (string != null) {
                try {
                    return valueOf(string);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(SteamLeaderboards.class.getName()).log(Level.WARNING, null, ex);
                }
            }
            return null;
        }
    }

    public enum UploadScoreMethod {

        None,
        KeepBest,
        ForceUpdate;

        private static final UploadScoreMethod[] values = values();

        static UploadScoreMethod byOrdinal(int value) {
            return values[value];
        }

        static UploadScoreMethod fromString(String string) {
            if (string != null) {
                try {
                    return valueOf(string);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(SteamLeaderboards.class.getName()).log(Level.WARNING, null, ex);
                }
            }
            return null;
        }
    }

    public static final class DeleteLeaderboard {

        public String name;
    }

    public static final class FindOrCreateLeaderboard {

        public String name;
        public SortMethod sortMethod;
        public DisplayType displayType;
        public boolean createIfNotFound;
        public boolean onlyTrustedWrites;
        public boolean onlyFriendsReads;

        public FindOrCreateLeaderboard() {
            // default values as stated by Valve
            sortMethod = SortMethod.Ascending;
            displayType = DisplayType.Numeric;
            createIfNotFound = true;
            onlyFriendsReads = false;
            onlyTrustedWrites = false;
        }
    }

    public static final class FindOrCreateLeaderboardResult {

        public LeaderBoard leaderBoard;
    }

    public static final class GetLeaderboardEntries {

        public int rangeStart;
        public int rangeEnd;
        public int boardId;
        public RequestType dataRequest;

        public long steamId;

        public GetLeaderboardEntries() {
            // default values as stated by Valve
            rangeStart = 0;
            rangeEnd = Integer.MAX_VALUE;
            dataRequest = RequestType.RequestGlobal;
        }
    }

    public static final class GetLeaderboardEntriesResult {

        public long appId;
        public long boardId;
        public int count;
        public List<LeaderboardEntry> entries;

        public GetLeaderboardEntriesResult() {
            entries = new ArrayList<LeaderboardEntry>();
        }
    }

    public static final class GetLeaderboardsForGameResult {

        public List<LeaderBoard> leaderBoards;

        public GetLeaderboardsForGameResult() {
            leaderBoards = new ArrayList<LeaderBoard>();
        }
    }

    public static final class LeaderBoard {

        public String name;
        public String displayName;
        public int boardId;
        public int entries;
        public SortMethod sortMethod;
        public DisplayType displayType;
        public boolean createifnotfound;
        public boolean onlyTrustedWrites;
        public boolean onlyFriendsReads;
    }

    public static final class LeaderboardEntry {

        public long steamId;
        public int score;
        public int rank;
        public long ugcId;
    }

    public static final class ResetLeaderBoard {

        public int boardId;
    }

    public static final class SetLeaderboardScore {

        public int boardId;
        public long steamId;
        public int score;
        public UploadScoreMethod scoreMethod;
        // max 256 bytes
        public byte[] details = null;
    }
}
