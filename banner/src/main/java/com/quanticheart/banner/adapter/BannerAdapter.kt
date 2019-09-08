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

package com.quanticheart.banner.adapter

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ViewFlipper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.quanticheart.banner.R
import com.quanticheart.banner.action.BannerSwipeListener
import com.quanticheart.banner.entity.Banner
import kotlinx.android.synthetic.main.ffght_cell.view.*

/**
 * @constructor Credits card Adapter horizontal
 *
 * @param activity for methods
 * @param dotsLayout container LinearLayout for create Dots Selected
 * @param viewPager for create
 *
 */
internal class BannerAdapter(
    private var activity: Context,
    private val dotsLayout: LinearLayout?,
    private val viewPager: ViewPager?
) : PagerAdapter() {

    /**
     * vars
     */
    private lateinit var view: View
    private val bannerList: ArrayList<Banner> = ArrayList()

    //
    private var containerLoad: LinearLayout? = null
    private var load: ProgressBar? = null
    private var flipper: ViewFlipper? = null

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

        load = view.loadBannerView
        containerLoad = view.containerLoadingBannerView
        flipper = view.containerFlipper

        setLoad(showLoading)
        setLoadingColor()
        setLoadingBackgroundColor()
        setBannerBackgroundColor()

        //
        val containerImg = view.viewBannerImgContainer
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
                        setLoad(true)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        setLoad(false)
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
            containerImg.setOnClickListener {
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
        viewPager?.currentItem = 0
        progressDots(0)
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
        val dots = arrayOfNulls<ImageView>(bannerList.size)
        dotsLayout?.removeAllViews()
        for (i in 0 until bannerList.size) {
            dots[i] = ImageView(activity)
            val parans: LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams(25, 25))
            parans.setMargins(10, 10, 10, 10)
            dots[i]?.layoutParams = parans
            dots[i]?.setImageResource(R.drawable.shape_circle)
            dots[i]?.setColorFilter(activity.resources.getColor(R.color.dot_disabled), PorterDuff.Mode.SRC_IN)
            dotsLayout?.addView(dots[i])
        }

        if (dots.isNotEmpty()) {
            dots[position]?.setImageResource(R.drawable.shape_circle)
            dots[position]?.setColorFilter(activity.resources.getColor(R.color.dot_enabled), PorterDuff.Mode.SRC_IN)
        }
        callback?.pageSelected(position, bannerList.size, bannerList[position])
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

    private fun setLoad(show: Boolean) {
        if (showLoading) {
            flipper?.displayedChild = if (show) 0 else 1
        } else {
            flipper?.displayedChild = 1
        }
    }

    /**
     * Funs for colors
     */
    private var colorLoading = -1

    fun setLoadingColor(colorRes: Int) {
        colorLoading = colorRes
    }

    private fun setLoadingColor() {
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

    private fun setLoadingBackgroundColor() {
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

    private fun setBannerBackgroundColor() {
        if (bannerBackgroundColor != -1) {
            flipper?.setBackgroundColor(bannerBackgroundColor)
        } else {
            flipper?.setBackgroundColor(0xffffff)
        }
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
        viewPager?.adapter = this
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) =
                enableDisableSwipeRefresh(p0 == ViewPager.SCROLL_STATE_IDLE, refresh)

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(p0: Int) = progressDots(p0)
        })
        progressDots(0)
    }

    private fun enableDisableSwipeRefresh(enable: Boolean, refresh: SwipeRefreshLayout?) {
        refresh?.isEnabled = enable
    }

}
