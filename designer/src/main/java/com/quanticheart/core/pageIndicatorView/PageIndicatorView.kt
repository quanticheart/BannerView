/*
 *
 *  *                                     /@
 *  *                      __        __   /\/
 *  *                     /==\      /  \_/\/
 *  *                   /======\    \/\__ \__
 *  *                 /==/\  /\==\    /\_|__ \
 *  *              /==/    ||    \=\ / / / /_/
 *  *            /=/    /\ || /\   \=\/ /
 *  *         /===/   /   \||/   \   \===\
 *  *       /===/   /_________________ \===\
 *  *    /====/   / |                /  \====\
 *  *  /====/   /   |  _________    /      \===\
 *  *  /==/   /     | /   /  \ / / /         /===/
 *  * |===| /       |/   /____/ / /         /===/
 *  *  \==\             /\   / / /          /===/
 *  *  \===\__    \    /  \ / / /   /      /===/   ____                    __  _         __  __                __
 *  *    \==\ \    \\ /____/   /_\ //     /===/   / __ \__  ______  ____ _/ /_(_)____   / / / /__  ____ ______/ /_
 *  *    \===\ \   \\\\\\\/   ///////     /===/  / / / / / / / __ \/ __ `/ __/ / ___/  / /_/ / _ \/ __ `/ ___/ __/
 *  *      \==\/     \\\\/ / //////       /==/  / /_/ / /_/ / / / / /_/ / /_/ / /__   / __  /  __/ /_/ / /  / /_
 *  *      \==\     _ \\/ / /////        |==/   \___\_\__,_/_/ /_/\__,_/\__/_/\___/  /_/ /_/\___/\__,_/_/   \__/
 *  *        \==\  / \ / / ///          /===/
 *  *        \==\ /   / / /________/    /==/
 *  *          \==\  /               | /==/
 *  *          \=\  /________________|/=/
 *  *            \==\     _____     /==/
 *  *           / \===\   \   /   /===/
 *  *          / / /\===\  \_/  /===/
 *  *         / / /   \====\ /====/
 *  *        / / /      \===|===/
 *  *        |/_/         \===/
 *  *                       =
 *  *
 *  * Copyright(c) Developed by John Alves at 2019/9/8 at 0:50:9 for quantic heart studios
 *
 */

package com.quanticheart.core.pageIndicatorView

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.quanticheart.core.R

class PageIndicatorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var dotsSize = 0

    init {
        attrs?.let { attributes ->
            val typedArray = context.obtainStyledAttributes(attributes, R.styleable.PageIndicatorView)

            try {

                /**
                 * Size
                 */
                if (typedArray.hasValue(R.styleable.PageIndicatorView_indicatorSize)) {
                    setIndicatiorSize(typedArray.getInt(R.styleable.PageIndicatorView_indicatorSize, 0))
                }

                if (typedArray.hasValue(R.styleable.PageIndicatorView_indicatorPosition)) {
                    setIndicatorPosition(typedArray.getInt(R.styleable.PageIndicatorView_indicatorPosition, 0))
                }

                if (typedArray.hasValue(R.styleable.PageIndicatorView_indicatorColor)) {
                    setColorIndicator(
                        typedArray.getColor(
                            R.styleable.PageIndicatorView_indicatorColor,
                            context.resources.getColor(R.color.dot_enabled, null)
                        )
                    )
                }

                if (typedArray.hasValue(R.styleable.PageIndicatorView_indicatorSelectedColor)) {
                    setColorIndicatorSelected(
                        typedArray.getColor(
                            R.styleable.PageIndicatorView_indicatorSelectedColor,
                            context.resources.getColor(R.color.dot_enabled, null)
                        )
                    )
                }

            } finally {
                typedArray.recycle()
            }
        }
    }

    private var colorIndicator: Int = context.resources.getColor(R.color.dot_disabled, null)
    private var colorIndicatorSelected: Int = context.resources.getColor(R.color.dot_enabled, null)

    fun setColorIndicator(colorInt: Int) {
        colorIndicator = colorInt
    }

    fun setColorIndicatorSelected(colorInt: Int) {
        colorIndicatorSelected = colorInt
    }

    /**
     * private fun for set dots in view
     * @param position position in selected card in view
     */
    private fun progressDots(position: Int) {
        val dots = arrayOfNulls<ImageView>(dotsSize)
        removeAllViews()
        for (i in 0 until dotsSize) {
            dots[i] = ImageView(context)
            val parans = LayoutParams(ViewGroup.LayoutParams(25, 25))
            parans.setMargins(10, 10, 10, 10)
            dots[i]?.layoutParams = parans
            dots[i]?.setImageResource(R.drawable.shape_circle)
            dots[i]?.setColorFilter(colorIndicator, PorterDuff.Mode.SRC_IN)
            addView(dots[i])
        }

        if (dots.isNotEmpty()) {
            dots[position]?.setImageResource(R.drawable.shape_circle)
            dots[position]?.setColorFilter(colorIndicatorSelected, PorterDuff.Mode.SRC_IN)
        }
    }

    fun setIndicatiorSize(size: Int) {
        dotsSize = size
        setIndicatorPosition(0)
    }

    fun setIndicatorPosition(position: Int) {
        progressDots(position)
    }
}