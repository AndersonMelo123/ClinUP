package br.com.projetofragmeto.clinup.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class ProfissionalTest {

    @Parameterized.Parameter
    public Profissional endTest;

    @Parameterized.Parameters
    public static Iterable<?> data() {
        String nome = "Francisco Ramiro";
        String endereco = "Rua da inconfidência";
        String especialidade = "computação";
        String formacao = "UFRPE";
        String num_registro = "765SF67F6DF";

        return Arrays.asList(
                new Profissional("", "", "", "",""),
                new Profissional(nome, endereco, "", "",""),
                new Profissional(nome, endereco, especialidade, "",""),
                new Profissional(nome, endereco, especialidade, formacao,""),
                new Profissional(nome, endereco, especialidade, formacao,num_registro),
                new Profissional(nome, "", especialidade, "",""),
                new Profissional(nome, "", especialidade, formacao,""),
                new Profissional(nome, "", especialidade, formacao,num_registro),
                new Profissional(nome, "", "", formacao,""),
                new Profissional(nome, "", "", formacao,num_registro),
                new Profissional("", endereco, "", "",""),
                new Profissional("", endereco, especialidade, "",""),
                new Profissional("", endereco, especialidade, formacao,""),
                new Profissional("", endereco, especialidade, formacao,num_registro),
                new Profissional("", "", especialidade, "",""),
                new Profissional("", "", especialidade, formacao,""),
                new Profissional("", "", especialidade, formacao,num_registro),
                new Profissional("", "", "", formacao,""),
                new Profissional("", "", "", formacao,num_registro),
                new Profissional("", endereco, "", formacao,""),
                new Profissional("", endereco, "", formacao,num_registro));
    }

    @Test
    public void testCampoObg() {
        data();
    }

}