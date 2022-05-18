package ch.hftm.mobilecomputing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.util.List;

import ch.hftm.mobilecomputing.databinding.ActivityGeneratorBinding;
import ch.hftm.mobilecomputing.viewmodel.GeneratorViewModel;

public class GeneratorActivity extends AppCompatActivity {

    private GeneratorViewModel generatorViewModel;
    private ImageView imageViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);

        this.generatorViewModel = new ViewModelProvider(this).get(GeneratorViewModel.class);
        ActivityGeneratorBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_generator);
        binding.setLifecycleOwner(this);
        binding.setGeneratorViewModel(this.generatorViewModel);

        this.imageViewResult = findViewById(R.id.imageViewResult);

        findViewById(R.id.buttonStartGenerator).setOnClickListener((v) -> this.generatorViewModel.onGenerate());
        findViewById(R.id.buttonStopGenerator).setOnClickListener((v) -> this.generatorViewModel.onStop());

        this.startGenerator();
    }

    private void startGenerator() {
        var dataList = List.of(
                "https://www.bing.de",
                "https://www.google.ch",
                "https://www.youtube.com",
                "https://www.previon.ch",
                "https://www.hftm.ch"
        );
        var charset = "UTF-8";
        var width = 100;
        var height = 100;
        new Thread(() -> {
            try {
                var dataIterator = dataList.iterator();
                var data = dataIterator.next();
                while (true) {
                    if (!this.generatorViewModel.isGenerating.getValue()) continue;

                    BitMatrix matrix = new MultiFormatWriter().encode(
                            new String(data.getBytes(charset), charset),
                            BarcodeFormat.QR_CODE, width, height);

                    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bitmap.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }

                    runOnUiThread(() -> {
//                        this.generatorViewModel.result.setValue(bitmap);
                        this.imageViewResult.setImageBitmap(bitmap);
                    });

                    if (dataIterator.hasNext()) {
                        data = dataIterator.next();
                    } else {
                        dataIterator = dataList.iterator();
                        data = dataIterator.next();
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }).start();
    }
}