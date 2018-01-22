package com.firefish.gadidreader;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context ctx = this;
        final TextView text = (TextView)findViewById(R.id.gadid);

        getGoogleAdId(this, new FGOnDeviceIdsRead() {
            @Override
            public void onGoogleAdIdRead(String googleAdId) {
                if (null != googleAdId) {
                    text.setText(googleAdId);
                    copy2Clipboard("google advertising id", googleAdId);
                    Toast.makeText(ctx, "Copied to clipboard!", Toast.LENGTH_LONG).show();
                }else {
                    text.setText("Get google advertising id failed!");
                    Toast.makeText(ctx, "Get google advertising id failed!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void copy2Clipboard(final String label, final String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }

    public static String getPlayAdId(Context context) {
        return FGReflection.getPlayAdId(context);
    }

    public static void getGoogleAdId(Context context, final FGOnDeviceIdsRead onDeviceIdRead) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            String GoogleAdId = getPlayAdId(context);
            onDeviceIdRead.onGoogleAdIdRead(GoogleAdId);
            return;
        }

        new AsyncTask<Context,Void,String>() {
            @Override
            protected String doInBackground(Context... params) {
                Context innerContext = params[0];
                String innerResult = getPlayAdId(innerContext);
                return innerResult;
            }

            @Override
            protected void onPostExecute(String playAdiId) {
                onDeviceIdRead.onGoogleAdIdRead(playAdiId);
            }
        }.execute(context);
    }


}
