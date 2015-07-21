package org.guateora.oraconmigo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import org.guateora.oraconmigo.R;

/**
 * Created by franz on 7/20/2015.
 */
public class FragmentUnete extends Fragment {

    private static final String URL_WEB_VIEW = "http://guateora.org/unete-al-movimiento/";

    private ProgressBar progressBar;

    public static FragmentUnete newInstance() { return new FragmentUnete(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unete, container, false);

        WebView myWebView = (WebView) view.findViewById(R.id.webView_unete);
        myWebView.loadUrl(URL_WEB_VIEW);

        return view;
    }
}
