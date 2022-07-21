package com.example.cikcanmiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class fragmentProfil extends Fragment {
    TextView txtProfilKullaniciAdi,lblKacGrup,txtProfilEmail,txtDurumProfil;
    Button btnAyarlar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_profil,container,false);
        txtDurumProfil = (TextView) rootView.findViewById(R.id.txtDurumProfil);
        lblKacGrup = (TextView) rootView.findViewById(R.id.lblKacGrup);
        txtProfilEmail = (TextView) rootView.findViewById(R.id.txtProfilEmail);
        txtProfilKullaniciAdi = (TextView) rootView.findViewById(R.id.txtProfilKullaniciAdi);
        btnAyarlar = (Button) rootView.findViewById(R.id.btnAyarlar);
        btnAyarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentSettings fragmentSettings = new fragmentSettings();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.FragmentContainer,fragmentSettings,"group");
                transaction.commit();
            }
        });
        setUserProp();
        return rootView;
    }
    private void setUserProp()
    {
        txtDurumProfil.setText(User.userCurrent.durum);
        txtProfilEmail.setText(User.userCurrent.mail);
        txtProfilKullaniciAdi.setText(User.userCurrent.username);
        int tmp = User.userCurrent.groups.size();
        String tmp2 = Integer.toString(tmp);
        lblKacGrup.setText(tmp2);
    }
}
