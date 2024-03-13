package md.webmasterstudio.domenator.networking

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import md.webmasterstudio.domenator.Domenator
import md.webmasterstudio.domenator.data.model.DictionaryModel

object UserRepository {
    suspend fun getDictionaryCoroutine(scope: CoroutineScope, context: Context) {
        val response = withContext(Dispatchers.IO) {
            ApiClient.instance?.userService?.getDictionary()
        }

        if (response != null && response.isSuccessful) {
            val responseBody = response.body() as JsonObject // Cast to JsonObject

            val ruDictionaryModel = DictionaryModel()
            val enDictionaryModel = DictionaryModel()
            val roDictionaryModel = DictionaryModel()

            // Parse JSON object for each language
            parseJsonToDictionary(ruDictionaryModel, "ru", responseBody)
            parseJsonToDictionary(enDictionaryModel, "en", responseBody)
            parseJsonToDictionary(roDictionaryModel, "ro", responseBody)

            Domenator.instance.enDictionary = enDictionaryModel
            Domenator.instance.roDictionary = roDictionaryModel
            Domenator.instance.ruDictionary = ruDictionaryModel

            // Handle successful response and use the parsed models (ruDictionaryModel, enDictionaryModel, roDictionaryModel)
        } else {
            val errorBody = response?.errorBody()
            val errorString = errorBody?.string() ?: "Unknown error"
            // Handle API error (check errorBody for details)
        }
    }

    private fun parseJsonToDictionary(dictionaryModel: DictionaryModel, languageKey: String, jsonObject: JsonObject) {
        val languageObject = jsonObject.getAsJsonObject("data").getAsJsonObject(languageKey) ?: return  // Check if language object exists

        // Iterate through key-value pairs in the language object
        for (key in languageObject.keySet()) {
            val value = languageObject.get(key).asString
            dictionaryModel.dictionary[key] = value
        }
    }
}