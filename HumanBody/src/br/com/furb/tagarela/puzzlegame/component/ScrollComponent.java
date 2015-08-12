package br.com.furb.tagarela.puzzlegame.component;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import br.com.engine.component.AbstractComponent;
import br.com.engine.model.GameObject;
import br.com.engine.system.message.MessageWrapper;
import br.com.engine.system.message.MessageWrapper.MessageType;
import br.com.engine.system.render.CanvasHolder;
import br.com.engine.system.render.RenderWrapper;
import br.com.furb.tagarela.puzzlegame.enumeration.EnumMessageId;
import br.com.furb.tagarela.puzzlegame.messages.MessageDragScroll;

public class ScrollComponent extends AbstractComponent {
	private static String DEBUG_TAG = "SCROLL";

	private RectBackground rectBackground;
	private float mPosXStarScroll = -1;
	private int mStartScrollOffset = 1;
	private int mTotalScrolled = 0;
	private List<ScrollObjectHolder> draggables;
	private int screenWidth;
	private boolean mScrolling = false;
	private int mScrollDistToDropObject = 120;
	private int mSpaceBetweenObjects = 30;

	public ScrollComponent(List<ScrollObjectHolder> draggables, int screenWidth, int screenHeight) {
		this.draggables = new ArrayList<ScrollObjectHolder>();
		this.draggables.addAll(draggables);
		this.screenWidth = screenWidth;
		this.rectBackground = new RectBackground(0, screenHeight, screenWidth, 160);
		this.configureObjects();
	}

	private void configureObjects() {
		int xInicial = 0;
		for (int i = 0; i < draggables.size(); i++) {
			ScrollObjectHolder go = draggables.get(i);
			xInicial += mSpaceBetweenObjects;
			go.obj.setY((this.rectBackground.getRect().bottom 
					+ this.rectBackground.getRect().top) / 2
					- go.height / 2);
			go.obj.setX(xInicial);
			xInicial += go.width;
		}
		
		int width = xInicial + mSpaceBetweenObjects;
		if (width < screenWidth) {
			width = screenWidth;
		}
		
		this.rectBackground.updatePosition(0, width);
	}

	@Override
	public void onUpdate() {
	}

	@Override
	public void onTouchListener(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (touchSelected(event.getX(), event.getY())) {
				mPosXStarScroll = event.getX();
				mScrolling = true;
				mTotalScrolled = 0;
			}
			break;

		case MotionEvent.ACTION_MOVE:
			if (!touchSelected(event.getX(), event.getY())
					|| draggables.isEmpty()) {
				mScrolling = false;
				return;
			}

			if (mScrolling) {
				float x = event.getX();
				float dx = x - mPosXStarScroll;
				
				if (mTotalScrolled > mScrollDistToDropObject
						|| mTotalScrolled < mScrollDistToDropObject * -1) {
					sendStopDragMessage();
				}
				
				if (event.getX() > mPosXStarScroll + mStartScrollOffset) {
					this.scrollObjects(ScrollDirection.RIGHT, (int) dx);
				} else if (event.getX() < mPosXStarScroll - mStartScrollOffset) {
					this.scrollObjects(ScrollDirection.LEFT, (int) dx);
				}

				mPosXStarScroll = event.getX();
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			break;
		default:
			break;
		}
	}

	private void sendStopDragMessage() {
		MessageWrapper wrapper = new MessageWrapper(
				EnumMessageId.STOP_DRAG.ordinal(), null,
				MessageType.LISTENER);

		for (ScrollObjectHolder scrollHolder : draggables) {
			((AbstractComponent) scrollHolder.obj.getComponents().get(0))
					.sendMessageToOwnerComponents(wrapper);
		}
	}

	private void scrollObjects(ScrollDirection direction, int value) {
		int proXScroll = rectBackground.getX() + value;
		
		if (direction == ScrollDirection.RIGHT && proXScroll >= 0) {
			proXScroll = 0;
			this.rectBackground.updatePosition(proXScroll, null);
			this.configureObjects();
			return;
		}  else if (direction == ScrollDirection.LEFT 
				&& proXScroll * -1 >=rectBackground.getWidth()  - screenWidth) {
			return;
		}
		
		this.mTotalScrolled += value;
		
		for (ScrollObjectHolder go : draggables) {
			go.obj.setX(go.obj.getX() + value);
		}
		
		this.rectBackground.updatePosition(proXScroll, null);
	}

	@Override
	public void onRender(RenderWrapper render) {
		CanvasHolder holder = render.getHolder();
		holder.paint.setStyle(Style.FILL);
		holder.paint.setColor(Color.YELLOW);
		holder.canvas.drawRect(rectBackground.getRect(), holder.paint);
	}

	public boolean touchSelected(float touchX, float touchY) {
		if (touchY <= rectBackground.getRect().bottom && touchY >= rectBackground.getRect().top) {
			return true;
		}

		return false;
	}

	@Override
	public void receiveMessage(MessageWrapper wrapper) {
		if (wrapper.getId() == EnumMessageId.DRAG_SCROLL_PUT_BACK.ordinal()) {
			MessageDragScroll message = wrapper.getMessage();

			GameObject obj = message.getGameObject();

			ScrollObjectHolder draggedHolder = getSelectedScrollHolder(obj);

			int index = draggables.indexOf(draggedHolder);
			putbackObjectIntoScroll(draggedHolder, index);

		} else if (wrapper.getId() == EnumMessageId.DRAG_SCROLL_REMOVE_ELEMNT
				.ordinal()) {
			MessageDragScroll message = wrapper.getMessage();

			GameObject obj = message.getGameObject();

			ScrollObjectHolder draggedHolder = getSelectedScrollHolder(obj);

			this.draggables.remove(draggedHolder);

			this.configureObjects();
		} 
	}

	private void putbackObjectIntoScroll(ScrollObjectHolder draggedHolder,
			int index) {
		if (index > 0) {
			ScrollObjectHolder anterior = draggables.get(index - 1);
			draggedHolder.obj
					.setY((this.rectBackground.getRect().bottom + this.rectBackground
							.getRect().top) / 2 - draggedHolder.height / 2);
			draggedHolder.obj.setX(anterior.obj.getX() + anterior.width
					+ mSpaceBetweenObjects);
		} else {
			ScrollObjectHolder prox = (index + 1 < draggables.size()) ? draggables
					.get(index + 1) : null;

			if (prox != null) {
				draggedHolder.obj.setX(prox.obj.getX() - draggedHolder.width
						- mSpaceBetweenObjects);
			} else {
				draggedHolder.obj.setX(mSpaceBetweenObjects);
			}

			draggedHolder.obj
					.setY((this.rectBackground.getRect().bottom + this.rectBackground
							.getRect().top) / 2 - draggedHolder.height / 2);
		}
	}

	private ScrollObjectHolder getSelectedScrollHolder(GameObject obj) {
		ScrollObjectHolder draggedHolder = null;
		for (ScrollObjectHolder holder : draggables) {
			if (holder.obj.equals(obj)) {
				draggedHolder = holder;
				break;
			}
		}

		return draggedHolder;
	}

	private enum ScrollDirection {
		LEFT, 
		RIGHT;
	}

	private class RectBackground {
		private int x;
		private int y;
		private int width;
		private int height;
		private Rect rect;
		
		public RectBackground(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.rect = new Rect(x, y - height, x + width, y);
		}
		
		public void updatePosition(Integer newX, Integer width) {
			if (width != null) {
				this.width = width;
			}
			
			this.x = newX;
			
			this.rect = new Rect(x, y - height, x + this.width, y);
		}

		public int getX() {
			return x;
		}
		
		public int getWidth() {
			return width;
		}

		public Rect getRect() {
			return this.rect;
		}
	}
}
