package javier.obeso.mymeds

import android.content.Context
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.drawerlayout.widget.DrawerLayout
import javier.obeso.mymeds.entidades.Alarma
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.alarma_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var adaptador: AdaptadorAlarmas? = null

    companion object{
        var alarmas = ArrayList<Alarma>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fecha_actual = SimpleDateFormat("EEEE dd 'de' MMMM yyyy", Locale("es","ES")).format(Date())

        val fecha:TextView = findViewById(R.id.fecha) as TextView
        fecha.setText(""+fecha_actual.capitalize()+"")

        val toolBar:Toolbar = R.layout.app_bar as Toolbar
        setSupportActionBar(toolBar)


        val menu:ImageButton = findViewById(R.id.boton_menu) as ImageButton

        menu.setOnClickListener(){

        }

        mockAlarmas()

        adaptador = AdaptadorAlarmas(this, alarmas)
        lista.adapter = adaptador
    }

    //Sólo para presentación
    fun mockAlarmas(){
        alarmas.add(Alarma("medicamento","10 hrs.", "10 ml.", "22:00"))
        alarmas.add(Alarma("medicamento","10 hrs.", "10 ml.", "08:00"))
        alarmas.add(Alarma("medicamento","10 hrs.", "10 ml.", "18:00"))
        alarmas.add(Alarma("medicamento","10 hrs.", "10 ml.", "18:00"))
        alarmas.add(Alarma("medicamento","10 hrs.", "10 ml.", "18:00"))
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

            vista.tv_title.setText("Próximo " + alarma.tipo)
            vista.tv_hour.setText(alarma.hora + " hrs.")
            vista.alarma_layout.setAlpha(1 - (p0 * 0.15f))

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
