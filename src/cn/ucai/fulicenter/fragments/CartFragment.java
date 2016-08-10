package cn.ucai.fulicenter.fragments;


import android.content.Context;
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

import java.util.ArrayList;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.adapter.CartAdapter;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.CartBean;
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
    ArrayList<CartBean> mArraylist;
    static final String TAG = BoutiqueFragment.class.getSimpleName();
    int action = I.ACTION_DOWNLOAD;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_boutique, container, false);
        mContext = getContext();
        initView(inflate);
        initData();
        setListener();
        return inflate;
    }
    private void initView(View inflate) {
        mArraylist = new ArrayList<>();
        mRecyclerView = (RecyclerView) inflate.findViewById(R.id.rvBoutique);
        mSwipeRefreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swBoutique);
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
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();
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

        downloadBoutique(new OkHttpUtils2.OnCompleteListener<CartBean[]>() {
            @Override
            public void onSuccess(CartBean[] result) {
                Log.e(TAG, "onSuccess,result=" + result);
                if (result != null && result.length != 0) {
                    Log.e(TAG, "result.length=" + result.length);
                    ArrayList<CartBean> list = Utils.array2List(result);
                    Log.e(TAG, "downloadBoutique,list=" + list);
                    mAdapter.initBoutiqueData(list);
                    if (action == I.ACTION_PULL_DOWN) {
                        tvRefresh.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                } else {
                    Log.e(TAG, "result=" + result);
                }
            }

            @Override
            public void onError(String error) {
                Log.i(TAG, "error=" + error);
                if (action == I.ACTION_PULL_DOWN) {
                    tvRefresh.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }
        });

    }

    private void downloadBoutique(OkHttpUtils2.OnCompleteListener<CartBean[]> listener) {
        OkHttpUtils2<CartBean[]> utils2 = new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
                .targetClass(CartBean[].class)
                .execute(listener);
    }



}
