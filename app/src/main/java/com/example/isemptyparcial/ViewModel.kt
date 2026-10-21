package com.example.isemptyparcial

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.isemptyparcial.data.exchange.ExchangeRateDto
import com.example.isemptyparcial.data.exchange.ExchangeRateRepository
import com.example.isemptyparcial.data.local.ExchangeDatabase
import com.example.isemptyparcial.data.local.FavoritosRepository
import com.example.isemptyparcial.models.ExchangeRateResumen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val exchangeRepository = ExchangeRateRepository()
    private val favoritosRepository = FavoritosRepository(ExchangeDatabase.obtener(application).favoritoDao())

    val favoritosIds: StateFlow<Set<Int>> = favoritosRepository.observarIds()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet()
        )

    val favoritosList: StateFlow<List<ExchangeRateResumen>> = favoritosRepository.observarFavoritos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _ratesState = MutableStateFlow<List<ExchangeRateDto>>(emptyList())
    val ratesState: StateFlow<List<ExchangeRateDto>> = _ratesState.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchExchangeRates()
    }

    fun fetchExchangeRates() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorState.value = null

            try {
                val rates = exchangeRepository.obtenerTiposDeCambio()
                _ratesState.value = rates
            } catch (e: Exception) {
                _errorState.value = "Hubo un error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun alternarFavorito(rateDto: ExchangeRateDto) {
        val base = rateDto.base ?: "USD"
        val quote = rateDto.quote ?: ""
        val rate = rateDto.rate ?: 0.0
        val date = rateDto.date ?: ""

        val id = "$base-$quote".hashCode()
        val esFavorito = id in favoritosIds.value

        viewModelScope.launch {
            if (esFavorito) {
                favoritosRepository.quitar(id)
            } else {
                favoritosRepository.agregar(
                    ExchangeRateResumen(
                        base = base,
                        quote = quote,
                        rate = rate,
                        date = date
                    )
                )
            }
        }
    }

    fun quitarFavorito(resumen: ExchangeRateResumen) {
        val id = "${resumen.base}-${resumen.quote}".hashCode()
        viewModelScope.launch {
            favoritosRepository.quitar(id)
        }
    }
}
