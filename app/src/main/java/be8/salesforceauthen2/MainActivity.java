package be8.salesforceauthen2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            // Your implementation
                            sendRequest();
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();

//                try {
//                    sendRequest();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendRequest() throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        String dipChipServiceUrl = "https://kasikornbank--DipChip.cs72.my.salesforce.com/services/apexrest/DipChipService";
        String client_id = "3MVG910YPh8zrcR1.6xM.eQOAiIAYRgKQlmcRyCEvLDQGCogs2_lZaGg0GB3sNW72zBmUp00_uyjd689c.10p";
        String client_secret = "8584147721397286853";
        String token = "00D5D0000008uo3!AREAQAmUUC5.fnUwKSM7k23otjjnJGFQY0aO71CSp51kAStWejd5wx3hLqFduyCjQrTljBBkiG1mX7GJ6HMFuavN1iFsEg9D";

        JSONObject dipChipModel = new JSONObject();
        dipChipModel.put("IDENT_NO", "xxxxxxxxxxxxx");
        dipChipModel.put("BRTH_DT", "");
        dipChipModel.put("TH_TTL", "");

        JSONObject jsonParam = new JSONObject();
        jsonParam.put("dipchip_model", dipChipModel);

        URL url = new URL(dipChipServiceUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        // Create the SSL connection
        SSLContext sc;
        sc = SSLContext.getInstance("TLS");
        sc.init(null, null, new java.security.SecureRandom());
        conn.setSSLSocketFactory(sc.getSocketFactory());

        conn.setRequestMethod("POST");
        conn.addRequestProperty("client_id", client_id);
        conn.addRequestProperty("client_secret", client_secret);
        conn.setRequestProperty("Authorization", "OAuth " + token);
        conn.setRequestProperty("Content-Type", "application/json");
        try{
            conn.setDoOutput(true); // Write data to server
            conn.setDoInput(true); // Read data from server
            conn.setChunkedStreamingMode(0);

//            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
//            out.write(jsonParam.getBytes("UTF-8"));
//            writeStream(out);

            OutputStream out =  conn.getOutputStream();
            out.write(jsonParam.toString().getBytes("UTF-8"));
            writeStream(out);

            InputStream in = new BufferedInputStream(conn.getInputStream());
            readStream(in);
        } finally {
            conn.disconnect();
        }


    }

    private void readStream(InputStream in) {
        Log.d(TAG,"readStream ::"+in);
    }

    private void writeStream(OutputStream out) {
        Log.d(TAG,"writeStream ::"+out);
    }
}
