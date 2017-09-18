package pro.rudloff.openvegemap;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;

public class SplashActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
