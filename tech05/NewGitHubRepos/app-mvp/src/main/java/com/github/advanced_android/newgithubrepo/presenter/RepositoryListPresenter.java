package com.github.advanced_android.newgithubrepo.presenter;

import android.text.format.DateFormat;

import com.github.advanced_android.newgithubrepo.contract.RepositoryListContract;
import com.github.advanced_android.newgithubrepo.model.GitHubService;

import java.util.Calendar;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * MVPのPresenterの役割を持つクラス
 */
public class RepositoryListPresenter implements RepositoryListContract.UserActions {
  private final RepositoryListContract.View repositoryListView;
  private final GitHubService gitHubService;

  public RepositoryListPresenter(RepositoryListContract.View repositoryListView, GitHubService gitHubService) {
    // ① RepositoryListContract.Viewとしてメンバ変数に格納する
    this.repositoryListView = repositoryListView;
    this.gitHubService = gitHubService;
  }

  @Override
  public void selectLanguage(String language) {
    loadRepositories();
  }

  @Override
  public void selectRepositoryItem(GitHubService.RepositoryItem item) {
    repositoryListView.startDetailActivity(item.full_name);
  }

  /**
   * 過去一週間で作られたライブラリのスター数順で取得
   */
  private void loadRepositories() {
    // ② 読込中なのでプログレスバーを表示する
    repositoryListView.showProgress();

    // 一週間前の日付の文字列 今が2016-10-27ならば2016-10-20 などの文字列を取得する
    final Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -7);
    String text = DateFormat.format("yyyy-MM-dd", calendar).toString();

    // Retrofitを利用してサーバーにアクセスする

    // 過去一週間で作られて、言語がlanguageのものをクエリとして渡す
    Observable<GitHubService.Repositories> observable = gitHubService.listRepos("language:" + repositoryListView.getSelectedLanguage() + " " + "created:>" + text);
    // 入出力(IO)用のスレッドで通信を行い、メインスレッドで結果を受け取るようにする
    observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GitHubService.Repositories>() {
      @Override
      public void onNext(GitHubService.Repositories repositories) {
        // ③ 読み込み終了したので、プログレスバーの表示を非表示にする
        repositoryListView.hideProgress();
        // ④ 取得したアイテムを表示するために、RecyclerViewにアイテムをセットして更新する
        repositoryListView.showRepositories(repositories);
      }

      @Override
      public void onError(Throwable e) {
        // 通信に失敗したら呼ばれる。
        // ここではスナックバーを表示する(下に表示されるバー)
        repositoryListView.showError();
      }

      @Override
      public void onCompleted() {
        // 何もしない
      }
    });
  }

}
