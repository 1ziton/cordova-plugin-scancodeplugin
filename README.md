# scanCodePlugin

[![](https://img.shields.io/npm/v/cordova-plugin-scancodeplugin.svg?style=flat-square)](https://www.npmjs.com/package/cordova-plugin-scancodeplugin)

ionic3 二维码扫描插件,安卓及iOS平台皆可用.

clone from https://github.com/YQianIOS/scanCodePlugin


## Usage

ionic cordova plugin add cordova-plugin-scancodeplugin

```ts

     cordova.plugins.ScanCodePlugin.scan("扫描二维码", function (msg) {
            if (!msg) {
                return
            }
            if (that.homeModel.mobile === msg) {
                that.alert.showAlert('不能添加自己为好友');
                return;
            }

            that.searchWorker(msg);
        }, function (msg) {
            that.alert.showAlert("扫描二维码失败");
        });


```