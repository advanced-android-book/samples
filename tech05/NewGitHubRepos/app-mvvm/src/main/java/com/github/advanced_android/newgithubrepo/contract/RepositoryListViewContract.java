package com.github.advanced_android.newgithubrepo.contract;

import com.github.advanced_android.newgithubrepo.model.GitHubService;

/**
 * リポジトリ一覧画面が持っているContract(契約)を定義しておくインターフェース
 * <p>
 * ViewModelが直接Activityを参照しないようにインターフェースで明確に分けている
 */
public interface RepositoryListViewContract {
  void showRepositories(GitHubService.Repositories repositories);

  void showError();

  void startDetailActivity(String fullRepositoryName);
}


