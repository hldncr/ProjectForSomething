package com.example.hldncr.pocketsphinx;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.zip.Inflater;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;



public class SpeechRecognizerManager {

    private static final String LOG_TAG = "1453" ;

    private static final String KWS_SEARCH = "wakeup";
    private static final String KEYPHRASE = "help me" ;
    private edu.cmu.pocketsphinx.SpeechRecognizer mPocketSphinxRecognizer;
    private Context mContext;
    private Communicate com ;


    public SpeechRecognizerManager(Context context,int i) {
        this.mContext = context;
        if(i == 1)
            initPockerSphinx();
        else
            initPockerSphinx2();
    }

    private void initPockerSphinx() {

        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(mContext);

                    //Performs the synchronization of assets in the application and external storage
                    File assetDir = assets.syncAssets();


                    //Creates a new speech recognizer builder with default configuration
                    SpeechRecognizerSetup speechRecognizerSetup = SpeechRecognizerSetup.defaultSetup();

                    speechRecognizerSetup.setAcousticModel(new File(assetDir, "en-us-ptm"));
                    speechRecognizerSetup.setDictionary(new File(assetDir, "cmudict-en-us.dict"));

                    // Threshold to tune for keyphrase to balance between false alarms and misses
                    speechRecognizerSetup.setKeywordThreshold(1e-15f);

                    //Creates a new SpeechRecognizer object based on previous set up.
                    mPocketSphinxRecognizer = speechRecognizerSetup.getRecognizer();

                    // Create keyword-activation search.
                    mPocketSphinxRecognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);
                    mPocketSphinxRecognizer.addListener(new PocketSphinxRecognitionListener());
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    Toast.makeText(mContext, "Failed to init pocketSphinxRecognizer ", Toast.LENGTH_SHORT).show();
                } else {
                    restartSearch(KWS_SEARCH);
                }
            }
        }.execute();

    }

    private void initPockerSphinx2() {

        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(mContext);

                    //Performs the synchronization of assets in the application and external storage
                    File assetDir = assets.syncAssets();


                    //Creates a new speech recognizer builder with default configuration
                    SpeechRecognizerSetup speechRecognizerSetup = SpeechRecognizerSetup.defaultSetup();

                    speechRecognizerSetup.setAcousticModel(new File(assetDir, "en-us-ptm"));
                    speechRecognizerSetup.setDictionary(new File(assetDir, "cmudict-en-us.dict"));

                    // Threshold to tune for keyphrase to balance between false alarms and misses
                    speechRecognizerSetup.setKeywordThreshold(1e-15f);

                    //Creates a new SpeechRecognizer object based on previous set up.
                    mPocketSphinxRecognizer = speechRecognizerSetup.getRecognizer();

                    // Create keyword-activation search.-
                    mPocketSphinxRecognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);
                    mPocketSphinxRecognizer.addListener(new PocketSphinxRecognitionListener());
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    Toast.makeText(mContext, "Failed to init pocketSphinxRecognizer ", Toast.LENGTH_SHORT).show();
                } else {
                    restartSearch(KWS_SEARCH);
                }
            }
        }.execute();

    }

    // TODO Say Something Here
    public void setCom(Communicate com) {
        this.com = com;
    }


    /** Restart function for loop */
    private void restartSearch(String searchName) {
        mPocketSphinxRecognizer.stop();
        mPocketSphinxRecognizer.startListening(searchName);
    }


    /** This interface for communicate from SpeechRecognizerManager to MainActivity */
    public interface Communicate {
        public void comResult() ;
    }

    //Speech Listener
    protected class PocketSphinxRecognitionListener implements edu.cmu.pocketsphinx.RecognitionListener {

        @Override
        public void onBeginningOfSpeech()
        {
            Log.e(LOG_TAG,"OnBeginning") ;
        }

        @Override
        public void onPartialResult(Hypothesis hypothesis) {
            if (hypothesis == null)
                return;


            String text = hypothesis.getHypstr();

            if(com!=null)
                com.comResult();

            if (text.contains(KEYPHRASE)) {

                Toast.makeText(mContext, "You said:" + text, Toast.LENGTH_SHORT).show();

                if(com!=null)
                    com.comResult();

                restartSearch(KWS_SEARCH);
            }
        }

        @Override
        public void onResult(Hypothesis hypothesis) {
        }

        @Override
        public void onEndOfSpeech() {
            Log.e(LOG_TAG,"EndOfSpeech") ;
        }

        public void onError(Exception error)
        {
            Log.e(LOG_TAG,"Something happen") ;
        }

        @Override
        public void onTimeout() {
        }
    }

}