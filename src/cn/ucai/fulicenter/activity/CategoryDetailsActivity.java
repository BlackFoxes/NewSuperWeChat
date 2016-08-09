package cn.ucai.fulicenter.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.internal.Util;

import java.util.ArrayList;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.GoodAdapter;
import cn.ucai.fulicenter.adapter.NewFriendsMsgAdapter;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.utils.DisplayUtils;
import cn.ucai.fulicenter.utils.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;
import cn.ucai.fulicenter.view.CatChildFilterButton;

/**
 * Created by Administrator on 2016/8/5.
 */
public class CategoryDetailsActivity extends BaseActivity {

    Context mContext;
    RecyclerView mRecyclerView;
    GoodAdapter mAdapter;
    ArrayList<NewGoodBean> mArraylist;
    SwipeRefreshLayout mSwipeRefreshLayout;
    GridLayoutManager mGridLayoutManager;
    TextView tvRefresh;
    int pageId;
    int childId;
    ArrayList<CategoryChildBean> childList;
    boolean sortByPrice;
    boolean sortByAddTime;
    int SortBy;
    Button btSortPrice;
    Button btSortAddTime;
    String groupName;
    static final String TAG = CategoryDetailsActivity.class.getSimpleName();
    int action = I.ACTION_DOWNLOAD;
    CatChildFilterButton mChildButton;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_category_details);
        mContext = this;
        childId = getIntent().getIntExtra(I.CategoryChild.CAT_ID, 0);
        Bundle bundle = getIntent().getExtras();
        childList= (ArrayList<CategoryChildBean>) bundle.getSerializable("childList");
        groupName = getIntent().getStringExtra("groupName");
        pageId = 0;
        initView();
        setListener();
        initData();
    }
    private void initView() {
        mArraylist = new ArrayList<>();
        tvRefresh = (TextView) findViewById(R.id.tvRefresh);
        mAdapter = new GoodAdapter(mContext, mArraylist);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swCategory);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.google_blue,
                R.color.google_yellow,
                R.color.google_green,
                R.color.google_red
        );
        mChildButton = (CatChildFilterButton) findViewById(R.id.btnCatChildFilter);
        btSortAddTime = (Button) findViewById(R.id.btnSortAddTime);
        btSortPrice = (Button) findViewById(R.id.btnSortPrice);
        mGridLayoutManager = new GridLayoutManager(mContext, 2);
        mGridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvCategory);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }

    class SortListener implements View.OnClickListener {
        Drawable arrow;



        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnSortAddTime:
                    if (sortByAddTime) {
                        SortBy = I.SORT_BY_ADDTIME_ASC;
                        arrow = getResources().getDrawable(R.drawable.arrow_order_up);



                    } else {
                        SortBy = I.SORT_BY_ADDTIME_DESC;
                        arrow = getResources().getDrawable(R.drawable.arrow_order_down);


                    }
                    btSortAddTime.setCompoundDrawablesWithIntrinsicBounds(null,null,arrow,null);
                    sortByAddTime = !sortByAddTime;

                    break;
                case R.id.btnSortPrice:
                    if (sortByPrice) {
                        SortBy = I.SORT_BY_PRICE_ASC;
                        arrow = getResources().getDrawable(R.drawable.arrow_order_up);


                    } else {
                        SortBy = I.SORT_BY_PRICE_DESC;
                        arrow = getResources().getDrawable(R.drawable.arrow_order_down);


                    }
                    btSortPrice.setCompoundDrawablesWithIntrinsicBounds(null,null,arrow,null);
                    sortByPrice = !sortByPrice;
                    break;
            }
            mAdapter.setSort(SortBy);

        }
    }

    private void setListener() {
        setPullDownRefreshListener();
        setPullUpRefreshListener();
        setSoryByListener();
        setPopuWindowListener();
        DisplayUtils.initBack(this);
    }

    private void setPopuWindowListener() {
        mChildButton.setText(groupName);
        mChildButton.setOnCatFilterClickListener(groupName, childList);

    }


    private void setSoryByListener() {
        SortListener sortListener = new SortListener();
        btSortAddTime.setOnClickListener(sortListener);
        btSortPrice.setOnClickListener(sortListener);
        mAdapter.setSort(SortBy);


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
        downloadNewGood(new OkHttpUtils2.OnCompleteListener<NewGoodBean[]>() {
            @Override
            public void onSuccess(NewGoodBean[] result) {
                mSwipeRefreshLayout.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(true);
                mAdapter.setTextFooter(getResources().getString(R.string.load_more));
                Log.e(TAG, "result=" + result);
                if (result != null) {
                    Log.e(TAG, "result.size=" + result.length);
                    ArrayList<NewGoodBean> newGoodBeen = Utils.array2List(result);
                    if (action == I.ACTION_PULL_DOWN || action == I.ACTION_DOWNLOAD) {
                        mAdapter.initData(newGoodBeen);
                    } else if (action == I.ACTION_PULL_UP) {
                        mAdapter.addData(newGoodBeen);
                    }
                    if (newGoodBeen.size() < I.PAGE_SIZE_DEFAULT) {
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
    private void downloadNewGood(OkHttpUtils2.OnCompleteListener<NewGoodBean[]> listenter) {
        OkHttpUtils2<NewGoodBean[]> utils2 = new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGood.CAT_ID, String.valueOf(childId))
                .addParam(I.PAGE_ID, String.valueOf(pageId))
                .addParam(I.PAGE_SIZE, String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodBean[].class)
                .execute(listenter);
    }

}
