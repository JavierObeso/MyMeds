package javier.obeso.mymeds

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat


class registroAlarma : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_alarma)

        configurarSpinnerMedicamentos()
        configurarSpinnerFrecuencia()
        configurarSpinnerDosis()
        configurarSpinnerHora()
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
    fun configurarSpinnerHora() {
        val horas = resources.getStringArray(R.array.arreglo_hora)

        val spinner = findViewById<Spinner>(R.id.spinner_hora)
        if (spinner != null) {
            //val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, medicamentos)
            val adapter = ArrayAdapter(this, R.layout.spinner_item, horas)
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    Toast.makeText(this@registroAlarma, getString(R.string.selected_item) + " " +
                            "" + horas[position], Toast.LENGTH_SHORT).show()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
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
