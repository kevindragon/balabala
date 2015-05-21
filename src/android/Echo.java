/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
*/
package com.lexiscn.balabala;

import java.util.TimeZone;

import android.widget.Toast;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.json.JSONException;
import org.json.JSONObject;

import android.provider.Settings;
import android.os.Bundle;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUnderstander;
import com.iflytek.cloud.SpeechUnderstanderListener;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.UnderstanderResult;

public class Echo extends CordovaPlugin {

    public static String iflytekAPPID = "551384a9";

    SpeechUnderstander myRecognizerDialog;

    CallbackContext jsCallback;

    Toast tst;

    public Echo() {
    }

    private void toast(String text) {
        tst.cancel();
        tst.setText(text);
        tst.show();
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        // your init code here
        tst = Toast.makeText(webView.getContext(), "This is a toast.", Toast.LENGTH_SHORT);

        toast("initialize");
        println("initialize");

        SpeechUtility.createUtility(this.webView.getContext(), SpeechConstant.APPID + "=" + iflytekAPPID);
    }

    private void understand() {
        //1.创建文本语义理解对象
        myRecognizerDialog = SpeechUnderstander.createUnderstander(this.webView.getContext(), null);
        myRecognizerDialog.setParameter(SpeechConstant.DOMAIN, "iat");
        myRecognizerDialog.setParameter(SpeechConstant.RESULT_TYPE, "json");
        myRecognizerDialog.setParameter(SpeechConstant.NLP_VERSION, "2.0");
        myRecognizerDialog.setParameter(SpeechConstant.PARAMS , "sch=1");
        myRecognizerDialog.startUnderstanding(mUnderstanderListener);
    }

    // XmlParser为结果解析类，见SpeechDemo
    private SpeechUnderstanderListener mUnderstanderListener = new SpeechUnderstanderListener() {
        public void onResult(UnderstanderResult result) {
            String text = result.getResultString();

            toast("text: " + text);
            println("text: " + text);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("action", "result");
                jsonObject.put("arg", text);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            jsCallback.success(jsonObject);
        }

        // 会话发生错误回调接口
        public void onError(SpeechError error) {
            toast("onError: " + error.getErrorDescription());
            println("onError: " + error.getErrorDescription());

            jsCallback.error("onError: " + error.getErrorDescription());
        }

        // 开始录音
        public void onBeginOfSpeech() {
            toast("On begin of speech");
            println("On begin of speech");
        }

        // 音量值0~30
        @SuppressWarnings("deprecation")
        public void onVolumeChanged(int volume) {
            println("On volume changed: " + volume);
            Echo.this.webView.sendJavascript("window.plugins.Balabala.volumeChanged("+volume+")");
        }

        // 结束录音
        public void onEndOfSpeech() {
            toast("On end of speech");
            println("On end of speech");
        }

        // 扩展用接口
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

    public boolean execute(String action, String arg, CallbackContext callbackContext) throws JSONException {
        jsCallback = callbackContext;
        if (action.equals("toast")) {
            this.toast(arg);
            callbackContext.success("called success");
        } else if (action.equals("understand")) {
            understand();
        } else {
            return false;
        }
        return true;
    }

    private void println(String str) {
        System.out.println(str);
    }
}
