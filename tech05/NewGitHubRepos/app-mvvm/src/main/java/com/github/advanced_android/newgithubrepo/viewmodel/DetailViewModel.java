package com.github.advanced_android.newgithubrepo.viewmodel;

import android.databinding.ObservableField;
import android.view.View;

import com.github.advanced_android.newgithubrepo.contract.DetailViewContract;
import com.github.advanced_android.newgithubrepo.model.GitHubService;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailViewModel {
  final DetailViewContract detailView;
  private final GitHubService gitHubService;
  public ObservableField<String> repoFullName = new ObservableField<>();
  public ObservableField<String> repoDetail = new ObservableField<>();
  public ObservableField<String> repoStar = new ObservableField<>();
  public ObservableField<String> repoFork = new ObservableField<>();
  public ObservableField<String> repoImageUrl = new ObservableField<>();
  private GitHubService.RepositoryItem repositoryItem;

  public DetailViewModel(DetailViewContract detailView, GitHubService gitHubService) {
    this.detailView = detailView;
    this.gitHubService = gitHubService;
  }

  public void prepare() {
    loadRepositories();
  }

  /**
   * 一つのリポジトリについての情報を取得する
   * 基本的にAPIアクセス方法についてはRepositoryListActivity#loadRepositories(String)と同じ
   */
  public void loadRepositories() {
    String fullRepoName = detailView.getFullRepositoryName();
    // リポジトリの名前を/で分割する
    final String[] repoData = fullRepoName.split("/");
    final String owner = repoData[0];
    final String repoName = repoData[1];
    gitHubService
        .detailRepo(owner, repoName)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<GitHubService.RepositoryItem>() {
          @Override
          public void onCompleted() {
            // 何もしない
          }

          @Override
          public void onError(Throwable e) {
            detailView.showError("読み込めませんでした。");
          }

          @Override
          public void onNext(GitHubService.RepositoryItem repositoryItem) {
            loadRepositoryItem(repositoryItem);
          }
        });
  }

  private void loadRepositoryItem(GitHubService.RepositoryItem repositoryItem) {
    this.repositoryItem = repositoryItem;
    repoFullName.set(repositoryItem.full_name);
    repoDetail.set(repositoryItem.description);
    repoStar.set(repositoryItem.stargazers_count);
    repoFork.set(repositoryItem.forks_count);
    repoImageUrl.set(repositoryItem.owner.avatar_url);
  }


  public void onTitleClick(View v) {
    try {
      detailView.startBrowser(repositoryItem.html_url);
    } catch (Exception e) {
      detailView.showError("リンクを開けませんでした。");
    }
  }
}
