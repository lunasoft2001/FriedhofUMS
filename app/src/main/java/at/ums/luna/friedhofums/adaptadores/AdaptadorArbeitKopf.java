package at.ums.luna.friedhofums.adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.modelo.ArbeitKopf;

/**
 * Created by luna-aleixos on 16.08.2016.
 */
public class AdaptadorArbeitKopf extends BaseAdapter implements Filterable {

    public Context context;
    public ArrayList<ArbeitKopf> objetoArrayList;
    public ArrayList<ArbeitKopf> origen;

    public AdaptadorArbeitKopf(Context context, ArrayList<ArbeitKopf> objetoArrayList){
        super();
        this.context = context;
        this.objetoArrayList = objetoArrayList;
    }

    public class ObjetoHolder{
        TextView tvidTitulo;
        TextView tvTerminado;
    }


    @Override
    public int getCount() {
        return objetoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return objetoArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ObjetoHolder holder;
        if(convertView == null){
            convertView= LayoutInflater.from(context).inflate(R.layout.fila_lista_arbeit_kopf, parent,false);
            holder = new ObjetoHolder();
            holder.tvidTitulo = (TextView) convertView.findViewById(R.id.textViewTitulo);
            holder.tvTerminado = (TextView) convertView.findViewById(R.id.textViewTerminado);
            convertView.setTag(holder);
        }
        else{
            holder=(ObjetoHolder)convertView.getTag();
        }

        String terminado="IN ERLEDIGUNG";

        holder.tvidTitulo.setText(objetoArrayList.get(position).getTitle());
        if(objetoArrayList.get(position).isTerminado()){
            terminado = "ERLEDIGT";
            holder.tvidTitulo.setTextColor(Color.parseColor("#8BC34A"));
        }
        holder.tvTerminado.setText("");

        return convertView;

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<ArbeitKopf> resultados = new ArrayList<>();
                if (origen == null)
                    origen = objetoArrayList;
                if(constraint != null){
                    if(origen != null & origen.size() >0) {
                        for(final ArbeitKopf g : origen) {
                            if(g.getTitle().toLowerCase().contains(constraint.toString()))
                                resultados.add(g);
                        }
//                        for(final ArbeitKopf g : origen) {
//                            if(g.getMitarbeiter().toLowerCase().contains(constraint.toString()))
//                                resultados.add(g);
//                        }

                    }



                    oReturn.values = resultados;
                }

                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                objetoArrayList = (ArrayList<ArbeitKopf>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
