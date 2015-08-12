package br.com.furb.tagarela.puzzlegame.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import br.com.engine.model.GameObject;

public class CollisionUtil {
	public static boolean touchSelected(GameObject go, Bitmap goBmp, float touchX, float touchY) {
		if ((touchX >= (go.getX())
				&& touchX <= (go.getX() + goBmp.getWidth()) && 
				(touchY <= (go
				.getY() + goBmp.getHeight()) && touchY >= (go
				.getY())))) {
			return true;
		}
		
		return false;
	}
	
	public static Rect getBoundBox(GameObject go, Bitmap gameObjBmp, Float reducePerc) {
		Rect boundBox = new Rect();
		int left = (int) go.getX();
		int right = (int) (go.getX() + gameObjBmp.getWidth());
		int top = (int) go.getY();;
		int bottom = (int) (go.getY() + gameObjBmp.getHeight());
		
		boundBox.left= left;
		boundBox.right = right;
		boundBox.top = top;
		boundBox.bottom = bottom;
		if (reducePerc != null) {
			int reduceWidth = (int) (gameObjBmp.getWidth() * reducePerc/2);
			int reduceHeight = (int) (gameObjBmp.getHeight() * reducePerc/2);
			
			boundBox.left += reduceWidth;
			boundBox.right -= reduceWidth;
			boundBox.top += reduceHeight;
			boundBox.bottom -= reduceHeight;
		}
		
		
		return boundBox;
	}
	
	public static int getTotalPixelColliding(Rect collisionBBox, Bitmap bmp1, int x1, int y1, Bitmap bmp2, int x2, int y2) {
		int limEsq = collisionBBox.left - x1;
		int limDir = collisionBBox.right - x1;
		int limTopo = collisionBBox.top - y1;
		int limBottom = collisionBBox.bottom - y1;
		
		System.out.println("LIM ESQ" + limEsq + " LIM DIR: " + limDir + " Lim top: " + limTopo + " Lim Bot: " + limBottom);
		
		int limX2 = collisionBBox.left - x2;
		int limY2 = collisionBBox.top - y2;
		
		int diffX2 = limX2 - limEsq;
		int diffY2 = limY2 - limTopo;
		
		int totalColliding = 0;
		
		int newX2;
		int newY2;
		for (int i = limEsq; i < limDir; i++) { 
			for (int j = limTopo; j < limBottom; j++) {
				x1 = i;
				y1 = j;	
				
				newX2 = x1 + diffX2;
				newY2 = y1 + diffY2;
				
				if (y1 < bmp1.getHeight() && x1 < bmp1.getWidth() && 
						newY2 < bmp2.getHeight() && newX2 < bmp2.getWidth()
						&& x1 >= 0 && newX2 >= 0 && y1 >= 0 && newY2 >= 0) {
					int pixel1 = bmp1.getPixel(x1, y1);
					int pixel2 = bmp2.getPixel(newX2, newY2);
					
					if (pixel1 != Color.TRANSPARENT && pixel2 != Color.TRANSPARENT) {
						totalColliding++;
					}
				}
					/*
				} else if (x1 < 0 || x1 > bmp1.getWidth()) {
						System.out.println("X1: "+ x1 + " IMG 1 WIDTH: " + bmp1.getWidth());
						System.out.println("LEFT: "+ newArea.left + " RIGHT: " + newArea.right);
				} else if (x2 < 0 || x2 > bmp2.getWidth()) {
						System.out.println("X2: "+ x2 + " IMG 2 WIDTH: " + bmp2.getWidth());
				} else if (y1 < 0 || y1 > bmp1.getHeight()) {
					System.out.println("Y1: "+ y1 + " IMG 1 HEIGHT: " + bmp1.getHeight());
				} else if (y2 < 0 || y2 > bmp2.getHeight()) {
					System.out.println("Y2: "+ y2 + " IMG 2 HEIGHT: " + bmp2.getHeight());
				}
					System.out.println("Y1: " + y1 + "X1: " + x1 + "Y2: " + y2 + "X2: " + x2);
					 */
			}	
		}
	
		return totalColliding;
	}
}
