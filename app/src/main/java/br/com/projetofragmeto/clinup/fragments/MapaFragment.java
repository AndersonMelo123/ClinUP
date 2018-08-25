package br.com.projetofragmeto.clinup.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.activity.PerfilCliente;
import br.com.projetofragmeto.clinup.adapter.CustomInfoWindowAdapter;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.model.Clinica;
import br.com.projetofragmeto.clinup.model.Endereco;
import br.com.projetofragmeto.clinup.model.Hospital;
import br.com.projetofragmeto.clinup.model.InfoWindowData;
import br.com.projetofragmeto.clinup.model.Laboratorio;
import br.com.projetofragmeto.clinup.model.Profissional;

public class MapaFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleApiClient mGoogleApiClient;
    private MapView mMapView;
    private View mView;

    private GoogleMap mMap;

    private DatabaseReference mEnderecos;

    private LocationRequest locationRequest;

    private Location ultimaLocalizacao;

    final int REQUEST_PERMISSION_LOCALIZATION = 221;

    private Map<String, InfoWindowData> marcadores = new HashMap<>();

    private String[] clientes = {
            "clinica",
            "laboratorios",
            "profissionais",
            "hospitais"};


    public MapaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = buildGoogleApiClient();

        createLocationRequest();
    }

    public boolean getLocalization(Context context) {
        boolean res = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.

            res = false;
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCALIZATION);

        }
        return res;
    }

    // Solicitação para que seja permitido usar a localização do aplicativo
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION_LOCALIZATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    // Método responsável por ativar o monitoramento do GPS
    protected void startLocationUpdates() {
        try {
            /* code should explicitly check to see if permission is available
            (with 'checkPermission') or explicitly handle a potential 'SecurityException'
             */

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);

        } catch (SecurityException e) {
            Log.i("SecurityException", e.toString());

        }
    }

    public void setMyLocation(Location location) {
        if (location != null) {
            // Recupera latitude e longitude da ultima localização do usuário
            LatLng ultimaLocalizacao = new LatLng(location.getLatitude(), location.getLongitude());
            // Configuração da câmera
            final CameraPosition position = new CameraPosition.Builder()
                    .target(ultimaLocalizacao) // Localização
                    .bearing(0) // Rotação da câmera
                    .tilt(45) // ngulo em graus
                    .zoom(35) // Zoom
                    .build();
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
            mMap.animateCamera(update);
            // Criando um objeto do tipo MarkerOptions
            final MarkerOptions markerOptions = new MarkerOptions();
            // Configurando as propriedades do marker
            markerOptions.position(ultimaLocalizacao) // Localização
                    .title("Minha Localização") // Título
                    .snippet("Latitude:" + String.valueOf(location.getLatitude()) + "," +
                            "Longitude:" + String.valueOf(location.getLongitude())); // Descrição
            mMap.addMarker(markerOptions);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        int tamanhoClientes = clientes.length;
        int aux = 0;

        for (; tamanhoClientes > aux; aux++) {

            switch (clientes[aux]) {
                case "clinica":
                    String clinica = "clinica";
                    setandoOsClientesMapa(mMap, aux, clinica);

                    break;
                case "laboratorios":
                    String laboratorios = "laboratorios";
                    setandoOsClientesMapa(mMap, aux, laboratorios);

                    break;
                case "hospitais":
                    String hospitais = "hospitais";
                    setandoOsClientesMapa(mMap, aux, hospitais);

                    break;
                default:
                    String profissionais = "profissionais";
                    setandoOsClientesMapa(mMap, aux, profissionais);

                    break;
            }
        }
    }


    protected synchronized GoogleApiClient buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        return mGoogleApiClient;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_mapa, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
            if (getLocalization(getContext())) {
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);// Para o GPS
            mGoogleApiClient.disconnect();
        }
    }

    private Location getMyLocation() {
        try {
            /* code should explicitly check to see if permission is available
            (with 'checkPermission') or explicitly handle a potential 'SecurityException'
             */
            ultimaLocalizacao = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (ultimaLocalizacao != null) {
                Log.i("Localização", String.valueOf(ultimaLocalizacao.getLatitude() + ultimaLocalizacao.getLongitude()));
            } else {
                Log.i("Localização", "mLastLocation == null\n");
                //showSettingsAlert();

            }

        } catch (SecurityException e) {
            Log.i("SecurityException", e.toString());

        }
        return ultimaLocalizacao;
    }

    public void onConnected(@Nullable Bundle bundle) {

        Location location = getMyLocation();

        if (location == null) {
            startLocationUpdates();// Inicia o GPS
        }
        Log.i("TAGLOG", "Conectado ao Google Play Services!");
    }

    public void onConnectionSuspended(int i) {
        Log.i("TAGLOG", "Conexão Interrompida");
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("TAGLOG", "Erro ao conectar: " + connectionResult);
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    public void showSettingsAlert() {

        final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getContext());

        // Titulo do dialogo
        alertDialog.setTitle("GPS");

        // Mensagem do dialogo
        alertDialog.setMessage("GPS não está habilitado. Deseja configurar?");

        // botao ajustar configuracao
        alertDialog.setPositiveButton("Configurar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
                getActivity().finish();
            }
        });

        // botao cancelar
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // visualizacao do dialogo
        alertDialog.show();
    }

    private LatLng determineLatLngFromAddress(Context appContext, String strAddress) {
        LatLng latLng = null;
        Geocoder geocoder = new Geocoder(appContext, Locale.getDefault());
        List<Address> geoResults = null;

        try {
            geoResults = geocoder.getFromLocationName(strAddress, 1);
            while (geoResults.size() == 0) {
                geoResults = geocoder.getFromLocationName(strAddress, 1);
            }
            if (geoResults.size() > 0) {
                Address addr = geoResults.get(0);
                latLng = new LatLng(addr.getLatitude(), addr.getLongitude());
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        return latLng; //LatLng value of address
    }

    private void setandoOsClientesMapa(final GoogleMap googleMap, int aux, final String cliente) {

        mEnderecos = ConfiguracaoFirebase.getFirebase().child(clientes[aux]);

        googleMap.setOnMarkerClickListener(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mEnderecos.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                paraCadaClienteColetaInformacoes(cliente, dataSnapshot, googleMap);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    // Percorrento todos os clientes e coletando informações
    private void paraCadaClienteColetaInformacoes(String cliente,
                                                  DataSnapshot dataSnapshot,
                                                  GoogleMap googleMap) {

        String nome = " ";
        String endereco = " ";

        switch (cliente) {
            case "clinica":

                Clinica clinica = dataSnapshot.getValue(Clinica.class);
                nome = clinica.getNome();
                endereco = clinica.getEndereco();

                getEndereco(endereco, googleMap, nome, cliente, clinica);

                break;

            case "laboratorios":

                Laboratorio laboratorio = dataSnapshot.getValue(Laboratorio.class);
                nome = laboratorio.getNome();
                endereco = laboratorio.getEndereco();

                getEndereco(endereco, googleMap, nome, cliente, laboratorio);

                break;
            case "hospitais":

                Hospital hospital = dataSnapshot.getValue(Hospital.class);
                nome = hospital.getNome();
                endereco = hospital.getEndereco();

                getEndereco(endereco, googleMap, nome, cliente, hospital);

                break;
            default:

                Profissional profissional = dataSnapshot.getValue(Profissional.class);
                nome = profissional.getNome();
                endereco = profissional.getEndereco();

                getEndereco(endereco, googleMap, nome, cliente, profissional);

                break;
        }

    }

    // Método que coleta do banco o endereço e extraí a latitude e longitude
    private void getEndereco(String idEndereco, final GoogleMap googleMap,
                             final String nome, final String cliente, final Object classe) {

        final LatLng[] location = new LatLng[1];

        DatabaseReference bancoDados = ConfiguracaoFirebase.getFirebase().child("endereco").child(idEndereco);

        bancoDados.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Endereco endereco = dataSnapshot.getValue(Endereco.class);

                    String enderecoMarcador = endereco.getLogradouro()
                            + "," + endereco.getLocalidade()
                            + " " + endereco.getUf()
                            + " " + endereco.getBairro();

                    location[0] = determineLatLngFromAddress(getContext(), enderecoMarcador);

                    setarMarcador(location[0], googleMap, nome, cliente, classe);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Cria e adiciona todos os marcadores com as informações de cada cliente
    private void setarMarcador(LatLng latLng, GoogleMap googleMap, String nome, final String cliente, final Object classe) {

        //Marker
        MarkerOptions markerOpt = new MarkerOptions();

        markerOpt
                .position(latLng)
                .title(nome)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        final InfoWindowData info = new InfoWindowData();
        info.setImage("icone_perfil");
        info.setPerfil(getString(R.string.visualizar_perfil));
        info.setNome(nome);

        //Set Custom InfoWindow Adapter
        CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(getContext());
        googleMap.setInfoWindowAdapter(adapter);

        Marker m = googleMap.addMarker(markerOpt);
        m.setTag(info);
        m.showInfoWindow();

        CameraPosition Liberty = CameraPosition.builder()
                .target(latLng)
                .zoom(13)
                .bearing(0)
                .tilt(45)
                .build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(Liberty));

        coletandoInformacoesDosClientes(cliente, classe, info, m.getId());

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marcadores.containsKey(marker.getId())) {
                    InfoWindowData infoWindowData = marcadores.get(marker.getId());
                    setandoInformacoesClientesPerfil(infoWindowData);
                }
            }
        });
    }

    //Coleta as informações individualmente e as coloca em um arrayList
    private void coletandoInformacoesDosClientes(String cliente, Object classe, InfoWindowData infoWindowData, String marcador) {

        switch (cliente) {
            case "clinica":

                infoWindowData.setNome(((Clinica) classe).getNome());
                infoWindowData.setEmail(((Clinica) classe).getEmail());
                infoWindowData.setId(((Clinica) classe).getId());
                infoWindowData.setEndereco(((Clinica) classe).getEndereco());
                infoWindowData.setTelefone(((Clinica) classe).getTelefone());
                infoWindowData.setCnpj(((Clinica) classe).getCnpj());
                infoWindowData.setCliente("clinica");
                infoWindowData.setClasse(Clinica.class);

                marcadores.put(marcador, infoWindowData);


                break;

            case "laboratorios":

                infoWindowData.setNome(((Laboratorio) classe).getNome());
                infoWindowData.setEmail(((Laboratorio) classe).getEmail());
                infoWindowData.setId(((Laboratorio) classe).getId());
                infoWindowData.setTelefone(((Laboratorio) classe).getTelefone());
                infoWindowData.setCliente("laboratorios");
                infoWindowData.setClasse(Laboratorio.class);
                marcadores.put(marcador, infoWindowData);

                break;
            case "hospitais":


                infoWindowData.setNome(((Hospital) classe).getNome());
                infoWindowData.setEmail(((Hospital) classe).getEmail());
                infoWindowData.setId(((Hospital) classe).getId());
                infoWindowData.setEndereco(((Hospital) classe).getEndereco());
                infoWindowData.setTelefone(((Hospital) classe).getTelefone());
                infoWindowData.setCnpj(((Hospital) classe).getCnpj());
                infoWindowData.setCliente("hospitais");
                infoWindowData.setClasse(Hospital.class);
                marcadores.put(marcador, infoWindowData);

                break;
            default:

                infoWindowData.setNome(((Profissional) classe).getNome());
                infoWindowData.setEmail(((Profissional) classe).getId());
                infoWindowData.setId(((Profissional) classe).getId());
                infoWindowData.setEndereco(((Profissional) classe).getEndereco());
                infoWindowData.setTelefone(((Profissional) classe).getTelefone());
                infoWindowData.setEspecialidade(((Profissional) classe).getEspecialidade());
                infoWindowData.setFormacao(((Profissional) classe).getFormacao());
                infoWindowData.setNumRegistro(((Profissional) classe).getNum_registro());
                infoWindowData.setCliente("profissionais");
                infoWindowData.setClasse(Profissional.class);

                marcadores.put(marcador, infoWindowData);

                break;
        }
    }

    //Ao ser clicado um marcador ele envia o usuário para o perfil com as informações do cliente
    private void setandoInformacoesClientesPerfil(InfoWindowData infoWindowData) {
        Intent intent = new Intent(getActivity(), PerfilCliente.class);

        switch (infoWindowData.getCliente()) {
            case "clinica":

                intent.putExtra("nome", infoWindowData.getNome());
                intent.putExtra("email", infoWindowData.getEmail());
                intent.putExtra("id", infoWindowData.getId());
                intent.putExtra("endereco", infoWindowData.getEndereco());
                intent.putExtra("telefone", infoWindowData.getTelefone());
                intent.putExtra("cnpj", infoWindowData.getCnpj());

                intent.putExtra("cliente", "clinica");
                intent.putExtra("classe", Clinica.class);
                startActivity(intent);

                break;

            case "laboratorios":

                intent.putExtra("nome", infoWindowData.getNome());
                intent.putExtra("email", infoWindowData.getEmail());
                intent.putExtra("id", infoWindowData.getId());
                intent.putExtra("telefone", infoWindowData.getTelefone());

                intent.putExtra("cliente", "laboratorios");
                intent.putExtra("classe", Laboratorio.class);
                startActivity(intent);

                break;
            case "hospitais":

                intent.putExtra("nome", infoWindowData.getNome());
                intent.putExtra("email", infoWindowData.getEmail());
                intent.putExtra("id", infoWindowData.getId());
                intent.putExtra("endereco", infoWindowData.getEndereco());
                intent.putExtra("telefone", infoWindowData.getTelefone());
                intent.putExtra("cnpj", infoWindowData.getCnpj());

                intent.putExtra("cliente", "hospitais");
                intent.putExtra("classe", Hospital.class);
                startActivity(intent);

                break;
            default:
                intent.putExtra("nome", infoWindowData.getNome());
                intent.putExtra("email", infoWindowData.getId());
                intent.putExtra("id", infoWindowData.getId());
                intent.putExtra("endereco", infoWindowData.getEndereco());
                intent.putExtra("telefone", infoWindowData.getTelefone());
                intent.putExtra("especialidade", infoWindowData.getEspecialidade());
                intent.putExtra("formacao", infoWindowData.getFormacao());
                intent.putExtra("Num_registro", infoWindowData.getNumRegistro());

                intent.putExtra("cliente", "profissionais");
                intent.putExtra("classe", Profissional.class);
                startActivity(intent);

                break;
        }
    }

}