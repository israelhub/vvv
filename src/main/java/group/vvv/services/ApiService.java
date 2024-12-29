package group.vvv.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.vvv.models.viagem.Cidade;
import group.vvv.models.viagem.Aeroporto;
import group.vvv.models.viagem.Porto;
import group.vvv.models.viagem.Estacao;
import group.vvv.models.viagem.Estado;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class ApiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final static String API_KEY = "xjodtwLrnLwFpHepDxqE30jlEBNWMMoNDl6QyVtz";

    public List<Estado> getEstados() {
        String url = "https://servicodados.ibge.gov.br/api/v1/localidades/estados";
        return List.of(restTemplate.getForObject(url, Estado[].class));
    }

    public List<Cidade> getCidadesPorEstado(String estadoSigla) {
        String url = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/" + estadoSigla + "/municipios";
        return List.of(restTemplate.getForObject(url, Cidade[].class));
    }

/*   public Aeroporto[] getAeroportosPorCidade(String estadoSigla) {
        String url = "https://sharpapi.com/api/v1/airports?country=BR&state=" + estadoSigla;
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer " + API_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            String jsonResponse = response.getBody();

            // Logar a resposta JSON para depuração
            System.out.println("Resposta JSON: " + jsonResponse);

            // Converter a resposta JSON para uma árvore de nós para inspeção
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            System.out.println("Estrutura JSON: " + rootNode.toPrettyString());

            // Converter a resposta JSON para o tipo Aeroporto[]
            return objectMapper.readValue(jsonResponse, Aeroporto[].class);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar a resposta JSON", e);
        }
    } */


    /*
     * public Porto[] getPortos() {
     * String url = "URL_DA_API_DE_PORTOS";
     * return restTemplate.getForObject(url, Porto[].class);
     * }
     * 
     * public Estacao[] getEstacoes() {
     * String url = "URL_DA_API_DE_ESTACOES";
     * return restTemplate.getForObject(url, Estacao[].class);
     * }
     */
}