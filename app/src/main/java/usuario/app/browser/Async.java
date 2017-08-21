package usuario.app.browser;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.GregorianCalendar;

public class Async extends AsyncTask<String,Integer,Integer>{
    public Context ctx;
    public String svInfo[][] = new String[300][300];
    String status = "Running";
    byte[] receiveDataIP = new byte[10000];
    byte[] buffer = new byte[10000];
    /*
    char[] chars = {'c'}; //query Master
    //Master List
    // all port 27000
    // 5.196.225.119 - master.quakeservers.net // off
    // 136.243.9.253 - qwmaster.ocrana.de
    // 213.133.100.142 - qwmaster.fodquake.net
    String master = "5.196.225.119";//Master Global - www.quakeservers.net
    int masterPort = 27000;
    */
    char[] command = {'ÿ', 'ÿ', 'ÿ', 'ÿ', 's', 't', 'a', 't', 'u', 's', ' ', '2', '3'}; //quake world query
    //char[] command = {'ÿ','ÿ','ÿ','ÿ','g','e','t','s','t','a','t','u','s'}; //quake 3 query
    int i = 0;
    int j = 0;
    int host = 0;
    DatagramSocket clientSocket;
    DatagramPacket sendPacketIP,receivePacketIP,sendPacket,receivePacket;
    ProgressBar bar;
    TextView barLog,wait;
    //=============================================================================================
    public Async(Context context, ProgressBar barx,TextView barxlog,TextView waitx){
        this.ctx = context;
        this.bar = barx;
        this.barLog = barxlog;
        this.wait = waitx;
    }
    //=============================================================================================
    @Override
    protected void onPreExecute(){
        //progressbar
        bar.setVisibility(View.VISIBLE);
        bar.setProgress(50);
        barLog.setVisibility(View.VISIBLE);
        wait.setVisibility(View.VISIBLE);
    }
    //=============================================================================================
    @Override
    protected Integer doInBackground(String... params) {
        try {
            status();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }
    //=============================================================================================
    @Override
    protected void onPostExecute(Integer numero){
        Intent intent = new Intent(ctx, activity_lista.class);
        String[] temp = new String[1500];
        //converte Array 2d para Array simples para return
        int i = 0;
        int j;
        int t = 0;
        while (svInfo[i][6] != null) {
            j = 0;
            while (svInfo[i][j] != null) {
                temp[t] = svInfo[i][j];
                j++;
                t++;
            }
            temp[t] = "*";
            i++;
            t++;
        }
        //chama tela da lista e envia vetor de retorno
        intent.putExtra("temp",temp);
        ctx.startActivity(intent);
    }
    //=============================================================================================
    protected void onProgressUpdate(Integer params){
        //code nao achei viavel para progressbar
    }

    public String[][] status() throws IOException{
        String svHost;
        int svPort;
        //gera lista de servers aqui
        /*
            Master List
             all port 27000
             5.196.225.119 - master.quakeservers.net
             213.133.100.142 - qwmaster.fodquake.net
             136.243.9.253 - qwmaster.ocrana.de
         */

        try {
            //query para Mater
            char[] chars = {'c'}; //query Master
            String master = "5.196.225.119";//Master Global - www.quakeservers.net
            int masterPort = 27000;

            //convertendo char para byte um a um
            buffer[0] = (byte) chars[0];//convertendo String do endereço
            InetAddress masterIP = InetAddress.getByName(master);//Inciando o Socket
            //criando pacote de envio
            clientSocket = new DatagramSocket();

            //criando pacote de envio
            sendPacketIP = new DatagramPacket(buffer, buffer.length, masterIP, masterPort);
            clientSocket.setSoTimeout(1000);
            clientSocket.send(sendPacketIP);

            //criando pacote para recebimento
            receivePacketIP = new DatagramPacket(receiveDataIP, receiveDataIP.length);//recebendo
            clientSocket.setSoTimeout(1000);
            clientSocket.receive(receivePacketIP);
        }catch (SocketTimeoutException e){
            //System.out.println("crash");
            try {
                //query para Mater
                char[] chars = {'c'}; //query Master
                String master = "136.243.9.253";//Master - qwmaster.fodquake.net
                //System.out.println(master);
                int masterPort = 27000;

                //System.out.println("Gera servList");
                //convertendo char para byte um a um
                buffer[0] = (byte) chars[0];//convertendo String do endereço
                InetAddress masterIP = InetAddress.getByName(master);//Inciando o Socket
                //criando pacote de envio
                clientSocket = new DatagramSocket();

                //criando pacote de envio
                sendPacketIP = new DatagramPacket(buffer, buffer.length, masterIP, masterPort);
                clientSocket.setSoTimeout(1000);
                clientSocket.send(sendPacketIP);

                //criando pacote para recebimento
                receivePacketIP = new DatagramPacket(receiveDataIP, receiveDataIP.length);//recebendo
                clientSocket.setSoTimeout(1000);
                clientSocket.receive(receivePacketIP);
            }catch (SocketTimeoutException f) {
                try {
                    //query para Mater
                    char[] chars = {'c'}; //query Master
                    String master = "136.243.9.253";//Master - qwmaster.ocrana.de
                    //System.out.println(master);
                    int masterPort = 27000;

                    //System.out.println("Gera servList");
                    //convertendo char para byte um a um
                    buffer[0] = (byte) chars[0];//convertendo String do endereço
                    InetAddress masterIP = InetAddress.getByName(master);//Inciando o Socket
                    //criando pacote de envio
                    clientSocket = new DatagramSocket();

                    //criando pacote de envio
                    sendPacketIP = new DatagramPacket(buffer, buffer.length, masterIP, masterPort);
                    clientSocket.setSoTimeout(1000);
                    clientSocket.send(sendPacketIP);

                    //criando pacote para recebimento
                    receivePacketIP = new DatagramPacket(receiveDataIP, receiveDataIP.length);//recebendo
                    clientSocket.setSoTimeout(1000);
                    clientSocket.receive(receivePacketIP);
                }catch (SocketTimeoutException g) {
                    //mensagem
                    //System.out.println("Master fora do ar ou cheque sua conexao...");
                }
            }
        }

        //trata pacote recebido
        //byte to hexa array
        String hexString = byteArrayToHexString(receiveDataIP);
        //hex to string array (utilizado para preservar a integração dos dados)
        int[] serverData = new int[hexString.length()/2];
        j=0;
        for(i=12;i+12<hexString.length();i+=2){
            serverData[j] = Integer.parseInt(hexString.substring(i, i+2),16);
            //System.out.println(serverData[j]); //imprime array de ips
            j++;
        }
        //gera array de ip
        String[] serverList = new String[(serverData.length / 6)];
        int svCont = 0;
        j = 0;
        for (i = 0; i < serverList.length; i += 6) {
            serverList[j] = (serverData[i] + "." + serverData[i + 1] + "." + serverData[i + 2] + "." + serverData[i + 3] + ":" + //ip
                    (long) ((256 * serverData[i + 4]) + serverData[i + 5]));//porta
            if(serverData[i] != 0)
                svCont++;
            //System.out.println(serverList[j]); //imprime lista de servers
            j++;
        }

        /*
        //lista de servers BR only Manual
        String serverList[] = new String[11];
        serverList[0] = "200.98.115.241:27500";
        serverList[1] = "200.98.115.241:27501";
        serverList[2] = "200.98.115.241:27502";
        serverList[3] = "200.98.115.241:27503";
        serverList[4] = "201.39.70.27:28501";
        serverList[5] = "201.39.70.27:28502";
        serverList[6] = "201.39.70.27:28503";
        serverList[7] = "200.229.193.189:28501";
        serverList[8] = "200.229.193.189:28502";
        serverList[9] = "200.229.193.189:28503";
        serverList[10] = "201.39.70.27:28504";
        int svCont = 11;
*/
        //progressbar update
        bar.setProgress(0);
        //=========================================================================================
        //gera lista de players por server
        //=========================================================================================
        //System.out.println("svlist");
        int svlist;
        for (svlist = 0; svlist < svCont; svlist++) { //percorre toda lista de servers
            if (serverList[svlist] != null) {
                svHost = serverList[svlist].substring(0, serverList[svlist].indexOf(":")); //recebe IP do server
                svPort = Integer.parseInt(serverList[svlist].substring(serverList[svlist].indexOf(":") + 1, serverList[svlist].length())); //recebe porta do server
                if (svPort != 30000 && svPort != 28000
                        && svPort != 27599 && svPort != 30001 && svPort != 30002
                        && svPort != 30003 && svPort != 30004 && svPort != 27666
                        && svPort != 27510 && svPort != 30002 && svPort != 27500) {//FLAG FINAL DA LISTA DE SERVERS
                    //==============================================================================
                    //progressbar update
                    bar.setMax(svCont);
                    bar.setProgress(svlist);
                    //==============================================================================
                    svPeople(svHost, svPort);//executa metodo para listar players
                    host++;
                }
            }
        }




        //fecha socket
        clientSocket.close();

        //=========================================================================================
        //progressbar update
        bar.setMax(100);
        bar.setProgress(0);
        //=========================================================================================

        //marca flag status
        status = "Finished";

        //=========================================================================================
        //preogressbar update
        bar.setProgress(99);
        //=========================================================================================

        //retorna matriz final
        return svInfo;
    }

    //Metodo svPeople()
    public boolean svPeople(String svHost, int svPort) throws IOException {
        //System.out.println("svPeople()");
        clientSocket = new DatagramSocket();
        byte[] sendData = new byte[2048];//pacote de saida
        byte[] receiveData = new byte[2048];//pacote de retorno
        int bar;
        //svHost = "74.192.6.61";
        //svPort = 27500;
        //envia pacote
        InetAddress svIP = InetAddress.getByName(svHost);//Converte ip

        //converte char para byte
        sendData[0] = (byte) command[0];
        sendData[1] = (byte) command[1];
        sendData[2] = (byte) command[2];
        sendData[3] = (byte) command[3];
        sendData[4] = (byte) command[4];
        sendData[5] = (byte) command[5];
        sendData[6] = (byte) command[6];
        sendData[7] = (byte) command[7];
        sendData[8] = (byte) command[8];
        sendData[9] = (byte) command[9];
        sendData[10] = (byte) command[10];
        sendData[11] = (byte) command[11];
        sendData[12] = (byte) command[12];
        //cria pacote
        sendPacket = new DatagramPacket(sendData, sendData.length, svIP, svPort);//monta pacote
        //envia pacote
        clientSocket.setSoTimeout(1000);//timeout de envio
        clientSocket.send(sendPacket);//envia
        //cria pacote de retorno
        receivePacket = new DatagramPacket(receiveData, receiveData.length);//recebe pacote
        try {
            long start = new GregorianCalendar().getTimeInMillis();//marca tempo inicial de resposta para ping
            clientSocket.setSoTimeout(1000);//timeout de resposta
            clientSocket.receive(receivePacket);//recebe retorno
            long finish = new GregorianCalendar().getTimeInMillis();//marca tempo final de resposta para ping
            long ping = finish - start; //calculo ping
            clientSocket.close();
            String modifiedSentence = new String(receivePacket.getData());//converte retorno em string

            //retira da string Server Header
            //System.out.println("Server Header");
            String headSentence = modifiedSentence.trim().replace("\\", " ").replace("*", "").replace("\"", "").replace("\n", "");
            //System.out.println(modifiedSentence);//imprime retorno da query
            String SvStatus[] = headSentence.split(" ");
            svInfo[host][0] = svHost; //IP
            svInfo[host][1] = Integer.toString(svPort); // PORTA
            svInfo[host][2] = Long.toString(ping);
            for (i = 0; i < SvStatus.length; i++) {
                //System.out.println(SvStatus[i]); //EXIBE VETOR STATUS INTEIRO
                if ("gamedir".equals(SvStatus[i]))
                    svInfo[host][3] = SvStatus[i + 1]; //GAMEDIR
                if ("hostname".equals(SvStatus[i]))
                    svInfo[host][4] = SvStatus[i + 1]; //HOSTNAME
                if ("map".equals(SvStatus[i]))
                    svInfo[host][5] = SvStatus[i + 1]; //MAP
            }

            //Server People info Players
            //System.out.println("Info Players");
            //trata string de players
            String svPeople[] = modifiedSentence.replace("\n", "").replace("\\s", "").replace("/ ","").replace("\\", "").replace("*", "").replace("  ", " ").replace("   ", " ").split(" ");
            if ("fortress".equals(svInfo[host][3])) {
                j = 7;
                for (i = 0; i < svPeople.length; i++) {
                    if (svPeople[i].startsWith("\"")) {
                        for (bar = i - 2; bar < svPeople.length - 2; bar = bar + 7) {
                            svInfo[host][j] = svPeople[bar].replace("\"", "");
                            svInfo[host][j + 1] = svPeople[bar + 2].replace("\"", "");
                            svInfo[host][j + 2] = svPeople[bar + 3].replace("\"", "");
                            svInfo[host][j + 3] = "";
                            j = j + 4;
                        }
                        break;
                    }
                }
            } else {
                j = 7;
                for (i = 0; i < svPeople.length; i++) {
                    if (svPeople[i].startsWith("\"")) {
                        for (bar = i - 3; bar < svPeople.length - 3; bar = bar + 8) {
                            svInfo[host][j] = svPeople[bar].replace("\"", ""); //frags
                            svInfo[host][j + 1] = svPeople[bar + 2].replace("\"", ""); // ping
                            svInfo[host][j + 2] = svPeople[bar + 3].replace("\"", ""); // name
                            svInfo[host][j + 3] = svPeople[bar + 7].replace("\"", ""); // team
                            j = j + 4;
                        }
                        break;
                    }
                }
            }

            //Conta people por server
            //System.out.println("Conta People");
            int count = 0;
            i = 0;
            svInfo[host][6] = "0";
            while (svInfo[host][i] != null) {
                count++;
                i++;
            }
            svInfo[host][6] = Integer.toString(Math.round((count - 7) / 4));
//                //=======================================================================
//                //Imprime matriz svInfo[][] em tempo real
//                //=======================================================================
            j=0;
            while(svInfo[host][j]!=null){
                System.out.print(svInfo[host][j]+" / ");//mostra linha da matriz
                j++;
            }
            System.out.println("");
        } catch (Exception e){
            host--;
        }
        //pacote de saida
        return true;
    }
    //metodo converter array byte to string
    public static String byteArrayToHexString(byte[] array) {
        StringBuffer hexString = new StringBuffer();
        for (byte b : array) {
            int intVal = b & 0xff;
            if (intVal < 0x10)
                hexString.append("0");
            hexString.append(Integer.toHexString(intVal));
        }
        return hexString.toString();
    }
}