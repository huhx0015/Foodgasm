package com.huhmoon.apparely.macysapihandler;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.Callback;
import retrofit.http.Query;

/**
 * Created by ssureymoon on 8/7/15.
 */
public class SimpleService {
    // public static final String API_URL = "http://api.macys.com";
    public static final String API_URL = "https://api.github.com";
    // Create a very simple REST adapter which points the GitHub API.
    private final Context mContext;

    public SimpleService(Context context){
        mContext = context;
    }
    public static class Contributor {
        public final String login;
        public final int contributions;

        public Contributor(String login, int contributions) {
            this.login = login;
            this.contributions = contributions;
        }
    }

/*
    class ErrorHandler implements retrofit.ErrorHandler {

        @Override
        public Throwable handleError(RetrofitError retrofitError) {

            Response r = retrofitError.getResponse();
            if ((r != null) && (r.getStatus() == 401)) {
                return new UnauthorizedException(cause);
            }

            return null;
        }
    }
    */

    public static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(API_URL)
            //.setErrorHandler()
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build();

    public static class Catalog {
        public int totalpages;
    }

//    public interface MacysService {
//        @Headers({
//                "Accept: application/json",
//                "X-Macys-Webservice-Client-Id: atthack2015"
//        })
//        @GET("/v4/catalog/search")
//        void listSearchedCatalog(@Query("searchphase") String searchphase, Callback<Catalog> cb);
//    }

    public interface GitHub {
        @GET("/repos/{owner}/{repo}/contributors")
        void listContributors(
                @Path("owner") String owner,
                @Path("repo") String repo,
                Callback<Contributor> cb);
    }

    public void runApi() throws IOException {

//        MacysService apiService =
//                restAdapter.create(MacysService.class);

        GitHub service = restAdapter.create(GitHub.class);
        service.listContributors("square", "retrofit", new Callback<Contributor>() {
            @Override
            public void success(Contributor c, Response response) {
                // Access user here after response is parsed
                Toast.makeText(mContext, c.login, Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                // Log error here since request failed
                Toast.makeText(mContext, retrofitError.getStackTrace().toString(), Toast.LENGTH_LONG).show();
            }
        });
//        String searchphase = "jeans";
//        apiService.listSearchedCatalog(searchphase, new Callback<Catalog>() {
//            @Override
//            public void success(Catalog catalog, Response response) {
//                // Access user here after response is parsed
//                Toast.makeText(mContext, Integer.toString(catalog.totalpages), Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void failure(RetrofitError retrofitError) {
//                // Log error here since request failed
//                Toast.makeText(mContext, retrofitError.getStackTrace().toString(), Toast.LENGTH_LONG).show();
//            }
//
//        });
    }
}