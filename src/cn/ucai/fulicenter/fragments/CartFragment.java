package cn.ucai.fulicenter.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.adapter.CartAdapter;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodDetailsBean;
import cn.ucai.fulicenter.utils.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    CartAdapter mAdapter;
    LinearLayoutManager mLinearLayoutManager;
    TextView tvRefresh;
    Context mContext;
    TextView tvAllPrice, tvSavedPrice,tvCartHint;
    private ArrayList<CartBean> mArraylist;
    static final String TAG = CartFragment.class.getSimpleName();
    int action = I.ACTION_DOWNLOAD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_cart, container, false);
        mContext = getContext();
        initView(inflate);
//        initData();
        setListener();
        return inflate;
    }
    private void initView(View inflate) {
        mArraylist = new ArrayList<>();
        mRecyclerView = (RecyclerView) inflate.findViewById(R.id.rvCart);
        mSwipeRefreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swCart);
        tvRefresh = (TextView) inflate.findViewById(R.id.tvRefresh);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.google_red,
                R.color.google_green,
                R.color.google_yellow,
                R.color.google_blue
        );
        mAdapter = new CartAdapter(mContext, mArraylist);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setAdapter(mAdapter);
        tvAllPrice = (TextView) inflate.findViewById(R.id.allPrice);
        tvCartHint = (TextView) inflate.findViewById(R.id.tvCartHint);
        tvSavedPrice = (TextView) inflate.findViewById(R.id.savedPrice);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();
        initReceiverListener();
    }

    private void setPullUpListener() {

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int firstPosition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstPosition = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                mSwipeRefreshLayout.setEnabled(firstPosition==0);
            }
        });
    }

    private void setPullDownListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                action = I.ACTION_PULL_DOWN;
                mSwipeRefreshLayout.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                initData();
            }
        });
    }
    private void initData() {
       List<CartBean> cartList = FuLiCenterApplication.getInstance().getCartList();
        Log.e(TAG, "cartList.size=" + cartList.size());
        mArraylist.clear();
        mArraylist.addAll(cartList);
        if (mArraylist != null && mArraylist.size() > 0) {
            Log.e(TAG, "result.length=" + mArraylist.size());
            Log.e(TAG, "initData,mArraylist=" + mArraylist);
            tvCartHint.setVisibility(View.GONE);
            mAdapter.initBoutiqueData(mArraylist);
            if (action == I.ACTION_PULL_DOWN) {
                tvRefresh.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        } else {
            tvCartHint.setVisibility(View.VISIBLE);
            Log.e(TAG, "mArraylist=" + mArraylist);
        }
        sumPrice();
    }
    CartReceiver mReveiver=new CartReceiver();

    class CartReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            initData();
        }
    }
    private void initReceiverListener() {
        IntentFilter filter = new IntentFilter("update_cart_list");
        mContext.registerReceiver(mReveiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mReveiver);
    }

    private void sumPrice() {
        if (mArraylist != null && mArraylist.size() > 0) {
            int sumPrice = 0;
            int rankPrice = 0;
            for (CartBean cart : mArraylist) {
                GoodDetailsBean goods = cart.getGoods();
                if (goods != null && cart.isChecked()) {
                    sumPrice += convertPrice(goods.getCurrencyPrice()) * cart.getCount();
                    rankPrice += convertPrice(goods.getRankPrice()) * cart.getCount();
                }
            }
            tvAllPrice.setText("合计:￥" + sumPrice);
            tvSavedPrice.setText("节省:￥" + (sumPrice - rankPrice));
        } else {
            tvAllPrice.setText("合计:￥00.00");
            tvSavedPrice.setText("节省:￥00.00");
        }

    }

    private int convertPrice(String price) {
        price = price.substring(price.indexOf("￥") + 1);
        return Integer.parseInt(price);


    }
}
