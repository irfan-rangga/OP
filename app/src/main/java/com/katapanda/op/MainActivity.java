package com.katapanda.op;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.katapanda.op.adapter.PenjualanAdapter;
import com.katapanda.op.library.CusFormat;
import com.katapanda.op.model.Penjualan;
import com.katapanda.op.model.User;
import com.katapanda.op.spref.SharedPrefManager;
import com.katapanda.op.url.URLs;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    long waktuAwal;
    TextInputEditText txtAwal,txtAkhir;

    private LinearLayout linRange, linNama, linKategori, laporanBulan, laporanFilter;
    BottomSheetDialog bottomSheetDialog;
    Button btnFilter, btnFilterNama, btnFilterKategori;
    LinearLayout linFilterTgl, linFilterNama, linFilterKategori;
    boolean bolFilterTgl=false,bolFilterNama=false,bolFilterKategori=false;

    TextView totalHari, totalBulan, totalFilter, totalBarang;


    private List<Penjualan> listPenjualan;
    private RecyclerView recyclerView;
    private PenjualanAdapter penjualanAdapter;

    ArrayList<String> listKategori, listNama, listIdNama;
    String kategori="", nama="", idNama;
    SearchableSpinner spinnerKategori, spinnerNama;
    private ProgressDialog progressDialog;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listKategori = new ArrayList<>();
        listNama= new ArrayList<>();
        listIdNama= new ArrayList<>();

        progressDialog  = new ProgressDialog(MainActivity.this);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        spinnerKategori = (SearchableSpinner) findViewById(R.id.spinner_kategori);
        spinnerNama = (SearchableSpinner) findViewById(R.id.spinner_nama);
        spinnerKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kategori = listKategori.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerNama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nama = listNama.get(i);
                idNama = listIdNama.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        dataSearch();

        linRange = (LinearLayout) findViewById(R.id.lin_range);
        linNama = (LinearLayout) findViewById(R.id.lin_nama_brg);
        linKategori = (LinearLayout) findViewById(R.id.lin_kategori);
        laporanBulan = (LinearLayout) findViewById(R.id.lin_laporan_bulan);
        laporanFilter = (LinearLayout) findViewById(R.id.lin_laporan_filter);

        laporanBulan.setVisibility(View.GONE);
        laporanFilter.setVisibility(View.GONE);

        totalHari = (TextView) findViewById(R.id.total_hari);
        totalBulan = (TextView) findViewById(R.id.total_bulan);
        totalFilter = (TextView) findViewById(R.id.total_filter);
        totalBarang = (TextView) findViewById(R.id.total_barang);

        linFilterTgl = (LinearLayout) findViewById(R.id.lin_filter_tgl);
        linFilterNama = (LinearLayout) findViewById(R.id.lin_filter_nama);
        linFilterKategori = (LinearLayout) findViewById(R.id.lin_filter_kategori);
        linFilterTgl.setVisibility(View.GONE);
        linFilterNama.setVisibility(View.GONE);
        linFilterKategori.setVisibility(View.GONE);

        linRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bolFilterNama= false;
                linFilterNama.setVisibility(View.GONE);
                bolFilterKategori= false;
                linFilterKategori.setVisibility(View.GONE);
                if (bolFilterTgl) {
                    bolFilterTgl = !bolFilterTgl;
                    linFilterTgl.setVisibility(View.GONE);
                } else {
                    bolFilterTgl = !bolFilterTgl;
                    linFilterTgl.setVisibility(View.VISIBLE);
                    dilterTgl();
                }
            }
        });
        linNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bolFilterTgl = false;
                linFilterTgl.setVisibility(View.GONE);
                bolFilterKategori= false;
                linFilterKategori.setVisibility(View.GONE);
                if (bolFilterNama) {
                    bolFilterNama= !bolFilterNama;
                    linFilterNama.setVisibility(View.GONE);
                } else {
                    bolFilterNama = !bolFilterNama;
                    linFilterNama.setVisibility(View.VISIBLE);
                    dilterNama();
                }
            }
        });
        linKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bolFilterTgl = false;
                linFilterTgl.setVisibility(View.GONE);
                bolFilterNama= false;
                linFilterNama.setVisibility(View.GONE);
                if (bolFilterKategori) {
                    bolFilterKategori= !bolFilterKategori;
                    linFilterKategori.setVisibility(View.GONE);
                } else {
                    bolFilterKategori = !bolFilterKategori;
                    linFilterKategori.setVisibility(View.VISIBLE);
                    dilterKategori();
                }
            }
        });

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);


        dialogDate();
        initRec();

    }
    private void dataSearch() {
        progressDialog.setTitle("Prosess...");
        progressDialog.setMessage("Sedang dalam proses, Silahkan Tunggu!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.GET, URLs.URL_KATEGORI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responOBJ = new JSONObject(response);
                    JSONArray dataArray = responOBJ.getJSONArray("data");
                    JSONArray dataBarang = responOBJ.getJSONArray("barang");

                    for (int i=0; i<dataArray.length(); i++) {
                        JSONObject c = dataArray.getJSONObject(i);
                        listKategori.add(c.getString("kategori"));
                    }
                    spinnerKategori.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner, listKategori));

                    for (int i=0; i<dataBarang.length(); i++) {
                        JSONObject c = dataBarang.getJSONObject(i);
                        listNama.add(c.getString("nama_brg"));
                        listIdNama.add(c.getString("kode_brg"));
                    }
                    spinnerNama.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner, listNama));
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Mohon Maaf Terjadi Kesalahan, Silahkan Coba Lagi",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        // Buat antrian request pada cache android
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        // Tambahkan Request pada antrian request
        requestQueue.add(request);
    }
    private void initRec() {
        //Initial data absensi rec
        recyclerView = (RecyclerView) findViewById(R.id.rec_penjualan);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
//        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(true);
    }

    private void dilterTgl() {
        btnFilter = (Button) findViewById(R.id.btn_filter);
        txtAwal = (TextInputEditText) findViewById(R.id.edt_tgl_awal);
        txtAwal.setInputType(InputType.TYPE_NULL);
        txtAwal.requestFocus();
        txtAkhir = (TextInputEditText) findViewById(R.id.edt_tgl_akhir);
        txtAkhir.setInputType(InputType.TYPE_NULL);
        txtAwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDatePickerDialog.show();
            }
        });
        txtAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                toDatePickerDialog.getDatePicker().setMinDate(waktuAwal);
                toDatePickerDialog.show();
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String tglAwal = txtAwal.getText().toString().trim();
                final String tglAkhir = txtAkhir.getText().toString().trim();
                if (TextUtils.isEmpty(tglAwal)) {
                    txtAwal.setError("Mohon Maaf, Silahkan Isi Tanggal Awal");
                    txtAwal.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(tglAkhir)) {
                    txtAkhir.setError("Mohon Maaf, Silahkan Isi Tanggal Akhir");
                    txtAkhir.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                dataFilter(1, tglAwal, tglAkhir);
            }
        });

    }

    private void dilterNama() {
        btnFilterNama = (Button) findViewById(R.id.btn_filter_nama);
        txtAwal = (TextInputEditText) findViewById(R.id.edt_tgl_awal_nama);
        txtAwal.setInputType(InputType.TYPE_NULL);
        txtAwal.requestFocus();
        txtAkhir = (TextInputEditText) findViewById(R.id.edt_tgl_akhir_nama);
        txtAkhir.setInputType(InputType.TYPE_NULL);
        txtAwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDatePickerDialog.show();
            }
        });
        txtAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                toDatePickerDialog.getDatePicker().setMinDate(waktuAwal);
                toDatePickerDialog.show();
            }
        });
        btnFilterNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String tglAwal = txtAwal.getText().toString().trim();
                final String tglAkhir = txtAkhir.getText().toString().trim();
                if (TextUtils.isEmpty(tglAwal)) {
                    txtAwal.setError("Mohon Maaf, Silahkan Isi Tanggal Awal");
                    txtAwal.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(tglAkhir)) {
                    txtAkhir.setError("Mohon Maaf, Silahkan Isi Tanggal Akhir");
                    txtAkhir.requestFocus();
                    return;
                }
                if (nama.equals("")){
                    Snackbar.make(findViewById(R.id.lay_main), "Nama Harus Di Pilih", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                dataFilter(2, tglAwal, tglAkhir);
            }
        });

    }
    private void dilterKategori() {
        btnFilterKategori = (Button) findViewById(R.id.btn_filter_kategori);
        txtAwal = (TextInputEditText) findViewById(R.id.edt_tgl_awal_kategori);
        txtAwal.setInputType(InputType.TYPE_NULL);
        txtAwal.requestFocus();
        txtAkhir = (TextInputEditText) findViewById(R.id.edt_tgl_akhir_kategori);
        txtAkhir.setInputType(InputType.TYPE_NULL);
        txtAwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDatePickerDialog.show();
            }
        });
        txtAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                toDatePickerDialog.getDatePicker().setMinDate(waktuAwal);
                toDatePickerDialog.show();
            }
        });
        btnFilterKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String tglAwal = txtAwal.getText().toString().trim();
                final String tglAkhir = txtAkhir.getText().toString().trim();
                if (TextUtils.isEmpty(tglAwal)) {
                    txtAwal.setError("Mohon Maaf, Silahkan Isi Tanggal Awal");
                    txtAwal.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(tglAkhir)) {
                    txtAkhir.setError("Mohon Maaf, Silahkan Isi Tanggal Akhir");
                    txtAkhir.requestFocus();
                    return;
                }
                if (kategori.equals("")){
                    Snackbar.make(findViewById(R.id.lay_main), "Nama Harus Di Pilih", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                dataFilter(3, tglAwal, tglAkhir);
            }
        });

    }

    private void dialogDate() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                waktuAwal = newDate.getTimeInMillis();
                txtAwal.setText(dateFormatter.format(newDate.getTime()));
                txtAkhir.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        toDatePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                if (waktuAwal > newDate.getTimeInMillis()) {
                    Toast.makeText(MainActivity.this, "Maaf Tanggal Akhir Salah", Toast.LENGTH_SHORT).show();
                } else{
                    txtAkhir.setText(dateFormatter.format(newDate.getTime()));
                }
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void dataFilter(int flag, String tglAwal, String tglAkhir) {

        JSONObject jsonParams = new JSONObject();
        String UR = "";
        try {
            jsonParams.put("tgl_mulai", tglAwal);
            jsonParams.put("tgl_akhir", tglAkhir);
            if (flag == 1) {
                UR = URLs.URL_PENJUALAN;
            }
            if (flag == 2){
                UR = URLs.URL_PENJUALANB;
                jsonParams.put("kode_brg", idNama);
            }
            if (flag == 3){
                UR = URLs.URL_PENJUALANK;
                jsonParams.put("kategori", kategori);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, UR, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // handle response
                        try {
                            String message = response.getString("message");
                            boolean success = response.getBoolean("success");
                            // Mengambil variable status pada response
                            if(success){
                                JSONArray data = response.getJSONArray("data");
                                double total = response.getDouble("total");
                                String totalB = response.getString("total_barang");
                                double total_bln = response.getDouble("total_bulan");
                                double total_harian = response.getDouble("total_hari");

                                totalFilter.setText(CusFormat.rupiah(total));
                                totalBarang.setText(totalB);
                                totalHari.setText(CusFormat.rupiah(total_harian));
                                totalBulan.setText(CusFormat.rupiah(total_bln));

                                laporanBulan.setVisibility(View.VISIBLE);
                                laporanFilter.setVisibility(View.VISIBLE);

                                listPenjualan= new ArrayList<Penjualan>();
                                for (int i = 0; i < data.length(); i++)
                                {
                                    JSONObject objectEvent= data.getJSONObject(i);
                                    Penjualan penjualan = new Penjualan();
                                    penjualan.setTglBrg(objectEvent.getString("tgl_jual"));
                                    penjualan.setNamaBrg(objectEvent.getString("nama_brg"));
                                    penjualan.setHargaBrg(objectEvent.getDouble("hrg_sat"));
                                    penjualan.setJmlBrg(objectEvent.getDouble("qty"));
                                    penjualan.setTotalBrg(objectEvent.getDouble("total_jual"));
                                    penjualan.setKategoriBrg(objectEvent.getString("kategori"));
                                    listPenjualan.add(penjualan);
                                }
                                penjualanAdapter = new PenjualanAdapter(MainActivity.this, listPenjualan);
                                recyclerView.setAdapter(penjualanAdapter);
                                penjualanAdapter.notifyDataSetChanged();

                                bolFilterTgl = false;
                                bolFilterNama = false;
                                bolFilterKategori = false;
                                linFilterTgl.setVisibility(View.GONE);
                                linFilterNama.setVisibility(View.GONE);
                                linFilterKategori.setVisibility(View.GONE);
                            }else{
                                // Jika Login Gagal Maka mengeluarkan Toast dengan message.
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Mohon maaf terjadi kesalahan, Silahkan Coba Lagi", Toast.LENGTH_LONG).show();
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
                progressBar.setVisibility(View.GONE);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
