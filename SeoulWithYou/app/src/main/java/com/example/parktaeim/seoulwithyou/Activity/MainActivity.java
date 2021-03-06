package com.example.parktaeim.seoulwithyou.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.parktaeim.seoulwithyou.Adapter.ViewPagerAdapter;
import com.example.parktaeim.seoulwithyou.CustomViewPager;
import com.example.parktaeim.seoulwithyou.Network.Service;
import com.example.parktaeim.seoulwithyou.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.stone.pile.libs.PileLayout;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private TabLayout tabLayout;
    private CustomViewPager viewPager;

    private Toolbar toolbar;
    private ImageView drawerBtn;
    private DrawerLayout drawerLayout;

    public static int screenWidth, screenHeight;

    private MaterialSearchView searchView;
    private ArrayList<String> courseTitleArrayList = new ArrayList<>();
    private String[] courseTitleArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences idPref = getSharedPreferences("myId",MODE_PRIVATE);
        SharedPreferences tokenPref = getSharedPreferences("tokenPref",MODE_PRIVATE);


        setDrawer();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;// 가로
        screenHeight = displayMetrics.heightPixels;// 세로

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        tabLayout.addTab(tabLayout.newTab().setText("음식"));
        tabLayout.addTab(tabLayout.newTab().setText("전통/역사"));
        tabLayout.addTab(tabLayout.newTab().setText("현대"));
        tabLayout.addTab(tabLayout.newTab().setText("예술/문화"));
        tabLayout.addTab(tabLayout.newTab().setText("힐링"));


        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setPagingEnabled(false);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        getCourseTitle();
        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        courseTitleArray = courseTitleArrayList.toArray(new String[courseTitleArrayList.size()]);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });
        searchView.setSuggestions(courseTitleArray);
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String query = (String) adapterView.getItemAtPosition(position);
                searchView.closeSearch();
                Log.d("search click query===",query);

            }
        });

        CircleImageView header_profileImg = (CircleImageView) findViewById(R.id.mypage_header_profile);
//        Glide.with(getApplicationContext()).load(R.drawable.icon_profile).into(header_profileImg);

    }

    private void getCourseTitle() {
        courseTitleArrayList.add("ff");
        courseTitleArrayList.add("ffdd");
        courseTitleArrayList.add("ffee");

        Service.getRetrofit(getApplicationContext()).getModernCourseList().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray jsonArray = response.body().getAsJsonArray("data");
                for(int i =0;i<jsonArray.size();i++){
                    JsonObject jsonObject = (JsonObject) jsonArray.get(i);
                    String courseTitle = jsonObject.getAsJsonPrimitive("title").getAsString();
                    Log.d("courseTitle ==",courseTitle);
                    courseTitleArrayList.add(courseTitle);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void setDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolBar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);

        CircleImageView header_profileImg = (CircleImageView) header.findViewById(R.id.mypage_header_profile);
        TextView header_name = (TextView) header.findViewById(R.id.mypage_header_myname);

        SharedPreferences tokenPref = getSharedPreferences("tokenPref", MODE_PRIVATE);
        SharedPreferences sharedPreferences = getSharedPreferences("myId",MODE_PRIVATE);

        com.example.parktaeim.seoulwithyou.Network.Service.getRetrofit(getApplicationContext()).mypage_info(tokenPref.getString("token", "null"), sharedPreferences.getString("myId","null")).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("mainac info retrofit===", String.valueOf(response.code()));

                if (response.code() == 200) {
                    JsonObject jsonObject = response.body();
                    Log.d("info object ==",response.body().toString());
                    String name = jsonObject.getAsJsonPrimitive("name").getAsString();
//                    image = jsonObject.getAsJsonPrimitive("image").getAsString();

//                    Glide.with(getApplicationContext()).load(image).into(header_profileImg);
                    header_name.setText(name);

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

        if(searchView.isSearchOpen()){
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mypage) {
            Intent intent = new Intent(MainActivity.this,MyPageDialogActivity.class);
            SharedPreferences sharedPreferences = getSharedPreferences("myId",MODE_PRIVATE);
            intent.putExtra("mypage_id",sharedPreferences.getString("myId","null"));
            startActivity(intent);

        } else if (id == R.id.nav_changePw) {
            Intent intent = new Intent(MainActivity.this,ChangePwActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                    .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            SharedPreferences sharedPreferences = getSharedPreferences("myId",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            Collection<?> collection = sharedPreferences.getAll().values();
                            Log.d("before clear pef===",collection.toString());

                            SharedPreferences tokenPref = getSharedPreferences("tokenPref",MODE_PRIVATE);
                            SharedPreferences.Editor tokenEditor = tokenPref.edit();

                            Collection<?> tokenCollection = tokenPref.getAll().values();
                            Log.d("before clear pef===",tokenCollection.toString());

                            editor.clear();
                            editor.commit();
                            tokenEditor.clear();
                            tokenEditor.commit();

                            Collection<?> collection2 = sharedPreferences.getAll().values();
                            Log.d("after clear pef===",collection2.toString());

                            Collection<?> tokenCollection2 = tokenPref.getAll().values();
                            Log.d("before clear pef===",tokenCollection2.toString());

                            finish();   //SettingsActivity finish

                            Intent i = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    })
                    .show();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search_menu, menu);
//
//        MenuItem item = menu.findItem(R.id.action_search);
//        searchView.setMenuItem(item);
//
//        return true;
//    }


}
