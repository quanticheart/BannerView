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
 *  * Copyright(c) Developed by John Alves at 2019/9/1 at 9:18:26 for quantic heart studios
 *
 */

package com.quanticheart.core.bannerView.adapter

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.quanticheart.core.R
import com.quanticheart.core.bannerView.action.BannerSwipeListener
import com.quanticheart.core.bannerView.entity.Banner
import com.quanticheart.core.pageIndicatorView.PageIndicatorView
import kotlinx.android.synthetic.main.ffght_cell.view.*

/**
 * @constructor Credits card Adapter horizontal
 *
 * @param activity for methods
 * @param pageIndicator container LinearLayout for create Dots Selected
 * @param viewPager for create
 *
 */
@Suppress("DEPRECATION")
internal class BannerAdapter(
    private var activity: Context,
    private val pageIndicator: PageIndicatorView?,
    private val viewPager: ViewPager?
) : PagerAdapter() {

    /**
     * vars
     */
    private lateinit var view: View
    private val bannerList: ArrayList<Banner> = ArrayList()

    //
    private var autoPlay = true
    private val counter = Handler()
    private var currentPage = 0
    private var timeStartAutoCarousel = 10
    private var timeStartAutoCarouselAfterTouch = 15

    /**
     * Auto Play
     */

    private fun startCarousel() {
        counter.post(startCarousel)
    }

    private val startCarousel = object : Runnable {
        override fun run() {
            viewPager?.currentItem = currentPage
            counter.postDelayed(this, (timeStartAutoCarousel * 1000).toLong())
            updateCurrentPage()
        }
    }

    private val restartCarousel =
        Runnable { counter.postDelayed(startCarousel, (timeStartAutoCarousel * 1000).toLong()) }

    private fun updateCurrentPage() {
        currentPage++
        if (currentPage >= bannerList.size) {
            currentPage = 0
        }
    }

    private fun updateCurrentPageByPos(position: Int) {
        currentPage = position
    }

    private var onTouchListener = View.OnTouchListener { _, _ ->
        counter.removeCallbacks(startCarousel)
        counter.removeCallbacks(restartCarousel)
        counter.postDelayed(restartCarousel, (timeStartAutoCarouselAfterTouch * 1000).toLong())
        false
    }

    /**
     * @init add Payments Methods and verify Size in Page Model
     */
    init {
        setAdapterConfig()
    }

    /**
     * override instantiateItem in PagerAdapter()
     */
    override fun instantiateItem(container: ViewGroup, position: Int): View {
        view = LayoutInflater.from(container.context).inflate(R.layout.ffght_cell, container, false)

        val load = view.loadBannerView
        val containerLayout = view.container
        val containerLoad = view.containerLoadingBannerView

        setLoad(showLoading, containerLoad)
        setLoadingColor(load)
        setLoadingBackgroundColor(containerLoad)
        setBannerBackgroundColor(containerLayout)

        //
//        val containerImg = view.viewBannerImgContainer
        val img = view.viewBannerImg
        val button = view.viewBannerButton

        //
        val banner = bannerList[position]

        //
        val imgLink = banner.imageLink
        val action = banner.actionListener
        val buttonTitle = banner.buttonText

        //
        if (imgLink != "") {
            Glide.with(container.context)
                .load(imgLink)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        setLoad(true, containerLoad)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        setLoad(false, containerLoad)
                        return false
                    }
                })
                .into(img)
        }

        buttonTitle?.let {
            button.apply {
                visibility = View.VISIBLE
                text = it
                setOnClickListener {
                    action?.onClickListener()
                }
            }
        } ?: run {
            button.visibility = View.GONE
            img.setOnClickListener {
                action?.onClickListener()
            }
        }

        container.addView(view)
        return view
    }

    /**
     * override getCount in PagerAdapter()
     */
    override fun getCount(): Int = bannerList.size

    /**
     * override isViewFromObject in PagerAdapter()
     */
    override fun isViewFromObject(p0: View, p1: Any): Boolean = p0 == p1

    /**
     * override destroyItem in PagerAdapter()
     */
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }

    /**
     * override getItemPosition in PagerAdapter()
     */
    override fun getItemPosition(`object`: Any): Int = POSITION_NONE

    /**
     * Delete a page at a `position`
     */
    fun deletePage(position: Int) {
        /**
         * Remove the corresponding item in the data set
         */
        bannerList.removeAt(position)
        /**
         * Notify the adapter that the data set is changed
         */
        verifyData()
        progressDots(position)
    }

    private fun verifyData() {
        notifyDataSetChanged()
        /**
         * Config Adapter for new Size Array
         */
        viewPager?.offscreenPageLimit = bannerList.size
        pageIndicator?.setIndicatiorSize(bannerList.size)
        if (autoPlay) {
            startCarousel()
        }
    }

    /**
     * for add in view, user's credit card list
     *
     * @param bannerList list with all banner data
     */
    fun addBannerList(bannerList: ArrayList<Banner>) {
        this.bannerList.addAll(bannerList)
        verifyData()
    }

    fun addBanner(banner: Banner) {
        bannerList.add(banner)
        verifyData()
    }

    /**
     * private fun for set dots in view
     * @param position position in selected card in view
     */
    private fun progressDots(position: Int) {
        pageIndicator?.setIndicatorPosition(position)
        callback?.pageSelected(position, bannerList.size, bannerList[position])
        updateCurrentPageByPos(position)
    }

    /**
     * set callback
     */
    private var callback: BannerSwipeListener? = null

    fun setBannerCallback(callback: BannerSwipeListener) {
        this.callback = callback
    }

    /**
     * setRefreshLayout
     */

    private var refresh: SwipeRefreshLayout? = null

    fun setRefreshLayout(refreshLayout: SwipeRefreshLayout) {
        this.refresh = refreshLayout
    }

    /**
     * Funs for Loading
     */
    private var showLoading = false

    fun setShowLoadingImg(show: Boolean) {
        this.showLoading = show
    }

    private fun setLoad(show: Boolean, flipper: LinearLayout?) {
        if (showLoading) {
            flipper?.visibility = if (show) View.VISIBLE else View.GONE
        } else {
            flipper?.visibility = View.GONE
        }
    }

    /**
     * Funs for colors
     */
    private var colorLoading = -1

    fun setLoadingColor(colorRes: Int) {
        colorLoading = colorRes
    }

    private fun setLoadingColor(load: ProgressBar?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            load?.indeterminateDrawable?.colorFilter = BlendModeColorFilter(colorLoading, BlendMode.SRC_ATOP)
        } else {
            load?.indeterminateDrawable?.setColorFilter(colorLoading, PorterDuff.Mode.MULTIPLY)
        }
    }

    private var loadingBackgroundColor = -1

    fun setLoadingBackgroundColor(colorRes: Int) {
        loadingBackgroundColor = colorRes
    }

    private fun setLoadingBackgroundColor(containerLoad: LinearLayout?) {
        if (loadingBackgroundColor != -1) {
            containerLoad?.setBackgroundColor(loadingBackgroundColor)
        } else {
            containerLoad?.setBackgroundColor(0xffffff)
        }
    }

    /**
     * Banner
     */

    private var bannerBackgroundColor = -1

    fun setBannerBackgroundColor(colorRes: Int) {
        bannerBackgroundColor = colorRes
    }

    private fun setBannerBackgroundColor(flipper: ConstraintLayout?) {
        if (bannerBackgroundColor != -1) {
            flipper?.setBackgroundColor(bannerBackgroundColor)
        } else {
            flipper?.setBackgroundColor(0xffffff)
        }
    }

    /**
     * Auto play
     */

    fun setAutoPlay(autoPlay: Boolean) {
        this.autoPlay = autoPlay
    }

    fun setStartTime(startAt: Int) {
        timeStartAutoCarousel = startAt
    }

    fun setRestartTime(restartAt: Int) {
        timeStartAutoCarouselAfterTouch = restartAt
    }

    //==============================================================================================
    //
    // ** Config
    //
    //==============================================================================================

    /**
     * this functions config viewpager and set adapter
     *
     */
    private fun setAdapterConfig() {
        viewPager?.apply {
            adapter = this@BannerAdapter
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(p0: Int) =
                    enableDisableSwipeRefresh(p0 == ViewPager.SCROLL_STATE_IDLE, refresh)

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

                override fun onPageSelected(p0: Int) = progressDots(p0)
            })
            setOnTouchListener(onTouchListener)
        }
    }

    private fun enableDisableSwipeRefresh(enable: Boolean, refresh: SwipeRefreshLayout?) {
        refresh?.isEnabled = enable
    }
}
