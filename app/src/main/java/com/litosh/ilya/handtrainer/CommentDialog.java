package com.litosh.ilya.handtrainer;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.litosh.ilya.handtrainer.db.DBService;
import com.litosh.ilya.handtrainer.db.models.Note;

/**
 * Created by ilya_ on 17.03.2018.
 */

public class CommentDialog extends Dialog {

    private Context context;
    private LayoutInflater layoutInflater;
    private Button buttonYes, buttonNo, buttonSubmit;
    private EditText inputComment;
    private LinearLayout commentForm, questionForm;
    private DBService dbService;

    public CommentDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        dbService = new DBService();
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void setContentView(@NonNull View view) {
        View newView;
        newView = layoutInflater.inflate(R.layout.comment_dialog, null, false);
        initComponents(newView);
        initListener();

        useConfig();
        super.setContentView(newView);
    }

    @Override
    public void show() {
        setContentView(new View(context));
        super.show();
    }

    private void initListener(){
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note = new Note();
                note.setId(User.getUserId());
                note.setComment(" ");
                if(User.getSessionCountRotations() == -1){
                    note.setTask(String.valueOf(User.getFinishedCountRotations()));
                }else{
                    note.setTask(User.getFinishedCountRotations() + "/" + User.getSessionCountRotations());
                }
                note.setDuration(User.getCurrentDuration());
                note.setDate(User.getCurrentDate());
                dbService.addNote(note);
                dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionForm.setVisibility(View.GONE);
                commentForm.setVisibility(View.VISIBLE);
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note = new Note();
                note.setId(User.getUserId());
                note.setComment(inputComment.getText().toString());
                if(User.getSessionCountRotations() == -1){
                    note.setTask(String.valueOf(User.getFinishedCountRotations()));
                }else{
                    note.setTask(User.getFinishedCountRotations() + "/" + User.getSessionCountRotations());
                }
                note.setDuration(User.getCurrentDuration());
                note.setDate(User.getCurrentDate());
                dbService.addNote(note);
                dismiss();
            }
        });
    }

    private void initComponents(View view){
        buttonNo = view.findViewById(R.id.button_no_commentdialog);
        buttonYes = view.findViewById(R.id.button_yes_commentdialog);
        commentForm = view.findViewById(R.id.comment_form_commentdialog);
        questionForm = view.findViewById(R.id.question_form_commentdialog);
        inputComment = view.findViewById(R.id.input_comment_commentdialog);
        buttonSubmit = view.findViewById(R.id.submit_button_comment_commentdialog);
    }

    private void useConfig(){
        setCancelable(false);
        setTitle("Оставить комментарий?");
    }

}
