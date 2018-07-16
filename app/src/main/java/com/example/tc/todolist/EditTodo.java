package com.example.tc.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditTodo extends AppCompatActivity {

    public static final String TITLE_KEY="Title";
    public static final String DESCRIPTION_KEY="Description";
    public static final String POSITION_KEY="Position";

    public static final int RESULT_CODE=2;

    int Position;

    EditText editText;
    EditText editText1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);

        editText=findViewById(R.id.N);
        editText1=findViewById(R.id.D);

        Intent intent =getIntent();
        Bundle bundle=intent.getExtras();
        String title=bundle.getString(TITLE_KEY);
        String description=bundle.getString(DESCRIPTION_KEY);
        Position=bundle.getInt(POSITION_KEY);

        editText.setText(title);
        editText1.setText(description);
    }

    public void save(View view){

        String new_title=editText.getText().toString();
        String new_description=editText1.getText().toString();

        Intent data=new Intent();
        data.putExtra(TITLE_KEY,new_title);
        data.putExtra(DESCRIPTION_KEY,new_description);
        data.putExtra(POSITION_KEY,Position);

        setResult(RESULT_CODE,data);
        finish();
    }

}
