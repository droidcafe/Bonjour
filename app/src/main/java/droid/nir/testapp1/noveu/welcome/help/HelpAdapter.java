package droid.nir.testapp1.noveu.welcome.help;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import  droid.nir.testapp1.noveu.welcome.data.Help;

/**
 * Created by droidcafe on 12/9/2016.
 */

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.ViewHolder> {

    private List<String> titleList, despList;
    private int count;

    public HelpAdapter(HashMap helpMap) {

        titleList = new ArrayList<>(helpMap.size());
        despList = new ArrayList<>(helpMap.size());
        count = helpMap.size();

        Log.d("ha", "count map " + count);
        Set helpSet = helpMap.entrySet();
        Iterator iterator = helpSet.iterator();
        while (iterator.hasNext()) {
            Log.d("ha", "iterating in loop ");
            Map.Entry help = (Map.Entry) iterator.next();
            titleList.add((String) help.getKey());
            despList.add((String) help.getValue());

        }
    }

    public HelpAdapter(List<String> titleList, List<String> despList) {
        this.titleList = titleList;
        this.despList = despList;
    }

    public HelpAdapter(HashMap<String,Help> helpMap, String config) {
        titleList = new ArrayList<>(helpMap.size());
        despList = new ArrayList<>(helpMap.size());
        count = helpMap.size();

        String[] order = config.split(":");
        for (String key : order) {
            Help help = helpMap.get(key);
            titleList.add(help.title);
            despList.add(help.desp);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("ha", "create");
        View helpCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_help, parent, false);
        return new ViewHolder(helpCard);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("ha", "bind " + position);

        String title = titleList.get(position);
        String desp = despList.get(position);

        if (title == null || title.equals(""))
            holder.title.setVisibility(View.GONE);
        else {
            holder.title.setText(title);
            Import.settypefaces(holder.title.getContext(), "Raleway-Light.ttf", holder.title);
        }
        if (desp == null || desp.equals("")) holder.desp.setVisibility(View.GONE);
        else {
            holder.desp.setText(desp);
            Import.settypefaces(holder.desp.getContext(), "SourceSansPro-Regular.otf", holder.desp);
        }


    }

    @Override
    public int getItemCount() {
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, desp;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d("ha", "vh");
            title = (TextView) itemView.findViewById(R.id.help_title);
            desp = (TextView) itemView.findViewById(R.id.help_desp);
        }
    }
}
