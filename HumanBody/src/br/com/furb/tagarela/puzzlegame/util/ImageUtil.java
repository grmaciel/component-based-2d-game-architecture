package br.com.furb.tagarela.puzzlegame.util;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class ImageUtil {
	public static byte[] getByteArrayFromBitmap(Bitmap bmp) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		return stream.toByteArray();
	}
	
	public static Bitmap getBitmapFromByte(byte[] array) {
		return BitmapFactory.decodeByteArray(array, 0,
				array.length);
	}
	
	public static Bitmap getBitmapFromRes(Context context, int resId) {
		return BitmapFactory.decodeResource(context.getResources(), resId);
	}
	
	public static Bitmap getBorderOfImage(Bitmap bitmap) {
		Bitmap mutable = bitmap.copy(Config.ARGB_4444, true);
		for (int i = 0; i < bitmap.getWidth(); i++) { 
			for (int j = 0; j < bitmap.getHeight(); j++) {
				// se o pixel anterior fo transparante
				// ou o pixel de cima for transparente e eu não estiver entre as ultimas / primeiras
				// fileiras ou o próximo for, então gg n00b
				
				int xAnt = i-1;
				int yAnt = j-1;
				int xProx = i+1;
				int yProx = j+1;
				
				boolean borda = false;
				
				if (xAnt < 0 || yAnt < 0 || xProx >= bitmap.getWidth() || yProx >= bitmap.getHeight()) {
					if (bitmap.getPixel(i, j) != Color.TRANSPARENT) {
						borda = true;
//						mutable.setPixel(i, j, Color.BLACK);	
					}
					// borda
				} else if (bitmap.getPixel(xAnt, j) == Color.TRANSPARENT) {
					// borda
//					mutable.setPixel(i, j, Color.BLACK);
					borda = true;
				} else if (bitmap.getPixel(i, yAnt) == Color.TRANSPARENT) {
					// borda
//					mutable.setPixel(i, j, Color.BLACK);
					borda = true;
				} else if (bitmap.getPixel(xProx, j) == Color.TRANSPARENT) {
//					mutable.setPixel(i, j, Color.BLACK);
					borda = true;
				} else if (bitmap.getPixel(i, yProx) == Color.TRANSPARENT) {
//					mutable.setPixel(i, j, Color.BLACK);
					borda = true;
				} else {
					mutable.setPixel(i, j, Color.TRANSPARENT);
				}
				
				if (borda && bitmap.getPixel(i, j) != Color.TRANSPARENT) {
					mutable.setPixel(i, j, Color.BLACK);
					
					// tentativa de dilatação, mas tem que ser altarada
					// analisar pra pintar somente novos e não em relação a novos e velhos
					// seguir o artigo favoritado :)
					/*
					int j1 = j+1;
					int i1 = i+1;
					
					if (i > 0) {
						mutable.setPixel(i-1, j, Color.BLACK);
						
						if (j > 0) {
							mutable.setPixel(i-1, j-1, Color.BLACK);
						}
						
						if (j1 < bitmap.getHeight()) {
							mutable.setPixel(i-1, j+1, Color.BLACK);
						}
					}
					
					if (j > 0) {
						mutable.setPixel(i, j-1, Color.BLACK);
						
						if (i1 < bitmap.getWidth()) {
							mutable.setPixel(i+1, j-1, Color.BLACK);
						}
					}
					
					
					
					if (i1 < bitmap.getWidth()) {
						mutable.setPixel(i1, j, Color.BLACK);
						Log.d("DILATE", "i1: " + i1 + " j" + j);
						
						if (j1 < bitmap.getHeight()) {
							mutable.setPixel(i+1, j+1, Color.BLACK);
						}
					}
					
					if ((j+1) < bitmap.getHeight()) {
						mutable.setPixel(i, j+1, Color.BLACK);
					}
					*/
				}
			}
		}
		
		// vamos tentar dilatar, muitos for nessa porra
//		int[][] alteracoes = new int[bitmap.getWidth()][bitmap.getHeight()];
		for (int i = 0; i < mutable.getWidth(); i++) { 
			for (int j = 0; j < mutable.getHeight(); j++) {
				// se o pixel anterior fo transparante
				// ou o pixel de cima for transparente e eu não estiver entre as ultimas / primeiras
				// fileiras ou o próximo for, então gg n00b
				
				int xAnt = i-1;
				int yAnt = j-1;
				int xProx = i+1;
				int yProx = j+1;
				
				if (mutable.getPixel(i, j) == Color.BLACK) {
						//alteracoes[i][j] = 1;
					
//					Log.d("DILATE", "i: " + i + " j:" + j + " W: " + mutable.getWidth() + " H: " + mutable.getHeight());
					if (i > 0) {
						mutable.setPixel(xAnt, j, Color.GREEN);
					} 
					
					if (j > 0) {
						mutable.setPixel(i, yAnt, Color.GREEN);
					}
					
					if (xProx < mutable.getWidth()) {
						mutable.setPixel(xProx, j, Color.GREEN);
					}
					
					if (yProx < mutable.getHeight()) {
						mutable.setPixel(i, yProx, Color.GREEN);
					}
					
					if (i > 0 && j > 0) {
						mutable.setPixel(xAnt, yAnt, Color.GREEN);
					} 
					
					if (xProx < mutable.getWidth() && yProx < mutable.getHeight()) {
						mutable.setPixel(xProx, yProx, Color.DKGRAY);
					}
					
					if (i > 0 && yProx < mutable.getHeight()) {
						mutable.setPixel(xAnt, yProx, Color.DKGRAY);
					}
					
					if (xProx < mutable.getWidth() && j > 0) {
						mutable.setPixel(xProx, yAnt, Color.DKGRAY);
					}
				}
			}
		}
		
		for (int i = 0; i < mutable.getWidth(); i++) { 
			for (int j = 0; j < mutable.getHeight(); j++) {
				if (mutable.getPixel(i, j) == Color.GREEN) {
					
						mutable.setPixel(i, j, Color.BLACK);
				}
			}
		}
		
//		return Bitmap.createScaledBitmap(mutable, (int) (mutable.getWidth()*0.5f), (int) (mutable.getHeight()*0.5f), true);
		return mutable;
	}
}
