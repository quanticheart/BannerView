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
 *  * Copyright(c) Developed by John Alves at 2019/8/31 at 9:43:26 for quantic heart studios
 *
 */

package com.quanticheart.banner

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ViewFlipper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.ffght.view.*
import android.os.Build
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.quanticheart.banner.action.BannerSwipeListener
import com.quanticheart.banner.adapter.BannerAdapter
import com.quanticheart.banner.entity.Banner


@Suppress("DEPRECATION", "MemberVisibilityCanBePrivate")
class BannerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var container: ConstraintLayout? = null
    private var vp: ViewPager? = null
    private var dots: LinearLayout? = null

    private var adapter: BannerAdapter? = null

    init {
        View.inflate(context, R.layout.ffght, this)

        container = containerBannerView
        vp = vpBannerView
        dots = llBannerView

        /**
         * init adapter
         */
        adapter = BannerAdapter(context, dots, vp)

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
                        resources.getColor(android.R.color.black, null)
                    )
                    setLoadingColor(i)
                }

                if (typedArray.hasValue(R.styleable.BannerView_loadingBackgroundColor)) {
                    val i = typedArray.getColor(
                        R.styleable.BannerView_loadingBackgroundColor,
                        resources.getColor(android.R.color.white, null)
                    )
                    setLoadingBackgroundColor(i)
                }

                /**
                 * Banner
                 */
                if (typedArray.hasValue(R.styleable.BannerView_bannerBackgroundColor)) {
                    val i = typedArray.getColor(
                        R.styleable.BannerView_bannerBackgroundColor,
                        resources.getColor(android.R.color.white, null)
                    )
                    setBannerBackgroundColor(i)
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
