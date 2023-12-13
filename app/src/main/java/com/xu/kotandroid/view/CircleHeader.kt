package com.xu.kotandroid.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.wuyr.coffeedrawable.CoffeeDrawable
import com.xu.kotandroid.compose.ui.theme.KotAndroidTheme
import com.xu.kotandroid.databinding.HeaderBioBinding

/**
 * @Author Xu
 * Date：2023/5/25 15:09
 * Description：
 */
class CircleHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), RefreshHeader {


    private var binding: HeaderBioBinding
    private var animator: ObjectAnimator? = null
    private var currentState = RefreshState.None

    private var coffeeDrawable: CoffeeDrawable? = null

    private val refreshState = mutableStateOf(RefreshState.None)

    private val progress = mutableFloatStateOf(0f)

    init {
        binding = HeaderBioBinding.inflate(LayoutInflater.from(context), this, false)

        binding.composeView.setContent {
            KotAndroidTheme {
                Text(text = "Hello Girl")
            }
        }

//        addView(binding.root)
        addView(ComposeView(context).apply {
            setContent {
                KotAndroidTheme {

                    val realRotateZ by remember{
                        derivedStateOf { progress.floatValue * 180f }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        when (refreshState.value) {
                            RefreshState.None, RefreshState.PullDownToRefresh, RefreshState.ReleaseToRefresh -> {
                                CircleRing(
                                    Modifier
                                        .size(18.dp)
                                        .graphicsLayer {
                                            rotationZ = realRotateZ
                                        })
                            }

                            RefreshState.Refreshing, RefreshState.RefreshFinish -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = Color.White,
                                    trackColor = Color.White.copy(0.25f),
                                    strokeWidth = 2.dp
                                )
                            }

                            else -> {}
                        }

                    }

                }
            }
        })
    }

    @Composable
    fun CircleRing(modifier: Modifier) {
        Canvas(modifier = modifier) {
            drawCircle(color = Color.White.copy(0.25f), style = Stroke(width = 2.dp.toPx()))
            drawArc(
                color = Color.White,
                30f,
                60f,
                useCenter = false,
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        currentState = newState
        refreshState.value = newState
        when (newState) {
            RefreshState.None, RefreshState.PullDownToRefresh -> {
                binding.tv.text = "下拉开始刷新"
//                coffeeDrawable.reset()
            }

            RefreshState.Refreshing -> {
//                if (animator == null) {
//                    animator =
//                        ObjectAnimator.ofFloat(binding.logo, "rotation", -45f, 0f, 45f).apply {
//                            repeatMode = ObjectAnimator.REVERSE
//                            repeatCount = ObjectAnimator.INFINITE
//                            duration = 250
//                            start()
//                        }
//                } else {
//                    animator?.start()
//                }
                coffeeDrawable?.start()
                binding.tv.text = "正在刷新"

            }

            RefreshState.ReleaseToRefresh -> {

                val vib = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vibratorManager =
                        context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                    vibratorManager.defaultVibrator
                } else {
                    @Suppress("DEPRECATION")
                    context.getSystemService(VIBRATOR_SERVICE) as Vibrator
                }
                vib.vibrate(VibrationEffect.createOneShot(2, VibrationEffect.DEFAULT_AMPLITUDE))

                binding.tv.text = "释放立即刷新"
            }

            else -> {}
        }

    }

    override fun getView(): View {
        return this
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    @SuppressLint("RestrictedApi")
    override fun setPrimaryColors(vararg colors: Int) {
    }

    @SuppressLint("RestrictedApi")
    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
        coffeeDrawable = CoffeeDrawable.create(binding.logo, height / 6)

        binding.logo.setImageDrawable(coffeeDrawable)
    }

    @SuppressLint("RestrictedApi")
    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int
    ) {

        if (currentState == RefreshState.Refreshing) return
        val realPercent = if (percent > 1) 1f else percent

        if (isDragging) {
            binding.tv.scaleX = realPercent
//            binding.logo.apply {
//                alpha = realPercent
//                scaleX = realPercent
//                scaleY = realPercent
//            }

            coffeeDrawable?.progress = realPercent

            progress.floatValue = realPercent

        }

        Log.e("here", "onMoving $isDragging $percent $offset $height $maxDragHeight")
    }

    @SuppressLint("RestrictedApi")
    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        Log.e("here", "onReleased $height $maxDragHeight")
    }

    @SuppressLint("RestrictedApi")
    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        Log.e("here", "onStartAnimator $height $maxDragHeight")
    }

    @SuppressLint("RestrictedApi")
    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {

        binding.tv.text = if (success) "刷新完成" else "刷新失败"
//        binding.logo.visibility = View.GONE

//        animator?.cancel()
//        binding.logo.apply {
//            animate().alpha(0f)
//                .scaleX(0f)
//                .scaleY(0f)
//                .rotation(0f)
//                .duration = 500
//        }

        coffeeDrawable?.finish()

//        progress.floatValue = 0f

        return 500
    }

    @SuppressLint("RestrictedApi")
    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
    }

    @SuppressLint("RestrictedApi")
    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }
}