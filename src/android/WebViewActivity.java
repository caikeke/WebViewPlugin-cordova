package com.webview.yhck;

import android.app.Activity;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
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
  //成功页面的host
  private String urlHost;
  //显示页面URL
  private String mURL;
  //JS方法和参数
  private String mFuncAddParams;
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

    //返回按纽，关闭WebVIew
    mBackView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setResult(1001);
        WebViewActivity.this.finish();
      }
    });

    //打开消息列表页面（暂时未开放）
    mNewsView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setResult(1001);
        WebViewActivity.this.finish();
      }
    });
  }

  private void initView() {
    String paramJSON = getIntent().getStringExtra("Param");
    try {
      JSONObject jsonObject = new JSONObject(paramJSON);
      mNumber = Integer.valueOf(jsonObject.get("badgeNum").toString());
      urlHost = jsonObject.get("host").toString();
      mTitle = jsonObject.get("title").toString();
      mURL = jsonObject.get("URL").toString();
      //2017.09.12:添加参数：android 调用JS方法

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
    // android 5.0以上默认不支持Mixed Content
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      mWebview.getSettings().setMixedContentMode(
        WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
    }
    //复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
    mWebview.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        try {
          URL mUrl = new URL(url);
          String currentHref = mUrl.getHost() + "/ods/zmxy/zhima_authInfo_req/";
          if (urlHost.equals(currentHref)) {
            new Thread() {
              public void run() {
                try {
                  Thread.sleep(3000);
                  setResult(1001);
                  WebViewActivity.this.finish();
                } catch (InterruptedException e) {
                }
              }
            }.start();     //这种内部匿名类的写法，快速生成一个线程对象，也有利于快速垃圾回收
          }
        } catch (MalformedURLException e) {
          e.printStackTrace();
        }

        return true;
      }

      @Override
      public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();// 接受所有网站的证书
      }
    });
  }

  //点击返回退出浏览器
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      setResult(1001);
      this.finish();
      return false;
    }
    return super.onKeyDown(keyCode, event);
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
