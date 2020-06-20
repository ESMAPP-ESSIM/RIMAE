package com.example.rimae;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.rimae.ui.interview.BookmarksInterview;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class VideoActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static SimpleExoPlayer player;
    PlayerView videoPlayer;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // Iniciar Fragment
        BookmarksInterview firstFragment = new BookmarksInterview();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,firstFragment).commit();

        // Iniciar video
        String interviewId = getIntent().getStringExtra("interviewId");
        Log.d("Interview","Activity " + interviewId);

        videoPlayer = findViewById(R.id.videoView);
        player = new SimpleExoPlayer.Builder(this).build();
        Globals.player = player;

        videoPlayer.setPlayer(player);

        db.collection("trainings").document(interviewId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Uri uri = Uri.parse(task.getResult().get("video_url").toString());
                    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(VideoActivity.this,Util.getUserAgent(VideoActivity.this,"Rimae"));
                    MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);

                    player.prepare(videoSource);
                    player.setPlayWhenReady(true);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.stop();
    }
}
