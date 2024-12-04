package cat.insbaixcamp.gratitudejournal.middlewares;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cat.insbaixcamp.gratitudejournal.fragments.LoginFragment;
import cat.insbaixcamp.gratitudejournal.utils.FragmentUtils;

public class AuthMiddleware {

    private final Context context;
    private final FirebaseAuth fbAuth;

    public AuthMiddleware(Context context) {
        this.context = context;
        fbAuth = FirebaseAuth.getInstance();
    }

    public boolean isAuthenticated() {
        FirebaseUser fbUser = fbAuth.getCurrentUser();
        if (fbUser == null) {
            FragmentUtils.navigateTo(context, new LoginFragment(), true);
            return false;
        }
        return true;
    }
}
