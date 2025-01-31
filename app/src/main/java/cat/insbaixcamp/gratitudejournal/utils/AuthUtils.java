package cat.insbaixcamp.gratitudejournal.utils;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cat.insbaixcamp.gratitudejournal.fragments.CalendarFragment;


public class AuthUtils {

    private final FirebaseFirestore firestore;
    private final FirebaseAuth fbAuth;
    private final Context context;

    public AuthUtils(Context context) {
        firestore = FirebaseFirestore.getInstance();
        fbAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    // Helper method to navigate to InsightsFragment after login
    private void navigateToInsightsFragment() {
        new BottomNavUtils(context).show();
        new SideBarUtils(context).enable();
        new SideBarUtils(context).fetchData();
        FragmentUtils.navigateTo(context, new CalendarFragment(), true);
    }

    // Sign in with email and password
    public void signIn(String email, String password, OnAuthResultListener listener) {
        fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                navigateToInsightsFragment();
                listener.onAuthSuccess();
            } else {
                String errorMessage = task.getException() != null ? task.getException().getLocalizedMessage() : "Unknown error occurred.";
                listener.onAuthFailure(errorMessage);
            }
        });
    }

    // Register a new user with email and password
    public void register(String email, String password, OnAuthResultListener listener) {
        fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String userId = Objects.requireNonNull(fbAuth.getCurrentUser()).getUid();

                Map<String, Object> user = new HashMap<>();
                user.put("userId", userId);
                user.put("points", 1);
                user.put("dayStreak", 1);
                user.put("lastTimeNoteCreated", System.currentTimeMillis());
                user.put("backgrounds", 1);
                user.put("currentBackground", 0);
                user.put("avatars", 3);
                user.put("currentAvatar", 0);

                firestore.collection("users")
                        .document(userId)
                        .set(user)
                        .addOnSuccessListener(aVoid -> signIn(email, password, listener))
                        .addOnFailureListener(e -> listener.onAuthFailure("Error creating user document: " + e.getMessage()));
            } else {
                String errorMessage = task.getException() != null ? task.getException().getLocalizedMessage() : "Unknown error occurred.";
                listener.onAuthFailure(errorMessage);
            }
        });
    }

    // Logout the user
    public void logout(Runnable onLogoutSuccess) {
        fbAuth.signOut();
        onLogoutSuccess.run();
    }

    // Removes the user account
    public void removeAccount(Runnable onAccountRemoved) {
        FirebaseUser currentUser = fbAuth.getCurrentUser();
        if (currentUser != null) {
            new UserUtils(context).deleteUserDataFromFirestore(() -> currentUser.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    onAccountRemoved.run();
                } else {
                    String errorMessage = task.getException() != null ? task.getException().getLocalizedMessage() : "Error deleting account.";
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                }
            }));
        }
    }

    // Gets the user email
    public String getEmail() {
        SharedPrefsUtils sharedPrefsUtils = new SharedPrefsUtils(context, "account");

        if (!sharedPrefsUtils.get("email", "").isEmpty()) {
            return sharedPrefsUtils.get("email", "anonymous");
        }

        FirebaseUser currentUser = fbAuth.getCurrentUser();
        if (currentUser != null && currentUser.getEmail() != null) {
            String email = currentUser.getEmail();

            if (!email.isEmpty()) {
                sharedPrefsUtils.createOrUpdate("email", email);
            }

            return email;
        }

        return "anonymous";
    }

    public interface OnAuthResultListener {
        void onAuthSuccess();

        void onAuthFailure(String errorMessage);
    }
}
