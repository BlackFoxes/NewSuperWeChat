package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.BoutiqueDetailsActivity;
import cn.ucai.fulicenter.activity.FootHolder;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.task.UpdateCartTask;
import cn.ucai.fulicenter.utils.ImageUtils;
import cn.ucai.fulicenter.utils.OkHttpUtils2;

/**
 * Created by Administrator on 2016/8/3.
 */
public class CartAdapter extends RecyclerView.Adapter {
    ArrayList<CartBean> mlist;
    Context mContext;
    RecyclerView.ViewHolder holder;
    static final String TAG = CartAdapter.class.getSimpleName();
    public CartAdapter(Context context, ArrayList<CartBean> boutiqueArrayList) {
        mlist = new ArrayList<>();
        this.mContext = context;
        mlist.addAll(boutiqueArrayList);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(TAG, "BoutiqueAdapter.onCreateViewHolder.isChecked");
        View itemCart = LayoutInflater.from(mContext).inflate(R.layout.item_cart, null, false);
        holder = new CartItemHolder(itemCart);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e(TAG, "BoutiqueAdapter.onBindViewHolder.isChecked");
        Log.e(TAG, "BoutiqueAdapter.onBindViewHolder.position="+position);

           final CartBean cartBean = mlist.get(position);
            String currencyPrice = cartBean.getGoods().getCurrencyPrice();
            int price = Integer.parseInt(currencyPrice.substring(currencyPrice.indexOf("￥") + 1));
            int allPrice=(cartBean.getCount())*price;
            ((CartItemHolder)holder).tvCartTitle.setText(cartBean.getGoods().getGoodsName());
                ((CartItemHolder)holder).tvCartCount.setText("("+cartBean.getCount()+")");
                ((CartItemHolder)holder).tvCartPrice.setText("￥"+allPrice);
                ((CartItemHolder)holder).mCheckBox.setChecked(cartBean.isChecked());
                ImageUtils.setGoodAvatar(cartBean.getGoods().getGoodsThumb(),mContext,((CartItemHolder)holder).mCartAvatar);
                ((CartItemHolder)holder).mCartDelete.setOnClickListener(new CartChangedListener(cartBean,-1));
            ((CartItemHolder)holder).mCartAdd.setOnClickListener(new CartChangedListener(cartBean,1));
        ((CartItemHolder)holder).mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cartBean.setChecked(isChecked);
                new UpdateCartTask(mContext, cartBean).execute();



            }
        });
    }

    class CartChangedListener implements View.OnClickListener {
        CartBean mCart;
        int num;

        public CartChangedListener(CartBean mCart, int count) {
            this.mCart = mCart;
            this.num = count;
        }

        @Override
        public void onClick(View v) {
            mCart.setCount(mCart.getCount() + num);
            Log.e(TAG, "mCart.setCount=" + mCart.getCount());
            new UpdateCartTask(mContext, mCart).execute();
        }
    }
    @Override
    public int getItemCount() {
        return mlist.size()!=0?mlist.size():0;
    }
    public void initBoutiqueData(ArrayList<CartBean> list) {
        Log.e(TAG, "getItemCount,list=" + list);
        Log.e(TAG, "getItemCount,mArrayList=" + mlist);
        if (mlist != null) {
            mlist.clear();
        }
        mlist.addAll(list);
        Log.e(TAG, "getItemCount,list1=" + list);
        Log.e(TAG, "getItemCount,mArrayList1=" + mlist);
        notifyDataSetChanged();
    }
    class CartItemHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckBox;
        ImageView mCartAvatar,mCartDelete,mCartAdd;
        TextView tvCartTitle,tvCartCount, tvCartPrice;
        public CartItemHolder(View itemView) {
            super(itemView);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.cb_cart);
            mCartAvatar = (ImageView) itemView.findViewById(R.id.cart_avatar);
            mCartDelete = (ImageView) itemView.findViewById(R.id.cart_delete);
            mCartAdd = (ImageView) itemView.findViewById(R.id.cart_add);
            tvCartTitle = (TextView) itemView.findViewById(R.id.name_cart);
            tvCartCount = (TextView) itemView.findViewById(R.id.count_cart);
            tvCartPrice = (TextView) itemView.findViewById(R.id.price_cart);
        }
    }
}
