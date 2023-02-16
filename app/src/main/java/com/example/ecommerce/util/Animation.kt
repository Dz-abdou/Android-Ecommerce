package com.example.ecommerce.util

import android.content.Context
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

class Animation() {

    companion object {
        suspend fun moveViewOutOfScreenLeftWithAnimation(
            context: Context,
            viewLifecycleOwner: LifecycleOwner,
            view: View
        ): Job {
//            animator = withContext(viewLifecycleOwner.lifecycleScope.coroutineContext) {
//                val duration = view.animate().translationX(-context.resources.displayMetrics.widthPixels.toFloat())
//                delay(duration.duration)
//                logMessage("From moveViewOutOfScreenLeftWithAnimation:${view.translationX}")
//            }
            return viewLifecycleOwner.lifecycleScope.launch(viewLifecycleOwner.lifecycleScope.coroutineContext) {
                val duration = view.animate()
                    .translationX(-context.resources.displayMetrics.widthPixels.toFloat())
                delay(duration.duration)
                logMessage("From moveViewOutOfScreenLeftWithAnimation:${view.translationX}")
            }

        }

        suspend fun moveViewOutOfScreenRightWithAnimation(
            context: Context,
            viewLifecycleOwner: LifecycleOwner,
            view: View
        ): Job {
//            withContext(viewLifecycleOwner.lifecycleScope.coroutineContext) {
//                val duration = view.animate().translationX(
//                    context.resources.displayMetrics.widthPixels.toFloat())
//                delay(duration.duration)
//                logMessage("From moveViewOutOfScreenRightWithAnimation:${view.translationX}")
//            }
            return viewLifecycleOwner.lifecycleScope.launch(viewLifecycleOwner.lifecycleScope.coroutineContext) {
                val duration = view.animate().translationX(
                    context.resources.displayMetrics.widthPixels.toFloat()
                )
                delay(duration.duration)
                logMessage("From moveViewOutOfScreenRightWithAnimation:${view.translationX}")
            }

        }

        suspend fun moveViewToInsideOfTheScreenWithAnimation(
            viewLifecycleOwner: LifecycleOwner,
            view: View
        ): Job {
//             withContext(viewLifecycleOwner.lifecycleScope.coroutineContext) {
//                 val duration = view.animate().translationX(0F)
//                 delay(duration.duration)
//                 logMessage("From moveViewToInsideOfTheScreenWithAnimation:${view.translationX}")
//             }
            return viewLifecycleOwner.lifecycleScope.launch(viewLifecycleOwner.lifecycleScope.coroutineContext) {
                val duration = view.animate().translationX(0F)
                delay(duration.duration)
                logMessage("From moveViewToInsideOfTheScreenWithAnimation:${view.translationX}")
            }
        }

        suspend fun moveViewOutsideOfTheScreenLeft(
            context: Context,
            viewLifecycleOwner: LifecycleOwner,
            view: View
        ) {
            viewLifecycleOwner.lifecycleScope.launch(viewLifecycleOwner.lifecycleScope.coroutineContext) {
                view.translationX = -context.resources.displayMetrics.widthPixels
                    .toFloat()
                logMessage("From moveViewOutsideOfTheScreenLeft:${view.translationX}")

            }
        }

        suspend fun moveViewOutsideOfTheScreenRight(
            context: Context,
            viewLifecycleOwner: LifecycleOwner,
            view: View
        ) {
            viewLifecycleOwner.lifecycleScope.launch(viewLifecycleOwner.lifecycleScope.coroutineContext) {
                view.translationX = context.resources.displayMetrics.widthPixels
                    .toFloat()
                logMessage("From moveViewOutsideOfTheScreenRight:${view.translationX}")

            }
        }
    }
}