package cat.insbaixcamp.gratitudejournal.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

import java.util.Objects;

public class UserUtils {

    private final FirebaseFirestore firestore;
    private final FirebaseAuth fbAuth;

    public UserUtils() {
        firestore = FirebaseFirestore.getInstance();
        fbAuth = FirebaseAuth.getInstance();
    }

    // Fetch user points from Firestore
    public void getUserPoints(String userId, OnUserPointsFetchedListener listener) {
        DocumentReference userDocRef = firestore.collection("users").document(userId);
        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                int points = Objects.requireNonNull(documentSnapshot.getLong("points")).intValue();
                listener.onSuccess(points);
            } else {
                listener.onFailure("User data not found.");
            }
        }).addOnFailureListener(e -> listener.onFailure("Error fetching points: " + e.getLocalizedMessage()));
    }

    // Update user points in Firestore
    public void updateUserPoints(String userId, int points, Runnable onSuccess, Runnable onFailure) {
        if (userId == null || userId.isEmpty()) {
            onFailure.run();
            return;
        }

        // Reference to the user document in Firestore
        firestore.collection("users")
                .document(userId)
                .update("points", points)
                .addOnSuccessListener(aVoid -> onSuccess.run())
                .addOnFailureListener(e -> onFailure.run());
    }

    // Delete user data from Firestore
    public void deleteUserDataFromFirestore(String userId, Runnable onDataDeleted) {
        DocumentReference userDocRef = firestore.collection("users").document(userId);
        userDocRef.delete()
                .addOnSuccessListener(aVoid -> onDataDeleted.run())
                .addOnFailureListener(e -> onDataDeleted.run());
    }

    // Get the current user's ID from FirebaseAuth
    public String getUserId() {
        if (fbAuth.getCurrentUser() != null) {
            return fbAuth.getCurrentUser().getUid();
        }

        return null;
    }

    public interface OnUserPointsFetchedListener {
        void onSuccess(int points);

        void onFailure(String errorMessage);
    }
}
