package nl.mprog.projects.nPuzzle6379176;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.view.MenuItem;

@SuppressLint("NewApi") public class GameActivity extends Activity
{

    private int reqHeight = 0, reqWidth = 0;
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

        //krijg info van intents
        moeilijkheid = getIntent().getExtras().getInt("moeilijkheid");
        imgnr = getIntent().getExtras().getInt("imagebm");

        reqWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
        reqHeight= (int) (getResources().getDisplayMetrics().widthPixels * 0.8);

        //maak bitmap van img     
        Bitmap volimgbm = BitmapFactory.decodeResource(getResources(), imageIds[imgnr]);

        //scale bitmap met goede aspect
        Bitmap scaledimgbm = scaleBm(volimgbm);
        //klaar met volimgbm, recycle
        //volimgbm.recycle();

        //aantal tiles
        int dimensieTiles = moeilijkheid + 3;
        int aantalTiles = dimensieTiles * dimensieTiles;


        int pt1 = (int)(scaledimgbm.getWidth()/dimensieTiles);
        int pt2 = (int)(scaledimgbm.getHeight()/dimensieTiles);

        if(moeilijkheid == 0)
        {
            maakStuks3(scaledimgbm);
        }
        else if(moeilijkheid == 1)
        {
            maakStuks4(scaledimgbm);
        }else if(moeilijkheid == 2)
        {
            maakStuks5(scaledimgbm);
        }

        //menubutton
        maakMenu();

        //debug moeilijkheid
        TextView tv3 = (TextView) findViewById(R.id.testMoeilijkheid);
        tv3.setText("moeilijkheid: " + moeilijkheid);
        //debug imgnr
        TextView tv4 = (TextView) findViewById(R.id.testImgnr);
        tv4.setText("imgnr: " + (imgnr+1));
    }


    Bitmap scaleBm(Bitmap volimgbm)
    {
        int volimgbmWidth = volimgbm.getWidth(), volimgbmHeight = volimgbm.getHeight();
        float scale;
        //scale x en y !met aspect behouden!
        float scaledimgbmx = (float) reqWidth / volimgbmWidth;
        float scaledimgbmy = (float) reqHeight / volimgbmHeight;

        //kijk welke dimensie het grootst is
        if(scaledimgbmx > scaledimgbmy)
        {
            scale = scaledimgbmx;
        } else
        {
            scale = scaledimgbmy;
        }

        //scale
        float scaledWidth = scale * volimgbmWidth;
        float scaledHeight = scale * volimgbmHeight;

        //pak linksboven punt in het midden  
        float puntx = (reqWidth - scaledWidth) / 2;
        float punty = (reqHeight - scaledHeight) / 2;

        //snijd vierkant uit te grote gescalede bm
        RectF vierkant = new RectF(puntx, punty, puntx + scaledWidth, punty + scaledHeight);

        //maak bm van vierkant
        Bitmap scaledbm = Bitmap.createBitmap(reqWidth, reqHeight, volimgbm.getConfig());
        Canvas canvas = new Canvas(scaledbm);
        canvas.drawBitmap(volimgbm, null, vierkant, null);

        return scaledbm;

    }



    void maakStuks3(Bitmap scaledimgbm)
    {
        //aantal tiles
        int dimensieTiles = moeilijkheid + 3;
        int aantalTiles = dimensieTiles * dimensieTiles;


        int pt1 = (int)(scaledimgbm.getWidth()/dimensieTiles);
        int pt2 = (int)(scaledimgbm.getHeight()/dimensieTiles);
        //bitmaps stukken boven
        Bitmap stuk1 = Bitmap.createBitmap(scaledimgbm, 1,1,pt1,pt2);
        Bitmap stuk2 = Bitmap.createBitmap(scaledimgbm, pt1,1,pt1,pt2);
        Bitmap stuk3 = Bitmap.createBitmap(scaledimgbm, pt1*2,1,pt1,pt2);

        //bitmaps stukken midden
        Bitmap stuk122 = Bitmap.createBitmap(scaledimgbm, 1,pt2,pt1,pt2);
        Bitmap stuk222 = Bitmap.createBitmap(scaledimgbm, pt1,pt2,pt1,pt2);
        Bitmap stuk322 = Bitmap.createBitmap(scaledimgbm, pt1*2,pt2,pt1,pt2);


        //bitmaps stukken onder
        Bitmap stuk133 = Bitmap.createBitmap(scaledimgbm, 1,pt2+pt2,pt1,pt2);
        Bitmap stuk233 = Bitmap.createBitmap(scaledimgbm, pt1,pt2+pt2,pt1,pt2);
        Bitmap stuk333 = Bitmap.createBitmap(scaledimgbm, pt1*2,pt2+pt2,pt1,pt2);

        //klaar met volledige bm, recycle
        //scaledimgbm.recycle();

        ImageView vImg11;
        vImg11 = (ImageView) findViewById(R.id.h11);
        vImg11.setImageBitmap(stuk1);
        ImageView vImg12;
        vImg12 = (ImageView) findViewById(R.id.h12);
        vImg12.setImageBitmap(stuk2);
        ImageView vImg13;
        vImg13 = (ImageView) findViewById(R.id.h13);
        vImg13.setImageBitmap(stuk3);

        ImageView vImg21;
        vImg21 = (ImageView) findViewById(R.id.h21);
        vImg21.setImageBitmap(stuk122);
        ImageView vImg22;
        vImg22 = (ImageView) findViewById(R.id.h22);
        vImg22.setImageBitmap(stuk222);
        ImageView vImg23;
        vImg23 = (ImageView) findViewById(R.id.h23);
        vImg23.setImageBitmap(stuk322);

        ImageView vImg31;
        vImg31 = (ImageView) findViewById(R.id.h31);
        vImg31.setImageBitmap(stuk133);
        ImageView vImg32;
        vImg32 = (ImageView) findViewById(R.id.h32);
        vImg32.setImageBitmap(stuk233);
        ImageView vImg33;
        vImg33 = (ImageView) findViewById(R.id.h33);
        vImg33.setImageBitmap(stuk333);
    }



    void maakStuks4(Bitmap scaledimgbm)
    {
        //aantal tiles
        int dimensieTiles = moeilijkheid + 3;
        int aantalTiles = dimensieTiles * dimensieTiles;


        int pt1 = (int)(scaledimgbm.getWidth()/dimensieTiles);
        int pt2 = (int)(scaledimgbm.getHeight()/dimensieTiles);
        //bitmaps stukken boven
        Bitmap stuk1 = Bitmap.createBitmap(scaledimgbm, 1,1,pt1,pt2);
        Bitmap stuk2 = Bitmap.createBitmap(scaledimgbm, pt1,1,pt1,pt2);
        Bitmap stuk3 = Bitmap.createBitmap(scaledimgbm, pt1*2,1,pt1,pt2);
        Bitmap stuk4 = Bitmap.createBitmap(scaledimgbm, pt1*3,1,pt1,pt2);

        //bitmaps stukken midden
        Bitmap stuk122 = Bitmap.createBitmap(scaledimgbm, 1,pt2,pt1,pt2);
        Bitmap stuk222 = Bitmap.createBitmap(scaledimgbm, pt1,pt2,pt1,pt2);
        Bitmap stuk322 = Bitmap.createBitmap(scaledimgbm, pt1*2,pt2,pt1,pt2);
        Bitmap stuk422 = Bitmap.createBitmap(scaledimgbm, pt1*3,pt2,pt1,pt2);

        //bitmaps stukken midden2
        Bitmap stuk133 = Bitmap.createBitmap(scaledimgbm, 1,pt2*2,pt1,pt2);
        Bitmap stuk233 = Bitmap.createBitmap(scaledimgbm, pt1,pt2*2,pt1,pt2);
        Bitmap stuk333 = Bitmap.createBitmap(scaledimgbm, pt1*2,pt2*2,pt1,pt2);
        Bitmap stuk433 = Bitmap.createBitmap(scaledimgbm, pt1*3,pt2*2,pt1,pt2);

        //bitmaps stukken onder
        Bitmap stuk144 = Bitmap.createBitmap(scaledimgbm, 1,pt2*3,pt1,pt2);
        Bitmap stuk244 = Bitmap.createBitmap(scaledimgbm, pt1,pt2*3,pt1,pt2);
        Bitmap stuk344 = Bitmap.createBitmap(scaledimgbm, pt1*2,pt2*3,pt1,pt2);
        Bitmap stuk444 = Bitmap.createBitmap(scaledimgbm, pt1*3,pt2*3,pt1,pt2);

        //klaar met volledige bm, recycle
        //scaledimgbm.recycle();

        ImageView vImg11;
        vImg11 = (ImageView) findViewById(R.id.h11);
        vImg11.setImageBitmap(stuk1);
        ImageView vImg12;
        vImg12 = (ImageView) findViewById(R.id.h12);
        vImg12.setImageBitmap(stuk2);
        ImageView vImg13;
        vImg13 = (ImageView) findViewById(R.id.h13);
        vImg13.setImageBitmap(stuk3);
        ImageView vImg14;
        vImg14 = (ImageView) findViewById(R.id.h14);
        vImg14.setImageBitmap(stuk4);

        ImageView vImg21;
        vImg21 = (ImageView) findViewById(R.id.h21);
        vImg21.setImageBitmap(stuk122);
        ImageView vImg22;
        vImg22 = (ImageView) findViewById(R.id.h22);
        vImg22.setImageBitmap(stuk222);
        ImageView vImg23;
        vImg23 = (ImageView) findViewById(R.id.h23);
        vImg23.setImageBitmap(stuk322);
        ImageView vImg24;
        vImg24 = (ImageView) findViewById(R.id.h24);
        vImg24.setImageBitmap(stuk422);

        ImageView vImg31;
        vImg31 = (ImageView) findViewById(R.id.h31);
        vImg31.setImageBitmap(stuk133);
        ImageView vImg32;
        vImg32 = (ImageView) findViewById(R.id.h32);
        vImg32.setImageBitmap(stuk233);
        ImageView vImg33;
        vImg33 = (ImageView) findViewById(R.id.h33);
        vImg33.setImageBitmap(stuk333);
        ImageView vImg34;
        vImg34 = (ImageView) findViewById(R.id.h34);
        vImg34.setImageBitmap(stuk433);

        ImageView vImg41;
        vImg41 = (ImageView) findViewById(R.id.h41);
        vImg41.setImageBitmap(stuk144);
        ImageView vImg42;
        vImg42 = (ImageView) findViewById(R.id.h42);
        vImg42.setImageBitmap(stuk244);
        ImageView vImg43;
        vImg43 = (ImageView) findViewById(R.id.h43);
        vImg43.setImageBitmap(stuk344);
        ImageView vImg44;
        vImg44 = (ImageView) findViewById(R.id.h44);
        vImg44.setImageBitmap(stuk444);
    }


    void maakStuks5(Bitmap scaledimgbm)
    {		//aantal tiles
        int dimensieTiles = moeilijkheid + 3;
        int aantalTiles = dimensieTiles * dimensieTiles;


        int pt1 = (int)(scaledimgbm.getWidth()/dimensieTiles);
        int pt2 = (int)(scaledimgbm.getHeight()/dimensieTiles);
        //bitmaps stukken boven
        Bitmap stuk1 = Bitmap.createBitmap(scaledimgbm, 1,1,pt1,pt2);
        Bitmap stuk2 = Bitmap.createBitmap(scaledimgbm, pt1,1,pt1,pt2);
        Bitmap stuk3 = Bitmap.createBitmap(scaledimgbm, pt1*2,1,pt1,pt2);
        Bitmap stuk4 = Bitmap.createBitmap(scaledimgbm, pt1*3,1,pt1,pt2);
        Bitmap stuk5 = Bitmap.createBitmap(scaledimgbm, pt1*4,1,pt1,pt2);

        //bitmaps stukken midden
        Bitmap stuk122 = Bitmap.createBitmap(scaledimgbm, 1,pt2,pt1,pt2);
        Bitmap stuk222 = Bitmap.createBitmap(scaledimgbm, pt1,pt2,pt1,pt2);
        Bitmap stuk322 = Bitmap.createBitmap(scaledimgbm, pt1*2,pt2,pt1,pt2);
        Bitmap stuk422 = Bitmap.createBitmap(scaledimgbm, pt1*3,pt2,pt1,pt2);
        Bitmap stuk522 = Bitmap.createBitmap(scaledimgbm, pt1*4,pt2,pt1,pt2);

        //bitmaps stukken midden2
        Bitmap stuk133 = Bitmap.createBitmap(scaledimgbm, 1,pt2*2,pt1,pt2);
        Bitmap stuk233 = Bitmap.createBitmap(scaledimgbm, pt1,pt2*2,pt1,pt2);
        Bitmap stuk333 = Bitmap.createBitmap(scaledimgbm, pt1*2,pt2*2,pt1,pt2);
        Bitmap stuk433 = Bitmap.createBitmap(scaledimgbm, pt1*3,pt2*2,pt1,pt2);
        Bitmap stuk533 = Bitmap.createBitmap(scaledimgbm, pt1*4,pt2*2,pt1,pt2);

        //bitmaps stukken midden3
        Bitmap stuk144 = Bitmap.createBitmap(scaledimgbm, 1,pt2*3,pt1,pt2);
        Bitmap stuk244 = Bitmap.createBitmap(scaledimgbm, pt1,pt2*3,pt1,pt2);
        Bitmap stuk344 = Bitmap.createBitmap(scaledimgbm, pt1*2,pt2*3,pt1,pt2);
        Bitmap stuk444 = Bitmap.createBitmap(scaledimgbm, pt1*3,pt2*3,pt1,pt2);
        Bitmap stuk544 = Bitmap.createBitmap(scaledimgbm, pt1*4,pt2*3,pt1,pt2);

        //bitmaps stukken beneden
        Bitmap stuk155 = Bitmap.createBitmap(scaledimgbm, 1,pt2*4,pt1,pt2);
        Bitmap stuk255 = Bitmap.createBitmap(scaledimgbm, pt1,pt2*4,pt1,pt2);
        Bitmap stuk355 = Bitmap.createBitmap(scaledimgbm, pt1*2,pt2*4,pt1,pt2);
        Bitmap stuk455 = Bitmap.createBitmap(scaledimgbm, pt1*3,pt2*4,pt1,pt2);
        Bitmap stuk555 = Bitmap.createBitmap(scaledimgbm, pt1*4,pt2*4,pt1,pt2);

        //klaar met volledige bm, recycle
        //scaledimgbm.recycle();

        ImageView vImg11;
        vImg11 = (ImageView) findViewById(R.id.h11);
        vImg11.setImageBitmap(stuk1);
        ImageView vImg12;
        vImg12 = (ImageView) findViewById(R.id.h12);
        vImg12.setImageBitmap(stuk2);
        ImageView vImg13;
        vImg13 = (ImageView) findViewById(R.id.h13);
        vImg13.setImageBitmap(stuk3);
        ImageView vImg14;
        vImg14 = (ImageView) findViewById(R.id.h14);
        vImg14.setImageBitmap(stuk4);
        ImageView vImg15;
        vImg15 = (ImageView) findViewById(R.id.h15);
        vImg15.setImageBitmap(stuk5);

        ImageView vImg21;
        vImg21 = (ImageView) findViewById(R.id.h21);
        vImg21.setImageBitmap(stuk122);
        ImageView vImg22;
        vImg22 = (ImageView) findViewById(R.id.h22);
        vImg22.setImageBitmap(stuk222);
        ImageView vImg23;
        vImg23 = (ImageView) findViewById(R.id.h23);
        vImg23.setImageBitmap(stuk322);
        ImageView vImg24;
        vImg24 = (ImageView) findViewById(R.id.h24);
        vImg24.setImageBitmap(stuk422);
        ImageView vImg25;
        vImg25 = (ImageView) findViewById(R.id.h25);
        vImg25.setImageBitmap(stuk522);

        ImageView vImg31;
        vImg31 = (ImageView) findViewById(R.id.h31);
        vImg31.setImageBitmap(stuk133);
        ImageView vImg32;
        vImg32 = (ImageView) findViewById(R.id.h32);
        vImg32.setImageBitmap(stuk233);
        ImageView vImg33;
        vImg33 = (ImageView) findViewById(R.id.h33);
        vImg33.setImageBitmap(stuk333);
        ImageView vImg34;
        vImg34 = (ImageView) findViewById(R.id.h34);
        vImg34.setImageBitmap(stuk433);
        ImageView vImg35;
        vImg35 = (ImageView) findViewById(R.id.h35);
        vImg35.setImageBitmap(stuk533);

        ImageView vImg41;
        vImg41 = (ImageView) findViewById(R.id.h41);
        vImg41.setImageBitmap(stuk144);
        ImageView vImg42;
        vImg42 = (ImageView) findViewById(R.id.h42);
        vImg42.setImageBitmap(stuk244);
        ImageView vImg43;
        vImg43 = (ImageView) findViewById(R.id.h43);
        vImg43.setImageBitmap(stuk344);
        ImageView vImg44;
        vImg44 = (ImageView) findViewById(R.id.h44);
        vImg44.setImageBitmap(stuk444);
        ImageView vImg45;
        vImg45 = (ImageView) findViewById(R.id.h45);
        vImg45.setImageBitmap(stuk544);

        ImageView vImg51;
        vImg51 = (ImageView) findViewById(R.id.h51);
        vImg51.setImageBitmap(stuk155);
        ImageView vImg52;
        vImg52 = (ImageView) findViewById(R.id.h52);
        vImg52.setImageBitmap(stuk255);
        ImageView vImg53;
        vImg53 = (ImageView) findViewById(R.id.h53);
        vImg53.setImageBitmap(stuk355);
        ImageView vImg54;
        vImg54 = (ImageView) findViewById(R.id.h54);
        vImg54.setImageBitmap(stuk455);
        ImageView vImg55;
        vImg55 = (ImageView) findViewById(R.id.h55);
        vImg55.setImageBitmap(stuk555);
    }

    //maak een menu bij de menubutton
    void maakMenu()
    {
        final Button buttonMenu = (Button) findViewById(R.id.buttonMenu);  
        buttonMenu.setOnClickListener(new OnClickListener()
        {  
            @Override  
            public void onClick(View v)
            {  
                PopupMenu popup = new PopupMenu(GameActivity.this, buttonMenu);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu()); 
                popup.show();  
            }  
        });
    }


    public void toStart(MenuItem item)
    {
        //stuur id van img mee en moeilijkheid
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void toMakkelijker(MenuItem item)
    {
        if(moeilijkheid != 0)
        {
            moeilijkheid -= 1;
        }
        //stuur id van img mee en moeilijkheid
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("imagebm", imgnr);
        intent.putExtra("moeilijkheid", moeilijkheid);
        startActivity(intent);
        finish();
    }


    public void toMoeilijker(MenuItem item)
    {
        if(moeilijkheid != 2)
        {
            moeilijkheid += 1;
        }
        //stuur id van img mee en moeilijkheid
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("imagebm", imgnr);
        intent.putExtra("moeilijkheid", moeilijkheid);
        startActivity(intent);
        finish();
    }


    //restart deze activity met moeilijkheid etc
    public void toRestart(View v)
    {
        //stuur id van img mee en moeilijkheid
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("imagebm", imgnr);
        intent.putExtra("moeilijkheid", moeilijkheid);
        startActivity(intent);
        finish();
    }
}
