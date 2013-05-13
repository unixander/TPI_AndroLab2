package org.unixander.webbrowser;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.TitlePageIndicator;

import android.os.Bundle;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * 
 * Web Browser Demo Using ActionBarSherlock and ViewPageIndicator libraries for
 * support devices 2.x
 * 
 * @version 1.0.0.0
 * @author unixander site: http://unixander.org
 * 
 */
public class MainActivity extends SherlockFragmentActivity {

	private WBFragmentAdapter fragmentAdapter;
	private ViewPager viewPager;
	private TitlePageIndicator indicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fragmentAdapter = new WBFragmentAdapter(getSupportFragmentManager());
		viewPager = (ViewPager) findViewById(R.id.pager);
		indicator = (TitlePageIndicator) findViewById(R.id.indicator);
		
		viewPager.setAdapter(fragmentAdapter);
		indicator.setViewPager(viewPager);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.createTab:
			fragmentAdapter.addNewTab();
			viewPager.setCurrentItem(fragmentAdapter.getCount() - 1);
			return true;
		case R.id.closeTab:
			this.closeCurrentTab();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void closeCurrentTab() {
		int position = viewPager.getCurrentItem();
		if (position == 0 && fragmentAdapter.getCount() == 1) {
			fragmentAdapter.addNewTab();
		}
		fragmentAdapter.removeCurrentTab(position);
		viewPager.setCurrentItem(position);
	}

	class WBFragmentAdapter extends FragmentStatePagerAdapter {
		private long last_id = 0;
		private ArrayList<WebBrowserFragment> fragments;

		public WBFragmentAdapter(FragmentManager fm) {
			super(fm);
			fragments = new ArrayList<WebBrowserFragment>();
			this.addNewTab();
		}

		public void addNewTab() {
			last_id++;
			fragments.add(WebBrowserFragment.newInstance(last_id));
			this.notifyDataSetChanged();
		}

		public void removeCurrentTab(int index) {
			if(index<fragments.size()&&index>=0)
				fragments.remove(index);
			this.notifyDataSetChanged();
			indicator.notifyDataSetChanged();
		}

		@Override
		public int getItemPosition(Object object) {
			int index = fragments.indexOf(object);
			if (index == -1)
				return POSITION_NONE;
			else
				return index;
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return fragments.get(position).getTitle();
		}
	}

}
