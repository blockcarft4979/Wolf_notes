package com.bck
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.text.format.DateUtils
import android.util.Log
import com.bck.data.NotesData
import com.bck.data.Path
import com.google.gson.Gson
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


fun loadAllNotesFromDirectory(path: String): MutableList<NotesData> {
    val notes = mutableListOf<NotesData>()
    val gson = Gson()

    val dir = File(Environment.getExternalStorageDirectory(), path)

    if (!dir.exists() || !dir.isDirectory) {
        return notes
    }

    val jsonFiles = dir.listFiles { file -> file.extension == "json" }
    jsonFiles?.forEach { file ->
        try {
            val jsonString = file.readText()
            val note = gson.fromJson(jsonString, NotesData::class.java)

            notes.add(
                note.copy(
                    id = file.name,
                    time = formatRelativeTimeFromTimestamp(file.lastModified())
                )
            )
            //notes.add(note.copy(time = formatRelativeTimeFromTimestamp(file.lastModified())))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    notes.sortByDescending { note ->
        val file = File(dir, note.id)
        if (file.exists()) file.lastModified() else 0L
    }
    //Log.d("woshiload", "已发现${notes.size}条数据")
    return notes
}

fun formatRelativeTimeFromTimestamp(
    timestamp: Long,
    thresholdDays: Int = 7
): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return if (diff > TimeUnit.DAYS.toMillis(thresholdDays.toLong())) {
        // 超过阈值，显示绝对日期
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.format(Date(timestamp))
    } else {
        // 阈值内，显示相对时间（几分钟前、几小时前、昨天等）
        DateUtils.getRelativeTimeSpanString(
            timestamp,
            now,
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString()
    }
}

fun saveNoteToJson(note: NotesData): Boolean {
    val dir = File(Environment.getExternalStorageDirectory(), Path.NOTES_PATH)
    if (!dir.exists()) dir.mkdirs()

    //val id = id
    Log.d("woshiId", note.id.isEmpty().toString() + "id:" + note.id)
    val fileName = if (note.id == "") {
        "${System.currentTimeMillis()}.json"
    } else {
        note.id
    }

    val file = File(dir, fileName)

    Log.d("woShipath", file.path)

    return try {
        val json = JSONObject().apply {
            put("title", note.title ?: "")
            put("content", note.content ?: "")
        }
        file.writeText(json.toString())
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun loadNoteById(id: String?): NotesData? {
    val file = File(
        Environment.getExternalStorageDirectory(),
        "${Path.NOTES_PATH}${id}"
    )
    if (!file.exists()) return null
    return try {
        val jsonString = file.readText()
        val gson = Gson()
        gson.fromJson(jsonString, NotesData::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun getAppVersionName(context: Context): String {
    return try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName ?: "null"
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        "null"
    }
}