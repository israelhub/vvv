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

import java.util.ArrayList;
import java.util.List;



@Service
public class ApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Cidade[] getCidades() {
        String url = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/RJ/municipios";
        return restTemplate.getForObject(url, Cidade[].class);
    }

   /* private final static String Bearer YOUR_API_KEY = "xjodtwLrnLwFpHepDxqE30jlEBNWMMoNDl6QyVtz";
    public Aeroporto[] getAeroportos() {
        String url = "https://sharpapi.com/api/v1/airports?country=SG";
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer YOUR_API_KEY");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Aeroporto[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Aeroporto[].class);
        return response.getBody();
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