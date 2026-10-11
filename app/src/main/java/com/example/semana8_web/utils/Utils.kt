package com.example.semana8_web.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.semana8_web.actividades.MainActivity
import com.example.semana8_web.apiservices.RetrofitClient
import com.example.semana8_web.clases.Vehiculo
import kotlinx.coroutines.launch
fun base64ToBitmap(base64: String): Bitmap {

    val bytes = Base64.decode(base64, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun uriToBase64(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        Base64.encodeToString(bytes, Base64.NO_WRAP)
    } catch (e: Exception) { null }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaVehiculosScreen() {
    val context = LocalContext.current
    var lista by remember { mutableStateOf(listOf<Vehiculo>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            lista = RetrofitClient.api.getVehiculos()
        } catch (e: Exception) {
            // Manejar error de conexión
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Button(
                    onClick = {
                        // Regresamos a la MainActivity (que es la de agregar)
                        val intent = Intent(context, MainActivity::class.java)
                        // Limpiamos la pila para que no se acumulen actividades
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Regresar a Registrar")
                }
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(lista) { vehiculo ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                val intent = Intent(context, MainActivity::class.java)
                                intent.putExtra("VEHICULO_EXTRA", vehiculo)
                                context.startActivity(intent)
                            }
                    ) {
                        VehiculoItem(v = vehiculo)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
@Composable
fun VehiculoItem(v: Vehiculo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                v.Foto?.let { base64 ->
                    val bitmap = base64ToBitmap(base64)
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Foto de ${v.Modelo}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(Icons.Default.Warning, contentDescription = "Error")
                    }
                } ?: Icon(Icons.Default.Lock, contentDescription = "Sin foto")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = v.Marca,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Modelo: ${v.Modelo}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(text = "Año: ${v.Fabricacion}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Precio: $${v.Precio}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF388E3C), // Verde oscuro
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarVehiculoScreen(
    vehiculoExistente: Vehiculo? = null,
    onVehiculoFinalizado: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var marca by remember { mutableStateOf(vehiculoExistente?.Marca ?: "") }
    var modelo by remember { mutableStateOf(vehiculoExistente?.Modelo ?: "") }
    var precio by remember { mutableStateOf(vehiculoExistente?.Precio?.toString() ?: "") }
    var fabricacion by remember { mutableStateOf(vehiculoExistente?.Fabricacion?.toString() ?: "") }
    var fotoBase64 by remember { mutableStateOf(vehiculoExistente?.Foto) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        uri?.let { fotoBase64 = uriToBase64(context, it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
                .clickable { galleryLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                AsyncImage(model = imageUri, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            } else if (!fotoBase64.isNullOrBlank()) {
                val bitmap = remember(fotoBase64) { base64ToBitmap(fotoBase64!!) }
                Image(bitmap = bitmap.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            } else {
                Text("Toca para foto", textAlign = TextAlign.Center)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = marca, onValueChange = { marca = it }, label = { Text("Marca") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = modelo, onValueChange = { modelo = it }, label = { Text("Modelo") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
        OutlinedTextField(value = fabricacion, onValueChange = { fabricacion = it }, label = { Text("Año") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                scope.launch {
                    try {
                        val p = precio.toDoubleOrNull() ?: 0.0
                        val f = fabricacion.toIntOrNull() ?: 2026
                        if (vehiculoExistente == null) {
                            val nuevoVehiculo = Vehiculo(
                                Id = 0, // El ID no importa para el insert porque es Identity en SQL
                                Marca = marca,
                                Modelo = modelo,
                                Precio = precio.toDoubleOrNull() ?: 0.0,
                                Fabricacion = fabricacion.toIntOrNull() ?: 2026,
                                Foto = fotoBase64 ?: ""
                            )
                            RetrofitClient.api.crear(nuevoVehiculo)
                        } else {
                            val editado = Vehiculo(vehiculoExistente.Id, marca, modelo, p, f, fotoBase64)
                            RetrofitClient.api.actualizar(editado)
                        }
                        onVehiculoFinalizado()
                    } catch (e: Exception) {
                        println(e.message)
                        Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = marca.isNotBlank() && modelo.isNotBlank()
        ) {
            Text(if (vehiculoExistente != null) "Actualizar Cambios" else "Guardar En Azure")
        }

        if (vehiculoExistente != null) {
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    scope.launch {
                        try {
                            RetrofitClient.api.eliminar(vehiculoExistente.Id)
                            Toast.makeText(context, "Vehículo eliminado", Toast.LENGTH_SHORT).show()
                            onVehiculoFinalizado()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(Color.Red))
            ) {
                Text("Eliminar Vehículo")
            }
        }
    }
}