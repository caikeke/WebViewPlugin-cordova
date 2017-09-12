package com.webview.yhck;


import android.content.Intent;

import com.picker.yhck.ShowActivity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

public class WebViewPlugin extends CordovaPlugin{
  private static final int MSG_REQUEST_CODE = 1000;
  private CallbackContext mCallbackContext;
  private String getParam;
  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    this.getParam="";
    this.mCallbackContext = callbackContext;
    if (!"".equals(action)||action!=null) {
      getParam=args.getJSONObject(0).toString();
      openActivity(action);
      return true;
    }
    mCallbackContext.error("error");
    return false;
  }

  private void openActivity(String action) {
    Intent intent = new Intent(cordova.getActivity(),WebViewActivity.class);
    intent.putExtra("Param",getParam);
    intent.putExtra("Action",action);
    cordova.startActivityForResult(this, intent, MSG_REQUEST_CODE);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    if (requestCode == MSG_REQUEST_CODE && resultCode == 1001) {
      mCallbackContext.success("openNews");
    }
  }
}
