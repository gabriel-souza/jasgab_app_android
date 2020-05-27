package br.com.jasgab.jasgab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;

import java.util.Objects;

import br.com.jasgab.jasgab.fragment.ComecarFragment;

public class FirstAccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_access);
        Objects.requireNonNull(getSupportActionBar()).hide();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_right);
        fragmentTransaction.add(R.id.content_first_access, new ComecarFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
