package poli.edu.co.pomoapp.ui.task;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import poli.edu.co.pomoapp.R;

public class TaskFragment extends Fragment {

    private TaskViewModel taskViewModel;
    private TextView textAddList;
    private TextView textAddTask;
    private TextView textTaskListName;
    private TextView textNameTaskList;
    private TextView textTask;
    private AlertDialog.Builder taskListDialogBuilder;
    private AlertDialog taskListDialog;
    private Button buttonDelete;
    private Button buttonPomodoro;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        View root = inflater.inflate(R.layout.fragment_task, container, false);

        textAddList = (TextView) root.findViewById(R.id.txtAddList);
        buttonPomodoro = (Button)  root.findViewById(R.id.btnPomodoro);

        buttonPomodoro.setVisibility(View.GONE);

        textAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTaskList(root, inflater);
            }
        });

        buttonPomodoro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), String.valueOf(getResources().getString(R.string.pomodoro_init)), Toast.LENGTH_LONG).show();
            }
        });

        textAddList.setTextSize(TypedValue.COMPLEX_UNIT_PX, (getResources().getDimension(R.dimen.txt_size_link) / getResources().getDisplayMetrics().density));

        return root;
    }

    private void addNewTaskList(View root, LayoutInflater inflater) {
        LinearLayout parentTaskListLayout = (LinearLayout) root.findViewById(R.id.parent_task_list_layout);
        View rootTaskListView = inflater.inflate(R.layout.row_add_task_list, null);
        textTaskListName = (TextView) rootTaskListView.findViewById(R.id.txtTaskListName);
        textAddTask = (TextView) rootTaskListView.findViewById(R.id.txtAddTask);

        textTaskListName.setTextSize(TypedValue.COMPLEX_UNIT_PX, (getResources().getDimension(R.dimen.txt_size_link) / getResources().getDisplayMetrics().density));
        textAddTask.setTextSize(TypedValue.COMPLEX_UNIT_PX, (getResources().getDimension(R.dimen.txt_size_link) / getResources().getDisplayMetrics().density));

        String textList = String.valueOf(getResources().getString(R.string.task_list)).concat(" #"+(parentTaskListLayout.getChildCount()+1));
        textTaskListName.setText(textList);
        textTaskListName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                createNewTaskListDialog(root, rootTaskListView, parentTaskListLayout);
                return false;
            }
        });

        textAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTask(root, rootTaskListView, inflater, parentTaskListLayout);
            }
        });

        buttonPomodoro.setVisibility(View.VISIBLE);
        parentTaskListLayout.addView(rootTaskListView, 0);
    }

    private void addNewTask(View root, View rootTaskListView, LayoutInflater inflater, LinearLayout parentTaskListLayout) {
        LinearLayout parentTaskLayout = (LinearLayout) rootTaskListView.findViewById(R.id.parent_task_layout);
        View rootTaskView = inflater.inflate(R.layout.row_add_task, null);

        textTask = (TextView) rootTaskView.findViewById(R.id.txtTask);

        String text = String.valueOf(getResources().getString(R.string.task)).concat(" #"+(parentTaskLayout.getChildCount()+1));
        textTask.setText(text);
        textTask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                createNewTaskDialog(root, rootTaskView, parentTaskLayout, parentTaskListLayout);
                return false;
            }
        });

        parentTaskLayout.addView(rootTaskView, 0);
    }

    private void createNewTaskListDialog(View root, View rootTaskListView, LinearLayout parentTaskListLayout) {
        taskListDialogBuilder = new AlertDialog.Builder(root.getContext());
        View taskListPopupView = getLayoutInflater().inflate(R.layout.popup_task_list, null);

        textTaskListName = (TextView) rootTaskListView.findViewById(R.id.txtTaskListName);
        textNameTaskList = (TextView) taskListPopupView.findViewById(R.id.txtNameTaskList);
        buttonDelete = (Button)  taskListPopupView.findViewById(R.id.btnDelete);

        textNameTaskList.setText(textTaskListName.getText().toString().trim());

        textNameTaskList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                textTaskListName.setText(textNameTaskList.getText().toString().trim());
            }
        });

        buttonDelete.setText(String.valueOf(getResources().getString(R.string.delete_task_list)));
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentTaskListLayout.removeView(rootTaskListView);
                root.refreshDrawableState();
                if (parentTaskListLayout.getChildCount() == 0) {
                    buttonPomodoro.setVisibility(View.GONE);
                }
                taskListDialog.dismiss();
            }
        });

        taskListDialogBuilder.setView(taskListPopupView);
        taskListDialog = taskListDialogBuilder.create();
        taskListDialog.show();
    }

    private void createNewTaskDialog(View root, View rootTaskView, LinearLayout parentTaskLayout, LinearLayout parentTaskListLayout) {
        taskListDialogBuilder = new AlertDialog.Builder(root.getContext());
        View taskListPopupView = getLayoutInflater().inflate(R.layout.popup_task_list, null);

        textTask = (TextView) rootTaskView.findViewById(R.id.txtTask);
        textNameTaskList = (TextView) taskListPopupView.findViewById(R.id.txtNameTaskList);
        buttonDelete = (Button)  taskListPopupView.findViewById(R.id.btnDelete);

        textNameTaskList.setText(textTask.getText().toString().trim());

        textNameTaskList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                textTask.setText(textNameTaskList.getText().toString().trim());
            }
        });

        buttonDelete.setText(String.valueOf(getResources().getString(R.string.delete_task)));
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentTaskLayout.removeView(rootTaskView);
                root.refreshDrawableState();
                taskListDialog.dismiss();
            }
        });

        taskListDialogBuilder.setView(taskListPopupView);
        taskListDialog = taskListDialogBuilder.create();
        taskListDialog.show();
    }
}