package com.adi.gfood.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.gfood.R;
import com.adi.gfood.adapters.SearchAdapter;
import com.adi.gfood.models.Dish;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SearchNCategoryShowActivity extends AppCompatActivity {
    EditText searchEt;
    ImageView backArrow;

    String category;

    RecyclerView searchRecyclerView;
    SearchAdapter searchAdapter;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ncategory_show);

        backArrow = findViewById(R.id.back_arrow_search);
        backArrow.setOnClickListener(v -> onBackPressed());

        searchRecyclerView = findViewById(R.id.search_recycler_view);
        searchRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        searchAdapter = new SearchAdapter(this);
        searchRecyclerView.setAdapter(searchAdapter);

        searchEt = findViewById(R.id.search_et_search);
        progressBar = findViewById(R.id.pb_search);

        if (getIntent().hasExtra("category")){
            category = getIntent().getStringExtra("category");
            searchEt.setText(category);
            search(category);
        }else {
            showSoftKeyboard();
        }

        searchEt.setOnEditorActionListener((v, actionId, event) -> {
            String query = searchEt.getText().toString().toLowerCase().trim();
            if (query.isEmpty())
                Toast.makeText(this, "Enter search query", Toast.LENGTH_SHORT).show();
            else
                search(query);
            return true;
        });
    }

    private void search(String query){
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("dishes");

        ArrayList<Dish> dishList = new ArrayList<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()){
                    Dish dish = snap.getValue(Dish.class);
                    boolean shallAdd = matchesQuery(dish, query);
                    Log.d("aditya", "onDataChange: " + shallAdd);
                    if (shallAdd) dishList.add(dish);
                }

                progressBar.setVisibility(View.GONE);
                if (dishList.isEmpty()) Toast.makeText(SearchNCategoryShowActivity.this, "No searches found", Toast.LENGTH_SHORT).show();
                else searchAdapter.submitList(dishList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private boolean matchesQuery(Dish dish, String query) {
        if (dish == null) return false;

        if (dish.getDishName().toLowerCase().contains(query.toLowerCase())) return true;
        if (dish.getDishCategory() != null && dish.getDishCategory().toLowerCase().contains(query.toLowerCase())) return true;
        if (dish.getDishContent().toLowerCase().contains(query.toLowerCase())) return true;
        if (dish.getDishDescription().toLowerCase().contains(query.toLowerCase())) return true;

        return false;
    }

    public void showSoftKeyboard() {
        if (searchEt.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchEt, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}