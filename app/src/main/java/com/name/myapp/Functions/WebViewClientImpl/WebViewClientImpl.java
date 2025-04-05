package com.advance_native;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewClientImpl extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();

        if (url.startsWith("mailto:")) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        } else if (url.startsWith("tel:")) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        } else if (url.startsWith("market://")) {
            try {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } catch (android.content.ActivityNotFoundException e) {
                view.loadUrl("http://play.google.com/store/apps/" + url.substring(11));
            }
            return true;
        } else if (url.startsWith("https://drive.google.com/") || url.startsWith("https://docs.google.com/")) {
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return true;
        } else if (url.startsWith("https://www.facebook.com/") ||
                url.startsWith("https://www.instagram.com/") ||
                url.startsWith("https://www.tiktok.com/") ||
                url.startsWith("https://twitter.com/") ||
                url.startsWith("https://www.youtube.com/")) {
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return true;
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (intent.resolveActivity(view.getContext().getPackageManager()) != null) {
                view.getContext().startActivity(intent);
                return true;
            } else {
                return false;
            }
        }
    }
}
