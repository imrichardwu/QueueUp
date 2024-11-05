package com.example.queueup.views.organizer;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.queueup.R;
import com.example.queueup.models.Event;
import com.example.queueup.viewmodels.EventArrayAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class OrganizerHomeFragment extends Fragment {

    // Constructor for the fragment, loading the layout file
    public OrganizerHomeFragment() {
        super(R.layout.organizer_home_fragment);  // Use a layout specific to the organizer's home screen
    }

    private ArrayList<Event> dataList;
    private ListView eventList;
    private EventArrayAdapter eventAdapter;
    private FirebaseFirestore db;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the data list and add events
        dataList = new ArrayList<>();  // Proper initialization of ArrayList
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Add sample events or logic to retrieve events for the organizer
        }

        db = FirebaseFirestore.getInstance();

        // Set up the ListView and its adapter
        eventList = view.findViewById(R.id.organizer_event_list);  // Ensure ID matches the organizer's ListView in XML
        eventAdapter = new EventArrayAdapter(view.getContext(), dataList);  // Initialize the adapter with the context and data list
        eventList.setAdapter(eventAdapter);  // Set the adapter to the ListView

        fetchEvents();
    }

    private void fetchEvents() {
        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dataList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event event = document.toObject(Event.class);
                            dataList.add(event);
                        }
                        eventAdapter.notifyDataSetChanged();
                    }
                    else {
                        Log.e("OrganizerHome", "Error fetching events", task.getException());
                    }
                });
    }
}
