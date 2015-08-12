package br.com.furb.tagarela.util;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

public class ImageUtil {
	private static Bitmap bitmap;
	
	public static byte[] getByteFromBase64Image(String img) {
		if(bitmap != null){
			bitmap.recycle();
		}
		bitmap = decodeImageBase64(img);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		return stream.toByteArray();
	}
	
	private static Bitmap decodeImageBase64(String input) {
		byte[] decodedByte = Base64.decode(input, Base64.NO_WRAP);		
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}
	
	public static Bitmap decodeImageByte(byte[] img) {
		return BitmapFactory.decodeByteArray(img, 0, img.length);
	}
}
