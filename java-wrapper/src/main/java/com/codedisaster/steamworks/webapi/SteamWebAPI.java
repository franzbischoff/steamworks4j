package com.codedisaster.steamworks.webapi;

import com.codedisaster.steamworks.SteamResult;
import com.codedisaster.steamworks.webapi.SteamLeaderboards.DeleteLeaderboard;
import com.codedisaster.steamworks.webapi.SteamLeaderboards.DisplayType;
import com.codedisaster.steamworks.webapi.SteamLeaderboards.FindOrCreateLeaderboard;
import com.codedisaster.steamworks.webapi.SteamLeaderboards.FindOrCreateLeaderboardResult;
import com.codedisaster.steamworks.webapi.SteamLeaderboards.GetLeaderboardEntries;
import com.codedisaster.steamworks.webapi.SteamLeaderboards.GetLeaderboardEntriesResult;
import com.codedisaster.steamworks.webapi.SteamLeaderboards.GetLeaderboardsForGameResult;
import com.codedisaster.steamworks.webapi.SteamLeaderboards.LeaderBoard;
import com.codedisaster.steamworks.webapi.SteamLeaderboards.LeaderboardEntry;
import com.codedisaster.steamworks.webapi.SteamLeaderboards.ResetLeaderBoard;
import com.codedisaster.steamworks.webapi.SteamLeaderboards.SetLeaderboardScore;
import com.codedisaster.steamworks.webapi.SteamLeaderboards.SortMethod;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.AccountStatus;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.AdjustAgreement;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.AdjustAgreementResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.Agreement;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.AgreementStatus;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.CancelAgreement;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.CancelAgreementResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.Currency;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.ErrorCode;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.FinalizeTxn;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.FinalizeTxnResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.GetReport;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.GetReportResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.GetUserAgreementInfo;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.GetUserAgreementInfoResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.GetUserInfo;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.GetUserInfoResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.InitTxn;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.InitTxnResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.Item;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.MicroTxnResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.Order;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.OrderStatus;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.Period;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.ProcessAgreement;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.ProcessAgreementResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.QueryTxn;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.QueryTxnResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.RefundTxn;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.RefundTxnResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.FormData;
import us.monoid.web.Resty;
import static us.monoid.web.Resty.content;
import static us.monoid.web.Resty.data;
import static us.monoid.web.Resty.form;


public class SteamWebAPI {

    static private long appId;
    static private String webApiKey;
    static private boolean sandBox = true;
    static private SteamWebAPICallback callbacks;
    static private ExecutorService pool;


    public static void init(long appid, String webapikey, boolean sandbox, SteamWebAPICallback callback) {
        appId = appid;
        webApiKey = webapikey;
        sandBox = sandbox;
        callbacks = callback;
        pool = Executors.newFixedThreadPool(5);
    }

    public static void dispose() {
        pool.shutdown();
        callbacks = null;
    }

    private SteamWebAPI() {
    }

    public static class Leaderboards {

        public static void deleteLeaderboard(final DeleteLeaderboard request) {
            pool.submit(
                    new Runnable() {

                        @Override
                        public void run() {

                            Resty r = new Resty();
                            String url = "https://api.steampowered.com/ISteamLeaderboards/DeleteLeaderboard/V0001";
                            SteamResult result;

                            try {

                                List<FormData> datas = new ArrayList<FormData>();

                                datas.add(data("key", webApiKey));
                                datas.add(data("appid", String.valueOf(appId)));
                                datas.add(data("name", request.name));

                                JSONObject object = r.json(url, form((FormData[]) datas.toArray())).object();

                                object = object.getJSONObject("result");

                                result = SteamResult.byValue(object.getInt("result"));

                            } catch (IOException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = SteamResult.UnexpectedError;

                            } catch (JSONException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = SteamResult.UnexpectedError;
                            }

                            callbacks.onDeleteLeaderboard(result);
                        }
                    }
            );
        }

        public static void findOrCreateLeaderboard(final FindOrCreateLeaderboard request) {
            pool.submit(
                    new Runnable() {

                        @Override
                        public void run() {

                            Resty r = new Resty();
                            String url = "https://api.steampowered.com/ISteamLeaderboards/FindOrCreateLeaderboard/V0002";
                            SteamResult result;

                            FindOrCreateLeaderboardResult params = new FindOrCreateLeaderboardResult();

                            try {

                                List<FormData> datas = new ArrayList<FormData>();

                                datas.add(data("key", webApiKey));
                                datas.add(data("appid", String.valueOf(appId)));
                                datas.add(data("name", request.name));
                                datas.add(data("sortmethod", request.sortMethod.toString()));
                                datas.add(data("displaytype", request.displayType.toString()));
                                datas.add(data("createifnotfound", request.createIfNotFound ? "1" : "0"));
                                datas.add(data("onlytrustedwrites", request.onlyTrustedWrites ? "1" : "0"));
                                datas.add(data("onlyfriendsreads", request.onlyFriendsReads ? "1" : "0"));

                                JSONObject object = r.json(url, form((FormData[]) datas.toArray())).object();

                                object = object.getJSONObject("result");

                                result = SteamResult.byValue(object.getInt("result"));

                                if (result.equals(SteamResult.OK)) {
                                    object = object.getJSONObject("leaderboard");

                                    if (object.getLong("leaderBoardID") > 0) {
                                        params.leaderBoard = new LeaderBoard();

                                        params.leaderBoard.name = object.getString("leaderboardName");
                                        params.leaderBoard.boardId = object.getInt("leaderBoardID");
                                        params.leaderBoard.entries = object.getInt("leaderBoardID");
                                        params.leaderBoard.sortMethod = SortMethod.fromString(object.getString("leaderBoardSortMethod"));
                                        params.leaderBoard.displayType = DisplayType.fromString(object.getString("leaderBoardDisplayType"));
                                        params.leaderBoard.onlyTrustedWrites = object.getBoolean("onlytrustedwrites");
                                        params.leaderBoard.onlyFriendsReads = object.getBoolean("onlyfriendsreads");
                                    } else {
                                        result = SteamResult.Fail;
                                    }
                                }

                            } catch (IOException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = SteamResult.UnexpectedError;

                            } catch (JSONException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = SteamResult.UnexpectedError;
                            }

                            callbacks.onFindOrCreateLeaderboard(result, params);
                        }
                    }
            );
        }

        public static void getLeaderboardEntries(final GetLeaderboardEntries request) {
            pool.submit(
                    new Runnable() {

                        @Override
                        public void run() {

                            Resty r = new Resty();
                            String url = "https://api.steampowered.com/ISteamLeaderboards/GetLeaderboardEntries/V0001/?key=" + webApiKey
                                    + "&appid=" + appId
                                    + "&rangestart=" + request.rangeStart
                                    + "&rangeend=" + request.rangeEnd
                                    + "&leaderboardid=" + request.boardId
                                    + "&datarequest=" + request.dataRequest.toString()
                                    + "&steamid=" + request.steamId;
                            
                            SteamResult result;

                            GetLeaderboardEntriesResult params = new GetLeaderboardEntriesResult();

                            try {
                                JSONObject object = r.json(url).object();

                                object = object.getJSONObject("leaderboardEntryInformation");

                                if (object.getInt("totalLeaderBoardEntryCount") > 0) {
                                    result = SteamResult.OK;

                                    params.appId = object.getInt("appID");
                                    params.boardId = object.getInt("leaderboardID");
                                    params.count = object.getInt("totalLeaderBoardEntryCount");

                                    JSONArray entries = object.getJSONArray("leaderboardEntries");

                                    if (entries.length() > 0) {

                                        int length = entries.length();

                                        for (int i = 0; i < length; i++) {
                                            JSONObject entryobj = entries.getJSONObject(i);
                                            LeaderboardEntry entry = new LeaderboardEntry();

                                            entry.steamId = entryobj.getLong("steamID");
                                            entry.score = entryobj.getInt("score");
                                            entry.rank = entryobj.getInt("rank");
                                            entry.ugcId = entryobj.getInt("ugcid");

                                            params.entries.add(entry);
                                        }
                                    }
                                } else {
                                    result = SteamResult.Fail;
                                }

                            } catch (IOException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = SteamResult.UnexpectedError;

                            } catch (JSONException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = SteamResult.UnexpectedError;
                            }

                            callbacks.onGetLeaderboardEntries(result, params);
                        }
                    }
            );
        }

        public static void getLeaderboardsForGame() {
            pool.submit(
                    new Runnable() {

                        @Override
                        public void run() {

                            Resty r = new Resty();
                            String url = "https://api.steampowered.com/ISteamLeaderboards/GetLeaderboardsForGame/V0002/?key=" + webApiKey
                                    + "&appid=" + appId;
                            
                            SteamResult result;

                            GetLeaderboardsForGameResult params = new GetLeaderboardsForGameResult();

                            try {
                                JSONObject object = r.json(url).object();
                                
                                object = object.getJSONObject("response");

                                result = SteamResult.byValue(object.getInt("result"));

                                if (result.equals(SteamResult.OK)) {
                                    JSONArray boards = object.getJSONArray("leaderboards");

                                    for (int i = 0; i < boards.length(); i++) {
                                        JSONObject boardobj = boards.getJSONObject(i);

                                        LeaderBoard entry = new LeaderBoard();

                                        entry.boardId = boardobj.getInt("id");
                                        entry.name = boardobj.getString("name");
                                        entry.entries = boardobj.getInt("entries");
                                        entry.sortMethod = SortMethod.fromString(boardobj.getString("sortmethod"));
                                        entry.displayType = DisplayType.fromString(boardobj.getString("displaytype"));
                                        entry.onlyTrustedWrites = boardobj.getBoolean("onlytrustedwrites");
                                        entry.onlyFriendsReads = boardobj.getBoolean("onlyfriendsreads");

                                        if (boardobj.has("display_name")) {
                                            entry.displayName = boardobj.getString("display_name");
                                        }

                                        params.leaderBoards.add(entry);
                                    }
                                }

                            } catch (IOException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = SteamResult.UnexpectedError;

                            } catch (JSONException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = SteamResult.UnexpectedError;
                            }

                            callbacks.onGetLeaderboardsForGame(result, params);
                        }
                    }
            );
        }

        public static void resetLeaderboard(final ResetLeaderBoard request) {
            pool.submit(
                    new Runnable() {

                        @Override
                        public void run() {

                            Resty r = new Resty();
                            String url = "https://api.steampowered.com/ISteamLeaderboards/ResetLeaderboard/V0001";
                            SteamResult result;

                            try {

                                List<FormData> datas = new ArrayList<FormData>();

                                datas.add(data("key", webApiKey));
                                datas.add(data("appid", String.valueOf(appId)));
                                datas.add(data("leaderboardid", String.valueOf(request.boardId)));

                                JSONObject object = r.json(url, form((FormData[]) datas.toArray())).object();

                                object = object.getJSONObject("result");

                                result = SteamResult.byValue(object.getInt("result"));

                            } catch (IOException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = SteamResult.UnexpectedError;

                            } catch (JSONException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = SteamResult.UnexpectedError;
                            }

                            callbacks.onResetLeaderboard(result);
                        }
                    }
            );
        }

        public static void setLeaderboardScore(final SetLeaderboardScore request) {
            pool.submit(
                    new Runnable() {

                        @Override
                        public void run() {

                            Resty r = new Resty();
                            String url = "https://api.steampowered.com/ISteamLeaderboards/SetLeaderboardScore/V0001";
                            SteamResult result;

                            try {

                                List<FormData> datas = new ArrayList<FormData>();

                                datas.add(data("key", webApiKey));
                                datas.add(data("appid", String.valueOf(appId)));
                                datas.add(data("leaderboardid", String.valueOf(request.boardId)));
                                datas.add(data("steamid", String.valueOf(request.steamId)));
                                datas.add(data("score", String.valueOf(request.score)));
                                datas.add(data("scoremethod", request.scoreMethod.toString()));

                                if (request.details != null) {
                                    datas.add(data("details", content(request.details)));
                                }

                                JSONObject object = r.json(url, form((FormData[]) datas.toArray())).object();

                                object = object.getJSONObject("result");

                                result = SteamResult.byValue(object.getInt("result"));

                            } catch (IOException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = SteamResult.UnexpectedError;

                            } catch (JSONException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = SteamResult.UnexpectedError;
                            }

                            callbacks.onSetLeaderboardScore(result);
                        }
                    }
            );
        }

        private Leaderboards() {
        }
    }

    public static class MicroTxn {

        public static void getUserInfo(final GetUserInfo request) {

            pool.submit(
                    new Runnable() {

                        @Override
                        public void run() {
                            Resty r = new Resty();
                            String url = "https://api.steampowered.com/ISteamMicroTxn" + (sandBox ? "Sandbox" : "") + "/GetUserInfo/V0002/?key=" + webApiKey
                                    + "&steamid=" + request.steamId
                                    + "&ipaddress=" + request.ipAddress;
                            MicroTxnResult result;
                            GetUserInfoResult params = new GetUserInfoResult();

                            try {

                                JSONObject object = r.json(url).object();

                                result = MicroTxnResult.fromString(object.getString("result"));

                                if (result.equals(MicroTxnResult.OK)) {

                                    object = object.getJSONObject("params");

                                    params.country = object.getString("country");
                                    params.currency = Currency.fromString(object.getString("currency"));
                                    params.state = object.getString("state");
                                    params.status = AccountStatus.fromString(object.getString("status"));

                                } else {
                                    object = object.getJSONObject("error");

                                    ErrorCode code = ErrorCode.byOrdinal(object.getInt("errorcode"));
                                    String desc = object.getString("errordesc");

                                    params.errorCode = code;
                                    params.errorDesc = desc;
                                }
                            } catch (IOException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = MicroTxnResult.Failure;
                                params.errorCode = ErrorCode.OPERATION_FAILED;
                                params.errorDesc = ex.getMessage();

                            } catch (JSONException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = MicroTxnResult.Failure;
                                params.errorCode = ErrorCode.OPERATION_FAILED;
                                params.errorDesc = ex.getMessage();

                            }

                            callbacks.onMicroTxnGetUserInfo(result, params);
                        }
                    }
            );
        }

        public static void initTxn(final InitTxn request) {

            pool.submit(
                    new Runnable() {

                        @Override
                        public void run() {

                            Resty r = new Resty();
                            String url = "https://api.steampowered.com/ISteamMicroTxn" + (sandBox ? "Sandbox" : "") + "/InitTxn/V0003";
                            MicroTxnResult result;
                            InitTxnResult params = new InitTxnResult();

                            try {

                                List<FormData> datas = new ArrayList<FormData>();

                                datas.add(data("key", webApiKey));
                                datas.add(data("appid", String.valueOf(appId)));
                                datas.add(data("orderid", String.valueOf(request.orderId)));
                                datas.add(data("steamid", String.valueOf(request.steamId)));
                                datas.add(data("language", request.language));
                                datas.add(data("currency", request.currency.toString()));

                                if (request.items.size() > 0) {
                                    datas.add(data("itemcount", String.valueOf(request.items.size())));

                                    int i = 0;
                                    for (Item item : request.items) {
                                        datas.add(data("itemid[" + i + "]", String.valueOf(item.itemId)));
                                        datas.add(data("qty[" + i + "]", String.valueOf(item.qty)));
                                        datas.add(data("amount[" + i + "]", String.valueOf(item.amount)));
                                        datas.add(data("description[" + i + "]", item.description));
                                        datas.add(data("category[" + i + "]", item.category));

                                        if (item.agreement != null) {
                                            Agreement agreement = item.agreement;
                                            datas.add(data("billingtype[" + i + "]", agreement.billingType.toString()));
                                            datas.add(data("startdate[" + i + "]", agreement.startDate));
                                            datas.add(data("enddate[" + i + "]", agreement.endDate));
                                            datas.add(data("period[" + i + "]", agreement.period.toString()));
                                            datas.add(data("frequency[" + i + "]", String.valueOf(agreement.frequency)));
                                            datas.add(data("recurringamt[" + i + "]", String.valueOf(agreement.recurringAmt)));
                                        }

                                        i++;
                                    }
                                }

                                JSONObject object = r.json(url, form((FormData[]) datas.toArray())).object();

                                result = MicroTxnResult.fromString(object.getString("result"));

                                if (result.equals(MicroTxnResult.OK)) {

                                    object = object.getJSONObject("params");

                                    params.orderId = object.getLong("orderid");
                                    params.transId = object.getLong("transid");
                                    params.steamUrl = object.getString("steamurl");

                                } else {
                                    object = object.getJSONObject("error");

                                    ErrorCode code = ErrorCode.byOrdinal(object.getInt("errorcode"));
                                    String desc = object.getString("errordesc");

                                    params.errorCode = code;
                                    params.errorDesc = desc;
                                }
                            } catch (IOException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = MicroTxnResult.Failure;
                                params.errorCode = ErrorCode.OPERATION_FAILED;
                                params.errorDesc = ex.getMessage();

                            } catch (JSONException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = MicroTxnResult.Failure;
                                params.errorCode = ErrorCode.OPERATION_FAILED;
                                params.errorDesc = ex.getMessage();

                            }

                            callbacks.onMicroTxnInitTxn(result, params);
                        }
                    }
            );
        }

        public static void finalizeTxn(final FinalizeTxn request) {

            pool.submit(
                    new Runnable() {

                        @Override
                        public void run() {

                            Resty r = new Resty();
                            String url = "https://api.steampowered.com/ISteamMicroTxn" + (sandBox ? "Sandbox" : "") + "/FinalizeTxn/V0002";
                            MicroTxnResult result;
                            FinalizeTxnResult params = new FinalizeTxnResult();

                            try {

                                List<FormData> datas = new ArrayList<FormData>();

                                datas.add(data("key", webApiKey));
                                datas.add(data("appid", String.valueOf(appId)));
                                datas.add(data("orderid", String.valueOf(request.orderId)));

                                JSONObject object = r.json(url, form((FormData[]) datas.toArray())).object();

                                result = MicroTxnResult.fromString(object.getString("result"));

                                if (result.equals(MicroTxnResult.OK)) {

                                    object = object.getJSONObject("params");

                                    params.orderId = object.getLong("orderid");
                                    params.transId = object.getLong("transid");

                                    // not an Array, why Valve?
                                    if (object.getJSONObject("agreements").has("agreement[0]")) {

                                        JSONObject agreement = object.getJSONObject("agreements").getJSONObject("agreement[0]");

                                        Agreement agr = new Agreement();

                                        agr.itemId = agreement.getLong("itemid");
                                        agr.agreementId = agreement.getLong("agreementid");

                                        params.agreement = agr;
                                    }
                                } else {
                                    object = object.getJSONObject("error");

                                    ErrorCode code = ErrorCode.byOrdinal(object.getInt("errorcode"));
                                    String desc = object.getString("errordesc");

                                    params.errorCode = code;
                                    params.errorDesc = desc;
                                }
                            } catch (IOException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = MicroTxnResult.Failure;
                                params.errorCode = ErrorCode.OPERATION_FAILED;
                                params.errorDesc = ex.getMessage();

                            } catch (JSONException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = MicroTxnResult.Failure;
                                params.errorCode = ErrorCode.OPERATION_FAILED;
                                params.errorDesc = ex.getMessage();

                            }

                            callbacks.onMicroTxnFinalizeTxn(result, params);
                        }
                    }
            );
        }

        public static void queryTxn(final QueryTxn request) {

            pool.submit(
                    new Runnable() {

                        @Override
                        public void run() {
                            Resty r = new Resty();
                            String url = "https://api.steampowered.com/ISteamMicroTxn" + (sandBox ? "Sandbox" : "") + "/QueryTxn/V0002/?key=" + webApiKey
                                    + "&appid=" + appId
                                    + "&orderid=" + request.orderId
                                    + "&transid=" + request.transId;
                            
                            MicroTxnResult result;
                            QueryTxnResult params = new QueryTxnResult();

                            try {

                                JSONObject object = r.json(url).object();

                                result = MicroTxnResult.fromString(object.getString("result"));

                                if (result.equals(MicroTxnResult.OK)) {

                                    object = object.getJSONObject("params");

                                    params.order.orderId = object.getLong("orderid");
                                    params.order.transId = object.getLong("transid");
                                    params.order.steamId = object.getLong("steamid");
                                    params.order.status = OrderStatus.fromString(object.getString("status"));
                                    params.order.currency = Currency.fromString(object.getString("currency"));
                                    params.order.time = object.getString("time");
                                    params.order.country = object.getString("country");
                                    params.order.usState = object.getString("usstate");

                                    if (object.has("agreementid")) {
                                        params.order.agreement = new Agreement();
                                        params.order.agreement.timeCreated = object.getString("timecreated");
                                        params.order.agreement.agreementId = object.getLong("agreementid");
                                        params.order.agreement.status = AgreementStatus.fromString(object.getString("agreementstatus"));
                                    }

                                    JSONArray items = object.getJSONArray("items");

                                    if (items.length() > 0) {

                                        for (int i = 0; i < items.length(); i++) {
                                            JSONObject itemobj = items.getJSONObject(i);
                                            Item item = new Item();

                                            item.itemId = itemobj.getLong("itemid");
                                            item.qty = itemobj.getInt("qty");
                                            item.amount = itemobj.getInt("amount");
                                            item.vat = itemobj.getInt("vat");
                                            item.itemStatus = OrderStatus.fromString(itemobj.getString("itemstatus"));

                                            params.order.items.add(item);
                                        }
                                    }

                                } else {
                                    object = object.getJSONObject("error");

                                    ErrorCode code = ErrorCode.byOrdinal(object.getInt("errorcode"));
                                    String desc = object.getString("errordesc");

                                    params.errorCode = code;
                                    params.errorDesc = desc;
                                }
                            } catch (IOException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = MicroTxnResult.Failure;
                                params.errorCode = ErrorCode.OPERATION_FAILED;
                                params.errorDesc = ex.getMessage();

                            } catch (JSONException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = MicroTxnResult.Failure;
                                params.errorCode = ErrorCode.OPERATION_FAILED;
                                params.errorDesc = ex.getMessage();

                            }

                            callbacks.onMicroTxnQueryTxn(result, params);
                        }
                    }
            );
        }

        public static void refundTxn(final RefundTxn request) {

            pool.submit(
                    new Runnable() {

                        @Override
                        public void run() {

                            Resty r = new Resty();
                            String url = "https://api.steampowered.com/ISteamMicroTxn" + (sandBox ? "Sandbox" : "") + "/RefundTxn/V0002";
                            MicroTxnResult result;
                            RefundTxnResult params = new RefundTxnResult();

                            try {

                                List<FormData> datas = new ArrayList<FormData>();

                                datas.add(data("key", webApiKey));
                                datas.add(data("appid", String.valueOf(appId)));
                                datas.add(data("orderid", String.valueOf(request.orderId)));

                                JSONObject object = r.json(url, form((FormData[]) datas.toArray())).object();

                                result = MicroTxnResult.fromString(object.getString("result"));

                                if (result.equals(MicroTxnResult.OK)) {

                                    object = object.getJSONObject("params");

                                    params.orderId = object.getLong("orderid");
                                    params.transId = object.getLong("transid");

                                } else {
                                    object = object.getJSONObject("error");

                                    ErrorCode code = ErrorCode.byOrdinal(object.getInt("errorcode"));
                                    String desc = object.getString("errordesc");

                                    params.errorCode = code;
                                    params.errorDesc = desc;
                                }
                            } catch (IOException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = MicroTxnResult.Failure;
                                params.errorCode = ErrorCode.OPERATION_FAILED;
                                params.errorDesc = ex.getMessage();

                            } catch (JSONException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = MicroTxnResult.Failure;
                                params.errorCode = ErrorCode.OPERATION_FAILED;
                                params.errorDesc = ex.getMessage();

                            }

                            callbacks.onMicroTxnRefundTxn(result, params);
                        }
                    }
            );
        }

        public static void getReport(final GetReport request) {

            pool.submit(
                    new Runnable() {

                        @Override
                        public void run() {
                            Resty r = new Resty();
                            String url = "https://api.steampowered.com/ISteamMicroTxn" + (sandBox ? "Sandbox" : "") + "/GetReport/V0003/?key=" + webApiKey
                                    + "&appid=" + appId
                                    + "&type=" + request.type.toString()
                                    + "&time=" + request.getUTCDate()
                                    + "&maxresults=" + request.maxResults;
                            
                            MicroTxnResult result;
                            GetReportResult params = new GetReportResult();

                            try {

                                JSONObject object = r.json(url).object();

                                result = MicroTxnResult.fromString(object.getString("result"));

                                if (result.equals(MicroTxnResult.OK)) {

                                    object = object.getJSONObject("params");

                                    JSONArray orders = object.getJSONArray("orders");

                                    if (orders.length() > 0) {

                                        for (int i = 0; i < orders.length(); i++) {
                                            JSONObject orderobj = orders.getJSONObject(i);
                                            Order order = new Order();

                                            order.orderId = orderobj.getLong("orderid");
                                            order.transId = orderobj.getLong("transid");
                                            order.steamId = orderobj.getLong("steamid");
                                            order.status = OrderStatus.fromString(orderobj.getString("status"));
                                            order.currency = Currency.fromString(orderobj.getString("currency"));
                                            order.time = orderobj.getString("time");
                                            order.country = orderobj.getString("country");
                                            order.usState = orderobj.getString("usstate");

                                            // TODO: agreement?
                                            JSONArray items = orderobj.getJSONArray("items");

                                            if (items.length() > 0) {

                                                for (int j = 0; j < items.length(); j++) {
                                                    JSONObject itemobj = items.getJSONObject(j);
                                                    Item item = new Item();

                                                    item.itemId = itemobj.getLong("itemid");
                                                    item.qty = itemobj.getInt("qty");
                                                    item.amount = itemobj.getInt("amount");
                                                    item.vat = itemobj.getInt("vat");
                                                    item.itemStatus = OrderStatus.fromString(itemobj.getString("itemstatus"));

                                                    order.items.add(item);
                                                }
                                            }

                                            params.orders.add(order);
                                        }
                                    }

                                } else {
                                    object = object.getJSONObject("error");

                                    ErrorCode code = ErrorCode.byOrdinal(object.getInt("errorcode"));
                                    String desc = object.getString("errordesc");

                                    params.errorCode = code;
                                    params.errorDesc = desc;
                                }
                            } catch (IOException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = MicroTxnResult.Failure;
                                params.errorCode = ErrorCode.OPERATION_FAILED;
                                params.errorDesc = ex.getMessage();

                            } catch (JSONException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = MicroTxnResult.Failure;
                                params.errorCode = ErrorCode.OPERATION_FAILED;
                                params.errorDesc = ex.getMessage();

                            }

                            callbacks.onMicroTxnGetReport(result, params);
                        }
                    }
            );
        }

        public static void cancelAgreement(final CancelAgreement request) {

            pool.submit(
                    new Runnable() {

                        @Override
                        public void run() {

                            Resty r = new Resty();
                            String url = "https://api.steampowered.com/ISteamMicroTxn" + (sandBox ? "Sandbox" : "") + "/CancelAgreement/V0001";
                            MicroTxnResult result;
                            CancelAgreementResult params = new CancelAgreementResult();

                            try {

                                List<FormData> datas = new ArrayList<FormData>();

                                datas.add(data("key", webApiKey));
                                datas.add(data("appid", String.valueOf(appId)));
                                datas.add(data("agreementid", String.valueOf(request.agreementId)));

                                JSONObject object = r.json(url, form((FormData[]) datas.toArray())).object();

                                result = MicroTxnResult.fromString(object.getString("result"));

                                if (result.equals(MicroTxnResult.OK)) {

                                    object = object.getJSONObject("params");

                                    params.agreementId = object.getLong("agreementid");

                                } else {
                                    object = object.getJSONObject("error");

                                    ErrorCode code = ErrorCode.byOrdinal(object.getInt("errorcode"));
                                    String desc = object.getString("errordesc");

                                    params.errorCode = code;
                                    params.errorDesc = desc;
                                }
                            } catch (IOException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = MicroTxnResult.Failure;
                                params.errorCode = ErrorCode.OPERATION_FAILED;
                                params.errorDesc = ex.getMessage();

                            } catch (JSONException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = MicroTxnResult.Failure;
                                params.errorCode = ErrorCode.OPERATION_FAILED;
                                params.errorDesc = ex.getMessage();

                            }

                            callbacks.onMicroTxnCancelAgreement(result, params);
                        }
                    }
            );
        }

        public static void processAgreement(final ProcessAgreement request) {

            pool.submit(
                    new Runnable() {

                        @Override
                        public void run() {

                            Resty r = new Resty();
                            String url = "https://api.steampowered.com/ISteamMicroTxn" + (sandBox ? "Sandbox" : "") + "/ProcessAgreement/V0001";
                            MicroTxnResult result;
                            ProcessAgreementResult params = new ProcessAgreementResult();

                            try {

                                List<FormData> datas = new ArrayList<FormData>();

                                datas.add(data("key", webApiKey));
                                datas.add(data("appid", String.valueOf(appId)));
                                datas.add(data("orderid", String.valueOf(request.orderId)));
                                datas.add(data("steamid", String.valueOf(request.steamId)));
                                datas.add(data("agreementid", String.valueOf(request.agreementId)));
                                datas.add(data("amount", String.valueOf(request.amount)));
                                datas.add(data("currency", request.currency.toString()));

                                JSONObject object = r.json(url, form((FormData[]) datas.toArray())).object();

                                result = MicroTxnResult.fromString(object.getString("result"));

                                if (result.equals(MicroTxnResult.OK)) {

                                    object = object.getJSONObject("params");

                                    params.orderId = object.getLong("orderid");
                                    params.transId = object.getLong("transid");
                                    params.agreementId = object.getLong("agreementid");

                                } else {
                                    object = object.getJSONObject("error");

                                    ErrorCode code = ErrorCode.byOrdinal(object.getInt("errorcode"));
                                    String desc = object.getString("errordesc");

                                    params.errorCode = code;
                                    params.errorDesc = desc;
                                }
                            } catch (IOException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = MicroTxnResult.Failure;
                                params.errorCode = ErrorCode.OPERATION_FAILED;
                                params.errorDesc = ex.getMessage();

                            } catch (JSONException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = MicroTxnResult.Failure;
                                params.errorCode = ErrorCode.OPERATION_FAILED;
                                params.errorDesc = ex.getMessage();

                            }

                            callbacks.onMicroTxnProcessAgreement(result, params);
                        }
                    }
            );
        }

        public static void getUserAgreementInfo(final GetUserAgreementInfo request) {

            pool.submit(
                    new Runnable() {

                        @Override
                        public void run() {
                            Resty r = new Resty();
                            String url = "https://api.steampowered.com/ISteamMicroTxn" + (sandBox ? "Sandbox" : "") + "/GetUserAgreementInfo/V0001/?key=" + webApiKey
                            + "&appid=" + appId
                            + "&steamid=" + request.steamId;
                            MicroTxnResult result;
                            GetUserAgreementInfoResult params = new GetUserAgreementInfoResult();

                            try {

                                JSONObject object = r.json(url).object();

                                result = MicroTxnResult.fromString(object.getString("result"));

                                if (result.equals(MicroTxnResult.OK)) {

                                    object = object.getJSONObject("params");

                                    if (object.getJSONObject("aggreements").has("aggreement[0]")) {

                                        JSONObject agreement = object.getJSONObject("aggreements").getJSONObject("aggreement[0]");

                                        params.agreement = new Agreement();
                                        params.agreement.agreementId = agreement.getLong("agreementid");
                                        params.agreement.itemId = agreement.getLong("itemid");
                                        params.agreement.status = AgreementStatus.fromString(agreement.getString("status"));
                                        params.agreement.period = Period.fromString(agreement.getString("period"));
                                        params.agreement.frequency = agreement.getInt("frequency");
                                        params.agreement.startDate = agreement.getString("startdate");
                                        params.agreement.endDate = agreement.getString("enddate");
                                        params.agreement.recurringAmt = agreement.getInt("recurringamt");
                                        params.agreement.currency = Currency.fromString(agreement.getString("currency"));
                                        params.agreement.timeCreated = agreement.getString("timecreated");
                                        params.agreement.lastPayment = agreement.getString("lastpayment");
                                        params.agreement.lastAmount = agreement.getInt("lastamount");
                                        params.agreement.nextPayment = agreement.getString("nextpayment");
                                        params.agreement.outstanding = agreement.getInt("outstanding");
                                        params.agreement.failedAttempts = agreement.getInt("failedattmpts");
                                    }

                                } else {
                                    object = object.getJSONObject("error");

                                    ErrorCode code = ErrorCode.byOrdinal(object.getInt("errorcode"));
                                    String desc = object.getString("errordesc");

                                    params.errorCode = code;
                                    params.errorDesc = desc;
                                }
                            } catch (IOException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = MicroTxnResult.Failure;
                                params.errorCode = ErrorCode.OPERATION_FAILED;
                                params.errorDesc = ex.getMessage();

                            } catch (JSONException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = MicroTxnResult.Failure;
                                params.errorCode = ErrorCode.OPERATION_FAILED;
                                params.errorDesc = ex.getMessage();

                            }

                            callbacks.onMicroTxnGetUserAgreementInfo(result, params);
                        }
                    }
            );
        }

        public static void adjustAgreement(final AdjustAgreement request) {

            pool.submit(
                    new Runnable() {

                        @Override
                        public void run() {

                            Resty r = new Resty();
                            String url = "https://api.steampowered.com/ISteamMicroTxn" + (sandBox ? "Sandbox" : "") + "/AdjustAgreement/V0001";
                            MicroTxnResult result;
                            AdjustAgreementResult params = new AdjustAgreementResult();

                            try {

                                List<FormData> datas = new ArrayList<FormData>();

                                datas.add(data("key", webApiKey));
                                datas.add(data("appid", String.valueOf(appId)));
                                datas.add(data("agreementid", String.valueOf(request.agreementId)));
                                datas.add(data("nextprocessdate", request.nextProcessDate));

                                JSONObject object = r.json(url, form((FormData[]) datas.toArray())).object();

                                result = MicroTxnResult.fromString(object.getString("result"));

                                if (result.equals(MicroTxnResult.OK)) {
                                    object = object.getJSONObject("params");

                                    params.agreementId = object.getLong("agreementid");
                                    params.nextProcessDate = object.getString("nextprocessdate");

                                } else {
                                    object = object.getJSONObject("error");

                                    ErrorCode code = ErrorCode.byOrdinal(object.getInt("errorcode"));
                                    String desc = object.getString("errordesc");

                                    params.errorCode = code;
                                    params.errorDesc = desc;
                                }
                            } catch (IOException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = MicroTxnResult.Failure;
                                params.errorCode = ErrorCode.OPERATION_FAILED;
                                params.errorDesc = ex.getMessage();

                            } catch (JSONException ex) {

                                Logger.getLogger(SteamWebAPI.class.getName()).log(Level.SEVERE, null, ex);
                                result = MicroTxnResult.Failure;
                                params.errorCode = ErrorCode.OPERATION_FAILED;
                                params.errorDesc = ex.getMessage();

                            }

                            callbacks.onMicroTxnAdjustAgreement(result, params);
                        }
                    }
            );
        }

        private MicroTxn() {
        }
    }
}
