package ch.hftm.mobilecomputing.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CounterViewModel extends ViewModel {

    public final MutableLiveData<Boolean> isCounting = new MutableLiveData<>(false);
    public final MutableLiveData<Integer> counter = new MutableLiveData<>(0);

    public void onStart() {
        this.isCounting.setValue(true);
    }

    public void onStop() {
        this.isCounting.setValue(false);
    }

    public void countUp() {
        var value = this.counter.getValue();

        if (value == null) value = 0;

        this.counter.setValue(value + 1);
    }
}
