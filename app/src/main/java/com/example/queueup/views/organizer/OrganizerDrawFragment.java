package com.example.queueup.views.organizer;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.queueup.R;
import com.example.queueup.models.User;
import com.example.queueup.viewmodels.UsersArrayAdapter;

import java.util.ArrayList;

public class OrganizerDrawFragment extends Fragment {
    public OrganizerDrawFragment() {
        super(R.layout.organizer_draw_fragment);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        TextView eventDate = view.findViewById(R.id.event_date);
        TextView locationText = view.findViewById(R.id.event_date);
        TextView dateText = view.findViewById(R.id.event_date);
        TextView timeText = view.findViewById(R.id.event_time);


    }

}
