package javier.obeso.mymeds

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*


class ProfileActivity : AppCompatActivity() {

    var correo: String = ""
    var nombre: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tv_nombre.setText(nombre)
        tv_correo.setText(correo)

        getInfoUser()

        var atras: ImageButton = findViewById(R.id.backPerfil) as ImageButton

        atras.setOnClickListener(){
            finish()
        }

        var signOut:Button = findViewById(R.id.boton_cerrar_sesion) as Button

        signOut.setOnClickListener(){
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    fun getInfoUser (){
        var id:String = FirebaseAuth.getInstance().getCurrentUser()!!.getUid();

        FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(object:
            ValueEventListener {
            override fun onCancelled(dataBaseError: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){
                    var map = dataSnapshot.value as Map<*,*>
                    tv_correo.text = map["email"].toString()
                    tv_nombre.text = map["name"].toString()
                }
            }
        })
    }

}
