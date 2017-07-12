# WebViewPlugin-cordova
ionic 项目，自定义WebView Cordova插件
使用方法：
declare let cordova:

any; let dataJson={
   title:"常见问题", 
   badgeNum:"10", 
   URL:"https://www.baidu.com/"
}
cordova.plugins.WebViewPlugin.showWebView(dataJson,(msg)=>{
  if("openNews"==msg){
  
  }
},null);

