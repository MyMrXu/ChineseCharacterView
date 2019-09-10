package com.xzwzz.writecharacter.bean;

/**
 * @author xzwzz
 * @time 2019-08-23
 * @package com.hdyj.huibenketang.bean
 */
public class FontStrokeDataBean {
    private String font;
    private String strokes;
    private String medians;
    private int position;

    public int getPosition() {
        return position;
    }

    public FontStrokeDataBean setPosition(int position) {
        this.position = position;
        return this;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getStrokes() {
        return strokes;
    }

    public void setStrokes(String strokes) {
        this.strokes = strokes;
    }

    public String getMedians() {
        return medians;
    }

    public void setMedians(String medians) {
        this.medians = medians;
    }

    public FontStrokeDataBean(String font, String strokes, String medians) {
        this.font = font;
        this.strokes = strokes;
        this.medians = medians;
    }

    @Override
    public String toString() {
        return "FontStrokeDataBean{" +
                "font='" + font + '\'' +
                ", strokes='" + strokes + '\'' +
                ", medians='" + medians + '\'' +
                '}';
    }
}
