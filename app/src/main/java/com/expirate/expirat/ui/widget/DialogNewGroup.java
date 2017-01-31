package com.expirate.expirat.ui.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.expirate.expirat.R;
import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DialogNewGroup extends DialogFragment {

    @Bind(R.id.group_input_layout) TextInputLayout groupInputLayout;
    @Bind(R.id.group_input) EditText groupInput;
    @Bind(R.id.btn_save) TextView btnSave;

    private DialogInputListener inputListener;

    public static DialogNewGroup newInstance() {
        return new DialogNewGroup();
    }

    public void setInputListener(DialogInputListener inputListener) {
        this.inputListener = inputListener;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_input_group, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        RxTextView.textChanges(groupInput)
                .map(charSequence -> charSequence.length() > 0)
                .subscribe(enabled -> { btnSave.setEnabled(enabled); },
                        throwable -> { /* DO NOTHING */ });

        btnSave.setOnClickListener(v -> {
            dismiss();

            if (inputListener == null) return;

            inputListener.onSave(groupInput.getText().toString());
        });
    }

    public interface DialogInputListener {
        void onSave(String value);
    }
}
