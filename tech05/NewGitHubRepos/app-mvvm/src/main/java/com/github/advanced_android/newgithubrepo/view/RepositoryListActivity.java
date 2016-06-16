package com.github.advanced_android.newgithubrepo.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.advanced_android.newgithubrepo.R;
import com.github.advanced_android.newgithubrepo.contract.RepositoryListViewContract;
import com.github.advanced_android.newgithubrepo.databinding.ActivityRepositoryListBinding;
import com.github.advanced_android.newgithubrepo.model.GitHubService;
import com.github.advanced_android.newgithubrepo.viewmodel.RepositoryListViewModel;

/**
 * リポジトリのリストを表示するActivity
 * MVVMのViewの役割を持つ
 */
public class RepositoryListActivity extends AppCompatActivity implements RepositoryListViewContract {

  private Spinner languageSpinner;
  private CoordinatorLayout coordinatorLayout;

  private RepositoryAdapter repositoryAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityRepositoryListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_repository_list);
    final GitHubService gitHubService = ((NewGitHubReposApplication) getApplication()).getGitHubService();
    binding.setViewModel(new RepositoryListViewModel((RepositoryListViewContract) this, gitHubService));

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
    repositoryAdapter = new RepositoryAdapter((Context) this, (RepositoryListViewContract) this);
    recyclerView.setAdapter(repositoryAdapter);

    // SnackBar表示で利用する
    coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

    // Spinner
    languageSpinner = (Spinner) findViewById(R.id.language_spinner);
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
    adapter.addAll("java", "objective-c", "swift", "groovy", "python", "ruby", "c");
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    languageSpinner.setAdapter(adapter);
  }

  // =====RepositoryListViewContract の実装=====
  // ここでPresenterから指示を受けてViewの変更などを行う

  @Override
  public void startDetailActivity(String full_name) {
    DetailActivity.start(this, full_name);
  }

  @Override
  public void showRepositories(GitHubService.Repositories repositories) {
    repositoryAdapter.setItemsAndRefresh(repositories.items);
  }

  @Override
  public void showError() {
    Snackbar.make(coordinatorLayout, "読み込めませんでした。", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
  }

}
