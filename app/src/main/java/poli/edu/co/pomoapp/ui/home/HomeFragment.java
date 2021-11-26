package poli.edu.co.pomoapp.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import poli.edu.co.pomoapp.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView txtWhatIsItLabel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        txtWhatIsItLabel = (TextView) root.findViewById(R.id.txtWhatIsItLabel);

        txtWhatIsItLabel.setText(Html.fromHtml(String.valueOf(getResources().getString(R.string.home_text)), Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM));
        txtWhatIsItLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, (getResources().getDimension(R.dimen.txt_size_link) / getResources().getDisplayMetrics().density));

        return root;
    }
}