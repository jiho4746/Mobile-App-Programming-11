package com.example.myapplication11

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication11.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.reflect.KProperty

//ActionBar / ToolBar (사용자가 직접 제어) / Fragment / Toogle / AndroidX
//리사이클러뷰(fragment1.kt) : ViewHoler, Adapter, LayoutManager (필수), ItemDecoration (선택)
//뷰페이저2(MainActivity.kt) : 스와이프로 넘기는 화면 구성
//드로우 레이아웃 : 여펭서 열리는 화면 구성 , Tooggle 버튼 함께 제공
//androidx : AppBarLayout, CoordinateLayout, TabLayout&tab(fragment2)
//네비게이션 뷰: 위쪽은 아이콘과 문자열, 아래쪽은 메뉴항목 나열(navigation_header, menu)
class MainActivity : AppCompatActivity() {
    //ViewPager2는 Adapter 필요
    class MyFragmentAdapter(activity:FragmentActivity) : FragmentStateAdapter(activity){
        val fragments:List<Fragment>
        init{
            fragments = listOf(Fragment1(), Fragment2(), Fragment3())
        }
        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }

    //다른 곳에서도 사용할 수 있게 전역변수로 binding 선언!!
    //lazy 초기화를 늦춤(오류 해결)
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    //toogle 생성
    lateinit var toogle : ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        //viewBinding
        //val binding = ActivityMainBinding.inflate(layoutInflater)

        //[ToolBar]
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        //[Tooogle]
        toogle = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_open, R.string.drawer_close) //R.string.drawer_open
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toogle.syncState() //->onOptionsItemSelected로!!

        //ViewPager2 사용하면서 [1]ragmentManager 작성 부분 주석처리
        //[Fragment를 activity_main에 가져올 때!!]
        //** [values-themes-themes.xml] 파일에서  parent="Theme.MaterialComponents.Light.NoActionBar"
        //val fragmentManager : FragmentManager = supportFragmentManager
        //val transaction : FragmentTransaction = supportFragmentManager.beginTransaction()
        //var fragment = Fragment1()
        //transaction.add(R.id.fragment_content, fragment)
        //transaction.commit()

        //[2]viewpager2 사용 (swipe 동작) -> gradle에 viewpage2 추가
        binding.viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL //가로로 넘기기
        binding.viewpager.adapter = MyFragmentAdapter(this)

        //Tab과 ViewPager2 연동
       TabLayoutMediator(binding.tab1, binding.viewpager){
            tab, position -> tab.text = "TAB ${position+1}"
        }.attach()

        //Navigation event
        binding.mainDrawerView.setNavigationItemSelectedListener {
        Log.d("mobileApp", "Navigation selected... ${it.title}")
            true
        }
        binding.fab.setOnClickListener { //플로팅 액션 버튼 클릭 이벤트
            when(binding.fab.isExtended){
                true -> binding.fab.shrink()
                false -> binding.fab.extend()
            }
        }
    }

    //옵션 메뉴 만들기 -> res 디렉터리 생성(menu 타입)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //변수를 이용한 메뉴 만들기
        //val menuItem1 : MenuItem? = menu?.add(0, 0, 0, "메뉴1")
        //val menuItem2 : MenuItem? = menu?.add(0, 1, 0, "메뉴2")

        //res - menu 디렉터리 안의 menu_xml을 가져옴
        menuInflater.inflate(R.menu.menu_main, menu)

        //menu_search 만들기, appcompat 라이브러리 : 검색
        //import androidx.appcompat.widget.SearchView
        val menuSearch = menu?.findItem(R.id.menu_search)
        val searchView = menuSearch?.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                //binding.tv1.text = p0
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    //메뉴 아이템이 설정될 때의 작업
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //toggle 선택되었는지 확인
        if(toogle.onOptionsItemSelected(item)) return true
        //menu_main에서 설정한 id 이용
        when(item.itemId){
            R.id.menu1 -> {
                //TextView의 색상을 변경
               //binding.tv1.setTextColor(Color.BLUE)
                true
            }
            R.id.menu2 -> {
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}