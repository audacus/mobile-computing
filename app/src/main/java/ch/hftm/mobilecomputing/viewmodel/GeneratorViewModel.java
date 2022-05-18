package ch.hftm.mobilecomputing.viewmodel;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GeneratorViewModel extends ViewModel {

    public MutableLiveData<Boolean> isGenerating = new MutableLiveData<>(false);
    public MutableLiveData<Bitmap> result = new MutableLiveData<>(null);

    public void onGenerate() {
        this.isGenerating.setValue(true);
    }

    public void onStop() {
        this.isGenerating.setValue(false);
    }
}
