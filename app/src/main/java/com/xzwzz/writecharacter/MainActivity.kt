package com.xzwzz.writecharacter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.xzwzz.writecharacter.util.FontStrokeUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var autoDraw = false;
    private var showMedian = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_character.setOnClickListener {
            search(et_character.text.toString())
        }
        btn_auto.setOnClickListener {
            autoDraw = !autoDraw
            mChineseCharacterView.setAutoDraw(autoDraw)
            mChineseCharacterView.redraw(false)
            btn_auto.text = "自动绘制$autoDraw"
        }
        btn_median.setOnClickListener {
            showMedian = !showMedian
            mChineseCharacterView.setShowMedian(showMedian)
            mChineseCharacterView.redraw(false)
            btn_median.text = "显示中线$showMedian"
        }
        btn_auto.text = "自动绘制$autoDraw"
        btn_median.text = "显示中线$showMedian"
    }

    private fun search(text: String) {
        if (text.length > 1) {
            Toast.makeText(this, "只能查询单个字", Toast.LENGTH_SHORT).show()
            return
        }
        val bean = FontStrokeUtil.getInstance().query(text)
        mChineseCharacterView.setStrokeInfo(bean.getStrokes()).setMedianPaths(bean.getMedians())
        mChineseCharacterView.redraw(true)
    }
}
