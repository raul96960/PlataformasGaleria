package com.example.progaleria.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.progaleria.R;

import java.util.ArrayList;

//https://github.com/bumptech/glide
public class GaleriaAdapter extends RecyclerView.Adapter<GaleriaAdapter.GaleriaviewHolder> {
    private Context mContext;
    private ArrayList<FotoGaleria> listaFotosGaleria;


    public GaleriaAdapter(ArrayList<FotoGaleria> fotosGaleria) {
        this.listaFotosGaleria = fotosGaleria;
    }
    //lo pongo por si el parent de galeriaviewholder no funciona en el caso del contexto
    public GaleriaAdapter(Context mContext, ArrayList<FotoGaleria> fotosGaleria) {
        this.mContext = mContext;
        this.listaFotosGaleria = fotosGaleria;
    }

    @NonNull
    @Override
    public GaleriaviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflamos la vista
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_galeria,parent,false);
        return new GaleriaviewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull final GaleriaviewHolder holder, int position) {
        //holder.txtTitulo.setText("TITULO: "+listaFotosGaleria.get(position).getNombreFOTO());
        holder.txtLatitud.setText("LATITUD: "+listaFotosGaleria.get(position).getLatitud());
        holder.txtLongitud.setText("LONGITUD: "+listaFotosGaleria.get(position).getLongitud());
        //holder.imgFoto.setImageResource(R.drawable.ic_launcher_background);

         Glide.with(mContext)
                .load(listaFotosGaleria.get(position).getUrl())
                .centerCrop()
                .error(R.drawable.ic_launcher_background)
                .into(holder.imgFoto);

        //final GaleriaviewHolder mholder = holder;
        /*
        Glide.with(mContext)
                .load(listaFotosGaleria.get(position).getUrlIMG())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.mProgresbar.setVisibility(View.INVISIBLE);
                        holder.imgFoto.setVisibility(View.VISIBLE);
                        holder.imgFoto.setImageResource(R.drawable.ic_launcher_background);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.mProgresbar.setVisibility(View.GONE);
                        holder.imgFoto.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.imgFoto);
            */

    }

    @Override
    public int getItemCount() {
        return listaFotosGaleria.size();
    }


    /*aqui implementaremos todo lo que tenga que ver con el layout row_galeria y tambien
     *debemos ponerle un implement click listener para poder usar el boton
     */
    public class GaleriaviewHolder extends RecyclerView.ViewHolder{
        public ImageView imgFoto;
        //public TextView txtTitulo;
        public TextView txtLatitud;
        public TextView txtLongitud;
        public ProgressBar mProgresbar;

        public GaleriaviewHolder(@NonNull View itemView) {
            super(itemView);
            //this.txtTitulo = itemView.findViewById(R.id.txt_titulo);
            this.txtLatitud = itemView.findViewById(R.id.txt_latitud);
            this.txtLongitud = itemView.findViewById(R.id.txt_longitud);
            this.imgFoto = itemView.findViewById(R.id.img_foto);
            //this.mProgresbar = itemView.findViewById(R.id.progress);
        }

    }
}
