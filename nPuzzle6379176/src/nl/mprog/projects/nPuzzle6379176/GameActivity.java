package nl.mprog.projects.nPuzzle6379176;

import java.util.Random;
import java.util.StringTokenizer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.view.MenuItem;

@SuppressLint("NewApi") public class GameActivity extends Activity
{
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Difficulty = "difficulty"; 
    public static final String ImageNumber = "imagenumber";
    public static final String State = "state";
    public static final String Array = "array";

    SharedPreferences settings;

    public int reqHeight = 0, reqWidth = 0;

    public int[] imageIds =
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

    public ImageView emptyTile;
    public int emptyTileRow = 0, emptyTileColumn = 0;
    public int state = 0;
    public int restart = 0;
    public int afterTimer = 0;
    public int[][] stateTiles  = new int[2][50];

    public int[][] tileIds =
        {
            {R.id.h11, R.id.h12, R.id.h13, R.id.h14, R.id.h15},
            {R.id.h21, R.id.h22, R.id.h23, R.id.h24, R.id.h25},
            {R.id.h31, R.id.h32, R.id.h33, R.id.h34, R.id.h35},
            {R.id.h41, R.id.h42, R.id.h43, R.id.h44, R.id.h45},
            {R.id.h51, R.id.h52, R.id.h53, R.id.h54, R.id.h55}
        };


    public int difficulty = 0, imagenumber = 0, dimensionTiles = 0;

    
    
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //krijg info van intents
        difficulty = getIntent().getExtras().getInt("difficulty");
        imagenumber = getIntent().getExtras().getInt("imagebm");
        restart = getIntent().getExtras().getInt("restart");

        double scaleDisplayMetrics = 0.8;
        reqWidth = (int) (getResources().getDisplayMetrics().widthPixels * scaleDisplayMetrics);
        reqHeight= (int) (getResources().getDisplayMetrics().widthPixels * scaleDisplayMetrics);

        dimensionTiles = difficulty + 3;
        Bitmap volimgbm = null;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        //maak bitmap van img     
        try
        {
            volimgbm = BitmapFactory.decodeResource(getResources(), imageIds[imagenumber]);
        }
        catch(OutOfMemoryError e)
        {
            options.inSampleSize = 1;
            try
            {
                volimgbm = BitmapFactory.decodeResource(getResources(), imageIds[imagenumber], options); 
            }
            catch(OutOfMemoryError e2)
            {
                options.inSampleSize *= 2;
                try
                {
                    volimgbm = BitmapFactory.decodeResource(getResources(), imageIds[imagenumber], options);   
                }
                catch(OutOfMemoryError e3)
                {
                        options.inSampleSize *= 2;
                        volimgbm = BitmapFactory.decodeResource(getResources(), imageIds[imagenumber], options);   
                }
            }
        }
        
        //scale bitmap met goede aspect
        Bitmap scaledimgbm = scaleBm(volimgbm);

        //menubutton
        maakMenu();

        settings = getSharedPreferences(MyPREFERENCES, 0);
        state = settings.getInt(State,0);

        if(state == 1)
        {
            //stateTiles vullen met tiles van eerdere state
            String savedString = settings.getString(Array, "");
            StringTokenizer st = new StringTokenizer(savedString, ",");
            if(savedString != null && !savedString.isEmpty())
            {
                for (int j = 0; j < 50; j++)
                {
                    stateTiles[1][j] = Integer.parseInt(st.nextToken());
                    stateTiles[0][j] = Integer.parseInt(st.nextToken());
                }
            }
        }
        //maak tiles
        createBitmapPieces(scaledimgbm, difficulty);
        afterTimer = 0;
        settings = getSharedPreferences(MyPREFERENCES, 0);
        state = settings.getInt(State,0);
        if(state != 1)
        {
            emptyTile = (ImageView) findViewById(tileIds[0][0]);
            //laat tiles even zien
            Runnable r = new Runnable()
            {
                @Override
                public void run()
                {
                    hussleTiles();
                    afterTimer = 1;
                }
            };
            Handler h = new Handler();
            h.postDelayed(r, 3000);
        }
        else
        {
            afterTimer = 1;
        }
    }


    

    @Override
    public void onPause()
    {
        super.onPause();
        settings = getSharedPreferences(MyPREFERENCES, 0);
        Editor editor = settings.edit();
        editor.clear();
        if(restart != 1)
        {
            editor.putInt(Difficulty, difficulty);
            editor.putInt(ImageNumber, imagenumber);
            editor.putInt(State, 1);

            String savedString = "";

            StringBuilder str = new StringBuilder();

            for (int j = 0; j < 50; j++)
            {
                str.append(stateTiles[1][j]).append(",");
                str.append(stateTiles[0][j]).append(",");
                savedString = str.toString();
            }
            editor.putString(Array, savedString);
            editor.commit();
        }
    }




    public Bitmap scaleBm(Bitmap volimgbm)
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

    
    

    public void hussleTiles()
    {
        Random r = new Random();
        int count = 0, randj = 0, randi = 0;
        while(count < 1000)
        {
            randj = r.nextInt(dimensionTiles);
            randi = r.nextInt(dimensionTiles);
            ImageView v;
            v = (ImageView) findViewById(tileIds[randi][randj]);
            onClickTileMove(v);   
            count++;
        }
    }

    
    
    
    public void createBitmapPieces(Bitmap scaledimgbm, final Integer difficulty)
    {
        int pt1 = (int)(scaledimgbm.getWidth()/dimensionTiles);
        int pt2 = (int)(scaledimgbm.getHeight()/dimensionTiles);

        if(difficulty == 0)
        {
            final Bitmap [][] bitmapTiles =
                {
                //bitmap stukken boven
                {Bitmap.createBitmap(scaledimgbm, 1,1,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,1,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,1,pt1,pt2)},
                //bitmaps stukken midden
                {Bitmap.createBitmap(scaledimgbm, 1,pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,pt2,pt1,pt2)},
                //bitmaps stukken onder
                {Bitmap.createBitmap(scaledimgbm, 1,pt2+pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,pt2+pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,pt2+pt2,pt1,pt2)}
                };
            createTiles(bitmapTiles);
        }
        else if(difficulty == 1)
        {
            
            Bitmap [][] bitmapTiles =
                {
                //bitmap stukken boven
                {Bitmap.createBitmap(scaledimgbm, 1,1,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,1,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,1,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*3,1,pt1,pt2)},
                //bitmaps stukken midden
                {Bitmap.createBitmap(scaledimgbm, 1,pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*3,pt2,pt1,pt2)},
                //bitmaps stukken onder
                {Bitmap.createBitmap(scaledimgbm, 1,pt2+pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,pt2+pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,pt2+pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*3,pt2*2,pt1,pt2)},
                
                {Bitmap.createBitmap(scaledimgbm, 1,pt2*3,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,pt2*3,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,pt2*3,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*3,pt2*3,pt1,pt2)}  
                };
            createTiles(bitmapTiles);
        }
        else if(difficulty == 2)
        {
            
            Bitmap [][] bitmapTiles =
                {
                {Bitmap.createBitmap(scaledimgbm, 1,1,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,1,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,1,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*3,1,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*4,1,pt1,pt2)},

                {Bitmap.createBitmap(scaledimgbm, 1,pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*3,pt2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*4,pt2,pt1,pt2)},

                {Bitmap.createBitmap(scaledimgbm, 1,pt2*2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,pt2*2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,pt2*2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*3,pt2*2,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*4,pt2*2,pt1,pt2)},

                {Bitmap.createBitmap(scaledimgbm, 1,pt2*3,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,pt2*3,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,pt2*3,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*3,pt2*3,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*4,pt2*3,pt1,pt2)},

                {Bitmap.createBitmap(scaledimgbm, 1,pt2*4,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1,pt2*4,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*2,pt2*4,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*3,pt2*4,pt1,pt2),
                Bitmap.createBitmap(scaledimgbm, pt1*4,pt2*4,pt1,pt2)}
                };
            createTiles(bitmapTiles);
        }
        
    }

    
    
    
    public void createTiles(Bitmap[][] bitmap)
    {
        final Bitmap[][] bitmapTiles = bitmap;
        int statesCount = 0;
        int stateX = 0;
        int stateY = 0;
        for(int i = 0; i < dimensionTiles; i++)
        {
            for(int j = 0; j < dimensionTiles; j++)
            {
                ImageView imageTile;
                imageTile = (ImageView) findViewById(tileIds[i][j]);
                if(state == 0)
                {
                    if(i == 0 && j == 0)
                    {
                        imageTile.setImageBitmap(bitmapTiles[i][j]);
                        imageTile.setTag(bitmapTiles[i][j]);  
                        imageTile.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        imageTile.setImageBitmap(bitmapTiles[i][j]);
                        imageTile.setTag(bitmapTiles[i][j]);  
                    }
                }
                else if(state == 1)
                {
                    stateY = stateTiles[1][statesCount];
                    stateX = stateTiles[0][statesCount];
                    if(stateX < 0)
                    {
                        imageTile.setImageBitmap(bitmapTiles[stateX+6][stateY+6]);
                        imageTile.setTag(bitmapTiles[stateX+6][stateY+6]);
                        emptyTile = (ImageView) findViewById(tileIds[i][j]);
                        emptyTileRow = i;
                        emptyTileColumn = j;
                        imageTile.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        imageTile.setImageBitmap(bitmapTiles[stateX][stateY]);
                        imageTile.setTag(bitmapTiles[stateX][stateY]);
                    }

                    statesCount++;
                }
                imageTile.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if(afterTimer == 1)
                        {
                            onClickTileMove(v);
                            checkEndGame(bitmapTiles);
                        }
                    }
                });
            }
        }
    }
    
    
    
    
    public void onClickTileMove(View v)
    {
        int x = -1, y= -1;
        //als het niet de "lege" tile is
        if(v.getVisibility() == View.VISIBLE)
        {
            //loop over tile ids voor coordinaten geklikte view
            for(int i = 0; i < dimensionTiles; i++)
            {
                for(int j = 0; j < dimensionTiles; j++)
                {
                    if(v.getId()==tileIds[i][j])
                    {
                        x = i;
                        y = j;
                    }
                }
            }
            //kijk of lege tile eromheen is
            if(x-1 == emptyTileRow && y == emptyTileColumn ||
                    x+1 == emptyTileRow && y == emptyTileColumn ||
                    x == emptyTileRow && y-1 == emptyTileColumn ||
                    x == emptyTileRow && y+1 == emptyTileColumn)
            {
                //wissel views om

                ImageView imageClicked = (ImageView) findViewById(tileIds[x][y]);
                Bitmap bitmap = ((BitmapDrawable)imageClicked.getDrawable()).getBitmap();
                emptyTile.setImageBitmap(bitmap);
                emptyTile.setVisibility(View.VISIBLE);
                imageClicked.setVisibility(View.INVISIBLE);
                emptyTile = imageClicked;
                emptyTileRow = x;
                emptyTileColumn = y;
            }
        }
    }


    
    
    public void checkEndGame(Bitmap [][] bitmapTiles)
    {
        //staan alle bitmaps in volgorde
        //kijk naar elke tileID welke bitmap erin staat
        //vergelijk deze met bitmapTiles
        //als gelijk dan is alles in volgorde en end game
        //state opslaan met bitmapTiles id
        //
        int aantalTiles = dimensionTiles*dimensionTiles;
        int count = 0;
        int tileCount = 0;
        for(int i = 0; i < dimensionTiles; i++)
        {
            for(int j = 0; j < dimensionTiles; j++)
            {
                ImageView viewChecked = (ImageView) findViewById(tileIds[i][j]);
                Bitmap bitmapView = ((BitmapDrawable)viewChecked.getDrawable()).getBitmap();
                Bitmap bitmapOriginalView = bitmapTiles[i][j];

                if(bitmapView.sameAs(bitmapOriginalView))
                {
                    count++;
                }
                //loop voor state
                for(int k = 0; k < dimensionTiles; k++)
                {
                    for(int l = 0; l < dimensionTiles; l++)
                    {
                        bitmapOriginalView = bitmapTiles[k][l];
                        if(bitmapView.sameAs(bitmapOriginalView))
                        {
                            stateTiles[0][tileCount]= k;
                            stateTiles[1][tileCount] = l;
                        }
                        //als het de lege tile is
                        if(viewChecked.getVisibility() == View.INVISIBLE)
                        {
                            stateTiles[0][tileCount]= k-6;
                            stateTiles[1][tileCount] = l-6;
                        }
                    }
                }
                tileCount++;
            }
        }
        if(count == (aantalTiles-1))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.endgame_text);
            builder.setTitle(R.string.endgame_title);
            builder.setPositiveButton(R.string.endgame_button, 
                    new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface arg0, int arg1)
                {
                    restart = 1;
                    settings = getSharedPreferences(MyPREFERENCES, 0);
                    Editor editor = settings.edit();
                    editor.clear();
                    editor.putInt(State, 0);
                    editor.commit();
                    Intent intent = new Intent(GameActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }


    

    //maak een menu bij de menubutton
    public void maakMenu()
    {
        final Button buttonMenu = (Button) findViewById(R.id.buttonMenu);  
        buttonMenu.setOnClickListener(new OnClickListener()
        {  
            @Override  
            public void onClick(View v)
            {  
                if(afterTimer == 1)
                {
                    PopupMenu popup = new PopupMenu(GameActivity.this, buttonMenu);
                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu()); 
                    popup.show();  
                }
            }  
        });
    }


    
    
    public void toStart(MenuItem item)
    {
        restart = 1;
        settings = getSharedPreferences(MyPREFERENCES, 0);
        Editor editor = settings.edit();
        editor.clear();
        editor.putInt(State, 0);
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    
    
    public void toMakkelijker(MenuItem item)
    {
        restart = 1;
        settings = getSharedPreferences(MyPREFERENCES, 0);
        Editor editor = settings.edit();
        editor.clear();
        editor.putInt(State, 0);
        editor.commit();
        if(difficulty != 0)
        {
            difficulty -= 1;
        }
        //stuur id van img mee en difficulty
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("imagebm", imagenumber);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
        finish();
    }

    
    

    public void toMoeilijker(MenuItem item)
    {
        restart = 1;
        settings = getSharedPreferences(MyPREFERENCES, 0);
        Editor editor = settings.edit();
        editor.clear();
        editor.putInt(State, 0);
        editor.commit();
        if(difficulty != 2)
        {
            difficulty += 1;
        }
        //stuur id van img mee en difficulty
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("imagebm", imagenumber);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
        finish();
    }


    
    
    //restart deze activity met difficulty etc
    public void toRestart(View v)
    {
        restart = 1;
        settings = getSharedPreferences(MyPREFERENCES, 0);
        Editor editor = settings.edit();
        editor.clear();
        editor.putInt(State, 0);
        editor.commit();
        //stuur id van img mee en difficulty
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("imagebm", imagenumber);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
        finish();
    }
}
