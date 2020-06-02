package com.example.rimae.ui.interview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rimae.R;

public class BookmarksInterview extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_video_markers_times, container, false);
        String interviewId = getActivity().getIntent().getStringExtra("interviewId");
        Log.d("Interview","Fragment " + interviewId);
        return  root;
    }
}
