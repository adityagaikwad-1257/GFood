package com.adi.gfood.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.gfood.R;
import com.adi.gfood.models.Address;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddressActivity extends AppCompatActivity {
    FloatingActionButton addAddressBtn;

    EditText nameEt, addressLine1Et, addressLine2Et, landMarkEt, pinCodeEt;

    Spinner states;
    
    ProgressDialog progressDialog;

    RecyclerView recyclerView;

    DatabaseReference reference;

    LinearLayout noAddressAnim;

    TextView selectText;

    boolean isForSelection = false;

    private static final String TAG = "aditya";

    FirebaseRecyclerAdapter<Address, AddressViewHolder> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_layout);

        addAddressBtn = findViewById(R.id.add_address_btn_address_layout);

        addAddressBtn.setOnClickListener(v -> showDialog());

        noAddressAnim = findViewById(R.id.no_address_anim);

        selectText = findViewById(R.id.select_address_text);

        if (getIntent().hasExtra("selection")){
            selectText.setVisibility(View.VISIBLE);
            isForSelection = true;
        }
        
        setUpProgressDialog();

        recyclerView = findViewById(R.id.recycler_view_address_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reference = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("addresses");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() == 0){
                    noAddressAnim.setVisibility(View.VISIBLE);
                }else{
                    noAddressAnim.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseRecyclerOptions<Address> options = new FirebaseRecyclerOptions.Builder<Address>()
                .setQuery(reference, snapshot -> snapshot.getValue(Address.class))
                .build();

        adapter = new FirebaseRecyclerAdapter<Address, AddressViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AddressViewHolder holder, int position, @NonNull Address address) {
                holder.nameTv.setText(address.getName());
                holder.addressTv.setText(address.getAddress());

                holder.itemView.setOnClickListener(v -> {
                    if (isForSelection){
                        AddressActivity.this.setResult(RESULT_OK, new Intent().putExtra("address", address));
                        AddressActivity.this.finish();
                    }
                });

            }

            @NonNull
            @Override
            public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new AddressViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.address_view_holder, parent, false));
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
        swipeActionHelper();

    }

    private void swipeActionHelper() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                String uid = adapter.getItem(viewHolder.getAbsoluteAdapterPosition()).getAddressUid();
                reference.child(uid).removeValue();
            }
        }).attachToRecyclerView(recyclerView);
    }

    static class AddressViewHolder extends RecyclerView.ViewHolder{
        TextView nameTv, addressTv;
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.name_tv_address_view_holder);
            addressTv = itemView.findViewById(R.id.address_tv_address_view_holder);
        }
    }

    private void setUpProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please hold on..");
        progressDialog.setMessage("while we update your address details");
        progressDialog.setCancelable(false);
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_address_layout);
        dialog.show();

        Button addBtn = dialog.findViewById(R.id.add_address_btn_address_dialog);

        addressLine1Et = dialog.findViewById(R.id.address_line_1_address_dialog);
        addressLine2Et = dialog.findViewById(R.id.address_line_2_address_dialog);
        nameEt = dialog.findViewById(R.id.name_address_line_dialog);
        landMarkEt = dialog.findViewById(R.id.land_mark_address_dialog);
        pinCodeEt = dialog.findViewById(R.id.pincode_address_dialog);
        states = dialog.findViewById(R.id.state_spinner_address_dialog);

        addBtn.setOnClickListener(v -> {
            if (verifyAddress()){
                dialog.dismiss();
                progressDialog.show();
                saveAddress();
            }
        });

    }

    private void saveAddress() {
        String key = reference.push().getKey();

        reference.child(key)
                .setValue(getAddress(key)).addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()){
                        Toast.makeText(this, "Address added", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private Address getAddress(String key){
        Address address = new Address(nameEt.getText().toString(),
                addressLine1Et.getText().toString(),
                addressLine2Et.getText().toString(),
                landMarkEt.getText().toString(),
                pinCodeEt.getText().toString(),
                states.getSelectedItem().toString());
        
        address.setAddressUid(key);
        
        return address;
    }

    private boolean verifyAddress(){
        boolean allOk = true;

        if (nameEt.getText().toString().trim().isEmpty()){
            allOk = false;
            nameEt.setError("required");
        }

        if (addressLine1Et.getText().toString().trim().isEmpty()){
            allOk = false;
            addressLine1Et.setError("required");
        }

        if (addressLine2Et.getText().toString().trim().isEmpty()){
            allOk = false;
            addressLine2Et.setError("required");
        }

        if (pinCodeEt.getText().toString().trim().isEmpty()){
            allOk = false;
            pinCodeEt.setError("required");
        }else if (pinCodeEt.getText().toString().length() != 6){
            allOk = false;
            pinCodeEt.setError("invalid pincode");
        }
        
        return allOk;
    }
}
