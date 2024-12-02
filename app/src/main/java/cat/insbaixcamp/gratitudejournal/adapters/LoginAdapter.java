package cat.insbaixcamp.gratitudejournal.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import cat.insbaixcamp.gratitudejournal.fragments.RegisterFragment;
import cat.insbaixcamp.gratitudejournal.utils.AuthUtils;
import cat.insbaixcamp.gratitudejournal.R;

public class LoginAdapter {

    private final ProgressBar progressBar;
    private final Context context;

    public LoginAdapter(ProgressBar progressBar, Context context) {
        this.progressBar = progressBar;
        this.context = context;
    }

    public void setUpLogin(View view) {
        TextInputEditText etEmail = view.findViewById(R.id.email);
        TextInputEditText etPassword = view.findViewById(R.id.password);
        Button btnLogin = view.findViewById(R.id.btn_login);
        TextView goToRegister = view.findViewById(R.id.go_to_register);

        etEmail.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                etPassword.requestFocus();
                return true;
            }
            return false;
        });

        etPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performLogin(etEmail, etPassword);
                return true;
            }
            return false;
        });

        btnLogin.setOnClickListener(v -> performLogin(etEmail, etPassword));

        goToRegister.setOnClickListener(v -> ((androidx.fragment.app.FragmentActivity) context)
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new RegisterFragment())
                .addToBackStack(null)
                .commit());
    }

    private void performLogin(TextInputEditText etEmail, TextInputEditText etPassword) {
        String email = getTextOrEmpty(etEmail);
        String password = getTextOrEmpty(etPassword);

        progressBar.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(email)) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthUtils authUtils = new AuthUtils(context);
        authUtils.signIn(email, password, new AuthUtils.OnAuthResultListener() {
            @Override
            public void onAuthSuccess() {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthFailure(String errorMessage) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Authentication failed: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getTextOrEmpty(TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }
}