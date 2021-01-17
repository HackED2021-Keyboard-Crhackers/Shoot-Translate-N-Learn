package shootTranslateLearn;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.examples.detection.R;

import java.util.ArrayList;

public class ViewSavedWords extends AppCompatActivity {
    ListView wordList;
    ArrayAdapter<Word> wordAdapter;
    ArrayList<Word> wordDataList;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_saved_words);

        wordList = findViewById(R.id.word_list);
        wordDataList = new ArrayList<>();

        // populate wordDataList with Firebase data (snapshot listener)

        // dummy data
        wordDataList.add(new Word("egg", "Japanese","tamago"));
        wordDataList.add(new Word("mouse", "French","souri"));
        wordDataList.add(new Word("sugar-free", "French","sans sucre"));

        wordAdapter = new CustomList(this, wordDataList);
        wordList.setAdapter(wordAdapter);
    }
}
