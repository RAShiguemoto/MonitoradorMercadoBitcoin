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
        System.out.println("Bitcoin: " + metaBitcoin);

        String litecoinString = JOptionPane.showInputDialog("Litecoin - Valor Monitorado (R$): ");
        BigDecimal metaLitecoin = convertStringToBigDecimal(litecoinString);
        System.out.println("Litecoin: " + metaLitecoin);

        String rippleString = JOptionPane.showInputDialog("Ripple - Valor Monitorado (R$): ");
        BigDecimal metaRipple = convertStringToBigDecimal(rippleString);
        System.out.println("Ripple: " + metaRipple);

        String chilizString = JOptionPane.showInputDialog("Chiliz - Valor Monitorado (R$): ");
        BigDecimal metaChiliz = convertStringToBigDecimal(chilizString);
        System.out.println("Chiliz: " + metaChiliz);

        final long time = 600000; // a cada X ms (10 minutos)
        Timer timer = new Timer();
        TimerTask tarefa = new TimerTask() {
            public void run() {
                try {
                    Boolean validador = Boolean.FALSE;
                    String mensagem = "";
                    
                    BigDecimal btcPreco = verificaBitcoin();
                    if (btcPreco.compareTo(metaBitcoin) >= 1) {
                        validador = Boolean.TRUE;
                        mensagem = "Bitcoin R$ " + btcPreco;
                    }
                    
                    BigDecimal ltcPreco = verificaLitecoin();
                    if (ltcPreco.compareTo(metaLitecoin) >= 1) {
                        validador = Boolean.TRUE;
                        mensagem = mensagem + "\nLitecoin R$ " + ltcPreco;
                    }
                    
                    BigDecimal xrpPreco = verificaRipple();
                    if (xrpPreco.compareTo(metaRipple) >= 1) {
                        validador = Boolean.TRUE;
                        mensagem = mensagem + "\nRipple R$ " + xrpPreco;
                    }
                    
                    BigDecimal chzPreco = verificaChiliz();
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
    
    private static BigDecimal verificaBitcoin() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.mercadobitcoin.net/api/BTC/ticker")
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
        Criptomoeda bitcoin = new Criptomoeda();
        bitcoin.setMaiorPreco(object.getBigDecimal("high"));
        bitcoin.setMenorPreco(object.getBigDecimal("low"));
        bitcoin.setVolume(object.getBigDecimal("vol"));
        bitcoin.setUltimoPreco(object.getBigDecimal("last"));
        bitcoin.setPrecoCompra(object.getBigDecimal("buy"));
        bitcoin.setPrecoVenda(object.getBigDecimal("sell"));
        bitcoin.setData(object.getInt("date"));

        System.out.println("** Bitcoin ** ");
        System.out.println("Maior Preço: " + bitcoin.getMaiorPreco().setScale(3, RoundingMode.HALF_UP));
        System.out.println("Menor Preço: " + bitcoin.getMenorPreco().setScale(3, RoundingMode.HALF_UP));
        System.out.println("Volume: " + bitcoin.getVolume().setScale(3, RoundingMode.HALF_UP));
        System.out.println("Ultimo Preço: " + bitcoin.getUltimoPreco().setScale(3, RoundingMode.HALF_UP));
        System.out.println("Preço Compra: " + bitcoin.getPrecoCompra().setScale(3, RoundingMode.HALF_UP));
        System.out.println("Preço Venda: " + bitcoin.getPrecoVenda().setScale(3, RoundingMode.HALF_UP));
        
        return bitcoin.getUltimoPreco().setScale(3, RoundingMode.HALF_UP);
    }
    
    private static BigDecimal verificaLitecoin() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.mercadobitcoin.net/api/LTC/ticker")
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
        Criptomoeda litecoin = new Criptomoeda();
        litecoin.setMaiorPreco(object.getBigDecimal("high"));
        litecoin.setMenorPreco(object.getBigDecimal("low"));
        litecoin.setVolume(object.getBigDecimal("vol"));
        litecoin.setUltimoPreco(object.getBigDecimal("last"));
        litecoin.setPrecoCompra(object.getBigDecimal("buy"));
        litecoin.setPrecoVenda(object.getBigDecimal("sell"));
        litecoin.setData(object.getInt("date"));

        System.out.println("** Bitcoin ** ");
        System.out.println("Maior Preço: " + litecoin.getMaiorPreco().setScale(3, RoundingMode.HALF_UP));
        System.out.println("Menor Preço: " + litecoin.getMenorPreco().setScale(3, RoundingMode.HALF_UP));
        System.out.println("Volume: " + litecoin.getVolume().setScale(3, RoundingMode.HALF_UP));
        System.out.println("Ultimo Preço: " + litecoin.getUltimoPreco().setScale(3, RoundingMode.HALF_UP));
        System.out.println("Preço Compra: " + litecoin.getPrecoCompra().setScale(3, RoundingMode.HALF_UP));
        System.out.println("Preço Venda: " + litecoin.getPrecoVenda().setScale(3, RoundingMode.HALF_UP));
        
        return litecoin.getUltimoPreco().setScale(3, RoundingMode.HALF_UP);
    }
    
    private static BigDecimal verificaRipple() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.mercadobitcoin.net/api/XRP/ticker")
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
        Criptomoeda ripple = new Criptomoeda();
        ripple.setMaiorPreco(object.getBigDecimal("high"));
        ripple.setMenorPreco(object.getBigDecimal("low"));
        ripple.setVolume(object.getBigDecimal("vol"));
        ripple.setUltimoPreco(object.getBigDecimal("last"));
        ripple.setPrecoCompra(object.getBigDecimal("buy"));
        ripple.setPrecoVenda(object.getBigDecimal("sell"));
        ripple.setData(object.getInt("date"));

        System.out.println("** Bitcoin ** ");
        System.out.println("Maior Preço: " + ripple.getMaiorPreco().setScale(3, RoundingMode.HALF_UP));
        System.out.println("Menor Preço: " + ripple.getMenorPreco().setScale(3, RoundingMode.HALF_UP));
        System.out.println("Volume: " + ripple.getVolume().setScale(3, RoundingMode.HALF_UP));
        System.out.println("Ultimo Preço: " + ripple.getUltimoPreco().setScale(3, RoundingMode.HALF_UP));
        System.out.println("Preço Compra: " + ripple.getPrecoCompra().setScale(3, RoundingMode.HALF_UP));
        System.out.println("Preço Venda: " + ripple.getPrecoVenda().setScale(3, RoundingMode.HALF_UP));
        
        return ripple.getUltimoPreco().setScale(3, RoundingMode.HALF_UP);
    }
    
    private static BigDecimal verificaChiliz() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.mercadobitcoin.net/api/CHZ/ticker")
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
        Criptomoeda chiliz = new Criptomoeda();
        chiliz.setMaiorPreco(object.getBigDecimal("high"));
        chiliz.setMenorPreco(object.getBigDecimal("low"));
        chiliz.setVolume(object.getBigDecimal("vol"));
        chiliz.setUltimoPreco(object.getBigDecimal("last"));
        chiliz.setPrecoCompra(object.getBigDecimal("buy"));
        chiliz.setPrecoVenda(object.getBigDecimal("sell"));
        chiliz.setData(object.getInt("date"));

        System.out.println("** Bitcoin ** ");
        System.out.println("Maior Preço: " + chiliz.getMaiorPreco().setScale(3, RoundingMode.HALF_UP));
        System.out.println("Menor Preço: " + chiliz.getMenorPreco().setScale(3, RoundingMode.HALF_UP));
        System.out.println("Volume: " + chiliz.getVolume().setScale(3, RoundingMode.HALF_UP));
        System.out.println("Ultimo Preço: " + chiliz.getUltimoPreco().setScale(3, RoundingMode.HALF_UP));
        System.out.println("Preço Compra: " + chiliz.getPrecoCompra().setScale(3, RoundingMode.HALF_UP));
        System.out.println("Preço Venda: " + chiliz.getPrecoVenda().setScale(3, RoundingMode.HALF_UP));
        
        return chiliz.getUltimoPreco().setScale(3, RoundingMode.HALF_UP);
    }
}
