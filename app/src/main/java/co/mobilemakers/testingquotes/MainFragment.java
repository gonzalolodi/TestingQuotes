package co.mobilemakers.testingquotes;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    TextView mTextQuotes;
    Button mButtonGet;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mTextQuotes = (TextView) rootView.findViewById(R.id.text_view_quote);
        mButtonGet = (Button) rootView.findViewById(R.id.button_get);
        mButtonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    URL url = constructURLQuery();
                    Request request = new Request.Builder().url(url.toString()).build();
                    OkHttpClient client = new OkHttpClient();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            final String responseString = response.body().string();

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mTextQuotes.setText(responseString);
                                }
                            });
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        return rootView;
    }

    private URL constructURLQuery() throws MalformedURLException {
        final String IHEART_QUOTES_BASE_URL = "iheartquotes.com";
        final String API = "api";
        final String VERSION_PATH = "v1";
        final String RANDOM_PATH = "random";
        final String QUOTES_ENDPOINT = "?source=misc";

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").authority(IHEART_QUOTES_BASE_URL).
                appendPath(API).
                appendPath(VERSION_PATH).
                appendPath(RANDOM_PATH).
                appendPath(QUOTES_ENDPOINT);
        Uri uri = builder.build();

        return new URL(uri.toString());
    }
}
