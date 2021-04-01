package com.example.financeapp.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.financeapp.R;

public class BalanceDialog extends AppCompatDialogFragment {
    private EditText editTextBalance;
    private BalanceDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout, null);

        builder.setView(view)
                .setTitle("Set Balance")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double balance = Double.parseDouble(editTextBalance.getText().toString());
                        listener.applyBalance(balance);
                    }
                });

        editTextBalance = view.findViewById(R.id.EditText);
        editTextBalance.setHint("CA$...");

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (BalanceDialogListener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() +
                    "must implement BalanceDialogListener");
        }
    }

    public interface BalanceDialogListener{
        void applyBalance(double balance);
    }
}
