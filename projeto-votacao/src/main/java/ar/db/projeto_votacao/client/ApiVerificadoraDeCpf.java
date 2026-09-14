package ar.db.projeto_votacao.client;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ApiVerificadoraDeCpf {

    public RespostaHttpSimulada isCpfValido(String cpf) {
        String cpfLimpo = cpf.replace("[-./]", "");

        if (cpfLimpo.length() != 11) {
            return new RespostaHttpSimulada(400, "INVALID_CPF");
        }

        Random random = new Random();

        int aleatorio = random.nextInt(1, 10);

        //mantem uma taxa de aprovação maior que a de reprovação
        if (aleatorio > 3 && aleatorio <= 10) {
          return new RespostaHttpSimulada(200, "ABLE_TO_VOTE");
        }
        else {
           return new RespostaHttpSimulada(404, "UNABLE_TO_VOTE");
        }
    }

    public record RespostaHttpSimulada(int statusCode, String status) {
    }
}
