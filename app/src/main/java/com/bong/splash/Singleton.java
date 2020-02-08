package com.bong.splash;

import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Singleton extends AppCompatActivity {

        // Private constructor prevents instantiation from other classes
     private Singleton() {
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setTitle("결과보기");

     }
        /**
         * SingletonHolder is loaded on the first execution of Singleton.getInstance()

         * or the first access to SingletonHolder.INSTANCE, not before.
         */
        private static class SingletonHolder {
            public static final Singleton INSTANCE = new Singleton();
        }
        public static Singleton getInstance() {
            return SingletonHolder.INSTANCE;
        }
}

