package com.pdrozzsolucoes.gor.utils.storage;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StorageConnection {

    public static StorageReference storageReference= FirebaseStorage.getInstance().getReference();

}
