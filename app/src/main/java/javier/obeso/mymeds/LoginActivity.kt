package javier.obeso.mymeds

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javier.obeso.mymeds.utilities.JSONFile


class LoginActivity : AppCompatActivity() {

    val RC_SIGN_IN = 123
    lateinit var mGoogleSignInClient: GoogleSignInClient
    var jsonFile:JSONFile ?= null
    var correo: String = ""
    var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        jsonFile = JSONFile()

        val botonIniciarSesion:Button = findViewById(R.id.boton_inicio) as Button

        var usuario: EditText = findViewById(R.id.loginUsuario)
        var contrasena: EditText = findViewById(R.id.loginContrasena)

        botonIniciarSesion.setOnClickListener(){
            correo = usuario.text.toString()
            password = contrasena.text.toString()

            if (!correo.isEmpty() || !password.isEmpty()) {
                iniciarSesion()
            } else {
                Toast.makeText(this, "No se llenaron todos los campos", Toast.LENGTH_SHORT).show()
            }
            /*
            if(iniciarSesion()) {
                val intent: Intent = Intent(this, MainActivity::class.java)
                intent.putExtra("name", nombreUsuario)
                intent.putExtra("email", correo)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
             */
        }

        val botonRegistro:Button = findViewById(R.id.boton_registro) as Button

        botonRegistro.setOnClickListener(){
            var intent: Intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        /*
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
         */
    }
    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    fun iniciarSesion() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(correo, password).addOnCompleteListener(){
            if (it.isSuccessful){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null){
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("name", account.displayName)
            intent.putExtra("email", account.email)
            startActivity(intent)
        }
    }

    fun iniciarSesion(): Boolean {
        var usuario: EditText = findViewById(R.id.loginUsuario)
        var usuarioString:String = usuario.text.toString()

        var contrasena: EditText = findViewById(R.id.loginContrasena)
        var contrasenaString:String = contrasena.text.toString()

        if(usuarioString.isEmpty() || contrasenaString.isEmpty()) {
            Toast.makeText(this, "No se llenaron todos los campos", Toast.LENGTH_SHORT).show()
        } else {
            var credenciales = fetchingData()
            if(!credenciales.isEmpty()) {
                if(usuarioString.equals(credenciales.get(0), true)
                    && contrasenaString.equals(credenciales.get(1), true)) {
                    nombreUsuario = credenciales.get(0)
                    correo = credenciales.get(2)
                    Toast.makeText(this, "Inicio de sesion exitoso", Toast.LENGTH_SHORT).show()
                    return true
                }
            } else {
                Toast.makeText(this, "Ocurrio un error al recuperar los datos guardados", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return false
    }

    fun fetchingData():ArrayList<String> {
        try {
            var json: String = jsonFile?.getData(this) ?: ""
            if(json != "") {
                var jsonArray: JSONArray = JSONArray(json)
                var lista = parseJson(jsonArray)
                return lista
            }
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
        return ArrayList<String>()
    }

    fun parseJson(jsonArray: JSONArray): ArrayList<String> {
        var lista = ArrayList<String>()

        for (i in 0..jsonArray.length()) {
            try {
                val usuario = jsonArray.getJSONObject(i).getString("usuario")
                val contrasena = jsonArray.getJSONObject(i).getString("contrasena")
                val correo = jsonArray.getJSONObject(i).getString("email")
                lista.add(usuario)
                lista.add(contrasena)
                lista.add(correo)
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
        }
        return lista
    }
     */

}
