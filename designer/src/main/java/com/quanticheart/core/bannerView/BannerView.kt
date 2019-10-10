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
 *  * Copyright(c) Developed by John Alves at 2019/9/8 at 0:49:30 for quantic heart studios
 *
 */

package com.quanticheart.core.bannerView

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.quanticheart.core.R
import com.quanticheart.core.bannerView.action.BannerSwipeListener
import com.quanticheart.core.bannerView.adapter.BannerAdapter
import com.quanticheart.core.bannerView.entity.Banner
import com.quanticheart.core.pageIndicatorView.PageIndicatorView
import kotlinx.android.synthetic.main.ffght.view.*

@Suppress("DEPRECATION", "MemberVisibilityCanBePrivate")
class BannerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var container: ConstraintLayout? = null
    private var viewPager: ViewPager? = null
    private var indicatorView: PageIndicatorView? = null

    private var adapter: BannerAdapter? = null

    init {
        View.inflate(context, R.layout.ffght, this)

        container = containerBannerView
        viewPager = vpBannerView
        indicatorView = llBannerView

        /**
         * init adapter
         */
        adapter = BannerAdapter(context, indicatorView, viewPager)

        attrs?.let { attributes ->
            val typedArray = context.obtainStyledAttributes(attributes, R.styleable.BannerView)

            try {

                /**
                 * Loadding
                 */
                if (typedArray.hasValue(R.styleable.BannerView_showLoading)) {
                    showLoadingBanner(typedArray.getBoolean(R.styleable.BannerView_showLoading, true))
                }

                if (typedArray.hasValue(R.styleable.BannerView_loadingColor)) {
                    val i = typedArray.getColor(
                        R.styleable.BannerView_loadingColor,
                        resources.getColor(android.R.color.black)
                    )
                    setLoadingColor(i)
                }

                if (typedArray.hasValue(R.styleable.BannerView_loadingBackgroundColor)) {
                    val i = typedArray.getColor(
                        R.styleable.BannerView_loadingBackgroundColor,
                        resources.getColor(android.R.color.white)
                    )
                    setLoadingBackgroundColor(i)
                }

                /**
                 * Banner
                 */
                if (typedArray.hasValue(R.styleable.BannerView_bannerBackgroundColor)) {
                    val i = typedArray.getColor(
                        R.styleable.BannerView_bannerBackgroundColor,
                        resources.getColor(android.R.color.white)
                    )
                    setBannerBackgroundColor(i)
                }

                /**
                 * Dots
                 */
                if (typedArray.hasValue(R.styleable.BannerView_showPageIndicator)) {
                    showDotsIndicatior(typedArray.getBoolean(R.styleable.BannerView_showPageIndicator, true))
                }

                if (typedArray.hasValue(R.styleable.BannerView_indicatorDotsColor)) {
                    setColorIndicator(
                        typedArray.getColor(
                            R.styleable.BannerView_indicatorDotsColor,
                            context.resources.getColor(R.color.dot_disabled)
                        )
                    )
                }

                if (typedArray.hasValue(R.styleable.BannerView_indicatorDotsSelectedColor)) {
                    setColorIndicatorSelected(
                        typedArray.getColor(
                            R.styleable.BannerView_indicatorDotsSelectedColor,
                            context.resources.getColor(R.color.dot_enabled)
                        )
                    )
                }

                /**
                 * auto play
                 */
                if (typedArray.hasValue(R.styleable.BannerView_autoCarousel)) {
                    autoCarousel(typedArray.getBoolean(R.styleable.BannerView_autoCarousel, true))
                }

                if (typedArray.hasValue(R.styleable.BannerView_startCarouselTime)) {
                    startCarouselAfter(typedArray.getInt(R.styleable.BannerView_startCarouselTime, 5))
                }

                if (typedArray.hasValue(R.styleable.BannerView_restartCarouselTime)) {
                    restartCarouselAfter(typedArray.getInt(R.styleable.BannerView_restartCarouselTime, 10))
                }

            } finally {
                typedArray.recycle()
            }
        }
    }

    /**
     * Funs for Loading
     */
    fun showLoadingBanner(show: Boolean) {
        adapter?.setShowLoadingImg(show)
    }

    fun setLoadingColor(colorRes: Int) {
        adapter?.setLoadingColor(colorRes)
    }

    fun setLoadingBackgroundColor(colorRes: Int) {
        adapter?.setLoadingBackgroundColor(colorRes)
    }

    /**
     * Banner
     */
    fun setBannerBackgroundColor(colorRes: Int) {
        adapter?.setBannerBackgroundColor(colorRes)
    }

    /**
     * Dots
     */
    fun showDotsIndicatior(show: Boolean) {
        indicatorView?.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setColorIndicator(colorInt: Int) {
        indicatorView?.setColorIndicator(colorInt)
    }

    fun setColorIndicatorSelected(colorInt: Int) {
        indicatorView?.setColorIndicatorSelected(colorInt)
    }

    /**
     * auto play
     */
    fun autoCarousel(show: Boolean) {
        adapter?.setAutoPlay(show)
    }

    fun startCarouselAfter(time: Int) {
        adapter?.setStartTime(time)
    }

    fun restartCarouselAfter(time: Int) {
        adapter?.setRestartTime(time)
    }

    /**
     * Adapter
     */

    fun deleteBannerAtPosition(position: Int) {
        adapter?.deletePage(position)
    }

    fun setBanner(banner: Banner) {
        adapter?.addBanner(banner)
    }

    fun setBannerList(bannerList: ArrayList<Banner>) {
        if (bannerList.size > 0) {
            adapter?.addBannerList(bannerList)
        }
    }

    /**
     * setBannerSelectCallback
     */
    fun setBannerSwipeListener(callback: BannerSwipeListener) {
        adapter?.setBannerCallback(callback)
    }

    /**
     * setRefreshLayout
     */
    fun setRefreshLayout(refreshLayout: SwipeRefreshLayout) {
        adapter?.setRefreshLayout(refreshLayout)
    }
}
