package com.example.covid_19tracker.ChatSupportActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.covid_19tracker.ModelClass.Users;
import com.example.covid_19tracker.R;
import com.example.covid_19tracker.databinding.ActivityExistingFriendsBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ExistingFriendsActivity extends AppCompatActivity {
    ActivityExistingFriendsBinding binding;
    FirebaseRecyclerOptions<Users> options;
    FirebaseRecyclerAdapter<Users,AllFriendsViewHolder> adapter;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityExistingFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Existing Helpers");
        binding.existingFriendsRecyclerView.setHasFixedSize(true);
        binding.existingFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth=FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference().child("Friends");
        loadAllFriends("");
    }
    private void loadAllFriends(String s) {
        Query query=reference.child(mAuth.getCurrentUser().getUid()).orderByChild("username").startAt(s).endAt(s+"\uf8ff");
        options=new FirebaseRecyclerOptions.Builder<Users>().setQuery(query,Users.class).build();
        adapter=new FirebaseRecyclerAdapter<Users, AllFriendsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull AllFriendsViewHolder holder, int position, @NonNull @NotNull Users model) {
                    Picasso.get().load(model.getProfilepic()).placeholder(R.drawable.blank).into(holder.profilePhoto);
                    holder.user_name.setText(model.getUsername());
                    holder.location.setText(model.getLocation());
                    holder.itemView.setOnClickListener(v -> {
                    Intent intent=new Intent(ExistingFriendsActivity.this,ChattingActivity.class);
                    intent.putExtra("otherUserKey",getRef(position).getKey());
                    startActivity(intent);

                });
//

            }

            @NonNull
            @NotNull
            @Override
            public AllFriendsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_item,parent,false);
                return new AllFriendsViewHolder(view);
            }
        };
        adapter.startListening();
        binding.existingFriendsRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.serach_menu,menu);
        MenuItem menuItem=menu.findItem(R.id.search);
        SearchView searchView= (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadAllFriends(newText);
                return false;
            }
        });
        return true;
    }
}