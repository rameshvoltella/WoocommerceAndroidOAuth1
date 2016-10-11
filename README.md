WoocommerceAndroidOAuth1 -Android
=================

This is a sample application which show how to use whocommerce using OAuth 1.0a “one-legged” authentication(HTTP).

Using the WooCommerce REST API 

There are two ways to authenticate with the API, the easy way (over HTTPS) or the hard way (over plain HTTP using OAuth).

##Over HTTPS

Simply use HTTP Basic Auth by providing the API Consumer Key as the username and the API Consumer Secret as the password(ITS EASY WAY)


##Over HTTP with OAuth(HARD WAY)

HTTP Basic authentication cannot be used over plain HTTP as the keys are susceptible to interception. The API uses OAuth 1.0a “one-legged” authentication to ensure your API keys cannot be intercepted. This process involves generating a signature and including it with your request. The API then generates it’s own signature and compares it against the one provided. If they match, the request is authenticated.
([SOURCE](https://www.skyverge.com/blog/using-woocommerce-rest-api-introduction/))


WoocommerceAndroidOAuth1  is making the hard way easy for android


### Over HTTP ###

You must use [OAuth 1.0a "one-legged" authentication](http://tools.ietf.org/html/rfc5849) to ensure API credentials cannot be intercepted. Typically you may use any standard OAuth 1.0a library in your language of choice to handle the authentication, or generate the necessary parameters by following these instructions.

#### Generating an OAuth signature ####

1) Set the HTTP method for the request:

`GET`

2) Set your base request URI -- this is the full request URI without query string parameters -- and URL encode according to RFC 3986:

`http://www.example.com/wc-api/v1/orders`

when encoded:

`http%3A%2F%2Fwww.example.com%2Fwc-api%2Fv1%2Forders`

3) Collect and normalize your query string parameters. This includes all `oauth_*` parameters except for the signature. Parameters should be normalized by URL encoding according to RFC 3986 (`rawurlencode` in PHP) and percent(`%`) characters should be double-encoded (e.g. `%` becomes `%25`.

4) Sort the parameters in byte-order (`uksort( $params, 'strcmp' )` in PHP)// Android Way will describe below

5) Join each parameter with an encoded equals sign (`%3D`):

`oauth_signature_method%3DHMAC-SHA1`

6) Join each parameter key/value with an encoded ampersand (`%26`):

`oauth_consumer_key%3Dabc123%26oauth_signature_method%3DHMAC-SHA1`

7) Form the string to sign by joining the HTTP method, encoded base request URI, and encoded parameter string with an unencoded ampersand symbol (&):

`GET&http%3A%2F%2Fwww.example.com%2Fwc-api%2Fv1%2Forders&oauth_consumer_key%3Dabc123%26oauth_signature_method%3DHMAC-SHA1`

8) Generate the signature using the string to key and your consumer secret key

If you are having trouble generating a correct signature, you'll want to review your string to sign for errors with encoding. The [authentication source](https://github.com/woothemes/woocommerce/blob/master/includes/api/class-wc-api-authentication.php#L177) can also be helpful in understanding how to properly generate the signature.([SOURCE FROM OFFICIAL woocommerce-rest-api-docs](https://github.com/woocommerce/woocommerce-rest-api-docs/blob/master/source/includes/v2/_introduction.md))



The Same methord is begin used but in Android WAY :) Lets get Started



WoocommerceAndroidOAuth1: how to use
------------------------

1. add library woocommerseandroidoauth1library to gradle
  
    ```java
     compile project(':woocommerseandroidoauth1library')
```

2. Setting nonce and timestamp
  
    ```java

         String nonce=new TimestampServiceImpl().getNonce();
         String timestamp=new TimestampServiceImpl().getTimestampInSeconds();
```

3.  Generate Base String (which is used to generate signatuekey)
  
    ```java

          String BASE_SITE = "yoursitename.com";
          String BASE_URL = "http://"+BASE_SITE+"/wp-json/wc/v1/products";
          String COSTUMER_KEY = "costumer key here";
          String COSTUMER_SECRET = "costumer secret here";
          String METHORD="GET";//change API method eg POST,PUT, DELETE etc (ONLY FOR THIS EXAMPLE FOR LIB LIKE RETROFIT,OKHTTP, The Are Dynamic Way)

          String firstEncodedString =METHORD+"&"+Encodeurl(BASE_URL);
          Log.d("firstEncodedString",firstEncodedString);

        String parameterString="oauth_consumer_key="+COSTUMER_KEY+"&oauth_nonce="+nonce+"&oauth_signature_method=HMAC-SHA1&oauth_timestamp="+timestamp+"&oauth_version=1.0";
        
        String secoundEncodedString="&"+Encodeurl(parameterString);

        Log.d("secoundEncodedString",secoundEncodedString);

        //The base String is used to generate signature
        String baseString=firstEncodedString+secoundEncodedString;

         
```


4. Generate Signature (using generate baseString and COSTUMER_SECRET)
  
    ```java

         String signature=new HMACSha1SignatureService().getSignature(baseString,COSTUMER_SECRET,"");
     
        //Signature is encoded before parsing (ONLY FOR THIS EXAMPLE, NOT NECESSARY FOR LIB LIKE RETROFIT,OKHTTP)
         signature=Encodeurl(signature);
         
```

5.  Generate Signature (using generate baseString and COSTUMER_SECRET)
  
    ```java

         String parseUrl=BASE_URL+"?oauth_signature_method=HMAC-SHA1&oauth_consumer_key="+COSTUMER_KEY+"&oauth_version=1.0&oauth_timestamp="+timestamp+"&oauth_nonce="+nonce+"&oauth_signature="+ signature;

           Use this url to authenitcate (USE ANY API METHORDS)
         
```






## License

    The MIT License (MIT)

    Copyright (c) 2016 Ramesh M Nair
 
     Permission is hereby granted, free of charge, to any person obtaining a copy
     of this software and associated documentation files (the "Software"), to deal
     in the Software without restriction, including without limitation the rights
     to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
     copies of the Software, and to permit persons to whom the Software is
     furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.


 



