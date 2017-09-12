# WebViewPlugin-cordova
```
ionic 项目，自定义WebView Cordova插件
使用方法：
declare let cordova:any;
1.简单显示
 let dataJson={
   title:"常见问题", 
   badgeNum:"10", 
   URL:"https://www.baidu.com/"
}
cordova.plugins.WebViewPlugin.showWebView(dataJson,(msg)=>{
  if("openNews"==msg){
  
  }
},null);
```
```
2.webView中原声与JS交互
let dataJson={
        title:"常见问题",
        badgeNum:"10",
        URL:"http://kevindianmt.oschina.io/webviewtesthtml/index.html",
        host:"10.0.68.202/ods/zmxy/zhima_authInfo_req/",
        funcAddParam:"callJS('在java中调用js代码_kevin')"
      };
  cordova.plugins.WebViewPlugin.webViewAddParam(dataJson,(msg)=>{
  },null);
```
