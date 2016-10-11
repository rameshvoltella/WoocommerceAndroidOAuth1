package coom.woocommerce.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.woocommerse.OAuth1.services.HMACSha1SignatureService;
import com.woocommerse.OAuth1.services.TimestampServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String BASE_SITE = "yoursitename.com";
        final String BASE_URL = "http://"+BASE_SITE+"/wp-json/wc/v1/products";
        final String COSTUMER_KEY = "costumer key here";
        String COSTUMER_SECRET = "costumer secret here";

        String METHORD="GET";//change API method eg POST,PUT, DELETE etc (ONLY FOR THIS EXAMPLE FOR LIB LIKE RETROFIT,OKHTTP, The Are Dynamic Way)


        final String nonce=new TimestampServiceImpl().getNonce();
        final String timestamp=new TimestampServiceImpl().getTimestampInSeconds();

       // GENERATED NONCE and TIME STAMP
        Log.d("nonce",nonce);
        Log.d("time",timestamp);

        String firstEncodedString =METHORD+"&"+encodeUrl(BASE_URL);
        Log.d("firstEncodedString",firstEncodedString);

        String parameterString="oauth_consumer_key="+COSTUMER_KEY+"&oauth_nonce="+nonce+"&oauth_signature_method=HMAC-SHA1&oauth_timestamp="+timestamp+"&oauth_version=1.0";
        String secoundEncodedString="&"+encodeUrl(parameterString);

        Log.d("secoundEncodedString",secoundEncodedString);


        String baseString=firstEncodedString+secoundEncodedString;

       //THE BASE STRING AND COSTUMER_SECRET KEY IS USED FOR GENERATING SIGNATURE
        Log.d("baseString",baseString);

        String signature=new HMACSha1SignatureService().getSignature(baseString,COSTUMER_SECRET,"");
        Log.d("SignatureBefore",signature);

        //Signature is encoded before parsing (ONLY FOR THIS EXAMPLE NOT NECESSARY FOR LIB LIKE RETROFIT,OKHTTP)
        signature=encodeUrl(signature);

        Log.d("SignatureAfter ENCODING",signature);


        final String finalSignature = signature;//BECAUSE I PUT IN SIMPLE THREAD NOT NECESSARY

         new Thread() {
            @Override
            public void run() {

                //  THIS IS A VERY BASIC EXAMPLE OF PARSING USER CAN USE ANY LATEST METHORD RETROFIT,OKHTTP,VOLLEY ETC

                String parseUrl=BASE_URL+"?oauth_signature_method=HMAC-SHA1&oauth_consumer_key="+COSTUMER_KEY+"&oauth_version=1.0&oauth_timestamp="+timestamp+"&oauth_nonce="+nonce+"&oauth_signature="+ finalSignature;

               getJSON(parseUrl);

            }
        }.start();
    }



    public String encodeUrl(String url)
    {
        String encodedurl="";
        try {

            encodedurl = URLEncoder.encode(url,"UTF-8");
            Log.d("Encodeurl", encodedurl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedurl;
    }



    public String getJSON(String url) {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            Log.d("urlioz",""+c.getURL());
//            c.setConnectTimeout(timeout);
//            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();
            Log.d("staus",""+status);
            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    Log.d("RESonse here ",sb.toString());
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return "failed";
    }
}
