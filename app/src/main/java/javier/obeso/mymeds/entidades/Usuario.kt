package javier.obeso.mymeds.entidades

data class Usuario (var name: String, var email: String, var password:String, var cantAlarmas:Int, var cantMedicamentos:Int, var medicamentosTomados:Int, var medicamentosNoTomados:Int)