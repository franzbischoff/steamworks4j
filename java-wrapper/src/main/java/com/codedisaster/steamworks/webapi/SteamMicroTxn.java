package com.codedisaster.steamworks.webapi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Francisco "Franz" Bischoff
 */
public class SteamMicroTxn {

    public enum AccountStatus {

        Active,
        Trusted;

        private static final AccountStatus[] values = values();

        static AccountStatus byOrdinal(int value) {
            return values[value];
        }

        static AccountStatus fromString(String string) {
            if (string != null) {
                try {
                    return valueOf(string);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(SteamMicroTxn.class.getName()).log(Level.WARNING, null, ex);
                }
            }
            return null;
        }
    }

    public enum AgreementStatus {

        Init,
        Active,
        Inactive,
        Canceled;

        private static final AgreementStatus[] values = values();

        static AgreementStatus byOrdinal(int value) {
            return values[value];
        }

        static AgreementStatus fromString(String string) {
            if (string != null) {
                try {
                    return valueOf(string);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(SteamMicroTxn.class.getName()).log(Level.WARNING, null, ex);
                }
            }
            return null;
        }
    }

    public enum BillingType {

        Steam,
        Game;

        private static final BillingType[] values = values();

        static BillingType byOrdinal(int value) {
            return values[value];
        }

        static BillingType fromString(String string) {
            if (string != null) {
                try {
                    return valueOf(string);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(SteamMicroTxn.class.getName()).log(Level.WARNING, null, ex);
                }
            }
            return null;
        }
    }

    public enum Currency {

        USD, //United States Dollar, reported in cents
        GBP, //United Kingdom Pound, reported in pence
        EUR, //European Union Euro, reported in eurocents
        RUB, //Russian Rouble, reported in kopeks
        BRL, //Brazilian Reals, reported in centavos
        CAD, //Canadian Dollars, reported in cents
        IDR, //Indonesian Rupiah, reported in sen. Must result in whole Rupiah (e.g. 1000, not 1050).
        JPY, //Japanese Yen, reported in sen. Must result in whole Yen (e.g. 1000, not 1050).
        MYR, //Malaysian Ringgit, reported in sen
        MXN, //Mexican Peso, reported in centavos
        NZD, //New Zealand Dollar, reported in cents
        NOK, //Norwegian Krone, reported in Øre
        PHP, //Philippine Peso, reported in centavos
        SGD, //Singapore Dollar, reported in cents
        THB, //Thai Baht, reported in satang
        TRY; //Turkish Lira, reported in kuruş

        private static final Currency[] values = values();

        static Currency byOrdinal(int value) {
            return values[value];
        }

        static Currency fromString(String string) {
            if (string != null) {
                try {
                    return valueOf(string);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(SteamMicroTxn.class.getName()).log(Level.WARNING, null, ex);
                }
            }
            return null;
        }
    }

    public enum ErrorCode {

        SUCCESS(1), // Success
        OPERATION_FAILED(2), // The operation failed
        INVALID_PARAMETER(3), // Invalid parameter
        INTERNAL_ERROR(4), // Internal error
        USER_NOT_APPROVED(5), // User has not approved transaction
        ALREADY_COMMITTED(6), // Transaction has already been committed
        USER_OFFLINE(7), // User is not logged in
        CURRENCY_MISMATCH(8), // Currency does not match user's Steam Account currency
        ACCOUNT_NOT_EXISTS(9), // Account does not exist or is temporarily unavailable
        DENIED_BY_USER(10), // Transaction has been denied by user
        DENIED_BY_COUNTRY(11), // Transaction has been denied because user is in a restricted country
        DENIED_BY_BILLING(12), // Transaction has been denied because billing agreement is not active
        BILLING_TYPE_NOT_GAME(13), // Billing agreement cannot be processed because it is not type GAME
        BILLING_ON_HOLD(14), // Billing agreement is on hold due to billing dispute or chargeback
        BILLING_TYPE_NOT_STEAM(15), // Billing agreement cannot be processed because it is not type STEAM
        USER_ALREADY_AGREED(16), // User already has a billing agreement for this game
        INSUFICIENT_FUNDS(100), // Insufficient funds
        TIMEOUT(101), // Time limit for finalization has been exceeded
        ACCOUNT_DISABLED(102), // Account is disabled
        ACCOUNT_NOT_ALLOWED(103), // Account is not allowed to purchase
        TRANSACTION_DENIED(104), // Transaction denied due to fraud detection
        NO_CACHED_PAYMENT(105), // No cached payment method
        WOULD_EXCEED_SPENDING_LIMIT(106), // Transaction would exceed the spending limit of the billing agreement// Transaction would exceed the spending limit of the billing agreement

        /**
         * If this is returned(), we missed to "port" an Steam error code above.
         */
        UnknownErrorCode_NotImplementedByAPI(0);

        private final int code;
        static private final ErrorCode[] valuesLookupTable;

        ErrorCode(int code) {
            this.code = code;
        }

        static public ErrorCode byOrdinal(int resultCode) {
            if (resultCode < valuesLookupTable.length) {
                return valuesLookupTable[resultCode];
            } else {
                return UnknownErrorCode_NotImplementedByAPI;
            }
        }

        static {
            ErrorCode[] values = values();
            int maxResultCode = 0;

            for (ErrorCode value : values) {
                maxResultCode = Math.max(maxResultCode, value.code);
            }

            valuesLookupTable = new ErrorCode[maxResultCode + 1];

            for (ErrorCode value : values) {
                valuesLookupTable[value.code] = value;
            }
        }
    }

    public enum MicroTxnResult {

        OK,
        Failure;

        private static final MicroTxnResult[] values = values();

        static MicroTxnResult byOrdinal(int value) {
            return values[value];
        }

        static MicroTxnResult fromString(String string) {
            if (string != null) {
                try {
                    return valueOf(string);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(SteamMicroTxn.class.getName()).log(Level.WARNING, null, ex);
                }
            }
            return null;
        }
    }

    public enum OrderStatus {

        Init, // Order has been created but not authorized by user
        Approved, // Order has been approved by user
        Succeeded, // Order has been successfully processed
        Failed, // Order has failed or been denied
        Refunded, // Order has been refunded and product should be revoked by the game
        PartialRefund, // One or more items in the cart have been refunded. Check itemstatus field of each item for details.
        Chargedback, // Order is fraudulent or disputed and product should be revoked by the game
        InDispute; // Order is fraudulent or disputed and product should be revoked by the game// Order is fraudulent or disputed and product should be revoked by the game// Order is fraudulent or disputed and product should be revoked by the game// Order is fraudulent or disputed and product should be revoked by the game// Order is fraudulent or disputed and product should be revoked by the game// Order is fraudulent or disputed and product should be revoked by the game// Order is fraudulent or disputed and product should be revoked by the game

        private static final OrderStatus[] values = values();

        static OrderStatus byOrdinal(int value) {
            return values[value];
        }

        static OrderStatus fromString(String string) {
            if (string != null) {
                try {
                    return valueOf(string);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(SteamMicroTxn.class.getName()).log(Level.WARNING, null, ex);
                }
            }
            return null;
        }
    }

    public enum Period {

        day,
        week,
        month,
        year;

        private static final Period[] values = values();

        static Period byOrdinal(int value) {
            return values[value];
        }

        static Period fromString(String string) {
            if (string != null) {
                try {
                    return valueOf(string);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(SteamMicroTxn.class.getName()).log(Level.WARNING, null, ex);
                }
            }
            return null;
        }
    }

    public enum ReportType {

        STEAMSTORESALES, // Return all sales created through the Steam store
        SETTLEMENT, // Return all transactions (sales, refunds, chargebacks, etc.) for a game regardless of the interface through which it was purchased
        CHARGEBACK, // Return all refunded or charged back transactions for a game regardless of the interface through which it was purchased
        GAMESALES; // Return all sales created through the Steam web API// Return all sales created through the Steam web API// Return all sales created through the Steam web API// Return all sales created through the Steam web API

        private static final ReportType[] values = values();

        static ReportType byOrdinal(int value) {
            return values[value];
        }

        static ReportType fromString(String string) {
            if (string != null) {
                try {
                    return valueOf(string);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(SteamMicroTxn.class.getName()).log(Level.WARNING, null, ex);
                }
            }
            return null;
        }
    }

    public enum UserSession {

        client,
        web;

        private static final UserSession[] values = values();

        static UserSession byOrdinal(int value) {
            return values[value];
        }

        static UserSession fromString(String string) {
            if (string != null) {
                try {
                    return valueOf(string);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(SteamMicroTxn.class.getName()).log(Level.WARNING, null, ex);
                }
            }
            return null;
        }
    }

    public static final class Agreement {

        public long agreementId;
        public long itemId;
        public BillingType billingType;
        public AgreementStatus status;
        public Period period;
        public int frequency;
        public String startDate;
        public String endDate;
        public int recurringAmt;
        public Currency currency;
        public String timeCreated;
        public String lastPayment;
        public int lastAmount;
        public String nextPayment;
        public int outstanding;
        public int failedAttempts;
    }

    public static final class Item {

        public long itemId;
        public int qty;
        public int amount;
        public int vat;
        public String description;
        public String category;
        public OrderStatus itemStatus;
        public Agreement agreement;
    }

    public static final class Order {

        public long orderId;
        public long transId;
        public long steamId;
        public OrderStatus status;
        public Currency currency;
        public String time;
        public String country;
        public String usState;
        public Agreement agreement;

        public List<Item> items;

        public Order() {
            items = new ArrayList<Item>();
        }
    }

    public static final class AdjustAgreement {

        public long steamId;
        public long agreementId;
        public String nextProcessDate;
    }

    public static final class AdjustAgreementResult {

        public long agreementId;
        public String nextProcessDate;

        public ErrorCode errorCode;
        public String errorDesc;
    }

    public static final class CancelAgreement {

        public long steamId;
        public long agreementId;
    }

    public static final class CancelAgreementResult {

        public long agreementId;

        public ErrorCode errorCode;
        public String errorDesc;
    }

    public static final class FinalizeTxn {

        public long orderId;
    }

    public static final class FinalizeTxnResult {

        public long orderId;
        public long transId;

        Agreement agreement;

        public ErrorCode errorCode;
        public String errorDesc;
    }

    public static final class GetUserAgreementInfo {

        public long steamId;
    }

    public static final class GetUserAgreementInfoResult {

        public Agreement agreement;

        public ErrorCode errorCode;
        public String errorDesc;
    }

    public static final class GetReport {

        public ReportType type;
        public short maxResults;
        private String timestamp;

        public GetReport() {
            type = ReportType.STEAMSTORESALES;
            setUTCDate(new Date());
            maxResults = 1000;
        }

        public void setUTCDate(Date date) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'h:m:ss'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            timestamp = sdf.format(date);
        }

        public String getUTCDate() {
            return timestamp;
        }
    }

    public static final class GetReportResult {

        public List<Order> orders;

        public ErrorCode errorCode;
        public String errorDesc;

        public GetReportResult() {
            orders = new ArrayList<Order>();
        }
    }

    public static final class GetUserInfo {

        public long steamId;
        public String ipAddress;
    }

    public static final class GetUserInfoResult {

        public String state;
        public String country;
        public Currency currency;
        public AccountStatus status;

        public ErrorCode errorCode;
        public String errorDesc;
    }

    public static final class InitTxn {

        public long orderId;
        public long steamId;
        public String language;
        public Currency currency;
        public UserSession userSession;

        public List<Item> items;

        public InitTxn() {
            userSession = UserSession.client;
            items = new ArrayList<Item>();
        }
    }

    public static final class InitTxnResult {

        public long orderId;
        public long transId;
        public String steamUrl;

        public ErrorCode errorCode;
        public String errorDesc;
    }

    public static final class ProcessAgreement {

        public long orderId;
        public long steamId;
        public long agreementId;
        public int amount;
        public Currency currency;
    }

    public static final class ProcessAgreementResult {

        public long orderId;
        public long transId;
        public long agreementId;

        public ErrorCode errorCode;
        public String errorDesc;
    }

    public static final class QueryTxn {

        public long orderId;
        public long transId;
    }

    public static final class QueryTxnResult {

        public Order order;

        public ErrorCode errorCode;
        public String errorDesc;
    }

    public static final class RefundTxn {

        public long orderId;
    }

    public static final class RefundTxnResult {

        public long orderId;
        public long transId;

        public ErrorCode errorCode;
        public String errorDesc;
    }

}
