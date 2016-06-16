package com.github.advanced_android.newgithubrepo;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.Calendar;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * リポジトリのリストを表示するActivity
 */
public class RepositoryListActivity extends AppCompatActivity implements RepositoryAdapter.OnRepositoryItemClickListener {

  private Spinner languageSpinner;
  private ProgressBar progressBar;
  private CoordinatorLayout coordinatorLayout;

  private RepositoryAdapter repositoryAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_repository_list);

    // Viewをセットアップ
    setupViews();
  }

  /**
   * リストなどの画面の要素を作る
   */
  private void setupViews() {
    // ツールバーのセット
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Recycler View
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_repos);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    repositoryAdapter = new RepositoryAdapter((Context) this, (RepositoryAdapter.OnRepositoryItemClickListener) this);
    recyclerView.setAdapter(repositoryAdapter);

    // ProgressBar
    progressBar = (ProgressBar) findViewById(R.id.progress_bar);

    // SnackBar表示で利用する
    coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

    // Spinner
    languageSpinner = (Spinner) findViewById(R.id.language_spinner);
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
    adapter.addAll("java", "objective-c", "swift", "groovy", "python", "ruby", "c");
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    languageSpinner.setAdapter(adapter);
    languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // 選択時だけでなく、最初にも呼ばれる
        String language = (String) languageSpinner.getItemAtPosition(position);
        loadRepositories(language);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }

    });
  }


  /**
   * 過去一週間で作られたライブラリのスター数順で取得
   * @param language 読み込むプログラミング言語
   */
  private void loadRepositories(String language) {
    // 読込中なのでプログレスバーを表示する
    progressBar.setVisibility(View.VISIBLE);

    // 一週間前の日付の文字列 今が2016-10-27ならば2016-10-20 などの文字列を取得する
    final Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -7);
    String text = DateFormat.format("yyyy-MM-dd", calendar).toString();

    // Retrofitを利用してサーバーにアクセスする
    final NewGitHubReposApplication application = (NewGitHubReposApplication) getApplication();
    // 過去一週間で作られて、言語がlanguageのものをクエリとして渡す
    Observable<GitHubService.Repositories> observable = application.getGitHubService().listRepos("language:" + language + " " + "created:>" + text);
    // 入出力(IO)用のスレッドで通信を行い、メインスレッドで結果を受け取るようにする
    observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GitHubService.Repositories>() {
      @Override
      public void onNext(GitHubService.Repositories repositories) {
        // 読み込み終了したので、プログレスバーの表示を非表示にする
        progressBar.setVisibility(View.GONE);
        // 取得したアイテムを表示するために、RecyclerViewにアイテムをセットして更新する
        repositoryAdapter.setItemsAndRefresh(repositories.items);
      }

      @Override
      public void onError(Throwable e) {
        // 通信に失敗したら呼ばれる。
        // ここではスナックバーを表示する(下に表示されるバー)
        Snackbar.make(coordinatorLayout, "読み込めませんでした。", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
      }

      @Override
      public void onCompleted() {
        // 何もしない
      }
    });
  }

  /**
   * 詳細画面を表示する
   * @see RepositoryAdapter.OnRepositoryItemClickListener#onRepositoryItemClickListener
   */
  @Override
  public void onRepositoryItemClick(GitHubService.RepositoryItem item) {
    DetailActivity.start(this, item.full_name);
  }
}
