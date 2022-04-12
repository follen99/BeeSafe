package com.example.okhttpdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    // uso la libreria OkHttp
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Report report = new Report(66.2312,
                "descrizione",
                4,
                66.2312,
                1802848429L,
                4,
                "/path/",
                "place name",
                "datetime",
                5,
                "violenza",
                "follen99@gmail.com");

        Gson gson = new Gson();
        String jsonData = gson.toJson(report);

        MediaType mt = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mt, jsonData);

        /**
         * Richiesta effettuata usando la libreria OkHttp
         * Semantica più semplice, ma non può eseguire Toast nel thread secondario.
         * */
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://192.168.1.45:8080/reports")
                .addHeader("uid", "qCou2nf9I8UcEsju9yAMBDu8Ihv2")
                .addHeader("digest", "C3AA0E70ACB552D9E7FC504BA6618A6E")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("OkHttp", e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                Log.d("OkHttp", response.body().string());
            }
        });

        /**
         * richiesta effettuata con la libreria Volley
         * Semantica meno semplice da comprendere, ma può eseguire Toast nel thread secondario.
         * */
        requestWithSomeHttpHeaders();

    }

    public void requestWithSomeHttpHeaders() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.45:8080/reports";
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        Toast.makeText(MainActivity.this, "Operazione andata a buon fine", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR","error => "+error.toString());
                        Toast.makeText(MainActivity.this, "Errore", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("uid", "qCou2nf9I8UcEsju9yAMBDu8Ihv2");
                params.put("digest", "C3AA0E70ACB552D9E7FC504BA6618A6E");

                return params;
            }
        };
        queue.add(getRequest);

    }
}