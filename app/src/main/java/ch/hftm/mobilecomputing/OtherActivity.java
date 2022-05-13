package ch.hftm.mobilecomputing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import ch.hftm.mobilecomputing.databinding.ActivityOtherBinding;

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

        findViewById(R.id.buttonGoToMain).setOnClickListener(this::goToMain);

        this.startCountLoop();
    }

    public void goToMain(View view) {
        startActivity(new Intent(this, MainActivity.class));
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