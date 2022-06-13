package com.wizeline.academy.animations.ui.more_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.wizeline.academy.animations.databinding.MoreDetailsFragmentBinding
import com.wizeline.academy.animations.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreDetailsFragment : Fragment() {

    private var _binding: MoreDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MoreDetailsViewModel by viewModels()
    private val args: MoreDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MoreDetailsFragmentBinding.inflate(inflater, container, false)
        binding.ivImageDetailLarge.loadImage(args.imageId)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.title.observe(viewLifecycleOwner) { binding.tvTitle.text = it }
        viewModel.content.observe(viewLifecycleOwner) { binding.tvFullTextContent.text = it }
        viewModel.fetchData(args.contentIndex)
        setupZoomAnimation()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupZoomAnimation() {
        var scale = 1f
        val springX = SpringAnimation(binding.ivImageDetailLarge, DynamicAnimation.SCALE_X).apply {
            spring = createSpringForce()
        }
        val springY = SpringAnimation(binding.ivImageDetailLarge, DynamicAnimation.SCALE_Y).apply {
            spring = createSpringForce()
        }
        val sgd = ScaleGestureDetector(requireContext(), object : ScaleGestureDetector.OnScaleGestureListener {
            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                springX.cancel()
                springY.cancel()
                return true
            }
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scale *= detector.scaleFactor
                binding.ivImageDetailLarge.scaleX *= scale
                binding.ivImageDetailLarge.scaleY *= scale
                return true
            }
            override fun onScaleEnd(detector: ScaleGestureDetector?) {
                springX.start()
                springY.start()
            }
        })
        binding.ivImageDetailLarge.setOnTouchListener { _, motionEvent ->
            sgd.onTouchEvent(motionEvent)
        }
    }

    private fun createSpringForce(): SpringForce {
        return SpringForce(1f).apply {
            dampingRatio = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY
            stiffness = SpringForce.STIFFNESS_LOW
        }
    }
}