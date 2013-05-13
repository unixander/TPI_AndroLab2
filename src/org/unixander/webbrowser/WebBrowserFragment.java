package org.unixander.webbrowser;

import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * 
 * Web Browser Demo
 * 
 * @version 1.0.0.0
 * @author unixander site: http://unixander.org
 * 
 */

@SuppressLint("SetJavaScriptEnabled")
public class WebBrowserFragment extends SherlockFragment implements
		OnClickListener {

	private ImageButton btnSearch, btnPrevious, btnNext;
	private ProgressBar progressSearch;
	private WebView webViewSearch;
	private EditText editTextSearch;
	private String address, title;
	private long id;

	private static final String WEBVIEW_STATE = "WEBVIEW_STATE",
			EDITTEXT_STATE = "EDITTEXT_STATE";

	public static WebBrowserFragment newInstance(long id) {
		WebBrowserFragment fragment = new WebBrowserFragment();
		Bundle args = new Bundle();
		args.putLong("id", id);
		fragment.setArguments(args);
		return fragment;
	}

	public String getTitle() {
		return this.title;
	}

	public long getID() {
		return getArguments().getLong("id", Long.MIN_VALUE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final Context contextThemeWrapper = new ContextThemeWrapper(
				getActivity(), R.style.Theme_Sherlock);
		LayoutInflater localInflater = inflater
				.cloneInContext(contextThemeWrapper);
		View view = localInflater.inflate(R.layout.web_fragment, container,
				false);
		this.title = "Untitled";
		this.id = getID();
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		btnSearch = (ImageButton) view.findViewById(R.id.search_button);
		btnSearch.setOnClickListener(this);
		progressSearch = (ProgressBar) view.findViewById(R.id.search_progress);
		webViewSearch = (WebView) view.findViewById(R.id.search_webview);
		editTextSearch = (EditText) view.findViewById(R.id.search_string);
		btnNext = (ImageButton) view.findViewById(R.id.next_page_btn);
		btnPrevious = (ImageButton) view.findViewById(R.id.previous_page_btn);

		if (savedInstanceState != null) {
			Bundle bundle = savedInstanceState.getBundle(WEBVIEW_STATE
					+ Long.toString(id));
			webViewSearch.restoreState(bundle);
			editTextSearch.setText(savedInstanceState.getString(EDITTEXT_STATE
					+ Long.toString(id)));
		}

		btnNext.setOnClickListener(this);
		btnPrevious.setOnClickListener(this);

		webViewSearch.getSettings().setJavaScriptEnabled(true);
		webViewSearch.getSettings().setSupportZoom(true);
		webViewSearch.getSettings().setBuiltInZoomControls(true);
		webViewSearch.getSettings().setDomStorageEnabled(true);
		webViewSearch.getSettings().setLightTouchEnabled(true);
		webViewSearch.setHorizontalScrollBarEnabled(true);
		webViewSearch.requestFocus(View.FOCUS_DOWN);
		webViewSearch.setWebChromeClient(new WebChromeClient());
		webViewSearch.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(getActivity(), "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				progressSearch.setVisibility(View.INVISIBLE);
				btnSearch.setVisibility(View.VISIBLE);
				editTextSearch.setText(webViewSearch.getUrl());
				title = webViewSearch.getTitle();
				if (webViewSearch.canGoBack())
					btnPrevious.setVisibility(View.VISIBLE);
				else
					btnPrevious.setVisibility(View.INVISIBLE);
				if (webViewSearch.canGoForward())
					btnNext.setVisibility(View.VISIBLE);
				else
					btnNext.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				progressSearch.setVisibility(View.VISIBLE);
				btnSearch.setVisibility(View.INVISIBLE);
			}
		});
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Bundle bundle = new Bundle();
		webViewSearch.saveState(bundle);
		outState.putBundle(WEBVIEW_STATE + Long.toString(id), bundle);
		outState.putString(EDITTEXT_STATE + Long.toString(id), editTextSearch
				.getText().toString());
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_button:
			address = editTextSearch.getText().toString();
			if (address.indexOf("http") == -1 && address.indexOf('.') > -1
					&& address.indexOf(' ') == -1)
				address = "http://" + address;
			try {
				new URL(address);
			} catch (MalformedURLException e) {
				address = "https://www.google.com.ua/search?q=" + address;
			}
			webViewSearch.loadUrl(address);
			break;
		case R.id.previous_page_btn:
			if (webViewSearch.canGoBack())
				webViewSearch.goBack();
			break;
		case R.id.next_page_btn:
			if (webViewSearch.canGoForward())
				webViewSearch.goForward();
			break;
		}

	}

}
