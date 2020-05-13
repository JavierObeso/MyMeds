package javier.obeso.mymeds

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javier.obeso.mymeds.entidades.Alarma
import javier.obeso.mymeds.entidades.Medicamento
import javier.obeso.mymeds.utilities.ReminderBroadcast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.alarma_view.view.*
import java.lang.Math.random
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private var adaptador: AdaptadorAlarmas? = null
    var create:Boolean = false
    var CHANNEL_ID:String = "notificacionMyMeds";

//    private var alarmMgr: AlarmManager? = null
//    private lateinit var alarmIntent: PendingIntent
//    var pendingIntents: ArrayList<PendingIntent>? = null

    companion object{
        var alarmas = ArrayList<Alarma>()
        var medicamentos = ArrayList<Medicamento>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //desde aqui
        createNotificationChannel()
//        pendingIntents = ArrayList<PendingIntent>()
//
//        alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//        var calendar = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis()
//            set(Calendar.HOUR_OF_DAY, 14)
//            set(Calendar.MINUTE, 18)
//            set(Calendar.SECOND,0)
//        }
//        var calendar2 = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis()
//            set(Calendar.HOUR_OF_DAY, 14)
//            set(Calendar.MINUTE, 19)
//            set(Calendar.SECOND,1)
//        }
//        for (i in 0..2) {
//            var intento:Intent = Intent(this, ReminderBroadcast::class.java)
//            var alarmIntent: PendingIntent = PendingIntent.getBroadcast(this, i, intento, 0)
//
//            if(i == 0) {
//                alarmMgr!!.setExact(
//                    AlarmManager.RTC_WAKEUP,
//                    calendar.timeInMillis,
//                    alarmIntent
//                )
//            }
//
//            if(i == 1) {
//                alarmMgr!!.setExact(
//                    AlarmManager.RTC_WAKEUP,
//                    calendar2.timeInMillis,
//                    alarmIntent
//                )
//            }
//        }



        //hasta aqui

        alerta_perdida.setVisibility(View.GONE);

        create = true

        alarmas.clear()
        medicamentos.clear()

        cargaAlarmas()
        cargaMedicamentos()

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

        val addButton:ImageButton = findViewById(R.id.boton_anadir) as ImageButton

        addButton.setOnClickListener(){
            var intent: Intent = Intent(this, registroMedicamento::class.java)
            startActivity(intent)
        }

        val fecha_actual = SimpleDateFormat("EEEE dd 'de' MMMM yyyy", Locale("es","ES")).format(Date())

        val fecha:TextView = findViewById(R.id.fecha) as TextView
        fecha.setText(""+fecha_actual.capitalize()+"")

        adaptador = AdaptadorAlarmas(this, alarmas)
        lista.adapter = adaptador
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    override fun onResume() {
        super.onResume()

        alerta_perdida.setVisibility(View.GONE);

        if (create != true){
            alarmas.clear()
            medicamentos.clear()

            cargaAlarmas()
            cargaMedicamentos()

            adaptador = AdaptadorAlarmas(this, alarmas)
            lista.adapter = adaptador
        } else {
            create = false
        }

    }

    fun cargaAlarmas(){
        val id:String = FirebaseAuth.getInstance().getCurrentUser()!!.getUid();

        FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(object:
            ValueEventListener {
            override fun onCancelled(dataBaseError: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map = dataSnapshot.value as HashMap<*, *>

                agregaAlarmas(map["cantAlarmas"].toString().toInt())
            }
        })
    }

    fun cargaMedicamentos(){
        val id:String = FirebaseAuth.getInstance().getCurrentUser()!!.getUid();

        FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(object:
            ValueEventListener {
            override fun onCancelled(dataBaseError: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map = dataSnapshot.value as HashMap<*, *>

                agregaMedicamentos(map["cantMedicamentos"].toString().toInt())
            }
        })
    }

    fun agregaAlarmas (cantAlarmas:Int){
        val id:String = FirebaseAuth.getInstance().getCurrentUser()!!.getUid();

        if (cantAlarmas != 0){
            for (i in 0..cantAlarmas){
                FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Alarmas").child(i.toString()).addValueEventListener(object:
                    ValueEventListener {
                    override fun onCancelled(dataBaseError: DatabaseError) {}

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()){
                            val map = dataSnapshot.value as Map<*,*>
                            val medicamento = map["nombre"].toString()
                            val dosis = map["dosis"].toString()
                            val frecuencia = map["frecuencia"].toString()
                            val hora = map["hora"].toString()
                            val inicio = map["inicio"].toString()
                            val fin = map["fin"].toString()
                            val revisor = map["revisor"].toString()

                            alarmas.add(Alarma(medicamento,frecuencia, dosis, hora, inicio, fin, revisor))
                            adaptador!!.notifyDataSetChanged()

                            if (alarmas.size != 0) {
                                makeCircles()
                            }
                        }
                    }
                })
            }
        }
    }

    fun agregaMedicamentos (cantMedicamentos:Int){
        val id:String = FirebaseAuth.getInstance().getCurrentUser()!!.getUid();

        if (cantMedicamentos != 0){
            for (i in 0..cantMedicamentos){
                FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Medicamentos").child(i.toString()).addValueEventListener(object:
                    ValueEventListener {
                    override fun onCancelled(dataBaseError: DatabaseError) {}

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()){
                            val map = dataSnapshot.value as Map<*,*>
                            val nombre = map["nombre"].toString()
                            val cantidad = map["cantidad"].toString()
                            val caducidad = map["caducidad"].toString()

                            medicamentos.add(Medicamento(nombre,cantidad, caducidad))
                        }
                    }
                })
            }
        }
    }

    /*
    fun fetchingData (){
        try {
            var json:String = jsonFile?.getDataAlarmas(this) ?: ""
            if (json != ""){
                this.data = true
                var jsonArray: JSONArray = JSONArray(json)

                alarmas = parseJSON(jsonArray)

            } else {
                this.data = false
            }
        } catch (exception: JSONException){
            exception.printStackTrace()
        }
    }

    fun parseJSON(jsonArray: JSONArray): ArrayList<Alarma>{
        var lista = ArrayList<Alarma>()

        for (i in 0..jsonArray.length()){
            try {
                val nombre = jsonArray.getJSONObject(i).getString("medicamento")
                val frecuencia = jsonArray.getJSONObject(i).getString("frecuencia")
                val dosis = jsonArray.getJSONObject(i).getString("dosis")
                val hora = jsonArray.getJSONObject(i).getString("hora")
                val inicio = jsonArray.getJSONObject(i).getString("inicio")
                val fin = jsonArray.getJSONObject(i).getString("fin")
                val revisor = jsonArray.getJSONObject(i).getString("revisor")

                var alarma:Alarma
                if(revisor.isEmpty() || revisor.equals("", true)){
                    alarma = Alarma(nombre, frecuencia, dosis, hora, inicio, fin, null)
                } else {
                    alarma = Alarma(nombre, frecuencia, dosis, hora, inicio, fin, revisor)
                }

                lista.add(alarma)

            } catch (exception: JSONException){
                exception.printStackTrace()
            }
        }
        return lista
    }
     */

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

            if (alarma.nombre.equals("Aún no tiene alarmas", true)) {
                vista.tv_title.setText(alarma.nombre)
                vista.tv_hour.setText(alarma.hora)
            } else {
                vista.tv_title.setText(alarma.nombre)
                vista.tv_hour.setText(alarma.hora + " hrs.")
            }


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
