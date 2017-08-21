package usuario.app.browser;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class activity_lista extends AppCompatActivity {
    public String[][] svShowEmpty= new String[300][300];
    public String[][] svHideEmpty= new String[300][300];
    private LinkedHashMap<String, GroupInfo> subjects = new LinkedHashMap<String, GroupInfo>();
    private ArrayList<GroupInfo> deptList = new ArrayList<GroupInfo>();
    private CustomAdapter listAdapter;
    private ExpandableListView lista;
    public Typeface fonte;
    public Button btnEmpt,btnRefresh,btnAbout;
    public int i,j,k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_lista);

        btnEmpt = (Button)findViewById(R.id.empty);
        btnAbout = (Button)findViewById(R.id.about);
        btnRefresh = (Button)findViewById(R.id.refresh);

        //Seleciona fonte para TextView (enviado como parametro)
        fonte = Typeface.createFromAsset(getAssets(),"courier.ttf");

        //Recebe Vetor com informações
        Intent intent = getIntent();
        String[] temp = intent.getStringArrayExtra("temp");

        //Mensagem de erro conecção ou master
        if(temp[0] == null){
            AlertDialog.Builder dialogo = new AlertDialog.Builder(activity_lista.this);
            dialogo.setTitle("Erro...");
            dialogo.setMessage("Server Master not Found...\n" +
                    "Check your connection!!!");
            dialogo.setNeutralButton("OK", null);
            dialogo.show();
        }

        int i = 0;
        int j = 0;
        int t = 0;

        //Retorna Vetor para Matriz Original
        while(temp[t] != null){
            if("*".equals(temp[t])){
                i++;
                j=0;
                t++;
            }else{
                //svInfo[i][j] = temp[t];
                svHideEmpty[i][j] = temp[t];
                svShowEmpty[i][j] = temp[t];
                j++;
                t++;
            }
        }
        ordena();

        // adiciona dados para exibir na ExpandableListView
        loadData(true);
        //get reference of the ExpandableListView
        lista = (ExpandableListView) findViewById(R.id.Lista);
        // create the adapter by passing your ArrayList data
        listAdapter = new CustomAdapter(activity_lista.this, deptList, fonte);
        // attach the adapter to the expandable list view
        lista.setAdapter(listAdapter);

        btnEmpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("Hide Empty".equals(btnEmpt.getText())){
                    btnEmpt.setText("Show Empty");
                    subjects.clear();
                    deptList.clear();
                    loadData(false);
                    listAdapter.notifyDataSetChanged();
                    collapseAll();
                }else{
                    btnEmpt.setText("Hide Empty");
                    subjects.clear();
                    deptList.clear();
                    loadData(true);
                    listAdapter.notifyDataSetChanged();
                    collapseAll();
                }
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(activity_lista.this,Browser.class);
                startActivity(intentBack);
                finish();
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intentAbout = new Intent(activity_lista.this,about.class);
                startActivity(intentAbout);
                //finish();
            }
        });



    }

    @Override
    public void onBackPressed(){
        //Metodo vazio para anular botao de votlar
    }

    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            //ListaHide.expandGroup(i);
            lista.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            //ListaHide.collapseGroup(i);
            lista.collapseGroup(i);
        }
    }

    //load some initial data into out list
    private void loadData(Boolean emp){
        //empty true mostra tudo
        Boolean empty = emp;
        String server,player;
        int i = 0;
        int j;
        if(empty) {
            while (svShowEmpty[i][6] != null) {
                j = 7;
                //          IP                           PORTA                  PING                            TIPO                        MAP                     NPLAYERS                                HOST
                server = (svShowEmpty[i][0] + ":" + svShowEmpty[i][1] + " [" + svShowEmpty[i][2] + "]ms\n" + svShowEmpty[i][3] + "/" + svShowEmpty[i][5] + " [" + svShowEmpty[i][6] + "]\n" + "Host " + svShowEmpty[i][4]);
                if (svShowEmpty[i][j] != null) {
                    while (svShowEmpty[i][j] != null) {
                        if (Integer.parseInt(svShowEmpty[i][j + 1]) > 0) {
                            //          NAME                                TEAM                                   FRAGS                          PING
                            player = (svShowEmpty[i][j + 2] + " Team " + svShowEmpty[i][j + 3] + " Frags[" + svShowEmpty[i][j] + "] Ping[" + svShowEmpty[i][j + 1] + "] ");
                        } else {
                            player = (svShowEmpty[i][j + 2] + " [Spectator] ");
                        }
                        addProduct(server, player);
                        j += 4;
                    }
                } else {
                    addProduct(server, "Empty");
                }
                i++;
            }
        }else{
            while (svHideEmpty[i][6] != null) {
                j = 7;
                //            IP                        PORTA                       PING                        TIPO                        MAP                     NPLAYERS                                HOST
                server = (svHideEmpty[i][0] + ":" + svHideEmpty[i][1] + " [" + svHideEmpty[i][2] + "]ms\n" + svHideEmpty[i][3] + "/" + svHideEmpty[i][5] + " [" + svHideEmpty[i][6] + "]\n" + "Host " + svHideEmpty[i][4]);
                if (svHideEmpty[i][j] != null) {
                    while (svHideEmpty[i][j + 1] != null) {
                        if (Integer.parseInt(svHideEmpty[i][j + 1]) > 0) {
                            //             NAME                              TEAM                                   FRAGS                         PING
                            player = (svHideEmpty[i][j + 2] + " Team " + svHideEmpty[i][j + 3] + " Frags[" + svHideEmpty[i][j] + "] Ping[" + svHideEmpty[i][j + 1] + "] ");
                        } else {
                            player = (svHideEmpty[i][j + 2] + " [Spectator] ");
                        }
                        addProduct(server, player);
                        j += 4;
                    }
                } else {
                    addProduct(server, "Empty");
                }
                i++;
            }
        }
    }

    //here we maintain our products in various departments
    private int addProduct(String department, String product){

        int groupPosition = 0;

        //check the hash map if the group already exists
        GroupInfo headerInfo = subjects.get(department);
        //add the group if doesn't exists
        if(headerInfo == null){
            headerInfo = new GroupInfo();
            headerInfo.setName(department);
            subjects.put(department, headerInfo);
            deptList.add(headerInfo);
        }

        //get the children for the group
        ArrayList<ChildInfo> productList = headerInfo.getProductList();
        //size of the children list
        int listSize = productList.size();
        //add to the counter
        listSize++;

        //create a new child and add that to the group
        ChildInfo detailInfo = new ChildInfo();
        detailInfo.setSequence(String.valueOf(listSize));
        detailInfo.setName(product);
        productList.add(detailInfo);
        headerInfo.setProductList(productList);

        //find the group position inside the list
        groupPosition = deptList.indexOf(headerInfo);

        return groupPosition;
    }
    public boolean ordena() {
        //System.out.println("ordena()");

        //==========================================================================================
        //trata svHideEmpty
        //==========================================================================================
        // Ordena por Players
        //System.out.println("Ordena");
        k = 0;
        while (svHideEmpty[k][0] != null) {
            i = 0;
            while (svHideEmpty[i + 1][6] != null) {
                if ((Integer.parseInt(svHideEmpty[i][6])) < (Integer.parseInt(svHideEmpty[i + 1][6]))) {
                    String aux[] = new String[300];
                    for (j = 0; j < 300; j++) {
                        aux[j] = svHideEmpty[i][j];
                        svHideEmpty[i][j] = svHideEmpty[i + 1][j];
                        svHideEmpty[i + 1][j] = aux[j];
                    }
                    aux = null;
                }
                i++;
            }
            k++;
        }

        // Apaga empty
        //System.out.println("Apaga empty");
        i = 0;
        while (svHideEmpty[i][6] != null) {
            j = 0;
            while (svHideEmpty[i][j] != null) {
                if ("0".equals(svHideEmpty[i][6])) {
                    svHideEmpty[i][j] = null;
                }
                j++;
            }
            i++;
        }

        //==========================================================================================
        //Ordena por ping svHideEmpty
        //System.out.println("Ordena por ping");
        k = 0;
        while (svHideEmpty[k][2] != null) {
            i = 0;
            while (svHideEmpty[i + 1 ][2] != null) {
                if ((Integer.parseInt(svHideEmpty[i][2])) > (Integer.parseInt(svHideEmpty[i + 1][2]))) {
                    String aux[] = new String[300];
                    for (j = 0; j < 300; j++) {
                        aux[j] = svHideEmpty[i][j];
                        svHideEmpty[i][j] = svHideEmpty[i + 1][j];
                        svHideEmpty[i + 1][j] = aux[j];
                    }
                    aux = null;
                }
                i++;
            }
            k++;
        }
        //==========================================================================================
        //trata svShowEmpty
        //==========================================================================================
        //Ordena por ping svHideEmpty
        //System.out.println("Ordena por ping");
        k = 0;
        while (svShowEmpty[k][2] != null) {
            i = 0;
            while (svShowEmpty[i + 1][2] != null) {
                if ((Integer.parseInt(svShowEmpty[i][2])) > (Integer.parseInt(svShowEmpty[i + 1][2]))) {
                    String aux[] = new String[300];
                    for (j = 0; j < 300; j++) {
                        aux[j] = svShowEmpty[i][j];
                        svShowEmpty[i][j] = svShowEmpty[i + 1][j];
                        svShowEmpty[i + 1][j] = aux[j];
                    }
                    aux = null;
                }
                i++;
            }
            k++;
        }
        /*
        //==========================================================================================
        //imprime matriz inteira
        //==========================================================================================
        //System.out.println("Iprime tudo");
        i = 0;
        while (svInfo[i][6] != null) {
            j = 0;
            while (svInfo[i][j] != null) {
                System.out.print(svInfo[i][j] + " / ");
                j++;
            }
            System.out.println("");
            i++;
        }
        */
        return true;
    }
}

