package br.com.projetofragmeto.clinup.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.projetofragmeto.clinup.R;

public class MapaFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    MapView mMapView;
    View mView;

    private Marker currentLocationMaker;
    private LatLng currentLocationLatLong;

    //Location ultimaLocalizacao = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient );

    public MapaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) getContext())
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) getContext())
                .addApi(LocationServices.API)
                .build();
        //setMyLocation(ultimaLocalizacao);*/
    }
/*
    public void setMyLocation(Location location) {
        if (location != null) {
            // Recupera latitude e longitude da
            // ultima localização do usuário
            LatLng ultimaLocalizacao = new LatLng(location.getLatitude(), location.getLongitude());
            // Configuração da câmera
            final CameraPosition position = new CameraPosition.Builder()
                    .target(ultimaLocalizacao) // Localização
                    .bearing(45) // Rotação da câmera
                    .tilt(90) // ngulo em graus
                    .zoom(17) // Zoom
                    .build();
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
            mGoogleMap.animateCamera(update);
// Criando um objeto do tipo MarkerOptions
            final MarkerOptions markerOptions = new MarkerOptions();
// Configurando as propriedades do marker
            markerOptions.position(ultimaLocalizacao) // Localização
                    .title("Minha Localização") // Título
                    .snippet("Latitude: , Longitude:"); // Descrição
            mGoogleMap.addMarker(markerOptions);
        }
    }
*/

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.addMarker(new MarkerOptions().position(new LatLng(-8.890212, -36.493041)).title("Local")
                .snippet("Estou aqui"));

        CameraPosition Liberty = CameraPosition.builder()
                .target(new LatLng(-8.890212, -36.493041))
                .zoom(18)
                .bearing(0)
                .tilt(45)
                .build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));


/*
//começou aqui
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Double lat = latLng.latitude;
                Double longi = latLng.longitude;

                Toast.makeText(getContext(), "lat: "+lat+"long: "+ longi, Toast.LENGTH_SHORT).show();
            }
        });

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Criar marker no marker
                MarkerOptions options = new MarkerOptions();
                options.position( latLng );
                mGoogleMap.addMarker( options );
                // Configurando as propriedades da Linha
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.add( new LatLng(-8.890212, -36.493041) );
                polylineOptions.add( latLng );
                polylineOptions.color(Color.BLUE );
// Adiciona a linha no mapa
                mGoogleMap.addPolyline( polylineOptions );
            }
        });
*/


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

        mMapView = (MapView) mView.findViewById(R.id.map);
        if (mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }
/*
    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    public void onConnected(@Nullable Bundle bundle) {
        Log.i("LOG", "Conectado ao Google Play Services!");
    }

    public void onConnectionSuspended(int i) {
        Log.i("LOG", "Conexão Interrompida");
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("LOG", "Erro ao conectar: " + connectionResult);
    }
*/
}
