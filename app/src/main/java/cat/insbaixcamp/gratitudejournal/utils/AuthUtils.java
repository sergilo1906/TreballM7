package cat.insbaixcamp.gratitudejournal.utils;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.fragment.app.FragmentActivity;

import cat.insbaixcamp.gratitudejournal.fragments.InsightsFragment;


public class AuthUtils {

    private final FirebaseFirestore firestore;
    private final FirebaseAuth fbAuth;
    private final UserUtils userUtils;
    private final Context context;

    public AuthUtils(Context context) {
        firestore = FirebaseFirestore.getInstance();
        fbAuth = FirebaseAuth.getInstance();
        userUtils = new UserUtils();
        this.context = context;
    }

    // Helper method to navigate to InsightsFragment after login
    private void navigateToInsightsFragment() {
        new BottomNavigationUtils(context).show();
        new SideBarUtils(context).enable();
        FragmentUtils.navigateTo((FragmentActivity) context, new InsightsFragment(), true);
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

                // Create a map of user data instead of using the User object
                Map<String, Object> user = new HashMap<>();
                user.put("userId", userId);
                user.put("points", 0);

                firestore.collection("users")
                        .document(userId)
                        .set(user)
                        .addOnSuccessListener(aVoid -> userUtils.updateUserPoints(userId, 0,
                                () -> signIn(email, password, new OnAuthResultListener() {
                                    @Override
                                    public void onAuthSuccess() {
                                        navigateToInsightsFragment();
                                        listener.onAuthSuccess();
                                    }

                                    @Override
                                    public void onAuthFailure(String errorMessage) {
                                        listener.onAuthFailure(errorMessage);
                                    }
                                }),
                                () -> listener.onAuthFailure("Error initializing points.")
                        ))
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
            String userId = currentUser.getUid();

            userUtils.deleteUserDataFromFirestore(userId, () -> currentUser.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    onAccountRemoved.run();
                } else {
                    String errorMessage = task.getException() != null ? task.getException().getLocalizedMessage() : "Error deleting account.";
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                }
            }));
        }
    }

    public interface OnAuthResultListener {
        void onAuthSuccess();

        void onAuthFailure(String errorMessage);
    }
}
