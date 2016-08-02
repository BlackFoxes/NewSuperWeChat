package cn.ucai.fulicenter.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.ucai.fulicenter.R;

/**
 * Created by Administrator on 2016/8/2.
 */
public class FootHolder extends RecyclerView.ViewHolder{
    public TextView mFooter;
    public FootHolder(View itemView) {
        super(itemView);
        mFooter = (TextView) itemView.findViewById(R.id.tvFooter);
    }
}
