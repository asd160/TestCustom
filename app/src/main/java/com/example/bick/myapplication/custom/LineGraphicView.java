package com.example.bick.myapplication.custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.example.bick.myapplication.R;

import java.util.ArrayList;

/**
 * Created by 白2 鹏 on 2018/1/30.
 */

public class LineGraphicView extends View
{
    /**
     * 公共部分  圆点的大小 每天数据的大小
     */
    private static final int CIRCLE_SIZE = 0;

    private static enum Linestyle
    {
        Line, Curve
    }
    private Context mContext;
    private Paint mPaint;
    private Paint mPaintLow;
    private Resources res; //资源
    private DisplayMetrics dm;

    /**
     * data
     */
    private Linestyle mStyle = Linestyle.Curve;

    private int canvasHeight;
    private int canvasWidth;
    private int bheight = 0;
    private int blwidh;
    private boolean isMeasure = true;
    /**
     * Y轴最大值
     */
    private int maxValue;

    /**
     * Y轴间距值
     */
    private int averageValue;
    private int marginTop = 100;
    private int marginBottom = 150;

    /**
     * 曲线上总点数
     */
    private Point[] mPoints;
    private Point[] mPointslow; //**
    /**
     * 纵坐标值
     */
    private ArrayList<Double> yRawData;
    private ArrayList<Double> yRawDataLow;//**
    /**
     * 横坐标值
     */
    //private ArrayList<String> xRawDatas;
    private ArrayList<Integer> xList = new ArrayList<Integer>();// 记录每个x的值
    private ArrayList<Integer> xListlow = new ArrayList<Integer>();// 记录每个x的值
    private int spacingHeight;

    /**
     * 图片间距
     * @param context
     */
    int space=10;


    public LineGraphicView(Context context)
    {
        this(context, null);
    }

    public LineGraphicView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    /**
     * 初始化
     */
    private void initView()
    {
        this.res = mContext.getResources();
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        if (isMeasure)
        {
            this.canvasHeight = getHeight();
            this.canvasWidth = getWidth();
            if (bheight == 0)
                bheight = (int) (canvasHeight - marginBottom);
            blwidh = dip2px(30);
            isMeasure = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {

        //表格的颜色
        mPaint.setColor(res.getColor(R.color.search_opaque));

        drawImageView(canvas);

        drawAllXLine(canvas);
        // 画直线（纵向）
        drawAllYLine(canvas);


        // 点的操作设置
        mPoints = getPoints();
        //*******
        mPointslow=getPointslow();

        /**
         *  线条的颜色
         */
        mPaint.setColor(res.getColor(R.color.lb_playback_progress_color_no_theme));
        mPaint.setStrokeWidth(dip2px(2.5f));
        mPaint.setStyle(Style.STROKE);
        if (mStyle == Linestyle.Curve)
        {
            drawScrollLine(canvas);
        }
        else
        {
            drawLine(canvas);
        }
        /**
         * 画点
         */
        mPaint.setStyle(Style.FILL);
        for (int i = 0; i < mPoints.length; i++)
        {
            canvas.drawCircle(mPoints[i].x, mPoints[i].y, CIRCLE_SIZE / 2, mPaint);
        }
        /**
         * /*****
         * //参数一为渐变起初点坐标x位置，参数二为y轴位置，参数三和四分辨对应渐变终点，最后参数为平铺方式，这里设置为镜像
         */
        mPaintLow =new Paint(Paint.ANTI_ALIAS_FLAG);
        LinearGradient lg2=new LinearGradient(0,0,100,100,Color.RED,Color.BLUE,Shader.TileMode.MIRROR);

        mPaintLow.setShader(lg2);
        mPaintLow.setStrokeWidth(dip2px(2.5f));
        mPaintLow.setStyle(Style.STROKE);
        if (mStyle == Linestyle.Curve)
        {
            //弧线
            drawScrollLinleow(canvas);
        }
        else
        {
            //圆点
            drawLine(canvas);
        }
        /**
         * 画点
         */
        mPaint.setStyle(Style.FILL);
        for (int i = 0; i < mPointslow.length; i++)
        {
            canvas.drawCircle(mPointslow[i].x, mPointslow[i].y, CIRCLE_SIZE / 2, mPaint);
        }

    }

    /**
     *  画背景图
     * @param canvas
     */
    private void drawImageView(Canvas canvas) {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.day);

        Paint mPaint = new Paint();

        //设置bitmap 位置
        canvas.drawBitmap(bitmap, new Rect(10, 100, 200, 1000), new Rect(10, 100, 200, 1000), null);


    }

    /**
     *  画所有横向表格，包括X轴
     */
    private void drawAllXLine(Canvas canvas)
    {
        for (int i = 0; i < spacingHeight + 1; i++)
        {
            canvas.drawLine(blwidh, bheight - (bheight / spacingHeight) * i + marginTop, (canvasWidth - blwidh),
                    bheight - (bheight / spacingHeight) * i + marginTop, mPaint);// Y坐标
            drawText(String.valueOf(averageValue * i), blwidh / 2, bheight - (bheight / spacingHeight) * i + marginTop,
                    canvas);


            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.movie);
            //设置bitmap 位置
            canvas.drawBitmap(bitmap, new Rect(blwidh + (canvasWidth - blwidh) / yRawData.size() * i, marginTop, blwidh
                    + (canvasWidth - blwidh) / yRawData.size() * i, bheight + marginTop), new Rect(blwidh + (canvasWidth - blwidh) / yRawData.size() * i, marginTop, blwidh
                    + (canvasWidth - blwidh) / yRawData.size() * i, bheight + marginTop), null);

        }
    }

    /**
     * 画所有纵向表格，包括Y轴
     */
    private void drawAllYLine(Canvas canvas)
    {
        for (int i = 0; i < yRawData.size(); i++)
        {
            /**
             * 记录数据 每个点
             */
            xList.add(blwidh + (canvasWidth - blwidh) / yRawData.size() * i);

            xListlow.add(blwidh + (canvasWidth - blwidh) / yRawDataLow.size() * i);
            /**
             * 最高气温
             */
            canvas.drawLine(blwidh + (canvasWidth - blwidh) / yRawData.size() * i, marginTop, blwidh
                    + (canvasWidth - blwidh) / yRawData.size() * i, bheight + marginTop, mPaint);
            /**
             *  di bu wen zi
             */
//            drawText(xRawDatas.get(i), blwidh + (canvasWidth - blwidh) / yRawData.size() * i, bheight + dip2px(26),
//                    canvas);// X坐标

            /**
             * 最低气温
             */
            canvas.drawLine(blwidh + (canvasWidth - blwidh) / yRawDataLow.size() * i, marginTop, blwidh
                   + (canvasWidth - blwidh) / yRawDataLow.size() * i, bheight + marginTop, mPaint);


        }
    }

    private void drawScrollLine(Canvas canvas)
    {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < mPoints.length - 1; i++)
        {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            int wt = (startp.x + endp.x) / 2;
            Point p3 = new Point();
            Point p4 = new Point();
            p3.y = startp.y;
            p3.x = wt;
            p4.y = endp.y;
            p4.x = wt;

            Path path = new Path();
            path.moveTo(startp.x, startp.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            canvas.drawPath(path, mPaint);
        }
    }

    /**
     * 最低温度线条
     * @param canvas
     */
    private void drawScrollLinleow(Canvas canvas)
    {
        //画点
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < mPointslow.length - 1; i++)
        {
            startp = mPointslow[i];//开始
            endp = mPointslow[i + 1];//结束位置
            int wt = (startp.x + endp.x) / 2;
            Point p3 = new Point();
            Point p4 = new Point();
            p3.y = startp.y;
            p3.x = wt;
            p4.y = endp.y;
            p4.x = wt;

            Path path = new Path();
            path.moveTo(startp.x, startp.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            canvas.drawPath(path, mPaintLow);
        }
    }

    private void drawLine(Canvas canvas)
    {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < mPoints.length - 1; i++)
        {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            canvas.drawLine(startp.x, startp.y, endp.x, endp.y, mPaint);
        }
    }

    private void drawText(String text, int x, int y, Canvas canvas)
    {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(dip2px(10));
        p.setColor(res.getColor(R.color.lb_default_search_color));
        p.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(text, x, y, p);
    }

    /**
     * 获取Y 轴总数 边缘的 数值
     * @return
     */
    private Point[] getPoints()
    {
        Point[] points = new Point[yRawData.size()];
        for (int i = 0; i < yRawData.size(); i++)
        {
            int ph = bheight - (int) (bheight * (yRawData.get(i) / maxValue));

            points[i] = new Point(xList.get(i), ph + marginTop);
        }
        return points;
    }
    /**
     * 获取Y 轴  //*****
     * @return
     */
    private Point[] getPointslow()
    {
        Point[] points = new Point[yRawDataLow.size()];
        for (int i = 0; i < yRawDataLow.size(); i++)
        {
            int ph = bheight - (int) (bheight * (yRawDataLow.get(i) / maxValue));

            points[i] = new Point(xListlow.get(i), ph + marginTop);
        }
        return points;
    }

    public void setData(ArrayList<Double> yRawData,ArrayList<Double> yRawDataLow, ArrayList<String> xRawData, int maxValue, int averageValue)
    {
        this.maxValue = maxValue;
        this.averageValue = averageValue;
        this.mPoints = new Point[yRawData.size()];//最高温度

        this.mPointslow = new Point[yRawDataLow.size()];//最低温度//*** 点

        //this.xRawDatas = xRawData;
        this.yRawData = yRawData;
        this.yRawDataLow = yRawDataLow;//***

        this.spacingHeight = maxValue / averageValue; //表格X轴 间距
    }

    public void setTotalvalue(int maxValue)
    {
        this.maxValue = maxValue;
    }

    public void setPjvalue(int averageValue)
    {
        this.averageValue = averageValue;
    }

    public void setMargint(int marginTop)
    {
        this.marginTop = marginTop;
    }

    public void setMarginb(int marginBottom)
    {
        this.marginBottom = marginBottom;
    }

    public void setMstyle(Linestyle mStyle)
    {
        this.mStyle = mStyle;
    }

    public void setBheight(int bheight)
    {
        this.bheight = bheight;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue)
    {
        return (int) (dpValue * dm.density + 0.5f);
    }

}