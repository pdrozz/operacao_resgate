package com.pdrozzsolucoes.gor.utils.auth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthUtils {

    public static FirebaseAuth auth;

    public static void initializeAuth(){
        if(auth==null){
            auth=FirebaseAuth.getInstance();
        }
    }

    public static FirebaseUser getCurrentUser(){
        initializeAuth();
        return auth.getCurrentUser();
    }

    public static Task<AuthResult> login(String s, String s1){
        initializeAuth();
        return auth.signInWithEmailAndPassword(s,s1);
    }

    public static void logout(){
        initializeAuth();
        auth.signOut();
    }

    public static boolean isLogged(){
        initializeAuth();
        if (auth.getCurrentUser()==null){
            return false;
        }
        else{
            return true;
        }
    }

}
