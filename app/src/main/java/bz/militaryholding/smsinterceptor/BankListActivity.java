package bz.militaryholding.smsinterceptor;

import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import bz.militaryholding.smsinterceptor.tools.Prefs;

import java.util.List;

public class BankListActivity extends AppCompatActivity {
    private ListView lvBanks;
    private ArrayAdapter<String> adapter;
    private List<String> banks;

    private Prefs prefs;

    private EditText etBankNumber;
    private Button btnAddBankNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = Prefs.getInstance(getApplicationContext());
        setContentView(R.layout.activity_bank_list);

        banks = prefs.getBanks();

        lvBanks = (ListView) findViewById(R.id.lvBanks);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, banks);
        lvBanks.setAdapter(adapter);
        lvBanks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteItem(i);
                return true;
            }
        });


        etBankNumber = (EditText) findViewById(R.id.etBankNumber);
        btnAddBankNumber = (Button) findViewById(R.id.btnAddBankNumber);
        btnAddBankNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                banks.add(etBankNumber.getText().toString());
                prefs.saveBanks(banks);
                etBankNumber.setText("");
                btnAddBankNumber.setEnabled(false);
                adapter.notifyDataSetChanged();
            }
        });
        etBankNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty()) {
                    btnAddBankNumber.setEnabled(true);
                } else {
                    btnAddBankNumber.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void deleteItem(final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BankListActivity.this);
        builder
                .setTitle("Удаление номера банка")
                .setMessage("Удалить "+banks.get(index)+"?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        banks.remove(index);
                        prefs.saveBanks(banks);
                        adapter.notifyDataSetChanged();
                        dialogInterface.cancel();
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.create().show();

    }
}