package coom.woocommerce.test;

import android.util.Log;

import com.woocommerse.OAuth1.services.HMACSha1SignatureService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by ramesh on 10/10/16.
 */





public class OAuthInterceptor implements Interceptor {

    /*THIS CLASS CONTAIN ERROR ITS BECAUSE THIS APP DOES NOT IMPORTED THE RETROFIT LIBRARY*/


    /*IMPORT below dependency to gradel to fix error
    *
    *  compile 'com.squareup.retrofit2:retrofit:2.1.0'
    compile 'com.squareup.retrofit2:converter-gson:2.1.0'
    compile 'com.squareup.okhttp3:okhttp:3.3.1'
    compile 'com.squareup.okhttp3:logging-interceptor:3.3.1'
    * */

        private static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
        private static final String OAUTH_NONCE = "oauth_nonce";
        private static final String OAUTH_SIGNATURE = "oauth_signature";
        private static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
        private static final String OAUTH_SIGNATURE_METHOD_VALUE = "HMAC-SHA1";
        private static final String OAUTH_TIMESTAMP = "oauth_timestamp";
        private static final String OAUTH_VERSION = "oauth_version";
        private static final String OAUTH_VERSION_VALUE = "1.0";

        private final String consumerKey;
        private final String consumerSecret;

    /*THIS CLASS CONTAIN ERROR ITS BECAUSE THIS APP DOESNOT IMPORTED THE RETROFIT LIBRARY*/


    /*IMPORT below dependency to gradel to fix error
    *
    *  compile 'com.squareup.retrofit2:retrofit:2.1.0'
    compile 'com.squareup.retrofit2:converter-gson:2.1.0'
    compile 'com.squareup.okhttp3:okhttp:3.3.1'
    compile 'com.squareup.okhttp3:logging-interceptor:3.3.1'
    * */





        private OAuthInterceptor(String consumerKey, String consumerSecret) {
            this.consumerKey = consumerKey;
            this.consumerSecret = consumerSecret;

        }
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

//        Log.d("URL",original.url().toString());
//
//        Log.d("method",original.method());

            ////////////////////////////////////////////////////////////

            final String nonce=new TimestampServiceImpl().getNonce();
            final String timestamp=new TimestampServiceImpl().getTimestampInSeconds();
            Log.d("nonce",nonce);
            Log.d("time",timestamp);

            String firstBaseString=original.method()+"&"+urlEncoded(original.url().toString());
            Log.d("firstBaseString",firstBaseString);

            String generatedBaseString="oauth_consumer_key="+consumerKey+"&oauth_nonce="+nonce+"&oauth_signature_method=HMAC-SHA1&oauth_timestamp="+timestamp+"&oauth_version=1.0";
            String secoundBaseString="&"+urlEncoded(generatedBaseString);



            String baseString=firstBaseString+secoundBaseString;

            String signature=new HMACSha1SignatureService().getSignature(baseString,consumerSecret,"");
            Log.d("Signature",signature);

            HttpUrl url = originalHttpUrl.newBuilder()

                    .addQueryParameter(OAUTH_SIGNATURE_METHOD,OAUTH_SIGNATURE_METHOD_VALUE)
                    .addQueryParameter(OAUTH_CONSUMER_KEY,consumerKey)
                    .addQueryParameter(OAUTH_VERSION,OAUTH_VERSION_VALUE)
                    .addQueryParameter(OAUTH_TIMESTAMP ,timestamp)
                    .addQueryParameter(OAUTH_NONCE,nonce)
                    .addQueryParameter(OAUTH_SIGNATURE,signature)


                    .build();

            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .url(url);

            Request request = requestBuilder.build();
            return chain.proceed(request);
        }


        public static final class Builder {

            private String consumerKey;
            private String consumerSecret;

            public Builder consumerKey(String consumerKey) {
                if (consumerKey == null) throw new NullPointerException("consumerKey = null");
                this.consumerKey = consumerKey;
                return this;
            }

            public Builder consumerSecret(String consumerSecret) {
                if (consumerSecret == null) throw new NullPointerException("consumerSecret = null");
                this.consumerSecret = consumerSecret;
                return this;
            }


            public OAuthInterceptor build() {

                if (consumerKey == null) throw new IllegalStateException("consumerKey not set");
                if (consumerSecret == null) throw new IllegalStateException("consumerSecret not set");

                return new OAuthInterceptor(consumerKey, consumerSecret);
            }
        }

        public String urlEncoded(String url)
        {
            String encodedurl="";
            try {

                encodedurl = URLEncoder.encode(url,"UTF-8");
                Log.d("TEST", encodedurl);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return encodedurl;
        }
    }