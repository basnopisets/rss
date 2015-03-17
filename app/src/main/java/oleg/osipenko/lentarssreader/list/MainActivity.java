package oleg.osipenko.lentarssreader.list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import oleg.osipenko.lentarssreader.R;

public class MainActivity extends ActionBarActivity{

    public static final String LOG_TAG = "rss-reader";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment listFragment = fragmentManager.findFragmentById(R.id.frame_main);
        if (null == listFragment) {
            listFragment = new ListFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.frame_main, listFragment)
                    .commit();
        }
    }
}
