package com.xzwzz.writecharacter

import android.app.Application
import android.content.Context
import com.xzwzz.writecharacter.util.FontStrokeUtil

/**
 *  @time 2019-09-10
 *  @author xzwzz
 *  @package com.xzwzz.writecharacter
 */
class AppContext : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        FontStrokeUtil.getInstance().init();
    }

    companion object {
        lateinit var context: Context
    }
}