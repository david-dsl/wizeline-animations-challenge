package com.wizeline.academy.animations.ui.splash_screen

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.CycleInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.wizeline.academy.animations.databinding.SplashFragmentBinding

class SplashFragment : Fragment() {

    private var _binding: SplashFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SplashFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        animate {
            goToHomeScreen()
        }
    }

    private fun animate(onEnd: () -> Unit = {}) {
        val firstDuration = 2500L
        val scaleInterpolator = CycleInterpolator(2f)
        val max = 1.025f
        val min = 0.975f
        val sx = ObjectAnimator.ofFloat(binding.ivWizelineLogo, View.SCALE_X, max, min)
            .setDuration(firstDuration).apply { interpolator = scaleInterpolator }
        val sy = ObjectAnimator.ofFloat(binding.ivWizelineLogo, View.SCALE_Y, max, min)
            .setDuration(firstDuration).apply { interpolator = scaleInterpolator }
        val secondDuration = 1000L
        val ry = ObjectAnimator.ofFloat(binding.ivWizelineLogo, View.ROTATION_Y, 0f, 360f * 3)
            .setDuration(secondDuration)
        val shrink = ValueAnimator.ofFloat(1f, 0f).setDuration(secondDuration).apply {
            addUpdateListener {
                binding.ivWizelineLogo.scaleX = it.animatedValue as Float
                binding.ivWizelineLogo.scaleY = it.animatedValue as Float
            }
        }
        AnimatorSet().apply {
            play(sx)
                .with(sy)
                .before(ry)
                .before(shrink)
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {}
                override fun onAnimationCancel(p0: Animator?) {}
                override fun onAnimationRepeat(p0: Animator?) {}
                override fun onAnimationEnd(p0: Animator?) {
                    onEnd()
                }
            })
        }.start()
    }

    private fun goToHomeScreen() {
        val directions = SplashFragmentDirections.toMainFragment()
        findNavController().navigate(directions)
    }
}