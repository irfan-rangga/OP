package com.katapanda.op;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.katapanda.op.model.User;
import com.katapanda.op.spref.SharedPrefManager;
import com.katapanda.op.url.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Button btnLogin;
    private TextView txtLewat;
    private TextInputEditText edtUser, edtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        btnLogin = (Button) findViewById(R.id.btn_login);
        edtUser = (TextInputEditText) findViewById(R.id.edt_user);
        edtPass = (TextInputEditText) findViewById(R.id.edt_pass);


        progressDialog = new ProgressDialog(LoginActivity.this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                cekLogin();
            }
        });

    }

    private void cekLogin() {
        final String user = edtUser.getText().toString().trim();
        final String password = edtPass.getText().toString().trim();
        if (TextUtils.isEmpty(user)) {
            edtUser.setError("Mohon Maaf, Tolong Isi Username");
            edtUser.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edtPass.setError("Mohon Maaf, Tolong Isi Password");
            edtPass.requestFocus();
            return;
        }
        progressDialog.setTitle("Login User...");
        progressDialog.setMessage("Sedang dalam proses, Silahkan Tunggu!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("username", user);
            jsonParams.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URLs.URL_LOGIN, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // handle response
                        try {
                            String message = response.getString("message");
                            boolean success = response.getBoolean("success");
                            // Mengambil variable status pada response
                            if(success){
                                JSONObject userData = response.getJSONObject("data");
                                String token = response.getString("token");
                                User user = new User(
                                        userData.getInt("id"),
                                        userData.getString("nama_user"),
                                        "-",
                                        userData.getString("nama_lengkap"),
                                        token
                                );

                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                // Jika Login Sukses Maka pindah ke activity lain.
                                progressDialog.dismiss();
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            }else{
                                // Jika Login Gagal Maka mengeluarkan Toast dengan message.
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Mohon maaf terjadi kesalahan, Silahkan Coba Lagi", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //This indicates that the reuest has either time out or there is no connection
                    Toast.makeText(getApplicationContext(), "Tidak Ada Koneksi atau Kehabisan Waktu", Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    //Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(getApplicationContext(), "Kesalahan Otentikasi", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(getApplicationContext(), "Jaringan Anda Tidak Stabil", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(getApplicationContext(), "Server Bermaslah", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        // Buat antrian request pada cache android
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Tambahkan Request pada antrian request
        requestQueue.add(req);
    }
}
