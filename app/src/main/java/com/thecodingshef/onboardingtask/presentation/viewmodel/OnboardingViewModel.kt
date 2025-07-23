package com.thecodingshef.onboardingtask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.thecodingshef.onboardingtask.core.base.Resource
import com.thecodingshef.onboardingtask.core.utils.resJson
import com.thecodingshef.onboardingtask.data.models.ManualBuyEducationData
import com.thecodingshef.onboardingtask.data.models.OnboardingResponse
import com.thecodingshef.onboardingtask.domain.GetOnboardingDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getOnboardingDataUseCase: GetOnboardingDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val _currentCardIndex = MutableStateFlow(0)
    val currentCardIndex: StateFlow<Int> = _currentCardIndex.asStateFlow()

    private val _animationPhase = MutableStateFlow(AnimationPhase.INITIAL)
    val animationPhase: StateFlow<AnimationPhase> = _animationPhase.asStateFlow()

    init {
        fetchOnboardingData()
    }


    // for testing
    fun fetchTestData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            delay(1000)
            val resData =
                Gson().fromJson<OnboardingResponse>(resJson, OnboardingResponse::class.java)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                data = resData?.data?.manualBuyEducationData,
                error = null
            )
        }
    }

    private fun fetchOnboardingData() {
        viewModelScope.launch {
            getOnboardingDataUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                    }

                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            data = resource.data?.data?.manualBuyEducationData,
                            error = null
                        )
                        // Start the animation sequence
                        _animationPhase.value = AnimationPhase.INTRO_DISPLAY
                    }

                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = resource.message
                        )
                    }
                }
            }
        }
    }

    fun updateCurrentCardIndex(index: Int) {
        _currentCardIndex.value = index
    }

    fun updateAnimationPhase(phase: AnimationPhase) {
        _animationPhase.value = phase
    }

    fun retryFetch() {
        _uiState.value = OnboardingUiState()
        _currentCardIndex.value = 0
        _animationPhase.value = AnimationPhase.INITIAL
        fetchOnboardingData()
    }

    fun resetAnimation() {
        _currentCardIndex.value = 0
        _animationPhase.value = AnimationPhase.INTRO_DISPLAY
    }
}

data class OnboardingUiState(
    val isLoading: Boolean = false,
    val data: ManualBuyEducationData? = null,
    val error: String? = null
)

enum class AnimationPhase {
    INITIAL,
    INTRO_DISPLAY,
    TITLE_COLLAPSE,
    CARDS_ANIMATION,
    COMPLETED
}

