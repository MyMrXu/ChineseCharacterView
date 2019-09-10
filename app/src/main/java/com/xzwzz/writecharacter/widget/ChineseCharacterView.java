package com.xzwzz.writecharacter.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import com.xzwzz.writecharacter.R;
import com.xzwzz.writecharacter.util.JsonUtil;
import com.xzwzz.writecharacter.util.PathParser;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ChineseCharacterView extends View {
    public static final int MSG = 1;

    private final String TAG = ChineseCharacterView.class.getSimpleName();

    private HashMap _$_findViewCache;

    private TimerHandler animateHandler = new TimerHandler(this);

    private float animateValue;

    private int arrowSize = dp2px(5.0F);

    private boolean autoDraw;

    private Region curClipRegion;

    private Path curDrawPath;

    private PathMeasure curDrawPathMeasure;

    private int curDrawingIndex;

    private boolean curDrawingPathValid;

    private Path curMedianPath;

    private RectF curPathRectF;

    private Region curPathRegion;

    private Path curStrokePath;

    private int curWrongTouchCount;

    private Paint dashPaint = new Paint(1);

    private float downX;

    private float downY;

    private boolean drawGrid = true;

    private int fixedSize = 1050;

    private Paint gridPaint = new Paint(1);

    private RectF gridRect = new RectF();

    private int gridWidth = dp2px(1.0F);

    private boolean isPause;

    private boolean isStop;

    private float lastX;

    private float lastY;

    private Paint maskPaint = new Paint(1);

    private final float[] maskPos;

    private float maskRadius;

    private final float[] maskTan;

    private final int maxWrongTouch = 60;

    private List<String> medianOriPaths;

    private Paint medianPaint = new Paint(1);

    private final ArrayList<Path> medianPaths = new ArrayList();

    private final long messageTimeGap = 30L;

    private final long messageTimeNextStrokeGap = 300L;

    private boolean midDrawingValid;

    private boolean needShift = true;

    private Matrix pathMatrix = new Matrix();

    private List<? extends List<? extends PointF>> pathPoints;

    private List<Path> preDrawPaths = (List) new ArrayList();

    private int rectSize;

    private float shiftFactor = 0.8F;

    private int strokeColor = Color.parseColor("#bcbcbc");

    private StrokeDrawListener strokeDrawCompleteListener;

    private List<String> strokeInfo;

    private Paint strokePaint = new Paint(1);

    private final ArrayList<Path> strokePaths = new ArrayList();

    private int touchColor = Color.parseColor("#ffba00");

    private Paint touchPaint = new Paint(1);

    private float touchWidth;

    public ChineseCharacterView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ChineseCharacterView);
        this.drawGrid = typedArray.getBoolean(R.styleable.ChineseCharacterView_drawgrid, true);
        this.autoDraw = typedArray.getBoolean(R.styleable.ChineseCharacterView_autodraw, false);
        typedArray.recycle();
        this.gridPaint.setColor(-7829368);
        this.gridPaint.setStyle(Paint.Style.STROKE);
        this.gridPaint.setStrokeWidth(this.gridWidth);
        this.dashPaint.setColor(-7829368);
        this.dashPaint.setStyle(Paint.Style.STROKE);
        this.dashPaint.setStrokeWidth(dp2px(1.0F));
        this.dashPaint.setPathEffect((PathEffect) new DashPathEffect(new float[]{5.0F, 5.0F}, 0.0F));
        this.medianPaint.setColor(this.touchColor);
        this.medianPaint.setStyle(Paint.Style.STROKE);
        this.medianPaint.setStrokeJoin(Paint.Join.ROUND);
        this.medianPaint.setStrokeCap(Paint.Cap.ROUND);
        this.medianPaint.setStrokeWidth(dp2px(1.0F));
        this.strokePaint.setColor(this.strokeColor);
        this.strokePaint.setStyle(Paint.Style.FILL);
        this.strokePaint.setStrokeJoin(Paint.Join.ROUND);
        this.strokePaint.setStrokeCap(Paint.Cap.ROUND);
        this.touchPaint.setColor(this.touchColor);
        this.touchPaint.setStyle(Paint.Style.STROKE);
        this.touchPaint.setXfermode((Xfermode) new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        this.maskPaint.setColor(this.touchColor);
        this.maskPaint.setStyle(Paint.Style.FILL);
        this.maskPaint.setXfermode((Xfermode) new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        setLayerType(1, null);
        this.curPathRectF = new RectF();
        this.curPathRegion = new Region();
        this.curClipRegion = new Region();
        this.midDrawingValid = true;
        this.maskPos = new float[2];
        this.maskTan = new float[2];

        init();
    }

    private void init() {
        initStrokePaths();

    }

    private void drawArrow(Canvas paramCanvas, Path paramPath) {
        float[] arrayOfFloat1 = new float[2];
        float[] arrayOfFloat2 = new float[2];
        PathMeasure pathMeasure = new PathMeasure(paramPath, false);
        pathMeasure.getPosTan(pathMeasure.getLength(), arrayOfFloat1, arrayOfFloat2);
        double d1 = Math.atan2(arrayOfFloat2[1], arrayOfFloat2[0]) * 180.0F / Math.PI;
        double d2 = 15;
        double d3 = '`';
        double d7 = (d1 + d2) * Math.PI / d3;
        double d6 = (d1 - d2) * Math.PI / d3;
        d1 = arrayOfFloat1[0];
        d2 = this.arrowSize;
        d3 = Math.cos(d7);
        double d4 = arrayOfFloat1[1];
        double d5 = this.arrowSize;
        d7 = Math.sin(d7);
        double d8 = arrayOfFloat1[0];
        double d9 = this.arrowSize;
        double d10 = Math.cos(d6);
        double d11 = arrayOfFloat1[1];
        double d12 = this.arrowSize;
        d6 = Math.sin(d6);
        Path path = new Path();
        path.moveTo((float) (d1 - d2 * d3), (float) (d4 - d5 * d7));
        path.lineTo(arrayOfFloat1[0], arrayOfFloat1[1]);
        path.lineTo((float) (d8 - d9 * d10), (float) (d11 - d12 * d6));
        if (paramCanvas != null) {
            paramCanvas.drawPath(path, this.medianPaint);
        }
    }

    private boolean inEndPointRange(float paramFloat1, float paramFloat2) {
        if (this.curDrawPathMeasure == null) {
            return false;
        }
        float[] arrayOfFloat1 = new float[2];
        float[] arrayOfFloat2 = new float[2];
        PathMeasure pathMeasure1 = this.curDrawPathMeasure;
        PathMeasure pathMeasure2 = this.curDrawPathMeasure;
        pathMeasure1.getPosTan(pathMeasure2.getLength(), arrayOfFloat1, arrayOfFloat2);
        paramFloat1 -= arrayOfFloat1[0];
        paramFloat2 -= arrayOfFloat1[1];
        return (Math.sqrt((paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2)) <= this.touchWidth);
    }

    private boolean inMidPointRange(float paramFloat1, float paramFloat2) {
        return this.curPathRegion.contains((int) paramFloat1, (int) paramFloat2);
    }

    private boolean inStartPointRange(float paramFloat1, float paramFloat2) {
        if (this.curDrawPathMeasure == null) {
            return false;
        }
        float[] arrayOfFloat1 = new float[2];
        float[] arrayOfFloat2 = new float[2];
        PathMeasure pathMeasure = this.curDrawPathMeasure;
        pathMeasure.getPosTan(0.0F, arrayOfFloat1, arrayOfFloat2);
        paramFloat1 -= arrayOfFloat1[0];
        paramFloat2 -= arrayOfFloat1[1];
        return (Math.sqrt((paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2)) <= this.touchWidth);
    }

    private static Path parser(String paramString) {
        Path path = PathParser.createPathFromPathData(paramString);
        path.setFillType(Path.FillType.WINDING);
        return path;
    }

    private void initStrokePaths() {
        try {
            List<String> list = this.strokeInfo;
            if (list != null) {
                this.strokePaths.clear();
                for (String str : list) {
                    Path path = parser(str);
                    path.transform(this.pathMatrix);
                    this.strokePaths.add(path);
                }
            }
            return;
        } catch (Exception exception) {
            return;
        }
    }

    private void setAnimateValue(float paramFloat) {
        float f;
        PathMeasure pathMeasure = this.curDrawPathMeasure;
        if (pathMeasure != null) {
            f = pathMeasure.getLength();
        } else {
            f = 0.0F;
        }
        if (paramFloat >= f) {
            this.animateValue = 0.0F;
            this.curDrawingIndex++;
            if (this.curDrawingIndex >= this.medianPaths.size()) {
                if (this.curDrawingIndex == this.medianPaths.size()) {
                    StrokeDrawListener strokeDrawListener = this.strokeDrawCompleteListener;
                    if (strokeDrawListener != null) {
                        strokeDrawListener.onStrokeDrawComplete();
                    }
                } else {
                    this.curDrawingIndex = this.medianPaths.size();
                }
            } else {
                Path path = this.curDrawPath;
                if (path != null) {
                    this.preDrawPaths.add(path);
                }
                this.curDrawPath = new Path();
                this.curMedianPath = (Path) this.medianPaths.get(this.curDrawingIndex);
                PathMeasure pathMeasure1 = this.curDrawPathMeasure;
                if (pathMeasure1 != null) {
                    pathMeasure1.setPath(this.curMedianPath, false);
                }
                this.animateHandler.sendMessageDelayed(Message.obtain((Handler) this.animateHandler, 1), this.messageTimeNextStrokeGap);
            }
        } else {
            long l;
            this.animateValue = paramFloat;
            pathMeasure = this.curDrawPathMeasure;
            if (pathMeasure != null) {
                pathMeasure.getSegment(0.0F, paramFloat, this.curDrawPath, true);
            }
            if (this.curDrawingIndex == 0 && paramFloat == 0.0F) {
                l = this.messageTimeNextStrokeGap;
            } else {
                l = this.messageTimeGap;
            }
            this.animateHandler.sendMessageDelayed(Message.obtain((Handler) this.animateHandler, 1), l);
        }
        invalidate();
    }

    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }
    }

    public View _$_findCachedViewById(int paramInt) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view2 = (View) this._$_findViewCache.get(Integer.valueOf(paramInt));
        View view1 = view2;
        if (view2 == null) {
            view1 = findViewById(paramInt);
            this._$_findViewCache.put(Integer.valueOf(paramInt), view1);
        }
        return view1;
    }

    public final void clear() {
        this.strokePaths.clear();
        this.medianPaths.clear();
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.animateHandler.removeMessages(1);
    }

    private boolean showMedian = false;

    public void setShowMedian(boolean showMedian) {
        this.showMedian = showMedian;
    }

    @Override
    protected void onDraw(Canvas paramCanvas) {
        try {
            super.onDraw(paramCanvas);
            if (this.drawGrid) {
                if (paramCanvas != null) {
                    paramCanvas.drawRect(this.gridRect, this.gridPaint);
                }
                if (paramCanvas != null) {
                    paramCanvas.drawLine(0.0F, (this.rectSize / 2), this.rectSize, (this.rectSize / 2), this.dashPaint);
                }
                if (paramCanvas != null) {
                    paramCanvas.drawLine((this.rectSize / 2), 0.0F, (this.rectSize / 2), this.rectSize, this.dashPaint);
                }
                if (paramCanvas != null) {
                    paramCanvas.drawLine(0.0F, 0.0F, this.rectSize, this.rectSize, this.dashPaint);
                }
                if (paramCanvas != null) {
                    paramCanvas.drawLine(0.0F, this.rectSize, this.rectSize, 0.0F, this.dashPaint);
                }
            }
            boolean bool = this.autoDraw;
            int j = 0;
            int i = 0;
            if (bool) {
                if (this.strokePaths.size() == 0) {
                    return;
                }
                if (this.curDrawingIndex < this.strokePaths.size()) {
                    int k = this.curDrawingIndex + 1;
                    j = this.strokePaths.size();
                    while (k < j) {
                        this.curStrokePath = (Path) this.strokePaths.get(k);
                        this.strokePaint.setColor(this.strokeColor);
                        Path path = this.curStrokePath;
                        if (path != null && paramCanvas != null) {
                            paramCanvas.drawPath(path, this.strokePaint);
                        }
                        k++;
                    }
                    if (paramCanvas != null) {
                        k = paramCanvas.saveLayer(0.0F, 0.0F, this.rectSize, this.rectSize, null, Canvas.ALL_SAVE_FLAG);
                    } else {
                        k = 0;
                    }
                    this.strokePaint.setColor(this.strokeColor);
                    this.curStrokePath = (Path) this.strokePaths.get(this.curDrawingIndex);
                    Path path2 = this.curStrokePath;
                    if (path2 != null && paramCanvas != null) {
                        paramCanvas.drawPath(path2, this.strokePaint);
                    }
                    if (this.animateValue == 0.0F) {
                        this.maskRadius = 0.0F;
                    } else {
                        this.maskRadius = this.rectSize * 1.0F / 30;
                    }
                    PathMeasure pathMeasure = this.curDrawPathMeasure;
                    if (pathMeasure == null) {
                    } else {
                        pathMeasure.getPosTan(0.0F, this.maskPos, this.maskTan);
                    }
                    if (paramCanvas != null) {
                        paramCanvas.drawCircle(this.maskPos[0], this.maskPos[1], this.maskRadius, this.maskPaint);
                    }
                    Path path1 = this.curDrawPath;
                    if (path1 != null && paramCanvas != null) {
                        paramCanvas.drawPath(path1, this.touchPaint);
                    }
                    if (paramCanvas != null) {
                        paramCanvas.restoreToCount(k);
                    }
                }
                j = this.curDrawingIndex;
                for (int b = i; b < j; b++) {
                    this.curStrokePath = (Path) this.strokePaths.get(b);
                    this.strokePaint.setColor(this.touchColor);
                    Path path = this.curStrokePath;
                    if (path != null && paramCanvas != null) {
                        paramCanvas.drawPath(path, this.strokePaint);
                    }
                }
            } else if (this.curDrawingIndex <= this.strokePaths.size()) {
                int k = this.curDrawingIndex + 1;
                i = this.strokePaths.size();
                while (k < i) {
                    this.curStrokePath = (Path) this.strokePaths.get(k);
                    this.strokePaint.setColor(showMedian ? this.strokeColor : this.touchColor);
                    Path path = this.curStrokePath;
                    if (path != null && paramCanvas != null) {
                        paramCanvas.drawPath(path, this.strokePaint);
                    }
                    k++;
                }
                if (this.curDrawingIndex < this.strokePaths.size()) {
                    if (paramCanvas != null) {
                        k = paramCanvas.saveLayer(0.0F, 0.0F, this.rectSize, this.rectSize, null, Canvas.ALL_SAVE_FLAG);
                    } else {
                        k = 0;
                    }
                    this.strokePaint.setColor(showMedian ? this.strokeColor : this.touchColor);
                    this.curStrokePath = (Path) this.strokePaths.get(this.curDrawingIndex);
                    Path path = this.curStrokePath;
                    if (path != null && paramCanvas != null) {
                        paramCanvas.drawPath(path, this.strokePaint);
                    }
                    if (showMedian) {
                        path = this.curMedianPath;
                        if (path != null) {
                            if (paramCanvas != null) {
                                paramCanvas.drawPath(path, this.medianPaint);
                            }
                            drawArrow(paramCanvas, path);
                        }
                    }
                    path = this.curDrawPath;
                    if (path != null && paramCanvas != null) {
                        paramCanvas.drawPath(path, this.touchPaint);
                    }
                    this.curStrokePath = (Path) this.strokePaths.get(this.curDrawingIndex);
                    this.curPathRectF.setEmpty();
                    path = this.curStrokePath;
                    if (path != null) {
                        path.computeBounds(this.curPathRectF, true);
                    }
                    this.curPathRegion.setEmpty();
                    this.curClipRegion.set((int) this.curPathRectF.left, (int) this.curPathRectF.top, (int) this.curPathRectF.right, (int) this.curPathRectF.bottom);
                    this.curPathRegion.setPath(this.curStrokePath, this.curClipRegion);
                    if (paramCanvas != null) {
                        paramCanvas.restoreToCount(k);
                    }
                }
                i = this.curDrawingIndex;
                for (k = j; k < i; k++) {
                    this.curStrokePath = (Path) this.strokePaths.get(k);
                    this.strokePaint.setColor(showMedian ? this.touchColor : this.strokeColor);
                    Path path = this.curStrokePath;
                    if (path != null && paramCanvas != null) {
                        paramCanvas.drawPath(path, this.strokePaint);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int paramInt1, int paramInt2) {
        super.onMeasure(paramInt1, paramInt2);
        View.MeasureSpec.getMode(paramInt1);
        int i = View.MeasureSpec.getSize(paramInt1);
        int j = View.MeasureSpec.getMode(paramInt2);
        paramInt1 = View.MeasureSpec.getSize(paramInt2);
        if (j == Integer.MIN_VALUE) {
            paramInt1 = i;
        }
        this.rectSize = Math.min(i, paramInt1);
        if (this.autoDraw) {
            this.touchWidth = this.rectSize * 1.0F / 8;
        } else {
            this.touchWidth = this.rectSize * 1.0F / 6;
        }
        this.maskRadius = this.rectSize * 1.0F / 30;
        this.touchPaint.setStrokeWidth(this.touchWidth);
        RectF rectF = this.gridRect;
        rectF.left = (this.gridWidth / 2);
        rectF.top = (this.gridWidth / 2);
        rectF.right = (this.rectSize - this.gridWidth / 2);
        rectF.bottom = (this.rectSize - this.gridWidth / 2);
        setMeasuredDimension(this.rectSize, this.rectSize);
    }


    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE && this.curDrawingIndex < this.strokePaths.size()) {
            this.animateHandler.sendMessageDelayed(Message.obtain((Handler) this.animateHandler, 1), 100);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (autoDraw) {
            return super.onTouchEvent(event);
        }
        boolean bool = false;
        Path path1;
        this.lastX = this.downX;
        this.lastY = this.downY;
        this.downX = event.getX();
        this.downY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:


                if (this.curDrawPath != null) {
                    this.curDrawPath.reset();
                }
                if (inStartPointRange(this.downX, this.downY)) {
                    Log.d(this.TAG, "开始，在起点范围内");
                    this.curDrawingPathValid = true;
                    this.midDrawingValid = true;
                    this.curWrongTouchCount = 0;
                    if (this.curDrawPath != null) {
                        this.curDrawPath.moveTo(this.downX, this.downY);
                        return true;
                    }
                } else {
                    this.curDrawingPathValid = false;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (this.curDrawingPathValid) {
                    if (this.midDrawingValid) {
                        if (!inMidPointRange(event.getX(), event.getY())) {
                            if (this.curWrongTouchCount < this.maxWrongTouch) {
                                this.curWrongTouchCount++;
                                Log.e("xzwzz", "onTouchEvent: 滑动中，不在中线，但没超过中线阈值，当前阈值为" + this.curWrongTouchCount);
                            } else {
                                Log.d(this.TAG, "滑动中，不在中线，已超过阈值");
                                this.midDrawingValid = false;
                            }
                        }
                        bool = true;
                    } else {
                        Log.d(this.TAG, "滑动中，不在中线，忽略");
                    }
                } else {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (this.curDrawingPathValid && this.midDrawingValid && inEndPointRange(event.getX(), event.getY())) {
                    Log.d(this.TAG, "结束，在末点范围内，开始下一划");
                    this.curDrawingIndex++;
                    if (this.curDrawingIndex > this.strokePaths.size()) {
                        this.curDrawingIndex = this.strokePaths.size() - 1;
                    } else {
                        if (this.curDrawPath != null) {
                            this.preDrawPaths.add(this.curDrawPath);
                        }
                        if (this.curDrawPath != null) {
                            this.curDrawPath.reset();
                        }
                        if (this.curDrawingIndex < this.strokePaths.size() && this.curDrawingIndex < this.medianPaths.size()) {
                            this.curMedianPath = (Path) this.medianPaths.get(this.curDrawingIndex);
                            PathMeasure pathMeasure = this.curDrawPathMeasure;
                            if (pathMeasure != null) {
                                pathMeasure.setPath(this.curMedianPath, false);
                            }
                            StrokeDrawListener strokeDrawListener = this.strokeDrawCompleteListener;
                            if (strokeDrawListener != null) {
                                strokeDrawListener.onStrokeStartDraw(this.curDrawingIndex);
                            }
                        } else {
                            StrokeDrawListener strokeDrawListener = this.strokeDrawCompleteListener;
                            if (strokeDrawListener != null) {
                                strokeDrawListener.onStrokeDrawComplete();
                            }
                        }
                    }
                } else {
                    Log.d(this.TAG, "结束，不在末点范围内，清除");
                    if (this.curDrawPath != null) {
                        this.curDrawPath.reset();
                    }
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
                if (this.curDrawPath != null) {
                    this.curDrawPath.reset();
                }
                break;
            default:
                break;
        }
        invalidate();
        this.midDrawingValid = bool;
        return true;
    }

    public final void redraw(Boolean paramBoolean) {
        this.preDrawPaths.clear();
        if (paramBoolean) {
            clear();
        }
        this.animateHandler.removeMessages(1);
        postDelayed(new ChineseCharacterView$redraw$1(), 300);
    }

    public void setAutoDraw(boolean autoDraw) {
        this.autoDraw = autoDraw;
    }

    public final ChineseCharacterView setMedianOriPaths(List<String> paramList) {
        this.medianOriPaths = paramList;
        return this;
    }

    public final ChineseCharacterView setMedianPaths(String src) {
        this.pathPoints = parseMedianData(src);
        return this;
    }

    public final ChineseCharacterView setNeedShift(boolean paramBoolean) {
        this.needShift = paramBoolean;
        return this;
    }

    public final ChineseCharacterView setStrokeDrawListener(StrokeDrawListener paramStrokeDrawListener) {
        this.strokeDrawCompleteListener = paramStrokeDrawListener;
        return this;
    }

    public final ChineseCharacterView setStrokeInfo(String src) {
        this.strokeInfo = parseStrokeData(src);
        return this;
    }

    public interface StrokeDrawListener {
        void onStrokeDrawComplete();

        void onStrokeStartDraw(int param1Int);
    }

    class TimerHandler extends Handler {
        private final WeakReference<ChineseCharacterView> characterView;

        private final int gap = dp2px(4.0F);

        public TimerHandler(ChineseCharacterView param1ChineseCharacterView) {
            this.characterView = new WeakReference(param1ChineseCharacterView);
        }

        @Override
        public void handleMessage(Message param1Message) {
            WeakReference weakReference = this.characterView;
            if (weakReference != null) {
                ChineseCharacterView chineseCharacterView = (ChineseCharacterView) weakReference.get();
                if (chineseCharacterView != null) {
                    if (!chineseCharacterView.isPause) {
                        if (chineseCharacterView.isStop) {
                            return;
                        }
                        if (chineseCharacterView.animateValue == 0.0F) {
                            ChineseCharacterView.StrokeDrawListener strokeDrawListener = chineseCharacterView.strokeDrawCompleteListener;
                            if (strokeDrawListener != null) {
                                strokeDrawListener.onStrokeStartDraw(chineseCharacterView.curDrawingIndex);
                            }
                        }
                        chineseCharacterView.setAnimateValue(chineseCharacterView.animateValue + this.gap);
                        return;
                    }
                    return;
                }
            }
        }
    }

    class ChineseCharacterView$redraw$1 implements Runnable {
        @Override
        public void run() {
            ChineseCharacterView.this.pathMatrix.reset();
            float f2 = (ChineseCharacterView.this.rectSize - ChineseCharacterView.this.fixedSize);
            float f1 = 2;
            f2 = f2 * 1.0F / f1;
            if (ChineseCharacterView.this.needShift) {
                f1 = (ChineseCharacterView.this.rectSize - ChineseCharacterView.this.fixedSize * ChineseCharacterView.this.shiftFactor) * 1.0F / f1;
            } else {
                f1 = f2;
            }
            float f3 = ChineseCharacterView.this.rectSize * 1.0F / ChineseCharacterView.this.fixedSize;
            ChineseCharacterView.this.pathMatrix.postTranslate(f2, f1);
            if (ChineseCharacterView.this.needShift) {
                ChineseCharacterView.this.pathMatrix.postScale(f3, -f3, (ChineseCharacterView.this.rectSize / 2), (ChineseCharacterView.this.rectSize / 2));
            } else {
                ChineseCharacterView.this.pathMatrix.postScale(f3, f3, (ChineseCharacterView.this.rectSize / 2), (ChineseCharacterView.this.rectSize / 2));
            }
            if (strokePaths == null || strokePaths.size() == 0) {
                ChineseCharacterView.this.initStrokePaths();
            }
            if (medianPaths == null || medianPaths.size() == 0) {
                ChineseCharacterView.this.initMedianPaths();
            }
            if (!ChineseCharacterView.this.strokePaths.isEmpty()) {
                if (ChineseCharacterView.this.medianPaths.isEmpty()) {
                    return;
                }
                ChineseCharacterView.this.curDrawingIndex = 0;
                if (ChineseCharacterView.this.autoDraw) {
                    ChineseCharacterView.this.curDrawPath = new Path();
                    ChineseCharacterView.this.curMedianPath = (Path) ChineseCharacterView.this.medianPaths.get(ChineseCharacterView.this.curDrawingIndex);
                    ChineseCharacterView.this.curDrawPathMeasure = new PathMeasure(ChineseCharacterView.this.curMedianPath, false);
                    ChineseCharacterView.this.setAnimateValue(0.0F);
                    return;
                }
                ChineseCharacterView.this.curDrawPath = new Path();
                ChineseCharacterView.this.curMedianPath = (Path) ChineseCharacterView.this.medianPaths.get(ChineseCharacterView.this.curDrawingIndex);
                ChineseCharacterView.this.curDrawPathMeasure = new PathMeasure(ChineseCharacterView.this.curMedianPath, false);
                ChineseCharacterView.StrokeDrawListener strokeDrawListener = ChineseCharacterView.this.strokeDrawCompleteListener;
                if (strokeDrawListener != null) {
                    strokeDrawListener.onStrokeStartDraw(ChineseCharacterView.this.curDrawingIndex);
                }
                ChineseCharacterView.this.invalidate();
            }
            return;
        }
    }

    private void initMedianPaths() {
        if (pathPoints != null) {
            medianPaths.clear();
            for (List<? extends PointF> pathPoint : pathPoints) {
                if (pathPoint != null || pathPoint.size() != 0) {
                    Path path = new Path();
                    path.moveTo(pathPoint.get(0).x, pathPoint.get(0).y);
                    for (int i = 0; i < pathPoint.size() - 1; i++) {
                        try {
                            path.quadTo(pathPoint.get(i).x, pathPoint.get(i).y, pathPoint.get(i + 1).x, pathPoint.get(i + 1).y);
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    path.transform(this.pathMatrix);
                    medianPaths.add(path);
                }
            }
        } else {
            medianPaths.clear();
            for (String path : medianOriPaths) {
                Path path1 = parser(path);
                path1.transform(this.pathMatrix);
                medianPaths.add(path1);

            }
        }
    }


    private List<List<PointF>> parseMedianData(String paramString) {
        boolean bool;
        CharSequence charSequence = (CharSequence) paramString;
        if (charSequence == null || charSequence.length() == 0) {
            bool = true;
        } else {
            bool = false;
        }
        if (bool) {
            return null;
        }
        List<List> stringList = JsonUtil.parseArray(paramString, List.class);
        List<List<PointF>> result = new ArrayList<>();
        for (List s : stringList) {
            List<List> list = JsonUtil.parseArray(s.toString(), List.class);
            List<PointF> pointFS = new ArrayList<>();
            for (List slll : list) {
                List<Number> numberList = JsonUtil.parseArray(slll.toString(), Number.class);
                PointF f = new PointF(numberList.get(0).floatValue(), numberList.get(1).floatValue());
                pointFS.add(f);
            }
            result.add(pointFS);
        }
        return result;
    }


    private List<String> parseOriMedianData(String paramString) {
        boolean bool;
        CharSequence charSequence = (CharSequence) paramString;
        if (charSequence == null || charSequence.length() == 0) {
            bool = true;
        } else {
            bool = false;
        }
        return bool ? null : JsonUtil.parseArray(paramString, String.class);
    }


    private List<String> parseStrokeData(String paramString) {
        boolean bool;
        List list;
        CharSequence charSequence = (CharSequence) paramString;
        if (charSequence == null || charSequence.length() == 0) {
            bool = true;
        } else {
            bool = false;
        }
        if (bool) {
            list = new ArrayList();
            return list;
        }
        return JsonUtil.parseArray(paramString, String.class);
    }

    public static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}