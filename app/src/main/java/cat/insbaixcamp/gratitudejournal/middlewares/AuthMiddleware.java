package cat.insbaixcamp.gratitudejournal.middlewares;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cat.insbaixcamp.gratitudejournal.R;
import cat.insbaixcamp.gratitudejournal.fragments.LoginFragment;

public class AuthMiddleware {

    private final FirebaseAuth fbAuth;

    public AuthMiddleware() {
        fbAuth = FirebaseAuth.getInstance();
    }

    public boolean isAuthenticated(Context context) {
        FirebaseUser fbUser = fbAuth.getCurrentUser();
        if (fbUser == null) {
            showLoginFragment(context);
            return false;
        }
        return true;
    }

    private void showLoginFragment(Context context) {
        ((androidx.fragment.app.FragmentActivity) context).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }
}
