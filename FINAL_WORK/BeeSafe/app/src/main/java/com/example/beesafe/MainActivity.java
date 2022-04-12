package com.example.beesafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beesafe.adapters.ReportAdapter;
import com.example.beesafe.listeners.ReportListener;
import com.example.beesafe.listeners.UserReferenceListener;
import com.example.beesafe.model.Report;
import com.example.beesafe.utils.Constants;
import com.example.beesafe.utils.DigestHelper;
import com.example.beesafe.utils.FirebaseUserHelper;
import com.example.beesafe.utils.LocalDatabaseHelper;
import com.example.beesafe.viewmodels.ReportViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity implements UserReferenceListener, View.OnClickListener, ReportListener {
    ImageView addReportButton;
    ReportViewModel viewModel;

    ImageView logOutButton;
    ImageView mapButton;
    ImageView filterByButton;
    TextView orderByGravityButton;
    TextView orderByUrgencyButton;
    ImageView refreshHomeButton;
    ImageView noConnectionInfo;
    ImageView noConnectionToRest;

    // client http
    private final OkHttpClient client = new OkHttpClient();

    private ReportAdapter adapter;

    FirebaseUserHelper firebaseUserHelper;

    private ArrayList<Report> allReports;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        noConnectionInfo = findViewById(R.id.no_connection);
        noConnectionInfo.setOnClickListener(this);
        setConnectionInfo();

        noConnectionToRest = findViewById(R.id.no_connection_to_rest);
        noConnectionToRest.setOnClickListener(this);


        // avvio l'istanza di FirebaseUserHelper per avere per tutto il tempo a disposizione l'helper
        FirebaseUserHelper helper = FirebaseUserHelper.getInstance();
        helper.setUserListener(this);

        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        adapter = new ReportAdapter(this, this);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        viewModel.getAllReports().observe(this, new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reports) {
                Toast.makeText(MainActivity.this, "Dati aggiornati", Toast.LENGTH_SHORT).show();
                setConnectionInfo();

                // rimuovo i report dal database locale che non appartengono all'user corrente
                FirebaseUser currentUser = helper.getCurrentFirebaseUser();
                reports.removeIf(report -> !report.getFromUser().equalsIgnoreCase(currentUser.getEmail()));

                adapter.setReportList(reports);
            }
        });

        // per aggiungere un report (solo per utenti base)
        addReportButton = findViewById(R.id.add_report_button);
        addReportButton.setOnClickListener(this);

        /**
         * apre un menu da cui si può:
         *  effettuare il logout
         *  aprire il proprio profilo con delle statistiche
         *      usando un profilo amministratore in questa schermata vengono
         *      mostrate le statistiche di tutta la collezione
         */
        logOutButton = findViewById(R.id.logout_button);
        logOutButton.setOnClickListener(this);

        // apre le mappe (solo per amministratori)
        mapButton = findViewById(R.id.maps_button);
        mapButton.setOnClickListener(this);

        // aggiorna la home effettuando query al server REST
        refreshHomeButton = findViewById(R.id.refresh_home);
        refreshHomeButton.setOnClickListener(this);

        filterByButton = findViewById(R.id.filter_by_button);
        orderByGravityButton = findViewById(R.id.order_by_gravity_button);
        orderByUrgencyButton = findViewById(R.id.order_by_urgency_button);

        orderByUrgencyButton.setOnClickListener(this);
        orderByGravityButton.setOnClickListener(this);
        filterByButton.setOnClickListener(this);


    }

    /**
     * usato per impostare le informazioni di connessione
     */
    private void setConnectionInfo(){
        if(!isNetworkAvailable())
            noConnectionInfo.setVisibility(View.VISIBLE);
        else
            noConnectionInfo.setVisibility(View.GONE);
    }

    /**
     * usato per visualizzare o nascondere il messaggio di errore di connessione
     * al server rest
     * @param toggle true = visibile, false = nascosto
     */
    private void setConnectionToRestInfo(boolean toggle){
        if (toggle)
            noConnectionToRest.setVisibility(View.VISIBLE);
        else noConnectionToRest.setVisibility(View.GONE);
    }

    /**
     * usato per gestire i tocchi sullo schermo
     * @param view la vista che è stata toccata
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.refresh_home:
                // aggiorno gli elementi
                if (isNetworkAvailable()){
                    getReports(Constants.REQUEST_GET_ALL);
                }
                else
                    showNoInternetConnectionError();
                setConnectionInfo();
                break;
            case R.id.no_connection:
                // messaggio informativo quando non si è connessi ad internet
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(Constants.NO_CONNECTION_TITLE)
                                .setMessage(Constants.NO_CONNECTION_MESSAGE)
                                .setPositiveButton("Ho capito", null)
                                .setNegativeButton("Minigame", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new Intent(MainActivity.this, NoConnectionActivity.class));
                                    }
                                })
                                .setIcon(R.drawable.ic_baseline_wifi_off_24)
                                .show();
                    }
                });
                break;
            case R.id.no_connection_to_rest:
                // messaggio informativo quando non si è connessi al server rest
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(Constants.NO_CONNECTION_TO_REST_TITLE)
                                .setMessage(Constants.NO_CONNECTION_TO_REST_MESSAGE)
                                .setPositiveButton("Ho capito", null)
                                .setNegativeButton("Minigame", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new Intent(MainActivity.this, NoConnectionActivity.class));
                                    }
                                })
                                .setIcon(R.drawable.ic_baseline_cloud_off_24)
                                .show();
                    }
                });
                break;
            case R.id.maps_button:
                if (isNetworkAvailable()){
                    //startActivity(new Intent(MainActivity.this, MapsActivity.class));
                    if (allReports!=null && allReports.size() != 0){
                        Intent mapsIntent = new Intent(MainActivity.this, GeneralMapsView.class);
                        mapsIntent.putExtra(Constants.MAPS_MULTIPLE_FLAG_NAME, true);
                        Bundle reportsBundle = new Bundle();
                        reportsBundle.putSerializable("ARRAYLIST", (Serializable) allReports);
                        mapsIntent.putExtra(Constants.MAPS_REPORT_ELEMENTS_NAME, reportsBundle);

                        startActivity(mapsIntent);
                    }
                }
                else showNoInternetConnectionError();
                break;
            case R.id.logout_button:
                showUserPopup();
                break;
            case R.id.add_report_button:
                if (isNetworkAvailable())
                    startActivity(new Intent(MainActivity.this, AddReportActivity.class));
                else showNoInternetConnectionError();
                break;
            case R.id.order_by_gravity_button:
                getReports(Constants.REQUEST_GET_ALL_ORDER_BY_GRAVITY);
                break;
            case R.id.order_by_urgency_button:
                getReports(Constants.REQUEST_GET_ALL_ORDER_BY_URGENCY);
                break;
            case R.id.filter_by_button:
                showFilterPopup();
                break;
        }
    }

    /**
     * Mostra il popup dei filtri
     */
    private void showFilterPopup(){
        PopupMenu filterMenu = new PopupMenu(this, filterByButton);

        filterMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.salute:
                        getReports(Constants.REQUEST_GET_ALL + "/" + Constants.FILTER_SALUTE);
                        break;
                    case R.id.furto:
                        getReports(Constants.REQUEST_GET_ALL + "/" + Constants.FILTER_FURTO);
                        break;
                    case R.id.violenza:
                        getReports(Constants.REQUEST_GET_ALL + "/" + Constants.FILTER_VIOLENZA);
                        break;
                    case R.id.incendio:
                        getReports(Constants.REQUEST_GET_ALL + "/" + Constants.FILTER_INCENDIO);
                        break;
                    case R.id.incidente_stradale:
                        getReports(Constants.REQUEST_GET_ALL + "/" + Constants.FILTER_INCIDENTE_STRADALE);
                        break;
                }
                return true;
            }
        });
        filterMenu.inflate(R.menu.filter_menu);
        filterMenu.show();
    }

    /**
     * mostra il popup menu all'utente
     */
    private void showUserPopup(){
        PopupMenu logoutMenu = new PopupMenu(this, logOutButton);
        if (firebaseUserHelper.getUserReference().getRole().equalsIgnoreCase(Constants.ADMINISTRATOR))
            logoutMenu.getMenu().add("Activity logs").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    startActivity(new Intent(MainActivity.this, LogsView.class));
                    return true;
                }
            });

        logoutMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.show_account_menu_item:
                        // avvia activity del profilo
                        if (isNetworkAvailable())
                            startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
                        else showNoInternetConnectionError();
                        return true;
                    case R.id.logout_menu_item:
                        FirebaseAuth.getInstance().signOut();
                        FirebaseUserHelper.destroySingleton();
                        goBackToLogin();
                        return true;

                }
                return false;
            }
        });
        logoutMenu.inflate(R.menu.account_menu);
        logoutMenu.show();
    }

    /**
     * eseguito per recuperare tutti i reports
     */
    @Deprecated
    private void getAllReportsRequest(){
        // se arrivo quì vuol dire che c'è internet, aggiorno l'icona di info
        setConnectionInfo();


        firebaseUserHelper = FirebaseUserHelper.getInstance();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://" + Constants.REST_HOSTNAME + ":" + Constants.REST_PORT + "/reports")
                .addHeader("uid", firebaseUserHelper.getCurrentFirebaseUser().getUid())
                .addHeader("digest", DigestHelper.getDigestFromCurrentUser())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //Log.d("OkHttp", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Connessione al cloud fallita.", Toast.LENGTH_SHORT).show();
                        // la connessione al server rest non è attiva
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // visualizzo l'icona di info
                                setConnectionToRestInfo(true);
                            }
                        });
                    }
                });
            }

            /**
             * eseguito quando è disponibile una risposta
             * @param call la chiamata
             * @param response la risposta
             * @throws IOException eventuale eccezione
             */
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                // la connessione al server rest è attiva
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // nascondo l'icona di info
                        setConnectionToRestInfo(false);
                    }
                });

                String responseBody = response.body().string();


                Gson gson = new Gson();
                Report[] reports = gson.fromJson(responseBody, Report[].class);

                allReports = new ArrayList<>(Arrays.asList(reports));

                /*for (Report report : reports)
                    Log.d("getallrequests", report.toString());*/

                if(FirebaseUserHelper.getInstance().getUserReference().getRole().equalsIgnoreCase(Constants.ADMINISTRATOR)){
                    /**
                     * non posso modificare l'interfaccia con un thread diverso da quello che l'ha creata
                     * per questo motivo uso questa porzione di codice per eseguire l'aggiornamento
                     * sul thread principale
                     *
                     * inoltre, se l'utente è un amministratore non serve effettuare il cacheing locale
                     * questo perchè l'admin deve avere sempre gli ultimi report,
                     * e non gli interessa sicuramente di rivedere repor vecchi. Inoltre si presuppone
                     * che l'amministratore abbia sempre una connessione internet, proprio perchè deve
                     * controllare sempre gli ultimi report.
                     */

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setReportList(Arrays.asList(reports));
                        }
                    });
                }else{
                    LocalDatabaseHelper helper = new LocalDatabaseHelper(MainActivity.this);
                    helper.saveReportAsync(Arrays.asList(reports));
                }



            }
        });
    }

    /**
     * usato per recuperare i reports dal database remoto tramite HTTP request al server REST
     * @param requestType la richiesta
     */
    private void getReports(String requestType){
        // se arrivo quì vuol dire che c'è internet, aggiorno l'icona di info
        setConnectionInfo();

        if (!isPathAllowed(requestType))
            return;

        firebaseUserHelper = FirebaseUserHelper.getInstance();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://" + Constants.REST_HOSTNAME + ":" + Constants.REST_PORT + requestType)
                .addHeader("uid", firebaseUserHelper.getCurrentFirebaseUser().getUid())
                .addHeader("digest", DigestHelper.getDigestFromCurrentUser())
                .build();

        client.newCall(request).enqueue(new Callback() {
            /**
             * eseguito quando la richiesta HTTP fallisce
             * @param call la chiamata
             * @param e eccezione
             */
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                /**
                 * esegue del codice sul thread principale
                 */
                runOnUiThread(new Runnable() {
                    /**
                     * contiene il codice eseguito sul thread selezionato
                     */
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Connessione al cloud fallita.", Toast.LENGTH_SHORT).show();
                        // la connessione al server rest non è attiva
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // visualizzo l'icona di info
                                setConnectionToRestInfo(true);
                            }
                        });
                    }
                });
            }

            /**
             * eseguito quando è disponibile una risposta
             * @param call la chiamata
             * @param response la risposta
             * @throws IOException eventuale eccezione
             */
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                // la connessione al server rest è attiva
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // nascondo l'icona di info
                        setConnectionToRestInfo(false);
                    }
                });

                String responseBody = response.body().string();


                Gson gson = new Gson();
                Report[] reports = gson.fromJson(responseBody, Report[].class);

                Log.d("retrieved_reports", reports.toString());

                allReports = new ArrayList<>(Arrays.asList(reports));

                /*for (Report report : reports)
                    Log.d("getallrequests", report.toString());*/

                if(FirebaseUserHelper.getInstance().getUserReference().getRole().equalsIgnoreCase(Constants.ADMINISTRATOR)){
                    /**
                     * non posso modificare l'interfaccia con un thread diverso da quello che l'ha creata
                     * per questo motivo uso questa porzione di codice per eseguire l'aggiornamento
                     * sul thread principale
                     *
                     * inoltre, se l'utente è un amministratore non serve effettuare il cacheing locale
                     * questo perchè l'admin deve avere sempre gli ultimi report,
                     * e non gli interessa sicuramente di rivedere repor vecchi. Inoltre si presuppone
                     * che l'amministratore abbia sempre una connessione internet, proprio perchè deve
                     * controllare sempre gli ultimi report.
                     */

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setReportList(Arrays.asList(reports));
                        }
                    });
                }else{
                    LocalDatabaseHelper helper = new LocalDatabaseHelper(MainActivity.this);
                    helper.saveReportAsync(Arrays.asList(reports));
                }



            }
        });
    }

    /**
     * usato per controllare se la connessione è disponibile
     * @return booleano che ci dice se si è connessi ad internet
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * usato per tornare al login senza la possibilità di tornare indietro alla home
     * questo metodo è usato dopo aver effettuato il logout dell'utente
     */
    private void goBackToLogin(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

        // finish() fondamentale: non permette all'utente di tornare indietro alla home
        finish();
    }

    /**
     * viene invocato quando l'utente è stato trovato nel database Firebase
     * e l'helper è disponibile
     */
    @Override
    public void onUserReady() {
        if (isNetworkAvailable())
            getReports(Constants.REQUEST_GET_ALL);

        // se è amministratore impostiamo il btn map visibile
        if (FirebaseUserHelper.getInstance().getUserReference().getRole().equalsIgnoreCase(Constants.ADMINISTRATOR)){
            mapButton.setVisibility(View.VISIBLE);
            filterByButton.setVisibility(View.VISIBLE);
            orderByUrgencyButton.setVisibility(View.VISIBLE);
            orderByGravityButton.setVisibility(View.VISIBLE);
        }
        // se è amministratore rimuoviamo la possibilità di aggiungere report
        if (FirebaseUserHelper.getInstance().getUserReference().getRole().equalsIgnoreCase(Constants.ADMINISTRATOR))
            addReportButton.setVisibility(View.GONE);

    }


    /**
     * usato per mostrare un Toast tramite il main thread
     */
    private void showNoInternetConnectionError(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Non sei connesso ad internet!\nClicca sull'icona rossa in alto per maggiori informazioni", Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * invocato quando un report viene toccato per un lungo periodo
     * @param report il report toccato
     * @param position la posizione del report nella collezione
     * @param view la vista contenente il report
     */
    @Override
    public void onReportLongClicked(Report report, int position, View view) {
        // funzione non implementata
    }

    /**
     * invocato quando un report viene toccato
     * @param report il report toccato
     * @param position la posizione del report nella collezione
     * @param view la vista contenente il report
     */
    @Override
    public void onReportClicked(Report report, int position, View view) {
        //Toast.makeText(this, "clicked: " + position, Toast.LENGTH_SHORT).show();
        Intent reportFocusViewIntent = new Intent(getApplicationContext(), ReportFocusViewActivity.class);
        reportFocusViewIntent.putExtra("report", report);

        startActivity(reportFocusViewIntent);
    }

    /**
     * usato per validare il path della richiesta
     * @param requestType la richiesta
     * @return booleano che ci dice se la richiesta è valida
     */
    private boolean isPathAllowed(String requestType){
        Set<String> allowdRequests = new HashSet<>();

        allowdRequests.add(Constants.REQUEST_GET_ALL);
        allowdRequests.add(Constants.REQUEST_GET_ALL_ORDER_BY_GRAVITY);
        allowdRequests.add(Constants.REQUEST_GET_ALL_ORDER_BY_URGENCY);
        allowdRequests.add(Constants.REQUEST_GET_ALL+"/"+Constants.FILTER_SALUTE);
        allowdRequests.add(Constants.REQUEST_GET_ALL+"/"+Constants.FILTER_FURTO);
        allowdRequests.add(Constants.REQUEST_GET_ALL+"/"+Constants.FILTER_INCENDIO);
        allowdRequests.add(Constants.REQUEST_GET_ALL+"/"+Constants.FILTER_VIOLENZA);
        allowdRequests.add(Constants.REQUEST_GET_ALL+"/"+Constants.FILTER_INCIDENTE_STRADALE);


        if (!allowdRequests.contains(requestType))
            return false;
        return true;
    }
}