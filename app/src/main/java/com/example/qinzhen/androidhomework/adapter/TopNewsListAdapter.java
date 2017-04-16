package com.example.qinzhen.androidhomework.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qinzhen.androidhomework.R;
import com.example.qinzhen.androidhomework.model.entity.NewsBean;
import com.pandaq.pandaqlib.magicrecyclerView.BaseItem;
import com.pandaq.pandaqlib.magicrecyclerView.BaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @anthor qinzhen
 * @time 2017/4/15 23:47
 */

public class TopNewsListAdapter extends BaseRecyclerAdapter{

    private Context mContext;
    private int widthPx;
    private int heighPx;

    public TopNewsListAdapter(Fragment fragment){
        mContext = fragment.getContext();
        float width = mContext.getResources().getDimension(R.dimen.news_image_width);
        widthPx = (int) width;
        heighPx = widthPx *3 /4;
    }


    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.topnews_item,parent,false);
        return new NormalViewHolder(view);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, BaseItem data) {
          if(viewHolder instanceof NormalViewHolder){
              NewsBean topNews = (NewsBean) data.getData();
              ((NormalViewHolder)viewHolder).mNewsTitle.setText(topNews.getTitle());
              ((NormalViewHolder)viewHolder).mSource.setText(topNews.getSource());
              String image = topNews.getImgsrc();
              if(!TextUtils.isEmpty(image)){
                  Picasso.with(mContext)
                          .load(image)
                          .resize(widthPx,heighPx)
                          .error(R.drawable.ic_action_download)
                          .into(((NormalViewHolder) viewHolder).mNewsImage);

              }
          }
    }



    static class NormalViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.news_image)
        ImageView mNewsImage;
        @BindView(R.id.news_title)
        TextView mNewsTitle;
        @BindView(R.id.source)
        TextView mSource;



        public NormalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
