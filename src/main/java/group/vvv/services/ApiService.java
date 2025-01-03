/*package group.vvv.services;

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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


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

    public List<Aeroporto> getAeroportosPorEstado(String estadoSigla) {
        String url = "https://sistemas.anac.gov.br/dadosabertos/Aerodromos/Aer%C3%B3dromos%20P%C3%BAblicos/Lista%20de%20aer%C3%B3dromos%20p%C3%BAblicos/AerodromosPublicos.json";
        Aeroporto[] aeroportos = restTemplate.getForObject(url, Aeroporto[].class);
        return Arrays.stream(aeroportos)
                     .filter(aeroporto -> aeroporto.getEstado().equalsIgnoreCase(estadoSigla))
                     .collect(Collectors.toList());
    }

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
/* } */