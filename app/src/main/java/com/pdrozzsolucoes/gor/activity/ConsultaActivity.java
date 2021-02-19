package com.pdrozzsolucoes.gor.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pdrozzsolucoes.gor.R;
import com.pdrozzsolucoes.gor.adapter.ItemSearchAdapter;
import com.pdrozzsolucoes.gor.helper.ViewMethodsHelper;
import com.pdrozzsolucoes.gor.model.UrlsPdfModel;
import com.pdrozzsolucoes.gor.utils.MaskText;
import com.pdrozzsolucoes.gor.utils.database.RealtimeConnection;

import java.util.ArrayList;
import java.util.List;

public class ConsultaActivity extends AppCompatActivity {

    private final String NUMERO="numero";
    private final String DATA="data";
    private final String CEP="cep";
    private final String ENDERECO="endereco";

    private final String OCORRENCIA="ocorrencia";
    private final String TRATAMENTO="tratamento";
    private final String ORIENTACAO="orientacao";

    private EditText editSearch;
    private DatabaseReference ref;
    private Query query;

    private List<UrlsPdfModel> listItems=new ArrayList<>();
    private List<String> items=new ArrayList<>();

    private ValueEventListener singlevalueEventListener;
    private ChildEventListener childEventListener;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ItemSearchAdapter adapter;

    String TAG="consultaconsulta";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        configWidgets();
        layoutManager=new LinearLayoutManager(this);
        adapter=new ItemSearchAdapter(listItems,this);
        initRecycler();

        initGroupListener();
        query=RealtimeConnection.databaseOcorrencia.child("ocorrencia");


        childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i(TAG, "onChildAdded: "+dataSnapshot.getValue().toString());
                UrlsPdfModel model=dataSnapshot.getValue(UrlsPdfModel.class);
                listItems.add(model);
                initRecycler();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

    }

    private void initGroupListener(){
        RadioGroup group=findViewById(R.id.groupPesquisa);
        RadioButton radioEndereco=findViewById(R.id.radioEndereco);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                editSearch=null;
                if(getRadioIsChecked(R.id.radioNumero)){
                    editSearch=findViewById(R.id.editSearch);
                    editSearch.setText("");
                    editSearch.setHint("Ex: 123123/2020");
                    return;
                }
                if(getRadioIsChecked(R.id.radioData)){
                    editSearch=findViewById(R.id.editSearch);
                    editSearch.setText("");
                    editSearch.setHint("Ex: 12/02/2020");
                    return;
                }
                editSearch=findViewById(R.id.editSearch);
                editSearch.setText("");
                editSearch.setHint("Endereço");
            }
        });

        RadioGroup groupTipo=findViewById(R.id.groupTipo);
        groupTipo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                listItems.clear();
                if(getRadioIsChecked(R.id.radioTratamento)){
                    radioEndereco.setVisibility(View.GONE);
                    return;
                }
                radioEndereco.setVisibility(View.VISIBLE);
            }
        });

    }

    private void configWidgets(){
        editSearch=findViewById(R.id.editSearch);
        recyclerView=findViewById(R.id.recycler);
    }

    public void search(View v){
        String s=editSearch.getText().toString();
        String type=getSearchType();
        String tipo=getTipo();
        listItems.clear();
        if (tipo.equals(OCORRENCIA))
        switch (type){
            case NUMERO:
                Log.i(TAG, "search type nuumero");
                items.clear();
                ref= RealtimeConnection.databaseOcorrencia;
                query.orderByChild("n").startAt(s).endAt(s+"\uf8ff").addChildEventListener(childEventListener);
                break;
            case DATA:
                items.clear();
                ref= RealtimeConnection.databaseOcorrencia;
                s.replace("/","-");
                query.orderByChild("dt").startAt(s).endAt(s+"\uf8ff").addChildEventListener(childEventListener);
                break;
            case CEP:
                items.clear();
                ref= RealtimeConnection.databaseOcorrencia;
                query.orderByChild("end").startAt(s).addChildEventListener(childEventListener);
                break;
            case ENDERECO:
                items.clear();
                ref= RealtimeConnection.databaseOcorrencia;
                query.orderByChild("end").startAt(s).endAt(s+"\uf8ff").addChildEventListener(childEventListener);
                break;
        }
        DatabaseReference refTratamento=RealtimeConnection.databaseOcorrencia.child("ficha");
        Query queryTratamento=refTratamento;
        if (tipo.equals(TRATAMENTO))
            switch (type){
                case NUMERO:
                    Log.i(TAG, "search type nuumero");
                    items.clear();
                    queryTratamento.orderByChild("n").startAt(s).endAt(s+"\uf8ff").addChildEventListener(childEventListener);
                    break;
                case DATA:
                    items.clear();
                    ref= RealtimeConnection.databaseOcorrencia;
                    s.replace("/","-");
                    queryTratamento.orderByChild("dt").startAt(s).endAt(s+"\uf8ff").addChildEventListener(childEventListener);
                    break;
                case ENDERECO:
                    items.clear();
                    ref= RealtimeConnection.databaseOcorrencia;
                    queryTratamento.orderByChild("end").startAt(s).endAt(s+"\uf8ff").addChildEventListener(childEventListener);
                    break;
            }
        Query queryOrientacao=RealtimeConnection.databaseOcorrencia.child("orientacao");
        if (tipo.equals(ORIENTACAO))
            if (tipo.equals(TRATAMENTO))
                switch (type){
                    case NUMERO:
                        Log.i(TAG, "search type nuumero");
                        items.clear();
                        queryOrientacao.orderByChild("n").startAt(s).endAt(s+"\uf8ff").addChildEventListener(childEventListener);
                        break;
                    case DATA:
                        items.clear();
                        ref= RealtimeConnection.databaseOcorrencia;
                        s.replace("/","-");
                        queryOrientacao.orderByChild("dt").startAt(s).endAt(s+"\uf8ff").addChildEventListener(childEventListener);
                        break;
                    case ENDERECO:
                        items.clear();
                        ref= RealtimeConnection.databaseOcorrencia;
                        queryOrientacao.orderByChild("end").startAt(s).endAt(s+"\uf8ff").addChildEventListener(childEventListener);
                        break;
                }
    }

    private void initRecycler(){
        Log.i(TAG, "initRecycler: init");
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private String getTipo(){
        if (getRadioIsChecked(R.id.radioOcorrencia)){
            return OCORRENCIA;
        }
        if (getRadioIsChecked(R.id.radioOrientacoes)){
            return ORIENTACAO;
        }
        if (getRadioIsChecked(R.id.radioTratamento))
            return TRATAMENTO;
        return "NA";
    }

    public String getSearchType(){
        if (getRadioIsChecked(R.id.radioData))
            return DATA;
        if (getRadioIsChecked(R.id.radioEndereco))
            return ENDERECO;
        if (getRadioIsChecked(R.id.radioNumero))
            return NUMERO;
        return "NA";
    }

    private boolean getRadioIsChecked(int id){
        RadioButton radioButton=findViewById(id);
        Log.i(TAG, "getRadioIsChecked: "+radioButton.isChecked());
        return radioButton.isChecked();
    }

}
