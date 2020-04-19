package javier.obeso.mymeds

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import java.net.URL


class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

            val nombre = MainActivity.nombre
            val correo = MainActivity.correo

            tv_nombre.setText(nombre)
            tv_correo.setText(correo)
    }
}
