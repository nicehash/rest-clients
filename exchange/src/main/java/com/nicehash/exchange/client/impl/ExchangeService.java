package com.nicehash.exchange.client.impl;

import com.nicehash.common.domain.OrderRelOp;
import com.nicehash.exchange.client.domain.OrderSide;
import com.nicehash.exchange.client.domain.OrderType;
import com.nicehash.exchange.client.domain.SingleOrListObject;
import com.nicehash.exchange.client.domain.SortDirection;
import com.nicehash.exchange.client.domain.TimeInForce;
import com.nicehash.exchange.client.domain.account.Account;
import com.nicehash.exchange.client.domain.account.FeeStatus;
import com.nicehash.exchange.client.domain.account.NewOrderResponse;
import com.nicehash.exchange.client.domain.account.Order;
import com.nicehash.exchange.client.domain.account.Orders;
import com.nicehash.exchange.client.domain.account.TradeHistoryItems;
import com.nicehash.exchange.client.domain.account.Trades;
import com.nicehash.exchange.client.domain.event.ListenKey;
import com.nicehash.exchange.client.domain.general.Assets;
import com.nicehash.exchange.client.domain.general.ExchangeInfo;
import com.nicehash.exchange.client.domain.general.ServerTime;
import com.nicehash.exchange.client.domain.market.AggTrades;
import com.nicehash.exchange.client.domain.market.BookTicker;
import com.nicehash.exchange.client.domain.market.Candlesticks;
import com.nicehash.exchange.client.domain.market.OrderBook;
import com.nicehash.exchange.client.domain.market.TickerPrice;
import com.nicehash.exchange.client.domain.market.TickerStatistic;
import com.nicehash.external.spi.ServiceBuilderConfiguration;
import com.nicehash.external.utils.HeaderConstants;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Exchange's REST API URL mappings and endpoint security configuration.
 */
@ServiceBuilderConfiguration(
    url = "${nicehash.url:http://localhost:8080/}",
    builder = ExchangeServiceBuilder.class
)
public interface ExchangeService {

    // General endpoints

    @GET("api/v1/ping")
    Call<Void> ping();

    @GET("api/v1/time")
    Call<ServerTime> getServerTime();

    @GET("api/v1/exchangeInfo")
    Call<ExchangeInfo> getExchangeInfo();

    @GET
    Call<Assets> getAllAssets(@Url String url);

    // Market data endpoints

    @GET("api/v1/depth")
    Call<OrderBook> getOrderBook(@Query("symbol") String symbol, @Query("limit") Integer limit);

    @GET("api/v1/trades")
    Call<TradeHistoryItems> getTrades(@Query("symbol") String symbol, @Query("limit") Integer limit);

    @GET("api/v1/historicalTrades")
    Call<TradeHistoryItems> getHistoricalTrades(@Query("symbol") String symbol, @Query("limit") Integer limit, @Query("tradeNumber") Long tradeNumber);

    @GET("api/v1/aggTrades")
    Call<AggTrades> getAggTrades(@Query("symbol") String symbol, @Query("fromId") String fromId, @Query("limit") Integer limit,
                                 @Query("startTime") Long startTime, @Query("endTime") Long endTime);

    @GET("api/v1/klines")
    Call<Candlesticks> getCandlestickBars(@Query("symbol") String symbol, @Query("interval") String interval, @Query("limit") Integer limit,
                                          @Query("startTime") Long startTime, @Query("endTime") Long endTime);

    @GET("api/v1/ticker/24hr")
    Call<SingleOrListObject<TickerStatistic>> get24HrPriceStatistics(@Query("symbol") String symbol);

    @GET("api/v1/ticker/price")
    Call<SingleOrListObject<TickerPrice>> getLatestPrice(@Query("symbol") String symbol);

    @GET("api/v1/ticker/bookTicker")
    Call<SingleOrListObject<BookTicker>> getBookTicker(@Query("symbol") String symbol);

    // Account endpoints

    @Headers(HeaderConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @POST("api/v1/order")
    Call<NewOrderResponse> newOrder(@Query("symbol") String symbol, @Query("side") OrderSide side, @Query("type") OrderType type,
                                    @Query("timeInForce") TimeInForce timeInForce, @Query("quantity") BigDecimal quantity, @Query("price") BigDecimal price,
                                    @Query(value = "clientOrderId", encoded = true) String clientOrderId, @Query("stopPrice") BigDecimal stopPrice,
                                    @Query("marketMaxAmount") BigDecimal marketMaxAmount, @Query("icebergQty") BigDecimal icebergQty,
                                    @Query("recvWindow") Long recvWindow, @Query("timestamp") Long timestamp);

    @Headers(HeaderConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @POST("api/v1/order/test")
    Call<Void> newOrderTest(@Query("symbol") String symbol, @Query("side") OrderSide side, @Query("type") OrderType type,
                            @Query("timeInForce") TimeInForce timeInForce, @Query("quantity") BigDecimal quantity, @Query("price") BigDecimal price,
                            @Query(value = "clientOrderId", encoded = true) String clientOrderId, @Query("stopPrice") BigDecimal stopPrice,
                            @Query("marketMaxAmount") BigDecimal marketMaxAmount, @Query("icebergQty") BigDecimal icebergQty,
                            @Query("recvWindow") Long recvWindow, @Query("timestamp") Long timestamp);

    @Headers(HeaderConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @GET("api/v1/order")
    Call<Order> getOrderStatus(@Query("symbol") String symbol, @Query("orderId") UUID orderId,
                               @Query(value = "clientOrderId", encoded = true) String clientOrderId,
                               @Query("recvWindow") Long recvWindow, @Query("timestamp") Long timestamp);

    @Headers(HeaderConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @DELETE("api/v1/order")
    Call<Void> cancelOrder(@Query("symbol") String symbol, @Query("orderId") UUID orderId,
                           @Query(value = "clientOrderId", encoded = true) String origClientOrderId,
                           @Query("recvWindow") Long recvWindow, @Query("timestamp") Long timestamp);

    @Headers(HeaderConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @GET("api/v1/openOrders")
    Call<Orders> getOpenOrders(@Query("symbol") String symbol, @Query("sortDirection") SortDirection sortDirection, @Query("recvWindow") Long recvWindow, @Query("timestamp") Long timestamp);

    @Headers(HeaderConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @GET("api/v1/orderTrades")
    Call<Trades> getOrderTrades(@Query("symbol") String symbol, @Query("orderId") UUID orderId, @Query(value = "clientOrderId", encoded = true) String clientOrderId,
                                @Query("recvWindow") Long recvWindow, @Query("timestamp") Long timestamp);

    @Headers(HeaderConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @GET("api/v1/allOrders")
    Call<Orders> getAllOrders(@Query("symbol") String symbol, @Query("relationalOp") OrderRelOp relationalOp, @Query("submitNumber") Long submitNumber,
                              @Query("submitTime") Long submitTime, @Query("terminated") Boolean terminated, @Query("limit") Integer limit,
                              @Query("sortDirection") SortDirection sortDirection, @Query("recvWindow") Long recvWindow, @Query("timestamp") Long timestamp);

    @Headers(HeaderConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @GET("api/v1/account")
    Call<Account> getAccount(@Query("recvWindow") Long recvWindow, @Query("timestamp") Long timestamp);

    @Headers(HeaderConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @GET("api/v1/feeStatus")
    Call<FeeStatus> getFeeStatus(@Query("recvWindow") Long recvWindow, @Query("timestamp") Long timestamp);

    @Headers(HeaderConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @GET("api/v1/myTrades")
    Call<Trades> getMyTrades(@Query("symbol") String symbol, @Query("relationalOp") OrderRelOp relationalOp, @Query("tradeNumber") Long tradeNumber,
                             @Query("limit") Integer limit, @Query("sortDirection") SortDirection sortDirection, @Query("recvWindow") Long recvWindow, @Query("timestamp") Long timestamp);

    // User stream endpoints

    @Headers(HeaderConstants.ENDPOINT_SECURITY_TYPE_APIKEY_HEADER)
    @POST("api/v1/userDataStream")
    Call<ListenKey> startUserDataStream();

    @Headers(HeaderConstants.ENDPOINT_SECURITY_TYPE_APIKEY_HEADER)
    @PUT("api/v1/userDataStream")
    Call<Void> keepAliveUserDataStream(@Query(value = "listenKey", encoded = true) String listenKey);

    @Headers(HeaderConstants.ENDPOINT_SECURITY_TYPE_APIKEY_HEADER)
    @DELETE("api/v1/userDataStream")
    Call<Void> closeAliveUserDataStream(@Query(value = "listenKey", encoded = true) String listenKey);
}
