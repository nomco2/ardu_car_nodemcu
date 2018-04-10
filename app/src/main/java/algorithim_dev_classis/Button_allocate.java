package algorithim_dev_classis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kimfamily.arduino_car_nodemcu.R;

import java.util.ArrayList;

import database.adapter.CustomAdapter;
import database.conf.Constants;
import database.data.InfoClass;
import database.database.DbOpenHelper;
import database.database.Project_button_list_DB;
import database.util.DLog;
import movable_classis.Movable_Layout_Class;
import movable_classis.Movable_Layout_Class_auto_lineup;

/**
 * Created by KimFamily on 2018-02-05.
 */

public class Button_allocate extends Activity{

    int project_list_num;
    private Cursor mCursor;
    private DbOpenHelper mDbOpenHelper;
    private InfoClass mInfoClass;
    private ArrayList<InfoClass> mInfoArray;

    private TextView project_title;

    private RelativeLayout button_allocate_main_layout;
    private ImageButton button_creation;
    private CheckBox button_editing;

    private int button_ids = 30000;


    DisplayMetrics dm;
    int display_width;
    int display_height;
    int screenSizeType;
    int resourceId;

    float text_size;
    int buttons_height;

    float line_size;


    /* 프로젝트 버튼 리스트 sharedpreference 관련 */
    SharedPreferences sharedPreferences_savaer;
    SharedPreferences.Editor sharedPreferences_editor;
    private ViewGroup mframe;
    private float loaction_x;
    private float loaction_y;

    private ViewGroup button_name_box;
    private EditText button_name_edit_text;
    private Button button_name_confirm;
    private Button button_name_cancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_allocate);
        Intent intent = getIntent();

        project_list_num = intent.getExtras().getInt("project_list_num");
//        Toast.makeText(this, project_list_num + "", Toast.LENGTH_LONG).show();

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mCursor = null;
        mCursor = mDbOpenHelper.getAllColumns();
        DLog.e("load list", "COUNT = " + mCursor.getCount());

        project_title = (TextView) findViewById(R.id.project_title);

        int while_count = 0;
        while (mCursor.moveToNext()) {


            while_count++;
            if (while_count == project_list_num) {
                project_title.setText(mCursor.getString(mCursor.getColumnIndex("name")));
            }
        }

        mCursor.close();


        dm = getApplicationContext().getResources().getDisplayMetrics();
        display_width = dm.widthPixels;
        display_height = dm.heightPixels;
        screenSizeType = (getApplicationContext().getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK);
        resourceId = getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");

        line_size = convertPixelsToDp(display_height / 6, getApplicationContext());
        Toast.makeText(getApplicationContext(),display_height +"",Toast.LENGTH_LONG).show();


        if(display_height < 1000){ //HD

            buttons_height = (int) convertPixelsToDp(display_height, getApplicationContext()) / 6;
            text_size =  (int) (buttons_height/1.2);

        }else if(display_height < 1400){ //FHD
            buttons_height = (int) convertPixelsToDp(display_height, getApplicationContext()) / 4;
            text_size = (int) (buttons_height/1.7);

        }else if(display_height < 2000){ //QHD
            buttons_height = (int) convertPixelsToDp(display_height, getApplicationContext()) / 3;
            text_size = (int) (buttons_height/2.3);

        }else{ //UHD
            buttons_height = (int) convertPixelsToDp(display_height, getApplicationContext()) / 2;
            text_size = (int) (buttons_height/2.6);
        }




        sharedPreferences_savaer = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences_editor = sharedPreferences_savaer.edit();




        button_name_box = (ViewGroup) findViewById(R.id.button_name_box);
        button_name_edit_text = (EditText) findViewById(R.id.button_name_edit_text);
        button_name_confirm = (Button)  findViewById(R.id.button_name_confirm);
        button_name_cancel = (Button)  findViewById(R.id.button_name_cancel);

        button_allocate_main_layout = (RelativeLayout) findViewById(R.id.button_allocate_main_layout);

        button_creation = (ImageButton) findViewById(R.id.button_creation);
        button_creation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_ids += 100;

                button_name_box.setVisibility(View.VISIBLE);
                button_name_edit_text.setText("");
                button_name_edit_text.requestFocus();


            }
        });

        //버튼이름 넣고 확인 버튼 누를때
        button_name_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String button_name_text = button_name_edit_text.getText().toString();
                ViewGroup frame = button_creating_method2(button_ids, button_name_text ,display_width/2, display_height/2, true);

                StringBuilder last_id_number = new StringBuilder(project_title.getText());
                last_id_number.append(button_ids);

                StringBuilder button_name = new StringBuilder(project_title.getText());
                button_name.append(button_ids);
                button_name.append("button_name");


                StringBuilder location_x_string = new StringBuilder(project_title.getText());
                location_x_string.append(button_ids);
                location_x_string.append("x");

                StringBuilder location_y_string = new StringBuilder(project_title.getText());
                location_y_string.append(button_ids);
                location_y_string.append("y");

                sharedPreferences_editor.putInt(last_id_number.toString(), button_ids);
                sharedPreferences_editor.putString(button_name.toString(), button_name_text);

                sharedPreferences_editor.putFloat(location_x_string.toString(), frame.getX());
                sharedPreferences_editor.putFloat(location_y_string.toString(), frame.getY());

                button_name_box.setVisibility(View.INVISIBLE);


            }
        });

        button_name_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button_name_box.setVisibility(View.INVISIBLE);

            }
        });


        button_editing = (CheckBox) findViewById(R.id.button_editing);
        button_editing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                visible_or_not();


            }
        });


        visible_or_not();





    }//oncreate 끝











    private RelativeLayout button_creating_method2(int id_numbers, final String button_name, int location_x, int location_y, Boolean moving_hold_permanently){

        final int this_layout_id_number = id_numbers;

        LinearLayout frame_linear = new LinearLayout((getApplicationContext()));
        frame_linear.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout new_linear = new RelativeLayout(getApplicationContext());


        float text_length;

        if(display_height < 1000){ //HD
            text_length = (int) (button_name.length()*buttons_height*1.5);
//            new_linear.setPadding((int)(text_length/8),0,0,0);

        }else if(display_height < 1400){ //FHD
            text_length = (int) (button_name.length()*buttons_height*1.5);
//            new_linear.setPadding((int)(text_length/6),(button_name.length()/2),0,0);

        }else if(display_height < 2000){ //QHD
            text_length = (int) (button_name.length()*buttons_height*1.3);
//            new_linear.setPadding((int)(text_length/6),(button_name.length()/4),0,0);

        }else{ //UHD
            text_length = (int) (button_name.length()*buttons_height*1.7);
//            new_linear.setPadding((int)(text_length/6),0,0,0);

        }


        new_linear.setGravity(Gravity.CENTER_VERTICAL);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) text_length, buttons_height*2);
        new_linear.setLayoutParams(params);
        new_linear.setId(id_numbers);





        //imageview 버튼 수정 체크박스 아닐때 온클릭 리스너 동작
        ImageView new_buttons = new ImageView(getApplicationContext());
//        new_buttons.setScaleType(ImageView.ScaleType.FIT_XY);
        new_buttons.setId(this_layout_id_number+10000);
        new_buttons.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        new_buttons.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_editing_check));
        new_buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Algorithm_dev_activity.class);
                intent.putExtra("project_list_num", button_name);
                startActivity(intent);
            }
        });



        ImageView new_buttons2 = new ImageView(getApplicationContext());
        new_buttons2.setId(this_layout_id_number+20000);
        new_buttons2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        new_buttons2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_editing_uncheck));

        new_buttons2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send wifi telnet


            }
        });


        TextView new_texts = new TextView(getApplicationContext());
        new_texts.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_allocate_back_rectangle));
        new_texts.setTextColor(Color.rgb(0,0,0));
        new_texts.setTextSize(1,text_size);
        new_texts.setText(button_name);
        new_texts.setId(id_numbers+30000);




        ImageView move_arrow = new ImageView(getApplicationContext());
        move_arrow.setId(this_layout_id_number+40000);
        move_arrow.setLayoutParams(new ViewGroup.LayoutParams(buttons_height*2,buttons_height*2));
        move_arrow.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.move_arrow));
        move_arrow.bringToFront() ;

        ImageView move_arrow2 = new ImageView(getApplicationContext());
        move_arrow2.setId(this_layout_id_number+50000);
        move_arrow2.setLayoutParams(new ViewGroup.LayoutParams(buttons_height*2,buttons_height*2));
        move_arrow2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.move_arrow));
        move_arrow2.bringToFront() ;





        String[] new_buttons_location = new String[2];
        new_buttons_location[0] = "new_button_x" + this_layout_id_number;
        new_buttons_location[1] = "new_button_y" + this_layout_id_number;
        String scale_size = "scale_size" + this_layout_id_number;
        Movable_Layout_Class new_movable_button =
                new Movable_Layout_Class(this, button_allocate_main_layout, frame_linear, new_buttons_location, scale_size, moving_hold_permanently);

        frame_linear.addView(move_arrow);
        frame_linear.addView(new_linear);
        frame_linear.addView(move_arrow2);
        new_linear.addView(new_buttons);
        new_linear.addView(new_buttons2);
        new_linear.addView(new_texts);

        button_allocate_main_layout.addView(frame_linear);
        frame_linear.setX(location_x);
        frame_linear.setY(location_y);




        visible_or_not();



        return new_linear;
    }
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    private void visible_or_not(){
        if (button_editing.isChecked()) {
            button_creation.setVisibility(View.VISIBLE);
        } else {
            button_creation.setVisibility(View.INVISIBLE);

        }

        for(int i =30000; i <= button_ids; i += 100){
            ImageView new_buttons2 = findViewById(i+20000);
            ImageView move_arrow = findViewById(i+40000);
            ImageView move_arrow2 = findViewById(i+50000);
            button_name_box.setVisibility(View.INVISIBLE);

            try {
                if (button_editing.isChecked()) {
                    new_buttons2.setVisibility(View.INVISIBLE);
                    move_arrow.setVisibility(View.VISIBLE);
                    move_arrow2.setVisibility(View.VISIBLE);

                } else {
                    new_buttons2.setVisibility(View.VISIBLE);
                    move_arrow.setVisibility(View.INVISIBLE);
                    move_arrow2.setVisibility(View.INVISIBLE);

                }



            }catch (Exception e){

            }
        }


    }


}
