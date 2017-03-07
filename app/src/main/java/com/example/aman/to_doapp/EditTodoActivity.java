package com.example.aman.to_doapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aman.to_doapp.interfaces.IEditPresenter;
import com.example.aman.to_doapp.interfaces.IEditView;
import com.example.aman.to_doapp.interfaces.ITodoService;
import com.example.aman.to_doapp.models.TodosModel;
import com.example.aman.to_doapp.presenters.EditTodoPresenter;
import com.example.aman.to_doapp.services.TodoService;
import com.example.aman.to_doapp.viewmodels.EditTodoViewModel;

/**
 * Created by Aman on 2/15/17.
 */

public class EditTodoActivity extends AppCompatActivity implements IEditView, View.OnClickListener {

    // presenter
    IEditPresenter presenter;

    // widgets
    TextView nameTv;
    TextView viewModelTv;
    EditText nameEt;
    EditText contentEt;
    EditText dueDateEt;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_todo_layout);
        bindView();
        ITodoService todoService = TodoService.gettodoService();
        presenter = new EditTodoPresenter(this, new TodosModel(todoService));
        presenter.showName(getIntent().getIntExtra("START_REASON", Constants.ADD_TODO_REQUEST_CODE));
        presenter.setViewModel(getIntent());
    }

    private void bindView() {
        nameTv = (TextView)findViewById(R.id.name_text);
        nameEt = (EditText)findViewById(R.id.nameEditText);
        contentEt = (EditText)findViewById(R.id.contentEditText);
        dueDateEt = (EditText)findViewById(R.id.dueDateEditText);
        viewModelTv = (TextView)findViewById(R.id.debug_model);
        saveBtn = (Button)findViewById(R.id.save_button);

        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name = nameEt.getText().toString();
                String content = contentEt.getText().toString();
                String dueDate = dueDateEt.getText().toString();

                presenter.saveTodo(name, content, dueDate);

          }
        });

        saveBtn.setOnClickListener(this);

        nameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                presenter.updateTodoName(editable.toString());
            }


        });

        contentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                presenter.updateTodoContent(editable.toString());
            }
        });

        dueDateEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                presenter.updateTodoDueDate(editable.toString());
            }
        });
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, EditTodoActivity.class);
        return intent;
    }

    @Override
    public void displayName(int name) {
        nameTv.setText(name);
    }

    @Override
    public void updateViewWithViewModel(EditTodoViewModel vm) {
        viewModelTv.setText("Name: " + vm.name + ", Content: " + vm.content
                +  ", Due Date: " + vm.dueDate);
    }


    @Override
    public void returnResult(EditTodoViewModel viewModel) {
        Intent intent = viewModel.makeIntent();
        presenter.saveTodo(viewModel.name, viewModel.content, viewModel.dueDate);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void displayInvalid(EditTodoViewModel viewModel) {
        viewModelTv.setText("Input is not valid");
    }

    @Override
    public void setInitialFields(EditTodoViewModel viewModel) {
        nameEt.setText(viewModel.name);
        contentEt.setText(viewModel.content);
        dueDateEt.setText(viewModel.dueDate);
    }

    @Override
    public void sendTodoBack(Intent intent) {
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.save_button:
                presenter.validateModel();
                break;
        }
    }
}
