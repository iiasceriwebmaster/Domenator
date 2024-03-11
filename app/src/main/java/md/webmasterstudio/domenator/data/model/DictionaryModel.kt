package md.webmasterstudio.domenator.data.model

import android.util.Log
import md.webmasterstudio.domenator.R
import org.json.JSONException
import org.json.JSONObject


class DictionaryModel {

    private val dictionaryList = mutableListOf<MutableMap<String, String>>()

    fun jsonToDictionaryObj(jsonObject: JSONObject) {
        Log.d("CREATION", "DictionaryModel -> jsonToDictionaryObj")
        try {
            val fields = R.string::class.java.fields
            for (field in fields) {
                val map = mutableMapOf<String, String>()
                map[field.name]=jsonObject.getString(field.name)
                dictionaryList.add(map)
            }
        } catch (e: JSONException) {
            Log.d("CREATION", "DictionaryModel error -> 1")
            e.printStackTrace()
        }
    }
}

