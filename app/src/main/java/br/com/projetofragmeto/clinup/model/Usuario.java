package br.com.projetofragmeto.clinup.model;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;

import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.database.PlanoDeSaudeImplements;

/*Adicionando a interface serializable será possível transformar o objeto num formato que poderá ser salvo num arquivo.
 Por exemplo, para utilizar um ObjectOutputStream e salvar um objeto num arquivo do disco será necessário implementar essa interface.*/

public class Usuario implements Serializable {

    private String idUsuario;
    private String id;

    private String nome;//
    private String email;//
    private String senha;//

    private String cpf;//
    private String telefone;//
    private String dataNascimento;//
    private String estadoCivil;
    private String foto;

    private String numPais;
    private String numEstado;
    private String numTelefone;

    private Endereco endereco;

    private PlanoDeSaude plano;//
    private String nomePlano;//
    private String numPlano;//

    public Usuario(String nome, String email, String foto) {
        this.nome = nome;
        this.email = email;
        this.foto = foto;
    }

    public Usuario(String id, String nome, String email, String foto) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.foto = foto;
    }

    public Usuario() {
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNumPais() {
        return numPais;
    }

    public void setNumPais(String numPais) {
        this.numPais = numPais;
    }

    public String getNumEstado() {
        return numEstado;
    }

    public void setNumEstado(String numEstado) {
        this.numEstado = numEstado;
    }

    public String getNumTelefone() {
        return numTelefone;
    }

    public void setNumTelefone(String numTelefone) {
        this.numTelefone = numTelefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }


    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void salvar(){
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("usuarios").child( getId() ).setValue( this );
    }


}
