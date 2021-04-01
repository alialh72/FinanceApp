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

public class MerchantDialog extends AppCompatDialogFragment {
    private EditText editTextMerchant;
    private MerchantDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout, null);

        builder.setView(view)
                .setTitle("Merchant")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String merchantName = editTextMerchant.getText().toString();
                        listener.applyTexts(merchantName);
                    }
                });

        editTextMerchant = view.findViewById(R.id.EditText);
        editTextMerchant.setHint("Doordash, Walmart, etc.");

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (MerchantDialogListener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() +
                    "must implement MerchantDialogListener");
        }
    }

    public interface MerchantDialogListener{
        void applyTexts(String merchantName);
    }
}
