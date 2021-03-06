package com.appkernel.io;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;


/**
 * @author Vadim Voituk

 */
public class IOUtils {
	
	public static interface LoadingProgressListener {
	    public void onProgress(int bytes);
	    public void onFinish();
		boolean isCanceled();
	};
	
	private static final int BYTE_BUFFER_SIZE = 64 * 1024;

	public static void copyChannel(final ReadableByteChannel src, final WritableByteChannel dest, final LoadingProgressListener listener) throws IOException {
		final ByteBuffer buffer = ByteBuffer.allocateDirect(BYTE_BUFFER_SIZE);
		int bytes;
		int total = 0;
		
		while ( (bytes = src.read(buffer)) != -1) {
			// prepare the buffer to be drained
			buffer.flip();
			// write to the channel, may block
			dest.write(buffer);
			// If partial transfer, shift remainder down
			// If buffer is empty, same as doing clear()
			buffer.compact();
			total += bytes;
			if (listener != null) {
				listener.onProgress(total);
				if (listener.isCanceled())
					return;
			}
		}
		// EOF will leave buffer in fill state
		buffer.flip();
		// make sure the buffer is fully drained.
		while (buffer.hasRemaining()) {
			dest.write(buffer);
		}
		
		if (listener!=null)
			listener.onFinish();
	}
	
	
	public static void toFile(InputStream inStream, File outFile) throws IOException {
		WritableByteChannel out = Channels.newChannel(new FileOutputStream(outFile));
		ReadableByteChannel in = Channels.newChannel(inStream);
		IOUtils.copyChannel(in, out, null);
	}

    public static String toString(InputStream stream) throws IOException {
        int n;
        char[] buffer = new char[1024 * 64];
        final InputStreamReader reader = new InputStreamReader(stream, "UTF-8");

        final StringWriter writer = new StringWriter();
        while (-1 != (n = reader.read(buffer))) {
            writer.write(buffer, 0, n);
        }

        return writer.toString();
    }
	
	
	/**
	 * Close quitely any closable resource
	 * @param stream
	 */
	public static void close(Closeable stream) {
		try { if(stream!=null) stream.close(); } catch (IOException e) {}
	}
	
	/***
	 * Close quitely socket
	 * @param socket
	 */	
	public static void close(Socket socket) {
		try { if(socket!=null) socket.close(); } catch (IOException e) {}
	}
	
	/***
	 * Close quitely channels
	 * @param in
	 */	
	public static void close(Channel in, Channel out) {
		try { if( in  != null ) in.close(); } catch (IOException e) {}
		try { if( out != null ) out.close(); } catch (IOException e) {}
	}
		
	public static void close(SQLiteDatabase db) {
		if(db != null) 
			db.close();
	}

	public static void close(Cursor c) {
		if (c != null && !c.isClosed())
			c.close();
	}
	
	public static void close(Cursor c, SQLiteDatabase db) {
		close(c);
		close(db);
	}

}
