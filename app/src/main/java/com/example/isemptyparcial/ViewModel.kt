package com.example.isemptyparcial

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.isemptyparcial.data.aProductUi
import com.example.isemptyparcial.data.exchange.ExchangeRateDto
import com.example.isemptyparcial.data.exchange.ExchangeRateRepository
import com.example.isemptyparcial.data.local.ExchangeDatabase
import com.example.isemptyparcial.data.local.FavoritosRepository
import com.example.isemptyparcial.data.product.ProductRepository
import com.example.isemptyparcial.models.ProductUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val exchangeRepository = ExchangeRateRepository()
    private val productRepository = ProductRepository()
    private val favoritosRepository = FavoritosRepository(ExchangeDatabase.obtener(application).favoritoDao())

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

    private val _ratesState = MutableStateFlow<List<ExchangeRateDto>>(emptyList())
    val ratesState: StateFlow<List<ExchangeRateDto>> = _ratesState.asStateFlow()

    private val _productsState = MutableStateFlow<List<ProductUi>>(emptyList())
    val productsState: StateFlow<List<ProductUi>> = _productsState.asStateFlow()

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
                val rates = exchangeRepository.obtenerTiposDeCambio()
                _ratesState.value = rates

                val rateBob = rates.find { it.quote == "BOB" }?.rate
                val rateEur = rates.find { it.quote == "EUR" }?.rate

                val rawProducts = productRepository.obtenerProductos()
                val mappedProducts = rawProducts.map { it.aProductUi(rateBob, rateEur) }
                _productsState.value = mappedProducts

            } catch (e: Exception) {
                _errorState.value = "Hubo un error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

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
}
