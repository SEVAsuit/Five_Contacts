package com.example.fivecontacts.main.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fivecontacts.R;
import com.example.fivecontacts.main.model.Contato;
import com.example.fivecontacts.main.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ListaDeContatos_ListView extends AppCompatActivity implements UIEducacionalPermissao.NoticeDialogListener,BottomNavigationView.OnNavigationItemSelectedListener{

    ListView lv;
    User user;
    boolean permission_asked;
   /* String[] itens ={"Filha", "Filho", "Netinho"};
    String[] numeros ={"tel:000000003435","tel:2000348835","tel:1003435888" };*/

    BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_contatos);
        bnv=findViewById(R.id.bnv);
        bnv.setOnNavigationItemSelectedListener(this);
       lv= findViewById(R.id.listView1);
        try
        {
            preencherListaDeContatos(); //Montagem do ListView
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        //Dados da Intent Anterior
        Intent quemChamou=this.getIntent();
        if (quemChamou!=null) {
            Bundle params = quemChamou.getExtras();
            if (params!=null) {
                //Recuperando o Usuario
                user = (User) params.getSerializable("usuario");
                if (user != null) {

                    Log.v("PDMRAFA", user.getNome());
                    Log.v("PDMRAFA", user.getLogin());
                    Log.v("PDMRAFA", user.getSenha());
                    Log.v("PDMRAFA", user.getEmail());

                }
            }
        }

    }

    protected void preencherListaDeContatos () throws UnsupportedEncodingException {
        //Vamos montar o ListView
       /*Contato c1,c2;
        c1=new Contato();
        c1.setNome("Neymar pai ta off");
        c1.setNumero("2");
                    final ArrayList<Contato> contatos;
        contatos=new ArrayList<Contato>();
        contatos.add(c1);
        //contatos.add(c2);*/
        SharedPreferences recuperarContatos=getSharedPreferences("contatos2", Activity.MODE_PRIVATE);
        int num=recuperarContatos.getInt("numContatos",0);
        final ArrayList<Contato> contatos =new ArrayList<Contato>();
        Contato contato;
        for(int i=1;i<=num;i++) {
            String objSel = recuperarContatos.getString("contato" + i, "");
            if (objSel.compareTo("") != 0) {
                try {


                    ByteArrayInputStream bis = new ByteArrayInputStream(objSel.getBytes(StandardCharsets.ISO_8859_1.name()));
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    contato = (Contato) ois.readObject();
                    Log.v("06092020","oi?");

                    if (contato != null)
                    {
                        contatos.add(contato);
                        Log.v("06092020","oi?");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
ArrayAdapter<String> adapter;
//adapter=new ArrayAdapter<String>(this,android.R.layout.preference_category);
        String[] itens=new String[contatos.size()];
        final String[] numeros=new String[contatos.size()];//WTF?
        for(int i=0;i<contatos.size();i++)
        {
            itens[i]=contatos.get(i).getNome();
            numeros[i]=contatos.get(i).getNumero();

        }
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,itens);
lv.setAdapter(adapter);
lv.setOnItemClickListener
        (
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long l)//o AdapterView<?> parent pode estar dando erro
                    {
                        checarPermissaoPhone(numeros[i]);
                    }
                }
        );
    }
        protected void checarPermissaoPhone(String s)
        {


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Uri uri = Uri.parse(s);
                Intent itLigar = new Intent(Intent.ACTION_CALL, uri);
                //intent=new Intent(Intent.ACTION_DIAL,uri);
                startActivity(itLigar);
            } else {
                if(!permission_asked)
                {
                    String[] permissoes = {Manifest.permission.CALL_PHONE};
                    ActivityCompat.requestPermissions(this, permissoes, 1212);
                    permission_asked=true;
                }
                else
                {
                    Uri uri = Uri.parse(s);
                    Intent itLigar = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(itLigar);
                }
            }
        }

        protected boolean checarPermissaoPhone_SMD()
        {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED)
            {
                Log.v("SMD","Tenho permissão");
                String mensagem="Nossa aplicação precisa acessar o telefone para discagem automática. Uma janela de permissão será aberta.";
                String titulo="Permissão de acesso a chamadas";
                int codigo=1;
                UIEducacionalPermissao mensagemPermissao=new UIEducacionalPermissao(mensagem,titulo,codigo);
                mensagemPermissao.onAttach((Context)this);
                mensagemPermissao.show(getSupportFragmentManager(),"primeiravez2");
            }
            else
            {
                String mensagem="Nossa aplicação precisa acessar o telefone para discagem automática. Uma janela de permissão será";
                String titulo="Permissão de acesso a chamadas";
                int codigo=1;
                UIEducacionalPermissao mensagemPermissao=new UIEducacionalPermissao(mensagem,titulo,codigo);
                mensagemPermissao.onAttach((Context)this);
                mensagemPermissao.show(getSupportFragmentManager(),"segundavez2");
                //comentando essa linha pq ta dando erro e é so log:
                //else Log.v("SMD","Outra Vez");
            }
            return false;//added. kd o return true no if?
        }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId()==R.id.anvPerfil)
        {
            Intent intent=new Intent(this,Profile.class);
            intent.putExtra("usuario",user);
            startActivity(intent);
            finish();
        }
        else if(item.getItemId()==R.id.anvMudar)
        {
            Intent intent=new Intent(this,Pick_Contacts.class);
            intent.putExtra("usuario",user);
            startActivity(intent);
            finish();
        }
        return true;
    }

    protected int mostrarListadeContatos()
    {
        //comentando essa linha pra resolver dps:
        //Cursor phones=cr.query(Contacts)
        return 0;//added
    }

    @Override
    public void onDialogPositiveClick(int codigo) {
        //Log.v("SMD","Clicou no OK");
        if(codigo==1)
        {
            String[] permissoes={Manifest.permission.CALL_PHONE};
            ActivityCompat.requestPermissions(this,permissoes,1212);
        }
    }

    public void onRequestPermissionResult(int requestCode,String[] permissions,int[] grantResults)
    {
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED)Toast.makeText(this,"VALEU",Toast.LENGTH_LONG).show();
        else Toast.makeText(this,"SEU FELA",Toast.LENGTH_LONG).show();
    }
}
