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
import androidx.core.content.ContextCompat.getSystemService
import com.blankj.utilcode.util.VibrateUtils
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.wuyr.coffeedrawable.CoffeeDrawable
import com.xu.kotandroid.databinding.HeaderBioBinding

/**
 * @Author Xu
 * Date：2023/5/25 15:09
 * Description：
 */
class BioHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), RefreshHeader {


    private var binding: HeaderBioBinding
    private var animator: ObjectAnimator? = null
    private var currentState = RefreshState.None

    private var coffeeDrawable: CoffeeDrawable? = null


    init {
        binding = HeaderBioBinding.inflate(LayoutInflater.from(context), this, false)


        addView(binding.root)
    }

    @SuppressLint("RestrictedApi")
    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        currentState = newState
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