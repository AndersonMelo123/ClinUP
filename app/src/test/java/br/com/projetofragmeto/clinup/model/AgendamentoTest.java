package br.com.projetofragmeto.clinup.model;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class AgendamentoTest {

    @Parameterized.Parameter
    public Agendamento endTest;

    @Parameterized.Parameters
    public static Iterable<?> data() {
        String id_Usuario = "8UE8TU8YET8";
        String id_Cliente = "FA76TF8QFG8";
        String dataConsulta = "17/10/1994";
        String dataAtual = "05/08/2018";
        String nomeUsuario = "Francisco Ramiro";
        String id_Plano = "765SF67F6DF";

        return Arrays.asList(
                new Agendamento("", "", "", "","",""),
                new Agendamento(id_Usuario, id_Cliente, "", "","",""),
                new Agendamento(id_Usuario, id_Cliente, dataConsulta, "","",""),
                new Agendamento(id_Usuario, id_Cliente, dataConsulta, dataAtual,"",""),
                new Agendamento(id_Usuario, id_Cliente, dataConsulta, dataAtual,nomeUsuario,""),
                new Agendamento(id_Usuario, id_Cliente, dataConsulta, dataAtual,nomeUsuario,id_Plano),
                new Agendamento(id_Usuario, "", dataConsulta, "","",""),
                new Agendamento(id_Usuario, "", dataConsulta, "",nomeUsuario,""),
                new Agendamento(id_Usuario, "", dataConsulta, "",nomeUsuario,id_Plano),
                new Agendamento(id_Usuario, "", dataConsulta, dataAtual,"",""),
                new Agendamento(id_Usuario, "", dataConsulta, dataAtual,nomeUsuario,""),
                new Agendamento(id_Usuario, "", dataConsulta, dataAtual,nomeUsuario,id_Plano),
                new Agendamento(id_Usuario, "", "", dataAtual,"",""),
                new Agendamento(id_Usuario, "", "", dataAtual,nomeUsuario,""),
                new Agendamento(id_Usuario, "", "", dataAtual,nomeUsuario,id_Plano),
                new Agendamento("", id_Cliente, "", "","",""),
                new Agendamento("", id_Cliente, dataConsulta, "","",""),
                new Agendamento("", id_Cliente, dataConsulta, dataAtual,"",""),
                new Agendamento("", id_Cliente, dataConsulta, dataAtual,nomeUsuario,""),
                new Agendamento("", id_Cliente, dataConsulta, dataAtual,nomeUsuario,id_Plano),
                new Agendamento("", "", dataConsulta, "","",""),
                new Agendamento("", "", dataConsulta, dataAtual,"",""),
                new Agendamento("", "", dataConsulta, dataAtual,nomeUsuario,""),
                new Agendamento("", "", dataConsulta, dataAtual,nomeUsuario,id_Plano),
                new Agendamento("", "", "", dataConsulta,"",""),
                new Agendamento("", "", "", dataConsulta,nomeUsuario,""),
                new Agendamento("", "", "", dataConsulta,nomeUsuario,id_Plano),
                new Agendamento("", id_Cliente, "", dataConsulta,"",""),
                new Agendamento("", id_Cliente, "", dataConsulta,nomeUsuario,""),
                new Agendamento("", id_Cliente, "", dataConsulta,nomeUsuario,id_Plano));
    }

    @Test
    public void testCampoObg() {
        data();
    }

}