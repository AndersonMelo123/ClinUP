package br.com.projetofragmeto.clinup.activity;

import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.TestTools;

/**
 * Created by Francisco.
 * Classe que executa os testes automatizados para login
 */


public class LoginActivityTest {

    private UiDevice mDevice;

    @Rule
    public ActivityTestRule <LoginActivity> loginActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() throws Exception {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        //Desloga caso já esteja logado.
        //Evita erros nos testes
        /*try{
            TestTools.clicarItemNavigationMenu(R.id.drawer_layout, R.id.nav_busca, R.id.nav_sair);
        }catch (Exception e){
            e.getMessage();
        }*/
    }


    //Teste que simula uma tentativa de login pelo Google confirmando autorização
    //  @Test
    public void testeLoginComSucessoGoogle() throws UiObjectNotFoundException, InterruptedException {
        TestTools.clicarBotao(R.id.btnLoginGoogle);
        Thread.sleep(2000);
        UiObject buttonInput = mDevice.findObject(new UiSelector().instance(0).className(ListView.class));
        if(buttonInput.exists()) buttonInput.click();
        Thread.sleep(2000);
        UiObject buttonInput2 = mDevice.findObject(new UiSelector().text("NÃO"));
        if(buttonInput2.exists()) buttonInput2.click();
    }

    //Teste que simula uma tentativa de login pelo Google cancelando autorização
    //@Test
    public void testeLoginCanceladoGoogle() throws UiObjectNotFoundException {
        TestTools.clicarBotao(R.id.btnLoginGoogle);
        mDevice.pressBack();
    }

    //Teste que simula uma tentativa de login pelo Facebook cancelando autorização
    @Test
    public void testeLoginComFalhaFacebook() throws UiObjectNotFoundException, InterruptedException {
        TestTools.clicarBotao(R.id.login_facebook_button);
        SystemClock.sleep(20000);
        UiObject buttonInput = mDevice.findObject(new UiSelector().instance(0).className(Button.class));
        buttonInput.click();
    }


    //Teste que simula uma tentativa de login pelo Facebook preenchendo os dados e confirmando autorização
    @Test
    public void testeLoginPreencherDadosComSucessoFacebook() throws UiObjectNotFoundException {
        TestTools.clicarBotao(R.id.login_facebook_button);
        SystemClock.sleep(20000);
        UiObject input = mDevice.findObject(new UiSelector().instance(0).className(EditText.class));
        input.setText("clinup_teste@gmail.com");
        SystemClock.sleep(2000);
        UiObject input2 = mDevice.findObject(new UiSelector().instance(1).className(EditText.class));
        input2.setText("DBOA8EATK3K");
        UiObject buttonInput = mDevice.findObject(new UiSelector().instance(0).className(Button.class));
        buttonInput.click();
        UiObject buttonInput2 = mDevice.findObject(new UiSelector().instance(0).className(Button.class));
        buttonInput2.click();
    }

    //Teste que simula uma tentativa de login pelo Facebook preenchendo os dados e cancelando autorização
    @Test
    public void testeLoginPreencherDadosECancelarFacebook() throws UiObjectNotFoundException {
        TestTools.clicarBotao(R.id.login_facebook_button);
        SystemClock.sleep(20000);
        UiObject input = mDevice.findObject(new UiSelector().instance(0).className(EditText.class));
        input.setText("clinup_teste@gmail.com");
        SystemClock.sleep(2000);
        UiObject input2 = mDevice.findObject(new UiSelector().instance(1).className(EditText.class));
        input2.setText("DBOA8EATK3K");
        UiObject buttonInput = mDevice.findObject(new UiSelector().instance(0).className(Button.class));
        buttonInput.click();
        UiObject buttonInput2 = mDevice.findObject(new UiSelector().instance(1).className(Button.class));
        buttonInput2.click();
        mDevice.pressBack();
    }

    @Test
    public void testLoginEmail() throws UiObjectNotFoundException{
        SystemClock.sleep(20000);
        UiObject input = mDevice.findObject(new UiSelector().instance(0).className(EditText.class));
        input.setText("clinup_teste@gmail.com");
        SystemClock.sleep(2000);
        UiObject input2 = mDevice.findObject(new UiSelector().instance(1).className(EditText.class));
        input2.setText("DBOA8EATK3K");
        UiObject buttonInput = mDevice.findObject(new UiSelector().instance(0).className(Button.class));
        buttonInput.click();
    }



    @After
    public void tearDown() throws Exception {
        Thread.sleep(3000);
    }


}