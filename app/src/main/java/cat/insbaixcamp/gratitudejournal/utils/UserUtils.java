package cat.insbaixcamp.gratitudejournal.utils;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;


public class UserUtils {

    private final FirebaseFirestore firestore;
    private final FirebaseAuth fbAuth;
    private final String userId;
    private final Context context;

    public UserUtils(Context context) {
        firestore = FirebaseFirestore.getInstance();
        fbAuth = FirebaseAuth.getInstance();
        this.context = context;
        this.userId = getUserId();
    }

    // Get the current user's ID from FirebaseAuth
    public String getUserId() {
        assert fbAuth != null;
        if (fbAuth.getCurrentUser() != null) {
            return fbAuth.getCurrentUser().getUid();
        }
        return null;
    }

    // Fetch user points from Firestore asynchronously and use a callback
    public void getUserPoints(final OnFetchCallback<Integer> callback) {
        if (userId == null || userId.isEmpty()) {
            callback.onFailure("User ID is invalid.");
            return;
        }

        DocumentReference userDocRef = firestore.collection("users").document(userId);
        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Long pointsLong = documentSnapshot.getLong("points");
                if (pointsLong != null) {
                    callback.onSuccess(pointsLong.intValue());
                } else {
                    callback.onFailure("Points not found.");
                }
            } else {
                callback.onFailure("User data not found.");
            }
        }).addOnFailureListener(e -> callback.onFailure("Error fetching points: " + e.getMessage()));
    }

    // Update user points in Firestore asynchronously
    public void updateUserPoints(int points, final Runnable onSuccess, final Runnable onFailure) {
        if (userId == null || userId.isEmpty()) {
            onFailure.run();
            return;
        }

        firestore.collection("users")
                .document(userId)
                .update("points", points)
                .addOnSuccessListener(aVoid -> onSuccess.run())
                .addOnFailureListener(e -> onFailure.run());

        new SideBarUtils(context).fetchData(); // Update sidebar
    }

    // Delete user data from Firestore
    public void deleteUserDataFromFirestore(final Runnable onDataDeleted) {
        DocumentReference userDocRef = firestore.collection("users").document(userId);
        userDocRef.delete()
                .addOnSuccessListener(aVoid -> onDataDeleted.run())
                .addOnFailureListener(e -> onDataDeleted.run());
    }

    // Callback interface for asynchronous fetching
    public interface OnFetchCallback<T> {
        void onSuccess(T value);

        void onFailure(String errorMessage);
    }
}
