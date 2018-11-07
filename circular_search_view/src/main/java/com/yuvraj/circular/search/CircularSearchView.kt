package com.yuvraj.circular.search

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Path
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.yuvraj.search.ViewPathAnimator
import kotlinx.android.synthetic.main.layout_search_view.view.*

/**
 * Created by Yuvraj on 07/11/18.
 */
class CircularSearchView : RelativeLayout {

    private lateinit var instance: Context
    private lateinit var searchView: View
    private var count = 1
    private var mCircleAnimTime = 12L
    private var mCircleRadius = 420F
    private var mDelay = 2000L

    constructor(context: Context) : this(context, null) {
        this.instance = context
        this.searchView = View(instance)
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        this.instance = context
        this.searchView = View(instance)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.instance = context
        this.searchView = View(instance)
        initSearch(attrs)
    }

    private fun initSearch(attrs: AttributeSet?) {

        val inflator: LayoutInflater = instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        searchView = inflator.inflate(R.layout.layout_search_view, this)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it,
                R.styleable.CircularSearchView, 0, 0)

            createIconWithSize(searchView, typedArray)

            typedArray.recycle()
        }

        searchView.image_user_1.visibility = View.VISIBLE
        mCircleRadius = resources.getDimension(R.dimen.circle_radius)
        startCircularAnimation(searchView, searchView.image_user_1, mCircleAnimTime, mCircleRadius)

//        addView(searchView)
    }

    private fun createIconWithSize(searchView: View, typedArray: TypedArray) {

        val mIcon = resources.getDrawable(typedArray
            .getResourceId(R.styleable
                .CircularSearchView_cs_icon, R.drawable.user1))

        val mIconSize = resources.getDimension(typedArray
            .getResourceId(R.styleable
                .CircularSearchView_cs_icon_size, R.dimen.margin80))

        val mCenterIcon = resources.getDrawable(typedArray
            .getResourceId(R.styleable
                .CircularSearchView_cs_center_icon, R.drawable.ic_zoom))

        val mCenterIconSize = resources.getDimension(typedArray
            .getResourceId(R.styleable
                .CircularSearchView_cs_center_icon_size, R.dimen.margin80))

        // Set Icon size to ImageView
        val layoutParams : ViewGroup.LayoutParams =  searchView.image_user_1.layoutParams
        layoutParams.width = mIconSize.toInt()
        layoutParams.height = mIconSize.toInt()
        searchView.image_user_1.layoutParams = layoutParams
        searchView.image_user_2.layoutParams = layoutParams
        searchView.image_user_3.layoutParams = layoutParams
        searchView.image_user_4.layoutParams = layoutParams
        searchView.image_user_5.layoutParams = layoutParams

        // Set Icon to ImageView
        searchView.image_user_1.setImageDrawable(mIcon)
        searchView.image_user_2.setImageDrawable(mIcon)
        searchView.image_user_3.setImageDrawable(mIcon)
        searchView.image_user_4.setImageDrawable(mIcon)
        searchView.image_user_5.setImageDrawable(mIcon)

        // Set center icon
        searchView.image_center.setImageDrawable(mCenterIcon)

        // Set center icon size
        searchView.image_center.layoutParams.width = mCenterIconSize.toInt()
        searchView.image_center.layoutParams.height = mCenterIconSize.toInt()
    }

    private fun startCircularAnimation(searchView : View , view: View, delay: Long, radius: Float) {

        val path = Path()
        path.addCircle(0F, 0f, radius, Path.Direction.CW)
        ViewPathAnimator().animate(view, path, delay.toInt(), 8)
        if (count == 1) {
            mDelay += 1000
        }

        Handler().postDelayed({
            count++
            when (count) {
                2 -> {
                    searchView.image_user_2.visibility = View.VISIBLE
                    mDelay = 2000
                    startCircularAnimation(searchView, searchView.image_user_2, mCircleAnimTime, mCircleRadius)
                }
                3 -> {
                    searchView.image_user_3.visibility = View.VISIBLE
                    mDelay = 2500
                    mCircleRadius -= 100
                    startCircularAnimation(searchView, searchView.image_user_3, mCircleAnimTime, mCircleRadius)
                }
                4 -> {
                    searchView.image_user_4.visibility = View.VISIBLE
                    startCircularAnimation(searchView, searchView.image_user_4, mCircleAnimTime, mCircleRadius)
                }
                5 -> {
                    searchView.image_user_5.visibility = View.VISIBLE
                    startCircularAnimation(searchView, searchView.image_user_5, mCircleAnimTime, mCircleRadius)
                }
            }
        }, mDelay)
    }
}