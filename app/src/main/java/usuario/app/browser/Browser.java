package usuario.app.browser;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Browser extends AppCompatActivity {
    TextView barLog,wait;
    ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        wait = (TextView) findViewById(R.id.textView);
        wait.setVisibility(View.INVISIBLE);
        barLog = (TextView) findViewById(R.id.barLog);
        barLog.setVisibility(View.INVISIBLE);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.INVISIBLE);
        bar.setMax(100);

        TextView ctv = (TextView)findViewById(R.id.textView);
        Typeface fonte = Typeface.createFromAsset(getAssets(),"quake.ttf");
        ctv.setTypeface(fonte);

        //System.out.println("Start");

        Async async = new Async(Browser.this,bar,barLog,wait);
        async.execute();
    }

    @Override
    public void onBackPressed(){
        //Metodo vazio para anular botao de votlar
    }
}