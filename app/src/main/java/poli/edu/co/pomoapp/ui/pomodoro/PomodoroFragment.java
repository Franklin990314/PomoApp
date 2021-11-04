package poli.edu.co.pomoapp.ui.pomodoro;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import poli.edu.co.pomoapp.R;

public class PomodoroFragment extends Fragment {

    private PomodoroViewModel mViewModel;

    public static PomodoroFragment newInstance() {
        return new PomodoroFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(PomodoroViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pomodoro, container, false);
        final TextView textView = root.findViewById(R.id.text_pomodoro);
        mViewModel.setText(String.valueOf(getResources().getString(R.string.hello_pomodoro_fragment)));
        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}