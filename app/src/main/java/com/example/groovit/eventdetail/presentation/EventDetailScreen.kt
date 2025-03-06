package com.example.groovit.eventdetail.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.groovit.ui.theme.BackgroundDark
import com.example.groovit.ui.theme.NeonPurple
import com.example.groovit.ui.theme.NeonPurpleLight
import com.example.groovit.ui.theme.TextWhite
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventDetailViewModel: EventDetailViewModel,
    eventId: String,
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit
) {
    val event by eventDetailViewModel.event.observeAsState(initial = null)
    val isLoading by eventDetailViewModel.isLoading.observeAsState(false)
    val error by eventDetailViewModel.error.observeAsState(null)
    // Cambiado de 1 a 0 para que inicie en cero
    val ticketCount by eventDetailViewModel.ticketCount.observeAsState(0)
    val totalPrice by eventDetailViewModel.totalPrice.observeAsState(0.0)
    val purchaseSuccess by eventDetailViewModel.purchaseSuccess.observeAsState(false)
    val purchaseMessage by eventDetailViewModel.purchaseMessage.observeAsState(null)

    // Cargar el evento cuando se inicie la pantalla
    LaunchedEffect(eventId) {
        eventDetailViewModel.loadEvent(eventId)
    }
    LaunchedEffect(ticketCount, totalPrice) {
        println("UI actualizada - Tickets: $ticketCount, Precio: $totalPrice")
    }


    // Diálogo de compra exitosa
    if (purchaseSuccess) {
        AlertDialog(
            onDismissRequest = {
                eventDetailViewModel.resetPurchaseState()
                navigateToHome()
            },
            title = {
                Text(
                    text = "¡Compra Exitosa!",
                    color = Color.Black
                )
            },
            text = {
                Text(
                    text = purchaseMessage ?: "",
                    color = Color.Black
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    eventDetailViewModel.resetPurchaseState()
                    navigateToHome()
                }) {
                    Text(
                        text = "Aceptar",
                        color = NeonPurple
                    )
                }
            },
            containerColor = Color.White
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = event?.titulo ?: "Detalles del Evento",
                        color = TextWhite,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1A1A1A)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212)) // Cambiado de BackgroundDark a un valor explícito
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = NeonPurple
                )
            } else if (error != null) {
                Text(
                    text = error ?: "Error desconocido",
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            } else if (event != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {

                    AsyncImage(
                        model = event?.imagen,
                        contentDescription = event?.titulo,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        error = ColorPainter(Color(0xFF2A2A2A)),
                        placeholder = ColorPainter(Color(0xFF2A2A2A))
                    )

                    // Información del evento
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Título
                        Text(
                            text = event?.titulo ?: "",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextWhite,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Descripción
                        Text(
                            text = event?.descripcion ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.LightGray
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Información adicional
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF1E1E1E)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                // Fecha
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Fecha",
                                        tint = NeonPurple,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Fecha y Hora",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                        Text(
                                            text = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(event?.getFechaAsDate()),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = TextWhite
                                        )
                                    }
                                }

                                Divider(
                                    color = Color(0xFF2A2A2A),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )

                                // Ubicación
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = "Ubicación",
                                        tint = NeonPurple,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Ubicación",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                        Text(
                                            text = event?.ubicacion ?: "",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = TextWhite
                                        )
                                    }
                                }

                                Divider(
                                    color = Color(0xFF2A2A2A),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )

                                // Precio
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AttachMoney,
                                        contentDescription = "Precio",
                                        tint = NeonPurple,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Precio por Lugar",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                        Text(
                                            text = "$${event?.precio}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = TextWhite
                                        )
                                    }
                                }

                                Divider(
                                    color = Color(0xFF2A2A2A),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )

                                // Capacidad
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Capacidad",
                                        tint = NeonPurple,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Disponibilidad",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                        Text(
                                            text = "${event?.lugares_disponibles} lugares disponibles de ${event?.capacidad}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = TextWhite
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Sección de compra de boletos
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF252525)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ){
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Comprar Boletos",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = TextWhite,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Selector de cantidad de boletos
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Cantidad:",
                                        color = TextWhite,
                                        style = MaterialTheme.typography.bodyLarge
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            onClick = { eventDetailViewModel.decrementTicketCount() },
                                            modifier = Modifier
                                                .size(40.dp)
                                                .background(
                                                    color = NeonPurple,
                                                    shape = CircleShape
                                                )
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Remove,
                                                contentDescription = "Decrementar",
                                                tint = TextWhite
                                            )
                                        }

                                        Text(
                                            text = "$ticketCount",
                                            color = TextWhite,
                                            modifier = Modifier.padding(horizontal = 16.dp),
                                            style = MaterialTheme.typography.titleMedium
                                        )

                                        IconButton(
                                            onClick = { eventDetailViewModel.incrementTicketCount() },
                                            modifier = Modifier
                                                .size(40.dp)
                                                .background(
                                                    color = NeonPurple,
                                                    shape = CircleShape
                                                )
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Incrementar",
                                                tint = TextWhite
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Total a pagar
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Total a pagar:",
                                        color = TextWhite,
                                        style = MaterialTheme.typography.bodyLarge
                                    )

                                    Text(
                                        text = NumberFormat.getCurrencyInstance(Locale.US).format(totalPrice),
                                        color = NeonPurpleLight,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                // Botón de compra
                                Button(
                                    onClick = { eventDetailViewModel.purchaseTickets() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    // Modificado para deshabilitar cuando ticketCount es 0
                                    enabled = !isLoading && ticketCount > 0 && (event?.lugares_disponibles ?: 0) >= ticketCount,                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent,
                                        disabledContainerColor = Color.Gray.copy(alpha = 0.5f)
                                    ),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                brush = Brush.horizontalGradient(
                                                    colors = listOf(NeonPurple, NeonPurpleLight)
                                                ),
                                                shape = RoundedCornerShape(12.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isLoading) {
                                            CircularProgressIndicator(
                                                color = TextWhite,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        } else {
                                            Text(
                                                text = "COMPRAR AHORA",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextWhite
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}