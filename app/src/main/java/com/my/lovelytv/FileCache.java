package com.my.lovelytv;

import java.io.File;

import android.content.Context;

public class FileCache {
	
	private File cacheDir;
	private File cacheDir2;
	private File cacheDir3;
	 
    public FileCache(Context context) {
        // Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            cacheDir2 = new File(
                    android.os.Environment.getExternalStorageDirectory(),
                    "oiichat/bigimg");
        else
            cacheDir2 = context.getCacheDir();
        if (!cacheDir2.exists())
            cacheDir2.mkdirs();
        
        
        
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            cacheDir3 = new File(
                    android.os.Environment.getExternalStorageDirectory(),
                    "oiichat/gallery");
        else
            cacheDir3 = context.getCacheDir();
        if (!cacheDir3.exists())
            cacheDir3.mkdirs();
        
        
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(
                    android.os.Environment.getExternalStorageDirectory(),
                    "oiichat/smallimg");
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }
 
    public File getFile(String url) {
        String filename = String.valueOf(url.hashCode());
        // String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
        return f;
 
    }
 
    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            f.delete();
    }
 
}