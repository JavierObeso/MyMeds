package javier.obeso.mymeds

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import javier.obeso.mymeds.entidades.Alarma
import javier.obeso.mymeds.utilities.JSONFile
import kotlinx.android.synthetic.main.activity_registro_alarma.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class registroAlarma : AppCompatActivity() {

    var jsonFile: JSONFile? = null
    val timeSetListener = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_alarma)

        jsonFile = JSONFile()

        configurarSpinnerMedicamentos()
        configurarSpinnerFrecuencia()
        configurarSpinnerDosis()
        configurarEditTextHora()
        configurarEditTextInicio()
        configurarEditTextFin()
        configurarSpinnerRevisor()

        back.setOnClickListener(){
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

            guardar()

            Toast.makeText(this, "Se añadió la alarma", Toast.LENGTH_SHORT).show()
        }

    }

    fun configurarSpinnerMedicamentos() {
        val medicamentos = resources.getStringArray(R.array.arreglo_medicamentos)

        val spinner = findViewById<Spinner>(R.id.spinner_medicamentos)
        if (spinner != null) {
            //val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, medicamentos)

            val adapter = ArrayAdapter(this, R.layout.spinner_item, medicamentos)
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    Toast.makeText(this@registroAlarma, getString(R.string.selected_item) + " " +
                            "" + medicamentos[position], Toast.LENGTH_SHORT).show()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {

                }

            }

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

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val tv: TextView = view as TextView
                    if(position == 0) {
                        tv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.light_gray, null))
                    } else {
                        Toast.makeText(this@registroAlarma, getString(R.string.selected_item) + " " +
                                "" + frecuencias[position], Toast.LENGTH_SHORT).show()
                    }

                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
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

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    Toast.makeText(this@registroAlarma, getString(R.string.selected_item) + " " +
                            "" + dosis[position], Toast.LENGTH_SHORT).show()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
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

}//Termina clase
