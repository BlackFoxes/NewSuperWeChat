package cn.ucai.fulicenter.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CollectAdapter;
import cn.ucai.fulicenter.adapter.GoodAdapter;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.utils.DisplayUtils;
import cn.ucai.fulicenter.utils.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/8/4.
 */
public class CollectActivity extends BaseActivity {
    RecyclerView mRecyclerView;
    CollectAdapter mAdapter;
    ArrayList<CollectBean> mArraylist;
    SwipeRefreshLayout mSwipeRefreshLayout;
    GridLayoutManager mGridLayoutManager;
    TextView tvRefresh;
    int pageId;
    int CAT_ID;
    static final String TAG = CollectActivity.class.getSimpleName();
    int action = I.ACTION_DOWNLOAD;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_collect);
        CAT_ID = getIntent().getIntExtra(I.NewAndBoutiqueGood.CAT_ID,0);
        pageId = 0;
        initView();
        setListener();
        initData();

    }
    private void setListener() {
        setPullDownRefreshListener();
        setPullUpRefreshListener();
        SetReceiverListener();
    }

    private void setPullUpRefreshListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                int a=RecyclerView.SCROLL_STATE_DRAGGING;
//                int b = RecyclerView.SCROLL_STATE_IDLE;
//                int c = RecyclerView.SCROLL_STATE_SETTLING;
//                Log.e(TAG, "a=" + a + ",b=" + b + ",c=" + c+",newState="+newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        &&lastPosition==mAdapter.getItemCount()-1) {
                    if (mAdapter.isMore()) {
                        pageId += I.PAGE_SIZE_DEFAULT;
                        action = I.ACTION_PULL_UP;
                        initData();
                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int d = mGridLayoutManager.findFirstVisibleItemPosition();
                int f = mGridLayoutManager.findFirstCompletelyVisibleItemPosition();
                int e = mGridLayoutManager.findLastVisibleItemPosition();
                mSwipeRefreshLayout.setEnabled(f==0);
                Log.e(TAG, "d=" + d + ",e=" + e);
                lastPosition=mGridLayoutManager.findLastVisibleItemPosition();
            }
        });
    }
    private void setPullDownRefreshListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageId = 0;
                tvRefresh.setVisibility(View.VISIBLE);
                action = I.ACTION_PULL_DOWN;
                initData();
            }
        });
    }
    private void initData() {
        String userName = FuLiCenterApplication.getInstance().getUserName();
        if (userName.isEmpty())finish();
        findCollectList(new OkHttpUtils2.OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                mSwipeRefreshLayout.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(true);
                mAdapter.setTextFooter(getResources().getString(R.string.load_more));
                Log.e(TAG, "result=" + result);
                if (result != null) {
                    Log.e(TAG, "result.size=" + result.length);
                    ArrayList<CollectBean> collectList = Utils.array2List(result);
                    if (action == I.ACTION_PULL_DOWN || action == I.ACTION_DOWNLOAD) {
                        mAdapter.initData(collectList);
                    } else if (action == I.ACTION_PULL_UP) {
                        mAdapter.addData(collectList);
                    }
                    if (collectList.size() < I.PAGE_SIZE_DEFAULT) {
                        mAdapter.setMore(false);
                        mAdapter.setTextFooter(getResources().getString(R.string.no_more_data));
                    }
                } else {
                    mAdapter.setMore(false);
                    mAdapter.setTextFooter(getResources().getString(R.string.no_more_data));
                }
            }
            @Override
            public void onError(String error) {
                Log.e(TAG, "error=" + error);
                mSwipeRefreshLayout.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
            }
        });
    }
    private void findCollectList(OkHttpUtils2.OnCompleteListener<CollectBean[]> listenter) {
        OkHttpUtils2<CollectBean[]> utils2 = new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_FIND_COLLECTS)
                .addParam(I.Collect.USER_NAME, FuLiCenterApplication.getInstance().getUserName())
                .addParam(I.PAGE_ID, String.valueOf(pageId))
                .addParam(I.PAGE_SIZE, String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(CollectBean[].class)
                .execute(listenter);
    }
    private void initView() {
        mArraylist = new ArrayList<>();
        DisplayUtils.initBackWithTitle(this,"收藏的宝贝");
        tvRefresh = (TextView) findViewById(R.id.tvRefresh);
        mAdapter = new CollectAdapter(this, mArraylist);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mSwipCollect);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.google_blue,
                R.color.google_yellow,
                R.color.google_green,
                R.color.google_red

        );
        mGridLayoutManager = new GridLayoutManager(this, 2);
        mGridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRvCollect);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }
    ReceiverListener mReceiver = new ReceiverListener();


    class ReceiverListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            initData();

        }

    }

    private void SetReceiverListener() {
        IntentFilter filter = new IntentFilter("update_collect_list");
        registerReceiver(mReceiver, filter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
