package com.example.fase1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.fase1.Data.FullPointOfInterest;
import com.example.fase1.Data.Media;
import com.example.fase1.ViewModel.MediaViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaActivity extends AppCompatActivity {

    @BindView((R.id.mediaLayout))
    LinearLayout view;

    private Map<Integer, Media> medias;

    private Map<Integer, MediaPlayer> mediaPlayers = new HashMap<>();
    private Map<Integer, SeekBar> seekBars = new HashMap<>();

    private Map<Integer, VideoView> videoViews = new HashMap<>();

    private MediaViewModel mvm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        ButterKnife.bind(this);
        setTitle("Pin Media");

        int id = getIntent().getIntExtra("pointID", 0);

        mvm = new ViewModelProvider(this).get(MediaViewModel.class);
        mvm.setup(id);
        LiveData<FullPointOfInterest> livePoint = mvm.getPoint();

        livePoint.observe(this, point -> {
            if (point.getPoint() != null && point.getMedias() != null) {
                medias = new HashMap<>();
                for (Media m : point.getMedias())
                    medias.put(m.getId(), m);
                try {
                    setMedia();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }


    public void setMedia() throws IOException {
        view.setDividerDrawable(AppCompatResources.getDrawable(this, R.drawable.divider));
        view.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

        if (medias.size() == 0) {
            TextView text = new TextView(this);
            text.setText(R.string.media_nothingToShow);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 20, 10, 10);
            text.setLayoutParams(params);

            view.addView(text);
            return;
        }

        medias.forEach((k, v) -> {
            switch (v.getMedia_type()) {
                case "I":                // image
                    imageSetup(v);
                    break;

                case "V":                // video
                    videoSetup(v);
                    break;

                case "R":                // audio
                    try {
                        audioSetup(v);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
            }
        });

        Button download = createDownloadBtn();
        view.addView(download);
    }



    public Button createDownloadBtn () {
        Button download = new Button(this);

        download.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        download.setText(R.string.media_downloadAllMedia);
        download.setTextColor(getResources().getColor(R.color.white, null));
        download.setBackgroundColor(getResources().getColor(R.color.logoGreen, null));
        download.setOnClickListener(view -> downloadAllMedia());

        return download;
    }

    public void downloadAllMedia(){

        Toast.makeText(this, "Downloading media...", Toast.LENGTH_SHORT).show();
        medias.forEach((k, v) -> {
            if(v.getMedia_type().equals("I")) mvm.downloadImage(v);
            else mvm.download(v);
        });
        Toast.makeText(this, "Download complete!", Toast.LENGTH_SHORT).show();

    }


    public void imageSetup (Media media) {
        LinearLayout shell = new LinearLayout(this);
        shell.setOrientation(LinearLayout.VERTICAL);
        shell.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        shell.setGravity(Gravity.CENTER);

        ImageView image = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(view.getWidth(), 1000);
        image.setLayoutParams(params);

        String imageUrl = media.getMedia_file();
        String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        File file = new File(this.getFilesDir(), filename);

        if (file.exists())
            image.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        else
            Picasso.get().load(imageUrl).into(image);

        shell.addView(image);
        view.addView(shell);
    }


    public void audioSetup(Media media) throws IOException {
        int id = media.getId();

        LinearLayout shell = new LinearLayout(this);
        shell.setOrientation(LinearLayout.VERTICAL);
        shell.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        shell.setGravity(Gravity.CENTER);

        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


        String url = media.getMedia_file();
        String filename = url.substring(url.lastIndexOf("/") + 1);
        String pathToLocal = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + filename;

        File file = new File(pathToLocal);

        if(file.exists())
            mediaPlayer.setDataSource(pathToLocal);
        else
            mediaPlayer.setDataSource(url);

        mediaPlayer.prepare();
        mediaPlayers.put(id, mediaPlayer);

        seekBarAudio(id, shell);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setGravity(Gravity.CENTER);

        Button playAudio = new Button(this);
        playAudio.setText(R.string.media_playAudio);

        Button stopAudio = new Button(this);
        stopAudio.setText(R.string.media_resetAudio);

        Button pauseAudio = new Button(this);
        pauseAudio.setText(R.string.media_pauseAudio);

        playAudio.setOnClickListener(v -> {
            mediaPlayers.get(id).start();
            Toast.makeText(MediaActivity.this, "Audio Started", Toast.LENGTH_SHORT).show();
        });

         pauseAudio.setOnClickListener(view -> {
             mediaPlayers.get(id).pause();
             Toast.makeText(MediaActivity.this, "Audio Paused", Toast.LENGTH_SHORT).show();
         });

        stopAudio.setOnClickListener(view -> {
            mediaPlayers.get(id).stop();
            Toast.makeText(MediaActivity.this, "Audio Reset", Toast.LENGTH_SHORT).show();
            try {
                mediaPlayers.get(id).prepare();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        layout.addView(playAudio);
        layout.addView(pauseAudio);
        layout.addView(stopAudio);

        shell.addView(layout);
        view.addView(shell);
    }


    public void seekBarAudio (int id, LinearLayout view) {

        if (seekBars.containsKey(id)) return;
        MediaPlayer mp = mediaPlayers.get(id);

        SeekBar seekBar = new SeekBar(this);
        if (mp != null) seekBar.setMax(mp.getDuration() / 1000);
        seekBars.put(id, seekBar);

        view.addView(seekBar);

        Handler mhandler = new Handler();
        new Thread() {
            public void run() {
                if (mediaPlayers.containsKey(id) && mediaPlayers.get(id) != null) {
                    int mCurrentPosition = mediaPlayers.get(id).getCurrentPosition() / 1000;
                    seekBars.get(id).setProgress(mCurrentPosition);
                }
                mhandler.postDelayed(this, 1000);
            }
        }.start();



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayers.containsKey(id) && fromUser) {
                    mediaPlayers.get(id).seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }




    public void videoSetup(Media v) {

        String url = v.getMedia_file();
        String filename = url.substring(url.lastIndexOf("/") + 1);
        String pathToLocal = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + filename;
        File file = new File(pathToLocal);

        int id = v.getId();

        LinearLayout shell = new LinearLayout(this);
        shell.setOrientation(LinearLayout.VERTICAL);
        shell.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        shell.setGravity(Gravity.CENTER);

        VideoView video = new VideoView(this);

        if(file.exists())
            video.setVideoPath(pathToLocal);
        else
            video.setVideoURI(Uri.parse(url));

        videoViews.put(v.getId(), video);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setGravity(Gravity.CENTER);

        Button playVideo = new Button(this);
        playVideo.setText(R.string.media_playVideo);

        Button pauseVideo = new Button(this);
        pauseVideo.setText(R.string.media_pauseVideo);

        playVideo.setOnClickListener(v1 -> {
            videoViews.get(id).start();
            Toast.makeText(MediaActivity.this, "Video Started", Toast.LENGTH_SHORT).show();
        });

        pauseVideo.setOnClickListener(view -> {
            videoViews.get(id).pause();
            Toast.makeText(MediaActivity.this, "Video Paused", Toast.LENGTH_SHORT).show();
        });

        layout.addView(playVideo);
        layout.addView(pauseVideo);

        shell.addView(video);
        shell.addView(layout);
        view.addView(shell);
    }


    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayers.forEach((k, v) -> v.release());
        mediaPlayers = new HashMap<>();
        videoViews.forEach((k, v) -> v.pause());
        videoViews = new HashMap<>();
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        mediaPlayers.forEach((k, v) -> v.release());
        mediaPlayers = new HashMap<>();
        videoViews.forEach((k, v) -> v.pause());
        videoViews = new HashMap<>();
    }
}