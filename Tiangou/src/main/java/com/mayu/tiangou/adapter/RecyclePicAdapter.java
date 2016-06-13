package com.mayu.tiangou.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.like.LikeButton;
import com.mayu.tiangou.R;
import com.mayu.tiangou.common.util.DensityUtil;
import com.mayu.tiangou.entity.ImageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maning on 16/5/17.
 */
public class RecyclePicAdapter extends RecyclerView.Adapter<RecyclePicAdapter.MyViewHolder> {
    private Context context;
    private List<ImageBean.TngouBean> commonDataResults;
    private LayoutInflater layoutInflater;
    private List<Integer> mHeights;
    private int ScreenHeight;

    public RecyclePicAdapter(Context context, List<ImageBean.TngouBean> commonDataResults) {
        this.context = context;
        this.commonDataResults = commonDataResults;
        layoutInflater = LayoutInflater.from(this.context);
        ScreenHeight = DensityUtil.getHeight(context);
        //高度
        mHeights = new ArrayList<>();
        addHeights();
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void updateDatas(List<ImageBean.TngouBean> commonDataResults) {
        this.commonDataResults = commonDataResults;
        //这里多计算了高度，因为滑动太快的话，这么可能出现计算不及时崩溃现象
        addHeights();
        notifyDataSetChanged();
    }

    private void addHeights() {
        for (int i = 0; i < commonDataResults.size(); i++) {
            mHeights.add((int) (ScreenHeight * 0.25 + Math.random() * ScreenHeight * 0.25));
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflate = layoutInflater.inflate(R.layout.item_welfare_staggered, parent, false);

        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {

        final ImageBean.TngouBean resultsEntity = commonDataResults.get(position);
        viewHolder.tvShowTime.setText(resultsEntity.getTitle());
        //图片显示
//        int w = (DensityUtil.getWidth(context) - DensityUtil.dip2px(context, 18)) / 2;
        String url = "http://tnfs.tngou.net/image" + resultsEntity.getImg();
        int h = DensityUtil.dip2px(context, 240);
        int w = (DensityUtil.getWidth(context) - DensityUtil.dip2px(context, 18)) / 2;
//        SimpleTarget target = new SimpleTarget<Bitmap>(w, h) {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                viewHolder.image.setImageBitmap(resource);
//            }
//        };
//
        SimpleTarget t= new SimpleTarget(w, h) {
            @Override
            public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
                viewHolder.image.setImageBitmap((Bitmap) resource);
            }
        };

        Glide.with(context)
                .load(url)
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.pic_gray_bg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(t);

        //查询是否存在收藏
//        boolean isCollect = new CollectDao().queryOneCollectByID(resultsEntity.get_id());
//        boolean isCollect = false;
//        if (isCollect) {
//            viewHolder.btnCollect.setLiked(true);
//        } else {
//            viewHolder.btnCollect.setLiked(false);
//        }
//        viewHolder.btnCollect.setOnLikeListener(new OnLikeListener() {
//            @Override
//            public void liked(LikeButton likeButton) {
//                boolean insertResult = new CollectDao().insertOneCollect(resultsEntity);
//                if (insertResult) {
//                    Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "收藏失败", Toast.LENGTH_SHORT).show();
//                    likeButton.setLiked(false);
//                }
//            }
//
//            @Override
//            public void unLiked(LikeButton likeButton) {
//                boolean deleteResult = new CollectDao().deleteOneCollect(resultsEntity.get_id());
//                if (deleteResult) {
//                    Toast.makeText(context, "取消收藏成功", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "取消收藏失败", Toast.LENGTH_SHORT).show();
//                    likeButton.setLiked(true);
//                }
//
//            }
//        });

        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return commonDataResults.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        LikeButton btnCollect;
        TextView tvShowTime;
        RelativeLayout rlRoot;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            btnCollect = (LikeButton) itemView.findViewById(R.id.btn_collect);
            tvShowTime = (TextView) itemView.findViewById(R.id.tvShowTime);
            rlRoot = (RelativeLayout) itemView.findViewById(R.id.rl_root);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }
}
