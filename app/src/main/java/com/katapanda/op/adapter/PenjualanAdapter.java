package com.katapanda.op.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.katapanda.op.R;
import com.katapanda.op.library.CusFormat;
import com.katapanda.op.model.Penjualan;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class PenjualanAdapter extends RecyclerView.Adapter<PenjualanAdapter.MyViewHolder> {
    private Context mContext;
    private List<Penjualan> mData;

    public PenjualanAdapter(Context mContext, List<Penjualan> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.list_view_penjualan, parent, false);

        final MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tanggal.setText(CusFormat.tanggalFormat(mData.get(position).getTglBrg(), "yyyy-mm-dd", "dd-MMMM-yyyy"));
        holder.namaBarang.setText(mData.get(position).getNamaBrg());
        holder.kategoriBrg.setText(mData.get(position).getKategoriBrg());
        holder.harga.setText(CusFormat.rupiah(mData.get(position).getHargaBrg()));
        holder.jml.setText(String.valueOf(mData.get(position).getJmlBrg()));
        holder.hargaTotal.setText(CusFormat.rupiah(mData.get(position).getTotalBrg()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tanggal, namaBarang, kategoriBrg, harga, jml, hargaTotal;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tanggal= itemView.findViewById(R.id.txt_list_tanggal);
            namaBarang= itemView.findViewById(R.id.txt_list_nama);
            kategoriBrg= itemView.findViewById(R.id.txt_list_kategori);
            harga= itemView.findViewById(R.id.txt_list_harga);
            jml= itemView.findViewById(R.id.txt_list_jml);
            hargaTotal= itemView.findViewById(R.id.txt_list_total);
//            img_thumbnail= itemView.findViewById(R.id.img_track);
        }
    }
}
