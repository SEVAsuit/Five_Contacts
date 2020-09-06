package com.example.fivecontacts.main.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.fivecontacts.R;
import com.example.fivecontacts.main.model.Contato;
import com.example.fivecontacts.main.model.User;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Pick_Contacts extends AppCompatActivity {

    TextView tv;
    EditText edtNome;
    ConstraintLayout constraint_layout;
  //  Button btSalvar;
    ArrayList<Switch> switches;
    ArrayList<Contato> contatos;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contacts);
        tv=findViewById(R.id.MessageIntent);//original:tv=findViewById(R.id.MessageIntent)
        edtNome=findViewById(R.id.edtBusca);
        constraint_layout=findViewById(R.id.constraint_layout);

                //Pegando parâmetros
        Intent quemChamou = this.getIntent();

        if (quemChamou != null)
        {
            Bundle params=quemChamou.getExtras();

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

       /* btSalvar = findViewById(R.id.btSalvar);

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
      */  }//o:   */  });

    //}//o:}
    public void cliquedoSalvar(View v)
    {
        for(int i=0;i<switches.size();i++)
        {
            if(switches.get(i).isChecked())
            {
                user.getContatos().add(contatos.get(i));
            }
        }
        Contato c,k,z;
        c=new Contato();
        c.setNumero("tel:+141414141");
        c.setNome("Peppa");
        k=new Contato();
        k.setNumero("tel:+242414141");
        k.setNome("George");
        z=new Contato();
        z.setNumero("tel:+342414141");
        z.setNome("Richard");
        SharedPreferences salvaContatos=getSharedPreferences("contatos2", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=salvaContatos.edit();
        int j=0;


        try
        {
            ByteArrayOutputStream dt=new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(dt);
            String contatoSerializado;
            for(int i=0;i<switches.size();i++)
            {
                Log.v("PDM","Buscando na switch de numero: "+i);
                Log.v("PDM","O nome dessa switch é: "+switches.get(i).getTextOff());


                if(switches.get(i).isChecked())
                {
                    j++;
                    dt=new ByteArrayOutputStream();
                    oos = new ObjectOutputStream(dt);
                    oos.writeObject(contatos.get(i));
                    contatoSerializado=dt.toString(StandardCharsets.ISO_8859_1.name());
                    editor.putString("contato"+String.valueOf(j),contatoSerializado);
                    Log.v("PDM","Achei um nome: "+contatos.get(i).getNome());
                }
            }
            /*oos.writeObject(c);

            dt=new ByteArrayOutputStream();
            oos = new ObjectOutputStream(dt);
            oos.writeObject(k);
            contatoSerializado=dt.toString(StandardCharsets.ISO_8859_1.name());
            editor.putString("contato2",contatoSerializado);

            dt=new ByteArrayOutputStream();
            oos = new ObjectOutputStream(dt);
            oos.writeObject(z);
            contatoSerializado=dt.toString(StandardCharsets.ISO_8859_1.name());
            editor.putString("contato3",contatoSerializado);*/

        }
        catch(Exception e)
        {
            e.printStackTrace();
            Log.v("PDM","excecao encontrada");
        }
        editor.putInt("numContatos",j);
        Log.v("PDM",String.valueOf(editor.commit()));

        Intent intent = new Intent(Pick_Contacts.this, ListaDeContatos_ListView.class);
        intent.putExtra("usuario",user);
        startActivity(intent);
        Log.v("","");
        finish();

    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.v("PDM","Matando a Activity Pick_Contacts");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("PDM","Matei a Activity Pick_Contacts");
    }

    public void onClickBuscar(View v)
    {
        Log.v("PDM","oi");
      //  if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED) { COMENTADO
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {

            Log.v("PDM", "Pedir permissão");
            String[] s=new String[1];
            s[0]=Manifest.permission.READ_CONTACTS;
            requestPermissions(s,2222);//o:            requestPermissions(new String[]Manifest.permission.READ_CONTACTS); ESSE REQUESTCODE TVZ ESTEJA ERRADO
            return;
        }
        Log.v("PDM","Tenho permissão");
        ContentResolver cr=getContentResolver();
        String consulta=ContactsContract.Contacts.DISPLAY_NAME+" LIKE ?";
        String [] argumentosConsulta={"%"+edtNome.getText()+"%"};
        Cursor cursor=cr.query(ContactsContract.Contacts.CONTENT_URI,null,consulta,argumentosConsulta,null);
        Contato c;
        int i=0;
        switches=new ArrayList<Switch>();
        contatos=new ArrayList<Contato>();
        while(cursor.moveToNext())
        {
            c=new Contato();
            i++;
            int indiceNome=cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
            String contatoNome=cursor.getString(indiceNome);
            Log.v("PDM","Contato "+i+", Nome:"+contatoNome);
            c.setNome(contatoNome);

            int indiceContatoID=cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
            String contactID=cursor.getString(indiceContatoID);

            String consultaPhone=ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = "+contactID;
            Cursor phones=cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,consultaPhone,null,null);
            int j=0;
            while(phones.moveToNext())
            {
                j++;
               //
                String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                c.setNumero("tel:+" + number);
                Log.v("PDM", " Telefone (" + j + " " + number);
                Switch s=new Switch(this);
                String name="switch_"+String.valueOf(i)+"_"+String.valueOf(j);
                int switch_id=getResources().getIdentifier(name,"id",getPackageName());
               // s=(Switch)findViewById(switch_id);
                constraint_layout.addView(s);
               // ConstraintLayout constraintLayout=constraint_layout;
               // ConstraintSet constraintSet = new ConstraintSet();
               // constraintSet.clone(constraint_layout);
                //constraintSet.connect(getResources().getIdentifier("s","id",getPackageName()),ConstraintSet.RIGHT,R.id.button,ConstraintSet.RIGHT,0);
              //  constraintSet.connect(switch_id,ConstraintSet.TOP,R.id.button,ConstraintSet.BOTTOM,(i+j)*50);
               // constraintSet.connect(switch_id,ConstraintSet.BOTTOM,R.id.constraint_layout,ConstraintSet.BOTTOM,0);
              //  constraintSet.connect(switch_id,ConstraintSet.END,R.id.constraint_layout,ConstraintSet.END,0);
              //  constraintSet.connect(switch_id,ConstraintSet.START,R.id.constraint_layout,ConstraintSet.START,0);


              //  constraintSet.applyTo(constraint_layout);
                s.setTextOff(contatoNome);
                s.setX(50);
                s.setY(250+((i+j)*50));
                s.setShowText(true);
                switches.add(s);
                contatos.add(c);
            }
        }
        /*SharedPreferences salvaContatos=getSharedPreferences("contatos2",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=salvaContatos.edit();
        editor.putInt("numContatos",4);
        try {
            ByteArrayOutputStream dt=new ByteArrayOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(dt);
            oos.writeObject(c);
            String contatoSerializado=dt.toString(StandardCharsets.ISO_8859_1.name());
                    editor.putString("contato4",contatoSerializado);
            editor.commit();
            }
        catch (Exception e)
        {
        }*/
        }


    }

