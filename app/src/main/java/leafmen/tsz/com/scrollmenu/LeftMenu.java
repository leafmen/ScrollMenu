package leafmen.tsz.com.scrollmenu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by LiQiang on 2015/12/23.
 */
public class LeftMenu extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_leftmenu, container, false);

        view.findViewById(R.id.btnToast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("你是一个好人");
            }
        });
        return view;


    }
}
