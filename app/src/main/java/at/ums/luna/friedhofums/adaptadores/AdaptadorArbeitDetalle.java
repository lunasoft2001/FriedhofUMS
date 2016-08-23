package at.ums.luna.friedhofums.adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;

import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.modelo.ArbeitDetail;
import at.ums.luna.friedhofums.modelo.ArbeitKopf;

/**
 * Created by luna-aleixos on 19.08.2016.
 */
public class AdaptadorArbeitDetalle extends BaseAdapter implements Filterable {

    public Context context;
    public ArrayList<ArbeitDetail> objetoArrayList;
    public ArrayList<ArbeitDetail> origen;

    public AdaptadorArbeitDetalle(Context context, ArrayList<ArbeitDetail> objetoArrayList){
        super();
        this.context = context;
        this.objetoArrayList = objetoArrayList;
    }

    public class ObjetoHolder{
        TextView tvIdGrab;
        TextView tvGrabname;
        TextView tvDetalle;
        ImageView icoTierra;
        ImageView icoRegar;
        ImageView icoRecoger;
        ImageView icoPlantar;
        ImageView icoPflege;
        ImageView icoLimpiar;
        ImageView icoDecorar;
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
            convertView= LayoutInflater.from(context).inflate(R.layout.fila_lista_detalle, parent,false);
            holder = new ObjetoHolder();
            holder.tvIdGrab = (TextView) convertView.findViewById(R.id.textViewIdGrab);
            holder.tvGrabname = (TextView) convertView.findViewById(R.id.textViewGrabname);
            holder.tvDetalle = (TextView) convertView.findViewById(R.id.textViewDetalle);
            holder.icoTierra = (ImageView) convertView.findViewById(R.id.botonTierra);
            holder.icoRegar = (ImageView) convertView.findViewById(R.id.botonRegar);
            holder.icoRecoger = (ImageView) convertView.findViewById(R.id.botonRecoger);
            holder.icoPlantar = (ImageView) convertView.findViewById(R.id.botonPlantar);
            holder.icoPflege = (ImageView) convertView.findViewById(R.id.botonPflege);
            holder.icoLimpiar = (ImageView) convertView.findViewById(R.id.botonLimpiar);
            holder.icoDecorar = (ImageView) convertView.findViewById(R.id.botonDecorar);
            convertView.setTag(holder);
        }
        else{
            holder=(ObjetoHolder)convertView.getTag();
        }

        holder.tvIdGrab.setText(objetoArrayList.get(position).getGrab().getIdGrab());
        holder.tvGrabname.setText(objetoArrayList.get(position).getGrab().getGrabname());
        holder.tvDetalle.setText(objetoArrayList.get(position).getObservaciones());
        holder.icoTierra.setAlpha(objetoArrayList.get(position).transparenciaTarea(objetoArrayList.get(position).getTierra()));
        holder.icoRegar.setAlpha(objetoArrayList.get(position).transparenciaTarea(objetoArrayList.get(position).getRegar()));
        holder.icoRecoger.setAlpha(objetoArrayList.get(position).transparenciaTarea(objetoArrayList.get(position).getRecoger()));
        holder.icoPlantar.setAlpha(objetoArrayList.get(position).transparenciaTarea(objetoArrayList.get(position).getPlantar()));
        holder.icoPflege.setAlpha(objetoArrayList.get(position).transparenciaTarea(objetoArrayList.get(position).getPflege()));
        holder.icoLimpiar.setAlpha(objetoArrayList.get(position).transparenciaTarea(objetoArrayList.get(position).getLimpiar()));
        holder.icoDecorar.setAlpha(objetoArrayList.get(position).transparenciaTarea(objetoArrayList.get(position).getDecorar()));

        return convertView;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<ArbeitDetail> resultados = new ArrayList<>();
                if (origen == null)
                    origen = objetoArrayList;
                if(constraint != null){
                    if(origen != null & origen.size() >0) {
                        for(final ArbeitDetail g : origen) {
                            if(g.getGrab().getIdGrab().toLowerCase().contains(constraint.toString()))
                                resultados.add(g);
                        }
                        for(final ArbeitDetail g : origen) {
                            if(g.getGrab().getGrabname().toLowerCase().contains(constraint.toString()))
                                resultados.add(g);
                        }

                    }



                    oReturn.values = resultados;
                }

                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                objetoArrayList = (ArrayList<ArbeitDetail>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
