package br.com.jasgab.jasgab;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Objects;

import br.com.jasgab.jasgab.fragment.FirstAccessFragment;

public class FirstAccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_access);
        Objects.requireNonNull(getSupportActionBar()).hide();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_first_access, new FirstAccessFragment())
                .addToBackStack(null)
                .commit();
    }
}
