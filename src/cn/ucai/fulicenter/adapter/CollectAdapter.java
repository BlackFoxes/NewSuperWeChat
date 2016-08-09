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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.FootHolder;
import cn.ucai.fulicenter.activity.GoodDetailsActivity;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.task.DownloadCollectCountTask;
import cn.ucai.fulicenter.utils.ImageUtils;
import cn.ucai.fulicenter.utils.OkHttpUtils2;

/**
 * Created by Administrator on 2016/8/1.
 */
public class CollectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    ArrayList<CollectBean> mArrayList;
    RecyclerView.ViewHolder holder;
    boolean isMore;
    String textFooter;
    public int adapterSort = I.SORT_BY_ADDTIME_ASC;
    final static String TAG = CollectAdapter.class.getSimpleName();
    public void setSort(int sort) {
        this.adapterSort = sort;
        notifyDataSetChanged();
    }

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

    public CollectAdapter(Context mContext, ArrayList<CollectBean> list) {
        this.mContext = mContext;
        this.mArrayList = list;
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
                View inflate1 = LayoutInflater.from(mContext).inflate(R.layout.item_collect, null, false);
                holder = new CollectHolder(inflate1);
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

        } else if (holder instanceof CollectHolder){
            final CollectBean goodBean = mArrayList.get(position);
            ((CollectHolder)holder).mName.setText(goodBean.getGoodsName());
            ((CollectHolder)holder).mGoodAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GoodDetailsActivity.class);
                    intent.putExtra(D.GoodDetails.KEY_GOODS_ID, goodBean.getGoodsId());
                    mContext.startActivity(intent);
                }
            });
            ImageUtils.setGoodAvatar(goodBean.getGoodsThumb(),mContext,((CollectHolder)holder).mGoodAvatar);
            ((CollectHolder)holder).ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OkHttpUtils2<MessageBean> utils2 = new OkHttpUtils2<MessageBean>();
                    utils2.setRequestUrl(I.REQUEST_DELETE_COLLECT)
                            .addParam(I.Collect.USER_NAME, FuLiCenterApplication.getInstance().getUserName())
                            .addParam(I.Collect.GOODS_ID, String.valueOf(goodBean.getGoodsId()))
                            .targetClass(MessageBean.class)
                            .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                                @Override
                                public void onSuccess(MessageBean msg) {
                                    Log.e(TAG, "result=" + msg);
                                    if (msg != null && msg.isSuccess()) {
                                        mArrayList.remove(goodBean);
                                        new DownloadCollectCountTask(FuLiCenterApplication.getInstance().getUserName(), mContext).execute();
                                        notifyDataSetChanged();

                                    } else {
                                        Log.e(TAG, "delete collect id failed");

                                    }
                                    Toast.makeText(mContext,msg.getMsg(),Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onError(String error) {
                                    Log.e(TAG, "error=" + error);


                                }
                            });

                }
            });

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

    public void initData(ArrayList<CollectBean> newGoodBeen) {
        if (mArrayList != null) {
            mArrayList.clear();
        }
        Log.e("initData", "mArrayList.size=" + mArrayList.size());
        mArrayList.addAll(newGoodBeen);
        notifyDataSetChanged();


    }

    public void addData(ArrayList<CollectBean> newGoodBeen) {
        mArrayList.addAll(newGoodBeen);
        notifyDataSetChanged();

    }

    class CollectHolder extends RecyclerView.ViewHolder {
        ImageView mGoodAvatar;
        ImageView ivDelete;
        TextView mName;
        public CollectHolder(View itemView) {
            super(itemView);
            mGoodAvatar = (ImageView) itemView.findViewById(R.id.niv_good_thumb);
            ivDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
            mName = (TextView) itemView.findViewById(R.id.tv_good_name);
        }

    }




    private int getPrice(String price) {
        String s = price.substring(price.indexOf("￥") + 1);
        return Integer.parseInt(s);

    }
}
