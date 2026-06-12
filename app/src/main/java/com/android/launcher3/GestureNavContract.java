/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.launcher3;

import static android.content.Intent.EXTRA_COMPONENT_NAME;
import static android.content.Intent.EXTRA_USER;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;

import androidx.annotation.NonNull;

/**
 * Class to encapsulate the handshake protocol between Launcher and gestureNav.
 */
public class GestureNavContract {
    private static final String TAG = "GestureNavContract";
    public static final String EXTRA_GESTURE_CONTRACT = "gesture_nav_contract_v1";
    public static final String EXTRA_ICON_POSITION = "gesture_nav_contract_icon_position";
    public static final String EXTRA_ICON_SURFACE = "gesture_nav_contract_surface_control";
    public static final String EXTRA_REMOTE_CALLBACK = "android.intent.extra.REMOTE_CALLBACK";
    public static final String EXTRA_ON_FINISH_CALLBACK = "gesture_nav_contract_finish_callback";

    public final ComponentName componentName;
    public final UserHandle userHandle;

    private final Message mCallback;

    public GestureNavContract(ComponentName componentName, UserHandle userHandle, Message callback) {
        this.componentName = componentName;
        this.userHandle = userHandle;
        this.mCallback = callback;
    }

    public void sendEndPosition(RectF position) {
        Bundle result = new Bundle();
        result.putParcelable(EXTRA_ICON_POSITION, position);
        result.putParcelable(EXTRA_ICON_SURFACE, null);

        if (sMessageReceiver == null) {
            sMessageReceiver = new StaticMessageReceiver();
        }
        result.putParcelable(EXTRA_ON_FINISH_CALLBACK, sMessageReceiver.setCurrentContext());

        Message callback = Message.obtain();
        callback.copyFrom(mCallback);
        callback.setData(result);

        try {
            callback.replyTo.send(callback);
        } catch (RemoteException e) {
            Log.e(TAG, "Error sending icon position", e);
        }
    }

    public static GestureNavContract fromIntent(Intent intent) {
        Bundle extras = intent.getBundleExtra(EXTRA_GESTURE_CONTRACT);

        if (extras == null) {
            return null;
        }

        intent.removeExtra(EXTRA_GESTURE_CONTRACT);

        ComponentName componentName = extras.getParcelable(EXTRA_COMPONENT_NAME, ComponentName.class);
        UserHandle userHandle = extras.getParcelable(EXTRA_USER, UserHandle.class);
        Message callback = extras.getParcelable(EXTRA_REMOTE_CALLBACK, Message.class);

        if (componentName != null && userHandle != null && callback != null && callback.replyTo != null) {
            return new GestureNavContract(componentName, userHandle, callback);
        }

        return null;
    }

    private static StaticMessageReceiver sMessageReceiver = null;
    private static class StaticMessageReceiver implements Handler.Callback {
        private static final int MSG_CLOSE_LAST_TARGET = 0;

        private final Messenger mMessenger = new Messenger(new Handler(Looper.getMainLooper(), this));

        public Message setCurrentContext() {
            Message msg = Message.obtain();
            msg.replyTo = mMessenger;
            msg.what = MSG_CLOSE_LAST_TARGET;

            return msg;
        }

        @Override
        public boolean handleMessage(@NonNull Message message) {
            return message.what == MSG_CLOSE_LAST_TARGET;
        }
    }
}