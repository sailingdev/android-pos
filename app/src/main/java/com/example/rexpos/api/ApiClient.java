package com.example.rexpos.api;


import android.content.Context;


import com.example.rexpos.common.GlobalConst;
import com.example.rexpos.models.request.ReqTransactionList;
import com.example.rexpos.models.response.ResKitchenTableTransaction;
import com.example.rexpos.models.response.ResTableTransaction;
import com.example.rexpos.models.response.ResLogin;
import com.example.rexpos.models.response.ResProduct;
import com.example.rexpos.models.response.ResTable;
import com.example.rexpos.models.response.ResTransaction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.cert.CertificateException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public class ApiClient {

    public static ApiInterface apiMainService;

    public static ApiInterface getApiClient(Context context) {
//        String api_root = ((BaseActivity)context).mPrefs.getServerUrl() + "/downtime_katolec/api/v1/";
        String api_root = GlobalConst.HOST_NAME +  "api/v1/";

        if (apiMainService == null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = getUnsafeOkHttpClient();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(api_root)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
            apiMainService = retrofit.create(ApiInterface.class);
        }

        return apiMainService;
    }

    private static OkHttpClient getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.readTimeout(60, TimeUnit.SECONDS);
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.writeTimeout(30, TimeUnit.SECONDS);
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public interface ApiInterface {
        @FormUrlEncoded
        @POST("signup")
        public Call<ResLogin> signUpAPI(@FieldMap Map<String, String> options);


        @FormUrlEncoded
        @POST("signin")
        Call<ResLogin> signInAPI(@Field("email") String email, @Field("password") String password, @Field("fcm_token") String fcm_token);


        @FormUrlEncoded
        @POST("getTatbles")
        Call<ResKitchenTableTransaction> getTableAPI(@Field("user_id") int userId , @Field("pageNum") int pageNum, @Field("searchKey") String searchKey, @Field("shop_id") String shop_id);


        @GET("getListTatble")
        Call<ResTable> getListTableAPI();


        @FormUrlEncoded
        @POST("getProducts")
        Call<ResProduct> getProductAPI(@Field("category_id") long category_id, @Field("shop_id") String shop_id);



        /*
        @POST("posttransaction")
        Call<ResTransaction> postTransactionAPI(@Body ReqTransactionList transactions);
        */

        @FormUrlEncoded
        @POST("getspecifiedtabletransactions")
        Call<ResTableTransaction> getspecifiedtabletransactionsAPI(@Field("table_id") long table_id, @Field("date_transaction") String date_transaction, @Field("shop_id") String shop_id);


        @FormUrlEncoded
        @POST("setStatus")
        Call<ResTransaction> setStatusAPI(@Field("transaction_id") long transaction_id, @Field("table_id") long table_id, @Field("date_transaction") String date_transaction, @Field("product_id") long product_id, @Field("status") String status);


        @FormUrlEncoded
        @POST("addOrder")
        Call<ResTableTransaction> addOrderAPI(@FieldMap Map<String , String> params);



        @FormUrlEncoded
        @POST("getOrdersFromTable")
        Call<ResTableTransaction> getOrdersFromTableAPI(@Field("table_id") long table_id, @Field("shop_id") String shop_id);

        @FormUrlEncoded
        @POST("deleteOrderItem")
        Call<ResTableTransaction> deleteOrderItemAPI(@Field("transaction_id") long transaction_id, @Field("table_id") long table_id, @Field("product_id") long product_id, @Field("shop_id") String shop_id);


        @FormUrlEncoded
        @POST("bill")
        Call<ResTableTransaction> billAPI(@Field("table_id") long table_id, @Field("shop_id") String shop_id);


        @FormUrlEncoded
        @POST("complete")
        Call<ResTableTransaction> completeAPI(@Field("table_id") long table_id, @Field("shop_id") String shop_id);


    }

}
