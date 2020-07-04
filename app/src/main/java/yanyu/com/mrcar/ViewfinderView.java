package yanyu.com.mrcar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public final class ViewfinderView extends View {
    private static final long ANIMATION_DELAY = 15L;
    private static final int POINT_SIZE = 20;
    private static final int CORNER_RECT_WIDTH             =   8;  //扫描区边角的宽
    private static final int CORNER_RECT_HEIGHT            =   40; //扫描区边角的高
    private static final int SCANNER_LINE_MOVE_DISTANCE    =   6;  //扫描线移动距离
    private static final int SCANNER_LINE_HEIGHT           =   10;  //扫描线宽度
    private Paint paint;
    private int maskColor = Color.parseColor("#60000000");;
    private int frameColor = Color.parseColor("#7F1FB3E2");
    private int laserColor = Color.parseColor("#FF1FB3E2");
    private int cornerColor = Color.parseColor("#FF1FB3E2");
    public int scannerStart = 0;
    public int scannerEnd = 0;
    private int frameWidth = 0;
    private int frameHeight = 0;

    private int screenWidth;
    private int screenHeight;
    private Rect frame;
    public enum LaserStyle{
        NONE(0),LINE(1),GRID(2);
        private int mValue;
        LaserStyle(int value){
            mValue = value;
        }

        private static LaserStyle getFromInt(int value){

            for(LaserStyle style : LaserStyle.values()){
                if(style.mValue == value){
                    return style;
                }
            }

            return LaserStyle.LINE;
        }
    }
    private LaserStyle laserStyle = LaserStyle.LINE;;
    private int gridColumn = 20;
    private int gridHeight = 40;

    public ViewfinderView(Context context) {
        this(context,null);
    }

    public ViewfinderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ViewfinderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }
    private DisplayMetrics getDisplayMetrics(){
        return getResources().getDisplayMetrics();
    }
    private void init(Context context, AttributeSet attrs) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        screenWidth = getDisplayMetrics().widthPixels;
        screenHeight = getDisplayMetrics().heightPixels;
        int size = (int)(Math.min(screenWidth,screenHeight) * 0.625f);

        if(frameWidth<=0 || frameWidth > screenWidth){
            frameWidth = size;
        }

        if(frameHeight<=0 || frameHeight > screenHeight){
            frameHeight = size/2;
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //扫码框默认居中，支持利用内距偏移扫码框
        int leftOffset = (screenWidth - frameWidth) / 2 + getPaddingLeft() - getPaddingRight();
        int topOffset = (screenHeight - frameHeight) / 2 + getPaddingTop() - getPaddingBottom() - 100;
        frame = new Rect(leftOffset, topOffset, leftOffset + frameWidth, topOffset + frameHeight);
    }
    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        if (frame == null) {
            return;
        }
        if(scannerStart == 0 || scannerEnd == 0) {
            scannerStart = frame.top;
            scannerEnd = frame.bottom - SCANNER_LINE_HEIGHT;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        drawExterior(canvas,frame,width,height);
        drawLaserScanner(canvas,frame);
        drawFrame(canvas, frame);
        drawCorner(canvas, frame);
        postInvalidateDelayed(ANIMATION_DELAY,
                frame.left - POINT_SIZE,
                frame.top - POINT_SIZE,
                frame.right + POINT_SIZE,
                frame.bottom + POINT_SIZE);
    }
    private void drawExterior(Canvas canvas, Rect frame, int width, int height) {
        paint.setColor(maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);
    }
    private void drawFrame(Canvas canvas, Rect frame) {
        paint.setColor(frameColor);
        canvas.drawRect(frame.left, frame.top, frame.right + 1, frame.top + 2, paint);
        canvas.drawRect(frame.left, frame.top + 2, frame.left + 2, frame.bottom - 1, paint);
        canvas.drawRect(frame.right - 1, frame.top, frame.right + 1, frame.bottom - 1, paint);
        canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1, frame.bottom + 1, paint);
    }
    private void drawCorner(Canvas canvas, Rect frame) {
        paint.setColor(cornerColor);
        //左上
        canvas.drawRect(frame.left, frame.top, frame.left + CORNER_RECT_WIDTH, frame.top + CORNER_RECT_HEIGHT, paint);
        canvas.drawRect(frame.left, frame.top, frame.left + CORNER_RECT_HEIGHT, frame.top + CORNER_RECT_WIDTH, paint);
        //右上
        canvas.drawRect(frame.right - CORNER_RECT_WIDTH, frame.top, frame.right, frame.top + CORNER_RECT_HEIGHT, paint);
        canvas.drawRect(frame.right - CORNER_RECT_HEIGHT, frame.top, frame.right, frame.top + CORNER_RECT_WIDTH, paint);
        //左下
        canvas.drawRect(frame.left, frame.bottom - CORNER_RECT_WIDTH, frame.left + CORNER_RECT_HEIGHT, frame.bottom, paint);
        canvas.drawRect(frame.left, frame.bottom - CORNER_RECT_HEIGHT, frame.left + CORNER_RECT_WIDTH, frame.bottom, paint);
        //右下
        canvas.drawRect(frame.right - CORNER_RECT_WIDTH, frame.bottom - CORNER_RECT_HEIGHT, frame.right, frame.bottom, paint);
        canvas.drawRect(frame.right - CORNER_RECT_HEIGHT, frame.bottom - CORNER_RECT_WIDTH, frame.right, frame.bottom, paint);
    }

    public int shadeColor(int color) {
        String hax = Integer.toHexString(color);
        String result = "01"+hax.substring(2);
        return Integer.valueOf(result, 16);
    }
    private void drawLineScanner(Canvas canvas,Rect frame){
        //线性渐变
        LinearGradient linearGradient = new LinearGradient(
                frame.left, scannerStart,
                frame.left, scannerStart + SCANNER_LINE_HEIGHT,
                shadeColor(laserColor),
                laserColor,
                Shader.TileMode.MIRROR);

        paint.setShader(linearGradient);
        if(scannerStart <= scannerEnd) {
            //椭圆
            RectF rectF = new RectF(frame.left + 2 * SCANNER_LINE_HEIGHT, scannerStart, frame.right - 2 * SCANNER_LINE_HEIGHT, scannerStart + SCANNER_LINE_HEIGHT);
            canvas.drawOval(rectF, paint);
            scannerStart += SCANNER_LINE_MOVE_DISTANCE;
        } else {
            scannerStart = frame.top;
        }
    }
    private void drawGridScanner(Canvas canvas,Rect frame){
        int stroke = 2;
        paint.setStrokeWidth(stroke);
        //计算Y轴开始位置
        int startY = gridHeight > 0 && scannerStart - frame.top > gridHeight ? scannerStart - gridHeight : frame.top;

        LinearGradient linearGradient = new LinearGradient(frame.left + frame.width()/2, startY, frame.left + frame.width()/2, scannerStart, new int[]{shadeColor(laserColor), laserColor}, new float[]{0,1f}, LinearGradient.TileMode.CLAMP);
        //给画笔设置着色器
        paint.setShader(linearGradient);

        float wUnit = frame.width() * 1.0f/ gridColumn;
        float hUnit = wUnit;
        //遍历绘制网格纵线
        for (int i = 1; i < gridColumn; i++) {
            canvas.drawLine(frame.left + i * wUnit, startY,frame.left + i * wUnit, scannerStart,paint);
        }

        int height = gridHeight > 0 && scannerStart - frame.top > gridHeight ? gridHeight : scannerStart - frame.top;

        //遍历绘制网格横线
        for (int i = 0; i <= height/hUnit; i++) {
            canvas.drawLine(frame.left, scannerStart - i * hUnit,frame.right, scannerStart - i * hUnit,paint);
        }

        if(scannerStart<scannerEnd){
            scannerStart += SCANNER_LINE_MOVE_DISTANCE;
        } else {
            scannerStart = frame.top;
        }

    }
    private void drawLaserScanner(Canvas canvas, Rect frame) {
        if(laserStyle!=null){
            paint.setColor(laserColor);
            switch (laserStyle){
                case LINE://线
                    drawLineScanner(canvas,frame);
                    break;
                case GRID://网格
                    drawGridScanner(canvas,frame);
                    break;
            }
            paint.setShader(null);
        }
    }

}