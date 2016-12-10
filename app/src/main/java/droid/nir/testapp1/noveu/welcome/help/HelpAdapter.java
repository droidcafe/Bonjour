package droid.nir.testapp1.noveu.welcome.help;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.welcome.data.Help;

/**
 * Created by droidcafe on 12/9/2016.
 */

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.ViewHolder> {

    private List<String> titleList, despList;
    private int count;

    public HelpAdapter(List<String> titleList, List<String> despList) {
        this.titleList = titleList;
        this.despList = despList;
    }

    public HelpAdapter(HashMap<String,Help> helpMap, String config) {
        titleList = new ArrayList<>(helpMap.size());
        despList = new ArrayList<>(helpMap.size());

        String[] order = config.split(":");
        for (String key : order) {
            Help help = helpMap.get(key);
            if(help == null)
                continue;
            titleList.add(help.title);
            despList.add(help.desp);
        }

        count = (titleList.size() < despList.size()) ? titleList.size() : despList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View helpCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_help, parent, false);
        return new ViewHolder(helpCard);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String title = titleList.get(position);
        String desp = despList.get(position);

        if (title == null || title.equals("") )
            holder.title.setVisibility(View.GONE);
        else {
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(title);
            Import.settypefaces(holder.title.getContext(), "Raleway-Light.ttf", holder.title);
        }
        if (desp == null || desp.equals("")) holder.desp.setVisibility(View.GONE);
        else {
            holder.desp.setVisibility(View.VISIBLE);
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
            title = (TextView) itemView.findViewById(R.id.help_title);
            desp = (TextView) itemView.findViewById(R.id.help_desp);
        }
    }
}
