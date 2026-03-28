package com.vibecut.ai.data.effects

import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

enum class SpecialEffect {
    SHAKE, GLITCH, ZOOM_PULSE, VHS, ECHO_BLUR, NEON_GLOW, FILM_GRAIN, VIGNETTE
}

@Singleton
class SpecialEffectsProcessor @Inject constructor() {

    suspend fun applyEffect(
        inputPath: String,
        outputPath: String,
        effect: SpecialEffect
    ): Result<String> = withContext(Dispatchers.IO) {
        val filterComplex = buildFilter(effect)
        val command = "-i \"$inputPath\" -vf \"$filterComplex\" -c:v libx264 -preset fast -crf 22 -c:a copy \"$outputPath\""

        suspendCancellableCoroutine { cont ->
            val session = FFmpegKit.executeAsync(command) { s ->
                if (ReturnCode.isSuccess(s.returnCode)) cont.resume(Result.success(outputPath))
                else cont.resume(Result.failure(Exception(s.failStackTrace)))
            }
            cont.invokeOnCancellation { FFmpegKit.cancel(session.sessionId) }
        }
    }

    private fun buildFilter(effect: SpecialEffect): String = when (effect) {

        SpecialEffect.SHAKE ->
            "crop=iw-10:ih-10:random(1)*10:random(2)*10,scale=iw+10:ih+10"

        SpecialEffect.GLITCH ->
            "split=3[r][g][b];[r]lutrgb=r='val':g=0:b=0,geq=r='if(mod(Y,4),r(X,Y),0)':g=0:b=0[rl];[g]lutrgb=r=0:g='val':b=0[gl];[b]lutrgb=r=0:g=0:b='val'[bl];[rl][gl]blend=all_mode=addition[rg];[rg][bl]blend=all_mode=addition"

        SpecialEffect.ZOOM_PULSE ->
            "zoompan=z='if(lte(mod(on,30),15),1+0.05*on/15,1+0.05*(30-mod(on,30))/15)':d=1:s=iw x ih"

        SpecialEffect.VHS ->
            "noise=alls=8:allf=t+u,hue=s=0.8,vignette=PI/4,eq=brightness=-0.05:saturation=0.85,geq=r='r(X,Y)+2':g='g(X,Y)':b='b(X+2,Y)'"

        SpecialEffect.ECHO_BLUR ->
            "tblend=all_mode=average:all_opacity=0.5"

        SpecialEffect.NEON_GLOW ->
            "split[orig][edge];[edge]edgedetect=low=0.1:high=0.4,colorize=hue=260:saturation=1[glow];[orig][glow]blend=all_mode=screen:all_opacity=0.6"

        SpecialEffect.FILM_GRAIN ->
            "noise=alls=15:allf=t,vignette=PI/5,eq=contrast=1.1:saturation=0.9"

        SpecialEffect.VIGNETTE ->
            "vignette=PI/3,eq=brightness=-0.05:contrast=1.15"
    }
}