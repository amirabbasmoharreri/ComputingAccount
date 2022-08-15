package com.abbasmoharreri.computingaccount.ui.note;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.SpeechActivity;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.ui.adapters.NoteAdapter;
import com.abbasmoharreri.computingaccount.ui.popupdialog.CustomProgressBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteFragment extends Fragment implements DialogInterface.OnDismissListener, View.OnClickListener {


    private NoteAdapter noteAdapter;
    private DataBaseController dataBaseController;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    CustomProgressBar customProgressBar;
    private Handler handler=new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_note, container, false);

        recyclerView = root.findViewById(R.id.recycle_view_note);
        floatingActionButton = root.findViewById(R.id.floating_note);
        floatingActionButton.setOnClickListener(this);
        customProgressBar = new CustomProgressBar();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        new BackgroundTask().execute("start");
    }

    private void setRecyclerView() {

        try {
            noteAdapter.setOnDismissListener(this);
            recyclerView.setAdapter(noteAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        new BackgroundTask().execute("start");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.floating_note) {
            Intent intent = new Intent(getActivity(), SpeechActivity.class);
            startActivity(intent);
        }
    }


    class BackgroundTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customProgressBar.show(getParentFragmentManager(),"");
        }

        @Override
        protected String doInBackground(String... strings) {
            dataBaseController = new DataBaseController(getActivity());
            noteAdapter = new NoteAdapter(getActivity(), dataBaseController.fetchNotes());
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            setRecyclerView();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    customProgressBar.dismiss();
                }
            },0);
            this.onCancelled();
        }
    }
}
