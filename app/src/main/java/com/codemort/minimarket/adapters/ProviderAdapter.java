package com.codemort.minimarket.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.codemort.minimarket.R;
import com.codemort.minimarket.model.ProviderVo;

import java.util.List;

public class ProviderAdapter extends RecyclerView.Adapter<ProviderAdapter.ProviderViewHolder>{

    List<ProviderVo> listProviders;

    public ProviderAdapter(List<ProviderVo> listProviders) {
        this.listProviders = listProviders;
    }

    @Override
    public ProviderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_provider,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new ProviderViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(ProviderViewHolder holder, int position) {
        holder.cardCodeProv.setText(listProviders.get(position).getCode().toString());
        holder.cardNameProv.setText(listProviders.get(position).getName().toString());
        holder.cardLastNameProv.setText(listProviders.get(position).getLast_name().toString());
        holder.cardProductProv.setText(listProviders.get(position).getProduct().toString());
        holder.cardPhoneProv.setText(listProviders.get(position).getPhone().toString());
        holder.cardEmailProv.setText(listProviders.get(position).getEmail().toString());
    }

    @Override
    public int getItemCount() {
        return listProviders.size();
    }

    public class ProviderViewHolder extends RecyclerView.ViewHolder{

        TextView cardCodeProv;
        TextView cardNameProv;
        TextView cardLastNameProv;
        TextView cardProductProv;
        TextView cardPhoneProv;
        TextView cardEmailProv;

        Button btnCardMakeOrder;
        Button btnCardDestroyProv;

        public ProviderViewHolder(View itemView) {
            super(itemView);
            cardCodeProv= (TextView) itemView.findViewById(R.id.cardCodeProv);
            cardNameProv= (TextView) itemView.findViewById(R.id.cardNameProv);
            cardLastNameProv= (TextView) itemView.findViewById(R.id.cardLastNameProv);
            cardProductProv= (TextView) itemView.findViewById(R.id.cardProductProv);
            cardPhoneProv= (TextView) itemView.findViewById(R.id.cardPhoneProv);
            cardEmailProv= (TextView) itemView.findViewById(R.id.cardEmailProv);

            btnCardMakeOrder= (Button) itemView.findViewById(R.id.btnCardMakeOrder);
            btnCardDestroyProv= (Button) itemView.findViewById(R.id.btnCardDestroyProv);

        }
    }
}
