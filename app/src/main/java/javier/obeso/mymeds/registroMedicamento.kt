package javier.obeso.mymeds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javier.obeso.mymeds.entidades.Medicamento
import kotlinx.android.synthetic.main.activity_registro_medicamento.*

class registroMedicamento : AppCompatActivity() {

    var cantMeds:Int = 0
    var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_medicamento)

        mDatabase = FirebaseDatabase.getInstance().getReference()
        cantMeds = MainActivity.medicamentos.size

        var atras: ImageButton = findViewById(R.id.backMed) as ImageButton

        atras.setOnClickListener(){
            finish()
        }

        boton_guardar_medicamento.setOnClickListener(){
            if (llenaMedicamento(cantMeds)){
                actualizaCantMeds(cantMeds)
            }
        }
    }

    fun llenaMedicamento (cantMeds:Int):Boolean{
        var id:String = FirebaseAuth.getInstance().getCurrentUser()!!.getUid();

        val nombreMed:String = nombre_medicamento.text.toString()
        val cantidad:String = cantidad_medicamento.text.toString()
        val caducidad: String = caducidad_medicamento.text.toString()

        if(nombreMed.isEmpty() || cantidad.isEmpty() || caducidad.isEmpty()) {
            Toast.makeText(this, "No se llenaron todos los campos", Toast.LENGTH_SHORT).show()
            return false
        } else {
            var medicamento:Medicamento
            medicamento = Medicamento(nombreMed, cantidad, caducidad)

            mDatabase!!.child("Users").child(id).child("Medicamentos").child(cantMeds.toString()).setValue(medicamento).addOnCompleteListener { task2: Task<Void> ->
                if (task2.isSuccessful()) {
                    Toast.makeText(this, "Medicamento registrado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No se pudo crear el medicamento", Toast.LENGTH_SHORT).show();
                }
            }

            return true
        }
    }

    fun actualizaCantMeds (cantMeds: Int){
        var id:String = FirebaseAuth.getInstance().getCurrentUser()!!.getUid();
        var ref:DatabaseReference = mDatabase!!.child("Users").child(id)
        val map = HashMap<String, Any>()
        var newCantMeds:Int = (cantMeds + 1)
        map.put("cantMedicamentos", newCantMeds)
        ref.updateChildren(map);
    }

}
