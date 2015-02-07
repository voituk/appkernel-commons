package com.appkernel.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

/** 
 * Helper class for local broadcasts management
 *  
 * @author Vadym Voitiuk <vadim@voituk.com>
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
    
    public static void register(@Nullable final Context context, BroadcastReceiver receiver, IntentFilter filter) {
        if (context == null)
            return;
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
    }

    public static void register(@Nullable final Context context, BroadcastReceiver receiver, String action) {
        if (context == null)
            return;
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter(action)); //TODO: use IntentFilter cache here
    }

    public static void unregister(@Nullable final Context context, final BroadcastReceiver... receivers) {
        if (context == null)
            return;
        final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        for(BroadcastReceiver receiver: receivers) {
            if (receiver != null)
                manager.unregisterReceiver(receiver);
        }
    }

}
