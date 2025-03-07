package com.capacitor.safearea;

import android.util.Log;

import android.os.Build;

import com.getcapacitor.Bridge;
import com.getcapacitor.JSObject;

import android.view.DisplayCutout;
import android.view.WindowInsets;

public class SafeArea {
    private Bridge bridge;

    public void setBridge(Bridge bridge) {
        this.bridge = bridge;
    }

    public JSObject getSafeAreaInsets() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.i(SafeAreaPlugin.class.toString(), String.format("Requires at least %d+", Build.VERSION_CODES.M));
            return this.result(0, 0, 0, 0);
        }
        DisplayCutout displayCutout = null;
        WindowInsets windowInsets = this.bridge.getActivity().getWindow().getDecorView().findViewById(android.R.id.content).getRootWindowInsets();
        if (windowInsets == null) {
            Log.i(SafeAreaPlugin.class.toString(), "WindowInsets is not available.");
            return this.result(0, 0, 0, 0);
        }

        int top = windowInsets.getStableInsetTop();
        int left = windowInsets.getStableInsetLeft();
        int right = windowInsets.getStableInsetRight();
        int bottom = windowInsets.getStableInsetBottom();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            displayCutout = windowInsets.getDisplayCutout();
            if(displayCutout != null) {
                top = displayCutout.getSafeInsetTop();
                left = displayCutout.getSafeInsetLeft();
                right = displayCutout.getSafeInsetRight();
                bottom = displayCutout.getSafeInsetBottom();

                if(!this.getStatusBarVisible()) {
                    top = Math.max(windowInsets.getStableInsetTop(), top);
                }
            }

        }
        return this.result(top, left, right, bottom);
    }

    private Boolean getStatusBarVisible() {
        WindowInsets windowInsets = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            windowInsets = this.bridge.getActivity().getWindow().getDecorView().getRootWindowInsets();
        }
        return  windowInsets != null && windowInsets.getSystemWindowInsetTop() == 0;
    }

    public int getStatusBarHeight() {
        int top = 0;
        float density = this.getDensity();
        int resourceId = this.bridge.getActivity().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            top = this.bridge.getActivity().getResources().getDimensionPixelSize(resourceId);
        }
        return Math.round(top / density);
    }

    public int getNavigationBarHeight() {
        int top = 0;
        float density = this.getDensity();
        int resourceId = this.bridge.getActivity().getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            top = this.bridge.getActivity().getResources().getDimensionPixelSize(resourceId);
        }
        return Math.round(top / density);
    }

    public JSObject result(int top, int left, int right, int bottom) {
        JSObject json = new JSObject();
        json.put("top", dpToPixels(top));
        json.put("left", dpToPixels(left));
        json.put("right", dpToPixels(right));
        json.put("bottom", dpToPixels(bottom));
        return json;
    }

    private int dpToPixels(int dp) {
        float density = this.getDensity();
        return (int) Math.round(dp / density);
    }

    private float getDensity() {
        return this.bridge.getActivity().getResources().getDisplayMetrics().density;
    }
}
