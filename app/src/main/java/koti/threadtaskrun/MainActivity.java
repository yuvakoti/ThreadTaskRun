package koti.threadtaskrun;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    TextView myTxt,myProg;
    int intrv=5500;
    MyTask myT;
    boolean taskIsRunning= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myTxt = ((TextView)findViewById(R.id.myTxt));
        myProg = ((TextView)findViewById(R.id.myProg));
    }

    @Override
    protected void onPause() {
        super.onPause();
         myT.cancel(true);
    }
    public void StartThread(View view){
        internalStartThread();
    }
    private void internalStartThread(){
        intrv = 500;
        myT = (MyTask) new MyTask();
        myT.execute(intrv);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (taskIsRunning)myT.cancel(true);
        outState.putBoolean("key",taskIsRunning);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        taskIsRunning = savedInstanceState.getBoolean("key",false);
        if(taskIsRunning) {
            internalStartThread();
        }
    }

    private class MyTask extends AsyncTask<Integer,Integer,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            taskIsRunning = true;
        }

        @Override
        protected String doInBackground(Integer... myInt) {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(myInt[0]/10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
            }
            return String.valueOf(myInt[0]);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            myProg.setText(" " + (values[0] + 1));
        }

        @Override
        protected void onPostExecute(String myStr) {
            super.onPostExecute(myStr);
            Log.d("FT- ", "Done " + myStr);
            myTxt.setText(myStr);
            taskIsRunning = false;
        }
    }


}
