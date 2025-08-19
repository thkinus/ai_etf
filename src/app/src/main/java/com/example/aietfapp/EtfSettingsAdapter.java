package com.example.aietfapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EtfSettingsAdapter extends RecyclerView.Adapter<EtfSettingsAdapter.ViewHolder> {
    
    public interface OnEtfSettingsListener {
        void onEtfToggled(int position, boolean enabled);
        void onEtfDeleted(int position);
        void onEtfMoved(int fromPosition, int toPosition);
    }
    
    private List<EtfData> etfList;
    private OnEtfSettingsListener listener;
    
    public EtfSettingsAdapter(List<EtfData> etfList, OnEtfSettingsListener listener) {
        this.etfList = etfList;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_etf_settings, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EtfData etf = etfList.get(position);
        
        holder.symbolText.setText(etf.getSymbol());
        holder.nameText.setText(etf.getName());
        holder.enabledSwitch.setChecked(etf.isEnabled());
        
        // Switch 변경 리스너
        holder.enabledSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onEtfToggled(holder.getAdapterPosition(), isChecked);
            }
        });
        
        // 삭제 버튼 클릭 리스너
        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEtfDeleted(holder.getAdapterPosition());
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return etfList.size();
    }
    
    public void moveItem(int fromPosition, int toPosition) {
        EtfData item = etfList.remove(fromPosition);
        etfList.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
        
        if (listener != null) {
            listener.onEtfMoved(fromPosition, toPosition);
        }
    }
    
    public void removeItem(int position) {
        etfList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, etfList.size());
    }
    
    public void addItem(EtfData etf) {
        etfList.add(etf);
        notifyItemInserted(etfList.size() - 1);
    }
    
    public void updateList(List<EtfData> newList) {
        this.etfList = newList;
        notifyDataSetChanged();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView symbolText;
        TextView nameText;
        Switch enabledSwitch;
        ImageView deleteButton;
        ImageView dragHandle;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            symbolText = itemView.findViewById(R.id.etfSymbolText);
            nameText = itemView.findViewById(R.id.etfNameText);
            enabledSwitch = itemView.findViewById(R.id.etfEnabledSwitch);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            dragHandle = itemView.findViewById(R.id.dragHandle);
        }
    }
}