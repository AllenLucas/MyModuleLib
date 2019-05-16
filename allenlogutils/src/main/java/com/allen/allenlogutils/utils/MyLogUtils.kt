package com.allen.allenlogutils.utils

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object MyLogUtils {

    private val TOP_LEFT_CORNER = '╔'
    private val BOTTOM_LEFT_CORNER = '╚'
    private val MIDDLE_CORNER = '╟'
    private val HORIZONTAL_DOUBLE_LINE = '║'
    private val DOUBLE_DIVIDER = "════════════════════════════════════════════"
    private val SINGLE_DIVIDER = "────────────────────────────────────────────"
    private val TOP_BORDER = "$TOP_LEFT_CORNER$DOUBLE_DIVIDER$DOUBLE_DIVIDER"
    private val BOTTOM_BORDER = "$BOTTOM_LEFT_CORNER$DOUBLE_DIVIDER$DOUBLE_DIVIDER"
    private val MIDDLE_BORDER = "$MIDDLE_CORNER$SINGLE_DIVIDER$SINGLE_DIVIDER"
    private val MIN_STACK_OFFSET = 3
    private val JSON_INDENT = 2
    private var TAG = "lal"

    private fun getStackOffset(trace: Array<StackTraceElement>): Int {
        var i = MIN_STACK_OFFSET
        while (i < trace.size) {
            val e = trace[i]
            val name = e.className
            if (name != MyLogUtils::class.java.name) {
                return --i
            }
            i++
        }
        return -1
    }

    private fun getMethodNames(): String {
        val sElements = Thread.currentThread().stackTrace

        var stackOffset = getStackOffset(sElements)

        stackOffset++
        val builder = StringBuilder("\t\n")
        builder.append(TOP_BORDER).append("\r\n")
            // 添加当前线程名
            .append("$HORIZONTAL_DOUBLE_LINE " + "Thread: " + Thread.currentThread().name).append("\r\n")
            .append(MIDDLE_BORDER).append("\r\n")
            // 添加类名、方法名、行数
            .append("$HORIZONTAL_DOUBLE_LINE ")
            .append(sElements[stackOffset].className)
            .append(".")
            .append(sElements[stackOffset].methodName)
            .append(" ")
            .append(" (")
            .append(sElements[stackOffset].fileName)
            .append(":")
            .append(sElements[stackOffset].lineNumber)
            .append(")")
            .append("\r\n")
            .append(MIDDLE_BORDER).append("\r\n")
            // 添加打印的日志信息
            .append("$HORIZONTAL_DOUBLE_LINE ").append("%s").append("\r\n")
            .append(BOTTOM_BORDER).append("\r\n")
        return builder.toString()
    }

    fun jsonLog(json: String) {
        var json = json
        if (json.isBlank()) {
            dLog("Empty/Null json content")
            return
        }

        try {
            json = json.trim { it <= ' ' }
            if (json.startsWith("{")) {
                val jsonObject = JSONObject(json)
                var message = jsonObject.toString(JSON_INDENT)
                message = message.replace("\n".toRegex(), "\n$HORIZONTAL_DOUBLE_LINE ")
                val s = getMethodNames()
                println(String.format(s, message))
                return
            }
            if (json.startsWith("[")) {
                val jsonArray = JSONArray(json)
                var message = jsonArray.toString(JSON_INDENT)
                message = message.replace("\n".toRegex(), "\n$HORIZONTAL_DOUBLE_LINE ")
                val s = getMethodNames()
                println(String.format(s, message))
                return
            }
            eLog("Invalid Json")
        } catch (e: JSONException) {
            eLog("Invalid Json")
        }

    }

    fun eLog(msg: Any,tag: String = TAG) {
        val content = msg.toString()
        if (content.isNotBlank()) {
            val s = getMethodNames()
            Log.e(tag, String.format(s, content))
        }
    }

    fun wLog(msg: String,tag: String = TAG) {
        val content = msg.toString()
        if (content.isNotBlank()) {
            val s = getMethodNames()
            Log.e(tag, String.format(s, content))
        }
    }

    fun iLog(msg: String,tag: String = TAG) {
        val content = msg.toString()
        if (content.isNotBlank()) {
            val s = getMethodNames()
            Log.i(tag, String.format(s, content))
        }
    }

    fun dLog(msg: String,tag: String = TAG) {
        val content = msg.toString()
        if (content.isNotBlank()) {
            val s = getMethodNames()
            Log.d(tag, String.format(s, content))
        }
    }
}