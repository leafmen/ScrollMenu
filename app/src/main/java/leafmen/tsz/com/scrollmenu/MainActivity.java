package leafmen.tsz.com.scrollmenu;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;


public class MainActivity extends FragmentActivity {

    private MainUI mainUI;
    private LeftMenu leftMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainUI = new MainUI(this);
        setContentView(mainUI);
        leftMenu = new LeftMenu();
        getFragmentManager().beginTransaction()
                .add(MainUI.LEFT_MENU_ID, leftMenu)
                .commit();


    }

}
