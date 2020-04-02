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
import java.text.SimpleDateFormat
import java.util.*


class registroAlarma : AppCompatActivity() {

    val timeSetListener = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_alarma)

        configurarSpinnerMedicamentos()
        configurarSpinnerFrecuencia()
        configurarSpinnerDosis()
        configurarEditTextHora()
        configurarEditTextInicio()
        configurarEditTextFin()
        configurarSpinnerRevisor()

    }//Termina metodo onCreate()

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





}//Termina clase
