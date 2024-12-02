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

import cat.insbaixcamp.gratitudejournal.fragments.LoginFragment;
import cat.insbaixcamp.gratitudejournal.utils.AuthUtils;
import cat.insbaixcamp.gratitudejournal.R;

public class RegisterAdapter {

    private final ProgressBar progressBar;
    private final Context context;

    public RegisterAdapter(ProgressBar progressBar, Context context) {
        this.progressBar = progressBar;
        this.context = context;
    }

    public void setUpRegistration(View view) {
        TextInputEditText etEmail = view.findViewById(R.id.email);
        TextInputEditText etPassword = view.findViewById(R.id.password);
        Button btnRegister = view.findViewById(R.id.btn_register);
        TextView goToLogin = view.findViewById(R.id.go_to_login);

        // Move focus to password field when "Next" is pressed on the email field
        etEmail.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                etEmail.requestFocus();
                return true;
            }
            return false;
        });

        // Trigger registration when "Done" is pressed on the password field
        etEmail.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performRegistration(etEmail, etPassword);
                return true;
            }
            return false;
        });

        // Trigger registration when the register button is clicked
        btnRegister.setOnClickListener(v -> performRegistration(etEmail, etPassword));

        // Navigate to login screen when the "Go to Login" text is clicked
        goToLogin.setOnClickListener(v -> ((androidx.fragment.app.FragmentActivity) context)
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .addToBackStack(null)
                .commit());
    }

    private void performRegistration(TextInputEditText editTextEmail, TextInputEditText editTextPassword) {
        String email = getTextOrEmpty(editTextEmail);
        String password = getTextOrEmpty(editTextPassword);

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
        authUtils.register(email, password, new AuthUtils.OnAuthResultListener() {
            @Override
            public void onAuthSuccess() {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show();
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
