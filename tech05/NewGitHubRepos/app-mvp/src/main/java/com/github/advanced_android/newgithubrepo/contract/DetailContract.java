package com.github.advanced_android.newgithubrepo.contract;

import com.github.advanced_android.newgithubrepo.model.GitHubService;

/**
 * それぞれの役割が持っているContract(契約)を定義しておくインターフェース
 */
public interface DetailContract {

  /**
   * MVPのViewが実装するインターフェース
   * PresenterがViewを操作する時に利用する
   */
  interface View {
    String getFullRepositoryName();

    void showRepositoryInfo(GitHubService.RepositoryItem response);

    void startBrowser(String url);

    void showError(String message);
  }

  /**
   * MVPのPresenterが実装するインターフェース
   * Viewをクリックした時などにViewがPresenterに教えるために利用する
   */
  interface UserActions {
    void titleClick();

    void prepare();
  }
}


