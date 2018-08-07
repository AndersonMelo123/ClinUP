package br.com.projetofragmeto.clinup.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;


@RunWith(Parameterized.class)
public class PlanoDeSaudeTest {

    @Parameterized.Parameter
    public PlanoDeSaude endTest;

    @Parameterized.Parameters
    public static Iterable<?> data() {
        String idUsuario = "8UE8TU8YET8";
        String id = "765SF67F6DF";
        String nomePlano = "UNIMED";
        String numPlano = "568464564564";

        return Arrays.asList(
                new PlanoDeSaude("", "", "", ""),
                new PlanoDeSaude(idUsuario, id, "", ""),
                new PlanoDeSaude(idUsuario, id, nomePlano, ""),
                new PlanoDeSaude(idUsuario, id, nomePlano, numPlano),
                new PlanoDeSaude(idUsuario, "", nomePlano, ""),
                new PlanoDeSaude(idUsuario, "", nomePlano, numPlano),
                new PlanoDeSaude(idUsuario, "", "", numPlano),
                new PlanoDeSaude("", id, "", ""),
                new PlanoDeSaude("", id, nomePlano, ""),
                new PlanoDeSaude("", id, nomePlano, numPlano),
                new PlanoDeSaude("", "", nomePlano, ""),
                new PlanoDeSaude("", "", nomePlano, numPlano),
                new PlanoDeSaude("", "", "", nomePlano),
                new PlanoDeSaude("", id, "", nomePlano));
    }

    @Test
    public void testCampoObg() {
        data();
    }
}