package at.ums.luna.friedhofums.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.modelo.Grab;

/**
 * Created by luna-aleixos on 09.08.2016.
 */
public class AdaptadorTumbas extends BaseAdapter implements Filterable {

    public Context context;
    public ArrayList<Grab> grabArrayList;
    public ArrayList<Grab> origen;

    public AdaptadorTumbas(Context context, ArrayList<Grab> grabArrayList){
        super();
        this.context = context;
        this.grabArrayList = grabArrayList;
    }

    public class GrabHolder{
        TextView tvidGrab;
        TextView tvGrabname;
        TextView tvTlf1;
    }


    @Override
    public int getCount() {
        return grabArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return grabArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GrabHolder holder;
        if(convertView == null){
            convertView= LayoutInflater.from(context).inflate(R.layout.fila_lista_tumbas, parent,false);
            holder = new GrabHolder();
            holder.tvidGrab = (TextView) convertView.findViewById(R.id.textViewIdGrab);
            holder.tvGrabname = (TextView) convertView.findViewById(R.id.textViewGrabname);
            holder.tvTlf1 = (TextView) convertView.findViewById(R.id.textViewTelefon1);
            convertView.setTag(holder);
        }
        else{
            holder=(GrabHolder)convertView.getTag();
        }
        holder.tvidGrab.setText(grabArrayList.get(position).getIdGrab());
        holder.tvGrabname.setText(grabArrayList.get(position).getGrabname());
        holder.tvTlf1.setText(grabArrayList.get(position).getTelefon1());

        return convertView;

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Grab> resultados = new ArrayList<Grab>();
                if (origen == null)
                    origen = grabArrayList;
                if(constraint != null){
                    if(origen != null & origen.size() >0) {
                        for(final Grab g : origen) {
                            if(g.getGrabname().toLowerCase().contains(constraint.toString()))
                                resultados.add(g);
                        }
                    }

                    oReturn.values = resultados;
                }

                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                grabArrayList = (ArrayList<Grab>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}

