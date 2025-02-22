package cn.lyric.getter.hook

import cn.lyric.getter.BuildConfig
import cn.lyric.getter.tool.ConfigTools.xConfig
import cn.lyric.getter.config.Config
import cn.lyric.getter.tool.HookTools
import cn.xiaowine.dsp.DSP
import cn.xiaowine.dsp.data.MODE

abstract class BaseHook {
    var isInit: Boolean = false
    val config: Config by lazy { xConfig }
    open fun init() {
        DSP.init(null, BuildConfig.APPLICATION_ID, MODE.HOOK, true)
    }
}