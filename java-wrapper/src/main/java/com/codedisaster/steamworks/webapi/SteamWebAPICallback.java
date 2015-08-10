package com.codedisaster.steamworks.webapi;

import com.codedisaster.steamworks.SteamResult;
import com.codedisaster.steamworks.webapi.SteamLeaderboards.FindOrCreateLeaderboardResult;
import com.codedisaster.steamworks.webapi.SteamLeaderboards.GetLeaderboardEntriesResult;
import com.codedisaster.steamworks.webapi.SteamLeaderboards.GetLeaderboardsForGameResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.AdjustAgreementResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.CancelAgreementResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.FinalizeTxnResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.GetReportResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.GetUserAgreementInfoResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.GetUserInfoResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.InitTxnResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.MicroTxnResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.ProcessAgreementResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.QueryTxnResult;
import com.codedisaster.steamworks.webapi.SteamMicroTxn.RefundTxnResult;

/**
 *
 * @author Francisco "Franz" Bischoff
 */
public interface SteamWebAPICallback {

    // Micro Transactions
    void onMicroTxnGetUserInfo(MicroTxnResult result, GetUserInfoResult params); //

    void onMicroTxnInitTxn(MicroTxnResult result, InitTxnResult params); //

    void onMicroTxnFinalizeTxn(MicroTxnResult result, FinalizeTxnResult params); //

    void onMicroTxnQueryTxn(MicroTxnResult result, QueryTxnResult params); //

    void onMicroTxnRefundTxn(MicroTxnResult result, RefundTxnResult params); //

    void onMicroTxnGetReport(MicroTxnResult result, GetReportResult params); //

    void onMicroTxnProcessAgreement(MicroTxnResult result, ProcessAgreementResult params);

    void onMicroTxnGetUserAgreementInfo(MicroTxnResult result, GetUserAgreementInfoResult params);

    void onMicroTxnCancelAgreement(MicroTxnResult result, CancelAgreementResult params);

    void onMicroTxnAdjustAgreement(MicroTxnResult result, AdjustAgreementResult params);

    // Leaderboards
    void onDeleteLeaderboard(SteamResult result);

    void onFindOrCreateLeaderboard(SteamResult result, FindOrCreateLeaderboardResult params);

    void onGetLeaderboardEntries(SteamResult result, GetLeaderboardEntriesResult params);

    void onGetLeaderboardsForGame(SteamResult result, GetLeaderboardsForGameResult params);

    void onResetLeaderboard(SteamResult result);

    void onSetLeaderboardScore(SteamResult result);
}
