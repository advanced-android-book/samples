package com.github.advanced_android.newgithubrepo.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.advanced_android.newgithubrepo.R;
import com.github.advanced_android.newgithubrepo.contract.RepositoryListViewContract;
import com.github.advanced_android.newgithubrepo.databinding.RepoItemBinding;
import com.github.advanced_android.newgithubrepo.model.GitHubService;
import com.github.advanced_android.newgithubrepo.viewmodel.RepositoryItemViewModel;

import java.util.List;

/**
 * RecyclerViewでリポジトリのリストを表示するためのAdapterクラス
 * このクラスによりRecyclerViewのアイテムのViewの生成し、Viewにデータを入れる
 */
public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepoViewHolder> {
  private final RepositoryListViewContract view;
  private final Context context;
  private List<GitHubService.RepositoryItem> items;

  public RepositoryAdapter(Context context, RepositoryListViewContract view) {
    this.context = context;
    this.view = view;
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
    RepoItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.repo_item, parent, false);
    binding.setViewModel(new RepositoryItemViewModel(view));
    return new RepoViewHolder(binding.getRoot(), binding.getViewModel());
  }

  /**
   * onCreateViewHolderで作ったViewHolderのViewに
   * setItemsAndRefresh(items)でセットされたデータを入れる
   */
  @Override
  public void onBindViewHolder(final RepoViewHolder holder, final int position) {
    final GitHubService.RepositoryItem item = getItemAt(position);
    holder.loadItem(item);

  }

  @Override
  public int getItemCount() {
    if (items == null) {
      return 0;
    }
    return items.size();
  }

  /**
   * Viewを保持しておくクラス
   * ここではViewModelを持つ
   */
  static class RepoViewHolder extends RecyclerView.ViewHolder {
    private final RepositoryItemViewModel viewModel;

    public RepoViewHolder(View itemView, RepositoryItemViewModel viewModel) {
      super(itemView);
      this.viewModel = viewModel;
    }

    public void loadItem(GitHubService.RepositoryItem item) {
      viewModel.loadItem(item);
    }
  }


}
