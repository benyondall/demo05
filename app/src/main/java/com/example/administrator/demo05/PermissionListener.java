package com.example.administrator.demo05;

import java.util.List;

/**
 * Created by Administrator on 2017/11/16 0016.
 */

public interface PermissionListener {
    void onGranted();

    void onDenied(List<String> deniedPermission);

    void onShouldShowRationale(List<String> deniedPermission);
}
