/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk2.api.face;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.squareup.okhttp.OkHttpClient;
import java.util.Date;
import net.ipetty.android.core.Constant;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 *
 * @author Administrator
 */
public class IpettyClientFactry {

        public static <T> T create(Class<T> clazz) {
                Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).create();
                OkHttpClient okHttpClient = new OkHttpClient();
                OkClient okClient = new OkClient(okHttpClient);
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setClient(okClient)
                        .setEndpoint(Constant.API_SERVER_BASE)
                        .setConverter(new GsonConverter(gson))
                        .build();
                return restAdapter.create(clazz);
        }
}
