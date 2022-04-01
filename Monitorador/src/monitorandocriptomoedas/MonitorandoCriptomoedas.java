package monitorandocriptomoedas;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;
import modelo.Criptomoeda;
import org.json.JSONObject;

/**
 *
 * @author RAS - DESKTOP
 */
public class MonitorandoCriptomoedas {

    public static void main(String[] args) throws IOException {
        String bitcoinString = JOptionPane.showInputDialog("Bitcoin - Valor Monitorado (R$): ");
        BigDecimal metaBitcoin = convertStringToBigDecimal(bitcoinString);

        String litecoinString = JOptionPane.showInputDialog("Litecoin - Valor Monitorado (R$): ");
        BigDecimal metaLitecoin = convertStringToBigDecimal(litecoinString);

        String rippleString = JOptionPane.showInputDialog("Ripple - Valor Monitorado (R$): ");
        BigDecimal metaRipple = convertStringToBigDecimal(rippleString);

        String chilizString = JOptionPane.showInputDialog("Chiliz - Valor Monitorado (R$): ");
        BigDecimal metaChiliz = convertStringToBigDecimal(chilizString);

        final long time = 300000; // 300.000 milessegundos ou 5 minutos
        Timer timer = new Timer();
        TimerTask tarefa = new TimerTask() {
            public void run() {
                try {
                    Boolean validador = Boolean.FALSE;
                    String mensagem = "";
                    
                    BigDecimal btcPreco = buscarPrecoMoeda("BTC");
                    if (btcPreco.compareTo(metaBitcoin) >= 1) {
                        validador = Boolean.TRUE;
                        mensagem = "Bitcoin R$ " + btcPreco;
                    }
                    
                    BigDecimal ltcPreco = buscarPrecoMoeda("LTC");
                    if (ltcPreco.compareTo(metaLitecoin) >= 1) {
                        validador = Boolean.TRUE;
                        mensagem = mensagem + "\nLitecoin R$ " + ltcPreco;
                    }
                    
                    BigDecimal xrpPreco = buscarPrecoMoeda("XRP");
                    if (xrpPreco.compareTo(metaRipple) >= 1) {
                        validador = Boolean.TRUE;
                        mensagem = mensagem + "\nRipple R$ " + xrpPreco;
                    }
                    
                    BigDecimal chzPreco = buscarPrecoMoeda("CHZ");
                    if (chzPreco.compareTo(metaChiliz) >= 1) {
                        validador = Boolean.TRUE;
                        mensagem = mensagem + "\nChiliz R$ " + chzPreco;
                    }
                    
                    if (validador.equals(Boolean.TRUE)) {
                        JOptionPane.showMessageDialog(null, mensagem);
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        timer.scheduleAtFixedRate(tarefa, time, time);
    }
    
    private static BigDecimal convertStringToBigDecimal(String valor) {
        String valorFormatado = valor.replace(",", ".");
        valorFormatado = valorFormatado.replace(" ", "");
        return new BigDecimal(valorFormatado);
    }
    
    private static BigDecimal buscarPrecoMoeda(String nomeMoeda) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.mercadobitcoin.net/api/" + nomeMoeda + "/ticker")
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "7e9f6cd2-6aa3-afe5-ab40-f59e25ca05e1")
                .build();
        
        Response response = client.newCall(request).execute();

        // TRANSFORMANDO RETORNO EM UMA STRING
        String jsonData = response.body().string();

        // DESPREZANDO INICIO DA STRING RETORNO
        Integer startIndex = jsonData.indexOf("ticker");

        // LIMPANDO RETORNO E FINALIZANDO A STRING COM CARACTERE INICIAL "["
        String jsonLimpo = jsonData.substring(startIndex + 9);

        JSONObject object = new JSONObject(jsonLimpo);
        Criptomoeda moeda = new Criptomoeda();
        moeda.setMaiorPreco(object.getBigDecimal("high"));
        moeda.setMenorPreco(object.getBigDecimal("low"));
        moeda.setVolume(object.getBigDecimal("vol"));
        moeda.setUltimoPreco(object.getBigDecimal("last"));
        moeda.setPrecoCompra(object.getBigDecimal("buy"));
        moeda.setPrecoVenda(object.getBigDecimal("sell"));
        moeda.setData(object.getInt("date"));
        
        return moeda.getUltimoPreco().setScale(3, RoundingMode.HALF_UP);
    }
}
