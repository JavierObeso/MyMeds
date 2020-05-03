package javier.obeso.mymeds

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import javier.obeso.mymeds.entidades.Alarma
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.alarma_view.view.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var adaptador: AdaptadorAlarmas? = null

    companion object{
        var alarmas = ArrayList<Alarma>()
        var nombre: String? = null
        var correo: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle = intent.extras
        if (bundle != null){
                nombre = bundle.getString("name")
                correo = bundle.getString("email")
        }

        val profileButton: ImageButton = findViewById(R.id.boton_perfil) as ImageButton
        val clockButton:ImageButton = findViewById(R.id.fondo_reloj) as ImageButton

        profileButton.setOnClickListener(){
            var intent: Intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        clockButton.setOnClickListener(){
            var intent: Intent = Intent(this, registroAlarma::class.java)
            startActivity(intent)
        }

        val addButton:Button = findViewById(R.id.boton_anadir) as Button

        addButton.setOnClickListener(){
            var intent: Intent = Intent(this, registroMedicamento::class.java)
            startActivity(intent)
        }

        val fecha_actual = SimpleDateFormat("EEEE dd 'de' MMMM yyyy", Locale("es","ES")).format(Date())

        val fecha:TextView = findViewById(R.id.fecha) as TextView
        fecha.setText(""+fecha_actual.capitalize()+"")

        adaptador = AdaptadorAlarmas(this, alarmas)
        lista.adapter = adaptador

        if (alarmas.size != 0) {
            makeCircles()
        }

    }

    override fun onResume() {
        super.onResume()

        adaptador = AdaptadorAlarmas(this, alarmas)
        lista.adapter = adaptador

        if (alarmas.size != 0) {
            makeCircles()
        }
    }

    fun makeCircles(){
        progress.removeAllViews()
        var c = 1
        for (item in alarmas){
            val circle: View = layoutInflater.inflate(R.layout.circle_view, null)
            val line: View = layoutInflater.inflate(R.layout.line_view, null)
            progress.addView(circle)
            if(c != alarmas.size){
                progress.addView(line)
            }
            c++
        }
    }

    private class AdaptadorAlarmas: BaseAdapter {
        var alarmas = ArrayList<Alarma>()
        var contexto: Context? = null

        constructor(contexto: Context, tasks: ArrayList<Alarma>){
            this.contexto = contexto
            this.alarmas = tasks
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var alarma = alarmas[p0]
            var inflador = LayoutInflater.from(contexto)
            var vista = inflador.inflate(R.layout.alarma_view, null)

            vista.tv_title.setText("Próximo medicamento")
            vista.tv_hour.setText(alarma.hora + " hrs.")

            return vista
        }

        override fun getItem(p0: Int): Any {
            return alarmas[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return alarmas.size
        }
    }
}
