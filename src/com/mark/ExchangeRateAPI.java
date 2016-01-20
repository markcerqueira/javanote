package com.mark;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper the Foreign Exchange Rate API available at http://fixer.io/
 *
 * Example usage:
 *
 *    ExchangeRateAPI.getExchangeRate(ExchangeRateAPI.USA, ExchangeRateAPI.KOREA);
 *
 *    ExchangeRateAPI.getExchangeRates(ExchangeRateAPI.JAPAN, ExchangeRateAPI.CHINA, ExchangeRateAPI.Korea);
 *
 */
public class ExchangeRateAPI {

    // Currency code constants. Can be accessed like ExchangeRateAPI.CANADA or ExchangeRateAPI.USA
    public static final String CANADA = "CAD";
    public static final String CHINA = "CNY";
    public static final String ENGLAND = "GBP";
    public static final String EURO = "EUR";
    public static final String JAPAN = "JPY";
    public static final String KOREA = "KRW";
    public static final String USA = "USD";

    /**
     *  This will test our currency exchange methods by calling the method multiple times with different currency
     *  codes.
     */
    public static void testExchangeRateAPI() {
        LogHelper.log(getExchangeRate(USA));

        LogHelper.newLine();

        LogHelper.log(getExchangeRate(USA, CANADA));
        LogHelper.log(getExchangeRate(KOREA, USA));
        LogHelper.log(getExchangeRate(JAPAN, ENGLAND));
        LogHelper.log(getExchangeRate(CHINA, EURO));
        LogHelper.log(getExchangeRate(CHINA, JAPAN));
        LogHelper.log(getExchangeRate(USA, KOREA));

        LogHelper.newLine();

        LogHelper.log(getExchangeRates(USA, CANADA, CHINA, ENGLAND, EURO, JAPAN, KOREA));

        // Test the error case
        // LogHelper.log(getExchangeRate("USS", "FAKE"));
    }

    /**
     *  Returns the exchange rate from baseCurrency to various currencies.
     *
     *      1 USD = 1.005 CHF, 7.0441 HRK, 18.119 MXN, 16.665 ZAR, 67.615 INR, 6.5786 CNY, 36.279 THB, 1.4421 AUD, ...
     *
     *  This calls to the API endpoint on fixer.io at: https://api.fixer.io/latest?base=USD
     *
     */
    public static String getExchangeRate(String baseCurrency) {
        return getExchangeRates(baseCurrency, null);
    }

    /**
     *  Returns the exchange rate from baseCurrency to toCurrency as a String. Calling this method with currency
     *  constants USD and JPY will return:
     *
     *      1 USD = 117.89 JPY
     *
     *  This calls to the API endpoint on fixer.io at: https://api.fixer.io/latest?base=USD&symbols=JPY
     *
     */
    public static String getExchangeRate(String baseCurrency, String toCurrency) {
        // Calls the method below
        return getExchangeRates(baseCurrency, toCurrency);
    }

    /**
     *  Returns the exchange rate from baseCurrency to multiple toCurrencies as a String. Calling this method with
     *  currency constants USD and CNY, GBP, EUR will return:
     *
     *      1 USD = 6.5786 CNY, 0.70365 GBP, 0.92013 EUR
     *
     *  This calls to the API endpoint on fixer.io at: https://api.fixer.io/latest?base=USD&symbols=CNY,GBP,EUR
     *
     */
    public static String getExchangeRates(String baseCurrency, String... toCurrencies) {
        try {
            // Create the URL we are going to call to. Start with base URL:
            String buildUrl = "https://api.fixer.io/latest";

            // Then add the base currency...
            buildUrl += "?base=" + baseCurrency;

            URL url;
            // And each of the currencies we want to convert to (this can range from one to many different currencies)
            // If we do not do this, all available currencies will be returned
            if (toCurrencies != null && toCurrencies.length > 0) {
                buildUrl += "&symbols=";
                for (String currency : toCurrencies) {
                    buildUrl += currency + ",";
                }

                url = new URL(buildUrl.toString().substring(0, buildUrl.length() - 1));
            } else {
                url = new URL(buildUrl.toString());
            }

            // LogHelper.log("getExchangeRates - calling to url: " + url.toString());

            // These next few lines make the call to the URL we constructed above.
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String input;
            while ((input = br.readLine()) != null){
                sb.append(input);
            }
            br.close();

            // The variable sb contains the response from the API. Here we push that response into a JSONObject.
            // JSON is a standard format for sending data. See more here: https://en.wikipedia.org/wiki/JSON
            JSONObject obj = new JSONObject(sb.toString());


            // Construct the string we are going to return. This will look like:
            //    1 USD = 6.5786 CNY, 0.70365 GBP, 0.92013 EUR

            // First show the base currency we are starting from:
            String returnValue = "1 " + baseCurrency + " = ";

            // Then iterate over all the currencies we converted to
            HashMap<String, Object> ratesMap = new ObjectMapper().readValue(obj.getJSONObject("rates").toString(), HashMap.class);
            for (Map.Entry<String, Object> currencyRate : ratesMap.entrySet()) {
                double value = Double.parseDouble("" + currencyRate.getValue());
                BigDecimal bigValue = BigDecimal.valueOf(value);
                returnValue += bigValue.toPlainString() + " " + currencyRate.getKey() + ", ";
            }

            return returnValue.substring(0, returnValue.length() - 2);
        } catch (Exception e) {
            LogHelper.logException(e);
        }

        return "getExchangeRate - error converting currency with baseCurrency = " + baseCurrency;
    }

    static {
        // Install the all-trusting trust manager. You normally do NOT want to do something like this but this will
        // allow us to avoid having everyone install SSL certificates on their computers. Without this calls to the
        // fixer.io API over HTTPS will result in HTTP error code 422.
        //
        // This "solution" was found here: http://stackoverflow.com/a/6055903/265791
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
                public X509Certificate[] getAcceptedIssuers(){return null;}
                public void checkClientTrusted(X509Certificate[] certs, String authType){}
                public void checkServerTrusted(X509Certificate[] certs, String authType){}
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            LogHelper.logException(e);
        }
    }
}
