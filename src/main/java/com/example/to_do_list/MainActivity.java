package com.example.to_do_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private EditText newItemEditText;
    private ListView listView;
    private ArrayAdapter<String> listAdapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newItemEditText = findViewById(R.id.newItemEditText);
        listView = findViewById(R.id.listView);

        sharedPreferences = getSharedPreferences("MyList", MODE_PRIVATE);
        Set<String> listData = sharedPreferences.getStringSet("items", new HashSet<>());

        listAdapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.taskTextView, new ArrayList<>(listData));
        listView.setAdapter(listAdapter);

        // ListView elemanlarına tıklama işlemi
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Tıklanan öğeyi işaretle
                markItemAsDone(position);
            }
        });
    }

    public void addItem(View view) {
        String newItem = newItemEditText.getText().toString().trim();

        if (!newItem.isEmpty()) {
            Set<String> listData = new HashSet<>(sharedPreferences.getStringSet("items", new HashSet<>()));
            listData.add(newItem);

            sharedPreferences.edit().putStringSet("items", listData).apply();

            listAdapter.clear();
            listAdapter.addAll(listData);

            newItemEditText.setText("");
        }
    }

    public void showDetail(View view) {
        int position = listView.getPositionForView(view);
        String selectedItem = listAdapter.getItem(position);

        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("selectedItem", selectedItem);
        startActivity(intent);
    }

    private void markItemAsDone(int position) {
        // İlgili pozisyondaki öğeyi al
        String selectedItem = listAdapter.getItem(position);

        // İlgili öğeyi işaretle
        // Bu örnekte, listeye öğe eklenirken checkbox durumu kaydedilmediği için sadece Toast mesajı gösteriliyor
        Toast.makeText(this, "Yapıldı: " + selectedItem, Toast.LENGTH_SHORT).show();
    }

    public void deleteItems(View view) {
        // Silme işlemi için AlertDialog göster
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Veri Silme")
                .setMessage("Seçili öğeleri silmek istiyor musunuz?")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Seçili öğeleri sil
                        deleteSelectedItems();
                    }
                })
                .setNegativeButton("Hayır", null)
                .show();
    }

    private void deleteSelectedItems() {
        Set<String> listData = new HashSet<>(sharedPreferences.getStringSet("items", new HashSet<>()));

        // Seçili öğeleri bul ve sil
        Iterator<String> iterator = listData.iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            if (listView.isItemChecked(listAdapter.getPosition(item))) {
                iterator.remove();
            }
        }

        // Değişiklikleri kaydet
        sharedPreferences.edit().putStringSet("items", listData).apply();

        // Liste güncellenmiş halini göster
        listAdapter.clear();
        listAdapter.addAll(listData);
        listView.clearChoices(); // CheckBox'ları temizle
    }

    // Sil butonuna tıklandığında çağrılacak fonksiyon
    public void deleteItem(View view) {
        // Silme işlemi için AlertDialog göster
        View parentView = (View) view.getParent();
        int position = listView.getPositionForView(parentView);
        String selectedItem = listAdapter.getItem(position);

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Veri Silme")
                .setMessage(selectedItem + " öğesini silmek istiyor musunuz?")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Seçili öğeyi sil
                        deleteSingleItem(selectedItem);
                    }
                })
                .setNegativeButton("Hayır", null)
                .show();
    }

    private void deleteSingleItem(String item) {
        Set<String> listData = new HashSet<>(sharedPreferences.getStringSet("items", new HashSet<>()));
        listData.remove(item);

        // Değişiklikleri kaydet
        sharedPreferences.edit().putStringSet("items", listData).apply();

        // Liste güncellenmiş halini göster
        listAdapter.clear();
        listAdapter.addAll(listData);
    }
}