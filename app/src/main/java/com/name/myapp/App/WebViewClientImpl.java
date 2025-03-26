package com.eroinnovations.qelmedistore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.View;
public class WebViewClientImpl extends WebViewClient {
    private final Activity activity;
    private final WebView webView;
    public WebViewClientImpl(Activity activity, WebView webView) {
        this.activity = activity;
        this.webView = webView;
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        String[] schemes = {
            "mailto:", "tel:", "sms:", "market://", "https://drive.google.com/",
            "https://docs.google.com/", "https://www.facebook.com/", "https://www.instagram.com/",
            "https://www.tiktok.com/", "https://twitter.com/", "https://www.youtube.com/",
            "https://www.linkedin.com/", "https://www.whatsapp.com/", "https://play.google.com/store/",
            "https://www.snapchat.com/", "https://www.pinterest.com/", "https://www.reddit.com/",
            "https://maps.google.com/", "https://www.spotify.com/", "https://www.netflix.com/",
            "https://www.amazon.com/", "https://www.ebay.com/", "https://www.zoom.us/", "https://slack.com/",
            "https://www.microsoft.com/", "https://www.apple.com/", "https://www.github.com/",
            "https://stackoverflow.com/", "clock:", "fm:", "settings:",
            "geo:", "http:", "https:", "file:", "ftp:", "sftp:", "rtsp:", "rtmp:", "ftp://",
            "ssh:", "nfc:", "bitcoin:", "chrome:", "chrome-native:", "googlechrome:", 
            "android-app:", "intent:", "fb:", "tg:", "twitter:", "whatsapp:", "mms:", 
            "viber:", "skype:", "zoom:", "line:", "wechat:", "signal:", "discord:", 
            "kakaotalk:", "zalo:", "messenger:", "hike:", "snapchat:", "yahoo:", "instagram://",
            "spotify://", "uber://", "lyft://", "airbnb://", "foursquare://", "yelp://",
            "tripadvisor://", "eventbrite://", "venmo:", "cashapp:", "paypal:", "paytm:",
            "razorpay:", "googlepay:", "applepay:", "amazonpay:", "facebook-messenger:",
            "twitter://", "google.maps://", "waze://", "uber://", "lyft://", "zellepay://",
            "snapchat://", "instagram://", "facebook://", "youtube://", "vimeo://", "twitch://"
        };
        for (String scheme : schemes) {
            if (url.startsWith(scheme)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
                return true; 
            }
        }
        Intent fallbackIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (fallbackIntent.resolveActivity(view.getContext().getPackageManager()) != null) {
            view.getContext().startActivity(fallbackIntent);
            return true;
        }
        return false;
    }
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        activity.findViewById(android.R.id.content).setBackgroundColor(Color.TRANSPARENT);
        webView.setVisibility(View.VISIBLE);
    }
}