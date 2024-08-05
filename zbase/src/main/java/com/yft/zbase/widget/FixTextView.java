package com.yft.zbase.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.yft.zbase.R;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class FixTextView extends AppCompatTextView implements View.OnClickListener {
    /**
     * 缓存测量过的数据
     */
    private static HashMap<String, SoftReference<MeasuredData>> measuredData = new HashMap<>();
    private static int hashIndex = 0;
    /**
     * 存储当前文本内容，每个item为一行
     */
    ArrayList<Line> contentList = new ArrayList<>();
    /**
     * 用于测量字符宽度
     */
    private TextPaint mPaint = new TextPaint();
    /**
     * 用于测量span高度
     */
    private Paint.FontMetricsInt mSpanFmInt = new Paint.FontMetricsInt();
    /**
     * 临时使用,以免在onDraw中反复生产新对象
     */
    private Paint.FontMetrics mFontMetrics = new Paint.FontMetrics();

    //行距
    private float lineSpacing;

    private boolean isClick;
    /**
     * 只有一行时的宽度
     */
    private int oneLineWidth = -1;
    /**
     * 已绘的行中最宽的一行的宽度
     */
    private float lineWidthMax = -1;
    /**
     * 存储当前文本内容,每个item为一个字符或者一个SpanObject
     */
    private ArrayList<Object> obList = new ArrayList<>();
    /**
     * 是否使用默认{@link #onMeasure(int, int)}和{@link #onDraw(android.graphics.Canvas)}
     */
    private boolean useDefault;

    /**
     * 源文本
     */
    protected CharSequence sourceText = "";
    /**
     * 收起状态时的行数 默认2行
     */
    private int mFoldLines;
    /**
     * 是否开启更多-收起 模式的开关，默认打开
     */
    private boolean isAutoFold;
    private int state = STATE_FOLD;
    public static final int STATE_FOLD = 1;
    public static final int STATE_UNFOLD = 2;

    /**
     * 用以获取屏幕高宽
     */
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    /**
     * {@link android.text.style.BackgroundColorSpan}用
     */
    private Paint textBgColorPaint = new Paint();
    /**
     * {@link android.text.style.BackgroundColorSpan}用
     */
    private Rect textBgColorRect = new Rect();
    /**
     * 收起文字
     */
    private String mUnFoldText;
    private int mUnFoldTextColor;
    /**
     * 是否执行收起的操作
     */
    private boolean isUnFold;
    /**
     * 展开文字
     */
    private String mFoldText = "...更多";
    private int mFoldTextColor;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public FixTextView(Context context) {
        this(context, null);
    }

    public FixTextView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FixTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FixTextView);
        mFoldLines = ta.getInteger(R.styleable.FixTextView_foldLines, 2);
        isAutoFold = ta.getBoolean(R.styleable.FixTextView_isAutoFold, true);
        mFoldText = ta.getString(R.styleable.FixTextView_foldText);
        mFoldTextColor = ta.getColor(R.styleable.FixTextView_foldTextColor, Color.BLUE);
        mUnFoldText = ta.getString(R.styleable.FixTextView_unFoldText);
        mUnFoldTextColor = ta.getColor(R.styleable.FixTextView_unFoldTextColor, Color.BLUE);
        isUnFold = ta.getBoolean(R.styleable.FixTextView_isUnFold, true);
        useDefault = ta.getBoolean(R.styleable.FixTextView_useDefault, false);
        sourceText = ta.getString(R.styleable.FixTextView_sourceText);
        isClick = ta.getBoolean(R.styleable.FixTextView_isClick, false);
        lineSpacing = ta.getDimension(R.styleable.FixTextView_lineSpace, dip2px(context, 5));
        ta.recycle();
        init();
    }

    private void init() {
        mPaint.setAntiAlias(true);
        if (!TextUtils.isEmpty(sourceText)) {
            setSourceText(sourceText);
        }
        if (isClick) {
            setOnClickListener(this);
        }
    }


    public void setUnFoldText(String unFoldText) {
        mUnFoldText = unFoldText;
        requestLayout();
    }

    public void setFoldText(String foldText) {
        mFoldText = foldText;
        requestLayout();
    }

    @Override
    public void onClick(View v) {
        if (!useDefault && isAutoFold) {
            int wantState = state == STATE_FOLD ? STATE_UNFOLD : isUnFold ? STATE_FOLD : STATE_UNFOLD;
            if (state != wantState) {
                state = wantState;
                requestLayout();
            }
        }
    }

    private class SpanObject {
        private Object span;
        private int start;
        private int end;
        private CharSequence source;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (useDefault) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int width = 0, height = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                width = widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                width = displayMetrics.widthPixels;
                break;
            default:
                break;
        }
        mPaint.setTextSize(this.getTextSize());
        mPaint.setColor(getCurrentTextColor());
        int realHeight = measureContentHeight(width);
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = realHeight;
                break;
            case MeasureSpec.UNSPECIFIED:
                height = realHeight;
                break;
            default:
                break;
        }
        height += getCompoundPaddingTop() + getCompoundPaddingBottom();
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (useDefault) {
            super.onDraw(canvas);
            return;
        }
        if (contentList.isEmpty()) {
            return;
        }
        int leftPadding = getCompoundPaddingLeft();
        int topPadding = getCompoundPaddingTop();
        float height = 0 + topPadding + lineSpacing;
        //只有一行时
        if (oneLineWidth != -1) {
            height = getMeasuredHeight() / 2 - contentList.get(0).height / 2;
        }
        for (int i = 0; i < contentList.size(); i++) {
            Line aContentList = contentList.get(i);
            if (isAutoFold && state == STATE_FOLD && i == mFoldLines - 1) {
                aContentList = generateFoldLine(aContentList);
            }
            if (isAutoFold && state == STATE_FOLD && i >= mFoldLines) {
                return;
            }
            boolean newParagraph = drawLine(canvas, leftPadding, height, aContentList);
            //如果要绘制段间距
            if (newParagraph) {
                height += aContentList.height + lineSpacing;
            } else {
                height += aContentList.height + lineSpacing;
            }
        }

    }

    private boolean drawLine(Canvas canvas, int leftPadding, float height, Line aContentList) {
        Object ob;
        int width;//绘制一行
        float realDrawedWidth = leftPadding;
        /** 是否换新段落*/
        boolean newParagraph = false;
        for (int j = 0; j < aContentList.line.size(); j++) {
            ob = aContentList.line.get(j);
            width = aContentList.widthList.get(j);
            mPaint.getFontMetrics(mFontMetrics);
            float x = realDrawedWidth;
            float y = height + aContentList.height - mPaint.getFontMetrics().descent;
            float top = y - aContentList.height;
            float bottom = y + mFontMetrics.descent;
            if (ob instanceof String) {
                mPaint.setColor(getCurrentTextColor());
                canvas.drawText((String) ob, realDrawedWidth, y, mPaint);
                realDrawedWidth += width;
                if (((String) ob).endsWith("\n") && j == aContentList.line.size() - 1) {
                    newParagraph = true;
                }
            } else if (ob instanceof SpanObject) {
                Object span = ((SpanObject) ob).span;
                if (span instanceof DynamicDrawableSpan) {
                    int start = ((Spannable) sourceText).getSpanStart(span);
                    int end = ((Spannable) sourceText).getSpanEnd(span);
                    ((DynamicDrawableSpan) span).draw(canvas, sourceText, start, end, (int) x, (int) top, (int) y, (int) bottom, mPaint);
                    realDrawedWidth += width;
                } else if (span instanceof BackgroundColorSpan) {
                    textBgColorPaint.setColor(((BackgroundColorSpan) span).getBackgroundColor());
                    textBgColorPaint.setStyle(Paint.Style.FILL);
                    textBgColorRect.left = (int) realDrawedWidth;
                    int textHeight = (int) getTextSize();
                    textBgColorRect.top = (int) (height + aContentList.height - textHeight - mFontMetrics.descent);
                    textBgColorRect.right = textBgColorRect.left + width;
                    textBgColorRect.bottom = (int) (height + aContentList.height + lineSpacing - mFontMetrics.descent);
                    canvas.drawRect(textBgColorRect, textBgColorPaint);
                    canvas.drawText(((SpanObject) ob).source.toString(), realDrawedWidth, height + aContentList.height - mFontMetrics.descent, mPaint);
                    realDrawedWidth += width;
                } else if (span instanceof ForegroundColorSpan) {
                    mPaint.setColor(((ForegroundColorSpan) span).getForegroundColor());
                    canvas.drawText(((SpanObject) ob).source.toString(), realDrawedWidth, height + aContentList.height - mFontMetrics.descent, mPaint);
                    realDrawedWidth += width;
                } else {//做字符串处理
                    mPaint.setColor(getCurrentTextColor());
                    canvas.drawText(((SpanObject) ob).source.toString(), realDrawedWidth, height + aContentList.height - mFontMetrics.descent, mPaint);
                    realDrawedWidth += width;
                }
            }
        }
        return newParagraph;
    }


    /**
     * 根据要显示的最后一行来初始化含有end标识的一行数据
     * line中的spans 最后一个如果不能替换掉end，则截取能装下的最小宽度，然后拼装起来返回
     *
     * @param line 待处理的fold源line
     * @return 已处理的fold line
     */
    private Line generateFoldLine(Line line) {
        if (TextUtils.isEmpty(mFoldText)) {
            return line;
        }
        Line endLine = new Line();
        endLine.height = line.height;
        endLine.widthList = (ArrayList<Integer>) line.widthList.clone();
        endLine.line = (ArrayList<Object>) line.line.clone();
        int endWidth = (int) mPaint.measureText(mFoldText);
        for (int i = 0; i < endLine.line.size(); i++) {
            if (endLine.getEndWidth(i) + endWidth >= endLine.getWidth()) {
                Object o = endLine.line.get(i);
                if (o instanceof String) {
                    String content = (String) o;
                    int endIndex = content.length() - 1;
                    int realWidth = getWidth() - getCompoundPaddingLeft() - getCompoundPaddingRight();
                    int measureWidth = (int) mPaint.measureText(content) + (i - 1 >= 0 ? endLine.getEndWidth(i - 1) : 0);
                    boolean flag = false;
                    while (measureWidth + endWidth > realWidth && endIndex > 0) {
                        flag = true;
                        content = content.substring(0, --endIndex);
                        measureWidth = (int) mPaint.measureText(content) + (i - 1 >= 0 ? endLine.getEndWidth(i - 1) : 0);
                    }
                    if (flag) {
                        //到这里已经能确保content和endWidth的宽度正好不超过控件的宽度
                        endLine.line.set(i, content);
                        endLine.widthList.set(i, (int) mPaint.measureText(content));
                        SpanObject spanObject = new SpanObject();
                        spanObject.source = mFoldText;
                        spanObject.span = new ForegroundColorSpan(mFoldTextColor);
                        endLine.line.add(spanObject);
                        endLine.widthList.add(endWidth);
                    }
                } else if (o instanceof SpanObject) {
                    SpanObject spanObject = (SpanObject) o;
                    if (spanObject.span instanceof DynamicDrawableSpan) {
                        //直接替换掉
                        endLine.line.remove(i);
                        endLine.widthList.remove(i);
                        SpanObject endSpanObject = new SpanObject();
                        endSpanObject.source = mFoldText;
                        endSpanObject.span = new ForegroundColorSpan(mFoldTextColor);
                        endLine.line.add(endSpanObject);
                        endLine.widthList.add(endWidth);
                    } else if (spanObject.span instanceof BackgroundColorSpan || spanObject.span instanceof ForegroundColorSpan) {
                        String source = (String) spanObject.source;
                        int endIndex = source.length() - 1;
                        int measureWidth = (int) mPaint.measureText(source) + (i - 1 >= 0 ? endLine.getEndWidth(i - 1) : 0);
                        int realWidth = getWidth() - getCompoundPaddingLeft() - getCompoundPaddingRight();
                        boolean flag = false;
                        while (measureWidth + endWidth > realWidth && endIndex > 0) {
                            flag = true;
                            source = source.substring(0, --endIndex);
                            measureWidth = (int) mPaint.measureText(source) + (i - 1 >= 0 ? endLine.getEndWidth(i - 1) : 0);
                        }
                        if (flag) {
                            spanObject.source = source;
                            spanObject.end = spanObject.start + endIndex;
                            endLine.line.set(i, spanObject);
                            endLine.widthList.set(i, (int) mPaint.measureText(source));
                            SpanObject endSpanObject = new SpanObject();
                            endSpanObject.source = mFoldText;
                            endSpanObject.span = new ForegroundColorSpan(mFoldTextColor);
                            endLine.line.add(endSpanObject);
                            endLine.widthList.add(endWidth);
                        }
                    }
                }
                break;
            }
        }
        return endLine;
    }

    /**
     * 设置源文本
     *
     * @param cs
     */
    public void setSourceText(CharSequence cs) {
        if (useDefault) {
            setText(cs);
        } else {
            if (TextUtils.isEmpty(cs)) {return;}
            sourceText = cs;
            generateSpanData(cs);
            requestLayout();
        }
    }

    /**
     * 根据文本初始化Spans对象
     *
     * @param cs 源文本
     */
    private void generateSpanData(CharSequence cs) {
        obList.clear();
        ArrayList<SpanObject> isList = new ArrayList<>();
        if (cs instanceof Spannable) {
            CharacterStyle[] spans = ((Spannable) cs).getSpans(0, cs.length(), CharacterStyle.class);
            for (int i = 0; i < spans.length; i++) {
                int s = ((Spannable) cs).getSpanStart(spans[i]);
                int e = ((Spannable) cs).getSpanEnd(spans[i]);
                SpanObject iS = new SpanObject();
                iS.span = spans[i];
                iS.start = s;
                iS.end = e;
                iS.source = cs.subSequence(s, e);
                isList.add(iS);
            }
        }
        //对span进行排序，以免不同种类的span位置错乱
        SpanObject[] spanArray = new SpanObject[isList.size()];
        isList.toArray(spanArray);
        Arrays.sort(spanArray, 0, spanArray.length, new SpanObjectComparator());
        isList.clear();
        for (SpanObject aSpanArray : spanArray) {
            isList.add(aSpanArray);
        }
        String str = cs.toString();
        for (int i = 0, j = 0; i < cs.length(); ) {
            if (j < isList.size()) {
                SpanObject is = isList.get(j);
                if (i < is.start) {
                    Integer cp = str.codePointAt(i);
                    //支持增补字符
                    if (Character.isSupplementaryCodePoint(cp)) {
                        i += 2;
                    } else {
                        i++;
                    }
                    obList.add(new String(Character.toChars(cp)));
                } else if (i >= is.start) {
                    obList.add(is);
                    j++;
                    i = is.end;
                }
            } else {
                Integer cp = str.codePointAt(i);
                if (Character.isSupplementaryCodePoint(cp)) {
                    i += 2;
                } else {
                    i++;
                }
                obList.add(new String(Character.toChars(cp)));
            }
        }
    }

    public int getLineCount() {
        if (contentList != null) {
            return Math.max(1, contentList.size());
        }
        return 1;
    }

    /**
     * 用于带ImageSpan的文本内容所占高度测量
     *
     * @param width 预定的宽度
     * @return 所需的高度
     */
    private int measureContentHeight(int width) {
        if (TextUtils.isEmpty(sourceText)) {return 0;}
        int nowLineNum = 0;
        int cachedHeight = getCachedData(sourceText.toString(), width);
        if (cachedHeight > 0) {
            return cachedHeight;
        }
        // 已绘的宽度
        float obWidth = 0;
        float obHeight = 0;
        float textSize = this.getTextSize();
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        //行高
        float lineHeight = fontMetrics.bottom - fontMetrics.top;
        //计算出的所需高度
        float height = lineSpacing;
        int leftPadding = getCompoundPaddingLeft();
        int rightPadding = getCompoundPaddingRight();
        float drawedWidth = 0;
        boolean splitFlag = false;//BackgroundColorSpan拆分用
        width = width - leftPadding - rightPadding;
        oneLineWidth = -1;
        contentList.clear();
        StringBuilder sb;
        Line line = new Line();
        for (int i = 0; i < obList.size(); i++) {
            Object ob = obList.get(i);
            if (ob instanceof String) {
                obWidth = mPaint.measureText((String) ob);
                obHeight = textSize;
                if ("\n".equals(ob)) { //遇到"\n"则换行
                    obWidth = width - drawedWidth;
                }
            } else if (ob instanceof SpanObject) {
                Object span = ((SpanObject) ob).span;
                if (span instanceof DynamicDrawableSpan) {
                    int start = ((Spannable) sourceText).getSpanStart(span);
                    int end = ((Spannable) sourceText).getSpanEnd(span);
                    obWidth = ((DynamicDrawableSpan) span).getSize(getPaint(), sourceText, start, end, mSpanFmInt);
                    obHeight = Math.abs(mSpanFmInt.top) + Math.abs(mSpanFmInt.bottom);
                    if (obHeight > lineHeight) {
                        lineHeight = obHeight;
                    }
                } else if (span instanceof BackgroundColorSpan) {
                    String str = ((SpanObject) ob).source.toString();
                    obWidth = mPaint.measureText(str);
                    obHeight = textSize;
                    //如果太长,拆分
                    int k = str.length() - 1;
                    while (width - drawedWidth < obWidth) {
                        obWidth = mPaint.measureText(str.substring(0, k--));
                    }
                    if (k < str.length() - 1) {
                        splitFlag = true;
                        SpanObject so1 = new SpanObject();
                        so1.start = ((SpanObject) ob).start;
                        so1.end = so1.start + k;
                        so1.source = str.substring(0, k + 1);
                        so1.span = ((SpanObject) ob).span;
                        SpanObject so2 = new SpanObject();
                        so2.start = so1.end;
                        so2.end = ((SpanObject) ob).end;
                        so2.source = str.substring(k + 1);
                        so2.span = ((SpanObject) ob).span;
                        ob = so1;
                        obList.set(i, so2);
                        i--;
                    }
                }//做字符串处理
                else {
                    String str = ((SpanObject) ob).source.toString();
                    obWidth = mPaint.measureText(str);
                    obHeight = textSize;
                }
            }
            //这一行满了，存入contentList,新起一行
            if (width - drawedWidth < obWidth || splitFlag) {
                splitFlag = false;
                contentList.add(line);
                if (drawedWidth > lineWidthMax) {
                    lineWidthMax = drawedWidth;
                }
                drawedWidth = 0;
                //判断是否有分段
                int objNum = line.line.size();
                if (lineSpacing > 0
                        && objNum > 0
                        && line.line.get(objNum - 1) instanceof String
                        && "\n".equals(line.line.get(objNum - 1))) {
                    nowLineNum++;
                    if (!isAutoFold || (state == STATE_UNFOLD) || (state == STATE_FOLD && nowLineNum <= mFoldLines)) {
                        height += line.height + lineSpacing;
                    }

                } else {
                    nowLineNum++;
                    if (!isAutoFold || (state == STATE_UNFOLD) || (state == STATE_FOLD && nowLineNum <= mFoldLines)) {
                        height += line.height + lineSpacing;
                    }
                }
                lineHeight = obHeight;
                line = new Line();
            }
            drawedWidth += obWidth;
            if (ob instanceof String && line.line.size() > 0 && (line.line.get(line.line.size() - 1) instanceof String)) {
                int size = line.line.size();
                sb = new StringBuilder();
                sb.append(line.line.get(size - 1));
                sb.append(ob);
                ob = sb.toString();
                obWidth = obWidth + line.widthList.get(size - 1);
                line.line.set(size - 1, ob);
                line.widthList.set(size - 1, (int) obWidth);
                line.height = (int) lineHeight;
            } else {
                line.line.add(ob);
                line.widthList.add((int) obWidth);
                line.height = (int) lineHeight;
            }
        }

        if (drawedWidth > lineWidthMax) {
            lineWidthMax = drawedWidth;
        }

        if (line.line.size() > 0) {
            contentList.add(line);
            if (!isAutoFold || (state == STATE_UNFOLD) || (state == STATE_FOLD && contentList.size() <= mFoldLines)) {
                height += lineHeight + lineSpacing;
            }
        }
        if (contentList.size() <= 1) {
            oneLineWidth = (int) drawedWidth + leftPadding + rightPadding;
            if (mFoldLines > 1) {
                height = lineSpacing + lineHeight + lineSpacing;
            }
        }
        if (state == STATE_UNFOLD && contentList.size() > mFoldLines && !TextUtils.isEmpty(mUnFoldText) && isUnFold) {
            int lastLineIndex = contentList.size() - 1;
            if (lastLineIndex >= 0) {
                int unFoldWidth = (int) mPaint.measureText(mUnFoldText);
                Line tmLine = contentList.get(lastLineIndex);
                if (tmLine.getWidth() + unFoldWidth > getWidth()) {
                    SpanObject spanObject = new SpanObject();
                    spanObject.source = mUnFoldText;
                    spanObject.span = new ForegroundColorSpan(mUnFoldTextColor);
                    spanObject.start = 0;
                    spanObject.end = spanObject.source.length();
                    Line unFoldLine = new Line();
                    unFoldLine.line.add(spanObject);
                    unFoldLine.widthList.add(unFoldWidth);
                    unFoldLine.height = lineHeight;
                    contentList.add(unFoldLine);
                    height += line.height + lineSpacing;
                } else {
                    SpanObject spanObject = new SpanObject();
                    spanObject.source = mUnFoldText;
                    spanObject.start = 0;
                    spanObject.end = spanObject.source.length();
                    spanObject.span = new ForegroundColorSpan(mUnFoldTextColor);
                    tmLine.line.add(spanObject);
                    tmLine.widthList.add(unFoldWidth);
                }
            }
        }

        cacheData(width, (int) height);
        return (int) height;
    }

    private int getCachedData(String text, int width) {
        SoftReference<MeasuredData> cache = measuredData.get(text);
        if (cache == null) {
            return -1;
        }
        MeasuredData md = cache.get();
        if (md != null && md.textSize == this.getTextSize() && width == md.width) {
            lineWidthMax = md.lineWidthMax;
            contentList = (ArrayList<Line>) md.contentList.clone();
            oneLineWidth = md.oneLineWidth;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < contentList.size(); i++) {
                Line line = contentList.get(i);
                sb.append(line.toString());
            }
            return md.measuredHeight;
        } else {
            return -1;
        }
    }

    /**
     * 缓存已测量的数据
     *
     * @param width
     * @param height
     */
    @SuppressWarnings("unchecked")
    private void cacheData(int width, int height) {
        MeasuredData md = new MeasuredData();
        md.contentList = (ArrayList<Line>) contentList.clone();
        md.textSize = this.getTextSize();
        md.lineWidthMax = lineWidthMax;
        md.oneLineWidth = oneLineWidth;
        md.measuredHeight = height;
        md.width = width;
        md.hashIndex = ++hashIndex;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < contentList.size(); i++) {
            Line line = contentList.get(i);
            sb.append(line.toString());
        }
        SoftReference<MeasuredData> cache = new SoftReference<MeasuredData>(md);
        measuredData.put(sourceText.toString(), cache);
    }

    private static class SpanObjectComparator implements Comparator<SpanObject> {
        @Override
        public int compare(SpanObject lhs, SpanObject rhs) {
            return lhs.start - rhs.start;
        }
    }

    private static class MeasuredData {
        private int measuredHeight;
        private float textSize;
        private int width;
        private float lineWidthMax;
        private int oneLineWidth;
        private int hashIndex;
        private ArrayList<Line> contentList;
    }

    private class Line {
        private ArrayList<Object> line = new ArrayList<Object>();
        private ArrayList<Integer> widthList = new ArrayList<Integer>();
        private float height;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("height:" + height + "   ");
            for (int i = 0; i < line.size(); i++) {
                sb.append(line.get(i) + ":" + widthList.get(i));
            }
            return sb.toString();
        }

        private int getWidth() {
            int width = 0;
            for (Integer integer : widthList) {
                width += integer;
            }
            return width;
        }

        private int getEndWidth(int endIndex) {
            int width = 0;
            for (int i = 0; i < widthList.size(); i++) {
                if (i <= endIndex) {
                    width += widthList.get(i);
                }
            }
            return width;
        }
    }
}
