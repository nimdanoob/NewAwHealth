package com.think.awhealth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.think.awhealth.ui.about.AboutActivity;
import com.think.awhealth.ui.base.BaseMainActivity;
import com.think.awhealth.ui.healthInfor.HealthInforViewPagerFragment;
import com.think.awhealth.ui.map.PeripheralMapActivity;
import com.think.awhealth.ui.question.QuestionActivity;
import com.think.awhealth.ui.search.SearchActivity;
import com.think.awhealth.ui.search.SearchFragment;
import com.think.awhealth.ui.setting.SettingsActivity;
import com.think.awhealth.util.AlarmManagerUtils;
import com.think.awhealth.util.ShareUtils;

public class MainActivity extends BaseMainActivity {

    private FragmentManager mFragmentManager;
    private int mCurrentItemId;
    private Menu menu;
    private SearchFragment mSearchFragment;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlarmManagerUtils.register(this);

        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        initVariables();
        initData();
        switchFragment(new HealthInforViewPagerFragment(), getString(R.string.title_HealthInfor), R.menu.menu_healthinfor);
        mNavigationView.setCheckedItem(R.id.nav_healthInfor);
    }

    private void initData() {
        Integer params=1;
        if (params instanceof Object){
        }
    }

    private void initVariables() {
        mFragmentManager = getSupportFragmentManager();
    }


    @Override
    public boolean navigationItemSelected(MenuItem id) {
        int itemId = id.getItemId();
        //如果选的是一样的不做操作
        if (mCurrentItemId == itemId)
            return false;

        Boolean selectedItemWillNoChanged = true;

        switch (itemId) {
            case R.id.nav_healthInfor:
                switchFragment(new HealthInforViewPagerFragment(), getString(R.string.title_HealthInfor), R.menu.menu_healthinfor);
                selectedItemWillNoChanged = false;
                break;
            case R.id.nav_setting:
                toSetting();
                break;
            case R.id.nav_query:
                forwardToActivity(SearchActivity.class);
                //因为这个是Activity，所以回退的时候要保持menu的一致
                break;
            case R.id.nav_map:
                forwardToActivity(PeripheralMapActivity.class );

                break;
            case R.id.nav_ask:
                forwardToActivity(QuestionActivity.class);
                break;

            case R.id.nav_share:
                ShareUtils.share(this,R.string.share_text);
                break;
            case R.id.nav_about:
                forwardToActivity(AboutActivity.class);
                break;

        }
        //如果跳转的是actiivity,不对menu 的选项做出改变，因为回退时需要保持一致
        if (selectedItemWillNoChanged == true){
            itemId = mCurrentItemId;
            mNavigationView.setCheckedItem(itemId);
            return false;
        }

        //说明选项是个 fragment
        mCurrentItemId = itemId;
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_healthinfor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        Environment.getExternalStorageDirectory();
        getExternalCacheDir();

        return super.onOptionsItemSelected(item);
    }

    /**
     * switch 要显示的fragment
     * @param fragment 要显示的fragment
     * @param title 标题
     * @param resourceMenu 菜单的资源id,替换MainActivity的菜单资源
     */
    private void switchFragment(Fragment fragment, String title, int resourceMenu) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.id_fragmentContainer, fragment);
        getSupportActionBar().setTitle(title);
        fragmentTransaction.commit();

        if (menu != null) {
            menu.clear();
            getMenuInflater().inflate(resourceMenu, menu);
        }
    }

    /**
     * 跳转到Setting 界面
     */
    private void toSetting() {
        Intent intent = new Intent();
        intent.setClass(this, SettingsActivity.class);
        startActivity(intent);
    }
    private void forwardToActivity(Class<?> classActivity){
        Intent intent = new Intent();
        intent.setClass(this, classActivity);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
