package com.vibecut.ai.ui.editor

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.*
import com.airbnb.lottie.compose.*
import kotlin.math.roundToInt

data class StickerOverlay(
    val id: String = java.util.UUID.randomUUID().toString(),
    val lottieUrl: String,
    val label: String,
    var x: Float = 0.5f,
    var y: Float = 0.5f,
    var scale: Float = 1f,
    var rotation: Float = 0f,
    val startMs: Long = 0,
    val endMs: Long = Long.MAX_VALUE
)

// Free Lottie sticker URLs from LottieFiles public library
val BUILT_IN_STICKERS = listOf(
    StickerOverlay(lottieUrl = "https://assets2.lottiefiles.com/packages/lf20_ichdlgsy.json", label = "Heart 💗"),
    StickerOverlay(lottieUrl = "https://assets2.lottiefiles.com/packages/lf20_wgbu8ewy.json", label = "Fire 🔥"),
    StickerOverlay(lottieUrl = "https://assets2.lottiefiles.com/packages/lf20_touohxv0.json", label = "Stars ⭐"),
    StickerOverlay(lottieUrl = "https://assets5.lottiefiles.com/packages/lf20_qjosmr4w.json", label = "Confetti 🎉"),
    StickerOverlay(lottieUrl = "https://assets3.lottiefiles.com/packages/lf20_yhTWOb.json", label = "Music 🎵"),
    StickerOverlay(lottieUrl = "https://assets3.lottiefiles.com/packages/lf20_vvt0iiil.json", label = "Crown 👑"),
    StickerOverlay(lottieUrl = "https://assets4.lottiefiles.com/packages/lf20_jR229a.json", label = "Smile 😊"),
    StickerOverlay(lottieUrl = "https://assets9.lottiefiles.com/packages/lf20_RmHFd1.json", label = "Wave 🌊"),
)

@Composable
fun AnimatedStickerOverlay(
    sticker: StickerOverlay,
    containerWidth: Float,
    containerHeight: Float,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onUpdate: (StickerOverlay) -> Unit
) {
    var posX by remember { mutableFloatStateOf(sticker.x * containerWidth) }
    var posY by remember { mutableFloatStateOf(sticker.y * containerHeight) }
    var scale by remember { mutableFloatStateOf(sticker.scale) }
    var rotation by remember { mutableFloatStateOf(sticker.rotation) }

    val composition by rememberLottieComposition(LottieCompositionSpec.Url(sticker.lottieUrl))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = Modifier
            .offset { IntOffset(posX.roundToInt(), posY.roundToInt()) }
            .size((100 * scale).dp)
            .rotate(rotation)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, rotDelta ->
                    posX += pan.x; posY += pan.y
                    scale = (scale * zoom).coerceIn(0.3f, 3f)
                    rotation += rotDelta
                    onUpdate(sticker.copy(
                        x = posX / containerWidth, y = posY / containerHeight,
                        scale = scale, rotation = rotation
                    ))
                }
            }
            .clickable { onSelect() }
            .then(if (isSelected) Modifier.border(2.dp, MaterialTheme.colorScheme.primary) else Modifier)
    ) {
        LottieAnimation(composition = composition, progress = { progress })
    }
}

@Composable
fun StickerPickerPanel(onStickerSelected: (StickerOverlay) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text("Stickers", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(bottom = 8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.height(200.dp),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(BUILT_IN_STICKERS) { template ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            onStickerSelected(template.copy(id = java.util.UUID.randomUUID().toString()))
                        }
                ) {
                    val composition by rememberLottieComposition(LottieCompositionSpec.Url(template.lottieUrl))
                    val progress by animateLottieCompositionAsState(composition = composition, iterations = LottieConstants.IterateForever)
                    LottieAnimation(composition = composition, progress = { progress }, modifier = Modifier.size(60.dp))
                    Text(template.label, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}