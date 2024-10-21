package com.example.queueup.handlers;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.queueup.controllers.UserController;
import com.example.queueup.viewmodels.UserViewModel;
import com.example.queueup.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class CurrentUserHandler {
    private static CurrentUserHandler singleInstance = null;
    private static UserViewModel userViewModel;
    private static AppCompatActivity ownerActivity;
    private static UserController userController = UserController.getInstance();

    /**
     * Initializes the singleton instance of CurrentUserHandler.
     */
    private static void setSingleton() {
        if (ownerActivity == null) {
            throw new RuntimeException("Owner activity must be set in MainActivity.");
        }
        singleInstance = new CurrentUserHandler();
        userViewModel = new ViewModelProvider(ownerActivity).get(UserViewModel.class);
    }

    /**
     * Retrieves the singleton instance of CurrentUserHandler.
     *
     * @return The singleton instance.
     */
    public static CurrentUserHandler getSingleton() {
        if (singleInstance == null) {
            setSingleton();
        }
        return singleInstance;
    }

    private CurrentUserHandler() {}

    /**
     * Sets the owner activity for the handler.
     *
     * @param activity The owner AppCompatActivity.
     */
    public static void setOwnerActivity(AppCompatActivity activity) {
        ownerActivity = activity;
    }

    /**
     * Retrieves the current user's ID.
     *
     * @return The current user's UUID, or null if not available.
     */
    public String getCurrentUserId() {
        LiveData<User> userLiveData = userViewModel.getCurrentUser();
        User user = userLiveData.getValue();
        return user != null ? user.getUuid() : null;
    }

    /**
     * Retrieves the current User object.
     *
     * @return The current User, or null if not available.
     */
    public LiveData<User> getCurrentUser() {
        return userViewModel.getCurrentUser();
    }

    /**
     * Initiates login by device ID.
     */
    public void loginWithDeviceId() {
        String deviceId = userViewModel.getDeviceId();
        userViewModel.loadUserByDeviceId(deviceId);
    }

    /**
     * Creates a new user.
     *
     * @param user The user to create.
     */
    public Task<Void> createUser(User user) {
        return userViewModel.createUser(user);
    }


    /**
     * Updates an existing user.
     *
     * @param user The user to update.
     */
    public void updateUser(User user) {
        userViewModel.updateUser(user);
    }

    /**
     * Checks and updates the FCM token for the current user.
     */
    public void checkAndUpdateFcmToken() {
        String userId = getCurrentUserId();
        if (userId == null) return;

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String newFcmToken = task.getResult();
                        Log.d("CurrentUserHandler", "Retrieved FCM Token: " + newFcmToken);
                        userController.updateNotificationPreferences(userId, true)
                                .addOnSuccessListener(aVoid -> Log.d("CurrentUserHandler", "User FCM token successfully updated."))
                                .addOnFailureListener(e -> Log.e("CurrentUserHandler", "Failed to update user FCM token.", e));
                    } else {
                        Log.e("CurrentUserHandler", "Failed to generate new FCM token.", task.getException());
                    }
                });
    }

    // Additional methods related to waiting lists and profile pictures can remain unchanged

    /**
     * Retrieves error messages from the ViewModel.
     *
     * @return LiveData containing error messages.
     */
    public LiveData<String> getErrorMessage() {
        return userViewModel.getErrorMessage();
    }

    /**
     * Retrieves LiveData for the current user.
     *
     * @return LiveData containing the current User.
     */
    public LiveData<User> getCurrentUserLiveData() {
        return userViewModel.getCurrentUser();
    }

    /**
     * Retrieves the device ID.
     *
     * @return The device ID.
     */
    public String getDeviceId() {
        return userViewModel.getDeviceId();
    }
}
