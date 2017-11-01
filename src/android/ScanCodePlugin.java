package cordova.plugin.ScanCodePlugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.yle.borcodescan.app.CaptureActivity;

import android.Manifest;
import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import javax.xml.validation.TypeInfoProvider;

/**
 * This class echoes a string called from JavaScript.
 */
public class ScanCodePlugin extends CordovaPlugin {
    private CallbackContext callbackContext;
    private static final int REQUEST_QRCODE = 0x01;
    private static final int PERMISSION_DENIED_ERROR=20;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE=1;
    private int code=1;
    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        if (action.equals("scan")) {
            if(Build.VERSION.SDK_INT >= 24){
                //android 7.0

                //判断权限
                if (ContextCompat.checkSelfPermission(this.cordova.getActivity(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this.cordova.getActivity(),new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSIONS_REQUEST_CODE);
                    code--;
                    if (code<0){
                        code=1;
                        Toast toast=Toast.makeText(cordova.getActivity(),"请在系统设置开启相机权限",Toast.LENGTH_LONG);
                        showtoast(toast,4000);
                    }
                }else{
                    Intent intent = new Intent(cordova.getActivity(), CaptureActivity.class);
                    cordova.startActivityForResult(this, intent, REQUEST_QRCODE);
                }
            }else {
                Intent intent = new Intent(cordova.getActivity(), CaptureActivity.class);
                cordova.startActivityForResult(this, intent, REQUEST_QRCODE);
            }

            return true;
        }
        return false;
    }

    //设置吐司时长
    public void showtoast(final Toast toast,final int cnt){
        final Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        },0,4000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        },cnt);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_QRCODE:
                if (resultCode == Activity.RESULT_OK) {
                    //扫描后的业务逻辑
                    String code = data.getStringExtra("SCAN_RESULT");
                    if (code.contains("http") || code.contains("https")) {
                        //打开链接
                        /*Intent intent = new Intent(this, AdBrowserActivity.class);
                        intent.putExtra(AdBrowserActivity.KEY_URL, code);
                        startActivity(intent);*/
                        // Toast.makeText(cordova.getActivity(), code, Toast.LENGTH_SHORT).show();
                        callbackContext.success(code);

                    } else {
                        // Toast.makeText(cordova.getActivity(), code, Toast.LENGTH_SHORT).show();
                        callbackContext.success(code);
                    }
                }else if(resultCode == 300){
                    //从本地相册扫描后的业务逻辑
                    String code = data.getStringExtra("LOCAL_PHOTO_RESULT");
                    // Toast.makeText(cordova.getActivity(), code, Toast.LENGTH_SHORT).show();
                    callbackContext.success(code);
                }
                break;
        }
    }

}
