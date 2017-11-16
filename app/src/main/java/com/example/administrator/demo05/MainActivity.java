package com.example.administrator.demo05;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;//权限请求码
    Button btnCloc;
    @BindView(R.id.tv_result)
    TextView tvResult;

    private final static int REQUEST_CODE = 0;
    private final static int RC_CAMERA = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        btnCloc = (Button) findViewById(R.id.btn_cloc);
        btnCloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=23){
                    checkPer();
                }else{
                    //创建PermissionUtil对象，参数为继承自V4包的 FragmentActivity
                    PermissionUtil permissionUtil = new PermissionUtil(MainActivity.this);
                    //调用requestPermissions
                    permissionUtil.requestPermissions(new String[]{Manifest.permission.CAMERA},
                            new PermissionListener() {
                                @Override
                                public void onGranted() {
                                    //所有权限都已经授权
                                    Toast.makeText(MainActivity.this, "所有权限都已授权", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onDenied(List<String> deniedPermission) {
                                    //Toast第一个被拒绝的权限
                                    Toast.makeText(MainActivity.this, "拒绝了权限" + deniedPermission.get(0), Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onShouldShowRationale(List<String> deniedPermission) {
                                    //Toast第一个勾选不在提示的权限
                                    Toast.makeText(MainActivity.this, "这个权限" + deniedPermission.get(0)+"勾选了不在提示，要像用户解释为什么需要这权限", Toast.LENGTH_LONG).show();
                                }
                            });
                    //
                }

            }
        });
    }



    private void checkPer() {

            if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            } else {
                EasyPermissions.requestPermissions(this, "为了您能够正常使用扫一扫功能，juxin商城需要获得相机权限",
                        RC_CAMERA, Manifest.permission.CAMERA);
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        // 请求权限已经被授权
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // 请求权限被拒绝
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).
                    setRationale("没有该权限，此应用程序可能无法正常工作。打开应用设置屏幕以修改应用权限").build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    // UiUtil.toast("解析结果" + result);
                    if (result != null) {
                        //一 京东二维码  https://item.m.jd.com/product/5546942.html?pc_source=pc_productDetail_5546942
                        if ((result.startsWith("https://item.m.jd.com/product")
                                || result.startsWith("http://item.m.jd.com/product")
                                || result.startsWith("item.m.jd.com/product"))) {
                            String code = result.substring(result.indexOf("product/") + 8, result.indexOf(".html"));
                            tvResult.setText(code);
                        }

                        }
                    }
                }
            }

        }

}
