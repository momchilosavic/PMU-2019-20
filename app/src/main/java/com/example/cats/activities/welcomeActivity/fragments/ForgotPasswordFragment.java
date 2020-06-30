package com.example.cats.activities.welcomeActivity.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cats.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordFragment extends Fragment {

    private OnForgotPasswordListener mListener;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        final TextInputLayout mailLayout = view.findViewById(R.id.forgotPasswordLayout);
        final TextInputEditText mailText = view.findViewById(R.id.forgotPasswordText);
        Button button = view.findViewById(R.id.sendPasswordButton);
        //TextView gotoLogin = view.findViewById(R.id.backToLogin);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mailText.getText().toString();
                if(mail.isEmpty()) mailLayout.setError("You must enter mail!");
                else{
                    if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) mailLayout.setError("Mail not valid!");
                    else mailLayout.setError(null);
                }
                if(mailLayout.getError() != null) return;

                mListener.onForgotPassword(mail);
            }
        });

        /*gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBackToLogin();
            }
        });*/

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnForgotPasswordListener) {
            mListener = (OnForgotPasswordListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnForgotPasswordListener {
        // TODO: Update argument type and name
        void onForgotPassword(String mail);
        void onBackToLogin();
    }
}
