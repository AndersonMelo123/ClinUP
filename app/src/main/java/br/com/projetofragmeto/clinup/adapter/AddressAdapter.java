package br.com.projetofragmeto.clinup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.model.Endereco;


public class AddressAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Endereco> enderecos;

    public AddressAdapter(Context context, List<Endereco> enderecos){
        inflater = LayoutInflater.from(context);
        this.enderecos = enderecos;
    }

    @Override
    public int getCount() {
        return enderecos.size();
    }

    @Override
    public Object getItem(int i) {
        return enderecos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if( view == null ){
            view = inflater.inflate(R.layout.address_item, null);
            holder = new ViewHolder();
            view.setTag( holder );
            holder.setViews( view );
        }
        else{
            holder = (ViewHolder) view.getTag();
        }

        holder.setData( enderecos.get(i) );

        return view;
    }


    private static class ViewHolder{
        TextView tvZipCode;
        TextView tvStreet;
        TextView tvNeighbor;

        private void setViews( View view ){
            tvZipCode = (TextView) view.findViewById(R.id.tv_zip_code);
            tvStreet = (TextView) view.findViewById(R.id.tv_street);
            tvNeighbor = (TextView) view.findViewById(R.id.tv_neighbor);
        }

        private void setData( Endereco endereco){
            tvZipCode.setText( "CEP: "+ endereco.getCep() );
            tvStreet.setText( "Rua: "+ endereco.getLogradouro() );
            tvNeighbor.setText( "Bairro: "+ endereco.getBairro() );
        }
    }

}
