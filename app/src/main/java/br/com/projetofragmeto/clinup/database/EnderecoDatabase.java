package br.com.projetofragmeto.clinup.database;

import br.com.projetofragmeto.clinup.model.Endereco;

public interface EnderecoDatabase {
    void inserir(Endereco endereco, String idUsuario);
}
