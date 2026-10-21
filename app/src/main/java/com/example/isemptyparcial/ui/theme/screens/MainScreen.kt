package com.example.isemptyparcial.ui.theme.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.isemptyparcial.MainViewModel

// Estética Soft & Warm: Crema, Naranja Suave y Verde Suave
val backgroundColor = Color(0xFFFBF8F3)      // Crema Cálido de fondo
val surfaceColor = Color(0xFFFFFFFF)         // Tarjetas en Blanco Cálido
val textColor = Color(0xFF2C2523)            // Texto Carbón Cálido
val mutedTextColor = Color(0xFF7A6F6B)       // Texto Secundario Suave
val primaryColor = Color(0xFFE07A5F)         // Naranja Terracota Suave
val accentColor = Color(0xFF6B9080)          // Verde Sabia Suave
val cardSecondaryColor = Color(0xFFF3EBE0)   // Arena/Crema para cajas de posts
val euroColor = Color(0xFF5B85AA)            // Azul/Verde Polvo Suave

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    // 0 = Productos (Maper 1), 1 = Posts (Maper 2), 2 = Cambio, 3 = Fav. Productos (DB 1), 4 = Fav. Posts (DB 2)
    var selectedTab by remember { mutableStateOf(0) }

    val favoritosIds by viewModel.favoritosIds.collectAsState()
    val favoritosList by viewModel.favoritosList.collectAsState()

    val favoritosPostIds by viewModel.favoritosPostIds.collectAsState()
    val favoritosPostList by viewModel.favoritosPostList.collectAsState()

    val ratesList by viewModel.ratesState.collectAsState()
    val productsList by viewModel.productsState.collectAsState()
    val combinedPostsList by viewModel.combinedPostsState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorState.collectAsState()

    val titleText = when (selectedTab) {
        0 -> "Productos (DB 1: Tipo Cambio)"
        1 -> "Posts (DB 2: Producto + Post)"
        2 -> "Tipo de Cambio"
        3 -> "Fav. Productos (DB 1)"
        else -> "Fav. Posts (DB 2)"
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = titleText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = primaryColor,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.cargarDatos() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Recargar datos",
                            tint = primaryColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor
                ),
                modifier = Modifier.shadow(4.dp)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = surfaceColor,
                contentColor = textColor
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.ShoppingBag, contentDescription = "Productos") },
                    label = { Text("Productos", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = primaryColor,
                        selectedTextColor = primaryColor,
                        indicatorColor = primaryColor.copy(alpha = 0.15f),
                        unselectedIconColor = Color(0xFF9E928E),
                        unselectedTextColor = Color(0xFF9E928E)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.AutoMirrored.Filled.Article, contentDescription = "Posts") },
                    label = { Text("Posts", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = primaryColor,
                        selectedTextColor = primaryColor,
                        indicatorColor = primaryColor.copy(alpha = 0.15f),
                        unselectedIconColor = Color(0xFF9E928E),
                        unselectedTextColor = Color(0xFF9E928E)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Cambio") },
                    label = { Text("Cambio", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = primaryColor,
                        selectedTextColor = primaryColor,
                        indicatorColor = primaryColor.copy(alpha = 0.15f),
                        unselectedIconColor = Color(0xFF9E928E),
                        unselectedTextColor = Color(0xFF9E928E)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Fav Productos") },
                    label = { Text("Fav Prod", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = primaryColor,
                        selectedTextColor = primaryColor,
                        indicatorColor = primaryColor.copy(alpha = 0.15f),
                        unselectedIconColor = Color(0xFF9E928E),
                        unselectedTextColor = Color(0xFF9E928E)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Default.Bookmark, contentDescription = "Fav Posts") },
                    label = { Text("Fav Posts", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = accentColor,
                        selectedTextColor = accentColor,
                        indicatorColor = accentColor.copy(alpha = 0.15f),
                        unselectedIconColor = Color(0xFF9E928E),
                        unselectedTextColor = Color(0xFF9E928E)
                    )
                )
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 -> ProductsScreen(
                productsList = productsList,
                favoritosIds = favoritosIds,
                isLoading = isLoading,
                error = error,
                onToggleFavorite = { product -> viewModel.alternarFavoritoProducto(product) },
                onRefresh = { viewModel.cargarDatos() },
                modifier = Modifier.padding(innerPadding)
            )

            1 -> PostsScreen(
                combinedPostsList = combinedPostsList,
                favoritosPostIds = favoritosPostIds,
                isLoading = isLoading,
                error = error,
                onToggleFavorite = { item -> viewModel.alternarFavoritoProductoPost(item) },
                onRefresh = { viewModel.cargarDatos() },
                modifier = Modifier.padding(innerPadding)
            )

            2 -> ExchangeScreen(
                ratesList = ratesList,
                isLoading = isLoading,
                error = error,
                onFetchRates = { viewModel.cargarDatos() },
                modifier = Modifier.padding(innerPadding)
            )

            3 -> ProductFavoritesScreen(
                favoritosList = favoritosList,
                onQuitar = { id -> viewModel.quitarFavoritoProducto(id) },
                modifier = Modifier.padding(innerPadding)
            )

            4 -> PostFavoritesScreen(
                favoritosPostList = favoritosPostList,
                onQuitar = { id -> viewModel.quitarFavoritoProductoPost(id) },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
