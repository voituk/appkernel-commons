package com.appkernel.io;

import android.util.Log;

import com.appkernel.commons.BuildConfig;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;


public abstract class Cache {

	/**
	 * @param key
	 * @param obj
	 * @return
	 */
	abstract public <T> boolean set(CharSequence key, T obj);	

	/**
	 * @param key
	 * @param clazz
	 * @param lifeTime - cache life time, <pre>0 or -1</pre> do not check age of cache
	 * @return null if no item cached
	 */
	abstract public <T> T get(CharSequence key, Class<T> clazz, long lifeTime);
	

	/**
	 * Clear cache in current thread
	 * Warning: It's long running IO-blocking operation
	 */
	abstract public void clear();
	

	/**
	 * Warning: It's long running IO-blocking operation
	 * @return
	 */
	abstract public long getCacheSize();

	
	private static final String	TAG	= Cache.class.getName();
	
	protected ExecutorService mExecutor;
	
	
	public Cache() {
		mExecutor = Executors.newSingleThreadExecutor( new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				final Thread t = new Thread(r);
				t.setPriority(Thread.MIN_PRIORITY);
				t.setName(Cache.class.getName());
				return t;
			}
		} );
	}

	
	
	/**
	 * Clear cache in IO background thread 
	 */
	public void clearAsync() {
		mExecutor.submit(new Runnable() {
			@Override
			public void run() {
				clear();
			}
		});
	}
	

	/**
	 * 
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T get(CharSequence key, Class<T> clazz) {
		return get(key, clazz, -1);
	}
	

	/**
	 * @param key
	 * @param obj
	 * @return
	 */
    public <T> Future<Boolean> setAsync(final CharSequence key, final T obj) {
		return mExecutor.submit(new Callable<Boolean>() {
			@Override
            public Boolean call() throws Exception {
				final long start = System.currentTimeMillis();
	            final boolean result = set(key, obj);
	            log(String.format("SET %s, time=%dms", key, System.currentTimeMillis() - start));
	            return result;
            }
		});
    }
    
    /**
     * Store multiple
     * @param keys
     * @param obj
     */
    public <T> Future<Boolean> setAsync(final Map<CharSequence, T> map) {
    	return mExecutor.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				final long start = System.currentTimeMillis();
				boolean result = true;
				for (CharSequence key : map.keySet()) {
					result &= set(key, map.get(key));
				}
				log(String.format("SET (%d keys), time=%dms", map.size(), System.currentTimeMillis() - start));
				return result;
			}
    	});
    }
    
    
	protected void log(CharSequence message) {
		if (BuildConfig.DEBUG)
			Log.d(TAG, message.toString());
	}

}