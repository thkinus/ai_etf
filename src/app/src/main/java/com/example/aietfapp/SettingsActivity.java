package com.example.aietfapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements EtfSettingsAdapter.OnEtfSettingsListener {

    private RecyclerView settingsRecyclerView;
    private EtfSettingsAdapter settingsAdapter;
    private List<EtfData> etfList;
    private ImageView backButton, addEtfButton, searchToggleButton;
    private Button resetDefaultButton, saveButton;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    
    // Search related UI components
    private LinearLayout searchContainer;
    private EditText searchInput;
    private RecyclerView searchResultsRecyclerView;
    private EtfSymbolSuggestionAdapter searchResultsAdapter;
    
    // 탭 관련 UI 컴포넌트
    private LinearLayout usSettingsTab, krSettingsTab;
    private TextView usSettingsTabText, krSettingsTabText, currentTabTitle;
    private ImageView usSettingsTabIcon, krSettingsTabIcon;
    private boolean isUsSettingsTabSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        initData();
        setupRecyclerView();
        setupClickListeners();
        
        // 기본적으로 US 탭 선택
        selectUsSettingsTab();
    }

    private void initViews() {
        settingsRecyclerView = findViewById(R.id.settingsRecyclerView);
        backButton = findViewById(R.id.backButton);
        addEtfButton = findViewById(R.id.addEtfButton);
        searchToggleButton = findViewById(R.id.searchToggleButton);
        resetDefaultButton = findViewById(R.id.resetDefaultButton);
        saveButton = findViewById(R.id.saveButton);
        
        // Initialize search related views
        searchContainer = findViewById(R.id.searchContainer);
        searchInput = findViewById(R.id.searchInput);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
        
        // 탭 관련 뷰 초기화
        usSettingsTab = findViewById(R.id.usSettingsTab);
        krSettingsTab = findViewById(R.id.krSettingsTab);
        usSettingsTabText = findViewById(R.id.usSettingsTabText);
        krSettingsTabText = findViewById(R.id.krSettingsTabText);
        usSettingsTabIcon = findViewById(R.id.usSettingsTabIcon);
        krSettingsTabIcon = findViewById(R.id.krSettingsTabIcon);
        currentTabTitle = findViewById(R.id.currentTabTitle);
        
        sharedPreferences = getSharedPreferences("etf_settings", Context.MODE_PRIVATE);
        gson = new Gson();
        
        setupSearchFunction();
    }
    
    private void setupSearchFunction() {
        // Setup search results adapter
        searchResultsAdapter = new EtfSymbolSuggestionAdapter(suggestion -> {
            // Add ETF directly when selected from search results
            String symbol = suggestion.getSymbol();
            String name = suggestion.getName();
            
            // 중복 체크
            for (EtfData etf : etfList) {
                if (etf.getSymbol().equals(symbol)) {
                    Toast.makeText(this, "ETF already added: " + symbol, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            
            // Add ETF
            EtfData newEtf = EtfDataProvider.createSampleEtf(symbol, name);
            settingsAdapter.addItem(newEtf);
            Toast.makeText(this, symbol + " ETF has been added.", Toast.LENGTH_SHORT).show();
            
            // Reset search field
            searchInput.setText("");
            searchResultsRecyclerView.setVisibility(View.GONE);
        });
        
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResultsRecyclerView.setAdapter(searchResultsAdapter);
        
        // Search input listener
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                
                if (query.length() >= 1) {
                    List<EtfSymbolDatabase.EtfSymbolSuggestion> searchResults = 
                            EtfSymbolDatabase.searchByNameOrSymbol(query);
                    
                    if (!searchResults.isEmpty()) {
                        searchResultsAdapter.updateSuggestions(searchResults);
                        searchResultsRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        searchResultsRecyclerView.setVisibility(View.GONE);
                    }
                } else {
                    searchResultsAdapter.clearSuggestions();
                    searchResultsRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void initData() {
        loadCurrentTabData();
    }
    
    private void loadCurrentTabData() {
        if (isUsSettingsTabSelected) {
            loadUsEtfSettings();
        } else {
            loadKrEtfSettings();
        }
    }
    
    private void loadUsEtfSettings() {
        // Load US ETF settings data
        String json = sharedPreferences.getString("us_etf_list", null);
        if (json != null) {
            Type listType = new TypeToken<List<EtfData>>(){}.getType();
            etfList = gson.fromJson(json, listType);
        }
        
        if (etfList == null || etfList.isEmpty()) {
            etfList = new ArrayList<>(EtfDataProvider.getEtfDataList());
        }
    }
    
    private void loadKrEtfSettings() {
        // Load KR ETF settings data (currently read-only, showing default data only)
        etfList = new ArrayList<>(KoreanEtfDataProvider.getKoreanAiEtfData());
        
        // KR ETF is set as read-only by default (can be extended in the future)
        // 현재는 조회만 가능
    }

    private void setupRecyclerView() {
        settingsAdapter = new EtfSettingsAdapter(etfList, this);
        settingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        settingsRecyclerView.setAdapter(settingsAdapter);

        // Add drag and drop functionality
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, 
                                RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                settingsAdapter.moveItem(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // 스와이프 기능은 사용하지 않음
            }
        });
        
        itemTouchHelper.attachToRecyclerView(settingsRecyclerView);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
        
        addEtfButton.setOnClickListener(v -> showAddEtfDialog());
        
        searchToggleButton.setOnClickListener(v -> toggleSearchContainer());
        
        // 탭 클릭 리스너
        usSettingsTab.setOnClickListener(v -> {
            if (!isUsSettingsTabSelected) {
                selectUsSettingsTab();
            }
        });
        
        krSettingsTab.setOnClickListener(v -> {
            if (isUsSettingsTabSelected) {
                selectKrSettingsTab();
            }
        });
        
        resetDefaultButton.setOnClickListener(v -> showResetConfirmDialog());
        
        saveButton.setOnClickListener(v -> saveSettings());
    }
    
    private void toggleSearchContainer() {
        if (searchContainer.getVisibility() == View.GONE) {
            searchContainer.setVisibility(View.VISIBLE);
            searchToggleButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
            searchInput.requestFocus();
        } else {
            searchContainer.setVisibility(View.GONE);
            searchToggleButton.setImageResource(android.R.drawable.ic_menu_search);
            searchInput.setText("");
            searchResultsRecyclerView.setVisibility(View.GONE);
        }
    }

    private void showAddEtfDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_etf, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        
        EditText symbolInput = dialogView.findViewById(R.id.etfSymbolInput);
        EditText nameInput = dialogView.findViewById(R.id.etfNameInput);
        RecyclerView suggestionsRecyclerView = dialogView.findViewById(R.id.suggestionsRecyclerView);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        Button addButton = dialogView.findViewById(R.id.addButton);

        // Setup autocomplete adapter
        EtfSymbolSuggestionAdapter suggestionAdapter = new EtfSymbolSuggestionAdapter(suggestion -> {
            // 제안된 심볼 선택 시
            symbolInput.setText(suggestion.getSymbol());
            nameInput.setText(suggestion.getName());
            suggestionsRecyclerView.setVisibility(View.GONE);
        });
        
        suggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        suggestionsRecyclerView.setAdapter(suggestionAdapter);

        // 심볼 입력 시 자동완성 기능
        symbolInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                
                if (query.length() >= 1) {
                    List<EtfSymbolDatabase.EtfSymbolSuggestion> suggestions = 
                            EtfSymbolDatabase.searchSymbols(query);
                    
                    if (!suggestions.isEmpty()) {
                        suggestionAdapter.updateSuggestions(suggestions);
                        suggestionsRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        suggestionsRecyclerView.setVisibility(View.GONE);
                    }
                } else {
                    suggestionAdapter.clearSuggestions();
                    suggestionsRecyclerView.setVisibility(View.GONE);
                }
                
                // 기존 ETF 이름 자동 채우기
                if (query.length() >= 2 && EtfSymbolDatabase.symbolExists(query)) {
                    String existingName = EtfSymbolDatabase.getSymbolName(query);
                    if (existingName != null && nameInput.getText().toString().isEmpty()) {
                        nameInput.setText(existingName);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        
        addButton.setOnClickListener(v -> {
            String symbol = symbolInput.getText().toString().trim().toUpperCase();
            String name = nameInput.getText().toString().trim();
            
            if (symbol.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // 중복 체크
            for (EtfData etf : etfList) {
                if (etf.getSymbol().equals(symbol)) {
                    Toast.makeText(this, "ETF already exists.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            
            // Add new ETF (with default data)
            EtfData newEtf = EtfDataProvider.createSampleEtf(symbol, name);
            settingsAdapter.addItem(newEtf);
            
            Toast.makeText(this, symbol + " ETF has been added.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        
        dialog.show();
    }

    private void showResetConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Reset to Default")
                .setMessage("Reset all settings to default values?")
                .setPositiveButton("Reset", (dialog, which) -> {
                    resetToDefault();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void resetToDefault() {
        if (isUsSettingsTabSelected) {
            etfList.clear();
            etfList.addAll(EtfDataProvider.getEtfDataList());
            settingsAdapter.notifyDataSetChanged();
            Toast.makeText(this, "US ETF reset to default values.", Toast.LENGTH_SHORT).show();
        } else {
            // KR ETF cannot be reset (read-only)
            Toast.makeText(this, "KR ETF cannot be edited currently.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveSettings() {
        // Save settings with appropriate key based on current tab
        String json = gson.toJson(etfList);
        String saveKey = isUsSettingsTabSelected ? "us_etf_list" : "kr_etf_list";
        
        if (isUsSettingsTabSelected) {
            // Save US ETF settings
            sharedPreferences.edit().putString("us_etf_list", json).apply();
            // 기존 호환성을 위해 기본 키도 업데이트
            sharedPreferences.edit().putString("etf_list", json).apply();
        } else {
            // KR ETF is currently read-only, so don't save
            Toast.makeText(this, "KR ETF cannot be edited currently.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Toast.makeText(this, "Settings saved.", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }


    @Override
    public void onEtfToggled(int position, boolean enabled) {
        if (position >= 0 && position < etfList.size()) {
            etfList.get(position).setEnabled(enabled);
        }
    }

    @Override
    public void onEtfDeleted(int position) {
        if (position >= 0 && position < etfList.size()) {
            String symbol = etfList.get(position).getSymbol();
            
            new AlertDialog.Builder(this)
                    .setTitle("Delete ETF")
                    .setMessage("Delete " + symbol + " ETF?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        settingsAdapter.removeItem(position);
                        Toast.makeText(this, symbol + " ETF has been deleted.", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    @Override
    public void onEtfMoved(int fromPosition, int toPosition) {
        // 순서 변경은 자동으로 처리됨
    }
    
    private void selectUsSettingsTab() {
        isUsSettingsTabSelected = true;
        
        // US 탭 활성화 스타일
        usSettingsTabText.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        usSettingsTabIcon.setColorFilter(getResources().getColor(android.R.color.holo_blue_dark));
        usSettingsTabText.setTypeface(null, android.graphics.Typeface.BOLD);
        
        // KR 탭 비활성화 스타일
        krSettingsTabText.setTextColor(getResources().getColor(android.R.color.darker_gray));
        krSettingsTabIcon.setColorFilter(getResources().getColor(android.R.color.darker_gray));
        krSettingsTabText.setTypeface(null, android.graphics.Typeface.NORMAL);
        
        // 탭 제목 변경
        currentTabTitle.setText("US ETF Stocks");
        
        // US ETF 데이터 로드
        loadCurrentTabData();
        settingsAdapter.updateList(etfList);
        
        // Enable editing for US ETF
        addEtfButton.setVisibility(View.VISIBLE);
        searchToggleButton.setVisibility(View.VISIBLE);
        resetDefaultButton.setEnabled(true);
    }
    
    private void selectKrSettingsTab() {
        isUsSettingsTabSelected = false;
        
        // KR 탭 활성화 스타일
        krSettingsTabText.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        krSettingsTabIcon.setColorFilter(getResources().getColor(android.R.color.holo_blue_dark));
        krSettingsTabText.setTypeface(null, android.graphics.Typeface.BOLD);
        
        // US 탭 비활성화 스타일
        usSettingsTabText.setTextColor(getResources().getColor(android.R.color.darker_gray));
        usSettingsTabIcon.setColorFilter(getResources().getColor(android.R.color.darker_gray));
        usSettingsTabText.setTypeface(null, android.graphics.Typeface.NORMAL);
        
        // 탭 제목 변경
        currentTabTitle.setText("KR ETF Stocks");
        
        // KR ETF 데이터 로드
        loadCurrentTabData();
        settingsAdapter.updateList(etfList);
        
        // KR ETF는 현재 읽기 전용
        addEtfButton.setVisibility(View.GONE);
        searchToggleButton.setVisibility(View.GONE);
        searchContainer.setVisibility(View.GONE);
        resetDefaultButton.setEnabled(false);
    }
}