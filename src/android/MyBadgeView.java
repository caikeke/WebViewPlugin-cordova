package com.webview.yhck;

import android.content.Context;
import android.util.AttributeSet;

import com.jauker.widget.BadgeView;

/**
 * Created by YH_CK on 2017/07/10.
 */

public class MyBadgeView extends BadgeView {

    public MyBadgeView(Context context) {
        super(context, null);
    }

    public MyBadgeView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public MyBadgeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setBadgeCount(int count) {
        String mCount = "";
        if(count>9){
            mCount="9+";
        }
        this.setText(mCount);
    }
}
