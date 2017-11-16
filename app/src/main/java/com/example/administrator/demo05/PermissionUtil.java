package com.example.administrator.demo05;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by Administrator on 2017/11/16 0016.
 */

public class PermissionUtil {
    private static final String TAG = "PermissionsUtil";

    private PermissionFragment fragment;

    public PermissionUtil(@NonNull FragmentActivity activity) {
        fragment = getPermissionsFragment(activity);
    }

    private PermissionFragment getPermissionsFragment(FragmentActivity activity) {
        PermissionFragment fragment = (PermissionFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
        boolean isNewInstance = fragment == null;
        if (isNewInstance) {
            fragment = new PermissionFragment();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(fragment, TAG)
                    .commit();
            fragmentManager.executePendingTransactions();
        }

        return fragment;
    }

    /**
     * 外部调用申请权限
     * @param permissions 申请的权限
     * @param listener 监听权限接口
     */
    public void requestPermissions(String[] permissions, PermissionListener listener) {
        fragment.setListener(listener);
        fragment.requestPermissions(permissions);

    }
}
