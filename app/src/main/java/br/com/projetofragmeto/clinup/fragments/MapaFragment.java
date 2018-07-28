package br.com.projetofragmeto.clinup.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.projetofragmeto.clinup.R;

public class MapaFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap; //variavel q será usada para exibir o mapa
    MapView mMapView;
    View mView;

    public MapaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) { // método q exibe o mapa

        MapsInitializer.initialize(getContext());//o mapa é inicializado

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); //o tipo do mapa é definido

        //é adicionado o marcador marker com a posição definida
        googleMap.addMarker(new MarkerOptions().position(new LatLng(-8.890212, -36.493041)).title("Local")
                .snippet("Estou aqui"));

        //é feita a configuração da camera que irá mostrar os elementos no mapa
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
