package nl.mprog.projects.nPuzzle6379176;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MainActivity extends Activity 
{
    public static final String State = "state";
    public static final String Moeilijkheid = "moeilijkheid"; 
    public static final String ImageNr = "imagenr";
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences settings;

    //ids van puzzle opslaan
    public Integer[] imageIds =
        {
            R.drawable.puzzle_0,
            R.drawable.puzzle_1,
            R.drawable.puzzle_2,
            R.drawable.puzzle_3,
            R.drawable.puzzle_4,
            R.drawable.puzzle_5,
            R.drawable.puzzle_6,
            R.drawable.puzzle_7,
            R.drawable.puzzle_8
            //R.drawable.puzzle_9
        };
    public int difficulty = 1, imgnr = 0;
    public int reqHeight = 800, reqWidth = 800;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //maak gallery
        LinearLayout gallery1 = (LinearLayout)findViewById(R.id.gallery1);

        //vul gallery met imgs
        for(int i=0;i<9;i++)
        {
            gallery1.addView(addGallery(imageIds[i], i));
        }

        //maak seekbar
        initSeek((SeekBar) findViewById(R.id.sliderMoeilijkheid));


    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.v("chingchong", "+ ON RESUME MAIN +");

        settings = getSharedPreferences(MyPREFERENCES, 0);

        // Necessary to clear first if we save preferences onPause. 
        int test = settings.getInt(State,0);
        int stateMoeilijkheid = settings.getInt(Moeilijkheid, 0);
        int stateImgnr = settings.getInt(ImageNr, 0);
        System.out.println(test);

        if(test == 1)
        { 
            Intent intent = new Intent(this, GameActivity.class);
            //stuur id van img mee en moeilijkheid
            intent.putExtra("imagebm", stateImgnr);
            intent.putExtra("moeilijkheid", stateMoeilijkheid);
            startActivity(intent);
            finish();
        }


    }

    //gallery
    public View addGallery(Integer imageId, final Integer i)
    {
        //maak bitmap
        Bitmap bm = null, bm2 = null;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        //vul bitmap
        bm = BitmapFactory.decodeResource(getResources(), imageId);
        //maak bitmap kleiner anders lag
        int subsample = resizeBitmap(bm);
        options.inSampleSize = subsample;
        //maak bitmap met subsample
        bm2 = BitmapFactory.decodeResource(getResources(), imageId, options); 

        //layout
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new LayoutParams(reqHeight+50, reqWidth+50));
        layout.setGravity(Gravity.CENTER);

        //set img view voor bitmap
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new LayoutParams(reqHeight, reqWidth));
        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bm2);

        //set onclick op afbeelding in gallery
        imageView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView tv = (TextView) findViewById(R.id.textAfbeeldingNr);
                tv.setText("Gekozen Afbeelding: " + (i+1));
                imgnr = i;
            }
        });

        layout.addView(imageView);
        return layout;
    }


    public int resizeBitmap(Bitmap bm)
    {
        //haal height/width van img
        int bmheight = bm.getHeight(), bmwidth = bm.getWidth();
        int subsample = 1;
        //bereken subsample
        if(bmheight > reqHeight || bmwidth > reqWidth)
        {
            if(bmwidth > bmheight)
            {
                subsample = (int) (bmheight/reqHeight);   
            } else 
            {
                subsample = (int) (bmwidth/reqWidth);   
            }   
        }
        return subsample;
    }

    //initialiseer de seekbar voor moeilijkheid
    public void initSeek(SeekBar seekbar)
    {
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                difficulty = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            public void onStopTrackingTouch(SeekBar seekBar)
            {
                TextView tv2 = (TextView) findViewById(R.id.textMoeilijkheid);
                if(difficulty == 0)
                {
                    tv2.setText("Moeilijkheid: Makkelijk");
                }else if(difficulty == 1)
                {
                    tv2.setText("Moeilijkheid: Normaal");
                }else
                {
                    tv2.setText("Moeilijkheid: Moeilijk");
                }
            }
        });
    }

    //naar Game
    public void toGame(View view)
    {
        Intent intent = new Intent(this, GameActivity.class);
        //stuur id van img mee en moeilijkheid
        intent.putExtra("imagebm", imgnr);
        intent.putExtra("moeilijkheid", difficulty);
        intent.putExtra("restart", 0);
        startActivity(intent);
        finish();
    }
}