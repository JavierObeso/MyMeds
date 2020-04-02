package javier.obeso.mymeds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val botonIniciarSesion:Button = findViewById(R.id.boton_inicio) as Button
        val botonRegistro:Button = findViewById(R.id.boton_registro) as Button

        botonIniciarSesion.setOnClickListener(){
            var intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        botonRegistro.setOnClickListener(){
            var intent: Intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }

}
