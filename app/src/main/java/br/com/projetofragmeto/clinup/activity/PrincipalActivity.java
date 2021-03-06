package br.com.projetofragmeto.clinup.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.database.OnGetDataListener;
import br.com.projetofragmeto.clinup.fragments.AgendamentosFragment;
import br.com.projetofragmeto.clinup.fragments.FavoritosFragment;
import br.com.projetofragmeto.clinup.fragments.MapaFragment;
import br.com.projetofragmeto.clinup.helper.Preferencias;
import br.com.projetofragmeto.clinup.model.Usuario;

public class PrincipalActivity extends AppCompatActivity {

    //Drawerr com valor nulo para fechar caso o usuario aperte "voltar"
    private Drawer result = null;

    private String nomeUser, emailUser, fotoUser;

    private ProgressDialog mProgressDialog;

    private FirebaseAuth autenticacaoUsuario;
    private GoogleApiClient googleApiClient;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        autenticacaoUsuario = ConfiguracaoFirebase.getFirebaseAutenticacao();

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Pegar o ID do usuário logado
        Preferencias preferencesUser = new Preferencias(PrincipalActivity.this);
        String idUsuarios = preferencesUser.getIdentificador();

        //Cria a imagem no navigation drawer
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }
        });

        //################################# - Google - ################################################

        // Para fazer o logout e encerrar a sessão do google caso ele esteja logado pelo google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(PrincipalActivity.this)
                .enableAutoManage(PrincipalActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(PrincipalActivity.this, "Login inválido", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //################################# - Google - ################################################

        mReadDataOnce(idUsuarios, new OnGetDataListener() {
            @Override
            public void onStart() {

                if (mProgressDialog == null) {
                    mProgressDialog = new ProgressDialog(PrincipalActivity.this);
                    mProgressDialog.setMessage(getString(R.string.carregando));
                    mProgressDialog.setIndeterminate(true);
                }
                mProgressDialog.show();
            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

                if (dataSnapshot.getValue() != null) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);

                    nomeUser = usuario.getNome();
                    emailUser = usuario.getEmail();

                    IProfile profile;

                    if (usuario.getFoto() != null) {
                        fotoUser = usuario.getFoto();
                        profile = new ProfileDrawerItem().withEmail(emailUser).withName(nomeUser).withIcon(fotoUser);

                    } else {
                        profile = new ProfileDrawerItem().withEmail(emailUser).withName(nomeUser).withIcon(R.mipmap.foto_defau_round);
                    }

                    // Cria o AccountHeader
                    AccountHeader headerResult = new AccountHeaderBuilder()
                            .withActivity(PrincipalActivity.this)
                            .withHeaderBackground(R.drawable.background_cadastro)
                            .addProfiles(profile)
                            .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                                @Override
                                public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                                    return false;
                                }
                            })
                            .build();

                    //Se você quiser atualizar os itens mais tarde, é recomendável mantê-los em uma variável
                    PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Home").withIcon(FontAwesome.Icon.faw_home);
                    PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("Perfil").withIcon(FontAwesome.Icon.faw_user);
                    PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("Agendamentos").withIcon(FontAwesome.Icon.faw_calendar_check);
                    PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName("Favoritos").withIcon(FontAwesome.Icon.faw_heart2);
                    PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName("Buscar").withIcon(FontAwesome.Icon.faw_search);

                    //Cria o drawer e lembra o 'Drawer' resulto objeto
                    result = new DrawerBuilder()
                            .withAccountHeader(headerResult)
                            .withActivity(PrincipalActivity.this)
                            .withToolbar(toolbar)
                            .addDrawerItems(
                                    item1,
                                    item2,
                                    item3,
                                    item4,
                                    item5
                            )
                            .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                //Cline no item do Navigation Drawer
                                @Override
                                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                    Intent intent = null;
                                    boolean valor = false;
                                    int item = 0;

                                    if (drawerItem != null) {
                                        if (drawerItem.getIdentifier() == 1) {

                                            intent = new Intent(PrincipalActivity.this, PrincipalActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        if (drawerItem.getIdentifier() == 2) {

                                            intent = new Intent(PrincipalActivity.this, PerfilActivity.class);
                                            startActivity(intent);

                                        }
                                        if (drawerItem.getIdentifier() == 3) {
                                            valor = true;
                                            item = 1;

                                        }
                                        if (drawerItem.getIdentifier() == 4) {
                                            valor = true;
                                            item = 2;

                                        }
                                        if (drawerItem.getIdentifier() == 5) {
                                            goToBuscaActivity();
                                        }

                                        if (drawerItem.getIdentifier() == 6) {
                                            deslogarUsuario();
                                        }
                                    }

                                    //Muda o fragmento
                                    if (valor) {
                                        viewPager.setCurrentItem(item);
                                    }
                                    return false;
                                }
                            })
                            .withTranslucentStatusBar(false)
                            .build();

                    result.addStickyFooterItem(new PrimaryDrawerItem().withIdentifier(6).withName("Sair").withIcon(FontAwesome.Icon.faw_sign_out_alt));
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
    }

    public void mReadDataOnce(String child, final OnGetDataListener listener) {
        listener.onStart();
        FirebaseDatabase.getInstance().getReference().child("usuarios").child(child).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });
    }

    private void deslogarUsuario() {

        logOutGoogle(); // Faz logout do google
        autenticacaoUsuario.signOut();
        FirebaseAuth.getInstance().signOut(); // Faz o logOut do banco
        LoginManager.getInstance().logOut(); // Faz o logOut do facebook

        goLogInScreen(); //Vai para a tela de login

    }

    public void logOutGoogle() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), "Sessão não foi fechada", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goLogInScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(PrincipalActivity.this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Perto de mim");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_mapa, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(PrincipalActivity.this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Agendamentos");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_agenda, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(PrincipalActivity.this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Favoritos");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_favoritos, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MapaFragment(), "Localização");
        adapter.addFragment(new AgendamentosFragment(), "Consultas");
        adapter.addFragment(new FavoritosFragment(), "Favoritos");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void goToBuscaActivity() {
        Intent intent = new Intent(getApplicationContext(), BuscaGeralActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // manipular a contrapressão: D fecha a gaveta primeiro e se a gaveta estiver fechada feche a atividade
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }


}