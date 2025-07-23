package com.thecodingshef.onboardingtask.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.thecodingshef.onboardingtask.core.utils.CommonUtil.toComposeColorSafely
import com.thecodingshef.onboardingtask.data.models.EducationCard
import com.thecodingshef.onboardingtask.data.models.ManualBuyEducationData
import com.thecodingshef.onboardingtask.presentation.components.SpacerHeight
import com.thecodingshef.onboardingtask.presentation.viewmodel.AnimationPhase
import com.thecodingshef.onboardingtask.presentation.viewmodel.OnboardingViewModel
import kotlinx.coroutines.delay

@Composable
fun ObScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onSaveInGold: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentCardIndex by viewModel.currentCardIndex.collectAsState()

    when {
        uiState.isLoading -> {
            LoadingScreen()
        }

        uiState.error != null -> {
            ErrorScreen(
                error = uiState.error!!, onRetry = { viewModel.retryFetch() })
        }

        uiState.data != null -> {
            // SlideHalfVisibleCard()
            OnboardingContent(
                data = uiState.data!!,
                currentCardIndex = currentCardIndex,
                onCardIndexChange = { viewModel.updateCurrentCardIndex(it) },
                onNavigateBack = onNavigateBack,
                onSaveInGold = onSaveInGold
            )
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A202C)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
private fun ErrorScreen(
    error: String, onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A202C)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Error: $error", color = Color.White, textAlign = TextAlign.Center
            )
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
private fun OnboardingContent(
    data: ManualBuyEducationData,
    currentCardIndex: Int,
    onCardIndexChange: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    onSaveInGold: () -> Unit
) {
    // Animation states for title and subtitle
    val titleAlpha = remember { Animatable(1f) }
    val subtitleAlpha = remember { Animatable(1f) }
    val titleTranslationY = remember { Animatable(0f) }
    val subtitleTranslationY = remember { Animatable(0f) }

    // Button animation
    val buttonTranslationY = remember { Animatable(200f) }
    val buttonAlpha = remember { Animatable(0f) }

    // Animation sequence controller
    var animationPhase by remember { mutableStateOf(AnimationPhase.INTRO_DISPLAY) }
    var currentAnimatingIndex by remember { mutableIntStateOf(0) }

    // Main animation sequence
    LaunchedEffect(data) {
        // Phase 1: Show intro for specified time
        delay(data.collapseExpandIntroInterval.toLong())
        animationPhase = AnimationPhase.TITLE_COLLAPSE

        // Collapse title and subtitle
        titleAlpha.animateTo(0f, tween(300))
        subtitleAlpha.animateTo(0f, tween(300))
        titleTranslationY.animateTo(-50f, tween(300))
        subtitleTranslationY.animateTo(-50f, tween(300))

        delay(300)
        animationPhase = AnimationPhase.CARDS_ANIMATION

        // Animate button in from bottom
        buttonTranslationY.animateTo(0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        buttonAlpha.animateTo(1f, tween(400))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF4A154B), // Deep purple center
                        Color(0xFF2D1B2E), // Darker purple
                        Color(0xFF1A0F1B)  // Almost black
                    ), radius = 1200f, center = Offset(0.5f, 0.3f)
                )
            )
    ) {
        // Top toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(top = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = data.toolBarText.orEmpty(),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.weight(1f))

            AsyncImage(
                model = data.toolBarIcon,
                contentDescription = "Toolbar Icon",
                modifier = Modifier.size(24.dp)
            )
        }

        // Animated intro section
        if (animationPhase == AnimationPhase.INTRO_DISPLAY) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 120.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = data.introTitle.orEmpty(),
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.graphicsLayer {
                        alpha = titleAlpha.value
                        translationY = titleTranslationY.value
                    })

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.graphicsLayer {
                        alpha = subtitleAlpha.value
                        translationY = subtitleTranslationY.value
                    }) {
                    Text(
                        text = data.introSubtitle.orEmpty(),
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    AsyncImage(
                        model = data.introSubtitleIcon,
                        contentDescription = "Lightning Icon",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        // Cards section
        if (animationPhase == AnimationPhase.CARDS_ANIMATION) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .padding(top = 100.dp, bottom = 140.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                data.educationCardList?.forEachIndexed { index, card ->
                    var isVisible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        delay(index * 2000L)
                        isVisible = true
                        onCardIndexChange(index)
                    }

                    EducationCard(
                        card = card,
                        isExpanded = index == currentCardIndex,
                        isVisible = isVisible,
                        modifier = Modifier.fillMaxWidth()
                    )

                }
            }
        }

        // Bottom CTA Button with animation
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(20.dp)
                .graphicsLayer {
                    translationY = buttonTranslationY.value
                    alpha = buttonAlpha.value
                }) {
            Button(
                onClick = onSaveInGold,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = data.saveButtonCta?.backgroundColor.toComposeColorSafely()
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = data.saveButtonCta?.text.orEmpty(),
                    color = data.saveButtonCta?.textColor.toComposeColorSafely(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


@Composable
private fun EducationCard(
    card: EducationCard,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    isVisible: Boolean = false
) {

    // Animate image size for both states
    val imageSize by animateDpAsState(
        targetValue = if (isExpanded) 400.dp else 60.dp,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "imageSize"
    )

    val density = LocalDensity.current

    val cardHeight = 200.dp
    val screenHeightPx = with(density) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    }
    val cardHeightPx = with(density) { cardHeight.toPx() }

    val halfVisibleOffsetPx = screenHeightPx - (cardHeightPx / 2)
    // Animate in pixels, not dp
    val offsetY by animateFloatAsState(
        targetValue = if (isVisible) 0f else halfVisibleOffsetPx,
        animationSpec = tween(durationMillis = 600),
        label = "offsetY"
    )

    Card(
        modifier = modifier
            .offset { IntOffset(0, offsetY.toInt()) }
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isExpanded) 16.dp else 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            card.startGradient.toComposeColorSafely(),
                            card.endGradient.toComposeColorSafely()
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    ), shape = RoundedCornerShape(20.dp)
                )
                .border(
                    width = 1.dp, brush = Brush.linearGradient(
                        colors = listOf(
                            card.strokeStartColor.toComposeColorSafely(),
                            card.strokeEndColor.toComposeColorSafely()
                        )
                    ), shape = RoundedCornerShape(20.dp)
                )
        ) {
            Column {
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn(animationSpec = tween(400)) + expandVertically(
                        animationSpec = tween(
                            600
                        )
                    ),
                    exit = fadeOut(animationSpec = tween(400)) + shrinkVertically(
                        animationSpec = tween(
                            600
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.6f)
                                    )
                                ), shape = RoundedCornerShape(20.dp)
                            )
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = card.image,
                            contentDescription = "Card Image",
                            modifier = Modifier
                                .size(imageSize) // Use animated size for both width and height
                                .clip(RoundedCornerShape(12))
                                .animateContentSize(
                                    animationSpec = tween(
                                        durationMillis = 600,
                                        easing = FastOutSlowInEasing
                                    )
                                ),
                            contentScale = ContentScale.Fit
                        )

                        SpacerHeight(12.dp)
                        Text(
                            text = card.expandStateText.orEmpty(),
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 28.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                        )
                    }
                }

                AnimatedVisibility(
                    visible = !isExpanded,
                    enter = fadeIn(animationSpec = tween(400)) + expandVertically(
                        animationSpec = tween(
                            600
                        )
                    ),
                    exit = fadeOut(animationSpec = tween(400)) + shrinkVertically(
                        animationSpec = tween(
                            600
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = card.image,
                            contentDescription = "Card Image",
                            modifier = Modifier
                                .size(imageSize) // Use same animated size
                                .clip(CircleShape)
                                .animateContentSize(
                                    animationSpec = tween(
                                        durationMillis = 600,
                                        easing = FastOutSlowInEasing
                                    )
                                ),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = card.collapsedStateText.orEmpty(),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

fun Int?.orDefault(default: Int = 1000): Int = this ?: default


@Composable
fun SlideHalfVisibleCard() {
    val context = LocalContext.current
    val density = LocalDensity.current

    val cardHeight = 200.dp
    val screenHeightPx = with(density) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    }
    val cardHeightPx = with(density) { cardHeight.toPx() }

    val halfVisibleOffsetPx = screenHeightPx - (cardHeightPx / 2)

    var isExpanded by remember { mutableStateOf(false) }

    // Animate in pixels, not dp
    val offsetY by animateFloatAsState(
        targetValue = if (isExpanded) 0f else halfVisibleOffsetPx,
        animationSpec = tween(durationMillis = 600),
        label = "offsetY"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        Card(
            modifier = Modifier
                .size(cardHeight)
                .offset { IntOffset(0, offsetY.toInt()) }
                .align(Alignment.TopCenter)
                .clickable { isExpanded = !isExpanded },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isExpanded) "Tap to Collapse" else "Tap to Expand",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}




