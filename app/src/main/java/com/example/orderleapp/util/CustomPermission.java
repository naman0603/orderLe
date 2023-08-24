package com.example.orderleapp.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

public class CustomPermission {
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1500;
    private Activity activity;

    public CustomPermission(Activity activity) {
        this.activity = activity;
    }

    private boolean multiPermission(boolean isForCamera, boolean isForVideo, boolean isForJobStatus) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            final List<String> permissionsList = new ArrayList<String>();
            if (isForJobStatus) {
                if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
                    permissionsNeeded.add("Access Location");
            }
            if (isForCamera) {
                if (!addPermission(permissionsList, Manifest.permission.CAMERA))
                    permissionsNeeded.add("Camera");
                if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    permissionsNeeded.add("Write External Storage");
            }
            if (isForVideo) {
                if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
                    permissionsNeeded.add("Record Audio");
                if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    permissionsNeeded.add("Write External Storage");
            }

            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + ", " + permissionsNeeded.get(i);

                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= 23) {
                                        activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                    }
                                }
                            });
                    return false;
                }
                activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                if (!activity.shouldShowRequestPermissionRationale(permission))
                    return false;
            }
            return true;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public boolean checkPermission(boolean isForCamera, boolean isForVideo, boolean isForJobStatus) {
        if (Build.VERSION.SDK_INT >= 23) {
            return multiPermission(isForCamera, isForVideo, isForJobStatus);
        } else {
            return true;
        }
    }
}