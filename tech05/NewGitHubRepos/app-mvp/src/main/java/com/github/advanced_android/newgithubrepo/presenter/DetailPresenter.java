package com.github.advanced_android.newgithubrepo.presenter;

import com.github.advanced_android.newgithubrepo.contract.DetailContract;
import com.github.advanced_android.newgithubrepo.model.GitHubService;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DetailPresenter implements DetailContract.UserActions {
  final DetailContract.View detailView;
  private final GitHubService gitHubService;
  private GitHubService.RepositoryItem repositoryItem;

  public DetailPresenter(DetailContract.View detailView, GitHubService gitHubService) {
    this.detailView = detailView;
    this.gitHubService = gitHubService;
  }

  @Override
  public void prepare() {
    loadRepositories();
  }

  /**
   * 一つのリポジトリについての情報を取得する
   * 基本的にAPIアクセス方法についてはRepositoryListActivity#loadRepositories(String)と同じ
   */
  private void loadRepositories() {
    String fullRepoName = detailView.getFullRepositoryName();
    // リポジトリの名前を/で分割する
    final String[] repoData = fullRepoName.split("/");
    final String owner = repoData[0];
    final String repoName = repoData[1];
    gitHubService
        .detailRepo(owner, repoName)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<GitHubService.RepositoryItem>() {
          @Override
          public void call(GitHubService.RepositoryItem response) {
            repositoryItem = response;
            detailView.showRepositoryInfo(response);
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            detailView.showError("読み込めませんでした。");
          }
        });
  }

  @Override
  public void titleClick() {
    try {
      detailView.startBrowser(repositoryItem.html_url);
    } catch (Exception e) {
      detailView.showError("リンクを開けませんでした。");
    }
  }

}
