package poli.edu.co.pomoapp.ui.pomodoro;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PomodoroViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PomodoroViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText.setValue(mText);
    }
}