package javier.obeso.mymeds

import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import javier.obeso.mymeds.entidades.Alarma
import javier.obeso.mymeds.entidades.Medicamento
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.alarma_view.view.*
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
        var horas = ArrayList<String>()
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
            startActivity(Intent(this, ProfileActivity::class.java))
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

        checaHoras()
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

        checaHoras()
    }

    fun checaHoras(){
        val id:String = FirebaseAuth.getInstance().getCurrentUser()!!.getUid();

        FirebaseDatabase.getInstance().getReference().child("Users").child(id).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onCancelled(dataBaseError: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map = dataSnapshot.value as HashMap<*, *>

                ponerAlarma(map["cantAlarmas"].toString().toInt())
            }
        })
    }

    fun ponerAlarma(cantAlarmas:Int){
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss a")
        var horaActual = sdf.format(Date())
        var ampm = horaActual.substring(horaActual.length-2)

        horaActual = horaActual.substring(10, 15)

        var horaActualInt1 = horaActual.substring(0,2)
        var horaActualInt2 = horaActual.substring(3,5)

        if (ampm.equals("PM",true)){
            var horaSuma = horaActualInt1.toInt() + 12
            horaActualInt1 = horaSuma.toString()
        }

        var horaActualInt = horaActualInt1 + horaActualInt2

        var t = Timer()

        val id:String = FirebaseAuth.getInstance().currentUser!!.uid;

        for (a in 0.. cantAlarmas){
            var ref: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(id).child("Alarmas").child(a.toString())
            ref.addListenerForSingleValueEvent(object:
                ValueEventListener {
                override fun onCancelled(dataBaseError: DatabaseError) {}

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()){
                        val map = dataSnapshot.value as Map<*,*>

                        val hora = map["hora"].toString()
                        var horaInt1 = hora.substring(0,2)
                        var horaInt2 = hora.substring(3,5)

                        var horaInt = horaInt1 + horaInt2

                        if (horaActualInt >= horaInt){
                            dialogoMedicamentos(map["nombre"].toString(), map["hora"].toString(), a)
                        }
                    }
                }
            })
        }
    }

    fun dialogoMedicamentos (medicamento:String, hora:String, alarma:Int){
        val idUser:String = FirebaseAuth.getInstance().currentUser!!.uid;
        var builder = AlertDialog.Builder(this, R.style.DialogCustomStyle)
        builder.setTitle("Medicamento")
        builder.setMessage("¿Se tomó la medicina ($medicamento) de las $hora?")
        builder.setPositiveButton("Sí") {
                dialog, id ->
            checkTomadas(true)
            var ref: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(idUser).child("Alarmas").child(alarma.toString())
            val map = HashMap<String, Any>()
            map.put("hora", "Tomado")
            ref.updateChildren(map);
            dialog.dismiss()
            reiniciarActivity(this@MainActivity)
            }
        builder.setNegativeButton("No"){
                    dialog, id ->
            checkTomadas(false)
            var ref: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(idUser).child("Alarmas").child(alarma.toString())
            val map = HashMap<String, Any>()
            map.put("hora", "No tomado")
            ref.updateChildren(map);
            dialog.dismiss()
            reiniciarActivity(this@MainActivity)
            }.show()
    }

    fun checkTomadas (si: Boolean){
        val id:String = FirebaseAuth.getInstance().currentUser!!.uid;

        val ref: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(id)

        ref.addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onCancelled(dataBaseError: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map = dataSnapshot.value as Map<*,*>

                if (si){
                    val medTomados = map["medicamentosTomados"].toString().toInt()
                    siTomada(medTomados)
                } else {
                    val medNoTomados = map["medicamentosNoTomados"].toString().toInt()
                    noTomada(medNoTomados)
                }
            }
        })
    }

    fun siTomada (medTomados:Int){
        var id:String = FirebaseAuth.getInstance().getCurrentUser()!!.getUid()
        var ref:DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(id)
        val map = HashMap<String, Any>()
        var newMedTomados:Int = (medTomados + 1)
        map.put("medicamentosTomados", newMedTomados)
        ref.updateChildren(map);
    }

    fun noTomada (medNoTomados:Int){
        var id:String = FirebaseAuth.getInstance().getCurrentUser()!!.getUid()
        var ref:DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(id)
        val map = HashMap<String, Any>()
        var newMedNoTomados:Int = (medNoTomados + 1)
        map.put("medicamentosNoTomados", newMedNoTomados)
        ref.updateChildren(map);
    }

    fun reiniciarActivity(actividad: Activity) {
        val intent = Intent()
        intent.setClass(actividad, actividad.javaClass)
        //llamamos a la actividad
        actividad.startActivity(intent)
        //finalizamos la actividad actual
        actividad.finish()
    }

    fun cargaAlarmas(){
        val id:String = FirebaseAuth.getInstance().getCurrentUser()!!.getUid();

        FirebaseDatabase.getInstance().getReference().child("Users").child(id).addListenerForSingleValueEvent(object:
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

        FirebaseDatabase.getInstance().getReference().child("Users").child(id).addListenerForSingleValueEvent(object:
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
                FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Alarmas").child(i.toString()).addListenerForSingleValueEvent(object:
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
                FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Medicamentos").child(i.toString()).addListenerForSingleValueEvent(object:
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
        if (alarmas.size > 1){
            var tmp: Alarma
            var encontrados = 0
            for (a in 0 until alarmas.size){
                var hora1Int = "0"
                for (a2 in 0 until alarmas.size){
                    var hora2Int = "0"
                    if (alarmas[a].hora.equals("Tomado", true) || alarmas[a].hora.equals("No tomado", true)){
                        encontrados ++
                        hora1Int = encontrados.toString()
                    } else {
                        var hora1Int1 = alarmas.get(a).hora.substring(0,2)
                        var hora1Int2 = alarmas.get(a).hora.substring(3,5)

                        hora1Int = hora1Int1 + hora1Int2
                    }

                    if (alarmas[a2].hora.equals("Tomado", true) || alarmas[a2].hora.equals("No tomado", true)){
                        encontrados ++
                        hora2Int = encontrados.toString()
                    } else {
                        var hora2Int1 = alarmas.get(a2).hora.substring(0,2)
                        var hora2Int2 = alarmas.get(a2).hora.substring(3,5)

                        hora2Int = hora2Int1 + hora2Int2
                    }

                    if (hora1Int.toInt() < hora2Int.toInt()){
                        tmp = alarmas.get(a2)
                        alarmas.set(a2, alarmas.get(a))
                        alarmas.set(a, tmp)
                    }
                }
            }
        }

        progress.removeAllViews()
        var c = 1
        for (item in alarmas){
            val circle: View
            if (item.hora.equals("Tomado", true)){
                circle = layoutInflater.inflate(R.layout.circle_green_view, null)
            } else if (item.hora.equals("No tomado", true)){
                alerta_perdida.visibility = View.VISIBLE;
                circle = layoutInflater.inflate(R.layout.circle_red_view, null)
            } else {
                circle = layoutInflater.inflate(R.layout.circle_view, null)
            }
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
                if (alarma.hora.equals("Tomado", true) || alarma.hora.equals("No tomado", true)){
                    vista.tv_hour.setText(alarma.hora)
                } else {
                    vista.tv_hour.setText(alarma.hora + " hrs.")
                }
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
