// Copyright 2018-2020 Twitter, Inc.
// Licensed under the MoPub SDK License Agreement
// http://www.mopub.com/legal/sdk-license-agreement/

package com.mopub.mobileads;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.mopub.common.Constants;
import com.mopub.common.CreativeOrientation;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Utils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.mopub.common.DataKeys.BROADCAST_IDENTIFIER_KEY;
import static com.mopub.common.DataKeys.CREATIVE_ORIENTATION_KEY;
import static com.mopub.common.logging.MoPubLog.SdkLogEvent.CUSTOM;
import static com.mopub.mobileads.VastVideoViewController.VAST_VIDEO_CONFIG;

public class BaseVideoPlayerActivity extends Activity {
    public static final String VIDEO_CLASS_EXTRAS_KEY = "video_view_class_name";
    public static final String VIDEO_URL = "video_url";

    public static void startMraid(final Context context, final String videoUrl) {
        final Intent intentVideoPlayerActivity = createIntentMraid(context, videoUrl);
        try {
            context.startActivity(intentVideoPlayerActivity);
        } catch (ActivityNotFoundException e) {
            MoPubLog.log(CUSTOM, "Activity MraidVideoPlayerActivity not found. Did you declare it in your AndroidManifest.xml?");
        }
    }

    static Intent createIntentMraid(final Context context,
            final String videoUrl) {
        final Intent intentVideoPlayerActivity = new Intent(context, MraidVideoPlayerActivity.class);
        intentVideoPlayerActivity.setFlags(FLAG_ACTIVITY_NEW_TASK);
        intentVideoPlayerActivity.putExtra(VIDEO_CLASS_EXTRAS_KEY, "mraid");
        intentVideoPlayerActivity.putExtra(VIDEO_URL, videoUrl);
        return intentVideoPlayerActivity;
    }

    static void startVast(final Context context,
            final VastVideoConfig vastVideoConfig,
            final long broadcastIdentifier,
            @Nullable final CreativeOrientation orientation) {
        final Intent intentVideoPlayerActivity = createIntentVast(context, vastVideoConfig,
                broadcastIdentifier, orientation);
        try {
            context.startActivity(intentVideoPlayerActivity);
        } catch (ActivityNotFoundException e) {
            MoPubLog.log(CUSTOM, "Attempt to start with VastVideoConfig failed. " +
                    "Activity MraidVideoPlayerActivity not found. " +
                    "Did you declare it in your AndroidManifest.xml?");
        }
    }

    static void startVast(final Context context,
                          final VastVideoConfigTwo vastVideoConfig,
                          final long broadcastIdentifier,
                          @Nullable final CreativeOrientation orientation) {
        final Intent intentVideoPlayerActivity = createIntentVast(context, vastVideoConfig,
                broadcastIdentifier, orientation);
        try {
            context.startActivity(intentVideoPlayerActivity);
        } catch (ActivityNotFoundException e) {
            MoPubLog.log(CUSTOM, "Attempt to start with VastVideoConfigTwo failed. " +
                    "Activity MraidVideoPlayerActivity not found. " +
                    "Did you declare it in your AndroidManifest.xml?");
        }
    }

    static Intent createIntentVast(final Context context,
            final VastVideoConfig vastVideoConfig,
            final long broadcastIdentifier,
            @Nullable final CreativeOrientation orientation) {
        final Intent intentVideoPlayerActivity = new Intent(context, MraidVideoPlayerActivity.class);
        intentVideoPlayerActivity.setFlags(FLAG_ACTIVITY_NEW_TASK);
        intentVideoPlayerActivity.putExtra(VIDEO_CLASS_EXTRAS_KEY, "vast");
        intentVideoPlayerActivity.putExtra(VAST_VIDEO_CONFIG, vastVideoConfig);
        intentVideoPlayerActivity.putExtra(BROADCAST_IDENTIFIER_KEY, broadcastIdentifier);
        intentVideoPlayerActivity.putExtra(CREATIVE_ORIENTATION_KEY, orientation);
        return intentVideoPlayerActivity;
    }

    static Intent createIntentVast(final Context context,
           final VastVideoConfigTwo vastVideoConfig,
           final long broadcastIdentifier,
           @Nullable final CreativeOrientation orientation) {
        final Intent intentVideoPlayerActivity = new Intent(context, MraidVideoPlayerActivity.class);
        intentVideoPlayerActivity.setFlags(FLAG_ACTIVITY_NEW_TASK);
        intentVideoPlayerActivity.putExtra(VIDEO_CLASS_EXTRAS_KEY, "vast_new");
        intentVideoPlayerActivity.putExtra(VAST_VIDEO_CONFIG, vastVideoConfig);
        intentVideoPlayerActivity.putExtra(BROADCAST_IDENTIFIER_KEY, broadcastIdentifier);
        intentVideoPlayerActivity.putExtra(CREATIVE_ORIENTATION_KEY, orientation);
        return intentVideoPlayerActivity;
    }

    public static void startNativeVideo(final Context context, final long nativeVideoId, final VastVideoConfig vastVideoConfig) {
        final Intent intentVideoPlayerActivity = createIntentNativeVideo(context, nativeVideoId, vastVideoConfig);
        try {
            context.startActivity(intentVideoPlayerActivity);
        } catch (ActivityNotFoundException e) {
            MoPubLog.log(CUSTOM, "Activity MraidVideoPlayerActivity not found. Did you declare it in your AndroidManifest.xml?");
        }
    }

    public static Intent createIntentNativeVideo(final Context context, final long nativeVideoId, final VastVideoConfig vastVideoConfig) {
        final Intent intentVideoPlayerActivity = new Intent(context, MraidVideoPlayerActivity.class);
        intentVideoPlayerActivity.setFlags(FLAG_ACTIVITY_NEW_TASK);
        intentVideoPlayerActivity.putExtra(VIDEO_CLASS_EXTRAS_KEY, "native");
        intentVideoPlayerActivity.putExtra(Constants.NATIVE_VIDEO_ID, nativeVideoId);
        intentVideoPlayerActivity.putExtra(Constants.NATIVE_VAST_VIDEO_CONFIG, vastVideoConfig);
        return intentVideoPlayerActivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.hideNavigationBar(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // VideoViews may never release audio focus, leaking the activity. See
        // https://code.google.com/p/android/issues/detail?id=152173.
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (am != null) {
            am.abandonAudioFocus(null);
        }
    }
}

