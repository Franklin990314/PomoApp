package poli.edu.co.pomoapp.ui.pomodoro;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import poli.edu.co.pomoapp.R;

public class PomodoroFragment extends Fragment {

    private PomodoroViewModel mViewModel;

    public static PomodoroFragment newInstance() {
        return new PomodoroFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pomodoro, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PomodoroViewModel.class);
        // TODO: Use the ViewModel
    }

}