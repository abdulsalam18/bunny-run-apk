package com.abdulsalam.startapp;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;

public class StartAppPlugin extends CordovaPlugin {

    private StartAppAd interstitialAd;
    private LinearLayout bannerView;

    private String APP_ID;

    // =========================
    // INIT (from cdv_strings.xml)
    // =========================
    private void initSDK() {
        try {

            Resources res = cordova.getActivity().getResources();
            int id = res.getIdentifier("startapp_app_id", "string",
                    cordova.getActivity().getPackageName());

            APP_ID = res.getString(id);

            StartAppSDK.init(cordova.getActivity(), APP_ID, false);

            interstitialAd = new StartAppAd(cordova.getActivity());
            loadInterstitial();

        } catch (Exception e) {
            Log.e("StartApp", "Init error: " + e.getMessage());
        }
    }

    // =========================
    // EXECUTE
    // =========================
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

        switch (action) {

            case "init":
                initSDK();
                return true;

            case "showBanner":
                showBanner();
                return true;

            case "hideBanner":
                hideBanner();
                return true;

            case "refreshBanner":
                refreshBanner();
                return true;

            case "showInterstitial":
                showInterstitial();
                return true;

            case "showReward":
                showReward(callbackContext);
                return true;

            case "more":
                openMore();
                return true;

            case "share":
                share();
                return true;
        }

        return false;
    }

    // =========================
    // BANNER (simple overlay)
    // =========================
    private void showBanner() {

        cordova.getActivity().runOnUiThread(() -> {

            if (bannerView != null) return;

            bannerView = new LinearLayout(cordova.getActivity());
            bannerView.setBackgroundColor(0xFF000000);
            bannerView.setPadding(15, 15, 15, 15);

            TextView tv = new TextView(cordova.getActivity());
            tv.setText("Banner Ad");
            tv.setTextColor(0xFFFFFFFF);

            bannerView.addView(tv);

            FrameLayout root = cordova.getActivity().findViewById(android.R.id.content);

            FrameLayout.LayoutParams params =
                    new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                    );

            params.gravity = Gravity.BOTTOM;

            root.addView(bannerView, params);
        });
    }

    private void hideBanner() {

        cordova.getActivity().runOnUiThread(() -> {

            if (bannerView != null) {
                FrameLayout root = cordova.getActivity().findViewById(android.R.id.content);
                root.removeView(bannerView);
                bannerView = null;
            }
        });
    }

    private void refreshBanner() {
        hideBanner();
        showBanner();
    }

    // =========================
    // INTERSTITIAL (SDK 5.1.0 SIMPLE)
    // =========================
    private void loadInterstitial() {
        if (interstitialAd != null) {
            interstitialAd.loadAd();
        }
    }

    private void showInterstitial() {

        cordova.getActivity().runOnUiThread(() -> {

            if (interstitialAd != null) {
                interstitialAd.showAd();
                loadInterstitial();   // auto reload
                refreshBanner();      // refresh banner after close
            } else {
                loadInterstitial();
            }
        });
    }

    // =========================
    // REWARD (SAFE SIMPLE VERSION)
    // =========================
    private void showReward(CallbackContext callbackContext) {

        cordova.getActivity().runOnUiThread(() -> {

            StartAppAd rewardAd = new StartAppAd(cordova.getActivity());

            rewardAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO);

            rewardAd.showAd();

            // safe callback (no broken listener usage)
            callbackContext.success("reward_requested");
        });
    }

    // =========================
    // MORE APPS
    // =========================
    private void openMore() {

        String url = "https://www.facebook.com/gamestudio6";

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        cordova.getActivity().startActivity(i);
    }

    // =========================
    // SHARE
    // =========================
    private void share() {

        try {
            Resources res = cordova.getActivity().getResources();
            String pkg = cordova.getActivity().getPackageName();

            int nameId = res.getIdentifier("app_name", "string", pkg);
            int descId = res.getIdentifier("app_desc", "string", pkg);
            int urlId  = res.getIdentifier("app_url", "string", pkg);

            String appName = res.getString(nameId);
            String appDesc = res.getString(descId);
            String appUrl  = res.getString(urlId);

            String text =
                    appName + "\n" +
                    appDesc + "\n" +
                    appUrl;

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, appName);
            intent.putExtra(Intent.EXTRA_TEXT, text);

            cordova.getActivity().startActivity(
                    Intent.createChooser(intent, "Share via")
            );

        } catch (Exception e) {
            Log.e("StartApp", "Share error: " + e.getMessage());
        }
    }
}