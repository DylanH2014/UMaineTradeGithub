package Data;

import android.graphics.drawable.Drawable;

//import com.abhinav.dylan.umainetrade.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhinav on 1/28/16.
 */
public class Person {

    public String name;
    public String age;
    public int photoId;

    public Person(String name, String age, int photoId) {
        this.name = name;
        this.age = age;
        this.photoId = photoId;
    }


    private List<Person> persons;



}
