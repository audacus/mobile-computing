package ch.hftm.mobilecomputing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import ch.hftm.mobilecomputing.databinding.ActivityOtherBinding;
import ch.hftm.mobilecomputing.service.MusicService;
import ch.hftm.mobilecomputing.viewmodel.CounterViewModel;

public class OtherActivity extends AppCompatActivity {

    private CounterViewModel counterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        this.counterViewModel = new ViewModelProvider(this).get(CounterViewModel.class);
        ActivityOtherBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_other);
        binding.setLifecycleOwner(this);
        binding.setCounterViewModel(this.counterViewModel);

        findViewById(R.id.buttonStart).setOnClickListener((v) -> this.counterViewModel.onStart());
        findViewById(R.id.buttonStop).setOnClickListener((v) -> this.counterViewModel.onStop());

        findViewById(R.id.buttonGoToMain).setOnClickListener(this::goToMain);

        findViewById(R.id.buttonPlay).setOnClickListener(this::startMusic);
        findViewById(R.id.buttonPause).setOnClickListener(this::stopMusic);

        this.startCountLoop();
    }

    private void goToMain(View view) {
        finish();
    }

    private void startMusic(View view) {
        startService(new Intent(this, MusicService.class).setAction(MusicService.ACTION_START));
    }

    private void stopMusic(View view) {
        stopService(new Intent(this, MusicService.class));
    }

    private void startCountLoop() {
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(1000);
                    if (
                        // check if should count
                        this.counterViewModel.isCounting.getValue() &&
                        // check if is below integer maximum
                        this.counterViewModel.counter.getValue() < Integer.MAX_VALUE
                    ) runOnUiThread(() -> this.counterViewModel.countUp());
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }).start();
    }
}