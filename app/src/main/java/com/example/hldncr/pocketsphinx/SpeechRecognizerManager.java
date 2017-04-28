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

    // This string is setting the KeyWord for the Listener which recognizes the word , Listener will then start the function from the MainActivity
    private static final String KEYPHRASE = "help me" ;

    // Open-Source Speech Recognizer
    private edu.cmu.pocketsphinx.SpeechRecognizer mPocketSphinxRecognizer;

    // Context and Communicate Object For Synchronization With MainActivity
    private Context mContext;
    private Communicate com ;


    //Constructor Function
    public SpeechRecognizerManager(Context context) {
        this.mContext = context;
        initPockerSphinx();
    }

    // Setting Up and Initializing the Speech Recognizer
    private void initPockerSphinx() {

        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {

                    Assets assets = new Assets(mContext);
                    File assetDir = assets.syncAssets();

                    //Creates a new speech recognizer builder with default configuration
                    SpeechRecognizerSetup speechRecognizerSetup = SpeechRecognizerSetup.defaultSetup();

                    // For Setting The Language
                    speechRecognizerSetup.setAcousticModel(new File(assetDir, "en-us-ptm"));
                    speechRecognizerSetup.setDictionary(new File(assetDir, "cmudict-en-us.dict"));

                    // For Sensivity
                    speechRecognizerSetup.setKeywordThreshold(1e-20f);

                    //Creates a new SpeechRecognizer object
                    mPocketSphinxRecognizer = speechRecognizerSetup.getRecognizer();

                    // Adds KEYWORD and Listener
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



    // Implement Communicate Object
    public void setCom(Communicate com) {
        this.com = com;
    }


    // Restart function for loop
    private void restartSearch(String searchName) {
        mPocketSphinxRecognizer.stop();
        com.IsItRecording(2);
        mPocketSphinxRecognizer.startListening(searchName);
    }


    //Speech Listener Object
    protected class PocketSphinxRecognitionListener implements edu.cmu.pocketsphinx.RecognitionListener {

        @Override
        public void onBeginningOfSpeech() {
            Log.e(LOG_TAG,"OnBeginning") ;
            com.IsItRecording(1);
        }

        @Override
        public void onPartialResult(Hypothesis hypothesis) {
            if (hypothesis == null)
                return;


            String text = hypothesis.getHypstr();

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
            com.IsItRecording(3);
        }

        public void onError(Exception error)
        {
            Log.e(LOG_TAG,"Something happen") ;
        }

        @Override
        public void onTimeout() {
        }
    }
    // The interface for communication between the SpeechRecognizerManager and MainActivity
    public interface Communicate {
        public void comResult() ;
        public void IsItRecording(int b) ;
    }
}