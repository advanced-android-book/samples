package com.github.advanced_android.newgithubrepo.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.github.advanced_android.newgithubrepo.R;
import com.github.advanced_android.newgithubrepo.contract.DetailViewContract;
import com.github.advanced_android.newgithubrepo.databinding.ActivityDetailBinding;
import com.github.advanced_android.newgithubrepo.model.GitHubService;
import com.github.advanced_android.newgithubrepo.viewmodel.DetailViewModel;

/**
 * 詳細画面を表示するActivity
 */
public class DetailActivity extends AppCompatActivity implements DetailViewContract {
  private static final String EXTRA_FULL_REPOSITORY_NAME = "EXTRA_FULL_REPOSITORY_NAME";
  private String fullRepoName;

  /**
   * DetailActivityを開始するメソッド
   * @param fullRepositoryName 表示したいリポジトリの名前(google/ioschedなど)
   */
  public static void start(Context context, String fullRepositoryName) {
    final Intent intent = new Intent(context, DetailActivity.class);
    intent.putExtra(EXTRA_FULL_REPOSITORY_NAME, fullRepositoryName);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
    final GitHubService gitHubService = ((NewGitHubReposApplication) getApplication()).getGitHubService();
    final DetailViewModel detailViewModel = new DetailViewModel((DetailViewContract) this, gitHubService);
    binding.setViewModel(detailViewModel);

    final Intent intent = getIntent();
    fullRepoName = intent.getStringExtra(EXTRA_FULL_REPOSITORY_NAME);
    detailViewModel.loadRepositories();
  }

  @Override
  public String getFullRepositoryName() {
    return fullRepoName;
  }


  /**
   * @throws Exception
   */
  @Override
  public void startBrowser(String url) {
    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
  }

  @Override
  public void showError(String message) {
    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
            .show();
  }

}
