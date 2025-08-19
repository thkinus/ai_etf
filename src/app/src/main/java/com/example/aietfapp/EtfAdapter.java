package com.example.aietfapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;
import java.util.List;

public class EtfAdapter extends RecyclerView.Adapter<EtfAdapter.EtfViewHolder> {

    private List<EtfData> etfDataList;

    public EtfAdapter(List<EtfData> etfDataList) {
        this.etfDataList = etfDataList;
    }

    @NonNull
    @Override
    public EtfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_etf_chart, parent, false);
        return new EtfViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EtfViewHolder holder, int position) {
        EtfData etf = etfDataList.get(position);
        
        holder.symbolTextView.setText(etf.getSymbol());
        holder.nameTextView.setText(etf.getName());
        holder.priceTextView.setText(String.format("$%.2f", etf.getCurrentPrice()));
        
        double changePercent = etf.getChangePercent();
        holder.changeTextView.setText(String.format("%+.2f%%", changePercent));
        Context context = holder.itemView.getContext();
        holder.changeTextView.setTextColor(changePercent >= 0 ? 
            context.getResources().getColor(R.color.ios_green) : 
            context.getResources().getColor(R.color.ios_red));
        
        // 기본값으로 1주일 차트 표시
        holder.currentPeriod = "1W";
        setupChart(holder.chart, etf.getPriceHistoryByPeriod("1W"), changePercent >= 0, "1W");
        setupPeriodButtons(holder, etf, changePercent >= 0);
        setupExpertOpinions(holder, etf.getExpertOpinions());
    }

    private void setupPeriodButtons(EtfViewHolder holder, EtfData etf, boolean isPositive) {
        Button[] buttons = {holder.btn1d, holder.btn1w, holder.btn1m, holder.btn6m, holder.btn1y};
        String[] periods = {"1D", "1W", "1M", "6M", "1Y"};
        
        for (int i = 0; i < buttons.length; i++) {
            final String period = periods[i];
            buttons[i].setOnClickListener(v -> {
                holder.currentPeriod = period;
                updateButtonStyles(holder, period);
                setupChart(holder.chart, etf.getPriceHistoryByPeriod(period), isPositive, period);
            });
        }
        
        // 초기 버튼 스타일 설정 (1W 선택됨)
        updateButtonStyles(holder, "1W");
    }
    
    private void updateButtonStyles(EtfViewHolder holder, String selectedPeriod) {
        Button[] buttons = {holder.btn1d, holder.btn1w, holder.btn1m, holder.btn6m, holder.btn1y};
        String[] periods = {"1D", "1W", "1M", "6M", "1Y"};
        
        Context context = holder.itemView.getContext();
        
        for (int i = 0; i < buttons.length; i++) {
            if (periods[i].equals(selectedPeriod)) {
                buttons[i].setBackgroundResource(R.drawable.ios_button_selected);
                buttons[i].setTextColor(Color.WHITE);
            } else {
                buttons[i].setBackgroundResource(R.drawable.ios_button_unselected);
                buttons[i].setTextColor(context.getResources().getColor(R.color.ios_text_secondary));
            }
        }
    }

    private void setupChart(LineChart chart, List<Double> priceHistory, boolean isPositive, String period) {
        if (priceHistory == null || priceHistory.isEmpty()) return;
        
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < priceHistory.size(); i++) {
            entries.add(new Entry(i, priceHistory.get(i).floatValue()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Price");
        Context context = chart.getContext();
        int lineColor = isPositive ? 
            context.getResources().getColor(R.color.ios_green) : 
            context.getResources().getColor(R.color.ios_red);
        int blueColor = context.getResources().getColor(R.color.ios_blue);
        
        dataSet.setColor(blueColor);
        dataSet.setLineWidth(2.5f);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(blueColor);
        dataSet.setFillAlpha(20);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        
        // Y축 (왼쪽) - 가격 표시
        int grayColor = context.getResources().getColor(R.color.ios_gray);
        int separatorColor = context.getResources().getColor(R.color.ios_separator);
        
        chart.getAxisLeft().setEnabled(true);
        chart.getAxisLeft().setValueFormatter(new PriceAxisValueFormatter());
        chart.getAxisLeft().setTextColor(grayColor);
        chart.getAxisLeft().setTextSize(11f);
        chart.getAxisLeft().setLabelCount(5, false);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getAxisLeft().setGridColor(separatorColor);
        chart.getAxisLeft().setGridLineWidth(0.8f);
        
        // Y축 (오른쪽) 비활성화
        chart.getAxisRight().setEnabled(false);
        
        // X축 - 날짜 표시
        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new DateAxisValueFormatter(period));
        xAxis.setTextColor(grayColor);
        xAxis.setTextSize(11f);
        xAxis.setLabelCount(4, false);
        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(separatorColor);
        xAxis.setGridLineWidth(0.8f);
        
        // 차트 설정
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
        chart.setDoubleTapToZoomEnabled(false);
        
        // 여백 설정
        chart.setExtraOffsets(10, 10, 10, 20);

        chart.invalidate();
    }
    
    private void setupExpertOpinions(EtfViewHolder holder, List<ExpertOpinion> opinions) {
        holder.expertOpinionsContainer.removeAllViews();
        
        for (ExpertOpinion opinion : opinions) {
            View opinionView = LayoutInflater.from(holder.itemView.getContext())
                    .inflate(R.layout.item_expert_opinion, holder.expertOpinionsContainer, false);
            
            TextView expertName = opinionView.findViewById(R.id.tv_expert_name);
            TextView company = opinionView.findViewById(R.id.tv_company);
            TextView rating = opinionView.findViewById(R.id.tv_rating);
            TextView targetPrice = opinionView.findViewById(R.id.tv_target_price);
            TextView opinionText = opinionView.findViewById(R.id.tv_opinion);
            
            expertName.setText(opinion.getExpertName());
            company.setText(opinion.getCompany());
            rating.setText(opinion.getRating());
            targetPrice.setText("Target: " + opinion.getTargetPrice());
            opinionText.setText(opinion.getOpinion());
            
            // 레이팅에 따른 iOS 색상 설정
            Context context = holder.itemView.getContext();
            switch (opinion.getRating().toLowerCase()) {
                case "buy":
                case "overweight":
                case "outperform":
                    rating.setTextColor(context.getResources().getColor(R.color.ios_green));
                    break;
                case "hold":
                    rating.setTextColor(context.getResources().getColor(R.color.ios_orange));
                    break;
                case "sell":
                case "underweight":
                    rating.setTextColor(context.getResources().getColor(R.color.ios_red));
                    break;
                default:
                    rating.setTextColor(context.getResources().getColor(R.color.ios_gray));
                    break;
            }
            
            // 전문가 의견 클릭 시 웹페이지 열기
            opinionView.setOnClickListener(v -> {
                Context clickContext = v.getContext();
                String url = opinion.getUrl();
                if (url != null && !url.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    clickContext.startActivity(intent);
                }
            });
            
            // 클릭 가능함을 나타내는 시각적 피드백 추가
            opinionView.setClickable(true);
            opinionView.setFocusable(true);
            opinionView.setBackgroundResource(android.R.drawable.list_selector_background);
            
            holder.expertOpinionsContainer.addView(opinionView);
        }
    }

    @Override
    public int getItemCount() {
        return etfDataList.size();
    }

    static class EtfViewHolder extends RecyclerView.ViewHolder {
        TextView symbolTextView;
        TextView nameTextView;
        TextView priceTextView;
        TextView changeTextView;
        LineChart chart;
        Button btn1d, btn1w, btn1m, btn6m, btn1y;
        LinearLayout expertOpinionsContainer;
        String currentPeriod = "1W";

        EtfViewHolder(View itemView) {
            super(itemView);
            symbolTextView = itemView.findViewById(R.id.tv_symbol);
            nameTextView = itemView.findViewById(R.id.tv_name);
            priceTextView = itemView.findViewById(R.id.tv_price);
            changeTextView = itemView.findViewById(R.id.tv_change);
            chart = itemView.findViewById(R.id.chart);
            btn1d = itemView.findViewById(R.id.btn_1d);
            btn1w = itemView.findViewById(R.id.btn_1w);
            btn1m = itemView.findViewById(R.id.btn_1m);
            btn6m = itemView.findViewById(R.id.btn_6m);
            btn1y = itemView.findViewById(R.id.btn_1y);
            expertOpinionsContainer = itemView.findViewById(R.id.expert_opinions_container);
        }
    }
}