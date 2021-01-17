package shootTranslateLearn;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.tensorflow.lite.examples.detection.R;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<Word> {
    private Context context;
    private ArrayList<Word> words;

    public CustomList(Context context, ArrayList<Word> words){
        super(context, 0, words);
        this.context = context;
        this.words = words;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.word_content, parent, false);
        }
        Log.d("Michelle","CustomList launched");

        Word word = words.get(position);

        TextView original_word_textview = view.findViewById(R.id.original_word_textview);
        TextView target_language_textview = view.findViewById(R.id.target_language_textview);
        TextView translated_word_textview = view.findViewById(R.id.translated_word_textview);

        original_word_textview.setText(word.getOriginalWord());
        target_language_textview.setText(word.getTargetLanguage());
        translated_word_textview.setText(word.getTranslatedWord());

        return view;
    }
}
