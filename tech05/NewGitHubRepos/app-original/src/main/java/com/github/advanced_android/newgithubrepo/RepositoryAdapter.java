package com.github.advanced_android.newgithubrepo;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

/**
 * RecyclerViewでリポジトリのリストを表示するためのAdapterクラス
 * このクラスによりRecyclerViewのアイテムのViewの生成し、Viewにデータを入れる
 */
public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepoViewHolder> {
  private final OnRepositoryItemClickListener onRepositoryItemClickListener;
  private final Context context;
  private List<GitHubService.RepositoryItem> items;

  public RepositoryAdapter(Context context, OnRepositoryItemClickListener onRepositoryItemClickListener) {
    this.context = context;
    this.onRepositoryItemClickListener = onRepositoryItemClickListener;
  }

  /**
   * リポジトリのデータをセットして更新する
   * @param items
   */
  public void setItemsAndRefresh(List<GitHubService.RepositoryItem> items) {
    this.items = items;
    notifyDataSetChanged();
  }

  public GitHubService.RepositoryItem getItemAt(int position) {
    return items.get(position);
  }

  /**
   * RecyclerViewのアイテムのView作成とViewを保持するViewHolderを作成
   */
  @Override
  public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(context).inflate(R.layout.repo_item, parent, false);
    return new RepoViewHolder(view);
  }

  /**
   * onCreateViewHolderで作ったViewHolderのViewに
   * setItemsAndRefresh(items)でセットされたデータを入れる
   */
  @Override
  public void onBindViewHolder(final RepoViewHolder holder, final int position) {
    final GitHubService.RepositoryItem item = getItemAt(position);

    // Viewがクリックされたら、クリックされたアイテムをListenerに教える
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onRepositoryItemClickListener.onRepositoryItemClick(item);
      }
    });

    holder.repoName.setText(item.name);
    holder.repoDetail.setText(item.description);
    holder.starCount.setText(item.stargazers_count);
    // 画像はGlideというライブラリを使ってデータをセットする
    Glide.with(context)
         .load(item.owner.avatar_url)
         .asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.repoImage) {
      @Override
      protected void setResource(Bitmap resource) {
        // 画像を丸く切り抜く
        RoundedBitmapDrawable circularBitmapDrawable =
            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
        circularBitmapDrawable.setCircular(true);
        holder.repoImage.setImageDrawable(circularBitmapDrawable);
      }
    });

  }

  @Override
  public int getItemCount() {
    if (items == null) {
      return 0;
    }
    return items.size();
  }

  interface OnRepositoryItemClickListener {
    /**
     * リポジトリのアイテムがタップされたら、呼ばれる
     */
    void onRepositoryItemClick(GitHubService.RepositoryItem item);
  }

  /**
   * Viewを保持しておくクラス
   */
  static class RepoViewHolder extends RecyclerView.ViewHolder {
    private final TextView repoName;
    private final TextView repoDetail;
    private final ImageView repoImage;
    private final TextView starCount;

    public RepoViewHolder(View itemView) {
      super(itemView);
      repoName = (TextView) itemView.findViewById(R.id.repo_name);
      repoDetail = (TextView) itemView.findViewById(R.id.repo_detail);
      repoImage = (ImageView) itemView.findViewById(R.id.repo_image);
      starCount = (TextView) itemView.findViewById(R.id.repo_star);
    }
  }
}
