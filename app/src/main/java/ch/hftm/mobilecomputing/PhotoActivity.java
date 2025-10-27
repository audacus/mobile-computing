package ch.hftm.mobilecomputing;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PhotoActivity extends AppCompatActivity {

    private String currentPhotoPath;
    private Uri imageUri;
    private ActivityResultLauncher<Uri> imageLauncher;

    private ImageView imageViewPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        var image = this.createImageFile();

        if (image == null) return;

        this.imageUri = FileProvider.getUriForFile(this, "ch.hftm.mobilecomputing.fileprovider", image);
        this.imageViewPhoto = findViewById(R.id.imageViewPhoto);
        this.imageLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), (result) -> {
            if (!result) return;

            this.loadImage();
        });

        findViewById(R.id.buttonTakePhoto).setOnClickListener(this::onTakePhoto);
    }

    public void onTakePhoto(View v) {
        this.imageLauncher.launch(this.imageUri);
    }

    private void loadImage() {
        this.imageViewPhoto.setImageBitmap(BitmapFactory.decodeFile(this.currentPhotoPath));
    }

    private File createImageFile() {
        var fileName = String.format(
                "JPEG_%s",
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()));
        var directory = new File(getFilesDir(), "pictures");

        try {
            if (!directory.exists() && !directory.mkdirs())
                throw new Exception(String.format("Could not create directory: %s", directory));

            var image = File.createTempFile(fileName, ".jpg", directory);
            this.currentPhotoPath = image.getAbsolutePath();
            return image;
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }
}