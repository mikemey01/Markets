# Markets
Markets is an Android app for creating automatic option trades using the TradeKing API. Trades are opened and closed based on the position of various technical indicators. TradeKing’s API uses the FIXML protocol for creating orders. Any exchange traded option symbols can be entered, analyzed, and traded.

# Assumptions
Markets makes a few assumptions to speed up the rate at which trades are opened and closed.
* Put and call options are opened depending on direction of the trend.
* Premium is never sold, contracts are only purchased
* Strike price: purchased at-the-money for both put and calls
* Expiry Date: if running on Monday or Tuesday, the current week’s expiration. If trading Wednesday - Friday, the following weeks expiration. This is to keep delta up so fewer movements of the underlying are required.
* Quantity: 10 contracts - usually only requires a few cents movement in the contract price to overcome the brokers commissions.
* Limit orders are used for both opening and closing trades.

# Indicators
The following indicators are currently being calculated by the app. By default the indicators are calculated for the one minute time-frame. This can be adjusted using the five minute API call to the broker.
* RSI
* MACD/Signal
* Slow Stochastics
* 50 period EMA
* 200 period EMA

# Process