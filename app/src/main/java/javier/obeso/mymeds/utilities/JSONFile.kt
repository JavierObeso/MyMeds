package javier.obeso.mymeds.utilities

import android.content.Context
import android.util.Log
import java.io.IOException

class JSONFile{
    val MY_MEDS = "usuariosMyMeds.json"

    constructor(){}

    fun saveData(context: Context, json:String) {
        try {
            context.openFileOutput(MY_MEDS, Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }
        } catch (e: IOException) {
            Log.e("GUARDAR", "Error in writing" + e.localizedMessage)
        }
    }

    fun getData(context: Context): String {
        try {
            return context.openFileInput(MY_MEDS).bufferedReader().readLine()
        } catch (e: IOException) {
            Log.e("OBTENER", "Error in fetching data" + e.localizedMessage)
            return ""
        }
    }




}