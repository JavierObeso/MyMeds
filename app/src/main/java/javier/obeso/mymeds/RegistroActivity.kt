package javier.obeso.mymeds

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javier.obeso.mymeds.entidades.Usuario
import javier.obeso.mymeds.utilities.JSONFile
import kotlinx.android.synthetic.main.activity_registro.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap


class RegistroActivity : AppCompatActivity() {

    var jsonFile: JSONFile? = null

    var usuarioString:String = ""
    var contrasenaString:String = ""
    var confirmarContrasenaString:String = ""
    var emailString:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val analytics:FirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        jsonFile = JSONFile()

        btnRegistrar.setOnClickListener {
            guardar()
        }

    }

    fun guardar() {
        var jsonArray: JSONArray = JSONArray()

        usuarioString = usuario.text.toString()
        contrasenaString = contrasena.text.toString()
        confirmarContrasenaString = confirmarContrasena.text.toString()
        emailString = email.text.toString()

        if(usuarioString.isEmpty() || contrasenaString.isEmpty() ||
                confirmarContrasenaString.isEmpty() || emailString.isEmpty()) {
            Toast.makeText(this, "No se llenaron todos los campos", Toast.LENGTH_SHORT).show()
        } else {
            if(contrasenaString.equals(confirmarContrasenaString, true) && contrasenaString.length >= 6) {
                /*
                var j: JSONObject = JSONObject()
                j.put("usuario", usuarioString)
                j.put("contrasena", contrasenaString)
                j.put("confirmarContrasena", confirmarContrasenaString)
                j.put("email", emailString)

                jsonArray.put(0, j)

                jsonFile?.saveData(this, jsonArray.toString())
                 */
                registerUser()
            } else if (!contrasenaString.equals(confirmarContrasenaString, true)){
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            } else if (contrasenaString.length < 6){
                Toast.makeText(this, "La contraseña debe tener mínimo 6 caracteres", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun registerUser(){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailString, contrasenaString).addOnCompleteListener(){
            if (it.isSuccessful){
                val user = Usuario (usuarioString, emailString, contrasenaString, 0, 0)

                var id:String = FirebaseAuth.getInstance().getCurrentUser()!!.getUid()

                FirebaseDatabase.getInstance().getReference().child("Users").child(id).setValue(user).addOnCompleteListener { task2: Task<Void> ->
                    if (task2.isSuccessful()) {
                        Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "No se pudieron crear los datos correctamente", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
