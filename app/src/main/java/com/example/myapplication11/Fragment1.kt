package com.example.myapplication11

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication11.databinding.Fragment1Binding
import com.example.myapplication11.databinding.ItemRecyclerviewBinding
import java.util.zip.Inflater

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

//fragment - ViewRecycler , ViewPage2

//Fragment에서 Recycler 제어
class MyViewHolder(val binding : ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)
class MyAdapter(val datas:MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        binding.itemTv.text = datas[position]
    }
}

//Fragment Decoration을 위한 클래스
class MyDecoration(val context: Context) : RecyclerView.ItemDecoration() {
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        //[res-drawable]안의 이미지 사용
        super.onDraw(c, parent, state)
        c.drawBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.stadium),0f,0f, null)

    }
    //모두 그러진 후에 호출되는 함수
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        //스마트폰의 크기 / 2 - 이미지크기 / 2
        val width = parent.width
        val height = parent.height

        val dr: Drawable? = ResourcesCompat.getDrawable(context.resources, R.drawable.kbo, null)
        val d_width = dr?.intrinsicWidth
        val d_height = dr?.intrinsicHeight

        val left = width/2- d_width?.div(2) as Int
        val top = height/2- d_height?.div(2) as Int
        //스마트폰의 중앙에 위치
        c.drawBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.kbo),left.toFloat(),top.toFloat(), null)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(10, 10, 10, 0)
        view.setBackgroundColor(Color.LTGRAY)
        view.setBackgroundColor(Color.parseColor("#49c1ff"))
        ViewCompat.setElevation(view, 20.0f)
    }
}
class Fragment1 : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    //fragment 사용시 반드시 작성!!! onCreateView
    //목록에 대한 정보를 저장하고 MyAdapter로 전달
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val datas = mutableListOf<String>()
        for(i in 1..9){
            datas.add("item $i")
        }
        val binding = Fragment1Binding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(activity)
        //[LinearLayout (수평적)]
        //layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        //[GridLayout]
        //val layoutManager = GridLayoutManager(activity, 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = MyAdapter(datas)

        //Decoration 추가
        binding.recyclerView.addItemDecoration(MyDecoration(activity as Context))

        MyAdapter(datas)

        //return inflater.inflate(R.layout.fragment_1, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}