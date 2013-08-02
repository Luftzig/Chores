package il.ac.huji.chores;

import android.content.Context;

import com.parse.Parse;

public class AppSetup {

    private static AppSetup instance;
    private Context _ctx;

    private AppSetup(Context ctx) {
        _ctx = ctx;
        Parse.initialize(_ctx, _ctx.getResources().getString(R.string.parse_app_id), _ctx.getResources().getString(R.string.parse_client_key));
        //  PushService.subscribe(_ctx, "", AppSetup.class);
        //  PushService.setDefaultPushCallback(_ctx, AppSetup.class);
    }

    public static AppSetup getInstance(Context ctx) {
        if (instance == null) {
            instance = new AppSetup(ctx);
        }
        return instance;
    }
}
