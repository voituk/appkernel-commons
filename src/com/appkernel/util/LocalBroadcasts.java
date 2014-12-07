package com.appkernel.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/** 
 * Helper class for local broadcasts management
 *  
 * @author vadim
 *
 * @todo: Implement InterFilter Cache
 */
public class LocalBroadcasts {
    
    
    public static void send(final Context context, final Intent intent) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    
    public static void send(final Context context, final String action) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(action));
    }
    
    public static void register(final Context context, BroadcastReceiver receiver, IntentFilter filter) {
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
    }
    
    
    public static void unregister(final Context context, final BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

}
