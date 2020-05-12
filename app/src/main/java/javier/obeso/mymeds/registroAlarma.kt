package javier.obeso.mymeds

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import javier.obeso.mymeds.entidades.Alarma
import javier.obeso.mymeds.entidades.Medicamento
import javier.obeso.mymeds.utilities.JSONFile
import kotlinx.android.synthetic.main.activity_registro_alarma.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class registroAlarma : AppCompatActivity() {

    var jsonFile: JSONFile? = null
    val timeSetListener = null;
    var cantAlarmas:Int = 0

    var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_alarma)

        mDatabase = FirebaseDatabase.getInstance().getReference()
        cantAlarmas = MainActivity.alarmas.size
        //jsonFile = JSONFile()

        configurarSpinnerMedicamentos()
        configurarSpinnerFrecuencia()
        configurarSpinnerDosis()
        configurarEditTextHora()
        configurarEditTextInicio()
        configurarEditTextFin()
        configurarSpinnerRevisor()

        var atras:ImageButton = findViewById(R.id.back) as ImageButton

        atras.setOnClickListener(){
            finish()
        }

        boton_guardar.setOnClickListener(){
            /* var nombre = spinner_medicamentos.selectedItem.toString()
            var frecuencia = spinner_frecuencia.selectedItem.toString()
            var dosis = spinner_dosis.selectedItem.toString()
            var hora = et_hora.text.toString()
            var inicio = inicio_periodo.text.toString()
            var fin = fin_periodo.text.toString()
            var revisor:String? = spinner_revisor.selectedItem.toString()

            var alarma = Alarma (nombre,frecuencia,dosis, hora, inicio, fin, revisor)

            MainActivity.alarmas.add(alarma)
             */
            if (llenaAlarma(cantAlarmas)){
                actualizaCantAlarmas(cantAlarmas)
            }
        }

    }

    fun configurarSpinnerMedicamentos() {
        var medicamentos = MainActivity.medicamentos
        var medList = ArrayList<String>()

        medList.add("Medicamento")

        for (m in medicamentos){
            medList.add(m.nombre)
        }

        val spinner = findViewById<Spinner>(R.id.spinner_medicamentos)
        if (spinner != null) {
            //val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, medicamentos)

            val adapter = ArrayAdapter(this, R.layout.spinner_item, medList)
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    fun configurarSpinnerFrecuencia() {
        val frecuencias = resources.getStringArray(R.array.arreglo_frecuencia)

        val spinner = findViewById<Spinner>(R.id.spinner_frecuencia)
        if (spinner != null) {
            //val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, medicamentos)
            val adapter = ArrayAdapter(this, R.layout.spinner_item, frecuencias)
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    fun configurarSpinnerDosis() {
        val dosis = resources.getStringArray(R.array.arreglo_dosis)

        val spinner = findViewById<Spinner>(R.id.spinner_dosis)
        if (spinner != null) {
            //val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, medicamentos)
            val adapter = ArrayAdapter(this, R.layout.spinner_item, dosis)
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    fun configurarEditTextHora() {
        val editTextHora = findViewById(R.id.et_hora) as EditText
        editTextHora.setRawInputType(InputType.TYPE_NULL)
        val cal = Calendar.getInstance()
        editTextHora.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                editTextHora.text = Editable.Factory.getInstance().newEditable(SimpleDateFormat("HH:mm").format(cal.time))
            }
            TimePickerDialog(this, R.style.TimePickerTheme, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                Calendar.MINUTE), false).show()
        }
    }

    fun configurarEditTextInicio() {
        val editTextInicio = findViewById(R.id.inicio_periodo) as EditText
        editTextInicio.setRawInputType(InputType.TYPE_NULL)
        val cal = Calendar.getInstance()

        editTextInicio.setOnClickListener {
            val dp = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, day)
                editTextInicio.text = Editable.Factory.getInstance().newEditable(SimpleDateFormat("yyyy/MM/dd").format(cal.time))

            }
            DatePickerDialog(this, R.style.TimePickerTheme, dp, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    fun configurarEditTextFin() {
        val editTextFin = findViewById(R.id.fin_periodo) as EditText
        editTextFin.setRawInputType(InputType.TYPE_NULL)
        val cal = Calendar.getInstance()

        editTextFin.setOnClickListener {
            val dp = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, day)
                editTextFin.text = Editable.Factory.getInstance().newEditable(SimpleDateFormat("yyyy/MM/dd").format(cal.time))

            }
            DatePickerDialog(this, R.style.TimePickerTheme, dp, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    fun configurarSpinnerRevisor() {
        val revisores = resources.getStringArray(R.array.arreglo_revisores)

        val spinner = findViewById<Spinner>(R.id.spinner_revisor)
        if (spinner != null) {
            //val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, medicamentos)
            val adapter = ArrayAdapter(this, R.layout.spinner_item, revisores)
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    Toast.makeText(this@registroAlarma, getString(R.string.selected_item) + " " +
                            "" + revisores[position], Toast.LENGTH_SHORT).show()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }
    }

    /*
    fun guardar() {
        var id:String = FirebaseAuth.getInstance().getCurrentUser()!!.getUid();

        mDatabase!!.child("Users").child(id).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onCancelled(dataBaseError: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var map = dataSnapshot.value as Map<*,*>

                cambiaCantAlarmas(map["cantAlarmas"].toString().toInt())
            }
        })

    }

    fun cambiaCantAlarmas (cAlarmas:Int){
            cantAlarmas = cAlarmas
    }
    */

    fun llenaAlarma (cantAlarmas:Int):Boolean{
        var id:String = FirebaseAuth.getInstance().getCurrentUser()!!.getUid();

        var medicamento:Spinner = findViewById(R.id.spinner_medicamentos) as Spinner
        var medicamentoString:String = medicamento.selectedItem.toString()

        var frecuencia:Spinner = findViewById(R.id.spinner_frecuencia) as Spinner
        var frecuenciaString:String = frecuencia.selectedItem.toString()

        var dosis:Spinner = findViewById(R.id.spinner_dosis) as Spinner
        var dosisString:String = dosis.selectedItem.toString()

        var hora:EditText = findViewById(R.id.et_hora) as EditText
        var horaString:String = hora.text.toString()

        var inicio:EditText = findViewById(R.id.inicio_periodo) as EditText
        var inicioString:String = inicio.text.toString()

        var fin:EditText = findViewById(R.id.fin_periodo) as EditText
        var finString:String = fin.text.toString()

        var revisor:Spinner = findViewById(R.id.spinner_revisor) as Spinner
        var revisorString:String = revisor.selectedItem.toString()

        if(medicamentoString.isEmpty() || medicamentoString.equals("Medicamento", true) || frecuenciaString.isEmpty() || frecuenciaString.equals("Frecuencia", true) ||
            dosisString.isEmpty() || dosisString.equals("Dosis", true) || horaString.isEmpty() || inicioString.isEmpty() || finString.isEmpty()) {
            Toast.makeText(this, "No se llenaron todos los campos", Toast.LENGTH_SHORT).show()
            return false
        } else {
            var alarma:Alarma

            if (!revisorString.isEmpty() && !revisorString.equals("Revisor", true)){
                alarma = Alarma(medicamentoString,frecuenciaString,dosisString,horaString,inicioString,finString,revisorString)
            } else {
                alarma = Alarma(medicamentoString,frecuenciaString,dosisString,horaString,inicioString,finString, "")
            }

            mDatabase!!.child("Users").child(id).child("Alarmas").child(cantAlarmas.toString()).setValue(alarma).addOnCompleteListener { task2: Task<Void> ->
                if (task2.isSuccessful()) {
                    Toast.makeText(this, "Alarma registrada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No se pudo crear la alarma", Toast.LENGTH_SHORT).show();
                }
            }

            return true
        }
    }

    fun actualizaCantAlarmas (cantAlarmas:Int){
        var id:String = FirebaseAuth.getInstance().getCurrentUser()!!.getUid();
        var ref:DatabaseReference = mDatabase!!.child("Users").child(id)
        val map = HashMap<String, Any>()
        var newCantAlarmas:Int = (cantAlarmas + 1)
        map.put("cantAlarmas", newCantAlarmas)
        ref.updateChildren(map);
    }

    fun salir(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    /*
    fun guardar() {
        var jsonArray: JSONArray = JSONArray()

        var medicamento:Spinner = findViewById(R.id.spinner_medicamentos) as Spinner
        var medicamentoString:String = medicamento.selectedItem.toString()

        var frecuencia:Spinner = findViewById(R.id.spinner_frecuencia) as Spinner
        var frecuenciaString:String = frecuencia.selectedItem.toString()

        var dosis:Spinner = findViewById(R.id.spinner_dosis) as Spinner
        var dosisString:String = dosis.selectedItem.toString()

        var hora:EditText = findViewById(R.id.et_hora) as EditText
        var horaString:String = hora.text.toString()

        var inicio:EditText = findViewById(R.id.inicio_periodo) as EditText
        var inicioString:String = inicio.text.toString()

        var fin:EditText = findViewById(R.id.fin_periodo) as EditText
        var finString:String = fin.text.toString()

        var revisor:Spinner = findViewById(R.id.spinner_revisor) as Spinner
        var revisorString:String = revisor.selectedItem.toString()

        if(medicamentoString.isEmpty() || frecuenciaString.isEmpty() || dosisString.isEmpty() ||
            horaString.isEmpty() || inicioString.isEmpty() || finString.isEmpty()) {
            Toast.makeText(this, "No se llenaron todos los campos", Toast.LENGTH_SHORT).show()
        } else {

            var j: JSONObject = JSONObject()
            j.put("medicamento", medicamentoString)
            j.put("frecuencia", frecuenciaString)
            j.put("dosis", dosisString)
            j.put("hora", horaString)
            j.put("inicio", inicioString)
            j.put("fin", finString)
            if (!revisorString.isEmpty() && !revisorString.equals("Revisor", true)){
                j.put("revisor", revisorString)
            } else {
                j.put("revisor", "")
            }

            jsonArray.put(MainActivity.alarmas.size, j)

            jsonFile?.saveDataAlarmas(this, jsonArray.toString())

        }

    }

     */

}//Termina clase
