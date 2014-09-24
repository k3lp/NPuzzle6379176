package nl.mprog.projects.nPuzzle6379176;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity 
{
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
	public int progressChanged = 1, imgnr = 0;
	private int reqHeight = 800, reqWidth = 800;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
    	
        //maak gallery
        LinearLayout gallery1 = (LinearLayout)findViewById(R.id.gallery1);
        
        //vul gallery imgs
        for(int i=0;i<9;i++)
        {
        	gallery1.addView(addGallery(imageIds[i], i));
        }
        
        //maak seekbar
        maakSeek((SeekBar) findViewById(R.id.sliderMoeilijkheid));

    }
    
    //gallery
	View addGallery(Integer imageId, final Integer i)
	{
		//maak bitmap
		Bitmap bm = null, bm2 = null;
		final BitmapFactory.Options options = new BitmapFactory.Options();
		//vul bitmap
		bm = BitmapFactory.decodeResource(getResources(), imageId);
		//resize bm, calc subsample
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
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bm2);
        
        //set onclick op afbeelding in gallery
        imageView.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View v)
        	{
        		TextView tv = (TextView) findViewById(R.id.textAfbeeldingNr);
        		tv.setText("Afbeelding: " + i);
        		imgnr = i;
        	}
        });

        layout.addView(imageView);
        return layout;
    }
	
	int resizeBitmap(Bitmap bm2)
	{
		//haal height/width van img
		int bmheight = bm2.getHeight(), bmwidth = bm2.getWidth();
	    int subsample = 1;
	    //bereken subsample
	    if(bmheight > reqHeight || bmwidth > reqWidth)
	    {
	      if(bmwidth > bmheight)
	      {
	      	subsample = Math.round((float)bmheight / (float)reqHeight);   
	      } else 
	      {
	      	subsample = Math.round((float)bmwidth / (float)reqWidth);   
	      }   
	    }
		return subsample;
	}
	
	//seekbar
	void maakSeek(SeekBar moeilijk1)
	{
		moeilijk1.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				progressChanged = progress;
			}

			public void onStartTrackingTouch(SeekBar seekBar)
			{
			}

			public void onStopTrackingTouch(SeekBar seekBar)
			{
        		TextView tv2 = (TextView) findViewById(R.id.textMoeilijkheid);
        		if(progressChanged == 0)
        		{
            		tv2.setText("Moeilijkheid: Makkelijk");
        		}else if(progressChanged == 1)
        		{
            		tv2.setText("Moeilijkheid: Normaal");
        		}
        		else
        		{
            		tv2.setText("Moeilijkheid: Moeilijk");
        		}

			}
		});
	}
	
	//naar Game
	public void toGame(View view){
		//stuur id van img mee en moeilijkheid
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("imagebm", imgnr);
		intent.putExtra("moeilijkheid", progressChanged);
		startActivity(intent);
	}
}