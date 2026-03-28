package com.vibecut.ai.ui.effects

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.random.Random

data class GlitterParticle(
    val x: Float, val y: Float,
    val size: Float, val color: Color,
    val rotation: Float, val speed: Float,
    val alpha: Float
)

@Composable
fun GlitterOverlay(
    modifier: Modifier = Modifier,
    particleCount: Int = 80,
    colors: List<Color> = listOf(
        Color(0xFFFFD700), Color(0xFFFF69B4), Color(0xFF00BFFF),
        Color(0xFF7FFF00), Color(0xFFFF6347), Color(0xFFEE82EE),
        Color(0xFFFFFFFF)
    )
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glitter")
    val tick by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing)),
        label = "tick"
    )

    val particles = remember {
        List(particleCount) {
            GlitterParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 8f + 3f,
                color = colors.random(),
                rotation = Random.nextFloat() * 360f,
                speed = Random.nextFloat() * 0.3f + 0.1f,
                alpha = Random.nextFloat() * 0.8f + 0.2f
            )
        }
    }

    Canvas(modifier = modifier) {
        particles.forEach { p ->
            val animatedY = (p.y + tick * p.speed) % 1f
            val animatedAlpha = (p.alpha * (0.5f + 0.5f * kotlin.math.sin(tick * Math.PI.toFloat() * 2 + p.x * 10))).coerceIn(0f, 1f)
            val animatedRotation = p.rotation + tick * 180f

            val x = p.x * size.width
            val y = animatedY * size.height

            rotate(animatedRotation, Offset(x, y)) {
                drawRect(
                    color = p.color.copy(alpha = animatedAlpha),
                    topLeft = Offset(x - p.size / 2, y - p.size / 2),
                    size = androidx.compose.ui.geometry.Size(p.size, p.size * 0.4f)
                )
            }
        }
    }
}