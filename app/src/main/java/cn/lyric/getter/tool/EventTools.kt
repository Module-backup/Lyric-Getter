package cn.lyric.getter.tool

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import cn.lyric.getter.api.data.ExtraData
import cn.lyric.getter.api.data.LyricData
import cn.lyric.getter.api.data.type.OperateType
import cn.lyric.getter.tool.ConfigTools.xConfig
import cn.xiaowine.xkt.Tool.isNull
import cn.xiaowine.xkt.Tool.observableChange
import cn.xiaowine.xkt.Tool.regexReplace

@SuppressLint("StaticFieldLeak")
class EventTools(val context: Context) {
    private var lastLyricData: LyricData? by observableChange(null) { _, _, newValue ->
        newValue?.run {
            val regexReplace = lyric.regexReplace(xConfig.regexReplace, "")
            if (regexReplace.isEmpty()) {
                cleanLyric()
            } else {
                context.sendBroadcast(Intent().apply {
                    action = "Lyric_Data"
                    putExtra("Data", newValue.apply {
                        lyric = regexReplace
                    })
                })
                Log.d(TAG, this.toString())
            }
        }
    }


    fun sendLyric(lyric: String, extra: ExtraData? = null) {
        lastLyricData = LyricData().apply {
            this.type = OperateType.UPDATE
            this.lyric = lyric
            if (extra.isNull()) {
                this.extraData.mergeExtra(ExtraData().apply {
                    this.packageName = context.packageName
                    this.customIcon = false
                    this.base64Icon = ""
                    this.useOwnMusicController = false
                    this.delay = 0
                })
            } else {
                this.extraData.mergeExtra(extra!!)
            }

        }
    }


    fun cleanLyric() {
        context.sendBroadcast(Intent().apply {
            action = "Lyric_Data"
            val lyricData = LyricData().apply {
                this.type = OperateType.STOP
            }
            putExtra("Data", lyricData)
            Log.d(TAG, lyricData.toString())
        })
    }

    companion object {
        private const val TAG = "Lyrics Getter"
    }
}