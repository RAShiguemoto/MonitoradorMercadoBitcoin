package monitorandocriptomoedas;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
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
    	
    	Map<String, BigDecimal> metas = new HashMap<>();
    	
    	try {
    		metas = getMetas();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	executarTarefa(metas);        
    }
    
	private static Map<String, BigDecimal> getMetas() throws Exception {
		Map<String, BigDecimal> metas = new HashMap<>();
		
		String metaPrecoString = JOptionPane.showInputDialog("Bitcoin - Valor Monitorado (R$): ");
		metas.put("BTC", convertStringToBigDecimal(metaPrecoString));

		metaPrecoString = JOptionPane.showInputDialog("Litecoin - Valor Monitorado (R$): ");
		metas.put("LTC", convertStringToBigDecimal(metaPrecoString));

		metaPrecoString = JOptionPane.showInputDialog("Ripple - Valor Monitorado (R$): ");
		metas.put("XRP", convertStringToBigDecimal(metaPrecoString));

		metaPrecoString = JOptionPane.showInputDialog("Chiliz - Valor Monitorado (R$): ");
		metas.put("CHZ", convertStringToBigDecimal(metaPrecoString));

		return metas;
	}
	
	private static void executarTarefa(Map<String, BigDecimal> metas) {
		final long CINCO_MINUTOS_EM_MILISSEGUNDOS = 300000;
		
        Timer timer = new Timer();
        
        TimerTask tarefa = new TimerTask() {
            public void run() {
                try {                	
                	boolean validador = false;
                    
                	String mensagem = "";
                    
                	BigDecimal precoMoeda;
                	
                	for (String key : metas.keySet()) {
                		precoMoeda = buscarPrecoMoeda(key);
                		
                		if (precoMoeda.compareTo(metas.get(key)) >= 1) {
                			validador = true;
                			mensagem = key + " R$ " + precoMoeda;
                		}
                	}
                    
                    if (validador == true) {
                        JOptionPane.showMessageDialog(null, mensagem);
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        
        timer.scheduleAtFixedRate(tarefa, CINCO_MINUTOS_EM_MILISSEGUNDOS, CINCO_MINUTOS_EM_MILISSEGUNDOS);
	}
    
    private static BigDecimal convertStringToBigDecimal(String valor) throws Exception {
    	
    	if (valor == null || valor.equals("") || !valor.matches("[0-9]+")) {
    		throw new IllegalArgumentException("Valor não pode ser nulo/vazio e deve conter apenas números.");
    	}
    	
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
