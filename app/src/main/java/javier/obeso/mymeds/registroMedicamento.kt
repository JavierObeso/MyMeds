package javier.obeso.mymeds

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_registro_medicamento.*

class registroMedicamento : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_medicamento)

        backMedicamento.setOnClickListener(){
            finish()
        }
    }
}
