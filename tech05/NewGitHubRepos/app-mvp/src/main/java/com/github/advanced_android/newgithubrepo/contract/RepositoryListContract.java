package com.github.advanced_android.newgithubrepo.contract;

import com.github.advanced_android.newgithubrepo.model.GitHubService;

/**
 * それぞれの役割が持っているContract(契約)を定義しておくインターフェース
 */
public interface RepositoryListContract {

  /**
   * MVPのViewが実装するインターフェース
   * PresenterがViewを操作する時に利用する
   */
  interface View {
    String getSelectedLanguage();
    void showProgress();
    void hideProgress();
    void showRepositories(GitHubService.Repositories repositories);
    void showError();
    void startDetailActivity(String fullRepositoryName);
  }

  /**
   * MVPのPresenterが実装するインターフェース
   * Viewをクリックした時などにViewがPresenterに教えるために利用する
   */
  interface UserActions {
    void selectLanguage(String language);
    void selectRepositoryItem(GitHubService.RepositoryItem item);
  }

}


