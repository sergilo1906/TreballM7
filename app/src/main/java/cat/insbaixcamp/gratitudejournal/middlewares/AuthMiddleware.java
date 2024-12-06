package cat.insbaixcamp.gratitudejournal.middlewares;

import com.google.firebase.auth.FirebaseAuth;

public class AuthMiddleware {
    public static boolean isAuthenticated() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }
}
