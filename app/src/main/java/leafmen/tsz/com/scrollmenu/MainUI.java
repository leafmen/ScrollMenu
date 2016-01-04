package leafmen.tsz.com.scrollmenu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by LiQiang on 2015/12/23.
 */
public class MainUI extends RelativeLayout {
    private Context context;
    private FrameLayout leftMenu;
    private FrameLayout middleMenu;
    private FrameLayout rightMenu;
    private FrameLayout middleMask;
    public static final int LEFT_MENU_ID = 0xaabbcc;
    public static final int MIDDLE_MENU_ID = 0xbbccaa;
    public static final int RIGHT_MENU_ID = 0xccbbaa;
    private Scroller mscroller;

    public MainUI(Context context) {
        super(context);
        initView(context);
    }

    public MainUI(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        mscroller = new Scroller(context, new DecelerateInterpolator());
        leftMenu = new FrameLayout(context);
        middleMenu = new FrameLayout(context);
        rightMenu = new FrameLayout(context);
        middleMask = new FrameLayout(context);
        leftMenu.setBackgroundColor(Color.YELLOW);
        middleMenu.setBackgroundColor(Color.WHITE);
        rightMenu.setBackgroundColor(Color.YELLOW);
        middleMask.setBackgroundColor(0x88000000);
        leftMenu.setId(LEFT_MENU_ID);
        middleMenu.setId(MIDDLE_MENU_ID);
        rightMenu.setId(RIGHT_MENU_ID);

        addView(leftMenu);
        addView(middleMenu);
        addView(rightMenu);
        addView(middleMask);
        middleMask.setAlpha(0);


    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        int curX = Math.abs(getScrollX());
        float scale = curX / (float) leftMenu.getMeasuredWidth();
        middleMask.setAlpha(scale);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        middleMenu.measure(widthMeasureSpec, heightMeasureSpec);
        middleMask.measure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int measureWidth = MeasureSpec.makeMeasureSpec((int) (width * 0.7f), MeasureSpec.EXACTLY);
        leftMenu.measure(measureWidth, heightMeasureSpec);
        rightMenu.measure(measureWidth, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        middleMenu.layout(l, t, r, b);
        middleMask.layout(l, t, r, b);
        leftMenu.layout(l - leftMenu.getMeasuredWidth(), t, r, b);
        rightMenu.layout(l + middleMenu.getMeasuredWidth(), t, l + middleMenu.getMeasuredWidth() + rightMenu.getMeasuredWidth(), b);
    }

    private boolean isTestCompete;
    boolean isleftrightEvent;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isTestCompete) {
            getEventType(ev);
            return true;
        }

        if (isleftrightEvent) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_MOVE:
                    int curScrollX = getScrollX();
                    int dis_x = (int) (ev.getX() - point.x);
                    int expectX = -dis_x + curScrollX;
                    int finalX = 0;
                    if (expectX < 0) {
                        finalX = Math.max(expectX, -leftMenu.getMeasuredWidth());
                    } else {
                        finalX = Math.min(expectX, rightMenu.getMeasuredWidth());
                    }
                    scrollTo(finalX, 0);
                    point.x = (int) ev.getX();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    curScrollX = getScrollX();
                    if (Math.abs(curScrollX) > leftMenu.getMeasuredWidth() >> 1) {
                        if (curScrollX < 0) {
                            mscroller.startScroll(curScrollX, 0, -leftMenu.getMeasuredWidth() - curScrollX, 0, 180);
                        } else {
                            mscroller.startScroll(curScrollX, 0, leftMenu.getMeasuredWidth() - curScrollX, 0, 180);
                        }

                    } else {
                        mscroller.startScroll(curScrollX, 0, -curScrollX, 0, 180);
                    }

                    invalidate();

                    isleftrightEvent = false;
                    isTestCompete = false;
                    break;
            }

        } else {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isleftrightEvent = false;
                    isTestCompete = false;
                    break;
            }
        }


        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (!mscroller.computeScrollOffset()) {
            return;
        }
        int tempX = mscroller.getCurrX();
        scrollTo(tempX, 0);
    }

    private Point point = new Point();
    private static final int TEST_DIS = 20;

    private void getEventType(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                point.x = (int) ev.getX();
                point.y = (int) ev.getY();
                super.dispatchTouchEvent(ev);
                break;

            case MotionEvent.ACTION_MOVE:
                int dX = Math.abs((int) ev.getX() - point.x);
                int dY = Math.abs((int) ev.getY() - point.y);
                if (dX >= TEST_DIS && dX > dY) {//左右滑动
                    isleftrightEvent = true;
                    isTestCompete = true;
                    point.x = (int) ev.getX();
                    point.y = (int) ev.getY();

                } else if (dY >= TEST_DIS && dY > dX) {//上下滑动
                    isleftrightEvent = false;
                    isTestCompete = true;
                    point.x = (int) ev.getX();
                    point.y = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                super.dispatchTouchEvent(ev);
                isleftrightEvent = false;
                isTestCompete = false;
                break;
        }
    }
}
