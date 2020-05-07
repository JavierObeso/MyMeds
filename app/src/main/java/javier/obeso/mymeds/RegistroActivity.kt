package javier.obeso.mymeds

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import javier.obeso.mymeds.utilities.JSONFile
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registro.*
import org.json.JSONArray
import org.json.JSONObject

class RegistroActivity : AppCompatActivity() {

    var jsonFile: JSONFile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        jsonFile = JSONFile()

        btnRegistrar.setOnClickListener {
            guardar()
        }

    }

    fun guardar() {
        var jsonArray: JSONArray = JSONArray()

        var usuario:EditText = findViewById(R.id.usuario)
        var usuarioString:String = usuario.text.toString()

        var contrasena:EditText = findViewById(R.id.contrasena)
        var contrasenaString:String = contrasena.text.toString()

        var confirmarContrasena:EditText = findViewById(R.id.confirmarContrasena)
        var confirmarContrasenaString:String = confirmarContrasena.text.toString()

        var email:EditText = findViewById(R.id.email)
        var emailString:String = email.text.toString()

        if(usuarioString.isEmpty() || contrasenaString.isEmpty() ||
                confirmarContrasenaString.isEmpty() || emailString.isEmpty()) {
            Toast.makeText(this, "No se llenaron todos los campos", Toast.LENGTH_SHORT).show()
        } else {

            if(contrasenaString.equals(confirmarContrasenaString, true)) {
                var j: JSONObject = JSONObject()
                j.put("usuario", usuarioString)
                j.put("contrasena", contrasenaString)
                j.put("confirmarContrasena", confirmarContrasenaString)
                j.put("email", emailString)

                jsonArray.put(0, j)

                jsonFile?.saveData(this, jsonArray.toString())

                Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Las contrasenas no coinciden", Toast.LENGTH_SHORT).show()
            }

        }


    }

}
