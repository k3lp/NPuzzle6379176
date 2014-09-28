package nl.mprog.projects.nPuzzle6379176;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class SplashActivity extends Activity
{
    private Thread mSplashThread;    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //layout
        setContentView(R.layout.activity_splash);
        final SplashActivity sPlashScreen = this;   
        mSplashThread =  new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    synchronized(this)
                    {
                        //timer splashscreen
                        wait(3000);
                    }
                }
                catch(InterruptedException ex)
                {            
                    
                }
                int done = 1;
                finish();
                //ga naar mainactivity
                Intent intent = new Intent();
                intent.setClass(sPlashScreen, MainActivity.class);
                startActivity(intent);                 
            }
        };
        mSplashThread.start();        
    }

    //voor als je de splash eerder wilt stoppen (ontouch)
    @Override
    public boolean onTouchEvent(MotionEvent evt)
    {
        if(evt.getAction() == MotionEvent.ACTION_DOWN)
        {
            synchronized(mSplashThread)
            {
                mSplashThread.notifyAll();
            }
        }
        return true;
    }    
} 
