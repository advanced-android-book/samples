package com.github.advanced_android.newgithubrepo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 詳細画面を表示するActivity
 */
public class DetailActivity extends AppCompatActivity {
  private static final String EXTRA_FULL_REPOSITORY_NAME = "EXTRA_FULL_REPOSITORY_NAME";
  private TextView fullNameTextView;
  private TextView detailTextView;
  private TextView repoStarTextView;
  private TextView repoForkTextView;
  private ImageView ownerImage;

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
    setContentView(R.layout.activity_detail);
    fullNameTextView = (TextView) DetailActivity.this.findViewById(R.id.fullname);
    detailTextView = (TextView) findViewById(R.id.detail);
    repoStarTextView = (TextView) findViewById(R.id.repo_star);
    repoForkTextView = (TextView) findViewById(R.id.repo_fork);
    ownerImage = (ImageView) findViewById(R.id.owner_image);

    final Intent intent = getIntent();
    final String fullRepoName = intent.getStringExtra(EXTRA_FULL_REPOSITORY_NAME);

    loadRepositories(fullRepoName);
  }

  /**
   * 一つのリポジトリについての情報を取得する
   * 基本的にAPIアクセス方法についてはRepositoryListActivity#loadRepositories(String)と同じ
   */
  private void loadRepositories(String fullRepoName) {
    // リポジトリの名前を/で分割する
    final String[] repoData = fullRepoName.split("/");
    final String owner = repoData[0];
    final String repoName = repoData[1];
    final GitHubService gitHubService = ((NewGitHubReposApplication) getApplication()).getGitHubService();
    gitHubService.detailRepo(owner, repoName)
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Action1<GitHubService.RepositoryItem>() {
                   @Override
                   public void call(GitHubService.RepositoryItem response) {
                     setupRepositoryInfo(response);

                   }
                 }, new Action1<Throwable>() {
                   @Override
                   public void call(Throwable throwable) {
                     Snackbar.make(findViewById(android.R.id.content), "読み込めませんでした。", Snackbar.LENGTH_LONG)
                             .setAction("Action", null).show();
                   }
                 });
  }

  private void setupRepositoryInfo(final GitHubService.RepositoryItem response) {
    fullNameTextView.setText(response.full_name);
    detailTextView.setText(response.description);
    repoStarTextView.setText(response.stargazers_count);
    repoForkTextView.setText(response.forks_count);
    // サーバーから画像を取得してimageViewに入れる
    Glide.with(DetailActivity.this)
         .load(response.owner.avatar_url)
         .asBitmap().centerCrop().into(new BitmapImageViewTarget(ownerImage) {
      @Override
      protected void setResource(Bitmap resource) {
        RoundedBitmapDrawable circularBitmapDrawable =
            RoundedBitmapDrawableFactory.create(getResources(), resource);
        circularBitmapDrawable.setCircular(true);
        ownerImage.setImageDrawable(circularBitmapDrawable);
      }
    });
    // ロゴとリポジトリの名前をタップしたら、作者のGitHubのページをブラウザで開く
    View.OnClickListener listener = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(response.html_url)));
        } catch (Exception e) {
          Snackbar.make(findViewById(android.R.id.content), "リンクを開けませんでした。", Snackbar.LENGTH_LONG)
                  .setAction("Action", null).show();
        }
      }
    };
    fullNameTextView.setOnClickListener(listener);
    ownerImage.setOnClickListener(listener);
  }
}
