package com.appkernel.io;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileCache extends Cache {


	private final File		mCacheDir;
	private final String	mKeySuffix;

	

	public FileCache(@NonNull File cacheDir, @Nullable String keySuffix) {
		mCacheDir = cacheDir;
		mKeySuffix = keySuffix;
	}


	/**
	 * {@inheritDoc}
	 * 
	 * @see com.appkernel.io.Cache#set(java.lang.CharSequence, T)
	 */
	@Override
	public <T> boolean set(CharSequence key, T obj) {
		final File cacheFile = file(key);
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(cacheFile));
			oos.writeObject(obj);
			oos.close();
			cacheFile.setLastModified(System.currentTimeMillis());
			return true;
		} catch (IOException e) {
			log(Log.getStackTraceString(e));
			return false;
		}
	}



	/**
	 * {@inheritDoc}
	 * 
	 * @see com.appkernel.io.Cache#get(java.lang.CharSequence, java.lang.Class, long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(CharSequence key, Class<T> clazz, long lifeTime) {
		File cacheFile = file(key);
		if (!cacheFile.exists() || !cacheFile.isFile())
			return null;

		long start = System.currentTimeMillis();
		
		T result = null;
		try {
			if (lifeTime > 0) {
				final long cacheAge = System.currentTimeMillis() - cacheFile.lastModified();
				if (cacheAge > lifeTime)
					return null;
			}

			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(cacheFile));
			result = (T) ois.readObject();
			ois.close();

			if (!clazz.isInstance(result))
				return null;
			
			log(String.format("Loaded data from cache: %s, lifeTime: %d, time=%dms", cacheFile.getPath(), lifeTime, System.currentTimeMillis()-start ));
			return result;
		} catch (IOException e) {
			log(Log.getStackTraceString(e));
		} catch (ClassCastException e) {
			log(Log.getStackTraceString(e));
		} catch (ClassNotFoundException e) {
			log(Log.getStackTraceString(e));
		}
		return null;

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.appkernel.io.Cache#clear()
	 */
	@Override
	public void clear() {
		File[] list = mCacheDir.listFiles();
		if (list == null)
			return;
		for (int i = 0; i < list.length; i++)
			if (list[i].getPath().endsWith(".fcache"))
				list[i].delete();
	}
	

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.appkernel.io.Cache#getCacheSize()
	 */
	@Override
	public long getCacheSize() {
		File[] list = mCacheDir.listFiles();
		if (list == null)
			return 0l;

		long size = 0;
		for (int i = 0; i < list.length; i++)
			size += list[i].length();

		return size;
	}



	protected File file(CharSequence key) {
		return new File(mCacheDir, key + (mKeySuffix == null || mKeySuffix.length() == 0 ? "" : "."+mKeySuffix)  +  ".fcache");
	}


	
}
