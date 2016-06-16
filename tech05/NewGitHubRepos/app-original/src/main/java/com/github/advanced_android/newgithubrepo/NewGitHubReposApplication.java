package com.github.advanced_android.newgithubrepo;

import android.app.Application;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewGitHubReposApplication extends Application {
  private Retrofit retrofit;
  private GitHubService gitHubService;

  @Override
  public void onCreate() {
    super.onCreate();
    // どのActivityからでもAPIを利用できるように、このクラスでAPIを利用する
    setupAPIClient();
  }

  private void setupAPIClient() {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
      @Override
      public void log(String message) {
        Log.d("API LOG", message);
      }
    });

    logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();

    retrofit = new Retrofit.Builder()
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .baseUrl("https://api.github.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build();

    gitHubService = retrofit.create(GitHubService.class);
  }

  public GitHubService getGitHubService() {
    return gitHubService;
  }
}
