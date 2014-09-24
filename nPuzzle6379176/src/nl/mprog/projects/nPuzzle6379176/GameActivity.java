package nl.mprog.projects.nPuzzle6379176;

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
import android.widget.TextView;

public class GameActivity extends Activity {
	
	private int reqHeight = 1000, reqWidth = 1000;
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
	public int moeilijkheid = 0, imgnr = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_game);
    	
    	moeilijkheid = getIntent().getExtras().getInt("moeilijkheid");
    	imgnr = getIntent().getExtras().getInt("imagebm");
    	
    	//vraag img op met imgnr
    	LinearLayout gallery1 = (LinearLayout)findViewById(R.id.gallery1);
    	gallery1.addView(createBm(imageIds[imgnr]));
    	//maak bitmap van img
    	
    	
    	//aantal tiles
    	int dimensieTiles = moeilijkheid + 3;
    	int aantalTiles = dimensieTiles * dimensieTiles;
    	
    	//maak tiles
    	
    	//vul tiles met stukken bitmap
    	
    	//debug moeilijkheid
    	TextView tv3 = (TextView) findViewById(R.id.testMoeilijkheid);
		tv3.setText("moeilijkheid: " + moeilijkheid);
		//debug imgnr
    	TextView tv4 = (TextView) findViewById(R.id.testImgnr);
		tv4.setText("imgnr: " + imgnr);
    }
	
	
	View createBm(Integer imageId)
	{
		//maak bitmap
		Bitmap bm = null;
		final BitmapFactory.Options options = new BitmapFactory.Options();
		//vul bitmap
		bm = BitmapFactory.decodeResource(getResources(), imageId);
		//resize bm, calc subsample
		int subsample = resizeBitmap(bm);
		
	    options.inSampleSize = subsample;
	    //maak bitmap met subsample
	    bm = BitmapFactory.decodeResource(getResources(), imageId, options); 

	    //layout
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new LayoutParams(reqHeight+50, reqWidth+50));
        layout.setGravity(Gravity.CENTER);
        
        //set img view voor bitmap
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new LayoutParams(reqHeight, reqWidth));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bm);
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
	
	
	/*geef menu popup
	public void toMenu(View view){
		//stuur id van img mee en moeilijkheid
		Intent intent = new Intent(this, MenuActivity.class);
		intent.putExtra("imagebm", imgnr);
		intent.putExtra("moeilijkheid", progressChanged);
		startActivity(intent);
	}*/
	
	//restart deze activity met moeilijkheid etc
	public void toRestart(View view){
		//stuur id van img mee en moeilijkheid
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("imagebm", imgnr);
		intent.putExtra("moeilijkheid", moeilijkheid);
		startActivity(intent);
		finish();
	}
}
