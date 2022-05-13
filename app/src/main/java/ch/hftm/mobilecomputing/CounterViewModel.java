package ch.hftm.mobilecomputing;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CounterViewModel extends ViewModel {

    public MutableLiveData<Boolean> isCounting = new MutableLiveData<>(false);
    public MutableLiveData<Integer> counter = new MutableLiveData<>(0);

    public void onStart() {
        this.isCounting.setValue(true);
    }

    public void onStop() {
        this.isCounting.setValue(false);
    }

    public void countUp() {
        this.counter.setValue(this.counter.getValue() + 1);
    }
}
