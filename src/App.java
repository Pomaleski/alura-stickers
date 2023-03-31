import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

public class App {
    public static void main(String[] args) throws Exception {
        
        // pegando a chave da api de um arquivo .env 
        Properties props = new Properties();
        try {
            FileInputStream envFile = new FileInputStream(".env");
            props.load(envFile);
            envFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String API_KEY = props.getProperty("NASA_API_KEY");

        // fazer uma conexão HTTP com API
        // String url = "https://api.nasa.gov/planetary/apod?api_key=" + API_KEY
        //     + "&start_date=2023-02-26&end_date=2023-02-28";

        String url = "http://localhost:8080/linguagens";

        var http = new ClienteHttp();
        String json = http.buscaDados(url);

        // extrair de dados em json
        ExtratorDeConteudo extrator =
            new ExtratorDeConteudoDoIMDB();
            // new ExtratorDeConteudoDaNasa();
        List<Conteudo> conteudos = extrator.extraiConteudos(json);

        // exibir e manipular os dados
        var geradora = new GeradoraDeFigurinhas();
        
        for (Conteudo conteudo : conteudos) {

            InputStream inputStream = new URL(conteudo.getUrlImagem()).openStream();
            String nomeArquivo = conteudo.getTitulo() + ".png";

            geradora.cria(inputStream, nomeArquivo);
        }        
    }
}
