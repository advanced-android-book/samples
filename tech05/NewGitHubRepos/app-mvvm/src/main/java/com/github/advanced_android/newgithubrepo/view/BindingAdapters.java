package com.github.advanced_android.newgithubrepo.view;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

public class BindingAdapters {

  @BindingAdapter({"imageUrl"})
  public static void loadImage(final ImageView imageView, final String imageUrl) {
    // 画像はGlideというライブラリを使ってデータをセットする
    Glide.with(imageView.getContext())
         .load(imageUrl)
         .asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
      @Override
      protected void setResource(Bitmap resource) {
        // 画像を丸く切り抜く
        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.getResources(), resource);
        circularBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(circularBitmapDrawable);
      }
    });
  }
}
