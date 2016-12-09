package droid.nir.testapp1.noveu.welcome.help;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.Import;

/**
 * Created by droidcafe on 12/9/2016.
 */

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.ViewHolder> {

    HashMap<String,String> helpMap;
    public HelpAdapter(HashMap helpMap){
        this.helpMap = helpMap;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View helpCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_help,parent,false);
        return new ViewHolder(helpCard);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Set helpSet = helpMap.entrySet();
        Iterator iterator= helpSet.iterator();
        while (iterator.hasNext()) {
            Map.Entry help = (Map.Entry) iterator.next();
            holder.title.setText((String)help.getKey());
            holder.desp.setText((String)help.getValue());

            Import.settypefaces(holder.title.getContext(), "Raleway-Light.ttf", holder.title);
            Import.settypefaces(holder.desp.getContext(), "SourceSansPro-Regular.otf", holder.desp);
        }
    }

    @Override
    public int getItemCount() {
        return helpMap.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,desp
        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.help_title);
            title = (TextView) itemView.findViewById(R.id.help_desp);
        }
    }
}
