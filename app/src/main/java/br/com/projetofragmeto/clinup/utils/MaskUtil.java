package br.com.projetofragmeto.clinup.utils;

import android.widget.EditText;

import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.pattern.MaskPattern;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

public class MaskUtil {

    public void maskCPF(EditText cpf) {
        // Criando as mascaras
        SimpleMaskFormatter nCpf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mCpf = new MaskTextWatcher(cpf, nCpf);
        cpf.addTextChangedListener(mCpf);
        //FIM DA MÁSCARA

    }

    public void maskDATA(EditText dataNascimento) {

        // Criando as mascara
        MaskPattern mp03 = new MaskPattern("[0-3]");
        MaskPattern mp09 = new MaskPattern("[0-9]");
        MaskPattern mp01 = new MaskPattern("[0-1]");

        MaskFormatter mf = new MaskFormatter("[0-3][0-9]/[0-1][0-9]/[0-9][0-9][0-9][0-9]");

        mf.registerPattern(mp01);
        mf.registerPattern(mp03);
        mf.registerPattern(mp09);

        dataNascimento.addTextChangedListener(new MaskTextWatcher(dataNascimento, mf));
        //FIM DA MÁSCARA

    }

    public void maskTelefone(EditText Telefone) {
        // Criando as mascaras
        SimpleMaskFormatter nNumTelefone = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher mNumTelefone = new MaskTextWatcher(Telefone, nNumTelefone);
        Telefone.addTextChangedListener(mNumTelefone);
        //FIM DA MÁSCARA

    }
}

