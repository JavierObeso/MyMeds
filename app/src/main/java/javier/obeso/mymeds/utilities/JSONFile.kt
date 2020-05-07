package javier.obeso.mymeds.utilities

import android.content.Context
import android.util.Log
import java.io.IOException

class JSONFile{
    val MY_MEDS_USERS = "usuariosMyMeds.json"
    val MY_MEDS_ALARMS = "alarmasMyMeds.json"

    constructor(){}

    fun saveData(context: Context, json:String) {
        try {
            context.openFileOutput(MY_MEDS_USERS, Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }
        } catch (e: IOException) {
            Log.e("GUARDAR", "Error in writing" + e.localizedMessage)
        }
    }

    fun saveDataAlarmas(context: Context, json:String) {
        try {
            context.openFileOutput(MY_MEDS_ALARMS, Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }
        } catch (e: IOException) {
            Log.e("GUARDAR", "Error in writing" + e.localizedMessage)
        }
    }

    fun getData(context: Context): String {
        try {
            return context.openFileInput(MY_MEDS_USERS).bufferedReader().readLine()
        } catch (e: IOException) {
            Log.e("OBTENER", "Error in fetching data" + e.localizedMessage)
            return ""
        }
    }

    fun getDataAlarmas(context: Context): String {
        try {
            return context.openFileInput(MY_MEDS_ALARMS).bufferedReader().readLine()
        } catch (e: IOException) {
            Log.e("OBTENER", "Error in fetching data" + e.localizedMessage)
            return ""
        }
    }

}