package com.quanticheart.bannerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.quanticheart.banner.action.BannerActionListener
import com.quanticheart.banner.action.BannerSwipeListener
import com.quanticheart.banner.entity.Banner
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bannerView.apply {
            setBannerSwipeListener(object : BannerSwipeListener {
                override fun pageSelected(mPosition: Int, size: Int, bannerData: Banner) {
                    Log.e("Banner data", "$mPosition - $size - $bannerData ")
                }
            })
            setBannerList(getBannerList())
        }
    }

    private fun getBannerList(): ArrayList<Banner> {
        val list = ArrayList<Banner>()
        list.add(Banner("https://cdn02.nintendo-europe.com/media/images/10_share_images/games_15/nintendo_switch_4/H2x1_NSwitch_SuperMarioOdyssey.jpg"))
        list.add(Banner("https://lh3.googleusercontent.com/d1FU1ONyGCnx4UMTWYfrbZTcHe4s0q-oxTB3Bcw-s_G2RoUm8pfxqfGZ7dy7OB2r1WG559aepg=w640-h400-e365"))
        list.add(
            Banner("https://nintendeal.com/wp-content/uploads/2017/11/smo_wallpaper_top-740x390.png",
                object : BannerActionListener {
                    override fun onClickListener() {
                        Toast.makeText(applicationContext, "BannerClick", Toast.LENGTH_LONG).show()
                    }
                })
        )
        list.add(
            Banner("https://stmed.net/sites/default/files/super-mario-odyssey-hd-wallpapers-33722-5625840.jpg",
                object : BannerActionListener {
                    override fun onClickListener() {
                        Toast.makeText(applicationContext, "ButtonClick", Toast.LENGTH_LONG).show()
                    }
                }, "Action!"
            )
        )
        return list
    }
}
