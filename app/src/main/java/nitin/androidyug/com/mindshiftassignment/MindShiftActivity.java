package nitin.androidyug.com.mindshiftassignment;

import android.graphics.RectF;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MindShiftActivity extends SingleFragmentActivity {


    @Override
    public Fragment createFragment() {
        return new MindShiftFragment();
    }
}


