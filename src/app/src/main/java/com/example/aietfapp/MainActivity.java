package com.example.aietfapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private static final int SETTINGS_REQUEST_CODE = 100;
    
    private RecyclerView recyclerView;
    private EtfAdapter adapter;
    private ImageView settingsButton;
    private TextView usTimeText, koreaTimeText, marketCountdownText;
    private List<EtfData> etfDataList;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    
    private Handler timeHandler;
    private Runnable timeUpdateRunnable;
    
    // 탭 관련 뷰들
    private LinearLayout usTab, krTab;
    private TextView usTabText, krTabText;
    private ImageView usTabIcon, krTabIcon;
    private boolean isUsTabSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        loadEtfData();
        setupClickListeners();
    }
    
    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        settingsButton = findViewById(R.id.settingsButton);
        usTimeText = findViewById(R.id.usTimeText);
        koreaTimeText = findViewById(R.id.koreaTimeText);
        marketCountdownText = findViewById(R.id.marketCountdownText);
        
        // 탭 뷰 초기화
        usTab = findViewById(R.id.usTab);
        krTab = findViewById(R.id.krTab);
        usTabText = findViewById(R.id.usTabText);
        krTabText = findViewById(R.id.krTabText);
        usTabIcon = findViewById(R.id.usTabIcon);
        krTabIcon = findViewById(R.id.krTabIcon);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        sharedPreferences = getSharedPreferences("etf_settings", Context.MODE_PRIVATE);
        gson = new Gson();
        
        setupTimeUpdates();
    }
    
    private void setupTimeUpdates() {
        timeHandler = new Handler(Looper.getMainLooper());
        
        timeUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                updateTimeDisplays();
                // 1초마다 업데이트
                timeHandler.postDelayed(this, 1000);
            }
        };
        
        // Start immediate time update
        timeHandler.post(timeUpdateRunnable);
    }
    
    private void updateTimeDisplays() {
        try {
            // Convert current time to each timezone
            long currentTimeMillis = System.currentTimeMillis();
            
            // US Eastern Time (EST/EDT)
            Calendar usCalendar = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
            usCalendar.setTimeInMillis(currentTimeMillis);
            SimpleDateFormat usTimeFormat = new SimpleDateFormat("MMM dd, hh:mm:ss a", Locale.ENGLISH);
            usTimeFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
            
            // Korea Time (KST)
            Calendar koreaCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
            koreaCalendar.setTimeInMillis(currentTimeMillis);
            SimpleDateFormat koreaTimeFormat = new SimpleDateFormat("MMM dd, HH:mm:ss", Locale.ENGLISH);
            koreaTimeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            
            // Update time text
            String usTimeString = "New York: " + usTimeFormat.format(usCalendar.getTime());
            String koreaTimeString = "Seoul: " + koreaTimeFormat.format(koreaCalendar.getTime());
            
            // UI 스레드에서 업데이트
            runOnUiThread(() -> {
                if (usTimeText != null) {
                    usTimeText.setText(usTimeString);
                    usTimeText.setVisibility(TextView.VISIBLE);
                }
                if (koreaTimeText != null) {
                    koreaTimeText.setText(koreaTimeString);
                    koreaTimeText.setVisibility(TextView.VISIBLE);
                }
            });
            
            // Update market opening countdown
            updateMarketCountdown(usCalendar);
            
        } catch (Exception e) {
            // 에러 발생 시 기본값 표시
            runOnUiThread(() -> {
                if (usTimeText != null) {
                    usTimeText.setText("New York: --:--:--");
                    usTimeText.setVisibility(TextView.VISIBLE);
                }
                if (koreaTimeText != null) {
                    koreaTimeText.setText("Seoul: --:--:--");
                    koreaTimeText.setVisibility(TextView.VISIBLE);
                }
                if (marketCountdownText != null) {
                    marketCountdownText.setVisibility(TextView.GONE);
                }
            });
        }
    }
    
    private void updateMarketCountdown(Calendar currentUsTime) {
        try {
            // Current US Eastern Time
            int currentHour = currentUsTime.get(Calendar.HOUR_OF_DAY);
            int currentMinute = currentUsTime.get(Calendar.MINUTE);
            int currentSecond = currentUsTime.get(Calendar.SECOND);
            int dayOfWeek = currentUsTime.get(Calendar.DAY_OF_WEEK);
            
            // 주말 체크 (토요일: 7, 일요일: 1)
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                // Calculate time until next Monday
                Calendar nextMonday = (Calendar) currentUsTime.clone();
                int daysUntilMonday = (Calendar.MONDAY - dayOfWeek + 7) % 7;
                if (daysUntilMonday == 0) daysUntilMonday = 1; // 일요일인 경우
                
                nextMonday.add(Calendar.DAY_OF_YEAR, daysUntilMonday);
                nextMonday.set(Calendar.HOUR_OF_DAY, 9);
                nextMonday.set(Calendar.MINUTE, 30);
                nextMonday.set(Calendar.SECOND, 0);
                
                long timeDiffMs = nextMonday.getTimeInMillis() - currentUsTime.getTimeInMillis();
                showCountdown(timeDiffMs, "Weekend - Market opens");
                return;
            }
            
            // Market hours: 9:30 AM - 4:00 PM (EST/EDT)
            boolean isMarketHours = (currentHour > 9 || (currentHour == 9 && currentMinute >= 30)) 
                                   && currentHour < 16;
            
            if (isMarketHours) {
                // Hide countdown if market is open
                runOnUiThread(() -> marketCountdownText.setVisibility(TextView.GONE));
            } else {
                // Calculate time until market opens if market is closed
                Calendar nextOpen = (Calendar) currentUsTime.clone();
                
                if (currentHour >= 16) {
                    // 오후 4시 이후면 다음날 9:30 AM
                    nextOpen.add(Calendar.DAY_OF_YEAR, 1);
                }
                
                nextOpen.set(Calendar.HOUR_OF_DAY, 9);
                nextOpen.set(Calendar.MINUTE, 30);
                nextOpen.set(Calendar.SECOND, 0);
                
                // Adjust to Monday if next opening day is weekend
                int nextDayOfWeek = nextOpen.get(Calendar.DAY_OF_WEEK);
                if (nextDayOfWeek == Calendar.SATURDAY) {
                    nextOpen.add(Calendar.DAY_OF_YEAR, 2); // 월요일로
                } else if (nextDayOfWeek == Calendar.SUNDAY) {
                    nextOpen.add(Calendar.DAY_OF_YEAR, 1); // 월요일로
                }
                
                long timeDiffMs = nextOpen.getTimeInMillis() - currentUsTime.getTimeInMillis();
                showCountdown(timeDiffMs, "Market opens in");
            }
            
        } catch (Exception e) {
            runOnUiThread(() -> marketCountdownText.setVisibility(TextView.GONE));
        }
    }
    
    private void showCountdown(long timeDiffMs, String prefix) {
        if (timeDiffMs <= 0) {
            runOnUiThread(() -> marketCountdownText.setVisibility(TextView.GONE));
            return;
        }
        
        long totalSeconds = timeDiffMs / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        
        String countdownText;
        if (hours > 0) {
            countdownText = String.format(Locale.ENGLISH, "%s: %dh %dm %ds", prefix, hours, minutes, seconds);
        } else if (minutes > 0) {
            countdownText = String.format(Locale.ENGLISH, "%s: %dm %ds", prefix, minutes, seconds);
        } else {
            countdownText = String.format(Locale.ENGLISH, "%s: %ds", prefix, seconds);
        }
        
        runOnUiThread(() -> {
            if (marketCountdownText != null) {
                marketCountdownText.setText(countdownText);
                marketCountdownText.setVisibility(TextView.VISIBLE);
            }
        });
    }
    
    private void loadEtfData() {
        if (isUsTabSelected) {
            loadUsEtfData();
        } else {
            loadKrEtfData();
        }
    }
    
    private void loadUsEtfData() {
        // SharedPreferences에서 설정된 ETF 목록 불러오기
        String json = sharedPreferences.getString("etf_list", null);
        if (json != null) {
            Type listType = new TypeToken<List<EtfData>>(){}.getType();
            etfDataList = gson.fromJson(json, listType);
        }
        
        // 저장된 데이터가 없으면 기본 데이터 사용
        if (etfDataList == null || etfDataList.isEmpty()) {
            etfDataList = EtfDataProvider.getAiEtfData();
        }
        
        // 활성화된 ETF만 필터링
        List<EtfData> enabledEtfs = new ArrayList<>();
        for (EtfData etf : etfDataList) {
            if (etf.isEnabled()) {
                enabledEtfs.add(etf);
            }
        }
        
        adapter = new EtfAdapter(enabledEtfs);
        recyclerView.setAdapter(adapter);
    }
    
    private void loadKrEtfData() {
        // Load Korean ETF data
        List<EtfData> koreanEtfs = KoreanEtfDataProvider.getKoreanAiEtfData();
        adapter = new EtfAdapter(koreanEtfs);
        recyclerView.setAdapter(adapter);
    }
    
    private void setupClickListeners() {
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, SETTINGS_REQUEST_CODE);
        });
        
        // US 탭 클릭 리스너
        usTab.setOnClickListener(v -> {
            if (!isUsTabSelected) {
                selectUsTab();
                loadUsEtfData();
            }
        });
        
        // KR 탭 클릭 리스너
        krTab.setOnClickListener(v -> {
            if (isUsTabSelected) {
                selectKrTab();
                loadKrEtfData();
            }
        });
    }
    
    private void selectUsTab() {
        isUsTabSelected = true;
        
        // US 탭 활성화 스타일
        usTabText.setTextColor(0xFF007AFF);
        usTabText.setTypeface(null, android.graphics.Typeface.BOLD);
        usTabIcon.setColorFilter(0xFF007AFF);
        
        // KR 탭 비활성화 스타일
        krTabText.setTextColor(0xFF8E8E93);
        krTabText.setTypeface(null, android.graphics.Typeface.NORMAL);
        krTabIcon.setColorFilter(0xFF8E8E93);
    }
    
    private void selectKrTab() {
        isUsTabSelected = false;
        
        // KR 탭 활성화 스타일
        krTabText.setTextColor(0xFF007AFF);
        krTabText.setTypeface(null, android.graphics.Typeface.BOLD);
        krTabIcon.setColorFilter(0xFF007AFF);
        
        // US 탭 비활성화 스타일
        usTabText.setTextColor(0xFF8E8E93);
        usTabText.setTypeface(null, android.graphics.Typeface.NORMAL);
        usTabIcon.setColorFilter(0xFF8E8E93);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == SETTINGS_REQUEST_CODE && resultCode == RESULT_OK) {
            // 설정에서 돌아왔을 때 데이터 새로고침
            loadEtfData();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Restart time updates when app becomes active again
        if (timeHandler != null && timeUpdateRunnable != null) {
            timeHandler.post(timeUpdateRunnable);
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        // Stop time updates when app goes to background (save battery)
        if (timeHandler != null && timeUpdateRunnable != null) {
            timeHandler.removeCallbacks(timeUpdateRunnable);
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 리소스 정리
        if (timeHandler != null && timeUpdateRunnable != null) {
            timeHandler.removeCallbacks(timeUpdateRunnable);
        }
    }
}