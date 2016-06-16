package com.github.advanced_android.newgithubrepo.contract;

/**
 * 詳細画面のビューが持っているContract(契約)を定義しておくインターフェース
 * <p>
 * ViewModelが直接Activityを参照しないようにインターフェースで明確に分けている
 */
public interface DetailViewContract {

  String getFullRepositoryName();

  void startBrowser(String url);

  void showError(String message);
}


