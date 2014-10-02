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
    public static final String Difficulty = "difficulty";
    public static final String ImageNumber = "imagenumber";
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
            R.drawable.puzzle_8,
            R.drawable.puzzle_9
        };
    public int difficulty = 1, imagenumber = 0;
    public int reqHeight = 0, reqWidth = 0;


    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //maak gallery
        LinearLayout gallery1 = (LinearLayout)findViewById(R.id.gallery1);
        double scaleDisplayMetrics = 0.8;
        reqWidth = (int) (getResources().getDisplayMetrics().widthPixels * scaleDisplayMetrics);
        reqHeight= (int) (getResources().getDisplayMetrics().widthPixels * scaleDisplayMetrics);
        //vul gallery met imgs
        for(int i=0;i<imageIds.length;i++)
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
        int state = settings.getInt(State,0);
        int stateDifficulty = settings.getInt(Difficulty, 0);
        int stateImgnr = settings.getInt(ImageNumber, 0);

        if(state == 1)
        { 
            Intent intent = new Intent(this, GameActivity.class);
            //stuur id van img mee en moeilijkheid
            intent.putExtra("imagebm", stateImgnr);
            intent.putExtra("difficulty", stateDifficulty);
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
        try
        {
            bm = BitmapFactory.decodeResource(getResources(), imageId);
        }
        catch(OutOfMemoryError e)
        {
            options.inSampleSize = 2;
            try
            {
                bm = BitmapFactory.decodeResource(getResources(), imageId, options); 
            }
            catch(OutOfMemoryError e2)
            {
                options.inSampleSize *= 2;
                try{
                    bm = BitmapFactory.decodeResource(getResources(), imageId, options);   
                }
                catch(OutOfMemoryError e3)
                {
                    options.inSampleSize *= 2;
                    bm = BitmapFactory.decodeResource(getResources(), imageId, options);   
                }

            }
            //Use BitmapFactory.Options with an inSampleSize >= 1, and do inSampleSize *= 2 before each retry. 
        }
        //maak bitmap kleiner anders lag
        int subsample = resizeBitmap(bm);
        bm.recycle();
        options.inSampleSize = subsample;
        
        //maak bitmap met subsample
        bm2 = BitmapFactory.decodeResource(getResources(), imageId, options); 

        //layout
        int padding = 50;
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new LayoutParams(reqHeight+padding, reqWidth+padding));
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
                imagenumber = i;
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

    
    
    //initialiseer de seekbar voor difficulty
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
        intent.putExtra("imagebm", imagenumber);
        intent.putExtra("difficulty", difficulty);
        intent.putExtra("restart", 0);
        startActivity(intent);
        finish();
    }
}