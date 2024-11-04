package com.example.queueup.views.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.queueup.R;
import com.example.queueup.models.User;
import com.example.queueup.viewmodels.UsersArrayAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AdminUsersFragment extends Fragment {

    public AdminUsersFragment() {
        super(R.layout.admin_users_fragment);
    }

    private ArrayList<User> dataList;
    private ListView userList;
    private UsersArrayAdapter usersAdapter;
    private FirebaseFirestore db;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        dataList = new ArrayList<>();

        userList = getView().findViewById(R.id.admin_user_list);
        usersAdapter = new UsersArrayAdapter(view.getContext(), dataList);
        userList.setAdapter(usersAdapter);

        fetchUsersFromFirestore();


        //User e = new User("Test","Last","Monger","test@test.ca","1234567890","e");
        //dataList = new ArrayList<User>();
        //dataList.add(e);

        fetchUsersFromFirestore();

        //userList = getView().findViewById(R.id.admin_user_list);
        //usersAdapter = new UsersArrayAdapter(view.getContext(), dataList);
        //userList.setAdapter(usersAdapter);
    }

    private void fetchUsersFromFirestore() {
        db.collection("users")
                .whereIn("role", new ArrayList<String>() {{ add("attendee"); add("organizer"); }})
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dataList.clear(); // Clear any old data
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            dataList.add(user); // Add the user to the list
                        }
                        usersAdapter.notifyDataSetChanged(); // Refresh the adapter
                    } else {
                        // Handle the error
                        Log.d("AdminUsersFragment", "Error fetching users: ", task.getException());
                    }
                });
    }

}
