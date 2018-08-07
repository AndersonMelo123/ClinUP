package br.com.projetofragmeto.clinup.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class UsuarioTest {

    @Parameterized.Parameter
    public Usuario endTest;

    @Parameterized.Parameters
    public static Iterable<?> data() {
        String id = "765SF67F6DF";
        String nome = "Francisco Ramiro";
        String email = "franciscolejes@gmail.com";
        String foto = "user.png";

        return Arrays.asList(
                new Usuario("", "", "", ""),
                new Usuario(id, nome, "", ""),
                new Usuario(id, nome, email, ""),
                new Usuario(id, nome, email, foto),
                new Usuario(id, "", email, ""),
                new Usuario(id, "", email, foto),
                new Usuario(id, "", "", foto),
                new Usuario("", nome, "", ""),
                new Usuario("", nome, email, ""),
                new Usuario("", nome, email, foto),
                new Usuario("", "", email, ""),
                new Usuario("", "", email, foto),
                new Usuario("", "", "", foto),
                new Usuario("", nome, "", foto));
    }

    @Test
    public void testCampoObg() {
        data();
    }

}