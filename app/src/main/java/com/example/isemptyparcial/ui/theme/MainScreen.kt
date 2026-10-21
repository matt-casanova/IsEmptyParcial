package com.example.isemptyparcial.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.isemptyparcial.MainViewModel
import com.example.isemptyparcial.data.exchange.ExchangeRateDto
import com.example.isemptyparcial.models.ExchangeRateResumen

val backgroundColor = Color(0xFF121212)
val surfaceColor = Color(0xFF1E1E1E)
val textColor = Color(0xFFF5F5F5)
val primaryColor = Color(0xFFE91E63)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    var showingFavorites by remember { mutableStateOf(false) }

    val favoritosIds by viewModel.favoritosIds.collectAsState()
    val favoritosList by viewModel.favoritosList.collectAsState()
    val ratesList by viewModel.ratesState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (showingFavorites) "Favoritos" else "Tipo de Cambio",
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        color = primaryColor,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    IconButton(onClick = { showingFavorites = !showingFavorites }) {
                        Icon(
                            imageVector = if (showingFavorites) Icons.Default.Info else Icons.Default.Favorite,
                            contentDescription = if (showingFavorites) "Ver tipos de cambio" else "Ver favoritos",
                            tint = primaryColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor
                ),
                modifier = Modifier.shadow(8.dp)
            )
        }
    ) { innerPadding ->
        if (showingFavorites) {
            ExchangeFavoritesContent(
                favoritosList = favoritosList,
                onQuitar = { resumen -> viewModel.quitarFavorito(resumen) },
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            ExchangeScreenContent(
                ratesList = ratesList,
                isLoading = isLoading,
                error = error,
                favoritosIds = favoritosIds,
                onFetchRates = { viewModel.fetchExchangeRates() },
                onToggleFavorite = { rateDto -> viewModel.alternarFavorito(rateDto) },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun ExchangeScreenContent(
    ratesList: List<ExchangeRateDto>,
    isLoading: Boolean,
    error: String?,
    favoritosIds: Set<Int>,
    onFetchRates: () -> Unit,
    onToggleFavorite: (ExchangeRateDto) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Obteniendo tipos de cambio...",
                        color = textColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error,
                        color = Color.Red,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            ratesList.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Presiona ACTUALIZAR para obtener los tipos de cambio",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(ratesList) { item ->
                        val base = item.base ?: "USD"
                        val quote = item.quote ?: ""
                        val rate = item.rate ?: 0.0
                        val date = item.date ?: ""
                        val id = "$base-$quote".hashCode()
                        val esFavorito = id in favoritosIds

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = surfaceColor)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "$base ➔ $quote",
                                        color = primaryColor,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "1 $base = $rate $quote",
                                        color = textColor,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                    if (date.isNotBlank()) {
                                        Text(
                                            text = "Fecha: $date",
                                            color = Color.Gray,
                                            fontSize = 13.sp,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }

                                IconButton(onClick = { onToggleFavorite(item) }) {
                                    Icon(
                                        imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = if (esFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                                        tint = if (esFavorito) Color(0xFFE91E63) else Color.Gray,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = onFetchRates,
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryColor,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(8.dp, RoundedCornerShape(12.dp))
        ) {
            Text(
                text = "ACTUALIZAR",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        }
    }
}

@Composable
fun ExchangeFavoritesContent(
    favoritosList: List<ExchangeRateResumen>,
    onQuitar: (ExchangeRateResumen) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tipos de Cambio Favoritos",
            color = textColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (favoritosList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún no tienes tipos de cambio guardados como favoritos.\nToca el ícono de corazón para guardar uno.",
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoritosList) { favorito ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = surfaceColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "${favorito.base} ➔ ${favorito.quote}",
                                    color = primaryColor,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "1 ${favorito.base} = ${favorito.rate} ${favorito.quote}",
                                    color = textColor,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                if (favorito.date.isNotBlank()) {
                                    Text(
                                        text = "Fecha: ${favorito.date}",
                                        color = Color.Gray,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }

                            IconButton(onClick = { onQuitar(favorito) }) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Quitar de favoritos",
                                    tint = Color(0xFFE91E63),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
