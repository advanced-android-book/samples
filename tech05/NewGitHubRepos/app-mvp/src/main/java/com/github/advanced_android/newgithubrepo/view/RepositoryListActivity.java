package com.github.advanced_android.newgithubrepo.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.github.advanced_android.newgithubrepo.R;
import com.github.advanced_android.newgithubrepo.contract.RepositoryListContract;
import com.github.advanced_android.newgithubrepo.model.GitHubService;
import com.github.advanced_android.newgithubrepo.presenter.RepositoryListPresenter;

/**
 * リポジトリのリストを表示するActivity
 * MVPのViewの役割を持つ
 */
public class RepositoryListActivity extends AppCompatActivity implements RepositoryAdapter.OnRepositoryItemClickListener,
    RepositoryListContract.View {

  private Spinner languageSpinner;
  private ProgressBar progressBar;
  private CoordinatorLayout coordinatorLayout;

  private RepositoryAdapter repositoryAdapter;

  private RepositoryListContract.UserActions repositoryListPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_repository_list);

    // Viewをセットアップ
    setupViews();

    // ① Presenterのインスタンスを作成
    final GitHubService gitHubService = ((NewGitHubReposApplication) getApplication()).getGitHubService();
    repositoryListPresenter = new RepositoryListPresenter((RepositoryListContract.View) this, gitHubService);
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
        //  スピナーの選択内容が変わったら呼ばれる
        String language = (String) languageSpinner.getItemAtPosition(position);
        // ② Presenterにプログラミング言語を選択したと知らせる
        repositoryListPresenter.selectLanguage(language);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }

    });
  }

  /**
   * RecyclerViewでクリックされた
   * @see RepositoryAdapter.OnRepositoryItemClickListener#onRepositoryItemClickListener
   */
  @Override
  public void onRepositoryItemClick(GitHubService.RepositoryItem item) {
    repositoryListPresenter.selectRepositoryItem(item);
  }


  // =====RepositoryListContract.View の実装=====
  // ここでPresenterから指示を受けてViewの変更などを行う

  @Override
  public void startDetailActivity(String full_name) {
    DetailActivity.start(this, full_name);
  }


  @Override
  public String getSelectedLanguage() {
    return (String) languageSpinner.getSelectedItem();
  }


  @Override
  public void showProgress() {
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideProgress() {
    progressBar.setVisibility(View.GONE);
  }

  @Override
  public void showRepositories(GitHubService.Repositories repositories) {
    // ③ リポジトリのリストをAdapterにセットする
    repositoryAdapter.setItemsAndRefresh(repositories.items);
  }

  @Override
  public void showError() {
    Snackbar.make(coordinatorLayout, "読み込めませんでした。", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
  }

}
