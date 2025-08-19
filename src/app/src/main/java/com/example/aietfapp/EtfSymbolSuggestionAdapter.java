package com.example.aietfapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class EtfSymbolSuggestionAdapter extends RecyclerView.Adapter<EtfSymbolSuggestionAdapter.ViewHolder> {
    
    public interface OnSuggestionClickListener {
        void onSuggestionClicked(EtfSymbolDatabase.EtfSymbolSuggestion suggestion);
    }
    
    private List<EtfSymbolDatabase.EtfSymbolSuggestion> suggestions = new ArrayList<>();
    private OnSuggestionClickListener listener;
    
    public EtfSymbolSuggestionAdapter(OnSuggestionClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_symbol_suggestion, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EtfSymbolDatabase.EtfSymbolSuggestion suggestion = suggestions.get(position);
        
        holder.symbolText.setText(suggestion.getSymbol());
        holder.nameText.setText(suggestion.getName());
        
        // 심볼 매칭과 이름 매칭을 다른 아이콘으로 구분
        if (suggestion.isMatchedBySymbol()) {
            holder.suggestionIcon.setImageResource(android.R.drawable.ic_menu_search);
        } else {
            holder.suggestionIcon.setImageResource(android.R.drawable.ic_search_category_default);
        }
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSuggestionClicked(suggestion);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return suggestions.size();
    }
    
    public void updateSuggestions(List<EtfSymbolDatabase.EtfSymbolSuggestion> newSuggestions) {
        this.suggestions.clear();
        if (newSuggestions != null) {
            this.suggestions.addAll(newSuggestions);
        }
        notifyDataSetChanged();
    }
    
    public void clearSuggestions() {
        this.suggestions.clear();
        notifyDataSetChanged();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView symbolText;
        TextView nameText;
        ImageView suggestionIcon;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            symbolText = itemView.findViewById(R.id.symbolText);
            nameText = itemView.findViewById(R.id.nameText);
            suggestionIcon = itemView.findViewById(R.id.suggestionIcon);
        }
    }
}