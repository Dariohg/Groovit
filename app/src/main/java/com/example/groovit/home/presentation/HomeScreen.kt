package com.example.groovit.home.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.groovit.home.data.model.EventModel
import com.example.groovit.ui.theme.BackgroundDark
import com.example.groovit.ui.theme.NeonPurple
import com.example.groovit.ui.theme.NeonPurpleLight
import com.example.groovit.ui.theme.TextWhite
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navigateToEventDetail: (String) -> Unit
) {
    val events by homeViewModel.events.observeAsState(emptyList())
    val isLoading by homeViewModel.isLoading.observeAsState(false)
    val error by homeViewModel.error.observeAsState(null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Groovit",
                        color = TextWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark)
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
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(events) { event ->
                        EventCard(
                            event = event,
                            onEventClick = { navigateToEventDetail(event.id.toString()) }
                        )
                    }

                    // Espacio al final para evitar que el último elemento quede cortado
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun EventCard(
    event: EventModel,
    onEventClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val eventDate = event.getFechaAsDate()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEventClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = event.imagen,
                contentDescription = event.titulo,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                error = ColorPainter(Color(0xFF2A2A2A)),
                placeholder = ColorPainter(Color(0xFF2A2A2A)),
                onLoading = {
                    Log.d("ImageLoading", "Cargando imagen: ${event.imagen}")
                },
                onError = {
                    // Versión corregida sin acceder a 'throwable'
                    Log.e("ImageLoading", "Error al cargar imagen: ${event.imagen}")
                }
            )

            // Contenido del evento
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Título
                Text(
                    text = event.titulo,
                    style = MaterialTheme.typography.titleLarge,
                    color = TextWhite,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Descripción
                Text(
                    text = event.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Fecha
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Fecha",
                        tint = NeonPurple,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = dateFormat.format(eventDate),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextWhite
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Ubicación
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Ubicación",
                        tint = NeonPurple,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = event.ubicacion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextWhite
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Precio
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = "Precio",
                        tint = NeonPurple,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$${event.precio}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextWhite
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Capacidad y disponibilidad
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Capacidad",
                        tint = NeonPurple,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${event.lugares_disponibles} lugares disponibles de ${event.capacidad}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextWhite
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de reservar (visual)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(
                            color = NeonPurpleLight,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ver detalles",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}