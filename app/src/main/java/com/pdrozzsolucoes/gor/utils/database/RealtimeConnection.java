package com.pdrozzsolucoes.gor.utils.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RealtimeConnection {

    public static String REF_OCORRENCIA="ocorrencia";

    public static DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
    public static DatabaseReference databaseOcorrencia=FirebaseDatabase.getInstance().getReference(REF_OCORRENCIA);
    //public static DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();

}
