package shootTranslateLearn;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.tensorflow.lite.examples.detection.R;

import java.util.ArrayList;

import io.grpc.internal.JsonUtil;

public class ViewSavedWords extends AppCompatActivity
{
    ListView wordList;
    ArrayAdapter<Word> wordAdapter;
    ArrayList<Word> wordDataList;
    FirebaseFirestore db;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_saved_words);

        wordList = findViewById(R.id.word_list);
        wordDataList = new ArrayList<>();
        wordAdapter = new CustomList(this, wordDataList);
        wordList.setAdapter(wordAdapter);

        db = FirebaseFirestore.getInstance();

        db
                .collection("words")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            for (DocumentSnapshot document : task.getResult())
                            {
                                String original_word = document.getString("original_word");
                                String target_language = document.getString("target_language");
                                String translated_word = document.getString("translated_word");

                                wordDataList.add(new Word(original_word, target_language, translated_word));
                            }
                            wordAdapter.notifyDataSetChanged();
                        }
                    }
                });

        wordList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TAG", "Clicked");
                FirebaseFirestore.getInstance()
                        .collection("words")
                        .document(wordDataList.get(i).getOriginalWord()+wordDataList.get(i).getTargetLanguage())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "Successfully deleted document from Collection");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG", "Failure to delete document from Collection.");
                            }
                        });
                wordDataList.remove(i);
                wordAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }
}
