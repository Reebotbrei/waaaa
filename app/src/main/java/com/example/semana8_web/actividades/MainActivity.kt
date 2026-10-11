package com.example.semana8_web.actividades

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.semana8_web.clases.Vehiculo
import com.example.semana8_web.ui.theme.Semana8_WebTheme

import com.example.semana8_web.utils.AgregarVehiculoScreen
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val vehiculoParaEditar = intent.getSerializableExtra("VEHICULO_EXTRA") as? Vehiculo
        setContent {
            MainScreen(
                vehiculoParaEditar = vehiculoParaEditar,
                onNavegarLista = {
                    val intent = Intent(this, MostrarActivity2::class.java)
                    startActivity(intent)
                },
                onFinalizarOperacion = {
                    val intent = Intent(this, MostrarActivity2::class.java)
                    startActivity(intent)
                    finish()
                }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    vehiculoParaEditar: Vehiculo?,
    onNavegarLista: () -> Unit,
    onFinalizarOperacion: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {Text(if(vehiculoParaEditar!=null) "Editar Vehículo" else "Nuevo Vehículo")},
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Box(modifier = Modifier.weight(1f)){
                AgregarVehiculoScreen(
                    vehiculoExistente = vehiculoParaEditar,
                    onVehiculoFinalizado = onFinalizarOperacion
                )
            }
            if (vehiculoParaEditar == null){
                Button(
                    onClick = {onNavegarLista()},
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Ver Inventario Completo")
                }
            }
        }
    }
}