package com.webview.yhck;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yhloan.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;

public class WebViewActivity extends Activity {

    //返回按纽
    private ImageView mBackView;
    //消息列表
    private ImageView mNewsView;
    //消息提醒
    private MyBadgeView mBadgeView;
    //标题
    private TextView mTitleView;
    //显示消息个数
    private int mNumber;
    //显示标题
    private String mTitle;
   private String urlHost;
  private String mURL;
    WebView mWebview;
    WebSettings mWebSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mBackView = (ImageView) findViewById(R.id.back_view);
        mTitleView = (TextView) findViewById(R.id.title_view);
        mNewsView = (ImageView) findViewById(R.id.news_view);
        mBadgeView = new MyBadgeView(WebViewActivity.this);
        initView();//初始化页面

        mWebview = (WebView) findViewById(R.id.webView);
        initWebView();//初始化WebView
        mBackView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            setResult(1001);
            WebViewActivity.this.finish();;
          }
       });
      mNewsView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          setResult(1001);
          WebViewActivity.this.finish();
        }
      });
    }

    private void initView(){
      String paramJSON = getIntent().getStringExtra("Param");
      try {
        JSONObject jsonObject = new JSONObject(paramJSON);
        mNumber=Integer.valueOf(jsonObject.get("badgeNum").toString());
        urlHost=jsonObject.get("host").toString();
        mTitle=jsonObject.get("title").toString();
        mURL=jsonObject.get("URL").toString();
      } catch (JSONException e) {
        e.printStackTrace();
      }

      mTitleView.setText(mTitle);
    //  mBadgeView.setBadgeCount(mNumber);
    //  mBadgeView.setTargetView(mNewsView);
    }
    private void initWebView() {
        mWebSettings = mWebview.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        mWebSettings.setJavaScriptEnabled(true);
        //不使用缓存:
        mWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebview.loadUrl(mURL);
        //复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        mWebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
              try {
                URL mUrl = new URL(url);
                String currentHost = mUrl.getHost();
                if(urlHost.equals(currentHost)){
                  new Thread(){
                    public void run(){
                      try {
                        Thread.sleep(3000);
                        setResult(1001);
                        WebViewActivity.this.finish();
                      } catch (InterruptedException e) { }
                    }
                  }.start();     //这种内部匿名类的写法，快速生成一个线程对象，也有利于快速垃圾回收
                }
                Toast.makeText(WebViewActivity.this,"CurrentURL:"+currentHost,Toast.LENGTH_LONG).show();
              } catch (MalformedURLException e) {
                e.printStackTrace();
              }

                return true;
            }
        });
    }

    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            setResult(1001);
            this.finish();
            return true;
        }
        return true;
    }
    //销毁Webview
    @Override
    protected void onDestroy() {
        if (mWebview != null) {
            mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();
            ((ViewGroup) mWebview.getParent()).removeView(mWebview);
            mWebview.destroy();
            mWebview = null;
        }
        super.onDestroy();
    }
}
