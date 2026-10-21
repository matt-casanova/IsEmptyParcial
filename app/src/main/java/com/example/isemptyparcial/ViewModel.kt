package com.example.isemptyparcial

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.isemptyparcial.data.aProductUi
import com.example.isemptyparcial.data.combinarProductoYPublicacion
import com.example.isemptyparcial.data.exchange.ExchangeRateDto
import com.example.isemptyparcial.data.exchange.ExchangeRateRepository
import com.example.isemptyparcial.data.local.ExchangeDatabase
import com.example.isemptyparcial.data.local.FavoritosPostRepository
import com.example.isemptyparcial.data.local.FavoritosRepository
import com.example.isemptyparcial.data.local.PostDatabase
import com.example.isemptyparcial.data.post.PostDto
import com.example.isemptyparcial.data.post.PostRepository
import com.example.isemptyparcial.data.product.ProductRepository
import com.example.isemptyparcial.models.ProductUi
import com.example.isemptyparcial.models.ProductoConPublicacionUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val exchangeRepository = ExchangeRateRepository()
    private val productRepository = ProductRepository()
    private val postRepository = PostRepository()

    // DB 1: Productos + Tipo de Cambio
    private val favoritosRepository = FavoritosRepository(ExchangeDatabase.obtener(application).favoritoDao())

    // DB 2: Productos + Posts
    private val favoritosPostRepository = FavoritosPostRepository(PostDatabase.obtener(application).favoritoPostDao())

    // DB 1 Flows (Productos + Tipo Cambio)
    val favoritosIds: StateFlow<Set<Int>> = favoritosRepository.observarIds()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet()
        )

    val favoritosList: StateFlow<List<ProductUi>> = favoritosRepository.observarFavoritos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // DB 2 Flows (Productos + Posts)
    val favoritosPostIds: StateFlow<Set<Int>> = favoritosPostRepository.observarIds()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet()
        )

    val favoritosPostList: StateFlow<List<ProductoConPublicacionUi>> = favoritosPostRepository.observarFavoritos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _ratesState = MutableStateFlow<List<ExchangeRateDto>>(emptyList())
    val ratesState: StateFlow<List<ExchangeRateDto>> = _ratesState.asStateFlow()

    // Maper 1: Productos con Tipo de Cambio (sin post)
    private val _productsState = MutableStateFlow<List<ProductUi>>(emptyList())
    val productsState: StateFlow<List<ProductUi>> = _productsState.asStateFlow()

    // Listado original de posts
    private val _postsState = MutableStateFlow<List<PostDto>>(emptyList())
    val postsState: StateFlow<List<PostDto>> = _postsState.asStateFlow()

    // Maper 2: Productos combinados con Posts (sin tipo de cambio)
    private val _combinedPostsState = MutableStateFlow<List<ProductoConPublicacionUi>>(emptyList())
    val combinedPostsState: StateFlow<List<ProductoConPublicacionUi>> = _combinedPostsState.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorState.value = null

            try {
                // 1. Obtener tipos de cambio
                val rates = exchangeRepository.obtenerTiposDeCambio()
                _ratesState.value = rates

                val rateBob = rates.find { it.quote == "BOB" }?.rate
                val rateEur = rates.find { it.quote == "EUR" }?.rate

                // 2. Obtener productos
                val rawProducts = productRepository.obtenerProductos()

                // MAPER 1: Productos con tipo de cambio (sin post)
                val productsWithExchange = rawProducts.map { it.aProductUi(rateBob, rateEur) }
                _productsState.value = productsWithExchange

                // 3. Obtener posts
                val rawPosts = postRepository.obtenerPublicaciones()
                _postsState.value = rawPosts

                // MAPER 2: Productos combinados con post (sin tipo de cambio)
                val productsWithPosts = rawProducts.mapIndexed { index, product ->
                    val post = rawPosts.getOrNull(index)
                    combinarProductoYPublicacion(product, post)
                }
                _combinedPostsState.value = productsWithPosts

            } catch (e: Exception) {
                _errorState.value = "Hubo un error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Funciones DB 1 (Productos + Cambio)
    fun alternarFavoritoProducto(product: ProductUi) {
        val esFavorito = product.id in favoritosIds.value
        viewModelScope.launch {
            if (esFavorito) {
                favoritosRepository.quitar(product.id)
            } else {
                favoritosRepository.agregar(product)
            }
        }
    }

    fun quitarFavoritoProducto(id: Int) {
        viewModelScope.launch {
            favoritosRepository.quitar(id)
        }
    }

    // Funciones DB 2 (Productos + Posts)
    fun alternarFavoritoProductoPost(item: ProductoConPublicacionUi) {
        val esFavorito = item.productoId in favoritosPostIds.value
        viewModelScope.launch {
            if (esFavorito) {
                favoritosPostRepository.quitar(item.productoId)
            } else {
                favoritosPostRepository.agregar(item)
            }
        }
    }

    fun quitarFavoritoProductoPost(id: Int) {
        viewModelScope.launch {
            favoritosPostRepository.quitar(id)
        }
    }
}
