package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.DemoHXSDKHelper;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.AlbumsBean;
import cn.ucai.fulicenter.bean.GoodDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.task.DownloadCollectCountTask;
import cn.ucai.fulicenter.utils.DisplayUtils;
import cn.ucai.fulicenter.utils.OkHttpUtils2;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.SlideAutoLoopView;
/**
 * Created by Administrator on 2016/8/3.
 */
public class GoodDetailsActivity extends BaseActivity {
    GoodDetailsActivity mContext;
    TextView tvNameEnglish,tvName,tvCurrentPrice, tvShopPrice,tvCartCount;
    WebView mWebView;
    ImageView ivCollect,ivShared, ivCart;
    SlideAutoLoopView mSlideAutoLoopView;
    FlowIndicator mFlowIndicator;
    static final String TAG = GoodDetailsActivity.class.getSimpleName();
    int mGoodId;
    boolean isCollected;
    GoodDetailsBean mGoodDetails;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_good_details);
        mContext = this;
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        CollectListener mListener=new CollectListener();
        ivCollect.setOnClickListener(mListener);


    }

    class CollectListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (DemoHXSDKHelper.getInstance().isLogined()) {

                switch (v.getId()) {
                    case R.id.iv_collect:
                        if (isCollected) {
                            OkHttpUtils2<MessageBean> utils2 = new OkHttpUtils2<MessageBean>();
                            utils2.setRequestUrl(I.REQUEST_DELETE_COLLECT)
                                    .addParam(I.Collect.USER_NAME, FuLiCenterApplication.getInstance().getUserName())
                                    .addParam(I.Collect.GOODS_ID, String.valueOf(mGoodId))
                                    .targetClass(MessageBean.class)
                                    .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                                        @Override
                                        public void onSuccess(MessageBean msg) {
                                            Log.e(TAG, "result=" + msg);
                                            if (msg != null && msg.isSuccess()) {
                                                new DownloadCollectCountTask(FuLiCenterApplication.getInstance().getUserName(), mContext).execute();
                                                isCollected = false;
                                                setCollectButton();
                                                sendStickyBroadcast(new Intent("update_collect_list"));

                                            } else {
                                                Log.e(TAG, "delete collect id failed");

                                            }
                                            Toast.makeText(mContext, msg.getMsg(), Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onError(String error) {
                                            Log.e(TAG, "error=" + error);


                                        }
                                    });

                        } else {
                            OkHttpUtils2<MessageBean> utils = new OkHttpUtils2<>();
                            utils.setRequestUrl(I.REQUEST_ADD_COLLECT)
                                    .addParam(I.Collect.USER_NAME,FuLiCenterApplication.getInstance().getUserName())
                                    .addParam(I.Collect.GOODS_ID, String.valueOf(mGoodDetails.getGoodsId()))
                                    .addParam(I.Collect.ADD_TIME, String.valueOf(mGoodDetails.getAddTime()))
                                    .addParam(I.Collect.GOODS_ENGLISH_NAME,mGoodDetails.getGoodsEnglishName())
                                    .addParam(I.Collect.GOODS_THUMB,mGoodDetails.getGoodsThumb())
                                    .addParam(I.Collect.GOODS_IMG,mGoodDetails.getGoodsImg())
                                    .addParam(I.Collect.GOODS_NAME,mGoodDetails.getGoodsName())
                                    .targetClass(MessageBean.class)
                                    .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                                        @Override
                                        public void onSuccess(MessageBean result) {
                                            Log.e(TAG, "result=" + result);
                                            if (result != null && result.isSuccess()) {
                                                isCollected = true;
                                                setCollectButton();
                                                new DownloadCollectCountTask(FuLiCenterApplication.getInstance().getUserName(), mContext).execute();

                                            }

                                        }

                                        @Override
                                        public void onError(String error) {
                                            Log.e(TAG, "error=" + error);

                                        }
                                    });

                        }

                        break;
                }
            } else {
                startActivity(new Intent(GoodDetailsActivity.this,LoginActivity.class));
            }
        }
    }

    private void setCollectButton() {
        if (isCollected) {
            ivCollect.setImageResource(R.drawable.bg_collect_out);


        } else {
            ivCollect.setImageResource(R.drawable.bg_collect_in);

        }

    }

    private void initData() {
        mGoodId = getIntent().getIntExtra(D.GoodDetails.KEY_GOODS_ID, 0);
        Log.e(TAG, "mGoodId=" + mGoodId);
        if (mGoodId > 0) {
            getGoodDetailsByGoodId(new OkHttpUtils2.OnCompleteListener<GoodDetailsBean>() {
                @Override
                public void onSuccess(GoodDetailsBean result) {
                    if (result != null) {
                        Log.e(TAG, "result=" + result);
                        mGoodDetails = result;
                        showGoodDetails();
                    }
                }
                @Override
                public void onError(String error) {
                    Log.e(TAG, "error=" + error);
                    finish();
                    Toast.makeText(mContext,"获取商品详细数据失败！",Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            finish();
            Toast.makeText(mContext,"获取商品详细数据失败！",Toast.LENGTH_SHORT).show();
        }
    }
    private void showGoodDetails() {
        tvName.setText(mGoodDetails.getGoodsName());
        tvNameEnglish.setText(mGoodDetails.getGoodsEnglishName());
        tvShopPrice.setText(mGoodDetails.getShopPrice());
        tvCurrentPrice.setText(mGoodDetails.getCurrencyPrice());
        mSlideAutoLoopView.startPlayLoop(mFlowIndicator,
                getAlbumImageUrl(),getAlbumImageSize());
        mWebView.loadDataWithBaseURL(null,mGoodDetails.getGoodsBrief(),D.TEXT_HTML,D.UTF_8,null);
    }
    private String[] getAlbumImageUrl() {
        String[] albumImageUrl = new String[]{};
        if (mGoodDetails.getProperties() != null && mGoodDetails.getProperties().length > 0) {
            AlbumsBean[] albumsBeen = mGoodDetails.getProperties()[0].getAlbums();
            albumImageUrl = new String[albumsBeen.length];
            for (int i=0;i<albumImageUrl.length;i++) {
                albumImageUrl[i] = albumsBeen[i].getImgUrl();
                Log.e(TAG, "albumImageUrl" + albumsBeen[i].getImgUrl());
            }
        }
        return albumImageUrl;
    }
    private int getAlbumImageSize() {
        if (mGoodDetails.getProperties() != null && mGoodDetails.getProperties().length > 0) {
            return mGoodDetails.getProperties()[0].getAlbums().length;
        }
        return 0;
    }
    private void getGoodDetailsByGoodId(OkHttpUtils2.OnCompleteListener<GoodDetailsBean> listener) {
        OkHttpUtils2<GoodDetailsBean> utils2 = new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(D.GoodDetails.KEY_GOODS_ID,String.valueOf(mGoodId))
                .targetClass(GoodDetailsBean.class)
                .execute(listener);
    }
    private void initView() {
        DisplayUtils.initBack(mContext);
        tvNameEnglish = (TextView) findViewById(R.id.tv_good_current_name_english);
        tvName = (TextView) findViewById(R.id.tv_good_current_name);
        tvCurrentPrice = (TextView) findViewById(R.id.tv_good_price_current);
        tvShopPrice = (TextView) findViewById(R.id.tv_good_price_shop);
        tvCartCount = (TextView) findViewById(R.id.tv_cart_cout);
        mWebView = (WebView) findViewById(R.id.wv_good_bried);
        ivCart = (ImageView) findViewById(R.id.iv_cart);
        ivShared = (ImageView) findViewById(R.id.iv_shared);
        ivCollect = (ImageView) findViewById(R.id.iv_collect);
        mSlideAutoLoopView = (SlideAutoLoopView) findViewById(R.id.salv);
        mFlowIndicator = (FlowIndicator) findViewById(R.id.indicator);
        WebSettings settings = mWebView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setBuiltInZoomControls(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DemoHXSDKHelper.getInstance().isLogined()) {
            initCollectImage();
        }
    }

    private void initCollectImage() {
        OkHttpUtils2<MessageBean> utils2 = new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_IS_COLLECT)
                .addParam(I.Collect.USER_NAME, FuLiCenterApplication.getInstance().getUserName())
                .addParam(I.Collect.GOODS_ID, String.valueOf(mGoodId))
                .targetClass(MessageBean.class)
                .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean msg) {
                        Log.e(TAG, "result=" + msg);
                        if (msg != null && msg.isSuccess()) {
                            ivCollect.setImageResource(R.drawable.bg_collect_out);
                            isCollected = true;
                        } else {
                            Log.e(TAG, "REQUEST_IS_COLLECT is failed");
                            isCollected = false;

                        }

                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "error=" + error);

                    }
                });



    }
}
