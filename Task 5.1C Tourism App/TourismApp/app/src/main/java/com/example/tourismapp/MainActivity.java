package com.example.tourismapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<Destination> destinationList = new ArrayList<>();
    ArrayList<Destination> topList = new ArrayList<>();

    // https://www.tripadvisor.com/blog/best-travel-attractions-most-popular/
    // Google Reviews
    // https://www.planetware.com/paris/eiffel-tower-f-p-et.htm
    Integer[] imageList = {
            R.drawable.wurundjeri,
            R.drawable.colosseum,
            R.drawable.stonehenge,
            R.drawable.eiffel_tower,
            R.drawable.machu_picchu,
            R.drawable.grand_canyon,
            R.drawable.sydney_opera_house,
            R.drawable.mount_kilimanjaro
    };
    Float[] rating = {4.6f, 4.7f, 4.5f, 4.6f, 4.8f, 4.8f, 4.7f, 4.4f};
    String[] titleList = {
            "Wurundjeri Spur Lookout",
            "Colosseum",
            "Stonehenge",
            "Eiffel Tower",
            "Machu Picchu",
            "Grand Canyon National Park",
            "Sydney Opera House",
            "Mount Kilimanjaro"
    };
    String[] locationList = {
            "Yarra Blvd, Kew VIC 3101",
            "Piazza del Colosseo, 00184 Rome Italy",
            "Wiltshire, England",
            "Champ de Mars, 75007 Paris, France",
            "08680, Peru",
            "Arizona, United States",
            "Bennelong Point, Sydney NSW 2000",
            "Tanzania, East Africa, Africa"

    };
    String[] description = {
            "The Wurundjeri Spur Lookout, also known as Trig Point, is at the Boulevard, one of the highest points in the Park, offering wonderful views to the CBD and to the nearer ranges. The main trail descends steeply to the Fairfield Pipe Bridge, opposite the Fairfield Boathouse.",
            "Perhaps the best-preserved of the monuments of ancient Rome, this huge marble structure was built to hold more than 50,000 spectators to witness bloody contests of might and the slaughter of wild beasts.",
            "One of the most important survivals of prehistoric England, Stonehenge consists of a group of huge rough-cut stones, some more than 20 feet high, arranged in two concentric circles.",
            "One of the most famous landmarks in the world, the Eiffel Tower (la Tour Eiffel) symbolizes Paris. Before arriving here, visitors have a vivid image of this monument as seen in a postcard, movie, or on a keychain, yet the Eiffel Tower still manages to amaze. This masterpiece of architectural achievement soars to a height of 324 meters. A feat of ingenuity, the structure of 18,000 iron parts is held together by 2.5 million rivets.",
            "Machu Picchu is an Incan citadel set high in the Andes Mountains in Peru, above the Urubamba River valley. Built in the 15th century and later abandoned, it’s renowned for its sophisticated dry-stone walls that fuse huge blocks without the use of mortar, intriguing buildings that play on astronomical alignments and panoramic views. Its exact former use remains a mystery.",
            "Grand Canyon National Park, in Arizona, is home to much of the immense Grand Canyon, with its layered bands of red rock revealing millions of years of geological history. Viewpoints include Mather Point, Yavapai Observation Station and architect Mary Colter’s Lookout Studio and her Desert View Watchtower. Lipan Point, with wide views of the canyon and Colorado River, is a popular, especially at sunrise and sunset.",
            "The Sydney Opera House is a multi-venue performing arts centre at Sydney Harbour located in Sydney, New South Wales, Australia. It is one of the 20th century's most famous and distinctive buildings.",
            "Mount Kilimanjaro is a dormant volcano in Tanzania. It has three volcanic cones: Kibo, Mawenzi, and Shira. It is the highest mountain in Africa and the highest single free-standing mountain in the world: 5,895 metres above sea level and about 4,900 metres above its plateau base."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateList();
        distributeList();
        loadMainFragment();
    }

    private void populateList() {
        for (int i = 0; i < imageList.length; i++) {
            destinationList.add(new Destination(i, imageList[i], rating[i], titleList[i], locationList[i], description[i]));
        }
    }

    private void distributeList() {
        // get 50% of the top rated destinations from the list
        int count = destinationList.size() * 50 / 100;
        List<Float> ratingList = new LinkedList<Float>(Arrays.asList(rating));
        for (int i = 0; i < count; i++) {
            int index = ratingList.indexOf(Collections.max(ratingList));
            ratingList.remove(index);
            topList.add(destinationList.get(index));
            destinationList.remove(index);
        }
    }

    private void loadMainFragment() {
        Fragment mainFragment = MainFragment.newInstance(destinationList, topList);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_placeholder, mainFragment);
        fragmentTransaction.commit();
    }

    private void loadDescriptionFragment(Destination destination) {
        Fragment descFragment = DescriptionFragment.newInstance(destination);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_placeholder, descFragment);
        fragmentTransaction.addToBackStack("description");
        fragmentTransaction.commit();

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        getSupportActionBar().setTitle(destination.getTitle());
    }

    public void showDescriptionFragment(Destination destination) {
        loadDescriptionFragment(destination);
    }

    public void showMainActionBar() {
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);   //show back button
        getSupportActionBar().setTitle(R.string.app_name);
    }

    // https://www.geeksforgeeks.org/how-to-add-and-customize-back-button-of-action-bar-in-android/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}