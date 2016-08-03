package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.FootHolder;
import cn.ucai.fulicenter.activity.GoodDetailsActivity;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.utils.ImageUtils;

/**
 * Created by Administrator on 2016/8/1.
 */
public class GoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    ArrayList<NewGoodBean> mArrayList;
    RecyclerView.ViewHolder holder;
    boolean isMore;
    String textFooter;

    public void setTextFooter(String textFooter) {
        this.textFooter = textFooter;
        notifyDataSetChanged();
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public GoodAdapter(Context mContext, ArrayList<NewGoodBean> list) {
        this.mContext = mContext;
        this.mArrayList = list;
        sortByAddTime();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("onCreateViewHolder","onCreateViewHolder.isChecked");
        switch (viewType) {
            case I.TYPE_FOOTER:
                View inflate = LayoutInflater.from(mContext).inflate(R.layout.load_footer, null, false);
                holder = new FootHolder(inflate);
                break;
            case I.TYPE_ITEM:
                View inflate1 = LayoutInflater.from(mContext).inflate(R.layout.new_good_details, null, false);
                holder = new NewGoodHolder(inflate1);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.e("onBindViewHolder","onBindViewHolder.isChecked");
        Log.e("onBindViewHolder", "position=" + position);
        if (holder instanceof FootHolder) {
            ((FootHolder) holder).mFooter.setText(textFooter);

        } else if (holder instanceof NewGoodHolder){
            final NewGoodBean goodBean = mArrayList.get(position);
            ((NewGoodHolder)holder).mPrice.setText(goodBean.getCurrencyPrice());
            ((NewGoodHolder)holder).mName.setText(goodBean.getGoodsName());
            ((NewGoodHolder)holder).mGoodAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GoodDetailsActivity.class);
                    intent.putExtra(D.GoodDetails.KEY_GOODS_ID, goodBean.getGoodsId());
                    mContext.startActivity(intent);
                }
            });
            ImageUtils.setGoodAvatar(goodBean.getGoodsThumb(),mContext,((NewGoodHolder)holder).mGoodAvatar);
        }


    }

    @Override
    public int getItemCount() {
        return mArrayList!=null?mArrayList.size()+1:1;

    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    public void initData(ArrayList<NewGoodBean> newGoodBeen) {
        if (mArrayList != null) {
            mArrayList.clear();
        }
        Log.e("initData", "mArrayList.size=" + mArrayList.size());
        mArrayList.addAll(newGoodBeen);
        sortByAddTime();
        notifyDataSetChanged();


    }

    public void addData(ArrayList<NewGoodBean> newGoodBeen) {
        mArrayList.addAll(newGoodBeen);
        sortByAddTime();
        notifyDataSetChanged();

    }

    class NewGoodHolder extends RecyclerView.ViewHolder {
        ImageView mGoodAvatar;
        TextView mPrice;
        TextView mName;
        public NewGoodHolder(View itemView) {
            super(itemView);
            mGoodAvatar = (ImageView) itemView.findViewById(R.id.niv_good_thumb);
            mPrice = (TextView) itemView.findViewById(R.id.tv_good_price);
            mName = (TextView) itemView.findViewById(R.id.tv_good_name);
        }

    }

    private void sortByAddTime() {
        Collections.sort(mArrayList, new Comparator<NewGoodBean>() {
            @Override
            public int compare(NewGoodBean goodLeft, NewGoodBean goodRight) {
                return (int) (Long.valueOf(goodRight.getAddTime())-Long.valueOf(goodLeft.getAddTime()));
            }
        });

    }
}
