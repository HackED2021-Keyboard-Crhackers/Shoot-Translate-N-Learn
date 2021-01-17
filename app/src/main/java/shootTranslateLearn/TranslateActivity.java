package shootTranslateLearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.tensorflow.lite.examples.detection.R;

import java.util.HashMap;
import java.util.Locale;

public class TranslateActivity extends AppCompatActivity {

    private TextView selected;
    private ImageView image;
    private Button translate_bttn;
    private TextView translated;
    private ImageButton speakButton;
    private TextToSpeech textToSpeech;
    private Button saveButton;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate_activity_view);

        selected = findViewById(R.id.objectDetected_textview);
        image = findViewById(R.id.imageView2);
        translate_bttn = findViewById(R.id.translate_button);
        translated = findViewById(R.id.output_textview);
        speakButton = findViewById(R.id.imageButton);
        saveButton = findViewById(R.id.saveButton);

        firebaseFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String originalWord = intent.getStringExtra("image");
        selected.setText(originalWord);
        byte[] byteArray = intent.getByteArrayExtra("BITMAP");
        Bitmap capturedImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        image.setImageBitmap(capturedImage);

        textToSpeech = new TextToSpeech(getApplicationContext(),
            new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                        textToSpeech.setSpeechRate((float) 0.5);
                    }
                }
            });

        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int speak = textToSpeech.speak(originalWord, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        // save word button
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                HashMap<String, String> word_info = new HashMap<>();
                word_info.put("original_word", originalWord);
                word_info.put("target_language", "FrenchTest");
                word_info.put("translated_word", "TranslatedWordTest");
                firebaseFirestore.collection("words")
                    .document()
                    .set(word_info)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Michelle", "word_info has been added successfully");
                            Toast.makeText(TranslateActivity.this, "Word saved!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Michelle", "word_info not added");
                        }
                    });
            }
        });

    }
}