package com.lexa.keydriver;

import android.view.KeyEvent;
import android.content.Intent;
import java.lang.reflect.Field;
import java.util.*;
import org.apache.cordova.*;
import org.json.*;


/**
 * Broadcasts key events for native applications.
 */
public class KeyDriver extends CordovaPlugin {
    // map of all known keys
    private Map<String, Integer> keycodes = new HashMap<String, Integer>();
    
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        try {
            for(Field field : KeyEvent.class.getDeclaredFields()) {
                if(field.getName().startsWith("KEYCODE_")) {
                    try {
                        keycodes.put(field.getName(), field.getInt(null));
                    } catch(IllegalAccessException e) {
                        // ignore static key code (all key codes are public).
                    }
                }
            }
        } catch(SecurityException ex) {
            // mapping will be empty, JS code can check it.
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if(action.equals("broadcastKey")) {
            if(args.length() != 1) {
                callbackContext.error("Keycode name expected as first argument.");
            } else {
                String keycode = args.getString(0);
                if(keycode == null) {
                    callbackContext.error("First argument has to be string specifying key code.");
                } else if(this.broadcastKey(keycode)) callbackContext.success();
                       else callbackContext.error("Unknown key code name: " + keycode);
            }
            return true;
        }
        return false;
    }

    /** Broadcast given key. */
    private boolean broadcastKey(String keycode) {
        if(!keycodes.containsKey(keycode))
            return false;

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_BUTTON);
        KeyEvent key = new KeyEvent(KeyEvent.ACTION_DOWN, keycodes.get(keycode).intValue());
        intent.putExtra(Intent.EXTRA_KEY_EVENT, key);
        ((CordovaActivity)this.cordova.getActivity()).sendBroadcast(intent);
        return true;
    }
}
